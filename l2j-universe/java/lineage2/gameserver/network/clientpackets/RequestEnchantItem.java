/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.clientpackets;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.EnchantScrollInfo;
import lineage2.gameserver.model.items.etcitems.EnchantScrollManager;
import lineage2.gameserver.model.items.etcitems.EnchantScrollType;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.network.serverpackets.EnchantResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestEnchantItem extends AbstractEnchantPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _catalystObjId.
	 */
	private int _catalystObjId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	public void readImpl()
	{
		_objectId = readD();
		_catalystObjId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (!isValidPlayer(player))
		{
			player.setEnchantScroll(null);
			player.sendPacket(EnchantResult.CANCEL);
			player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
			player.sendActionFailed();
		}
		PcInventory inventory = player.getInventory();
		inventory.writeLock();
		try
		{
			ItemInstance item = inventory.getItemByObjectId(_objectId);
			ItemInstance scroll = player.getEnchantScroll();
			ItemInstance catalyst = _catalystObjId > 0 ? inventory.getItemByObjectId(_catalystObjId) : null;
			if (!ItemFunctions.checkCatalyst(item, catalyst))
			{
				catalyst = null;
			}
			if ((item == null) || (scroll == null))
			{
				player.sendActionFailed();
				return;
			}
			EnchantScrollInfo esi = EnchantScrollManager.getScrollInfo(scroll.getItemId());
			if (esi == null)
			{
				player.sendActionFailed();
				return;
			}
			if ((item.getEnchantLevel() >= esi.getMax()) || (item.getEnchantLevel() < esi.getMin()))
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
				player.sendActionFailed();
				return;
			}
			if (esi.getType() != EnchantScrollType.SPECIAL)
			{
				if (!checkItem(item, esi))
				{
					player.sendPacket(EnchantResult.CANCEL);
					player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
					player.sendActionFailed();
					return;
				}
			}
			if ((!inventory.destroyItem(scroll, 1)) || ((catalyst != null) && (!inventory.destroyItem(catalyst, 1))))
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendActionFailed();
				return;
			}
			boolean equipped = item.isEquipped();
			if (equipped)
			{
				inventory.isRefresh = true;
				inventory.unEquipItem(item);
			}
			int safeEnchantLevel = item.getTemplate().getBodyPart() == 32768 ? (esi.getSafe() + 1) : esi.getSafe();
			int chance = esi.getChance();
			if (catalyst != null)
			{
				chance += ItemFunctions.getCatalystPower(catalyst.getItemId());
			}
			if ((esi.getType() == EnchantScrollType.ANCIENT) || (esi.getType() == EnchantScrollType.ITEM_MALL))
			{
				chance += 10;
			}
			if (esi.getType() == EnchantScrollType.DIVINE)
			{
				chance = 100;
			}
			if (item.getEnchantLevel() <= safeEnchantLevel)
			{
				chance = 100;
			}
			chance = Math.min(chance, 100);

			if (item.isArmor())
				WorldStatisticsManager.getInstance().updateStat(player, CategoryType.ARMOR_ENCHANT_TRY, item.getCrystalType().ordinal(), item.getEnchantLevel() + 1);
			else if (item.isWeapon())
				WorldStatisticsManager.getInstance().updateStat(player, CategoryType.WEAPON_ENCHANT_TRY, item.getCrystalType().ordinal(), item.getEnchantLevel() + 1);
			
			if (Rnd.chance(chance))
			{
				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();
				if (equipped)
				{
					inventory.equipItem(item);
					inventory.isRefresh = false;
				}
				player.sendPacket(new InventoryUpdate().addModifiedItem(item));

				if (item.isArmor())
					WorldStatisticsManager.getInstance().updateStat(player, CategoryType.ARMOR_ENCHANT_MAX, item.getCrystalType().ordinal(), item.getEnchantLevel());

				if (item.isWeapon())

					WorldStatisticsManager.getInstance().updateStat(player, CategoryType.WEAPON_ENCHANT_MAX, item.getCrystalType().ordinal(), item.getEnchantLevel());

				player.sendPacket(new EnchantResult(0, 0, 0L, item.getEnchantLevel()));

				if (Config.SHOW_ENCHANT_EFFECT_RESULT)
				{
					player.broadcastPacket(new L2GameServerPacket[] { new SystemMessage(3013).addName(player).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()) });
					player.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(player, player, 5965, 1, 500, 1500L) });
				}
			}
			else
			{
				switch (esi.getType())
				{
					case NORMAL:
						if (item.isEquipped())
						{
							player.sendDisarmMessage(item);
						}
						if (!inventory.destroyItem(item, 1L))
						{
							player.sendActionFailed();
							return;
						}
						int crystalId = item.getCrystalType().cry;
						if ((crystalId > 0) && (item.getTemplate().getCrystalCount() > 0))
						{
							int crystalAmount = (int) (item.getTemplate().getCrystalCount() * 0.87D);
							if (item.getEnchantLevel() > 3)
							{
								crystalAmount = (int) (crystalAmount + (item.getTemplate().getCrystalCount() * 0.25D * (item.getEnchantLevel() - 3)));
							}
							if (crystalAmount < 1)
							{
								crystalAmount = 1;
							}
							player.sendPacket(new EnchantResult(1, crystalId, crystalAmount));
							ItemFunctions.addItem(player, crystalId, crystalAmount, true);
						}
						else
						{
							player.sendPacket(EnchantResult.FAILED_NO_CRYSTALS);
						}
						break;
					case DESTRUCTION:
						item.setEnchantLevel(item.getEnchantLevel());
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
						if (equipped)
						{
							inventory.equipItem(item);
							inventory.isRefresh = false;
						}
						player.sendPacket(new InventoryUpdate().addModifiedItem(item));
						player.sendPacket(SystemMsg.THE_BLESSED_ENCHANT_FAILED);
						player.sendPacket(EnchantResult.ANCIENT_FAILED);
						break;
					case BLESSED:
					case ITEM_MALL:
					case CRYSTALL:
						item.setEnchantLevel(0);
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
						if (equipped)
						{
							inventory.equipItem(item);
							inventory.isRefresh = false;
						}
						player.sendPacket(new InventoryUpdate().addModifiedItem(item));
						player.sendPacket(SystemMsg.THE_BLESSED_ENCHANT_FAILED);
						player.sendPacket(EnchantResult.BLESSED_FAILED);
						break;
					case ANCIENT:
						player.sendPacket(EnchantResult.ANCIENT_FAILED);
						break;
					default:
						break;
				}
			}
		}
		finally
		{
			inventory.writeUnlock();
			player.setEnchantScroll(null);
			player.updateStats();
		}
	}
}

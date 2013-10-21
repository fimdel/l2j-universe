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
package handler.items;

import gnu.trove.set.hash.TIntHashSet;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EquipableItem extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private final int[] _itemIds;
	
	/**
	 * Constructor for EquipableItem.
	 */
	public EquipableItem()
	{
		final TIntHashSet set = new TIntHashSet();
		for (ItemTemplate template : ItemHolder.getInstance().getAllTemplates())
		{
			if (template == null)
			{
				continue;
			}
			if (template.isEquipable())
			{
				set.add(template.getItemId());
			}
		}
		_itemIds = set.toArray();
	}
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (!playable.isPlayer())
		{
			return false;
		}
		final Player player = playable.getPlayer();
		if (player.isCastingNow())
		{
			player.sendPacket(Msg.YOU_MAY_NOT_EQUIP_ITEMS_WHILE_CASTING_OR_PERFORMING_A_SKILL);
			return false;
		}
		if (player.isStunned() || player.isSleeping() || player.isParalyzed() || player.isAlikeDead() || player.isWeaponEquipBlocked() || player.isAirBinded() || player.isKnockedBack() || player.isKnockedDown() || player.isPulledNow())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		final int bodyPart = item.getBodyPart();
		if ((bodyPart == ItemTemplate.SLOT_LR_HAND) || (bodyPart == ItemTemplate.SLOT_L_HAND) || (bodyPart == ItemTemplate.SLOT_R_HAND))
		{
			if (player.isMounted() || player.isCursedWeaponEquipped() || (player.getActiveWeaponFlagAttachment() != null) || player.isClanAirShipDriver())
			{
				player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
				return false;
			}
		}
		if (item.isCursed())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		if (player.isInOlympiadMode() && item.isHeroWeapon())
		{
			player.sendActionFailed();
			return false;
		}
		if (item.isEquipped())
		{
			final ItemInstance weapon = player.getActiveWeaponInstance();
			if (item.equals(weapon))
			{
				player.abortAttack(true, true);
				player.abortCast(true, true);
			}
			player.sendDisarmMessage(item);
			player.getInventory().unEquipItem(item);
			return false;
		}
		final L2GameServerPacket p = ItemFunctions.checkIfCanEquip(player, item);
		if (p != null)
		{
			player.sendPacket(p);
			return false;
		}
		player.getInventory().equipItem(item);
		if (!item.isEquipped())
		{
			player.sendActionFailed();
			return false;
		}
		SystemMessage sm;
		if (item.getEnchantLevel() > 0)
		{
			sm = new SystemMessage(SystemMessage.EQUIPPED__S1_S2);
			sm.addNumber(item.getEnchantLevel());
			sm.addItemName(item.getItemId());
		}
		else
		{
			sm = new SystemMessage(SystemMessage.YOU_HAVE_EQUIPPED_YOUR_S1).addItemName(item.getItemId());
		}
		player.sendPacket(sm);
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}

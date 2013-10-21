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
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExVariationCancelResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.ShortCutRegister;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestRefineCancel extends L2GameClientPacket
{
	/**
	 * Field _targetItemObjId.
	 */
	private int _targetItemObjId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendPacket(new ExVariationCancelResult(0));
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(new ExVariationCancelResult(0));
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(new ExVariationCancelResult(0));
			return;
		}
		ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		if ((targetItem == null) || !targetItem.isAugmented())
		{
			activeChar.sendPacket(new ExVariationCancelResult(0), Msg.AUGMENTATION_REMOVAL_CAN_ONLY_BE_DONE_ON_AN_AUGMENTED_ITEM);
			return;
		}
		int price = getRemovalPrice(targetItem.getTemplate());
		if (price < 0)
		{
			activeChar.sendPacket(new ExVariationCancelResult(0));
		}
		if (!activeChar.reduceAdena(price, true))
		{
			activeChar.sendPacket(new ExVariationCancelResult(0), Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		boolean equipped = targetItem.isEquipped();
		if (equipped)
		{
			activeChar.getInventory().unEquipItem(targetItem);
		}
		targetItem.setAugmentationId(0);
		targetItem.setJdbcState(JdbcEntityState.UPDATED);
		targetItem.update();
		if (equipped)
		{
			activeChar.getInventory().equipItem(targetItem);
		}
		InventoryUpdate iu = new InventoryUpdate().addModifiedItem(targetItem);
		SystemMessage sm = new SystemMessage(SystemMessage.AUGMENTATION_HAS_BEEN_SUCCESSFULLY_REMOVED_FROM_YOUR_S1);
		sm.addItemName(targetItem.getItemId());
		activeChar.sendPacket(new ExVariationCancelResult(1), iu, sm);
		for (ShortCut sc : activeChar.getAllShortCuts())
		{
			if ((sc.getId() == targetItem.getObjectId()) && (sc.getType() == ShortCut.TYPE_ITEM))
			{
				activeChar.sendPacket(new ShortCutRegister(activeChar, sc));
			}
		}
		activeChar.sendChanges();
	}
	
	/**
	 * Method getRemovalPrice.
	 * @param item ItemTemplate
	 * @return int
	 */
	public static int getRemovalPrice(ItemTemplate item)
	{
		switch (item.getItemGrade().cry)
		{
			case ItemTemplate.CRYSTAL_C:
				if (item.getCrystalCount() < 1720)
				{
					return 95000;
				}
				else if (item.getCrystalCount() < 2452)
				{
					return 150000;
				}
				else
				{
					return 210000;
				}
			case ItemTemplate.CRYSTAL_B:
				if (item.getCrystalCount() < 1746)
				{
					return 240000;
				}
				return 270000;
			case ItemTemplate.CRYSTAL_A:
				if (item.getCrystalCount() < 2160)
				{
					return 330000;
				}
				else if (item.getCrystalCount() < 2824)
				{
					return 390000;
				}
				else
				{
					return 420000;
				}
			case ItemTemplate.CRYSTAL_S:
				if (item.getCrystalCount() == 10394)
				{
					return 920000;
				}
				else if (item.getCrystalCount() == 7050)
				{
					return 720000;
				}
				else if (item.getName().contains("Vesper"))
				{
					return 920000;
				}
				else
				{
					return 480000;
				}
			case ItemTemplate.CRYSTAL_R:
				if (item.getItemGrade() == Grade.R)
				{
					return 1530000;
				}
				else if (item.getItemGrade() == Grade.R95)
				{
					return 5400000;
				}
				else if (item.getItemGrade() == Grade.R99)
				{
					return 14160000;
				}
				return 1530000;
			default:
				return -1;
		}
	}
}

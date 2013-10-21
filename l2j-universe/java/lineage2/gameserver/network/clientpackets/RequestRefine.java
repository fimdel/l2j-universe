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
import lineage2.gameserver.model.items.etcitems.LifeStoneInfo;
import lineage2.gameserver.model.items.etcitems.LifeStoneManager;
import lineage2.gameserver.network.serverpackets.ExVariationResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.ShortCutRegister;
import lineage2.gameserver.tables.AugmentationData;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestRefine extends AbstractRefinePacket
{
	/**
	 * Field _gemstoneItemObjId. Field _refinerItemObjId. Field _targetItemObjId.
	 */
	private int _targetItemObjId, _refinerItemObjId, _gemstoneItemObjId;
	/**
	 * Field _gemstoneCount.
	 */
	private long _gemstoneCount;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
		_gemstoneItemObjId = readD();
		_gemstoneCount = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_gemstoneCount < 1))
		{
			return;
		}
		ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);
		ItemInstance gemstoneItem = activeChar.getInventory().getItemByObjectId(_gemstoneItemObjId);
		if ((targetItem == null) || (refinerItem == null) || (gemstoneItem == null))
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		LifeStoneInfo lsi = LifeStoneManager.getStoneInfo(refinerItem.getItemId());
		if (lsi == null)
		{
			return;
		}
		if (!isValid(activeChar, targetItem, refinerItem, gemstoneItem))
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0), Msg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		if (TryAugmentItem(activeChar, targetItem, lsi))
		{
			int stat12 = 0x0000FFFF & targetItem.getAugmentationId();
			int stat34 = targetItem.getAugmentationId() >> 16;
			activeChar.sendPacket(new ExVariationResult(stat12, stat34, 1), Msg.THE_ITEM_WAS_SUCCESSFULLY_AUGMENTED);
		}
		else
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0), Msg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
		}
	}
	
	/**
	 * Method TryAugmentItem.
	 * @param player Player
	 * @param targetItem ItemInstance
	 * @param lsi LifeStoneInfo
	 * @return boolean
	 */
	boolean TryAugmentItem(Player player, ItemInstance targetItem, LifeStoneInfo lsi)
	{
		if (!player.getInventory().destroyItemByObjectId(_gemstoneItemObjId, _gemstoneCount))
		{
			return false;
		}
		if (!player.getInventory().destroyItemByObjectId(_refinerItemObjId, 1L))
		{
			return false;
		}
		int augmentation = AugmentationData.getInstance().generateRandomAugmentation(lsi.getLevel(), lsi.getGrade(), targetItem.getTemplate().getBodyPart());
		boolean equipped = targetItem.isEquipped();
		if (equipped)
		{
			player.getInventory().unEquipItem(targetItem);
		}
		targetItem.setAugmentationId(augmentation);
		targetItem.setJdbcState(JdbcEntityState.UPDATED);
		targetItem.update();
		if (equipped)
		{
			player.getInventory().equipItem(targetItem);
		}
		player.sendPacket(new InventoryUpdate().addModifiedItem(targetItem));
		for (ShortCut sc : player.getAllShortCuts())
		{
			if ((sc.getId() == targetItem.getObjectId()) && (sc.getType() == ShortCut.TYPE_ITEM))
			{
				player.sendPacket(new ShortCutRegister(player, sc));
			}
		}
		player.sendChanges();
		return true;
	}
}

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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.etcitems.LifeStoneInfo;
import lineage2.gameserver.model.items.etcitems.LifeStoneManager;
import lineage2.gameserver.network.serverpackets.ExPutCommissionResultForVariationMake;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestConfirmGemStone extends AbstractRefinePacket
{
	/**
	 * Field _targetItemObjId.
	 */
	private int _targetItemObjId;
	/**
	 * Field _refinerItemObjId.
	 */
	private int _refinerItemObjId;
	/**
	 * Field _gemstoneItemObjId.
	 */
	private int _gemstoneItemObjId;
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
		if (_gemstoneCount <= 0)
		{
			return;
		}
		Player activeChar = getClient().getActiveChar();
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
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		if (_gemstoneCount != getGemStoneCount(lsi.getGrade(), targetItem.getTemplate().getItemGrade()))
		{
			activeChar.sendPacket(Msg.GEMSTONE_QUANTITY_IS_INCORRECT);
			return;
		}
		activeChar.sendPacket(new ExPutCommissionResultForVariationMake(_gemstoneItemObjId, _gemstoneCount), Msg.PRESS_THE_AUGMENT_BUTTON_TO_BEGIN);
	}
}

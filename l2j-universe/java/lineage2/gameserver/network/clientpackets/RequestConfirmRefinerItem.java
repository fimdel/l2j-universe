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
import lineage2.gameserver.network.serverpackets.ExPutIntensiveResultForVariationMake;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestConfirmRefinerItem extends AbstractRefinePacket
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
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
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
		ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);
		if ((targetItem == null) || (refinerItem == null))
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		LifeStoneInfo lsi = LifeStoneManager.getStoneInfo(refinerItem.getItemId());
		if (lsi == null)
		{
			return;
		}
		if (!isValid(activeChar, targetItem, refinerItem))
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		final int refinerItemId = refinerItem.getItemId();
		final Grade grade = targetItem.getTemplate().getItemGrade();
		final int gemStoneId = getGemStoneId(grade);
		final int gemStoneCount = getGemStoneCount(lsi.getGrade(), grade);
		SystemMessage sm = new SystemMessage(SystemMessage.REQUIRES_S1_S2).addNumber(gemStoneCount).addItemName(gemStoneId);
		activeChar.sendPacket(new ExPutIntensiveResultForVariationMake(_refinerItemObjId, refinerItemId, gemStoneId, gemStoneCount), sm);
	}
}

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

import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.Player;

import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConfirmDlg extends L2GameClientPacket
{
	/**
	 * Field _requestId. Field _answer.
	 */
	private int _answer, _requestId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		readD();
		_answer = readD();
		_requestId = readD();
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
		Pair<Integer, OnAnswerListener> entry = activeChar.getAskListener(true);
		if ((entry == null) || (entry.getKey() != _requestId))
		{
			return;
		}
		OnAnswerListener listener = entry.getValue();
		if (_answer == 1)
		{
			listener.sayYes();
		}
		else
		{
			listener.sayNo();
		}
	}
}

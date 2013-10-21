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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.network.serverpackets.ExMentorList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Mentoring;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConfirmMenteeAdd extends L2GameClientPacket
{
	/**
	 * Field _answer.
	 */
	private int _answer;
	/**
	 * Field _mentorName.
	 */
	@SuppressWarnings("unused")
	private String _mentorName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_answer = readD();
		_mentorName = readS();
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
		Request request = activeChar.getRequest();
		if ((request == null) || !request.isTypeOf(Request.L2RequestType.MENTEE))
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!request.isInProgress())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isOutOfControl())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		Player requestor = request.getRequestor();
		if (requestor == null)
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
			activeChar.sendActionFailed();
			return;
		}
		if (requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (_answer == 0)
		{
			request.cancel();
			requestor.sendPacket(new SystemMessage2(SystemMsg.S1_HAS_DECLINED_BECOMING_YOUR_MENTEE).addString(activeChar.getName()));
			return;
		}
		if (requestor.isActionsDisabled())
		{
			request.cancel();
			activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ON_ANOTHER_TASK).addString(requestor.getName()));
			activeChar.sendActionFailed();
			return;
		}
		try
		{
			requestor.getMenteeMentorList().addMentee(activeChar);
			activeChar.getMenteeMentorList().addMentor(requestor);
			activeChar.sendPacket(new SystemMessage2(SystemMsg.FROM_NOW_ON_S1_WILL_BE_YOUR_MENTOR).addName(requestor), new ExMentorList(activeChar));
			requestor.sendPacket(new SystemMessage2(SystemMsg.FROM_NOW_ON_S1_WILL_BE_YOUR_MENTEE).addName(activeChar), new ExMentorList(requestor));
			Mentoring.applyMenteeBuffs(activeChar);
			Mentoring.applyMentorBuffs(requestor);
		}
		finally
		{
			request.done();
		}
	}
}

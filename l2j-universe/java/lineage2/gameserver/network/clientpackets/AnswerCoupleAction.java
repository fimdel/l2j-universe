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

import lineage2.gameserver.ai.PlayableAI;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AnswerCoupleAction extends L2GameClientPacket
{
	/**
	 * Field _charObjId.
	 */
	private int _charObjId;
	/**
	 * Field _actionId.
	 */
	private int _actionId;
	/**
	 * Field _answer.
	 */
	private int _answer;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_actionId = readD();
		_answer = readD();
		_charObjId = readD();
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
		if ((request == null) || !request.isTypeOf(L2RequestType.COUPLE_ACTION))
		{
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
		if ((requestor.getObjectId() != _charObjId) || (requestor.getRequest() != request))
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		switch (_answer)
		{
			case -1:
				requestor.sendPacket(new SystemMessage2(SystemMsg.C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS_AND_CANNOT_BE_REQUESTED_FOR_A_COUPLE_ACTION).addName(activeChar));
				request.cancel();
				break;
			case 0:
				activeChar.sendPacket(SystemMsg.THE_COUPLE_ACTION_WAS_DENIED);
				requestor.sendPacket(SystemMsg.THE_COUPLE_ACTION_WAS_CANCELLED);
				requestor.sendActionFailed();
				request.cancel();
				break;
			case 1:
				try
				{
					if (!checkCondition(activeChar, requestor) || !checkCondition(requestor, activeChar))
					{
						return;
					}
					Location loc = requestor.applyOffset(activeChar.getLoc(), 25);
					loc = GeoEngine.moveCheck(requestor.getX(), requestor.getY(), requestor.getZ(), loc.x, loc.y, requestor.getGeoIndex());
					requestor.moveToLocation(loc, 0, false);
					requestor.getAI().setNextAction(PlayableAI.nextAction.COUPLE_ACTION, activeChar, _actionId, true, false);
				}
				finally
				{
					request.done();
				}
				break;
		}
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Player
	 * @param requestor Player
	 * @return boolean
	 */
	private static boolean checkCondition(Player activeChar, Player requestor)
	{
		if (!activeChar.isInRange(requestor, 300) || activeChar.isInRange(requestor, 25) || !GeoEngine.canSeeTarget(activeChar, requestor, false))
		{
			activeChar.sendPacket(SystemMsg.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
			return false;
		}
		return activeChar.checkCoupleAction(requestor);
	}
}

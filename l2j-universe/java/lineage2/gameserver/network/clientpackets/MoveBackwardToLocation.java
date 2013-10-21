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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.CharMoveToLocation;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MoveBackwardToLocation extends L2GameClientPacket
{
	/**
	 * Field _targetLoc.
	 */
	private final Location _targetLoc = new Location();
	/**
	 * Field _originLoc.
	 */
	private final Location _originLoc = new Location();
	/**
	 * Field _moveMovement.
	 */
	private int _moveMovement;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_targetLoc.x = readD();
		_targetLoc.y = readD();
		_targetLoc.z = readD();
		_originLoc.x = readD();
		_originLoc.y = readD();
		_originLoc.z = readD();
		if (_buf.hasRemaining())
		{
			_moveMovement = readD();
		}
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
		activeChar.setActive();
		if ((System.currentTimeMillis() - activeChar.getLastMovePacket()) < Config.MOVE_PACKET_DELAY)
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.setLastMovePacket();
		if (activeChar.isTeleporting())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isFrozen())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_MOVE_WHILE_FROZEN, ActionFail.STATIC);
			return;
		}
		if (activeChar.isInObserverMode())
		{
			if (activeChar.getOlympiadObserveGame() == null)
			{
				activeChar.sendActionFailed();
			}
			else
			{
				activeChar.sendPacket(new CharMoveToLocation(activeChar.getObjectId(), _originLoc, _targetLoc));
			}
			return;
		}
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.getTeleMode() > 0)
		{
			if (activeChar.getTeleMode() == 1)
			{
				activeChar.setTeleMode(0);
			}
			activeChar.sendActionFailed();
			activeChar.teleToLocation(_targetLoc);
			return;
		}
		if (activeChar.isInFlyingTransform())
		{
			_targetLoc.z = Math.min(5950, Math.max(50, _targetLoc.z));
		}
		activeChar.moveToLocation(_targetLoc, 0, (_moveMovement != 0) && !activeChar.getVarB("no_pf"));
	}
}

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

import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestExCubeGameChangeTeam extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(RequestExCubeGameChangeTeam.class);
	/**
	 * Field _arena. Field _team.
	 */
	int _team, _arena;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_arena = readD() + 1;
		_team = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (HandysBlockCheckerManager.getInstance().arenaIsBeingUsed(_arena))
		{
			return;
		}
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || activeChar.isDead())
		{
			return;
		}
		switch (_team)
		{
			case 0:
			case 1:
				HandysBlockCheckerManager.getInstance().changePlayerToTeam(activeChar, _arena, _team);
				break;
			case -1:
			{
				int team = HandysBlockCheckerManager.getInstance().getHolder(_arena).getPlayerTeam(activeChar);
				if (team > -1)
				{
					HandysBlockCheckerManager.getInstance().removePlayer(activeChar, _arena, team);
				}
				break;
			}
			default:
				_log.warn("Wrong Team ID: " + _team);
				break;
		}
	}
}

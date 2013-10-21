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
package lineage2.gameserver.listener.actor.player.impl;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonAnswerListener implements OnAnswerListener
{
	/**
	 * Field _playerRef.
	 */
	private final HardReference<Player> _playerRef;
	/**
	 * Field _location.
	 */
	private final Location _location;
	/**
	 * Field _count.
	 */
	private final long _count;
	
	/**
	 * Constructor for SummonAnswerListener.
	 * @param player Player
	 * @param loc Location
	 * @param count long
	 */
	public SummonAnswerListener(Player player, Location loc, long count)
	{
		_playerRef = player.getRef();
		_location = loc;
		_count = count;
	}
	
	/**
	 * Method sayYes.
	 * @see lineage2.gameserver.listener.actor.player.OnAnswerListener#sayYes()
	 */
	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if (player == null)
		{
			return;
		}
		player.abortAttack(true, true);
		player.abortCast(true, true);
		player.stopMove();
		if (_count > 0)
		{
			if (player.getInventory().destroyItemByItemId(8615, _count))
			{
				player.sendPacket(SystemMessage2.removeItems(8615, _count));
				player.teleToLocation(_location);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		else
		{
			player.teleToLocation(_location);
		}
	}
	
	/**
	 * Method sayNo.
	 * @see lineage2.gameserver.listener.actor.player.OnAnswerListener#sayNo()
	 */
	@Override
	public void sayNo()
	{
	}
}

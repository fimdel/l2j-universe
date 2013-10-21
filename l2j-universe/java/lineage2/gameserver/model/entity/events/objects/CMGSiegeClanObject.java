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
package lineage2.gameserver.model.entity.events.objects;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.pledge.Clan;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CMGSiegeClanObject extends SiegeClanObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _players.
	 */
	private final IntSet _players = new HashIntSet();
	/**
	 * Field _param.
	 */
	private long _param;
	
	/**
	 * Constructor for CMGSiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 * @param date long
	 */
	public CMGSiegeClanObject(String type, Clan clan, long param, long date)
	{
		super(type, clan, param, date);
		_param = param;
	}
	
	/**
	 * Constructor for CMGSiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 */
	public CMGSiegeClanObject(String type, Clan clan, long param)
	{
		super(type, clan, param);
		_param = param;
	}
	
	/**
	 * Method addPlayer.
	 * @param objectId int
	 */
	public void addPlayer(int objectId)
	{
		_players.add(objectId);
	}
	
	/**
	 * Method getParam.
	 * @return long
	 */
	@Override
	public long getParam()
	{
		return _param;
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	@Override
	public boolean isParticle(Player player)
	{
		return _players.contains(player.getObjectId());
	}
	
	/**
	 * Method setEvent.
	 * @param start boolean
	 * @param event SiegeEvent<?,?>
	 */
	@Override
	public void setEvent(boolean start, SiegeEvent<?, ?> event)
	{
		for (int i : _players.toArray())
		{
			Player player = GameObjectsStorage.getPlayer(i);
			if (player != null)
			{
				if (start)
				{
					player.addEvent(event);
				}
				else
				{
					player.removeEvent(event);
				}
				player.broadcastCharInfo();
			}
		}
	}
	
	/**
	 * Method setParam.
	 * @param param long
	 */
	public void setParam(long param)
	{
		_param = param;
	}
	
	/**
	 * Method getPlayers.
	 * @return IntSet
	 */
	public IntSet getPlayers()
	{
		return _players;
	}
}

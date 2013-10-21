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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.dao.SiegePlayerDAO;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CTBSiegeClanObject extends SiegeClanObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _players.
	 */
	private final List<Integer> _players = new ArrayList<>();
	/**
	 * Field _npcId.
	 */
	private long _npcId;
	
	/**
	 * Constructor for CTBSiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 * @param date long
	 */
	public CTBSiegeClanObject(String type, Clan clan, long param, long date)
	{
		super(type, clan, param, date);
		_npcId = param;
	}
	
	/**
	 * Constructor for CTBSiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 */
	public CTBSiegeClanObject(String type, Clan clan, long param)
	{
		this(type, clan, param, System.currentTimeMillis());
	}
	
	/**
	 * Method select.
	 * @param r Residence
	 */
	public void select(Residence r)
	{
		_players.addAll(SiegePlayerDAO.getInstance().select(r, getObjectId()));
	}
	
	/**
	 * Method getPlayers.
	 * @return List<Integer>
	 */
	public List<Integer> getPlayers()
	{
		return _players;
	}
	
	/**
	 * Method setEvent.
	 * @param start boolean
	 * @param event SiegeEvent<?,?>
	 */
	@Override
	public void setEvent(boolean start, SiegeEvent<?, ?> event)
	{
		for (int i : getPlayers())
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
	 * Method getParam.
	 * @return long
	 */
	@Override
	public long getParam()
	{
		return _npcId;
	}
	
	/**
	 * Method setParam.
	 * @param npcId int
	 */
	public void setParam(int npcId)
	{
		_npcId = npcId;
	}
}

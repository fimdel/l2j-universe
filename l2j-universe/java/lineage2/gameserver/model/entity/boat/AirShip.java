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
package lineage2.gameserver.model.entity.boat;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExAirShipInfo;
import lineage2.gameserver.network.serverpackets.ExGetOffAirShip;
import lineage2.gameserver.network.serverpackets.ExGetOnAirShip;
import lineage2.gameserver.network.serverpackets.ExMoveToLocationAirShip;
import lineage2.gameserver.network.serverpackets.ExMoveToLocationInAirShip;
import lineage2.gameserver.network.serverpackets.ExStopMoveAirShip;
import lineage2.gameserver.network.serverpackets.ExStopMoveInAirShip;
import lineage2.gameserver.network.serverpackets.ExValidateLocationInAirShip;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AirShip extends Boat
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for AirShip.
	 * @param objectId int
	 * @param template CharTemplate
	 */
	public AirShip(int objectId, CharTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method infoPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket infoPacket()
	{
		return new ExAirShipInfo(this);
	}
	
	/**
	 * Method movePacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket movePacket()
	{
		return new ExMoveToLocationAirShip(this);
	}
	
	/**
	 * Method inMovePacket.
	 * @param player Player
	 * @param src Location
	 * @param desc Location
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket inMovePacket(Player player, Location src, Location desc)
	{
		return new ExMoveToLocationInAirShip(player, this, src, desc);
	}
	
	/**
	 * Method stopMovePacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket stopMovePacket()
	{
		return new ExStopMoveAirShip(this);
	}
	
	/**
	 * Method inStopMovePacket.
	 * @param player Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket inStopMovePacket(Player player)
	{
		return new ExStopMoveInAirShip(player);
	}
	
	/**
	 * Method startPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket startPacket()
	{
		return null;
	}
	
	/**
	 * Method checkLocationPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket checkLocationPacket()
	{
		return null;
	}
	
	/**
	 * Method validateLocationPacket.
	 * @param player Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket validateLocationPacket(Player player)
	{
		return new ExValidateLocationInAirShip(player);
	}
	
	/**
	 * Method getOnPacket.
	 * @param playable Playable
	 * @param location Location
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket getOnPacket(Playable playable, Location location)
	{
		return new ExGetOnAirShip(playable.getPlayer(), this, location);
	}
	
	/**
	 * Method getOffPacket.
	 * @param playable Playable
	 * @param location Location
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket getOffPacket(Playable playable, Location location)
	{
		return new ExGetOffAirShip(playable.getPlayer(), this, location);
	}
	
	/**
	 * Method isAirShip.
	 * @return boolean
	 */
	@Override
	public boolean isAirShip()
	{
		return true;
	}
	
	/**
	 * Method oustPlayers.
	 */
	@Override
	public void oustPlayers()
	{
		for (Player player : _players)
		{
			oustPlayer(player, getReturnLoc(), true);
		}
	}
}

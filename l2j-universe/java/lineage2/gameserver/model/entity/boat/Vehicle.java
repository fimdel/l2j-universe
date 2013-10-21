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
import lineage2.gameserver.network.serverpackets.GetOffVehicle;
import lineage2.gameserver.network.serverpackets.GetOnVehicle;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MoveToLocationInVehicle;
import lineage2.gameserver.network.serverpackets.StopMove;
import lineage2.gameserver.network.serverpackets.StopMoveToLocationInVehicle;
import lineage2.gameserver.network.serverpackets.ValidateLocationInVehicle;
import lineage2.gameserver.network.serverpackets.VehicleCheckLocation;
import lineage2.gameserver.network.serverpackets.VehicleDeparture;
import lineage2.gameserver.network.serverpackets.VehicleInfo;
import lineage2.gameserver.network.serverpackets.VehicleStart;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Vehicle extends Boat
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for Vehicle.
	 * @param objectId int
	 * @param template CharTemplate
	 */
	public Vehicle(int objectId, CharTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method startPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket startPacket()
	{
		return new VehicleStart(this);
	}
	
	/**
	 * Method validateLocationPacket.
	 * @param player Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket validateLocationPacket(Player player)
	{
		return new ValidateLocationInVehicle(player);
	}
	
	/**
	 * Method checkLocationPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket checkLocationPacket()
	{
		return new VehicleCheckLocation(this);
	}
	
	/**
	 * Method infoPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket infoPacket()
	{
		return new VehicleInfo(this);
	}
	
	/**
	 * Method movePacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket movePacket()
	{
		return new VehicleDeparture(this);
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
		return new MoveToLocationInVehicle(player, this, src, desc);
	}
	
	/**
	 * Method stopMovePacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket stopMovePacket()
	{
		return new StopMove(this);
	}
	
	/**
	 * Method inStopMovePacket.
	 * @param player Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket inStopMovePacket(Player player)
	{
		return new StopMoveToLocationInVehicle(player);
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
		if (!playable.isPlayer())
		{
			return null;
		}
		return new GetOnVehicle(playable.getPlayer(), this, location);
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
		if (!playable.isPlayer())
		{
			return null;
		}
		return new GetOffVehicle(playable.getPlayer(), this, location);
	}
	
	/**
	 * Method oustPlayers.
	 */
	@Override
	public void oustPlayers()
	{
	}
	
	/**
	 * Method isVehicle.
	 * @return boolean
	 */
	@Override
	public boolean isVehicle()
	{
		return true;
	}
}

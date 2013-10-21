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
package lineage2.gameserver.listener.zone.impl;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.instances.ClanAirShipControllerInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AirshipControllerZoneListener implements OnZoneEnterLeaveListener
{
	/**
	 * Field _controllerInstance.
	 */
	private ClanAirShipControllerInstance _controllerInstance;
	
	/**
	 * Method onZoneEnter.
	 * @param zone Zone
	 * @param actor Creature
	 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
	 */
	@Override
	public void onZoneEnter(Zone zone, Creature actor)
	{
		if ((_controllerInstance == null) && (actor instanceof ClanAirShipControllerInstance))
		{
			_controllerInstance = (ClanAirShipControllerInstance) actor;
		}
		else if (actor.isClanAirShip())
		{
			_controllerInstance.setDockedShip((ClanAirShip) actor);
		}
	}
	
	/**
	 * Method onZoneLeave.
	 * @param zone Zone
	 * @param actor Creature
	 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
	 */
	@Override
	public void onZoneLeave(Zone zone, Creature actor)
	{
		if (actor.isClanAirShip())
		{
			_controllerInstance.setDockedShip(null);
		}
	}
}

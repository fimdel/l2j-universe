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
package lineage2.gameserver.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.network.GamePacketHandler;
import lineage2.gameserver.templates.InstantZone;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ReflectionUtils
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GamePacketHandler.class);
	/**
	 * Method getDoor.
	 * @param id int
	 * @return DoorInstance
	 */
	public static DoorInstance getDoor(int id)
	{
		return ReflectionManager.DEFAULT.getDoor(id);
	}
	
	/**
	 * Method getZone.
	 * @param name String
	 * @return Zone
	 */
	public static Zone getZone(String name)
	{
		return ReflectionManager.DEFAULT.getZone(name);
	}
	
	/**
	 * Method getZonesByType.
	 * @param zoneType Zone.ZoneType
	 * @return List<Zone>
	 */
	public static List<Zone> getZonesByType(Zone.ZoneType zoneType)
	{
		Collection<Zone> zones = ReflectionManager.DEFAULT.getZones();
		if (zones.isEmpty())
		{
			return Collections.emptyList();
		}
		List<Zone> zones2 = new ArrayList<>(5);
		for (Zone z : zones)
		{
			if (z.getType() == zoneType)
			{
				zones2.add(z);
			}
		}
		return zones2;
	}
	
	/**
	 * Method enterReflection.
	 * @param invoker Player
	 * @param instancedZoneId int
	 * @return Reflection
	 */
	public static Reflection enterReflection(Player invoker, int instancedZoneId)
	{
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
		return enterReflection(invoker, new Reflection(), iz);
	}
	
	/**
	 * Method enterReflection.
	 * @param invoker Player
	 * @param r Reflection
	 * @param instancedZoneId int
	 * @return Reflection
	 */
	public static Reflection enterReflection(Player invoker, Reflection r, int instancedZoneId)
	{
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
		return enterReflection(invoker, r, iz);
	}
	
	/**
	 * Method enterReflection.
	 * @param invoker Player
	 * @param r Reflection
	 * @param iz InstantZone
	 * @return Reflection
	 */
	public static Reflection enterReflection(Player invoker, Reflection r, InstantZone iz)
	{
		r.init(iz);
		
		_log.info("Player: " + invoker.getName() + " started instance "+ r.getName() +" id:" + r.getId());
		if (r.getReturnLoc() == null)
		{
			r.setReturnLoc(invoker.getLoc());
		}
		switch (iz.getEntryType())
		{
			case SOLO:
				if (iz.getRemovedItemId() > 0)
				{
					ItemFunctions.removeItem(invoker, iz.getRemovedItemId(), iz.getRemovedItemCount(), true);
				}
				if (iz.getGiveItemId() > 0)
				{
					ItemFunctions.addItem(invoker, iz.getGiveItemId(), iz.getGiveItemCount(), true);
				}
				if (iz.isDispelBuffs())
				{
					invoker.dispelBuffs();
				}
				if (iz.getSetReuseUponEntry() && (iz.getResetReuse().next(System.currentTimeMillis()) > System.currentTimeMillis()))
				{
					invoker.setInstanceReuse(iz.getId(), System.currentTimeMillis());
				}
				invoker.setVar("backCoords", invoker.getLoc().toXYZString(), -1);
				invoker.teleToLocation(iz.getTeleportCoord(), r);
				break;
			case PARTY:
				Party party = invoker.getParty();
				party.setReflection(r);
				r.setParty(party);
				for (Player member : party.getPartyMembers())
				{
					if (iz.getRemovedItemId() > 0)
					{
						ItemFunctions.removeItem(member, iz.getRemovedItemId(), iz.getRemovedItemCount(), true);
					}
					if (iz.getGiveItemId() > 0)
					{
						ItemFunctions.addItem(member, iz.getGiveItemId(), iz.getGiveItemCount(), true);
					}
					if (iz.isDispelBuffs())
					{
						member.dispelBuffs();
					}
					if (iz.getSetReuseUponEntry())
					{
						member.setInstanceReuse(iz.getId(), System.currentTimeMillis());
					}
					member.setVar("backCoords", invoker.getLoc().toXYZString(), -1);
					member.teleToLocation(iz.getTeleportCoord(), r);
				}
				break;
			case COMMAND_CHANNEL:
				Party commparty = invoker.getParty();
				CommandChannel cc = commparty.getCommandChannel();
				cc.setReflection(r);
				r.setCommandChannel(cc);
				for (Player member : cc)
				{
					if (iz.getRemovedItemId() > 0)
					{
						ItemFunctions.removeItem(member, iz.getRemovedItemId(), iz.getRemovedItemCount(), true);
					}
					if (iz.getGiveItemId() > 0)
					{
						ItemFunctions.addItem(member, iz.getGiveItemId(), iz.getGiveItemCount(), true);
					}
					if (iz.isDispelBuffs())
					{
						member.dispelBuffs();
					}
					if (iz.getSetReuseUponEntry())
					{
						member.setInstanceReuse(iz.getId(), System.currentTimeMillis());
					}
					member.setVar("backCoords", invoker.getLoc().toXYZString(), -1);
					member.teleToLocation(iz.getTeleportCoord(), r);
				}
				break;
		}
		return r;
	}
}

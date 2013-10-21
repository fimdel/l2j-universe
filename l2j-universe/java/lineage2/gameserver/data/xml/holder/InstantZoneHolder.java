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
package lineage2.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.InstantZone;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class InstantZoneHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final InstantZoneHolder _instance = new InstantZoneHolder();
	/**
	 * Field _zones.
	 */
	private final IntObjectMap<InstantZone> _zones = new HashIntObjectMap<>();
	
	/**
	 * Method getInstance.
	 * @return InstantZoneHolder
	 */
	public static InstantZoneHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addInstantZone.
	 * @param zone InstantZone
	 */
	public void addInstantZone(InstantZone zone)
	{
		_zones.put(zone.getId(), zone);
	}
	
	/**
	 * Method getInstantZone.
	 * @param id int
	 * @return InstantZone
	 */
	public InstantZone getInstantZone(int id)
	{
		return _zones.get(id);
	}
	
	/**
	 * Method getResetReuseById.
	 * @param id int
	 * @return SchedulingPattern
	 */
	private SchedulingPattern getResetReuseById(int id)
	{
		InstantZone zone = getInstantZone(id);
		return zone == null ? null : zone.getResetReuse();
	}
	
	/**
	 * Method getMinutesToNextEntrance.
	 * @param id int
	 * @param player Player
	 * @return int
	 */
	public int getMinutesToNextEntrance(int id, Player player)
	{
		SchedulingPattern resetReuse = getResetReuseById(id);
		if (resetReuse == null)
		{
			return 0;
		}
		Long time = null;
		if ((getSharedReuseInstanceIds(id) != null) && !getSharedReuseInstanceIds(id).isEmpty())
		{
			List<Long> reuses = new ArrayList<>();
			for (int i : getSharedReuseInstanceIds(id))
			{
				if (player.getInstanceReuse(i) != null)
				{
					reuses.add(player.getInstanceReuse(i));
				}
			}
			if (!reuses.isEmpty())
			{
				Collections.sort(reuses);
				time = reuses.get(reuses.size() - 1);
			}
		}
		else
		{
			time = player.getInstanceReuse(id);
		}
		if (time == null)
		{
			return 0;
		}
		return (int) Math.max((resetReuse.next(time) - System.currentTimeMillis()) / 60000L, 0);
	}
	
	/**
	 * Method getSharedReuseInstanceIds.
	 * @param id int
	 * @return List<Integer>
	 */
	public List<Integer> getSharedReuseInstanceIds(int id)
	{
		if (getInstantZone(id).getSharedReuseGroup() < 1)
		{
			return null;
		}
		List<Integer> sharedInstanceIds = new ArrayList<>();
		for (InstantZone iz : _zones.values())
		{
			if ((iz.getSharedReuseGroup() > 0) && (getInstantZone(id).getSharedReuseGroup() > 0) && (iz.getSharedReuseGroup() == getInstantZone(id).getSharedReuseGroup()))
			{
				sharedInstanceIds.add(iz.getId());
			}
		}
		return sharedInstanceIds;
	}
	
	/**
	 * Method getSharedReuseInstanceIdsByGroup.
	 * @param groupId int
	 * @return List<Integer>
	 */
	public List<Integer> getSharedReuseInstanceIdsByGroup(int groupId)
	{
		if (groupId < 1)
		{
			return null;
		}
		List<Integer> sharedInstanceIds = new ArrayList<>();
		for (InstantZone iz : _zones.values())
		{
			if ((iz.getSharedReuseGroup() > 0) && (iz.getSharedReuseGroup() == groupId))
			{
				sharedInstanceIds.add(iz.getId());
			}
		}
		return sharedInstanceIds;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _zones.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_zones.clear();
	}
}

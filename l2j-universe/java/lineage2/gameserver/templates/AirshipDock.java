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
package lineage2.gameserver.templates;

import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.entity.events.objects.BoatPoint;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AirshipDock
{
	/**
	 * @author Mobius
	 */
	public static class AirshipPlatform
	{
		/**
		 * Field _oustMovie.
		 */
		private final SceneMovie _oustMovie;
		/**
		 * Field _oustLoc.
		 */
		private final Location _oustLoc;
		/**
		 * Field _spawnLoc.
		 */
		private final Location _spawnLoc;
		/**
		 * Field _arrivalPoints.
		 */
		private List<BoatPoint> _arrivalPoints = Collections.emptyList();
		/**
		 * Field _departPoints.
		 */
		private List<BoatPoint> _departPoints = Collections.emptyList();
		
		/**
		 * Constructor for AirshipPlatform.
		 * @param movie SceneMovie
		 * @param oustLoc Location
		 * @param spawnLoc Location
		 * @param arrival List<BoatPoint>
		 * @param depart List<BoatPoint>
		 */
		public AirshipPlatform(SceneMovie movie, Location oustLoc, Location spawnLoc, List<BoatPoint> arrival, List<BoatPoint> depart)
		{
			_oustMovie = movie;
			_oustLoc = oustLoc;
			_spawnLoc = spawnLoc;
			_arrivalPoints = arrival;
			_departPoints = depart;
		}
		
		/**
		 * Method getOustMovie.
		 * @return SceneMovie
		 */
		public SceneMovie getOustMovie()
		{
			return _oustMovie;
		}
		
		/**
		 * Method getOustLoc.
		 * @return Location
		 */
		public Location getOustLoc()
		{
			return _oustLoc;
		}
		
		/**
		 * Method getSpawnLoc.
		 * @return Location
		 */
		public Location getSpawnLoc()
		{
			return _spawnLoc;
		}
		
		/**
		 * Method getArrivalPoints.
		 * @return List<BoatPoint>
		 */
		public List<BoatPoint> getArrivalPoints()
		{
			return _arrivalPoints;
		}
		
		/**
		 * Method getDepartPoints.
		 * @return List<BoatPoint>
		 */
		public List<BoatPoint> getDepartPoints()
		{
			return _departPoints;
		}
	}
	
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _teleportList.
	 */
	private List<BoatPoint> _teleportList = Collections.emptyList();
	/**
	 * Field _platformList.
	 */
	private List<AirshipPlatform> _platformList = Collections.emptyList();
	
	/**
	 * Constructor for AirshipDock.
	 * @param id int
	 * @param teleport List<BoatPoint>
	 * @param platformList List<AirshipPlatform>
	 */
	public AirshipDock(int id, List<BoatPoint> teleport, List<AirshipPlatform> platformList)
	{
		_id = id;
		_teleportList = teleport;
		_platformList = platformList;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getTeleportList.
	 * @return List<BoatPoint>
	 */
	public List<BoatPoint> getTeleportList()
	{
		return _teleportList;
	}
	
	/**
	 * Method getPlatform.
	 * @param id int
	 * @return AirshipPlatform
	 */
	public AirshipPlatform getPlatform(int id)
	{
		return _platformList.get(id);
	}
}

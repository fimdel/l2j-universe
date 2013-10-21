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

import java.util.Collection;
import java.util.TreeMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ShuttleTemplate extends CharTemplate
{
	/**
	 * @author Mobius
	 */
	public static class ShuttleDoor
	{
		/**
		 * Field _id.
		 */
		private final int _id;
		/**
		 * Field unkParam.
		 */
		public final int[] unkParam = new int[9];
		
		/**
		 * Constructor for ShuttleDoor.
		 * @param id int
		 * @param set StatsSet
		 */
		public ShuttleDoor(int id, StatsSet set)
		{
			_id = id;
			unkParam[0] = set.getInteger("unk_param_0", 0);
			unkParam[1] = set.getInteger("unk_param_1", 0);
			unkParam[2] = set.getInteger("unk_param_2", 0);
			unkParam[3] = set.getInteger("unk_param_3", 0);
			unkParam[4] = set.getInteger("unk_param_4", 0);
			unkParam[5] = set.getInteger("unk_param_5", 0);
			unkParam[6] = set.getInteger("unk_param_6", 0);
			unkParam[7] = set.getInteger("unk_param_7", 0);
			unkParam[8] = set.getInteger("unk_param_8", 0);
		}
		
		/**
		 * Method getId.
		 * @return int
		 */
		public int getId()
		{
			return _id;
		}
	}
	
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _doors.
	 */
	private final TreeMap<Integer, ShuttleDoor> _doors = new TreeMap<>();
	
	/**
	 * Constructor for ShuttleTemplate.
	 * @param id int
	 */
	public ShuttleTemplate(int id)
	{
		super(CharTemplate.getEmptyStatsSet());
		_id = id;
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
	 * Method getDoors.
	 * @return Collection<ShuttleDoor>
	 */
	public Collection<ShuttleDoor> getDoors()
	{
		return _doors.values();
	}
	
	/**
	 * Method getDoor.
	 * @param id int
	 * @return ShuttleDoor
	 */
	public ShuttleDoor getDoor(int id)
	{
		return _doors.get(id);
	}
	
	/**
	 * Method addDoor.
	 * @param door ShuttleDoor
	 */
	public void addDoor(ShuttleDoor door)
	{
		_doors.put(door.getId(), door);
	}
}

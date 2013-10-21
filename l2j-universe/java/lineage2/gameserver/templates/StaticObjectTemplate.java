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

import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StaticObjectTemplate
{
	/**
	 * Field _uid.
	 */
	private final int _uid;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _filePath.
	 */
	private final String _filePath;
	/**
	 * Field _mapX.
	 */
	private final int _mapX;
	/**
	 * Field _mapY.
	 */
	private final int _mapY;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _x.
	 */
	private final int _x;
	/**
	 * Field _y.
	 */
	private final int _y;
	/**
	 * Field _z.
	 */
	private final int _z;
	/**
	 * Field _spawn.
	 */
	private final boolean _spawn;
	
	/**
	 * Constructor for StaticObjectTemplate.
	 * @param set StatsSet
	 */
	public StaticObjectTemplate(StatsSet set)
	{
		_uid = set.getInteger("uid");
		_type = set.getInteger("stype");
		_mapX = set.getInteger("map_x");
		_mapY = set.getInteger("map_y");
		_filePath = set.getString("path");
		_name = set.getString("name");
		_x = set.getInteger("x");
		_y = set.getInteger("y");
		_z = set.getInteger("z");
		_spawn = set.getBool("spawn");
	}
	
	/**
	 * Method getUId.
	 * @return int
	 */
	public int getUId()
	{
		return _uid;
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	public int getType()
	{
		return _type;
	}
	
	/**
	 * Method getFilePath.
	 * @return String
	 */
	public String getFilePath()
	{
		return _filePath;
	}
	
	/**
	 * Method getMapX.
	 * @return int
	 */
	public int getMapX()
	{
		return _mapX;
	}
	
	/**
	 * Method getMapY.
	 * @return int
	 */
	public int getMapY()
	{
		return _mapY;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getX.
	 * @return int
	 */
	public int getX()
	{
		return _x;
	}
	
	/**
	 * Method getY.
	 * @return int
	 */
	public int getY()
	{
		return _y;
	}
	
	/**
	 * Method getZ.
	 * @return int
	 */
	public int getZ()
	{
		return _z;
	}
	
	/**
	 * Method isSpawn.
	 * @return boolean
	 */
	public boolean isSpawn()
	{
		return _spawn;
	}
	
	/**
	 * Method newInstance.
	 * @return StaticObjectInstance
	 */
	public StaticObjectInstance newInstance()
	{
		StaticObjectInstance instance = new StaticObjectInstance(IdFactory.getInstance().getNextId(), this);
		instance.spawnMe(new Location(getX(), getY(), getZ()));
		return instance;
	}
}

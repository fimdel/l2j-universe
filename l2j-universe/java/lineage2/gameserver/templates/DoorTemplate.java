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

import java.lang.reflect.Constructor;

import lineage2.commons.geometry.Polygon;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.DoorAI;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DoorTemplate extends CharTemplate
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(DoorTemplate.class);
	/**
	 * Field DEFAULT_AI_CONSTRUCTOR.
	 */
	@SuppressWarnings("unchecked")
	public static final Constructor<DoorAI> DEFAULT_AI_CONSTRUCTOR = (Constructor<DoorAI>) CharacterAI.class.getConstructors()[0];
	
	/**
	 * @author Mobius
	 */
	public static enum DoorType
	{
		/**
		 * Field DOOR.
		 */
		DOOR,
		/**
		 * Field WALL.
		 */
		WALL
	}
	
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _doorType.
	 */
	private final DoorType _doorType;
	/**
	 * Field _unlockable.
	 */
	private final boolean _unlockable;
	/**
	 * Field _isHPVisible.
	 */
	private final boolean _isHPVisible;
	/**
	 * Field _opened.
	 */
	private final boolean _opened;
	/**
	 * Field _targetable.
	 */
	private final boolean _targetable;
	/**
	 * Field _polygon.
	 */
	private final Polygon _polygon;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field _key.
	 */
	private final int _key;
	/**
	 * Field _openTime.
	 */
	private final int _openTime;
	/**
	 * Field _rndTime.
	 */
	private final int _rndTime;
	/**
	 * Field _closeTime.
	 */
	private final int _closeTime;
	/**
	 * Field _masterDoor.
	 */
	private final int _masterDoor;
	/**
	 * Field _aiParams.
	 */
	private final StatsSet _aiParams;
	/**
	 * Field _classAI.
	 */
	private Class<DoorAI> _classAI = DoorAI.class;
	/**
	 * Field _constructorAI.
	 */
	private Constructor<DoorAI> _constructorAI = DEFAULT_AI_CONSTRUCTOR;
	
	/**
	 * Constructor for DoorTemplate.
	 * @param set StatsSet
	 */
	public DoorTemplate(StatsSet set)
	{
		super(set);
		_id = set.getInteger("uid");
		_name = set.getString("name");
		_doorType = set.getEnum("door_type", DoorType.class, DoorType.DOOR);
		_unlockable = set.getBool("unlockable", false);
		_isHPVisible = set.getBool("show_hp", false);
		_opened = set.getBool("opened", false);
		_targetable = set.getBool("targetable", true);
		_loc = (Location) set.get("pos");
		_polygon = (Polygon) set.get("shape");
		_key = set.getInteger("key", 0);
		_openTime = set.getInteger("open_time", 0);
		_rndTime = set.getInteger("random_time", 0);
		_closeTime = set.getInteger("close_time", 0);
		_masterDoor = set.getInteger("master_door", 0);
		_aiParams = (StatsSet) set.getObject("ai_params", StatsSet.EMPTY);
		setAI(set.getString("ai", "DoorAI"));
	}
	
	/**
	 * Method setAI.
	 * @param ai String
	 */
	@SuppressWarnings("unchecked")
	private void setAI(String ai)
	{
		Class<DoorAI> classAI = null;
		try
		{
			classAI = (Class<DoorAI>) Class.forName("lineage2.gameserver.ai." + ai);
		}
		catch (ClassNotFoundException e)
		{
			classAI = (Class<DoorAI>) Scripts.getInstance().getClasses().get("ai.door." + ai);
		}
		if (classAI == null)
		{
			_log.error("Not found ai class for ai: " + ai + ". DoorId: " + _id);
		}
		else
		{
			_classAI = classAI;
			_constructorAI = (Constructor<DoorAI>) _classAI.getConstructors()[0];
		}
		if (_classAI.isAnnotationPresent(Deprecated.class))
		{
			_log.error("Ai type: " + ai + ", is deprecated. DoorId: " + _id);
		}
	}
	
	/**
	 * Method getNewAI.
	 * @param door DoorInstance
	 * @return CharacterAI
	 */
	public CharacterAI getNewAI(DoorInstance door)
	{
		try
		{
			return _constructorAI.newInstance(door);
		}
		catch (Exception e)
		{
			_log.error("Unable to create ai of doorId " + _id, e);
		}
		return new DoorAI(door);
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	@Override
	public int getNpcId()
	{
		return _id;
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
	 * Method getDoorType.
	 * @return DoorType
	 */
	public DoorType getDoorType()
	{
		return _doorType;
	}
	
	/**
	 * Method isUnlockable.
	 * @return boolean
	 */
	public boolean isUnlockable()
	{
		return _unlockable;
	}
	
	/**
	 * Method isHPVisible.
	 * @return boolean
	 */
	public boolean isHPVisible()
	{
		return _isHPVisible;
	}
	
	/**
	 * Method getPolygon.
	 * @return Polygon
	 */
	public Polygon getPolygon()
	{
		return _polygon;
	}
	
	/**
	 * Method getKey.
	 * @return int
	 */
	public int getKey()
	{
		return _key;
	}
	
	/**
	 * Method isOpened.
	 * @return boolean
	 */
	public boolean isOpened()
	{
		return _opened;
	}
	
	/**
	 * Method getLoc.
	 * @return Location
	 */
	public Location getLoc()
	{
		return _loc;
	}
	
	/**
	 * Method getOpenTime.
	 * @return int
	 */
	public int getOpenTime()
	{
		return _openTime;
	}
	
	/**
	 * Method getRandomTime.
	 * @return int
	 */
	public int getRandomTime()
	{
		return _rndTime;
	}
	
	/**
	 * Method getCloseTime.
	 * @return int
	 */
	public int getCloseTime()
	{
		return _closeTime;
	}
	
	/**
	 * Method isTargetable.
	 * @return boolean
	 */
	public boolean isTargetable()
	{
		return _targetable;
	}
	
	/**
	 * Method getMasterDoor.
	 * @return int
	 */
	public int getMasterDoor()
	{
		return _masterDoor;
	}
	
	/**
	 * Method getAIParams.
	 * @return StatsSet
	 */
	public StatsSet getAIParams()
	{
		return _aiParams;
	}
}

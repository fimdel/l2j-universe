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
package lineage2.gameserver.model.actor.instances.player;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import lineage2.gameserver.dao.CharacterSubclassDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SubClassList
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SubClassList.class);
	/**
	 * Field MAX_SUB_COUNT. (value is 4)
	 */
	public static final int MAX_SUB_COUNT = 4;
	/**
	 * Field _listByIndex.
	 */
	private final TreeMap<Integer, SubClass> _listByIndex = new TreeMap<Integer, SubClass>();
	/**
	 * Field _listByClassId.
	 */
	private final TreeMap<Integer, SubClass> _listByClassId = new TreeMap<Integer, SubClass>();
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _baseSubClass.
	 */
	private SubClass _baseSubClass = null;
	/**
	 * Field _activeSubClass.
	 */
	private SubClass _activeSubClass = null;
	/**
	 * Field _lastFreeIndex.
	 */
	private int _lastFreeIndex = 1;
	
	/**
	 * Constructor for SubClassList.
	 * @param owner Player
	 */
	public SubClassList(Player owner)
	{
		_owner = owner;
	}
	
	/**
	 * Method restore.
	 */
	public void restore()
	{
		_listByIndex.clear();
		_listByClassId.clear();
		List<SubClass> subclasses = CharacterSubclassDAO.getInstance().restore(_owner);
		int lastFreeIndex = 2;
		for (SubClass sub : subclasses)
		{
			if (sub == null)
			{
				continue;
			}
			if (size() >= MAX_SUB_COUNT)
			{
				_log.warn("SubClassList:restore: Limit is subclass! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
				break;
			}
			if (sub.isActive())
			{
				_activeSubClass = sub;
			}
			if (sub.isBase())
			{
				_baseSubClass = sub;
				sub.setIndex(1);
			}
			else
			{
				sub.setIndex(lastFreeIndex);
				lastFreeIndex++;
			}
			if (_listByIndex.containsKey(sub.getIndex()))
			{
				_log.warn("SubClassList:restore: Duplicate index in player subclasses! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
			}
			_listByIndex.put(sub.getIndex(), sub);
			if (_listByClassId.containsKey(sub.getClassId()))
			{
				_log.warn("SubClassList:restore: Duplicate class_id in player subclasses! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
			}
			_listByClassId.put(sub.getClassId(), sub);
		}
		_lastFreeIndex = lastFreeIndex;
		if (_listByIndex.size() != _listByClassId.size())
		{
			_log.warn("SubClassList:restore: The size of the lists do not match! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
		}
	}
	
	/**
	 * Method values.
	 * @return Collection<SubClass>
	 */
	public Collection<SubClass> values()
	{
		return _listByIndex.values();
	}
	
	/**
	 * Method getByClassId.
	 * @param classId int
	 * @return SubClass
	 */
	public SubClass getByClassId(int classId)
	{
		return _listByClassId.get(classId);
	}
	
	/**
	 * Method getByIndex.
	 * @param index int
	 * @return SubClass
	 */
	public SubClass getByIndex(int index)
	{
		return _listByIndex.get(index);
	}
	
	/**
	 * Method removeByClassId.
	 * @param classId int
	 */
	public void removeByClassId(int classId)
	{
		if (!_listByClassId.containsKey(classId))
		{
			return;
		}
		int index = _listByClassId.get(classId).getIndex();
		_listByIndex.remove(index);
		_listByClassId.remove(classId);
	}
	
	/**
	 * Method getActiveSubClass.
	 * @return SubClass
	 */
	public SubClass getActiveSubClass()
	{
		return _activeSubClass;
	}
	
	/**
	 * Method getBaseSubClass.
	 * @return SubClass
	 */
	public SubClass getBaseSubClass()
	{
		return _baseSubClass;
	}
	
	/**
	 * Method isBaseClassActive.
	 * @return boolean
	 */
	public boolean isBaseClassActive()
	{
		return _activeSubClass == _baseSubClass;
	}

	
	/**
	 * Method isBaseClassActive.
	 * @return boolean
	 */
	public boolean isDoubleClassActive()
	{
		return _activeSubClass.isDouble();
	}
	/**
	 * Method haveSubClasses.
	 * @return boolean
	 */
	public boolean haveSubClasses()
	{
		return size() > 1;
	}
	
	/**
	 * Method haveDualClass.
	 * @return boolean
	 */
	public boolean haveDualClass()
	{
		for(SubClass sc : _listByClassId.values())
		{
			if(sc.isDouble())
			{
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Method changeSubClassId.
	 * @param oldClassId int
	 * @param newClassId int
	 * @return boolean
	 */
	public boolean changeSubClassId(int oldClassId, int newClassId)
	{
		if (!_listByClassId.containsKey(oldClassId))
		{
			return false;
		}
		if (_listByClassId.containsKey(newClassId))
		{
			return false;
		}
		SubClass sub = _listByClassId.get(oldClassId);
		sub.setClassId(newClassId);
		_listByClassId.remove(oldClassId);
		_listByClassId.put(sub.getClassId(), sub);
		return true;
	}
	
	/**
	 * Method add.
	 * @param sub SubClass
	 * @return boolean
	 */
	public boolean add(SubClass sub)
	{
		if (sub == null)
		{
			return false;
		}
		if (size() >= MAX_SUB_COUNT)
		{
			return false;
		}
		if (_listByClassId.containsKey(sub.getClassId()))
		{
			return false;
		}
		sub.setIndex(_lastFreeIndex);
		_lastFreeIndex++;
		_listByIndex.put(sub.getIndex(), sub);
		_listByClassId.put(sub.getClassId(), sub);
		return true;
	}
	
	/**
	 * Method add.
	 * @param sub SubClass
	 * @return boolean
	 */
	public boolean addToIndex(SubClass sub, int index)
	{
		if (sub == null)
		{
			return false;
		}
		if (size() >= MAX_SUB_COUNT)
		{
			return false;
		}
		if (_listByClassId.containsKey(sub.getClassId()))
		{
			return false;
		}
		if(index < 1 && index > 4)
		{
			return false;
		}
		sub.setIndex(index);
		_listByIndex.put(sub.getIndex(), sub);
		_listByClassId.put(sub.getClassId(), sub);
		return true;
	}
	
	/**
	 * Method changeActiveSubClass.
	 * @param classId int
	 * @return SubClass
	 */
	public SubClass changeActiveSubClass(int classId)
	{
		if (!_listByClassId.containsKey(classId))
		{
			return null;
		}
		if (_activeSubClass == null)
		{
			return null;
		}
		_activeSubClass.setActive(false);
		SubClass sub = _listByClassId.get(classId);
		sub.setActive(true);
		_activeSubClass = sub;
		return sub;
	}
	
	/**
	 * Method containsClassId.
	 * @param classId int
	 * @return boolean
	 */
	public boolean containsClassId(int classId)
	{
		return _listByClassId.containsKey(classId);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public int size()
	{
		return _listByIndex.size();
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "SubClassList[owner=" + _owner.getName() + "]";
	}
}

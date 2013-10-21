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
package lineage2.gameserver.skills;

import lineage2.gameserver.model.Skill;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TimeStamp
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _reuse.
	 */
	private final long _reuse;
	/**
	 * Field _endTime.
	 */
	private final long _endTime;
	
	/**
	 * Constructor for TimeStamp.
	 * @param id int
	 * @param endTime long
	 * @param reuse long
	 */
	public TimeStamp(int id, long endTime, long reuse)
	{
		_id = id;
		_level = 0;
		_reuse = reuse;
		_endTime = endTime;
	}
	
	/**
	 * Constructor for TimeStamp.
	 * @param skill Skill
	 * @param reuse long
	 */
	public TimeStamp(Skill skill, long reuse)
	{
		this(skill, System.currentTimeMillis() + reuse, reuse);
	}
	
	/**
	 * Constructor for TimeStamp.
	 * @param skill Skill
	 * @param endTime long
	 * @param reuse long
	 */
	public TimeStamp(Skill skill, long endTime, long reuse)
	{
		_id = skill.getId();
		_level = skill.getLevel();
		_reuse = reuse;
		_endTime = endTime;
	}
	
	/**
	 * Method getReuseBasic.
	 * @return long
	 */
	public long getReuseBasic()
	{
		if (_reuse == 0)
		{
			return getReuseCurrent();
		}
		return _reuse;
	}
	
	/**
	 * Method getReuseCurrent.
	 * @return long
	 */
	public long getReuseCurrent()
	{
		return Math.max(_endTime - System.currentTimeMillis(), 0);
	}
	
	/**
	 * Method getEndTime.
	 * @return long
	 */
	public long getEndTime()
	{
		return _endTime;
	}
	
	/**
	 * Method hasNotPassed.
	 * @return boolean
	 */
	public boolean hasNotPassed()
	{
		return System.currentTimeMillis() < _endTime;
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
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
}

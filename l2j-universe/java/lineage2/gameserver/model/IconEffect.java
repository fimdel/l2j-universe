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
package lineage2.gameserver.model;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class IconEffect
{
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _duration.
	 */
	private final int _duration;
	/**
	 * Field _obj.
	 */
	private final int _obj;
	
	/**
	 * Constructor for IconEffect.
	 * @param skillId int
	 * @param level int
	 * @param duration int
	 * @param obj int
	 */
	public IconEffect(int skillId, int level, int duration, int obj)
	{
		_skillId = skillId;
		_level = level;
		_duration = duration;
		_obj = obj;
	}
	
	/**
	 * Method getSkillId.
	 * @return int
	 */
	public int getSkillId()
	{
		return _skillId;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method getDuration.
	 * @return int
	 */
	public int getDuration()
	{
		return _duration;
	}
	
	/**
	 * Method getObj.
	 * @return int
	 */
	public int getObj()
	{
		return _obj;
	}
}

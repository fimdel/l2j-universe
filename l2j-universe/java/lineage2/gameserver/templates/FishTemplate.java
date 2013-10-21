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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishTemplate
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
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _HP.
	 */
	private final int _HP;
	/**
	 * Field _HpRegen.
	 */
	private final int _HpRegen;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _group.
	 */
	private final int _group;
	/**
	 * Field _fish_guts.
	 */
	private final int _fish_guts;
	/**
	 * Field _guts_check_time.
	 */
	private final int _guts_check_time;
	/**
	 * Field _wait_time.
	 */
	private final int _wait_time;
	/**
	 * Field _combat_time.
	 */
	private final int _combat_time;
	
	/**
	 * Constructor for FishTemplate.
	 * @param id int
	 * @param lvl int
	 * @param name String
	 * @param HP int
	 * @param HpRegen int
	 * @param type int
	 * @param group int
	 * @param fish_guts int
	 * @param guts_check_time int
	 * @param wait_time int
	 * @param combat_time int
	 */
	public FishTemplate(int id, int lvl, String name, int HP, int HpRegen, int type, int group, int fish_guts, int guts_check_time, int wait_time, int combat_time)
	{
		_id = id;
		_level = lvl;
		_name = name.intern();
		_HP = HP;
		_HpRegen = HpRegen;
		_type = type;
		_group = group;
		_fish_guts = fish_guts;
		_guts_check_time = guts_check_time;
		_wait_time = wait_time;
		_combat_time = combat_time;
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
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getHP.
	 * @return int
	 */
	public int getHP()
	{
		return _HP;
	}
	
	/**
	 * Method getHpRegen.
	 * @return int
	 */
	public int getHpRegen()
	{
		return _HpRegen;
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
	 * Method getGroup.
	 * @return int
	 */
	public int getGroup()
	{
		return _group;
	}
	
	/**
	 * Method getFishGuts.
	 * @return int
	 */
	public int getFishGuts()
	{
		return _fish_guts;
	}
	
	/**
	 * Method getGutsCheckTime.
	 * @return int
	 */
	public int getGutsCheckTime()
	{
		return _guts_check_time;
	}
	
	/**
	 * Method getWaitTime.
	 * @return int
	 */
	public int getWaitTime()
	{
		return _wait_time;
	}
	
	/**
	 * Method getCombatTime.
	 * @return int
	 */
	public int getCombatTime()
	{
		return _combat_time;
	}
}

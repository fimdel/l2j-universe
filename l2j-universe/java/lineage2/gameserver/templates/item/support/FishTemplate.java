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
package lineage2.gameserver.templates.item.support;

import lineage2.commons.collections.MultiValueSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishTemplate
{
	/**
	 * Field _group.
	 */
	private final FishGroup _group;
	/**
	 * Field _grade.
	 */
	private final FishGrade _grade;
	/**
	 * Field _biteRate.
	 */
	private final double _biteRate;
	/**
	 * Field _guts.
	 */
	private final double _guts;
	/**
	 * Field _lengthRate.
	 */
	private final double _lengthRate;
	/**
	 * Field _hpRegen.
	 */
	private final double _hpRegen;
	/**
	 * Field _gutsCheckProbability.
	 */
	private final double _gutsCheckProbability;
	/**
	 * Field _cheatingProb.
	 */
	private final double _cheatingProb;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _hp.
	 */
	private final int _hp;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _maxLength.
	 */
	private final int _maxLength;
	/**
	 * Field _startCombatTime.
	 */
	private final int _startCombatTime;
	/**
	 * Field _combatDuration.
	 */
	private final int _combatDuration;
	/**
	 * Field _gutsCheckTime.
	 */
	private final int _gutsCheckTime;
	
	/**
	 * Constructor for FishTemplate.
	 * @param map MultiValueSet<String>
	 */
	public FishTemplate(MultiValueSet<String> map)
	{
		_group = map.getEnum("group", FishGroup.class);
		_grade = map.getEnum("grade", FishGrade.class);
		_biteRate = map.getDouble("bite_rate");
		_guts = map.getDouble("guts");
		_lengthRate = map.getDouble("length_rate");
		_hpRegen = map.getDouble("hp_regen");
		_gutsCheckProbability = map.getDouble("guts_check_probability");
		_cheatingProb = map.getDouble("cheating_prob");
		_itemId = map.getInteger("item_id");
		_level = map.getInteger("level");
		_hp = map.getInteger("hp");
		_maxLength = map.getInteger("max_length");
		_startCombatTime = map.getInteger("start_combat_time");
		_combatDuration = map.getInteger("combat_duration");
		_gutsCheckTime = map.getInteger("guts_check_time");
	}
	
	/**
	 * Method getGroup.
	 * @return FishGroup
	 */
	public FishGroup getGroup()
	{
		return _group;
	}
	
	/**
	 * Method getGrade.
	 * @return FishGrade
	 */
	public FishGrade getGrade()
	{
		return _grade;
	}
	
	/**
	 * Method getBiteRate.
	 * @return double
	 */
	public double getBiteRate()
	{
		return _biteRate;
	}
	
	/**
	 * Method getGuts.
	 * @return double
	 */
	public double getGuts()
	{
		return _guts;
	}
	
	/**
	 * Method getLengthRate.
	 * @return double
	 */
	public double getLengthRate()
	{
		return _lengthRate;
	}
	
	/**
	 * Method getHpRegen.
	 * @return double
	 */
	public double getHpRegen()
	{
		return _hpRegen;
	}
	
	/**
	 * Method getGutsCheckProbability.
	 * @return double
	 */
	public double getGutsCheckProbability()
	{
		return _gutsCheckProbability;
	}
	
	/**
	 * Method getCheatingProb.
	 * @return double
	 */
	public double getCheatingProb()
	{
		return _cheatingProb;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getHp.
	 * @return int
	 */
	public int getHp()
	{
		return _hp;
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
	 * Method getMaxLength.
	 * @return int
	 */
	public int getMaxLength()
	{
		return _maxLength;
	}
	
	/**
	 * Method getStartCombatTime.
	 * @return int
	 */
	public int getStartCombatTime()
	{
		return _startCombatTime;
	}
	
	/**
	 * Method getCombatDuration.
	 * @return int
	 */
	public int getCombatDuration()
	{
		return _combatDuration;
	}
	
	/**
	 * Method getGutsCheckTime.
	 * @return int
	 */
	public int getGutsCheckTime()
	{
		return _gutsCheckTime;
	}
}

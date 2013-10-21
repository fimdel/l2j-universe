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

import gnu.trove.map.hash.TIntIntHashMap;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.model.Skill;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CubicTemplate
{
	/**
	 * @author Mobius
	 */
	public static class SkillInfo
	{
		/**
		 * Field _skill.
		 */
		private final Skill _skill;
		/**
		 * Field _chance.
		 */
		private final int _chance;
		/**
		 * Field _actionType.
		 */
		private final ActionType _actionType;
		/**
		 * Field _canAttackDoor.
		 */
		private final boolean _canAttackDoor;
		/**
		 * Field _chanceList.
		 */
		private final TIntIntHashMap _chanceList;
		
		/**
		 * Constructor for SkillInfo.
		 * @param skill Skill
		 * @param chance int
		 * @param actionType ActionType
		 * @param canAttackDoor boolean
		 * @param set TIntIntHashMap
		 */
		public SkillInfo(Skill skill, int chance, ActionType actionType, boolean canAttackDoor, TIntIntHashMap set)
		{
			_skill = skill;
			_chance = chance;
			_actionType = actionType;
			_canAttackDoor = canAttackDoor;
			_chanceList = set;
		}
		
		/**
		 * Method getChance.
		 * @return int
		 */
		public int getChance()
		{
			return _chance;
		}
		
		/**
		 * Method getActionType.
		 * @return ActionType
		 */
		public ActionType getActionType()
		{
			return _actionType;
		}
		
		/**
		 * Method getSkill.
		 * @return Skill
		 */
		public Skill getSkill()
		{
			return _skill;
		}
		
		/**
		 * Method isCanAttackDoor.
		 * @return boolean
		 */
		public boolean isCanAttackDoor()
		{
			return _canAttackDoor;
		}
		
		/**
		 * Method getChance.
		 * @param a int
		 * @return int
		 */
		public int getChance(int a)
		{
			return _chanceList.get(a);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static enum ActionType
	{
		/**
		 * Field ATTACK.
		 */
		ATTACK,
		/**
		 * Field DEBUFF.
		 */
		DEBUFF,
		/**
		 * Field CANCEL.
		 */
		CANCEL,
		/**
		 * Field HEAL.
		 */
		HEAL
	}
	
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _delay.
	 */
	private final int _delay;
	/**
	 * Field _skills.
	 */
	private final List<Map.Entry<Integer, List<SkillInfo>>> _skills = new ArrayList<>(3);
	
	/**
	 * Constructor for CubicTemplate.
	 * @param id int
	 * @param level int
	 * @param delay int
	 */
	public CubicTemplate(int id, int level, int delay)
	{
		_id = id;
		_level = level;
		_delay = delay;
	}
	
	/**
	 * Method putSkills.
	 * @param chance int
	 * @param skill List<SkillInfo>
	 */
	public void putSkills(int chance, List<SkillInfo> skill)
	{
		_skills.add(new AbstractMap.SimpleImmutableEntry<>(chance, skill));
	}
	
	/**
	 * Method getSkills.
	 * @return Iterable<Map.Entry<Integer,List<SkillInfo>>>
	 */
	public Iterable<Map.Entry<Integer, List<SkillInfo>>> getSkills()
	{
		return _skills;
	}
	
	/**
	 * Method getDelay.
	 * @return int
	 */
	public int getDelay()
	{
		return _delay;
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

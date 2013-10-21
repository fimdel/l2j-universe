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
package lineage2.gameserver.stats.triggers;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.conditions.Condition;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TriggerInfo extends Skill.AddedSkill
{
	/**
	 * Field _type.
	 */
	private final TriggerType _type;
	/**
	 * Field _chance.
	 */
	private final double _chance;
	/**
	 * Field _conditions.
	 */
	private Condition[] _conditions = Condition.EMPTY_ARRAY;
	
	/**
	 * Constructor for TriggerInfo.
	 * @param id int
	 * @param level int
	 * @param type TriggerType
	 * @param chance double
	 */
	public TriggerInfo(int id, int level, TriggerType type, double chance)
	{
		super(id, level);
		_type = type;
		_chance = chance;
	}
	
	/**
	 * Method addCondition.
	 * @param c Condition
	 */
	public final void addCondition(Condition c)
	{
		_conditions = ArrayUtils.add(_conditions, c);
	}
	
	/**
	 * Method checkCondition.
	 * @param actor Creature
	 * @param target Creature
	 * @param aimTarget Creature
	 * @param owner Skill
	 * @param damage double
	 * @return boolean
	 */
	public boolean checkCondition(Creature actor, Creature target, Creature aimTarget, Skill owner, double damage)
	{
		if (getSkill().checkTarget(actor, aimTarget, aimTarget, false, false) != null)
		{
			return false;
		}
		Env env = new Env();
		env.character = actor;
		env.skill = owner;
		env.target = target;
		env.value = damage;
		for (Condition c : _conditions)
		{
			if (!c.test(env))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method getType.
	 * @return TriggerType
	 */
	public TriggerType getType()
	{
		return _type;
	}
	
	/**
	 * Method getChance.
	 * @return double
	 */
	public double getChance()
	{
		return _chance;
	}
}

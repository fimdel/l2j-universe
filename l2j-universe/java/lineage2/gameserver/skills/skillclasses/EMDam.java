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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EMDam extends Skill
{
	/**
	 * Field TRUE_ELEMENTS.
	 */
	private static final int[] TRUE_ELEMENTS =
	{
		11007,
		11008,
		11009,
		11010
	};
	
	/**
	 * Constructor for EMDam.
	 * @param set StatsSet
	 */
	public EMDam(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
		{
			return false;
		}
		if (!activeChar.getEffectList().containEffectFromSkills(TRUE_ELEMENTS))
		{
			if (activeChar.isPlayer())
			{
				activeChar.sendMessage("No Stance Activated...");
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		int sps = isSSPossible() ? isMagic() ? activeChar.getChargedSpiritShot() : activeChar.getChargedSoulShot() ? 2 : 0 : 0;
		Creature realTarget;
		boolean reflected;
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (target.isDead())
				{
					continue;
				}
				reflected = target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;
				Formulas.AttackInfo info = Formulas.calcMagicDam(activeChar, realTarget, this, sps);
				if (info.damage >= 1)
				{
					realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
				}
				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}
		}
		if (isSuicideAttack())
		{
			activeChar.doDie(null);
		}
		else if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}

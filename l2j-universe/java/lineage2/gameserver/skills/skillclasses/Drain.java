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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Drain extends Skill
{
	/**
	 * Field _absorbAbs.
	 */
	private final double _absorbAbs;
	
	/**
	 * Constructor for Drain.
	 * @param set StatsSet
	 */
	public Drain(StatsSet set)
	{
		super(set);
		_absorbAbs = set.getDouble("absorbAbs", 0.f);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		int sps = isSSPossible() ? activeChar.getChargedSpiritShot() : 0;
		boolean ss = isSSPossible() && activeChar.getChargedSoulShot();
		Creature realTarget;
		boolean reflected;
		final boolean corpseSkill = _targetType == SkillTargetType.TARGET_CORPSE;
		for (Creature target : targets)
		{
			if (target != null)
			{
				reflected = !corpseSkill && target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;
				if ((getPower() > 0) || (_absorbAbs > 0))
				{
					if (realTarget.isDead() && !corpseSkill)
					{
						continue;
					}
					double hp = 0.;
					double targetHp = realTarget.getCurrentHp();
					if (!corpseSkill)
					{
						double damage, reflectableDamage = 0;
						if (isMagic())
						{
							AttackInfo info = Formulas.calcMagicDam(activeChar, realTarget, this, sps);
							damage = info.damage;
							reflectableDamage = info.reflectableDamage;
						}
						else
						{
							AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, false, ss, false);
							damage = info.damage;
							reflectableDamage = info.reflectableDamage;
							if (info.lethal_dmg > 0)
							{
								realTarget.reduceCurrentHp(info.lethal_dmg, reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
							}
						}
						double targetCP = realTarget.getCurrentCp();
						if ((damage > targetCP) || !realTarget.isPlayer())
						{
							hp = (damage - targetCP) * _absorbPart;
						}
						realTarget.reduceCurrentHp(damage, reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
						if (!reflected)
						{
							realTarget.doCounterAttack(this, activeChar, false);
						}
					}
					if ((_absorbAbs == 0) && (_absorbPart == 0))
					{
						continue;
					}
					hp += _absorbAbs;
					if ((hp > targetHp) && !corpseSkill)
					{
						hp = targetHp;
					}
					double addToHp = Math.max(0, Math.min(hp, ((activeChar.calcStat(Stats.HP_LIMIT, null, null) * activeChar.getMaxHp()) / 100.) - activeChar.getCurrentHp()));
					if ((addToHp > 0) && !activeChar.isHealBlocked())
					{
						activeChar.setCurrentHp(activeChar.getCurrentHp() + addToHp, false);
					}
					if (realTarget.isDead() && corpseSkill && realTarget.isNpc())
					{
						activeChar.getAI().setAttackTarget(null);
						((NpcInstance) realTarget).endDecayTask();
					}
				}
				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}
		}
		if (isMagic() ? sps != 0 : ss)
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}

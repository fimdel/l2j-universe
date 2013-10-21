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

import java.util.Collections;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.EffectsComparator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StealBuff extends Skill
{
	/**
	 * Field _stealCount.
	 */
	private final int _stealCount;
	
	/**
	 * Constructor for StealBuff.
	 * @param set StatsSet
	 */
	public StealBuff(StatsSet set)
	{
		super(set);
		_stealCount = set.getInteger("stealCount", 1);
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
		if ((target == null) || !target.isPlayer())
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (calcStealChance(target, activeChar))
				{
					int stealCount = Rnd.get(1, _stealCount);
					int counter = 0;
					if (!target.isPlayer())
					{
						continue;
					}
					List<Effect> effectsList = target.getEffectList().getAllEffects();
					Collections.sort(effectsList, EffectsComparator.getInstance());
					Collections.reverse(effectsList);
					for (Effect e : effectsList)
					{
						if (counter < stealCount)
						{
							if (canSteal(e))
							{
								Effect stolenEffect = cloneEffect(activeChar, e);
								if (stolenEffect != null)
								{
									activeChar.getEffectList().addEffect(stolenEffect);
								}
								e.exit();
								counter++;
							}
						}
						else
						{
							break;
						}
					}
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addString(target.getName()).addSkillName(getId(), getLevel()));
					continue;
				}
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
	
	/**
	 * Method canSteal.
	 * @param e Effect
	 * @return boolean
	 */
	private boolean canSteal(Effect e)
	{
		return (e != null) && e.isInUse() && e.isCancelable() && !e.getSkill().isToggle() && !e.getSkill().isPassive() && !e.getSkill().isOffensive() && (e.getEffectType() != EffectType.Vitality) && !e.getTemplate()._applyOnCaster;
	}
	
	/**
	 * Method calcStealChance.
	 * @param effected Creature
	 * @param effector Creature
	 * @return boolean
	 */
	private boolean calcStealChance(Creature effected, Creature effector)
	{
		double cancel_res_multiplier = effected.calcStat(Stats.CANCEL_RESIST, 1, null, null);
		int dml = effector.getLevel() - effected.getLevel();
		double prelimChance = (dml + 50) * (1 - (cancel_res_multiplier * .01));
		return Rnd.chance(prelimChance);
	}
	
	/**
	 * Method cloneEffect.
	 * @param cha Creature
	 * @param eff Effect
	 * @return Effect
	 */
	private Effect cloneEffect(Creature cha, Effect eff)
	{
		Skill skill = eff.getSkill();
		for (EffectTemplate et : skill.getEffectTemplates())
		{
			Effect effect = et.getEffect(new Env(cha, cha, skill));
			if (effect != null)
			{
				effect.setCount(eff.getCount());
				effect.setPeriod(eff.getCount() == 1 ? eff.getPeriod() - eff.getTime() : eff.getPeriod());
				return effect;
			}
		}
		return null;
	}
}

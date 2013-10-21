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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ChainHeal extends Skill
{
	/**
	 * Field _healPercents.
	 */
	private final int[] _healPercents;
	/**
	 * Field _healRadius.
	 */
	private final int _healRadius;
	/**
	 * Field _maxTargets.
	 */
	private final int _maxTargets;
	
	/**
	 * Constructor for ChainHeal.
	 * @param set StatsSet
	 */
	public ChainHeal(StatsSet set)
	{
		super(set);
		_healRadius = set.getInteger("healRadius", 350);
		String[] params = set.getString("healPercents", "").split(";");
		_maxTargets = params.length;
		_healPercents = new int[params.length];
		for (int i = 0; i < params.length; i++)
		{
			_healPercents[i] = Integer.parseInt(params[i]);
		}
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		int curTarget = 0;
		for (Creature target : targets)
		{
			if (target == null)
			{
				continue;
			}
			getEffects(activeChar, target, getActivateRate() > 0, false);
			double hp = (_healPercents[curTarget] * target.getMaxHp()) / 100.;
			double addToHp = Math.max(0, Math.min(hp, ((target.calcStat(Stats.HP_LIMIT, null, null) * target.getMaxHp()) / 100.) - target.getCurrentHp()));
			if (addToHp > 0)
			{
				target.setCurrentHp(addToHp + target.getCurrentHp(), false);
			}
			if (target.isPlayer())
			{
				if (activeChar != target)
				{
					target.sendPacket(new SystemMessage(SystemMessage.XS2S_HP_HAS_BEEN_RESTORED_BY_S1).addString(activeChar.getName()).addNumber(Math.round(addToHp)));
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToHp)));
				}
			}
			curTarget++;
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
	
	/**
	 * Method getTargets.
	 * @param activeChar Creature
	 * @param aimingTarget Creature
	 * @param forceUse boolean
	 * @return List<Creature>
	 */
	@Override
	public List<Creature> getTargets(Creature activeChar, Creature aimingTarget, boolean forceUse)
	{
		List<Creature> result = new ArrayList<>();
		List<Creature> targets = aimingTarget.getAroundCharacters(_healRadius, 128);
		if ((targets == null) || targets.isEmpty())
		{
			return result;
		}
		List<HealTarget> healTargets = new ArrayList<>();
		healTargets.add(new HealTarget(-100.0D, aimingTarget));
		for (Creature target : targets)
		{
			if ((target == null) || target.isHealBlocked() || ((activeChar.getObjectId() != aimingTarget.getObjectId()) && (target.getObjectId() == activeChar.getObjectId())))
			{
				continue;
			}
			if (target.isAutoAttackable(activeChar))
			{
				continue;
			}
			double hpPercent = target.getCurrentHp() / target.getMaxHp();
			healTargets.add(new HealTarget(hpPercent, target));
		}
		HealTarget[] healTargetsArr = new HealTarget[healTargets.size()];
		healTargets.toArray(healTargetsArr);
		Arrays.sort(healTargetsArr, new Comparator<HealTarget>()
		{
			@Override
			public int compare(HealTarget o1, HealTarget o2)
			{
				if ((o1 == null) || (o2 == null))
				{
					return 0;
				}
				if (o1.getHpPercent() < o2.getHpPercent())
				{
					return -1;
				}
				if (o1.getHpPercent() > o2.getHpPercent())
				{
					return 1;
				}
				return 0;
			}
		});
		int targetsCount = 0;
		for (HealTarget ht : healTargetsArr)
		{
			result.add(ht.getTarget());
			targetsCount++;
			if (targetsCount >= _maxTargets)
			{
				break;
			}
		}
		return result;
	}
	
	/**
	 * @author Mobius
	 */
	private static class HealTarget
	{
		/**
		 * Field hpPercent.
		 */
		private final double hpPercent;
		/**
		 * Field target.
		 */
		private final Creature target;
		
		/**
		 * Constructor for HealTarget.
		 * @param hpPercent double
		 * @param target Creature
		 */
		public HealTarget(double hpPercent, Creature target)
		{
			this.hpPercent = hpPercent;
			this.target = target;
		}
		
		/**
		 * Method getHpPercent.
		 * @return double
		 */
		public double getHpPercent()
		{
			return hpPercent;
		}
		
		/**
		 * Method getTarget.
		 * @return Creature
		 */
		public Creature getTarget()
		{
			return target;
		}
	}
}

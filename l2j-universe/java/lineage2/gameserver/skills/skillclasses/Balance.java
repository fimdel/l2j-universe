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
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Balance extends Skill
{
	/**
	 * Constructor for Balance.
	 * @param set StatsSet
	 */
	public Balance(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		double summaryCurrentHp = 0;
		int summaryMaximumHp = 0;
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (target.isAlikeDead())
				{
					continue;
				}
				summaryCurrentHp += target.getCurrentHp();
				summaryMaximumHp += target.getMaxHp();
			}
		}
		double percent = summaryCurrentHp / summaryMaximumHp;
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (target.isAlikeDead())
				{
					continue;
				}
				double hp = target.getMaxHp() * percent;
				if (hp > target.getCurrentHp())
				{
					double limit = (target.calcStat(Stats.HP_LIMIT, null, null) * target.getMaxHp()) / 100.;
					if (target.getCurrentHp() < limit)
					{
						target.setCurrentHp(Math.min(hp, limit), false);
					}
				}
				else
				{
					target.setCurrentHp(Math.max(1.01, hp), false);
				}
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}

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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CombatPointHeal extends Skill
{
	/**
	 * Field _ignoreCpEff.
	 */
	private final boolean _ignoreCpEff;
	
	/**
	 * Constructor for CombatPointHeal.
	 * @param set StatsSet
	 */
	public CombatPointHeal(StatsSet set)
	{
		super(set);
		_ignoreCpEff = set.getBool("ignoreCpEff", false);
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
				if (target.isDead() || target.isHealBlocked())
				{
					continue;
				}
				double maxNewCp = (_power * (!_ignoreCpEff ? target.calcStat(Stats.CPHEAL_EFFECTIVNESS, 100., activeChar, this) : 100.)) / 100.;
				double addToCp = Math.max(0, Math.min(maxNewCp, ((target.calcStat(Stats.CP_LIMIT, null, null) * target.getMaxCp()) / 100.) - target.getCurrentCp()));
				if (addToCp > 0)
				{
					target.setCurrentCp(addToCp + target.getCurrentCp());
				}
				target.sendPacket(new SystemMessage(SystemMessage.S1_CPS_WILL_BE_RESTORED).addNumber((long) addToCp));
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}

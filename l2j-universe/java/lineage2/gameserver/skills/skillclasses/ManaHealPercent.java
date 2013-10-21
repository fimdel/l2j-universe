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
public class ManaHealPercent extends Skill
{
	/**
	 * Field _ignoreMpEff.
	 */
	private final boolean _ignoreMpEff;
	
	/**
	 * Constructor for ManaHealPercent.
	 * @param set StatsSet
	 */
	public ManaHealPercent(StatsSet set)
	{
		super(set);
		_ignoreMpEff = set.getBool("ignoreMpEff", true);
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
				getEffects(activeChar, target, getActivateRate() > 0, false);
				double mp = (_power * target.getMaxMp()) / 100.;
				double newMp = (mp * (!_ignoreMpEff ? target.calcStat(Stats.MANAHEAL_EFFECTIVNESS, 100., activeChar, this) : 100.)) / 100.;
				double addToMp = Math.max(0, Math.min(newMp, ((target.calcStat(Stats.MP_LIMIT, null, null) * target.getMaxMp()) / 100.) - target.getCurrentMp()));
				if (addToMp > 0)
				{
					target.setCurrentMp(target.getCurrentMp() + addToMp);
				}
				if (target.isPlayer())
				{
					if (activeChar != target)
					{
						target.sendPacket(new SystemMessage(SystemMessage.XS2S_MP_HAS_BEEN_RESTORED_BY_S1).addString(activeChar.getName()).addNumber(Math.round(addToMp)));
					}
					else
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_MPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToMp)));
					}
				}
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}

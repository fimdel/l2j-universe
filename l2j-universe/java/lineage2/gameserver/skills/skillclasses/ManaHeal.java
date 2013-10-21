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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManaHeal extends Skill
{
	/**
	 * Field _ignoreMpEff.
	 */
	private final boolean _ignoreMpEff;
	
	/**
	 * Constructor for ManaHeal.
	 * @param set StatsSet
	 */
	public ManaHeal(StatsSet set)
	{
		super(set);
		_ignoreMpEff = set.getBool("ignoreMpEff", false);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		double mp = _power;
		int sps = isSSPossible() ? activeChar.getChargedSpiritShot() : 0;
		if ((sps > 0) && Config.MANAHEAL_SPS_BONUS)
		{
			mp *= sps == 2 ? 1.5 : 1.3;
		}
		for (Creature target : targets)
		{
			if (target.isHealBlocked())
			{
				continue;
			}
			double newMp = activeChar == target ? mp : Math.min(mp * 1.7, (mp * (!_ignoreMpEff ? target.calcStat(Stats.MANAHEAL_EFFECTIVNESS, 100., activeChar, this) : 100.)) / 100.);
			if ((getMagicLevel() > 0) && (activeChar != target))
			{
				int diff = target.getLevel() - getMagicLevel();
				if (diff > 5)
				{
					if (diff < 20)
					{
						newMp = (newMp / 100) * (100 - (diff * 5));
					}
					else
					{
						newMp = 0;
					}
				}
			}
			if (newMp == 0)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_FAILED).addSkillName(_id, getDisplayLevel()));
				getEffects(activeChar, target, getActivateRate() > 0, false);
				continue;
			}
			double addToMp = Math.max(0, Math.min(newMp, ((target.calcStat(Stats.MP_LIMIT, null, null) * target.getMaxMp()) / 100.) - target.getCurrentMp()));
			if (addToMp > 0)
			{
				target.setCurrentMp(addToMp + target.getCurrentMp());
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
			getEffects(activeChar, target, getActivateRate() > 0, false);
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}

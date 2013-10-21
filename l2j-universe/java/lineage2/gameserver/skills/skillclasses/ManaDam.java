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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManaDam extends Skill
{
	/**
	 * Constructor for ManaDam.
	 * @param set StatsSet
	 */
	public ManaDam(StatsSet set)
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
		int sps = 0;
		if (isSSPossible())
		{
			sps = activeChar.getChargedSpiritShot();
		}
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (target.isDead())
				{
					continue;
				}
				int magicLevel = getMagicLevel() == 0 ? activeChar.getLevel() : getMagicLevel();
				int landRate = Rnd.get(30, 100);
				landRate *= target.getLevel();
				landRate /= magicLevel;
				if (Rnd.chance(landRate))
				{
					double mAtk = activeChar.getMAtk(target, this);
					if (sps == 2)
					{
						mAtk *= 4;
					}
					else if (sps == 1)
					{
						mAtk *= 2;
					}
					double mDef = target.getMDef(activeChar, this);
					if (mDef < 1.)
					{
						mDef = 1.;
					}
					double damage = (Math.sqrt(mAtk) * this.getPower() * (target.getMaxMp() / 97)) / mDef;
					boolean crit = Formulas.calcMCrit(activeChar.getMagicCriticalRate(target, this));
					if (crit)
					{
						activeChar.sendPacket(Msg.MAGIC_CRITICAL_HIT);
						damage *= 2.0;
						damage += activeChar.getMagicCriticalDmg(target, this);
					}
					target.reduceCurrentMp(damage, activeChar);
				}
				else
				{
					SystemMessage msg = new SystemMessage(SystemMessage.C1_RESISTED_C2S_MAGIC).addName(target).addName(activeChar);
					activeChar.sendPacket(msg);
					target.sendPacket(msg);
					target.reduceCurrentHp(1., 0, activeChar, this, true, true, false, true, false, false, true);
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

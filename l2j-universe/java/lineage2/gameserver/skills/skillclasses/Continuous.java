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
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Continuous extends Skill
{
	/**
	 * Field _lethal1.
	 */
	private final int _lethal1;
	/**
	 * Field _lethal2.
	 */
	private final int _lethal2;
	
	/**
	 * Constructor for Continuous.
	 * @param set StatsSet
	 */
	public Continuous(StatsSet set)
	{
		super(set);
		_lethal1 = set.getInteger("lethal1", 0);
		_lethal2 = set.getInteger("lethal2", 0);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		Creature realTarget;
		boolean reflected;
		for (Creature target : targets)
		{
			if (target != null)
			{
				if ((getSkillType() == Skill.SkillType.BUFF) && (target != activeChar))
				{
					if (target.isCursedWeaponEquipped() || activeChar.isCursedWeaponEquipped())
					{
						continue;
					}
				}
				reflected = target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;
				double mult = 0.01 * realTarget.calcStat(Stats.DEATH_VULNERABILITY, activeChar, this);
				double lethal1 = _lethal1 * mult;
				double lethal2 = _lethal2 * mult;
				if ((lethal1 > 0) && Rnd.chance(lethal1))
				{
					if (realTarget.isPlayer())
					{
						realTarget.reduceCurrentHp(realTarget.getCurrentCp(), 0, activeChar, this, true, true, false, true, false, false, true);
						realTarget.sendPacket(SystemMsg.LETHAL_STRIKE);
						activeChar.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
					}
					else if (realTarget.isNpc() && !realTarget.isLethalImmune())
					{
						realTarget.reduceCurrentHp(realTarget.getCurrentHp() / 2, 0, activeChar, this, true, true, false, true, false, false, true);
						activeChar.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
					}
				}
				else if ((lethal2 > 0) && Rnd.chance(lethal2))
				{
					if (realTarget.isPlayer())
					{
						realTarget.reduceCurrentHp((realTarget.getCurrentHp() + realTarget.getCurrentCp()) - 1, 0, activeChar, this, true, true, false, true, false, false, true);
						realTarget.sendPacket(SystemMsg.LETHAL_STRIKE);
						activeChar.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
					}
					else if (realTarget.isNpc() && !realTarget.isLethalImmune())
					{
						realTarget.reduceCurrentHp(realTarget.getCurrentHp() - 1, 0, activeChar, this, true, true, false, true, false, false, true);
						activeChar.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
					}
				}
				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}
		}
		if (isSSPossible())
		{
			if (!(Config.SAVING_SPS && (_skillType == SkillType.BUFF)))
			{
				activeChar.unChargeShots(isMagic());
			}
		}
	}
}

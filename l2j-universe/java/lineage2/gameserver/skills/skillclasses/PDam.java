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
import lineage2.gameserver.network.serverpackets.FinishRotating;
import lineage2.gameserver.network.serverpackets.StartRotating;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PDam extends Skill
{
	/**
	 * Field _onCrit.
	 */
	private final boolean _onCrit;
	/**
	 * Field _directHp.
	 */
	private final boolean _directHp;
	/**
	 * Field _turner.
	 */
	private final boolean _turner;
	/**
	 * Field _blow.
	 */
	private final boolean _blow;
	
	/**
	 * Constructor for PDam.
	 * @param set StatsSet
	 */
	public PDam(StatsSet set)
	{
		super(set);
		_onCrit = set.getBool("onCrit", false);
		_directHp = set.getBool("directHp", false);
		_turner = set.getBool("turner", false);
		_blow = set.getBool("blow", false);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		boolean ss = activeChar.getChargedSoulShot() && isSSPossible();
		Creature realTarget;
		boolean reflected;
		for (Creature target : targets)
		{
			if ((target != null) && !target.isDead())
			{
				if (_turner && !target.isInvul())
				{
					target.broadcastPacket(new StartRotating(target, target.getHeading(), 1, 65535));
					target.broadcastPacket(new FinishRotating(target, activeChar.getHeading(), 65535));
					target.setHeading(activeChar.getHeading());
					target.sendPacket(new SystemMessage(SystemMessage.S1_S2S_EFFECT_CAN_BE_FELT).addSkillName(_displayId, _displayLevel));
				}
				reflected = target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;
				AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, _blow, ss, _onCrit);
				if (info.lethal_dmg > 0)
				{
					realTarget.reduceCurrentHp(info.lethal_dmg, info.reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
				}
				if (!info.miss || (info.damage >= 1))
				{
					realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, info.lethal ? false : _directHp, true, false, false, getPower() != 0);
				}
				if (!reflected)
				{
					realTarget.doCounterAttack(this, activeChar, _blow);
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

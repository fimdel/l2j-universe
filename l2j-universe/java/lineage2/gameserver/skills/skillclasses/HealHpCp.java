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
import lineage2.gameserver.model.instances.residences.SiegeFlagInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HealHpCp extends Skill
{
	/**
	 * Field _ignoreHpEff.
	 */
	private final boolean _ignoreHpEff;
	/**
	 * Field _staticPower.
	 */
	private final boolean _staticPower;
	
	/**
	 * Constructor for HealHpCp.
	 * @param set StatsSet
	 */
	public HealHpCp(StatsSet set)
	{
		super(set);
		_ignoreHpEff = set.getBool("ignoreHpEff", false);
		_staticPower = set.getBool("staticPower", isHandler());
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
		if ((target == null) || target.isDoor() || (target instanceof SiegeFlagInstance))
		{
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
		double hp = _power;
		if (!_staticPower)
		{
			hp += 0.1 * _power * Math.sqrt(activeChar.getMAtk(null, this) / 333);
		}
		int sps = isSSPossible() && (getHpConsume() == 0) ? activeChar.getChargedSpiritShot() : 0;
		if (sps == 2)
		{
			hp *= 1.5;
		}
		else if (sps == 1)
		{
			hp *= 1.3;
		}
		if ((activeChar.getSkillMastery(getId()) == 3) && !_staticPower)
		{
			activeChar.removeSkillMastery(getId());
			hp *= 3.;
		}
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (target.isDead() || target.isHealBlocked())
				{
					continue;
				}
				if (target != activeChar)
				{
					if (target.isPlayer() && target.isCursedWeaponEquipped())
					{
						continue;
					}
					else if (activeChar.isPlayer() && activeChar.isCursedWeaponEquipped())
					{
						continue;
					}
				}
				double addToHp = 0;
				double addToHpOld = 0;
				double addToCp = 0;
				if (_staticPower)
				{
					addToHp = _power;
				}
				else
				{
					addToHp = (hp * (!_ignoreHpEff ? target.calcStat(Stats.HEAL_EFFECTIVNESS, 100., activeChar, this) : 100.)) / 100.;
					addToHp = activeChar.calcStat(Stats.HEAL_POWER, addToHp, target, this);
				}
				addToHpOld = addToHp;
				addToHp = Math.max(0, Math.min(addToHp, ((target.calcStat(Stats.HP_LIMIT, null, null) * target.getMaxHp()) / 100.) - target.getCurrentHp()));
				if (addToHp > 0)
				{
					target.setCurrentHp(addToHp + target.getCurrentHp(), false);
				}
				addToCp = addToHpOld - addToHp;
				addToCp = Math.max(0, Math.min(addToCp, ((target.calcStat(Stats.CP_LIMIT, null, null) * target.getMaxCp()) / 100.) - target.getCurrentCp()));
				if (addToCp > 0)
				{
					target.setCurrentCp(addToCp + target.getCurrentCp());
					
				}
				if (target.isPlayer())
				{
					if (activeChar == target)
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToHp)));
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CPS_WILL_BE_RESTORED).addNumber(Math.round(addToCp)));
					}
					else
					{
						target.sendPacket(new SystemMessage(SystemMessage.XS2S_HP_HAS_BEEN_RESTORED_BY_S1).addString(activeChar.getName()).addNumber(Math.round(addToHp)));
						target.sendPacket(new SystemMessage(SystemMessage.S1_WILL_RESTORE_S2S_CP).addString(activeChar.getName()).addNumber(Math.round(addToCp)));
					}
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

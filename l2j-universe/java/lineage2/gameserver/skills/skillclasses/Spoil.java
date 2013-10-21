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
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Spoil extends Skill
{
	/**
	 * Constructor for Spoil.
	 * @param set StatsSet
	 */
	public Spoil(StatsSet set)
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
		if (!activeChar.isPlayer())
		{
			return;
		}
		int ss = isSSPossible() ? (isMagic() ? activeChar.getChargedSpiritShot() : activeChar.getChargedSoulShot() ? 2 : 0) : 0;
		if ((ss > 0) && (getPower() > 0))
		{
			activeChar.unChargeShots(false);
		}
		for (Creature target : targets)
		{
			if ((target != null) && !target.isDead())
			{
				if (target.isMonster())
				{
					if (((MonsterInstance) target).isSpoiled())
					{
						activeChar.sendPacket(Msg.ALREADY_SPOILED);
					}
					else
					{
						MonsterInstance monster = (MonsterInstance) target;
						boolean success;
						if (!Config.ALT_SPOIL_FORMULA)
						{
							int monsterLevel = monster.getLevel();
							int modifier = Math.abs(monsterLevel - activeChar.getLevel());
							double rateOfSpoil = Config.BASE_SPOIL_RATE;
							if (modifier > 8)
							{
								rateOfSpoil = rateOfSpoil - ((rateOfSpoil * (modifier - 8) * 9) / 100);
							}
							rateOfSpoil = (rateOfSpoil * getMagicLevel()) / monsterLevel;
							if (rateOfSpoil < Config.MINIMUM_SPOIL_RATE)
							{
								rateOfSpoil = Config.MINIMUM_SPOIL_RATE;
							}
							else if (rateOfSpoil > 99.)
							{
								rateOfSpoil = 99.;
							}
							if (((Player) activeChar).isGM())
							{
								activeChar.sendMessage(new CustomMessage("lineage2.gameserver.skills.skillclasses.Spoil.Chance", (Player) activeChar).addNumber((long) rateOfSpoil));
							}
							success = Rnd.chance(rateOfSpoil);
						}
						else
						{
							success = Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate());
						}
						if (success && monster.setSpoiled((Player) activeChar))
						{
							activeChar.sendPacket(Msg.THE_SPOIL_CONDITION_HAS_BEEN_ACTIVATED);
						}
						else
						{
							activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_FAILED).addSkillName(_id, getDisplayLevel()));
						}
					}
				}
				if (getPower() > 0)
				{
					double damage, reflectableDamage = 0;
					if (isMagic())
					{
						AttackInfo info = Formulas.calcMagicDam(activeChar, target, this, ss);
						damage = info.damage;
						reflectableDamage = info.reflectableDamage;
					}
					else
					{
						AttackInfo info = Formulas.calcPhysDam(activeChar, target, this, false, false, ss > 0, false);
						damage = info.damage;
						reflectableDamage = info.reflectableDamage;
						if (info.lethal_dmg > 0)
						{
							target.reduceCurrentHp(info.lethal_dmg, reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
						}
					}
					target.reduceCurrentHp(damage, reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
					target.doCounterAttack(this, activeChar, false);
				}
				getEffects(activeChar, target, false, false);
				target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, Math.max(_effectPoint, 1));
			}
		}
	}
}

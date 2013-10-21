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
package lineage2.gameserver.stats;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.base.BaseStats;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.SkillTrait;
import lineage2.gameserver.model.instances.ReflectionBossInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.tables.AttributeDamageResistTable;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Formulas
{
	/**
	 * Method calcHpRegen.
	 * @param cha Creature
	 * @return double
	 */
	
	public static double calcHpRegen(Creature cha)
	{
		double init;
		if (cha.isPlayer())
		{
			init = cha.getPlayer().getTemplate().getBaseHpReg(cha.getLevel());
		}
		else
		{
			init = cha.getTemplate().getBaseHpReg();
		}
		if (cha.isPlayable())
		{
			init *= BaseStats.CON.calcBonus(cha);
			if (cha.isServitor())
			{
				init *= 2;
			}
		}
		return cha.calcStat(Stats.REGENERATE_HP_RATE, init);
	}
	
	/**
	 * Method calcMpRegen.
	 * @param cha Creature
	 * @return double
	 */
	public static double calcMpRegen(Creature cha)
	{
		double init;
		if (cha.isPlayer())
		{
			init = cha.getPlayer().getTemplate().getBaseMpReg(cha.getLevel());
		}
		else
		{
			init = cha.getTemplate().getBaseMpReg();
		}
		if (cha.isPlayable())
		{
			init *= BaseStats.MEN.calcBonus(cha);
			if (cha.isServitor())
			{
				init *= 2;
			}
		}
		return cha.calcStat(Stats.REGENERATE_MP_RATE, init);
	}
	
	/**
	 * Method calcCpRegen.
	 * @param cha Creature
	 * @return double
	 */
	public static double calcCpRegen(Creature cha)
	{
		double init = 0.0;
		if (cha.isPlayer())
		{
			init = cha.getPlayer().getTemplate().getBaseCpReg(cha.getLevel()) * BaseStats.CON.calcBonus(cha);
		}
		return cha.calcStat(Stats.REGENERATE_CP_RATE, init);
	}
	
	/**
	 * @author Mobius
	 */
	public static class AttackInfo
	{
		/**
		 * Field reflectableDamage.
		 */
		public double reflectableDamage = 0;
		/**
		 * Field damage.
		 */
		public double damage = 0;
		/**
		 * Field defence.
		 */
		public double defence = 0;
		/**
		 * Field crit_static.
		 */
		public double crit_static = 0;
		/**
		 * Field death_rcpt.
		 */
		public double death_rcpt = 0;
		/**
		 * Field lethal1.
		 */
		public double lethal1 = 0;
		/**
		 * Field lethal2.
		 */
		public double lethal2 = 0;
		/**
		 * Field lethal_dmg.
		 */
		public double lethal_dmg = 0;
		/**
		 * Field crit.
		 */
		public boolean crit = false;
		/**
		 * Field shld.
		 */
		public boolean shld = false;
		/**
		 * Field lethal.
		 */
		public boolean lethal = false;
		/**
		 * Field miss.
		 */
		public boolean miss = false;
	}
	
	/**
	 * Method calcPhysDam.
	 * @param attacker Creature
	 * @param target Creature
	 * @param skill Skill
	 * @param dual boolean
	 * @param blow boolean
	 * @param ss boolean
	 * @param onCrit boolean
	 * @return AttackInfo
	 */
	public static AttackInfo calcPhysDam(Creature attacker, Creature target, Skill skill, boolean dual, boolean blow, boolean ss, boolean onCrit)
	{
		AttackInfo info = new AttackInfo();
		info.damage = attacker.getPAtk(target);
		info.defence = target.getPDef(attacker);
		info.crit_static = attacker.calcStat(Stats.CRITICAL_DAMAGE_STATIC, target, skill);
		info.death_rcpt = 0.01 * target.calcStat(Stats.DEATH_VULNERABILITY, attacker, skill);
		info.lethal1 = skill == null ? 0 : skill.getLethal1() * info.death_rcpt;
		info.lethal2 = skill == null ? 0 : skill.getLethal2() * info.death_rcpt;
		info.crit = Rnd.chance(calcCrit(attacker, target, skill, blow));
		info.shld = ((skill == null) || !skill.getShieldIgnore()) && Formulas.calcShldUse(attacker, target);
		info.lethal = false;
		info.miss = false;
		boolean isPvP = attacker.isPlayable() && target.isPlayable();
		if (info.shld)
		{
			info.defence += target.getShldDef();
		}
		info.defence = Math.max(info.defence, 1);
		if (skill != null)
		{
			if (!blow && !target.isLethalImmune())
			{
				if (Rnd.chance(info.lethal1))
				{
					if (target.isPlayer())
					{
						info.lethal = true;
						info.lethal_dmg = target.getCurrentCp();
						target.sendPacket(Msg.CP_DISAPPEARS_WHEN_HIT_WITH_A_HALF_KILL_SKILL);
					}
					else
					{
						info.lethal_dmg = target.getCurrentHp() / 2;
					}
					attacker.sendPacket(Msg.HALF_KILL);
				}
				else if (Rnd.chance(info.lethal2))
				{
					if (target.isPlayer())
					{
						info.lethal = true;
						info.lethal_dmg = (target.getCurrentHp() + target.getCurrentCp()) - 1.1;
						target.sendPacket(SystemMsg.LETHAL_STRIKE);
					}
					else
					{
						info.lethal_dmg = target.getCurrentHp() - 1;
					}
					attacker.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
				}
			}
			if (skill.getPower(target) == 0)
			{
				info.damage = 0;
				return info;
			}
			if (blow && !skill.isBehind() && ss)
			{
				info.damage *= 2.04;
			}
			info.damage += Math.max(0., skill.getPower(target) * attacker.calcStat(Stats.SKILL_POWER, 1, null, null));
			if (blow && skill.isBehind() && ss)
			{
				info.damage *= 1.5;
			}
			if (!skill.isChargeBoost())
			{
				info.damage *= 1 + (((Rnd.get() * attacker.getRandomDamage() * 2) - attacker.getRandomDamage()) / 100);
			}
			if (blow)
			{
				info.damage *= 0.01 * attacker.calcStat(Stats.CRITICAL_DAMAGE, target, skill);
				info.damage = target.calcStat(Stats.CRIT_DAMAGE_RECEPTIVE, info.damage, attacker, skill);
				info.damage += 6.1 * info.crit_static;
			}
			if (skill.isChargeBoost())
			{
				info.damage *= 0.8 + (0.2 * attacker.getIncreasedForce());
			}
			if (skill.getSkillType() == SkillType.CHARGE)
			{
				info.damage *= 2;
			}
			else if (skill.isSoulBoost())
			{
				info.damage *= 1.0 + (0.06 * Math.min(attacker.getConsumedSouls(), 5));
			}
			info.damage *= 1.10113;
			if (info.crit)
			{
				if (skill.isChargeBoost() || (skill.getSkillType() == SkillType.CHARGE))
				{
					info.damage *= 2.;
				}
				else
				{
					info.damage = 2 * target.calcStat(Stats.CRIT_DAMAGE_RECEPTIVE, info.damage, attacker, skill);
				}
			}
		}
		else
		{
			info.damage *= 1 + (((Rnd.get() * attacker.getRandomDamage() * 2) - attacker.getRandomDamage()) / 100);
			if (dual)
			{
				info.damage /= 2.;
			}
			if (info.crit)
			{
				info.damage *= 0.01 * attacker.calcStat(Stats.CRITICAL_DAMAGE, target, skill);
				info.damage = 2 * target.calcStat(Stats.CRIT_DAMAGE_RECEPTIVE, info.damage, attacker, skill);
				info.damage += info.crit_static;
			}
		}
		if (info.crit)
		{
			int chance = attacker.getSkillLevel(Skill.SKILL_SOUL_MASTERY);
			if (chance > 0)
			{
				if (chance >= 21)
				{
					chance = 30;
				}
				else if (chance >= 15)
				{
					chance = 25;
				}
				else if (chance >= 9)
				{
					chance = 20;
				}
				else if (chance >= 4)
				{
					chance = 15;
				}
				if (Rnd.chance(chance))
				{
					attacker.setConsumedSouls(attacker.getConsumedSouls() + 1, null);
				}
			}
		}
		switch (PositionUtils.getDirectionTo(target, attacker))
		{
			case BEHIND:
				info.damage *= 1.2;
				break;
			case SIDE:
				info.damage *= 1.1;
				break;
			default:
				break;
		}
		if (ss)
		{
			info.damage *= blow ? 1.0 : 2.0;
		}
		info.damage *= 70. / info.defence;
		info.damage = attacker.calcStat(Stats.PHYSICAL_DAMAGE, info.damage, target, skill);
		info.damage += attacker.calcStat(Stats.PHYS_SKILL_DMG_STATIC, 1, target, skill);
		if (info.shld && Rnd.chance(5))
		{
			info.damage = 1;
		}
		if (isPvP)
		{
			if (skill == null)
			{
				info.damage *= attacker.calcStat(Stats.PVP_PHYS_DMG_BONUS, 1, null, null);
				info.damage /= target.calcStat(Stats.PVP_PHYS_DEFENCE_BONUS, 1, null, null);
			}
			else
			{
				info.damage *= attacker.calcStat(Stats.PVP_PHYS_SKILL_DMG_BONUS, 1, null, null);
				info.damage /= target.calcStat(Stats.PVP_PHYS_SKILL_DEFENCE_BONUS, 1, null, null);
			}
		}
		if (skill != null)
		{
			if (info.shld)
			{
				if (info.damage == 1)
				{
					target.sendPacket(SystemMsg.YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS);
				}
				else
				{
					target.sendPacket(SystemMsg.YOUR_SHIELD_DEFENSE_HAS_SUCCEEDED);
				}
			}
			if ((info.damage > 1) && !skill.hasEffects() && Rnd.chance(target.calcStat(Stats.PSKILL_EVASION, 0, attacker, skill)))
			{
				attacker.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addName(attacker));
				target.sendPacket(new SystemMessage(SystemMessage.C1_HAS_EVADED_C2S_ATTACK).addName(target).addName(attacker));
				info.damage = 0;
			}
			if ((info.damage > 1) && skill.isDeathlink())
			{
				info.damage *= 1.8 * (1.0 - attacker.getCurrentHpRatio());
			}
			if (onCrit && !calcBlow(attacker, target, skill))
			{
				info.miss = true;
				info.damage = 0;
				attacker.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addName(attacker));
			}
			if (blow)
			{
				if (Rnd.chance(info.lethal1))
				{
					if (target.isPlayer())
					{
						info.lethal = true;
						info.lethal_dmg = target.getCurrentCp();
						target.sendPacket(Msg.CP_DISAPPEARS_WHEN_HIT_WITH_A_HALF_KILL_SKILL);
					}
					else if (target.isLethalImmune())
					{
						info.damage *= 2;
					}
					else
					{
						info.lethal_dmg = target.getCurrentHp() / 2;
					}
					attacker.sendPacket(Msg.HALF_KILL);
				}
				else if (Rnd.chance(info.lethal2))
				{
					if (target.isPlayer())
					{
						info.lethal = true;
						info.lethal_dmg = (target.getCurrentHp() + target.getCurrentCp()) - 1.1;
						target.sendPacket(SystemMsg.LETHAL_STRIKE);
					}
					else if (target.isLethalImmune())
					{
						info.damage *= 3;
					}
					else
					{
						info.lethal_dmg = target.getCurrentHp() - 1;
					}
					attacker.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
				}
			}
			if(skill.isPowerModified())
			{
				double percentMod = skill.getWeaponModifiedPower(attacker);
				info.damage *= percentMod;
			}
			if (info.damage > 0)
			{
				attacker.displayGiveDamageMessage(target, (int) info.damage, info.crit || blow, false, false, false);
			}
			if (target.isStunned() && calcStunBreak(info.crit))
			{
				target.getEffectList().stopEffects(EffectType.Stun);
			}
			if (calcCastBreak(target, info.crit))
			{
				target.abortCast(false, true);
			}
		}
		double receiveDamageLimit = target.calcStat(Stats.RECEIVE_DAMAGE_LIMIT, 0, target, skill);
		info.reflectableDamage = info.damage;
		if ((receiveDamageLimit > 0) && (info.damage > receiveDamageLimit))
		{
			info.reflectableDamage = info.damage - receiveDamageLimit;
			info.damage = receiveDamageLimit;
		}
		return info;
	}
	
	/**
	 * Method calcMagicDam.
	 * @param attacker Creature
	 * @param target Creature
	 * @param skill Skill
	 * @param sps int
	 * @return AttackInfo
	 */
	public static AttackInfo calcMagicDam(Creature attacker, Creature target, Skill skill, int sps)
	{
		AttackInfo info = new AttackInfo();
		boolean isPvP = attacker.isPlayable() && target.isPlayable();
		info.crit_static = attacker.calcStat(Stats.MCRITICAL_DAMAGE_STATIC, target, skill);
		info.shld = skill.getShieldIgnore() && calcShldUse(attacker, target);
		info.miss = false;
		double mAtk = attacker.getMAtk(target, skill);
		if (sps == 2)
		{
			mAtk *= 4;
		}
		else if (sps == 1)
		{
			mAtk *= 2;
		}
		double mdef = target.getMDef(null, skill);
		if (info.shld)
		{
			mdef += target.getShldDef();
		}
		if (mdef == 0)
		{
			mdef = 1;
		}
		double power = skill.getPower(target);
		info.lethal_dmg = 0;
		if (Rnd.chance(skill.getLethal1()))
		{
			if (target.isPlayer())
			{
				info.lethal = true;
				info.lethal_dmg = target.getCurrentCp();
				target.sendPacket(Msg.CP_DISAPPEARS_WHEN_HIT_WITH_A_HALF_KILL_SKILL);
			}
			else if (!target.isLethalImmune())
			{
				info.lethal = true;
				info.lethal_dmg = target.getCurrentHp() / 2;
			}
			else
			{
				power *= 2;
			}
			attacker.sendPacket(Msg.HALF_KILL);
		}
		else if (Rnd.chance(skill.getLethal2()))
		{
			if (target.isPlayer())
			{
				info.lethal = true;
				info.lethal_dmg = (target.getCurrentHp() + target.getCurrentCp()) - 1.1;
				target.sendPacket(SystemMsg.LETHAL_STRIKE);
			}
			else if (!target.isLethalImmune())
			{
				info.lethal = true;
				info.lethal_dmg = target.getCurrentHp() - 1;
			}
			else
			{
				power *= 3;
			}
			attacker.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
		}
		if (power == 0)
		{
			if (info.lethal_dmg > 0)
			{
				attacker.displayGiveDamageMessage(target, (int) info.lethal_dmg, false, false, false, false);
			}
			return info;
		}
		if (skill.isSoulBoost())
		{
			power *= 1.0 + (0.06 * Math.min(attacker.getConsumedSouls(), 5));
		}
		info.damage = (91 * power * Math.sqrt(mAtk)) / mdef;
		info.damage *= 1 + (((Rnd.get() * attacker.getRandomDamage() * 2) - attacker.getRandomDamage()) / 100);
		info.crit = calcMCrit(attacker.getMagicCriticalRate(target, skill));
		if (info.crit)
		{
			info.damage *= attacker.getMagicCriticalDmg(target, skill);
			info.damage += info.crit_static;
		}
		info.damage = attacker.calcStat(Stats.MAGIC_DAMAGE, info.damage, target, skill);
		if (info.shld)
		{
			if (Rnd.chance(5))
			{
				info.damage = 0;
				target.sendPacket(SystemMsg.YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS);
				attacker.sendPacket(new SystemMessage(SystemMessage.C1_RESISTED_C2S_MAGIC).addName(target).addName(attacker));
			}
			else
			{
				target.sendPacket(SystemMsg.YOUR_SHIELD_DEFENSE_HAS_SUCCEEDED);
				attacker.sendPacket(new SystemMessage(SystemMessage.YOUR_OPPONENT_HAS_RESISTANCE_TO_MAGIC_THE_DAMAGE_WAS_DECREASED));
			}
		}
		int levelDiff = target.getLevel() - attacker.getLevel();
		if ((info.damage > 1) && skill.isDeathlink())
		{
			info.damage *= 1.8 * (1.0 - attacker.getCurrentHpRatio());
		}
		if ((info.damage > 1) && skill.isBasedOnTargetDebuff())
		{
			info.damage *= 1 + (0.05 * target.getEffectList().getAllEffects().size());
		}
		info.damage += info.lethal_dmg;
		if (skill.getSkillType() == SkillType.MANADAM)
		{
			info.damage = Math.max(1, info.damage / 4.);
		}
		if (isPvP && (info.damage > 1))
		{
			info.damage *= attacker.calcStat(Stats.PVP_MAGIC_SKILL_DMG_BONUS, 1, null, null);
			info.damage /= target.calcStat(Stats.PVP_MAGIC_SKILL_DEFENCE_BONUS, 1, null, null);
		}
		double magic_rcpt = target.calcStat(Stats.MAGIC_RESIST, attacker, skill) - attacker.calcStat(Stats.MAGIC_POWER, target, skill);
		double failChance = 4. * Math.max(1., levelDiff) * (1. + (magic_rcpt / 100.));
		if (Rnd.chance(failChance))
		{
			if (levelDiff > 9)
			{
				info.damage = 0;
				SystemMessage msg = new SystemMessage(SystemMessage.C1_RESISTED_C2S_MAGIC).addName(target).addName(attacker);
				attacker.sendPacket(msg);
				target.sendPacket(msg);
			}
			else
			{
				info.damage /= 2;
				SystemMessage msg = new SystemMessage(SystemMessage.DAMAGE_IS_DECREASED_BECAUSE_C1_RESISTED_AGAINST_C2S_MAGIC).addName(target).addName(attacker);
				attacker.sendPacket(msg);
				target.sendPacket(msg);
			}
		}
		if ((info.damage > 1) && skill.isMagic() && calcMagicMiss(attacker,target))
		{
			attacker.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addName(attacker));
			target.sendPacket(new SystemMessage(SystemMessage.C1_HAS_EVADED_C2S_ATTACK).addName(target).addName(attacker));
			info.damage = 0;
		}
		if (info.damage > 0)
		{
			attacker.displayGiveDamageMessage(target, (int) info.damage, info.crit, false, false, true);
		}
		if (calcCastBreak(target, info.crit))
		{
			target.abortCast(false, true);
		}
		double receiveDamageLimit = target.calcStat(Stats.RECEIVE_DAMAGE_LIMIT, 0, target, skill);
		info.reflectableDamage = info.damage;
		if ((receiveDamageLimit > 0) && (info.damage > receiveDamageLimit))
		{
			info.reflectableDamage = info.damage - receiveDamageLimit;
			info.damage = receiveDamageLimit;
		}
		if (skill.isMarkDamage())
		{
			int boost = 1;
			//TODO ADD INTO XML LIST OF SKILL TO CHECK
			// name="dependOnTargetEffectId" value="11259,11261,11262"
			int[] list = {11259,11261,11262};
			for (int id : list)
			{
				if (target.getEffectList().containEffectFromSkillId(id, true))
				{
					boost += 1;
				}
			}
			//TODO CHECK RETAIL INFO ABOUT BOOST DAMAGE
			info.damage *= boost;
		}
		return info;
	}
	
	/**
	 * Method calcStunBreak.
	 * @param crit boolean
	 * @return boolean
	 */
	public static boolean calcStunBreak(boolean crit)
	{
		return Rnd.chance(crit ? 75 : 10);
	}
	
	/**
	 * Method calcBlow.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param skill Skill
	 * @return boolean
	 */
	public static boolean calcBlow(Creature activeChar, Creature target, Skill skill)
	{
		WeaponTemplate weapon = activeChar.getActiveWeaponItem();
		double base_weapon_crit = weapon == null ? 4. : weapon.getCritical();
		double crit_height_bonus = (0.008 * Math.min(25, Math.max(-25, target.getZ() - activeChar.getZ()))) + 1.1;
		double buffs_mult = activeChar.calcStat(Stats.FATALBLOW_RATE, target, skill);
		double skill_mod = skill.isBehind() ? 5 : 4;
		double chance = base_weapon_crit * buffs_mult * crit_height_bonus * skill_mod;
		if (!target.isInCombat())
		{
			chance *= 1.1;
		}
		switch (PositionUtils.getDirectionTo(target, activeChar))
		{
			case BEHIND:
				chance *= 1.3;
				break;
			case SIDE:
				chance *= 1.1;
				break;
			case FRONT:
				if (skill.isBehind())
				{
					chance = 3.0;
				}
				break;
			default:
				break;
		}
		chance = Math.min(skill.isBehind() ? 100 : 80, chance);
		return Rnd.chance(chance);
	}
	
	/**
	 * Method calcCrit.
	 * @param attacker Creature
	 * @param target Creature
	 * @param skill Skill
	 * @param blow boolean
	 * @return double
	 */
	public static double calcCrit(Creature attacker, Creature target, Skill skill, boolean blow)
	{
		if (attacker.isPlayer() && (attacker.getActiveWeaponItem() == null))
		{
			return 0;
		}
		if (skill != null)
		{
			return skill.getCriticalRate() * (blow ? BaseStats.DEX.calcBonus(attacker) : BaseStats.STR.calcBonus(attacker)) * 0.01 * attacker.calcStat(Stats.SKILL_CRIT_CHANCE_MOD, target, skill);
		}
		double rate = attacker.getCriticalHit(target, null) * 0.01 * target.calcStat(Stats.CRIT_CHANCE_RECEPTIVE, attacker, skill);
		switch (PositionUtils.getDirectionTo(target, attacker))
		{
			case BEHIND:
				rate *= 1.4;
				break;
			case SIDE:
				rate *= 1.2;
				break;
			default:
				break;
		}
		return rate / 10;
	}
	
	/**
	 * Method calcMCrit.
	 * @param mRate double
	 * @return boolean
	 */
	public static boolean calcMCrit(double mRate)
	{
		return mRate > Rnd.get(1000);
	}
	
	/**
	 * Method calcCastBreak.
	 * @param target Creature
	 * @param crit boolean
	 * @return boolean
	 */
	public static boolean calcCastBreak(Creature target, boolean crit)
	{
		if ((target == null) || target.isInvul() || target.isRaid() || !target.isCastingNow())
		{
			return false;
		}
		Skill skill = target.getCastingSkill();
		if ((skill != null) && ((skill.getSkillType() == SkillType.TAKECASTLE) || (skill.getSkillType() == SkillType.TAKEFORTRESS)))
		{
			return false;
		}
		return Rnd.chance(target.calcStat(Stats.CAST_INTERRUPT, crit ? 75 : 10, null, skill));
	}
	
	/**
	 * Method calcPAtkSpd.
	 * @param rate double
	 * @return int
	 */
	public static int calcPAtkSpd(double rate)
	{
		return (int) (500000 / rate);
	}
	
	/**
	 * Method calcMAtkSpd.
	 * @param attacker Creature
	 * @param skill Skill
	 * @param skillTime double
	 * @return int
	 */
	public static int calcMAtkSpd(Creature attacker, Skill skill, double skillTime)
	{
		if (skill.isMagic())
		{
			return (int) ((skillTime * 333) / Math.max(attacker.getMAtkSpd(), 1));
		}
		return (int) ((skillTime * 333) / Math.max(attacker.getPAtkSpd(), 1));
	}
	
	/**
	 * Method calcSkillReuseDelay.
	 * @param actor Creature
	 * @param skill Skill
	 * @return long
	 */
	public static long calcSkillReuseDelay(Creature actor, Skill skill)
	{
		long reuseDelay = skill.getReuseDelay();
		if (actor.isMonster())
		{
			reuseDelay = skill.getReuseForMonsters();
		}
		if (skill.isReuseDelayPermanent() || skill.isHandler() || skill.isItemSkill())
		{
			return reuseDelay;
		}
		if (actor.getSkillMastery(skill.getId()) == 1)
		{
			actor.removeSkillMastery(skill.getId());
			return 0;
		}
		if (skill.isMusic())
		{
			return (long) actor.calcStat(Stats.MUSIC_REUSE_RATE, reuseDelay, null, skill);
		}
		if (skill.isMagic())
		{
			return (long) actor.calcStat(Stats.MAGIC_REUSE_RATE, reuseDelay, null, skill);
		}
		return (long) actor.calcStat(Stats.PHYSIC_REUSE_RATE, reuseDelay, null, skill);
	}
	
	/**
	 * Method calcHitMiss.
	 * @param attacker Creature
	 * @param target Creature
	 * @return boolean
	 */
	public static boolean calcHitMiss(Creature attacker, Creature target)
	{
		int chanceToHit = 88 + (2 * (attacker.getAccuracy() - target.getEvasionRate(attacker)));
		chanceToHit = Math.max(chanceToHit, 28);
		chanceToHit = Math.min(chanceToHit, 98);
		PositionUtils.TargetDirection direction = PositionUtils.getDirectionTo(attacker, target);
		switch (direction)
		{
			case BEHIND:
				chanceToHit *= 1.2;
				break;
			case SIDE:
				chanceToHit *= 1.1;
				break;
			default:
				break;
		}
		return !Rnd.chance(chanceToHit);
	}

	/**
	 * Method calcMagicMiss.
	 * @param attacker Creature
	 * @param target Creature
	 * @return boolean
	 */
	public static boolean calcMagicMiss(Creature attacker, Creature target)
	{
		int chanceToHit = 88 + (2 * (attacker.getMAccuracy() - target.getMEvasionRate((attacker))));
		chanceToHit = Math.max(chanceToHit, 28);
		chanceToHit = Math.min(chanceToHit, 98);
		return !Rnd.chance(chanceToHit);
	}
	
	/**
	 * Method calcShldUse.
	 * @param attacker Creature
	 * @param target Creature
	 * @return boolean
	 */
	public static boolean calcShldUse(Creature attacker, Creature target)
	{
		WeaponTemplate template = target.getSecondaryWeaponItem();
		if ((template == null) || (template.getItemType() != WeaponTemplate.WeaponType.NONE))
		{
			return false;
		}
		int angle = (int) target.calcStat(Stats.SHIELD_ANGLE, attacker, null);
		if (!PositionUtils.isFacing(target, attacker, angle))
		{
			return false;
		}
		return Rnd.chance((int) target.calcStat(Stats.SHIELD_RATE, attacker, null));
	}
	
	/**
	 * Method calcSkillSuccess.
	 * @param env Env
	 * @param et EffectTemplate
	 * @param spiritshot int
	 * @return boolean
	 */
	public static boolean calcSkillSuccess(Env env, EffectTemplate et, int spiritshot)
	{
		if (env.value == -1)
		{
			return true;
		}
		env.value = Math.max(Math.min(env.value, 100), 1);
		final double base = env.value;
		final Skill skill = env.skill;
		if (!skill.isOffensive())
		{
			return Rnd.chance(env.value);
		}
		final Creature caster = env.character;
		final Creature target = env.target;
		boolean debugCaster = false;
		boolean debugTarget = false;
		boolean debugGlobal = false;
		if (Config.ALT_DEBUG_ENABLED)
		{
			debugCaster = (caster.getPlayer() != null) && caster.getPlayer().isDebug();
			debugTarget = (target.getPlayer() != null) && target.getPlayer().isDebug();
			final boolean debugPvP = Config.ALT_DEBUG_PVP_ENABLED && (debugCaster && debugTarget) && (!Config.ALT_DEBUG_PVP_DUEL_ONLY || (caster.getPlayer().isInDuel() && target.getPlayer().isInDuel()));
			debugGlobal = debugPvP || (Config.ALT_DEBUG_PVE_ENABLED && ((debugCaster && target.isMonster()) || (debugTarget && caster.isMonster())));
		}
		double statMod = 1.;
		if (skill.getSaveVs() != null)
		{
			statMod = skill.getSaveVs().calcChanceMod(target);
			env.value *= statMod;
		}
		env.value = Math.max(env.value, 1);
		double mAtkMod = 1.;
		int ssMod = 0;
		if (skill.isMagic())
		{
			int mdef = Math.max(1, target.getMDef(target, skill));
			double matk = caster.getMAtk(target, skill);
			if (skill.isSSPossible())
			{
				switch (spiritshot)
				{
					case ItemInstance.CHARGED_BLESSED_SPIRITSHOT:
						ssMod = 4;
						break;
					case ItemInstance.CHARGED_SPIRITSHOT:
						ssMod = 2;
						break;
					default:
						ssMod = 1;
				}
				matk *= ssMod;
			}
			mAtkMod = (Config.SKILLS_CHANCE_MOD * Math.pow(matk, Config.SKILLS_CHANCE_POW)) / mdef;
			env.value *= mAtkMod;
			env.value = Math.max(env.value, 1);
		}
		double lvlDependMod = skill.getLevelModifier();
		if (lvlDependMod != 0)
		{
			final int attackLevel = skill.getMagicLevel() > 0 ? skill.getMagicLevel() : caster.getLevel();
			lvlDependMod = 1. + ((attackLevel - target.getLevel()) * 0.03 * lvlDependMod);
			if (lvlDependMod < 0)
			{
				lvlDependMod = 0;
			}
			else if (lvlDependMod > 2)
			{
				lvlDependMod = 2;
			}
			env.value *= lvlDependMod;
		}
		double vulnMod = 0;
		double profMod = 0;
		double resMod = 1.;
		double debuffMod = 1.;
		if (!skill.isIgnoreResists())
		{
			debuffMod = 1. - (target.calcStat(Stats.DEBUFF_RESIST, caster, skill) / 120.);
			if (debuffMod != 1)
			{
				if (debuffMod == Double.NEGATIVE_INFINITY)
				{
					if (debugGlobal)
					{
						if (debugCaster)
						{
							caster.getPlayer().sendMessage("Full debuff immunity");
						}
						if (debugTarget)
						{
							target.getPlayer().sendMessage("Full debuff immunity");
						}
					}
					return false;
				}
				if (debuffMod == Double.POSITIVE_INFINITY)
				{
					if (debugGlobal)
					{
						if (debugCaster)
						{
							caster.getPlayer().sendMessage("Full debuff vulnerability");
						}
						if (debugTarget)
						{
							target.getPlayer().sendMessage("Full debuff vulnerability");
						}
					}
					return true;
				}
				debuffMod = Math.max(debuffMod, 0);
				env.value *= debuffMod;
			}
			SkillTrait trait = skill.getTraitType();
			if (trait != null)
			{
				vulnMod = trait.calcVuln(env);
				profMod = trait.calcProf(env);
				final double maxResist = 90 + (profMod * 0.85);
				resMod = (maxResist - vulnMod) / 60.;
			}
			if (resMod != 1)
			{
				if (resMod == Double.NEGATIVE_INFINITY)
				{
					if (debugGlobal)
					{
						if (debugCaster)
						{
							caster.getPlayer().sendMessage("Full immunity");
						}
						if (debugTarget)
						{
							target.getPlayer().sendMessage("Full immunity");
						}
					}
					return false;
				}
				if (resMod == Double.POSITIVE_INFINITY)
				{
					if (debugGlobal)
					{
						if (debugCaster)
						{
							caster.getPlayer().sendMessage("Full vulnerability");
						}
						if (debugTarget)
						{
							target.getPlayer().sendMessage("Full vulnerability");
						}
					}
					return true;
				}
				resMod = Math.max(resMod, 0);
				env.value *= resMod;
			}
		}
		double elementMod = 0;
		final Element element = skill.getElement();
		if (element != Element.NONE)
		{
			elementMod = skill.getElementPower();
			Element attackElement = getAttackElement(caster, target);
			if (attackElement == element)
			{
				elementMod += caster.calcStat(element.getAttack(), 0);
			}
			elementMod -= target.calcStat(element.getDefence(), 0);
			elementMod = Math.round(elementMod / 10);
			env.value += elementMod;
		}
		env.value = Math.max(env.value, Math.min(base, Config.SKILLS_CHANCE_MIN));
		env.value = Math.max(Math.min(env.value, Config.SKILLS_CHANCE_CAP), 1);
		final boolean result = Rnd.chance((int) env.value);
		if (debugGlobal)
		{
			StringBuilder stat = new StringBuilder(100);
			if (et == null)
			{
				stat.append(skill.getName());
			}
			else
			{
				stat.append(et._effectType.name());
			}
			stat.append(" AR:");
			stat.append((int) base);
			stat.append(' ');
			if (skill.getSaveVs() != null)
			{
				stat.append(skill.getSaveVs().name());
				stat.append(':');
				stat.append(String.format("%1.1f", statMod));
			}
			if (skill.isMagic())
			{
				stat.append(' ');
				stat.append(" mAtk:");
				stat.append(String.format("%1.1f", mAtkMod));
			}
			if (skill.getTraitType() != null)
			{
				stat.append(' ');
				stat.append(skill.getTraitType().name());
			}
			stat.append(' ');
			stat.append(String.format("%1.1f", resMod));
			stat.append('(');
			stat.append(String.format("%1.1f", profMod));
			stat.append('/');
			stat.append(String.format("%1.1f", vulnMod));
			if (debuffMod != 0)
			{
				stat.append('+');
				stat.append(String.format("%1.1f", debuffMod));
			}
			stat.append(") lvl:");
			stat.append(String.format("%1.1f", lvlDependMod));
			stat.append(" elem:");
			stat.append((int) elementMod);
			stat.append(" Chance:");
			stat.append(String.format("%1.1f", env.value));
			if (!result)
			{
				stat.append(" failed");
			}
			if (debugCaster)
			{
				caster.getPlayer().sendMessage(stat.toString());
			}
			if (debugTarget)
			{
				target.getPlayer().sendMessage(stat.toString());
			}
		}
		return result;
	}
	
	/**
	 * Method calcSkillSuccess.
	 * @param player Creature
	 * @param target Creature
	 * @param skill Skill
	 * @param activateRate int
	 * @return boolean
	 */
	public static boolean calcSkillSuccess(Creature player, Creature target, Skill skill, int activateRate)
	{
		Env env = new Env();
		env.character = player;
		env.target = target;
		env.skill = skill;
		env.value = activateRate;
		return calcSkillSuccess(env, null, player.getChargedSpiritShot());
	}
	
	/**
	 * Method calcSkillMastery.
	 * @param skill Skill
	 * @param activeChar Creature
	 */
	public static void calcSkillMastery(Skill skill, Creature activeChar)
	{
		if (skill.isHandler())
		{
			return;
		}
		if (((activeChar.getSkillLevel(331) > 0) && (activeChar.calcStat(Stats.SKILL_MASTERY, activeChar.getINT(), null, skill) >= Rnd.get(1000))) || (((activeChar.getSkillLevel(330) > 0) || (activeChar.getSkillLevel(10501) > 0)) && (activeChar.calcStat(Stats.SKILL_MASTERY, activeChar.getSTR(), null, skill) >= Rnd.get(1000))))
		{
			int masteryLevel;
			Skill.SkillType type = skill.getSkillType();
			if (skill.isMusic() || (type == Skill.SkillType.BUFF) || (type == Skill.SkillType.HOT) || (type == Skill.SkillType.HEAL_PERCENT))
			{
				masteryLevel = 2;
			}
			else if ((type == Skill.SkillType.HEAL) || (type == Skill.SkillType.HEAL_HP_CP))
			{
				masteryLevel = 3;
			}
			else
			{
				masteryLevel = 1;
			}
			if (masteryLevel > 0)
			{
				activeChar.setSkillMastery(skill.getId(), masteryLevel);
			}
		}
	}
	
	/**
	 * Method calcDamageResists.
	 * @param skill Skill
	 * @param attacker Creature
	 * @param defender Creature
	 * @param value double
	 * @return double
	 */
	public static double calcDamageResists(Skill skill, Creature attacker, Creature defender, double value)
	{
		if (attacker == defender)
		{
			return value;
		}
		if (attacker.isBoss())
		{
			value *= Config.RATE_EPIC_ATTACK;
		}
		else if (attacker.isRaid() || (attacker instanceof ReflectionBossInstance))
		{
			value *= Config.RATE_RAID_ATTACK;
		}
		if (defender.isBoss())
		{
			value /= Config.RATE_EPIC_DEFENSE;
		}
		else if (defender.isRaid() || (defender instanceof ReflectionBossInstance))
		{
			value /= Config.RATE_RAID_DEFENSE;
		}
		Player pAttacker = attacker.getPlayer();
		int diff = defender.getLevel() - (pAttacker != null ? pAttacker.getLevel() : attacker.getLevel());
		if (attacker.isPlayable() && defender.isMonster() && (defender.getLevel() >= 78) && (diff > 2))
		{
			value *= .7 / Math.pow(diff - 2, .25);
		}
		Element element = Element.NONE;
		double power = 0.;
		if (skill != null)
		{
			element = skill.getElement();
			power = skill.getElementPower();
		}
		else
		{
			element = getAttackElement(attacker, defender);
		}
		if (element == Element.NONE)
		{
			return value;
		}
		Double attDiff = attacker.calcStat(element.getAttack(), power) - defender.calcStat(element.getDefence(), 0.);
		if ((pAttacker != null) && pAttacker.isDebug())
		{
			pAttacker.sendMessage("Element: " + element.name());
			pAttacker.sendMessage("Attack: " + attacker.calcStat(element.getAttack(), power));
			pAttacker.sendMessage("Defence: " + defender.calcStat(element.getDefence(), 0.));
			pAttacker.sendMessage("Modifier: " + (attDiff < 0 ? "On defense " : "On attack ") + AttributeDamageResistTable.getInstance().getAttributeBonus(attDiff));
		}
		if(attDiff < 0)
		{
			return value / AttributeDamageResistTable.getInstance().getAttributeBonus(attDiff);
		}
		else
		{
			return value * AttributeDamageResistTable.getInstance().getAttributeBonus(attDiff);
		}
	}
	/**
	 * Method getAttackElement.
	 * @param attacker Creature
	 * @param target Creature
	 * @return Element
	 */
	public static Element getAttackElement(Creature attacker, Creature target)
	{
		double val, max = Double.MIN_VALUE, maxElementDefenseVal = Double.MIN_VALUE;
		Element maxElementDefense = Element.NONE;		
		Element result = Element.NONE;
		for (Element e : Element.VALUES)
		{
			val = attacker.calcStat(e.getAttack(), 0., null, null);
			if (val <= 0.)
			{
				if(target != null && ((target.calcStat(e.getDefence(), 0., null, null)) > maxElementDefenseVal))
				{
					maxElementDefenseVal = target.calcStat(e.getDefence(), 0., null, null); 
					maxElementDefense = e;
				}
				continue;
			}
			if(val > max)
			{
				max = val;
				result = e;
			}	
		}
		if(result == Element.NONE && target != null)
		{
			return maxElementDefense;
		}
		return result;
	}
}

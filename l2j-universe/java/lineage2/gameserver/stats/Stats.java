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

import java.util.NoSuchElementException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum Stats
{
	/**
	 * Field MAX_HP.
	 */
	MAX_HP("maxHp", 0., Double.POSITIVE_INFINITY, 1.),
	/**
	 * Field MAX_MP.
	 */
	MAX_MP("maxMp", 0., Double.POSITIVE_INFINITY, 1.),
	/**
	 * Field MAX_CP.
	 */
	MAX_CP("maxCp", 0., Double.POSITIVE_INFINITY, 1.),
	/**
	 * Field REGENERATE_HP_RATE.
	 */
	REGENERATE_HP_RATE("regHp"),
	/**
	 * Field REGENERATE_CP_RATE.
	 */
	REGENERATE_CP_RATE("regCp"),
	/**
	 * Field REGENERATE_MP_RATE.
	 */
	REGENERATE_MP_RATE("regMp"),
	/**
	 * Field HP_LIMIT.
	 */
	HP_LIMIT("hpLimit", 1., 100., 100.),
	/**
	 * Field MP_LIMIT.
	 */
	MP_LIMIT("mpLimit", 1., 100., 100.),
	/**
	 * Field CP_LIMIT.
	 */
	CP_LIMIT("cpLimit", 1., 100., 100.),
	/**
	 * Field RUN_SPEED.
	 */
	RUN_SPEED("runSpd"),
	/**
	 * Field POWER_DEFENCE.
	 */
	POWER_DEFENCE("pDef"),
	/**
	 * Field MAGIC_DEFENCE.
	 */
	MAGIC_DEFENCE("mDef"),
	/**
	 * Field POWER_ATTACK.
	 */
	POWER_ATTACK("pAtk"),
	/**
	 * Field MAGIC_ATTACK.
	 */
	MAGIC_ATTACK("mAtk"),
	/**
	 * Field POWER_ATTACK_SPEED.
	 */
	POWER_ATTACK_SPEED("pAtkSpd"),
	/**
	 * Field MAGIC_ATTACK_SPEED.
	 */
	MAGIC_ATTACK_SPEED("mAtkSpd"),
	/**
	 * Field MAGIC_REUSE_RATE.
	 */
	MAGIC_REUSE_RATE("mReuse"),
	/**
	 * Field PHYSIC_REUSE_RATE.
	 */
	PHYSIC_REUSE_RATE("pReuse"),
	/**
	 * Field MUSIC_REUSE_RATE.
	 */
	MUSIC_REUSE_RATE("musicReuse"),
	/**
	 * Field ATK_REUSE.
	 */
	ATK_REUSE("atkReuse"),
	/**
	 * Field ATK_BASE.
	 */
	ATK_BASE("atkBaseSpeed"),
	/**
	 * Field CRITICAL_DAMAGE.
	 */
	CRITICAL_DAMAGE("cAtk", 0., Double.POSITIVE_INFINITY, 100.),
	/**
	 * Field CRITICAL_DAMAGE_STATIC.
	 */
	CRITICAL_DAMAGE_STATIC("cAtkStatic"),
	/**
	 * Field EVASION_RATE.
	 */
	EVASION_RATE("rEvas"),
	/**
	 * Field MEVASION_RATE.
	 */
	MEVASION_RATE("mEvas"),
	/**
	 * Field ACCURACY_COMBAT.
	 */
	ACCURACY_COMBAT("accCombat"),
	/**
	 * Field MACCURACY_COMBAT.
	 */
	MACCURACY_COMBAT("maccCombat"),
	/**
	 * Field CRITICAL_BASE.
	 */
	CRITICAL_BASE("baseCrit", 0., Double.POSITIVE_INFINITY, 100.),
	/**
	 * Field CRITICAL_RATE.
	 */
	CRITICAL_RATE("rCrit", 0., Double.POSITIVE_INFINITY, 100.),
	/**
	 * Field MCRITICAL_RATE.
	 */
	MCRITICAL_RATE("mCritRate", 0., Double.POSITIVE_INFINITY, 80.),
	/**
	 * Field MCRITICAL_DAMAGE.
	 */
	MCRITICAL_DAMAGE("mCritDamage", 0., 10., 2),
	/**
	 * Field CRITICAL_DAMAGE_STATIC.
	 */
	MCRITICAL_DAMAGE_STATIC("mCAtkStatic"),
	/**
	 * Field PHYSICAL_DAMAGE.
	 */
	PHYSICAL_DAMAGE("physDamage"),
	/**
	 * Field MAGIC_DAMAGE.
	 */
	MAGIC_DAMAGE("magicDamage"),
	/**
	 * Field PHYSICAL_RESIST.
	 */
	PHYSICAL_RESIST("physResist"),
	/**
	 * Field MAGICAL_RESIST.
	 */
	MAGICAL_RESIST("magicResist"),
	/**
	 * Field CAST_INTERRUPT.
	 */
	CAST_INTERRUPT("concentration", 0., 100.),
	/**
	 * Field SHIELD_DEFENCE.
	 */
	SHIELD_DEFENCE("sDef"),
	/**
	 * Field SHIELD_RATE.
	 */
	SHIELD_RATE("rShld", 0., 90.),
	/**
	 * Field SHIELD_ANGLE.
	 */
	SHIELD_ANGLE("shldAngle", 0., 180., 60.),
	/**
	 * Field POWER_ATTACK_RANGE.
	 */
	POWER_ATTACK_RANGE("pAtkRange", 0., 1500.),
	/**
	 * Field MAGIC_ATTACK_RANGE.
	 */
	MAGIC_ATTACK_RANGE("mAtkRange", 0., 1500.),
	/**
	 * Field POLE_ATTACK_ANGLE.
	 */
	POLE_ATTACK_ANGLE("poleAngle", 0., 180.),
	/**
	 * Field POLE_TARGET_COUNT.
	 */
	POLE_TARGET_COUNT("poleTargetCount"),
	/**
	 * Field STAT_STR.
	 */
	STAT_STR("STR", 1., 150.),
	/**
	 * Field STAT_CON.
	 */
	STAT_CON("CON", 1., 150.),
	/**
	 * Field STAT_DEX.
	 */
	STAT_DEX("DEX", 1., 150.),
	/**
	 * Field STAT_INT.
	 */
	STAT_INT("INT", 1., 150.),
	/**
	 * Field STAT_WIT.
	 */
	STAT_WIT("WIT", 1., 150.),
	/**
	 * Field STAT_MEN.
	 */
	STAT_MEN("MEN", 1., 150.),
	/**
	 * Field BREATH.
	 */
	BREATH("breath"),
	/**
	 * Field FALL.
	 */
	FALL("fall"),
	/**
	 * Field EXP_LOST.
	 */
	EXP_LOST("expLost"),
	/**
	 * Field BLEED_RESIST.
	 */
	BLEED_RESIST("bleedResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field POISON_RESIST.
	 */
	POISON_RESIST("poisonResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field STUN_RESIST.
	 */
	STUN_RESIST("stunResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field ROOT_RESIST.
	 */
	ROOT_RESIST("rootResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field MENTAL_RESIST.
	 */
	MENTAL_RESIST("mentalResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field SLEEP_RESIST.
	 */
	SLEEP_RESIST("sleepResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field PARALYZE_RESIST.
	 */
	PARALYZE_RESIST("paralyzeResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field CANCEL_RESIST.
	 */
	CANCEL_RESIST("cancelResist", -200., 300.),
	/**
	 * Field DEBUFF_RESIST.
	 */
	DEBUFF_RESIST("debuffResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field MAGIC_RESIST.
	 */
	MAGIC_RESIST("magicResist", -200., 300.),
	/**
	 * Field AIRJOKE_RESIST.
	 */
	AIRJOKE_RESIST("airjokeResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field MUTATE_RESIST.
	 */
	MUTATE_RESIST("mutateResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**c
	 * Field DISARM_RESIST.
	 */
	DISARM_RESIST("disarmResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field PULL_RESIST.
	 */
	PULL_RESIST("pullResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field KNOCKBACK_RESIST.
	 */
	KNOCKBACK_RESIST("knockBackResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field KNOCKDOWN_RESIST.
	 */
	KNOCKDOWN_RESIST("knockDownResist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field BLEED_POWER.
	 */
	BLEED_POWER("bleedPower", -200., 200.),
	/**
	 * Field POISON_POWER.
	 */
	POISON_POWER("poisonPower", -200., 200.),
	/**
	 * Field STUN_POWER.
	 */
	STUN_POWER("stunPower", -200., 200.),
	/**
	 * Field ROOT_POWER.
	 */
	ROOT_POWER("rootPower", -200., 200.),
	/**
	 * Field MENTAL_POWER.
	 */
	MENTAL_POWER("mentalPower", -200., 200.),
	/**
	 * Field SLEEP_POWER.
	 */
	SLEEP_POWER("sleepPower", -200., 200.),
	/**
	 * Field PARALYZE_POWER.
	 */
	PARALYZE_POWER("paralyzePower", -200., 200.),
	/**
	 * Field CANCEL_POWER.
	 */
	CANCEL_POWER("cancelPower", -200., 200.),
	/**
	 * Field DEBUFF_POWER.
	 */
	DEBUFF_POWER("debuffPower", -200., 200.),
	/**
	 * Field MAGIC_POWER.
	 */
	MAGIC_POWER("magicPower", -200., 200.),
	/**
	 * Field TRANSFORM_POWER.
	 */
	MUTATE_POWER("mutatePower", -200., 200.),
	/**
	 * Field PULL_POWER.
	 */
	AIRJOKE_POWER("airjokePower", -200., 200.),
	/**
	 * Field PULL_POWER.
	 */
	DISARM_POWER("disarmPower", -200., 200.),
	/**
	 * Field PULL_POWER.
	 */
	PULL_POWER("pullPower", -200., 200.),
	/**
	 * Field KNOCKBACK_POWER.
	 */
	KNOCKBACK_POWER("knockBackPower", -200., 200.),
	/**
	 * Field KNOCKDOWN_POWER.
	 */
	KNOCKDOWN_POWER("knockDownPower", -200., 200.),
	/**
	 * Field FATALBLOW_RATE.
	 */
	FATALBLOW_RATE("blowRate", 0., 10., 1.),
	/**
	 * Field SKILL_CRIT_CHANCE_MOD.
	 */
	SKILL_CRIT_CHANCE_MOD("SkillCritChanceMod", 10., 190., 100.),
	/**
	 * Field DEATH_VULNERABILITY.
	 */
	DEATH_VULNERABILITY("deathVuln", 10., 190., 100.),
	/**
	 * Field CRIT_DAMAGE_RECEPTIVE.
	 */
	CRIT_DAMAGE_RECEPTIVE("critDamRcpt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field CRIT_CHANCE_RECEPTIVE.
	 */
	CRIT_CHANCE_RECEPTIVE("critChanceRcpt", 10., 190., 100.),
	/**
	 * Field DEFENCE_FIRE.
	 */
	DEFENCE_FIRE("defenceFire", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field DEFENCE_WATER.
	 */
	DEFENCE_WATER("defenceWater", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field DEFENCE_WIND.
	 */
	DEFENCE_WIND("defenceWind", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field DEFENCE_EARTH.
	 */
	DEFENCE_EARTH("defenceEarth", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field DEFENCE_HOLY.
	 */
	DEFENCE_HOLY("defenceHoly", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field DEFENCE_UNHOLY.
	 */
	DEFENCE_UNHOLY("defenceUnholy", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	/**
	 * Field ATTACK_FIRE.
	 */
	ATTACK_FIRE("attackFire", 0., Double.POSITIVE_INFINITY),
	/**
	 * Field ATTACK_WATER.
	 */
	ATTACK_WATER("attackWater", 0., Double.POSITIVE_INFINITY),
	/**
	 * Field ATTACK_WIND.
	 */
	ATTACK_WIND("attackWind", 0., Double.POSITIVE_INFINITY),
	/**
	 * Field ATTACK_EARTH.
	 */
	ATTACK_EARTH("attackEarth", 0., Double.POSITIVE_INFINITY),
	/**
	 * Field ATTACK_HOLY.
	 */
	ATTACK_HOLY("attackHoly", 0., Double.POSITIVE_INFINITY),
	/**
	 * Field ATTACK_UNHOLY.
	 */
	ATTACK_UNHOLY("attackUnholy", 0., Double.POSITIVE_INFINITY),
	/**
	 * Field SWORD_WPN_VULNERABILITY.
	 */
	SWORD_WPN_VULNERABILITY("swordWpnVuln", 10., 200., 100.),
	/**
	 * Field DUAL_WPN_VULNERABILITY.
	 */
	DUAL_WPN_VULNERABILITY("dualWpnVuln", 10., 200., 100.),
	/**
	 * Field BLUNT_WPN_VULNERABILITY.
	 */
	BLUNT_WPN_VULNERABILITY("bluntWpnVuln", 10., 200., 100.),
	/**
	 * Field DAGGER_WPN_VULNERABILITY.
	 */
	DAGGER_WPN_VULNERABILITY("daggerWpnVuln", 10., 200., 100.),
	/**
	 * Field BOW_WPN_VULNERABILITY.
	 */
	BOW_WPN_VULNERABILITY("bowWpnVuln", 10., 200., 100.),
	/**
	 * Field CROSSBOW_WPN_VULNERABILITY.
	 */
	CROSSBOW_WPN_VULNERABILITY("crossbowWpnVuln", 10., 200., 100.),
	/**
	 * Field POLE_WPN_VULNERABILITY.
	 */
	POLE_WPN_VULNERABILITY("poleWpnVuln", 10., 200., 100.),
	/**
	 * Field FIST_WPN_VULNERABILITY.
	 */
	FIST_WPN_VULNERABILITY("fistWpnVuln", 10., 200., 100.),
	/**
	 * Field ABSORB_DAMAGE_PERCENT.
	 */
	ABSORB_DAMAGE_PERCENT("absorbDam", 0., 100.),
	/**
	 * Field ABSORB_DAMAGEMP_PERCENT.
	 */
	ABSORB_DAMAGEMP_PERCENT("absorbDamMp", 0., 100.),
	/**
	 * Field TRANSFER_TO_SUMMON_DAMAGE_PERCENT.
	 */
	TRANSFER_TO_SUMMON_DAMAGE_PERCENT("transferPetDam", 0., 100.),
	/**
	 * Field TRANSFER_TO_EFFECTOR_DAMAGE_PERCENT.
	 */
	TRANSFER_TO_EFFECTOR_DAMAGE_PERCENT("transferToEffectorDam", 0., 100.),
	/**
	 * Field TRANSFER_TO_MP_DAMAGE_PERCENT.
	 */
	TRANSFER_TO_MP_DAMAGE_PERCENT("transferToMpDam", 0., 100.),
	/**
	 * Field REFLECT_AND_BLOCK_DAMAGE_CHANCE.
	 */
	REFLECT_AND_BLOCK_DAMAGE_CHANCE("reflectAndBlockDam", 0., 100.),
	/**
	 * Field REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE.
	 */
	REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE("reflectAndBlockPSkillDam", 0., 100.),
	/**
	 * Field REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE.
	 */
	REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE("reflectAndBlockMSkillDam", 0., 100.),
	/**
	 * Field REFLECT_RESISTANCE_PERCENT.
	 */
	REFLECT_RESISTANCE_PERCENT("reflectResist", 0., 100.),
	/**
	 * Field REFLECT_DAMAGE_PERCENT.
	 */
	REFLECT_DAMAGE_PERCENT("reflectDam", 0., 100.),
	/**
	 * Field REFLECT_PSKILL_DAMAGE_PERCENT.
	 */
	REFLECT_PSKILL_DAMAGE_PERCENT("reflectPSkillDam", 0., 100.),
	/**
	 * Field REFLECT_MSKILL_DAMAGE_PERCENT.
	 */
	REFLECT_MSKILL_DAMAGE_PERCENT("reflectMSkillDam", 0., 100.),
	/**
	 * Field REFLECT_PHYSIC_SKILL.
	 */
	REFLECT_PHYSIC_SKILL("reflectPhysicSkill", 0., 100.),
	/**
	 * Field REFLECT_MAGIC_SKILL.
	 */
	REFLECT_MAGIC_SKILL("reflectMagicSkill", 0., 100.),
	/**
	 * Field REFLECT_PHYSIC_DEBUFF.
	 */
	REFLECT_PHYSIC_DEBUFF("reflectPhysicDebuff", 0., 100.),
	/**
	 * Field REFLECT_MAGIC_DEBUFF.
	 */
	REFLECT_MAGIC_DEBUFF("reflectMagicDebuff", 0., 100.),
	/**
	 * Field PSKILL_EVASION.
	 */
	PSKILL_EVASION("pSkillEvasion", 0., 100.),
	/**
	 * Field COUNTER_ATTACK.
	 */
	COUNTER_ATTACK("counterAttack", 0., 100.),
	/**
	 * Field SKILL_POWER.
	 */
	SKILL_POWER("skillPower"),
	/**
	 * Field PVP_PHYS_DMG_BONUS.
	 */
	PVP_PHYS_DMG_BONUS("pvpPhysDmgBonus"),
	/**
	 * Field PVP_PHYS_SKILL_DMG_BONUS.
	 */
	PVP_PHYS_SKILL_DMG_BONUS("pvpPhysSkillDmgBonus"),
	/**
	 * Field PVP_MAGIC_SKILL_DMG_BONUS.
	 */
	PVP_MAGIC_SKILL_DMG_BONUS("pvpMagicSkillDmgBonus"),
	/**
	 * Field PVP_PHYS_DEFENCE_BONUS.
	 */
	PVP_PHYS_DEFENCE_BONUS("pvpPhysDefenceBonus"),
	/**
	 * Field PVP_PHYS_SKILL_DEFENCE_BONUS.
	 */
	PVP_PHYS_SKILL_DEFENCE_BONUS("pvpPhysSkillDefenceBonus"),
	/**
	 * Field PVP_MAGIC_SKILL_DEFENCE_BONUS.
	 */
	PVP_MAGIC_SKILL_DEFENCE_BONUS("pvpMagicSkillDefenceBonus"),
	/**
	 * Field PHYS_SKILL_DMG_STATIC.
	 */
	PHYS_SKILL_DMG_STATIC("PhysSkillDmgStatic"),
	/**
	 * Field MAGIC_SKILL_DMG_STATIC.
	 */
	MAGIC_SKILL_DMG_STATIC("MagicSkillDmgStatic"),
	/**
	 * Field HEAL_EFFECTIVNESS.
	 */
	HEAL_EFFECTIVNESS("hpEff", 0., 1000.),
	/**
	 * Field MANAHEAL_EFFECTIVNESS.
	 */
	MANAHEAL_EFFECTIVNESS("mpEff", 0., 1000.),
	/**
	 * Field CPHEAL_EFFECTIVNESS.
	 */
	CPHEAL_EFFECTIVNESS("cpEff", 0., 1000.),
	/**
	 * Field HEAL_POWER.
	 */
	HEAL_POWER("healPower"),
	/**
	 * Field MP_MAGIC_SKILL_CONSUME.
	 */
	MP_MAGIC_SKILL_CONSUME("mpConsum"),
	/**
	 * Field MP_PHYSICAL_SKILL_CONSUME.
	 */
	MP_PHYSICAL_SKILL_CONSUME("mpConsumePhysical"),
	/**
	 * Field MP_DANCE_SKILL_CONSUME.
	 */
	MP_DANCE_SKILL_CONSUME("mpDanceConsume"),
	/**
	 * Field MP_USE_BOW.
	 */
	MP_USE_BOW("cheapShot"),
	/**
	 * Field MP_USE_BOW_CHANCE.
	 */
	MP_USE_BOW_CHANCE("cheapShotChance"),
	/**
	 * Field SS_USE_BOW.
	 */
	SS_USE_BOW("miser"),
	/**
	 * Field SS_USE_BOW_CHANCE.
	 */
	SS_USE_BOW_CHANCE("miserChance"),
	/**
	 * Field SKILL_MASTERY.
	 */
	SKILL_MASTERY("skillMastery"),
	/**
	 * Field MAX_LOAD.
	 */
	MAX_LOAD("maxLoad"),
	/**
	 * Field MAX_NO_PENALTY_LOAD.
	 */
	MAX_NO_PENALTY_LOAD("maxNoPenaltyLoad"),
	/**
	 * Field INVENTORY_LIMIT.
	 */
	INVENTORY_LIMIT("inventoryLimit"),
	/**
	 * Field STORAGE_LIMIT.
	 */
	STORAGE_LIMIT("storageLimit"),
	/**
	 * Field TRADE_LIMIT.
	 */
	TRADE_LIMIT("tradeLimit"),
	/**
	 * Field COMMON_RECIPE_LIMIT.
	 */
	COMMON_RECIPE_LIMIT("CommonRecipeLimit"),
	/**
	 * Field DWARVEN_RECIPE_LIMIT.
	 */
	DWARVEN_RECIPE_LIMIT("DwarvenRecipeLimit"),
	/**
	 * Field BUFF_LIMIT.
	 */
	BUFF_LIMIT("buffLimit"),
	/**
	 * Field SOULS_LIMIT.
	 */
	SOULS_LIMIT("soulsLimit"),
	/**
	 * Field SOULS_CONSUME_EXP.
	 */
	SOULS_CONSUME_EXP("soulsExp"),
	/**
	 * Field TALISMANS_LIMIT.
	 */
	TALISMANS_LIMIT("talismansLimit", 0., 6.),
	/**
	 * Field CUBICS_LIMIT.
	 */
	CUBICS_LIMIT("cubicsLimit", 0., 3., 1.),
	/**
	 * Field CLOAK_SLOT.
	 */
	CLOAK_SLOT("openCloakSlot", 0., 1.),
	/**
	 * Field SUMMON_POINT.
	 */
	SUMMON_POINT("summonPointCount", 0, 9),
	/**
	 * Field GRADE_EXPERTISE_LEVEL.
	 */
	GRADE_EXPERTISE_LEVEL("gradeExpertiseLevel"),
	/**
	 * Field EXP.
	 */
	EXP("ExpMultiplier"),
	/**
	 * Field SP.
	 */
	SP("SpMultiplier"),
	/**
	 * Field REWARD_MULTIPLIER.
	 */
	REWARD_MULTIPLIER("DropMultiplier"),
	/**
	 * Field RECEIVE_DAMAGE_LIMIT.
	 */
	RECEIVE_DAMAGE_LIMIT("receiveDamageLimit"),
	/**
	 * Field CANCEL_TARGET.
	 */
	CANCEL_TARGET("cancelTarget", 0, 100.);
	/**
	 * Field NUM_STATS.
	 */
	public static final int NUM_STATS = values().length;
	/**
	 * Field _value.
	 */
	private final String _value;
	/**
	 * Field _min.
	 */
	private double _min;
	/**
	 * Field _max.
	 */
	private double _max;
	/**
	 * Field _init.
	 */
	private double _init;
	
	/**
	 * Method getValue.
	 * @return String
	 */
	public String getValue()
	{
		return _value;
	}
	
	/**
	 * Method getInit.
	 * @return double
	 */
	public double getInit()
	{
		return _init;
	}
	
	/**
	 * Constructor for Stats.
	 * @param s String
	 */
	private Stats(String s)
	{
		this(s, 0., Double.POSITIVE_INFINITY, 0.);
	}
	
	/**
	 * Constructor for Stats.
	 * @param s String
	 * @param min double
	 * @param max double
	 */
	private Stats(String s, double min, double max)
	{
		this(s, min, max, 0.);
	}
	
	/**
	 * Constructor for Stats.
	 * @param s String
	 * @param min double
	 * @param max double
	 * @param init double
	 */
	private Stats(String s, double min, double max, double init)
	{
		_value = s;
		_min = min;
		_max = max;
		_init = init;
	}
	
	/**
	 * Method validate.
	 * @param val double
	 * @return double
	 */
	public double validate(double val)
	{
		if (val < _min)
		{
			return _min;
		}
		if (val > _max)
		{
			return _max;
		}
		return val;
	}
	
	/**
	 * Method valueOfXml.
	 * @param name String
	 * @return Stats
	 */
	public static Stats valueOfXml(String name)
	{
		for (Stats s : values())
		{
			if (s.getValue().equals(name))
			{
				return s;
			}
		}
		throw new NoSuchElementException("Unknown name '" + name + "' for enum BaseStats");
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _value;
	}
}

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
package lineage2.gameserver.skills;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.skills.effects.EffectAddSkills;
import lineage2.gameserver.skills.effects.EffectAgathionRes;
import lineage2.gameserver.skills.effects.EffectAggression;
import lineage2.gameserver.skills.effects.EffectBetray;
import lineage2.gameserver.skills.effects.EffectBlessNoblesse;
import lineage2.gameserver.skills.effects.EffectBlockStat;
import lineage2.gameserver.skills.effects.EffectBlockTarget;
import lineage2.gameserver.skills.effects.EffectBluff;
import lineage2.gameserver.skills.effects.EffectBuff;
import lineage2.gameserver.skills.effects.EffectCPDamPercent;
import lineage2.gameserver.skills.effects.EffectCallSkills;
import lineage2.gameserver.skills.effects.EffectCannotTarget;
import lineage2.gameserver.skills.effects.EffectCharge;
import lineage2.gameserver.skills.effects.EffectChargesOverTime;
import lineage2.gameserver.skills.effects.EffectCharmOfCourage;
import lineage2.gameserver.skills.effects.EffectCombatPointHealOverTime;
import lineage2.gameserver.skills.effects.EffectConsumeSoulsOverTime;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.skills.effects.EffectCurseOfLifeFlow;
import lineage2.gameserver.skills.effects.EffectDamOverTime;
import lineage2.gameserver.skills.effects.EffectDamOverTimeLethal;
import lineage2.gameserver.skills.effects.EffectDeathImmunity;
import lineage2.gameserver.skills.effects.EffectDebuffImmunity;
import lineage2.gameserver.skills.effects.EffectDestroySummon;
import lineage2.gameserver.skills.effects.EffectDisarm;
import lineage2.gameserver.skills.effects.EffectDiscord;
import lineage2.gameserver.skills.effects.EffectDispelEffects;
import lineage2.gameserver.skills.effects.EffectDispelOnHIt;
import lineage2.gameserver.skills.effects.EffectEnervation;
import lineage2.gameserver.skills.effects.EffectFakeDeath;
import lineage2.gameserver.skills.effects.EffectFear;
import lineage2.gameserver.skills.effects.EffectGiantForceAura;
import lineage2.gameserver.skills.effects.EffectGrow;
import lineage2.gameserver.skills.effects.EffectHPDamPercent;
import lineage2.gameserver.skills.effects.EffectHate;
import lineage2.gameserver.skills.effects.EffectHeal;
import lineage2.gameserver.skills.effects.EffectHealAndDamage;
import lineage2.gameserver.skills.effects.EffectHealBlock;
import lineage2.gameserver.skills.effects.EffectHealCPPercent;
import lineage2.gameserver.skills.effects.EffectHealHPCP;
import lineage2.gameserver.skills.effects.EffectHealOverTime;
import lineage2.gameserver.skills.effects.EffectHealPercent;
import lineage2.gameserver.skills.effects.EffectHellBinding;
import lineage2.gameserver.skills.effects.EffectHourglass;
import lineage2.gameserver.skills.effects.EffectHpToOne;
import lineage2.gameserver.skills.effects.EffectImmobilize;
import lineage2.gameserver.skills.effects.EffectIncreaseChargesOverTime;
import lineage2.gameserver.skills.effects.EffectInterrupt;
import lineage2.gameserver.skills.effects.EffectInvisible;
import lineage2.gameserver.skills.effects.EffectInvulnerable;
import lineage2.gameserver.skills.effects.EffectKnockBack;
import lineage2.gameserver.skills.effects.EffectKnockDown;
import lineage2.gameserver.skills.effects.EffectLDManaDamOverTime;
import lineage2.gameserver.skills.effects.EffectLockInventory;
import lineage2.gameserver.skills.effects.EffectMDamOverTime;
import lineage2.gameserver.skills.effects.EffectMPDamPercent;
import lineage2.gameserver.skills.effects.EffectManaDamOverTime;
import lineage2.gameserver.skills.effects.EffectManaHeal;
import lineage2.gameserver.skills.effects.EffectManaHealOverTime;
import lineage2.gameserver.skills.effects.EffectManaHealPercent;
import lineage2.gameserver.skills.effects.EffectMeditation;
import lineage2.gameserver.skills.effects.EffectMute;
import lineage2.gameserver.skills.effects.EffectMuteAll;
import lineage2.gameserver.skills.effects.EffectMuteAttack;
import lineage2.gameserver.skills.effects.EffectMutePhisycal;
import lineage2.gameserver.skills.effects.EffectNegateEffects;
import lineage2.gameserver.skills.effects.EffectNegateMusic;
import lineage2.gameserver.skills.effects.EffectParalyze;
import lineage2.gameserver.skills.effects.EffectPetrification;
import lineage2.gameserver.skills.effects.EffectRandomHate;
import lineage2.gameserver.skills.effects.EffectRelax;
import lineage2.gameserver.skills.effects.EffectRemoveTarget;
import lineage2.gameserver.skills.effects.EffectRestoration;
import lineage2.gameserver.skills.effects.EffectRestorationRandom;
import lineage2.gameserver.skills.effects.EffectRoot;
import lineage2.gameserver.skills.effects.EffectSalvation;
import lineage2.gameserver.skills.effects.EffectServitorShare;
import lineage2.gameserver.skills.effects.EffectShadowStep;
import lineage2.gameserver.skills.effects.EffectSilentMove;
import lineage2.gameserver.skills.effects.EffectSleep;
import lineage2.gameserver.skills.effects.EffectStun;
import lineage2.gameserver.skills.effects.EffectSummonSkill;
import lineage2.gameserver.skills.effects.EffectSymbol;
import lineage2.gameserver.skills.effects.EffectTalismanOfPower;
import lineage2.gameserver.skills.effects.EffectTargetToMe;
import lineage2.gameserver.skills.effects.EffectTargetToOwner;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.skills.effects.EffectTransformation;
import lineage2.gameserver.skills.effects.EffectTurner;
import lineage2.gameserver.skills.effects.EffectUnAggro;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum EffectType
{
	/**
	 * Field AddSkills.
	 */
	AddSkills(EffectAddSkills.class, null, false),
	/**
	 * Field AgathionResurrect.
	 */
	AgathionResurrect(EffectAgathionRes.class, null, true),
	/**
	 * Field Aggression.
	 */
	Aggression(EffectAggression.class, null, true),
	/**
	 * Field Betray.
	 */
	Betray(EffectBetray.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field BlessNoblesse.
	 */
	BlessNoblesse(EffectBlessNoblesse.class, null, true),
	/**
	 * Field BlockStat.
	 */
	BlockStat(EffectBlockStat.class, null, true),
	/**
	 * Field BlockTarget.
	 */
	BlockTarget(EffectBlockTarget.class, null, true),
	/**
	 * Field Buff.
	 */
	Buff(EffectBuff.class, null, false),
	/**
	 * Field Bluff.
	 */
	Bluff(EffectBluff.class, AbnormalEffect.NULL, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field DebuffImmunity.
	 */
	DebuffImmunity(EffectDebuffImmunity.class, null, true),
	/**
	 * Field DispelEffects.
	 */
	DispelEffects(EffectDispelEffects.class, null, Stats.CANCEL_RESIST, Stats.CANCEL_POWER, true),
	/**
	 * Field CallSkills.
	 */
	CallSkills(EffectCallSkills.class, null, false),
	/**
	 * Field CombatPointHealOverTime.
	 */
	CombatPointHealOverTime(EffectCombatPointHealOverTime.class, null, true),
	/**
	 * Field ConsumeSoulsOverTime.
	 */
	ConsumeSoulsOverTime(EffectConsumeSoulsOverTime.class, null, true),
	/**
	 * Field Charge.
	 */
	Charge(EffectCharge.class, null, false),
	/**
	 * Field CharmOfCourage.
	 */
	CharmOfCourage(EffectCharmOfCourage.class, null, true),
	/**
	 * Field CPDamPercent.
	 */
	CPDamPercent(EffectCPDamPercent.class, null, true),
	/**
	 * Field Cubic.
	 */
	Cubic(EffectCubic.class, null, true),
	/**
	 * Field DamOverTime.
	 */
	DamOverTime(EffectDamOverTime.class, null, false),
	/**
	 * Field DamOverTimeLethal.
	 */
	DamOverTimeLethal(EffectDamOverTimeLethal.class, null, false),
	/**
	 * Field DeathImmunity.
	 */
	DeathImmunity(EffectDeathImmunity.class, null, false),
	/**
	 * Field DestroySummon.
	 */
	DestroySummon(EffectDestroySummon.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field Disarm.
	 */
	Disarm(EffectDisarm.class, null, Stats.DISARM_RESIST, Stats.DISARM_POWER, true),
	/**
	 * Field Discord.
	 */
	Discord(EffectDiscord.class, AbnormalEffect.WIND, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field Enervation.
	 */
	Enervation(EffectEnervation.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, false),
	/**
	 * Field FakeDeath.
	 */
	FakeDeath(EffectFakeDeath.class, null, true),
	/**
	 * Field Fear.
	 */
	Fear(EffectFear.class, AbnormalEffect.FEAR, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field Grow.
	 */
	Grow(EffectGrow.class, AbnormalEffect.GROW, false),
	/**
	 * Field GiantForceAura.
	 */
	GiantForceAura(EffectGiantForceAura.class, null, false),
	/**
	 * Field Hate.
	 */
	Hate(EffectHate.class, null, false),
	/**
	 * Field Heal.
	 */
	Heal(EffectHeal.class, null, false),
	/**
	 * Field Heal.
	 */
	HealAndDamage(EffectHealAndDamage.class, null, false),
	/**
	 * Field HealBlock.
	 */
	HealBlock(EffectHealBlock.class, null, true),
	/**
	 * Field HealCPPercent.
	 */
	HealCPPercent(EffectHealCPPercent.class, null, true),
	/**
	 * Field HealHPCP.
	 */
	HealHPCP(EffectHealHPCP.class, null, true),
	/**
	 * Field HealOverTime.
	 */
	HealOverTime(EffectHealOverTime.class, null, false),
	/**
	 * Field HealPercent.
	 */
	HealPercent(EffectHealPercent.class, null, false),
	/**
	 * Field HellBinding.
	 */
	HellBinding(EffectHellBinding.class, AbnormalEffect.S_HELLBINDING, true),
	/**
	 * Field HPDamPercent.
	 */
	HPDamPercent(EffectHPDamPercent.class, null, true),
	/**
	 * Field HpToOne.
	 */
	HpToOne(EffectHpToOne.class, null, true),
	/**
	 * Field IncreaseChargesOverTime.
	 */
	IncreaseChargesOverTime(EffectIncreaseChargesOverTime.class, null, true),
	/**
	 * Field IgnoreSkill.
	 */
	IgnoreSkill(EffectBuff.class, null, false),
	/**
	 * Field Immobilize.
	 */
	Immobilize(EffectImmobilize.class, null, true),
	/**
	 * Field Interrupt.
	 */
	Interrupt(EffectInterrupt.class, null, true),
	/**
	 * Field Invulnerable.
	 */
	Invulnerable(EffectInvulnerable.class, AbnormalEffect.INVULNERABLE, false),
	/**
	 * Field Invisible.
	 */
	Invisible(EffectInvisible.class, AbnormalEffect.STEALTH, false),
	/**
	 * Field LockInventory.
	 */
	LockInventory(EffectLockInventory.class, null, false),
	/**
	 * Field CurseOfLifeFlow.
	 */
	CurseOfLifeFlow(EffectCurseOfLifeFlow.class, null, true),
	/**
	 * Field LDManaDamOverTime.
	 */
	LDManaDamOverTime(EffectLDManaDamOverTime.class, null, true),
	/**
	 * Field ManaDamOverTime.
	 */
	ManaDamOverTime(EffectManaDamOverTime.class, null, true),
	/**
	 * Field ManaHeal.
	 */
	ManaHeal(EffectManaHeal.class, null, false),
	/**
	 * Field ManaHealOverTime.
	 */
	ManaHealOverTime(EffectManaHealOverTime.class, null, false),
	/**
	 * Field ManaHealPercent.
	 */
	ManaHealPercent(EffectManaHealPercent.class, null, false),
	/**
	 * Field MDamOverTime.
	 */
	MDamOverTime(EffectMDamOverTime.class, null, false),
	/**
	 * Field Meditation.
	 */
	Meditation(EffectMeditation.class, null, false),
	/**
	 * Field MPDamPercent.
	 */
	MPDamPercent(EffectMPDamPercent.class, null, true),
	/**
	 * Field Mute.
	 */
	Mute(EffectMute.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field MuteAll.
	 */
	MuteAll(EffectMuteAll.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field MuteAttack.
	 */
	MuteAttack(EffectMuteAttack.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field MutePhisycal.
	 */
	MutePhisycal(EffectMutePhisycal.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	/**
	 * Field NegateEffects.
	 */
	NegateEffects(EffectNegateEffects.class, null, false),
	/**
	 * Field NegateMusic.
	 */
	NegateMusic(EffectNegateMusic.class, null, false),
	/**
	 * Field Paralyze.
	 */
	Paralyze(EffectParalyze.class, AbnormalEffect.HOLD_1, Stats.PARALYZE_RESIST, Stats.PARALYZE_POWER, true),
	/**
	 * Field Petrification.
	 */
	Petrification(EffectPetrification.class, AbnormalEffect.HOLD_2, Stats.PARALYZE_RESIST, Stats.PARALYZE_POWER, true),
	/**
	 * Field RandomHate.
	 */
	RandomHate(EffectRandomHate.class, null, true),
	/**
	 * Field Relax.
	 */
	Relax(EffectRelax.class, null, true),
	/**
	 * Field CannotTarget.
	 */
	CannotTarget(EffectCannotTarget.class, null, true),
	/**
	 * Field RemoveTarget.
	 */
	RemoveTarget(EffectRemoveTarget.class, null, true),
	/**
	 * Field RestorationRandom.
	 */
	RestorationRandom(EffectRestorationRandom.class, null, true),
	/**
	 * Field Restoration.
	 */
	Restoration(EffectRestoration.class, null, true),
	/**
	 * Field Root.
	 */
	Root(EffectRoot.class, AbnormalEffect.ROOT, Stats.ROOT_RESIST, Stats.ROOT_POWER, true),
	/**
	 * Field Hourglass.
	 */
	Hourglass(EffectHourglass.class, null, true),
	/**
	 * Field Salvation.
	 */
	Salvation(EffectSalvation.class, null, true),
	/**
	 * Field ServitorShare.
	 */
	ServitorShare(EffectServitorShare.class, null, true),
	/**
	 * Field SilentMove.
	 */
	SilentMove(EffectSilentMove.class, AbnormalEffect.STEALTH, true),
	/**
	 * Field Sleep.
	 */
	Sleep(EffectSleep.class, AbnormalEffect.SLEEP, Stats.SLEEP_RESIST, Stats.SLEEP_POWER, true),
	/**
	 * Field Stun.
	 */
	Stun(EffectStun.class, AbnormalEffect.STUN, Stats.STUN_RESIST, Stats.STUN_POWER, true),
	/**
	 * Field DispelOnHit.
	 */
	DispelOnHit(EffectDispelOnHIt.class, null, true),
	/**
	 * Field SummonSkill.
	 */
	SummonSkill(EffectSummonSkill.class, null, true),
	/**
	 * Field Symbol.
	 */
	Symbol(EffectSymbol.class, null, false),
	/**
	 * Field Transformation.
	 */
	Transformation(EffectTransformation.class, null, Stats.MUTATE_RESIST, Stats.MUTATE_POWER, true),
	/**
	 * Field UnAggro.
	 */
	UnAggro(EffectUnAggro.class, null, true),
	/**
	 * Field Vitality.
	 */
	Vitality(EffectBuff.class, AbnormalEffect.VITALITY, true),
	/**
	 * Field TalismanOfPower.
	 */
	TalismanOfPower(EffectTalismanOfPower.class, null, false),
	/**
	 * Field TargetToMe.
	 */
	TargetToMe(EffectTargetToMe.class, null, Stats.PULL_RESIST, Stats.PULL_POWER, true),
	/**
	 * Field TargetToOwner.
	 */
	TargetToOwner(EffectTargetToOwner.class, null, true),
	/**
	 * Field TransferDam.
	 */
	TransferDam(EffectBuff.class, null, false),
	/**
	 * Field Turner.
	 */
	Turner(EffectTurner.class, null, false),
	/**
	 * Field Poison.
	 */
	Poison(EffectDamOverTime.class, null, Stats.POISON_RESIST, Stats.POISON_POWER, false),
	/**
	 * Field PoisonLethal.
	 */
	PoisonLethal(EffectDamOverTimeLethal.class, null, Stats.POISON_RESIST, Stats.POISON_POWER, false),
	/**
	 * Field Bleed.
	 */
	Bleed(EffectDamOverTime.class, AbnormalEffect.BLEEDING, Stats.BLEED_RESIST, Stats.BLEED_POWER, false),
	/**
	 * Field Debuff.
	 */
	Debuff(EffectBuff.class, null, false),
	/**
	 * Field WatcherGaze.
	 */
	WatcherGaze(EffectBuff.class, null, false),
	/**
	 * Field KnockDown.
	 */
	KnockDown(EffectKnockDown.class, AbnormalEffect.S_51, Stats.KNOCKDOWN_RESIST, Stats.KNOCKDOWN_POWER, true),
	/**
	 * Field KnockBack.
	 */
	KnockBack(EffectKnockBack.class, null, Stats.KNOCKBACK_RESIST, Stats.KNOCKBACK_POWER, true),
	/**
	 * Field ShadowStep
	 */
	ShadowStep(EffectShadowStep.class, null, true),
	/**
	 * Field ChargesOverTime.
	 */
	ChargesOverTime(EffectChargesOverTime.class, null, true),
	/**
	 * Field Mentoring.
	 */
	Mentoring(EffectBuff.class, null, false),
	/**
	 * Field AbsorbDamageToEffector.
	 */
	AbsorbDamageToEffector(EffectBuff.class, null, false),
	/**
	 * Field AbsorbDamageToMp.
	 */
	AbsorbDamageToMp(EffectBuff.class, null, false),
	/**
	 * Field AbsorbDamageToSummon.
	 */
	AbsorbDamageToSummon(EffectLDManaDamOverTime.class, null, true);
	/**
	 * Field _constructor.
	 */
	private final Constructor<? extends Effect> _constructor;
	/**
	 * Field _abnormal.
	 */
	private final AbnormalEffect _abnormal;
	/**
	 * Field _resistType.
	 */
	private final Stats _resistType;
	/**
	 * Field _attributeType.
	 */
	private final Stats _attributeType;
	/**
	 * Field _isRaidImmune.
	 */
	private final boolean _isRaidImmune;
	
	/**
	 * Constructor for EffectType.
	 * @param clazz Class<? extends Effect>
	 * @param abnormal AbnormalEffect
	 * @param isRaidImmune boolean
	 */
	private EffectType(Class<? extends Effect> clazz, AbnormalEffect abnormal, boolean isRaidImmune)
	{
		this(clazz, abnormal, null, null, isRaidImmune);
	}
	
	/**
	 * Constructor for EffectType.
	 * @param clazz Class<? extends Effect>
	 * @param abnormal AbnormalEffect
	 * @param resistType Stats
	 * @param attributeType Stats
	 * @param isRaidImmune boolean
	 */
	private EffectType(Class<? extends Effect> clazz, AbnormalEffect abnormal, Stats resistType, Stats attributeType, boolean isRaidImmune)
	{
		try
		{
			_constructor = clazz.getConstructor(Env.class, EffectTemplate.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new Error(e);
		}
		_abnormal = abnormal;
		_resistType = resistType;
		_attributeType = attributeType;
		_isRaidImmune = isRaidImmune;
	}
	
	/**
	 * Method getAbnormal.
	 * @return AbnormalEffect
	 */
	public AbnormalEffect getAbnormal()
	{
		return _abnormal;
	}
	
	/**
	 * Method getResistType.
	 * @return Stats
	 */
	public Stats getResistType()
	{
		return _resistType;
	}
	
	/**
	 * Method getAttributeType.
	 * @return Stats
	 */
	public Stats getAttributeType()
	{
		return _attributeType;
	}
	
	/**
	 * Method isRaidImmune.
	 * @return boolean
	 */
	public boolean isRaidImmune()
	{
		return _isRaidImmune;
	}
	
	/**
	 * Method makeEffect.
	 * @param env Env
	 * @param template EffectTemplate
	 * @return Effect * @throws IllegalArgumentException * @throws InstantiationException * @throws IllegalAccessException * @throws InvocationTargetException
	 */
	public Effect makeEffect(Env env, EffectTemplate template) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return _constructor.newInstance(env, template);
	}
}

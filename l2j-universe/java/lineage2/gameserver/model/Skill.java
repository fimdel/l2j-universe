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
package lineage2.gameserver.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.geometry.Polygon;
import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager.ArenaParticipantsHolder;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.base.BaseStats;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.SkillTrait;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.ChestInstance;
import lineage2.gameserver.model.instances.DecoyInstance;
import lineage2.gameserver.model.instances.FeedableBeastInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.FlyToLocation.FlyType;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.skills.skillclasses.AIeffects;
import lineage2.gameserver.skills.skillclasses.Aggression;
import lineage2.gameserver.skills.skillclasses.Balance;
import lineage2.gameserver.skills.skillclasses.BeastFeed;
import lineage2.gameserver.skills.skillclasses.BuffCharger;
import lineage2.gameserver.skills.skillclasses.CPDam;
import lineage2.gameserver.skills.skillclasses.Call;
import lineage2.gameserver.skills.skillclasses.ChainHeal;
import lineage2.gameserver.skills.skillclasses.Charge;
import lineage2.gameserver.skills.skillclasses.ChargeSoul;
import lineage2.gameserver.skills.skillclasses.ClanGate;
import lineage2.gameserver.skills.skillclasses.CombatPointHeal;
import lineage2.gameserver.skills.skillclasses.Continuous;
import lineage2.gameserver.skills.skillclasses.Craft;
import lineage2.gameserver.skills.skillclasses.DeathPenalty;
import lineage2.gameserver.skills.skillclasses.Decoy;
import lineage2.gameserver.skills.skillclasses.Default;
import lineage2.gameserver.skills.skillclasses.DefuseTrap;
import lineage2.gameserver.skills.skillclasses.DeleteHate;
import lineage2.gameserver.skills.skillclasses.DeleteHateOfMe;
import lineage2.gameserver.skills.skillclasses.DestroySummon;
import lineage2.gameserver.skills.skillclasses.DetectTrap;
import lineage2.gameserver.skills.skillclasses.Disablers;
import lineage2.gameserver.skills.skillclasses.Drain;
import lineage2.gameserver.skills.skillclasses.DrainSoul;
import lineage2.gameserver.skills.skillclasses.EMDam;
import lineage2.gameserver.skills.skillclasses.EffectsFromSkills;
import lineage2.gameserver.skills.skillclasses.EnergyReplenish;
import lineage2.gameserver.skills.skillclasses.ExtractStone;
import lineage2.gameserver.skills.skillclasses.FishingSkill;
import lineage2.gameserver.skills.skillclasses.Harvesting;
import lineage2.gameserver.skills.skillclasses.Heal;
import lineage2.gameserver.skills.skillclasses.HealHpCp;
import lineage2.gameserver.skills.skillclasses.HealPercent;
import lineage2.gameserver.skills.skillclasses.HealWithCp;
import lineage2.gameserver.skills.skillclasses.ItemR;
import lineage2.gameserver.skills.skillclasses.KamaelWeaponExchange;
import lineage2.gameserver.skills.skillclasses.LethalShot;
import lineage2.gameserver.skills.skillclasses.MDam;
import lineage2.gameserver.skills.skillclasses.ManaDam;
import lineage2.gameserver.skills.skillclasses.ManaHeal;
import lineage2.gameserver.skills.skillclasses.ManaHealPercent;
import lineage2.gameserver.skills.skillclasses.NegateEffects;
import lineage2.gameserver.skills.skillclasses.NegateStats;
import lineage2.gameserver.skills.skillclasses.PDam;
import lineage2.gameserver.skills.skillclasses.PcBangPointsAdd;
import lineage2.gameserver.skills.skillclasses.PetSummon;
import lineage2.gameserver.skills.skillclasses.Plunder;
import lineage2.gameserver.skills.skillclasses.Recall;
import lineage2.gameserver.skills.skillclasses.ReelingPumping;
import lineage2.gameserver.skills.skillclasses.Refill;
import lineage2.gameserver.skills.skillclasses.Replace;
import lineage2.gameserver.skills.skillclasses.Restoration;
import lineage2.gameserver.skills.skillclasses.Resurrect;
import lineage2.gameserver.skills.skillclasses.Ride;
import lineage2.gameserver.skills.skillclasses.SPHeal;
import lineage2.gameserver.skills.skillclasses.ShiftAggression;
import lineage2.gameserver.skills.skillclasses.Sowing;
import lineage2.gameserver.skills.skillclasses.Spoil;
import lineage2.gameserver.skills.skillclasses.StealBuff;
import lineage2.gameserver.skills.skillclasses.Subjob;
import lineage2.gameserver.skills.skillclasses.SummonItem;
import lineage2.gameserver.skills.skillclasses.SummonMentor;
import lineage2.gameserver.skills.skillclasses.SummonServitor;
import lineage2.gameserver.skills.skillclasses.SummonSiegeFlag;
import lineage2.gameserver.skills.skillclasses.Sweep;
import lineage2.gameserver.skills.skillclasses.TakeCastle;
import lineage2.gameserver.skills.skillclasses.TakeFortress;
import lineage2.gameserver.skills.skillclasses.TameControl;
import lineage2.gameserver.skills.skillclasses.TeleportNpc;
import lineage2.gameserver.skills.skillclasses.Toggle;
import lineage2.gameserver.skills.skillclasses.Transformation;
import lineage2.gameserver.skills.skillclasses.Unlock;
import lineage2.gameserver.skills.skillclasses.VitalityHeal;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.StatTemplate;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Skill extends StatTemplate implements Cloneable
{
	/**
	 * @author Mobius
	 */
	public static class AddedSkill
	{
		/**
		 * Field EMPTY_ARRAY.
		 */
		public static final AddedSkill[] EMPTY_ARRAY = new AddedSkill[0];
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field level.
		 */
		public int level;
		/**
		 * Field _skill.
		 */
		private Skill _skill;
		
		/**
		 * Constructor for AddedSkill.
		 * @param id int
		 * @param level int
		 */
		public AddedSkill(int id, int level)
		{
			this.id = id;
			this.level = level;
		}
		
		/**
		 * Method getSkill.
		 * @return Skill
		 */
		public Skill getSkill()
		{
			if (_skill == null)
			{
				_skill = SkillTable.getInstance().getInfo(id, level);
			}
			return _skill;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static enum NextAction
	{
		/**
		 * Field ATTACK.
		 */
		ATTACK,
		/**
		 * Field CAST.
		 */
		CAST,
		/**
		 * Field DEFAULT.
		 */
		DEFAULT,
		/**
		 * Field MOVE.
		 */
		MOVE,
		/**
		 * Field NONE.
		 */
		NONE
	}
	
	/**
	 * @author Mobius
	 */
	public static enum SkillOpType
	{
		/**
		 * Field OP_ACTIVE.
		 */
		OP_ACTIVE,
		/**
		 * Field OP_PASSIVE.
		 */
		OP_PASSIVE,
		/**
		 * Field OP_TOGGLE.
		 */
		OP_TOGGLE,
		/**
		 * Field OP_ACTIVE_TOGGLE.
		 */
		OP_ACTIVE_TOGGLE
	}
	
	/**
	 * @author Mobius
	 */
	public static enum Ternary
	{
		/**
		 * Field TRUE.
		 */
		TRUE,
		/**
		 * Field FALSE.
		 */
		FALSE,
		/**
		 * Field DEFAULT.
		 */
		DEFAULT
	}
	
	/**
	 * @author Mobius
	 */
	public static enum SkillMagicType
	{
		/**
		 * Field PHYSIC.
		 */
		PHYSIC,
		/**
		 * Field MAGIC.
		 */
		MAGIC,
		/**
		 * Field SPECIAL.
		 */
		SPECIAL,
		/**
		 * Field MUSIC.
		 */
		MUSIC
	}
	
	/**
	 * @author Mobius
	 */
	public static enum SkillTargetType
	{
		/**
		 * Field TARGET_ALLY.
		 */
		TARGET_ALLY,
		/**
		 * Field TARGET_AREA.
		 */
		TARGET_AREA,
		/**
		 * Field TARGET_AREA_AIM_CORPSE.
		 */
		TARGET_AREA_AIM_CORPSE,
		/**
		 * Field TARGET_AURA.
		 */
		TARGET_AURA,
		/**
		 * Field TARGET_PET_AURA.
		 */
		TARGET_PET_AURA,
		/**
		 * Field TARGET_CHEST.
		 */
		TARGET_CHEST,
		/**
		 * Field TARGET_FEEDABLE_BEAST.
		 */
		TARGET_FEEDABLE_BEAST,
		/**
		 * Field TARGET_CLAN.
		 */
		TARGET_CLAN,
		/**
		 * Field TARGET_CLAN_ONLY.
		 */
		TARGET_CLAN_ONLY,
		/**
		 * Field TARGET_CORPSE.
		 */
		TARGET_CORPSE,
		/**
		 * Field TARGET_CORPSE_PLAYER.
		 */
		TARGET_CORPSE_PLAYER,
		/**
		 * Field TARGET_ENEMY_PET.
		 */
		TARGET_ENEMY_PET,
		/**
		 * Field TARGET_ENEMY_SUMMON.
		 */
		TARGET_ENEMY_SUMMON,
		/**
		 * Field TARGET_ENEMY_SERVITOR.
		 */
		TARGET_ENEMY_SERVITOR,
		/**
		 * Field TARGET_EVENT.
		 */
		TARGET_EVENT,
		/**
		 * Field TARGET_FLAGPOLE.
		 */
		TARGET_FLAGPOLE,
		/**
		 * Field TARGET_COMMCHANNEL.
		 */
		TARGET_COMMCHANNEL,
		/**
		 * Field TARGET_HOLY.
		 */
		TARGET_HOLY,
		/**
		 * Field TARGET_ITEM.
		 */
		TARGET_ITEM,
		/**
		 * Field TARGET_MENTEE.
		 */
		TARGET_MENTEE,
		/**
		 * Field TARGET_MULTIFACE.
		 */
		TARGET_MULTIFACE,
		/**
		 * Field TARGET_MULTIFACE_AURA.
		 */
		TARGET_MULTIFACE_AURA,
		/**
		 * Field TARGET_TUNNEL.
		 */
		TARGET_TUNNEL,
		/**
		 * Field TARGET_NONE.
		 */
		TARGET_NONE,
		/**
		 * Field TARGET_ONE.
		 */
		TARGET_ONE,
		/**
		 * Field TARGET_ONE_PLAYER.
		 */
		TARGET_ONE_PLAYER,
		/**
		 * Field TARGET_OWNER.
		 */
		TARGET_OWNER,
		/**
		 * Field TARGET_PARTY.
		 */
		TARGET_PARTY,
		/**
		 * Field TARGET_PARTY_ONE.
		 */
		TARGET_PARTY_ONE,
		/**
		 * Field TARGET_PARTY_WITHOUT_ME.
		 */
		TARGET_PARTY_WITHOUT_ME,
		/**
		 * Field TARGET_PET.
		 */
		TARGET_PET,
		/**
		 * Field TARGET_SUMMON.
		 */
		TARGET_SUMMON,
		/**
		 * Field TARGET_SELF.
		 */
		TARGET_SELF,
		/**
		 * Field TARGET_SIEGE.
		 */
		TARGET_SIEGE,
		/**
		 * Field TARGET_UNLOCKABLE.
		 */
		TARGET_UNLOCKABLE,
		/**
		 * Field TARGET_SUMMON_AURA.
		 */
		TARGET_SUMMON_AURA,
		/**
		 * Field TARGET_SUMMON_AURA_AND_ME.
		 */
		TARGET_SUMMON_AURA_AND_ME,
		/**
		 * Field TARGET_GROUND.
		 */
		TARGET_GROUND
	}
	
	/**
	 * @author Mobius
	 */
	public static enum SkillType
	{
		/**
		 * Field AGGRESSION.
		 */
		AGGRESSION(Aggression.class),
		/**
		 * Field AIEFFECTS.
		 */
		AIEFFECTS(AIeffects.class),
		/**
		 * Field BALANCE.
		 */
		BALANCE(Balance.class),
		/**
		 * Field BEAST_FEED.
		 */
		BEAST_FEED(BeastFeed.class),
		/**
		 * Field BLEED.
		 */
		BLEED(Continuous.class),
		/**
		 * Field BUFF.
		 */
		BUFF(Continuous.class),
		/**
		 * Field BUFF_CHARGER.
		 */
		BUFF_CHARGER(BuffCharger.class),
		/**
		 * Field CALL.
		 */
		CALL(Call.class),
		/**
		 * Field CHAIN_HEAL.
		 */
		CHAIN_HEAL(ChainHeal.class),
		/**
		 * Field CHARGE.
		 */
		CHARGE(Charge.class),
		/**
		 * Field CHARGE_SOUL.
		 */
		CHARGE_SOUL(ChargeSoul.class),
		/**
		 * Field CLAN_GATE.
		 */
		CLAN_GATE(ClanGate.class),
		/**
		 * Field COMBATPOINTHEAL.
		 */
		COMBATPOINTHEAL(CombatPointHeal.class),
		/**
		 * Field CONT.
		 */
		CONT(Toggle.class),
		/**
		 * Field CPDAM.
		 */
		CPDAM(CPDam.class),
		/**
		 * Field CPHOT.
		 */
		CPHOT(Continuous.class),
		/**
		 * Field CRAFT.
		 */
		CRAFT(Craft.class),
		/**
		 * Field DEATH_PENALTY.
		 */
		DEATH_PENALTY(DeathPenalty.class),
		/**
		 * Field DECOY.
		 */
		DECOY(Decoy.class),
		/**
		 * Field DEBUFF.
		 */
		DEBUFF(Continuous.class),
		/**
		 * Field DELETE_HATE.
		 */
		DELETE_HATE(DeleteHate.class),
		/**
		 * Field DELETE_HATE_OF_ME.
		 */
		DELETE_HATE_OF_ME(DeleteHateOfMe.class),
		/**
		 * Field DESTROY_SUMMON.
		 */
		DESTROY_SUMMON(DestroySummon.class),
		/**
		 * Field DEFUSE_TRAP.
		 */
		DEFUSE_TRAP(DefuseTrap.class),
		/**
		 * Field DETECT_TRAP.
		 */
		DETECT_TRAP(DetectTrap.class),
		/**
		 * Field DISCORD.
		 */
		DISCORD(Continuous.class),
		/**
		 * Field DOT.
		 */
		DOT(Continuous.class),
		/**
		 * Field DRAIN.
		 */
		DRAIN(Drain.class),
		/**
		 * Field DRAIN_SOUL.
		 */
		DRAIN_SOUL(DrainSoul.class),
		/**
		 * Field EFFECT.
		 */
		EFFECT(lineage2.gameserver.skills.skillclasses.Effect.class),
		/**
		 * Field EFFECTS_FROM_SKILLS.
		 */
		EFFECTS_FROM_SKILLS(EffectsFromSkills.class),
		/**
		 * Field ENERGY_REPLENISH.
		 */
		ENERGY_REPLENISH(EnergyReplenish.class),
		/**
		 * Field ENCHANT_ARMOR.
		 */
		ENCHANT_ARMOR,
		/**
		 * Field ENCHANT_WEAPON.
		 */
		ENCHANT_WEAPON,
		/**
		 * Field EXTRACT_STONE.
		 */
		EXTRACT_STONE(ExtractStone.class),
		/**
		 * Field FEED_PET.
		 */
		FEED_PET,
		/**
		 * Field FISHING.
		 */
		FISHING(FishingSkill.class),
		/**
		 * Field HARDCODED.
		 */
		HARDCODED(lineage2.gameserver.skills.skillclasses.Effect.class),
		/**
		 * Field HARVESTING.
		 */
		HARVESTING(Harvesting.class),
		/**
		 * Field HEAL.
		 */
		HEAL(Heal.class),
		/**
		 * Field HEAL_WITH_CP.
		 */
		HEAL_WITH_CP(HealWithCp.class),
		/**
		 * Field HEAL_HP_CP.
		 */
		HEAL_HP_CP(HealHpCp.class),
		/**
		 * Field HEAL_PERCENT.
		 */
		HEAL_PERCENT(HealPercent.class),
		/**
		 * Field HOT.
		 */
		HOT(Continuous.class),
		/**
		 * Field KAMAEL_WEAPON_EXCHANGE.
		 */
		KAMAEL_WEAPON_EXCHANGE(KamaelWeaponExchange.class),
		/**
		 * Field ITEM_R.
		 */
		ITEM_R(ItemR.class),
		/**
		 * Field LETHAL_SHOT.
		 */
		LETHAL_SHOT(LethalShot.class),
		/**
		 * Field LUCK.
		 */
		LUCK,
		/**
		 * Field MANADAM.
		 */
		MANADAM(ManaDam.class),
		/**
		 * Field MANAHEAL.
		 */
		MANAHEAL(ManaHeal.class),
		/**
		 * Field MANAHEAL_PERCENT.
		 */
		MANAHEAL_PERCENT(ManaHealPercent.class),
		/**
		 * Field MDAM.
		 */
		MDAM(MDam.class),
		/**
		 * Field EMDAM.
		 */
		EMDAM(EMDam.class),
		/**
		 * Field MDOT.
		 */
		MDOT(Continuous.class),
		/**
		 * Field MPHOT.
		 */
		MPHOT(Continuous.class),
		/**
		 * Field MUTE.
		 */
		MUTE(Disablers.class),
		/**
		 * Field NEGATE_EFFECTS.
		 */
		NEGATE_EFFECTS(NegateEffects.class),
		/**
		 * Field NEGATE_STATS.
		 */
		NEGATE_STATS(NegateStats.class),
		/**
		 * Field ADD_PC_BANG.
		 */
		ADD_PC_BANG(PcBangPointsAdd.class),
		/**
		 * Field NOTDONE.
		 */
		NOTDONE,
		/**
		 * Field NOTUSED.
		 */
		NOTUSED,
		/**
		 * Field PARALYZE.
		 */
		PARALYZE(Disablers.class),
		/**
		 * Field PASSIVE.
		 */
		PASSIVE,
		/**
		 * Field PDAM.
		 */
		PDAM(PDam.class),
		/**
		 * Field PET_SUMMON.
		 */
		PET_SUMMON(PetSummon.class),
		/**
		 * Field POISON.
		 */
		POISON(Continuous.class),
		/**
		 * Field PUMPING.
		 */
		PUMPING(ReelingPumping.class),
		/**
		 * Field RECALL.
		 */
		RECALL(Recall.class),
		/**
		 * Field REELING.
		 */
		REELING(ReelingPumping.class),
		/**
		 * Field REFILL.
		 */
		REFILL(Refill.class),
		/**
		 * Field RESURRECT.
		 */
		RESURRECT(Resurrect.class),
		/**
		 * Field RESTORE_ITEM.
		 */
		RESTORE_ITEM(lineage2.gameserver.skills.skillclasses.Effect.class),
		/**
		 * Field REPLACE.
		 */
		REPLACE(Replace.class),
		/**
		 * Field RIDE.
		 */
		RIDE(Ride.class),
		/**
		 * Field ROOT.
		 */
		ROOT(Disablers.class),
		/**
		 * Field SHIFT_AGGRESSION.
		 */
		SHIFT_AGGRESSION(ShiftAggression.class),
		/**
		 * Field SLEEP.
		 */
		SLEEP(Disablers.class),
		/**
		 * Field SOULSHOT.
		 */
		SOULSHOT,
		/**
		 * Field SOWING.
		 */
		SOWING(Sowing.class),
		/**
		 * Field SPHEAL.
		 */
		SPHEAL(SPHeal.class),
		/**
		 * Field SPIRITSHOT.
		 */
		SPIRITSHOT,
		/**
		 * Field SPOIL.
		 */
		SPOIL(Spoil.class),
		/**
		 * Field STEAL_BUFF.
		 */
		STEAL_BUFF(StealBuff.class),
		/**
		 * Field STUN.
		 */
		STUN(Disablers.class),
		/**
		 * Field SUB_JOB.
		 */
		SUB_JOB(Subjob.class),
		/**
		 * Field SUMMON.
		 */
		SUMMON(SummonServitor.class),
		/**
		 * Field SUMMON_FLAG.
		 */
		SUMMON_FLAG(SummonSiegeFlag.class),
		/**
		 * Field SUMMON_ITEM.
		 */
		SUMMON_ITEM(SummonItem.class),
		RESTORATION(Restoration.class), 
		/**
		 * Field SUMMON_MENTOR.
		 */
		SUMMON_MENTOR(SummonMentor.class),
		/**
		 * Field SWEEP.
		 */
		SWEEP(Sweep.class),
		/**
		 * Field TAKECASTLE.
		 */
		TAKECASTLE(TakeCastle.class),
		/**
		 * Field TAKEFORTRESS.
		 */
		TAKEFORTRESS(TakeFortress.class),
		/**
		 * Field TAMECONTROL.
		 */
		TAMECONTROL(TameControl.class),
		/**
		 * Field TELEPORT_NPC.
		 */
		TELEPORT_NPC(TeleportNpc.class),
		/**
		 * Field TRANSFORMATION.
		 */
		TRANSFORMATION(Transformation.class),
		/**
		 * Field UNLOCK.
		 */
		UNLOCK(Unlock.class),
		/**
		 * Field WATCHER_GAZE.
		 */
		WATCHER_GAZE(Continuous.class),
		/**
		 * Field VITALITY_HEAL.
		 */
		VITALITY_HEAL(VitalityHeal.class),
		/**
		 * Field SWEEP.
		 */
		PLUNDER(Plunder.class);
		/**
		 * Field clazz.
		 */
		private final Class<? extends Skill> clazz;
		
		/**
		 * Constructor for SkillType.
		 */
		private SkillType()
		{
			clazz = Default.class;
		}
		
		/**
		 * Constructor for SkillType.
		 * @param clazz Class<? extends Skill>
		 */
		private SkillType(Class<? extends Skill> clazz)
		{
			this.clazz = clazz;
		}
		
		/**
		 * Method makeSkill.
		 * @param set StatsSet
		 * @return Skill
		 */
		public Skill makeSkill(StatsSet set)
		{
			try
			{
				Constructor<? extends Skill> c = clazz.getConstructor(StatsSet.class);
				return c.newInstance(set);
			}
			catch (Exception e)
			{
				_log.error("", e);
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Method isPvM.
		 * @return boolean
		 */
		public final boolean isPvM()
		{
			switch (this)
			{
				case DISCORD:
					return true;
				default:
					return false;
			}
		}
		
		/**
		 * Method isAI.
		 * @return boolean
		 */
		public boolean isAI()
		{
			switch (this)
			{
				case AGGRESSION:
				case AIEFFECTS:
				case SOWING:
				case DELETE_HATE:
				case DELETE_HATE_OF_ME:
					return true;
				default:
					return false;
			}
		}
		
		/**
		 * Method isPvpSkill.
		 * @return boolean
		 */
		public final boolean isPvpSkill()
		{
			switch (this)
			{
				case BLEED:
				case AGGRESSION:
				case DEBUFF:
				case DOT:
				case MDOT:
				case MUTE:
				case PARALYZE:
				case POISON:
				case ROOT:
				case SLEEP:
				case MANADAM:
				case DESTROY_SUMMON:
				case NEGATE_STATS:
				case NEGATE_EFFECTS:
				case STEAL_BUFF:
				case DELETE_HATE:
				case DELETE_HATE_OF_ME:
					return true;
				default:
					return false;
			}
		}
		
		/**
		 * Method isOffensive.
		 * @return boolean
		 */
		public boolean isOffensive()
		{
			switch (this)
			{
				case AGGRESSION:
				case AIEFFECTS:
				case BLEED:
				case DEBUFF:
				case DOT:
				case DRAIN:
				case DRAIN_SOUL:
				case LETHAL_SHOT:
				case MANADAM:
				case MDAM:
				case MDOT:
				case MUTE:
				case PARALYZE:
				case PDAM:
				case CPDAM:
				case POISON:
				case ROOT:
				case SLEEP:
				case SOULSHOT:
				case SPIRITSHOT:
				case SPOIL:
				case STUN:
				case SWEEP:
				case HARVESTING:
				case TELEPORT_NPC:
				case SOWING:
				case DELETE_HATE:
				case DELETE_HATE_OF_ME:
				case DESTROY_SUMMON:
				case STEAL_BUFF:
				case DISCORD:
				case PLUNDER:
					return true;
				default:
					return false;
			}
		}
	}
	
	/**
	 * Field _log.
	 */
	static final Logger _log = LoggerFactory.getLogger(Skill.class);
	/**
	 * Field EMPTY_ARRAY.
	 */
	public static final Skill[] EMPTY_ARRAY = new Skill[0];
	/**
	 * Field _effectTemplates.
	 */
	protected EffectTemplate[] _effectTemplates = EffectTemplate.EMPTY_ARRAY;
	/**
	 * Field _teachers.
	 */
	protected List<Integer> _teachers;
	/**
	 * Field _canLearn.
	 */
	protected List<ClassId> _canLearn;
	/**
	 * Field _addedSkills.
	 */
	protected AddedSkill[] _addedSkills = AddedSkill.EMPTY_ARRAY;
	/**
	 * Field _itemConsume.
	 */
	protected final int[] _itemConsume;
	/**
	 * Field _itemConsumeId.
	 */
	protected final int[] _itemConsumeId;
	/**
	 * Field _relationSkillsId.
	 */
	protected final int[] _relationSkillsId;
	/**
	 * Field _referenceItemId.
	 */
	protected final int _referenceItemId;
	/**
	 * Field _referenceItemMpConsume.
	 */
	protected final int _referenceItemMpConsume;
	/**
	 * Field SKILL_CRAFTING. (value is 172)
	 */
	public static final int SKILL_CRAFTING = 172;
	/**
	 * Field SKILL_POLEARM_MASTERY. (value is 216)
	 */
	public static final int SKILL_POLEARM_MASTERY = 216;
	/**
	 * Field SKILL_CRYSTALLIZE. (value is 248)
	 */
	public static final int SKILL_CRYSTALLIZE = 248;
	/**
	 * Field SKILL_WEAPON_MAGIC_MASTERY1. (value is 249)
	 */
	public static final int SKILL_WEAPON_MAGIC_MASTERY1 = 249;
	/**
	 * Field SKILL_WEAPON_MAGIC_MASTERY2. (value is 250)
	 */
	public static final int SKILL_WEAPON_MAGIC_MASTERY2 = 250;
	/**
	 * Field SKILL_BLINDING_BLOW. (value is 321)
	 */
	public static final int SKILL_BLINDING_BLOW = 321;
	/**
	 * Field SKILL_STRIDER_ASSAULT. (value is 325)
	 */
	public static final int SKILL_STRIDER_ASSAULT = 325;
	/**
	 * Field SKILL_WYVERN_AEGIS. (value is 327)
	 */
	public static final int SKILL_WYVERN_AEGIS = 327;
	/**
	 * Field SKILL_BLUFF. (value is 358)
	 */
	public static final int SKILL_BLUFF = 358;
	/**
	 * Field SKILL_HEROIC_MIRACLE. (value is 395)
	 */
	public static final int SKILL_HEROIC_MIRACLE = 395;
	/**
	 * Field SKILL_HEROIC_BERSERKER. (value is 396)
	 */
	public static final int SKILL_HEROIC_BERSERKER = 396;
	/**
	 * Field SKILL_SOUL_MASTERY. (value is 467)
	 */
	public static final int SKILL_SOUL_MASTERY = 467;
	/**
	 * Field SKILL_TRANSFORM_DISPEL. (value is 619)
	 */
	public static final int SKILL_TRANSFORM_DISPEL = 619;
	/**
	 * Field SKILL_FINAL_FLYING_FORM. (value is 840)
	 */
	public static final int SKILL_FINAL_FLYING_FORM = 840;
	/**
	 * Field SKILL_AURA_BIRD_FALCON. (value is 841)
	 */
	public static final int SKILL_AURA_BIRD_FALCON = 841;
	/**
	 * Field SKILL_AURA_BIRD_OWL. (value is 842)
	 */
	public static final int SKILL_AURA_BIRD_OWL = 842;
	/**
	 * Field SKILL_DETECTION. (value is 933)
	 */
	public static final int SKILL_DETECTION = 933;
	/**
	 * Field SKILL_RECHARGE. (value is 1013)
	 */
	public static final int SKILL_RECHARGE = 1013;
	/**
	 * Field SKILL_TRANSFER_PAIN. (value is 1262)
	 */
	public static final int SKILL_TRANSFER_PAIN = 1262;
	/**
	 * Field SKILL_FISHING_MASTERY. (value is 1315)
	 */
	public static final int SKILL_FISHING_MASTERY = 1315;
	/**
	 * Field SKILL_NOBLESSE_BLESSING. (value is 1323)
	 */
	public static final int SKILL_NOBLESSE_BLESSING = 1323;
	/**
	 * Field SKILL_SUMMON_CP_POTION. (value is 1324)
	 */
	public static final int SKILL_SUMMON_CP_POTION = 1324;
	/**
	 * Field SKILL_FORTUNE_OF_NOBLESSE. (value is 1325)
	 */
	public static final int SKILL_FORTUNE_OF_NOBLESSE = 1325;
	/**
	 * Field SKILL_HARMONY_OF_NOBLESSE. (value is 1326)
	 */
	public static final int SKILL_HARMONY_OF_NOBLESSE = 1326;
	/**
	 * Field SKILL_SYMPHONY_OF_NOBLESSE. (value is 1327)
	 */
	public static final int SKILL_SYMPHONY_OF_NOBLESSE = 1327;
	/**
	 * Field SKILL_HEROIC_VALOR. (value is 1374)
	 */
	public static final int SKILL_HEROIC_VALOR = 1374;
	/**
	 * Field SKILL_HEROIC_GRANDEUR. (value is 1375)
	 */
	public static final int SKILL_HEROIC_GRANDEUR = 1375;
	/**
	 * Field SKILL_HEROIC_DREAD. (value is 1376)
	 */
	public static final int SKILL_HEROIC_DREAD = 1376;
	/**
	 * Field SKILL_MYSTIC_IMMUNITY. (value is 1411)
	 */
	public static final int SKILL_MYSTIC_IMMUNITY = 1411;
	/**
	 * Field SKILL_RAID_BLESSING. (value is 2168)
	 */
	public static final int SKILL_RAID_BLESSING = 2168;
	/**
	 * Field SKILL_HINDER_STRIDER. (value is 4258)
	 */
	public static final int SKILL_HINDER_STRIDER = 4258;
	/**
	 * Field SKILL_WYVERN_BREATH. (value is 4289)
	 */
	public static final int SKILL_WYVERN_BREATH = 4289;
	/**
	 * Field SKILL_RAID_CURSE. (value is 4515)
	 */
	public static final int SKILL_RAID_CURSE = 4515;
	/**
	 * Field SKILL_CHARM_OF_COURAGE. (value is 5041)
	 */
	public static final int SKILL_CHARM_OF_COURAGE = 5041;
	/**
	 * Field SKILL_EVENT_TIMER. (value is 5239)
	 */
	public static final int SKILL_EVENT_TIMER = 5239;
	/**
	 * Field SKILL_BATTLEFIELD_DEATH_SYNDROME. (value is 5660)
	 */
	public static final int SKILL_BATTLEFIELD_DEATH_SYNDROME = 5660;
	/**
	 * Field SKILL_SERVITOR_SHARE. (value is 1557)
	 */
	public static final int SKILL_SERVITOR_SHARE = 1557;
	/**
	 * Field SKILL_TRUE_FIRE. (value is 11007)
	 */
	public static final int SKILL_TRUE_FIRE = 11007;
	/**
	 * Field SKILL_TRUE_WATER. (value is 11008)
	 */
	public static final int SKILL_TRUE_WATER = 11008;
	/**
	 * Field SKILL_TRUE_WIND. (value is 11009)
	 */
	public static final int SKILL_TRUE_WIND = 11009;
	/**
	 * Field SKILL_TRUE_EARTH. (value is 11010)
	 */
	public static final int SKILL_TRUE_EARTH = 11010;
	/**
	 * Field SKILL_DUAL_CAST. (value is 11068)
	 */
	public static final int SKILL_DUAL_CAST = 11068;
	/**
	 * Field _isAuraSkill.
	 */
	protected boolean _isAuraSkill;
	/**
	 * Field _isAlterSkill.
	 */
	protected boolean _isAlterSkill;
	/**
	 * Field _isAltUse.
	 */
	protected boolean _isAltUse;
	/**
	 * Field _isBehind.
	 */
	protected boolean _isBehind;
	/**
	 * Field _scopeAngle.
	 */
	protected int _scopeAngle;
	/**
	 * Field _maxHitCancelCount.
	 */
	protected int _maxHitCancelCount;
	/**
	 * Field _isCancelable.
	 */
	protected boolean _isCancelable;
	/**
	 * Field _isCorpse.
	 */
	protected boolean _isCorpse;
	/**
	 * Field _isCommon.
	 */
	protected boolean _isCommon;
	/**
	 * Field _castOverStun.
	 */
	protected boolean _castOverStun;
	/**
	 * Field _isItemHandler.
	 */
	protected boolean _isItemHandler;
	/**
	 * Field _isOffensive.
	 */
	protected boolean _isOffensive;
	/**
	 * Field _isPvpSkill.
	 */
	protected boolean _isPvpSkill;
	/**
	 * Field _isNotUsedByAI.
	 */
	protected boolean _isNotUsedByAI;
	/**
	 * Field _isFishingSkill.
	 */
	protected boolean _isFishingSkill;
	/**
	 * Field _isPvm.
	 */
	protected boolean _isPvm;
	/**
	 * Field _isForceUse.
	 */
	protected boolean _isForceUse;
	/**
	 * Field _isNewbie.
	 */
	protected boolean _isNewbie;
	/**
	 * Field _isPreservedOnDeath.
	 */
	protected boolean _isPreservedOnDeath;
	/**
	 * Field _isHeroic.
	 */
	protected boolean _isHeroic;
	/**
	 * Field _isSaveable.
	 */
	protected boolean _isSaveable;
	/**
	 * Field _isSkillTimePermanent.
	 */
	protected boolean _isSkillTimePermanent;
	/**
	 * Field _isReuseDelayPermanent.
	 */
	protected boolean _isReuseDelayPermanent;
	/**
	 * Field _isHealDamageSkill.
	 */
	protected boolean _isHealDamageSkill = false;
	/**
	 * Field _lakcisHealStance.
	 */
	protected boolean _skillHealStance = false;
	/**
	 * Field _isReflectable.
	 */
	protected boolean _isReflectable;
	/**
	 * Field _isSuicideAttack.
	 */
	protected boolean _isSuicideAttack;
	/**
	 * Field _isShieldignore.
	 */
	protected boolean _isShieldignore;
	/**
	 * Field _isUndeadOnly.
	 */
	protected boolean _isUndeadOnly;
	/**
	 * Field _isUseSS.
	 */
	protected Ternary _isUseSS;
	/**
	 * Field _isOverhit.
	 */
	protected boolean _isOverhit;
	/**
	 * Field _isSoulBoost.
	 */
	protected boolean _isSoulBoost;
	/**
	 * Field _isChargeBoost.
	 */
	protected boolean _isChargeBoost;
	/**
	 * Field _isUsingWhileCasting.
	 */
	protected boolean _isUsingWhileCasting;
	/**
	 * Field _isIgnoreResists.
	 */
	protected boolean _isIgnoreResists;
	/**
	 * Field _isIgnoreInvul.
	 */
	protected boolean _isIgnoreInvul;
	/**
	 * Field _isTrigger.
	 */
	protected boolean _isTrigger;
	/**
	 * Field _isNotAffectedByMute.
	 */
	protected boolean _isNotAffectedByMute;
	/**
	 * Field _basedOnTargetDebuff.
	 */
	protected boolean _basedOnTargetDebuff;
	/**
	 * Field _deathlink.
	 */
	protected boolean _deathlink;
	/**
	 * Field _hideStartMessage.
	 */
	protected boolean _hideStartMessage;
	/**
	 * Field _hideUseMessage.
	 */
	protected boolean _hideUseMessage;
	/**
	 * Field _skillInterrupt.
	 */
	protected boolean _skillInterrupt;
	/**
	 * Field _flyingTransformUsage.
	 */
	protected boolean _flyingTransformUsage;
	/**
	 * Field _flySpeed.
	 */
	protected int _flySpeed;
	/**
	 * Field _canUseTeleport.
	 */
	protected boolean _canUseTeleport;
	/**
	 * Field _isProvoke.
	 */
	protected boolean _isProvoke;
	/**
	 * Field _isCubicSkill.
	 */
	protected boolean _isCubicSkill = false;
	/**
	 * Field _isAwakeningToggle.
	 */
	protected boolean _isAwakeningToggle = false;
	/**
	 * Field _isSelfDispellable.
	 */
	protected boolean _isSelfDispellable;
	/**
	 * Field _isRelation.
	 */
	protected boolean _isRelation = false;
	/**
	 * Field _skillType.
	 */
	protected SkillType _skillType;
	/**
	 * Field _operateType.
	 */
	protected SkillOpType _operateType;
	/**
	 * Field _targetType.
	 */
	protected SkillTargetType _targetType;
	/**
	 * Field _magicType.
	 */
	protected SkillMagicType _magicType;
	/**
	 * Field _traitType.
	 */
	protected SkillTrait _traitType;
	/**
	 * Field _saveVs.
	 */
	protected BaseStats _saveVs;
	/**
	 * Field _dispelOnDamage;
	 */
	protected boolean _dispelOnDamage;
	/**
	 * Field _nextAction.
	 */
	protected NextAction _nextAction;
	/**
	 * Field _element.
	 */
	protected Element _element;
	/**
	 * Field _flyType.
	 */
	protected FlyType _flyType;
	/**
	 * Field _flyToBack.
	 */
	protected boolean _flyToBack;
	/**
	 * Field _preCondition.
	 */
	protected Condition[] _preCondition = Condition.EMPTY_ARRAY;
	/**
	 * Field _id.
	 */
	public int _id;
	/**
	 * Field _level.
	 */
	protected int _level;
	/**
	 * Field _baseLevel.
	 */
	protected int _baseLevel;
	/**
	 * Field _displayId.
	 */
	protected int _displayId;
	/**
	 * Field _displayLevel.
	 */
	protected int _displayLevel;
	/**
	 * Field _activateRate.
	 */
	protected int _activateRate;
	/**
	 * Field _castRange.
	 */
	protected int _castRange;
	/**
	 * Field _cancelTarget.
	 */
	protected int _cancelTarget;
	/**
	 * Field _condCharges.
	 */
	protected int _condCharges;
	/**
	 * Field _coolTime.
	 */
	protected int _coolTime;
	/**
	 * Field _delayedEffect.
	 */
	protected int _delayedEffect;
	/**
	 * Field _effectPoint.
	 */
	protected int _effectPoint;
	/**
	 * Field _energyConsume.
	 */
	protected int _energyConsume;
	/**
	 * Field _elementPower.
	 */
	protected int _elementPower;
	/**
	 * Field _flyRadius.
	 */
	protected int _flyRadius;
	/**
	 * Field _hitTime.
	 */
	protected int _hitTime;
	/**
	 * Field _hpConsume.
	 */
	protected int _hpConsume;
	/**
	 * Field _levelModifier.
	 */
	protected int _levelModifier;
	/**
	 * Field _magicLevel.
	 */
	protected int _magicLevel;
	/**
	 * Field _matak.
	 */
	protected int _matak;
	/**
	 * Field _minPledgeClass.
	 */
	protected int _minPledgeClass;
	/**
	 * Field _minRank.
	 */
	protected int _minRank;
	/**
	 * Field _negatePower.
	 */
	protected int _negatePower;
	/**
	 * Field _negateSkill.
	 */
	protected int _negateSkill;
	/**
	 * Field _npcId.
	 */
	protected int _npcId;
	/**
	 * Field _numCharges.
	 */
	protected int _numCharges;
	/**
	 * Field _maxCharges.
	 */
	protected int _maxCharges;
	/**
	 * Field _skillInterruptTime.
	 */
	protected int _skillInterruptTime;
	/**
	 * Field _skillRadius.
	 */
	protected int _skillRadius;
	/**
	 * Field _soulsConsume.
	 */
	protected int _soulsConsume;
	/**
	 * Field _symbolId.
	 */
	protected int _symbolId;
	/**
	 * Field _weaponsAllowed.
	 */
	protected int _weaponsAllowed;
	/**
	 * Field _castCount.
	 */
	protected int _castCount;
	/**
	 * Field _enchantLevelCount.
	 */
	protected int _enchantLevelCount;
	/**
	 * Field _criticalRate.
	 */
	protected int _criticalRate;
	/**
	 * Field _reuseDelay.
	 */
	protected long _reuseDelay;
	/**
	 * Field _power.
	 */
	protected double _power;
	/**
	 * Field _powerPvP.
	 */
	protected double _powerPvP;
	/**
	 * Field _powerPvE.
	 */
	protected double _powerPvE;
	/**
	 * Field _mpConsume1.
	 */
	protected double _mpConsume1;
	/**
	 * Field _mpConsume2.
	 */
	protected double _mpConsume2;
	/**
	 * Field _lethal1.
	 */
	protected double _lethal1;
	/**
	 * Field _lethal2.
	 */
	protected double _lethal2;
	/**
	 * Field _absorbPart.
	 */
	protected double _absorbPart;
	/**
	 * Field _name.
	 */
	protected String _name;
	/**
	 * Field _baseValues.
	 */
	protected String _baseValues;
	/**
	 * Field _icon.
	 */
	protected String _icon;

	protected boolean _isMarkDamage;
	/**
	 * Field _skillToCast.
	 */
	protected int _skillToCast;
	/**
	 * Field _skillToCastLevel.
	 */
	protected int _skillToCastLevel;
	/**
	 * Field hashCode.
	 */
	private final int hashCode;
	/**
	 * Field reuseGroupId.
	 */
	private final int reuseGroupId = -1;
	protected boolean _isPowerModified = false;
	private int _powerModCount;
	private HashMap <List<String>, Double> _powerModifiers = new HashMap<List<String>, Double>();
	private double _power2;
	
	/**
	 * Constructor for Skill.
	 * @param set StatsSet
	 */
	protected Skill(StatsSet set)
	{
		_id = set.getInteger("skill_id");
		_level = set.getInteger("level");
		_displayId = set.getInteger("displayId", _id);
		_displayLevel = set.getInteger("displayLevel", _level);
		_baseLevel = set.getInteger("base_level");
		_name = set.getString("name");
		_operateType = set.getEnum("operateType", SkillOpType.class);
		_isNewbie = set.getBool("isNewbie", false);
		_isSelfDispellable = set.getBool("isSelfDispellable", true);
		_isPreservedOnDeath = set.getBool("isPreservedOnDeath", false);
		_isAwakeningToggle = set.getBool("isAwakeningToggle", false);
		_isHeroic = set.getBool("isHeroic", false);
		_isAltUse = set.getBool("altUse", false);
		_mpConsume1 = set.getInteger("mpConsume1", 0);
		_mpConsume2 = set.getInteger("mpConsume2", 0);
		_maxHitCancelCount = set.getInteger("maxHitCancelCount", 0);
		_energyConsume = set.getInteger("energyConsume", 0);
		_hpConsume = set.getInteger("hpConsume", 0);
		_soulsConsume = set.getInteger("soulsConsume", 0);
		_isSoulBoost = set.getBool("soulBoost", false);
		_isChargeBoost = set.getBool("chargeBoost", false);
		_isProvoke = set.getBool("provoke", false);
		_isUsingWhileCasting = set.getBool("isUsingWhileCasting", false);
		_matak = set.getInteger("mAtk", 0);
		_isUseSS = Ternary.valueOf(set.getString("useSS", Ternary.DEFAULT.toString()).toUpperCase());
		_magicLevel = set.getInteger("magicLevel", 0);
		_castCount = set.getInteger("castCount", 0);
		_castRange = set.getInteger("castRange", 40);
		_baseValues = set.getString("baseValues", null);
		_skillToCast = set.getInteger("skillToCast", 0);
		_skillToCastLevel = set.getInteger("skillToCastLevel", 0);
		String s1 = set.getString("itemConsumeCount", "");
		String s2 = set.getString("itemConsumeId", "");
		String s3 = set.getString("relationSkillsId", "");
		_powerModCount = set.getInteger("powerModCount",0);
		if(!(_powerModCount == 0))
		{
			_isPowerModified = true;
			for(int i = 0; i < _powerModCount; i++)
			{
				List <String> weaponsMod = new ArrayList<String>();
				String sPowerMod = set.getString("powerModByWeapon"+String.valueOf(i+1),"");
				double dPowerMod = set.getDouble("powerModPercent"+String.valueOf(i+1),1.);
				if(sPowerMod.length() == 0)
				{
					weaponsMod.add("None");
				}
				else
				{
					String[] s = sPowerMod.split(";");
					for(int j = 0; j < s.length; j++)
					{
						weaponsMod.add(s[j]);
					}
				}
				_powerModifiers.put(weaponsMod,dPowerMod);
			}
		}
		if (s1.length() == 0)
		{
			_itemConsume = new int[]
			{
				0
			};
		}
		else
		{
			String[] s = s1.split(" ");
			_itemConsume = new int[s.length];
			for (int i = 0; i < s.length; i++)
			{
				_itemConsume[i] = Integer.parseInt(s[i]);
			}
		}
		if (s2.length() == 0)
		{
			_itemConsumeId = new int[]
			{
				0
			};
		}
		else
		{
			String[] s = s2.split(" ");
			_itemConsumeId = new int[s.length];
			for (int i = 0; i < s.length; i++)
			{
				_itemConsumeId[i] = Integer.parseInt(s[i]);
			}
		}
		if (s3.length() == 0)
		{
			_relationSkillsId = new int[]
			{
				0
			};
		}
		else
		{
			_isRelation = true;
			String[] s = s3.split(";");
			_relationSkillsId = new int[s.length];
			for (int i = 0; i < s.length; i++)
			{
				_relationSkillsId[i] = Integer.parseInt(s[i]);
			}
		}
		_referenceItemId = set.getInteger("referenceItemId", 0);
		_referenceItemMpConsume = set.getInteger("referenceItemMpConsume", 0);
		_castOverStun = set.getBool("castOverStun", false);
		_isItemHandler = set.getBool("isHandler", false);
		_isCommon = set.getBool("isCommon", false);
		_isAuraSkill= set.getBool("isAuraSkill", false);
		_isAlterSkill= set.getBool("isAlterSkill", false);
		_isSaveable = set.getBool("isSaveable", true);
		_coolTime = set.getInteger("coolTime", 0);
		_skillInterruptTime = set.getInteger("hitCancelTime", 0);
		_reuseDelay = set.getLong("reuseDelay", 0);
		_hitTime = set.getInteger("hitTime", 0);
		_skillRadius = set.getInteger("skillRadius", 80);
		_targetType = set.getEnum("target", SkillTargetType.class);
		_magicType = set.getEnum("magicType", SkillMagicType.class, SkillMagicType.PHYSIC);
		_traitType = set.getEnum("trait", SkillTrait.class, null);
		_saveVs = set.getEnum("saveVs", BaseStats.class, null);
		_dispelOnDamage = set.getBool("dispelOnDamage", false);
		_hideStartMessage = set.getBool("isHideStartMessage", false);
		_hideUseMessage = set.getBool("isHideUseMessage", false);
		_isUndeadOnly = set.getBool("undeadOnly", false);
		_isCorpse = set.getBool("corpse", false);
		_power = set.getDouble("power", 0.);
		_power2 = set.getDouble("power2", 0.);
		_powerPvP = set.getDouble("powerPvP", 0.);
		_powerPvE = set.getDouble("powerPvE", 0.);
		_effectPoint = set.getInteger("effectPoint", 0);
		_nextAction = NextAction.valueOf(set.getString("nextAction", "DEFAULT").toUpperCase());
		_skillType = set.getEnum("skillType", SkillType.class);
		_isSuicideAttack = set.getBool("isSuicideAttack", false);
		_isSkillTimePermanent = set.getBool("isSkillTimePermanent", false);
		_isReuseDelayPermanent = set.getBool("isReuseDelayPermanent", false);
		_deathlink = set.getBool("deathlink", false);
		_basedOnTargetDebuff = set.getBool("basedOnTargetDebuff", false);
		_isNotUsedByAI = set.getBool("isNotUsedByAI", false);
		_isIgnoreResists = set.getBool("isIgnoreResists", false);
		_isIgnoreInvul = set.getBool("isIgnoreInvul", false);
		_isTrigger = set.getBool("isTrigger", false);
		_isNotAffectedByMute = set.getBool("isNotAffectedByMute", false);
		_flyingTransformUsage = set.getBool("flyingTransformUsage", false);
		_flySpeed = set.getInteger("flySpeed", 0);
		_canUseTeleport = set.getBool("canUseTeleport", true);
		if (NumberUtils.isNumber(set.getString("element", "NONE")))
		{
			_element = Element.getElementById(set.getInteger("element", -1));
		}
		else
		{
			_element = Element.getElementByName(set.getString("element", "none").toUpperCase());
		}
		_elementPower = set.getInteger("elementPower", 0);
		_activateRate = set.getInteger("activateRate", -1);
		_levelModifier = set.getInteger("levelModifier", 1);
		_isCancelable = set.getBool("cancelable", true);
		_isReflectable = set.getBool("reflectable", true);
		_isHealDamageSkill = set.getBool("isHealDamageSkill", false);
		_isShieldignore = set.getBool("shieldignore", false);
		_criticalRate = set.getInteger("criticalRate", 0);
		_isOverhit = set.getBool("overHit", false);
		_weaponsAllowed = set.getInteger("weaponsAllowed", 0);
		_minPledgeClass = set.getInteger("minPledgeClass", 0);
		_minRank = set.getInteger("minRank", 0);
		_isOffensive = set.getBool("isOffensive", _skillType.isOffensive());
		_isPvpSkill = set.getBool("isPvpSkill", _skillType.isPvpSkill());
		_isFishingSkill = set.getBool("isFishingSkill", false);
		_isPvm = set.getBool("isPvm", _skillType.isPvM());
		_isForceUse = set.getBool("isForceUse", false);
		_isBehind = set.getBool("behind", false);
		_scopeAngle = set.getInteger("scopeAngle", 120);
		_symbolId = set.getInteger("symbolId", 0);
		_npcId = set.getInteger("npcId", 0);
		_flyType = FlyType.valueOf(set.getString("flyType", "NONE").toUpperCase());
		_flyToBack = set.getBool("flyToBack", false);
		_flyRadius = set.getInteger("flyRadius", 200);
		_negateSkill = set.getInteger("negateSkill", 0);
		_negatePower = set.getInteger("negatePower", Integer.MAX_VALUE);
		_numCharges = set.getInteger("num_charges", 0);
		_maxCharges = set.getInteger("max_charges", 0);
		_condCharges = set.getInteger("cond_charges", 0);
		_delayedEffect = set.getInteger("delayedEffect", 0);
		_cancelTarget = set.getInteger("cancelTarget", 0);
		_skillInterrupt = set.getBool("skillInterrupt", false);
		_lethal1 = set.getDouble("lethal1", 0.);
		_lethal2 = set.getDouble("lethal2", 0.);
		_absorbPart = set.getDouble("absorbPart", 0.);
		_icon = set.getString("icon", "");
		_isMarkDamage = set.getBool("isMarkDamage", false);

		StringTokenizer st = new StringTokenizer(set.getString("addSkills", ""), ";");
		while (st.hasMoreTokens())
		{
			int id = Integer.parseInt(st.nextToken());
			int level = Integer.parseInt(st.nextToken());
			if (level == -1)
			{
				level = _level;
			}
			_addedSkills = ArrayUtils.add(_addedSkills, new AddedSkill(id, level));
		}
		if (_nextAction == NextAction.DEFAULT)
		{
			switch (_skillType)
			{
				case PDAM:
				case CPDAM:
				case LETHAL_SHOT:
				case SPOIL:
				case SOWING:
				case STUN:
				case DRAIN_SOUL:
					_nextAction = NextAction.ATTACK;
					break;
				default:
					_nextAction = NextAction.NONE;
			}
		}
		String canLearn = set.getString("canLearn", null);
		if (canLearn == null)
		{
			_canLearn = null;
		}
		else
		{
			_canLearn = new ArrayList<ClassId>();
			st = new StringTokenizer(canLearn, " \r\n\t,;");
			while (st.hasMoreTokens())
			{
				String cls = st.nextToken();
				_canLearn.add(ClassId.valueOf(cls));
			}
		}
		String teachers = set.getString("teachers", null);
		if (teachers == null)
		{
			_teachers = null;
		}
		else
		{
			_teachers = new ArrayList<Integer>();
			st = new StringTokenizer(teachers, " \r\n\t,;");
			while (st.hasMoreTokens())
			{
				String npcid = st.nextToken();
				_teachers.add(Integer.parseInt(npcid));
			}
		}
		hashCode = (_id * 1023) + _level;
	}
	
	/**
	 * Method getWeaponDependancy.
	 * @param activeChar Creature
	 * @return boolean
	 */
	public final boolean getWeaponDependancy(Creature activeChar)
	{
		if (_weaponsAllowed == 0)
		{
			return true;
		}
		if ((activeChar.getActiveWeaponInstance() != null) && (activeChar.getActiveWeaponItem() != null))
		{
			if ((activeChar.getActiveWeaponItem().getItemType().mask() & _weaponsAllowed) != 0)
			{
				return true;
			}
		}
		if ((activeChar.getSecondaryWeaponInstance() != null) && (activeChar.getSecondaryWeaponItem() != null))
		{
			if ((activeChar.getSecondaryWeaponItem().getItemType().mask() & _weaponsAllowed) != 0)
			{
				return true;
			}
		}
		activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(_displayId, _displayLevel));
		return false;
	}
	

	/**
	 * Method getWeaponModifiedPower.
	 * @param activeChar Creature
	 * @return boolean
	 */
	public double getWeaponModifiedPower(Creature activeChar)
	{
		if ((activeChar.getActiveWeaponInstance() != null) && (activeChar.getActiveWeaponItem() != null))
		{		
			
			for(Iterator <Entry<List<String>,Double>> i = _powerModifiers.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry<List<String>,Double> e = i.next();
				for(String weaponName : e.getKey())
				{
					if(activeChar.getActiveWeaponItem().getItemType().toString().equals(weaponName))
					{
						return e.getValue();
					}
				}
			}
		}
		if ((activeChar.getSecondaryWeaponInstance() != null) && (activeChar.getSecondaryWeaponItem() != null))
		{			
			for(Iterator <Entry<List<String>,Double>> i = _powerModifiers.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry<List<String>,Double> e = i.next();
				for(String weaponName : e.getKey())
				{
					if(activeChar.getActiveWeaponItem().getItemType().toString().equals(weaponName))
					{
						return e.getValue();
					}
				}
			}			
		}
		return 1.;		
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
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = activeChar.getPlayer();
		if (activeChar.isDead())
		{
			return false;
		}
		if ((target != null) && (activeChar.getReflection() != target.getReflection()))
		{
			activeChar.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
			return false;
		}
		if (!getWeaponDependancy(activeChar))
		{
			return false;
		}
		if (activeChar.isUnActiveSkill(_id))
		{
			return false;
		}
		if (first && activeChar.isSkillDisabled(this))
		{
			activeChar.sendReuseMessage(this);
			return false;
		}
		if (first && (activeChar.getCurrentMp() < (isMagic() ? _mpConsume1 + activeChar.calcStat(Stats.MP_MAGIC_SKILL_CONSUME, _mpConsume2, target, this) : _mpConsume1 + activeChar.calcStat(Stats.MP_PHYSICAL_SKILL_CONSUME, _mpConsume2, target, this))))
		{
			activeChar.sendPacket(Msg.NOT_ENOUGH_MP);
			return false;
		}
		if (activeChar.getCurrentHp() < (_hpConsume + 1))
		{
			activeChar.sendPacket(Msg.NOT_ENOUGH_HP);
			return false;
		}
		if (!(_isItemHandler || _isAltUse) && activeChar.isMuted(this))
		{
			return false;
		}
		if (_soulsConsume > activeChar.getConsumedSouls())
		{
			activeChar.sendPacket(Msg.THERE_IS_NOT_ENOUGHT_SOUL);
			return false;
		}
		if ((activeChar.getIncreasedForce() < _condCharges) || (activeChar.getIncreasedForce() < _numCharges))
		{
			activeChar.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
			return false;
		}
		if (player != null)
		{
			if (player.isInFlyingTransform() && _isItemHandler && !flyingTransformUsage())
			{
				player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(getItemConsumeId()[0]));
				return false;
			}
			if (player.isInBoat())
			{
				if (player.getBoat().isAirShip() && !_isItemHandler)
				{
					return false;
				}
				if (player.getBoat().isVehicle() && !((this instanceof FishingSkill) || (this instanceof ReelingPumping)))
				{
					return false;
				}
			}
			if (player.isInObserverMode())
			{
				activeChar.sendPacket(Msg.OBSERVERS_CANNOT_PARTICIPATE);
				return false;
			}
			if (first && (_itemConsume[0] > 0))
			{
				for (int i = 0; i < _itemConsume.length; i++)
				{
					Inventory inv = ((Playable) activeChar).getInventory();
					if (inv == null)
					{
						inv = player.getInventory();
					}
					ItemInstance requiredItems = inv.getItemByItemId(_itemConsumeId[i]);
					if ((requiredItems == null) || (requiredItems.getCount() < _itemConsume[i]))
					{
						if (activeChar == player)
						{
							player.sendPacket(isHandler() ? SystemMsg.INCORRECT_ITEM_COUNT : SystemMsg.THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL);
						}
						return false;
					}
				}
			}
			if (player.isFishing() && !isFishingSkill() && !altUse() && !(activeChar.isServitor() || activeChar.isPet()))
			{
				if (activeChar == player)
				{
					player.sendPacket(Msg.ONLY_FISHING_SKILLS_ARE_AVAILABLE);
				}
				return false;
			}
		}
		if ((getFlyType() != FlyType.NONE) && (getId() != 628) && (getId() != 821) && (activeChar.isImmobilized() || activeChar.isRooted()))
		{
			activeChar.getPlayer().sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return false;
		}
		if (first && (target != null) && (getFlyType() == FlyType.CHARGE) && activeChar.isInRange(target.getLoc(), Math.min(150, getFlyRadius())))
		{
			activeChar.getPlayer().sendPacket(Msg.THERE_IS_NOT_ENOUGH_SPACE_TO_MOVE_THE_SKILL_CANNOT_BE_USED);
			return false;
		}
		SystemMsg msg = checkTarget(activeChar, target, target, forceUse, first);
		if ((msg != null) && (activeChar.getPlayer() != null))
		{
			activeChar.getPlayer().sendPacket(msg);
			return false;
		}
		if (_preCondition.length == 0)
		{
			return true;
		}
		Env env = new Env();
		env.character = activeChar;
		env.skill = this;
		env.target = target;
		if (first)
		{
			for (Condition p : _preCondition)
			{
				if (!p.test(env))
				{
					SystemMsg cond_msg = p.getSystemMsg();
					if (cond_msg != null)
					{
						if (cond_msg.size() > 0)
						{
							activeChar.sendPacket(new SystemMessage2(cond_msg).addSkillName(this));
						}
						else
						{
							activeChar.sendPacket(cond_msg);
						}
					}
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Method checkTarget.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param aimingTarget Creature
	 * @param forceUse boolean
	 * @param first boolean
	 * @return SystemMsg
	 */
	public SystemMsg checkTarget(Creature activeChar, Creature target, Creature aimingTarget, boolean forceUse, boolean first)
	{
		if (((target == activeChar) && isNotTargetAoE()) || (activeChar.isPlayer() && (target == activeChar.getPlayer().getSummonList().getFirstServitor()) && (_targetType == SkillTargetType.TARGET_PET_AURA)))
		{
			return null;
		}
		if ((target == null) || (isOffensive() && (target == activeChar)))
		{
			return SystemMsg.THAT_IS_AN_INCORRECT_TARGET;
		}
		if (activeChar.getReflection() != target.getReflection())
		{
			return SystemMsg.CANNOT_SEE_TARGET;
		}
		if (!first && (target != activeChar) && (target == aimingTarget) && (getCastRange() > 0) && (getCastRange() != 32767) && !activeChar.isInRange(target.getLoc(), getCastRange() + (getCastRange() < 200 ? 400 : 500)))
		{
			return SystemMsg.YOUR_TARGET_IS_OUT_OF_RANGE;
		}
		if ((_skillType == SkillType.TAKECASTLE) || (_skillType == SkillType.TAKEFORTRESS))
		{
			return null;
		}
		if (!first && (target != activeChar) && ((_targetType == SkillTargetType.TARGET_MULTIFACE) || (_targetType == SkillTargetType.TARGET_MULTIFACE_AURA) || (_targetType == SkillTargetType.TARGET_TUNNEL)) && (_isBehind ? PositionUtils.isFacing(activeChar, target, (360 - _scopeAngle)) : !PositionUtils.isFacing(activeChar, target, _scopeAngle)))
		{
			return SystemMsg.YOUR_TARGET_IS_OUT_OF_RANGE;
		}
		if (((target.isDead() != _isCorpse) && (_targetType != SkillTargetType.TARGET_AREA_AIM_CORPSE)) || (_isUndeadOnly && !target.isUndead()))
		{
			return SystemMsg.INVALID_TARGET;
		}
		if(target.isNpc() && (!_skillHealStance && _isHealDamageSkill))
		{
			return null;
		}
		if(!target.isPlayer() && (_skillHealStance && _isHealDamageSkill))
		{
			return SystemMsg.INVALID_TARGET;
		}
		if (_isAltUse || (_targetType == SkillTargetType.TARGET_FEEDABLE_BEAST) || (_targetType == SkillTargetType.TARGET_UNLOCKABLE) || (_targetType == SkillTargetType.TARGET_CHEST))
		{
			return null;
		}
		Player player = activeChar.getPlayer();
		if (player != null)
		{
			Player pcTarget = target.getPlayer();
			if (pcTarget != null)
			{
				if (isPvM())
				{
					return SystemMsg.THAT_IS_AN_INCORRECT_TARGET;
				}
				if (player.isInZone(ZoneType.epic) != pcTarget.isInZone(ZoneType.epic))
				{
					return SystemMsg.THAT_IS_AN_INCORRECT_TARGET;
				}
				if (pcTarget.isInOlympiadMode() && (!player.isInOlympiadMode() || (player.getOlympiadGame() != pcTarget.getOlympiadGame())))
				{
					return SystemMsg.THAT_IS_AN_INCORRECT_TARGET;
				}
				if ((player.getBlockCheckerArena() > -1) && (pcTarget.getBlockCheckerArena() > -1) && (_targetType == SkillTargetType.TARGET_EVENT))
				{
					return null;
				}
				if (isOffensive() ||(!_skillHealStance && _isHealDamageSkill))
				{
					if (player.isInOlympiadMode() && !player.isOlympiadCompStart())
					{
						return SystemMsg.INVALID_TARGET;
					}
					if (player.isInOlympiadMode() && player.isOlympiadCompStart() && (player.getOlympiadSide() == pcTarget.getOlympiadSide()) && !forceUse)
					{
						return SystemMsg.THAT_IS_AN_INCORRECT_TARGET;
					}
					if (isAoE() && (getCastRange() < Integer.MAX_VALUE) && !GeoEngine.canSeeTarget(activeChar, target, activeChar.isFlying()))
					{
						return SystemMsg.CANNOT_SEE_TARGET;
					}
					if ((activeChar.isInZoneBattle() != target.isInZoneBattle()) && !player.getPlayerAccess().PeaceAttack)
					{
						return SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE;
					}
					if ((activeChar.isInZonePeace() || target.isInZonePeace()) && !player.getPlayerAccess().PeaceAttack)
					{
						return SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE;
					}
					if (activeChar.isInZoneBattle())
					{
						if (!forceUse && !isForceUse() && (player.getParty() != null) && (player.getParty() == pcTarget.getParty()))
						{
							return SystemMsg.INVALID_TARGET;
						}
						return null;
					}
					SystemMsg msg = null;
					for (GlobalEvent e : player.getEvents())
					{
						if ((msg = e.checkForAttack(target, activeChar, this, forceUse)) != null)
						{
							return msg;
						}
					}
					for (GlobalEvent e : player.getEvents())
					{
						if (e.canAttack(target, activeChar, this, forceUse))
						{
							return null;
						}
					}
					if (isProvoke())
					{
						if (!forceUse && (player.getParty() != null) && (player.getParty() == pcTarget.getParty()))
						{
							return SystemMsg.INVALID_TARGET;
						}
						return null;
					}
					if (isPvpSkill() || !forceUse || isAoE())
					{
						if (player == pcTarget)
						{
							return SystemMsg.INVALID_TARGET;
						}
						if ((player.getParty() != null) && (player.getParty() == pcTarget.getParty()))
						{
							return SystemMsg.INVALID_TARGET;
						}
						if ((player.getClanId() != 0) && (player.getClanId() == pcTarget.getClanId()))
						{
							return SystemMsg.INVALID_TARGET;
						}
					}
					if (activeChar.isInZone(ZoneType.SIEGE) && target.isInZone(ZoneType.SIEGE))
					{
						return null;
					}
					if (player.atMutualWarWith(pcTarget))
					{
						return null;
					}
					if (isForceUse())
					{
						return null;
					}
					if (pcTarget.getPvpFlag() != 0)
					{
						return null;
					}
					if (pcTarget.isChaotic())
					{
						return null;
					}
					if (forceUse && !isPvpSkill() && (!isAoE() || (aimingTarget == target)))
					{
						return null;
					}
					return SystemMsg.INVALID_TARGET;
				}
				if ((_targetType == SkillTargetType.TARGET_MENTEE) && (pcTarget.getMenteeMentorList().getMentor() != player.getObjectId()))
				{
					return SystemMsg.INVALID_TARGET;
				}
				if ((player.getParty() == null) && (_skillHealStance && _isHealDamageSkill))
				{
					return SystemMsg.INVALID_TARGET;
				}
				if ((pcTarget == player) && (_skillHealStance && _isHealDamageSkill) && ((player.getParty() != null) && (player.getParty() != pcTarget.getParty())))
				{
					return SystemMsg.INVALID_TARGET;
				}
				if ((pcTarget == player) && (_skillHealStance && _isHealDamageSkill) && ((player.getParty() != null) && (player.getParty() == pcTarget.getParty())))
				{
					return null;
				}
				if (pcTarget == player)
				{
					return null;
				}
				if (player.isInOlympiadMode() && !forceUse && (player.getOlympiadSide() != pcTarget.getOlympiadSide()))
				{
					return SystemMsg.THAT_IS_AN_INCORRECT_TARGET;
				}
				if (!activeChar.isInZoneBattle() && target.isInZoneBattle())
				{
					return SystemMsg.INVALID_TARGET;
				}
				if (forceUse || isForceUse())
				{
					return null;
				}
				if ((player.getParty() != null) && (player.getParty() == pcTarget.getParty()))
				{
					return null;
				}
				if ((player.getClanId() != 0) && (player.getClanId() == pcTarget.getClanId()))
				{
					return null;
				}
				if (player.atMutualWarWith(pcTarget))
				{
					return SystemMsg.INVALID_TARGET;
				}
				if (pcTarget.getPvpFlag() != 0)
				{
					return SystemMsg.INVALID_TARGET;
				}
				if (pcTarget.isChaotic())
				{
					return SystemMsg.INVALID_TARGET;
				}
				return null;
			}
		}
		if (isAoE() && isOffensive() && (!_skillHealStance && _isHealDamageSkill) && (getCastRange() < Integer.MAX_VALUE) && !GeoEngine.canSeeTarget(activeChar, target, activeChar.isFlying()))
		{
			return SystemMsg.CANNOT_SEE_TARGET;
		}
		if (!forceUse && !isForceUse() && !isOffensive() && target.isAutoAttackable(activeChar))
		{
			return SystemMsg.INVALID_TARGET;
		}
		if (!forceUse && !isForceUse() && isOffensive() && !target.isAutoAttackable(activeChar))
		{
			return SystemMsg.INVALID_TARGET;
		}
		if (!target.isAttackable(activeChar))
		{
			return SystemMsg.INVALID_TARGET;
		}
		return null;
	}
	
	/**
	 * Method getAimingTarget.
	 * @param activeChar Creature
	 * @param obj GameObject
	 * @return Creature
	 */
	public final Creature getAimingTarget(Creature activeChar, GameObject obj)
	{
		Creature target = (obj == null) || !obj.isCreature() ? null : (Creature) obj;
		switch (_targetType)
		{
			case TARGET_SUMMON_AURA:
			case TARGET_SUMMON_AURA_AND_ME:
			case TARGET_ALLY:
			case TARGET_CLAN:
			case TARGET_PARTY:
			case TARGET_PARTY_WITHOUT_ME:
			case TARGET_CLAN_ONLY:
			case TARGET_SELF:
				return activeChar;
			case TARGET_AURA:
			case TARGET_GROUND:
			case TARGET_COMMCHANNEL:
			case TARGET_MULTIFACE_AURA:
				return activeChar;
			case TARGET_HOLY:
				return (target != null) && activeChar.isPlayer() && target.isArtefact() ? target : null;
			case TARGET_FLAGPOLE:
				return activeChar;
			case TARGET_UNLOCKABLE:
				return ((target != null) && target.isDoor()) || (target instanceof ChestInstance) ? target : null;
			case TARGET_CHEST:
				return target instanceof ChestInstance ? target : null;
			case TARGET_FEEDABLE_BEAST:
				return target instanceof FeedableBeastInstance ? target : null;
			case TARGET_PET:
				return (target != null) && (target.isPet()) && (target.isDead() == _isCorpse) ? target : null;
			case TARGET_PET_AURA:
				target = activeChar.getPlayer().getSummonList().getFirstServitor();
				return (target != null) && (target.isDead() == _isCorpse) ? target : null;
			case TARGET_SUMMON:
				return (target != null) && (target.isServitor()) && (target.isDead() == _isCorpse) ? target : null;
			case TARGET_OWNER:
				if (activeChar.isServitor() || activeChar.isPet())
				{
					target = activeChar.getPlayer();
				}
				else
				{
					return null;
				}
				return (target != null) && (target.isDead() == _isCorpse) ? target : null;
			case TARGET_ENEMY_PET:
				if ((target == null) || (target == activeChar.getPlayer().getSummonList().getPet()) || !target.isPet())
				{
					return null;
				}
				return target;
			case TARGET_ENEMY_SUMMON:
				if ((target == null) || (target == activeChar.getPlayer().getSummonList().getPet()) || !target.isServitor())
				{
					return null;
				}
				return target;
			case TARGET_ENEMY_SERVITOR:
				if ((target == null) || (target == activeChar.getPlayer().getSummonList().getPet()) || !(target instanceof Summon))
				{
					return null;
				}
				return target;
			case TARGET_EVENT:
				return (target != null) && !target.isDead() && (target.getPlayer().getBlockCheckerArena() > -1) ? target : null;
			case TARGET_MENTEE:
			case TARGET_ONE:
				return (target != null) && (target.isDead() == _isCorpse) && !((target == activeChar) && isOffensive()) && (!_isUndeadOnly || target.isUndead()) ? target : null;
			case TARGET_ONE_PLAYER:
				return (target != null) && target.isPlayer() && (target.isDead() == _isCorpse) && !((target == activeChar) && isOffensive()) && (!_isUndeadOnly || target.isUndead()) ? target : null;
			case TARGET_PARTY_ONE:
				if (target == null)
				{
					return null;
				}
				Player player = activeChar.getPlayer();
				Player ptarget = target.getPlayer();
				if ((ptarget != null) && (ptarget == activeChar))
				{
					return target;
				}
				if ((player != null) && player.isInOlympiadMode() && (ptarget != null) && (player.getOlympiadSide() == ptarget.getOlympiadSide()) && (player.getOlympiadGame() == ptarget.getOlympiadGame()) && (target.isDead() == _isCorpse) && !((target == activeChar) && isOffensive()) && (!_isUndeadOnly || target.isUndead()))
				{
					return target;
				}
				if ((ptarget != null) && (player != null) && (player.getParty() != null) && player.getParty().containsMember(ptarget) && (target.isDead() == _isCorpse) && !((target == activeChar) && isOffensive()) && (!_isUndeadOnly || target.isUndead()))
				{
					return target;
				}
				return null;
			case TARGET_AREA:
			case TARGET_MULTIFACE:
			case TARGET_TUNNEL:
				return (target != null) && (target.isDead() == _isCorpse) && !((target == activeChar) && isOffensive()) && (!_isUndeadOnly || target.isUndead()) ? target : null;
			case TARGET_AREA_AIM_CORPSE:
				return (target != null) && target.isDead() ? target : null;
			case TARGET_CORPSE:
				if ((target == null) || !target.isDead())
				{
					return null;
				}
				if (target.isServitor() && (target != activeChar.getPlayer().getSummonList().getPet()))
				{
					return target;
				}
				return target.isNpc() ? target : null;
			case TARGET_CORPSE_PLAYER:
				return (target != null) && target.isPlayable() && target.isDead() ? target : null;
			case TARGET_SIEGE:
				return (target != null) && !target.isDead() && target.isDoor() ? target : null;
			default:
				activeChar.sendMessage("Target type of skill is not currently handled");
				return null;
		}
	}
	
	/**
	 * Method getTargets.
	 * @param activeChar Creature
	 * @param aimingTarget Creature
	 * @param forceUse boolean
	 * @return List<Creature>
	 */
	public List<Creature> getTargets(Creature activeChar, Creature aimingTarget, boolean forceUse)
	{
		List<Creature> targets;
		if (oneTarget())
		{
			targets = new LazyArrayList<Creature>(1);
			targets.add(aimingTarget);
			return targets;
		}
		targets = new LazyArrayList<Creature>();
		switch (_targetType)
		{
			case TARGET_EVENT:
			{
				if (activeChar.isPlayer())
				{
					Player player = activeChar.getPlayer();
					int playerArena = player.getBlockCheckerArena();
					if (playerArena != -1)
					{
						ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(playerArena);
						int team = holder.getPlayerTeam(player);
						for (Player actor : World.getAroundPlayers(activeChar, 250, 100))
						{
							if (holder.getAllPlayers().contains(actor) && (holder.getPlayerTeam(actor) != team))
							{
								targets.add(actor);
							}
						}
					}
				}
				break;
			}
			case TARGET_AREA_AIM_CORPSE:
			case TARGET_AREA:
			case TARGET_MULTIFACE:
			case TARGET_TUNNEL:
			{
				if ((aimingTarget.isDead() == _isCorpse) && (!_isUndeadOnly || aimingTarget.isUndead()))
				{
					targets.add(aimingTarget);
				}
				if (_targetType == SkillTargetType.TARGET_MULTIFACE)
				{
					addTargetsToList(targets, activeChar, activeChar, forceUse);
				}
				else
				{
					addTargetsToList(targets, aimingTarget, activeChar, forceUse);
				}
				break;
			}
			case TARGET_AURA:
			case TARGET_MULTIFACE_AURA:
			{
				addTargetsToList(targets, activeChar, activeChar, forceUse);
				break;
			}
			case TARGET_COMMCHANNEL:
			{
				if (activeChar.getPlayer() != null)
				{
					if (activeChar.getPlayer().isInParty())
					{
						if (activeChar.getPlayer().getParty().isInCommandChannel())
						{
							for (Player p : activeChar.getPlayer().getParty().getCommandChannel())
							{
								if (!p.isDead() && p.isInRange(activeChar, _skillRadius == 0 ? 600 : _skillRadius))
								{
									targets.add(p);
								}
							}
							addTargetAndPetToList(targets, activeChar.getPlayer(), activeChar.getPlayer());
							break;
						}
						for (Player p : activeChar.getPlayer().getParty().getPartyMembers())
						{
							if (!p.isDead() && p.isInRange(activeChar, _skillRadius == 0 ? 600 : _skillRadius))
							{
								targets.add(p);
							}
						}
						addTargetAndPetToList(targets, activeChar.getPlayer(), activeChar.getPlayer());
						break;
					}
					targets.add(activeChar);
					addTargetAndPetToList(targets, activeChar.getPlayer(), activeChar.getPlayer());
				}
				break;
			}
			case TARGET_PET_AURA:
				if (activeChar.getPlayer().getSummonList().getFirstServitor() == null)
				{
					break;
				}
				addTargetsToList(targets, activeChar.getPlayer().getSummonList().getFirstServitor(), activeChar, forceUse);
				break;
			case TARGET_SUMMON_AURA:
			case TARGET_SUMMON_AURA_AND_ME:
				List<Summon> servitors = activeChar.getPlayer().getSummonList().getServitors();
				for (Summon summon : servitors)
				{
					targets.add(summon);
				}
				switch (_targetType)
				{
					case TARGET_SUMMON_AURA_AND_ME:
						targets.add(activeChar);
						break;
					default:
						break;
				}
				break;
			case TARGET_PARTY:
			case TARGET_PARTY_WITHOUT_ME:
			case TARGET_CLAN:
			case TARGET_CLAN_ONLY:
			case TARGET_ALLY:
			{
				if (activeChar.isMonster() || activeChar.isSiegeGuard())
				{
					targets.add(activeChar);
					for (Creature c : World.getAroundCharacters(activeChar, _skillRadius, 600))
					{
						if (!c.isDead() && (c.isMonster() || c.isSiegeGuard()))
						{
							targets.add(c);
						}
					}
					break;
				}
				Player player = activeChar.getPlayer();
				if (player == null)
				{
					break;
				}
				for (Player target : World.getAroundPlayers(player, _skillRadius, 600))
				{
					boolean check = false;
					switch (_targetType)
					{
						case TARGET_PARTY:
							check = (player.getParty() != null) && (player.getParty() == target.getParty());
							break;
						case TARGET_PARTY_WITHOUT_ME:
							check = (player.getParty() != null) && (player.getParty() == target.getParty()) && (player != target);
							break;
						case TARGET_CLAN:
							check = ((player.getClanId() != 0) && (target.getClanId() == player.getClanId())) || ((player.getParty() != null) && (target.getParty() == player.getParty()));
							break;
						case TARGET_CLAN_ONLY:
							check = (player.getClanId() != 0) && (target.getClanId() == player.getClanId());
							break;
						case TARGET_ALLY:
							check = ((player.getClanId() != 0) && (target.getClanId() == player.getClanId())) || ((player.getAllyId() != 0) && (target.getAllyId() == player.getAllyId()));
							break;
						default:
							break;
					}
					if (!check)
					{
						continue;
					}
					if (player.isInOlympiadMode() && target.isInOlympiadMode() && (player.getOlympiadSide() != target.getOlympiadSide()))
					{
						continue;
					}
					if (checkTarget(player, target, aimingTarget, forceUse, false) != null)
					{
						continue;
					}
					addTargetAndPetToList(targets, player, target);
				}
				if (_targetType != SkillTargetType.TARGET_PARTY_WITHOUT_ME)
				{
					addTargetAndPetToList(targets, player, player);
				}
				break;
			}
			case TARGET_GROUND:
			{
				Player player = activeChar.getPlayer();
				if (player == null)
				{
					break;
				}
				Location loc = player.getGroundSkillLoc();
				addTargetsToList(targets, loc, activeChar, forceUse);
				break;
			}
			default:
				break;
		}
		return targets;
	}
	
	/**
	 * Method addTargetAndPetToList.
	 * @param targets List<Creature>
	 * @param actor Player
	 * @param target Player
	 */
	private void addTargetAndPetToList(List<Creature> targets, Player actor, Player target)
	{
		if (((actor == target) || actor.isInRange(target, _skillRadius)) && (target.isDead() == _isCorpse))
		{
			targets.add(target);
		}
		for (Summon pet : target.getSummonList())
		{
			if ((pet != null) && actor.isInRange(pet, _skillRadius) && (pet.isDead() == _isCorpse))
			{
				targets.add(pet);
			}
		}
	}
	
	/**
	 * Method addTargetsToList.
	 * @param targets List<Creature>
	 * @param aimingTarget Creature
	 * @param activeChar Creature
	 * @param forceUse boolean
	 */
	private void addTargetsToList(List<Creature> targets, Creature aimingTarget, Creature activeChar, boolean forceUse)
	{
		int count = 0;
		Polygon terr = null;
		if (_targetType == SkillTargetType.TARGET_TUNNEL)
		{
			int radius = 100;
			int zmin1 = activeChar.getZ() - 200;
			int zmax1 = activeChar.getZ() + 200;
			int zmin2 = aimingTarget.getZ() - 200;
			int zmax2 = aimingTarget.getZ() + 200;
			double angle = PositionUtils.convertHeadingToDegree(activeChar.getHeading());
			double radian1 = Math.toRadians(angle - 90);
			double radian2 = Math.toRadians(angle + 90);
			terr = new Polygon().add(activeChar.getX() + (int) (Math.cos(radian1) * radius), activeChar.getY() + (int) (Math.sin(radian1) * radius)).add(activeChar.getX() + (int) (Math.cos(radian2) * radius), activeChar.getY() + (int) (Math.sin(radian2) * radius)).add(aimingTarget.getX() + (int) (Math.cos(radian2) * radius), aimingTarget.getY() + (int) (Math.sin(radian2) * radius)).add(aimingTarget.getX() + (int) (Math.cos(radian1) * radius), aimingTarget.getY() + (int) (Math.sin(radian1) * radius)).setZmin(Math.min(zmin1, zmin2)).setZmax(Math.max(zmax1, zmax2));
		}
		for (Creature target : aimingTarget.getAroundCharacters(_skillRadius, 300))
		{
			if ((terr != null) && !terr.isInside(target.getX(), target.getY(), target.getZ()))
			{
				continue;
			}
			if ((target == null) || (activeChar == target) || ((activeChar.getPlayer() != null) && (activeChar.getPlayer() == target.getPlayer())))
			{
				continue;
			}
			if (getId() == SKILL_DETECTION)
			{
				target.checkAndRemoveInvisible();
			}
			if (checkTarget(activeChar, target, aimingTarget, forceUse, false) != null)
			{
				continue;
			}
			if (!(activeChar instanceof DecoyInstance) && activeChar.isNpc() && target.isNpc())
			{
				continue;
			}
			targets.add(target);
			count++;
			if (isOffensive() && (count >= 20) && !activeChar.isRaid())
			{
				break;
			}
		}
	}
	
	/**
	 * Method addTargetsToLakcisDamage.
	 * @param targets List<Creature>
	 * @param aimingTarget Creature
	 * @param activeChar Creature
	 * @param forceUse boolean
	 */
	public void addTargetsToLakcis(List<Creature> targets, Creature activeChar, boolean isHealTask)
	{		
		_skillHealStance = isHealTask;
		int count = 0;
		Polygon terr = null;
		for (Creature target : activeChar.getAroundCharacters(_skillRadius, 300))
		{
			if ((terr != null) && !terr.isInside(target.getX(), target.getY(), target.getZ()))
			{
				continue;
			}
			if ((target == null) || (activeChar == target) || ((activeChar.getPlayer() != null) && (activeChar.getPlayer() == target.getPlayer())))
			{
				continue;
			}
			if (checkTarget(activeChar, target, activeChar, false, false) != null)
			{
				continue;
			}
			if (!(activeChar instanceof DecoyInstance) && activeChar.isNpc() && target.isNpc())
			{
				continue;
			}
			targets.add(target);
			count++;
			if (isOffensive() && (count >= 20) && !activeChar.isRaid())
			{
				break;
			}
		}
	}
	/**
	 * Method addTargetsToList.
	 * @param targets List<Creature>
	 * @param loc Location
	 * @param activeChar Creature
	 * @param forceUse boolean
	 */
	private void addTargetsToList(List<Creature> targets, Location loc, Creature activeChar, boolean forceUse)
	{
		int count = 0;
		for (Creature target : activeChar.getAroundCharacters(1600, 300))
		{
			if ((target == null) || (activeChar == target) || ((activeChar.getPlayer() != null) && (activeChar.getPlayer() == target.getPlayer())))
			{
				continue;
			}
			if (target.getDistance(loc) < getSkillRadius())
			{
				if (checkTarget(activeChar, target, target, forceUse, false) != null)
				{
					continue;
				}
				if (!(activeChar instanceof DecoyInstance) && activeChar.isNpc() && target.isNpc())
				{
					continue;
				}
				targets.add(target);
			}
			count++;
			if (isOffensive() && (count >= 20) && !activeChar.isRaid())
			{
				break;
			}
		}
	}

	/**
	 * Method getEffects.
	 * @param effector Creature
	 * @param effected Creature
	 * @param calcChance boolean
	 * @param applyOnCaster boolean
	 */
	public final void getEffects(Creature effector, Creature effected, boolean calcChance, boolean applyOnCaster)
	{
		getEffects(effector, effected, calcChance, applyOnCaster, false);
	}
	
	/**
	 * Method getEffects.
	 * @param effector Creature
	 * @param effected Creature
	 * @param calcChance boolean
	 * @param applyOnCaster boolean
	 * @param skillReflected boolean
	 */
	public final void getEffects(Creature effector, Creature effected, boolean calcChance, boolean applyOnCaster, boolean skillReflected)
	{
		double timeMult = 1.0;
		if (isMusic())
		{
			timeMult = Config.SONGDANCETIME_MODIFIER;
		}
		else if ((getId() >= 4342) && (getId() <= 4360))
		{
			timeMult = Config.CLANHALL_BUFFTIME_MODIFIER;
		}
		getEffects(effector, effected, calcChance, applyOnCaster, 0, timeMult, skillReflected);
	}
	
	/**
	 * Method getEffects.
	 * @param effector Creature
	 * @param effected Creature
	 * @param calcChance boolean
	 * @param applyOnCaster boolean
	 * @param timeConst long
	 * @param timeMult double
	 * @param skillReflected boolean
	 */
	public final void getEffects(final Creature effector, final Creature effected, final boolean calcChance, final boolean applyOnCaster, final long timeConst, final double timeMult, final boolean skillReflected)
	{
		if (isPassive() || !hasEffects() || (effector == null) || (effected == null))
		{
			return;
		}
		if ((effected.isEffectImmune() || (effected.isInvul() && isOffensive())) && (effector != effected))
		{
			if (effector.isPlayer())
			{
				effector.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addName(effected).addSkillName(_displayId, _displayLevel));
			}
			return;
		}
		if (effected.isDoor() || (effected.isAlikeDead() && !isPreservedOnDeath()))
		{
			return;
		}
		ThreadPoolManager.getInstance().execute(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				boolean success = false;
				boolean skillMastery = false;
				int sps = effector.getChargedSpiritShot();
				if (effector.getSkillMastery(getId()) == 2)
				{
					skillMastery = true;
					effector.removeSkillMastery(getId());
				}
				for (EffectTemplate et : getEffectTemplates())
				{
					if ((applyOnCaster != et._applyOnCaster) || (et._count == 0))
					{
						continue;
					}
					Creature character = et._applyOnCaster || (et._isReflectable && skillReflected) ? effector : effected;
					List<Creature> targets = new LazyArrayList<Creature>(1);
					targets.add(character);
					if (et._applyOnSummon && character.isPlayer())
					{
						for (Summon summon : character.getPlayer().getSummonList())
						{
							if ((summon != null) && summon.isServitor() && !isOffensive() && !isToggle() && !isCubicSkill())
							{
								targets.add(summon);
							}
						}
					}
					loop:
					for (Creature target : targets)
					{
						if (target.isAlikeDead() && !isPreservedOnDeath())
						{
							continue;
						}
						if (target.isRaid() && et.getEffectType().isRaidImmune())
						{
							continue;
						}
						if (((effected.isBuffImmune() && !isOffensive()) || (effected.isDebuffImmune() && isOffensive())) && (et.getPeriod() > 0) && (effector != effected))
						{
							continue;
						}
						if (isBlockedByChar(target, et))
						{
							continue;
						}
						if (et._stackOrder == -1)
						{
							if (!et._stackTypes.contains(EffectTemplate.NO_STACK))
							{
								for (Effect e : target.getEffectList().getAllEffects())
								{
									for (String arg : et._stackTypes)
									{
										if (e.getStackType().contains(arg))
										{
											continue loop;
										}
									}
								}
							}
							else if (target.getEffectList().getEffectsBySkillId(getId()) != null)
							{
								continue;
							}
						}
						Env env = new Env(effector, target, Skill.this);
						int chance = et.chance(getActivateRate());
						if ((calcChance || (chance >= 0)) && !et._applyOnCaster)
						{
							env.value = chance;
							if (!Formulas.calcSkillSuccess(env, et, sps))
							{
								continue;
							}
						}
						if (_isReflectable && et._isReflectable && isOffensive() && (target != effector) && !effector.isTrap())
						{
							if (Rnd.chance(target.calcStat(isMagic() ? Stats.REFLECT_MAGIC_DEBUFF : Stats.REFLECT_PHYSIC_DEBUFF, 0, effector, Skill.this)))
							{
								target.sendPacket(new SystemMessage(SystemMessage.YOU_COUNTERED_C1S_ATTACK).addName(effector));
								effector.sendPacket(new SystemMessage(SystemMessage.C1_DODGES_THE_ATTACK).addName(target));
								target = effector;
								env.target = target;
							}
						}
						if (success)
						{
							env.value = Integer.MAX_VALUE;
						}
						final Effect e = et.getEffect(env);
						if (e != null)
						{
							if (chance > 0)
							{
								success = true;
							}
							if (e.isOneTime())
							{
								if (e.checkCondition())
								{
									e.onStart();
									e.onActionTime();
									e.onExit();
								}
							}
							else
							{
								int count = et.getCount();
								long period = et.getPeriod();
								if (skillMastery)
								{
									if (count > 1)
									{
										count *= 2;
									}
									else
									{
										period *= 2;
									}
								}
								if (!et._applyOnCaster && isOffensive() && !isIgnoreResists() && !effector.isRaid())
								{
									double res = 0;
									if (et.getEffectType().getResistType() != null)
									{
										res += effected.calcStat(et.getEffectType().getResistType(), effector, Skill.this);
									}
									if (et.getEffectType().getAttributeType() != null)
									{
										res -= effector.calcStat(et.getEffectType().getAttributeType(), effected, Skill.this);
									}
									res += effected.calcStat(Stats.DEBUFF_RESIST, effector, Skill.this);
									if (res != 0)
									{
										double mod = 1 + Math.abs(0.01 * res);
										if (res > 0)
										{
											mod = 1. / mod;
										}
										if (count > 1)
										{
											count = (int) Math.floor(Math.max(count * mod, 1));
										}
										else
										{
											period = (long) Math.floor(Math.max(period * mod, 1));
										}
									}
								}
								if (timeConst > 0L)
								{
									if (count > 1)
									{
										period = timeConst / count;
									}
									else
									{
										period = timeConst;
									}
								}
								else if (timeMult > 1.0)
								{
									if (count > 1)
									{
										count *= timeMult;
									}
									else
									{
										period *= timeMult;
									}
								}
								e.setCount(count);
								e.setPeriod(period);
								e.schedule();
							}
						}
					}
				}
				if (calcChance)
				{
					if (success)
					{
						effector.sendPacket(new SystemMessage(SystemMessage.S1_HAS_SUCCEEDED).addSkillName(_displayId, _displayLevel));
					}
					else
					{
						effector.sendPacket(new SystemMessage(SystemMessage.S1_HAS_FAILED).addSkillName(_displayId, _displayLevel));
					}
				}
			}
		});
	}
	
	/**
	 * Method attach.
	 * @param effect EffectTemplate
	 */
	public final void attach(EffectTemplate effect)
	{
		_effectTemplates = ArrayUtils.add(_effectTemplates, effect);
	}
	
	/**
	 * Method getEffectTemplates.
	 * @return EffectTemplate[]
	 */
	public EffectTemplate[] getEffectTemplates()
	{
		return _effectTemplates;
	}
	
	/**
	 * Method hasEffects.
	 * @return boolean
	 */
	public boolean hasEffects()
	{
		return _effectTemplates.length > 0;
	}
	
	/**
	 * Method getStatFuncs.
	 * @return Func[]
	 */
	public final Func[] getStatFuncs()
	{
		return getStatFuncs(this);
	}
	
	/**
	 * Method equals.
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		return hashCode() == ((Skill) obj).hashCode();
	}
	
	/**
	 * Method hashCode.
	 * @return int
	 */
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	
	/**
	 * Method attach.
	 * @param c Condition
	 */
	public final void attach(Condition c)
	{
		_preCondition = ArrayUtils.add(_preCondition, c);
	}
	
	/**
	 * Method altUse.
	 * @return boolean
	 */
	public final boolean altUse()
	{
		return _isAltUse;
	}
	
	/**
	 * Method canTeachBy.
	 * @param npcId int
	 * @return boolean
	 */
	public final boolean canTeachBy(int npcId)
	{
		return (_teachers == null) || _teachers.contains(npcId);
	}
	
	/**
	 * Method getActivateRate.
	 * @return int
	 */
	public final int getActivateRate()
	{
		return _activateRate;
	}
	
	/**
	 * Method getAddedSkills.
	 * @return AddedSkill[]
	 */
	public AddedSkill[] getAddedSkills()
	{
		return _addedSkills;
	}
	
	/**
	 * Method getCanLearn.
	 * @param cls ClassId
	 * @return boolean
	 */
	public final boolean getCanLearn(ClassId cls)
	{
		return (_canLearn == null) || _canLearn.contains(cls);
	}
	
	/**
	 * Method getCastRange.
	 * @return int
	 */
	public final int getCastRange()
	{
		return _castRange;
	}
	
	/**
	 * Method getAOECastRange.
	 * @return int
	 */
	public final int getAOECastRange()
	{
		return Math.max(_castRange, _skillRadius);
	}
	
	/**
	 * Method getCondCharges.
	 * @return int
	 */
	public int getCondCharges()
	{
		return _condCharges;
	}
	
	/**
	 * Method getCoolTime.
	 * @return int
	 */
	public final int getCoolTime()
	{
		return _coolTime;
	}
	
	/**
	 * Method setCoolTime.
	 * @param _time int
	 */
	public final void setCoolTime(int _time)
	{
		_coolTime = _time;
	}
	
	/**
	 * Method getCorpse.
	 * @return boolean
	 */
	public boolean getCorpse()
	{
		return _isCorpse;
	}
	
	/**
	 * Method getDelayedEffect.
	 * @return int
	 */
	public int getDelayedEffect()
	{
		return _delayedEffect;
	}
	
	/**
	 * Method getSkillToCast.
	 * @return int
	 */
	public final int getSkillToCast()
	{
		return _skillToCast;
	}
	
	/**
	 * Method getSkillToCastLevel.
	 * @return int
	 */
	public final int getSkillToCastLevel()
	{
		return _skillToCastLevel;
	}
	
	/**
	 * Method getDisplayId.
	 * @return int
	 */
	public final int getDisplayId()
	{
		return _displayId;
	}
	
	/**
	 * Method getDisplayLevel.
	 * @return int
	 */
	public int getDisplayLevel()
	{
		return _displayLevel;
	}
	
	/**
	 * Method getEffectPoint.
	 * @return int
	 */
	public int getEffectPoint()
	{
		return _effectPoint;
	}
	
	/**
	 * Method getSameByStackType.
	 * @param list List<Effect>
	 * @return Effect
	 */
	public Effect getSameByStackType(List<Effect> list)
	{
		Effect ret;
		for (EffectTemplate et : getEffectTemplates())
		{
			if ((et != null) && ((ret = et.getSameByStackType(list)) != null))
			{
				return ret;
			}
		}
		return null;
	}
	
	/**
	 * Method getSameByStackType.
	 * @param list EffectList
	 * @return Effect
	 */
	public Effect getSameByStackType(EffectList list)
	{
		return getSameByStackType(list.getAllEffects());
	}
	
	/**
	 * Method getSameByStackType.
	 * @param actor Creature
	 * @return Effect
	 */
	public Effect getSameByStackType(Creature actor)
	{
		return getSameByStackType(actor.getEffectList().getAllEffects());
	}
	
	/**
	 * Method getElement.
	 * @return Element
	 */
	public final Element getElement()
	{
		return _element;
	}
	
	/**
	 * Method getElementPower.
	 * @return int
	 */
	public final int getElementPower()
	{
		return _elementPower;
	}
	
	/**
	 * Method getFirstAddedSkill.
	 * @return Skill
	 */
	public Skill getFirstAddedSkill()
	{
		if (_addedSkills.length == 0)
		{
			return null;
		}
		return _addedSkills[0].getSkill();
	}
	
	/**
	 * Method getFlyRadius.
	 * @return int
	 */
	public int getFlyRadius()
	{
		return _flyRadius;
	}
	
	/**
	 * Method getFlyType.
	 * @return FlyType
	 */
	public FlyType getFlyType()
	{
		return _flyType;
	}
	
	/**
	 * Method isFlyToBack.
	 * @return boolean
	 */
	public boolean isFlyToBack()
	{
		return _flyToBack;
	}
	
	/**
	 * Method getHitTime.
	 * @return int
	 */
	public final int getHitTime()
	{
		return _hitTime;
	}
	
	/**
	 * Method getHpConsume.
	 * @return int
	 */
	public final int getHpConsume()
	{
		return _hpConsume;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method setId.
	 * @param id int
	 */
	public void setId(int id)
	{
		_id = id;
	}
	
	/**
	 * Method getItemConsume.
	 * @return int[]
	 */
	public final int[] getItemConsume()
	{
		return _itemConsume;
	}
	
	/**
	 * Method getItemConsumeId.
	 * @return int[]
	 */
	public final int[] getItemConsumeId()
	{
		return _itemConsumeId;
	}
	
	/**
	 * Method getReferenceItemId.
	 * @return int
	 */
	public final int getReferenceItemId()
	{
		return _referenceItemId;
	}
	
	/**
	 * Method getReferenceItemMpConsume.
	 * @return int
	 */
	public final int getReferenceItemMpConsume()
	{
		return _referenceItemMpConsume;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public final int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method getBaseLevel.
	 * @return int
	 */
	public final int getBaseLevel()
	{
		return _baseLevel;
	}
	
	/**
	 * Method setBaseLevel.
	 * @param baseLevel int
	 */
	public final void setBaseLevel(int baseLevel)
	{
		_baseLevel = baseLevel;
	}
	
	/**
	 * Method getLevelModifier.
	 * @return int
	 */
	public final int getLevelModifier()
	{
		return _levelModifier;
	}
	
	/**
	 * Method getMagicLevel.
	 * @return int
	 */
	public final int getMagicLevel()
	{
		return _magicLevel;
	}
	
	/**
	 * Method getMatak.
	 * @return int
	 */
	public int getMatak()
	{
		return _matak;
	}
	
	/**
	 * Method getMinPledgeClass.
	 * @return int
	 */
	public int getMinPledgeClass()
	{
		return _minPledgeClass;
	}
	
	/**
	 * Method getMinRank.
	 * @return int
	 */
	public int getMinRank()
	{
		return _minRank;
	}
	
	/**
	 * Method getMpConsume.
	 * @return double
	 */
	public final double getMpConsume()
	{
		return _mpConsume1 + _mpConsume2;
	}
	
	/**
	 * Method getMpConsume1.
	 * @return double
	 */
	public final double getMpConsume1()
	{
		return _mpConsume1;
	}
	
	/**
	 * Method getMpConsume2.
	 * @return double
	 */
	public final double getMpConsume2()
	{
		return _mpConsume2;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Method getNegatePower.
	 * @return int
	 */
	public int getNegatePower()
	{
		return _negatePower;
	}
	
	/**
	 * Method getNegateSkill.
	 * @return int
	 */
	public int getNegateSkill()
	{
		return _negateSkill;
	}
	
	/**
	 * Method getNextAction.
	 * @return NextAction
	 */
	public NextAction getNextAction()
	{
		return _nextAction;
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	public int getNpcId()
	{
		return _npcId;
	}
	
	/**
	 * Method getNumCharges.
	 * @return int
	 */
	public int getNumCharges()
	{
		return _numCharges;
	}
	
	/**
	 * Method getMaxCharges.
	 * @return int
	 */
	public int getMaxCharges()
	{
		return _maxCharges;
	}
	
	/**
	 * Method getPower.
	 * @param target Creature
	 * @return double
	 */
	public final double getPower(Creature target)
	{
		if (target != null)
		{
			if (target.isPlayable())
			{
				return getPowerPvP();
			}
			if (target.isMonster())
			{
				return getPowerPvE();
			}
		}
		return getPower();
	}
	
	/**
	 * Method getPower.
	 * @return double
	 */
	public final double getPower()
	{
		return _power;
	}
	
	/**
	 * Method getPower2.
	 * @return double
	 */
	public final double getPower2()
	{
		return _power2;
	}
	
	/**
	 * Method getPowerPvP.
	 * @return double
	 */
	public final double getPowerPvP()
	{
		return _powerPvP != 0 ? _powerPvP : _power;
	}
	
	/**
	 * Method getPowerPvE.
	 * @return double
	 */
	public final double getPowerPvE()
	{
		return _powerPvE != 0 ? _powerPvE : _power;
	}
	
	/**
	 * Method getReuseDelay.
	 * @return long
	 */
	public final long getReuseDelay()
	{
		return _reuseDelay;
	}
	
	/**
	 * Method setReuseDelay.
	 * @param newReuseDelay long
	 */
	public final void setReuseDelay(long newReuseDelay)
	{
		_reuseDelay = newReuseDelay;
	}

	/**
	 * Method getMaxHitCancelCount.
	 * @return double
	 */
	public final int getMaxHitCancelCount()
	{
		return _maxHitCancelCount;
	}
	/**
	 * Method getShieldIgnore.
	 * @return boolean
	 */
	public final boolean getShieldIgnore()
	{
		return _isShieldignore;
	}
	
	/**
	 * Method isReflectable.
	 * @return boolean
	 */
	public final boolean isReflectable()
	{
		return _isReflectable;
	}
	
	/**
	 * Method getSkillInterruptTime.
	 * @return int
	 */
	public final int getSkillInterruptTime()
	{
		return _skillInterruptTime;
	}
	
	/**
	 * Method getSkillRadius.
	 * @return int
	 */
	public final int getSkillRadius()
	{
		return _skillRadius;
	}
	
	/**
	 * Method getSkillType.
	 * @return SkillType
	 */
	public final SkillType getSkillType()
	{
		return _skillType;
	}
	
	/**
	 * Method getSoulsConsume.
	 * @return int
	 */
	public int getSoulsConsume()
	{
		return _soulsConsume;
	}
	
	/**
	 * Method getSymbolId.
	 * @return int
	 */
	public int getSymbolId()
	{
		return _symbolId;
	}
	
	/**
	 * Method getTargetType.
	 * @return SkillTargetType
	 */
	public final SkillTargetType getTargetType()
	{
		return _targetType;
	}
	
	/**
	 * Method getTraitType.
	 * @return SkillTrait
	 */
	public final SkillTrait getTraitType()
	{
		return _traitType;
	}
	
	/**
	 * Method getSaveVs.
	 * @return BaseStats
	 */
	public final BaseStats getSaveVs()
	{
		return _saveVs;
	}
	
	public final boolean isDispelOnDamage()
	{
		return _dispelOnDamage;
	}
	
	/**
	 * Method getWeaponsAllowed.
	 * @return int
	 */
	public final int getWeaponsAllowed()
	{
		return _weaponsAllowed;
	}
	
	/**
	 * Method getLethal1.
	 * @return double
	 */
	public double getLethal1()
	{
		return _lethal1;
	}
	
	/**
	 * Method getLethal2.
	 * @return double
	 */
	public double getLethal2()
	{
		return _lethal2;
	}
	
	/**
	 * Method getBaseValues.
	 * @return String
	 */
	public String getBaseValues()
	{
		return _baseValues;
	}
	
	/**
	 * Method isBlockedByChar.
	 * @param effected Creature
	 * @param et EffectTemplate
	 * @return boolean
	 */
	public boolean isBlockedByChar(Creature effected, EffectTemplate et)
	{
		if (et.getAttachedFuncs() == null)
		{
			return false;
		}
		for (FuncTemplate func : et.getAttachedFuncs())
		{
			if ((func != null) && effected.checkBlockedStat(func._stat))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method isCancelable.
	 * @return boolean
	 */
	public final boolean isCancelable()
	{
		return _isCancelable && (getSkillType() != SkillType.TRANSFORMATION) && !isToggle();
	}
	
	/**
	 * Method isCommon.
	 * @return boolean
	 */
	public final boolean isCommon()
	{
		return _isCommon;
	}
	
	/**
	 * Method getCriticalRate.
	 * @return int
	 */
	public final int getCriticalRate()
	{
		return _criticalRate;
	}
	
	/**
	 * Method isHandler.
	 * @return boolean
	 */
	public final boolean isHandler()
	{
		return _isItemHandler;
	}

	/**
	 * Method castOverStun.
	 * @return boolean
	 */
	public final boolean isCastOverStun()
	{
		return _castOverStun;
	}
	/**
	 * Method isMagic.
	 * @return boolean
	 */
	public final boolean isMagic()
	{
		return _magicType == SkillMagicType.MAGIC;
	}
	
	/**
	 * Method getMagicType.
	 * @return SkillMagicType
	 */
	public final SkillMagicType getMagicType()
	{
		return _magicType;
	}
	
	/**
	 * Method isNewbie.
	 * @return boolean
	 */
	public final boolean isNewbie()
	{
		return _isNewbie;
	}
	
	/**
	 * Method isPreservedOnDeath.
	 * @return boolean
	 */
	public final boolean isPreservedOnDeath()
	{
		return _isPreservedOnDeath;
	}
	
	/**
	 * Method isHeroic.
	 * @return boolean
	 */
	public final boolean isHeroic()
	{
		return _isHeroic;
	}
	
	/**
	 * Method isAwakeningToggle.
	 * @return boolean
	 */
	public final boolean isAwakeningToggle()
	{
		return _isAwakeningToggle;
	}
	
	/**
	 * Method isSelfDispellable.
	 * @return boolean
	 */
	public final boolean isSelfDispellable()
	{
		return _isSelfDispellable;
	}
	
	/**
	 * Method setOperateType.
	 * @param type SkillOpType
	 */
	public void setOperateType(SkillOpType type)
	{
		_operateType = type;
	}
	
	/**
	 * Method isOverhit.
	 * @return boolean
	 */
	public final boolean isOverhit()
	{
		return _isOverhit;
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public final boolean isActive()
	{
		return (_operateType == SkillOpType.OP_ACTIVE) || (_operateType == SkillOpType.OP_ACTIVE_TOGGLE);
	}
	
	/**
	 * Method isPassive.
	 * @return boolean
	 */
	public final boolean isPassive()
	{
		return _operateType == SkillOpType.OP_PASSIVE;
	}
	
	/**
	 * Method isSaveable.
	 * @return boolean
	 */
	public boolean isSaveable()
	{
		if (!Config.ALT_SAVE_UNSAVEABLE && (isMusic() || _name.startsWith("Herb of")))
		{
			return false;
		}
		return _isSaveable;
	}
	
	/**
	 * Method isSkillTimePermanent.
	 * @return boolean
	 */
	public final boolean isSkillTimePermanent()
	{
		return _isSkillTimePermanent || _isItemHandler || _name.contains("Talisman");
	}
	
	/**
	 * Method isReuseDelayPermanent.
	 * @return boolean
	 */
	public final boolean isReuseDelayPermanent()
	{
		return _isReuseDelayPermanent || _isItemHandler;
	}
	
	/**
	 * Method isDeathlink.
	 * @return boolean
	 */
	public boolean isDeathlink()
	{
		return _deathlink;
	}
	
	/**
	 * Method isBasedOnTargetDebuff.
	 * @return boolean
	 */
	public boolean isBasedOnTargetDebuff()
	{
		return _basedOnTargetDebuff;
	}
	
	/**
	 * Method isSoulBoost.
	 * @return boolean
	 */
	public boolean isSoulBoost()
	{
		return _isSoulBoost;
	}
	
	/**
	 * Method isChargeBoost.
	 * @return boolean
	 */
	public boolean isChargeBoost()
	{
		return _isChargeBoost;
	}
	
	/**
	 * Method isUsingWhileCasting.
	 * @return boolean
	 */
	public boolean isUsingWhileCasting()
	{
		return _isUsingWhileCasting;
	}
	
	/**
	 * Method isBehind.
	 * @return boolean
	 */
	public boolean isBehind()
	{
		return _isBehind;
	}
	
	/**
	 * Method isHideStartMessage.
	 * @return boolean
	 */
	public boolean isHideStartMessage()
	{
		return _hideStartMessage;
	}
	
	/**
	 * Method isHideUseMessage.
	 * @return boolean
	 */
	public boolean isHideUseMessage()
	{
		return _hideUseMessage;
	}
	
	/**
	 * Method isSSPossible.
	 * @return boolean
	 */
	public boolean isSSPossible()
	{
		return (_isUseSS == Ternary.TRUE) || ((_isUseSS == Ternary.DEFAULT) && !_isItemHandler && !isMusic() && isActive() && !((getTargetType() == SkillTargetType.TARGET_SELF) && !isMagic()));
	}
	
	/**
	 * Method isSuicideAttack.
	 * @return boolean
	 */
	public final boolean isSuicideAttack()
	{
		return _isSuicideAttack;
	}
	
	/**
	 * Method isToggle.
	 * @return boolean
	 */
	public final boolean isToggle()
	{
		return (_operateType == SkillOpType.OP_TOGGLE) || (_operateType == SkillOpType.OP_ACTIVE_TOGGLE);
	}
	
	/**
	 * Method isActiveToggle.
	 * @return boolean
	 */
	public final boolean isActiveToggle()
	{
		return _operateType == SkillOpType.OP_ACTIVE_TOGGLE;
	}
	
	/**
	 * Method setCastRange.
	 * @param castRange int
	 */
	public void setCastRange(int castRange)
	{
		_castRange = castRange;
	}
	
	/**
	 * Method setDisplayLevel.
	 * @param lvl int
	 */
	public void setDisplayLevel(int lvl)
	{
		_displayLevel = lvl;
	}
	
	/**
	 * Method setHitTime.
	 * @param hitTime int
	 */
	public void setHitTime(int hitTime)
	{
		_hitTime = hitTime;
	}
	
	/**
	 * Method setHpConsume.
	 * @param hpConsume int
	 */
	public void setHpConsume(int hpConsume)
	{
		_hpConsume = hpConsume;
	}
	
	/**
	 * Method setMagicType.
	 * @param type SkillMagicType
	 */
	public void setMagicType(SkillMagicType type)
	{
		_magicType = type;
	}
	
	/**
	 * Method setMagicLevel.
	 * @param newlevel int
	 */
	public final void setMagicLevel(int newlevel)
	{
		_magicLevel = newlevel;
	}
	
	/**
	 * Method setMpConsume1.
	 * @param mpConsume1 double
	 */
	public void setMpConsume1(double mpConsume1)
	{
		_mpConsume1 = mpConsume1;
	}
	
	/**
	 * Method setMpConsume2.
	 * @param mpConsume2 double
	 */
	public void setMpConsume2(double mpConsume2)
	{
		_mpConsume2 = mpConsume2;
	}
	
	/**
	 * Method setName.
	 * @param name String
	 */
	public void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * Method setOverhit.
	 * @param isOverhit boolean
	 */
	public void setOverhit(final boolean isOverhit)
	{
		_isOverhit = isOverhit;
	}
	
	/**
	 * Method setPower.
	 * @param power double
	 */
	public final void setPower(double power)
	{
		_power = power;
	}
	
	/**
	 * Method setSkillInterruptTime.
	 * @param skillInterruptTime int
	 */
	public void setSkillInterruptTime(int skillInterruptTime)
	{
		_skillInterruptTime = skillInterruptTime;
	}
	
	/**
	 * Method isItemSkill.
	 * @return boolean
	 */
	public boolean isItemSkill()
	{
		return _name.contains("Item Skill") || _name.contains("Talisman"); 
	}

		
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _name + "[id=" + _id + ",lvl=" + _level + "]";
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	public abstract void useSkill(Creature activeChar, List<Creature> targets);
	
	/**
	 * Method isAoE.
	 * @return boolean
	 */
	public boolean isAoE()
	{
		switch (_targetType)
		{
			case TARGET_AREA:
			case TARGET_AREA_AIM_CORPSE:
			case TARGET_AURA:
			case TARGET_GROUND:
			case TARGET_PET_AURA:
			case TARGET_MULTIFACE:
			case TARGET_MULTIFACE_AURA:
			case TARGET_TUNNEL:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Method isNotTargetAoE.
	 * @return boolean
	 */
	public boolean isNotTargetAoE()
	{
		switch (_targetType)
		{
			case TARGET_AURA:
			case TARGET_GROUND:
			case TARGET_MULTIFACE_AURA:
			case TARGET_ALLY:
			case TARGET_CLAN:
			case TARGET_CLAN_ONLY:
			case TARGET_PARTY:
			case TARGET_PARTY_WITHOUT_ME:
			case TARGET_SUMMON_AURA:
			case TARGET_SUMMON_AURA_AND_ME:
				return true;
			default:
				return false;
		}
	}
	/**
	 * Method isAlterSkill.
	 * @return boolean
	 */
	public boolean isAlterSkill()
	{
		return _isAlterSkill;
	}

	/**
	 * Method isAuraSkill.
	 * @return boolean
	 */
	public boolean isAuraSkill()
	{
		return _isAuraSkill;
	}
	
	/**
	 * Method isOffensive.
	 * @return boolean
	 */
	public boolean isOffensive()
	{
		return _isOffensive;
	}
	
	/**
	 * Method isForceUse.
	 * @return boolean
	 */
	public final boolean isForceUse()
	{
		return _isForceUse;
	}
	
	/**
	 * Method isAI.
	 * @return boolean
	 */
	public boolean isAI()
	{
		return _skillType.isAI();
	}
	
	/**
	 * Method isPvM.
	 * @return boolean
	 */
	public boolean isPvM()
	{
		return _isPvm;
	}
	
	/**
	 * Method isPvpSkill.
	 * @return boolean
	 */
	public final boolean isPvpSkill()
	{
		return _isPvpSkill;
	}
	
	/**
	 * Method isFishingSkill.
	 * @return boolean
	 */
	public final boolean isFishingSkill()
	{
		return _isFishingSkill;
	}
	
	/**
	 * Method isMusic.
	 * @return boolean
	 */
	public boolean isMusic()
	{
		return _magicType == SkillMagicType.MUSIC;
	}
	
	/**
	 * Method isTrigger.
	 * @return boolean
	 */
	public boolean isTrigger()
	{
		return _isTrigger;
	}
	
	/**
	 * Method oneTarget.
	 * @return boolean
	 */
	public boolean oneTarget()
	{
		switch (_targetType)
		{
			case TARGET_CORPSE:
			case TARGET_CORPSE_PLAYER:
			case TARGET_HOLY:
			case TARGET_FLAGPOLE:
			case TARGET_ITEM:
			case TARGET_NONE:
			case TARGET_MENTEE:
			case TARGET_ONE:
			case TARGET_ONE_PLAYER:
			case TARGET_PARTY_ONE:
			case TARGET_PET:
			case TARGET_SUMMON:
			case TARGET_OWNER:
			case TARGET_ENEMY_PET:
			case TARGET_ENEMY_SUMMON:
			case TARGET_ENEMY_SERVITOR:
			case TARGET_SELF:
			case TARGET_UNLOCKABLE:
			case TARGET_CHEST:
			case TARGET_FEEDABLE_BEAST:
			case TARGET_SIEGE:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Method getCancelTarget.
	 * @return int
	 */
	public int getCancelTarget()
	{
		return _cancelTarget;
	}
	
	/**
	 * Method isSkillInterrupt.
	 * @return boolean
	 */
	public boolean isSkillInterrupt()
	{
		return _skillInterrupt;
	}
	
	/**
	 * Method isNotUsedByAI.
	 * @return boolean
	 */
	public boolean isNotUsedByAI()
	{
		return _isNotUsedByAI;
	}
	
	/**
	 * Method isIgnoreResists.
	 * @return boolean
	 */
	public boolean isIgnoreResists()
	{
		return _isIgnoreResists;
	}
	
	/**
	 * Method isIgnoreInvul.
	 * @return boolean
	 */
	public boolean isIgnoreInvul()
	{
		return _isIgnoreInvul;
	}
	
	/**
	 * Method isNotAffectedByMute.
	 * @return boolean
	 */
	public boolean isNotAffectedByMute()
	{
		return _isNotAffectedByMute;
	}
	
	/**
	 * Method flyingTransformUsage.
	 * @return boolean
	 */
	public boolean flyingTransformUsage()
	{
		return _flyingTransformUsage;
	}
	
	/**
	 * Method canUseTeleport.
	 * @return boolean
	 */
	public boolean canUseTeleport()
	{
		return _canUseTeleport;
	}
	
	/**
	 * Method getCastCount.
	 * @return int
	 */
	public int getCastCount()
	{
		return _castCount;
	}
	
	/**
	 * Method getEnchantLevelCount.
	 * @return int
	 */
	public int getEnchantLevelCount()
	{
		return _enchantLevelCount;
	}
	
	/**
	 * Method setEnchantLevelCount.
	 * @param count int
	 */
	public void setEnchantLevelCount(int count)
	{
		_enchantLevelCount = count;
	}
	
	/**
	 * Method isClanSkill.
	 * @return boolean
	 */
	public boolean isClanSkill()
	{
		return ((_id >= 370) && (_id <= 391)) || ((_id >= 611) && (_id <= 616)) || ((_id >= 19003) && (_id <= 19076));
	}
	
	/**
	 * Method isBaseTransformation.
	 * @return boolean
	 */
	public boolean isBaseTransformation()
	{
		return ((_id >= 810) && (_id <= 813)) || ((_id >= 1520) && (_id <= 1522)) || (_id == 538);
	}
	
	/**
	 * Method isSummonerTransformation.
	 * @return boolean
	 */
	public boolean isSummonerTransformation()
	{
		return (_id >= 929) && (_id <= 931);
	}
	
	/**
	 * Method getSimpleDamage.
	 * @param attacker Creature
	 * @param target Creature
	 * @return double
	 */
	public double getSimpleDamage(Creature attacker, Creature target)
	{
		if (isMagic())
		{
			double mAtk = attacker.getMAtk(target, this);
			double mdef = target.getMDef(null, this);
			double power = getPower();
			int sps = (attacker.getChargedSpiritShot() > 0) && isSSPossible() ? attacker.getChargedSpiritShot() * 2 : 1;
			return (91 * power * Math.sqrt(sps * mAtk)) / mdef;
		}
		double pAtk = attacker.getPAtk(target);
		double pdef = target.getPDef(attacker);
		double power = getPower();
		int ss = attacker.getChargedSoulShot() && isSSPossible() ? 2 : 1;
		return (ss * (pAtk + power) * 70.) / pdef;
	}
	
	/**
	 * Method getReuseForMonsters.
	 * @return long
	 */
	public long getReuseForMonsters()
	{
		long min = 1000;
		switch (_skillType)
		{
			case PARALYZE:
			case DEBUFF:
			case NEGATE_EFFECTS:
			case NEGATE_STATS:
			case STEAL_BUFF:
				min = 10000;
				break;
			case MUTE:
			case ROOT:
			case SLEEP:
			case STUN:
				min = 5000;
				break;
			default:
				break;
		}
		return Math.max(Math.max(_hitTime + _coolTime, _reuseDelay), min);
	}
	
	/**
	 * Method getAbsorbPart.
	 * @return double
	 */
	public double getAbsorbPart()
	{
		return _absorbPart;
	}
	
	/**
	 * Method isProvoke.
	 * @return boolean
	 */
	public boolean isProvoke()
	{
		return _isProvoke;
	}
	
	/**
	 * Method getIcon.
	 * @return String
	 */
	public String getIcon()
	{
		return _icon;
	}
	
	public Boolean isMarkDamage()
	{
		return _isMarkDamage;
	}
	
	/**
	 * Method getEnergyConsume.
	 * @return int
	 */
	public int getEnergyConsume()
	{
		return _energyConsume;
	}
	
	/**
	 * Method setCubicSkill.
	 * @param value boolean
	 */
	public void setCubicSkill(boolean value)
	{
		_isCubicSkill = value;
	}
	
	/**
	 * Method isCubicSkill.
	 * @return boolean
	 */
	public boolean isCubicSkill()
	{
		return _isCubicSkill;
	}
	
	/**
	 * Method getReuseGroupId.
	 * @return int
	 */
	public int getReuseGroupId()
	{
		return reuseGroupId;
	}
	
	/**
	 * Method getRelationSkills.
	 * @return int[]
	 */
	public int[] getRelationSkills()
	{
		return _relationSkillsId;
	}
	
	/**
	 * Method isRelationSkill.
	 * @return boolean
	 */
	public boolean isRelationSkill()
	{
		return _isRelation;
	}
	
	/**
	 * Method getFlySpeed.
	 * @return int
	 */
	public int getFlySpeed()
	{
		return _flySpeed;
	}

	public boolean isPowerModified() 
	{		
		return _isPowerModified;
	}
}

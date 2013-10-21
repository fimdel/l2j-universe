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

import java.util.List;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.network.serverpackets.ExVoteSystemInfo;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameObjectTasks
{
	/**
	 * @author Mobius
	 */
	public static class DeleteTask extends RunnableImpl
	{
		/**
		 * Field _ref.
		 */
		private final HardReference<? extends Creature> _ref;
		
		/**
		 * Constructor for DeleteTask.
		 * @param c Creature
		 */
		public DeleteTask(Creature c)
		{
			_ref = c.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature c = _ref.get();
			if (c != null)
			{
				c.deleteMe();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SoulConsumeTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for SoulConsumeTask.
		 * @param player Player
		 */
		public SoulConsumeTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			player.setConsumedSouls(player.getConsumedSouls() + 1, null);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class PvPFlagTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for PvPFlagTask.
		 * @param player Player
		 */
		public PvPFlagTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			long diff = Math.abs(System.currentTimeMillis() - player.getlastPvpAttack());
			if (diff > Config.PVP_TIME)
			{
				player.stopPvPFlag();
			}
			else if (diff > (Config.PVP_TIME - 20000))
			{
				player.updatePvPFlag(2);
			}
			else
			{
				player.updatePvPFlag(1);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class HourlyTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for HourlyTask.
		 * @param player Player
		 */
		public HourlyTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			int hoursInGame = player.getHoursInGame();
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_BEEN_PLAYING_FOR_AN_EXTENDED_PERIOD_OF_TIME_PLEASE_CONSIDER_TAKING_A_BREAK).addNumber(hoursInGame));
			player.sendPacket(new SystemMessage(SystemMessage.YOU_OBTAINED_S1_RECOMMENDS).addNumber(player.addRecomLeft()));
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class RecomBonusTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for RecomBonusTask.
		 * @param player Player
		 */
		public RecomBonusTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			player.setRecomBonusTime(0);
			player.sendPacket(new ExVoteSystemInfo(player));
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class WaterTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for WaterTask.
		 * @param player Player
		 */
		public WaterTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			if (player.isDead() || !player.isInWater())
			{
				player.stopWaterTask();
				return;
			}
			double reduceHp = player.getMaxHp() < 100 ? 1 : player.getMaxHp() / 100;
			player.reduceCurrentHp(reduceHp, 0, player, null, false, false, true, false, false, false, false);
			player.sendPacket(new SystemMessage(SystemMessage.YOU_RECEIVED_S1_DAMAGE_BECAUSE_YOU_WERE_UNABLE_TO_BREATHE).addNumber((long) reduceHp));
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class KickTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for KickTask.
		 * @param player Player
		 */
		public KickTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			player.setOfflineMode(false);
			player.kick();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class UnJailTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for UnJailTask.
		 * @param player Player
		 */
		public UnJailTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			player.unblock();
			player.standUp();
			player.teleToLocation(17817, 170079, -3530, ReflectionManager.DEFAULT);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class EndSitDownTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for EndSitDownTask.
		 * @param player Player
		 */
		public EndSitDownTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			player.sittingTaskLaunched = false;
			player.getAI().clearNextAction();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class EndStandUpTask extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for EndStandUpTask.
		 * @param player Player
		 */
		public EndStandUpTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				return;
			}
			player.sittingTaskLaunched = false;
			player.setSitting(false);
			if (!player.getAI().setNextIntention())
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class AltMagicUseTask extends RunnableImpl
	{
		/**
		 * Field _skill.
		 */
		public final Skill _skill;
		/**
		 * Field _targetRef.
		 */
		/**
		 * Field _charRef.
		 */
		private final HardReference<? extends Creature> _charRef, _targetRef;
		
		/**
		 * Constructor for AltMagicUseTask.
		 * @param character Creature
		 * @param target Creature
		 * @param skill Skill
		 */
		public AltMagicUseTask(Creature character, Creature target, Skill skill)
		{
			_charRef = character.getRef();
			_targetRef = target.getRef();
			_skill = skill;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature cha, target;
			if (((cha = _charRef.get()) == null) || ((target = _targetRef.get()) == null))
			{
				return;
			}
			cha.altOnMagicUseTimer(target, _skill);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class CastEndTimeTask extends RunnableImpl
	{
		/**
		 * Field _charRef.
		 */
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for CastEndTimeTask.
		 * @param character Creature
		 */
		public CastEndTimeTask(Creature character)
		{
			_charRef = character.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			if (character == null)
			{
				return;
			}
			character.onCastEndTime(true);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class HitTask extends RunnableImpl
	{
		/**
		 * Field _notify.
		 */
		/**
		 * Field _unchargeSS.
		 */
		/**
		 * Field _soulshot.
		 */
		/**
		 * Field _shld.
		 */
		/**
		 * Field _miss.
		 */
		/**
		 * Field _crit.
		 */
		boolean _crit, _miss, _shld, _soulshot, _unchargeSS, _notify;
		/**
		 * Field _reflectableDamage.
		 */
		/**
		 * Field _damage.
		 */
		int _damage, _reflectableDamage;
		/**
		 * Field _targetRef.
		 */
		/**
		 * Field _charRef.
		 */
		private final HardReference<? extends Creature> _charRef, _targetRef;
		
		/**
		 * Constructor for HitTask.
		 * @param cha Creature
		 * @param target Creature
		 * @param damage int
		 * @param reflectableDamage int
		 * @param crit boolean
		 * @param miss boolean
		 * @param soulshot boolean
		 * @param shld boolean
		 * @param unchargeSS boolean
		 * @param notify boolean
		 */
		public HitTask(Creature cha, Creature target, int damage, int reflectableDamage, boolean crit, boolean miss, boolean soulshot, boolean shld, boolean unchargeSS, boolean notify)
		{
			_charRef = cha.getRef();
			_targetRef = target.getRef();
			_damage = damage;
			_reflectableDamage = reflectableDamage;
			_crit = crit;
			_shld = shld;
			_miss = miss;
			_soulshot = soulshot;
			_unchargeSS = unchargeSS;
			_notify = notify;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character, target;
			if (((character = _charRef.get()) == null) || ((target = _targetRef.get()) == null))
			{
				return;
			}
			if (character.isAttackAborted())
			{
				return;
			}
			character.onHitTimer(target, _damage, _reflectableDamage, _crit, _miss, _soulshot, _shld, _unchargeSS);
			if (_notify)
			{
				character.getAI().notifyEvent(CtrlEvent.EVT_READY_TO_ACT);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class MagicUseTask extends RunnableImpl
	{
		/**
		 * Field _forceUse.
		 */
		public boolean _forceUse;
		/**
		 * Field _charRef.
		 */
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for MagicUseTask.
		 * @param cha Creature
		 * @param forceUse boolean
		 */
		public MagicUseTask(Creature cha, boolean forceUse)
		{
			_charRef = cha.getRef();
			_forceUse = forceUse;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			if (character == null)
			{
				return;
			}
			Skill castingSkill = character.getCastingSkill();
			Creature castingTarget = character.getCastingTarget();
			if ((castingSkill == null) || (castingTarget == null))
			{
				character.clearCastVars();
				return;
			}
			character.onMagicUseTimer(castingTarget, castingSkill, _forceUse);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class MagicLaunchedTask extends RunnableImpl
	{
		/**
		 * Field _forceUse.
		 */
		public boolean _forceUse;
		/**
		 * Field _charRef.
		 */
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for MagicLaunchedTask.
		 * @param cha Creature
		 * @param forceUse boolean
		 */
		public MagicLaunchedTask(Creature cha, boolean forceUse)
		{
			_charRef = cha.getRef();
			_forceUse = forceUse;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			if (character == null)
			{
				return;
			}
			Skill castingSkill = character.getCastingSkill();
			Creature castingTarget = character.getCastingTarget();
			if ((castingSkill == null) || (castingTarget == null))
			{
				character.clearCastVars();
				return;
			}
			List<Creature> targets = castingSkill.getTargets(character, castingTarget, _forceUse);
			character.broadcastPacket(new MagicSkillLaunched(character.getObjectId(), castingSkill.getDisplayId(), castingSkill.getDisplayLevel(), targets));
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class NotifyAITask extends RunnableImpl
	{
		/**
		 * Field _evt.
		 */
		private final CtrlEvent _evt;
		/**
		 * Field _agr0.
		 */
		private final Object _agr0;
		/**
		 * Field _agr1.
		 */
		private final Object _agr1;
		/**
		 * Field _charRef.
		 */
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for NotifyAITask.
		 * @param cha Creature
		 * @param evt CtrlEvent
		 * @param agr0 Object
		 * @param agr1 Object
		 */
		public NotifyAITask(Creature cha, CtrlEvent evt, Object agr0, Object agr1)
		{
			_charRef = cha.getRef();
			_evt = evt;
			_agr0 = agr0;
			_agr1 = agr1;
		}
		
		/**
		 * Constructor for NotifyAITask.
		 * @param cha Creature
		 * @param evt CtrlEvent
		 */
		public NotifyAITask(Creature cha, CtrlEvent evt)
		{
			this(cha, evt, null, null);
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			if ((character == null) || !character.hasAI())
			{
				return;
			}
			character.getAI().notifyEvent(_evt, _agr0, _agr1);
		}
	}
}

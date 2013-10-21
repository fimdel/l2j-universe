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
package lineage2.gameserver.ai;

import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_CAST;
import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_INTERACT;
import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_PICK_UP;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Skill.NextAction;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.ClonePlayer;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayableAI extends CharacterAI
{
	/**
	 * Field thinking.
	 */
	private volatile int thinking = 0;
	/**
	 * Field _intention_arg1. Field _intention_arg0.
	 */
	protected Object _intention_arg0 = null, _intention_arg1 = null;
	/**
	 * Field _skill.
	 */
	protected Skill _skill;
	/**
	 * Field _nextAction.
	 */
	private nextAction _nextAction;
	/**
	 * Field _nextAction_arg0.
	 */
	private Object _nextAction_arg0;
	/**
	 * Field _nextAction_arg1.
	 */
	private Object _nextAction_arg1;
	/**
	 * Field _nextAction_arg2.
	 */
	private boolean _nextAction_arg2;
	/**
	 * Field _nextAction_arg3.
	 */
	private boolean _nextAction_arg3;
	/**
	 * Field _forceUse.
	 */
	protected boolean _forceUse;
	/**
	 * Field _dontMove.
	 */
	private boolean _dontMove;
	/**
	 * Field _followTask.
	 */
	ScheduledFuture<?> _followTask;
	
	/**
	 * Constructor for PlayableAI.
	 * @param actor Playable
	 */
	public PlayableAI(Playable actor)
	{
		super(actor);
	}
	
	/**
	 * @author Mobius
	 */
	public enum nextAction
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
		 * Field MOVE.
		 */
		MOVE,
		/**
		 * Field REST.
		 */
		REST,
		/**
		 * Field PICKUP.
		 */
		PICKUP,
		/**
		 * Field INTERACT.
		 */
		INTERACT,
		/**
		 * Field COUPLE_ACTION.
		 */
		COUPLE_ACTION
	}
	
	/**
	 * Method changeIntention.
	 * @param intention CtrlIntention
	 * @param arg0 Object
	 * @param arg1 Object
	 */
	@Override
	public void changeIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		super.changeIntention(intention, arg0, arg1);
		_intention_arg0 = arg0;
		_intention_arg1 = arg1;
	}
	
	/**
	 * Method setIntention.
	 * @param intention CtrlIntention
	 * @param arg0 Object
	 * @param arg1 Object
	 */
	@Override
	public void setIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		_intention_arg0 = null;
		_intention_arg1 = null;
		super.setIntention(intention, arg0, arg1);
	}
	
	/**
	 * Method onIntentionCast.
	 * @param skill Skill
	 * @param target Creature
	 */
	@Override
	protected void onIntentionCast(Skill skill, Creature target)
	{
		_skill = skill;
		super.onIntentionCast(skill, target);
	}
	
	/**
	 * Method setNextAction.
	 * @param action nextAction
	 * @param arg0 Object
	 * @param arg1 Object
	 * @param arg2 boolean
	 * @param arg3 boolean
	 */
	@Override
	public void setNextAction(nextAction action, Object arg0, Object arg1, boolean arg2, boolean arg3)
	{
		_nextAction = action;
		_nextAction_arg0 = arg0;
		_nextAction_arg1 = arg1;
		_nextAction_arg2 = arg2;
		_nextAction_arg3 = arg3;
	}
	
	/**
	 * Method setNextIntention.
	 * @return boolean
	 */
	public boolean setNextIntention()
	{
		nextAction nextAction = _nextAction;
		Object nextAction_arg0 = _nextAction_arg0;
		Object nextAction_arg1 = _nextAction_arg1;
		boolean nextAction_arg2 = _nextAction_arg2;
		boolean nextAction_arg3 = _nextAction_arg3;
		Playable actor = getActor();
		if ((nextAction == null) || actor.isActionsDisabled())
		{
			return false;
		}
		Skill skill;
		Creature target;
		GameObject object;
		switch (nextAction)
		{
			case ATTACK:
				if (nextAction_arg0 == null)
				{
					return false;
				}
				target = (Creature) nextAction_arg0;
				_forceUse = nextAction_arg2;
				_dontMove = nextAction_arg3;
				clearNextAction();
				setIntention(AI_INTENTION_ATTACK, target);
				break;
			case CAST:
				if ((nextAction_arg0 == null) || (nextAction_arg1 == null))
				{
					return false;
				}
				skill = (Skill) nextAction_arg0;
				target = (Creature) nextAction_arg1;
				_forceUse = nextAction_arg2;
				_dontMove = nextAction_arg3;
				clearNextAction();
				if (!skill.checkCondition(actor, target, _forceUse, _dontMove, true))
				{
					if ((skill.getNextAction() == NextAction.ATTACK) && !actor.equals(target))
					{
						setNextAction(PlayableAI.nextAction.ATTACK, target, null, _forceUse, false);
						return setNextIntention();
					}
					return false;
				}
				setIntention(AI_INTENTION_CAST, skill, target);
				break;
			case MOVE:
				if ((nextAction_arg0 == null) || (nextAction_arg1 == null))
				{
					return false;
				}
				Location loc = (Location) nextAction_arg0;
				Integer offset = (Integer) nextAction_arg1;
				clearNextAction();
				actor.moveToLocation(loc, offset, nextAction_arg2);
				break;
			case REST:
				actor.sitDown(null);
				break;
			case INTERACT:
				if (nextAction_arg0 == null)
				{
					return false;
				}
				object = (GameObject) nextAction_arg0;
				clearNextAction();
				onIntentionInteract(object);
				break;
			case PICKUP:
				if (nextAction_arg0 == null)
				{
					return false;
				}
				object = (GameObject) nextAction_arg0;
				clearNextAction();
				onIntentionPickUp(object);
				break;
			case COUPLE_ACTION:
				if ((nextAction_arg0 == null) || (nextAction_arg1 == null))
				{
					return false;
				}
				target = (Creature) nextAction_arg0;
				Integer socialId = (Integer) nextAction_arg1;
				_forceUse = nextAction_arg2;
				_nextAction = null;
				clearNextAction();
				onIntentionCoupleAction((Player) target, socialId);
				break;
			default:
				return false;
		}
		return true;
	}
	
	/**
	 * Method clearNextAction.
	 */
	@Override
	public void clearNextAction()
	{
		_nextAction = null;
		_nextAction_arg0 = null;
		_nextAction_arg1 = null;
		_nextAction_arg2 = false;
		_nextAction_arg3 = false;
	}
	
	/**
	 * Method onEvtFinishCasting.
	 * @param skill_id int
	 * @param success boolean
	 */
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if (!setNextIntention())
		{
			setIntention(AI_INTENTION_ACTIVE);
		}
	}
	
	/**
	 * Method onEvtReadyToAct.
	 */
	@Override
	protected void onEvtReadyToAct()
	{
		if (!setNextIntention())
		{
			onEvtThink();
		}
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		if (!setNextIntention())
		{
			if ((getIntention() == AI_INTENTION_INTERACT) || (getIntention() == AI_INTENTION_PICK_UP))
			{
				onEvtThink();
			}
			else
			{
				changeIntention(AI_INTENTION_ACTIVE, null, null);
			}
		}
	}
	
	/**
	 * Method onEvtArrivedTarget.
	 */
	@Override
	protected void onEvtArrivedTarget()
	{
		switch (getIntention())
		{
			case AI_INTENTION_ATTACK:
				thinkAttack(false);
				break;
			case AI_INTENTION_CAST:
				thinkCast(false);
				break;
			case AI_INTENTION_FOLLOW:
				thinkFollow();
				break;
			default:
				onEvtThink();
				break;
		}
	}
	
	/**
	 * Method onEvtThink.
	 */
	@Override
	protected final void onEvtThink()
	{
		Playable actor = getActor();
		if (actor.isActionsDisabled())
		{
			return;
		}
		try
		{
			if (thinking++ > 1)
			{
				return;
			}
			switch (getIntention())
			{
				case AI_INTENTION_ACTIVE:
					thinkActive();
					break;
				case AI_INTENTION_ATTACK:
					thinkAttack(true);
					break;
				case AI_INTENTION_CAST:
					thinkCast(true);
					break;
				case AI_INTENTION_PICK_UP:
					thinkPickUp();
					break;
				case AI_INTENTION_INTERACT:
					thinkInteract();
					break;
				case AI_INTENTION_FOLLOW:
					thinkFollow();
					break;
				case AI_INTENTION_COUPLE_ACTION:
					thinkCoupleAction((Player) _intention_arg0, (Integer) _intention_arg1, false);
					break;
				case AI_INTENTION_IDLE:
					thinkIdle();
					break;
				case AI_INTENTION_REST:
					thinkRest();
					break;
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			thinking--;
		}
	}
	
	/**
	 * Method thinkActive.
	 */
	protected void thinkActive()
	{
	}
	
	/**
	 * Method thinkFollow.
	 */
	protected void thinkFollow()
	{
		Playable actor = getActor();
		Creature target = (Creature) _intention_arg0;
		Integer offset = (Integer) _intention_arg1;
		if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000) || (offset == null))
		{
			clientActionFailed();
			return;
		}
		if (actor.isFollow && (actor.getFollowTarget() == target))
		{
			clientActionFailed();
			return;
		}
		if (actor.isInRange(target, offset + 20) || actor.isMovementDisabled())
		{
			clientActionFailed();
		}
		if (_followTask != null)
		{
			_followTask.cancel(false);
			_followTask = null;
		}
		_followTask = ThreadPoolManager.getInstance().schedule(new ThinkFollow(), 250L);
	}
	
	/**
	 * @author Mobius
	 */
	protected class ThinkFollow extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Playable actor = getActor();
			if (getIntention() != AI_INTENTION_FOLLOW)
			{
				if (actor.isClone() && getIntention() == AI_INTENTION_ACTIVE)
				{
					((ClonePlayer) actor).setFollowMode(false);
				}
				else if ((actor.isPet() || actor.isServitor()) && (getIntention() == AI_INTENTION_ACTIVE))
				{
					((Summon) actor).setFollowMode(false);
				}
				return;
			}
			Creature target = (Creature) _intention_arg0;
			int offset = _intention_arg1 instanceof Integer ? (Integer) _intention_arg1 : 0;
			if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000))
			{
				setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				return;
			}
			Player player = actor.getPlayer();
			if ((player == null) || player.isLogoutStarted() || ((actor.isPet() || actor.isServitor()) && !player.getSummonList().contains(actor) && !actor.isClone()))
			{
				setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				return;
			}
			if (!actor.isInRange(target, offset + 20) && (!actor.isFollow || (actor.getFollowTarget() != target)))
			{
				actor.followToCharacter(target, offset, false);
			}
			_followTask = ThreadPoolManager.getInstance().schedule(this, 250L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class ExecuteFollow extends RunnableImpl
	{
		/**
		 * Field _target.
		 */
		private final Creature _target;
		/**
		 * Field _range.
		 */
		private final int _range;
		
		/**
		 * Constructor for ExecuteFollow.
		 * @param target Creature
		 * @param range int
		 */
		public ExecuteFollow(Creature target, int range)
		{
			_target = target;
			_range = range;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_target.isDoor())
			{
				_actor.moveToLocation(_target.getLoc(), 40, true);
			}
			else
			{
				_actor.followToCharacter(_target, _range, true);
			}
		}
	}
	
	/**
	 * Method onIntentionInteract.
	 * @param object GameObject
	 */
	@Override
	protected void onIntentionInteract(GameObject object)
	{
		Playable actor = getActor();
		if (actor.isActionsDisabled())
		{
			setNextAction(nextAction.INTERACT, object, null, false, false);
			clientActionFailed();
			return;
		}
		clearNextAction();
		changeIntention(AI_INTENTION_INTERACT, object, null);
		onEvtThink();
	}
	
	/**
	 * Method onIntentionCoupleAction.
	 * @param player Player
	 * @param socialId Integer
	 */
	@Override
	protected void onIntentionCoupleAction(Player player, Integer socialId)
	{
		clearNextAction();
		changeIntention(CtrlIntention.AI_INTENTION_COUPLE_ACTION, player, socialId);
		onEvtThink();
	}
	
	/**
	 * Method thinkInteract.
	 */
	protected void thinkInteract()
	{
		Playable actor = getActor();
		GameObject target = (GameObject) _intention_arg0;
		if (target == null)
		{
			setIntention(AI_INTENTION_ACTIVE);
			return;
		}
		int range = (int) (Math.max(30, actor.getMinDistance(target)) + 20);
		if (actor.isInRangeZ(target, range))
		{
			if (actor.isPlayer())
			{
				//((Player) actor).doInteract(target);
				target.onActionSelect((Player) actor, false);
			}
			setIntention(AI_INTENTION_ACTIVE);
		}
		else
		{
			actor.moveToLocation(target.getLoc(), 40, true);
			setNextAction(nextAction.INTERACT, target, null, false, false);
		}
	}
	
	/**
	 * Method onIntentionPickUp.
	 * @param object GameObject
	 */
	@Override
	protected void onIntentionPickUp(GameObject object)
	{
		Playable actor = getActor();
		if (actor.isActionsDisabled())
		{
			setNextAction(nextAction.PICKUP, object, null, false, false);
			clientActionFailed();
			return;
		}
		clearNextAction();
		changeIntention(AI_INTENTION_PICK_UP, object, null);
		onEvtThink();
	}
	
	/**
	 * Method thinkPickUp.
	 */
	protected void thinkPickUp()
	{
		final Playable actor = getActor();
		final GameObject target = (GameObject) _intention_arg0;
		if (target == null)
		{
			setIntention(AI_INTENTION_ACTIVE);
			return;
		}
		if (actor.isInRange(target, 30) && (Math.abs(actor.getZ() - target.getZ()) < 50))
		{
			if (actor.isPlayer() || actor.isPet())
			{
				actor.doPickupItem(target);
			}
			setIntention(AI_INTENTION_ACTIVE);
		}
		else
		{
			ThreadPoolManager.getInstance().execute(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					actor.moveToLocation(target.getLoc(), 10, true);
					setNextAction(nextAction.PICKUP, target, null, false, false);
				}
			});
		}
	}
	
	/**
	 * Method thinkAttack.
	 * @param checkRange boolean
	 */
	protected void thinkAttack(boolean checkRange)
	{
		Playable actor = getActor();
		Player player = actor.getPlayer();
		if (player == null)
		{
			setIntention(AI_INTENTION_ACTIVE);
			return;
		}
		if (actor.isActionsDisabled() || actor.isAttackingDisabled())
		{
			actor.sendActionFailed();
			return;
		}
		boolean isPosessed = (actor instanceof Summon) && ((Summon) actor).isDepressed();
		Creature attack_target = getAttackTarget();
		if ((attack_target == null) || attack_target.isDead() || (!isPosessed && !(_forceUse ? attack_target.isAttackable(actor) : attack_target.isAutoAttackable(actor))))
		{
			setIntention(AI_INTENTION_ACTIVE);
			actor.sendActionFailed();
			return;
		}
		if (!checkRange)
		{
			clientStopMoving();
			actor.doAttack(attack_target);
			return;
		}
		int range = actor.getPhysicalAttackRange();
		if (range < 10)
		{
			range = 10;
		}
		boolean canSee = GeoEngine.canSeeTarget(actor, attack_target, false);
		if (!canSee && ((range > 200) || (Math.abs(actor.getZ() - attack_target.getZ()) > 200)))
		{
			actor.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
			setIntention(AI_INTENTION_ACTIVE);
			actor.sendActionFailed();
			return;
		}
		range += actor.getMinDistance(attack_target);
		if (actor.isFakeDeath())
		{
			actor.breakFakeDeath();
		}
		if (actor.isInRangeZ(attack_target, range))
		{
			if (!canSee)
			{
				actor.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
				setIntention(AI_INTENTION_ACTIVE);
				actor.sendActionFailed();
				return;
			}
			clientStopMoving(false);
			actor.doAttack(attack_target);
		}
		else if (!_dontMove)
		{
			ThreadPoolManager.getInstance().execute(new ExecuteFollow(attack_target, range - 20));
		}
		else
		{
			actor.sendActionFailed();
		}
	}
	
	/**
	 * Method thinkCast.
	 * @param checkRange boolean
	 */
	protected void thinkCast(boolean checkRange)
	{
		Playable actor = getActor();
		Creature target = getAttackTarget();
		if ((_skill.getSkillType() == SkillType.CRAFT) || _skill.isToggle())
		{
			if (_skill.checkCondition(actor, target, _forceUse, _dontMove, true))
			{
				actor.doCast(_skill, target, _forceUse);
			}
			return;
		}
		if ((target == null) || ((target.isDead() != _skill.getCorpse()) && !_skill.isNotTargetAoE()))
		{
			setIntention(AI_INTENTION_ACTIVE);
			actor.sendActionFailed();
			return;
		}
		if (!checkRange)
		{
			if ((_skill.getNextAction() == NextAction.ATTACK) && !actor.equals(target))
			{
				setNextAction(nextAction.ATTACK, target, null, _forceUse, false);
			}
			else
			{
				clearNextAction();
			}
			clientStopMoving();
			if (_skill.checkCondition(actor, target, _forceUse, _dontMove, true))
			{
				actor.doCast(_skill, target, _forceUse);
			}
			else
			{
				setNextIntention();
				if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
				{
					thinkAttack(true);
				}
			}
			return;
		}
		int range = actor.getMagicalAttackRange(_skill);
		if (range < 10)
		{
			range = 10;
		}
		boolean canSee = (_skill.getSkillType() == SkillType.TAKECASTLE) || (_skill.getSkillType() == SkillType.TAKEFORTRESS) || GeoEngine.canSeeTarget(actor, target, actor.isFlying());
		boolean noRangeSkill = _skill.getCastRange() == 32767;
		if (!noRangeSkill && !canSee && ((range > 200) || (Math.abs(actor.getZ() - target.getZ()) > 200)))
		{
			actor.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
			setIntention(AI_INTENTION_ACTIVE);
			actor.sendActionFailed();
			return;
		}
		range += actor.getMinDistance(target);
		if (actor.isFakeDeath())
		{
			actor.breakFakeDeath();
		}
		if (actor.isInRangeZ(target, range) || noRangeSkill)
		{
			if (!noRangeSkill && !canSee)
			{
				actor.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
				setIntention(AI_INTENTION_ACTIVE);
				actor.sendActionFailed();
				return;
			}
			if ((_skill.getNextAction() == NextAction.ATTACK) && !actor.equals(target))
			{
				setNextAction(nextAction.ATTACK, target, null, _forceUse, false);
			}
			else
			{
				clearNextAction();
			}
			if (_skill.checkCondition(actor, target, _forceUse, _dontMove, true))
			{
				clientStopMoving(false);
				actor.doCast(_skill, target, _forceUse);
			}
			else
			{
				setNextIntention();
				if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
				{
					thinkAttack(true);
				}
			}
		}
		else if (!_dontMove)
		{
			ThreadPoolManager.getInstance().execute(new ExecuteFollow(target, range - 20));
		}
		else
		{
			actor.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
			setIntention(AI_INTENTION_ACTIVE);
			actor.sendActionFailed();
		}
	}
	
	/**
	 * Method thinkCoupleAction.
	 * @param target Player
	 * @param socialId Integer
	 * @param cancel boolean
	 */
	protected void thinkCoupleAction(Player target, Integer socialId, boolean cancel)
	{
	}
	
	/**
	 * Method thinkIdle.
	 */
	protected void thinkIdle()
	{
	}
	
	/**
	 * Method thinkRest.
	 */
	protected void thinkRest()
	{
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		clearNextAction();
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtFakeDeath.
	 */
	@Override
	protected void onEvtFakeDeath()
	{
		clearNextAction();
		super.onEvtFakeDeath();
	}
	
	/**
	 * Method lockTarget.
	 * @param target Creature
	 */
	public void lockTarget(Creature target)
	{
		Playable actor = getActor();
		if ((target == null) || target.isDead())
		{
			actor.setAggressionTarget(null);
		}
		else if (actor.getAggressionTarget() == null)
		{
			actor.getTarget();
			actor.setAggressionTarget(target);
			actor.setTarget(target);
			clearNextAction();
		}
	}
	
	/**
	 * Method Attack.
	 * @param target GameObject
	 * @param forceUse boolean
	 * @param dontMove boolean
	 */
	@Override
	public void Attack(GameObject target, boolean forceUse, boolean dontMove)
	{
		Playable actor = getActor();
		if (target.isCreature() && (actor.isActionsDisabled() || actor.isAttackingDisabled()))
		{
			setNextAction(nextAction.ATTACK, target, null, forceUse, false);
			actor.sendActionFailed();
			return;
		}
		_dontMove = dontMove;
		_forceUse = forceUse;
		clearNextAction();
		setIntention(AI_INTENTION_ATTACK, target);
	}
	
	/**
	 * Method Cast.
	 * @param skill Skill
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 */
	@Override
	public void Cast(Skill skill, Creature target, boolean forceUse, boolean dontMove)
	{
		Playable actor = getActor();
		if (skill.altUse() || skill.isToggle())
		{
			if (((skill.isToggle() && !skill.isAwakeningToggle())|| skill.isHandler()) && (actor.isOutOfControl() || actor.isStunned() || actor.isSleeping() || actor.isParalyzed() || actor.isAlikeDead() || actor.isAirBinded() || actor.isKnockedBack() || actor.isKnockedDown() || actor.isPulledNow()))
			{
				clientActionFailed();
			}
			else
			{
				actor.altUseSkill(skill, target);
			}
			return;
		}
		if (actor.isActionsDisabled())
		{
			setNextAction(nextAction.CAST, skill, target, forceUse, dontMove);
			clientActionFailed();
			return;
		}
		_forceUse = forceUse;
		_dontMove = dontMove;
		clearNextAction();
		setIntention(CtrlIntention.AI_INTENTION_CAST, skill, target);
	}
	
	/**
	 * Method getActor.
	 * @return Playable
	 */
	@Override
	public Playable getActor()
	{
		return (Playable) super.getActor();
	}
}

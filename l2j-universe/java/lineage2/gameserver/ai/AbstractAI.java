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

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AbstractAI extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	protected static final Logger _log = LoggerFactory.getLogger(AbstractAI.class);
	/**
	 * Field _actor.
	 */
	protected final Creature _actor;
	/**
	 * Field _attackTarget.
	 */
	private HardReference<? extends Creature> _attackTarget = HardReferences.emptyRef();
	/**
	 * Field _intention.
	 */
	private CtrlIntention _intention = CtrlIntention.AI_INTENTION_IDLE;
	
	/**
	 * Constructor for AbstractAI.
	 * @param actor Creature
	 */
	protected AbstractAI(Creature actor)
	{
		_actor = actor;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
	}
	
	/**
	 * Method changeIntention.
	 * @param intention CtrlIntention
	 * @param arg0 Object
	 * @param arg1 Object
	 */
	public void changeIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		_intention = intention;
		if ((intention != CtrlIntention.AI_INTENTION_CAST) && (intention != CtrlIntention.AI_INTENTION_ATTACK))
		{
			setAttackTarget(null);
		}
	}
	
	/**
	 * Method setIntention.
	 * @param intention CtrlIntention
	 */
	public final void setIntention(CtrlIntention intention)
	{
		setIntention(intention, null, null);
	}
	
	/**
	 * Method setIntention.
	 * @param intention CtrlIntention
	 * @param arg0 Object
	 */
	public final void setIntention(CtrlIntention intention, Object arg0)
	{
		setIntention(intention, arg0, null);
	}
	
	/**
	 * Method setIntention.
	 * @param intention CtrlIntention
	 * @param arg0 Object
	 * @param arg1 Object
	 */
	public void setIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		if ((intention != CtrlIntention.AI_INTENTION_CAST) && (intention != CtrlIntention.AI_INTENTION_ATTACK))
		{
			setAttackTarget(null);
		}
		Creature actor = getActor();
		if (!actor.isVisible())
		{
			if (_intention == CtrlIntention.AI_INTENTION_IDLE)
			{
				return;
			}
			intention = CtrlIntention.AI_INTENTION_IDLE;
		}
		actor.getListeners().onAiIntention(intention, arg0, arg1);
		switch (intention)
		{
			case AI_INTENTION_IDLE:
				onIntentionIdle();
				break;
			case AI_INTENTION_ACTIVE:
				onIntentionActive();
				break;
			case AI_INTENTION_REST:
				onIntentionRest();
				break;
			case AI_INTENTION_ATTACK:
				onIntentionAttack((Creature) arg0);
				break;
			case AI_INTENTION_CAST:
				onIntentionCast((Skill) arg0, (Creature) arg1);
				break;
			case AI_INTENTION_PICK_UP:
				onIntentionPickUp((GameObject) arg0);
				break;
			case AI_INTENTION_INTERACT:
				onIntentionInteract((GameObject) arg0);
				break;
			case AI_INTENTION_FOLLOW:
				onIntentionFollow((Creature) arg0, (Integer) arg1);
				break;
			case AI_INTENTION_COUPLE_ACTION:
				onIntentionCoupleAction((Player) arg0, (Integer) arg1);
				break;
		}
	}
	
	/**
	 * Method notifyEvent.
	 * @param evt CtrlEvent
	 */
	public final void notifyEvent(CtrlEvent evt)
	{
		notifyEvent(evt, new Object[] {});
	}
	
	/**
	 * Method notifyEvent.
	 * @param evt CtrlEvent
	 * @param arg0 Object
	 */
	public final void notifyEvent(CtrlEvent evt, Object arg0)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			null,
			null
		});
	}
	
	/**
	 * Method notifyEvent.
	 * @param evt CtrlEvent
	 * @param arg0 Object
	 * @param arg1 Object
	 */
	public final void notifyEvent(CtrlEvent evt, Object arg0, Object arg1)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			arg1,
			null
		});
	}
	
	/**
	 * Method notifyEvent.
	 * @param evt CtrlEvent
	 * @param arg0 Object
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	public final void notifyEvent(CtrlEvent evt, Object arg0, Object arg1, Object arg2)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			arg1,
			arg2
		});
	}
	
	/**
	 * Method notifyEvent.
	 * @param evt CtrlEvent
	 * @param args Object[]
	 */
	public void notifyEvent(CtrlEvent evt, Object[] args)
	{
		Creature actor = getActor();
		if ((actor == null) || !actor.isVisible())
		{
			return;
		}
		actor.getListeners().onAiEvent(evt, args);
		switch (evt)
		{
			case EVT_THINK:
				onEvtThink();
				break;
			case EVT_ATTACKED:
				onEvtAttacked((Creature) args[0], ((Number) args[1]).intValue());
				break;
			case EVT_CLAN_ATTACKED:
				onEvtClanAttacked((Creature) args[0], (Creature) args[1], ((Number) args[2]).intValue());
				break;
			case EVT_AGGRESSION:
				onEvtAggression((Creature) args[0], ((Number) args[1]).intValue());
				break;
			case EVT_READY_TO_ACT:
				onEvtReadyToAct();
				break;
			case EVT_ARRIVED:
				onEvtArrived();
				break;
			case EVT_ARRIVED_TARGET:
				onEvtArrivedTarget();
				break;
			case EVT_ARRIVED_BLOCKED:
				onEvtArrivedBlocked((Location) args[0]);
				break;
			case EVT_FORGET_OBJECT:
				onEvtForgetObject((GameObject) args[0]);
				break;
			case EVT_DEAD:
				onEvtDead((Creature) args[0]);
				break;
			case EVT_FAKE_DEATH:
				onEvtFakeDeath();
				break;
			case EVT_FINISH_CASTING:
				onEvtFinishCasting((Integer) args[0], (Boolean) args[1]);
				break;
			case EVT_SEE_SPELL:
				onEvtSeeSpell((Skill) args[0], (Creature) args[1]);
				break;
			case EVT_SPAWN:
				onEvtSpawn();
				break;
			case EVT_DESPAWN:
				onEvtDeSpawn();
				break;
			case EVT_TIMER:
				onEvtTimer(((Number) args[0]).intValue(), args[1], args[2]);
				break;
			case EVT_SCRIPT_EVENT:
				onEvtScriptEvent(args[0].toString(), args[1], args[2]);
				break;
			case EVT_MENU_SELECTED:
				onEvtMenuSelected((Player) args[0], ((Number) args[1]).intValue(), ((Number) args[2]).intValue());
				break;
			case EVT_KNOCK_DOWN:
				onEvtKnockDown();
				break;
			case EVT_TELEPORTED:
				onEvtTeleported();
				break;
		}
	}
	
	/**
	 * Method clientActionFailed.
	 */
	protected void clientActionFailed()
	{
		Creature actor = getActor();
		if ((actor != null) && actor.isPlayer())
		{
			actor.sendActionFailed();
		}
	}
	
	/**
	 * Method clientStopMoving.
	 * @param validate boolean
	 */
	public void clientStopMoving(boolean validate)
	{
		Creature actor = getActor();
		actor.stopMove(validate);
	}
	
	/**
	 * Method clientStopMoving.
	 */
	public void clientStopMoving()
	{
		Creature actor = getActor();
		actor.stopMove();
	}
	
	/**
	 * Method getActor.
	 * @return Creature
	 */
	public Creature getActor()
	{
		return _actor;
	}
	
	/**
	 * Method getIntention.
	 * @return CtrlIntention
	 */
	public CtrlIntention getIntention()
	{
		return _intention;
	}
	
	/**
	 * Method setAttackTarget.
	 * @param target Creature
	 */
	public void setAttackTarget(Creature target)
	{
		_attackTarget = target == null ? HardReferences.<Creature> emptyRef() : target.getRef();
	}
	
	/**
	 * Method getAttackTarget.
	 * @return Creature
	 */
	public Creature getAttackTarget()
	{
		return _attackTarget.get();
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	public boolean isGlobalAI()
	{
		return false;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " for " + getActor();
	}
	
	/**
	 * Method onIntentionIdle.
	 */
	protected abstract void onIntentionIdle();
	
	/**
	 * Method onIntentionActive.
	 */
	protected abstract void onIntentionActive();
	
	/**
	 * Method onIntentionRest.
	 */
	protected abstract void onIntentionRest();
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	protected abstract void onIntentionAttack(Creature target);
	
	/**
	 * Method onIntentionCast.
	 * @param skill Skill
	 * @param target Creature
	 */
	protected abstract void onIntentionCast(Skill skill, Creature target);
	
	/**
	 * Method onIntentionPickUp.
	 * @param item GameObject
	 */
	protected abstract void onIntentionPickUp(GameObject item);
	
	/**
	 * Method onIntentionInteract.
	 * @param object GameObject
	 */
	protected abstract void onIntentionInteract(GameObject object);
	
	/**
	 * Method onIntentionCoupleAction.
	 * @param player Player
	 * @param socialId Integer
	 */
	protected abstract void onIntentionCoupleAction(Player player, Integer socialId);
	
	/**
	 * Method onEvtThink.
	 */
	protected abstract void onEvtThink();
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	protected abstract void onEvtAttacked(Creature attacker, int damage);
	
	/**
	 * Method onEvtClanAttacked.
	 * @param attacked_member Creature
	 * @param attacker Creature
	 * @param damage int
	 */
	protected abstract void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage);
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	protected abstract void onEvtAggression(Creature target, int aggro);
	
	/**
	 * Method onEvtReadyToAct.
	 */
	protected abstract void onEvtReadyToAct();
	
	/**
	 * Method onEvtArrived.
	 */
	protected abstract void onEvtArrived();
	
	/**
	 * Method onEvtArrivedTarget.
	 */
	protected abstract void onEvtArrivedTarget();
	
	/**
	 * Method onEvtArrivedBlocked.
	 * @param blocked_at_pos Location
	 */
	protected abstract void onEvtArrivedBlocked(Location blocked_at_pos);
	
	/**
	 * Method onEvtForgetObject.
	 * @param object GameObject
	 */
	protected abstract void onEvtForgetObject(GameObject object);
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	protected abstract void onEvtDead(Creature killer);
	
	/**
	 * Method onEvtFakeDeath.
	 */
	protected abstract void onEvtFakeDeath();
	
	/**
	 * Method onEvtFinishCasting.
	 * @param skill_id int
	 * @param success boolean
	 */
	protected abstract void onEvtFinishCasting(int skill_id, boolean success);
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	protected abstract void onEvtSeeSpell(Skill skill, Creature caster);
	
	/**
	 * Method onEvtSpawn.
	 */
	protected abstract void onEvtSpawn();
	
	/**
	 * Method onEvtDeSpawn.
	 */
	public abstract void onEvtDeSpawn();
	
	/**
	 * Method onIntentionFollow.
	 * @param target Creature
	 * @param offset Integer
	 */
	protected abstract void onIntentionFollow(Creature target, Integer offset);
	
	/**
	 * Method onEvtTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	protected abstract void onEvtTimer(int timerId, Object arg1, Object arg2);
	
	/**
	 * Method onEvtScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	protected abstract void onEvtScriptEvent(String event, Object arg1, Object arg2);
	
	/**
	 * Method onEvtMenuSelected.
	 * @param player Player
	 * @param ask int
	 * @param reply int
	 */
	protected abstract void onEvtMenuSelected(Player player, int ask, int reply);
	
	/**
	 * Method onEvtKnockDown.
	 */
	protected abstract void onEvtKnockDown();
	
	/**
	 * Method onEvtTeleported.
	 */
	protected abstract void onEvtTeleported();
}

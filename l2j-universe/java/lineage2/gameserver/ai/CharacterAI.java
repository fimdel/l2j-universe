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

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.PlayableAI.nextAction;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Die;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterAI extends AbstractAI
{
	/**
	 * Constructor for CharacterAI.
	 * @param actor Creature
	 */
	public CharacterAI(Creature actor)
	{
		super(actor);
	}
	
	/**
	 * Method onIntentionIdle.
	 */
	@Override
	protected void onIntentionIdle()
	{
		clientStopMoving();
		changeIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
	}
	
	/**
	 * Method onIntentionActive.
	 */
	@Override
	protected void onIntentionActive()
	{
		clientStopMoving();
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
		onEvtThink();
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		setAttackTarget(target);
		clientStopMoving();
		changeIntention(CtrlIntention.AI_INTENTION_ATTACK, target, null);
		onEvtThink();
	}
	
	/**
	 * Method onIntentionCast.
	 * @param skill Skill
	 * @param target Creature
	 */
	@Override
	protected void onIntentionCast(Skill skill, Creature target)
	{
		setAttackTarget(target);
		changeIntention(CtrlIntention.AI_INTENTION_CAST, skill, target);
		onEvtThink();
	}
	
	/**
	 * Method onIntentionFollow.
	 * @param target Creature
	 * @param offset Integer
	 */
	@Override
	protected void onIntentionFollow(Creature target, Integer offset)
	{
		changeIntention(CtrlIntention.AI_INTENTION_FOLLOW, target, offset);
		onEvtThink();
	}
	
	/**
	 * Method onIntentionInteract.
	 * @param object GameObject
	 */
	@Override
	protected void onIntentionInteract(GameObject object)
	{
	}
	
	/**
	 * Method onIntentionPickUp.
	 * @param item GameObject
	 */
	@Override
	protected void onIntentionPickUp(GameObject item)
	{
	}
	
	/**
	 * Method onIntentionRest.
	 */
	@Override
	protected void onIntentionRest()
	{
	}
	
	/**
	 * Method onIntentionCoupleAction.
	 * @param player Player
	 * @param socialId Integer
	 */
	@Override
	protected void onIntentionCoupleAction(Player player, Integer socialId)
	{
	}
	
	/**
	 * Method onEvtArrivedBlocked.
	 * @param blocked_at_pos Location
	 */
	@Override
	protected void onEvtArrivedBlocked(Location blocked_at_pos)
	{
		Creature actor = getActor();
		if (actor.isPlayer())
		{
			Location loc = ((Player) actor).getLastServerPosition();
			if (loc != null)
			{
				actor.setLoc(loc, true);
			}
			actor.stopMove();
		}
		onEvtThink();
	}
	
	/**
	 * Method onEvtForgetObject.
	 * @param object GameObject
	 */
	@Override
	protected void onEvtForgetObject(GameObject object)
	{
		if (object == null)
		{
			return;
		}
		Creature actor = getActor();
		if (actor.isAttackingNow() && (getAttackTarget() == object))
		{
			actor.abortAttack(true, true);
		}
		if (actor.isCastingNow() && (getAttackTarget() == object))
		{
			actor.abortCast(true, true);
		}
		if (getAttackTarget() == object)
		{
			setAttackTarget(null);
		}
		if (actor.getTargetId() == object.getObjectId())
		{
			actor.setTarget(null);
		}
		if (actor.getFollowTarget() == object)
		{
			actor.setFollowTarget(null);
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		Creature actor = getActor();
		actor.abortAttack(true, true);
		actor.abortCast(true, true);
		actor.stopMove();
		actor.broadcastPacket(new Die(actor));
		setIntention(CtrlIntention.AI_INTENTION_IDLE);
	}
	
	/**
	 * Method onEvtFakeDeath.
	 */
	@Override
	protected void onEvtFakeDeath()
	{
		clientStopMoving();
		setIntention(CtrlIntention.AI_INTENTION_IDLE);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	/**
	 * Method onEvtClanAttacked.
	 * @param attacked_member Creature
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage)
	{
	}
	
	/**
	 * Method Attack.
	 * @param target GameObject
	 * @param forceUse boolean
	 * @param dontMove boolean
	 */
	public void Attack(GameObject target, boolean forceUse, boolean dontMove)
	{
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
	}
	
	/**
	 * Method Cast.
	 * @param skill Skill
	 * @param target Creature
	 */
	public void Cast(Skill skill, Creature target)
	{
		Cast(skill, target, false, false);
	}
	
	/**
	 * Method Cast.
	 * @param skill Skill
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 */
	public void Cast(Skill skill, Creature target, boolean forceUse, boolean dontMove)
	{
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
	}
	
	/**
	 * Method onEvtThink.
	 */
	@Override
	protected void onEvtThink()
	{
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
	
	/**
	 * Method onEvtFinishCasting.
	 * @param skill_id int
	 * @param success boolean
	 */
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
	}
	
	/**
	 * Method onEvtReadyToAct.
	 */
	@Override
	protected void onEvtReadyToAct()
	{
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
	}
	
	/**
	 * Method onEvtArrivedTarget.
	 */
	@Override
	protected void onEvtArrivedTarget()
	{
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
	}
	
	/**
	 * Method onEvtDeSpawn.
	 */
	@Override
	public void onEvtDeSpawn()
	{
	}
	
	/**
	 * Method stopAITask.
	 */
	public void stopAITask()
	{
	}
	
	/**
	 * Method startAITask.
	 */
	public void startAITask()
	{
	}
	
	/**
	 * Method setNextAction.
	 * @param action nextAction
	 * @param arg0 Object
	 * @param arg1 Object
	 * @param arg2 boolean
	 * @param arg3 boolean
	 */
	public void setNextAction(nextAction action, Object arg0, Object arg1, boolean arg2, boolean arg3)
	{
	}
	
	/**
	 * Method clearNextAction.
	 */
	public void clearNextAction()
	{
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return true;
	}
	
	/**
	 * Method onEvtTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
	}
	
	/**
	 * Method onEvtScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
	}
	
	/**
	 * Method onEvtMenuSelected.
	 * @param player Player
	 * @param ask int
	 * @param reply int
	 */
	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
	}
	
	/**
	 * Method onEvtKnockDown.
	 */
	@Override
	protected void onEvtKnockDown()
	{
	}
	
	/**
	 * Method onEvtTeleported.
	 */
	@Override
	protected void onEvtTeleported()
	{
	}
	
	/**
	 * Method addTimer.
	 * @param timerId int
	 * @param delay long
	 */
	protected void addTimer(int timerId, long delay)
	{
		addTimer(timerId, null, null, delay);
	}
	
	/**
	 * Method addTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param delay long
	 */
	protected void addTimer(int timerId, Object arg1, long delay)
	{
		addTimer(timerId, arg1, null, delay);
	}
	
	/**
	 * Method addTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param arg2 Object
	 * @param delay long
	 */
	protected void addTimer(int timerId, Object arg1, Object arg2, long delay)
	{
		ThreadPoolManager.getInstance().schedule(new Timer(timerId, arg1, arg2), delay);
	}
	
	/**
	 * @author Mobius
	 */
	protected class Timer extends RunnableImpl
	{
		/**
		 * Field _timerId.
		 */
		private final int _timerId;
		/**
		 * Field _arg1.
		 */
		private final Object _arg1;
		/**
		 * Field _arg2.
		 */
		private final Object _arg2;
		
		/**
		 * Constructor for Timer.
		 * @param timerId int
		 * @param arg1 Object
		 * @param arg2 Object
		 */
		public Timer(int timerId, Object arg1, Object arg2)
		{
			_timerId = timerId;
			_arg1 = arg1;
			_arg2 = arg2;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			notifyEvent(CtrlEvent.EVT_TIMER, _timerId, _arg1, _arg2);
		}
	}
	
	/**
	 * Method broadCastScriptEvent.
	 * @param event String
	 * @param radius int
	 */
	protected void broadCastScriptEvent(String event, int radius)
	{
		broadCastScriptEvent(event, null, null, radius);
	}
	
	/**
	 * Method broadCastScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param radius int
	 */
	protected void broadCastScriptEvent(String event, Object arg1, int radius)
	{
		broadCastScriptEvent(event, arg1, null, radius);
	}
	
	/**
	 * Method broadCastScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 * @param radius int
	 */
	protected void broadCastScriptEvent(String event, Object arg1, Object arg2, int radius)
	{
		List<NpcInstance> npcs = World.getAroundNpc(getActor(), radius, radius);
		for (NpcInstance npc : npcs)
		{
			npc.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, event, arg1, arg2);
		}
	}
}

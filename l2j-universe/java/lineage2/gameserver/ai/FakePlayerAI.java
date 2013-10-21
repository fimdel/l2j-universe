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

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.FakePlayer;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FakePlayerAI extends CharacterAI
{
	/**
	 * Field thinking.
	 */
	private volatile int thinking = 0;
	/**
	 * Field _intention_arg0.
	 */
	Object _intention_arg0 = null;
	/**
	 * Field _intention_arg1.
	 */
	Object _intention_arg1 = null;
	/**
	 * Field _followTask.
	 */
	ScheduledFuture<?> _followTask;
	
	/**
	 * Constructor for FakePlayerAI.
	 * @param actor FakePlayer
	 */
	public FakePlayerAI(FakePlayer actor)
	{
		super(actor);
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
	 * Method onEvtThink.
	 */
	@Override
	protected final void onEvtThink()
	{
		FakePlayer actor = getActor();
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
			switch (getIntention().ordinal())
			{
				case 1:
					break;
				case 2:
					thinkAttack(true);
					break;
				case 3:
					thinkFollow();
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			thinking -= 1;
		}
	}
	
	/**
	 * Method thinkFollow.
	 */
	protected void thinkFollow()
	{
		FakePlayer actor = getActor();
		Creature target = (Creature) _intention_arg0;
		Integer offset = (Integer) _intention_arg1;
		if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000.0D) || (offset == null))
		{
			clientActionFailed();
			return;
		}
		if (actor.isFollow && (actor.getFollowTarget() == target))
		{
			clientActionFailed();
			return;
		}
		if (actor.isInRange(target, offset.intValue() + 20) || actor.isMovementDisabled())
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
	 * Method thinkAttack.
	 * @param checkRange boolean
	 */
	protected void thinkAttack(boolean checkRange)
	{
		FakePlayer actor = getActor();
		Player player = actor.getPlayer();
		if (player == null)
		{
			setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			return;
		}
		if (actor.isActionsDisabled() || actor.isAttackingDisabled())
		{
			return;
		}
		Creature attack_target = getAttackTarget();
		if ((attack_target == null) || (attack_target.isDead()))
		{
			setIntention(CtrlIntention.AI_INTENTION_FOLLOW);
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
		if ((!canSee && (range > 200)) || (Math.abs(actor.getZ() - attack_target.getZ()) > 200))
		{
			setIntention(CtrlIntention.AI_INTENTION_FOLLOW);
			return;
		}
		range = (int) (range + actor.getMinDistance(attack_target));
		if (actor.isFakeDeath())
		{
			actor.breakFakeDeath();
		}
		if (actor.isInRangeZ(attack_target, range))
		{
			if (!canSee)
			{
				setIntention(CtrlIntention.AI_INTENTION_FOLLOW);
				return;
			}
			clientStopMoving(false);
			actor.doAttack(attack_target);
		}
	}
	
	/**
	 * Method getActor.
	 * @return FakePlayer
	 */
	@Override
	public FakePlayer getActor()
	{
		return (FakePlayer) super.getActor();
	}
	
	/**
	 * @author Mobius
	 */
	protected class ThinkFollow extends RunnableImpl
	{
		/**
		 * Constructor for ThinkFollow.
		 */
		protected ThinkFollow()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			FakePlayer actor = getActor();
			if (getIntention() != CtrlIntention.AI_INTENTION_FOLLOW)
			{
				return;
			}
			Creature target = (Creature) _intention_arg0;
			int offset = (_intention_arg1 instanceof Integer) ? ((Integer) _intention_arg1).intValue() : 0;
			if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000.0D))
			{
				setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				return;
			}
			Player player = actor.getPlayer();
			if ((player == null) || player.isLogoutStarted())
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
}

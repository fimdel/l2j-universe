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
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Guard;
import lineage2.gameserver.listener.actor.OnAttackListener;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class InfiltrationOfficer extends Guard implements OnAttackListener, OnMagicUseListener
{
	/**
	 * @author Mobius
	 */
	public enum State
	{
		/**
		 * Field AI_IDLE.
		 */
		AI_IDLE(0),
		/**
		 * Field AI_FOLLOW.
		 */
		AI_FOLLOW(1),
		/**
		 * Field AI_ATTACK_GENERATOR.
		 */
		AI_ATTACK_GENERATOR(2),
		/**
		 * Field AI_NEXT_STEP.
		 */
		AI_NEXT_STEP(3);
		/**
		 * Field _id.
		 */
		@SuppressWarnings("unused")
		private final int _id;
		
		/**
		 * Constructor for State.
		 * @param id int
		 */
		State(int id)
		{
			_id = id;
		}
	}
	
	/**
	 * Field GENERATOR. (value is 33216)
	 */
	private final static int GENERATOR = 33216;
	/**
	 * Field POINTS.
	 */
	private final static int[][] POINTS =
	{
		{
			-117032,
			212568,
			-8617
		},
		{
			-117896,
			214264,
			-8617
		},
		{
			-119208,
			213768,
			-8617
		},
	};
	/**
	 * Field configured.
	 */
	private boolean configured = false;
	/**
	 * Field _step.
	 */
	private short _step = 0;
	/**
	 * Field _state.
	 */
	private State _state = State.AI_IDLE;
	/**
	 * Field lastFollowPlayer.
	 */
	private long lastFollowPlayer = 0;
	/**
	 * Field attacksGenerator.
	 */
	private boolean attacksGenerator = false;
	/**
	 * Field lastOfficerSay.
	 */
	private long lastOfficerSay = 0;
	/**
	 * Field player.
	 */
	private Player player = null;
	
	/**
	 * Constructor for InfiltrationOfficer.
	 * @param actor NpcInstance
	 */
	public InfiltrationOfficer(NpcInstance actor)
	{
		super(actor);
		actor.setRunning();
	}
	
	/**
	 * Method config.
	 */
	private void config()
	{
		if (!configured)
		{
			getActor().getFollowTarget().addListener(this);
			configured = true;
		}
	}
	
	/**
	 * Method onAttack.
	 * @param actor Creature
	 * @param target Creature
	 * @see lineage2.gameserver.listener.actor.OnAttackListener#onAttack(Creature, Creature)
	 */
	@Override
	public void onAttack(Creature actor, Creature target)
	{
		if (isUnderState(State.AI_FOLLOW) && target.isMonster())
		{
			getActor().getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
	}
	
	/**
	 * Method onMagicUse.
	 * @param actor Creature
	 * @param skill Skill
	 * @param target Creature
	 * @param alt boolean
	 * @see lineage2.gameserver.listener.actor.OnMagicUseListener#onMagicUse(Creature, Skill, Creature, boolean)
	 */
	@Override
	public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
	{
		if (isUnderState(State.AI_FOLLOW) && target.isMonster())
		{
			getActor().getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
	}
	
	/**
	 * Method setState.
	 * @param state State
	 */
	public void setState(State state)
	{
		config();
		_state = state;
	}
	
	/**
	 * Method isUnderState.
	 * @param state State
	 * @return boolean
	 */
	public boolean isUnderState(State state)
	{
		return _state == state;
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtDeSpawn.
	 */
	@Override
	public void onEvtDeSpawn()
	{
		if (getActor().getFollowTarget() != null)
		{
			getActor().getFollowTarget().removeListener(this);
		}
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		super.thinkAttack();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (isUnderState(State.AI_IDLE))
		{
			return false;
		}
		final NpcInstance actor = getActor();
		if (player == null)
		{
			player = actor.getFollowTarget().getPlayer();
		}
		if (player == null)
		{
			return false;
		}
		actor.setRunning();
		if (isUnderState(State.AI_FOLLOW) && actor.getAggroList().isEmpty() && ((System.currentTimeMillis() - lastFollowPlayer) > 2000))
		{
			lastFollowPlayer = System.currentTimeMillis();
			actor.setFollowTarget(player);
			actor.moveToLocation(player.getLoc(), Rnd.get(200), true);
		}
		else if (isUnderState(State.AI_NEXT_STEP))
		{
			if (_step < POINTS.length)
			{
				actor.setFollowTarget(player);
				actor.moveToLocation(new Location(POINTS[_step][0], POINTS[_step][1], POINTS[_step][2]), 0, true);
				++_step;
			}
			setState(State.AI_IDLE);
		}
		else if (isUnderState(State.AI_ATTACK_GENERATOR))
		{
			if (!attacksGenerator)
			{
				actor.getAggroList().clear();
				actor.getAggroList().addDamageHate(actor.getReflection().getAllByNpcId(GENERATOR, true).get(0), 0, Integer.MAX_VALUE);
				setIntention(CtrlIntention.AI_INTENTION_ATTACK);
				attacksGenerator = true;
			}
			if ((System.currentTimeMillis() - lastOfficerSay) > 3000)
			{
				actor.broadcastPacket(new NpcSay(actor, ChatType.ALL, NpcString.DONT_COME_BACK_HERE));
				lastOfficerSay = System.currentTimeMillis();
			}
		}
		return false;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (getActor().getFollowTarget() != null)
		{
			getActor().getFollowTarget().sendPacket(new ExShowScreenMessage(NpcString.IF_TERAIN_DIES_MISSION_WILL_FAIL, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
		}
	}
}

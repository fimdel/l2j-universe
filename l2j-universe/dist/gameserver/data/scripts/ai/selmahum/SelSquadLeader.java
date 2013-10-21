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
package ai.selmahum;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SelSquadLeader extends Fighter
{
	/**
	 * Field isBusy.
	 */
	private boolean isBusy;
	/**
	 * Field isImmobilized.
	 */
	private boolean isImmobilized;
	/**
	 * Field busyTimeout.
	 */
	private long busyTimeout = 0;
	/**
	 * Field idleTimeout.
	 */
	private long idleTimeout = 0;
	/**
	 * Field phrase.
	 */
	private static final NpcString[] phrase =
	{
		NpcString.COME_AND_EAT,
		NpcString.LOOKS_DELICIOUS,
		NpcString.LETS_GO_EAT
	};
	/**
	 * Field NPC_ID_FIRE.
	 */
	private static final int NPC_ID_FIRE = 18927;
	/**
	 * Field NPC_ID_FIRE_FEED.
	 */
	private static final int NPC_ID_FIRE_FEED = 18933;
	
	/**
	 * Constructor for SelSquadLeader.
	 * @param actor NpcInstance
	 */
	public SelSquadLeader(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 6000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (!isBusy)
		{
			if (System.currentTimeMillis() > idleTimeout)
			{
				for (NpcInstance npc : actor.getAroundNpc(600, 300))
				{
					if ((npc.getNpcId() == NPC_ID_FIRE_FEED) && GeoEngine.canSeeTarget(actor, npc, false))
					{
						isBusy = true;
						actor.setRunning();
						actor.setNpcState(1);
						busyTimeout = System.currentTimeMillis() + ((60 + Rnd.get(15)) * 1000L);
						addTaskMove(Location.findPointToStay(npc, 50, 150), true);
						if (Rnd.chance(40))
						{
							Functions.npcSay(actor, phrase[Rnd.get(2)]);
						}
					}
					else if ((npc.getNpcId() == NPC_ID_FIRE) && (npc.getNpcState() == 1) && GeoEngine.canSeeTarget(actor, npc, false))
					{
						isBusy = true;
						actor.setNpcState(2);
						busyTimeout = System.currentTimeMillis() + ((60 + Rnd.get(60)) * 1000L);
						addTaskMove(Location.findPointToStay(npc, 50, 150), true);
					}
				}
			}
		}
		else
		{
			if (System.currentTimeMillis() > busyTimeout)
			{
				wakeUp();
				actor.setWalking();
				addTaskMove(actor.getSpawnedLoc(), true);
				return true;
			}
		}
		if (isImmobilized)
		{
			return true;
		}
		return super.thinkActive();
	}
	
	/**
	 * Method wakeUp.
	 */
	private void wakeUp()
	{
		final NpcInstance actor = getActor();
		if (isBusy)
		{
			isBusy = false;
			busyTimeout = 0;
			idleTimeout = System.currentTimeMillis() + (Rnd.get(3 * 60, 5 * 60) * 1000L);
			if (isImmobilized)
			{
				isImmobilized = false;
				actor.stopImmobilized();
				actor.setNpcState(3);
				actor.setRHandId(0);
				actor.broadcastCharInfo();
			}
		}
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		final NpcInstance actor = getActor();
		super.onEvtArrived();
		if (isBusy)
		{
			isImmobilized = true;
			actor.startImmobilized();
			actor.setRHandId(15280);
			actor.broadcastCharInfo();
		}
	}
	
	/**
	 * Method onIntentionActive.
	 */
	@Override
	protected void onIntentionActive()
	{
		idleTimeout = System.currentTimeMillis() + (Rnd.get(60, 5 * 60) * 1000L);
		super.onIntentionActive();
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		wakeUp();
		super.onIntentionAttack(target);
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		// empty method
	}
}

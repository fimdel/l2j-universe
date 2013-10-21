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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SelChef extends Fighter
{
	/**
	 * Field targetLoc.
	 */
	private Location targetLoc;
	/**
	 * Field wait_timeout.
	 */
	private long wait_timeout = 0;
	
	/**
	 * Constructor for SelChef.
	 * @param actor NpcInstance
	 */
	public SelChef(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = Integer.MAX_VALUE;
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		getActor().getMinionList().spawnMinions();
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
		if (_def_think)
		{
			doTask();
			return true;
		}
		if (System.currentTimeMillis() > wait_timeout)
		{
			wait_timeout = System.currentTimeMillis() + 2000;
			actor.setWalking();
			targetLoc = findFirePlace(actor);
			addTaskMove(targetLoc, true);
			doTask();
			return true;
		}
		return false;
	}
	
	/**
	 * Method findFirePlace.
	 * @param actor NpcInstance
	 * @return Location
	 */
	private Location findFirePlace(NpcInstance actor)
	{
		Location loc = new Location();
		final List<NpcInstance> list = new ArrayList<>();
		for (NpcInstance npc : actor.getAroundNpc(3000, 600))
		{
			if ((npc.getNpcId() == 18927) && GeoEngine.canSeeTarget(actor, npc, false))
			{
				list.add(npc);
			}
		}
		if (!list.isEmpty())
		{
			loc = list.get(Rnd.get(list.size())).getLoc();
		}
		else
		{
			loc = Location.findPointToStay(actor, 1000, 1500);
		}
		return loc;
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
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
}

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
package ai.hermunkus_message;

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Rogin_19193 extends Dwarvs
{
	/**
	 * Field BRONK_ID. (value is 19192)
	 */
	private static final int BRONK_ID = 19192;
	/**
	 * Field MOVE_LOC.
	 */
	private static final int[][] MOVE_LOC =
	{
		{
			116400,
			-183069,
			-1600
		}
	};
	/**
	 * Field currentPoint.
	 */
	private int currentPoint;
	
	/**
	 * Constructor for Rogin_19193.
	 * @param actor NpcInstance
	 */
	public Rogin_19193(NpcInstance actor)
	{
		super(actor);
		currentPoint = 0;
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		getActor().setRunning();
		addTaskMove(MOVE_LOC[currentPoint][0], MOVE_LOC[currentPoint][1], MOVE_LOC[currentPoint][2], true);
		doTask();
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		super.onEvtArrived();
		if (currentPoint == 0)
		{
			addTimer(1, 1600);
			currentPoint++;
		}
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
		if (event.equalsIgnoreCase("ROGIN_1"))
		{
			addTimer(3, 1600);
		}
		else if (event.equalsIgnoreCase("SHOUT_ALL"))
		{
			Functions.npcSayInRange(getActor(), 1500, NpcString.CHIEF_);
		}
		else
		{
			super.onEvtScriptEvent(event, arg1, arg2);
		}
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
		super.onEvtTimer(timerId, arg1, arg2);
		final Reflection r = getActor().getReflection();
		if (r.equals(ReflectionManager.DEFAULT))
		{
			return;
		}
		List<NpcInstance> list;
		switch (timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcString.CHIEF_REPORTING_IN);
				addTimer(2, 1600);
				break;
			case 2:
				Functions.npcSayInRange(getActor(), 1500, NpcString.ENEMIES_ARE_APPROACHING_FROM_THE_SOUTH);
				list = r.getAllByNpcId(BRONK_ID, true);
				if (list.size() > 0)
				{
					final NpcInstance bronk = list.get(0);
					bronk.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "BRONK_1");
				}
				break;
			case 3:
				Functions.npcSayInRange(getActor(), 1500, NpcString.THE_ELDERS_HAVENT_BEEN_MOVED_TO_SAFETY);
				addTimer(4, 1600);
				break;
			case 4:
				Functions.npcSayInRange(getActor(), 1500, NpcString.MANY_RESIDENTS_STILL_HAVENT_LEFT_THEIR_HOMES);
				list = r.getAllByNpcId(BRONK_ID, true);
				if (list.size() > 0)
				{
					final NpcInstance bronk = list.get(0);
					bronk.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "BRONK_2");
				}
				break;
		}
	}
}

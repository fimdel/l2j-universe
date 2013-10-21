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
public class Torocco_19198 extends Dwarvs
{
	/**
	 * Field ROGIN_ID. (value is 19193)
	 */
	private static final int ROGIN_ID = 19193;
	
	/**
	 * Constructor for Torocco_19198.
	 * @param actor NpcInstance
	 */
	public Torocco_19198(NpcInstance actor)
	{
		super(actor);
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
		super.onEvtScriptEvent(event, arg1, arg2);
		if (event.equalsIgnoreCase("TOROCCO_1"))
		{
			addTimer(1, 1200);
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
		switch (timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcString.ROGIN_IM_HERE);
				final List<NpcInstance> list = r.getAllByNpcId(ROGIN_ID, true);
				if (list.size() > 0)
				{
					final NpcInstance rogin = list.get(0);
					rogin.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "ROGIN_1");
				}
				break;
		}
	}
}

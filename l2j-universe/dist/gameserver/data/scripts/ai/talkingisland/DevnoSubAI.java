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
package ai.talkingisland;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DevnoSubAI extends DefaultAI
{
	/**
	 * Field _points.
	 */
	protected Location[] _points;
	/**
	 * Field _lastPoint.
	 */
	private int _lastPoint = 0;
	
	/**
	 * Constructor for DevnoSubAI.
	 * @param actor NpcInstance
	 */
	public DevnoSubAI(NpcInstance actor)
	{
		super(actor);
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
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			startMoveTask();
		}
		return true;
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		if (Rnd.chance(30))
		{
			sayRndMsg();
		}
		super.onEvtArrived();
	}
	
	/**
	 * Method startMoveTask.
	 */
	private void startMoveTask()
	{
		_lastPoint++;
		if (_lastPoint >= _points.length)
		{
			_lastPoint = 0;
		}
		addTaskMove(_points[_lastPoint], false);
		doTask();
	}
	
	/**
	 * Method sayRndMsg.
	 */
	private void sayRndMsg()
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		NpcString ns;
		switch (Rnd.get(6))
		{
			case 1:
				ns = NpcString.ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE;
				break;
			case 2:
				ns = NpcString.ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE;
				break;
			case 3:
				ns = NpcString.ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE;
				break;
			case 4:
				ns = NpcString.ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE;
				break;
			case 5:
				ns = NpcString.ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE;
				break;
			default:
				ns = NpcString.ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE;
				break;
		}
		Functions.npcSay(actor, ns);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}

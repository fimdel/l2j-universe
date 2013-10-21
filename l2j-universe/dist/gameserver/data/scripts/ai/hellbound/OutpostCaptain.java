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
package ai.hellbound;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OutpostCaptain extends Fighter
{
	/**
	 * Field _attacked.
	 */
	private boolean _attacked = false;
	
	/**
	 * Constructor for OutpostCaptain.
	 * @param actor NpcInstance
	 */
	public OutpostCaptain(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if ((attacker == null) || (attacker.getPlayer() == null))
		{
			return;
		}
		for (NpcInstance minion : World.getAroundNpc(getActor(), 3000, 2000))
		{
			if ((minion.getNpcId() == 22358) || (minion.getNpcId() == 22357))
			{
				minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
			}
		}
		if (!_attacked)
		{
			Functions.npcSay(getActor(), "Fool, you and your friends will die! Attack!");
			_attacked = true;
		}
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}

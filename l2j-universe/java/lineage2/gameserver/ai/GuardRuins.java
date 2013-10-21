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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardRuins extends Fighter
{
	/**
	 * Constructor for GuardRuins.
	 * @param actor NpcInstance
	 */
	public GuardRuins(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		List<NpcInstance> around = actor.getAroundNpc(500, 300);
		if ((around != null) && !around.isEmpty())
		{
			for (NpcInstance npc : around)
			{
				if (npc.isMonster() && (npc.getNpcId() != 19153) && (npc.getNpcId() != 19152))
				{
					actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 300);
				}
			}
		}
		return true;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if ((attacker == null) || attacker.isPlayable())
		{
			return;
		}
		super.onEvtAttacked(attacker, damage);
	}
}

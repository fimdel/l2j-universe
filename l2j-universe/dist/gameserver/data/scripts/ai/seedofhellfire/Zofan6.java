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
package ai.seedofhellfire;

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class Zofan6 extends Fighter
{
	public Zofan6(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if ((attacker == null) || (attacker.isPlayable()))
		{
			return;
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	public boolean checkAggression(Creature target)
	{
		if (target.isPlayable())
		{
			return false;
		}
		return super.checkAggression(target);
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		List<NpcInstance> around = actor.getAroundNpc(800, 500);
		if ((around != null) && (!around.isEmpty()))
		{
			for (NpcInstance npc : around)
			{
				if (npc.getNpcId() == 51007)
				{
					actor.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, npc, Integer.valueOf(500));
				}
			}
		}
		return true;
	}
}
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
package ai.freya;

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class JiniaKnight extends Fighter
{
	/**
	 * Constructor for JiniaKnight.
	 * @param actor NpcInstance
	 */
	public JiniaKnight(NpcInstance actor)
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
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		final List<NpcInstance> around = actor.getAroundNpc(4000, 300);
		if ((around != null) && !around.isEmpty())
		{
			for (NpcInstance npc : around)
			{
				if (npc.getNpcId() == 22767)
				{
					actor.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, npc, 300);
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
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		if (target.isPlayable())
		{
			return false;
		}
		return super.checkAggression(target);
	}
}

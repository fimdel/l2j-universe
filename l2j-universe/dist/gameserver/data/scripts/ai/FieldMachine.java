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

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FieldMachine extends DefaultAI
{
	/**
	 * Field _lastAction.
	 */
	private long _lastAction;
	
	/**
	 * Constructor for FieldMachine.
	 * @param actor NpcInstance
	 */
	public FieldMachine(NpcInstance actor)
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
		final NpcInstance actor = getActor();
		if ((attacker == null) || (attacker.getPlayer() == null))
		{
			return;
		}
		if ((System.currentTimeMillis() - _lastAction) > 15000)
		{
			_lastAction = System.currentTimeMillis();
			Functions.npcSayCustomMessage(actor, "scripts.ai.FieldMachine." + actor.getNpcId());
			final List<NpcInstance> around = actor.getAroundNpc(1500, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if (npc.isMonster() && (npc.getNpcId() >= 22656) && (npc.getNpcId() <= 22659))
					{
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
					}
				}
			}
		}
	}
}

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
package ai.Heine.FieldOfSelenceAndWhispers;

import java.util.List;

import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BrazierOfPurity extends CharacterAI
{
	/**
	 * Field _firstTimeAttacked.
	 */
	private boolean _firstTimeAttacked = true;
	
	/**
	 * Constructor for BrazierOfPurity.
	 * @param actor Creature
	 */
	public BrazierOfPurity(Creature actor)
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
		final NpcInstance actor = (NpcInstance) getActor();
		if ((actor == null) || actor.isDead())
		{
			return;
		}
		if (_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			Functions.npcSay(actor, NpcString.THE_PURIFICATION_FIELD_IS_BEING_ATTACKED_GUARDIAN_SPIRITS_PROTECT_THE_MAGIC_FORCE, ChatType.ALL, 15000);
			final List<NpcInstance> around = actor.getAroundNpc(1500, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if ((npc.isMonster() && (npc.getNpcId() == 22658)) || (npc.getNpcId() == 22659))
					{
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
					}
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
}

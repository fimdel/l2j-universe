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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ContaminatedMucrokian extends Fighter
{
	/**
	 * Constructor for ContaminatedMucrokian.
	 * @param actor NpcInstance
	 */
	public ContaminatedMucrokian(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
		{
			Functions.npcSay(actor, NpcString.NAIA_WAGANAGEL_PEUTAGUN, ChatType.ALL, 5000);
		}
		super.onIntentionAttack(target);
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
		if ((actor != null) && !actor.isDead())
		{
			if (attacker != null)
			{
				if ((attacker.getNpcId() >= 22656) && (attacker.getNpcId() <= 22659))
				{
					if (Rnd.chance(25))
					{
						final Location pos = Location.findPointToStay(actor, 200, 300);
						if (GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), pos.x, pos.y, pos.z, actor.getGeoIndex()))
						{
							actor.setRunning();
						}
						addTaskMove(pos, false);
					}
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
}

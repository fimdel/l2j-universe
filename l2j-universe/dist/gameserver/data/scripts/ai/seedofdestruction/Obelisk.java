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
package ai.seedofdestruction;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Obelisk extends DefaultAI
{
	/**
	 * Field MOBS.
	 */
	private static final int[] MOBS =
	{
		22541,
		22544,
		22543
	};
	/**
	 * Field _firstTimeAttacked.
	 */
	private boolean _firstTimeAttacked = true;
	
	/**
	 * Constructor for Obelisk.
	 * @param actor NpcInstance
	 */
	public Obelisk(NpcInstance actor)
	{
		super(actor);
		actor.block();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		final NpcInstance actor = getActor();
		actor.broadcastPacket(new ExShowScreenMessage("Obelisk has collapsed. Don't let the enemies jump around wildly anymore!!!!", 3000, ScreenMessageAlign.MIDDLE_CENTER, false));
		actor.stopDecay();
		for (NpcInstance n : actor.getReflection().getNpcs())
		{
			if (n.getNpcId() == 18777)
			{
				n.stopDamageBlocked();
			}
		}
		super.onEvtDead(killer);
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
		if (_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			for (int i = 0; i < 8; i++)
			{
				for (int mobId : MOBS)
				{
					NpcInstance npc = actor.getReflection().addSpawnWithoutRespawn(mobId, Location.findPointToStay(actor, 400, 1000), 0);
					Creature randomHated = actor.getAggroList().getRandomHated();
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, (randomHated != null) ? randomHated : attacker, Rnd.get(1, 100));
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
}

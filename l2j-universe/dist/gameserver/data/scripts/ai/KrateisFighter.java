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

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.KrateisCubeEvent;
import lineage2.gameserver.model.entity.events.objects.KrateisCubePlayerObject;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KrateisFighter extends Fighter
{
	/**
	 * Constructor for KrateisFighter.
	 * @param actor NpcInstance
	 */
	public KrateisFighter(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		final Player player = killer.getPlayer();
		if (player == null)
		{
			return;
		}
		final KrateisCubeEvent cubeEvent = getActor().getEvent(KrateisCubeEvent.class);
		if (cubeEvent == null)
		{
			return;
		}
		final KrateisCubePlayerObject particlePlayer = cubeEvent.getParticlePlayer(player);
		particlePlayer.setPoints(particlePlayer.getPoints() + 3);
		cubeEvent.updatePoints(particlePlayer);
	}
}

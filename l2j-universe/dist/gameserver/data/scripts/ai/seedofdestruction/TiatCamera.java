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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TiatCamera extends DefaultAI
{
	/**
	 * Field _players.
	 */
	private final List<Player> _players = new ArrayList<>();
	
	/**
	 * Constructor for TiatCamera.
	 * @param actor NpcInstance
	 */
	public TiatCamera(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
		actor.startDamageBlocked();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		for (Player p : World.getAroundPlayers(actor, 300, 300))
		{
			if (!_players.contains(p))
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_TIAT_OPENING);
				_players.add(p);
			}
		}
		return true;
	}
}

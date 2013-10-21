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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.RaceManagerInstance;
import lineage2.gameserver.network.serverpackets.MonRaceInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RaceManager extends DefaultAI
{
	/**
	 * Field thinking.
	 */
	private boolean thinking = false;
	/**
	 * Field _knownPlayers.
	 */
	private List<Player> _knownPlayers = new ArrayList<>();
	
	/**
	 * Constructor for RaceManager.
	 * @param actor NpcInstance
	 */
	public RaceManager(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 5000;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		onEvtThink();
	}
	
	/**
	 * Method onEvtThink.
	 */
	@Override
	protected void onEvtThink()
	{
		RaceManagerInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		MonRaceInfo packet = actor.getPacket();
		if (packet == null)
		{
			return;
		}
		synchronized (this)
		{
			if (thinking)
			{
				return;
			}
			thinking = true;
		}
		try
		{
			List<Player> newPlayers = new ArrayList<>();
			for (Player player : World.getAroundPlayers(actor, 1200, 200))
			{
				if (player == null)
				{
					continue;
				}
				newPlayers.add(player);
				if (!_knownPlayers.contains(player))
				{
					player.sendPacket(packet);
				}
				_knownPlayers.remove(player);
			}
			for (Player player : _knownPlayers)
			{
				actor.removeKnownPlayer(player);
			}
			_knownPlayers = newPlayers;
		}
		finally
		{
			thinking = false;
		}
	}
	
	/**
	 * Method getActor.
	 * @return RaceManagerInstance
	 */
	@Override
	public RaceManagerInstance getActor()
	{
		return (RaceManagerInstance) super.getActor();
	}
}

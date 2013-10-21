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
package npc.model;

import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.DelusionChamberManager;
import lineage2.gameserver.instancemanager.DelusionChamberManager.DelusionChamberRoom;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.DelusionChamber;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DelustionGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for DelustionGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public DelustionGatekeeperInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.startsWith("enterDC"))
		{
			int izId = Integer.parseInt(command.substring(8));
			int type = izId - 120;
			Map<Integer, DelusionChamberRoom> rooms = DelusionChamberManager.getInstance().getRooms(type);
			if (rooms == null)
			{
				player.sendPacket(Msg.SYSTEM_ERROR);
				return;
			}
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(izId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(izId))
			{
				Party party = player.getParty();
				if (party != null)
				{
					new DelusionChamber(party, type, Rnd.get(1, rooms.size() - 1));
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

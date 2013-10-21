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
package npc.model.events;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.KrateisCubeEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KrateisCubeMatchManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for KrateisCubeMatchManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public KrateisCubeMatchManagerInstance(int objectId, NpcTemplate template)
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
		KrateisCubeEvent cubeEvent = player.getEvent(KrateisCubeEvent.class);
		if (cubeEvent == null)
		{
			return;
		}
		if (command.startsWith("KrateiEnter"))
		{
			if (!cubeEvent.isInProgress())
			{
				showChatWindow(player, 1);
			}
			else
			{
				List<Location> locs = cubeEvent.getObjects(KrateisCubeEvent.TELEPORT_LOCS);
				player.teleToLocation(Rnd.get(locs), ReflectionManager.DEFAULT);
			}
		}
		else if (command.startsWith("KrateiExit"))
		{
			cubeEvent.exitCube(player, true);
		}
	}
}

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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import bosses.ValakasManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ValakasGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field FLOATING_STONE. (value is 7267)
	 */
	private static final int FLOATING_STONE = 7267;
	/**
	 * Field TELEPORT_POSITION1.
	 */
	private static final Location TELEPORT_POSITION1 = new Location(183831, -115457, -3296);
	
	/**
	 * Constructor for ValakasGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ValakasGatekeeperInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_passage"))
		{
			if (!ValakasManager.isEnableEnterToLair())
			{
				player.sendMessage("Valakas is now reborning and there's no way to enter the hall now.");
				return;
			}
			if (player.getInventory().getCountOf(FLOATING_STONE) < 1)
			{
				player.sendMessage("In order to enter the Hall of Flames you should carry at least one Flotaing Stone");
				return;
			}
			player.teleToLocation(TELEPORT_POSITION1);
			return;
		}
		else if (command.equalsIgnoreCase("request_valakas"))
		{
			ValakasManager.enterTheLair(player);
			return;
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

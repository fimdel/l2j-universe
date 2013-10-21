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

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import bosses.BaiumManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class BaiumGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field Baium. (value is 29020)
	 */
	private static final int Baium = 29020;
	/**
	 * Field BaiumNpc. (value is 29025)
	 */
	private static final int BaiumNpc = 29025;
	/**
	 * Field BloodedFabric. (value is 4295)
	 */
	private static final int BloodedFabric = 4295;
	/**
	 * Field TELEPORT_POSITION.
	 */
	private static final Location TELEPORT_POSITION = new Location(113100, 14500, 10077);
	
	/**
	 * Constructor for BaiumGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BaiumGatekeeperInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("request_entrance"))
		{
			if (ItemFunctions.getItemCount(player, BloodedFabric) > 0)
			{
				NpcInstance baiumBoss = GameObjectsStorage.getByNpcId(Baium);
				if (baiumBoss != null)
				{
					showChatWindow(player, "default/31862-1.htm");
					return;
				}
				NpcInstance baiumNpc = GameObjectsStorage.getByNpcId(BaiumNpc);
				if (baiumNpc == null)
				{
					showChatWindow(player, "default/31862-2.htm");
					return;
				}
				ItemFunctions.removeItem(player, BloodedFabric, 1, true);
				player.setVar("baiumPermission", "granted", -1);
				player.teleToLocation(TELEPORT_POSITION);
			}
			else
			{
				showChatWindow(player, "default/31862-3.htm");
			}
		}
		else if (command.startsWith("request_wakeup"))
		{
			if ((player.getVar("baiumPermission") == null) || !player.getVar("baiumPermission").equalsIgnoreCase("granted"))
			{
				showChatWindow(player, "default/29025-1.htm");
				return;
			}
			if (isBusy())
			{
				showChatWindow(player, "default/29025-2.htm");
			}
			setBusy(true);
			Functions.npcSay(this, "You called my name! Now you gonna die!");
			BaiumManager.spawnBaium(this, player);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

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
import lineage2.gameserver.utils.ItemFunctions;
import bosses.SailrenManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SairlenGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field GAZKH. (value is 8784)
	 */
	private static final int GAZKH = 8784;
	
	/**
	 * Constructor for SairlenGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SairlenGatekeeperInstance(int objectId, NpcTemplate template)
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
			if (player.getLevel() < 75)
			{
				showChatWindow(player, "default/32109-3.htm");
			}
			else if (ItemFunctions.getItemCount(player, GAZKH) > 0)
			{
				int check = SailrenManager.canIntoSailrenLair(player);
				if ((check == 1) || (check == 2))
				{
					showChatWindow(player, "default/32109-5.htm");
				}
				else if (check == 3)
				{
					showChatWindow(player, "default/32109-4.htm");
				}
				else if (check == 4)
				{
					showChatWindow(player, "default/32109-1.htm");
				}
				else if (check == 0)
				{
					ItemFunctions.removeItem(player, GAZKH, 1, true);
					SailrenManager.setSailrenSpawnTask();
					SailrenManager.entryToSailrenLair(player);
				}
			}
			else
			{
				showChatWindow(player, "default/32109-2.htm");
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

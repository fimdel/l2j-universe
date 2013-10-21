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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class KeplonInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for KeplonInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public KeplonInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("buygreen"))
		{
			if (ItemFunctions.removeItem(player, 57, 10000, true) >= 10000)
			{
				ItemFunctions.addItem(player, 4401, 1, true);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		else if (command.startsWith("buyblue"))
		{
			if (ItemFunctions.removeItem(player, 57, 10000, true) >= 10000)
			{
				ItemFunctions.addItem(player, 4402, 1, true);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		else if (command.startsWith("buyred"))
		{
			if (ItemFunctions.removeItem(player, 57, 10000, true) >= 10000)
			{
				ItemFunctions.addItem(player, 4403, 1, true);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

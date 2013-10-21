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

import instances.IsthinaNormal;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RumieseNpcInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is 5984176213940365077)
	 */
	private static final long serialVersionUID = 5984176213940365077L;
	/**
	 * Field normalIsthinaInstId. (value is 169)
	 */
	private static final int normalIsthinaInstId = 169;
	/**
	 * Field hardIsthinaInstId. (value is 170)
	 */
	private static final int hardIsthinaInstId = 170;
	
	/**
	 * Constructor for RumieseNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RumieseNpcInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_normalisthina"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(normalIsthinaInstId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(normalIsthinaInstId))
			{
				ReflectionUtils.enterReflection(player, new IsthinaNormal(), normalIsthinaInstId);
			}
		}
		else if (command.equalsIgnoreCase("request_hardisthina"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(hardIsthinaInstId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(hardIsthinaInstId))
			{
				ReflectionUtils.enterReflection(player, new IsthinaNormal(), hardIsthinaInstId);
			}
		}
		else if (command.equalsIgnoreCase("request_takemyprize"))
		{
		}
		else if (command.equalsIgnoreCase("request_Device"))
		{
			if (ItemFunctions.getItemCount(player, 17608) > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no.htm");
				return;
			}
			Functions.addItem(player, 17608, 1);
			showChatWindow(player, "default/" + getNpcId() + "-ok.htm");
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

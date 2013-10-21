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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KegorNpcInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for KegorNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public KegorNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String htmlpath = null;
		if (getReflection().isDefault())
		{
			htmlpath = "default/32761-default.htm";
		}
		else
		{
			htmlpath = "default/32761.htm";
		}
		return htmlpath;
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
		if (command.equalsIgnoreCase("request_stone"))
		{
			if ((player.getInventory().getCountOf(15469) == 0) && (player.getInventory().getCountOf(15470) == 0))
			{
				Functions.addItem(player, 15469, 1);
			}
			else
			{
				player.sendMessage("You can't take more than 1 Frozen Core.");
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

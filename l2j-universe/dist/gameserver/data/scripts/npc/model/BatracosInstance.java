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
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class BatracosInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field urogosIzId. (value is 505)
	 */
	private static final int urogosIzId = 505;
	
	/**
	 * Constructor for BatracosInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BatracosInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if (val == 0)
		{
			String htmlpath = null;
			if (getReflection().isDefault())
			{
				htmlpath = "default/32740.htm";
			}
			else
			{
				htmlpath = "default/32740-4.htm";
			}
			player.sendPacket(new NpcHtmlMessage(player, this, htmlpath, val));
		}
		else
		{
			super.showChatWindow(player, val);
		}
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
		if (command.equalsIgnoreCase("request_seer"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(urogosIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(urogosIzId))
			{
				ReflectionUtils.enterReflection(player, urogosIzId);
			}
		}
		else if (command.equalsIgnoreCase("leave"))
		{
			if (!getReflection().isDefault())
			{
				getReflection().collapse();
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

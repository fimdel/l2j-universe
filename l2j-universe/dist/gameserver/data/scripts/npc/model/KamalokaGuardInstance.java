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

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class KamalokaGuardInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for KamalokaGuardInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public KamalokaGuardInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("kamaloka"))
		{
			int val = Integer.parseInt(command.substring(9));
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(val))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(val))
			{
				ReflectionUtils.enterReflection(player, val);
			}
		}
		else if (command.startsWith("escape"))
		{
			if ((player.getParty() == null) || !player.getParty().isLeader(player))
			{
				showChatWindow(player, "not_party_leader.htm");
				return;
			}
			player.getReflection().collapse();
		}
		else if (command.startsWith("return"))
		{
			Reflection r = player.getReflection();
			if (r.getReturnLoc() != null)
			{
				player.teleToLocation(r.getReturnLoc(), ReflectionManager.DEFAULT);
			}
			else
			{
				player.setReflection(ReflectionManager.DEFAULT);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
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
		String pom;
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return "instance/kamaloka/" + pom + ".htm";
	}
}

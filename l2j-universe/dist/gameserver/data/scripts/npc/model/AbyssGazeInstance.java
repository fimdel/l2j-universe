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

import instances.HeartInfinityAttack;
import instances.HeartInfinityDefence;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AbyssGazeInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ekimusIzId. (value is 121)
	 */
	private static final int ekimusIzId = 121;
	/**
	 * Field hoidefIzId. (value is 122)
	 */
	private static final int hoidefIzId = 122;
	
	/**
	 * Constructor for AbyssGazeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AbyssGazeInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("request_permission"))
		{
			if ((SoIManager.getCurrentStage() == 2) || (SoIManager.getCurrentStage() == 5))
			{
				showChatWindow(player, "default/32540-2.htm");
				return;
			}
			else if ((SoIManager.getCurrentStage() == 3) && SoIManager.isSeedOpen())
			{
				showChatWindow(player, "default/32540-3.htm");
				return;
			}
			else
			{
				showChatWindow(player, "default/32540-1.htm");
				return;
			}
		}
		else if (command.equalsIgnoreCase("request_ekimus"))
		{
			if (SoIManager.getCurrentStage() == 2)
			{
				Reflection r = player.getActiveReflection();
				if (r != null)
				{
					if (player.canReenterInstance(ekimusIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(ekimusIzId))
				{
					ReflectionUtils.enterReflection(player, new HeartInfinityAttack(), ekimusIzId);
				}
			}
		}
		else if (command.equalsIgnoreCase("enter_seed"))
		{
			if (SoIManager.getCurrentStage() == 3)
			{
				SoIManager.teleportInSeed(player);
				return;
			}
		}
		else if (command.equalsIgnoreCase("hoi_defence"))
		{
			if (SoIManager.getCurrentStage() == 5)
			{
				Reflection r = player.getActiveReflection();
				if (r != null)
				{
					if (player.canReenterInstance(hoidefIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(hoidefIzId))
				{
					ReflectionUtils.enterReflection(player, new HeartInfinityDefence(), hoidefIzId);
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

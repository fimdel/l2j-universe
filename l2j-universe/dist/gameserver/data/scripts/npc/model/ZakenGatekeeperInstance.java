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

import instances.ZakenDay;
import instances.ZakenDay83;
import instances.ZakenNight;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ZakenGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field nightZakenIzId. (value is 114)
	 */
	private static final int nightZakenIzId = 114;
	/**
	 * Field dayZakenIzId. (value is 133)
	 */
	private static final int dayZakenIzId = 133;
	/**
	 * Field ultraZakenIzId. (value is 135)
	 */
	private static final int ultraZakenIzId = 135;
	
	/**
	 * Constructor for ZakenGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ZakenGatekeeperInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_nightzaken"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(nightZakenIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(nightZakenIzId))
			{
				ReflectionUtils.enterReflection(player, new ZakenNight(), nightZakenIzId);
			}
		}
		else if (command.equalsIgnoreCase("request_dayzaken"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(dayZakenIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(dayZakenIzId))
			{
				ReflectionUtils.enterReflection(player, new ZakenDay(), dayZakenIzId);
			}
		}
		else if (command.equalsIgnoreCase("request_ultrazaken"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(ultraZakenIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(ultraZakenIzId))
			{
				ReflectionUtils.enterReflection(player, new ZakenDay83(), ultraZakenIzId);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

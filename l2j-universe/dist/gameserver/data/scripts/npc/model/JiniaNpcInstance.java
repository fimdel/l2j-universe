/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model;

import instances.FreyaHard;
import instances.FreyaNormal;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class JiniaNpcInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field normalFreyaIzId. (value is 139)
	 */
	private static final int normalFreyaIzId = 139;
	/**
	 * Field extremeFreyaIzId. (value is 144)
	 */
	private static final int extremeFreyaIzId = 144;
	
	/**
	 * Constructor for JiniaNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public JiniaNpcInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_normalfreya"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(normalFreyaIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(normalFreyaIzId))
			{
				ReflectionUtils.enterReflection(player, new FreyaNormal(), normalFreyaIzId);
			}
		}
		else if (command.equalsIgnoreCase("request_extremefreya"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(extremeFreyaIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(extremeFreyaIzId))
			{
				ReflectionUtils.enterReflection(player, new FreyaHard(), extremeFreyaIzId);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

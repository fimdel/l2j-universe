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

import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AllenosInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field tiatIzId. (value is 110)
	 */
	private static final int tiatIzId = 110;
	
	/**
	 * Constructor for AllenosInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AllenosInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("enter_seed"))
		{
			if (SoDManager.isAttackStage())
			{
				Reflection r = player.getActiveReflection();
				if (r != null)
				{
					if (player.canReenterInstance(tiatIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(tiatIzId))
				{
					ReflectionUtils.enterReflection(player, tiatIzId);
				}
			}
			else
			{
				SoDManager.teleportIntoSeed(player);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

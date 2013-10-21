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

import instances.ErosionHallAttack;
import instances.ErosionHallDefence;
import instances.SufferingHallAttack;
import instances.SufferingHallDefence;
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
public final class EkimusMouthInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field hosattackIzId. (value is 115)
	 */
	private static final int hosattackIzId = 115;
	/**
	 * Field hoeattackIzId. (value is 119)
	 */
	private static final int hoeattackIzId = 119;
	/**
	 * Field hosdefenceIzId. (value is 116)
	 */
	private static final int hosdefenceIzId = 116;
	/**
	 * Field hoedefenceIzId. (value is 120)
	 */
	private static final int hoedefenceIzId = 120;
	
	/**
	 * Constructor for EkimusMouthInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public EkimusMouthInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("hos_enter"))
		{
			Reflection r = player.getActiveReflection();
			if (SoIManager.getCurrentStage() == 1)
			{
				if (r != null)
				{
					if (player.canReenterInstance(hosattackIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(hosattackIzId))
				{
					ReflectionUtils.enterReflection(player, new SufferingHallAttack(), hosattackIzId);
				}
			}
			else if (SoIManager.getCurrentStage() == 4)
			{
				if (r != null)
				{
					if (player.canReenterInstance(hosdefenceIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(hosdefenceIzId))
				{
					ReflectionUtils.enterReflection(player, new SufferingHallDefence(), hosdefenceIzId);
				}
			}
		}
		else if (command.equalsIgnoreCase("hoe_enter"))
		{
			Reflection r = player.getActiveReflection();
			if (SoIManager.getCurrentStage() == 1)
			{
				if (r != null)
				{
					if (player.canReenterInstance(hoeattackIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(hoeattackIzId))
				{
					ReflectionUtils.enterReflection(player, new ErosionHallAttack(), hoeattackIzId);
				}
			}
			else if (SoIManager.getCurrentStage() == 4)
			{
				if (r != null)
				{
					if (player.canReenterInstance(hoedefenceIzId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(hoedefenceIzId))
				{
					ReflectionUtils.enterReflection(player, new ErosionHallDefence(), hoedefenceIzId);
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

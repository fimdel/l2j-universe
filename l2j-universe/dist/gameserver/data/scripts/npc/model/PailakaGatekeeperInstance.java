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

import instances.RimPailaka;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.residence.ResidenceType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PailakaGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field rimIzId. (value is 80)
	 */
	private static final int rimIzId = 80;
	
	/**
	 * Constructor for PailakaGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public PailakaGatekeeperInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("rimentrance"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(rimIzId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(rimIzId))
			{
				if (checkGroup(player))
				{
					ReflectionUtils.enterReflection(player, new RimPailaka(), rimIzId);
				}
				else
				{
					player.sendMessage("Failed to enter Rim Pailaka due to improper conditions");
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method checkGroup.
	 * @param p Player
	 * @return boolean
	 */
	private boolean checkGroup(Player p)
	{
		if (!p.isInParty())
		{
			return false;
		}
		for (Player member : p.getParty().getPartyMembers())
		{
			if (member.getClan() == null)
			{
				return false;
			}
			if ((member.getClan().getResidenceId(ResidenceType.Castle) == 0) && (member.getClan().getResidenceId(ResidenceType.Fortress) == 0))
			{
				return false;
			}
		}
		return true;
	}
}

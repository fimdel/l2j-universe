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
package npc.model.residences.fortress;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DoormanInstance extends npc.model.residences.DoormanInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _loc.
	 */
	private Location _loc;
	
	/**
	 * Constructor for DoormanInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public DoormanInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		String loc = template.getAIParams().getString("tele_loc", null);
		if (loc != null)
		{
			_loc = Location.parseLoc(loc);
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
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
				if (command.equalsIgnoreCase("openDoors"))
				{
					for (int i : _doors)
					{
						ReflectionUtils.getDoor(i).openMe(player, true);
					}
				}
				else if (command.equalsIgnoreCase("closeDoors"))
				{
					for (int i : _doors)
					{
						ReflectionUtils.getDoor(i).closeMe(player, true);
					}
				}
				break;
			case COND_SIEGE:
				if (command.equalsIgnoreCase("tele"))
				{
					player.teleToLocation(_loc);
				}
				break;
			case COND_FAIL:
				player.sendPacket(new NpcHtmlMessage(player, this, _failDialog, 0));
				break;
		}
	}
	
	/**
	 * Method setDialogs.
	 */
	@Override
	public void setDialogs()
	{
		_mainDialog = "residence2/fortress/fortress_doorkeeper001.htm";
		_failDialog = "residence2/fortress/fortress_doorkeeper002.htm";
		_siegeDialog = "residence2/fortress/fortress_doorkeeper003.htm";
	}
	
	/**
	 * Method getOpenPriv.
	 * @return int
	 */
	@Override
	public int getOpenPriv()
	{
		return Clan.CP_CS_ENTRY_EXIT;
	}
	
	/**
	 * Method getResidence.
	 * @return Residence
	 */
	@Override
	public Residence getResidence()
	{
		return getFortress();
	}
}

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
package npc.model.residences.castle;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
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
	 * Field _locs.
	 */
	private final Location[] _locs = new Location[2];
	
	/**
	 * Constructor for DoormanInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public DoormanInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		for (int i = 0; i < _locs.length; i++)
		{
			String loc = template.getAIParams().getString("tele_loc" + i, null);
			if (loc != null)
			{
				_locs[i] = Location.parseLoc(loc);
			}
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
				else if (command.startsWith("tele"))
				{
					int id = Integer.parseInt(command.substring(4, 5));
					Location loc = _locs[id];
					if (loc != null)
					{
						player.teleToLocation(loc);
					}
				}
				break;
			case COND_SIEGE:
				if (command.startsWith("tele"))
				{
					int id = Integer.parseInt(command.substring(4, 5));
					Location loc = _locs[id];
					if (loc != null)
					{
						player.teleToLocation(loc);
					}
				}
				else
				{
					player.sendPacket(new NpcHtmlMessage(player, this, _siegeDialog, 0));
				}
				break;
			case COND_FAIL:
				player.sendPacket(new NpcHtmlMessage(player, this, _failDialog, 0));
				break;
		}
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
		String filename = null;
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
			case COND_SIEGE:
				filename = _mainDialog;
				break;
			case COND_FAIL:
				filename = _failDialog;
				break;
		}
		player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
	}
	
	/**
	 * Method getCond.
	 * @param player Player
	 * @return int
	 */
	@Override
	protected int getCond(Player player)
	{
		Castle residence = getCastle();
		Clan residenceOwner = residence.getOwner();
		if ((residenceOwner != null) && (player.getClan() == residenceOwner) && ((player.getClanPrivileges() & getOpenPriv()) == getOpenPriv()))
		{
			if (residence.getSiegeEvent().isInProgress())
			{
				return COND_SIEGE;
			}
			return COND_OWNER;
		}
		return COND_FAIL;
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
		return getCastle();
	}
}

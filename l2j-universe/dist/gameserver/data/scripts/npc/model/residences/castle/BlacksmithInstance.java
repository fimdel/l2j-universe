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

import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BlacksmithInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field COND_ALL_FALSE. (value is 0)
	 */
	protected static final int COND_ALL_FALSE = 0;
	/**
	 * Field COND_BUSY_BECAUSE_OF_SIEGE. (value is 1)
	 */
	protected static final int COND_BUSY_BECAUSE_OF_SIEGE = 1;
	/**
	 * Field COND_OWNER. (value is 2)
	 */
	protected static final int COND_OWNER = 2;
	
	/**
	 * Constructor for BlacksmithInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BlacksmithInstance(int objectId, NpcTemplate template)
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
		if (CastleManorManager.getInstance().isDisabled())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("npcdefault.htm");
			player.sendPacket(html);
			return;
		}
		int condition = validateCondition(player);
		if (condition <= COND_ALL_FALSE)
		{
			return;
		}
		if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
		{
			return;
		}
		if (condition == COND_OWNER)
		{
			if (command.startsWith("Chat"))
			{
				int val = 0;
				try
				{
					val = Integer.parseInt(command.substring(5));
				}
				catch (IndexOutOfBoundsException ioobe)
				{
				}
				catch (NumberFormatException nfe)
				{
				}
				showMessageWindow(player, val);
			}
			else
			{
				super.onBypassFeedback(player, command);
			}
		}
	}
	
	/**
	 * Method showMessageWindow.
	 * @param player Player
	 * @param val int
	 */
	private void showMessageWindow(Player player, int val)
	{
		player.sendActionFailed();
		String filename = "castle/blacksmith/castleblacksmith-no.htm";
		int condition = validateCondition(player);
		if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
			{
				filename = "castle/blacksmith/castleblacksmith-busy.htm";
			}
			else if (condition == COND_OWNER)
			{
				if (val == 0)
				{
					filename = "castle/blacksmith/castleblacksmith.htm";
				}
				else
				{
					filename = "castle/blacksmith/castleblacksmith-" + val + ".htm";
				}
			}
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(filename);
		html.replace("%castleid%", Integer.toString(getCastle().getId()));
		player.sendPacket(html);
	}
	
	/**
	 * Method validateCondition.
	 * @param player Player
	 * @return int
	 */
	protected int validateCondition(Player player)
	{
		if (player.isGM())
		{
			return COND_OWNER;
		}
		if ((getCastle() != null) && (getCastle().getId() > 0))
		{
			if (player.getClan() != null)
			{
				if (getCastle().getSiegeEvent().isInProgress())
				{
					return COND_BUSY_BECAUSE_OF_SIEGE;
				}
				else if ((getCastle().getOwnerId() == player.getClanId()) && ((player.getClanPrivileges() & Clan.CP_CS_MANOR_ADMIN) == Clan.CP_CS_MANOR_ADMIN))
				{
					return COND_OWNER;
				}
			}
		}
		return COND_ALL_FALSE;
	}
}

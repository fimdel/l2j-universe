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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.skillclasses.Call;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CourtInstance extends NpcInstance
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
	 * Constructor for CourtInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CourtInstance(int objectId, NpcTemplate template)
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
		int condition = validateCondition(player);
		if (condition <= COND_ALL_FALSE)
		{
			return;
		}
		else if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
		{
			return;
		}
		else if ((player.getClanPrivileges() & Clan.CP_CS_USE_FUNCTIONS) != Clan.CP_CS_USE_FUNCTIONS)
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		else if (condition == COND_OWNER)
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
				showChatWindow(player, val);
				return;
			}
			if (command.startsWith("gotoleader"))
			{
				if (player.getClan() != null)
				{
					Player clanLeader = player.getClan().getLeader().getPlayer();
					if (clanLeader == null)
					{
						return;
					}
					if (clanLeader.getEffectList().getEffectsBySkillId(3632) != null)
					{
						if (Call.canSummonHere(clanLeader) != null)
						{
							return;
						}
						if (Call.canBeSummoned(player) == null)
						{
							player.teleToLocation(Location.findAroundPosition(clanLeader, 100));
						}
						return;
					}
					showChatWindow(player, "castle/CourtMagician/CourtMagician-nogate.htm");
				}
			}
			else if(command.equalsIgnoreCase("Cloak")) // Give Cloak of Light/Dark
			{
				if (player.getClan() != null)
				{
					if (player.getCastle() != null)
					{
						if (player.getCastle().isCastleTypeLight())
						{
							if(player.getInventory().getItemByItemId(34925) == null)	//Cloak of Light
							{
								player.getInventory().addItem(ItemFunctions.createItem(34925));
								NpcHtmlMessage html = new NpcHtmlMessage(player, this);
								html.setFile("castle/CourtMagician/CourtMagician-givecloak.htm");
								html.replace("%CharName%", player.getName());
								html.replaceNpcString("%FeudName%", player.getCastle().getNpcStringName());
								player.sendPacket(html);
							}
							else
							{
								NpcHtmlMessage html = new NpcHtmlMessage(player, this);
								html.setFile("castle/CourtMagician/alreadyhavecloak.htm");
								player.sendPacket(html);
							}
						}
						else
						{
							if(player.getInventory().getItemByItemId(34926) == null)	//Cloak of Darkness
							{
								player.getInventory().addItem(ItemFunctions.createItem(34926));
								NpcHtmlMessage html = new NpcHtmlMessage(player, this);
								html.setFile("castle/CourtMagician/CourtMagician-givecloak.htm");
								html.replace("%CharName%", player.getName());
								html.replaceNpcString("%FeudName%", player.getCastle().getNpcStringName());
								player.sendPacket(html);
							}
							else
							{
								NpcHtmlMessage html = new NpcHtmlMessage(player, this);
								html.setFile("castle/CourtMagician/alreadyhavecloak.htm");
								player.sendPacket(html);
							}
						}
					}
				}
				
			}
			else
			{
				super.onBypassFeedback(player, command);
			}
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
		player.sendActionFailed();
		String filename = "castle/CourtMagician/CourtMagician-no.htm";
		int condition = validateCondition(player);
		if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
			{
				filename = "castle/CourtMagician/CourtMagician-busy.htm";
			}
			else if (condition == COND_OWNER)
			{
				if (val == 0)
				{
					filename = "castle/CourtMagician/CourtMagician.htm";
				}
				else
				{
					filename = "castle/CourtMagician/CourtMagician-" + val + ".htm";
				}
			}
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
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
				else if (getCastle().getOwnerId() == player.getClanId())
				{
					return COND_OWNER;
				}
			}
		}
		return COND_ALL_FALSE;
	}
}

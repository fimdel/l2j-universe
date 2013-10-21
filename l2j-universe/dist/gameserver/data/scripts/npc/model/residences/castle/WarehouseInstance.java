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

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.WarehouseFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WarehouseInstance extends NpcInstance
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
	 * Field ITEM_BLOOD_ALLI. (value is 9911)
	 */
	private static final int ITEM_BLOOD_ALLI = 9911;
	/**
	 * Field ITEM_BLOOD_OATH. (value is 9910)
	 */
	private static final int ITEM_BLOOD_OATH = 9910;
	
	/**
	 * Constructor for WarehouseInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WarehouseInstance(int objectId, NpcTemplate template)
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
		if ((player.getClanPrivileges() & Clan.CP_CS_USE_FUNCTIONS) != Clan.CP_CS_USE_FUNCTIONS)
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		if (player.getEnchantScroll() != null)
		{
			Log.add("Player " + player.getName() + " trying to use enchant exploit[CastleWarehouse], ban this player!", "illegal-actions");
			player.kick();
			return;
		}
		if (command.startsWith("WithdrawP"))
		{
			int val = Integer.parseInt(command.substring(10));
			if (val == 99)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("warehouse/personal.htm");
				player.sendPacket(html);
			}
			else
			{
				WarehouseFunctions.showRetrieveWindow(player, val);
			}
		}
		else if (command.equals("DepositP"))
		{
			WarehouseFunctions.showDepositWindow(player);
		}
		else if (command.startsWith("WithdrawC"))
		{
			int val = Integer.parseInt(command.substring(10));
			if (val == 99)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("warehouse/clan.htm");
				player.sendPacket(html);
			}
			else
			{
				WarehouseFunctions.showWithdrawWindowClan(player, val);
			}
		}
		else if (command.equals("DepositC"))
		{
			WarehouseFunctions.showDepositWindowClan(player);
		}
		else if (command.equalsIgnoreCase("CheckHonoraryItems"))
		{
			String filename;
			if (!player.isClanLeader())
			{
				filename = "castle/warehouse/castlewarehouse-notcl.htm";
			}
			else
			{
				filename = "castle/warehouse/castlewarehouse-5.htm";
			}
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile(filename);
			html.replace("%total_items%", String.valueOf(getCastle().getRewardCount()));
			player.sendPacket(html);
		}
		else if (command.equalsIgnoreCase("ExchangeBloodAlli"))
		{
			if (!player.isClanLeader())
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/warehouse/castlewarehouse-notcl.htm");
				player.sendPacket(html);
			}
			else if (Functions.removeItem(player, ITEM_BLOOD_ALLI, 1) == 0)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
			}
			else
			{
				ItemFunctions.addItem(player, ITEM_BLOOD_OATH, 30, true);
			}
		}
		else if (command.equalsIgnoreCase("ReciveBloodAlli"))
		{
			Castle castle = getCastle();
			String filename;
			int count = castle.getRewardCount();
			if (!player.isClanLeader())
			{
				filename = "castle/warehouse/castlewarehouse-notcl.htm";
			}
			else if (count > 0)
			{
				filename = "castle/warehouse/castlewarehouse-3.htm";
				castle.setRewardCount(0);
				castle.setJdbcState(JdbcEntityState.UPDATED);
				castle.update();
				Functions.addItem(player, ITEM_BLOOD_ALLI, count);
			}
			else
			{
				filename = "castle/warehouse/castlewarehouse-4.htm";
			}
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile(filename);
			player.sendPacket(html);
		}
		else if (command.startsWith("Chat"))
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
		}
		else
		{
			super.onBypassFeedback(player, command);
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
		String filename = "castle/warehouse/castlewarehouse-no.htm";
		int condition = validateCondition(player);
		if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
			{
				filename = "castle/warehouse/castlewarehouse-busy.htm";
			}
			else if (condition == COND_OWNER)
			{
				if (val == 0)
				{
					filename = "castle/warehouse/castlewarehouse.htm";
				}
				else
				{
					filename = "castle/warehouse/castlewarehouse-" + val + ".htm";
				}
			}
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(filename);
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

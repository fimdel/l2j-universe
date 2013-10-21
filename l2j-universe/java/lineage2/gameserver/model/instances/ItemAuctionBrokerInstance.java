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
package lineage2.gameserver.model.instances;

import java.text.SimpleDateFormat;
import java.util.Date;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.itemauction.ItemAuction;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionInstance;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExItemAuctionInfo;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ItemAuctionBrokerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field fmt.
	 */
	private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	/**
	 * Field _instance.
	 */
	private ItemAuctionInstance _instance;
	
	/**
	 * Constructor for ItemAuctionBrokerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ItemAuctionBrokerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, final int val, Object... arg)
	{
		String filename = val == 0 ? "itemauction/itembroker.htm" : "itemauction/itembroker-" + val + ".htm";
		player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public final void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		final String[] params = command.split(" ");
		if (params.length == 1)
		{
			return;
		}
		if (params[0].equals("auction"))
		{
			if (_instance == null)
			{
				_instance = ItemAuctionManager.getInstance().getManagerInstance(getTemplate().npcId);
				if (_instance == null)
				{
					return;
				}
			}
			if (params[1].equals("cancel"))
			{
				if (params.length == 3)
				{
					int auctionId = 0;
					try
					{
						auctionId = Integer.parseInt(params[2]);
					}
					catch (NumberFormatException e)
					{
						e.printStackTrace();
						return;
					}
					final ItemAuction auction = _instance.getAuction(auctionId);
					if (auction != null)
					{
						auction.cancelBid(player);
					}
					else
					{
						player.sendPacket(Msg.THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU);
					}
				}
				else
				{
					final ItemAuction[] auctions = _instance.getAuctionsByBidder(player.getObjectId());
					for (final ItemAuction auction : auctions)
					{
						auction.cancelBid(player);
					}
				}
			}
			else if (params[1].equals("show"))
			{
				final ItemAuction currentAuction = _instance.getCurrentAuction();
				final ItemAuction nextAuction = _instance.getNextAuction();
				if (currentAuction == null)
				{
					player.sendPacket(Msg.IT_IS_NOT_AN_AUCTION_PERIOD);
					if (nextAuction != null)
					{
						player.sendMessage("The next auction will begin on the " + fmt.format(new Date(nextAuction.getStartingTime())) + ".");
					}
					return;
				}
				if (!player.getAndSetLastItemAuctionRequest())
				{
					player.sendPacket(Msg.THERE_ARE_NO_OFFERINGS_I_OWN_OR_I_MADE_A_BID_FOR);
					return;
				}
				player.sendPacket(new ExItemAuctionInfo(false, currentAuction, nextAuction));
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

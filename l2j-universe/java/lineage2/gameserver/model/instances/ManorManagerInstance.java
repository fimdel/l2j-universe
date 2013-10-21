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

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.BuyListSeed;
import lineage2.gameserver.network.serverpackets.ExShowCropInfo;
import lineage2.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import lineage2.gameserver.network.serverpackets.ExShowProcureCropDetail;
import lineage2.gameserver.network.serverpackets.ExShowSeedInfo;
import lineage2.gameserver.network.serverpackets.ExShowSellCropList;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.manor.CropProcure;
import lineage2.gameserver.templates.manor.SeedProduction;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManorManagerInstance extends MerchantInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for ManorManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ManorManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onInteract(final Player player)
	{
		if(CastleManorManager.getInstance().isDisabled())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);

			html.setFile("npcdefault.htm");
			html.replace("%objectId%", String.valueOf(getObjectId()));
			html.replace("%npcname%", getName());
			player.sendPacket(html);
		}
		else if(!player.isGM() // Player is not GM
			&& player.isClanLeader() // Player is clan leader of clan (then he is the lord)
			&& (getCastle() != null // Verification of castle
		) && (getCastle().getOwnerId() == player.getClanId() // Player's clan owning the castle
		))
		{
			showMessageWindow(player, "manager-lord.htm");
		}
		else
		{
			showMessageWindow(player, "manager.htm");
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
		if (command.startsWith("manor_menu_select"))
		{
			if (CastleManorManager.getInstance().isUnderMaintenance())
			{
				player.sendPacket(ActionFail.STATIC, Msg.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
				return;
			}
			String params = command.substring(command.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(params, "&");
			int ask = Integer.parseInt(st.nextToken().split("=")[1]);
			int state = Integer.parseInt(st.nextToken().split("=")[1]);
			int time = Integer.parseInt(st.nextToken().split("=")[1]);
			Castle castle = getCastle();
			int castleId;
			if (state == -1)
			{
				castleId = castle.getId();
			}
			else
			{
				castleId = state;
			}
			switch (ask)
			{
				case 1:
					if (castleId != castle.getId())
					{
						player.sendPacket(Msg._HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR);
					}
					else
					{
						NpcTradeList tradeList = new NpcTradeList(0);
						List<SeedProduction> seeds = castle.getSeedProduction(CastleManorManager.PERIOD_CURRENT);
						for (SeedProduction s : seeds)
						{
							TradeItem item = new TradeItem();
							item.setItemId(s.getId());
							item.setOwnersPrice(s.getPrice());
							item.setCount(s.getCanProduce());
							if ((item.getCount() > 0) && (item.getOwnersPrice() > 0))
							{
								tradeList.addItem(item);
							}
						}
						BuyListSeed bl = new BuyListSeed(tradeList, castleId, player.getAdena());
						player.sendPacket(bl);
					}
					break;
				case 2:
					player.sendPacket(new ExShowSellCropList(player, castleId, castle.getCropProcure(CastleManorManager.PERIOD_CURRENT)));
					break;
				case 3:
					if ((time == 1) && !ResidenceHolder.getInstance().getResidence(Castle.class, castleId).isNextPeriodApproved())
					{
						player.sendPacket(new ExShowSeedInfo(castleId, Collections.<SeedProduction> emptyList()));
					}
					else
					{
						player.sendPacket(new ExShowSeedInfo(castleId, ResidenceHolder.getInstance().getResidence(Castle.class, castleId).getSeedProduction(time)));
					}
					break;
				case 4:
					if ((time == 1) && !ResidenceHolder.getInstance().getResidence(Castle.class, castleId).isNextPeriodApproved())
					{
						player.sendPacket(new ExShowCropInfo(castleId, Collections.<CropProcure> emptyList()));
					}
					else
					{
						player.sendPacket(new ExShowCropInfo(castleId, ResidenceHolder.getInstance().getResidence(Castle.class, castleId).getCropProcure(time)));
					}
					break;
				case 5:
					player.sendPacket(new ExShowManorDefaultInfo());
					break;
				case 6:
					showShopWindow(player, Integer.parseInt("3" + getNpcId()), false);
					break;
				case 9:
					player.sendPacket(new ExShowProcureCropDetail(state));
					break;
			}
		}
		else if (command.startsWith("help"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			String filename = "manor_client_help00" + st.nextToken() + ".htm";
			showMessageWindow(player, filename);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @return String
	 */
	public String getHtmlPath()
	{
		return "manormanager/";
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		return "manormanager/manager.htm";
	}
	
	/**
	 * Method showMessageWindow.
	 * @param player Player
	 * @param filename String
	 */
	private void showMessageWindow(Player player, String filename)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(getHtmlPath() + filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getNpcId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
}

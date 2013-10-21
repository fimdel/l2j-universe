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

import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.network.serverpackets.ExBuySellList;
import lineage2.gameserver.network.serverpackets.ExGetPremiumItemList;
import lineage2.gameserver.network.serverpackets.ShopPreviewList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.mapregion.DomainArea;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MerchantInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MerchantInstance.class);
	
	/**
	 * Constructor for MerchantInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MerchantInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		String pom;
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		if (getTemplate().getHtmRoot() != null)
		{
			return getTemplate().getHtmRoot() + pom + ".htm";
		}
		String temp = "merchant/" + pom + ".htm";
		if (HtmCache.getInstance().getNullable(temp, player) != null)
		{
			return temp;
		}
		temp = "teleporter/" + pom + ".htm";
		if (HtmCache.getInstance().getNullable(temp, player) != null)
		{
			return temp;
		}
		temp = "petmanager/" + pom + ".htm";
		if (HtmCache.getInstance().getNullable(temp, player) != null)
		{
			return temp;
		}
		return "default/" + pom + ".htm";
	}
	
	/**
	 * Method showWearWindow.
	 * @param player Player
	 * @param val int
	 */
	private void showWearWindow(Player player, int val)
	{
		if (!player.getPlayerAccess().UseShop)
		{
			return;
		}
		NpcTradeList list = BuyListHolder.getInstance().getBuyList(val);
		if (list != null)
		{
			ShopPreviewList bl = new ShopPreviewList(list, player);
			player.sendPacket(bl);
		}
		else
		{
			_log.warn("no buylist with id:" + val);
			player.sendActionFailed();
		}
	}
	
	/**
	 * Method showShopWindow.
	 * @param player Player
	 * @param listId int
	 * @param tax boolean
	 */
	protected void showShopWindow(Player player, int listId, boolean tax)
	{
		if (!player.getPlayerAccess().UseShop)
		{
			return;
		}
		double taxRate = 0;
		if (tax)
		{
			Castle castle = getCastle(player);
			if (castle != null)
			{
				taxRate = castle.getTaxRate();
			}
		}
		NpcTradeList list = BuyListHolder.getInstance().getBuyList(listId);
		if ((list == null) || (list.getNpcId() == getNpcId()))
		{
			player.sendPacket(new ExBuySellList.BuyList(list, player, taxRate), new ExBuySellList.SellRefundList(player, false));
		}
		else
		{
			_log.warn("[L2MerchantInstance] possible client hacker: " + player.getName() + " attempting to buy from GM shop! < Ban him!");
			_log.warn("buylist id:" + listId + " / list_npc = " + list.getNpcId() + " / npc = " + getNpcId());
		}
	}
	
	/**
	 * Method showShopWindow.
	 * @param player Player
	 */
	protected void showShopWindow(Player player)
	{
		showShopWindow(player, 0, false);
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
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();
		if (actualCommand.equalsIgnoreCase("Buy") || actualCommand.equalsIgnoreCase("Sell"))
		{
			int val = 0;
			if (st.countTokens() > 0)
			{
				val = Integer.parseInt(st.nextToken());
			}
			showShopWindow(player, val, true);
		}
		else if (actualCommand.equalsIgnoreCase("Wear"))
		{
			if (st.countTokens() < 1)
			{
				return;
			}
			int val = Integer.parseInt(st.nextToken());
			showWearWindow(player, val);
		}
		else if (actualCommand.equalsIgnoreCase("Multisell"))
		{
			if (st.countTokens() < 1)
			{
				return;
			}
			int val = Integer.parseInt(st.nextToken());
			Castle castle = getCastle(player);
			MultiSellHolder.getInstance().SeparateAndSend(val, player, castle != null ? castle.getTaxRate() : 0);
		}
		else if (actualCommand.equalsIgnoreCase("ReceivePremium"))
		{
			if (player.getPremiumItemList().isEmpty())
			{
				player.sendPacket(Msg.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
				return;
			}
			player.sendPacket(new ExGetPremiumItemList(player));
		}
		else if (actualCommand.equalsIgnoreCase("MenteeCertGet"))
		{
			if ((player.getInventory().getCountOf(33800) == 0) && (player.getLevel() < 86) && (player.getVar("MenteeCertGet") == null))
			{
				player.setVar("MenteeCertGet", "1", -1);
				ItemFunctions.addItem(player, 33800, 1, true);
			}
			else
			{
				player.sendPacket(new SystemMessage2(SystemMsg.YOU_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER));
			}
		}
		else if (actualCommand.equalsIgnoreCase("MenteeCertChange"))
		{
			if ((player.getInventory().getCountOf(33800) == 1) && (player.getLevel() >= 86))
			{
				ItemFunctions.addItem(player, 33805, 40, true);
				ItemFunctions.removeItem(player, 33800, 1, true);
			}
			else
			{
				player.sendPacket(new SystemMessage2(SystemMsg.YOU_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER));
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getCastle.
	 * @param player Player
	 * @return Castle
	 */
	@Override
	public Castle getCastle(Player player)
	{
		if (Config.SERVICES_OFFSHORE_NO_CASTLE_TAX || ((getReflection() == ReflectionManager.PARNASSUS) && Config.SERVICES_PARNASSUS_NOTAX))
		{
			return null;
		}
		if ((getReflection() == ReflectionManager.GIRAN_HARBOR) || (getReflection() == ReflectionManager.PARNASSUS))
		{
			String var = player.getVar("backCoords");
			if ((var != null) && !var.isEmpty())
			{
				Location loc = Location.parseLoc(var);
				DomainArea domain = MapRegionManager.getInstance().getRegionData(DomainArea.class, loc);
				if (domain != null)
				{
					return ResidenceHolder.getInstance().getResidence(Castle.class, domain.getId());
				}
			}
			return super.getCastle();
		}
		return super.getCastle(player);
	}
	
	/**
	 * Method isMerchantNpc.
	 * @return boolean
	 */
	@Override
	public boolean isMerchantNpc()
	{
		return true;
	}
}

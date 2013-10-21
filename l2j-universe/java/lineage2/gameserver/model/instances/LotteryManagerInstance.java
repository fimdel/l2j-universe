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

import java.text.DateFormat;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.games.LotteryManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LotteryManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for LotteryManagerInstance.
	 * @param objectID int
	 * @param template NpcTemplate
	 */
	public LotteryManagerInstance(int objectID, NpcTemplate template)
	{
		super(objectID, template);
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
		if (command.startsWith("Loto"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(5));
				showLotoWindow(player, val);
			}
			catch (NumberFormatException e)
			{
				Log.debug("L2LotteryManagerInstance: bypass: " + command + "; player: " + player, e);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
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
			pom = "LotteryManager";
		}
		else
		{
			pom = "LotteryManager-" + val;
		}
		return "lottery/" + pom + ".htm";
	}
	
	/**
	 * Method showLotoWindow.
	 * @param player Player
	 * @param val int
	 */
	public void showLotoWindow(Player player, int val)
	{
		int npcId = getTemplate().npcId;
		String filename;
		SystemMessage sm;
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		if (val == 0)
		{
			filename = getHtmlPath(npcId, 1, player);
			html.setFile(filename);
		}
		else if ((val >= 1) && (val <= 21))
		{
			if (!LotteryManager.getInstance().isStarted())
			{
				player.sendPacket(Msg.LOTTERY_TICKETS_ARE_NOT_CURRENTLY_BEING_SOLD);
				return;
			}
			if (!LotteryManager.getInstance().isSellableTickets())
			{
				player.sendPacket(Msg.TICKETS_FOR_THE_CURRENT_LOTTERY_ARE_NO_LONGER_AVAILABLE);
				return;
			}
			filename = getHtmlPath(npcId, 5, player);
			html.setFile(filename);
			int count = 0;
			int found = 0;
			for (int i = 0; i < 5; i++)
			{
				if (player.getLoto(i) == val)
				{
					player.setLoto(i, 0);
					found = 1;
				}
				else if (player.getLoto(i) > 0)
				{
					count++;
				}
			}
			if ((count < 5) && (found == 0) && (val <= 20))
			{
				for (int i = 0; i < 5; i++)
				{
					if (player.getLoto(i) == 0)
					{
						player.setLoto(i, val);
						break;
					}
				}
			}
			count = 0;
			for (int i = 0; i < 5; i++)
			{
				if (player.getLoto(i) > 0)
				{
					count++;
					String button = String.valueOf(player.getLoto(i));
					if (player.getLoto(i) < 10)
					{
						button = "0" + button;
					}
					String search = "fore=\"L2UI.lottoNum" + button + "\" back=\"L2UI.lottoNum" + button + "a_check\"";
					String replace = "fore=\"L2UI.lottoNum" + button + "a_check\" back=\"L2UI.lottoNum" + button + "\"";
					html.replace(search, replace);
				}
			}
			if (count == 5)
			{
				String search = "";
				String replace = "";
				if (player.getVar("lang@").equalsIgnoreCase("en"))
				{
					search = "0\">Return";
					replace = "22\">The winner selected the numbers above.";
				}
				else
				{
					search = "0\">�?азад";
					replace = "22\">Выигры�?ные номера выбранные вы�?е.";
				}
				html.replace(search, replace);
			}
			player.sendPacket(html);
		}
		if (val == 22)
		{
			if (!LotteryManager.getInstance().isStarted())
			{
				player.sendPacket(Msg.LOTTERY_TICKETS_ARE_NOT_CURRENTLY_BEING_SOLD);
				return;
			}
			if (!LotteryManager.getInstance().isSellableTickets())
			{
				player.sendPacket(Msg.TICKETS_FOR_THE_CURRENT_LOTTERY_ARE_NO_LONGER_AVAILABLE);
				return;
			}
			int price = Config.SERVICES_ALT_LOTTERY_PRICE;
			int lotonumber = LotteryManager.getInstance().getId();
			int enchant = 0;
			int type2 = 0;
			for (int i = 0; i < 5; i++)
			{
				if (player.getLoto(i) == 0)
				{
					return;
				}
				if (player.getLoto(i) < 17)
				{
					enchant += Math.pow(2, player.getLoto(i) - 1);
				}
				else
				{
					type2 += Math.pow(2, player.getLoto(i) - 17);
				}
			}
			if (player.getAdena() < price)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			player.reduceAdena(price, true);
			sm = new SystemMessage(SystemMessage.ACQUIRED__S1_S2);
			sm.addNumber(lotonumber);
			sm.addItemName(4442);
			player.sendPacket(sm);
			ItemInstance item = ItemFunctions.createItem(4442);
			item.setCustomType1(lotonumber);
			item.setEnchantLevel(enchant);
			item.setCustomType2(type2);
			player.getInventory().addItem(item);
			filename = getHtmlPath(npcId, 3, player);
			html.setFile(filename);
		}
		else if (val == 23)
		{
			filename = getHtmlPath(npcId, 3, player);
			html.setFile(filename);
		}
		else if (val == 24)
		{
			filename = getHtmlPath(npcId, 4, player);
			html.setFile(filename);
			int lotonumber = LotteryManager.getInstance().getId();
			String message = "";
			for (ItemInstance item : player.getInventory().getItems())
			{
				if (item == null)
				{
					continue;
				}
				if ((item.getItemId() == 4442) && (item.getCustomType1() < lotonumber))
				{
					message += "<a action=\"bypass -h npc_%objectId%_Loto " + item.getObjectId() + "\">" + item.getCustomType1();
					message += " " + HtmlUtils.htmlNpcString(NpcString.EVENT_NUMBER) + " ";
					int[] numbers = LotteryManager.getInstance().decodeNumbers(item.getEnchantLevel(), item.getCustomType2());
					for (int i = 0; i < 5; i++)
					{
						message += numbers[i] + " ";
					}
					int[] check = LotteryManager.getInstance().checkTicket(item);
					if (check[0] > 0)
					{
						message += "- ";
						switch (check[0])
						{
							case 1:
								message += HtmlUtils.htmlNpcString(NpcString.FIRST_PRIZE);
								break;
							case 2:
								message += HtmlUtils.htmlNpcString(NpcString.SECOND_PRIZE);
								break;
							case 3:
								message += HtmlUtils.htmlNpcString(NpcString.THIRD_PRIZE);
								break;
							case 4:
								message += HtmlUtils.htmlNpcString(NpcString.FOURTH_PRIZE);
								break;
						}
						message += " " + check[1] + "a.";
					}
					message += "</a>";
				}
			}
			if (message.length() == 0)
			{
				message += HtmlUtils.htmlNpcString(NpcString.THERE_HAS_BEEN_NO_WINNING_LOTTERY_TICKET);
			}
			html.replace("%result%", message);
		}
		else if (val == 25)
		{
			filename = getHtmlPath(npcId, 2, player);
			html.setFile(filename);
		}
		else if (val > 25)
		{
			int lotonumber = LotteryManager.getInstance().getId();
			ItemInstance item = player.getInventory().getItemByObjectId(val);
			if ((item == null) || (item.getItemId() != 4442) || (item.getCustomType1() >= lotonumber))
			{
				return;
			}
			int[] check = LotteryManager.getInstance().checkTicket(item);
			if (player.getInventory().destroyItem(item, 1L))
			{
				player.sendPacket(SystemMessage2.removeItems(4442, 1));
				int adena = check[1];
				if (adena > 0)
				{
					player.addAdena(adena);
				}
			}
			return;
		}
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%race%", "" + LotteryManager.getInstance().getId());
		html.replace("%adena%", "" + LotteryManager.getInstance().getPrize());
		html.replace("%ticket_price%", "" + Config.SERVICES_LOTTERY_TICKET_PRICE);
		html.replace("%prize5%", "" + (Config.SERVICES_LOTTERY_5_NUMBER_RATE * 100));
		html.replace("%prize4%", "" + (Config.SERVICES_LOTTERY_4_NUMBER_RATE * 100));
		html.replace("%prize3%", "" + (Config.SERVICES_LOTTERY_3_NUMBER_RATE * 100));
		html.replace("%prize2%", "" + Config.SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE);
		html.replace("%enddate%", "" + DateFormat.getDateInstance().format(LotteryManager.getInstance().getEndDate()));
		player.sendPacket(html);
		player.sendActionFailed();
	}
}

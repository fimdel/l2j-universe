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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PriestOfBlessingInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	private static class Hourglass
	{
		/**
		 * Field minLevel.
		 */
		public int minLevel;
		/**
		 * Field maxLevel.
		 */
		public int maxLevel;
		/**
		 * Field itemPrice.
		 */
		public int itemPrice;
		/**
		 * Field itemId.
		 */
		public int[] itemId;
		
		/**
		 * Constructor for Hourglass.
		 * @param min int
		 * @param max int
		 * @param price int
		 * @param id int[]
		 */
		public Hourglass(int min, int max, int price, int[] id)
		{
			minLevel = min;
			maxLevel = max;
			itemPrice = price;
			itemId = id;
		}
	}
	
	/**
	 * Field hourglassList.
	 */
	private static List<Hourglass> hourglassList = new ArrayList<>();
	static
	{
		hourglassList.add(new Hourglass(1, 19, 4000, new int[]
		{
			17095,
			17096,
			17097,
			17098,
			17099
		}));
		hourglassList.add(new Hourglass(20, 39, 30000, new int[]
		{
			17100,
			17101,
			17102,
			17103,
			17104
		}));
		hourglassList.add(new Hourglass(40, 51, 110000, new int[]
		{
			17105,
			17106,
			17107,
			17108,
			17109
		}));
		hourglassList.add(new Hourglass(52, 60, 310000, new int[]
		{
			17110,
			17111,
			17112,
			17113,
			17114
		}));
		hourglassList.add(new Hourglass(61, 75, 970000, new int[]
		{
			17115,
			17116,
			17117,
			17118,
			17119
		}));
		hourglassList.add(new Hourglass(76, 79, 2160000, new int[]
		{
			17120,
			17121,
			17122,
			17123,
			17124
		}));
		hourglassList.add(new Hourglass(80, 85, 5000000, new int[]
		{
			17125,
			17126,
			17127,
			17128,
			17129
		}));
	}
	
	/**
	 * Constructor for PriestOfBlessingInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public PriestOfBlessingInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("BuyHourglass"))
		{
			int val = Integer.parseInt(command.substring(13));
			Hourglass hg = getHourglass(player);
			int itemId = getHourglassId(hg);
			buyLimitedItem(player, "hourglass" + hg.minLevel + hg.maxLevel, itemId, val, false);
		}
		else if (command.startsWith("BuyVoice"))
		{
			buyLimitedItem(player, "nevitsVoice" + player.getAccountName(), 17094, 100000, true);
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
		if (val == 0)
		{
			Hourglass hg = getHourglass(player);
			if (hg != null)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile(getHtmlPath(getNpcId(), val, player));
				html.replace("%price%", String.valueOf(hg.itemPrice));
				html.replace("%priceBreak%", Util.formatAdena(hg.itemPrice));
				html.replace("%minLvl%", String.valueOf(hg.minLevel));
				html.replace("%maxLvl%", String.valueOf(hg.maxLevel));
				player.sendPacket(html);
			}
			return;
		}
		super.showChatWindow(player, val);
	}
	
	/**
	 * Method getHourglass.
	 * @param player Player
	 * @return Hourglass
	 */
	private static Hourglass getHourglass(Player player)
	{
		for (Hourglass hg : hourglassList)
		{
			if ((player.getLevel() >= hg.minLevel) && (player.getLevel() <= hg.maxLevel))
			{
				return hg;
			}
		}
		return null;
	}
	
	/**
	 * Method getHourglassId.
	 * @param hg Hourglass
	 * @return int
	 */
	private static int getHourglassId(Hourglass hg)
	{
		int id = hg.itemId[Rnd.get(hg.itemId.length)];
		return id;
	}
	
	/**
	 * Method buyLimitedItem.
	 * @param player Player
	 * @param var String
	 * @param itemId int
	 * @param price int
	 * @param isGlobalVar boolean
	 */
	private void buyLimitedItem(Player player, String var, int itemId, int price, boolean isGlobalVar)
	{
		long _remaining_time;
		long _reuse_time = 20 * 60 * 60 * 1000;
		long _curr_time = System.currentTimeMillis();
		String _last_use_time = player.getVar(var);
		if (isGlobalVar)
		{
			Map<Integer, String> chars = player.getAccountChars();
			if (chars != null)
			{
				long use_time = 0;
				for (int objId : chars.keySet())
				{
					String val = Player.getVarFromPlayer(objId, var);
					if (val != null)
					{
						if (Long.parseLong(val) > use_time)
						{
							use_time = Long.parseLong(val);
						}
					}
				}
				if (use_time > 0)
				{
					_last_use_time = String.valueOf(use_time);
				}
			}
		}
		if (_last_use_time != null)
		{
			_remaining_time = _curr_time - Long.parseLong(_last_use_time);
		}
		else
		{
			_remaining_time = _reuse_time;
		}
		if (_remaining_time >= _reuse_time)
		{
			if (player.reduceAdena(price, true))
			{
				Functions.addItem(player, itemId, 1);
				player.setVar(var, String.valueOf(_curr_time), -1);
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage._2_UNITS_OF_THE_ITEM_S1_IS_REQUIRED).addItemName(57).addNumber(price));
			}
		}
		else
		{
			int hours = (int) (_reuse_time - _remaining_time) / 3600000;
			int minutes = ((int) (_reuse_time - _remaining_time) % 3600000) / 60000;
			if (hours > 0)
			{
				player.sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S1_HOURSS_AND_S2_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED).addNumber(hours).addNumber(minutes));
			}
			else if (minutes > 0)
			{
				player.sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S1_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED).addNumber(minutes));
			}
			else if (player.reduceAdena(price, true))
			{
				Functions.addItem(player, itemId, 1);
				player.setVar(var, String.valueOf(_curr_time), -1);
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage._2_UNITS_OF_THE_ITEM_S1_IS_REQUIRED).addItemName(57).addNumber(price));
			}
		}
	}
}

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
package events.MasterOfEnchanting;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnchantingReward extends Functions implements ScriptFile
{
	/**
	 * Field MASTER_YOGI_STAFF.
	 */
	private static final int MASTER_YOGI_STAFF = 13539;
	/**
	 * Field MASTER_YOGI_SCROLL.
	 */
	private static final int MASTER_YOGI_SCROLL = 13540;
	/**
	 * Field ADENA.
	 */
	private static final int ADENA = 57;
	/**
	 * Field STAFF_PRICE.
	 */
	private static final int STAFF_PRICE = 1000;
	/**
	 * Field TIMED_SCROLL_PRICE.
	 */
	private static final int TIMED_SCROLL_PRICE = 6000;
	/**
	 * Field TIMED_SCROLL_HOURS.
	 */
	private static final int TIMED_SCROLL_HOURS = 6;
	/**
	 * Field ONE_SCROLL_PRICE.
	 */
	private static final int ONE_SCROLL_PRICE = 77777;
	/**
	 * Field TEN_SCROLLS_PRICE.
	 */
	private static final int TEN_SCROLLS_PRICE = 777770;
	/**
	 * Field HAT_SHADOW.
	 */
	private static final int[] HAT_SHADOW = new int[]
	{
		13074,
		13075,
		13076
	};
	/**
	 * Field HAT_EVENT.
	 */
	private static final int[] HAT_EVENT = new int[]
	{
		13518,
		13519,
		13522
	};
	/**
	 * Field SOUL_CRYSTALL.
	 */
	private static final int[] SOUL_CRYSTALL = new int[]
	{
		9570,
		9571,
		9572
	};
	
	/**
	 * Method buy_staff.
	 */
	public void buy_staff()
	{
		final Player player = getSelf();
		if ((getItemCount(player, MASTER_YOGI_STAFF) == 0) && (getItemCount(player, ADENA) >= STAFF_PRICE))
		{
			removeItem(player, ADENA, STAFF_PRICE);
			addItem(player, MASTER_YOGI_STAFF, 1);
			show("scripts/events/MasterOfEnchanting/32599-staffbuyed.htm", player);
		}
		else
		{
			show("scripts/events/MasterOfEnchanting/32599-staffcant.htm", player);
		}
	}
	
	/**
	 * Method buy_scroll_lim.
	 */
	public void buy_scroll_lim()
	{
		final Player player = getSelf();
		final long _reuse_time = TIMED_SCROLL_HOURS * 60 * 60 * 1000;
		final long _curr_time = System.currentTimeMillis();
		final String _last_use_time = player.getVar("MasterOfEnch");
		long _remaining_time;
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
			if (getItemCount(player, ADENA) >= TIMED_SCROLL_PRICE)
			{
				removeItem(player, ADENA, TIMED_SCROLL_PRICE);
				addItem(player, MASTER_YOGI_SCROLL, 1);
				player.setVar("MasterOfEnch", String.valueOf(_curr_time), -1);
				show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
			}
			else
			{
				show("scripts/events/MasterOfEnchanting/32599-s24-no.htm", player);
			}
		}
		else
		{
			final int hours = (int) (_reuse_time - _remaining_time) / 3600000;
			final int minutes = ((int) (_reuse_time - _remaining_time) % 3600000) / 60000;
			if (hours > 0)
			{
				final SystemMessage sm = new SystemMessage(SystemMessage.THERE_ARE_S1_HOURSS_AND_S2_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED);
				sm.addNumber(hours);
				sm.addNumber(minutes);
				player.sendPacket(sm);
				show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
			}
			else if (minutes > 0)
			{
				final SystemMessage sm = new SystemMessage(SystemMessage.THERE_ARE_S1_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED);
				sm.addNumber(minutes);
				player.sendPacket(sm);
				show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
			}
			else if (getItemCount(player, ADENA) >= TIMED_SCROLL_PRICE)
			{
				removeItem(player, ADENA, TIMED_SCROLL_PRICE);
				addItem(player, MASTER_YOGI_SCROLL, 1);
				player.setVar("MasterOfEnch", String.valueOf(_curr_time), -1);
				show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
			}
			else
			{
				show("scripts/events/MasterOfEnchanting/32599-s24-no.htm", player);
			}
		}
	}
	
	/**
	 * Method buy_scroll_1.
	 */
	public void buy_scroll_1()
	{
		final Player player = getSelf();
		if (getItemCount(player, ADENA) >= ONE_SCROLL_PRICE)
		{
			removeItem(player, ADENA, ONE_SCROLL_PRICE);
			addItem(player, MASTER_YOGI_SCROLL, 1);
			show("scripts/events/MasterOfEnchanting/32599-scroll-ok.htm", player);
		}
		else
		{
			show("scripts/events/MasterOfEnchanting/32599-s1-no.htm", player);
		}
	}
	
	/**
	 * Method buy_scroll_10.
	 */
	public void buy_scroll_10()
	{
		final Player player = getSelf();
		if (getItemCount(player, ADENA) >= TEN_SCROLLS_PRICE)
		{
			removeItem(player, ADENA, TEN_SCROLLS_PRICE);
			addItem(player, MASTER_YOGI_SCROLL, 10);
			show("scripts/events/MasterOfEnchanting/32599-scroll-ok.htm", player);
		}
		else
		{
			show("scripts/events/MasterOfEnchanting/32599-s10-no.htm", player);
		}
	}
	
	/**
	 * Method receive_reward.
	 */
	public void receive_reward()
	{
		final Player player = getSelf();
		final int Equip_Id = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
		if (Equip_Id != MASTER_YOGI_STAFF)
		{
			show("scripts/events/MasterOfEnchanting/32599-rewardnostaff.htm", player);
			return;
		}
		final ItemInstance enchanteditem = player.getInventory().getItemByItemId(Equip_Id);
		final int Ench_Lvl = enchanteditem.getEnchantLevel();
		if ((Equip_Id == MASTER_YOGI_STAFF) && (Ench_Lvl > 3))
		{
			switch (Ench_Lvl)
			{
				case 4:
					addItem(player, 6406, 1);
					break;
				case 5:
					addItem(player, 6406, 2);
					addItem(player, 6407, 1);
					break;
				case 6:
					addItem(player, 6406, 3);
					addItem(player, 6407, 2);
					break;
				case 7:
					addItem(player, HAT_SHADOW[Rnd.get(HAT_SHADOW.length)], 1);
					break;
				case 8:
					addItem(player, 955, 1);
					break;
				case 9:
					addItem(player, 955, 1);
					addItem(player, 956, 1);
					break;
				case 10:
					addItem(player, 951, 1);
					break;
				case 11:
					addItem(player, 951, 1);
					addItem(player, 952, 1);
					break;
				case 12:
					addItem(player, 948, 1);
					break;
				case 13:
					addItem(player, 729, 1);
					break;
				case 14:
					addItem(player, HAT_EVENT[Rnd.get(HAT_EVENT.length)], 1);
					break;
				case 15:
					addItem(player, 13992, 1);
					break;
				case 16:
					addItem(player, 8762, 1);
					break;
				case 17:
					addItem(player, 959, 1);
					break;
				case 18:
					addItem(player, 13991, 1);
					break;
				case 19:
					addItem(player, 13990, 1);
					break;
				case 20:
					addItem(player, SOUL_CRYSTALL[Rnd.get(SOUL_CRYSTALL.length)], 1);
					break;
				case 21:
					addItem(player, 8762, 1);
					addItem(player, 8752, 1);
					addItem(player, SOUL_CRYSTALL[Rnd.get(SOUL_CRYSTALL.length)], 1);
					break;
				case 22:
					addItem(player, 13989, 1);
					break;
				case 23:
					addItem(player, 13988, 1);
					break;
				default:
					if (Ench_Lvl > 23)
					{
						addItem(player, 13988, 1);
					}
					break;
			}
			removeItem(player, Equip_Id, 1);
			show("scripts/events/MasterOfEnchanting/32599-rewardok.htm", player);
		}
		else
		{
			show("scripts/events/MasterOfEnchanting/32599-rewardnostaff.htm", player);
		}
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		// empty method
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		// empty method
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		// empty method
	}
}

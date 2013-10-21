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
package lineage2.gameserver.handler.admincommands.impl;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCreateItem implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_itemcreate.
		 */
		admin_itemcreate,
		/**
		 * Field admin_create_item.
		 */
		admin_create_item,
		/**
		 * Field admin_ci.
		 */
		admin_ci,
		/**
		 * Field admin_spreaditem.
		 */
		admin_spreaditem,
		/**
		 * Field admin_create_item_element.
		 */
		admin_create_item_element
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().UseGMShop)
		{
			return false;
		}
		switch (command)
		{
			case admin_itemcreate:
				activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/itemcreation.htm"));
				break;
			case admin_ci:
			case admin_create_item:
				try
				{
					if (wordList.length < 2)
					{
						activeChar.sendMessage("USAGE: create_item id [count]");
						return false;
					}
					int item_id = Integer.parseInt(wordList[1]);
					long item_count = wordList.length < 3 ? 1 : Long.parseLong(wordList[2]);
					createItem(activeChar, item_id, item_count);
				}
				catch (NumberFormatException nfe)
				{
					activeChar.sendMessage("USAGE: create_item id [count]");
				}
				activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/itemcreation.htm"));
				break;
			case admin_spreaditem:
				try
				{
					int id = Integer.parseInt(wordList[1]);
					int num = wordList.length > 2 ? Integer.parseInt(wordList[2]) : 1;
					long count = wordList.length > 3 ? Long.parseLong(wordList[3]) : 1;
					for (int i = 0; i < num; i++)
					{
						ItemInstance createditem = ItemFunctions.createItem(id);
						createditem.setCount(count);
						createditem.dropMe(activeChar, Location.findPointToStay(activeChar, 100));
					}
				}
				catch (NumberFormatException nfe)
				{
					activeChar.sendMessage("Specify a valid number.");
				}
				catch (StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Can't create this item.");
				}
				break;
			case admin_create_item_element:
				try
				{
					if (wordList.length < 4)
					{
						activeChar.sendMessage("USAGE: create_item_attribue [id] [element id] [value]");
						return false;
					}
					int item_id = Integer.parseInt(wordList[1]);
					int elementId = Integer.parseInt(wordList[2]);
					int value = Integer.parseInt(wordList[3]);
					if ((elementId > 5) || (elementId < 0))
					{
						activeChar.sendMessage("Improper element Id");
						return false;
					}
					if ((value < 1) || (value > 300))
					{
						activeChar.sendMessage("Improper element value");
						return false;
					}
					ItemInstance item = createItem(activeChar, item_id, 1);
					Element element = Element.getElementById(elementId);
					item.setAttributeElement(element, item.getAttributeElementValue(element, false) + value);
					item.setJdbcState(JdbcEntityState.UPDATED);
					item.update();
					activeChar.sendPacket(new InventoryUpdate().addModifiedItem(item));
				}
				catch (NumberFormatException nfe)
				{
					activeChar.sendMessage("USAGE: create_item id [count]");
				}
				activeChar.sendPacket(new NpcHtmlMessage(5).setFile("data/html/admin/itemcreation.htm"));
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
	
	/**
	 * Method createItem.
	 * @param activeChar Player
	 * @param itemId int
	 * @param count long
	 * @return ItemInstance
	 */
	private ItemInstance createItem(Player activeChar, int itemId, long count)
	{
		ItemInstance createditem = ItemFunctions.createItem(itemId);
		createditem.setCount(count);
		Log.LogItem(activeChar, Log.Create, createditem);
		activeChar.getInventory().addItem(createditem);
		if (!createditem.isStackable())
		{
			for (long i = 0; i < (count - 1); i++)
			{
				createditem = ItemFunctions.createItem(itemId);
				Log.LogItem(activeChar, Log.Create, createditem);
				activeChar.getInventory().addItem(createditem);
			}
		}
		activeChar.sendPacket(SystemMessage2.obtainItems(itemId, count, 0));
		return createditem;
	}
}

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

import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminEnchant implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_seteh.
		 */
		admin_seteh,
		/**
		 * Field admin_setec.
		 */
		admin_setec,
		/**
		 * Field admin_seteg.
		 */
		admin_seteg,
		/**
		 * Field admin_setel.
		 */
		admin_setel,
		/**
		 * Field admin_seteb.
		 */
		admin_seteb,
		/**
		 * Field admin_setew.
		 */
		admin_setew,
		/**
		 * Field admin_setes.
		 */
		admin_setes,
		/**
		 * Field admin_setle.
		 */
		admin_setle,
		/**
		 * Field admin_setre.
		 */
		admin_setre,
		/**
		 * Field admin_setlf.
		 */
		admin_setlf,
		/**
		 * Field admin_setrf.
		 */
		admin_setrf,
		/**
		 * Field admin_seten.
		 */
		admin_seten,
		/**
		 * Field admin_setun.
		 */
		admin_setun,
		/**
		 * Field admin_setba.
		 */
		admin_setba,
		/**
		 * Field admin_setha.
		 */
		admin_setha,
		/**
		 * Field admin_setdha.
		 */
		admin_setdha,
		/**
		 * Field admin_setlbr.
		 */
		admin_setlbr,
		/**
		 * Field admin_setrbr.
		 */
		admin_setrbr,
		/**
		 * Field admin_setbelt.
		 */
		admin_setbelt,
		/**
		 * Field admin_enchant.
		 */
		admin_enchant
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
		if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		int armorType = -1;
		switch (command)
		{
			case admin_enchant:
				showMainPage(activeChar);
				return true;
			case admin_seteh:
				armorType = Inventory.PAPERDOLL_HEAD;
				break;
			case admin_setec:
				armorType = Inventory.PAPERDOLL_CHEST;
				break;
			case admin_seteg:
				armorType = Inventory.PAPERDOLL_GLOVES;
				break;
			case admin_seteb:
				armorType = Inventory.PAPERDOLL_FEET;
				break;
			case admin_setel:
				armorType = Inventory.PAPERDOLL_LEGS;
				break;
			case admin_setew:
				armorType = Inventory.PAPERDOLL_RHAND;
				break;
			case admin_setes:
				armorType = Inventory.PAPERDOLL_LHAND;
				break;
			case admin_setle:
				armorType = Inventory.PAPERDOLL_LEAR;
				break;
			case admin_setre:
				armorType = Inventory.PAPERDOLL_REAR;
				break;
			case admin_setlf:
				armorType = Inventory.PAPERDOLL_LFINGER;
				break;
			case admin_setrf:
				armorType = Inventory.PAPERDOLL_RFINGER;
				break;
			case admin_seten:
				armorType = Inventory.PAPERDOLL_NECK;
				break;
			case admin_setun:
				armorType = Inventory.PAPERDOLL_UNDER;
				break;
			case admin_setba:
				armorType = Inventory.PAPERDOLL_BACK;
				break;
			case admin_setha:
				armorType = Inventory.PAPERDOLL_HAIR;
				break;
			case admin_setdha:
				armorType = Inventory.PAPERDOLL_HAIR;
				break;
			case admin_setlbr:
				armorType = Inventory.PAPERDOLL_LBRACELET;
				break;
			case admin_setrbr:
				armorType = Inventory.PAPERDOLL_RBRACELET;
				break;
			case admin_setbelt:
				armorType = Inventory.PAPERDOLL_BELT;
				break;
		}
		if ((armorType == -1) || (wordList.length < 2))
		{
			showMainPage(activeChar);
			return true;
		}
		try
		{
			int ench = Integer.parseInt(wordList[1]);
			if ((ench < 0) || (ench > 65535))
			{
				activeChar.sendMessage("You must set the enchant level to be between 0-65535.");
			}
			else
			{
				setEnchant(activeChar, ench, armorType);
			}
		}
		catch (StringIndexOutOfBoundsException e)
		{
			activeChar.sendMessage("Please specify a new enchant value.");
		}
		catch (NumberFormatException e)
		{
			activeChar.sendMessage("Please specify a valid new enchant value.");
		}
		showMainPage(activeChar);
		return true;
	}
	
	/**
	 * Method setEnchant.
	 * @param activeChar Player
	 * @param ench int
	 * @param armorType int
	 */
	private void setEnchant(Player activeChar, int ench, int armorType)
	{
		GameObject target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		if (!target.isPlayer())
		{
			activeChar.sendMessage("Wrong target type.");
			return;
		}
		Player player = (Player) target;
		int curEnchant = 0;
		ItemInstance itemInstance = player.getInventory().getPaperdollItem(armorType);
		if (itemInstance != null)
		{
			curEnchant = itemInstance.getEnchantLevel();
			player.getInventory().unEquipItem(itemInstance);
			itemInstance.setEnchantLevel(ench);
			player.getInventory().equipItem(itemInstance);
			player.sendPacket(new InventoryUpdate().addModifiedItem(itemInstance));
			player.broadcastCharInfo();
			activeChar.sendMessage("Changed enchantment of " + player.getName() + "'s " + itemInstance.getName() + " from " + curEnchant + " to " + ench + ".");
			player.sendMessage("Admin has changed the enchantment of your " + itemInstance.getName() + " from " + curEnchant + " to " + ench + ".");
		}
	}
	
	/**
	 * Method showMainPage.
	 * @param activeChar Player
	 */
	public void showMainPage(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		Player player = activeChar;
		if (target.isPlayer())
		{
			player = (Player) target;
		}
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<center><table width=260><tr><td width=40>");
		replyMSG.append("<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		replyMSG.append("</td><td width=180>");
		replyMSG.append("<center>Enchant Equip for player: " + player.getName() + "</center>");
		replyMSG.append("</td><td width=40>");
		replyMSG.append("</td></tr></table></center><br>");
		replyMSG.append("<center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Shirt\" action=\"bypass -h admin_setun $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Helmet\" action=\"bypass -h admin_seteh $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Cloak\" action=\"bypass -h admin_setba $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Mask\" action=\"bypass -h admin_setha $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Necklace\" action=\"bypass -h admin_seten $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table>");
		replyMSG.append("</center><center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Weapon\" action=\"bypass -h admin_setew $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Chest\" action=\"bypass -h admin_setec $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Shield\" action=\"bypass -h admin_setes $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Earring\" action=\"bypass -h admin_setre $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Earring\" action=\"bypass -h admin_setle $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table>");
		replyMSG.append("</center><center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Gloves\" action=\"bypass -h admin_seteg $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Leggings\" action=\"bypass -h admin_setel $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Boots\" action=\"bypass -h admin_seteb $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Ring\" action=\"bypass -h admin_setrf $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Ring\" action=\"bypass -h admin_setlf $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table>");
		replyMSG.append("</center><center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Hair\" action=\"bypass -h admin_setdha $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"R-Bracelet\" action=\"bypass -h admin_setrbr $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"L-Bracelet\" action=\"bypass -h admin_setlbr $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Belt\" action=\"bypass -h admin_setbelt $menu_command\" width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table>");
		replyMSG.append("</center><br>");
		replyMSG.append("<center>[Enchant 0-65535]</center>");
		replyMSG.append("<center><edit var=\"menu_command\" width=100 height=15></center><br>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
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
}

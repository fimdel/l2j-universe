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
package services.community;

import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.item.EtcItemTemplate;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.BbsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnchantCommunity extends Functions implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _Instance.
	 */
	private static EnchantCommunity _Instance = null;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EnchantCommunity.class);
	
	/**
	 * Method getInstance.
	 * @return EnchantCommunity
	 */
	public static EnchantCommunity getInstance()
	{
		if (_Instance == null)
		{
			_Instance = new EnchantCommunity();
		}
		return _Instance;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.ENCHANT_ENABLED)
		{
			_log.info("CommunityBoard: Enchant Community service loaded.");
			CommunityBoardManager.getInstance().registerHandler(this);
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		if (Config.ENCHANT_ENABLED)
		{
			CommunityBoardManager.getInstance().removeHandler(this);
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method getBypassCommands.
	 * @return String[]
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]
		{
			"_bbsechant;"
		};
	}
	
	/**
	 * Method onBypassCommand.
	 * @param activeChar Player
	 * @param command String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onBypassCommand(Player, String)
	 */
	@Override
	public void onBypassCommand(Player activeChar, String command)
	{
		if (command.equals("_bbsechant;"))
		{
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			StringBuilder sb = new StringBuilder();
			sb.append("<table width=400>");
			ItemInstance[] arr = activeChar.getInventory().getItems();
			int len = arr.length;
			for (int i = 0; i < len; i++)
			{
				ItemInstance _item = arr[i];
				if ((_item == null) || (_item.getTemplate() instanceof EtcItemTemplate) || _item.getTemplate().isBelt() || _item.getTemplate().isUnderwear() || !_item.isEquipped() || _item.isHeroWeapon() || _item.getTemplate().isBracelet() || _item.getTemplate().isCloak() || (_item.getTemplate().getCrystalType() == ItemTemplate.Grade.NONE) || ((_item.getItemId() >= 7816) && (_item.getItemId() <= 7831)) || _item.isShadowItem() || _item.isCommonItem() || _item.isTemporalItem() || (_item.getEnchantLevel() >= (Config.MAX_ENCHANT + 1)))
				{
					continue;
				}
				sb.append("<tr><td><img src=icon." + _item.getTemplate().getIcon() + " width=32 height=32></td><td>");
				sb.append("<font color=\"LEVEL\">" + _item.getTemplate().getName() + " " + (_item.getEnchantLevel() <= 0 ? "" : "</font><br><font color=3293F3>Enchanted to: +" + _item.getEnchantLevel()) + "</font><br1>");
				sb.append("Enchant for: <font color=\"LEVEL\">" + name + "</font>");
				sb.append("<img src=\"l2ui.squaregray\" width=\"170\" height=\"1\">");
				sb.append("</td><td>");
				sb.append("<button value=\"Enchant\" action=\"bypass _bbsechant;enchlistpage;" + _item.getObjectId() + "\" width=75 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				sb.append("</td><td>");
				sb.append("<button value=\"Attribute\" action=\"bypass _bbsechant;enchlistpageAtrChus;" + _item.getObjectId() + "\" width=75 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				sb.append("</td></tr>");
			}
			sb.append("</table>");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if (command.startsWith("_bbsechant;enchlistpage;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int ItemForEchantObjID = Integer.parseInt(st.nextToken());
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(ItemForEchantObjID);
			StringBuilder sb = new StringBuilder();
			sb.append("Enchant selected ingredient?<br1><table width=300>");
			sb.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
			sb.append("<font color=\"LEVEL\">" + EhchantItem.getTemplate().getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Enchanted to: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");
			sb.append("Enchant for: <font color=\"LEVEL\">" + name + "</font>");
			sb.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
			sb.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<br>");
			sb.append("<br>");
			sb.append("<table border=0 width=400><tr><td width=200>");
			for (int i = 0; i < Config.ENCHANT_LEVELS.length; i++)
			{
				sb.append("<center><button value=\"Add +" + Config.ENCHANT_LEVELS[i] + " (Price:" + (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_PRICE_WPN[i] : Config.ENCHANT_PRICE_ARM[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgo;" + Config.ENCHANT_LEVELS[i] + ";" + (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_PRICE_WPN[i] : Config.ENCHANT_PRICE_ARM[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
				sb.append("<br1>");
			}
			sb.append("</td></tr></table><br1><button value=\"Back\" action=\"bypass _bbsechant;\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if (command.startsWith("_bbsechant;enchlistpageAtrChus;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int ItemForEchantObjID = Integer.parseInt(st.nextToken());
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(ItemForEchantObjID);
			StringBuilder sb = new StringBuilder();
			sb.append("Enchant selected ingredient?<br><table width=300>");
			sb.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
			sb.append("<font color=\"LEVEL\">" + EhchantItem.getTemplate().getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Enchanted to: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");
			sb.append("Enchant for: <font color=\"LEVEL\">" + name + "</font>");
			sb.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
			sb.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<br>");
			sb.append("<br>");
			sb.append("<table border=0 width=400><tr><td width=200>");
			sb.append("<center><img src=icon.etc_wind_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Wind \" action=\"bypass _bbsechant;enchlistpageAtr;2;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_earth_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Earth \" action=\"bypass _bbsechant;enchlistpageAtr;3;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_fire_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Fire \" action=\"bypass _bbsechant;enchlistpageAtr;0;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("</td><td width=200>");
			sb.append("<center><img src=icon.etc_water_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Water \" action=\"bypass _bbsechant;enchlistpageAtr;1;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_holy_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Divine \" action=\"bypass _bbsechant;enchlistpageAtr;4;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_unholy_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Dark \" action=\"bypass _bbsechant;enchlistpageAtr;5;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("</td></tr></table><br1><button value=\"Back\" action=\"bypass _bbsechant;\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if (command.startsWith("_bbsechant;enchlistpageAtr;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int AtributType = Integer.parseInt(st.nextToken());
			int ItemForEchantObjID = Integer.parseInt(st.nextToken());
			String ElementName = "";
			if (AtributType == 0)
			{
				ElementName = "Fire";
			}
			else if (AtributType == 1)
			{
				ElementName = "Water";
			}
			else if (AtributType == 2)
			{
				ElementName = "Wind";
			}
			else if (AtributType == 3)
			{
				ElementName = "Earth";
			}
			else if (AtributType == 4)
			{
				ElementName = "Divine";
			}
			else if (AtributType == 5)
			{
				ElementName = "Dark";
			}
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(ItemForEchantObjID);
			StringBuilder sb = new StringBuilder();
			sb.append("Chosen Element: <font color=\"LEVEL\">" + ElementName + "</font><br1> Enchant selected ingredient?<br1><table width=300>");
			sb.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
			sb.append("<font color=\"LEVEL\">" + EhchantItem.getTemplate().getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Enchant to: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");
			sb.append("Enchant for: <font color=\"LEVEL\">" + name + "</font>");
			sb.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
			sb.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<br1>");
			sb.append("<br1>");
			if (!EhchantItem.getTemplate().getName().contains("PvP") && ((EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S80) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S84) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R95) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R99)))
			{
				sb.append("<table border=0 width=400><tr><td width=200>");
				for (int i = 0; i < (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_ATTRIBUTE_LEVELS.length : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM.length); i++)
				{
					sb.append("<center><button value=\"Add +" + (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + " (Price:" + (EhchantItem.getTemplate().isWeapon() ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgoAtr;" + (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + ";" + AtributType + ";" + (EhchantItem.getTemplate().isWeapon() ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
					sb.append("<br1>");
				}
				sb.append("</td></tr></table><br1>");
			}
			else if (EhchantItem.getTemplate().getName().contains("PvP") && Config.ENCHANT_ATT_PVP && ((EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S80) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S84) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R95) || (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R99)))
			{
				sb.append("<table border=0 width=400><tr><td width=200>");
				for (int i = 0; i < (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_ATTRIBUTE_LEVELS.length : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM.length); i++)
				{
					sb.append("<center><button value=\"Add +" + (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + " (Price:" + (EhchantItem.getTemplate().isWeapon() ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgoAtr;" + (EhchantItem.getTemplate().isWeapon() ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + ";" + AtributType + ";" + (EhchantItem.getTemplate().isWeapon() ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
					sb.append("<br1>");
				}
				sb.append("</td></tr></table><br1>");
			}
			else
			{
				sb.append("<table border=0 width=400><tr><td width=200>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<center><font color=\"LEVEL\">Enchant is impossible!</font></center>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("</td></tr></table><br>");
			}
			sb.append("<button value=\"Back\" action=\"bypass _bbsechant;\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if (command.startsWith("_bbsechant;enchantgo;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int EchantVal = Integer.parseInt(st.nextToken());
			int EchantPrice = Integer.parseInt(st.nextToken());
			int EchantObjID = Integer.parseInt(st.nextToken());
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID);
			ItemInstance pay = activeChar.getInventory().getItemByItemId(item.getItemId());
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(EchantObjID);
			if ((pay != null) && (pay.getCount() >= EchantPrice))
			{
				activeChar.getInventory().destroyItem(pay, EchantPrice);
				if (EhchantItem.isEquipped())
				{
					activeChar.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
				}
				EhchantItem.setEnchantLevel(EchantVal);
				activeChar.getInventory().equipItem(EhchantItem);
				activeChar.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
				activeChar.broadcastUserInfo();
				activeChar.sendMessage("" + EhchantItem.getTemplate().getName() + " was enchanted to " + EchantVal + ". Thanks.");
				onBypassCommand(activeChar, "_bbsechant;");
			}
			else
			{
				activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		if (command.startsWith("_bbsechant;enchantgoAtr;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int EchantVal = Integer.parseInt(st.nextToken());
			int AtrType = Integer.parseInt(st.nextToken());
			Element el = Element.getElementById(AtrType);
			int EchantPrice = Integer.parseInt(st.nextToken());
			int EchantObjID = Integer.parseInt(st.nextToken());
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID);
			ItemInstance pay = activeChar.getInventory().getItemByItemId(item.getItemId());
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(EchantObjID);
			if (EhchantItem.isWeapon())
			{
				if ((pay != null) && (pay.getCount() >= EchantPrice))
				{
					activeChar.getInventory().destroyItem(pay, EchantPrice);
					if (EhchantItem.isEquipped())
					{
						activeChar.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
					}
					EhchantItem.setAttributeElement(el, EchantVal);
					activeChar.getInventory().equipItem(EhchantItem);
					activeChar.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
					activeChar.broadcastUserInfo();
					activeChar.sendMessage("" + EhchantItem.getTemplate().getName() + " was enchanted to " + EchantVal + ". Thanks.");
					onBypassCommand(activeChar, "_bbsechant;");
				}
				else
				{
					activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				}
			}
			else if (EhchantItem.isArmor())
			{
				if (!canEnchantArmorAttribute(AtrType, EhchantItem))
				{
					activeChar.sendMessage("attribute reservation by region?");
					onBypassCommand(activeChar, "_bbsechant;");
					return;
				}
				if ((pay != null) && (pay.getCount() >= EchantPrice))
				{
					activeChar.getInventory().destroyItem(pay, EchantPrice);
					if (EhchantItem.isEquipped())
					{
						activeChar.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
					}
					EhchantItem.setAttributeElement(el, EchantVal);
					activeChar.getInventory().equipItem(EhchantItem);
					activeChar.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
					activeChar.broadcastUserInfo();
					activeChar.sendMessage("" + EhchantItem.getTemplate().getName() + " was enchanted to " + EchantVal + ". Thanks.");
					onBypassCommand(activeChar, "_bbsechant;");
				}
			}
		}
	}
	
	/**
	 * Method onWriteCommand.
	 * @param player Player
	 * @param bypass String
	 * @param arg1 String
	 * @param arg2 String
	 * @param arg3 String
	 * @param arg4 String
	 * @param arg5 String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onWriteCommand(Player, String, String, String, String, String, String)
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
	
	/**
	 * Method canEnchantArmorAttribute.
	 * @param attr int
	 * @param item ItemInstance
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private boolean canEnchantArmorAttribute(int attr, ItemInstance item)
	{
		Element elm = Element.getElementById(attr);
		switch (attr)
		{
			case 0:
				if (item.getDefenceWater() != 0)
				{
					return false;
				}
				break;
			case 1:
				if (item.getDefenceFire() != 0)
				{
					return false;
				}
				break;
			case 2:
				if (item.getDefenceEarth() != 0)
				{
					return false;
				}
				break;
			case 3:
				if (item.getDefenceWind() != 0)
				{
					return false;
				}
				break;
			case 4:
				if (item.getDefenceUnholy() != 0)
				{
					return false;
				}
				break;
			case 5:
				if (item.getDefenceHoly() != 0)
				{
					return false;
				}
				break;
		}
		return true;
	}
}

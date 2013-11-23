/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import javolution.text.TextBuilder;
import l2p.gameserver.Config;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Element;
import l2p.gameserver.model.items.ItemAttributes;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.item.type.ItemGrade;
import l2p.gameserver.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringTokenizer;

public class CommunityBoardEnchant implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardEnchant.class);

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: Enchant service loaded.");
            CommunityBoardHandler.getInstance().registerHandler(this);
        }
    }

    @Override
    public void onReload() {
        if (Config.COMMUNITYBOARD_ENABLED)
            CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]{
                "_bbsechant",
                "_bbsechant;enchlistpage;",
                "_bbsechant;enchlistpageAtrChus;",
                "_bbsechant;enchlistpageAtr;",
                "_bbsechant;enchantgo;",
                "_bbsechant;enchantgoAtr;"};
    }

    @Override
    public void onBypassCommand(Player player, String command) {
        _log.info("command = " + command);
        if (command.equals("_bbsechant")) {
            String name = "None Name";
            name = ItemHolder.getInstance().getTemplate(Config.CBB_ENCHANT_ITEM).getName();
            String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/services/enchant.htm", player);
            TextBuilder html = new TextBuilder("");

            html.append("<table width=400>");
            ItemInstance[] arr = player.getInventory().getItems();
            int len = arr.length;
            for (int i = 0; i < len; i++) {
                ItemInstance _item = arr[i];
                //TODO добавить проверок на тип предмета
                if (_item == null || !_item.isEquipped() || _item.getEnchantLevel() >= Config.CBB_MAX_ENCHANT)
                    continue;
                html.append("<tr><td><img src=icon." + _item.getTemplate().getIcon() + " width=32 height=32></td><td>");
                html.append("<font color=\"LEVEL\">" + _item.getName() + " " + (_item.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + _item.getEnchantLevel()) + "</font><br1>");
                html.append("Заточка за: <font color=\"LEVEL\">" + name + "</font>");
                html.append("<img src=\"l2ui.squaregray\" width=\"170\" height=\"1\">");
                html.append("</td><td>");
                html.append("<button value=\"Обычная\" action=\"bypass _bbsechant;enchlistpage;" + _item.getObjectId() + "\" width=75 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
                html.append("</td><td>");
                html.append("<button value=\"Аттрибут\" action=\"bypass _bbsechant;enchlistpageAtrChus;" + _item.getObjectId() + "\" width=75 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
                html.append("</td></tr>");
            }
            html.append("</table>");

            content = content.replace("%enchant_content%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
        } else if (command.startsWith("_bbsechant;enchlistpage;")) {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            int ItemForEchantObjID = Integer.parseInt(st.nextToken());
            String name = "None Name";
            name = ItemHolder.getInstance().getTemplate(Config.CBB_ENCHANT_ITEM).getName();
            ItemInstance EhchantItem = player.getInventory().getItemByObjectId(ItemForEchantObjID);

            String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/services/enchant.htm", player);
            TextBuilder html = new TextBuilder("");

            html.append("Для обычной заточки выбрана вещь:<br1><table width=300>");
            html.append(("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>"));
            html.append("<font color=\"LEVEL\">" + EhchantItem.getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");
            html.append("Заточка производится за: <font color=\"LEVEL\">" + name + "</font>");
            html.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
            html.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
            html.append("</tr>");
            html.append("</table>");
            html.append("<br1>");
            html.append("<br1>");
            html.append("<table border=0 width=400><tr><td width=200>");
            for (int i = 0; i < Config.CBB_ENCHANT_LVL.length; i++) {
                html.append("<center><button value=\"На +" + Config.CBB_ENCHANT_LVL[i] + " (Цена:" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_PRICE_WEAPON[i] : Config.CBB_ENCHANT_PRICE_ARMOR[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgo;" + Config.CBB_ENCHANT_LVL[i] + ";" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_PRICE_WEAPON[i] : Config.CBB_ENCHANT_PRICE_ARMOR[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
                html.append("<br1>");
            }
            html.append("</td></tr></table><br1><button value=\"Назад\" action=\"bypass _bbsechant\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

            content = content.replace("%enchant_content%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
        } else if (command.startsWith("_bbsechant;enchlistpageAtrChus;")) {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            int ItemForEchantObjID = Integer.parseInt(st.nextToken());
            String name = "None Name";
            name = ItemHolder.getInstance().getTemplate(Config.CBB_ENCHANT_ITEM).getName();
            ItemInstance EhchantItem = player.getInventory().getItemByObjectId(ItemForEchantObjID);

            String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/services/enchant.htm", player);
            TextBuilder html = new TextBuilder("");

            html.append("Для заточки на атрибут выбрана вещь:<br1><table width=300>");
            html.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
            html.append("<font color=\"LEVEL\">" + EhchantItem.getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");
            html.append("Заточка производится за: <font color=\"LEVEL\">" + name + "</font>");
            html.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
            html.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
            html.append("</tr>");
            html.append("</table>");
            html.append("<br1>");
            html.append("<br1>");
            html.append("<table border=0 width=400><tr><td width=200>");
            html.append("<center><img src=icon.etc_wind_stone_i00 width=32 height=32></center><br1>");
            html.append("<button value=\"Wind \" action=\"bypass _bbsechant;enchlistpageAtr;2;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
            html.append("<br1><center><img src=icon.etc_earth_stone_i00 width=32 height=32></center><br1>");
            html.append("<button value=\"Earth \" action=\"bypass _bbsechant;enchlistpageAtr;3;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
            html.append("<br1><center><img src=icon.etc_fire_stone_i00 width=32 height=32></center><br1>");
            html.append("<button value=\"Fire \" action=\"bypass _bbsechant;enchlistpageAtr;0;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
            html.append("</td><td width=200>");
            html.append("<center><img src=icon.etc_water_stone_i00 width=32 height=32></center><br1>");
            html.append("<button value=\"Water \" action=\"bypass _bbsechant;enchlistpageAtr;1;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
            html.append("<br1><center><img src=icon.etc_holy_stone_i00 width=32 height=32></center><br1>");
            html.append("<button value=\"Divine \" action=\"bypass _bbsechant;enchlistpageAtr;4;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
            html.append("<br1><center><img src=icon.etc_unholy_stone_i00 width=32 height=32></center><br1>");
            html.append("<button value=\"Dark \" action=\"bypass _bbsechant;enchlistpageAtr;5;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
            html.append("</td></tr></table><br1><button value=\"Назад\" action=\"bypass _bbsechant\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

            content = content.replace("%enchant_content%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
        } else if (command.startsWith("_bbsechant;enchlistpageAtr;")) {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            int AtributType = Integer.parseInt(st.nextToken());
            int ItemForEchantObjID = Integer.parseInt(st.nextToken());
            String ElementName = "";
            if (AtributType == 0)
                ElementName = "Fire";
            else if (AtributType == 1)
                ElementName = "Water";
            else if (AtributType == 2)
                ElementName = "Wind";
            else if (AtributType == 3)
                ElementName = "Earth";
            else if (AtributType == 4)
                ElementName = "Divine";
            else if (AtributType == 5)
                ElementName = "Dark";
            String name = "None Name";
            name = ItemHolder.getInstance().getTemplate(Config.CBB_ENCHANT_ITEM).getName();
            ItemInstance EhchantItem = player.getInventory().getItemByObjectId(ItemForEchantObjID);

            String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/services/enchant.htm", player);
            TextBuilder html = new TextBuilder("");

            html.append("Выбран элемент: <font color=\"LEVEL\">" + ElementName + "</font><br1> Для заточки выбрана вещь:<br1><table width=300>");
            html.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
            html.append("<font color=\"LEVEL\">" + EhchantItem.getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");
            html.append("Заточка производится за: <font color=\"LEVEL\">" + name + "</font>");
            html.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
            html.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
            html.append("</tr>");
            html.append("</table>");
            html.append("<br1>");
            html.append("<br1>");
            if (EhchantItem.getTemplate().isAttributable() && (EhchantItem.getTemplate().getCrystalType() == ItemGrade.S || EhchantItem.getTemplate().getCrystalType() == ItemGrade.S80 || EhchantItem.getTemplate().getCrystalType() == ItemGrade.S84)) {
                html.append("<table border=0 width=400><tr><td width=200>");
                for (int i = 0; i < (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_LVL_WEAPON.length : Config.CBB_ENCHANT_ATRIBUTE_LVL_ARMOR.length); i++) {
                    html.append("<center><button value=\"На +" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_LVL_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_LVL_ARMOR[i]) + " (Цена:" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_PRICE_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_PRICE_ARMOR[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgoAtr;" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_LVL_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_LVL_ARMOR[i]) + ";" + AtributType + ";" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_PRICE_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_PRICE_ARMOR[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
                    html.append("<br1>");
                }
                html.append("</td></tr></table><br1>");
            } else if (!EhchantItem.getTemplate().isAttributable() && Config.CBB_ENCHANT_ATRIBUTE_PVP && (EhchantItem.getTemplate().getCrystalType() == ItemGrade.S || EhchantItem.getTemplate().getCrystalType() == ItemGrade.S80 || EhchantItem.getTemplate().getCrystalType() == ItemGrade.S84)) {
                html.append("<table border=0 width=400><tr><td width=200>");
                for (int i = 0; i < (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_LVL_WEAPON.length : Config.CBB_ENCHANT_ATRIBUTE_LVL_ARMOR.length); i++) {
                    html.append("<center><button value=\"На +" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_LVL_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_LVL_ARMOR[i]) + " (Цена:" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_PRICE_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_PRICE_ARMOR[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgoAtr;" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_LVL_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_LVL_ARMOR[i]) + ";" + AtributType + ";" + (EhchantItem.getTemplate().isWeapon() != false ? Config.CBB_ENCHANT_ATRIBUTE_PRICE_WEAPON[i] : Config.CBB_ENCHANT_ATRIBUTE_PRICE_ARMOR[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
                    html.append("<br1>");
                }
                html.append("</td></tr></table><br1>");
            } else {
                html.append("<table border=0 width=400><tr><td width=200>");
                html.append("<br1>");
                html.append("<br1>");
                html.append("<br1>");
                html.append("<br1>");
                html.append("<center><font color=\"LEVEL\">Заточка данной вещи не возможна!</font></center>");
                html.append("<br1>");
                html.append("<br1>");
                html.append("<br1>");
                html.append("<br1>");
                html.append("</td></tr></table><br1>");
            }
            html.append("<button value=\"Назад\" action=\"bypass _bbsechant\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

            content = content.replace("%enchant_content%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
        } else if (command.startsWith("_bbsechant;enchantgo;")) {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            int EchantVal = Integer.parseInt(st.nextToken());
            int EchantPrice = Integer.parseInt(st.nextToken());
            int EchantObjID = Integer.parseInt(st.nextToken());
            ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CBB_ENCHANT_ITEM);
            ItemInstance pay = player.getInventory().getItemByItemId(item.getItemId());
            ItemInstance EhchantItem = player.getInventory().getItemByObjectId(EchantObjID);
            if (pay != null && pay.getCount() >= EchantPrice) {
                player.getInventory().destroyItem(pay, EchantPrice);
                player.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
                EhchantItem.setEnchantLevel(EchantVal);
                player.getInventory().equipItem(EhchantItem);
                player.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
                player.broadcastUserInfo(true);
                player.sendMessage("" + EhchantItem.getName() + " было заточено до " + EchantVal + ". Спасибо.");
                Log.add(player.getName() + " enchant item:" + EhchantItem.getName() + " val: " + EchantVal + "", "wmzSeller");
                String pBypass = "_bbsechant";
                if (pBypass != null) {
                    ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
                    if (handler != null)
                        handler.onBypassCommand(player, pBypass);
                }
            } else
                player.sendMessage("Incorrect items count");
        } else if (command.startsWith("_bbsechant;enchantgoAtr;")) {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            st.nextToken();
            int EchantVal = Integer.parseInt(st.nextToken());
            int AtrType = Integer.parseInt(st.nextToken());
            int EchantPrice = Integer.parseInt(st.nextToken());
            int EchantObjID = Integer.parseInt(st.nextToken());
            ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CBB_ENCHANT_ITEM);
            ItemInstance pay = player.getInventory().getItemByItemId(item.getItemId());
            ItemInstance EhchantItem = player.getInventory().getItemByObjectId(EchantObjID);
            if (EhchantItem.isWeapon()) {
                if (pay != null && pay.getCount() >= EchantPrice) {
                    player.getInventory().destroyItem(pay, EchantPrice);
                    player.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
                    EhchantItem.setAttributeElement(Element.getElementById(AtrType), EchantVal);
                    player.getInventory().equipItem(EhchantItem);
                    player.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
                    player.broadcastUserInfo(true);
                    player.sendMessage("" + EhchantItem.getName() + " было заточено до " + EchantVal + ". Спасибо.");
                    Log.add(player.getName() + " enchant item:" + EhchantItem.getName() + " val: " + EchantVal + " AtributType:" + AtrType, "wmzSeller");
                    String pBypass = "_bbsechant";
                    if (pBypass != null) {
                        ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
                        if (handler != null)
                            handler.onBypassCommand(player, pBypass);
                    }
                } else
                    player.sendMessage("инкоррект итем каунт блядь");
            } else if (EhchantItem.isArmor()) {
                if (!canEnchantArmorAttribute(AtrType, EhchantItem)) {
                    player.sendMessage("Невозможно вставить аттрибут, не соблюдены условия");
                    return;
                }
                if (pay != null && pay.getCount() >= EchantPrice) {
                    player.getInventory().destroyItem(pay, EchantPrice);
                    player.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
                    ItemAttributes deffAttr = EhchantItem.getAttributes();
                    deffAttr.setValue(Element.getElementById(AtrType), EchantVal);
                    EhchantItem.setAttributes(deffAttr);
                    player.getInventory().equipItem(EhchantItem);
                    player.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
                    player.broadcastUserInfo(true);
                    player.sendMessage("" + EhchantItem.getName() + " было заточено до " + EchantVal + ". Спасибо.");
                    Log.add(player.getName() + " enchant item:" + EhchantItem.getName() + " val: " + EchantVal + " AtributType:" + AtrType, "wmzSeller");
                    String pBypass = "_bbsechant";
                    if (pBypass != null) {
                        ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
                        if (handler != null)
                            handler.onBypassCommand(player, pBypass);
                    }
                }
            }
        } else if (player.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br>", player);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
    }

    private boolean canEnchantArmorAttribute(int attr, ItemInstance item) {
        switch (attr) {
            case 0:
                if (item.getAttributeElementValue(Element.getReverseElement(Element.getElementById(0)), false) != 0)
                    return false;
                break;
            case 1:
                if (item.getAttributeElementValue(Element.getReverseElement(Element.getElementById(1)), false) != 0)
                    return false;
                break;
            case 2:
                if (item.getAttributeElementValue(Element.getReverseElement(Element.getElementById(2)), false) != 0)
                    return false;
                break;
            case 3:
                if (item.getAttributeElementValue(Element.getReverseElement(Element.getElementById(3)), false) != 0)
                    return false;
                break;
            case 4:
                if (item.getAttributeElementValue(Element.getReverseElement(Element.getElementById(4)), false) != 0)
                    return false;
                break;
            case 5:
                if (item.getAttributeElementValue(Element.getReverseElement(Element.getElementById(5)), false) != 0)
                    return false;
                break;
        }
        return true;
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
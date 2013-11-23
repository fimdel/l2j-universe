package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminEnchant implements IAdminCommandHandler {
    private static enum Commands {
        admin_seteh, // 6
        admin_setec, // 10
        admin_seteg, // 9
        admin_setel, // 11
        admin_seteb, // 12
        admin_setew, // 7
        admin_setes, // 8
        admin_setle, // 1
        admin_setre, // 2
        admin_setlf, // 4
        admin_setrf, // 5
        admin_seten, // 3
        admin_setun, // 0
        admin_setba,
        admin_setha,
        admin_setdha,
        admin_setlbr,
        admin_setrbr,
        admin_setbelt,
        admin_enchant
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().CanEditChar)
            return false;

        int armorType = -1;

        switch (command) {
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

        if (armorType == -1 || wordList.length < 2) {
            showMainPage(activeChar);
            return true;
        }

        try {
            int ench = Integer.parseInt(wordList[1]);
            if (ench < 0 || ench > 65535)
                activeChar.sendMessage("You must set the enchant level to be between 0-65535.");
            else
                setEnchant(activeChar, ench, armorType);
        } catch (StringIndexOutOfBoundsException e) {
            activeChar.sendMessage("Please specify a new enchant value.");
        } catch (NumberFormatException e) {
            activeChar.sendMessage("Please specify a valid new enchant value.");
        }

        // show the enchant menu after an action
        showMainPage(activeChar);
        return true;
    }

    private void setEnchant(Player activeChar, int ench, int armorType) {
        // get the target
        GameObject target = activeChar.getTarget();
        if (target == null)
            target = activeChar;
        if (!target.isPlayer()) {
            activeChar.sendMessage("Wrong target type.");
            return;
        }

        Player player = (Player) target;

        // now we need to find the equipped weapon of the targeted character...
        int curEnchant = 0; // display purposes only

        // only attempt to enchant if there is a weapon equipped
        ItemInstance itemInstance = player.getInventory().getPaperdollItem(armorType);

        if (itemInstance != null) {
            curEnchant = itemInstance.getEnchantLevel();

            // set enchant value
            player.getInventory().unEquipItem(itemInstance);
            itemInstance.setEnchantLevel(ench);
            player.getInventory().equipItem(itemInstance);

            // send packets
            player.sendPacket(new InventoryUpdate().addModifiedItem(itemInstance));
            player.broadcastCharInfo();

            // informations
            activeChar.sendMessage("Changed enchantment of " + player.getName() + "'s " + itemInstance.getName() + " from " + curEnchant + " to " + ench + ".");
            player.sendMessage("Admin has changed the enchantment of your " + itemInstance.getName() + " from " + curEnchant + " to " + ench + ".");
        }
    }

    public void showMainPage(Player activeChar) {
        // get the target
        GameObject target = activeChar.getTarget();
        if (target == null)
            target = activeChar;
        Player player = activeChar;
        if (target.isPlayer())
            player = (Player) target;

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><body>");
        replyMSG.append("<center><table width=260><tr><td width=40>");
        replyMSG.append("<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
        replyMSG.append("</td><td width=220>");
        replyMSG.append("<center>Enchant player: " + player.getName() + "</center>");
        replyMSG.append("</td>");
        replyMSG.append("</tr></table></center><br>");
        replyMSG.append("<table width=260><tr><td width=50><center>[0-65535]</center></td>");
        replyMSG.append("<td width=190><center><edit var=\"menu_command\" width=190 height=15></center></td>");
        replyMSG.append("</tr></table><br><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_seteh 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_seteh 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Helmet\" action=\"bypass -h admin_seteh $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Mask\" action=\"bypass -h admin_setha $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setha 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setha 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setec 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setec 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Chest\" action=\"bypass -h admin_setec $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Hair\" action=\"bypass -h admin_setdha $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setdha 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setdha 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setel 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setel 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Leggings\" action=\"bypass -h admin_setel $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Earring\" action=\"bypass -h admin_setre $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setre 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setre 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_seteg 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_seteg 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Gloves\" action=\"bypass -h admin_seteg $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Earring\" action=\"bypass -h admin_setle $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setle 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setle 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_seteb 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_seteb 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Boots\" action=\"bypass -h admin_seteb $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Necklace\" action=\"bypass -h admin_seten $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_seten 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_seten 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setba 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setba 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Cloak\" action=\"bypass -h admin_setba $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Ring\" action=\"bypass -h admin_setrf $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setrf 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setrf 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setun 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setun 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Shirt\" action=\"bypass -h admin_setun $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Ring\" action=\"bypass -h admin_setlf $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setlf 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setlf 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setbelt 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setbelt 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Belt\" action=\"bypass -h admin_setbelt $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"R-Bracelet\" action=\"bypass -h admin_setrbr $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setrbr 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setrbr 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center><center><table width=270><tr>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setew 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setew 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Weapon\" action=\"bypass -h admin_setew $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Shield\" action=\"bypass -h admin_setes $menu_command\" width=70 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+20\" action=\"bypass -h admin_setes 20\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"+3\" action=\"bypass -h admin_setes 3\" width=30 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table></center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}

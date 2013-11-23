/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.MerchantInstance;
import l2p.gameserver.network.serverpackets.PackageToList;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.WarehouseFunctions;

/**
 * @author VISTALL
 * @date 20:32/16.05.2011
 */
public class FreightSenderInstance extends MerchantInstance {
    /**
     *
     */
    private static final long serialVersionUID = -2189525902583757257L;

    public FreightSenderInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("deposit_items"))
            player.sendPacket(new PackageToList(player));
        else if (command.equalsIgnoreCase("withdraw_items"))
            WarehouseFunctions.showFreightWindow(player);
        else if (command.equalsIgnoreCase("ReceivePremium")) {
            if (player.getLevel() >= 1 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel1") == null) {
                    ItemFunctions.addItem(player, 20418, 1, true); //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel1", "@give", -1);
                }
            }
            if (player.getLevel() >= 20 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel20") == null) {
                    ItemFunctions.addItem(player, 22578, 1, true);                            //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel20", "@give", -1);
                }
            }
            if (player.getLevel() >= 40 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel40") == null) {
                    ItemFunctions.addItem(player, 22583, 1, true);
                    ItemFunctions.addItem(player, 22600, 1, true);
                    ItemFunctions.addItem(player, 22592, 1, true);
                    ItemFunctions.addItem(player, 33800, 1, true);    //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel40", "@give", -1);
                }
            }
            if (player.getLevel() >= 45 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel45") == null) {
                    ItemFunctions.addItem(player, 22579, 1, true); //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel45", "@give", -1);
                }
            }
            if (player.getLevel() >= 50 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel50") == null) {
                    ItemFunctions.addItem(player, 22584, 1, true);
                    ItemFunctions.addItem(player, 22594, 1, true);
                    ItemFunctions.addItem(player, 22602, 1, true);                            //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel50", "@give", -1);
                }
            } else if (player.getLevel() >= 55 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel55") == null) {
                    ItemFunctions.addItem(player, 22594, 1, true);
                    ItemFunctions.addItem(player, 22602, 1, true);
                    ItemFunctions.addItem(player, 22580, 1, true);
                    //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel55", "@give", -1);
                }
            }
            if (player.getLevel() >= 60 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel60") == null) {
                    ItemFunctions.addItem(player, 22585, 1, true);
                    ItemFunctions.addItem(player, 22596, 1, true);
                    ItemFunctions.addItem(player, 22604, 1, true);                            //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel60", "@give", -1);
                }
            }
            if (player.getLevel() >= 65 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel65") == null) {
                    ItemFunctions.addItem(player, 22596, 1, true);
                    ItemFunctions.addItem(player, 22604, 1, true);//ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel65", "@give", -1);
                }
            }
            if (player.getLevel() >= 70 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel70") == null) {
                    ItemFunctions.addItem(player, 22596, 1, true);
                    ItemFunctions.addItem(player, 22604, 1, true);
                    ItemFunctions.addItem(player, 22581, 1, true);                            //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel70", "@give", -1);
                }
            }
            if (player.getLevel() >= 76 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel76") == null) {
                    ItemFunctions.addItem(player, 20863, 1, true);
                    ItemFunctions.addItem(player, 22586, 1, true);
                    ItemFunctions.addItem(player, 21816, 1, true);
                    ItemFunctions.addItem(player, 22619, 1, true);//ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel76", "@give", -1);
                }
            }
            if (player.getLevel() >= 80 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel80") == null) {
                    ItemFunctions.addItem(player, 23127, 1, true);
                    ItemFunctions.addItem(player, 23128, 1, true);
                    ItemFunctions.addItem(player, 23129, 1, true);
                    ItemFunctions.addItem(player, 20448, 1, true);

                    player.setVar("ItemLevel80", "@give", -1);
                }
            }
            if (player.getLevel() >= 85 & player.getLevel() <= 99) {
                if (player.getVar("ItemLevel85") == null) {
                    ItemFunctions.addItem(player, 20429, 1, true); //ItemFunctions.addItem(player, itemId, count, boolean saveInBD); Где player - кому, itemId - ID, count - кол-во,
                    player.setVar("ItemLevel85", "@give", -1);
                }
            }
        } else
            super.onBypassFeedback(player, command);
    }
}

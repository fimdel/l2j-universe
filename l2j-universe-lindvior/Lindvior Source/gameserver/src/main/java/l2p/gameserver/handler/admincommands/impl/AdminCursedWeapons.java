package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.instancemanager.CursedWeaponsManager;
import l2p.gameserver.model.CursedWeapon;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.utils.ItemFunctions;

public class AdminCursedWeapons implements IAdminCommandHandler {
    private static enum Commands {
        admin_cw_info,
        admin_cw_remove,
        admin_cw_goto,
        admin_cw_reload,
        admin_cw_add,
        admin_cw_drop
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().Menu)
            return false;

        CursedWeaponsManager cwm = CursedWeaponsManager.getInstance();

        CursedWeapon cw = null;
        switch (command) {
            case admin_cw_remove:
            case admin_cw_goto:
            case admin_cw_add:
            case admin_cw_drop:
                if (wordList.length < 2) {
                    activeChar.sendMessage("Вы не указали id");
                    return false;
                }
                for (CursedWeapon cwp : CursedWeaponsManager.getInstance().getCursedWeapons())
                    if (cwp.getName().toLowerCase().contains(wordList[1].toLowerCase()))
                        cw = cwp;
                if (cw == null) {
                    activeChar.sendMessage("Неизвестный id");
                    return false;
                }
                break;
            default:
                break;
        }

        switch (command) {
            case admin_cw_info:
                activeChar.sendMessage("======= Cursed Weapons: =======");
                for (CursedWeapon c : cwm.getCursedWeapons()) {
                    activeChar.sendMessage("> " + c.getName() + " (" + c.getItemId() + ")");
                    if (c.isActivated()) {
                        Player pl = c.getPlayer();
                        activeChar.sendMessage("  Player holding: " + pl.getName());
                        activeChar.sendMessage("  Player karma: " + c.getPlayerKarma());
                        activeChar.sendMessage("  Time Remaining: " + c.getTimeLeft() / 60000 + " min.");
                        activeChar.sendMessage("  Kills : " + c.getNbKills());
                    } else if (c.isDropped()) {
                        activeChar.sendMessage("  Lying on the ground.");
                        activeChar.sendMessage("  Time Remaining: " + c.getTimeLeft() / 60000 + " min.");
                        activeChar.sendMessage("  Kills : " + c.getNbKills());
                    } else
                        activeChar.sendMessage("  Don't exist in the world.");
                }
                break;
            case admin_cw_reload:
                activeChar.sendMessage("Cursed weapons can't be reloaded.");
                break;
            case admin_cw_remove:
                if (cw == null)
                    return false;
                CursedWeaponsManager.getInstance().endOfLife(cw);
                break;
            case admin_cw_goto:
                if (cw == null)
                    return false;
                activeChar.teleToLocation(cw.getLoc());
                break;
            case admin_cw_add:
                if (cw == null)
                    return false;
                if (cw.isActive())
                    activeChar.sendMessage("This cursed weapon is already active.");
                else {
                    GameObject target = activeChar.getTarget();
                    if (target != null && target.isPlayer() && !((Player) target).isInOlympiadMode()) {
                        Player player = (Player) target;
                        ItemInstance item = ItemFunctions.createItem(cw.getItemId());
                        cwm.activate(player, player.getInventory().addItem(item));
                        cwm.showUsageTime(player, cw);
                    }
                }
                break;
            case admin_cw_drop:
                if (cw == null)
                    return false;
                if (cw.isActive())
                    activeChar.sendMessage("This cursed weapon is already active.");
                else {
                    GameObject target = activeChar.getTarget();
                    if (target != null && target.isPlayer() && !((Player) target).isInOlympiadMode()) {
                        Player player = (Player) target;
                        cw.create(null, player);
                    }
                }
                break;
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}

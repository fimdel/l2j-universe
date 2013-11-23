/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Experience;
import l2p.gameserver.model.instances.PetInstance;
import l2p.gameserver.tables.PetDataTable;

public class AdminLevel implements IAdminCommandHandler {
    private static enum Commands {
        admin_add_level, admin_addLevel, admin_set_level, admin_setLevel,
    }

    private void setLevel(Player activeChar, GameObject target, int level) {
        if (target == null || !(target.isPlayer() || target.isPet())) {
            activeChar.sendPacket(Msg.INVALID_TARGET);
            return;
        }
        if (level < 1 || level > Experience.getMaxLevel()) {
            activeChar.sendMessage("You must specify level 1 - " + Experience.getMaxLevel());
            return;
        }
        if (target.isPlayer()) {
            Long exp_add = Experience.LEVEL[level] - ((Player) target).getExp();
            ((Player) target).addExpAndSp(exp_add, 0);
            return;
        }
        if (target.isPet()) {
            Long exp_add = PetDataTable.getInstance().getInfo(((PetInstance) target).getNpcId(), level).getExp() - ((PetInstance) target).getExp();
            ((PetInstance) target).addExpAndSp(exp_add, 0);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().CanEditChar)
            return false;

        GameObject target = activeChar.getTarget();
        if (target == null || !(target.isPlayer() || target.isPet())) {
            activeChar.sendPacket(Msg.INVALID_TARGET);
            return false;
        }
        int level;

        switch (command) {
            case admin_add_level:
            case admin_addLevel:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //addLevel level");
                    return false;
                }
                try {
                    level = Integer.parseInt(wordList[1]);
                } catch (NumberFormatException e) {
                    activeChar.sendMessage("You must specify level");
                    return false;
                }
                setLevel(activeChar, target, level + ((Creature) target).getLevel());
                break;
            case admin_set_level:
            case admin_setLevel:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //setLevel level");
                    return false;
                }
                try {
                    level = Integer.parseInt(wordList[1]);
                } catch (NumberFormatException e) {
                    activeChar.sendMessage("You must specify level");
                    return false;
                }
                setLevel(activeChar, target, level);
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

package handler.admincommands;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 * @date 2:52/29.06.2011
 */
public class AdminTeam extends ScriptAdminCommand {
    enum Commands {
        admin_setteam
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        TeamType team = TeamType.NONE;
        if (wordList.length >= 2)
            for (TeamType t : TeamType.values())
                if (wordList[1].equalsIgnoreCase(t.name()))
                    team = t;

        GameObject object = activeChar.getTarget();
        if (object == null || !object.isCreature()) {
            activeChar.sendPacket(SystemMsg.INVALID_TARGET);
            return false;
        }

        ((Creature) object).setTeam(team);
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}

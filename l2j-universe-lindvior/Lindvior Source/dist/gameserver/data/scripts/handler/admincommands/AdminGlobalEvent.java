package handler.admincommands;

import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 * @date 18:45/07.06.2011
 */
public class AdminGlobalEvent extends ScriptAdminCommand {
    enum Commands {
        admin_list_events
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands c = (Commands) comm;
        switch (c) {
            case admin_list_events:
                GameObject object = activeChar.getTarget();
                if (object == null)
                    activeChar.sendPacket(SystemMsg.INVALID_TARGET);
                else
                    for (GlobalEvent e : object.getEvents())
                        activeChar.sendMessage("- " + e.toString());
                break;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}

package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminEvents implements IAdminCommandHandler {
    private static enum Commands {
        admin_events,
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().IsEventGm)
            return false;

        switch (command) {
            case admin_events:
                if (wordList.length == 1)
                    activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/events/events.htm"));
                else
                    activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/events/" + wordList[1].trim()));
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

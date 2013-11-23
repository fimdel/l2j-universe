/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2012.
 */

package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.Config;
import l2p.gameserver.geodata.geoeditorcon.GeoEditorListener;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.Player;

import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 04.11.12
 * Time: 15:01
 */
public class AdminGeoEditor implements IAdminCommandHandler {

    private static enum Commands {
        admin_ge_status,
        admin_ge_mode,
        admin_ge_join,
        admin_ge_leave
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;
        if (!Config.ACCEPT_GEOEDITOR_CONN) {
            activeChar.sendMessage("Server do not accepts geoeditor connections now.");
            return true;
        }
        if (fullString.startsWith("admin_ge_status")) {
            activeChar.sendMessage(GeoEditorListener.getInstance().getStatus());
        } else if (fullString.startsWith("admin_ge_mode")) {
            if (GeoEditorListener.getInstance().getThread() == null) {
                activeChar.sendMessage("Geoeditor not connected.");
                return true;
            }
            try {
                String val = fullString.substring("admin_ge_mode".length());
                StringTokenizer st = new StringTokenizer(val);

                if (st.countTokens() < 1) {
                    activeChar.sendMessage("Usage: //ge_mode X");
                    activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
                    activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
                    activeChar.sendMessage("Mode 2: Send coordinates each second.");
                    return true;
                }
                int m;
                m = Integer.parseInt(st.nextToken());
                GeoEditorListener.getInstance().getThread().setMode(m);
                activeChar.sendMessage("Geoeditor connection mode set to " + m + ".");
            } catch (Exception e) {
                activeChar.sendMessage("Usage: //ge_mode X");
                activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
                activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
                activeChar.sendMessage("Mode 2: Send coordinates each second.");
                e.printStackTrace();
            }
            return true;
        } else if (command.equals("admin_ge_join")) {
            if (GeoEditorListener.getInstance().getThread() == null) {
                activeChar.sendMessage("Geoeditor not connected.");
                return true;
            }
            GeoEditorListener.getInstance().getThread().addGM(activeChar);
            activeChar.sendMessage("You added to list for geoeditor.");
        } else if (command.equals("admin_ge_leave")) {
            if (GeoEditorListener.getInstance().getThread() == null) {
                activeChar.sendMessage("Geoeditor not connected.");
                return true;
            }
            GeoEditorListener.getInstance().getThread().removeGM(activeChar);
            activeChar.sendMessage("You removed from list for geoeditor.");
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}

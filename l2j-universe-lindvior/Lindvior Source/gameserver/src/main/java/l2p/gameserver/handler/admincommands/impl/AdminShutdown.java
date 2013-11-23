package l2p.gameserver.handler.admincommands.impl;

import l2p.commons.lang.StatsUtils;
import l2p.gameserver.Config;
import l2p.gameserver.GameTimeController;
import l2p.gameserver.Shutdown;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminShutdown implements IAdminCommandHandler {
    private static enum Commands {
        admin_server_shutdown,
        admin_server_restart,
        admin_server_abort
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().CanRestart)
            return false;

        try {
            switch (command) {
                case admin_server_shutdown:
                    Shutdown.getInstance().schedule(NumberUtils.toInt(wordList[1], -1), Shutdown.SHUTDOWN);
                    break;
                case admin_server_restart:
                    Shutdown.getInstance().schedule(NumberUtils.toInt(wordList[1], -1), Shutdown.RESTART);
                    break;
                case admin_server_abort:
                    Shutdown.getInstance().cancel();
                    break;
            }
        } catch (Exception e) {
            sendHtmlForm(activeChar);
        }

        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }

    private void sendHtmlForm(Player activeChar) {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        int t = GameTimeController.getInstance().getGameTime();
        int h = t / 60;
        int m = t % 60;
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);

        StringBuilder replyMSG = new StringBuilder("<html><body>");
        replyMSG.append("<table width=260><tr>");
        replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td width=180><center>Server Management Menu</center></td>");
        replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("</tr></table>");
        replyMSG.append("<br><br>");
        replyMSG.append("<table>");
        replyMSG.append("<tr><td>Players Online: " + GameObjectsStorage.getAllPlayersCount() + "</td></tr>");
        replyMSG.append("<tr><td>Used Memory: " + StatsUtils.getMemUsedMb() + "</td></tr>");
        replyMSG.append("<tr><td>Server Rates: " + Config.RATE_XP + "x, " + Config.RATE_SP + "x, " + Config.RATE_DROP_ADENA + "x, " + Config.RATE_DROP_ITEMS + "x</td></tr>");
        replyMSG.append("<tr><td>Game Time: " + format.format(cal.getTime()) + "</td></tr>");
        replyMSG.append("</table><br>");
        replyMSG.append("<table width=270>");
        replyMSG.append("<tr><td>Enter in seconds the time till the server shutdowns bellow:</td></tr>");
        replyMSG.append("<br>");
        replyMSG.append("<tr><td><center>Seconds till: <edit var=\"shutdown_time\" width=60></center></td></tr>");
        replyMSG.append("</table><br>");
        replyMSG.append("<center><table><tr><td>");
        replyMSG.append("<button value=\"Shutdown\" action=\"bypass -h admin_server_shutdown $shutdown_time\" width=60 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
        replyMSG.append("<button value=\"Restart\" action=\"bypass -h admin_server_restart $shutdown_time\" width=60 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
        replyMSG.append("<button value=\"Abort\" action=\"bypass -h admin_server_abort\" width=60 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
        replyMSG.append("</td></tr></table></center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }
}

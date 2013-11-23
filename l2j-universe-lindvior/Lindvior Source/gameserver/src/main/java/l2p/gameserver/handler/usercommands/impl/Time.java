package l2p.gameserver.handler.usercommands.impl;

import l2p.gameserver.Config;
import l2p.gameserver.GameTimeController;
import l2p.gameserver.handler.usercommands.IUserCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Support for /time command
 */
public class Time implements IUserCommandHandler {
    private static final int[] COMMAND_IDS = {77};

    private static final NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
    private static final SimpleDateFormat sf = new SimpleDateFormat("H:mm");

    static {
        df.setMinimumIntegerDigits(2);
    }

    @Override
    public boolean useUserCommand(int id, Player activeChar) {
        if (COMMAND_IDS[0] != id)
            return false;

        int h = GameTimeController.getInstance().getGameHour();
        int m = GameTimeController.getInstance().getGameMin();

        SystemMessage sm;
        if (GameTimeController.getInstance().isNowNight())
            sm = new SystemMessage(SystemMessage.THE_CURRENT_TIME_IS_S1S2_IN_THE_NIGHT);
        else
            sm = new SystemMessage(SystemMessage.THE_CURRENT_TIME_IS_S1S2_IN_THE_DAY);
        sm.addString(df.format(h)).addString(df.format(m));

        activeChar.sendPacket(sm);

        if (Config.ALT_SHOW_SERVER_TIME)
            activeChar.sendMessage(new CustomMessage("usercommandhandlers.Time.ServerTime", activeChar, sf.format(new Date(System.currentTimeMillis()))));

        return true;
    }

    @Override
    public final int[] getUserCommandList() {
        return COMMAND_IDS;
    }
}

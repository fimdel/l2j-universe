package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.09.12
 * Time: 17:06
 */
public class RequestExEscapeScene extends L2GameClientPacket {
    protected void readImpl() {
    }

    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        activeChar.sendActionFailed();
        activeChar.setIsInMovie(false);
    }

    public String getType() {
        return "[C] D0:93 RequestExEscapeScene";
    }
}

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;

/**
 * @author ALF
 * @data 09.02.2012
 */
public class RequestEx2ndPasswordVerify extends L2GameClientPacket {
    private String _password;

    @Override
    protected void readImpl() {
        _password = readS();
    }

    @Override
    protected void runImpl() {
        if (!Config.SECOND_AUTH_ENABLED)
            return;

        getClient().getSecondaryAuth().checkPassword(_password, false);
    }
}

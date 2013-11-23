package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.network.serverpackets.Ex2ndPasswordCheck;

/**
 * @author ALF
 * @data 09.02.2012
 */
public class RequestEx2ndPasswordCheck extends L2GameClientPacket {
    @Override
    protected void readImpl() {
        //
    }

    @Override
    protected void runImpl() {
        if (!Config.SECOND_AUTH_ENABLED || getClient().getSecondaryAuth().isAuthed()) {
            sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_OK));
            return;
        }

        getClient().getSecondaryAuth().openDialog();
        sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_OK));
    }
}

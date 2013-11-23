package l2p.gameserver.loginservercon.lspackets;

import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.loginservercon.ReceivablePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServerFail extends ReceivablePacket {
    private static final Logger _log = LoggerFactory.getLogger(LoginServerFail.class);

    private static final String[] reasons = {
            "none",
            "IP banned",
            "IP reserved",
            "wrong hexid",
            "ID reserved",
            "no free ID",
            "not authed",
            "already logged in"};
    private int _reason;

    public String getReason() {
        return reasons[_reason];
    }

    @Override
    protected void readImpl() {
        _reason = readC();
    }

    @Override
    protected void runImpl() {
        _log.warn("Authserver registration failed! Reason: " + getReason());
        LoginServerCommunication.getInstance().restart();
    }
}
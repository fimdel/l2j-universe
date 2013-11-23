package l2p.gameserver.ccpGuard;


import l2p.gameserver.ccpGuard.crypt.ProtectionCrypt;
import l2p.gameserver.network.GameClient;

import static l2p.gameserver.ccpGuard.commons.utils.Util.intToBytes;


public class ProtectInfo {

    public static enum ProtectionClientState {

        ENABLED, DISABLED, LIMITED
    }

    private ProtectionClientState protectState;
    private GameClient client;
    private int _protectPenalty = 0;
    private String _loginName = "";
    private String _playerName = "";
    private int _playerId = 0;
    private String HWID = "";
    private String HWIDSec = "";
    private String IP = "";
    public final ProtectionCrypt GGdec = new ProtectionCrypt();
    private boolean GameGuardStatus = true;
    public boolean GGReaded = false;
    public boolean protect_used = false;

    public ProtectInfo(GameClient client, String ip, boolean offline) {
        protect_used = ConfigProtect.PROTECT_ENABLE;

        byte[] newKey = new byte[24];
        for (int i = 0; i < 6; i++) {
            int val = ProtectionCrypt.getValue(i);
            intToBytes(val, newKey, i * 4);
        }
        GGdec.setKey(newKey);

        if (offline) {
            protectState = ProtectionClientState.ENABLED;
        } else {
            protectState = ProtectionClientState.DISABLED;
        }

        IP = ip;
        if (protect_used) {
            protect_used = !ConfigProtect.PROTECT_UNPROTECTED_IPS.isIpInNets(ip);
            this.client = client;
        }
    }

    public final GameClient getClient() {
        return client;
    }

    public final boolean getGGStatus() {
        return GameGuardStatus;
    }

    public final void setGGStatus(boolean st) {
        GameGuardStatus = st;
    }

    public final String getHWID() {
        return HWID;
    }

    public void setHWID(final String hwid) {
        HWID = hwid;
    }

    public final String getHWIDSec() {
        return HWIDSec;
    }

    public void setHWIDSec(final String hwid) {
        HWIDSec = hwid;
    }

    public int getProtectPenalty() {
        return _protectPenalty;
    }

    public void setProtectPenalty(int protectPenalty) {
        _protectPenalty = protectPenalty;
    }

    public void addProtectPenalty(int protectPenalty) {
        _protectPenalty = _protectPenalty + protectPenalty;
    }

    public ProtectionClientState getProtectState() {
        return protectState;
    }

    public void setProtectState(ProtectionClientState protectState) {
        this.protectState = protectState;
    }

    public final String getLoginName() {
        return _loginName;
    }

    public void setLoginName(final String name) {
        _loginName = name;
    }

    public final String getPlayerName() {
        return _playerName;
    }

    public void setPlayerName(final String name) {
        _playerName = name;
    }

    public int getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(int plId) {
        _playerId = plId;
    }

    public String getIpAddr() {
        return IP;
    }

    public String toString() {
        if (_playerName.isEmpty() && _playerId == 0) {
            if (_loginName.isEmpty() && HWID.isEmpty()) {
                if (!IP.isEmpty()) {
                    return "client - " + IP;
                } else {
                    return "";
                }
            } else {
                return "LoginName:" + _loginName + "|HWID:" + HWID + "|IP:" + IP;
            }
        } else {
            return "PlayerName:" + _playerName + "[" + String.valueOf(_playerId) + "]HWID:" + HWID + "|IP:" + IP;
        }
    }
}

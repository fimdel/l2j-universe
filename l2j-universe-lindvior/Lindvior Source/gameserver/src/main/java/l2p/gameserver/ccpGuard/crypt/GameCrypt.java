package l2p.gameserver.ccpGuard.crypt;


import l2p.gameserver.ccpGuard.ProtectInfo;
import l2p.gameserver.ccpGuard.Protection;

import static l2p.gameserver.ccpGuard.commons.utils.Util.bytesToInt;
import static l2p.gameserver.ccpGuard.commons.utils.Util.intToBytes;


public class GameCrypt {

    private final byte[] _inKey = new byte[16];
    private final byte[] _outKey = new byte[16];
    private final ProtectionCrypt enc = new ProtectionCrypt();
    private final ProtectionCrypt dec = new ProtectionCrypt();
    private ProtectInfo.ProtectionClientState protectState;
    private boolean _isEnabled;

    public void setKey(byte[] key) {
        System.arraycopy(key, 0, _inKey, 0, 16);
        System.arraycopy(key, 0, _outKey, 0, 16);
        if (Protection.protect_use) {
            byte[] newKey = new byte[8 * 4];
            for (int i = 0; i < 8; i++) {
                int val = ProtectionCrypt.getValue(key[i] & 0xFF);
                intToBytes(val, newKey, i * 4);
            }
            dec.setKey(newKey);
            enc.setKey(key);
        }
    }

    public void setState(ProtectInfo.ProtectionClientState protectState) {
        this.protectState = protectState;
    }

    public void decrypt(byte[] raw, final int offset, final int size) {
        if (!_isEnabled) {
            return;
        }

        if (Protection.protect_use) {
            dec.decrypt(raw, offset, raw, offset, size);
        } else {
            int temp = 0;
            for (int i = 0; i < size; i++) {
                int temp2 = raw[offset + i] & 0xFF;
                raw[offset + i] = (byte) (temp2 ^ _inKey[i & 15] ^ temp);
                temp = temp2;
            }
            intToBytes(bytesToInt(_inKey, 8) + size, _inKey, 8);
        }
    }

    public void encrypt(byte[] raw, final int offset, final int size) {
        if (!_isEnabled) {
            _isEnabled = true;
            return;
        }

        if (Protection.protect_use) {
            enc.encrypt(_outKey, 0, _outKey, 0, _outKey.length);
        }

        int temp = 0;
        for (int i = 0; i < size; i++) {
            int temp2 = raw[offset + i] & 0xFF;
            temp = temp2 ^ _outKey[i & 15] ^ temp;
            raw[offset + i] = (byte) temp;
        }
        intToBytes(bytesToInt(_outKey, 8) + size, _outKey, 8);
    }
}

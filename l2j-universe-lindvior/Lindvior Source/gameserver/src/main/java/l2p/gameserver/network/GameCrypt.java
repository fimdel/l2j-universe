package l2p.gameserver.network;

import l2p.gameserver.Config;

public class GameCrypt {
    private final byte[] _inKey = new byte[16], _outKey = new byte[16];
    private boolean _isEnabled = false;

    private static final byte RC4_KEY[] = new byte[32];

    static {

        RC4_KEY[0] = (byte) 0x48;
        RC4_KEY[1] = (byte) 0x18;
        RC4_KEY[2] = (byte) 0x9F;
        RC4_KEY[3] = (byte) 0x10;
        RC4_KEY[4] = (byte) 0x9E;
        RC4_KEY[5] = (byte) 0xB2;
        RC4_KEY[6] = (byte) 0xD4;
        RC4_KEY[7] = (byte) 0xAF;
        RC4_KEY[8] = (byte) 0x08;
        RC4_KEY[9] = (byte) 0x87;
        RC4_KEY[10] = (byte) 0xD9;
        RC4_KEY[11] = (byte) 0x70;
        RC4_KEY[12] = (byte) 0xCF;
        RC4_KEY[13] = (byte) 0xA2;
        RC4_KEY[14] = (byte) 0xFC;
        RC4_KEY[15] = (byte) 0xC2;
        RC4_KEY[16] = (byte) 0x58;
        RC4_KEY[17] = (byte) 0x86;
        RC4_KEY[18] = (byte) 0xFB;
        RC4_KEY[19] = (byte) 0xE8;
        RC4_KEY[20] = (byte) 0x97;
        RC4_KEY[21] = (byte) 0x48;
        RC4_KEY[22] = (byte) 0xEB;
        RC4_KEY[23] = (byte) 0x5D;
        RC4_KEY[24] = (byte) 0xC7;
        RC4_KEY[25] = (byte) 0x8E;
        RC4_KEY[26] = (byte) 0x9D;
        RC4_KEY[27] = (byte) 0xA0;
        RC4_KEY[28] = (byte) 0xB8;
        RC4_KEY[29] = (byte) 0xAB;
        RC4_KEY[30] = (byte) 0x14;
        RC4_KEY[31] = (byte) 0x70;
    }

    private final RC4 rc4In = new RC4(RC4_KEY);
    private final RC4 rc4Out = new RC4(RC4_KEY);

    public void setKey(byte[] key) {
        System.arraycopy(key, 0, _inKey, 0, 16);
        System.arraycopy(key, 0, _outKey, 0, 16);
    }

    public void setKey(byte[] key, boolean value) {
        setKey(key);
    }

    public boolean decrypt(byte[] raw, final int offset, final int size) {
        if (!_isEnabled)
            return true;

        if (Config.ENABLE_RC4_ENCRYPT) {
            rc4In.rc4(raw, offset, size);
            return true;
        }

        int temp = 0;
        for (int i = 0; i < size; i++) {
            int temp2 = raw[offset + i] & 0xFF;
            raw[offset + i] = (byte) (temp2 ^ _inKey[i & 15] ^ temp);
            temp = temp2;
        }

        int old = _inKey[8] & 0xff;
        old |= _inKey[9] << 8 & 0xff00;
        old |= _inKey[10] << 0x10 & 0xff0000;
        old |= _inKey[11] << 0x18 & 0xff000000;

        old += size;

        _inKey[8] = (byte) (old & 0xff);
        _inKey[9] = (byte) (old >> 0x08 & 0xff);
        _inKey[10] = (byte) (old >> 0x10 & 0xff);
        _inKey[11] = (byte) (old >> 0x18 & 0xff);
        return true;
    }

    public void encrypt(byte[] raw, final int offset, final int size) {
        if (!_isEnabled) {
            _isEnabled = true;
            return;
        }

        if (Config.ENABLE_RC4_ENCRYPT) {
            rc4Out.rc4(raw, offset, size);
            return;
        }

        int temp = 0;
        for (int i = 0; i < size; i++) {
            int temp2 = raw[offset + i] & 0xFF;
            temp = temp2 ^ _outKey[i & 15] ^ temp;
            raw[offset + i] = (byte) temp;
        }

        int old = _outKey[8] & 0xff;
        old |= _outKey[9] << 8 & 0xff00;
        old |= _outKey[10] << 0x10 & 0xff0000;
        old |= _outKey[11] << 0x18 & 0xff000000;

        old += size;

        _outKey[8] = (byte) (old & 0xff);
        _outKey[9] = (byte) (old >> 0x08 & 0xff);
        _outKey[10] = (byte) (old >> 0x10 & 0xff);
        _outKey[11] = (byte) (old >> 0x18 & 0xff);
    }
}
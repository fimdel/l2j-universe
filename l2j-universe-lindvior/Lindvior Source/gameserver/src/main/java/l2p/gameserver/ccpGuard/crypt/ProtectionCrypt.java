package l2p.gameserver.ccpGuard.crypt;

public final class ProtectionCrypt {

    int x;
    int y;
    byte[] state = new byte[256];
    boolean _inited = false;

    final int arcfour_byte() {
        int x;
        int y;
        int sx, sy;

        x = (this.x + 1) & 0xff;
        sx = (int) state[x];
        y = (sx + this.y) & 0xff;
        sy = (int) state[y];
        this.x = x;
        this.y = y;
        state[y] = (byte) (sx & 0xff);
        state[x] = (byte) (sy & 0xff);
        return (int) state[((sx + sy) & 0xff)];
    }

    public synchronized void encrypt(byte[] src, int srcOff, byte[] dest, int destOff, int len) {
        if (!_inited) {
            return;
        }
        int end = srcOff + len;
        for (int si = srcOff, di = destOff; si < end; si++, di++) {
            dest[di] = (byte) (((int) src[si] ^ arcfour_byte()) & 0xff);
        }
    }

    public void decrypt(byte[] src, int srcOff, byte[] dest, int destOff, int len) {
        encrypt(src, srcOff, dest, destOff, len);
    }

    public void setKey(byte[] key) {
        int t, u;
        int counter;
        this.x = 0;
        this.y = 0;

        for (counter = 0; counter < 256; counter++) {
            state[counter] = (byte) counter;
        }

        int keyindex = 0;
        int stateindex = 0;
        for (counter = 0; counter < 256; counter++) {
            t = (int) state[counter];
            stateindex = (stateindex + key[keyindex] + t) & 0xff;
            u = (int) state[stateindex];
            state[stateindex] = (byte) (t & 0xff);
            state[counter] = (byte) (u & 0xff);
            if (++keyindex >= key.length) {
                keyindex = 0;
            }
        }
        this._inited = true;
    }

    public boolean isInited() {
        return this._inited;
    }

    public static int getValue(final int index) {
        return ProtectData.getValue(index);
    }
}

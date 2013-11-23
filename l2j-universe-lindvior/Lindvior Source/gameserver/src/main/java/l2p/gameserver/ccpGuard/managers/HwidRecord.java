package l2p.gameserver.ccpGuard.managers;

import l2p.gameserver.ccpGuard.commons.utils.Util;

/**
 * @author Argot
 */
public class HwidRecord {

    public static enum HwidType {

        SYSTEM, UUID, MAINBOARD, ERROR
    }

    private HwidType type;
    private byte[] id;

    public HwidRecord(byte[] data, int off) {
        switch (Util.bytesToShort(data, off)) {
            case 01:
                type = HwidType.SYSTEM;
                break;
            case 02:
                type = HwidType.UUID;
                break;
            case 03:
                type = HwidType.MAINBOARD;
                break;
            default:
                type = HwidType.ERROR;
                break;
        }
        id = new byte[16];
        System.arraycopy(data, off + 2, id, 0, 16);
    }

    public HwidType getType() {
        return type;
    }

    public byte[] getId() {
        return id;
    }

    public static String fillHex(int data, int digits) {
        String number = Integer.toHexString(data);

        for (int i = number.length(); i < digits; i++) {
            number = "0" + number;
        }

        return number;
    }

    @Override
    public String toString() {
        String result = "";

        switch (getType()) {
            case SYSTEM:
                result += "[01]";
                break;
            case UUID:
                result += "[02]";
                break;
            case MAINBOARD:
                result += "[03]";
                break;
            case ERROR:
                result += "[04]";
                break;
        }

        StringBuilder resultHWID = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            resultHWID.append(fillHex(id[i] & 0xff, 2));
        }
        return result + resultHWID.toString();
    }
}

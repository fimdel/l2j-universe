package l2p.gameserver.ccpGuard.commons.utils;

public class Util {
    public static String LastErrorConvertion(Integer LastError) {
        return LastError.toString();
    }

    private static String fillHex(int data, int digits) {
        String number = Integer.toHexString(data);
        for (int i = number.length(); i < digits; i++)
            number = "0" + number;
        return number;
    }


    public static int doXORdecGG(byte[] data, int offset, int data_len) {
        int ecx;
        int pos;
        int edx = 0;
        ecx = bytesToInt(data, offset);
        intToBytes(bytesToInt(data, offset) ^ ecx, data, offset);
        pos = offset + data_len - 4;
        while (pos >= offset) {
            edx = bytesToInt(data, pos);
            edx ^= ecx;
            ecx -= edx;
            intToBytes(edx, data, pos);
            pos -= 4;
        }
        return ecx;
    }

    public static boolean verifyChecksumHwid(byte[] raw, final int offset, final int size) {
        int sum = 0;
        for (int a = 0; a < size; a++) {
            sum += (raw[a + offset] & 0xFF);
        }
        return sum == 0;
    }

    public static boolean verifyChecksum(byte[] raw, final int offset, final int size) {
        // check if size is multiple of 4 and if there is more then only the checksum
        if ((size & 3) != 0 || size <= 4) {
            return false;
        }
        long chksum = 0;
        int count = size - 4;
        int i = 0;
        for (i = offset; i < count; i += 4) {
            chksum ^= bytesToInt(raw, i);
        }
        long check = bytesToInt(raw, count);
        return check == chksum;
    }


    public static String printData(byte[] data, int offset, int len) {
        StringBuffer result = new StringBuffer();

        int counter = 0;

        for (int i = 0; i < len; i++) {
            if (counter % 16 == 0)
                result.append(fillHex(i, 4) + ": ");

            result.append(fillHex(data[i + offset] & 0xff, 2) + " ");
            counter++;
            if (counter == 16) {
                result.append("   ");

                int charpoint = i - 15;
                for (int a = 0; a < 16; a++) {
                    int t1 = data[charpoint++ + offset];
                    if (t1 > 0x1f && t1 < 0x80)
                        result.append((char) t1);
                    else
                        result.append('.');
                }

                result.append("\n");
                counter = 0;
            }
        }

        int rest = len % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; i++)
                result.append("   ");

            int charpoint = len - rest;
            for (int a = 0; a < rest; a++) {
                int t1 = data[charpoint++];
                if (t1 > 0x1f && t1 < 0x80)
                    result.append((char) t1);
                else
                    result.append('.');
            }

            result.append("\n");
        }

        return result.toString();
    }

    public static String printData(byte[] raw) {
        return printData(raw, raw.length);
    }

    public static String printData(byte[] raw, int len) {
        return printData(raw, 0, len);
    }

    public static int bytesToShort(byte[] array, int offset) {
        return (((int) array[offset++] & 0xff)
                | (((int) array[offset++] & 0xff) << 8));
    }

    public static int bytesToInt(byte[] array, int offset) {
        return (((int) array[offset++] & 0xff)
                | (((int) array[offset++] & 0xff) << 8)
                | (((int) array[offset++] & 0xff) << 16)
                | (((int) array[offset++] & 0xff) << 24));
    }

    public static void shortToBytes(int value, byte[] array, int offset) {
        array[offset++] = (byte) (value & 0xff);
        array[offset++] = (byte) (value >> 0x08 & 0xff);
    }

    public static void intToBytes(int value, byte[] array, int offset) {
        array[offset++] = (byte) (value & 0xff);
        array[offset++] = (byte) (value >> 0x08 & 0xff);
        array[offset++] = (byte) (value >> 0x10 & 0xff);
        array[offset++] = (byte) (value >> 0x18 & 0xff);
    }

}

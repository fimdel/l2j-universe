package l2p.gameserver.network;

public class RC4 {

    private byte state[] = new byte[256];
    private int x;
    private int y;

    /**
     * Инициализация потока.
     * В аргументе - Ключик потока
     */
    public RC4(String key) throws NullPointerException {
        this(key.getBytes());
    }

    /**
     * Инициализация класса с ключевыми байтами массива.
     * Длина нормального ключ должен быть от 1 до 2048 бит.
     * Но этот метод не проверяет длину.
     */
    public RC4(byte[] key) throws NullPointerException {

        for (int i = 0; i < 256; i++)
            state[i] = (byte) i;

        x = 0;
        y = 0;

        int index1 = 0;
        int index2 = 0;

        byte tmp;

        if (key == null || key.length == 0)
            throw new NullPointerException();

        for (int i = 0; i < 256; i++) {

            index2 = (key[index1] & 0xff) + (state[i] & 0xff) + index2 & 0xff;

            tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;

            index1 = (index1 + 1) % key.length;
        }
    }

    /**
     * Шифрование\Расшифрование массива байтов, оптимизировано под сборку)
     */
    public void rc4(byte[] buf, final int offset, final int size) {
        int xorIndex;
        byte tmp;

        for (int i = 0; i < size; i++) {

            x = x + 1 & 0xff;
            y = (state[x] & 0xff) + y & 0xff;

            tmp = state[x];
            state[x] = state[y];
            state[y] = tmp;

            xorIndex = (state[x] & 0xff) + (state[y] & 0xff) & 0xff;
            buf[offset + i] ^= state[xorIndex];
        }

    }

}
package l2p.commons.net.nio.impl;

import java.nio.ByteOrder;

public class SelectorConfig {
    /**
     * Размер буфера на чтение
     */
    public int READ_BUFFER_SIZE = 65536;
    /**
     * Размер буффра на запись
     */
    public int WRITE_BUFFER_SIZE = 131072;
    /**
     * Максимальное количество пакетов при проходе на запись, может быть меньше этого числа, если буфер записи будет заполнен
     */
    public int MAX_SEND_PER_PASS = 32;
    /**
     * Задержка в миллимекундах после каждого прохода в цикле SelectorThread
     */
    public long SLEEP_TIME = 10;
    /**
     * Задержка перед сменой запланированного интересуемого действия
     */
    public long INTEREST_DELAY = 30;
    /**
     * Размер заголовка
     */
    public int HEADER_SIZE = 2;
    /**
     * Максимальный размер пакета
     */
    public int PACKET_SIZE = 32768;
    /**
     * Количество вспомогательных буферов
     */
    public int HELPER_BUFFER_COUNT = 64;
    /**
     * Порядок байтов
     */
    public ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
}
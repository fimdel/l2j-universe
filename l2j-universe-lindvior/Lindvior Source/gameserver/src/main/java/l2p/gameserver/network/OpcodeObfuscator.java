package l2p.gameserver.network;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 15.08.12
 * Time: 3:43
 */
public class OpcodeObfuscator {
    private boolean m_enabled;
    private int m_rand_seed;
    private int[] m_decodeTable1;
    private int[] m_decodeTable2;

    public OpcodeObfuscator() {
        initialize();
    }

    private void initialize() {
        m_enabled = false;
        m_rand_seed = 0;
        m_decodeTable1 = m_decodeTable2 = null;
    }

    public static OpcodeObfuscator init_tables(int obfKey) {
        OpcodeObfuscator oo = new OpcodeObfuscator();
        oo.prepare(obfKey);
        return oo;
    }

    private void prepare(int obfKey) {
        int l = 0;
        m_decodeTable1 = new int[0xD0 + 1];
        m_decodeTable2 = new int[0x83 + 1];
        for (int i = 0; i <= 0xD0; ++i)
            m_decodeTable1[i] = i;
        for (int i = 0; i <= 0x83; ++i)
            m_decodeTable2[i] = i;
        pseudo_rand_seed(obfKey);
        for (int i = 1; i <= 0xD0; ++i) {
            int k = pseudo_rand() % (i + 1);
            int j = m_decodeTable1[k];
            m_decodeTable1[k] = m_decodeTable1[i];
            m_decodeTable1[i] = j;
        }
        for (int i = 1; i <= 0x83; ++i) {
            int k = pseudo_rand() % (i + 1);
            int j = m_decodeTable2[k];
            m_decodeTable2[k] = m_decodeTable2[i];
            m_decodeTable2[i] = j;
        }
        for (l = 0; m_decodeTable1[l] != 0x12; l++) ;
        int j = m_decodeTable1[0x12];
        m_decodeTable1[0x12] = 0x12;
        m_decodeTable1[l] = j;
        for (l = 0; m_decodeTable1[l] != 0xB1; l++) ;
        int d = m_decodeTable1[0xB1];
        m_decodeTable1[0xB1] = 0xB1;
        m_decodeTable1[l] = d;
        m_decodeTable1[0xD0] = 0xD0;
        m_decodeTable1[0x11] = 0x11;
        m_enabled = true;
    }

    public int decodeSingleOpcode(int paramInt) {
        if (!m_enabled)
            return paramInt;
        if (paramInt > 0xD0)
            return paramInt;
        paramInt = m_decodeTable1[paramInt];
        return paramInt;
    }

    public int decodeDoubleOpcode(int paramInt) {
        if (!this.m_enabled)
            return paramInt;
        if (paramInt > 0x83)
            return paramInt;
        paramInt = this.m_decodeTable2[paramInt];
        return paramInt;
    }

    private void pseudo_rand_seed(int obfKey) {
        m_rand_seed = obfKey;
    }

    private int pseudo_rand() {
        int i = (m_rand_seed * 0x343FD + 0x269EC3) & 0xFFFFFFFF;
        m_rand_seed = i;
        int j = (m_rand_seed >> 0x10) & 0x7FFF;
        return j;
    }
}

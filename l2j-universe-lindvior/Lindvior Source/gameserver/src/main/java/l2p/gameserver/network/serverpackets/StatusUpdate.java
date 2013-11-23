package l2p.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

/**
 * Даные параметры актуальны для С6(Interlude), 04/10/2007, протокол 746
 */
public class StatusUpdate extends L2GameServerPacket {
    /**
     * Даный параметр отсылается оффом в паре с MAX_HP
     * Сначала CUR_HP, потом MAX_HP
     */
    public final static int CUR_HP = 0x09;
    public final static int MAX_HP = 0x0a;

    /**
     * Даный параметр отсылается оффом в паре с MAX_MP
     * Сначала CUR_MP, потом MAX_MP
     */
    public final static int CUR_MP = 0x0b;
    public final static int MAX_MP = 0x0c;

    /**
     * Меняется отображение только в инвентаре, для статуса требуется UserInfo
     */
    public final static int CUR_LOAD = 0x0e;

    /**
     * Меняется отображение только в инвентаре, для статуса требуется UserInfo
     */
    public final static int MAX_LOAD = 0x0f;

    public final static int PVP_FLAG = 0x1a;
    public final static int KARMA = 0x1b;

    /**
     * Даный параметр отсылается оффом в паре с MAX_CP
     * Сначала CUR_CP, потом MAX_CP
     */
    public final static int CUR_CP = 0x21;
    public final static int MAX_CP = 0x22;

    /**
     * GOD отображение демага
     */
    public final static int DAMAGE = 0x23;

    private final int _objectId;
    private int _playerId;
    private final List<Attribute> _attributes = new ArrayList<Attribute>();

    class Attribute {
        public final int id;
        public final int value;

        Attribute(int id, int value) {
            this.id = id;
            this.value = value;
        }
    }

    public StatusUpdate(int objectId) {
        _objectId = objectId;
        _playerId = 0;
    }

    public StatusUpdate(int objectId, int playerId) {
        _objectId = objectId;
        _playerId = playerId;
    }

    public StatusUpdate addAttribute(int id, int level) {
        _attributes.add(new Attribute(id, level));
        return this;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x18);
        writeD(_objectId);
        writeD(_playerId);
        writeD(0x01); //При 1 = рег ХП
        writeD(_attributes.size());

        for (Attribute temp : _attributes) {
            writeD(temp.id);
            writeD(temp.value);
        }

        writeD(0x00); //TODO а на NA оффе этих дшек нету
        writeD(0x00); //TODO а на NA оффе этих дшек нету
        writeD(0x00); //TODO а на NA оффе этих дшек нету
    }

    public boolean hasAttributes() {
        return !_attributes.isEmpty();
    }
}
package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;

public class NpcSay extends NpcStringContainer {
    private int _objId;
    private int _type;
    private int _id;
    @SuppressWarnings("unused")
    private int _npcString;

    public NpcSay(NpcInstance npc, ChatType chatType, String text) {
        this(npc, chatType, NpcString.NONE, text);
    }

    public NpcSay(NpcInstance npc, ChatType chatType, NpcString npcString, String... params) {
        super(npcString, params);
        _objId = npc.getObjectId();
        _id = npc.getNpcId();
        _type = chatType.ordinal();
    }

    public NpcSay(int objectId, int messageType, int npcId, int npcString) {
        super(NpcString.valueOf(npcString));
        _objId = objectId;
        _type = messageType;
        _id = (1000000 + npcId);
        _npcString = npcString;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x30);
        writeD(_objId);
        writeD(_type);
        writeD(1000000 + _id);
        writeElements();
    }
}

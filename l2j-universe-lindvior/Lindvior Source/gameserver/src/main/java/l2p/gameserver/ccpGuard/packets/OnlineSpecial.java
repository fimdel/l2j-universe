package l2p.gameserver.ccpGuard.packets;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class OnlineSpecial extends L2GameServerPacket {

    private int x;
    private int y;
    private int color;
    private int count;

    //0xFFBBGGRR
    public OnlineSpecial(int x, int y, int color, int count) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.count = count;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xb0);
        writeC(0xA1);
        writeC(0x01);
        writeD(x);
        writeD(y);
        writeD(color);
        writeD(count); // Current Online
    }

    @Override
    public String getType() {
        return "OnlineSpecial";
    }
}

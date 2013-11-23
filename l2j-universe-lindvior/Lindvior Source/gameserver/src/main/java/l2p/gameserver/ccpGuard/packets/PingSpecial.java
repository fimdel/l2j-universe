package l2p.gameserver.ccpGuard.packets;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PingSpecial extends L2GameServerPacket {

    private int x;
    private int y;
    private int color;
    private int ping;

    public void PingSpecial(int x, int y, int color, int ping) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.ping = ping;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xb0);
        writeC(0xA2);
        writeC(0x01);
        writeD(x);
        writeD(y);
        writeD(color);
        writeD(ping); // Current Online
    }

    @Override
    public String getType() {
        return "[S] B0 PingSpecial";
    }
}

package l2p.gameserver.ccpGuard.packets;


import l2p.gameserver.ccpGuard.ConfigProtect;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ServerTitleSpecial extends L2GameServerPacket {
    private String anyString;
    private int x;
    private int y;
    private int color;

    public static ServerTitleSpecial STATIC = new ServerTitleSpecial();

    public ServerTitleSpecial() {
        this.x = ConfigProtect.PROTECT_SERVER_TITLE_X;
        this.y = ConfigProtect.PROTECT_SERVER_TITLE_Y;
        this.color = ConfigProtect.PROTECT_SERVER_TITLE_COLOR;
        this.anyString = ConfigProtect.PROTECT_SERVER_TITLE;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xb0);
        writeC(0xA0);
        writeC(0x01);
        writeD(280);
        writeD(10);
        writeD(0xFF2491fd);
        writeS(anyString);  // wide string max len = 25
    }

    @Override
    public String getType() {
        return "[S] B0 ServerTitleSpecial";
    }
}

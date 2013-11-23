package l2p.gameserver.loginservercon.gspackets;

import l2p.gameserver.loginservercon.SendablePacket;

public class ChangeAccessLevel extends SendablePacket {
    private String account;
    private int level;
    private int banExpire;

    public ChangeAccessLevel(String account, int level, int banExpire) {
        this.account = account;
        this.level = level;
        this.banExpire = banExpire;
    }

    @Override
    protected void writeImpl() {
        writeC(0x11);
        writeS(account);
        writeD(level);
        writeD(banExpire);
    }
}

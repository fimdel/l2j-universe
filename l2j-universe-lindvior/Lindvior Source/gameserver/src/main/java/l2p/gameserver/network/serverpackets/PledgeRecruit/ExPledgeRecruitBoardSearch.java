/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.PledgeRecruit;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeRecruitBoardSearch extends L2GameServerPacket {

    public String leadername;
    public int ClanLvl;
    public String clanname;
    public String notice;
    public int ClanSize;
    public int size;

    public ExPledgeRecruitBoardSearch(Player pl) {
        leadername = pl.getClan().getLeaderName();
        clanname = pl.getClan().getName();
        ClanLvl = pl.getClan().getLevel();
        ClanSize = pl.getClan().getAllSize();
        notice = pl.getClan().getNotice();
    }

    @Override
    protected void writeImpl() {
        writeEx(0x14B);
        writeD(0);
        writeD(0);
        writeD(0);
        writeD(0);
        writeD(size);
        for (; ; ) {
            writeD(0);
            writeD(0);
            writeS(clanname);  //название клана
            writeS(leadername);  //КЛ
            writeD(ClanLvl);     //уровень
            writeD(ClanSize);     //кол-во чел в клане
            writeD(0);
            writeS(notice);  //текст приглашения
        }
    }
}
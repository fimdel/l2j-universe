/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:46
 */
public class ListMenteeWaiting extends L2GameServerPacket {
    List<Player> mentees;
    int page;
    int playersInPage;

    public ListMenteeWaiting(Player activeChar, int _page, int minLevel, int maxLevel) {
        this.mentees = new ArrayList();
        this.page = _page;
        this.playersInPage = 64;

        for (Player player : World.getAroundPlayers(activeChar))
            if ((player.getLevel() >= minLevel) && (player.getLevel() <= maxLevel) && (player.isMentee()))
                this.mentees.add(player);
    }

    @Override
    protected void writeImpl() {
        writeEx(0x123);

        writeD(this.page);
        int i;
        if (!this.mentees.isEmpty()) {
            writeD(this.mentees.size());
            writeD(this.mentees.size() % this.playersInPage);
            i = 1;
            for (Player player : this.mentees)
                if ((i <= this.playersInPage * this.page) && (i > this.playersInPage * (this.page - 1))) {
                    writeS(player.getName());
                    writeD(player.getClassId().ordinal());
                    writeD(player.getLevel());
                }
        } else {
            writeD(0);
            writeD(0);
        }
    }
}

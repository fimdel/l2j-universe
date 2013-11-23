/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.Mentee;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:43
 */
public class ExMentorList extends L2GameServerPacket {
    private List<Mentee> _list = Collections.emptyList();
    private int _mentor;
    private Player _activeChar;

    public ExMentorList(Player player) {
        this._mentor = player.getMenteeList().getMentor();
        this._list = player.getMenteeList().getMentees();

        this._activeChar = player;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x121);

        writeD((this._mentor == 0) && (this._activeChar.isMentor()) ? 1 : 2);

        writeD(this._list.size());
        for (Mentee entry : this._list) {
            writeD(entry.getObjectId());
            writeS(entry.getName());
            writeD(entry.getClassId());
            writeD(entry.getLevel());
            writeD(entry.isOnline());
        }
    }
}
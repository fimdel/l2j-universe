/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:44
 */
public class ExMentorAdd extends L2GameServerPacket {
    private String _newMentorName;
    private int _newMentorClassId;
    private int _newMentorLvl;

    public ExMentorAdd(Player newMentor) {
        this._newMentorName = newMentor.getName();
        this._newMentorClassId = newMentor.getClassId().getId();
        this._newMentorLvl = newMentor.getLevel();
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x122);

        writeS(this._newMentorName);
        writeD(this._newMentorClassId);
        writeD(this._newMentorLvl);
    }
}
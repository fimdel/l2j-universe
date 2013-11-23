/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Author: VISTALL
 */
public class ExSubPledgeSkillAdd extends L2GameServerPacket {
    private int _type, _id, _level;

    public ExSubPledgeSkillAdd(int type, int id, int level) {
        _type = type;
        _id = id;
        _level = level;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0x76);
        writeD(_type);
        writeD(_id);
        writeD(_level);
    }
}
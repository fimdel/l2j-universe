/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeSkillListAdd extends L2GameServerPacket {
    private int _skillId;
    private int _skillLevel;

    public PledgeSkillListAdd(int skillId, int skillLevel) {
        _skillId = skillId;
        _skillLevel = skillLevel;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x3b);
        writeD(_skillId);
        writeD(_skillLevel);
    }
}
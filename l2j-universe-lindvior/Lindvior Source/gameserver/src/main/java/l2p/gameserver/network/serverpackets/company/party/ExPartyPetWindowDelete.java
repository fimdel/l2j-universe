/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.model.Summon;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPartyPetWindowDelete extends L2GameServerPacket {
    private int _summonObjectId;
    private int _ownerObjectId;
    private int _type;
    private String _summonName;

    public ExPartyPetWindowDelete(Summon summon) {
        _summonObjectId = summon.getObjectId();
        _summonName = summon.getName();
        _type = summon.getSummonType();
        _ownerObjectId = summon.getPlayer().getObjectId();
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x6B);
        writeD(_summonObjectId);
        writeD(_type);
        writeD(_ownerObjectId);
        writeS(_summonName);
    }
}
/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.ResidenceSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bonux
 */
public class ExCastleState extends L2GameServerPacket {
    private static final Logger _log = LoggerFactory.getLogger(ExCastleState.class);
    private final int _id;
    private final ResidenceSide _side;

    public ExCastleState(Castle castle) {
        _id = castle.getId();
        _side = castle.getResidenceSide();
    }

    protected void writeImpl() {
        writeEx(0x133);
        writeD(_id);
        writeD(_side.ordinal());

    }
}

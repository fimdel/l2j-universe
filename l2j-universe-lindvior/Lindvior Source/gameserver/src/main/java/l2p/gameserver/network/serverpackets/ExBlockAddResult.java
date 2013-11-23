/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.dao.CharacterDAO;

/**
 * @author Darvin
 */
public class ExBlockAddResult extends L2GameServerPacket {
    private int objId;

    public ExBlockAddResult(int OID) {
        objId = OID;
    }

    @Override
    protected void writeImpl() {
        writeEx(0xED);
        writeD(0x00);
        writeS(CharacterDAO.getInstance().getNameByObjectId(objId));
    }
}

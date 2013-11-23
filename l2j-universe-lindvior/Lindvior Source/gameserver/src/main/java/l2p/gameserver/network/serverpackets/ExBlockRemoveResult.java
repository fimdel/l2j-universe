/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.dao.CharacterDAO;

/**
 * @author Darvin
 */
public class ExBlockRemoveResult extends L2GameServerPacket {

    private static final String _S__FE_ED_EXBLOCKREMOVERESULT = "[S] FE:ED ExBlockRemoveResult";

    private int objId;

    public ExBlockRemoveResult(int OID) {
        objId = OID;
    }

    @Override
    protected void writeImpl() {
        writeEx(0xEE);
        writeD(0x00);
        writeS(CharacterDAO.getInstance().getNameByObjectId(objId));
    }

    @Override
    public String getType() {
        return _S__FE_ED_EXBLOCKREMOVERESULT;
    }

}

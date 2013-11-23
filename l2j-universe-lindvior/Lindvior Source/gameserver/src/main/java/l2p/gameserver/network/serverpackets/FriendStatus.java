package l2p.gameserver.network.serverpackets;

import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.model.World;

public class FriendStatus extends L2GameServerPacket {
    private boolean _online;
    private int _objid;
    private String _name;

    public FriendStatus(int objId) {
        _objid = objId;
        _name = CharacterDAO.getInstance().getNameByObjectId(objId);
        _online = World.getPlayer(objId) != null;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x77);
        writeD(_online ? 1 : 0);
        writeS(_name);
        writeD(_objid);
    }
}

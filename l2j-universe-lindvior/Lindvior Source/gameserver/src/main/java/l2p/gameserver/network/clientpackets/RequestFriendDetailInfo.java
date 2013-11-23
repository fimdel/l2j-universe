package l2p.gameserver.network.clientpackets;

import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExFriendDetailInfo;

/**
 * @author Darvin
 */
public final class RequestFriendDetailInfo extends L2GameClientPacket {
    private static final String _C__D0_97_REQUESTFRIENDDETAILINFO = "[C] D0:97 RequestFriendDetailInfo";

    private String _name;

    @Override
    protected void readImpl() {
        _name = readS();
    }

    @Override
    public void runImpl() {
        Player player = getClient().getActiveChar();
        int objId = CharacterDAO.getInstance().getObjectIdByName(_name);
        player.sendPacket(new ExFriendDetailInfo(player, objId));

    }

    @Override
    public String getType() {
        return _C__D0_97_REQUESTFRIENDDETAILINFO;
    }
}

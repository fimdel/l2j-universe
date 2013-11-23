package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

public class RequestFriendDel extends L2GameClientPacket {
    private String _name;

    @Override
    protected void readImpl() {
        _name = readS(16);
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.getFriendList().removeFriend(_name);
    }
}
package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.actor.instances.player.Friend;
import l2p.gameserver.network.serverpackets.SystemMessage;

import java.util.Map;

public class RequestFriendList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        activeChar.sendPacket(Msg._FRIENDS_LIST_);
        Map<Integer, Friend> _list = activeChar.getFriendList().getList();
        for (Map.Entry<Integer, Friend> entry : _list.entrySet()) {
            Player friend = World.getPlayer(entry.getKey());
            if (friend != null)
                activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CURRENTLY_ONLINE).addName(friend));
            else
                activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CURRENTLY_OFFLINE).addString(entry.getValue().getName()));
        }
        activeChar.sendPacket(Msg.__EQUALS__);
    }
}

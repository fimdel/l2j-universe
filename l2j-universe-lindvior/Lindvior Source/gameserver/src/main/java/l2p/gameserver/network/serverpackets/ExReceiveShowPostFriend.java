package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import org.napile.primitive.maps.IntObjectMap;

/**
 * @author VISTALL
 * @date 22:01/22.03.2011
 */
public class ExReceiveShowPostFriend extends L2GameServerPacket {
    private IntObjectMap<String> _list;

    public ExReceiveShowPostFriend(Player player) {
        _list = player.getPostFriends();
    }

    @Override
    public void writeImpl() {
        writeEx449(0xD3);
        writeD(_list.size());
        for (String t : _list.values())
            writeS(t);
    }
}

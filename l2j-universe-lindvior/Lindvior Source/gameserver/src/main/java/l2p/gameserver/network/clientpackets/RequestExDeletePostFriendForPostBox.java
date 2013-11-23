package l2p.gameserver.network.clientpackets;

import l2p.gameserver.dao.CharacterPostFriendDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;

/**
 * @author VISTALL
 * @date 21:06/22.03.2011
 */
public class RequestExDeletePostFriendForPostBox extends L2GameClientPacket {
    private String _name;

    @Override
    protected void readImpl() throws Exception {
        _name = readS();
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        if (StringUtils.isEmpty(_name))
            return;

        int key = 0;
        IntObjectMap<String> postFriends = player.getPostFriends();
        for (IntObjectMap.Entry<String> entry : postFriends.entrySet())
            if (entry.getValue().equalsIgnoreCase(_name))
                key = entry.getKey();

        if (key == 0) {
            player.sendPacket(SystemMsg.THE_NAME_IS_NOT_CURRENTLY_REGISTERED);
            return;
        }

        player.getPostFriends().remove(key);

        CharacterPostFriendDAO.getInstance().delete(player, key);
        player.sendPacket(new SystemMessage2(SystemMsg.S1_WAS_SUCCESSFULLY_DELETED_FROM_YOUR_CONTACT_LIST).addString(_name));
    }
}

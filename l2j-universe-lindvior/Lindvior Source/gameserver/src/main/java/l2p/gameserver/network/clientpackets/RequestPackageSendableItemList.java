package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.PackageSendableList;

/**
 * @author VISTALL
 * @date 20:35/16.05.2011
 */
public class RequestPackageSendableItemList extends L2GameClientPacket {
    private int _objectId;

    @Override
    protected void readImpl() throws Exception {
        _objectId = readD();
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.sendPacket(new PackageSendableList(_objectId, player));
    }
}

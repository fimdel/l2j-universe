package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExNoticePostArrived;
import l2p.gameserver.network.serverpackets.ExShowReceivedPostList;

/**
 * Отсылается при нажатии на кнопку "почта", "received mail" или уведомление от {@link ExNoticePostArrived}, запрос входящих писем.
 * В ответ шлется {@link ExShowReceivedPostList}
 */
public class RequestExRequestReceivedPostList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
        //just a trigger
    }

    @Override
    protected void runImpl() {
        Player cha = getClient().getActiveChar();
        if (cha != null)
            cha.sendPacket(new ExShowReceivedPostList(cha));
    }
}
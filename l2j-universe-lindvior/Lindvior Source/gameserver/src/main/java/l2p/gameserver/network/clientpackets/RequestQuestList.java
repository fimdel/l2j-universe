package l2p.gameserver.network.clientpackets;

import l2p.gameserver.network.serverpackets.QuestList;

public class RequestQuestList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        sendPacket(new QuestList(getClient().getActiveChar()));
    }
}
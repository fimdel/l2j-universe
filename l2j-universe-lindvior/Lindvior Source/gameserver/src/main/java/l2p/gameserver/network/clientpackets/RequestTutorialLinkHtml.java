package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.Quest;

public class RequestTutorialLinkHtml extends L2GameClientPacket {
    // format: cS

    String _bypass;

    @Override
    protected void readImpl() {
        _bypass = readS();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        Quest q = QuestManager.getQuest(255);
        if (q != null)
            player.processQuestEvent(q.getName(), _bypass, null);
    }
}
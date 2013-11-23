package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.Quest;

public class RequestTutorialQuestionMark extends L2GameClientPacket {
    // format: cd
    int _number = 0;

    @Override
    protected void readImpl() {
        _number = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        Quest q = QuestManager.getQuest(255);
        if (q != null)
            player.processQuestEvent(q.getName(), "QM" + _number, null);
    }
}
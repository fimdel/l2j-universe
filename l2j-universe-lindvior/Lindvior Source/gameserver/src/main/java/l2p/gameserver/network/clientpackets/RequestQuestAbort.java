package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

public class RequestQuestAbort extends L2GameClientPacket {
    private int _questID;

    @Override
    protected void readImpl() {
        _questID = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        Quest quest = QuestManager.getQuest(_questID);
        if (activeChar == null || quest == null)
            return;

        if (!quest.canAbortByPacket())
            return;

        QuestState qs = activeChar.getQuestState(quest.getClass());
        if (qs != null && !qs.isCompleted())
            qs.abortQuest();
    }
}
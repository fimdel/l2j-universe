package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.Quest;

public class RequestTutorialPassCmdToServer extends L2GameClientPacket {
    // format: cS

    String _bypass = null;

    @Override
    protected void readImpl() {
        _bypass = readS();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        Quest tutorial = QuestManager.getQuest(255);

        if (tutorial != null)
            player.processQuestEvent(tutorial.getName(), _bypass, null);
    }
}
/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.DynamicQuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.dynamic.DynamicQuest;
import l2p.gameserver.network.serverpackets.ExDynamicQuest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDynamicQuestProgressInfo extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestDynamicQuestProgressInfo.class);
    private int _id;
    private int _step;

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null) {
            return;
        }
        DynamicQuest quest = DynamicQuestManager.getInstance().getQuest(_id, _step);
        if (quest != null) {
            quest.sendProgress(activeChar, ExDynamicQuest.UpdateAction.ACTION_PROGRESS);
        }
        // _log.info("RequestDynamicQuestProgressInfo");
    }

    @Override
    protected void readImpl() {
        _id = readD();
        _step = readD();
    }
}
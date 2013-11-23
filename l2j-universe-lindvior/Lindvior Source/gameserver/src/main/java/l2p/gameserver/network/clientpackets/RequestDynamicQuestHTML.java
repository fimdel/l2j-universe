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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//0xd0:0x96:0x04 RequestDynamicQuestHTML ch cdd
public class RequestDynamicQuestHTML extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestDynamicQuestHTML.class);
    private int _id;
    private int _step;

    private boolean validate(Player activeChar) {
        if (activeChar == null) {
            return false;
        }
        DynamicQuest quest = DynamicQuestManager.getInstance().getQuest(_id, _step);

        if (quest == null) {
            return false;
        }
        return activeChar.getLevel() >= quest.getTemplate().getMinLevel();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (!validate(activeChar)) {
            return;
        }
        DynamicQuest quest = DynamicQuestManager.getInstance().getQuest(_id, _step);

        if (quest.isParticipiant(activeChar)) {
            quest.showDialog(activeChar, "accept");
        } else {
            quest.showDialog(activeChar, "start");
        }
        //   _log.info("RequestDynamicQuestHTML");
    }

    @Override
    protected void readImpl() {
        _id = readD();
        _step = readD();
    }
}
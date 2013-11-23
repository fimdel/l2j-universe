/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

//import l2p.gameserver.instancemanager.AwakingManager;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.network.serverpackets.ExCallToChangeClass;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;

/**
 * Created by IntelliJ IDEA.
 * User: Darvin
 * Date: 24.01.12
 * Time: 16:33
 */
public class RequestCallToChangeClass extends L2GameClientPacket {

    @Override
    protected void readImpl() throws Exception {
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        if (activeChar.getVarB("GermunkusUSM")) {
            return;
        }
        int _cId = 0;
        for (ClassId Cl : ClassId.VALUES) {
            if ((Cl.isOfLevel(ClassLevel.AWAKED)) && (activeChar.getClassId().childOf(Cl))) {
                _cId = Cl.getId();
                break;
            }
        }

        if (activeChar.isDead()) {
            sendPacket(new ExShowScreenMessage(NpcString.YOU_CANNOT_TELEPORT_WHILE_YOU_ARE_DEAD, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, new String[0]));
            sendPacket(new ExCallToChangeClass(_cId, false));
            return;
        }

        activeChar.processQuestEvent("_10338_SeizeYourDestiny", "MemoryOfDisaster", null);
    }
}

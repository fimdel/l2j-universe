package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import l2p.gameserver.network.serverpackets.SkillList;

public class RequestMagicSkillList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        sendPacket(new SkillList(activeChar));
        sendPacket(new ExAcquirableSkillListByClass(activeChar));
    }
}

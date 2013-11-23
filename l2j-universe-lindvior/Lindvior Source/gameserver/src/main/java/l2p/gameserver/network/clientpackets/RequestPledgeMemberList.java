package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;

public class RequestPledgeMemberList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        Clan clan = activeChar.getClan();
        if (clan != null)
            activeChar.sendPacket(clan.listAll());
    }
}
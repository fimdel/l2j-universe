package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.CrestCache;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;

public class RequestSetPledgeCrest extends L2GameClientPacket {
    private int _length;
    private byte[] _data;

    @Override
    protected void readImpl() {
        _length = readD();
        if (_length == CrestCache.CREST_SIZE && _length == _buf.remaining()) {
            _data = new byte[_length];
            readB(_data);
        }
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Clan clan = activeChar.getClan();
        if ((activeChar.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) == Clan.CP_CL_EDIT_CREST) {
            if (clan.getLevel() < 3) {
                activeChar.sendPacket(Msg.CLAN_CREST_REGISTRATION_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3);
                return;
            }

            int crestId = 0;

            if (_data != null)
                crestId = CrestCache.getInstance().savePledgeCrest(clan.getClanId(), _data);
            else if (clan.hasCrest())
                CrestCache.getInstance().removePledgeCrest(clan.getClanId());

            clan.setCrestId(crestId);
            clan.broadcastClanStatus(false, true, false);
        }
    }
}
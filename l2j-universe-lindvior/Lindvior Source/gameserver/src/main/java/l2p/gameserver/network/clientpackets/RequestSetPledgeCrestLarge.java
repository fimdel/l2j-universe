package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.CrestCache;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;

public class RequestSetPledgeCrestLarge extends L2GameClientPacket {
    private int _length;
    private byte[] _data;

    /**
     * format: chd(b)
     */
    @Override
    protected void readImpl() {
        _length = readD();
        if (_length == CrestCache.LARGE_CREST_SIZE && _length == _buf.remaining()) {
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
        if (clan == null)
            return;

        if ((activeChar.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) == Clan.CP_CL_EDIT_CREST) {
            if (clan.getCastle() == 0 && clan.getHasHideout() == 0) {
                activeChar.sendPacket(Msg.THE_CLANS_EMBLEM_WAS_SUCCESSFULLY_REGISTERED__ONLY_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_A_CASTLE_CAN_GET_THEIR_EMBLEM_DISPLAYED_ON_CLAN_RELATED_ITEMS);
                return;
            }

            int crestId = 0;

            if (_data != null) {
                crestId = CrestCache.getInstance().savePledgeCrestLarge(clan.getClanId(), _data);
                activeChar.sendPacket(Msg.THE_CLANS_EMBLEM_WAS_SUCCESSFULLY_REGISTERED__ONLY_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_A_CASTLE_CAN_GET_THEIR_EMBLEM_DISPLAYED_ON_CLAN_RELATED_ITEMS);
            } else if (clan.hasCrestLarge())
                CrestCache.getInstance().removePledgeCrestLarge(clan.getClanId());

            clan.setCrestLargeId(crestId);
            clan.broadcastClanStatus(false, true, false);
        }
    }
}
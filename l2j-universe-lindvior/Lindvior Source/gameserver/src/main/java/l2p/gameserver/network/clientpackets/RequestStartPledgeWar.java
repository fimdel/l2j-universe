package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.tables.ClanTable;

public class RequestStartPledgeWar extends L2GameClientPacket {
    private String _pledgeName;

    @Override
    protected void readImpl() {
        _pledgeName = readS(32);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Clan clan = activeChar.getClan();
        if (clan == null) {
            activeChar.sendActionFailed();
            return;
        }

        if (!((activeChar.getClanPrivileges() & Clan.CP_CL_CLAN_WAR) == Clan.CP_CL_CLAN_WAR)) {
            activeChar.sendActionFailed();
            return;
        }

        if (clan.getWarsCount() >= 30) {
            activeChar.sendPacket(Msg.A_DECLARATION_OF_WAR_AGAINST_MORE_THAN_30_CLANS_CANT_BE_MADE_AT_THE_SAME_TIME, ActionFail.STATIC);
            return;
        }

        if (clan.getLevel() < 3 || clan.getAllSize() < 15) {
            activeChar.sendPacket(Msg.A_CLAN_WAR_CAN_BE_DECLARED_ONLY_IF_THE_CLAN_IS_LEVEL_THREE_OR_ABOVE_AND_THE_NUMBER_OF_CLAN_MEMBERS_IS_FIFTEEN_OR_GREATER, ActionFail.STATIC);
            return;
        }

        Clan targetClan = ClanTable.getInstance().getClanByName(_pledgeName);
        if (targetClan == null) {
            activeChar.sendPacket(Msg.THE_DECLARATION_OF_WAR_CANT_BE_MADE_BECAUSE_THE_CLAN_DOES_NOT_EXIST_OR_ACT_FOR_A_LONG_PERIOD, ActionFail.STATIC);
            return;
        } else if (clan.equals(targetClan)) {
            activeChar.sendPacket(Msg.FOOL_YOU_CANNOT_DECLARE_WAR_AGAINST_YOUR_OWN_CLAN, ActionFail.STATIC);
            return;
        } else if (clan.isAtWarWith(targetClan.getClanId())) {
            activeChar.sendPacket(Msg.THE_DECLARATION_OF_WAR_HAS_BEEN_ALREADY_MADE_TO_THE_CLAN, ActionFail.STATIC);
            return;
        } else if (clan.getAllyId() == targetClan.getAllyId() && clan.getAllyId() != 0) {
            activeChar.sendPacket(Msg.A_DECLARATION_OF_CLAN_WAR_AGAINST_AN_ALLIED_CLAN_CANT_BE_MADE, ActionFail.STATIC);
            return;
        } else if (targetClan.getLevel() < 3 || targetClan.getAllSize() < 15) {
            activeChar.sendPacket(Msg.A_CLAN_WAR_CAN_BE_DECLARED_ONLY_IF_THE_CLAN_IS_LEVEL_THREE_OR_ABOVE_AND_THE_NUMBER_OF_CLAN_MEMBERS_IS_FIFTEEN_OR_GREATER, ActionFail.STATIC);
            return;
        }

        ClanTable.getInstance().startClanWar(activeChar.getClan(), targetClan);
    }
}
package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Alliance;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.tables.ClanTable;

public class RequestOustAlly extends L2GameClientPacket {
    private String _clanName;

    @Override
    protected void readImpl() {
        _clanName = readS(32);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Clan leaderClan = activeChar.getClan();
        if (leaderClan == null) {
            activeChar.sendActionFailed();
            return;
        }
        Alliance alliance = leaderClan.getAlliance();
        if (alliance == null) {
            activeChar.sendPacket(Msg.YOU_ARE_NOT_CURRENTLY_ALLIED_WITH_ANY_CLANS);
            return;
        }

        Clan clan;

        if (!activeChar.isAllyLeader()) {
            activeChar.sendPacket(Msg.FEATURE_AVAILABLE_TO_ALLIANCE_LEADERS_ONLY);
            return;
        }

        if (_clanName == null)
            return;

        clan = ClanTable.getInstance().getClanByName(_clanName);

        if (clan != null) {
            if (!alliance.isMember(clan.getClanId())) {
                activeChar.sendActionFailed();
                return;
            }

            if (alliance.getLeader().equals(clan)) {
                activeChar.sendPacket(Msg.YOU_HAVE_FAILED_TO_WITHDRAW_FROM_THE_ALLIANCE);
                return;
            }

            clan.broadcastToOnlineMembers(new SystemMessage("Your clan has been expelled from " + alliance.getAllyName() + " alliance."), new SystemMessage(SystemMessage.A_CLAN_THAT_HAS_WITHDRAWN_OR_BEEN_EXPELLED_CANNOT_ENTER_INTO_AN_ALLIANCE_WITHIN_ONE_DAY_OF_WITHDRAWAL_OR_EXPULSION));
            clan.setAllyId(0);
            clan.setLeavedAlly();
            alliance.broadcastAllyStatus();
            alliance.removeAllyMember(clan.getClanId());
            alliance.setExpelledMember();
            activeChar.sendMessage(new CustomMessage("l2p.gameserver.clientpackets.RequestOustAlly.ClanDismissed", activeChar).addString(clan.getName()).addString(alliance.getAllyName()));
        }
    }
}
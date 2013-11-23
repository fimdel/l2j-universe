/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.model.Request.L2RequestType;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.company.pledge.AskJoinPledge;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

public class RequestJoinPledge extends L2GameClientPacket {
    private int _objectId;
    private int _pledgeType;

    @Override
    protected void readImpl() {
        _objectId = readD();
        _pledgeType = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null || activeChar.getClan() == null)
            return;

        if (activeChar.isOutOfControl()) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isProcessingRequest()) {
            activeChar.sendPacket(Msg.WAITING_FOR_ANOTHER_REPLY);
            return;
        }

        Clan clan = activeChar.getClan();
        if (!clan.canInvite()) {
            activeChar.sendPacket(Msg.AFTER_A_CLAN_MEMBER_IS_DISMISSED_FROM_A_CLAN_THE_CLAN_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER);
            return;
        }

        if (_objectId == activeChar.getObjectId()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_ASK_YOURSELF_TO_APPLY_TO_A_CLAN);
            return;
        }

        if ((activeChar.getClanPrivileges() & Clan.CP_CL_INVITE_CLAN) != Clan.CP_CL_INVITE_CLAN) {
            activeChar.sendPacket(Msg.ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS);
            return;
        }

        GameObject object = activeChar.getVisibleObject(_objectId);
        if (object == null || !object.isPlayer()) {
            activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            return;
        }

        Player member = (Player) object;
        if (member.getClan() == activeChar.getClan()) {
            activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            return;
        }

        if (!member.getPlayerAccess().CanJoinClan) {
            activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_JOIN_THE_CLAN_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_HE_SHE_LEFT_ANOTHER_CLAN).addName(member));
            return;
        }

        if (member.getClan() != null) {
            activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_WORKING_WITH_ANOTHER_CLAN).addName(member));
            return;
        }

        if (member.isBusy()) {
            activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER).addName(member));
            return;
        }

        if (_pledgeType == Clan.SUBUNIT_ACADEMY && (member.getLevel() > 75 || member.getClassLevel() > 3)) {
            activeChar.sendPacket(Msg.ONLY_CHARACTERS_OVER_LEVEL_76_WHO_COMPLETED_3RD_CLASS_TRANSFER_MAY_BE_A_SPONSOR_OF_ACADEMY_TRAINEE); //TODO: новое сообщение должно быть :)
            return;
        }

        if (clan.getUnitMembersSize(_pledgeType) >= clan.getSubPledgeLimit(_pledgeType)) {
            if (_pledgeType == 0)
                activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_FULL_AND_CANNOT_ACCEPT_ADDITIONAL_CLAN_MEMBERS_AT_THIS_TIME).addString(clan.getName()));
            else
                activeChar.sendPacket(Msg.THE_ACADEMY_ROYAL_GUARD_ORDER_OF_KNIGHTS_IS_FULL_AND_CANNOT_ACCEPT_NEW_MEMBERS_AT_THIS_TIME);
            return;
        }

        Request request = new Request(L2RequestType.CLAN, activeChar, member).setTimeout(10000L);
        request.set("pledgeType", _pledgeType);
        member.sendPacket(new AskJoinPledge(activeChar.getObjectId(), activeChar.getClan().getName()));
    }
}
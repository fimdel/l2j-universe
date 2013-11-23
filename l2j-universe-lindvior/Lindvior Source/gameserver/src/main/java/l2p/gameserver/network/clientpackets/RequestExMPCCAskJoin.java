/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.*;
import l2p.gameserver.model.Request.L2RequestType;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.company.MPCC.ExAskJoinMPCC;

/**
 * Format: (ch) S
 */
public class RequestExMPCCAskJoin extends L2GameClientPacket {
    private String _name;

    @Override
    protected void readImpl() {
        _name = readS(16);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.isOutOfControl()) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isProcessingRequest()) {
            activeChar.sendPacket(Msg.WAITING_FOR_ANOTHER_REPLY);
            return;
        }

        if (!activeChar.isInParty()) {
            activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL);
            return;
        }

        Player target = World.getPlayer(_name);

        // Чар с таким именем не найден в мире
        if (target == null) {
            activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE);
            return;
        }

        // Нельзя приглашать безпартийных или члена своей партии
        if (activeChar == target || !target.isInParty() || activeChar.getParty() == target.getParty()) {
            activeChar.sendPacket(Msg.YOU_HAVE_INVITED_WRONG_TARGET);
            return;
        }

        // Если приглашен в СС не лидер партии, то посылаем приглашение лидеру его партии
        if (target.isInParty() && !target.getParty().isLeader(target))
            target = target.getParty().getPartyLeader();

        if (target == null) {
            activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE);
            return;
        }

        if (target.getParty().isInCommandChannel()) {
            activeChar.sendPacket(new SystemMessage(SystemMessage.S1_PARTY_IS_ALREADY_A_MEMBER_OF_THE_COMMAND_CHANNEL).addString(target.getName()));
            return;
        }

        if (target.isBusy()) {
            activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER).addString(target.getName()));
            return;
        }

        Party activeParty = activeChar.getParty();

        if (activeParty.isInCommandChannel()) {
            // Приглашение в уже существующий СС
            // Приглашать в СС может только лидер CC
            if (activeParty.getCommandChannel().getChannelLeader() != activeChar) {
                activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL);
                return;
            }

            sendInvite(activeChar, target);
        } else // СС еще не существует. Отсылаем запрос на инвайт и в случае согласия создаем канал
            if (CommandChannel.checkAuthority(activeChar))
                sendInvite(activeChar, target);
    }

    private void sendInvite(Player requestor, Player target) {
        new Request(L2RequestType.CHANNEL, requestor, target).setTimeout(10000L);
        target.sendPacket(new ExAskJoinMPCC(requestor.getName()));
        requestor.sendMessage("You invited " + target.getName() + " to your Command Channel."); //TODO [G1ta0] custom message
    }
}
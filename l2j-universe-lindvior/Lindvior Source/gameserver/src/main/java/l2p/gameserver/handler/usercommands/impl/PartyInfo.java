package l2p.gameserver.handler.usercommands.impl;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.usercommands.IUserCommandHandler;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;

/**
 * Support for /partyinfo command
 */
public class PartyInfo implements IUserCommandHandler {
    private static final int[] COMMAND_IDS = {81};

    @Override
    public boolean useUserCommand(int id, Player activeChar) {
        if (id != COMMAND_IDS[0])
            return false;

        Party playerParty = activeChar.getParty();
        if (!activeChar.isInParty())
            return false;

        Player partyLeader = playerParty.getPartyLeader();
        if (partyLeader == null)
            return false;

        int memberCount = playerParty.getMemberCount();
        int lootDistribution = playerParty.getLootDistribution();

        activeChar.sendPacket(Msg._PARTY_INFORMATION_);

        switch (lootDistribution) {
            case Party.ITEM_LOOTER:
                activeChar.sendPacket(Msg.LOOTING_METHOD_FINDERS_KEEPERS);
                break;
            case Party.ITEM_ORDER:
                activeChar.sendPacket(Msg.LOOTING_METHOD_BY_TURN);
                break;
            case Party.ITEM_ORDER_SPOIL:
                activeChar.sendPacket(Msg.LOOTING_METHOD_BY_TURN_INCLUDING_SPOIL);
                break;
            case Party.ITEM_RANDOM:
                activeChar.sendPacket(Msg.LOOTING_METHOD_RANDOM);
                break;
            case Party.ITEM_RANDOM_SPOIL:
                activeChar.sendPacket(Msg.LOOTING_METHOD_RANDOM_INCLUDING_SPOIL);
                break;
        }

        activeChar.sendPacket(new SystemMessage(SystemMessage.PARTY_LEADER_S1).addString(partyLeader.getName()));
        activeChar.sendMessage(new CustomMessage("scripts.commands.user.PartyInfo.Members", activeChar).addNumber(memberCount));
        activeChar.sendPacket(Msg.__DASHES__);
        return true;
    }

    @Override
    public final int[] getUserCommandList() {
        return COMMAND_IDS;
    }
}
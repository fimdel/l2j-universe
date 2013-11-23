package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.network.serverpackets.SystemMessage;

public class RequestExOustFromMPCC extends L2GameClientPacket {
    private String _name;

    /**
     * format: chS
     */
    @Override
    protected void readImpl() {
        _name = readS(16);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null || !activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
            return;

        Player target = World.getPlayer(_name);

        // Чар с таким имененм не найден в мире
        if (target == null) {
            activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE);
            return;
        }

        // Сам себя нельзя
        if (activeChar == target)
            return;

        // Указанный чар не в пати, не в СС, в чужом СС
        if (!target.isInParty() || !target.getParty().isInCommandChannel() || activeChar.getParty().getCommandChannel() != target.getParty().getCommandChannel()) {
            activeChar.sendPacket(Msg.INVALID_TARGET);
            return;
        }

        // Это может делать только лидер СС
        if (activeChar.getParty().getCommandChannel().getChannelLeader() != activeChar) {
            activeChar.sendPacket(Msg.ONLY_THE_CREATOR_OF_A_CHANNEL_CAN_ISSUE_A_GLOBAL_COMMAND);
            return;
        }

        target.getParty().getCommandChannel().getChannelLeader().sendPacket(new SystemMessage(SystemMessage.S1_PARTY_HAS_BEEN_DISMISSED_FROM_THE_COMMAND_CHANNEL).addString(target.getName()));
        target.getParty().getCommandChannel().removeParty(target.getParty());
        target.getParty().broadCast(Msg.YOU_WERE_DISMISSED_FROM_THE_COMMAND_CHANNEL);
    }
}
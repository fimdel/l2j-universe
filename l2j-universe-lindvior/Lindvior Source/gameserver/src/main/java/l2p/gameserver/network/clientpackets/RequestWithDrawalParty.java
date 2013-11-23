package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.DimensionalRift;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.network.serverpackets.components.CustomMessage;

public class RequestWithDrawalParty extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Party party = activeChar.getParty();
        if (party == null) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isInOlympiadMode()) {
            activeChar.sendMessage("Вы не можете сейчас выйти из группы."); //TODO [G1ta0] custom message
            return;
        }

        Reflection r = activeChar.getParty().getReflection();
        if (r != null && r instanceof DimensionalRift && activeChar.getReflection().equals(r))
            activeChar.sendMessage(new CustomMessage("l2p.gameserver.clientpackets.RequestWithDrawalParty.Rift", activeChar));
        else if (r != null && activeChar.isInCombat())
            activeChar.sendMessage("Вы не можете сейчас выйти из группы.");
        else
            activeChar.leaveParty();
    }
}
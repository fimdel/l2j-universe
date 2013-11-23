/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.model.Request.L2RequestType;
import l2p.gameserver.model.World;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.company.party.AskJoinParty;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

public class RequestJoinParty extends L2GameClientPacket {
    private String _name;
    private int _itemDistribution;

    @Override
    protected void readImpl() {
        _name = readS(Config.CNAME_MAXLEN);
        _itemDistribution = readD();
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
            activeChar.sendPacket(SystemMsg.WAITING_FOR_ANOTHER_REPLY);
            return;
        }

        Player target = World.getPlayer(_name);
        if (target == null) {
            activeChar.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
            return;
        }

        if (target == activeChar) {
            activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            activeChar.sendActionFailed();
            return;
        }

        if (target.isBusy()) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ON_ANOTHER_TASK).addName(target));
            return;
        }

        IStaticPacket problem = target.canJoinParty(activeChar);
        if (problem != null) {
            activeChar.sendPacket(problem);
            return;
        }

        if (activeChar.isInParty()) {
            if (activeChar.getParty().getMemberCount() >= Party.MAX_SIZE) {
                activeChar.sendPacket(SystemMsg.THE_PARTY_IS_FULL);
                return;
            }

            // Только Party Leader может приглашать новых членов
            if (Config.PARTY_LEADER_ONLY_CAN_INVITE && !activeChar.getParty().isLeader(activeChar)) {
                activeChar.sendPacket(SystemMsg.ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS);
                return;
            }

            if (activeChar.getParty().isInDimensionalRift()) {
                activeChar.sendMessage(new CustomMessage("l2r.gameserver.network.clientpackets.RequestJoinParty.InDimensionalRift", activeChar));
                activeChar.sendActionFailed();
                return;
            }
        }

        PartySubstituteManager.getInstance().removePlayerFromParty(activeChar);

        new Request(L2RequestType.PARTY, activeChar, target).setTimeout(10000L).set("itemDistribution", _itemDistribution);

        target.sendPacket(new AskJoinParty(activeChar.getName(), _itemDistribution));
        activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_BEEN_INVITED_TO_THE_PARTY).addName(target));
    }
}

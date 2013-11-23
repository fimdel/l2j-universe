package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.07.12
 * Time: 14:44
 * ответ от чара выбранного на замену
 */
public class RequestAcceptWaitingSubstitute extends L2GameClientPacket {
    private int _code;
    //private int _unk1;
    //private int _unk2;

    @Override
    protected void readImpl() {
        _code = readD();
        //_unk1 = readD();
        //_unk2 = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Request request = activeChar.getRequest();
        if (request == null || !request.isTypeOf(Request.L2RequestType.SUBSTITUTE))
            return;

        if (!request.isInProgress()) {
            request.cancel();
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isOutOfControl()) {
            request.cancel();
            activeChar.sendActionFailed();
            return;
        }

        Player requestor = request.getRequestor();

        if (requestor == null) {
            request.cancel();
            activeChar.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
            activeChar.sendActionFailed();
            return;
        }

        if (requestor.getRequest() != request) {
            request.cancel();
            activeChar.sendActionFailed();
            return;
        }

        switch (_code) {
            case 0:
                request.cancel();
                break;
            case 1:
                SystemMessage2 sm = new SystemMessage2(SystemMsg.PLAYER_C1_WILL_BE_REPLACED);
                sm.addString(requestor.getName());
                requestor.getParty().getPartyLeader().sendPacket(sm);

                activeChar.sendPacket(SystemMsg.YOU_HAVE_ACCEPTED_TO_JOIN_A_PARTY);

                // TODO: Запрос ПЛу, согласен ли он
                try {
                    PartySubstituteManager.getInstance().doReplace(requestor, activeChar);
                } finally {
                    request.done();
                }
                break;
        }

    }
}

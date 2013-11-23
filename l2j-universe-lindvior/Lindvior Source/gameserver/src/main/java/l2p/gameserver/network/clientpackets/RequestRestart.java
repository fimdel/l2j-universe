package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.GameClient.GameClientState;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.network.serverpackets.CharacterSelectionInfo;
import l2p.gameserver.network.serverpackets.ExLoginVitalityEffectInfo;
import l2p.gameserver.network.serverpackets.RestartResponse;
import l2p.gameserver.network.serverpackets.components.CustomMessage;

public class RequestRestart extends L2GameClientPacket {
    /**
     * packet type id 0x57
     * format:      c
     */

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null)
            return;

        if (activeChar.isInObserverMode()) {
            activeChar.sendPacket(Msg.OBSERVERS_CANNOT_PARTICIPATE, RestartResponse.FAIL, ActionFail.STATIC);
            return;
        }

        if (activeChar.isInCombat()) {    //TODO
            activeChar.sendPacket(Msg.YOU_CANNOT_RESTART_WHILE_IN_COMBAT, RestartResponse.FAIL, ActionFail.STATIC);
            return;
        }

        if (activeChar.isFishing()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING, RestartResponse.FAIL, ActionFail.STATIC);
            return;
        }

        if (activeChar.isBlocked() && !activeChar.isFlying()) // Разрешаем выходить из игры если используется сервис HireWyvern. Вернет в начальную точку.
        {
            activeChar.sendMessage(new CustomMessage("l2p.gameserver.clientpackets.RequestRestart.OutOfControl", activeChar));
            activeChar.sendPacket(RestartResponse.FAIL, ActionFail.STATIC);
            return;
        }

        if (getClient() != null)
            getClient().setState(GameClientState.AUTHED);
        l2p.gameserver.ccpGuard.Protection.doDisconection(getClient());

        activeChar.restart();
        // send char list
        CharacterSelectionInfo cl = new CharacterSelectionInfo(getClient().getLogin(), getClient().getSessionKey().playOkID1);
        ExLoginVitalityEffectInfo vl = new ExLoginVitalityEffectInfo(cl.getCharInfo());
        sendPacket(RestartResponse.OK, cl, vl);
        getClient().setCharSelection(cl.getCharInfo());
    }
}

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.model.Request.L2RequestType;
import l2p.gameserver.network.serverpackets.SendTradeRequest;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Util;

public class TradeRequest extends L2GameClientPacket {
    //Format: cd
    private int _objectId;

    @Override
    protected void readImpl() {
        _objectId = readD();
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

        if (!activeChar.getPlayerAccess().UseTrade) {
            activeChar.sendPacket(Msg.THIS_ACCOUNT_CANOT_TRADE_ITEMS);
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isInStoreMode()) {
            activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
            return;
        }

        if (activeChar.isFishing()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
            return;
        }

        if (activeChar.isInTrade()) {
            activeChar.sendPacket(Msg.YOU_ARE_ALREADY_TRADING_WITH_SOMEONE);
            return;
        }

        if (activeChar.isProcessingRequest()) {
            activeChar.sendPacket(Msg.WAITING_FOR_ANOTHER_REPLY);
            return;
        }

        String tradeBan = activeChar.getVar("tradeBan");
        if (tradeBan != null && (tradeBan.equals("-1") || Long.parseLong(tradeBan) >= System.currentTimeMillis())) {
            if (tradeBan.equals("-1"))
                activeChar.sendMessage(new CustomMessage("common.TradeBannedPermanently", activeChar));
            else
                activeChar.sendMessage(new CustomMessage("common.TradeBanned", activeChar).addString(Util.formatTime((int) (Long.parseLong(tradeBan) / 1000L - System.currentTimeMillis() / 1000L))));
            return;
        }

        GameObject target = activeChar.getVisibleObject(_objectId);
        if (target == null || !target.isPlayer() || target == activeChar) {
            activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            return;
        }

        if (!activeChar.isInRangeZ(target, Creature.INTERACTION_DISTANCE)) {
            activeChar.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
            return;
        }

        Player reciever = (Player) target;
        if (!reciever.getPlayerAccess().UseTrade) {
            activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            return;
        }

        tradeBan = reciever.getVar("tradeBan");
        if (tradeBan != null && (tradeBan.equals("-1") || Long.parseLong(tradeBan) >= System.currentTimeMillis())) {
            activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            return;
        }

        if (reciever.isInBlockList(activeChar)) {
            activeChar.sendPacket(Msg.YOU_HAVE_BEEN_BLOCKED_FROM_THE_CONTACT_YOU_SELECTED);
            return;
        }

        if (reciever.getTradeRefusal() || reciever.isBusy()) {
            activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER).addString(reciever.getName()));
            return;
        }

        new Request(L2RequestType.TRADE_REQUEST, activeChar, reciever).setTimeout(10000L);
        reciever.sendPacket(new SendTradeRequest(activeChar.getObjectId()));
        activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_REQUESTED_A_TRADE_WITH_C1).addString(reciever.getName()));
    }
}
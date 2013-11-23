package l2p.gameserver.handler.voicecommands.impl;

import l2p.gameserver.Config;
import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;

public class Offline extends Functions implements IVoicedCommandHandler {
    private String[] _commandList = new String[]{"offline"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String args) {
        if (!Config.SERVICES_OFFLINE_TRADE_ALLOW)
            return false;

        if (activeChar.getOlympiadObserveGame() != null || activeChar.getOlympiadGame() != null || Olympiad.isRegisteredInComp(activeChar) || activeChar.getKarma() > 0) {
            activeChar.sendActionFailed();
            return false;
        }

        if (activeChar.getLevel() < Config.SERVICES_OFFLINE_TRADE_MIN_LEVEL) {
            show(new CustomMessage("voicedcommandhandlers.Offline.LowLevel", activeChar).addNumber(Config.SERVICES_OFFLINE_TRADE_MIN_LEVEL), activeChar);
            return false;
        }

        if (!activeChar.isInZone(Zone.ZoneType.offshore) && Config.SERVICES_OFFLINE_TRADE_ALLOW_OFFSHORE) {
            show(new CustomMessage("trade.OfflineNoTradeZoneOnlyOffshore", activeChar), activeChar);
            return false;
        }

        if (!activeChar.isInStoreMode()) {
            show(new CustomMessage("voicedcommandhandlers.Offline.IncorrectUse", activeChar), activeChar);
            return false;
        }

        if (activeChar.getNoChannelRemained() > 0) {
            show(new CustomMessage("voicedcommandhandlers.Offline.BanChat", activeChar), activeChar);
            return false;
        }

        if (activeChar.isActionBlocked(Zone.BLOCKED_ACTION_PRIVATE_STORE)) {
            show(new CustomMessage("trade.OfflineNoTradeZone", activeChar), activeChar);
            return false;
        }

        if (Config.SERVICES_OFFLINE_TRADE_PRICE > 0 && Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM > 0) {
            if (getItemCount(activeChar, Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM) < Config.SERVICES_OFFLINE_TRADE_PRICE) {
                show(new CustomMessage("voicedcommandhandlers.Offline.NotEnough", activeChar).addItemName(Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM).addNumber(Config.SERVICES_OFFLINE_TRADE_PRICE), activeChar);
                return false;
            }
            removeItem(activeChar, Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM, Config.SERVICES_OFFLINE_TRADE_PRICE);
        }

        activeChar.offline();
        return true;
    }

    @Override
    public String[] getVoicedCommandList() {
        return _commandList;
    }
}
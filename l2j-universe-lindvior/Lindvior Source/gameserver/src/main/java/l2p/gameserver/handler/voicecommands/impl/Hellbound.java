package l2p.gameserver.handler.voicecommands.impl;

import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.scripts.Functions;

public class Hellbound extends Functions implements IVoicedCommandHandler {
    private final String[] _commandList = new String[]{"hellbound"};

    @Override
    public String[] getVoicedCommandList() {
        return _commandList;
    }

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target) {
        if (command.equals("hellbound")) {
            activeChar.sendMessage("Hellbound level: " + HellboundManager.getHellboundLevel());
            activeChar.sendMessage("Confidence: " + HellboundManager.getConfidence());
        }
        return false;
    }
}

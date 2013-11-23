package handler.voicecommands;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.skills.skillclasses.Call;
import l2p.gameserver.utils.Location;

import java.util.List;

public class Relocate implements IVoicedCommandHandler, ScriptFile {
    public static int SUMMON_PRICE = 5;

    private final String[] _commandList = new String[]{"summon_clan"};

    @Override
    public String[] getVoicedCommandList() {
        return _commandList;
    }

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target) {
        if (command.equalsIgnoreCase("summon_clan")) {
            if (!activeChar.isClanLeader()) {
                activeChar.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
                return false;
            }

            SystemMessage msg = Call.canSummonHere(activeChar);
            if (msg != null) {
                activeChar.sendPacket(msg);
                return false;
            }

            if (activeChar.isAlikeDead()) {
                activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Relocate.Dead", activeChar));
                return false;
            }

            List<Player> clan = activeChar.getClan().getOnlineMembers(activeChar.getObjectId());

            for (Player pl : clan)
                if (Call.canBeSummoned(pl) == null)
                    // Спрашиваем, согласие на призыв
                    pl.summonCharacterRequest(activeChar, Location.findPointToStay(activeChar.getX(), activeChar.getY(), activeChar.getZ(), 100, 150, activeChar.getReflection().getGeoIndex()), SUMMON_PRICE);

            return true;
        }
        return false;
    }

    @Override
    public void onLoad() {
        VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }
}
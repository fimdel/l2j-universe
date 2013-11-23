package handler.voicecommands;

import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.skills.EffectType;

public class Leadergate implements IVoicedCommandHandler, ScriptFile {
    private String[] _commandList = new String[]{"leadergate"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target) {
        if (command.equals("leadergate"))
            if (activeChar.getClan() != null) {
                Player clanLeader = activeChar.getClan().getLeader().getPlayer();
                if (clanLeader == null)
                    return false;
                if (clanLeader.getEffectList().getEffectByType(EffectType.Immobilize) != null) {
                    if (!validateGateCondition(clanLeader, activeChar))
                        return false;
                    activeChar.teleToLocation(clanLeader.getX(), clanLeader.getY(), clanLeader.getZ());
                    return true;
                }
                activeChar.sendMessage("I cannot find the lord's signal right now, so it is impossible to cast the spell.");
            }
        return true;
    }

    private static boolean validateGateCondition(Player clanLeader, Player player) {
        if (clanLeader.isAlikeDead()) {
            // Need retail message if there's one.
            player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
            return false;
        }
        if (clanLeader.isInStoreMode()) {
            // Need retail message if there's one.
            player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
            return false;
        }
        if (clanLeader.isRooted() || clanLeader.isInCombat()) {
            // Need retail message if there's one.
            player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
            return false;
        }
        if (clanLeader.isInOlympiadMode()) {
            // Need retail message if there's one.
            player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
            return false;
        }
        if (clanLeader.isInObserverMode()) {
            // Need retail message if there's one.
            player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
            return false;
        }

        return true;

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

    @Override
    public String[] getVoicedCommandList() {
        return _commandList;
    }
}

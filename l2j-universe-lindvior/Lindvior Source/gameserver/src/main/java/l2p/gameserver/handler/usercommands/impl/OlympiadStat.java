package l2p.gameserver.handler.usercommands.impl;

import l2p.gameserver.Config;
import l2p.gameserver.handler.usercommands.IUserCommandHandler;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.olympiad.CompType;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * Support for /olympiadstat command
 */
public class OlympiadStat implements IUserCommandHandler {
    private static final int[] COMMAND_IDS = {109};

    @Override
    public boolean useUserCommand(int id, Player activeChar) {
        if (id != COMMAND_IDS[0]) {
            return false;
        }

        GameObject objectTarget = Config.OLYMPIAD_OLDSTYLE_STAT ? activeChar : activeChar.getTarget();

        if ((objectTarget == null) || !objectTarget.isPlayer() || !objectTarget.getPlayer().isNoble()) {
            activeChar.sendPacket(SystemMsg.THIS_COMMAND_CAN_ONLY_BE_USED_BY_A_NOBLESSE.packet());
            return true;
        }

        Player playerTarget = objectTarget.getPlayer();
        SystemMessage2 sm = new SystemMessage2(SystemMsg.FOR_THE_CURRENT_GRAND_OLYMPIAD_YOU_HAVE_PARTICIPATED_IN_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_CURRENTLY_HAVE_S4_OLYMPIAD_POINTS);

        sm.addInteger(Olympiad.getCompetitionDone(playerTarget.getObjectId()));
        sm.addInteger(Olympiad.getCompetitionWin(playerTarget.getObjectId()));
        sm.addInteger(Olympiad.getCompetitionLoose(playerTarget.getObjectId()));
        sm.addInteger(Olympiad.getNoblePoints(playerTarget.getObjectId()));
        activeChar.sendPacket(sm);

        final int ar = Olympiad.getWeekGameCounts(playerTarget.getObjectId());

        sm = new SystemMessage2((Olympiad.getCompetitionType() == CompType.CLASSED) ? SystemMsg.THE_MATCHES_THIS_WEEK_ARE_CLASS_BATTLES_THE_NUMBER_OF_MATCHES_THAT_ARE_ALLOWED_TO_PARTICIPATE_IS_S1 : SystemMsg.THE_MATCHES_THIS_WEEK_ARE_CLASS_FREE_BATTLES_THE_NUMBER_OF_MATCHES_THAT_ARE_ALLOWED_TO_PARTICIPATE_IS_S1);

        sm.addInteger(ar);
        activeChar.sendPacket(sm);
        return true;
    }

    @Override
    public int[] getUserCommandList() {
        return COMMAND_IDS;
    }
}
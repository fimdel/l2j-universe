/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.model.World;
import l2p.gameserver.model.actor.instances.player.Mentee;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.ExMentorAdd;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:47
 */
public class RequestMenteeAdd extends L2GameClientPacket {
    private static final int MENTEE_CERTIFICATE = 33800;
    private String _newMentee;

    @Override
    protected void runImpl() {
        GameClient client = (GameClient) getClient();
        Player activeChar = client.getActiveChar();
        Player newMentee = World.getPlayer(this._newMentee);

        if (newMentee == null) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE));
            return;
        }

        if (activeChar.getClassId().getId() < 139) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_MUST_AWAKEN_IN_ORDER_TO_BECOME_A_MENTOR));
            return;
        }

        if (activeChar.getMenteeList().getMentees().size() == 3) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.A_MENTOR_CAN_HAVE_UP_TO_3_MENTEES_AT_THE_SAME_TIME));
            return;
        }

        if (newMentee.getMenteeList().getMentor() != 0) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_ALREADY_HAS_A_MENTOR).addName(newMentee));
            return;
        }

        if (activeChar.getName().equals(this._newMentee)) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_CANNOT_BECOME_YOUR_OWN_MENTEE));
            return;
        }

        if (!newMentee.getActiveSubClass().isBase()) {
            return;
        }

        if (newMentee.getLevel() > 85) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_IS_ABOVE_LEVEL_86_AND_CANNOT_BECOME_A_MENTEE).addName(newMentee));
            return;
        }

        if (newMentee.getInventory().getItemByItemId(33800) == null) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_DOES_NOT_HAVE_THE_ITEM_NEDEED_TO_BECOME_A_MENTEE).addName(newMentee));
            return;
        }

        for (Mentee entry : activeChar.getMenteeList().getMentees()) {
            if (entry.getName().equals(this._newMentee))
                return;
        }
        long mentorPenalty = activeChar.getVarLong("mentorPenalty", 0L);

        if (mentorPenalty > System.currentTimeMillis()) {
            long milisPenalty = mentorPenalty - System.currentTimeMillis();
            double numSecs = milisPenalty / 1000L % 60L;
            double countDown = (milisPenalty / 1000L - numSecs) / 60.0D;
            int numMins = (int) Math.floor(countDown % 60.0D);
            countDown = (countDown - numMins) / 60.0D;
            int numHours = (int) Math.floor(countDown % 24.0D);
            int numDays = (int) Math.floor((countDown - numHours) / 24.0D);
            activeChar.sendPacket(((new SystemMessage2(SystemMsg.YOU_CAN_BOND_WITH_A_NEW_MENTEE_IN_S1_DAYS_S2_HOUR_S3_MINUTE).addInteger(numDays)).addInteger(numHours)).addInteger(numMins));
            return;
        }

        new Request(Request.L2RequestType.MENTEE, activeChar, newMentee).setTimeout(10000L);
        activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_OFFERED_TO_BECOME_S1_MENTOR).addName(newMentee));
        newMentee.sendPacket(new ExMentorAdd(activeChar));
    }

    @Override
    protected void readImpl() {
        this._newMentee = readS();
    }
}
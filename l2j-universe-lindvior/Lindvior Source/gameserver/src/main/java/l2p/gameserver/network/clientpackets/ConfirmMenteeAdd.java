/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.network.serverpackets.ExMentorList;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Mentoring;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 1:02
 */
public class ConfirmMenteeAdd extends L2GameClientPacket {
    private int _answer;
    private String _mentorName;

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        Request request = activeChar.getRequest();
        if ((request == null) || (!request.isTypeOf(Request.L2RequestType.MENTEE))) {
            activeChar.sendActionFailed();
            return;
        }

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

        if (this._answer == 0) {
            request.cancel();
            requestor.sendPacket(new SystemMessage2(SystemMsg.S1_HAS_DECLINED_BECOMING_YOUR_MENTEE).addString(activeChar.getName()));
            return;
        }

        if (requestor.isActionsDisabled()) {
            request.cancel();
            activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ON_ANOTHER_TASK).addString(requestor.getName()));
            activeChar.sendActionFailed();
            return;
        }

        try {
            requestor.getMenteeList().addMentee(activeChar);
            activeChar.getMenteeList().addMentor(requestor);
            activeChar.sendPacket(new SystemMessage2(SystemMsg.FROM_NOW_ON_S1_WILL_BE_YOUR_MENTOR).addName(requestor), new ExMentorList(activeChar));
            requestor.sendPacket(new SystemMessage2(SystemMsg.FROM_NOW_ON_S1_WILL_BE_YOUR_MENTEE).addName(activeChar), new ExMentorList(requestor));
            Mentoring.applyMentoringConditions(requestor);
            Mentoring.applyMentoringConditions(activeChar);
            Mentoring.addSkillsToMentor(requestor);
            Mentoring.addSkillsToMentee(activeChar);
        } finally {
            request.done();
        }
    }

    @Override
    protected void readImpl() {
        this._answer = readD();
        this._mentorName = readS();
    }
}

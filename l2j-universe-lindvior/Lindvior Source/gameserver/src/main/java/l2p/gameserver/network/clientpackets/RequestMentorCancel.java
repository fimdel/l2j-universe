/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.network.serverpackets.ExMentorList;
import l2p.gameserver.utils.Mentoring;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:49
 */
public class RequestMentorCancel extends L2GameClientPacket {
    private int _mtype;
    private String _charName;

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        Player menteeChar = World.getPlayer(this._charName);

        activeChar.getMenteeList().remove(this._charName, this._mtype == 1, true);
        activeChar.sendPacket(new ExMentorList(activeChar));

        if ((menteeChar != null) && (menteeChar.isOnline())) {
            menteeChar.getMenteeList().remove(activeChar.getName(), this._mtype != 1, false);
            menteeChar.sendPacket(new ExMentorList(menteeChar));
        }
        Mentoring.removeConditions(activeChar);
        Mentoring.setTimePenalty(this._mtype == 1 ? activeChar.getObjectId() : activeChar.getMenteeList().getMentor(), System.currentTimeMillis() + 604800000L, -1L);
    }

    @Override
    protected void readImpl() {
        this._mtype = readD();
        this._charName = readS();
    }
}

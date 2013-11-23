/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ListMenteeWaiting;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:48
 * Приходит при нажатии наставником кнопки "+" в окне учеников
 * Ответом на пакет является {@link l2p.gameserver.network.serverpackets.ListMenteeWaiting}
 */
public class RequestMenteeWaitingList extends L2GameClientPacket {
    private int maxLevel;
    private int minLevel;
    private int page;

    @Override
    protected void runImpl()
            throws Exception {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null) {
            return;
        }
        activeChar.sendPacket(new ListMenteeWaiting(activeChar, this.page, this.minLevel, this.maxLevel));
    }

    @Override
    protected void readImpl()
            throws Exception {
        this.page = readD();
        this.minLevel = readD();
        this.maxLevel = readD();
    }
}

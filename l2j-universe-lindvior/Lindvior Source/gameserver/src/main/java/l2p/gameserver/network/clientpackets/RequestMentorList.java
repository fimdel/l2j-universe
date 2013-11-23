/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExMentorList;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:49
 * Приходит при нажатии кнопки Friends в клиенте.
 * Не имеет структуры, ответом на этот запрос является пакет {@link l2p.gameserver.network.serverpackets.ExMentorList}
 */
public class RequestMentorList extends L2GameClientPacket {
    @Override
    protected void runImpl() {
    }

    @Override
    protected void readImpl() {
        Player activeChar = getClient().getActiveChar();
        sendPacket(new ExMentorList(activeChar));
    }
}

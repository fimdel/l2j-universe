/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.mail.Mail;

public class ExUnReadMailCount extends L2GameServerPacket {
    public int count;

    public ExUnReadMailCount(Mail mail) {
        count = mail.getMessageId();
    }

    @Override
    protected void writeImpl() {
        writeEx(0x146);
        writeD(count);
    }
}

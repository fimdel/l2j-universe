/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.CuriousHouse;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.ExInzoneWaitingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestInzoneWaitingTime extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestInzoneWaitingTime.class);

    protected void readImpl() {
    }

    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.sendPacket(new ExInzoneWaitingInfo(player));
        _log.info("[IMPLEMENT ME!] RequestInzoneWaitingTime (maybe trigger)");
    }
}

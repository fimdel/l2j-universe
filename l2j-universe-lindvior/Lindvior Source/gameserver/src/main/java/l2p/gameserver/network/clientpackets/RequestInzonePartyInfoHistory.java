package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.07.12
 * Time: 11:20
 */
public class RequestInzonePartyInfoHistory extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestInzonePartyInfoHistory.class);

    protected void readImpl() {
    }

    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        _log.info("[IMPLEMENT ME!] RequestInzonePartyInfoHistory (maybe trigger)");
    }

    public String getType() {
        return "[C] D0:9A RequestInzonePartyInfoHistory";
    }
}

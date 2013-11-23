package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author n0nam3
 * @date 22/08/2010 15:16
 */

public class RequestLinkHtml extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestLinkHtml.class);

    //Format: cS
    private String _link;

    @Override
    protected void readImpl() {
        _link = readS();
    }

    @Override
    protected void runImpl() {

        Player actor = getClient().getActiveChar();
        if (actor == null)
            return;

        if (_link.contains("..") || !_link.endsWith(".htm")) {
            _log.warn("[RequestLinkHtml] hack? link contains prohibited characters: '" + _link + "', skipped");
            return;
        }
        try {
            NpcHtmlMessage msg = new NpcHtmlMessage(0);
            msg.setFile("" + _link);
            sendPacket(msg);
        } catch (Exception e) {
            _log.warn("Bad RequestLinkHtml: ", e);
        }
    }
}
package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class RequestBlock extends L2GameClientPacket {
    // format: cd(S)
    private static final Logger _log = LoggerFactory.getLogger(RequestBlock.class);

    private final static int BLOCK = 0;
    private final static int UNBLOCK = 1;
    private final static int BLOCKLIST = 2;
    private final static int ALLBLOCK = 3;
    private final static int ALLUNBLOCK = 4;

    private Integer _type;
    private String targetName = null;

    @Override
    protected void readImpl() {
        _type = readD(); //0x00 - block, 0x01 - unblock, 0x03 - allblock, 0x04 - allunblock

        if (_type == BLOCK || _type == UNBLOCK)
            targetName = readS(16);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        switch (_type) {
            case BLOCK:
                activeChar.addToBlockList(targetName);
                break;
            case UNBLOCK:
                activeChar.removeFromBlockList(targetName);
                break;
            case BLOCKLIST:
                Collection<String> blockList = activeChar.getBlockList();

                if (blockList != null) {
                    activeChar.sendPacket(Msg._IGNORE_LIST_);

                    for (String name : blockList)
                        activeChar.sendMessage(name);

                    activeChar.sendPacket(Msg.__EQUALS__);
                }
                break;
            case ALLBLOCK:
                activeChar.setBlockAll(true);
                activeChar.sendPacket(Msg.YOU_ARE_NOW_BLOCKING_EVERYTHING);
                activeChar.sendEtcStatusUpdate();
                break;
            case ALLUNBLOCK:
                activeChar.setBlockAll(false);
                activeChar.sendPacket(Msg.YOU_ARE_NO_LONGER_BLOCKING_EVERYTHING);
                activeChar.sendEtcStatusUpdate();
                break;
            default:
                _log.info("Unknown 0x0a block type: " + _type);
        }
    }
}
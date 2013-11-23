package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.SystemMessage;

public class RequestShowBoard extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int _unknown;

    /**
     * packet type id 0x5E
     * <p/>
     * sample
     * <p/>
     * 5E
     * 01 00 00 00
     * <p/>
     * format:		cd
     */
    @Override
    public void readImpl() {
        _unknown = readD();
    }

    @Override
    public void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (Config.COMMUNITYBOARD_ENABLED) {
            ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(Config.BBS_DEFAULT);
            if (handler != null)
                handler.onBypassCommand(activeChar, Config.BBS_DEFAULT);
        } else
            activeChar.sendPacket(new SystemMessage(SystemMessage.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE));
    }
}

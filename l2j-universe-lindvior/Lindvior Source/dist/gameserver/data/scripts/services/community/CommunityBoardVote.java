/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import l2p.gameserver.Config;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommunityBoardVote implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardVote.class);

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: Vote service loaded.");
            CommunityBoardHandler.getInstance().registerHandler(this);
        }
    }

    @Override
    public void onReload() {
        if (Config.COMMUNITYBOARD_ENABLED)
            CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]{"_bbsvote"};
    }

    @Override
    public void onBypassCommand(Player player, String command) {
        //TODO
        /*if(command.equals("_bbsvote"))
          else if(command.startsWith("_bbssmscounrty"))
          {}
          else */
        if (player.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br>", player);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
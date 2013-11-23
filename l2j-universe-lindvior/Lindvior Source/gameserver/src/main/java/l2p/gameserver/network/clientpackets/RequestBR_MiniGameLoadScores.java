package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExBR_MiniGameLoadScores;

/**
 * @author VISTALL
 * @date 19:57:41/25.05.2010
 */
public class RequestBR_MiniGameLoadScores extends L2GameClientPacket {
    @Override
    protected void readImpl() throws Exception {
        //
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null || !Config.EX_JAPAN_MINIGAME)
            return;

        player.sendPacket(new ExBR_MiniGameLoadScores(player));
    }
}
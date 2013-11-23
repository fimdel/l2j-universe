package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.instancemanager.games.FishingChampionShipManager;
import l2p.gameserver.model.Player;

/**
 * @author n0nam3
 * @date 08/08/2010 15:53
 */
public class RequestExFishRanking extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
            FishingChampionShipManager.getInstance().showMidResult(getClient().getActiveChar());
    }
}
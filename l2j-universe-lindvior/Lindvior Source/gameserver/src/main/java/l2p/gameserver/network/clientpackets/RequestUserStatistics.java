package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.WorldStatisticsManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.WorldStatistic.CharacterStatisticElement;
import l2p.gameserver.network.serverpackets.ExLoadStatUser;

import java.util.List;

public class RequestUserStatistics extends L2GameClientPacket {

    @Override
    protected void readImpl() throws Exception {
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();

        if (player == null)
            return;

        List<CharacterStatisticElement> stat = WorldStatisticsManager.getInstance().getCurrentStatisticsForPlayer(player.getObjectId());
        player.sendPacket(new ExLoadStatUser(stat));
    }
}

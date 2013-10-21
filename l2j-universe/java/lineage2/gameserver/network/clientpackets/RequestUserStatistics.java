package lineage2.gameserver.network.clientpackets;

import java.util.List;

import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.worldstatistics.CharacterStatisticElement;
import lineage2.gameserver.network.serverpackets.ExLoadStatUser;

public class RequestUserStatistics extends L2GameClientPacket
{

	@Override
	protected void readImpl() throws Exception
	{
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();

		if (player == null)
			return;

		List<CharacterStatisticElement> stat = WorldStatisticsManager.getInstance().getCurrentStatisticsForPlayer(player.getObjectId());
		player.sendPacket(new ExLoadStatUser(stat));
	}
}
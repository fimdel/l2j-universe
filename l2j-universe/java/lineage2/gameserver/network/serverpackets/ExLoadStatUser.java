package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.worldstatistics.CharacterStatisticElement;

import java.util.List;

/**
 * @author KilRoy
 */
public class ExLoadStatUser extends L2GameServerPacket
{
	private List<CharacterStatisticElement> list;

	public ExLoadStatUser(List<CharacterStatisticElement> list)
	{
		this.list = list;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x102);
		writeD(list.size());
		for (CharacterStatisticElement stat : list)
		{
			writeD(stat.getCategoryType().getClientId());
			writeD(stat.getCategoryType().getSubcat());
			writeQ(stat.getMonthlyValue());
			writeQ(stat.getValue());
		}
	}
}
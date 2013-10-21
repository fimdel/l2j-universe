package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.worldstatistics.CharacterStatistic;

import java.util.List;

public class ExLoadStatHotLink extends L2GameServerPacket
{
	private final int categoryId;
	private final int subCatId;
	private List<CharacterStatistic> globalStatistic;
	private List<CharacterStatistic> monthlyStatistic;

	public ExLoadStatHotLink(int categoryId, int subCatId, List<CharacterStatistic> globalStatistic, List<CharacterStatistic> monthlyStatistic)
	{
		this.categoryId = categoryId;
		this.subCatId = subCatId;
		this.globalStatistic = globalStatistic;
		this.monthlyStatistic = monthlyStatistic;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x103);

		writeD(categoryId); // catId
		writeD(subCatId); // subCatId
		// Monthly
		writeD(monthlyStatistic.size()); // loop count (always 5)
		for (int i = 0; i < monthlyStatistic.size(); i++)
		{
			CharacterStatistic statistic = monthlyStatistic.get(i);
			writeH(i + 1); // rating pos
			writeD(statistic.getObjId()); // objId
			writeS(statistic.getName()); // CharName
			writeQ(statistic.getValue()); // Value
			writeH(0x00);// TODO: Поднялся или опустился в рейтинге
			writeD(0x00);
			writeD(0x00);
		}
		// General
		writeD(globalStatistic.size()); // loop count (always 5)
		for (int i = 0; i < globalStatistic.size(); i++)
		{
			CharacterStatistic statistic = globalStatistic.get(i);
			writeH(i + 1); // rating pos
			writeD(statistic.getObjId()); // objId
			writeS(statistic.getName()); // CharName
			writeQ(statistic.getValue()); // Value
			writeH(0x00);// TODO: Поднялся или опустился в рейтинге
			writeD(0x00);
			writeD(0x00);
		}
	}
}
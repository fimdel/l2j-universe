package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.worldstatistics.CharacterStatistic;

import java.util.List;

/**
 * @author KilRoy
 */
public class ExLoadStatWorldRank extends L2GameServerPacket
{
	private final int _section;
	private final int _subSection;
	private final List<CharacterStatistic> _monthlyData;
	private final List<CharacterStatistic> _generalData;

	public ExLoadStatWorldRank(int section, int subSection, List<CharacterStatistic> generalData, List<CharacterStatistic> monthlyData)
	{
		_section = section;
		_subSection = subSection;
		_generalData = generalData;
		_monthlyData = monthlyData;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x101);

		writeD(_section);
		writeD(_subSection);

		writeD(_monthlyData.size());
		for (int i = 0, monthlyDataSize = _monthlyData.size(); i < monthlyDataSize; i++)
		{
			CharacterStatistic statistic = _monthlyData.get(i);
			writeH(i + 1);
			writeD(statistic.getObjId());
			writeS(statistic.getName());
			writeQ(statistic.getValue());
			writeH(0);
			writeD(statistic.getClanObjId());
			writeD(statistic.getClanCrestId());
		}

		writeD(_generalData.size());
		for (int i = 0, generalDataSize = _generalData.size(); i < generalDataSize; i++)
		{
			CharacterStatistic statistic = _generalData.get(i);
			writeH(i + 1);
			writeD(statistic.getObjId());
			writeS(statistic.getName());
			writeQ(statistic.getValue());
			writeH(0);
			writeD(statistic.getObjId());
			writeD(statistic.getClanCrestId());
		}
	}
}

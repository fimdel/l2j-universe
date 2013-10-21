package lineage2.gameserver.network.serverpackets;

public class ExVitalityPointInfo extends L2GameServerPacket
{
	private final int _vitality;

	public ExVitalityPointInfo(int vitality)
	{
		_vitality = vitality;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xA1);
		writeD(_vitality);
		writeD(0);
		writeD(0);
		writeD(0);
	}
}
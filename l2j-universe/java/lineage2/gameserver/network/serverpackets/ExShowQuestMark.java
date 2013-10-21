package lineage2.gameserver.network.serverpackets;

public class ExShowQuestMark extends L2GameServerPacket
{
	private int _questId;
	private int _cond;

	public ExShowQuestMark(int questId, int cond)
	{
		_questId = questId;
		_cond = cond;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x21);
		writeD(_questId);
		writeD(_cond);
	}
}
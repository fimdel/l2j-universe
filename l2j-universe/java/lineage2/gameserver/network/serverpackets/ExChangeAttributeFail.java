package lineage2.gameserver.network.serverpackets;

public class ExChangeAttributeFail extends L2GameServerPacket
{
	public static final L2GameServerPacket STATIC = new ExChangeAttributeFail();

	public ExChangeAttributeFail()
	{
		//
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x11B);
	}
}
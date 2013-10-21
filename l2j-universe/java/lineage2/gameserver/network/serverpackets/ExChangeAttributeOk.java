package lineage2.gameserver.network.serverpackets;

public class ExChangeAttributeOk extends L2GameServerPacket
{
	public static final L2GameServerPacket STATIC = new ExChangeAttributeOk();

	public ExChangeAttributeOk()
	{
		//
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x11a);
	}
}
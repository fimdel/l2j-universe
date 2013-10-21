package lineage2.gameserver.network.serverpackets;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 14.06.12 Time: 17:17
 */
public class ExDummyPacket extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x00);
		// TODO
	}
}

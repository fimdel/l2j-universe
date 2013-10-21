package lineage2.gameserver.network.serverpackets;

/**
 * Created by IntelliJ IDEA. User: Darvin Date: 24.02.12 Time: 18:17
 */
public class DummyPacket extends L2GameServerPacket
{

	@Override
	protected final void writeImpl()
	{
		writeC(0x8d);
		// TODO maybe trigger
	}
}

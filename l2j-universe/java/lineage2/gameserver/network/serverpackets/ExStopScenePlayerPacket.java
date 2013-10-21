package lineage2.gameserver.network.serverpackets;

/**
 * @author K1mel
 */
public class ExStopScenePlayerPacket extends L2GameServerPacket
{
	public ExStopScenePlayerPacket()
	{
		//
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0xE7);
		// TODO: [K1mel]
	}
}

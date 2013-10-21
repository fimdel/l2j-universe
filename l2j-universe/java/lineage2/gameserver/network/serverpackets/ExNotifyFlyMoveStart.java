package lineage2.gameserver.network.serverpackets;

public final class ExNotifyFlyMoveStart extends L2GameServerPacket
{
	public static final L2GameServerPacket STATIC = new ExNotifyFlyMoveStart();

	public ExNotifyFlyMoveStart()
	{
		// trigger
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x115);
	}
}
package lineage2.gameserver.network.serverpackets;

public class FlySelfDestination extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x44);
		// TODO dddd
	}
}
package lineage2.gameserver.network.serverpackets;

public class ExRaidReserveResult extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xB7);
		// TODO dx[dddd]
	}
}
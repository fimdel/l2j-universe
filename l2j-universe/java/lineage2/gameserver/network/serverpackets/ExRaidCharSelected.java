package lineage2.gameserver.network.serverpackets;

public class ExRaidCharSelected extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xBB);
		// just a trigger
	}
}
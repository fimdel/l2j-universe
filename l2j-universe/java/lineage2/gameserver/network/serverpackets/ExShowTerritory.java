package lineage2.gameserver.network.serverpackets;

public class ExShowTerritory extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x8D);
		// TODO ddd[dd]
	}
}
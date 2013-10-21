package lineage2.gameserver.network.serverpackets;

public class ExGet24HzSessionID extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x109);
		// TODO: [K1mel]
	}
}
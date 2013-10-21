package lineage2.gameserver.network.serverpackets;

public class ExTutorialList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x6C);
		writeB(new byte[128]);
	}
}
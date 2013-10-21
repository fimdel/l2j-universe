package lineage2.gameserver.network.serverpackets;

public class ExPeriodicItemList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x88);
		writeD(0); // count of dd
	}
}
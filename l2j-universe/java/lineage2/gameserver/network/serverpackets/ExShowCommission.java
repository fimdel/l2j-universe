package lineage2.gameserver.network.serverpackets;

public class ExShowCommission extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xF2);
		writeD(1);// unk
	}
}
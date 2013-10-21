package lineage2.gameserver.network.serverpackets;

public class ExPartyMemberRenamed extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xA7);
		// TODO ddd
	}
}
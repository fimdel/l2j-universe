package lineage2.gameserver.network.serverpackets;

public class ExShowPetitionHtml extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xB2);
		// TODO dx[dcS]
	}
}
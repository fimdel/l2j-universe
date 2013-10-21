package lineage2.gameserver.network.serverpackets;

public class ExAgitAuctionCmdPacket extends L2GameServerPacket
{
	// 0xfe:0xd1 ExAgitAuctionCmdPacket

	@Override
	protected void writeImpl()
	{
		writeEx(0xD2);
	}
}

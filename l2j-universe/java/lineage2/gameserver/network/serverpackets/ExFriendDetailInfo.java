package lineage2.gameserver.network.serverpackets;

public class ExFriendDetailInfo extends L2GameServerPacket
{
	public ExFriendDetailInfo()
	{
		//
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0xEC);
		// TODO: [K1mel]
	}
}

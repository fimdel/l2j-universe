package lineage2.gameserver.network.serverpackets;

/**
 * Created by IntelliJ IDEA. User: Darvin Date: 24.02.12 Time: 18:05
 */
public class FriendAdd extends L2GameServerPacket
{

	private String _unk;

	public FriendAdd(String unk)
	{
		_unk = unk;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x56);
		writeS(_unk);
		writeD(0);
		writeD(0);
		writeS(_unk);
		writeC(0);
		writeD(0);
	}
}

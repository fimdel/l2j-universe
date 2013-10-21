package lineage2.gameserver.network.serverpackets;

public class FriendStatus extends L2GameServerPacket
{
	public FriendStatus()
	{
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x59);
		// TODO: when implementing, consult an up-to-date
		// packets_game_server.xml and/or savormix
		writeD(0); // Offline, branching condition
		writeS(""); // Name
		// if char is offline
		{
			writeD(0); // Friend OID
		}
	}
}

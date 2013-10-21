package lineage2.gameserver.network.serverpackets;

/**
 * Created by IntelliJ IDEA. User: Darvin Date: 24.02.12 Time: 18:21
 */
public class FriendRemove extends L2GameServerPacket
{

	@Override
	protected final void writeImpl()
	{
		writeC(0x57);
		// TODO dddSS cddQddQ SSQQQd Sd dS
	}
}

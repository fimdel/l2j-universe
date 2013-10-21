package lineage2.gameserver.network.serverpackets;

/**
 * Created by IntelliJ IDEA. User: Darvin Date: 24.02.12 Time: 18:15
 */
public class SetPledgeCrestPacket extends L2GameServerPacket
{

	@Override
	protected void writeImpl()
	{
		writeC(0x69);
		// TODO maybe trigger
	}
}

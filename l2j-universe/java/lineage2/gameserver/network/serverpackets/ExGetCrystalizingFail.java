package lineage2.gameserver.network.serverpackets;

/**
 * @author ALF
 */
public class ExGetCrystalizingFail extends L2GameServerPacket
{

	@Override
	protected void writeImpl()
	{
		writeEx(0xE2);

	}

}

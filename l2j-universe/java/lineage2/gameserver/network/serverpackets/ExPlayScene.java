package lineage2.gameserver.network.serverpackets;

/**
 * Format: ch Протокол 828: при отправке пакета клиенту ничего не происходит.
 */
public class ExPlayScene extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x5D);
		writeD(0x00); // Kamael
	}
}
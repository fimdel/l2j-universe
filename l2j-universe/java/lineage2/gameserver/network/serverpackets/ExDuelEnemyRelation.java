package lineage2.gameserver.network.serverpackets;

public class ExDuelEnemyRelation extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x5A);
		// just trigger
	}
}
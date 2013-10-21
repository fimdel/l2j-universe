package lineage2.gameserver.network.serverpackets;

public class ExPVPMatchRecord extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x7E);
		// TODO ddddd d[Sdd] d[Sdd] (currentState:%d blueTeamTotalKillCnt:%d,
		// redTeamTotalKillCnt:%d)
	}
}
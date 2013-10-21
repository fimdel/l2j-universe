package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.events.objects.KrateisCubePlayerObject;

/**
 * @author VISTALL
 */
public class ExPVPMatchCCMyRecord extends L2GameServerPacket
{
	private int _points;

	public ExPVPMatchCCMyRecord(KrateisCubePlayerObject player)
	{
		_points = player.getPoints();
	}

	@Override
	public void writeImpl()
	{
		writeEx(0x8A);
		writeD(_points);
	}
}
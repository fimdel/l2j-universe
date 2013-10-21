package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.templates.jump.JumpPoint;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public class ExFlyMove extends L2GameServerPacket
{
	public static final int MANY_WAY_TYPE = 0;
	public static final int ONE_WAY_TYPE = 2;

	private int _objId;
	private JumpPoint[] _points;
	private int _type;
	private int _trackId;

	public ExFlyMove(int objId, JumpPoint[] points, int trackId)
	{
		_objId = objId;
		_points = points;
		if (_points.length > 1)
			_type = MANY_WAY_TYPE;
		else
			_type = ONE_WAY_TYPE;
		_trackId = trackId;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xE8);
		writeD(_objId); // Player Object ID

		writeD(_type); // Fly Type (1 - Many Way, 2 - One Way)
		writeD(0x00); // UNK
		writeD(_trackId); // Track ID

		writeD(_points.length); // Next Points Count
		for (JumpPoint point : _points)
		{
			writeD(point.getNextWayId()); // Next Way ID
			writeD(0x00); // UNK
			writeD(point.getLocation().getX());
			writeD(point.getLocation().getY());
			writeD(point.getLocation().getZ());
		}
	}
}

package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.utils.Location;

public class FlyToLocation extends L2GameServerPacket
{
	private int _chaObjId;
	private final FlyType _type;
	private Location _loc;
	private Location _destLoc;
	private int _speed;

	public enum FlyType
	{
		THROW_UP, THROW_HORIZONTAL, DUMMY, CHARGE, PUSH_HORIZONTAL, JUMP_EFFECTED, NOT_USED, PUSH_DOWN_HORIZONTAL, WARP_BACK, WARP_FORWARD, NONE;// для
		                                                                                                                                         // скилов
		                                                                                                                                         // надо...
	}

	public FlyToLocation(Creature cha, Location destLoc, FlyType type, int speed)
	{
		_destLoc = destLoc;
		_type = type;
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_speed = speed;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0xd4);
		writeD(_chaObjId);
		writeD(_destLoc.x);
		writeD(_destLoc.y);
		writeD(_destLoc.z);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_type.ordinal());
		writeD(_speed);
		writeD(0x00); // Unknown (GOD)
	}
}
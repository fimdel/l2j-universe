package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.utils.Location;

public class ExStopMoveAirShip extends L2GameServerPacket
{
	private int boat_id;
	private Location _loc;

	public ExStopMoveAirShip(Boat boat)
	{
		boat_id = boat.getBoatId();
		_loc = boat.getLoc();
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x67);
		writeD(boat_id);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
	}
}
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.utils.Location;

public class ExMoveToLocationAirShip extends L2GameServerPacket
{
	private int _objectId;
	private Location _origin, _destination;

	public ExMoveToLocationAirShip(Boat boat)
	{
		_objectId = boat.getBoatId();
		_origin = boat.getLoc();
		_destination = boat.getDestination();
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x66);
		writeD(_objectId);

		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z);
		writeD(_origin.x);
		writeD(_origin.y);
		writeD(_origin.z);
	}
}
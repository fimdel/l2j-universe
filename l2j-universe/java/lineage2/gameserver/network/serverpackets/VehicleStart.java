package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.boat.Boat;

public class VehicleStart extends L2GameServerPacket
{
	private int _objectId, _state;

	public VehicleStart(Boat boat)
	{
		_objectId = boat.getBoatId();
		_state = boat.getRunState();
	}

	@Override
	protected void writeImpl()
	{
		writeC(0xC0);
		writeD(_objectId);
		writeD(_state);
	}
}
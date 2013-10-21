package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.utils.Location;

public class ExMoveToTargetInAirShip extends L2GameServerPacket
{
	private int char_id, boat_id, target_id, _dist;
	private Location _loc;

	public ExMoveToTargetInAirShip(Player cha, Boat boat, int targetId, int dist, Location origin)
	{
		char_id = cha.getObjectId();
		boat_id = boat.getBoatId();
		target_id = targetId;
		_dist = dist;
		_loc = origin;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x72);

		writeD(char_id); // ID:%d
		writeD(target_id); // TargetID:%d
		writeD(_dist); // Dist:%d
		writeD(_loc.y); // OriginX:%d
		writeD(_loc.z); // OriginY:%d
		writeD(_loc.h); // OriginZ:%d
		writeD(boat_id); // AirShipID:%d
	}
}
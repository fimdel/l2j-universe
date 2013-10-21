package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.Shuttle;
import lineage2.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class ExMTLInShuttlePacket extends L2GameServerPacket
{
	private int _playableObjectId, _shuttleId;
	private Location _origin, _destination;

	public ExMTLInShuttlePacket(Player player, Shuttle shuttle, Location origin, Location destination)
	{
		_playableObjectId = player.getObjectId();
		_shuttleId = shuttle.getBoatId();
		_origin = origin;
		_destination = destination;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0xCF);
		writeD(_playableObjectId); // Player ObjID
		writeD(_shuttleId); // Shuttle ID (Arkan: 1,2; Cruma: 3)
		writeD(_destination.x); // Destination X in shuttle
		writeD(_destination.y); // Destination Y in shuttle
		writeD(_destination.z); // Destination Z in shuttle
		writeD(_origin.x); // X in shuttle
		writeD(_origin.y); // Y in shuttle
		writeD(_origin.z); // Z in shuttle
	}
}
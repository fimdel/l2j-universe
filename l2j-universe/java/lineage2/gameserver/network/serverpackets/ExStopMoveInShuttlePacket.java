package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class ExStopMoveInShuttlePacket extends L2GameServerPacket
{
	private int _playerObjectId, _shuttleId, _playerHeading;
	private Location _loc;

	public ExStopMoveInShuttlePacket(Player cha)
	{
		_playerObjectId = cha.getObjectId();
		_shuttleId = cha.getBoat().getBoatId();
		_loc = cha.getInBoatPosition();
		_playerHeading = cha.getHeading();
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0xD0);
		writeD(_playerObjectId);// Player ObjID
		writeD(_shuttleId);// Shuttle ID (Arkan: 1,2)
		writeD(_loc.x); // X in shuttle
		writeD(_loc.y); // Y in shuttle
		writeD(_loc.z); // Z in shuttle
		writeD(_playerHeading); // H in shuttle
	}
}
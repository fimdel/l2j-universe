package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

/**
 * Format: (chd) ddd d: always -1 d: player team d: player object id
 */
public class ExCubeGameChangeTeam extends L2GameServerPacket
{
	private int _objectId;
	private boolean _fromRedTeam;

	public ExCubeGameChangeTeam(Player player, boolean fromRedTeam)
	{
		_objectId = player.getObjectId();
		_fromRedTeam = fromRedTeam;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x97);
		writeD(0x05);

		writeD(_objectId);
		writeD(_fromRedTeam ? 0x01 : 0x00);
		writeD(_fromRedTeam ? 0x00 : 0x01);
	}
}
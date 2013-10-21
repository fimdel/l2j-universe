package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.utils.Location;

public class ExTeleportToLocationActivate extends L2GameServerPacket
{
	private int _targetId;
	private Location _loc;

	public ExTeleportToLocationActivate(GameObject cha, int x, int y, int z)
	{
		_targetId = cha.getObjectId();
		_loc = new Location(x, y, z, cha.getHeading());
	}

	@Override
    protected final void writeImpl()
	{
        writeEx(0x142);
		writeD(_targetId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z + Config.CLIENT_Z_SHIFT);
		writeD(0x00); // IsValidation
		writeD(_loc.h);
    }
}
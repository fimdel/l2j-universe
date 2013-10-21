package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

public class ExUISetting extends L2GameServerPacket
{
	private final byte data[];

	public ExUISetting(Player player)
	{
		data = player.getKeyBindings();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x71);
		writeD(data.length);
		writeB(data);
	}
}

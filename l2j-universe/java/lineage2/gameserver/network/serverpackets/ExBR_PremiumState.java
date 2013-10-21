package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

public class ExBR_PremiumState extends L2GameServerPacket
{
	private int _objectId;
	private int _state;

	public ExBR_PremiumState(Player activeChar, boolean state)
	{
		_objectId = activeChar.getObjectId();
		_state = state ? 1 : 0;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xDA);
		writeD(_objectId);
		writeC(_state);
	}
}

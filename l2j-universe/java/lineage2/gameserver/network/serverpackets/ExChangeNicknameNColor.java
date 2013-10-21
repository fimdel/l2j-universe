package lineage2.gameserver.network.serverpackets;

public class ExChangeNicknameNColor extends L2GameServerPacket
{
	private int _itemObjId;

	public ExChangeNicknameNColor(int itemObjId)
	{
		_itemObjId = itemObjId;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x84);
		writeD(_itemObjId);
	}
}
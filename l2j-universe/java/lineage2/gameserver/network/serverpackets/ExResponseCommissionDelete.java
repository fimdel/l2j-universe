package lineage2.gameserver.network.serverpackets;

public class ExResponseCommissionDelete extends L2GameServerPacket
{
	private int _code;
	private int _itemId;
	private long _count;
	
	public ExResponseCommissionDelete(int code, int itemId, long count)
	{
		this._code = code;
		_itemId = itemId;
		_count = count;
	}
	
	protected void writeImpl()
	{
		writeEx(0xF6);
		writeD(_code);
		writeD(_itemId);
		writeQ(_count);
	}
}

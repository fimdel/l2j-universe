package lineage2.gameserver.network.serverpackets;

public class ExPledgeCrestLarge extends L2GameServerPacket
{
	private int _crestId;
	private byte[] _data;
	private int _i;

	public ExPledgeCrestLarge(int crestId, byte[] data, int i)
	{
		_crestId = crestId;
		_data = data;
		_i = i;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x1b);
		writeD(0x00);
		writeD(_crestId);
		writeD(_i);			//split number
		writeD(65664);		//total size
		writeD(_data.length);	//split size
		writeB(_data);	//split data
	}
}
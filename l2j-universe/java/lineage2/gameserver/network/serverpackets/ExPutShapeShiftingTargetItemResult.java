package lineage2.gameserver.network.serverpackets;

/**
 * @author kick
 **/
public class ExPutShapeShiftingTargetItemResult extends L2GameServerPacket
{
	public static L2GameServerPacket FAIL = new ExPutShapeShiftingTargetItemResult(0x00, 0L);
	public static int SUCCESS_RESULT = 0x01;

	private final int _resultId;
	private final long _price;

	public ExPutShapeShiftingTargetItemResult(int resultId, long price)
	{
		_resultId = resultId;
		_price = price;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x130);
		writeD(_resultId);
		writeQ(_price);
	}
}
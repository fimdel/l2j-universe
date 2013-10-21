package lineage2.gameserver.network.serverpackets;

/**
 * @author kick
 **/
public class ExShapeShiftingResult extends L2GameServerPacket
{
	public static L2GameServerPacket FAIL = new ExShapeShiftingResult(0x00, 0, 0);
	public static int SUCCESS_RESULT = 0x01;
	;

	private final int _result;
	private final int _targetItemId;
	private final int _extractItemId;

	public ExShapeShiftingResult(int result, int targetItemId, int extractItemId)
	{
		_result = result;
		_targetItemId = targetItemId;
		_extractItemId = extractItemId;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x132);
		writeD(_result); //Result
		writeD(_targetItemId);
		writeD(_extractItemId);
	}
}
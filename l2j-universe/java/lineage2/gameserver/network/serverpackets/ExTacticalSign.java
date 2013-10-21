package lineage2.gameserver.network.serverpackets;

/**
 * @author ALF
 * @modified KilRoy
 * @date 29.07.2012
 */
public class ExTacticalSign extends L2GameServerPacket
{
	/**
	 * Field _targetId.
	 */
	private final int _targetId;
	/**
	 * Field _signId.
	 */
	private final int _signId;

	public static int SIGN_NONE = 0;
	public static int SIGN_STAR = 1;
	public static int SIGN_HEART = 2;
	public static int SIGN_MOON = 3;
	public static int SIGN_CROSS = 4;

	
	/**
	 * Constructor for ExTacticalSign.
	 * @param target int
	 * @param sign int
	 */
	public ExTacticalSign(int target, int sign)
	{
		_targetId = target;
		_signId = sign;
	}

	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x100);
		writeD(_targetId);
		writeD(_signId);
	}
}
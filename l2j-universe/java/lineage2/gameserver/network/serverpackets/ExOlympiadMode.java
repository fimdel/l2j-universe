package lineage2.gameserver.network.serverpackets;

public class ExOlympiadMode extends L2GameServerPacket
{
	// chc
	private int _mode;

	/**
	 * @param _mode
	 *            (0 = return, 3 = spectate)
	 */
	public ExOlympiadMode(int mode)
	{
		_mode = mode;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x7D);

		writeC(_mode);
	}
}
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Creature;

/**
 * 0000: 3e 2a 89 00 4c 01 00 00 00 .|...
 * <p/>
 * format dd
 */
public class ChangeMoveType extends L2GameServerPacket
{
	public static int WALK = 0;
	public static int RUN = 1;

	private int _chaId;
	private boolean _running;

	public ChangeMoveType(Creature cha)
	{
		_chaId = cha.getObjectId();
		_running = cha.isRunning();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x28);
		writeD(_chaId);
		writeD(_running ? RUN : WALK);
		writeD(0); // c2
	}
}
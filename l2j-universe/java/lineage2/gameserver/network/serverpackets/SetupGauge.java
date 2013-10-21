package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Creature;

public class SetupGauge extends L2GameServerPacket
{
	public static final int BLUE_DUAL = 0;
	public static final int BLUE = 1;
	public static final int BLUE_MINI = 2;
	public static final int GREEN_MINI = 3;
	public static final int RED_MINI = 4;

	private int _charId;
	private int _dat1;
	private int _time;

	public static enum Colors
	{
		BLUE_DUAL, BLUE, BLUE_MINI, GREEN_MINI, RED_MINI
	}

	public SetupGauge(Creature character, int dat1, int time)
	{
		_charId = character.getObjectId();
		_dat1 = dat1;
		_time = time;
	}

	public SetupGauge(Creature character, Colors color, int time, int lostTime)
	{
		_charId = character.getObjectId();
		_dat1 = color.ordinal();
		_time = time;
		_time = lostTime;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x6b);
		writeD(_charId);
		writeD(_dat1);
		writeD(_time);
		writeD(_time);
	}
}
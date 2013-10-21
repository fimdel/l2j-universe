package lineage2.gameserver.network.serverpackets;

/**
 * @author ALF
 * @data 04.02.2012
 */
public class ExShowUsmVideo extends L2GameServerPacket
{

	public static final int Q001 = 0x01; // Какие то врата красные
	public static final int GD1_INTRO = 0x02; // Стартовое видео
	public static final int Q002 = 0x03; // Какие то врата синие
	public static final int Q003 = 0x04; // Какие то типа церберы
	public static final int Q004 = 0x05; // Ниче не показывает
	public static final int Q005 = 0x06; // Богиня разрушения предлагает славу
	                                     // тьмы
	public static final int Q006 = 0x07; // Богиня разрушения... отомстить...
	public static final int Q007 = 0x08; // Богиня разрушения... Принеси тьме
	                                     // велику. жертву
	public static final int Q009 = 0x09; // ниче нету
	public static final int Q010 = 0x0A; // Пробуждение, начало
	public static final int Q011 = 0x0B; //
	public static final int Q012 = 0x0C; //

	private int _id;

	public ExShowUsmVideo(int id)
	{
		_id = id;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x10E);
		writeD(_id);
	}

}

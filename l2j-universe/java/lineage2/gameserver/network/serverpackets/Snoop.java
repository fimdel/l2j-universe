package lineage2.gameserver.network.serverpackets;

public class Snoop extends L2GameServerPacket
{
	private int _convoID;
	private String _name;
	private int _type;
	private int _fStringId;
	private String _speaker;
	private String[] _params;

	public Snoop(int id, String name, int type, String speaker, String msg, int fStringId, String... params)
	{
		_convoID = id;
		_name = name;
		_type = type;
		_speaker = speaker;
		_fStringId = fStringId;
		_params = params;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xdb);

		writeD(_convoID);
		writeS(_name);
		writeD(0x00); // ??
		writeD(_type);
		writeS(_speaker);
		writeD(_fStringId);
		for (String param : _params)
			writeS(param);
	}
}
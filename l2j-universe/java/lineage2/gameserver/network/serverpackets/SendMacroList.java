package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.actor.instances.player.Macro;


public class SendMacroList extends L2GameServerPacket
{
	private final int _count;
	private final Macro _macro;
	private final int _type;
	private final boolean _first;
	private final int _macroId;

	public SendMacroList(Macro macro, int count, int type, int macroId, boolean first)
	{
		_count = count;
		_macro = macro;
		_type = type;
		_first = first;
		_macroId = macroId;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xe8);

		writeC(_type);
		writeD(_first ? 0x00 : _macroId);
		writeC(_count);
		writeC(_macro != null ? 1 : 0);

		if(_macro != null)
		{
			writeD(_macro.id); //Macro ID
			writeS(_macro.name); //Macro Name
			writeS(_macro.descr); //Desc
			writeS(_macro.acronym); //acronym
			writeC(_macro.icon); //icon

			writeC(_macro.commands.length); //count

			for(int i = 0; i < _macro.commands.length; i++)
			{
				Macro.L2MacroCmd cmd = _macro.commands[i];
				writeC(i + 1); //i of count
				writeC(cmd.type); //type  1 = skill, 3 = action, 4 = shortcut
				writeD(cmd.d1); // skill id
				writeC(cmd.d2); // shortcut id
				writeS(cmd.cmd); // command name
			}
		}
	}
}
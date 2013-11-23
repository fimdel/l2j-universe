/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.actor.instances.player.Macro;

/**
 * packet type id 0xe7
 * <p/>
 * sample
 * <p/>
 * e7 d // unknown change of Macro edit,add,delete c // unknown c //count of
 * Macros c // unknown
 * <p/>
 * d // id S // macro name S // desc S // acronym c // icon c // count
 * <p/>
 * c // entry c // type d // skill id c // shortcut id S // command name
 * <p/>
 * format: cdccdSSScc (ccdcS)
 */
public class SendMacroList extends L2GameServerPacket {
    private final int _macroId;
    private final int _count;
    private final Action _action;
    private final Macro _macro;

    public SendMacroList(int macroId, Action action, int count, Macro macro) {
        this._macroId = macroId;
        this._action = action;
        this._count = count;
        this._macro = macro;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xe8);

        writeC(_action.ordinal());
        writeD(_macroId);
        writeC(_count);

        if (_macro != null) {
            writeC(1);
            writeD(_macro._id);
            writeS(_macro._name);
            writeS(_macro._descr);
            writeS(_macro._acronym);
            writeC(_macro._icon);

            writeC(_macro._commands.length);

            for (int i = 0; i < _macro._commands.length; i++) {
                Macro.L2MacroCmd cmd = _macro._commands[i];
                writeC(i + 1);
                writeC(cmd.type);
                writeD(cmd.d1);
                writeC(cmd.d2);
                writeS(cmd.cmd);
            }
        } else {
            writeC(0);
        }
    }

    public static enum Action {
        DELETE,
        ADD,
        UPDATE
    }
}

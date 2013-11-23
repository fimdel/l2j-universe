/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.actor.instances.player;

/**
 * This class ...
 *
 * @version $Revision: 1.3 $ $Date: 2004/10/23 22:12:44 $
 */
public class Macro {
    public static final int CMD_TYPE_SKILL = 1;
    public static final int CMD_TYPE_ACTION = 3;
    public static final int CMD_TYPE_SHORTCUT = 4;
    public int _id;
    public final int _icon;
    public final String _name;
    public final String _descr;
    public final String _acronym;
    public final L2MacroCmd[] _commands;

    public Macro(int id, int icon, String name, String descr, String acronym, L2MacroCmd[] commands) {
        _id = id;
        _icon = icon;
        _name = name;
        _descr = descr;
        _acronym = (acronym.length() > 4 ? acronym.substring(0, 4) : acronym);
        _commands = commands;
    }

    public String toString() {
        return "macro id=" + _id + " icon=" + _icon + "name=" + _name + " descr=" + _descr + " acronym=" + _acronym + " commands=" + _commands;
    }

    public static class L2MacroCmd {
        public final int entry;
        public final int type;
        public final int d1;
        public final int d2;
        public final String cmd;

        public L2MacroCmd(int entry, int type, int d1, int d2, String cmd) {
            this.entry = entry;
            this.type = type;
            this.d1 = d1;
            this.d2 = d2;
            this.cmd = cmd;
        }
    }
}
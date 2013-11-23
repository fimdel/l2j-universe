/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.actor.instances.player;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.Macro.L2MacroCmd;
import l2p.gameserver.network.serverpackets.SendMacroList;
import l2p.gameserver.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class MacroList {
    private static final Logger _log = LoggerFactory.getLogger(MacroList.class);
    private final Player player;
    private final Map<Integer, Macro> _macroses = new HashMap<Integer, Macro>();
    private int _macroId;

    public MacroList(Player _player) {
        player = _player;
        _macroId = 1000;
    }

    private void registerMacroInDb(Macro macro) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("REPLACE INTO character_macroses (char_obj_id,id,icon,name,descr,acronym,commands) values(?,?,?,?,?,?,?)");
            statement.setInt(1, player.getObjectId());
            statement.setInt(2, macro._id);
            statement.setInt(3, macro._icon);
            statement.setString(4, macro._name);
            statement.setString(5, macro._descr);
            statement.setString(6, macro._acronym);
            StringBuilder sb = new StringBuilder();
            for (Macro.L2MacroCmd cmd : macro._commands) {
                sb.append(cmd.type).append(',');
                sb.append(cmd.d1).append(',');
                sb.append(cmd.d2);
                if ((cmd.cmd != null) && (cmd.cmd.length() > 0)) {
                    sb.append(',').append(cmd.cmd);
                }
                sb.append(';');
            }
            statement.setString(7, sb.toString());
            statement.execute();
        } catch (Exception e) {
            _log.error("could not store macro: " + macro.toString());
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    private void deleteMacroFromDb(Macro macro) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM character_macroses WHERE char_obj_id=? AND id=?");
            statement.setInt(1, player.getObjectId());
            statement.setInt(2, macro._id);
            statement.execute();
        } catch (Exception e) {
            _log.error("could not delete macro:", e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public void sendUpdate() {
        Macro[] all = getAllMacroses();
        for (Macro m : all) {
            player.sendPacket(new SendMacroList(m._id, SendMacroList.Action.ADD, all.length, m));
        }
    }

    public void restore() {
        _macroses.clear();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT char_obj_id, id, icon, name, descr, acronym, commands FROM character_macroses WHERE char_obj_id=?");
            statement.setInt(1, player.getObjectId());
            rset = statement.executeQuery();
            while (rset.next()) {
                int id = rset.getInt("id");
                int icon = rset.getInt("icon");
                String name = Strings.stripSlashes(rset.getString("name"));
                String descr = Strings.stripSlashes(rset.getString("descr"));
                String acronym = Strings.stripSlashes(rset.getString("acronym"));
                List commands = new ArrayList<L2MacroCmd>();
                StringTokenizer st1 = new StringTokenizer(rset.getString("commands"), ";");
                while (st1.hasMoreTokens()) {
                    StringTokenizer st = new StringTokenizer(st1.nextToken(), ",");
                    int type = Integer.parseInt(st.nextToken());
                    int d1 = Integer.parseInt(st.nextToken());
                    int d2 = Integer.parseInt(st.nextToken());
                    String cmd = "";
                    if (st.hasMoreTokens()) {
                        cmd = st.nextToken();
                    }
                    Macro.L2MacroCmd mcmd = new Macro.L2MacroCmd(commands.size(), type, d1, d2, cmd);
                    commands.add(mcmd);
                }

                _macroses.put(id, new Macro(id, icon, name, descr, acronym, (Macro.L2MacroCmd[]) commands.toArray(new Macro.L2MacroCmd[commands.size()])));
            }
        } catch (Exception e) {
            _log.error("could not restore shortcuts:", e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    public void deleteMacro(int id) {
        Macro toRemove = _macroses.get(id);

        if (toRemove != null) {
            deleteMacroFromDb(toRemove);
        }
        _macroses.remove(id);

        player.sendPacket(new SendMacroList(toRemove._id, SendMacroList.Action.DELETE, _macroses.size(), toRemove));
    }

    public void registerMacro(Macro macro) {
        if (macro._id == 0) {
            macro._id = (_macroId++);

            while (_macroses.get(macro._id) != null) {
                macro._id = (_macroId++);
            }
            _macroses.put(macro._id, macro);

            player.sendPacket(new SendMacroList(macro._id, SendMacroList.Action.ADD, _macroses.size(), macro));

            registerMacroInDb(macro);
        } else {
            Macro old = _macroses.put(macro._id, macro);

            if (old != null) {
                deleteMacroFromDb(old);
            }
            player.sendPacket(new SendMacroList(macro._id, SendMacroList.Action.UPDATE, _macroses.size(), macro));

            registerMacroInDb(macro);
        }
    }

    public Macro[] getAllMacroses() {
        return _macroses.values().toArray(new Macro[_macroses.size()]);
    }
}

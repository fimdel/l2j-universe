package l2p.gameserver.handler.voicecommands.impl;

import l2p.commons.dao.JdbcEntityState;
import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.dao.ItemsDAO;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.ItemInstance.ItemLocation;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

public class Repair extends Functions implements IVoicedCommandHandler {
    private static final Logger _log = LoggerFactory.getLogger(Repair.class);

    private final String[] _commandList = new String[]{"repair"};

    @Override
    public String[] getVoicedCommandList() {
        return _commandList;
    }

    @SuppressWarnings("resource")
    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target) {
        if (!target.isEmpty()) {
            if (activeChar.getName().equalsIgnoreCase(target)) {
                sendMessage(new CustomMessage("voicedcommandhandlers.Repair.YouCantRepairYourself", activeChar), activeChar);
                return false;
            }

            int objId = 0;

            for (Map.Entry<Integer, String> e : activeChar.getAccountChars().entrySet())
                if (e.getValue().equalsIgnoreCase(target)) {
                    objId = e.getKey();
                    break;
                }

            if (objId == 0) {
                sendMessage(new CustomMessage("voicedcommandhandlers.Repair.YouCanRepairOnlyOnSameAccount", activeChar), activeChar);
                return false;
            } else if (World.getPlayer(objId) != null) {
                sendMessage(new CustomMessage("voicedcommandhandlers.Repair.CharIsOnline", activeChar), activeChar);
                return false;
            }

            Connection con = null;
            PreparedStatement statement = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.getInstance().getConnection();
                statement = con.prepareStatement("SELECT karma FROM characters WHERE obj_Id=?");
                statement.setInt(1, objId);
                statement.execute();
                rs = statement.getResultSet();

                int karma = 0;

                rs.next();

                karma = rs.getInt("karma");

                DbUtils.close(statement, rs);

                if (karma > 0) {
                    statement = con.prepareStatement("UPDATE characters SET x=17144, y=170156, z=-3502 WHERE obj_Id=?");
                    statement.setInt(1, objId);
                    statement.execute();
                    DbUtils.close(statement);
                } else {
                    statement = con.prepareStatement("UPDATE characters SET x=0, y=0, z=0 WHERE obj_Id=?");
                    statement.setInt(1, objId);
                    statement.execute();
                    DbUtils.close(statement);

                    Collection<ItemInstance> items = ItemsDAO.getInstance().getItemsByOwnerIdAndLoc(objId, ItemLocation.PAPERDOLL);
                    for (ItemInstance item : items) {
                        item.setEquipped(false);
                        item.setLocData(0);
                        item.setLocation(ItemLocation.WAREHOUSE);
                        item.setJdbcState(JdbcEntityState.UPDATED);
                        item.update();
                    }
                }

                statement = con.prepareStatement("DELETE FROM character_variables WHERE obj_id=? AND type='user-var' AND name='reflection'");
                statement.setInt(1, objId);
                statement.execute();
                DbUtils.close(statement);

                sendMessage(new CustomMessage("voicedcommandhandlers.Repair.RepairDone", activeChar), activeChar);
                return true;
            } catch (Exception e) {
                _log.error("", e);
                return false;
            } finally {
                DbUtils.closeQuietly(con, statement, rs);
            }
        } else
            activeChar.sendMessage(".repair <name>");

        return false;
    }
}

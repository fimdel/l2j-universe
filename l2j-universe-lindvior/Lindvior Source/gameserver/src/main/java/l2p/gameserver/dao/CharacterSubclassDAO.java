/*
 * Copyright Java Team (c) 2012. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.DeathPenalty;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.base.SubClassType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.04.12
 * Time: 18:56
 */
public class CharacterSubclassDAO {
    private static final Logger _log = LoggerFactory.getLogger(CharacterSubclassDAO.class);
    private static CharacterSubclassDAO _instance = new CharacterSubclassDAO();

    public static final String SELECT_SQL_QUERY = "SELECT class_id, default_class_id, exp, sp, curHp, curCp, curMp, active, type, death_penalty, certification FROM character_subclasses WHERE char_obj_id=?";
    public static final String INSERT_SQL_QUERY = "INSERT INTO character_subclasses (char_obj_id, class_id, default_class_id, exp, sp, curHp, curMp, curCp, maxHp, maxMp, maxCp, level, active, type, death_penalty, certification) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static CharacterSubclassDAO getInstance() {
        return _instance;
    }

    public boolean insert(int objId, int classId, int dafaultClassId, long exp, int sp, double curHp, double curMp, double curCp, double maxHp, double maxMp, double maxCp, int level, boolean active, SubClassType type, DeathPenalty deathPenalty, int certification) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(INSERT_SQL_QUERY);
            statement.setInt(1, objId);
            statement.setInt(2, classId);
            statement.setInt(3, dafaultClassId);
            statement.setLong(4, exp);
            statement.setInt(5, sp);
            statement.setDouble(6, curHp);
            statement.setDouble(7, curMp);
            statement.setDouble(8, curCp);
            statement.setDouble(9, maxHp);
            statement.setDouble(10, maxMp);
            statement.setDouble(11, maxCp);
            statement.setInt(12, level);
            statement.setInt(13, (active ? 1 : 0));
            statement.setInt(14, type.ordinal());
            statement.setInt(15, deathPenalty == null ? 0 : deathPenalty.getLevelOnSaveDB());
            statement.setInt(16, certification);
            statement.executeUpdate();
        } catch (final Exception e) {
            _log.error("CharacterSubclassDAO:insert(player)", e);
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
        return true;
    }

    public List<SubClass> restore(Player player) {
        List<SubClass> result = new ArrayList<SubClass>();

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_SQL_QUERY);
            statement.setInt(1, player.getObjectId());
            rset = statement.executeQuery();
            while (rset.next()) {
                SubClass subClass = new SubClass();
                subClass.setClassId(rset.getInt("class_id"));
                subClass.setDefaultClassId(rset.getInt("default_class_id"));
                subClass.setExp(rset.getLong("exp"));
                subClass.setSp(rset.getInt("sp"));
                subClass.setHp(rset.getDouble("curHp"));
                subClass.setMp(rset.getDouble("curMp"));
                subClass.setCp(rset.getDouble("curCp"));
                subClass.setActive(rset.getInt("active") == 1);
                subClass.setType(SubClassType.VALUES[rset.getInt("type")]);
                subClass.setDeathPenalty(new DeathPenalty(player, rset.getInt("death_penalty")));
                subClass.setCertification(rset.getInt("certification"));
                result.add(subClass);
            }
        } catch (Exception e) {
            _log.error("CharacterSubclassDAO:restore(player)", e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
        return result;
    }

    public boolean store(Player player) {
        Connection con = null;
        Statement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.createStatement();

            StringBuilder sb;
            for (SubClass subClass : player.getSubClassList().values()) {
                sb = new StringBuilder("UPDATE character_subclasses SET ");
                sb.append("exp=").append(subClass.getExp()).append(",");
                sb.append("sp=").append(subClass.getSp()).append(",");
                sb.append("curHp=").append(subClass.getHp()).append(",");
                sb.append("curMp=").append(subClass.getMp()).append(",");
                sb.append("curCp=").append(subClass.getCp()).append(",");
                sb.append("level=").append(subClass.getLevel()).append(",");
                sb.append("active=").append(subClass.isActive() ? 1 : 0).append(",");
                sb.append("type=").append(subClass.getType().ordinal()).append(",");
                sb.append("death_penalty=").append(subClass.getDeathPenalty(player).getLevelOnSaveDB()).append(",");
                sb.append("certification='").append(subClass.getCertification()).append("'");
                sb.append(" WHERE char_obj_id=").append(player.getObjectId()).append(" AND class_id=").append(subClass.getClassId()).append(" LIMIT 1");
                statement.executeUpdate(sb.toString());
            }

            sb = new StringBuilder("UPDATE character_subclasses SET ");
            sb.append("maxHp=").append(player.getMaxHp()).append(",");
            sb.append("maxMp=").append(player.getMaxMp()).append(",");
            sb.append("maxCp=").append(player.getMaxCp());
            sb.append(" WHERE char_obj_id=").append(player.getObjectId()).append(" AND active=1 LIMIT 1");
            statement.executeUpdate(sb.toString());
        } catch (final Exception e) {
            _log.error("CharacterSubclassDAO:store(player)", e);
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
        return true;
    }
}

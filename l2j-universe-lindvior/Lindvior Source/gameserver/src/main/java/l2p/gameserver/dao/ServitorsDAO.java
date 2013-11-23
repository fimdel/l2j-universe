package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Summon;
import l2p.gameserver.model.instances.SummonInstance;
import l2p.gameserver.skills.skillclasses.SummonServitor;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.npc.NpcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServitorsDAO {
    private static final Logger _log = LoggerFactory.getLogger(MentoringDAO.class);
    private static final String INSERT_SQL_QUERY = "INSERT INTO servitors(objId, ownerId, curHp, curMp, skill_id, skill_lvl) VALUES (?,?,?,?,?,?)";
    private static final String SELECT_SQL_QUERY = "SELECT objId, curHp, curMp, skill_id, skill_lvl FROM servitors WHERE ownerId=?";
    private static final String DELETE_SQL_QUERY = "DELETE FROM servitors WHERE ownerId=?";

    private static final ServitorsDAO ourInstance = new ServitorsDAO();

    public static ServitorsDAO getInstance() {
        return ourInstance;
    }

    private ServitorsDAO() {
    }

    public void store(Summon summon) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(INSERT_SQL_QUERY);
            statement.setInt(1, summon.getObjectId());
            statement.setInt(2, summon.getPlayer().getObjectId());
            statement.setInt(3, (int) summon.getCurrentHp());
            statement.setInt(4, (int) summon.getCurrentMp());
            statement.setInt(5, summon.getSummonSkillId());
            statement.setInt(6, summon.getSummonSkillLvl());
            statement.execute();
        } catch (Exception e) {
            _log.warn("ServitorsDAO:store(Summon)", e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public List<Summon> restore(Player owner) {
        List<Summon> summons = new ArrayList<Summon>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_SQL_QUERY);
            statement.setInt(1, owner.getObjectId());
            rset = statement.executeQuery();
            while (rset.next()) {
                int objId = rset.getInt("objId");
                int curHp = rset.getInt("curHp");
                int curMp = rset.getInt("curMp");
                int skill_id = rset.getInt("skill_id");
                int skill_lvl = rset.getInt("skill_lvl");

                Skill skill = SkillTable.getInstance().getInfo(skill_id, skill_lvl);
                if (skill == null)
                    continue;
                if (skill.getSkillType() != Skill.SkillType.SUMMON)
                    continue;

                NpcTemplate template = NpcHolder.getInstance().getTemplate(skill.getNpcId());
                SummonInstance s = new SummonInstance(objId, template, owner, ((SummonServitor) skill).getLifeTime(), ((SummonServitor) skill).getSummonPoint(), skill);

                s.setCurrentHp(curHp, true);
                s.setCurrentMp(curMp, true);

                summons.add(s);
            }
            DbUtils.closeQuietly(statement, rset);

            statement = con.prepareStatement(DELETE_SQL_QUERY);
            statement.setInt(1, owner.getObjectId());
            statement.execute();
            DbUtils.close(statement);
        } catch (Exception e) {
            _log.error("ServitorsDAO:restore(Player):" + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
        return summons;
    }
}

package lineage2.gameserver.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.model.worldstatistics.CharacterStatistic;
import lineage2.gameserver.model.worldstatistics.CharacterStatisticElement;
import lineage2.gameserver.templates.StatuesSpawnTemplate;

public class WorldStatisticDAO
{
	public static final int UPDATE_STATISTIC_MODE_ADD = 0;
	public static final int UPDATE_STATISTIC_MODE_REPLACE = 1;
	public static final int UPDATE_STATISTIC_MODE_INSERT_MAX = 2;
	private static final int STATUE_SPAWN_SELECT_LIMIT = 1;
	private static final int STATUES_TOP_PLAYER_LIMIT = 5;
	private static final Logger _log = LoggerFactory.getLogger(WorldStatisticDAO.class);
	// Select value
	private static final String SELECT_GLOBAL_RESULT = "SELECT `value` FROM `world_statistic` WHERE `objId`=? AND `categoryId`=? AND `subCategoryId`=?";
	private static final String SELECT_MONTHLY_RESULT = "SELECT `value` FROM `world_statistic_monthly` WHERE `objId`=? AND `categoryId`=? AND `subCategoryId`=?";
	// Update values
	private static final String UPDATE_MONTHLY_STATISTIC = "REPLACE INTO `world_statistic_monthly` (objId, categoryId, subCategoryId, value, classId, race, sex, hairstyle, haircolor, face, necklace, head, rhand, lhand, gloves, chest, pants, boots, cloak, hair1, hair2, date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_GLOBAL_STATISTIC = "REPLACE INTO `world_statistic` (objId, categoryId, subCategoryId, value, date) VALUES(?, ?, ?, ?, ?)";
	// Reset winners statistic
	private static final String RESET_GLOBAL_WINNERS_STATISTIC = "TRUNCATE TABLE `world_statistic_winners`";
	private static final String RESET_MONTHLY_WINNERS_STATISTIC = "TRUNCATE TABLE `world_statistic_winners_monthly`";
	// Update monthly winners
	private static final String UPDATE_MONTHLY_WINNERS = "INSERT INTO world_statistic_winners_monthly (objId, categoryId, subCategoryId, value, classId, race, sex, hairstyle, haircolor, face, necklace, head, rhand, lhand, gloves, chest, pants, boots, cloak, hair1, hair2, date) (SELECT * FROM `world_statistic_monthly` WHERE `categoryId`=? AND `subCategoryId`=?  ORDER BY `value` DESC, date DESC LIMIT ?)";
	private static final String UPDATE_GLOBAL_WINNERS = "INSERT INTO `world_statistic_winners` (objId, categoryId, subCategoryId, value, date) (SELECT * FROM `world_statistic` WHERE `categoryId`=? AND `subCategoryId`=? ORDER BY `value` DESC, date DESC LIMIT ?)";
	// Select personal statistic
	private static final String SELECT_PERSONAL_STATISTIC = "SELECT ws.categoryId, ws.subCategoryId, ws.value, COALESCE(wsm.value, 0) FROM world_statistic ws LEFT JOIN world_statistic_monthly wsm ON ws.categoryId = wsm.categoryId AND ws.subCategoryId = wsm.subCategoryId WHERE ws.objId = ?";
	// Select top statistic
	private static final String SELECT_TOP_GLOBAL_STATISTIC = "SELECT c.obj_Id, c.char_name, ws.value FROM world_statistic ws INNER JOIN characters c ON ws.objId=c.obj_Id WHERE ws.categoryId=? AND ws.subCategoryId=? ORDER BY ws.value DESC, date DESC LIMIT ?";
	private static final String SELECT_TOP_MONTHLY_STATISTIC = "SELECT c.obj_Id, c.char_name, ws.value FROM world_statistic_monthly ws INNER JOIN characters c ON ws.objId=c.obj_Id WHERE ws.categoryId=? AND ws.subCategoryId=? ORDER BY ws.value DESC, date DESC LIMIT ?";
	// Select winner info
	private static final String SELECT_WINNER_INFOS = "SELECT ws.objId, c.char_name, ws.value, ws.classId, ws.race, ws.sex, ws.hairstyle, ws.haircolor, ws.face, ws.necklace, ws.head, ws.rhand, ws.lhand, ws.gloves, ws.chest, ws.pants, ws.boots, ws.cloak, ws.hair1, ws.hair2 FROM world_statistic_winners_monthly ws LEFT JOIN characters c ON c.obj_Id = ws.objId WHERE ws.categoryId=? ORDER BY ws.value DESC, ws.date DESC LIMIT ?";
	// Select winners
	private static final String SELECT_GLOBAL_WINNERS = "SELECT ws.objId, c.char_name, ws.value FROM world_statistic_winners ws LEFT JOIN characters c ON c.obj_Id = ws.objId WHERE ws.categoryId=? ORDER BY ws.value DESC, ws.date DESC LIMIT ?";
	private static final String SELECT_MONTHLY_WINNERS = "SELECT ws.objId, c.char_name, ws.value FROM world_statistic_winners_monthly ws LEFT JOIN characters c ON c.obj_Id = ws.objId WHERE ws.categoryId=? ORDER BY ws.value DESC, ws.date DESC LIMIT ?";
	private static WorldStatisticDAO ourInstance = new WorldStatisticDAO();
	private Lock writeLock = new ReentrantReadWriteLock().writeLock();

	private WorldStatisticDAO()
	{
	}

	public static WorldStatisticDAO getInstance()
	{
		return ourInstance;
	}

	public void updateStatisticFor(Player player, CategoryType categoryType, long value)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		writeLock.lock();
		try {
			con = DatabaseFactory.getInstance().getConnection();
			long globalValue = value;
			if (categoryType.getSaveMode() == UPDATE_STATISTIC_MODE_ADD) {
				statement = con.prepareStatement(SELECT_GLOBAL_RESULT);
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, categoryType.getClientId());
				statement.setInt(3, categoryType.getSubcat());
				rset = statement.executeQuery();
				if (rset.next()) {
					globalValue += rset.getLong(1);
				}
				DbUtils.closeQuietly(statement, rset);
			} else if (categoryType.getSaveMode() == UPDATE_STATISTIC_MODE_INSERT_MAX) {
				statement = con.prepareStatement(SELECT_GLOBAL_RESULT);
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, categoryType.getClientId());
				statement.setInt(3, categoryType.getSubcat());
				rset = statement.executeQuery();
				if (rset.next()) {
					long currentValue = rset.getLong(1);
					if (currentValue >= value) {
						return;
					}
				}
				DbUtils.closeQuietly(statement, rset);
			}
			updateMonthlyStatistic(player, categoryType, value);

			// update global statistic
			statement = con.prepareStatement(UPDATE_GLOBAL_STATISTIC);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, categoryType.getClientId());
			statement.setInt(3, categoryType.getSubcat());
			statement.setLong(4, globalValue);
			statement.setLong(5, System.currentTimeMillis());
			statement.execute();

		} catch (Exception e) {
			_log.info("WorldStatisticDAO.updateStatisticFor(Player, CategoryType, long): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
			writeLock.unlock();
		}
	}

	private void updateMonthlyStatistic(Player player, CategoryType categoryType, long value) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			if (categoryType.getSaveMode() == UPDATE_STATISTIC_MODE_ADD) {
				statement = con.prepareStatement(SELECT_MONTHLY_RESULT);
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, categoryType.getClientId());
				statement.setInt(3, categoryType.getSubcat());
				rset = statement.executeQuery();
				if (rset.next()) {
					value += rset.getLong(1);
				}
				DbUtils.closeQuietly(statement, rset);
			} else if (categoryType.getSaveMode() == UPDATE_STATISTIC_MODE_INSERT_MAX) {
				statement = con.prepareStatement(SELECT_MONTHLY_RESULT);
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, categoryType.getClientId());
				statement.setInt(3, categoryType.getSubcat());
				rset = statement.executeQuery();
				if (rset.next()) {
					long currentValue = rset.getLong(1);
					if (currentValue >= value) {
						return;
					}
				}
				DbUtils.closeQuietly(statement, rset);
			}
			// update monthly statistic
			statement = con.prepareStatement(UPDATE_MONTHLY_STATISTIC);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, categoryType.getClientId());
			statement.setInt(3, categoryType.getSubcat());
			statement.setLong(4, value);
			statement.setInt(5, player.getClassId().getId());
			statement.setInt(6, player.getRace().ordinal());
			statement.setInt(7, player.getSex());
			statement.setInt(8, player.getHairStyle());
			statement.setInt(9, player.getHairColor());
			statement.setInt(10, player.getFace());
			statement.setInt(11, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
			statement.setInt(12, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			statement.setInt(13, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			statement.setInt(14, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			statement.setInt(15, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			statement.setInt(16, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			statement.setInt(17, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			statement.setInt(18, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			statement.setInt(19, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			statement.setInt(20, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			statement.setInt(21, player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
			statement.setLong(22, System.currentTimeMillis());
			statement.execute();

		} catch (Exception e) {
			_log.info("WorldStatisticDAO.updateMonthlyStatistic(Player, CategoryType, long): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	private void resetWinnersStatistic() {
		Connection con = null;
		PreparedStatement statement = null;
		writeLock.lock();
		try {
			con = DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement(RESET_MONTHLY_WINNERS_STATISTIC);
			statement.execute();
			DbUtils.closeQuietly(statement);
			statement = con.prepareStatement(RESET_GLOBAL_WINNERS_STATISTIC);
			statement.execute();

		} catch (Exception e) {
			_log.info("WorldStatisticDAO.resetWinnersStatistic(): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement);
			writeLock.unlock();
		}
	}

	public void recalculateWinners() {
		resetWinnersStatistic();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try {
			writeLock.lock();
			con = DatabaseFactory.getInstance().getConnection();
			for (CategoryType type : CategoryType.values()) {
				statement = con.prepareStatement(UPDATE_MONTHLY_WINNERS);
				statement.setInt(1, type.getClientId());
				statement.setInt(2, type.getSubcat());
				statement.setInt(3, STATUES_TOP_PLAYER_LIMIT);
				statement.executeUpdate();
				DbUtils.closeQuietly(statement, rset);

				statement = con.prepareStatement(UPDATE_GLOBAL_WINNERS);
				statement.setInt(1, type.getClientId());
				statement.setInt(2, type.getSubcat());
				statement.setInt(3, STATUES_TOP_PLAYER_LIMIT);
				statement.executeUpdate();
			}
		} catch (Exception e) {
			_log.info("WorldStatisticDAO.resetWinnersStatistic(): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
			writeLock.unlock();
		}
	}

	public List<CharacterStatisticElement> getPersonalStatisticFor(int charId) {
		List<CharacterStatisticElement> statistics = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PERSONAL_STATISTIC);
			statement.setInt(1, charId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				CategoryType categoryType = CategoryType.getCategoryById(rset.getInt(1), rset.getInt(2));
				statistics.add(new CharacterStatisticElement(categoryType, rset.getLong(3), rset.getLong(4)));
			}
		}
		catch (Exception e)
		{
			_log.info("WorldStatisticDAO.getPersonalStatisticFor(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return statistics;
	}

	public List<CharacterStatistic> getStatisticForCategory(CategoryType cat, boolean global, int limit)
	{
		List<CharacterStatistic> statistics = new ArrayList<>(limit);
		String query = global ? SELECT_TOP_GLOBAL_STATISTIC : SELECT_TOP_MONTHLY_STATISTIC;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(query);
			statement.setInt(1, cat.getClientId());
			statement.setInt(2, cat.getSubcat());
			statement.setInt(3, limit);
			rset = statement.executeQuery();

			while (rset.next())
			{
				CharacterStatisticElement statisticElement = new CharacterStatisticElement(cat, rset.getLong(3));
				statistics.add(new CharacterStatistic(rset.getInt(1), rset.getString(2), statisticElement));
			}
		} catch (Exception e) {
			_log.info("WorldStatisticDAO.getStatisticForCategory(CategoryType, boolean, int): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
		}

		return statistics;
	}

	public List<StatuesSpawnTemplate> getStatueTemplates(Collection<CategoryType> categoryTypes) {
		List<StatuesSpawnTemplate> templates = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			for (CategoryType categoryType : categoryTypes) {
				statement = con.prepareStatement(SELECT_WINNER_INFOS);
				statement.setInt(1, categoryType.getClientId());
				statement.setInt(2, STATUE_SPAWN_SELECT_LIMIT);
				rset = statement.executeQuery();
				if (rset.next()) {
					StatuesSpawnTemplate template = new StatuesSpawnTemplate(categoryType);
					template.setName(rset.getString("char_name"));
					template.setClassId(rset.getInt("classId"));
					template.setRaceId(rset.getInt("race"));
					template.setSex(rset.getInt("sex"));
					template.setHairStyle(rset.getInt("hairstyle"));
					template.setHairColor(rset.getInt("haircolor"));
					template.setFace(rset.getInt("face"));
					template.setNecklace(rset.getInt("necklace"));
					template.setHead(rset.getInt("head"));
					template.setRHand(rset.getInt("rhand"));
					template.setLHand(rset.getInt("lhand"));
					template.setGloves(rset.getInt("gloves"));
					template.setChest(rset.getInt("chest"));
					template.setPants(rset.getInt("pants"));
					template.setBoots(rset.getInt("boots"));
					template.setCloak(rset.getInt("cloak"));
					template.setHair1(rset.getInt("hair1"));
					template.setHair2(rset.getInt("hair2"));
					templates.add(template);
				}
			}

		} catch (Exception e) {
			_log.info("WorldStatisticDAO.getStatueTemplates(Collection<CategoryType>): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
		}

		return templates;
	}

	public List<CharacterStatistic> getWinners(CategoryType cat, boolean global, int limit) {
		List<CharacterStatistic> statistics = new ArrayList<>(limit);
		String query = global ? SELECT_GLOBAL_WINNERS : SELECT_MONTHLY_WINNERS;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(query);
			statement.setInt(1, cat.getClientId());
			statement.setInt(2, limit);
			rset = statement.executeQuery();

			while (rset.next()) {
				CharacterStatisticElement statisticElement = new CharacterStatisticElement(cat, rset.getLong(3));
				statistics.add(new CharacterStatistic(rset.getInt(1), rset.getString(2), statisticElement));
			}
		} catch (Exception e) {
			_log.info("WorldStatisticDAO.getWinners(CategoryType, boolean, int): " + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
		}

		return statistics;
	}
}

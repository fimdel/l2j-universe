/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.HtmlUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Hero
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Hero.class);
	/**
	 * Field _instance.
	 */
	private static Hero _instance;
	/**
	 * Field GET_HEROES. (value is ""SELECT * FROM heroes WHERE played = 1"")
	 */
	private static final String GET_HEROES = "SELECT * FROM heroes WHERE played = 1";
	/**
	 * Field GET_ALL_HEROES. (value is ""SELECT * FROM heroes"")
	 */
	private static final String GET_ALL_HEROES = "SELECT * FROM heroes";
	/**
	 * Field _heroes.
	 */
	private static Map<Integer, StatsSet> _heroes;
	/**
	 * Field _completeHeroes.
	 */
	private static Map<Integer, StatsSet> _completeHeroes;
	/**
	 * Field _herodiary.
	 */
	private static Map<Integer, List<HeroDiary>> _herodiary;
	/**
	 * Field _heroMessage.
	 */
	private static Map<Integer, String> _heroMessage;
	/**
	 * Field COUNT. (value is ""count"")
	 */
	public static final String COUNT = "count";
	/**
	 * Field PLAYED. (value is ""played"")
	 */
	public static final String PLAYED = "played";
	/**
	 * Field CLAN_NAME. (value is ""clan_name"")
	 */
	public static final String CLAN_NAME = "clan_name";
	/**
	 * Field CLAN_CREST. (value is ""clan_crest"")
	 */
	public static final String CLAN_CREST = "clan_crest";
	/**
	 * Field ALLY_NAME. (value is ""ally_name"")
	 */
	public static final String ALLY_NAME = "ally_name";
	/**
	 * Field ALLY_CREST. (value is ""ally_crest"")
	 */
	public static final String ALLY_CREST = "ally_crest";
	/**
	 * Field ACTIVE. (value is ""active"")
	 */
	public static final String ACTIVE = "active";
	/**
	 * Field MESSAGE. (value is ""message"")
	 */
	public static final String MESSAGE = "message";
	
	/**
	 * Method getInstance.
	 * @return Hero
	 */
	public static Hero getInstance()
	{
		if (_instance == null)
		{
			_instance = new Hero();
		}
		return _instance;
	}
	
	/**
	 * Constructor for Hero.
	 */
	public Hero()
	{
		init();
	}
	
	/**
	 * Method HeroSetClanAndAlly.
	 * @param charId int
	 * @param hero StatsSet
	 */
	private static void HeroSetClanAndAlly(int charId, StatsSet hero)
	{
		Entry<Clan, Alliance> e = ClanTable.getInstance().getClanAndAllianceByCharId(charId);
		hero.set(CLAN_CREST, e.getKey() == null ? 0 : e.getKey().getCrestId());
		hero.set(CLAN_NAME, e.getKey() == null ? "" : e.getKey().getName());
		hero.set(ALLY_CREST, e.getValue() == null ? 0 : e.getValue().getAllyCrestId());
		hero.set(ALLY_NAME, e.getValue() == null ? "" : e.getValue().getAllyName());
		e = null;
	}
	
	/**
	 * Method init.
	 */
	private void init()
	{
		_heroes = new ConcurrentHashMap<>();
		_completeHeroes = new ConcurrentHashMap<>();
		_herodiary = new ConcurrentHashMap<>();
		_heroMessage = new ConcurrentHashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(GET_HEROES);
			rset = statement.executeQuery();
			while (rset.next())
			{
				StatsSet hero = new StatsSet();
				int charId = rset.getInt(Olympiad.CHAR_ID);
				hero.set(Olympiad.CHAR_NAME, Olympiad.getNobleName(charId));
				hero.set(Olympiad.CLASS_ID, Olympiad.getNobleClass(charId));
				hero.set(COUNT, rset.getInt(COUNT));
				hero.set(PLAYED, rset.getInt(PLAYED));
				hero.set(ACTIVE, rset.getInt(ACTIVE));
				HeroSetClanAndAlly(charId, hero);
				loadDiary(charId);
				loadMessage(charId);
				_heroes.put(charId, hero);
			}
			DbUtils.close(statement, rset);
			statement = con.prepareStatement(GET_ALL_HEROES);
			rset = statement.executeQuery();
			while (rset.next())
			{
				StatsSet hero = new StatsSet();
				int charId = rset.getInt(Olympiad.CHAR_ID);
				hero.set(Olympiad.CHAR_NAME, Olympiad.getNobleName(charId));
				hero.set(Olympiad.CLASS_ID, Olympiad.getNobleClass(charId));
				hero.set(COUNT, rset.getInt(COUNT));
				hero.set(PLAYED, rset.getInt(PLAYED));
				hero.set(ACTIVE, rset.getInt(ACTIVE));
				HeroSetClanAndAlly(charId, hero);
				_completeHeroes.put(charId, hero);
			}
		}
		catch (SQLException e)
		{
			_log.warn("Hero System: Couldnt load Heroes", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		_log.info("Hero System: Loaded " + _heroes.size() + " Heroes.");
		_log.info("Hero System: Loaded " + _completeHeroes.size() + " all time Heroes.");
	}
	
	/**
	 * Method getHeroes.
	 * @return Map<Integer,StatsSet>
	 */
	public Map<Integer, StatsSet> getHeroes()
	{
		return _heroes;
	}
	
	/**
	 * Method clearHeroes.
	 */
	public synchronized void clearHeroes()
	{
		mysql.set("UPDATE heroes SET played = 0, active = 0");
		if (!_heroes.isEmpty())
		{
			for (StatsSet hero : _heroes.values())
			{
				if (hero.getInteger(ACTIVE) == 0)
				{
					continue;
				}
				String name = hero.getString(Olympiad.CHAR_NAME);
				Player player = World.getPlayer(name);
				if (player != null)
				{
					PcInventory inventory = player.getInventory();
					inventory.writeLock();
					try
					{
						for (ItemInstance item : player.getInventory().getItems())
						{
							if (item.isHeroWeapon())
							{
								player.getInventory().destroyItem(item);
							}
						}
					}
					finally
					{
						inventory.writeUnlock();
					}
					player.setHero(false);
					player.updatePledgeClass();
					player.broadcastUserInfo();
				}
			}
		}
		_heroes.clear();
		_herodiary.clear();
	}
	
	/**
	 * Method computeNewHeroes.
	 * @param newHeroes List<StatsSet>
	 * @return boolean
	 */
	public synchronized boolean computeNewHeroes(List<StatsSet> newHeroes)
	{
		if (newHeroes.size() == 0)
		{
			return true;
		}
		Map<Integer, StatsSet> heroes = new ConcurrentHashMap<>();
		boolean error = false;
		for (StatsSet hero : newHeroes)
		{
			int charId = hero.getInteger(Olympiad.CHAR_ID);
			if ((_completeHeroes != null) && _completeHeroes.containsKey(charId))
			{
				StatsSet oldHero = _completeHeroes.get(charId);
				int count = oldHero.getInteger(COUNT);
				oldHero.set(COUNT, count + 1);
				oldHero.set(PLAYED, 1);
				oldHero.set(ACTIVE, 0);
				heroes.put(charId, oldHero);
			}
			else
			{
				StatsSet newHero = new StatsSet();
				newHero.set(Olympiad.CHAR_NAME, hero.getString(Olympiad.CHAR_NAME));
				newHero.set(Olympiad.CLASS_ID, hero.getInteger(Olympiad.CLASS_ID));
				newHero.set(COUNT, 1);
				newHero.set(PLAYED, 1);
				newHero.set(ACTIVE, 0);
				heroes.put(charId, newHero);
			}
			addHeroDiary(charId, HeroDiary.ACTION_HERO_GAINED, 0);
			loadDiary(charId);
		}
		_heroes.putAll(heroes);
		heroes.clear();
		updateHeroes(0);
		return error;
	}
	
	/**
	 * Method updateHeroes.
	 * @param id int
	 */
	public void updateHeroes(int id)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO heroes (char_id, count, played, active) VALUES (?,?,?,?)");
			for (Integer heroId : _heroes.keySet())
			{
				if ((id > 0) && (heroId != id))
				{
					continue;
				}
				StatsSet hero = _heroes.get(heroId);
				statement.setInt(1, heroId);
				statement.setInt(2, hero.getInteger(COUNT));
				statement.setInt(3, hero.getInteger(PLAYED));
				statement.setInt(4, hero.getInteger(ACTIVE));
				statement.execute();
				if ((_completeHeroes != null) && !_completeHeroes.containsKey(heroId))
				{
					HeroSetClanAndAlly(heroId, hero);
					_completeHeroes.put(heroId, hero);
				}
			}
		}
		catch (SQLException e)
		{
			_log.warn("Hero System: Couldnt update Heroes");
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method isHero.
	 * @param id int
	 * @return boolean
	 */
	public boolean isHero(int id)
	{
		if ((_heroes == null) || _heroes.isEmpty())
		{
			return false;
		}
		if (_heroes.containsKey(id) && (_heroes.get(id).getInteger(ACTIVE) == 1))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method isInactiveHero.
	 * @param id int
	 * @return boolean
	 */
	public boolean isInactiveHero(int id)
	{
		if ((_heroes == null) || _heroes.isEmpty())
		{
			return false;
		}
		if (_heroes.containsKey(id) && (_heroes.get(id).getInteger(ACTIVE) == 0))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method activateHero.
	 * @param player Player
	 */
	public void activateHero(Player player)
	{
		StatsSet hero = _heroes.get(player.getObjectId());
		hero.set(ACTIVE, 1);
		_heroes.remove(player.getObjectId());
		_heroes.put(player.getObjectId(), hero);
		if (player.getBaseClassId() == player.getActiveClassId())
		{
			addSkills(player);
		}
		player.setHero(true);
		player.updatePledgeClass();
		player.broadcastPacket(new SocialAction(player.getObjectId(), SocialAction.GIVE_HERO));
		if ((player.getClan() != null) && (player.getClan().getLevel() >= 5))
		{
			player.getClan().incReputation(5000, true, "Hero:activateHero:" + player);
			player.getClan().broadcastToOtherOnlineMembers(new SystemMessage(SystemMessage.CLAN_MEMBER_S1_WAS_NAMED_A_HERO_2S_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE).addString(player.getName()).addNumber(Math.round(5000 * Config.RATE_CLAN_REP_SCORE)), player);
		}
		player.broadcastUserInfo();
		updateHeroes(player.getObjectId());
	}
	
	/**
	 * Method addSkills.
	 * @param player Player
	 */
	public static void addSkills(Player player)
	{
		player.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_HEROIC_MIRACLE, 1));
		player.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_HEROIC_BERSERKER, 1));
		player.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_HEROIC_VALOR, 1));
		player.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_HEROIC_GRANDEUR, 1));
		player.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_HEROIC_DREAD, 1));
	}
	
	/**
	 * Method removeSkills.
	 * @param player Player
	 */
	public static void removeSkills(Player player)
	{
		player.removeSkillById(Skill.SKILL_HEROIC_MIRACLE);
		player.removeSkillById(Skill.SKILL_HEROIC_BERSERKER);
		player.removeSkillById(Skill.SKILL_HEROIC_VALOR);
		player.removeSkillById(Skill.SKILL_HEROIC_GRANDEUR);
		player.removeSkillById(Skill.SKILL_HEROIC_DREAD);
	}
	
	/**
	 * Method loadDiary.
	 * @param charId int
	 */
	public void loadDiary(int charId)
	{
		List<HeroDiary> diary = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM  heroes_diary WHERE charId=? ORDER BY time ASC");
			statement.setInt(1, charId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				long time = rset.getLong("time");
				int action = rset.getInt("action");
				int param = rset.getInt("param");
				HeroDiary d = new HeroDiary(action, time, param);
				diary.add(d);
			}
			_herodiary.put(charId, diary);
			if (Config.DEBUG)
			{
				_log.info("Hero System: Loaded " + diary.size() + " diary entries for Hero(object id: #" + charId + ")");
			}
		}
		catch (SQLException e)
		{
			_log.warn("Hero System: Couldnt load Hero Diary for CharId: " + charId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method showHeroDiary.
	 * @param activeChar Player
	 * @param heroclass int
	 * @param charid int
	 * @param page int
	 */
	public void showHeroDiary(Player activeChar, int heroclass, int charid, int page)
	{
		final int perpage = 10;
		List<HeroDiary> mainlist = _herodiary.get(charid);
		if (mainlist != null)
		{
			NpcHtmlMessage html = new NpcHtmlMessage(activeChar, null);
			html.setFile("olympiad/monument_hero_info.htm");
			html.replace("%title%", StringHolder.getInstance().getNotNull(activeChar, "hero.diary"));
			html.replace("%heroname%", Olympiad.getNobleName(charid));
			html.replace("%message%", _heroMessage.get(charid));
			List<HeroDiary> list = new ArrayList<>(mainlist);
			Collections.reverse(list);
			boolean color = true;
			final StringBuilder fList = new StringBuilder(500);
			int counter = 0;
			int breakat = 0;
			for (int i = (page - 1) * perpage; i < list.size(); i++)
			{
				breakat = i;
				HeroDiary diary = list.get(i);
				Map.Entry<String, String> entry = diary.toString(activeChar);
				fList.append("<tr><td>");
				if (color)
				{
					fList.append("<table width=270 bgcolor=\"131210\">");
				}
				else
				{
					fList.append("<table width=270>");
				}
				fList.append("<tr><td width=270><font color=\"LEVEL\">" + entry.getKey() + "</font></td></tr>");
				fList.append("<tr><td width=270>" + entry.getValue() + "</td></tr>");
				fList.append("<tr><td>&nbsp;</td></tr></table>");
				fList.append("</td></tr>");
				color = !color;
				counter++;
				if (counter >= perpage)
				{
					break;
				}
			}
			if (breakat < (list.size() - 1))
			{
				html.replace("%buttprev%", HtmlUtils.PREV_BUTTON);
				html.replace("%prev_bypass%", "_diary?class=" + heroclass + "&page=" + (page + 1));
			}
			else
			{
				html.replace("%buttprev%", StringUtils.EMPTY);
			}
			if (page > 1)
			{
				html.replace("%buttnext%", HtmlUtils.NEXT_BUTTON);
				html.replace("%next_bypass%", "_diary?class=" + heroclass + "&page=" + (page - 1));
			}
			else
			{
				html.replace("%buttnext%", StringUtils.EMPTY);
			}
			html.replace("%list%", fList.toString());
			activeChar.sendPacket(html);
		}
	}
	
	/**
	 * Method addHeroDiary.
	 * @param playerId int
	 * @param id int
	 * @param param int
	 */
	public void addHeroDiary(int playerId, int id, int param)
	{
		insertHeroDiary(playerId, id, param);
		List<HeroDiary> list = _herodiary.get(playerId);
		if (list != null)
		{
			list.add(new HeroDiary(id, System.currentTimeMillis(), param));
		}
	}
	
	/**
	 * Method insertHeroDiary.
	 * @param charId int
	 * @param action int
	 * @param param int
	 */
	private void insertHeroDiary(int charId, int action, int param)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO heroes_diary (charId, time, action, param) values(?,?,?,?)");
			statement.setInt(1, charId);
			statement.setLong(2, System.currentTimeMillis());
			statement.setInt(3, action);
			statement.setInt(4, param);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.error("SQL exception while saving DiaryData.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method loadMessage.
	 * @param charId int
	 */
	public void loadMessage(int charId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			String message = null;
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT message FROM heroes WHERE char_id=?");
			statement.setInt(1, charId);
			rset = statement.executeQuery();
			rset.next();
			message = rset.getString("message");
			_heroMessage.put(charId, message);
		}
		catch (SQLException e)
		{
			_log.error("Hero System: Couldnt load Hero Message for CharId: " + charId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method setHeroMessage.
	 * @param charId int
	 * @param message String
	 */
	public void setHeroMessage(int charId, String message)
	{
		_heroMessage.put(charId, message);
	}
	
	/**
	 * Method saveHeroMessage.
	 * @param charId int
	 */
	public void saveHeroMessage(int charId)
	{
		if (_heroMessage.get(charId) == null)
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE heroes SET message=? WHERE char_id=?;");
			statement.setString(1, _heroMessage.get(charId));
			statement.setInt(2, charId);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.error("SQL exception while saving HeroMessage.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method shutdown.
	 */
	public void shutdown()
	{
		for (int charId : _heroMessage.keySet())
		{
			saveHeroMessage(charId);
		}
	}
	
	/**
	 * Method getHeroByClass.
	 * @param classid int
	 * @return int
	 */
	public int getHeroByClass(int classid)
	{
		if (!_heroes.isEmpty())
		{
			for (Integer heroId : _heroes.keySet())
			{
				StatsSet hero = _heroes.get(heroId);
				if (hero.getInteger(Olympiad.CLASS_ID) == classid)
				{
					return heroId;
				}
			}
		}
		return 0;
	}
	
	/**
	 * Method getHeroStats.
	 * @param classId int
	 * @return Map.Entry<Integer,StatsSet>
	 */
	public Map.Entry<Integer, StatsSet> getHeroStats(int classId)
	{
		if (!_heroes.isEmpty())
		{
			for (Map.Entry<Integer, StatsSet> entry : _heroes.entrySet())
			{
				if (entry.getValue().getInteger(Olympiad.CLASS_ID) == classId)
				{
					return entry;
				}
			}
		}
		return null;
	}
	
	/**
	 * Method deleteHero.
	 * @param player Player
	 */
	public static void deleteHero(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO heroes (char_id, count, played, active) VALUES (?,?,?,?)");
			for (Integer heroId : _heroes.keySet())
			{
				int id = player.getObjectId();
				if ((id > 0) && (heroId != id))
				{
					continue;
				}
				StatsSet hero = _heroes.get(heroId);
				statement.setInt(1, heroId);
				statement.setInt(2, hero.getInteger(COUNT));
				statement.setInt(3, hero.getInteger(PLAYED));
				statement.setInt(4, 0);
				statement.execute();
				if ((_completeHeroes != null) && !_completeHeroes.containsKey(heroId))
				{
					_completeHeroes.remove(heroId);
				}
			}
		}
		catch (SQLException e)
		{
			_log.warn("Hero System: Couldnt update Heroes");
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}

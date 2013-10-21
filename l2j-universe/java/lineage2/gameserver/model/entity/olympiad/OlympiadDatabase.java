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
package lineage2.gameserver.model.entity.olympiad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.dao.OlympiadNobleDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadDatabase
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadDatabase.class);
	
	/**
	 * Method loadNoblesRank.
	 */
	public static synchronized void loadNoblesRank()
	{
		Olympiad._noblesRank = new ConcurrentHashMap<>();
		Map<Integer, Integer> tmpPlace = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(OlympiadNobleDAO.GET_ALL_CLASSIFIED_NOBLESS);
			rset = statement.executeQuery();
			int place = 1;
			while (rset.next())
			{
				tmpPlace.put(rset.getInt(Olympiad.CHAR_ID), place++);
			}
		}
		catch (Exception e)
		{
			_log.error("Olympiad System: Error!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		int rank1 = (int) Math.round(tmpPlace.size() * 0.01);
		int rank2 = (int) Math.round(tmpPlace.size() * 0.10);
		int rank3 = (int) Math.round(tmpPlace.size() * 0.25);
		int rank4 = (int) Math.round(tmpPlace.size() * 0.50);
		if (rank1 == 0)
		{
			rank1 = 1;
			rank2++;
			rank3++;
			rank4++;
		}
		for (int charId : tmpPlace.keySet())
		{
			if (tmpPlace.get(charId) <= rank1)
			{
				Olympiad._noblesRank.put(charId, 1);
			}
			else if (tmpPlace.get(charId) <= rank2)
			{
				Olympiad._noblesRank.put(charId, 2);
			}
			else if (tmpPlace.get(charId) <= rank3)
			{
				Olympiad._noblesRank.put(charId, 3);
			}
			else if (tmpPlace.get(charId) <= rank4)
			{
				Olympiad._noblesRank.put(charId, 4);
			}
			else
			{
				Olympiad._noblesRank.put(charId, 5);
			}
		}
	}
	
	/**
	 * Method cleanupNobles.
	 */
	public static synchronized void cleanupNobles()
	{
		_log.info("Olympiad: Calculating last period...");
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(OlympiadNobleDAO.OLYMPIAD_CALCULATE_LAST_PERIOD);
			statement.setInt(1, Config.OLYMPIAD_BATTLES_FOR_REWARD);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement(OlympiadNobleDAO.OLYMPIAD_CLEANUP_NOBLES);
			statement.setInt(1, Config.OLYMPIAD_POINTS_DEFAULT);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("Olympiad System: Couldn't calculate last period!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		for (Integer nobleId : Olympiad._nobles.keySet())
		{
			StatsSet nobleInfo = Olympiad._nobles.get(nobleId);
			int points = nobleInfo.getInteger(Olympiad.POINTS);
			int compDone = nobleInfo.getInteger(Olympiad.COMP_DONE);
			nobleInfo.set(Olympiad.POINTS, Config.OLYMPIAD_POINTS_DEFAULT);
			if (compDone >= Config.OLYMPIAD_BATTLES_FOR_REWARD)
			{
				nobleInfo.set(Olympiad.POINTS_PAST, points);
				nobleInfo.set(Olympiad.POINTS_PAST_STATIC, points);
			}
			else
			{
				nobleInfo.set(Olympiad.POINTS_PAST, 0);
				nobleInfo.set(Olympiad.POINTS_PAST_STATIC, 0);
			}
			nobleInfo.set(Olympiad.COMP_DONE, 0);
			nobleInfo.set(Olympiad.COMP_WIN, 0);
			nobleInfo.set(Olympiad.COMP_LOOSE, 0);
			nobleInfo.set(Olympiad.GAME_CLASSES_COUNT, 0);
			nobleInfo.set(Olympiad.GAME_NOCLASSES_COUNT, 0);
		}
	}
	
	/**
	 * Method getClassLeaderBoard.
	 * @param classId int
	 * @return List<String>
	 */
	public static List<String> getClassLeaderBoard(int classId)
	{
		List<String> names = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(OlympiadNobleDAO.GET_EACH_CLASS_LEADER);
			statement.setInt(1, classId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				names.add(rset.getString(Olympiad.CHAR_NAME));
			}
		}
		catch (Exception e)
		{
			_log.error("Olympiad System: Couldnt get heros from db!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return names;
	}
	
	/**
	 * Method sortHerosToBe.
	 */
	public static synchronized void sortHerosToBe()
	{
		if (Olympiad._period != 1)
		{
			return;
		}
		Olympiad._heroesToBe = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			StatsSet hero;
			for (ClassId id : ClassId.VALUES)
			{
				if (id.getId() > 138)
				{
					statement = con.prepareStatement(OlympiadNobleDAO.OLYMPIAD_GET_HEROS);
					statement.setInt(1, id.getId());
					statement.setInt(2, Config.OLYMPIAD_BATTLES_FOR_REWARD);
					rset = statement.executeQuery();
					if (rset.next())
					{
						hero = new StatsSet();
						hero.set(Olympiad.CLASS_ID, id.getId());
						hero.set(Olympiad.CHAR_ID, rset.getInt(Olympiad.CHAR_ID));
						hero.set(Olympiad.CHAR_NAME, rset.getString(Olympiad.CHAR_NAME));
						Olympiad._heroesToBe.add(hero);
					}
					DbUtils.close(statement, rset);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Olympiad System: Couldnt get heroes from db!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method saveNobleData.
	 * @param nobleId int
	 */
	public static synchronized void saveNobleData(int nobleId)
	{
		OlympiadNobleDAO.getInstance().replace(nobleId);
	}
	
	/**
	 * Method saveNobleData.
	 */
	public static synchronized void saveNobleData()
	{
		if (Olympiad._nobles == null)
		{
			return;
		}
		for (Integer nobleId : Olympiad._nobles.keySet())
		{
			saveNobleData(nobleId);
		}
	}
	
	/**
	 * Method setNewOlympiadEnd.
	 */
	public static synchronized void setNewOlympiadEnd()
	{
		Announcements.getInstance().announceToAll(new SystemMessage(SystemMessage.OLYMPIAD_PERIOD_S1_HAS_STARTED).addNumber(Olympiad._currentCycle));
		Calendar currentTime = Calendar.getInstance();
		currentTime.set(Calendar.DAY_OF_MONTH, 1);
		currentTime.add(Calendar.MONTH, 1);
		currentTime.set(Calendar.HOUR_OF_DAY, 00);
		currentTime.set(Calendar.MINUTE, 00);
		Olympiad._olympiadEnd = currentTime.getTimeInMillis();
		Calendar nextChange = Calendar.getInstance();
		Olympiad._nextWeeklyChange = nextChange.getTimeInMillis() + Config.ALT_OLY_WPERIOD;
		Olympiad._isOlympiadEnd = false;
	}
	
	/**
	 * Method save.
	 */
	public static void save()
	{
		saveNobleData();
		ServerVariables.set("Olympiad_CurrentCycle", Olympiad._currentCycle);
		ServerVariables.set("Olympiad_Period", Olympiad._period);
		ServerVariables.set("Olympiad_End", Olympiad._olympiadEnd);
		ServerVariables.set("Olympiad_ValdationEnd", Olympiad._validationEnd);
		ServerVariables.set("Olympiad_NextWeeklyChange", Olympiad._nextWeeklyChange);
	}
}

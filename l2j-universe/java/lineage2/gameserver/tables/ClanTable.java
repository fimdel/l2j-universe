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
package lineage2.gameserver.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListDeleteAll;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.SiegeUtils;
import lineage2.gameserver.utils.Util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ClanTable.class);
	/**
	 * Field _instance.
	 */
	private static ClanTable _instance;
	/**
	 * Field _clans.
	 */
	private final Map<Integer, Clan> _clans = new ConcurrentHashMap<>();
	/**
	 * Field _alliances.
	 */
	private final Map<Integer, Alliance> _alliances = new ConcurrentHashMap<>();
	
	/**
	 * Method getInstance.
	 * @return ClanTable
	 */
	public static ClanTable getInstance()
	{
		if (_instance == null)
		{
			new ClanTable();
		}
		return _instance;
	}
	
	/**
	 * Method getClans.
	 * @return Clan[]
	 */
	public Clan[] getClans()
	{
		return _clans.values().toArray(new Clan[_clans.size()]);
	}
	
	/**
	 * Method getAlliances.
	 * @return Alliance[]
	 */
	public Alliance[] getAlliances()
	{
		return _alliances.values().toArray(new Alliance[_alliances.size()]);
	}
	
	/**
	 * Constructor for ClanTable.
	 */
	private ClanTable()
	{
		_instance = this;
		restoreClans();
		restoreAllies();
		restoreWars();
	}
	
	/**
	 * Method getClan.
	 * @param clanId int
	 * @return Clan
	 */
	public Clan getClan(int clanId)
	{
		if (clanId <= 0)
		{
			return null;
		}
		return _clans.get(clanId);
	}
	
	/**
	 * Method getClanName.
	 * @param clanId int
	 * @return String
	 */
	public String getClanName(int clanId)
	{
		Clan c = getClan(clanId);
		return c != null ? c.getName() : StringUtils.EMPTY;
	}
	
	/**
	 * Method getClanByCharId.
	 * @param charId int
	 * @return Clan
	 */
	public Clan getClanByCharId(int charId)
	{
		if (charId <= 0)
		{
			return null;
		}
		for (Clan clan : getClans())
		{
			if ((clan != null) && clan.isAnyMember(charId))
			{
				return clan;
			}
		}
		return null;
	}
	
	/**
	 * Method getAlliance.
	 * @param allyId int
	 * @return Alliance
	 */
	public Alliance getAlliance(int allyId)
	{
		if (allyId <= 0)
		{
			return null;
		}
		return _alliances.get(allyId);
	}
	
	/**
	 * Method getAllianceByCharId.
	 * @param charId int
	 * @return Alliance
	 */
	public Alliance getAllianceByCharId(int charId)
	{
		if (charId <= 0)
		{
			return null;
		}
		Clan charClan = getClanByCharId(charId);
		return charClan == null ? null : charClan.getAlliance();
	}
	
	/**
	 * Method getClanAndAllianceByCharId.
	 * @param charId int
	 * @return Map.Entry<Clan,Alliance>
	 */
	public Map.Entry<Clan, Alliance> getClanAndAllianceByCharId(int charId)
	{
		Player player = GameObjectsStorage.getPlayer(charId);
		Clan charClan = player != null ? player.getClan() : getClanByCharId(charId);
		return new SimpleEntry<>(charClan, charClan == null ? null : charClan.getAlliance());
	}
	
	/**
	 * Method restoreClans.
	 */
	public void restoreClans()
	{
		List<Integer> clanIds = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT clan_id FROM clan_data");
			result = statement.executeQuery();
			while (result.next())
			{
				clanIds.add(result.getInt("clan_id"));
			}
		}
		catch (Exception e)
		{
			_log.warn("Error while restoring clans!!! " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, result);
		}
		for (int clanId : clanIds)
		{
			Clan clan = Clan.restore(clanId);
			if (clan == null)
			{
				_log.warn("Error while restoring clanId: " + clanId);
				continue;
			}
			if (clan.getAllSize() <= 0)
			{
				_log.warn("membersCount = 0 for clanId: " + clanId);
				continue;
			}
			if (clan.getLeader() == null)
			{
				_log.warn("Not found leader for clanId: " + clanId);
				continue;
			}
			_clans.put(clan.getClanId(), clan);
		}
	}
	
	/**
	 * Method restoreAllies.
	 */
	public void restoreAllies()
	{
		List<Integer> allyIds = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT ally_id FROM ally_data");
			result = statement.executeQuery();
			while (result.next())
			{
				allyIds.add(result.getInt("ally_id"));
			}
		}
		catch (Exception e)
		{
			_log.warn("Error while restoring allies!!! " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, result);
		}
		for (int allyId : allyIds)
		{
			Alliance ally = new Alliance(allyId);
			if (ally.getMembersCount() <= 0)
			{
				_log.warn("membersCount = 0 for allyId: " + allyId);
				continue;
			}
			if (ally.getLeader() == null)
			{
				_log.warn("Not found leader for allyId: " + allyId);
				continue;
			}
			_alliances.put(ally.getAllyId(), ally);
		}
	}
	
	/**
	 * Method getClanByName.
	 * @param clanName String
	 * @return Clan
	 */
	public Clan getClanByName(String clanName)
	{
		if (!Util.isMatchingRegexp(clanName, Config.CLAN_NAME_TEMPLATE))
		{
			return null;
		}
		for (Clan clan : _clans.values())
		{
			if (clan.getName().equalsIgnoreCase(clanName))
			{
				return clan;
			}
		}
		return null;
	}
	
	/**
	 * Method getAllyByName.
	 * @param allyName String
	 * @return Alliance
	 */
	public Alliance getAllyByName(String allyName)
	{
		if (!Util.isMatchingRegexp(allyName, Config.ALLY_NAME_TEMPLATE))
		{
			return null;
		}
		for (Alliance ally : _alliances.values())
		{
			if (ally.getAllyName().equalsIgnoreCase(allyName))
			{
				return ally;
			}
		}
		return null;
	}
	
	/**
	 * Method createClan.
	 * @param player Player
	 * @param clanName String
	 * @return Clan
	 */
	public Clan createClan(Player player, String clanName)
	{
		if (getClanByName(clanName) == null)
		{
			UnitMember leader = new UnitMember(player);
			leader.setLeaderOf(Clan.SUBUNIT_MAIN_CLAN);
			Clan clan = new Clan(IdFactory.getInstance().getNextId());
			SubUnit unit = new SubUnit(clan, Clan.SUBUNIT_MAIN_CLAN, leader, clanName);
			unit.addUnitMember(leader);
			clan.addSubUnit(unit, false);
			clan.store();
			player.setPledgeType(Clan.SUBUNIT_MAIN_CLAN);
			player.setClan(clan);
			player.setPowerGrade(6);
			leader.setPlayerInstance(player, false);
			_clans.put(clan.getClanId(), clan);
			return clan;
		}
		return null;
	}
	
	/**
	 * Method dissolveClan.
	 * @param player Player
	 */
	public void dissolveClan(Player player)
	{
		Clan clan = player.getClan();
		long curtime = System.currentTimeMillis();
		SiegeUtils.removeSiegeSkills(player);
		for (Player clanMember : clan.getOnlineMembers(0))
		{
			clanMember.setClan(null);
			clanMember.setTitle(null);
			clanMember.sendPacket(PledgeShowMemberListDeleteAll.STATIC, Msg.YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN_YOU_ARE_NOT_ALLOWED_TO_JOIN_ANOTHER_CLAN_FOR_24_HOURS);
			clanMember.broadcastCharInfo();
			clanMember.setLeaveClanTime(curtime);
		}
		clan.flush();
		deleteClanFromDb(clan.getClanId());
		_clans.remove(clan.getClanId());
		player.sendPacket(Msg.CLAN_HAS_DISPERSED);
		player.setDeleteClanTime(curtime);
	}
	
	/**
	 * Method deleteClanFromDb.
	 * @param clanId int
	 */
	public void deleteClanFromDb(int clanId)
	{
		long curtime = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET clanid=0,title='',pledge_type=0,pledge_rank=0,lvl_joined_academy=0,apprentice=0,leaveclan=? WHERE clanid=?");
			statement.setLong(1, curtime / 1000L);
			statement.setInt(2, clanId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM clan_data WHERE clan_id=?");
			statement.setInt(1, clanId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM clan_subpledges WHERE clan_id=?");
			statement.setInt(1, clanId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM clan_privs WHERE clan_id=?");
			statement.setInt(1, clanId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM clan_skills WHERE clan_id=?");
			statement.setInt(1, clanId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("could not dissolve clan:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method createAlliance.
	 * @param player Player
	 * @param allyName String
	 * @return Alliance
	 */
	public Alliance createAlliance(Player player, String allyName)
	{
		Alliance alliance = null;
		if (getAllyByName(allyName) == null)
		{
			Clan leader = player.getClan();
			alliance = new Alliance(IdFactory.getInstance().getNextId(), allyName, leader);
			alliance.store();
			_alliances.put(alliance.getAllyId(), alliance);
			player.getClan().setAllyId(alliance.getAllyId());
			for (Player temp : player.getClan().getOnlineMembers(0))
			{
				temp.broadcastCharInfo();
			}
		}
		return alliance;
	}
	
	/**
	 * Method dissolveAlly.
	 * @param player Player
	 */
	public void dissolveAlly(Player player)
	{
		int allyId = player.getAllyId();
		for (Clan member : player.getAlliance().getMembers())
		{
			member.setAllyId(0);
			member.broadcastClanStatus(false, true, false);
			member.broadcastToOnlineMembers(Msg.YOU_HAVE_WITHDRAWN_FROM_THE_ALLIANCE);
			member.setLeavedAlly();
		}
		deleteAllyFromDb(allyId);
		_alliances.remove(allyId);
		player.sendPacket(Msg.THE_ALLIANCE_HAS_BEEN_DISSOLVED);
		player.getClan().setDissolvedAlly();
	}
	
	/**
	 * Method deleteAllyFromDb.
	 * @param allyId int
	 */
	public void deleteAllyFromDb(int allyId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET ally_id=0 WHERE ally_id=?");
			statement.setInt(1, allyId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM ally_data WHERE ally_id=?");
			statement.setInt(1, allyId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("could not dissolve clan:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method startClanWar.
	 * @param clan1 Clan
	 * @param clan2 Clan
	 */
	public void startClanWar(Clan clan1, Clan clan2)
	{
		clan1.setEnemyClan(clan2);
		clan2.setAttackerClan(clan1);
		clan1.broadcastClanStatus(false, false, true);
		clan2.broadcastClanStatus(false, false, true);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO clan_wars (clan1, clan2) VALUES(?,?)");
			statement.setInt(1, clan1.getClanId());
			statement.setInt(2, clan2.getClanId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("could not store clan war data:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		clan1.broadcastToOnlineMembers(new SystemMessage(SystemMessage.CLAN_WAR_HAS_BEEN_DECLARED_AGAINST_S1_CLAN_IF_YOU_ARE_KILLED_DURING_THE_CLAN_WAR_BY_MEMBERS_OF_THE_OPPOSING_CLAN_THE_EXPERIENCE_PENALTY_WILL_BE_REDUCED_TO_1_4_OF_NORMAL).addString(clan2.getName()));
		clan2.broadcastToOnlineMembers(new SystemMessage(SystemMessage.S1_CLAN_HAS_DECLARED_CLAN_WAR).addString(clan1.getName()));
	}
	
	/**
	 * Method stopClanWar.
	 * @param clan1 Clan
	 * @param clan2 Clan
	 */
	public void stopClanWar(Clan clan1, Clan clan2)
	{
		clan1.deleteEnemyClan(clan2);
		clan2.deleteAttackerClan(clan1);
		clan1.broadcastClanStatus(false, false, true);
		clan2.broadcastClanStatus(false, false, true);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM clan_wars WHERE clan1=? AND clan2=?");
			statement.setInt(1, clan1.getClanId());
			statement.setInt(2, clan2.getClanId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("could not delete war data:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		clan1.broadcastToOnlineMembers(new SystemMessage(SystemMessage.THE_WAR_AGAINST_S1_CLAN_HAS_BEEN_STOPPED).addString(clan2.getName()));
		clan2.broadcastToOnlineMembers(new SystemMessage(SystemMessage.S1_CLAN_HAS_STOPPED_THE_WAR).addString(clan1.getName()));
	}
	
	/**
	 * Method restoreWars.
	 */
	private void restoreWars()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT clan1, clan2 FROM clan_wars");
			rset = statement.executeQuery();
			Clan clan1;
			Clan clan2;
			while (rset.next())
			{
				clan1 = getClan(rset.getInt("clan1"));
				clan2 = getClan(rset.getInt("clan2"));
				if ((clan1 != null) && (clan2 != null))
				{
					clan1.setEnemyClan(clan2);
					clan2.setAttackerClan(clan1);
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("could not restore clan wars data:");
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method unload.
	 */
	public static void unload()
	{
		if (_instance != null)
		{
			try
			{
				_instance.finalize();
			}
			catch (Throwable e)
			{
			}
		}
	}
}

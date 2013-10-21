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
package lineage2.gameserver.model.pledge;

import java.sql.Connection;
import java.sql.PreparedStatement;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NickNameChanged;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class UnitMember
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(UnitMember.class);
	/**
	 * Field _player.
	 */
	private Player _player;
	/**
	 * Field _clan.
	 */
	private Clan _clan;
	/**
	 * Field _name.
	 */
	private String _name;
	/**
	 * Field _title.
	 */
	private String _title;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _classId.
	 */
	private int _classId;
	/**
	 * Field _sex.
	 */
	private int _sex;
	/**
	 * Field _pledgeType.
	 */
	private int _race;
	private int _pledgeType;
	/**
	 * Field _powerGrade.
	 */
	private int _powerGrade;
	/**
	 * Field _apprentice.
	 */
	private int _apprentice;
	/**
	 * Field _leaderOf.
	 */
	private int _leaderOf = Clan.SUBUNIT_NONE;
	
	/**
	 * Constructor for UnitMember.
	 * @param clan Clan
	 * @param name String
	 * @param title String
	 * @param level int
	 * @param classId int
	 * @param objectId int
	 * @param pledgeType int
	 * @param powerGrade int
	 * @param apprentice int
	 * @param sex int
	 * @param leaderOf int
	 */
	public UnitMember(Clan clan, String name, String title, int level, int classId, int objectId, int pledgeType, int powerGrade, int apprentice, int sex, int leaderOf)
	{
		_clan = clan;
		_objectId = objectId;
		_name = name;
		_title = title;
		_level = level;
		_classId = classId;
		_pledgeType = pledgeType;
		_powerGrade = powerGrade;
		_apprentice = apprentice;
		_sex = sex;
		_leaderOf = leaderOf;
		if (powerGrade != 0)
		{
			RankPrivs r = clan.getRankPrivs(powerGrade);
			r.setParty(clan.countMembersByRank(powerGrade));
		}
	}
	
	/**
	 * Constructor for UnitMember.
	 * @param player Player
	 */
	public UnitMember(Player player)
	{
		_objectId = player.getObjectId();
		_player = player;
	}
	
	/**
	 * Method setPlayerInstance.
	 * @param player Player
	 * @param exit boolean
	 */
	public void setPlayerInstance(Player player, boolean exit)
	{
		_player = exit ? null : player;
		if (player == null)
		{
			return;
		}
		_clan = player.getClan();
		_name = player.getName();
		_title = player.getTitle();
		_level = player.getLevel();
		_classId = player.getClassId().getId();
		_pledgeType = player.getPledgeType();
		_powerGrade = player.getPowerGrade();
		_apprentice = player.getApprentice();
		_sex = player.getSex();
		setRace(player.getRace().ordinal());
	}
	
	public void setRace(int _race)
	{
		this._race = _race;
	}

	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _player;
	}
	
	/**
	 * Method isOnline.
	 * @return boolean
	 */
	public boolean isOnline()
	{
		Player player = getPlayer();
		return (player != null) && !player.isInOfflineMode();
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	public Clan getClan()
	{
		Player player = getPlayer();
		return player == null ? _clan : player.getClan();
	}
	
	/**
	 * Method getClassId.
	 * @return int
	 */
	public int getClassId()
	{
		Player player = getPlayer();
		return player == null ? _classId : player.getClassId().getId();
	}
	
	/**
	 * Method getSex.
	 * @return int
	 */
	public int getSex()
	{
		Player player = getPlayer();
		return player == null ? _sex : player.getSex();
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		Player player = getPlayer();
		return player == null ? _level : player.getLevel();
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		Player player = getPlayer();
		return player == null ? _name : player.getName();
	}
	
	/**
	 * Method getObjectId.
	 * @return int
	 */
	public int getObjectId()
	{
		return _objectId;
	}
	
	/**
	 * Method getTitle.
	 * @return String
	 */
	public String getTitle()
	{
		Player player = getPlayer();
		return player == null ? _title : player.getTitle();
	}
	
	/**
	 * Method setTitle.
	 * @param title String
	 */
	public void setTitle(String title)
	{
		Player player = getPlayer();
		_title = title;
		if (player != null)
		{
			player.setTitle(title);
			player.broadcastPacket(new NickNameChanged(player));
		}
		else
		{
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("UPDATE characters SET title=? WHERE obj_Id=?");
				statement.setString(1, title);
				statement.setInt(2, getObjectId());
				statement.execute();
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}
	
	/**
	 * Method getSubUnit.
	 * @return SubUnit
	 */
	public SubUnit getSubUnit()
	{
		return _clan.getSubUnit(_pledgeType);
	}
	
	/**
	 * Method getPledgeType.
	 * @return int
	 */
	public int getPledgeType()
	{
		Player player = getPlayer();
		return player == null ? _pledgeType : player.getPledgeType();
	}
	
	/**
	 * Method setPledgeType.
	 * @param pledgeType int
	 */
	public void setPledgeType(int pledgeType)
	{
		Player player = getPlayer();
		_pledgeType = pledgeType;
		if (player != null)
		{
			player.setPledgeType(pledgeType);
		}
		else
		{
			updatePledgeType();
		}
	}
	
	/**
	 * Method updatePledgeType.
	 */
	private void updatePledgeType()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET pledge_type=? WHERE obj_Id=?");
			statement.setInt(1, _pledgeType);
			statement.setInt(2, getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getPowerGrade.
	 * @return int
	 */
	public int getPowerGrade()
	{
		Player player = getPlayer();
		return player == null ? _powerGrade : player.getPowerGrade();
	}
	
	/**
	 * Method setPowerGrade.
	 * @param newPowerGrade int
	 */
	public void setPowerGrade(int newPowerGrade)
	{
		Player player = getPlayer();
		int oldPowerGrade = getPowerGrade();
		_powerGrade = newPowerGrade;
		if (player != null)
		{
			player.setPowerGrade(newPowerGrade);
		}
		else
		{
			updatePowerGrade();
		}
		updatePowerGradeParty(oldPowerGrade, newPowerGrade);
	}
	
	/**
	 * Method updatePowerGradeParty.
	 * @param oldGrade int
	 * @param newGrade int
	 */
	private void updatePowerGradeParty(int oldGrade, int newGrade)
	{
		if (oldGrade != 0)
		{
			RankPrivs r1 = getClan().getRankPrivs(oldGrade);
			r1.setParty(getClan().countMembersByRank(oldGrade));
		}
		if (newGrade != 0)
		{
			RankPrivs r2 = getClan().getRankPrivs(newGrade);
			r2.setParty(getClan().countMembersByRank(newGrade));
		}
	}
	
	/**
	 * Method updatePowerGrade.
	 */
	private void updatePowerGrade()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET pledge_rank=? WHERE obj_Id=?");
			statement.setInt(1, _powerGrade);
			statement.setInt(2, getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getApprentice.
	 * @return int
	 */
	private int getApprentice()
	{
		Player player = getPlayer();
		return player == null ? _apprentice : player.getApprentice();
	}
	
	/**
	 * Method setApprentice.
	 * @param apprentice int
	 */
	public void setApprentice(int apprentice)
	{
		Player player = getPlayer();
		_apprentice = apprentice;
		if (player != null)
		{
			player.setApprentice(apprentice);
		}
		else
		{
			updateApprentice();
		}
	}
	
	/**
	 * Method updateApprentice.
	 */
	private void updateApprentice()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET apprentice=? WHERE obj_Id=?");
			statement.setInt(1, _apprentice);
			statement.setInt(2, getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getApprenticeName.
	 * @return String
	 */
	public String getApprenticeName()
	{
		if (getApprentice() != 0)
		{
			if (getClan().getAnyMember(getApprentice()) != null)
			{
				return getClan().getAnyMember(getApprentice()).getName();
			}
		}
		return "";
	}
	
	/**
	 * Method hasApprentice.
	 * @return boolean
	 */
	public boolean hasApprentice()
	{
		return getApprentice() != 0;
	}
	
	/**
	 * Method getSponsor.
	 * @return int
	 */
	public int getSponsor()
	{
		if (getPledgeType() != Clan.SUBUNIT_ACADEMY)
		{
			return 0;
		}
		int id = getObjectId();
		for (UnitMember element : getClan())
		{
			if (element.getApprentice() == id)
			{
				return element.getObjectId();
			}
		}
		return 0;
	}
	
	/**
	 * Method getSponsorName.
	 * @return String
	 */
	private String getSponsorName()
	{
		int sponsorId = getSponsor();
		if (sponsorId == 0)
		{
			return "";
		}
		else if (getClan().getAnyMember(sponsorId) != null)
		{
			return getClan().getAnyMember(sponsorId).getName();
		}
		return "";
	}
	
	/**
	 * Method hasSponsor.
	 * @return boolean
	 */
	public boolean hasSponsor()
	{
		return getSponsor() != 0;
	}
	
	/**
	 * Method getRelatedName.
	 * @return String
	 */
	public String getRelatedName()
	{
		if (getPledgeType() == Clan.SUBUNIT_ACADEMY)
		{
			return getSponsorName();
		}
		return getApprenticeName();
	}
	
	/**
	 * Method isClanLeader.
	 * @return boolean
	 */
	public boolean isClanLeader()
	{
		Player player = getPlayer();
		return player == null ? (_leaderOf == Clan.SUBUNIT_MAIN_CLAN) : player.isClanLeader();
	}
	
	/**
	 * Method isSubLeader.
	 * @return int
	 */
	public int isSubLeader()
	{
		for (SubUnit pledge : getClan().getAllSubUnits())
		{
			if (pledge.getLeaderObjectId() == getObjectId())
			{
				return pledge.getType();
			}
		}
		return 0;
	}
	
	/**
	 * Method setLeaderOf.
	 * @param leaderOf int
	 */
	public void setLeaderOf(int leaderOf)
	{
		_leaderOf = leaderOf;
	}
	
	/**
	 * Method getLeaderOf.
	 * @return int
	 */
	public int getLeaderOf()
	{
		return _leaderOf;
	}

	public int getRace()
	{
		return _race;
	}
}

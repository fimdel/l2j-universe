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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import lineage2.commons.collections.JoinedIterator;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.ResidenceType;
import lineage2.gameserver.model.items.ClanWarehouse;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.PledgeReceiveSubPledgeCreated;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListAll;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListDeleteAll;
import lineage2.gameserver.network.serverpackets.PledgeSkillList;
import lineage2.gameserver.network.serverpackets.PledgeSkillListAdd;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Clan implements Iterable<UnitMember>
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Clan.class);
	/**
	 * Field _clanId.
	 */
	private final int _clanId;
	/**
	 * Field _allyId.
	 */
	private int _allyId;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _hasCastle.
	 */
	private int _hasCastle;
	/**
	 * Field _hasFortress.
	 */
	private int _hasFortress;
	/**
	 * Field _hasHideout.
	 */
	private int _hasHideout;
	/**
	 * Field _crestId.
	 */
	private int _crestId;
	/**
	 * Field _crestLargeId.
	 */
	private int _crestLargeId;
	/**
	 * Field _expelledMemberTime.
	 */
	private long _expelledMemberTime;
	/**
	 * Field _leavedAllyTime.
	 */
	private long _leavedAllyTime;
	/**
	 * Field _dissolvedAllyTime.
	 */
	private long _dissolvedAllyTime;
	/**
	 * Field _airship.
	 */
	private ClanAirShip _airship;
	/**
	 * Field _airshipLicense.
	 */
	private boolean _airshipLicense;
	/**
	 * Field _airshipFuel.
	 */
	private int _airshipFuel;
	/**
	 * Field EXPELLED_MEMBER_PENALTY.
	 */
	public static long EXPELLED_MEMBER_PENALTY = 24 * 60 * 60 * 1000L;
	/**
	 * Field LEAVED_ALLY_PENALTY.
	 */
	public static long LEAVED_ALLY_PENALTY = 24 * 60 * 60 * 1000L;
	/**
	 * Field DISSOLVED_ALLY_PENALTY.
	 */
	public static long DISSOLVED_ALLY_PENALTY = 24 * 60 * 60 * 1000L;
	/**
	 * Field _warehouse.
	 */
	private final ClanWarehouse _warehouse;
	/**
	 * Field _whBonus.
	 */
	private int _whBonus = -1;
	/**
	 * Field _notice.
	 */
	private String _notice = null;
	/**
	 * Field _atWarWith.
	 */
	private final List<Clan> _atWarWith = new ArrayList<>();
	/**
	 * Field _underAttackFrom.
	 */
	private final List<Clan> _underAttackFrom = new ArrayList<>();
	/**
	 * Field _skills.
	 */
	protected IntObjectMap<Skill> _skills = new CTreeIntObjectMap<>();
	/**
	 * Field _privs.
	 */
	protected IntObjectMap<RankPrivs> _privs = new CTreeIntObjectMap<>();
	/**
	 * Field _subUnits.
	 */
	protected IntObjectMap<SubUnit> _subUnits = new CTreeIntObjectMap<>();
	/**
	 * Field _clanLeaderSkill.
	 */
	static Skill _clanLeaderSkill = SkillTable.getInstance().getInfo(19009, 1);
	/**
	 * Field _reputation.
	 */
	private int _reputation = 0;
	/**
	 * Field CP_NOTHING. (value is 0)
	 */
	public static final int CP_NOTHING = 0;
	/**
	 * Field CP_CL_INVITE_CLAN. (value is 2)
	 */
	public static final int CP_CL_INVITE_CLAN = 2;
	/**
	 * Field CP_CL_MANAGE_TITLES. (value is 4)
	 */
	public static final int CP_CL_MANAGE_TITLES = 4;
	/**
	 * Field CP_CL_WAREHOUSE_SEARCH. (value is 8)
	 */
	public static final int CP_CL_WAREHOUSE_SEARCH = 8;
	/**
	 * Field CP_CL_MANAGE_RANKS. (value is 16)
	 */
	public static final int CP_CL_MANAGE_RANKS = 16;
	/**
	 * Field CP_CL_CLAN_WAR. (value is 32)
	 */
	public static final int CP_CL_CLAN_WAR = 32;
	/**
	 * Field CP_CL_DISMISS. (value is 64)
	 */
	public static final int CP_CL_DISMISS = 64;
	/**
	 * Field CP_CL_EDIT_CREST. (value is 128)
	 */
	public static final int CP_CL_EDIT_CREST = 128;
	/**
	 * Field CP_CL_APPRENTICE. (value is 256)
	 */
	public static final int CP_CL_APPRENTICE = 256;
	/**
	 * Field CP_CL_TROOPS_FAME. (value is 512)
	 */
	public static final int CP_CL_TROOPS_FAME = 512;
	/**
	 * Field CP_CL_SUMMON_AIRSHIP. (value is 1024)
	 */
	public static final int CP_CL_SUMMON_AIRSHIP = 1024;
	/**
	 * Field CP_CH_ENTRY_EXIT. (value is 2048)
	 */
	public static final int CP_CH_ENTRY_EXIT = 2048;
	/**
	 * Field CP_CH_USE_FUNCTIONS. (value is 4096)
	 */
	public static final int CP_CH_USE_FUNCTIONS = 4096;
	/**
	 * Field CP_CH_AUCTION. (value is 8192)
	 */
	public static final int CP_CH_AUCTION = 8192;
	/**
	 * Field CP_CH_DISMISS. (value is 16384)
	 */
	public static final int CP_CH_DISMISS = 16384;
	/**
	 * Field CP_CH_SET_FUNCTIONS. (value is 32768)
	 */
	public static final int CP_CH_SET_FUNCTIONS = 32768;
	/**
	 * Field CP_CS_ENTRY_EXIT. (value is 65536)
	 */
	public static final int CP_CS_ENTRY_EXIT = 65536;
	/**
	 * Field CP_CS_MANOR_ADMIN. (value is 131072)
	 */
	public static final int CP_CS_MANOR_ADMIN = 131072;
	/**
	 * Field CP_CS_MANAGE_SIEGE. (value is 262144)
	 */
	public static final int CP_CS_MANAGE_SIEGE = 262144;
	/**
	 * Field CP_CS_USE_FUNCTIONS. (value is 524288)
	 */
	public static final int CP_CS_USE_FUNCTIONS = 524288;
	/**
	 * Field CP_CS_DISMISS. (value is 1048576)
	 */
	public static final int CP_CS_DISMISS = 1048576;
	/**
	 * Field CP_CS_TAXES. (value is 2097152)
	 */
	public static final int CP_CS_TAXES = 2097152;
	/**
	 * Field CP_CS_MERCENARIES. (value is 4194304)
	 */
	public static final int CP_CS_MERCENARIES = 4194304;
	/**
	 * Field CP_CS_SET_FUNCTIONS. (value is 8388606)
	 */
	public static final int CP_CS_SET_FUNCTIONS = 8388606;
	/**
	 * Field CP_ALL. (value is 16777214)
	 */
	public static final int CP_ALL = 16777214;
	/**
	 * Field RANK_FIRST. (value is 1)
	 */
	public static final int RANK_FIRST = 1;
	/**
	 * Field RANK_LAST. (value is 9)
	 */
	public static final int RANK_LAST = 9;
	/**
	 * Field SUBUNIT_NONE.
	 */
	public static final int SUBUNIT_NONE = Byte.MIN_VALUE;
	/**
	 * Field SUBUNIT_ACADEMY. (value is -1)
	 */
	public static final int SUBUNIT_ACADEMY = -1;
	/**
	 * Field SUBUNIT_MAIN_CLAN. (value is 0)
	 */
	public static final int SUBUNIT_MAIN_CLAN = 0;
	/**
	 * Field SUBUNIT_ROYAL1. (value is 100)
	 */
	public static final int SUBUNIT_ROYAL1 = 100;
	/**
	 * Field SUBUNIT_ROYAL2. (value is 200)
	 */
	public static final int SUBUNIT_ROYAL2 = 200;
	/**
	 * Field SUBUNIT_KNIGHT1. (value is 1001)
	 */
	public static final int SUBUNIT_KNIGHT1 = 1001;
	/**
	 * Field SUBUNIT_KNIGHT2. (value is 1002)
	 */
	public static final int SUBUNIT_KNIGHT2 = 1002;
	/**
	 * Field SUBUNIT_KNIGHT3. (value is 2001)
	 */
	public static final int SUBUNIT_KNIGHT3 = 2001;
	/**
	 * Field SUBUNIT_KNIGHT4. (value is 2002)
	 */
	public static final int SUBUNIT_KNIGHT4 = 2002;
	/**
	 * Field REPUTATION_COMPARATOR.
	 */
	private final static ClanReputationComparator REPUTATION_COMPARATOR = new ClanReputationComparator();
	/**
	 * Field REPUTATION_PLACES. (value is 100)
	 */
	private final static int REPUTATION_PLACES = 100;
	
	/**
	 * Constructor for Clan.
	 * @param clanId int
	 */
	public Clan(int clanId)
	{
		_clanId = clanId;
		InitializePrivs();
		_warehouse = new ClanWarehouse(this);
		_warehouse.restore();
	}
	
	/**
	 * Method getClanId.
	 * @return int
	 */
	public int getClanId()
	{
		return _clanId;
	}
	
	/**
	 * Method getLeaderId.
	 * @return int
	 */
	public int getLeaderId()
	{
		return getLeaderId(SUBUNIT_MAIN_CLAN);
	}
	
	/**
	 * Method getLeader.
	 * @return UnitMember
	 */
	public UnitMember getLeader()
	{
		return getLeader(SUBUNIT_MAIN_CLAN);
	}
	
	/**
	 * Method getLeaderName.
	 * @return String
	 */
	public String getLeaderName()
	{
		return getLeaderName(SUBUNIT_MAIN_CLAN);
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return getUnitName(SUBUNIT_MAIN_CLAN);
	}
	
	/**
	 * Method getAnyMember.
	 * @param id int
	 * @return UnitMember
	 */
	public UnitMember getAnyMember(int id)
	{
		for (SubUnit unit : getAllSubUnits())
		{
			UnitMember m = unit.getUnitMember(id);
			if (m != null)
			{
				return m;
			}
		}
		return null;
	}
	
	/**
	 * Method getAnyMember.
	 * @param name String
	 * @return UnitMember
	 */
	public UnitMember getAnyMember(String name)
	{
		for (SubUnit unit : getAllSubUnits())
		{
			UnitMember m = unit.getUnitMember(name);
			if (m != null)
			{
				return m;
			}
		}
		return null;
	}
	
	/**
	 * Method getAllSize.
	 * @return int
	 */
	public int getAllSize()
	{
		int size = 0;
		for (SubUnit unit : getAllSubUnits())
		{
			size += unit.size();
		}
		return size;
	}
	
	/**
	 * Method getUnitName.
	 * @param unitType int
	 * @return String
	 */
	public String getUnitName(int unitType)
	{
		if ((unitType == SUBUNIT_NONE) || !_subUnits.containsKey(unitType))
		{
			return StringUtils.EMPTY;
		}
		return getSubUnit(unitType).getName();
	}
	
	/**
	 * Method getLeaderName.
	 * @param unitType int
	 * @return String
	 */
	public String getLeaderName(int unitType)
	{
		if ((unitType == SUBUNIT_NONE) || !_subUnits.containsKey(unitType))
		{
			return StringUtils.EMPTY;
		}
		return getSubUnit(unitType).getLeaderName();
	}
	
	/**
	 * Method getLeaderId.
	 * @param unitType int
	 * @return int
	 */
	public int getLeaderId(int unitType)
	{
		if ((unitType == SUBUNIT_NONE) || !_subUnits.containsKey(unitType))
		{
			return 0;
		}
		return getSubUnit(unitType).getLeaderObjectId();
	}
	
	/**
	 * Method getLeader.
	 * @param unitType int
	 * @return UnitMember
	 */
	public UnitMember getLeader(int unitType)
	{
		if ((unitType == SUBUNIT_NONE) || !_subUnits.containsKey(unitType))
		{
			return null;
		}
		return getSubUnit(unitType).getLeader();
	}
	
	/**
	 * Method flush.
	 */
	public void flush()
	{
		for (UnitMember member : this)
		{
			removeClanMember(member.getObjectId());
		}
		_warehouse.writeLock();
		try
		{
			for (ItemInstance item : _warehouse.getItems())
			{
				_warehouse.destroyItem(item);
			}
		}
		finally
		{
			_warehouse.writeUnlock();
		}
		if (_hasCastle != 0)
		{
			ResidenceHolder.getInstance().getResidence(Castle.class, _hasCastle).changeOwner(null);
		}
		if (_hasFortress != 0)
		{
			ResidenceHolder.getInstance().getResidence(Fortress.class, _hasFortress).changeOwner(null);
		}
	}
	
	/**
	 * Method removeClanMember.
	 * @param id int
	 */
	public void removeClanMember(int id)
	{
		if (id == getLeaderId(SUBUNIT_MAIN_CLAN))
		{
			return;
		}
		for (SubUnit unit : getAllSubUnits())
		{
			if (unit.isUnitMember(id))
			{
				removeClanMember(unit.getType(), id);
				break;
			}
		}
	}
	
	/**
	 * Method removeClanMember.
	 * @param subUnitId int
	 * @param objectId int
	 */
	public void removeClanMember(int subUnitId, int objectId)
	{
		SubUnit subUnit = getSubUnit(subUnitId);
		if (subUnit == null)
		{
			return;
		}
		subUnit.removeUnitMember(objectId);
	}
	
	/**
	 * Method getAllMembers.
	 * @return List<UnitMember>
	 */
	public List<UnitMember> getAllMembers()
	{
		Collection<SubUnit> units = getAllSubUnits();
		int size = 0;
		for (SubUnit unit : units)
		{
			size += unit.size();
		}
		List<UnitMember> members = new ArrayList<>(size);
		for (SubUnit unit : units)
		{
			members.addAll(unit.getUnitMembers());
		}
		return members;
	}
	
	/**
	 * Method getOnlineMembers.
	 * @param exclude int
	 * @return List<Player>
	 */
	public List<Player> getOnlineMembers(int exclude)
	{
		final List<Player> result = new ArrayList<>(getAllSize() - 1);
		for (final UnitMember temp : this)
		{
			if ((temp != null) && temp.isOnline() && (temp.getObjectId() != exclude))
			{
				result.add(temp.getPlayer());
			}
		}
		return result;
	}
	
	/**
	 * Method getAllyId.
	 * @return int
	 */
	public int getAllyId()
	{
		return _allyId;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method getCastle.
	 * @return int
	 */
	public int getCastle()
	{
		return _hasCastle;
	}
	
	/**
	 * Method getHasFortress.
	 * @return int
	 */
	public int getHasFortress()
	{
		return _hasFortress;
	}
	
	/**
	 * Method getHasHideout.
	 * @return int
	 */
	public int getHasHideout()
	{
		return _hasHideout;
	}
	
	/**
	 * Method getResidenceId.
	 * @param r ResidenceType
	 * @return int
	 */
	public int getResidenceId(ResidenceType r)
	{
		switch (r)
		{
			case Castle:
				return _hasCastle;
			case Fortress:
				return _hasFortress;
			case ClanHall:
				return _hasHideout;
			default:
				return 0;
		}
	}
	
	/**
	 * Method setAllyId.
	 * @param allyId int
	 */
	public void setAllyId(int allyId)
	{
		_allyId = allyId;
	}
	
	/**
	 * Method setHasCastle.
	 * @param castle int
	 */
	public void setHasCastle(int castle)
	{
		if (_hasFortress == 0)
		{
			_hasCastle = castle;
		}
	}
	
	/**
	 * Method setHasFortress.
	 * @param fortress int
	 */
	public void setHasFortress(int fortress)
	{
		if (_hasCastle == 0)
		{
			_hasFortress = fortress;
		}
	}
	
	/**
	 * Method setHasHideout.
	 * @param hasHideout int
	 */
	public void setHasHideout(int hasHideout)
	{
		_hasHideout = hasHideout;
	}
	
	/**
	 * Method setLevel.
	 * @param level int
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	
	/**
	 * Method isAnyMember.
	 * @param id int
	 * @return boolean
	 */
	public boolean isAnyMember(int id)
	{
		for (SubUnit unit : getAllSubUnits())
		{
			if (unit.isUnitMember(id))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method updateClanInDB.
	 */
	public void updateClanInDB()
	{
		if (getLeaderId() == 0)
		{
			_log.warn("updateClanInDB with empty LeaderId");
			Thread.dumpStack();
			return;
		}
		if (getClanId() == 0)
		{
			_log.warn("updateClanInDB with empty ClanId");
			Thread.dumpStack();
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET ally_id=?,reputation_score=?,expelled_member=?,leaved_ally=?,dissolved_ally=?,clan_level=?,warehouse=?,airship=? WHERE clan_id=?");
			statement.setInt(1, getAllyId());
			statement.setInt(2, getReputationScore());
			statement.setLong(3, getExpelledMemberTime() / 1000);
			statement.setLong(4, getLeavedAllyTime() / 1000);
			statement.setLong(5, getDissolvedAllyTime() / 1000);
			statement.setInt(6, _level);
			statement.setInt(7, getWhBonus());
			statement.setInt(8, isHaveAirshipLicense() ? getAirshipFuel() : -1);
			statement.setInt(9, getClanId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("error while updating clan '" + getClanId() + "' data in db");
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method store.
	 */
	public void store()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO clan_data (clan_id,clan_level,hasCastle,hasFortress,hasHideout,ally_id,expelled_member,leaved_ally,dissolved_ally,airship) values (?,?,?,?,?,?,?,?,?,?)");
			statement.setInt(1, _clanId);
			statement.setInt(2, _level);
			statement.setInt(3, _hasCastle);
			statement.setInt(4, _hasFortress);
			statement.setInt(5, _hasHideout);
			statement.setInt(6, _allyId);
			statement.setLong(7, getExpelledMemberTime() / 1000);
			statement.setLong(8, getLeavedAllyTime() / 1000);
			statement.setLong(9, getDissolvedAllyTime() / 1000);
			statement.setInt(10, isHaveAirshipLicense() ? getAirshipFuel() : -1);
			statement.execute();
			DbUtils.close(statement);
			SubUnit mainSubUnit = _subUnits.get(SUBUNIT_MAIN_CLAN);
			statement = con.prepareStatement("INSERT INTO clan_subpledges (clan_id, type, leader_id, name) VALUES (?,?,?,?)");
			statement.setInt(1, _clanId);
			statement.setInt(2, mainSubUnit.getType());
			statement.setInt(3, mainSubUnit.getLeaderObjectId());
			statement.setString(4, mainSubUnit.getName());
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE characters SET clanid=?,pledge_type=? WHERE obj_Id=?");
			statement.setInt(1, getClanId());
			statement.setInt(2, mainSubUnit.getType());
			statement.setInt(3, getLeaderId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("Exception: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restore.
	 * @param clanId int
	 * @return Clan
	 */
	public static Clan restore(int clanId)
	{
		if (clanId == 0)
		{
			return null;
		}
		Clan clan = null;
		Connection con1 = null;
		PreparedStatement statement1 = null;
		ResultSet clanData = null;
		try
		{
			con1 = DatabaseFactory.getInstance().getConnection();
			statement1 = con1.prepareStatement("SELECT clan_level,hasCastle,hasFortress,hasHideout,ally_id,reputation_score,expelled_member,leaved_ally,dissolved_ally,warehouse,airship FROM clan_data where clan_id=?");
			statement1.setInt(1, clanId);
			clanData = statement1.executeQuery();
			if (clanData.next())
			{
				clan = new Clan(clanId);
				clan.setLevel(clanData.getInt("clan_level"));
				clan.setHasCastle(clanData.getInt("hasCastle"));
				clan.setHasFortress(clanData.getInt("hasFortress"));
				clan.setHasHideout(clanData.getInt("hasHideout"));
				clan.setAllyId(clanData.getInt("ally_id"));
				clan._reputation = clanData.getInt("reputation_score");
				clan.setExpelledMemberTime(clanData.getLong("expelled_member") * 1000L);
				clan.setLeavedAllyTime(clanData.getLong("leaved_ally") * 1000L);
				clan.setDissolvedAllyTime(clanData.getLong("dissolved_ally") * 1000L);
				clan.setWhBonus(clanData.getInt("warehouse"));
				clan.setAirshipLicense(clanData.getInt("airship") != -1);
				if (clan.isHaveAirshipLicense())
				{
					clan.setAirshipFuel(clanData.getInt("airship"));
				}
			}
			else
			{
				_log.warn("Clan " + clanId + " doesnt exists!");
				return null;
			}
		}
		catch (Exception e)
		{
			_log.error("Error while restoring clan!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con1, statement1, clanData);
		}
		if (clan == null)
		{
			_log.warn("Clan " + clanId + " does't exist");
			return null;
		}
		clan.restoreSkills();
		clan.restoreSubPledges();
		for (SubUnit unit : clan.getAllSubUnits())
		{
			unit.restore();
			unit.restoreSkills();
		}
		clan.restoreRankPrivs();
		clan.setCrestId(CrestCache.getInstance().getPledgeCrestId(clanId));
		clan.setCrestLargeId(CrestCache.getInstance().getPledgeCrestLargeId(clanId));
		return clan;
	}
	
	/**
	 * Method broadcastToOnlineMembers.
	 * @param packets IStaticPacket[]
	 */
	public void broadcastToOnlineMembers(IStaticPacket... packets)
	{
		for (UnitMember member : this)
		{
			if (member.isOnline())
			{
				member.getPlayer().sendPacket(packets);
			}
		}
	}
	
	/**
	 * Method broadcastToOnlineMembers.
	 * @param packets L2GameServerPacket[]
	 */
	public void broadcastToOnlineMembers(L2GameServerPacket... packets)
	{
		for (UnitMember member : this)
		{
			if (member.isOnline())
			{
				member.getPlayer().sendPacket(packets);
			}
		}
	}
	
	/**
	 * Method broadcastToOtherOnlineMembers.
	 * @param packet L2GameServerPacket
	 * @param player Player
	 */
	public void broadcastToOtherOnlineMembers(L2GameServerPacket packet, Player player)
	{
		for (UnitMember member : this)
		{
			if (member.isOnline() && (member.getPlayer() != player))
			{
				member.getPlayer().sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return getName();
	}
	
	/**
	 * Method setCrestId.
	 * @param newcrest int
	 */
	public void setCrestId(int newcrest)
	{
		_crestId = newcrest;
	}
	
	/**
	 * Method getCrestId.
	 * @return int
	 */
	public int getCrestId()
	{
		return _crestId;
	}
	
	/**
	 * Method hasCrest.
	 * @return boolean
	 */
	public boolean hasCrest()
	{
		return _crestId > 0;
	}
	
	/**
	 * Method getCrestLargeId.
	 * @return int
	 */
	public int getCrestLargeId()
	{
		return _crestLargeId;
	}
	
	/**
	 * Method setCrestLargeId.
	 * @param newcrest int
	 */
	public void setCrestLargeId(int newcrest)
	{
		_crestLargeId = newcrest;
	}
	
	/**
	 * Method hasCrestLarge.
	 * @return boolean
	 */
	public boolean hasCrestLarge()
	{
		return _crestLargeId > 0;
	}
	
	/**
	 * Method getAdenaCount.
	 * @return long
	 */
	public long getAdenaCount()
	{
		return _warehouse.getCountOfAdena();
	}
	
	/**
	 * Method getWarehouse.
	 * @return ClanWarehouse
	 */
	public ClanWarehouse getWarehouse()
	{
		return _warehouse;
	}
	
	/**
	 * Method isAtWar.
	 * @return int
	 */
	public int isAtWar()
	{
		if ((_atWarWith != null) && !_atWarWith.isEmpty())
		{
			return 1;
		}
		return 0;
	}
	
	/**
	 * Method isAtWarOrUnderAttack.
	 * @return int
	 */
	public int isAtWarOrUnderAttack()
	{
		if (((_atWarWith != null) && !_atWarWith.isEmpty()) || ((_underAttackFrom != null) && !_underAttackFrom.isEmpty()))
		{
			return 1;
		}
		return 0;
	}
	
	/**
	 * Method isAtWarWith.
	 * @param id int
	 * @return boolean
	 */
	public boolean isAtWarWith(int id)
	{
		Clan clan = ClanTable.getInstance().getClan(id);
		if ((_atWarWith != null) && !_atWarWith.isEmpty())
		{
			if (_atWarWith.contains(clan))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method isUnderAttackFrom.
	 * @param id int
	 * @return boolean
	 */
	public boolean isUnderAttackFrom(int id)
	{
		Clan clan = ClanTable.getInstance().getClan(id);
		if ((_underAttackFrom != null) && !_underAttackFrom.isEmpty())
		{
			if (_underAttackFrom.contains(clan))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method setEnemyClan.
	 * @param clan Clan
	 */
	public void setEnemyClan(Clan clan)
	{
		_atWarWith.add(clan);
	}
	
	/**
	 * Method deleteEnemyClan.
	 * @param clan Clan
	 */
	public void deleteEnemyClan(Clan clan)
	{
		_atWarWith.remove(clan);
	}
	
	/**
	 * Method setAttackerClan.
	 * @param clan Clan
	 */
	public void setAttackerClan(Clan clan)
	{
		_underAttackFrom.add(clan);
	}
	
	/**
	 * Method deleteAttackerClan.
	 * @param clan Clan
	 */
	public void deleteAttackerClan(Clan clan)
	{
		_underAttackFrom.remove(clan);
	}
	
	/**
	 * Method getEnemyClans.
	 * @return List<Clan>
	 */
	public List<Clan> getEnemyClans()
	{
		return _atWarWith;
	}
	
	/**
	 * Method getWarsCount.
	 * @return int
	 */
	public int getWarsCount()
	{
		return _atWarWith.size();
	}
	
	/**
	 * Method getAttackerClans.
	 * @return List<Clan>
	 */
	public List<Clan> getAttackerClans()
	{
		return _underAttackFrom;
	}
	
	/**
	 * Method broadcastClanStatus.
	 * @param updateList boolean
	 * @param needUserInfo boolean
	 * @param relation boolean
	 */
	public void broadcastClanStatus(boolean updateList, boolean needUserInfo, boolean relation)
	{
		List<L2GameServerPacket> listAll = updateList ? listAll() : null;
		PledgeShowInfoUpdate update = new PledgeShowInfoUpdate(this);
		for (UnitMember member : this)
		{
			if (member.isOnline())
			{
				if (updateList)
				{
					member.getPlayer().sendPacket(PledgeShowMemberListDeleteAll.STATIC);
					member.getPlayer().sendPacket(listAll);
				}
				member.getPlayer().sendPacket(update);
				if (needUserInfo)
				{
					member.getPlayer().broadcastCharInfo();
				}
				if (relation)
				{
					member.getPlayer().broadcastRelationChanged();
				}
			}
		}
	}
	
	/**
	 * Method getAlliance.
	 * @return Alliance
	 */
	public Alliance getAlliance()
	{
		return _allyId == 0 ? null : ClanTable.getInstance().getAlliance(_allyId);
	}
	
	/**
	 * Method setExpelledMemberTime.
	 * @param time long
	 */
	public void setExpelledMemberTime(long time)
	{
		_expelledMemberTime = time;
	}
	
	/**
	 * Method getExpelledMemberTime.
	 * @return long
	 */
	public long getExpelledMemberTime()
	{
		return _expelledMemberTime;
	}
	
	/**
	 * Method setExpelledMember.
	 */
	public void setExpelledMember()
	{
		_expelledMemberTime = System.currentTimeMillis();
		updateClanInDB();
	}
	
	/**
	 * Method setLeavedAllyTime.
	 * @param time long
	 */
	public void setLeavedAllyTime(long time)
	{
		_leavedAllyTime = time;
	}
	
	/**
	 * Method getLeavedAllyTime.
	 * @return long
	 */
	public long getLeavedAllyTime()
	{
		return _leavedAllyTime;
	}
	
	/**
	 * Method setLeavedAlly.
	 */
	public void setLeavedAlly()
	{
		_leavedAllyTime = System.currentTimeMillis();
		updateClanInDB();
	}
	
	/**
	 * Method setDissolvedAllyTime.
	 * @param time long
	 */
	public void setDissolvedAllyTime(long time)
	{
		_dissolvedAllyTime = time;
	}
	
	/**
	 * Method getDissolvedAllyTime.
	 * @return long
	 */
	public long getDissolvedAllyTime()
	{
		return _dissolvedAllyTime;
	}
	
	/**
	 * Method setDissolvedAlly.
	 */
	public void setDissolvedAlly()
	{
		_dissolvedAllyTime = System.currentTimeMillis();
		updateClanInDB();
	}
	
	/**
	 * Method canInvite.
	 * @return boolean
	 */
	public boolean canInvite()
	{
		return (System.currentTimeMillis() - _expelledMemberTime) >= EXPELLED_MEMBER_PENALTY;
	}
	
	/**
	 * Method canJoinAlly.
	 * @return boolean
	 */
	public boolean canJoinAlly()
	{
		return (System.currentTimeMillis() - _leavedAllyTime) >= LEAVED_ALLY_PENALTY;
	}
	
	/**
	 * Method canCreateAlly.
	 * @return boolean
	 */
	public boolean canCreateAlly()
	{
		return (System.currentTimeMillis() - _dissolvedAllyTime) >= DISSOLVED_ALLY_PENALTY;
	}
	
	/**
	 * Method getRank.
	 * @return int
	 */
	public int getRank()
	{
		Clan[] clans = ClanTable.getInstance().getClans();
		Arrays.sort(clans, REPUTATION_COMPARATOR);
		int place = 1;
		for (int i = 0; i < clans.length; i++)
		{
			if (i == REPUTATION_PLACES)
			{
				return 0;
			}
			Clan clan = clans[i];
			if (clan == this)
			{
				return place + i;
			}
		}
		return 0;
	}
	
	/**
	 * Method getReputationScore.
	 * @return int
	 */
	public int getReputationScore()
	{
		return _reputation;
	}
	
	/**
	 * Method setReputationScore.
	 * @param rep int
	 */
	private void setReputationScore(int rep)
	{
		if ((_reputation >= 0) && (rep < 0))
		{
			broadcastToOnlineMembers(Msg.SINCE_THE_CLAN_REPUTATION_SCORE_HAS_DROPPED_TO_0_OR_LOWER_YOUR_CLAN_SKILLS_WILL_BE_DE_ACTIVATED);
			for (UnitMember member : this)
			{
				if (member.isOnline() && (member.getPlayer() != null))
				{
					disableSkills(member.getPlayer());
				}
			}
		}
		else if ((_reputation < 0) && (rep >= 0))
		{
			broadcastToOnlineMembers(Msg.THE_CLAN_SKILL_WILL_BE_ACTIVATED_BECAUSE_THE_CLANS_REPUTATION_SCORE_HAS_REACHED_TO_0_OR_HIGHER);
			for (UnitMember member : this)
			{
				if (member.isOnline() && (member.getPlayer() != null))
				{
					enableSkills(member.getPlayer());
				}
			}
		}
		if (_reputation != rep)
		{
			_reputation = rep;
			broadcastToOnlineMembers(new PledgeShowInfoUpdate(this));
		}
		updateClanInDB();
	}
	
	/**
	 * Method incReputation.
	 * @param inc int
	 * @param rate boolean
	 * @param source String
	 * @return int
	 */
	public int incReputation(int inc, boolean rate, String source)
	{
		if (_level < 5)
		{
			return 0;
		}
		if (rate && (Math.abs(inc) <= Config.RATE_CLAN_REP_SCORE_MAX_AFFECTED))
		{
			inc = (int) Math.round(inc * Config.RATE_CLAN_REP_SCORE);
		}
		setReputationScore(_reputation + inc);
		Log.add(getName() + "|" + inc + "|" + _reputation + "|" + source, "clan_reputation");
		return inc;
	}
	
	/**
	 * Method restoreSkills.
	 */
	private void restoreSkills()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT skill_id,skill_level FROM clan_skills WHERE clan_id=?");
			statement.setInt(1, getClanId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int id = rset.getInt("skill_id");
				int level = rset.getInt("skill_level");
				Skill skill = SkillTable.getInstance().getInfo(id, level);
				_skills.put(skill.getId(), skill);
			}
		}
		catch (Exception e)
		{
			_log.warn("Could not restore clan skills: " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method getSkills.
	 * @return Collection<Skill>
	 */
	public Collection<Skill> getSkills()
	{
		return _skills.values();
	}
	
	/**
	 * Method getAllSkills.
	 * @return Skill[]
	 */
	public final Skill[] getAllSkills()
	{
		if (_reputation < 0)
		{
			return Skill.EMPTY_ARRAY;
		}
		return _skills.values().toArray(new Skill[_skills.values().size()]);
	}
	
	/**
	 * Method addSkill.
	 * @param newSkill Skill
	 * @param store boolean
	 * @return Skill
	 */
	public Skill addSkill(Skill newSkill, boolean store)
	{
		Skill oldSkill = null;
		if (newSkill != null)
		{
			oldSkill = _skills.put(newSkill.getId(), newSkill);
			if (store)
			{
				Connection con = null;
				PreparedStatement statement = null;
				try
				{
					con = DatabaseFactory.getInstance().getConnection();
					if (oldSkill != null)
					{
						statement = con.prepareStatement("UPDATE clan_skills SET skill_level=? WHERE skill_id=? AND clan_id=?");
						statement.setInt(1, newSkill.getLevel());
						statement.setInt(2, oldSkill.getId());
						statement.setInt(3, getClanId());
						statement.execute();
					}
					else
					{
						statement = con.prepareStatement("INSERT INTO clan_skills (clan_id,skill_id,skill_level) VALUES (?,?,?)");
						statement.setInt(1, getClanId());
						statement.setInt(2, newSkill.getId());
						statement.setInt(3, newSkill.getLevel());
						statement.execute();
					}
				}
				catch (Exception e)
				{
					_log.warn("Error could not store char skills: " + e);
					_log.error("", e);
				}
				finally
				{
					DbUtils.closeQuietly(con, statement);
				}
			}
			PledgeSkillListAdd p = new PledgeSkillListAdd(newSkill.getId(), newSkill.getLevel());
			PledgeSkillList p2 = new PledgeSkillList(this);
			for (UnitMember temp : this)
			{
				if (temp.isOnline())
				{
					Player player = temp.getPlayer();
					if (player != null)
					{
						addSkill(player, newSkill);
						player.sendPacket(p, p2);
						player.sendSkillList();
					}
				}
			}
		}
		return oldSkill;
	}
	
	/**
	 * Method addSkillsQuietly.
	 * @param player Player
	 */
	public void addSkillsQuietly(Player player)
	{
		for (Skill skill : _skills.values())
		{
			addSkill(player, skill);
		}
		final SubUnit subUnit = getSubUnit(player.getPledgeType());
		if (subUnit != null)
		{
			subUnit.addSkillsQuietly(player);
		}
	}
	
	/**
	 * Method enableSkills.
	 * @param player Player
	 */
	public void enableSkills(Player player)
	{
		if (player.isInOlympiadMode())
		{
			return;
		}
		for (Skill skill : _skills.values())
		{
			if (skill.getMinPledgeClass() <= player.getPledgeClass())
			{
				player.removeUnActiveSkill(skill);
			}
		}
		final SubUnit subUnit = getSubUnit(player.getPledgeType());
		if (subUnit != null)
		{
			subUnit.enableSkills(player);
		}
	}
	
	/**
	 * Method disableSkills.
	 * @param player Player
	 */
	public void disableSkills(Player player)
	{
		for (Skill skill : _skills.values())
		{
			player.addUnActiveSkill(skill);
		}
		final SubUnit subUnit = getSubUnit(player.getPledgeType());
		if (subUnit != null)
		{
			subUnit.disableSkills(player);
		}
	}
	
	/**
	 * Method addSkill.
	 * @param player Player
	 * @param skill Skill
	 */
	private void addSkill(Player player, Skill skill)
	{
		if (skill.getMinPledgeClass() <= player.getPledgeClass())
		{
			player.addSkill(skill, false);
			if ((_reputation < 0) || player.isInOlympiadMode())
			{
				player.addUnActiveSkill(skill);
			}
		}
	}
	
	/**
	 * Method removeSkill.
	 * @param skill int
	 */
	public void removeSkill(int skill)
	{
		_skills.remove(skill);
		new PledgeSkillListAdd(skill, 0);
		for (UnitMember temp : this)
		{
			Player player = temp.getPlayer();
			if ((player != null) && player.isOnline())
			{
				player.removeSkillById(skill);
				player.sendSkillList();
			}
		}
	}
	
	/**
	 * Method broadcastSkillListToOnlineMembers.
	 */
	public void broadcastSkillListToOnlineMembers()
	{
		for (UnitMember temp : this)
		{
			Player player = temp.getPlayer();
			if ((player != null) && player.isOnline())
			{
				player.sendPacket(new PledgeSkillList(this));
				player.sendSkillList();
			}
		}
	}
	
	/**
	 * Method isAcademy.
	 * @param pledgeType int
	 * @return boolean
	 */
	public static boolean isAcademy(int pledgeType)
	{
		return pledgeType == SUBUNIT_ACADEMY;
	}
	
	/**
	 * Method isRoyalGuard.
	 * @param pledgeType int
	 * @return boolean
	 */
	public static boolean isRoyalGuard(int pledgeType)
	{
		return (pledgeType == SUBUNIT_ROYAL1) || (pledgeType == SUBUNIT_ROYAL2);
	}
	
	/**
	 * Method isOrderOfKnights.
	 * @param pledgeType int
	 * @return boolean
	 */
	public static boolean isOrderOfKnights(int pledgeType)
	{
		return (pledgeType == SUBUNIT_KNIGHT1) || (pledgeType == SUBUNIT_KNIGHT2) || (pledgeType == SUBUNIT_KNIGHT3) || (pledgeType == SUBUNIT_KNIGHT4);
	}
	
	/**
	 * Method getAffiliationRank.
	 * @param pledgeType int
	 * @return int
	 */
	public int getAffiliationRank(int pledgeType)
	{
		if (isAcademy(pledgeType))
		{
			return 9;
		}
		else if (isOrderOfKnights(pledgeType))
		{
			return 8;
		}
		else if (isRoyalGuard(pledgeType))
		{
			return 7;
		}
		else
		{
			return 6;
		}
	}
	
	/**
	 * Method getSubUnit.
	 * @param pledgeType int
	 * @return SubUnit
	 */
	public final SubUnit getSubUnit(int pledgeType)
	{
		return _subUnits.get(pledgeType);
	}
	
	/**
	 * Method addSubUnit.
	 * @param sp SubUnit
	 * @param updateDb boolean
	 */
	public final void addSubUnit(SubUnit sp, boolean updateDb)
	{
		_subUnits.put(sp.getType(), sp);
		if (updateDb)
		{
			broadcastToOnlineMembers(new PledgeReceiveSubPledgeCreated(sp));
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("INSERT INTO `clan_subpledges` (clan_id,type,leader_id,name) VALUES (?,?,?,?)");
				statement.setInt(1, getClanId());
				statement.setInt(2, sp.getType());
				statement.setInt(3, sp.getLeaderObjectId());
				statement.setString(4, sp.getName());
				statement.execute();
			}
			catch (Exception e)
			{
				_log.warn("Could not store clan Sub pledges: " + e);
				_log.error("", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}
	
	/**
	 * Method createSubPledge.
	 * @param player Player
	 * @param pledgeType int
	 * @param leader UnitMember
	 * @param name String
	 * @return int
	 */
	public int createSubPledge(Player player, int pledgeType, UnitMember leader, String name)
	{
		int temp = pledgeType;
		pledgeType = getAvailablePledgeTypes(pledgeType);
		if (pledgeType == SUBUNIT_NONE)
		{
			if (temp == SUBUNIT_ACADEMY)
			{
				player.sendPacket(Msg.YOUR_CLAN_HAS_ALREADY_ESTABLISHED_A_CLAN_ACADEMY);
			}
			else
			{
				player.sendMessage("You can't create any more sub-units of this type");
			}
			return SUBUNIT_NONE;
		}
		switch (pledgeType)
		{
			case SUBUNIT_ACADEMY:
				break;
			case SUBUNIT_ROYAL1:
			case SUBUNIT_ROYAL2:
				if (getReputationScore() < 5000)
				{
					player.sendPacket(Msg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
					return SUBUNIT_NONE;
				}
				incReputation(-5000, false, "SubunitCreate");
				break;
			case SUBUNIT_KNIGHT1:
			case SUBUNIT_KNIGHT2:
			case SUBUNIT_KNIGHT3:
			case SUBUNIT_KNIGHT4:
				if (getReputationScore() < 10000)
				{
					player.sendPacket(Msg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
					return SUBUNIT_NONE;
				}
				incReputation(-10000, false, "SubunitCreate");
				break;
		}
		addSubUnit(new SubUnit(this, pledgeType, leader, name), true);
		return pledgeType;
	}
	
	/**
	 * Method getAvailablePledgeTypes.
	 * @param pledgeType int
	 * @return int
	 */
	public int getAvailablePledgeTypes(int pledgeType)
	{
		if (pledgeType == SUBUNIT_MAIN_CLAN)
		{
			return SUBUNIT_NONE;
		}
		if (_subUnits.get(pledgeType) != null)
		{
			switch (pledgeType)
			{
				case SUBUNIT_ACADEMY:
					return SUBUNIT_NONE;
				case SUBUNIT_ROYAL1:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_ROYAL2);
					break;
				case SUBUNIT_ROYAL2:
					return SUBUNIT_NONE;
				case SUBUNIT_KNIGHT1:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_KNIGHT2);
					break;
				case SUBUNIT_KNIGHT2:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_KNIGHT3);
					break;
				case SUBUNIT_KNIGHT3:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_KNIGHT4);
					break;
				case SUBUNIT_KNIGHT4:
					return SUBUNIT_NONE;
			}
		}
		return pledgeType;
	}
	
	/**
	 * Method restoreSubPledges.
	 */
	private void restoreSubPledges()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM clan_subpledges WHERE clan_id=?");
			statement.setInt(1, getClanId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int type = rset.getInt("type");
				int leaderId = rset.getInt("leader_id");
				String name = rset.getString("name");
				SubUnit pledge = new SubUnit(this, type, leaderId, name);
				addSubUnit(pledge, false);
			}
		}
		catch (Exception e)
		{
			_log.warn("Could not restore clan SubPledges: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method getSubPledgeLimit.
	 * @param pledgeType int
	 * @return int
	 */
	public int getSubPledgeLimit(int pledgeType)
	{
		int limit;
		switch (_level)
		{
			case 0:
				limit = 10;
				break;
			case 1:
				limit = 15;
				break;
			case 2:
				limit = 20;
				break;
			case 3:
				limit = 30;
				break;
			default:
				limit = 40;
				break;
		}
		switch (pledgeType)
		{
			case SUBUNIT_ACADEMY:
			case SUBUNIT_ROYAL1:
			case SUBUNIT_ROYAL2:
				if (getLevel() >= 11)
				{
					limit = 30;
				}
				else
				{
					limit = 20;
				}
				break;
			case SUBUNIT_KNIGHT1:
			case SUBUNIT_KNIGHT2:
				if (getLevel() >= 9)
				{
					limit = 25;
				}
				else
				{
					limit = 10;
				}
				break;
			case SUBUNIT_KNIGHT3:
			case SUBUNIT_KNIGHT4:
				if (getLevel() >= 10)
				{
					limit = 25;
				}
				else
				{
					limit = 10;
				}
				break;
		}
		return limit;
	}
	
	/**
	 * Method getUnitMembersSize.
	 * @param pledgeType int
	 * @return int
	 */
	public int getUnitMembersSize(int pledgeType)
	{
		if ((pledgeType == Clan.SUBUNIT_NONE) || !_subUnits.containsKey(pledgeType))
		{
			return 0;
		}
		return getSubUnit(pledgeType).size();
	}
	
	/**
	 * Method restoreRankPrivs.
	 */
	private void restoreRankPrivs()
	{
		if (_privs == null)
		{
			InitializePrivs();
		}
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT privilleges,rank FROM clan_privs WHERE clan_id=?");
			statement.setInt(1, getClanId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int rank = rset.getInt("rank");
				int privileges = rset.getInt("privilleges");
				RankPrivs p = _privs.get(rank);
				if (p != null)
				{
					p.setPrivs(privileges);
				}
				else
				{
					_log.warn("Invalid rank value (" + rank + "), please check clan_privs table");
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("Could not restore clan privs by rank: " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method InitializePrivs.
	 */
	public void InitializePrivs()
	{
		for (int i = RANK_FIRST; i <= RANK_LAST; i++)
		{
			_privs.put(i, new RankPrivs(i, 0, CP_NOTHING));
		}
	}
	
	/**
	 * Method updatePrivsForRank.
	 * @param rank int
	 */
	public void updatePrivsForRank(int rank)
	{
		for (UnitMember member : this)
		{
			if (member.isOnline() && (member.getPlayer() != null) && (member.getPlayer().getPowerGrade() == rank))
			{
				if (member.getPlayer().isClanLeader())
				{
					continue;
				}
				member.getPlayer().sendUserInfo();
			}
		}
	}
	
	/**
	 * Method getRankPrivs.
	 * @param rank int
	 * @return RankPrivs
	 */
	public RankPrivs getRankPrivs(int rank)
	{
		if ((rank < RANK_FIRST) || (rank > RANK_LAST))
		{
			_log.warn("Requested invalid rank value: " + rank);
			Thread.dumpStack();
			return null;
		}
		if (_privs.get(rank) == null)
		{
			_log.warn("Request of rank before init: " + rank);
			Thread.dumpStack();
			setRankPrivs(rank, CP_NOTHING);
		}
		return _privs.get(rank);
	}
	
	/**
	 * Method countMembersByRank.
	 * @param rank int
	 * @return int
	 */
	public int countMembersByRank(int rank)
	{
		int ret = 0;
		for (UnitMember m : this)
		{
			if (m.getPowerGrade() == rank)
			{
				ret++;
			}
		}
		return ret;
	}
	
	/**
	 * Method setRankPrivs.
	 * @param rank int
	 * @param privs int
	 */
	public void setRankPrivs(int rank, int privs)
	{
		if ((rank < RANK_FIRST) || (rank > RANK_LAST))
		{
			_log.warn("Requested set of invalid rank value: " + rank);
			Thread.dumpStack();
			return;
		}
		if (_privs.get(rank) != null)
		{
			_privs.get(rank).setPrivs(privs);
		}
		else
		{
			_privs.put(rank, new RankPrivs(rank, countMembersByRank(rank), privs));
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO clan_privs (clan_id,rank,privilleges) VALUES (?,?,?)");
			statement.setInt(1, getClanId());
			statement.setInt(2, rank);
			statement.setInt(3, privs);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("Could not store clan privs for rank: " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getAllRankPrivs.
	 * @return RankPrivs[]
	 */
	public final RankPrivs[] getAllRankPrivs()
	{
		if (_privs == null)
		{
			return new RankPrivs[0];
		}
		return _privs.values().toArray(new RankPrivs[_privs.values().size()]);
	}
	
	/**
	 * @author Mobius
	 */
	private static class ClanReputationComparator implements Comparator<Clan>
	{
		/**
		 * Constructor for ClanReputationComparator.
		 */
		public ClanReputationComparator()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method compare.
		 * @param o1 Clan
		 * @param o2 Clan
		 * @return int
		 */
		@Override
		public int compare(Clan o1, Clan o2)
		{
			if ((o1 == null) || (o2 == null))
			{
				return 0;
			}
			return o2.getReputationScore() - o1.getReputationScore();
		}
	}
	
	/**
	 * Method getWhBonus.
	 * @return int
	 */
	public int getWhBonus()
	{
		return _whBonus;
	}
	
	/**
	 * Method setWhBonus.
	 * @param i int
	 */
	public void setWhBonus(int i)
	{
		if (_whBonus != -1)
		{
			mysql.set("UPDATE `clan_data` SET `warehouse`=? WHERE `clan_id`=?", i, getClanId());
		}
		_whBonus = i;
	}
	
	/**
	 * Method setAirshipLicense.
	 * @param val boolean
	 */
	public void setAirshipLicense(boolean val)
	{
		_airshipLicense = val;
	}
	
	/**
	 * Method isHaveAirshipLicense.
	 * @return boolean
	 */
	public boolean isHaveAirshipLicense()
	{
		return _airshipLicense;
	}
	
	/**
	 * Method getAirship.
	 * @return ClanAirShip
	 */
	public ClanAirShip getAirship()
	{
		return _airship;
	}
	
	/**
	 * Method setAirship.
	 * @param airship ClanAirShip
	 */
	public void setAirship(ClanAirShip airship)
	{
		_airship = airship;
	}
	
	/**
	 * Method getAirshipFuel.
	 * @return int
	 */
	public int getAirshipFuel()
	{
		return _airshipFuel;
	}
	
	/**
	 * Method setAirshipFuel.
	 * @param fuel int
	 */
	public void setAirshipFuel(int fuel)
	{
		_airshipFuel = fuel;
	}
	
	/**
	 * Method getAllSubUnits.
	 * @return Collection<SubUnit>
	 */
	public final Collection<SubUnit> getAllSubUnits()
	{
		return _subUnits.values();
	}
	
	/**
	 * Method listAll.
	 * @return List<L2GameServerPacket>
	 */
	public List<L2GameServerPacket> listAll()
	{
		List<L2GameServerPacket> p = new ArrayList<>(_subUnits.size());
		for (SubUnit unit : getAllSubUnits())
		{
			p.add(new PledgeShowMemberListAll(this, unit));
		}
		return p;
	}
	
	/**
	 * Method getNotice.
	 * @return String
	 */
	public String getNotice()
	{
		return _notice;
	}
	
	/**
	 * Method setNotice.
	 * @param notice String
	 */
	public void setNotice(String notice)
	{
		_notice = notice;
	}
	
	/**
	 * Method getSkillLevel.
	 * @param id int
	 * @param def int
	 * @return int
	 */
	public int getSkillLevel(int id, int def)
	{
		Skill skill = _skills.get(id);
		return skill == null ? def : skill.getLevel();
	}
	
	/**
	 * Method getSkillLevel.
	 * @param id int
	 * @return int
	 */
	public int getSkillLevel(int id)
	{
		return getSkillLevel(id, -1);
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<UnitMember> * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<UnitMember> iterator()
	{
		List<Iterator<UnitMember>> iterators = new ArrayList<>(_subUnits.size());
		for (SubUnit subUnit : _subUnits.values())
		{
			iterators.add(subUnit.getUnitMembers().iterator());
		}
		return new JoinedIterator<>(iterators);
	}
	
	/**
	 * Method startNotifyClanEnterWorld.
	 * @param activeChar Player
	 */
	public void startNotifyClanEnterWorld(Player activeChar)
	{
		if (activeChar.isClanLeader())
		{
			_clanLeaderSkill = SkillTable.getInstance().getInfo(19009, 1);
			for (Player member : getOnlineMembers(0))
			{
				for (EffectTemplate et : _clanLeaderSkill.getEffectTemplates())
				{
					Effect effect = et.getEffect(new Env(member, member, _clanLeaderSkill));
					if (effect != null)
					{
						member.getEffectList().addEffect(effect);
					}
				}		
			}
		}
		else if (getLeader().isOnline())
		{
			_clanLeaderSkill = SkillTable.getInstance().getInfo(19009, 1);
			for (EffectTemplate et : _clanLeaderSkill.getEffectTemplates())
			{
				Effect effect = et.getEffect(new Env(activeChar, activeChar, _clanLeaderSkill));
				if (effect != null)
				{
					activeChar.getEffectList().addEffect(effect);
				}
			}		
		}
	}

	/**
	 * Method startNotifyClanLogOut.
	 * @param activeChar Player
	 */
	public void startNotifyClanLogOut(Player activeChar)
	{
		if (activeChar.isClanLeader())
		{
			_clanLeaderSkill = SkillTable.getInstance().getInfo(19009, 1);
			for (Player member : getOnlineMembers(0))
			{
				member.getEffectList().stopEffect(_clanLeaderSkill);
			}
		}
	}
}

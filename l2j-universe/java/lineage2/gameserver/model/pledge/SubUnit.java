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
import java.util.Collection;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.ExSubPledgeSkillAdd;
import lineage2.gameserver.tables.SkillTable;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SubUnit
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SubUnit.class);
	/**
	 * Field _skills.
	 */
	private final IntObjectMap<Skill> _skills = new CTreeIntObjectMap<>();
	/**
	 * Field _members.
	 */
	private final IntObjectMap<UnitMember> _members = new CHashIntObjectMap<>();
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _leaderObjectId.
	 */
	private int _leaderObjectId;
	/**
	 * Field _leader.
	 */
	private UnitMember _leader;
	/**
	 * Field _name.
	 */
	private String _name;
	/**
	 * Field _clan.
	 */
	private final Clan _clan;
	
	/**
	 * Constructor for SubUnit.
	 * @param c Clan
	 * @param type int
	 * @param leader UnitMember
	 * @param name String
	 */
	public SubUnit(Clan c, int type, UnitMember leader, String name)
	{
		_clan = c;
		_type = type;
		_name = name;
		setLeader(leader, false);
	}
	
	/**
	 * Constructor for SubUnit.
	 * @param c Clan
	 * @param type int
	 * @param leader int
	 * @param name String
	 */
	public SubUnit(Clan c, int type, int leader, String name)
	{
		_clan = c;
		_type = type;
		_leaderObjectId = leader;
		_name = name;
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	public int getType()
	{
		return _type;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getLeader.
	 * @return UnitMember
	 */
	public UnitMember getLeader()
	{
		return _leader;
	}
	
	/**
	 * Method isUnitMember.
	 * @param obj int
	 * @return boolean
	 */
	public boolean isUnitMember(int obj)
	{
		return _members.containsKey(obj);
	}
	
	/**
	 * Method addUnitMember.
	 * @param member UnitMember
	 */
	public void addUnitMember(UnitMember member)
	{
		_members.put(member.getObjectId(), member);
	}
	
	/**
	 * Method getUnitMember.
	 * @param obj int
	 * @return UnitMember
	 */
	public UnitMember getUnitMember(int obj)
	{
		if (obj == 0)
		{
			return null;
		}
		return _members.get(obj);
	}
	
	/**
	 * Method getUnitMember.
	 * @param obj String
	 * @return UnitMember
	 */
	public UnitMember getUnitMember(String obj)
	{
		for (UnitMember m : getUnitMembers())
		{
			if (m.getName().equalsIgnoreCase(obj))
			{
				return m;
			}
		}
		return null;
	}
	
	/**
	 * Method removeUnitMember.
	 * @param objectId int
	 */
	public void removeUnitMember(int objectId)
	{
		UnitMember m = _members.remove(objectId);
		if (m == null)
		{
			return;
		}
		if (objectId == getLeaderObjectId())
		{
			setLeader(null, true);
		}
		if (m.hasSponsor())
		{
			_clan.getAnyMember(m.getSponsor()).setApprentice(0);
		}
		removeMemberInDatabase(m);
		m.setPlayerInstance(null, true);
	}
	
	/**
	 * Method replace.
	 * @param objectId int
	 * @param newUnitId int
	 */
	public void replace(int objectId, int newUnitId)
	{
		SubUnit newUnit = _clan.getSubUnit(newUnitId);
		if (newUnit == null)
		{
			return;
		}
		UnitMember m = _members.remove(objectId);
		if (m == null)
		{
			return;
		}
		m.setPledgeType(newUnitId);
		newUnit.addUnitMember(m);
		if (m.getPowerGrade() > 5)
		{
			m.setPowerGrade(_clan.getAffiliationRank(m.getPledgeType()));
		}
	}
	
	/**
	 * Method getLeaderObjectId.
	 * @return int
	 */
	public int getLeaderObjectId()
	{
		return _leader == null ? 0 : _leader.getObjectId();
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public int size()
	{
		return _members.size();
	}
	
	/**
	 * Method getUnitMembers.
	 * @return Collection<UnitMember>
	 */
	public Collection<UnitMember> getUnitMembers()
	{
		return _members.values();
	}
	
	/**
	 * Method setLeader.
	 * @param newLeader UnitMember
	 * @param updateDB boolean
	 */
	public void setLeader(UnitMember newLeader, boolean updateDB)
	{
		final UnitMember old = _leader;
		if (old != null)
		{
			old.setLeaderOf(Clan.SUBUNIT_NONE);
		}
		_leader = newLeader;
		_leaderObjectId = newLeader == null ? 0 : newLeader.getObjectId();
		if (newLeader != null)
		{
			newLeader.setLeaderOf(_type);
		}
		if (updateDB)
		{
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("UPDATE clan_subpledges SET leader_id=? WHERE clan_id=? and type=?");
				statement.setInt(1, getLeaderObjectId());
				statement.setInt(2, _clan.getClanId());
				statement.setInt(3, _type);
				statement.execute();
			}
			catch (Exception e)
			{
				_log.error("Exception: " + e, e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}
	
	/**
	 * Method setName.
	 * @param name String
	 * @param updateDB boolean
	 */
	public void setName(String name, boolean updateDB)
	{
		_name = name;
		if (updateDB)
		{
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("UPDATE clan_subpledges SET name=? WHERE clan_id=? and type=?");
				statement.setString(1, _name);
				statement.setInt(2, _clan.getClanId());
				statement.setInt(3, _type);
				statement.execute();
			}
			catch (Exception e)
			{
				_log.error("Exception: " + e, e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}
	
	/**
	 * Method getLeaderName.
	 * @return String
	 */
	public String getLeaderName()
	{
		return _leader == null ? StringUtils.EMPTY : _leader.getName();
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
						statement = con.prepareStatement("UPDATE clan_subpledges_skills SET skill_level=? WHERE skill_id=? AND clan_id=? AND type=?");
						statement.setInt(1, newSkill.getLevel());
						statement.setInt(2, oldSkill.getId());
						statement.setInt(3, _clan.getClanId());
						statement.setInt(4, _type);
						statement.execute();
					}
					else
					{
						statement = con.prepareStatement("INSERT INTO clan_subpledges_skills (clan_id,type,skill_id,skill_level) VALUES (?,?,?,?)");
						statement.setInt(1, _clan.getClanId());
						statement.setInt(2, _type);
						statement.setInt(3, newSkill.getId());
						statement.setInt(4, newSkill.getLevel());
						statement.execute();
					}
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
			ExSubPledgeSkillAdd packet = new ExSubPledgeSkillAdd(_type, newSkill.getId(), newSkill.getLevel());
			for (UnitMember temp : _clan)
			{
				if (temp.isOnline())
				{
					Player player = temp.getPlayer();
					if (player != null)
					{
						player.sendPacket(packet);
						if (player.getPledgeType() == _type)
						{
							addSkill(player, newSkill);
						}
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
	}
	
	/**
	 * Method enableSkills.
	 * @param player Player
	 */
	public void enableSkills(Player player)
	{
		for (Skill skill : _skills.values())
		{
			if (skill.getMinRank() <= player.getPledgeClass())
			{
				player.removeUnActiveSkill(skill);
			}
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
	}
	
	/**
	 * Method addSkill.
	 * @param player Player
	 * @param skill Skill
	 */
	private void addSkill(Player player, Skill skill)
	{
		if (skill.getMinRank() <= player.getPledgeClass())
		{
			player.addSkill(skill, false);
			if ((_clan.getReputationScore() < 0) || player.isInOlympiadMode())
			{
				player.addUnActiveSkill(skill);
			}
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
	 * Method removeMemberInDatabase.
	 * @param member UnitMember
	 */
	private static void removeMemberInDatabase(UnitMember member)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET clanid=0, pledge_type=?, pledge_rank=0, lvl_joined_academy=0, apprentice=0, title='', leaveclan=? WHERE obj_Id=?");
			statement.setInt(1, Clan.SUBUNIT_NONE);
			statement.setLong(2, System.currentTimeMillis() / 1000);
			statement.setInt(3, member.getObjectId());
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
	 */
	public void restore()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `c`.`char_name` AS `char_name`," + "`s`.`level` AS `level`," + "`s`.`class_id` AS `classid`," + "`c`.`obj_Id` AS `obj_id`," + "`c`.`title` AS `title`," + "`c`.`pledge_rank` AS `pledge_rank`," + "`c`.`apprentice` AS `apprentice`, " + "`c`.`sex` AS `sex` " + "FROM `characters` `c` " + "LEFT JOIN `character_subclasses` `s` ON (`s`.`char_obj_id` = `c`.`obj_Id` AND `s`.`type` = '1') " + "WHERE `c`.`clanid`=? AND `c`.`pledge_type`=? ORDER BY `c`.`lastaccess` DESC");
			statement.setInt(1, _clan.getClanId());
			statement.setInt(2, _type);
			rset = statement.executeQuery();
			while (rset.next())
			{
				UnitMember member = new UnitMember(_clan, rset.getString("char_name"), rset.getString("title"), rset.getInt("level"), rset.getInt("classid"), rset.getInt("obj_Id"), _type, rset.getInt("pledge_rank"), rset.getInt("apprentice"), rset.getInt("sex"), Clan.SUBUNIT_NONE);
				addUnitMember(member);
			}
			if (_type != Clan.SUBUNIT_ACADEMY)
			{
				SubUnit mainClan = _clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
				UnitMember leader = mainClan.getUnitMember(_leaderObjectId);
				if (leader != null)
				{
					setLeader(leader, false);
				}
				else if (_type == Clan.SUBUNIT_MAIN_CLAN)
				{
					_log.error("Clan " + _name + " have no leader!");
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("Error while restoring clan members for clan: " + _clan.getClanId() + " " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method restoreSkills.
	 */
	public void restoreSkills()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT skill_id,skill_level FROM clan_subpledges_skills WHERE clan_id=? AND type=?");
			statement.setInt(1, _clan.getClanId());
			statement.setInt(2, _type);
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
			_log.warn("Exception: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
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
}

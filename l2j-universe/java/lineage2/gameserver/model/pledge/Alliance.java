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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.tables.ClanTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Alliance
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Alliance.class);
	/**
	 * Field _allyName.
	 */
	private String _allyName;
	/**
	 * Field _allyId.
	 */
	private int _allyId;
	/**
	 * Field _leader.
	 */
	private Clan _leader = null;
	/**
	 * Field _members.
	 */
	private final Map<Integer, Clan> _members = new ConcurrentHashMap<>();
	/**
	 * Field _allyCrestId.
	 */
	private int _allyCrestId;
	/**
	 * Field _expelledMemberTime.
	 */
	private long _expelledMemberTime;
	/**
	 * Field EXPELLED_MEMBER_PENALTY.
	 */
	public static long EXPELLED_MEMBER_PENALTY = 24 * 60 * 60 * 1000L;
	
	/**
	 * Constructor for Alliance.
	 * @param allyId int
	 */
	public Alliance(int allyId)
	{
		_allyId = allyId;
		restore();
	}
	
	/**
	 * Constructor for Alliance.
	 * @param allyId int
	 * @param allyName String
	 * @param leader Clan
	 */
	public Alliance(int allyId, String allyName, Clan leader)
	{
		_allyId = allyId;
		_allyName = allyName;
		setLeader(leader);
	}
	
	/**
	 * Method getLeaderId.
	 * @return int
	 */
	public int getLeaderId()
	{
		return _leader != null ? _leader.getClanId() : 0;
	}
	
	/**
	 * Method getLeader.
	 * @return Clan
	 */
	public Clan getLeader()
	{
		return _leader;
	}
	
	/**
	 * Method setLeader.
	 * @param leader Clan
	 */
	public void setLeader(Clan leader)
	{
		_leader = leader;
		_members.put(leader.getClanId(), leader);
	}
	
	/**
	 * Method getAllyLeaderName.
	 * @return String
	 */
	public String getAllyLeaderName()
	{
		return _leader != null ? _leader.getLeaderName() : "";
	}
	
	/**
	 * Method addAllyMember.
	 * @param member Clan
	 * @param storeInDb boolean
	 */
	public void addAllyMember(Clan member, boolean storeInDb)
	{
		_members.put(member.getClanId(), member);
		if (storeInDb)
		{
			storeNewMemberInDatabase(member);
		}
	}
	
	/**
	 * Method getAllyMember.
	 * @param id int
	 * @return Clan
	 */
	public Clan getAllyMember(int id)
	{
		return _members.get(id);
	}
	
	/**
	 * Method removeAllyMember.
	 * @param id int
	 */
	public void removeAllyMember(int id)
	{
		if ((_leader != null) && (_leader.getClanId() == id))
		{
			return;
		}
		Clan exMember = _members.remove(id);
		if (exMember == null)
		{
			_log.warn("Clan " + id + " not found in alliance while trying to remove");
			return;
		}
		removeMemberInDatabase(exMember);
	}
	
	/**
	 * Method getMembers.
	 * @return Clan[]
	 */
	public Clan[] getMembers()
	{
		return _members.values().toArray(new Clan[_members.size()]);
	}
	
	/**
	 * Method getMembersCount.
	 * @return int
	 */
	public int getMembersCount()
	{
		return _members.size();
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
	 * Method getAllyName.
	 * @return String
	 */
	public String getAllyName()
	{
		return _allyName;
	}
	
	/**
	 * Method setAllyCrestId.
	 * @param allyCrestId int
	 */
	public void setAllyCrestId(int allyCrestId)
	{
		_allyCrestId = allyCrestId;
	}
	
	/**
	 * Method getAllyCrestId.
	 * @return int
	 */
	public int getAllyCrestId()
	{
		return _allyCrestId;
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
	 * Method setAllyName.
	 * @param allyName String
	 */
	public void setAllyName(String allyName)
	{
		_allyName = allyName;
	}
	
	/**
	 * Method isMember.
	 * @param id int
	 * @return boolean
	 */
	public boolean isMember(int id)
	{
		return _members.containsKey(id);
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
		updateAllyInDB();
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
	 * Method updateAllyInDB.
	 */
	public void updateAllyInDB()
	{
		if (getLeaderId() == 0)
		{
			_log.warn("updateAllyInDB with empty LeaderId");
			Thread.dumpStack();
			return;
		}
		if (getAllyId() == 0)
		{
			_log.warn("updateAllyInDB with empty AllyId");
			Thread.dumpStack();
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE ally_data SET leader_id=?,expelled_member=? WHERE ally_id=?");
			statement.setInt(1, getLeaderId());
			statement.setLong(2, getExpelledMemberTime() / 1000);
			statement.setInt(3, getAllyId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("error while updating ally '" + getAllyId() + "' data in db: " + e);
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
			statement = con.prepareStatement("INSERT INTO ally_data (ally_id,ally_name,leader_id) values (?,?,?)");
			statement.setInt(1, getAllyId());
			statement.setString(2, getAllyName());
			statement.setInt(3, getLeaderId());
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE clan_data SET ally_id=? WHERE clan_id=?");
			statement.setInt(1, getAllyId());
			statement.setInt(2, getLeaderId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("error while saving new ally to db " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method storeNewMemberInDatabase.
	 * @param member Clan
	 */
	private void storeNewMemberInDatabase(Clan member)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET ally_id=? WHERE clan_id=?");
			statement.setInt(1, getAllyId());
			statement.setInt(2, member.getClanId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("error while saving new alliance member to db " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method removeMemberInDatabase.
	 * @param member Clan
	 */
	private void removeMemberInDatabase(Clan member)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET ally_id=0 WHERE clan_id=?");
			statement.setInt(1, member.getClanId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("error while removing ally member in db " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restore.
	 */
	private void restore()
	{
		if (getAllyId() == 0)
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			Clan member;
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT ally_name,leader_id FROM ally_data where ally_id=?");
			statement.setInt(1, getAllyId());
			rset = statement.executeQuery();
			if (rset.next())
			{
				setAllyName(rset.getString("ally_name"));
				int leaderId = rset.getInt("leader_id");
				DbUtils.close(statement, rset);
				statement = con.prepareStatement("SELECT clan_id FROM clan_data WHERE ally_id=?");
				statement.setInt(1, getAllyId());
				rset = statement.executeQuery();
				while (rset.next())
				{
					member = ClanTable.getInstance().getClan(rset.getInt("clan_id"));
					if (member != null)
					{
						if (member.getClanId() == leaderId)
						{
							setLeader(member);
						}
						else
						{
							addAllyMember(member, false);
						}
					}
				}
			}
			setAllyCrestId(CrestCache.getInstance().getAllyCrestId(getAllyId()));
		}
		catch (Exception e)
		{
			_log.warn("error while restoring ally");
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method broadcastToOnlineMembers.
	 * @param packet L2GameServerPacket
	 */
	public void broadcastToOnlineMembers(L2GameServerPacket packet)
	{
		for (Clan member : _members.values())
		{
			if (member != null)
			{
				member.broadcastToOnlineMembers(packet);
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
		for (Clan member : _members.values())
		{
			if (member != null)
			{
				member.broadcastToOtherOnlineMembers(packet, player);
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
		return getAllyName();
	}
	
	/**
	 * Method hasAllyCrest.
	 * @return boolean
	 */
	public boolean hasAllyCrest()
	{
		return _allyCrestId > 0;
	}
	
	/**
	 * Method broadcastAllyStatus.
	 */
	public void broadcastAllyStatus()
	{
		for (Clan member : getMembers())
		{
			member.broadcastClanStatus(false, true, false);
		}
	}
}

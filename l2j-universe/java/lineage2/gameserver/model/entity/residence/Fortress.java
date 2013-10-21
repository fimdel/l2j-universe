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
package lineage2.gameserver.model.entity.residence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.dao.ClanDataDAO;
import lineage2.gameserver.dao.FortressDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Fortress extends Residence
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Fortress.class);
	/**
	 * Field REMOVE_CYCLE.
	 */
	private static final long REMOVE_CYCLE = 7 * 24;
	/**
	 * Field REWARD_CYCLE. (value is 6)
	 */
	private static final long REWARD_CYCLE = 6;
	/**
	 * Field CASTLE_FEE. (value is 25000)
	 */
	public static final long CASTLE_FEE = 25000;
	/**
	 * Field DOMAIN. (value is 0)
	 */
	public static final int DOMAIN = 0;
	/**
	 * Field BOUNDARY. (value is 1)
	 */
	public static final int BOUNDARY = 1;
	/**
	 * Field NOT_DECIDED. (value is 0)
	 */
	public static final int NOT_DECIDED = 0;
	/**
	 * Field INDEPENDENT. (value is 1)
	 */
	public static final int INDEPENDENT = 1;
	/**
	 * Field CONTRACT_WITH_CASTLE. (value is 2)
	 */
	public static final int CONTRACT_WITH_CASTLE = 2;
	/**
	 * Field REINFORCE. (value is 0)
	 */
	public static final int REINFORCE = 0;
	/**
	 * Field GUARD_BUFF. (value is 1)
	 */
	public static final int GUARD_BUFF = 1;
	/**
	 * Field DOOR_UPGRADE. (value is 2)
	 */
	public static final int DOOR_UPGRADE = 2;
	/**
	 * Field DWARVENS. (value is 3)
	 */
	public static final int DWARVENS = 3;
	/**
	 * Field SCOUT. (value is 4)
	 */
	public static final int SCOUT = 4;
	/**
	 * Field FACILITY_MAX. (value is 5)
	 */
	public static final int FACILITY_MAX = 5;
	/**
	 * Field _facilities.
	 */
	private final int[] _facilities = new int[FACILITY_MAX];
	/**
	 * Field _state.
	 */
	private int _state;
	/**
	 * Field _castleId.
	 */
	private int _castleId;
	/**
	 * Field _supplyCount.
	 */
	private int _supplyCount;
	/**
	 * Field _relatedCastles.
	 */
	private final List<Castle> _relatedCastles = new ArrayList<>(5);
	
	/**
	 * Constructor for Fortress.
	 * @param set StatsSet
	 */
	public Fortress(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method getType.
	 * @return ResidenceType
	 */
	@Override
	public ResidenceType getType()
	{
		return ResidenceType.Fortress;
	}
	
	/**
	 * Method changeOwner.
	 * @param clan Clan
	 */
	@Override
	public void changeOwner(Clan clan)
	{
		if (clan != null)
		{
			if (clan.getHasFortress() != 0)
			{
				Fortress oldFortress = ResidenceHolder.getInstance().getResidence(Fortress.class, clan.getHasFortress());
				if (oldFortress != null)
				{
					oldFortress.changeOwner(null);
				}
			}
			if (clan.getCastle() != 0)
			{
				Castle oldCastle = ResidenceHolder.getInstance().getResidence(Castle.class, clan.getCastle());
				if (oldCastle != null)
				{
					oldCastle.changeOwner(null);
				}
			}
		}
		if ((getOwnerId() > 0) && ((clan == null) || (clan.getClanId() != getOwnerId())))
		{
			removeSkills();
			Clan oldOwner = getOwner();
			if (oldOwner != null)
			{
				oldOwner.setHasFortress(0);
			}
			cancelCycleTask();
			clearFacility();
		}
		if (clan != null)
		{
			clan.setHasFortress(getId());
		}
		updateOwnerInDB(clan);
		rewardSkills();
		setFortState(NOT_DECIDED, 0);
		setJdbcState(JdbcEntityState.UPDATED);
		update();
	}
	
	/**
	 * Method loadData.
	 */
	@Override
	protected void loadData()
	{
		_owner = ClanDataDAO.getInstance().getOwner(this);
		FortressDAO.getInstance().select(this);
	}
	
	/**
	 * Method updateOwnerInDB.
	 * @param clan Clan
	 */
	private void updateOwnerInDB(Clan clan)
	{
		_owner = clan;
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET hasFortress=0 WHERE hasFortress=? LIMIT 1");
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			if (clan != null)
			{
				statement = con.prepareStatement("UPDATE clan_data SET hasFortress=? WHERE clan_id=? LIMIT 1");
				statement.setInt(1, getId());
				statement.setInt(2, getOwnerId());
				statement.execute();
				clan.broadcastClanStatus(true, false, false);
			}
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
	 * Method setFortState.
	 * @param state int
	 * @param castleId int
	 */
	public void setFortState(int state, int castleId)
	{
		_state = state;
		_castleId = castleId;
	}
	
	/**
	 * Method getCastleId.
	 * @return int
	 */
	public int getCastleId()
	{
		return _castleId;
	}
	
	/**
	 * Method getContractState.
	 * @return int
	 */
	public int getContractState()
	{
		return _state;
	}
	
	/**
	 * Method chanceCycle.
	 */
	@Override
	public void chanceCycle()
	{
		super.chanceCycle();
		if (getCycle() >= REMOVE_CYCLE)
		{
			getOwner().broadcastToOnlineMembers(SystemMsg.ENEMY_BLOOD_PLEDGES_HAVE_INTRUDED_INTO_THE_FORTRESS);
			changeOwner(null);
			return;
		}
		setPaidCycle(getPaidCycle() + 1);
		if ((getPaidCycle() % REWARD_CYCLE) == 0)
		{
			setPaidCycle(0);
			setRewardCount(getRewardCount() + 1);
			if (getContractState() == CONTRACT_WITH_CASTLE)
			{
				Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _castleId);
				if ((castle.getOwner() == null) || (castle.getOwner().getReputationScore() < 2) || (_owner.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) > CASTLE_FEE))
				{
					setSupplyCount(0);
					setFortState(INDEPENDENT, 0);
					clearFacility();
				}
				else
				{
					if (_supplyCount < 6)
					{
						castle.getOwner().incReputation(-2, false, "Fortress:chanceCycle():" + getId());
						_owner.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, CASTLE_FEE);
						_supplyCount++;
					}
				}
			}
		}
	}
	
	/**
	 * Method update.
	 * @see lineage2.commons.dao.JdbcEntity#update()
	 */
	@Override
	public void update()
	{
		FortressDAO.getInstance().update(this);
	}
	
	/**
	 * Method getSupplyCount.
	 * @return int
	 */
	public int getSupplyCount()
	{
		return _supplyCount;
	}
	
	/**
	 * Method setSupplyCount.
	 * @param c int
	 */
	public void setSupplyCount(int c)
	{
		_supplyCount = c;
	}
	
	/**
	 * Method getFacilityLevel.
	 * @param type int
	 * @return int
	 */
	public int getFacilityLevel(int type)
	{
		return _facilities[type];
	}
	
	/**
	 * Method setFacilityLevel.
	 * @param type int
	 * @param val int
	 */
	public void setFacilityLevel(int type, int val)
	{
		_facilities[type] = val;
	}
	
	/**
	 * Method clearFacility.
	 */
	public void clearFacility()
	{
		for (int i = 0; i < _facilities.length; i++)
		{
			_facilities[i] = 0;
		}
	}
	
	/**
	 * Method getFacilities.
	 * @return int[]
	 */
	public int[] getFacilities()
	{
		return _facilities;
	}
	
	/**
	 * Method addRelatedCastle.
	 * @param castle Castle
	 */
	public void addRelatedCastle(Castle castle)
	{
		_relatedCastles.add(castle);
	}
	
	/**
	 * Method getRelatedCastles.
	 * @return List<Castle>
	 */
	public List<Castle> getRelatedCastles()
	{
		return _relatedCastles;
	}
}

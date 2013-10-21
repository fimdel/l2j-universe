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

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.dao.ClanDataDAO;
import lineage2.gameserver.dao.ClanHallDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.PlayerMessageStack;
import lineage2.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanHall extends Residence
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ClanHall.class);
	/**
	 * Field REWARD_CYCLE. (value is 168)
	 */
	private static final int REWARD_CYCLE = 168;
	/**
	 * Field _auctionLength.
	 */
	private int _auctionLength;
	/**
	 * Field _auctionMinBid.
	 */
	private long _auctionMinBid;
	/**
	 * Field _auctionDescription.
	 */
	private String _auctionDescription = StringUtils.EMPTY;
	/**
	 * Field _grade.
	 */
	private final int _grade;
	/**
	 * Field _rentalFee.
	 */
	private final long _rentalFee;
	/**
	 * Field _minBid.
	 */
	private final long _minBid;
	/**
	 * Field _deposit.
	 */
	private final long _deposit;
	
	/**
	 * Constructor for ClanHall.
	 * @param set StatsSet
	 */
	public ClanHall(StatsSet set)
	{
		super(set);
		_grade = set.getInteger("grade", 0);
		_rentalFee = set.getInteger("rental_fee", 0);
		_minBid = set.getInteger("min_bid", 0);
		_deposit = set.getInteger("deposit", 0);
	}
	
	/**
	 * Method init.
	 */
	@Override
	public void init()
	{
		initZone();
		initEvent();
		loadData();
		loadFunctions();
		rewardSkills();
		if ((getSiegeEvent().getClass() == ClanHallAuctionEvent.class) && (_owner != null) && (getAuctionLength() == 0))
		{
			startCycleTask();
		}
	}
	
	/**
	 * Method changeOwner.
	 * @param clan Clan
	 */
	@Override
	public void changeOwner(Clan clan)
	{
		Clan oldOwner = getOwner();
		if ((oldOwner != null) && ((clan == null) || (clan.getClanId() != oldOwner.getClanId())))
		{
			removeSkills();
			oldOwner.setHasHideout(0);
			cancelCycleTask();
		}
		updateOwnerInDB(clan);
		rewardSkills();
		update();
		if ((clan == null) && (getSiegeEvent().getClass() == ClanHallAuctionEvent.class))
		{
			getSiegeEvent().reCalcNextTime(false);
		}
	}
	
	/**
	 * Method getType.
	 * @return ResidenceType
	 */
	@Override
	public ResidenceType getType()
	{
		return ResidenceType.ClanHall;
	}
	
	/**
	 * Method loadData.
	 */
	@Override
	protected void loadData()
	{
		_owner = ClanDataDAO.getInstance().getOwner(this);
		ClanHallDAO.getInstance().select(this);
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
			statement = con.prepareStatement("UPDATE clan_data SET hasHideout=0 WHERE hasHideout=?");
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE clan_data SET hasHideout=? WHERE clan_id=?");
			statement.setInt(1, getId());
			statement.setInt(2, getOwnerId());
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM residence_functions WHERE id=?");
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			if (clan != null)
			{
				clan.setHasHideout(getId());
				clan.broadcastClanStatus(false, true, false);
			}
		}
		catch (Exception e)
		{
			_log.warn("Exception: updateOwnerInDB(L2Clan clan): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getGrade.
	 * @return int
	 */
	public int getGrade()
	{
		return _grade;
	}
	
	/**
	 * Method update.
	 * @see lineage2.commons.dao.JdbcEntity#update()
	 */
	@Override
	public void update()
	{
		ClanHallDAO.getInstance().update(this);
	}
	
	/**
	 * Method getAuctionLength.
	 * @return int
	 */
	public int getAuctionLength()
	{
		return _auctionLength;
	}
	
	/**
	 * Method setAuctionLength.
	 * @param auctionLength int
	 */
	public void setAuctionLength(int auctionLength)
	{
		_auctionLength = auctionLength;
	}
	
	/**
	 * Method getAuctionDescription.
	 * @return String
	 */
	public String getAuctionDescription()
	{
		return _auctionDescription;
	}
	
	/**
	 * Method setAuctionDescription.
	 * @param auctionDescription String
	 */
	public void setAuctionDescription(String auctionDescription)
	{
		_auctionDescription = auctionDescription == null ? StringUtils.EMPTY : auctionDescription;
	}
	
	/**
	 * Method getAuctionMinBid.
	 * @return long
	 */
	public long getAuctionMinBid()
	{
		return _auctionMinBid;
	}
	
	/**
	 * Method setAuctionMinBid.
	 * @param auctionMinBid long
	 */
	public void setAuctionMinBid(long auctionMinBid)
	{
		_auctionMinBid = auctionMinBid;
	}
	
	/**
	 * Method getRentalFee.
	 * @return long
	 */
	public long getRentalFee()
	{
		return _rentalFee;
	}
	
	/**
	 * Method getBaseMinBid.
	 * @return long
	 */
	public long getBaseMinBid()
	{
		return _minBid;
	}
	
	/**
	 * Method getDeposit.
	 * @return long
	 */
	public long getDeposit()
	{
		return _deposit;
	}
	
	/**
	 * Method chanceCycle.
	 */
	@Override
	public void chanceCycle()
	{
		super.chanceCycle();
		setPaidCycle(getPaidCycle() + 1);
		if (getPaidCycle() >= REWARD_CYCLE)
		{
			if (_owner.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) > _rentalFee)
			{
				_owner.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, _rentalFee);
				setPaidCycle(0);
			}
			else
			{
				UnitMember member = _owner.getLeader();
				if (member.isOnline())
				{
					member.getPlayer().sendPacket(SystemMsg.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED);
				}
				else
				{
					PlayerMessageStack.getInstance().mailto(member.getObjectId(), SystemMsg.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED.packet(null));
				}
				changeOwner(null);
			}
		}
	}
}

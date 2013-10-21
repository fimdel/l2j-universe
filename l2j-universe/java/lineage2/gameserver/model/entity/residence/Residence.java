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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lineage2.commons.dao.JdbcEntity;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Residence implements JdbcEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	public class ResidenceCycleTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			chanceCycle();
			update();
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Residence.class);
	/**
	 * Field CYCLE_TIME.
	 */
	public static final long CYCLE_TIME = 60 * 60 * 1000L;
	/**
	 * Field _id.
	 */
	protected final int _id;
	/**
	 * Field _name.
	 */
	protected final String _name;
	/**
	 * Field _owner.
	 */
	protected Clan _owner;
	/**
	 * Field _zone.
	 */
	protected Zone _zone;
	/**
	 * Field _functions.
	 */
	protected List<ResidenceFunction> _functions = new ArrayList<>();
	/**
	 * Field _skills.
	 */
	protected List<Skill> _skills = new ArrayList<>();
	/**
	 * Field _siegeEvent.
	 */
	protected SiegeEvent<?, ?> _siegeEvent;
	/**
	 * Field _siegeDate.
	 */
	protected Calendar _siegeDate = Calendar.getInstance();
	/**
	 * Field _lastSiegeDate.
	 */
	protected Calendar _lastSiegeDate = Calendar.getInstance();
	/**
	 * Field _ownDate.
	 */
	protected Calendar _ownDate = Calendar.getInstance();
	/**
	 * Field _cycleTask.
	 */
	protected ScheduledFuture<?> _cycleTask;
	/**
	 * Field _cycle.
	 */
	private int _cycle;
	/**
	 * Field _rewardCount.
	 */
	private int _rewardCount;
	/**
	 * Field _paidCycle.
	 */
	private int _paidCycle;
	/**
	 * Field _jdbcEntityState.
	 */
	protected JdbcEntityState _jdbcEntityState = JdbcEntityState.CREATED;
	/**
	 * Field _banishPoints.
	 */
	protected List<Location> _banishPoints = new ArrayList<>();
	/**
	 * Field _ownerRestartPoints.
	 */
	protected List<Location> _ownerRestartPoints = new ArrayList<>();
	/**
	 * Field _otherRestartPoints.
	 */
	protected List<Location> _otherRestartPoints = new ArrayList<>();
	/**
	 * Field _chaosRestartPoints.
	 */
	protected List<Location> _chaosRestartPoints = new ArrayList<>();
	
	/**
	 * Constructor for Residence.
	 * @param set StatsSet
	 */
	public Residence(StatsSet set)
	{
		_id = set.getInteger("id");
		_name = set.getString("name");
	}
	
	/**
	 * Method getType.
	 * @return ResidenceType
	 */
	public abstract ResidenceType getType();
	
	/**
	 * Method init.
	 */
	public void init()
	{
		initZone();
		initEvent();
		loadData();
		loadFunctions();
		rewardSkills();
		startCycleTask();
	}
	
	/**
	 * Method initZone.
	 */
	protected void initZone()
	{
		_zone = ReflectionUtils.getZone("residence_" + _id);
		_zone.setParam("residence", this);
	}
	
	/**
	 * Method initEvent.
	 */
	protected void initEvent()
	{
		_siegeEvent = EventHolder.getInstance().getEvent(EventType.SIEGE_EVENT, _id);
	}
	
	/**
	 * Method getSiegeEvent.
	 * @return E
	 */
	@SuppressWarnings("unchecked")
	public <E extends SiegeEvent<?, ?>> E getSiegeEvent()
	{
		return (E) _siegeEvent;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
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
	 * Method getOwnerId.
	 * @return int
	 */
	public int getOwnerId()
	{
		return _owner == null ? 0 : _owner.getClanId();
	}
	
	/**
	 * Method getOwner.
	 * @return Clan
	 */
	public Clan getOwner()
	{
		return _owner;
	}
	
	/**
	 * Method getZone.
	 * @return Zone
	 */
	public Zone getZone()
	{
		return _zone;
	}
	
	/**
	 * Method loadData.
	 */
	protected abstract void loadData();
	
	/**
	 * Method changeOwner.
	 * @param clan Clan
	 */
	public abstract void changeOwner(Clan clan);
	
	/**
	 * Method getOwnDate.
	 * @return Calendar
	 */
	public Calendar getOwnDate()
	{
		return _ownDate;
	}
	
	/**
	 * Method getSiegeDate.
	 * @return Calendar
	 */
	public Calendar getSiegeDate()
	{
		return _siegeDate;
	}
	
	/**
	 * Method getLastSiegeDate.
	 * @return Calendar
	 */
	public Calendar getLastSiegeDate()
	{
		return _lastSiegeDate;
	}
	
	/**
	 * Method addSkill.
	 * @param skill Skill
	 */
	public void addSkill(Skill skill)
	{
		_skills.add(skill);
	}
	
	/**
	 * Method addFunction.
	 * @param function ResidenceFunction
	 */
	public void addFunction(ResidenceFunction function)
	{
		_functions.add(function);
	}
	
	/**
	 * Method checkIfInZone.
	 * @param loc Location
	 * @param ref Reflection
	 * @return boolean
	 */
	public boolean checkIfInZone(Location loc, Reflection ref)
	{
		return checkIfInZone(loc.x, loc.y, loc.z, ref);
	}
	
	/**
	 * Method checkIfInZone.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param ref Reflection
	 * @return boolean
	 */
	public boolean checkIfInZone(int x, int y, int z, Reflection ref)
	{
		return (getZone() != null) && getZone().checkIfInZone(x, y, z, ref);
	}
	
	/**
	 * Method banishForeigner.
	 */
	public void banishForeigner()
	{
		for (Player player : _zone.getInsidePlayers())
		{
			if (player.getClanId() == getOwnerId())
			{
				continue;
			}
			player.teleToLocation(getBanishPoint());
		}
	}
	
	/**
	 * Method rewardSkills.
	 */
	public void rewardSkills()
	{
		Clan owner = getOwner();
		if (owner != null)
		{
			for (Skill skill : _skills)
			{
				owner.addSkill(skill, false);
				owner.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skill));
			}
		}
	}
	
	/**
	 * Method removeSkills.
	 */
	public void removeSkills()
	{
		Clan owner = getOwner();
		if (owner != null)
		{
			for (Skill skill : _skills)
			{
				owner.removeSkill(skill.getId());
			}
		}
	}
	
	/**
	 * Method loadFunctions.
	 */
	protected void loadFunctions()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM residence_functions WHERE id = ?");
			statement.setInt(1, getId());
			rs = statement.executeQuery();
			while (rs.next())
			{
				ResidenceFunction function = getFunction(rs.getInt("type"));
				function.setLvl(rs.getInt("lvl"));
				function.setEndTimeInMillis(rs.getInt("endTime") * 1000L);
				function.setInDebt(rs.getBoolean("inDebt"));
				function.setActive(true);
				startAutoTaskForFunction(function);
			}
		}
		catch (Exception e)
		{
			_log.warn("Residence: loadFunctions(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method isFunctionActive.
	 * @param type int
	 * @return boolean
	 */
	public boolean isFunctionActive(int type)
	{
		ResidenceFunction function = getFunction(type);
		if ((function != null) && function.isActive() && (function.getLevel() > 0))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method getFunction.
	 * @param type int
	 * @return ResidenceFunction
	 */
	public ResidenceFunction getFunction(int type)
	{
		for (int i = 0; i < _functions.size(); i++)
		{
			if (_functions.get(i).getType() == type)
			{
				return _functions.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Method updateFunctions.
	 * @param type int
	 * @param level int
	 * @return boolean
	 */
	public boolean updateFunctions(int type, int level)
	{
		Clan clan = getOwner();
		if (clan == null)
		{
			return false;
		}
		long count = clan.getAdenaCount();
		ResidenceFunction function = getFunction(type);
		if (function == null)
		{
			return false;
		}
		if (function.isActive() && (function.getLevel() == level))
		{
			return true;
		}
		int lease = level == 0 ? 0 : getFunction(type).getLease(level);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (!function.isActive())
			{
				if (count >= lease)
				{
					clan.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, lease);
				}
				else
				{
					return false;
				}
				long time = Calendar.getInstance().getTimeInMillis() + 86400000;
				statement = con.prepareStatement("REPLACE residence_functions SET id=?, type=?, lvl=?, endTime=?");
				statement.setInt(1, getId());
				statement.setInt(2, type);
				statement.setInt(3, level);
				statement.setInt(4, (int) (time / 1000));
				statement.execute();
				function.setLvl(level);
				function.setEndTimeInMillis(time);
				function.setActive(true);
				startAutoTaskForFunction(function);
			}
			else
			{
				if (count >= (lease - getFunction(type).getLease()))
				{
					if (lease > getFunction(type).getLease())
					{
						clan.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, lease - getFunction(type).getLease());
					}
				}
				else
				{
					return false;
				}
				statement = con.prepareStatement("REPLACE residence_functions SET id=?, type=?, lvl=?");
				statement.setInt(1, getId());
				statement.setInt(2, type);
				statement.setInt(3, level);
				statement.execute();
				function.setLvl(level);
			}
		}
		catch (Exception e)
		{
			_log.warn("Exception: SiegeUnit.updateFunctions(int type, int lvl, int lease, long rate, long time, boolean addNew): " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
	
	/**
	 * Method removeFunction.
	 * @param type int
	 */
	public void removeFunction(int type)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM residence_functions WHERE id=? AND type=?");
			statement.setInt(1, getId());
			statement.setInt(2, type);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("Exception: removeFunctions(int type): " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method startAutoTaskForFunction.
	 * @param function ResidenceFunction
	 */
	void startAutoTaskForFunction(ResidenceFunction function)
	{
		if (getOwnerId() == 0)
		{
			return;
		}
		Clan clan = getOwner();
		if (clan == null)
		{
			return;
		}
		if (function.getEndTimeInMillis() > System.currentTimeMillis())
		{
			ThreadPoolManager.getInstance().schedule(new AutoTaskForFunctions(function), function.getEndTimeInMillis() - System.currentTimeMillis());
		}
		else if (function.isInDebt() && (clan.getAdenaCount() >= function.getLease()))
		{
			clan.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, function.getLease());
			function.updateRentTime(false);
			ThreadPoolManager.getInstance().schedule(new AutoTaskForFunctions(function), function.getEndTimeInMillis() - System.currentTimeMillis());
		}
		else if (!function.isInDebt())
		{
			function.setInDebt(true);
			function.updateRentTime(true);
			ThreadPoolManager.getInstance().schedule(new AutoTaskForFunctions(function), function.getEndTimeInMillis() - System.currentTimeMillis());
		}
		else
		{
			function.setLvl(0);
			function.setActive(false);
			removeFunction(function.getType());
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class AutoTaskForFunctions extends RunnableImpl
	{
		/**
		 * Field _function.
		 */
		ResidenceFunction _function;
		
		/**
		 * Constructor for AutoTaskForFunctions.
		 * @param function ResidenceFunction
		 */
		public AutoTaskForFunctions(ResidenceFunction function)
		{
			_function = function;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			startAutoTaskForFunction(_function);
		}
	}
	
	/**
	 * Method setJdbcState.
	 * @param state JdbcEntityState
	 * @see lineage2.commons.dao.JdbcEntity#setJdbcState(JdbcEntityState)
	 */
	@Override
	public void setJdbcState(JdbcEntityState state)
	{
		_jdbcEntityState = state;
	}
	
	/**
	 * Method getJdbcState.
	 * @return JdbcEntityState * @see lineage2.commons.dao.JdbcEntity#getJdbcState()
	 */
	@Override
	public JdbcEntityState getJdbcState()
	{
		return _jdbcEntityState;
	}
	
	/**
	 * Method save.
	 * @see lineage2.commons.dao.JdbcEntity#save()
	 */
	@Override
	public void save()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method delete.
	 * @see lineage2.commons.dao.JdbcEntity#delete()
	 */
	@Override
	public void delete()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method cancelCycleTask.
	 */
	public void cancelCycleTask()
	{
		_cycle = 0;
		_paidCycle = 0;
		_rewardCount = 0;
		if (_cycleTask != null)
		{
			_cycleTask.cancel(false);
			_cycleTask = null;
		}
		setJdbcState(JdbcEntityState.UPDATED);
	}
	
	/**
	 * Method startCycleTask.
	 */
	public void startCycleTask()
	{
		if (_owner == null)
		{
			return;
		}
		long ownedTime = getOwnDate().getTimeInMillis();
		if (ownedTime == 0)
		{
			return;
		}
		long diff = System.currentTimeMillis() - ownedTime;
		while (diff >= CYCLE_TIME)
		{
			diff -= CYCLE_TIME;
		}
		_cycleTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ResidenceCycleTask(), diff, CYCLE_TIME);
	}
	
	/**
	 * Method chanceCycle.
	 */
	public void chanceCycle()
	{
		setCycle(getCycle() + 1);
		setJdbcState(JdbcEntityState.UPDATED);
	}
	
	/**
	 * Method getSkills.
	 * @return List<Skill>
	 */
	public List<Skill> getSkills()
	{
		return _skills;
	}
	
	/**
	 * Method addBanishPoint.
	 * @param loc Location
	 */
	public void addBanishPoint(Location loc)
	{
		_banishPoints.add(loc);
	}
	
	/**
	 * Method addOwnerRestartPoint.
	 * @param loc Location
	 */
	public void addOwnerRestartPoint(Location loc)
	{
		_ownerRestartPoints.add(loc);
	}
	
	/**
	 * Method addOtherRestartPoint.
	 * @param loc Location
	 */
	public void addOtherRestartPoint(Location loc)
	{
		_otherRestartPoints.add(loc);
	}
	
	/**
	 * Method addChaosRestartPoint.
	 * @param loc Location
	 */
	public void addChaosRestartPoint(Location loc)
	{
		_chaosRestartPoints.add(loc);
	}
	
	/**
	 * Method getBanishPoint.
	 * @return Location
	 */
	public Location getBanishPoint()
	{
		if (_banishPoints.isEmpty())
		{
			return null;
		}
		return _banishPoints.get(Rnd.get(_banishPoints.size()));
	}
	
	/**
	 * Method getOwnerRestartPoint.
	 * @return Location
	 */
	public Location getOwnerRestartPoint()
	{
		if (_ownerRestartPoints.isEmpty())
		{
			return null;
		}
		return _ownerRestartPoints.get(Rnd.get(_ownerRestartPoints.size()));
	}
	
	/**
	 * Method getOtherRestartPoint.
	 * @return Location
	 */
	public Location getOtherRestartPoint()
	{
		if (_otherRestartPoints.isEmpty())
		{
			return null;
		}
		return _otherRestartPoints.get(Rnd.get(_otherRestartPoints.size()));
	}
	
	/**
	 * Method getChaosRestartPoint.
	 * @return Location
	 */
	public Location getChaosRestartPoint()
	{
		if (_chaosRestartPoints.isEmpty())
		{
			return null;
		}
		return _chaosRestartPoints.get(Rnd.get(_chaosRestartPoints.size()));
	}
	
	/**
	 * Method getNotOwnerRestartPoint.
	 * @param player Player
	 * @return Location
	 */
	public Location getNotOwnerRestartPoint(Player player)
	{
		return player.isChaotic() ? getChaosRestartPoint() : getOtherRestartPoint();
	}
	
	/**
	 * Method getCycle.
	 * @return int
	 */
	public int getCycle()
	{
		return _cycle;
	}
	
	/**
	 * Method getCycleDelay.
	 * @return long
	 */
	public long getCycleDelay()
	{
		if (_cycleTask == null)
		{
			return 0;
		}
		return _cycleTask.getDelay(TimeUnit.SECONDS);
	}
	
	/**
	 * Method setCycle.
	 * @param cycle int
	 */
	public void setCycle(int cycle)
	{
		_cycle = cycle;
	}
	
	/**
	 * Method getPaidCycle.
	 * @return int
	 */
	public int getPaidCycle()
	{
		return _paidCycle;
	}
	
	/**
	 * Method setPaidCycle.
	 * @param paidCycle int
	 */
	public void setPaidCycle(int paidCycle)
	{
		_paidCycle = paidCycle;
	}
	
	/**
	 * Method getRewardCount.
	 * @return int
	 */
	public int getRewardCount()
	{
		return _rewardCount;
	}
	
	/**
	 * Method setRewardCount.
	 * @param rewardCount int
	 */
	public void setRewardCount(int rewardCount)
	{
		_rewardCount = rewardCount;
	}
}

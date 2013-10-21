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
package lineage2.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.items.Warehouse;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.templates.manor.CropProcure;
import lineage2.gameserver.templates.manor.SeedProduction;
import lineage2.gameserver.utils.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleManorManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CastleManorManager.class);
	/**
	 * Field _instance.
	 */
	private static CastleManorManager _instance;
	/**
	 * Field PERIOD_CURRENT. (value is 0)
	 */
	public static final int PERIOD_CURRENT = 0;
	/**
	 * Field PERIOD_NEXT. (value is 1)
	 */
	public static final int PERIOD_NEXT = 1;
	/**
	 * Field var_name. (value is ""ManorApproved"")
	 */
	protected static final String var_name = "ManorApproved";
	/**
	 * Field CASTLE_MANOR_LOAD_PROCURE. (value is ""SELECT * FROM castle_manor_procure WHERE castle_id=?"")
	 */
	private static final String CASTLE_MANOR_LOAD_PROCURE = "SELECT * FROM castle_manor_procure WHERE castle_id=?";
	/**
	 * Field CASTLE_MANOR_LOAD_PRODUCTION. (value is ""SELECT * FROM castle_manor_production WHERE castle_id=?"")
	 */
	private static final String CASTLE_MANOR_LOAD_PRODUCTION = "SELECT * FROM castle_manor_production WHERE castle_id=?";
	/**
	 * Field NEXT_PERIOD_APPROVE.
	 */
	static final int NEXT_PERIOD_APPROVE = Config.MANOR_APPROVE_TIME;
	/**
	 * Field NEXT_PERIOD_APPROVE_MIN.
	 */
	static final int NEXT_PERIOD_APPROVE_MIN = Config.MANOR_APPROVE_MIN;
	/**
	 * Field MANOR_REFRESH.
	 */
	static final int MANOR_REFRESH = Config.MANOR_REFRESH_TIME;
	/**
	 * Field MANOR_REFRESH_MIN.
	 */
	static final int MANOR_REFRESH_MIN = Config.MANOR_REFRESH_MIN;
	/**
	 * Field MAINTENANCE_PERIOD.
	 */
	protected static final long MAINTENANCE_PERIOD = Config.MANOR_MAINTENANCE_PERIOD / 60000;
	/**
	 * Field _underMaintenance.
	 */
	private boolean _underMaintenance;
	/**
	 * Field _disabled.
	 */
	private boolean _disabled;
	
	/**
	 * Method getInstance.
	 * @return CastleManorManager
	 */
	public static CastleManorManager getInstance()
	{
		if (_instance == null)
		{
			_log.info("Manor System: Initializing...");
			_instance = new CastleManorManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for CastleManorManager.
	 */
	private CastleManorManager()
	{
		load();
		init();
		_underMaintenance = false;
		_disabled = !Config.ALLOW_MANOR;
		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castleList)
		{
			c.setNextPeriodApproved(ServerVariables.getBool(var_name));
		}
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
			for (Castle castle : castleList)
			{
				List<SeedProduction> production = new ArrayList<>();
				List<SeedProduction> productionNext = new ArrayList<>();
				List<CropProcure> procure = new ArrayList<>();
				List<CropProcure> procureNext = new ArrayList<>();
				statement = con.prepareStatement(CASTLE_MANOR_LOAD_PRODUCTION);
				statement.setInt(1, castle.getId());
				rs = statement.executeQuery();
				while (rs.next())
				{
					int seedId = rs.getInt("seed_id");
					long canProduce = rs.getLong("can_produce");
					long startProduce = rs.getLong("start_produce");
					long price = rs.getLong("seed_price");
					int period = rs.getInt("period");
					if (period == PERIOD_CURRENT)
					{
						production.add(new SeedProduction(seedId, canProduce, price, startProduce));
					}
					else
					{
						productionNext.add(new SeedProduction(seedId, canProduce, price, startProduce));
					}
				}
				DbUtils.close(statement, rs);
				castle.setSeedProduction(production, PERIOD_CURRENT);
				castle.setSeedProduction(productionNext, PERIOD_NEXT);
				statement = con.prepareStatement(CASTLE_MANOR_LOAD_PROCURE);
				statement.setInt(1, castle.getId());
				rs = statement.executeQuery();
				while (rs.next())
				{
					int cropId = rs.getInt("crop_id");
					long canBuy = rs.getLong("can_buy");
					long startBuy = rs.getLong("start_buy");
					int rewardType = rs.getInt("reward_type");
					long price = rs.getLong("price");
					int period = rs.getInt("period");
					if (period == PERIOD_CURRENT)
					{
						procure.add(new CropProcure(cropId, canBuy, rewardType, startBuy, price));
					}
					else
					{
						procureNext.add(new CropProcure(cropId, canBuy, rewardType, startBuy, price));
					}
				}
				castle.setCropProcure(procure, PERIOD_CURRENT);
				castle.setCropProcure(procureNext, PERIOD_NEXT);
				if (!procure.isEmpty() || !procureNext.isEmpty() || !production.isEmpty() || !productionNext.isEmpty())
				{
					_log.info("Manor System: Loaded data for " + castle.getName() + " castle");
				}
				DbUtils.close(statement, rs);
			}
		}
		catch (Exception e)
		{
			_log.error("Manor System: Error restoring manor data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method init.
	 */
	protected void init()
	{
		if (ServerVariables.getString(var_name, "").isEmpty())
		{
			Calendar manorRefresh = Calendar.getInstance();
			manorRefresh.set(Calendar.HOUR_OF_DAY, MANOR_REFRESH);
			manorRefresh.set(Calendar.MINUTE, MANOR_REFRESH_MIN);
			manorRefresh.set(Calendar.SECOND, 0);
			manorRefresh.set(Calendar.MILLISECOND, 0);
			Calendar periodApprove = Calendar.getInstance();
			periodApprove.set(Calendar.HOUR_OF_DAY, NEXT_PERIOD_APPROVE);
			periodApprove.set(Calendar.MINUTE, NEXT_PERIOD_APPROVE_MIN);
			periodApprove.set(Calendar.SECOND, 0);
			periodApprove.set(Calendar.MILLISECOND, 0);
			boolean isApproved = (periodApprove.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) && (manorRefresh.getTimeInMillis() > Calendar.getInstance().getTimeInMillis());
			ServerVariables.set(var_name, isApproved);
		}
		Calendar FirstDelay = Calendar.getInstance();
		FirstDelay.set(Calendar.SECOND, 0);
		FirstDelay.set(Calendar.MILLISECOND, 0);
		FirstDelay.add(Calendar.MINUTE, 1);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new ManorTask(), FirstDelay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis(), 60000);
	}
	
	/**
	 * Method setNextPeriod.
	 */
	public void setNextPeriod()
	{
		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castleList)
		{
			if (c.getOwnerId() <= 0)
			{
				continue;
			}
			Clan clan = ClanTable.getInstance().getClan(c.getOwnerId());
			if (clan == null)
			{
				continue;
			}
			Warehouse cwh = clan.getWarehouse();
			for (CropProcure crop : c.getCropProcure(PERIOD_CURRENT))
			{
				if (crop.getStartAmount() == 0)
				{
					continue;
				}
				if (crop.getStartAmount() > crop.getAmount())
				{
					_log.info("Manor System [" + c.getName() + "]: Start Amount of Crop " + crop.getStartAmount() + " > Amount of current " + crop.getAmount());
					long count = crop.getStartAmount() - crop.getAmount();
					count = (count * 90) / 100;
					if ((count < 1) && (Rnd.get(99) < 90))
					{
						count = 1;
					}
					if (count >= 1)
					{
						int id = Manor.getInstance().getMatureCrop(crop.getId());
						cwh.addItem(id, count);
					}
				}
				if (crop.getAmount() > 0)
				{
					c.addToTreasuryNoTax(crop.getAmount() * crop.getPrice(), false, false);
					Log.add(c.getName() + "|" + (crop.getAmount() * crop.getPrice()) + "|ManorManager|" + crop.getAmount() + "*" + crop.getPrice(), "treasury");
				}
				c.setCollectedShops(0);
				c.setCollectedSeed(0);
			}
			c.setSeedProduction(c.getSeedProduction(PERIOD_NEXT), PERIOD_CURRENT);
			c.setCropProcure(c.getCropProcure(PERIOD_NEXT), PERIOD_CURRENT);
			long manor_cost = c.getManorCost(PERIOD_CURRENT);
			if (c.getTreasury() < manor_cost)
			{
				c.setSeedProduction(getNewSeedsList(c.getId()), PERIOD_NEXT);
				c.setCropProcure(getNewCropsList(c.getId()), PERIOD_NEXT);
				Log.add(c.getName() + "|" + manor_cost + "|ManorManager Error@setNextPeriod", "treasury");
			}
			else
			{
				List<SeedProduction> production = new ArrayList<>();
				List<CropProcure> procure = new ArrayList<>();
				for (SeedProduction s : c.getSeedProduction(PERIOD_CURRENT))
				{
					s.setCanProduce(s.getStartProduce());
					production.add(s);
				}
				for (CropProcure cr : c.getCropProcure(PERIOD_CURRENT))
				{
					cr.setAmount(cr.getStartAmount());
					procure.add(cr);
				}
				c.setSeedProduction(production, PERIOD_NEXT);
				c.setCropProcure(procure, PERIOD_NEXT);
			}
			c.saveCropData();
			c.saveSeedData();
			PlayerMessageStack.getInstance().mailto(clan.getLeaderId(), Msg.THE_MANOR_INFORMATION_HAS_BEEN_UPDATED);
			c.setNextPeriodApproved(false);
		}
	}
	
	/**
	 * Method approveNextPeriod.
	 */
	public void approveNextPeriod()
	{
		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castleList)
		{
			if (c.getOwnerId() <= 0)
			{
				continue;
			}
			long manor_cost = c.getManorCost(PERIOD_NEXT);
			if (c.getTreasury() < manor_cost)
			{
				c.setSeedProduction(getNewSeedsList(c.getId()), PERIOD_NEXT);
				c.setCropProcure(getNewCropsList(c.getId()), PERIOD_NEXT);
				manor_cost = c.getManorCost(PERIOD_NEXT);
				if (manor_cost > 0)
				{
					Log.add(c.getName() + "|" + -manor_cost + "|ManorManager Error@approveNextPeriod", "treasury");
				}
				Clan clan = c.getOwner();
				PlayerMessageStack.getInstance().mailto(clan.getLeaderId(), Msg.THE_AMOUNT_IS_NOT_SUFFICIENT_AND_SO_THE_MANOR_IS_NOT_IN_OPERATION);
			}
			else
			{
				c.addToTreasuryNoTax(-manor_cost, false, false);
				Log.add(c.getName() + "|" + -manor_cost + "|ManorManager", "treasury");
			}
			c.setNextPeriodApproved(true);
		}
	}
	
	/**
	 * Method getNewSeedsList.
	 * @param castleId int
	 * @return List<SeedProduction>
	 */
	private List<SeedProduction> getNewSeedsList(int castleId)
	{
		List<SeedProduction> seeds = new ArrayList<>();
		List<Integer> seedsIds = Manor.getInstance().getSeedsForCastle(castleId);
		for (int sd : seedsIds)
		{
			seeds.add(new SeedProduction(sd));
		}
		return seeds;
	}
	
	/**
	 * Method getNewCropsList.
	 * @param castleId int
	 * @return List<CropProcure>
	 */
	private List<CropProcure> getNewCropsList(int castleId)
	{
		List<CropProcure> crops = new ArrayList<>();
		List<Integer> cropsIds = Manor.getInstance().getCropsForCastle(castleId);
		for (int cr : cropsIds)
		{
			crops.add(new CropProcure(cr));
		}
		return crops;
	}
	
	/**
	 * Method isUnderMaintenance.
	 * @return boolean
	 */
	public boolean isUnderMaintenance()
	{
		return _underMaintenance;
	}
	
	/**
	 * Method setUnderMaintenance.
	 * @param mode boolean
	 */
	public void setUnderMaintenance(boolean mode)
	{
		_underMaintenance = mode;
	}
	
	/**
	 * Method isDisabled.
	 * @return boolean
	 */
	public boolean isDisabled()
	{
		return _disabled;
	}
	
	/**
	 * Method setDisabled.
	 * @param mode boolean
	 */
	public void setDisabled(boolean mode)
	{
		_disabled = mode;
	}
	
	/**
	 * Method getNewSeedProduction.
	 * @param id int
	 * @param amount long
	 * @param price long
	 * @param sales long
	 * @return SeedProduction
	 */
	public SeedProduction getNewSeedProduction(int id, long amount, long price, long sales)
	{
		return new SeedProduction(id, amount, price, sales);
	}
	
	/**
	 * Method getNewCropProcure.
	 * @param id int
	 * @param amount long
	 * @param type int
	 * @param price long
	 * @param buy long
	 * @return CropProcure
	 */
	public CropProcure getNewCropProcure(int id, long amount, int type, long price, long buy)
	{
		return new CropProcure(id, amount, type, buy, price);
	}
	
	/**
	 * Method save.
	 */
	public void save()
	{
		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castleList)
		{
			c.saveSeedData();
			c.saveCropData();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ManorTask extends RunnableImpl
	{
		/**
		 * Constructor for ManorTask.
		 */
		public ManorTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			int H = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int M = Calendar.getInstance().get(Calendar.MINUTE);
			if (ServerVariables.getBool(var_name))
			{
				if ((H < NEXT_PERIOD_APPROVE) || (H > MANOR_REFRESH) || ((H == MANOR_REFRESH) && (M >= MANOR_REFRESH_MIN)))
				{
					ServerVariables.set(var_name, false);
					setUnderMaintenance(true);
					_log.info("Manor System: Under maintenance mode started");
				}
			}
			else if (isUnderMaintenance())
			{
				if ((H != MANOR_REFRESH) || (M >= (MANOR_REFRESH_MIN + MAINTENANCE_PERIOD)))
				{
					setUnderMaintenance(false);
					_log.info("Manor System: Next period started");
					if (isDisabled())
					{
						return;
					}
					setNextPeriod();
					try
					{
						save();
					}
					catch (Exception e)
					{
						_log.info("Manor System: Failed to save manor data: " + e);
					}
				}
			}
			else if (((H > NEXT_PERIOD_APPROVE) && (H < MANOR_REFRESH)) || ((H == NEXT_PERIOD_APPROVE) && (M >= NEXT_PERIOD_APPROVE_MIN)))
			{
				ServerVariables.set(var_name, true);
				_log.info("Manor System: Next period approved");
				if (isDisabled())
				{
					return;
				}
				approveNextPeriod();
			}
		}
	}
	
	/**
	 * Method getOwner.
	 * @param castleId int
	 * @return String
	 */
	public String getOwner(int castleId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT clan_id FROM clan_data WHERE hasCastle = ? LIMIT 1");
			statement.setInt(1, castleId);
			rs = statement.executeQuery();
			if (rs.next())
			{
				return ClanTable.getInstance().getClan(rs.getInt("clan_id")).toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
		return null;
	}
}

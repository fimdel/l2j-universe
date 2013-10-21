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
package lineage2.gameserver.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.manor.CropProcure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Manor
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Manor.class);
	/**
	 * Field _instance.
	 */
	private static Manor _instance;
	/**
	 * Field _seeds.
	 */
	private static Map<Integer, SeedData> _seeds;
	
	/**
	 * Constructor for Manor.
	 */
	public Manor()
	{
		_seeds = new ConcurrentHashMap<>();
		parseData();
	}
	
	/**
	 * Method getInstance.
	 * @return Manor
	 */
	public static Manor getInstance()
	{
		if (_instance == null)
		{
			_instance = new Manor();
		}
		return _instance;
	}
	
	/**
	 * Method getAllCrops.
	 * @return List<Integer>
	 */
	public List<Integer> getAllCrops()
	{
		List<Integer> crops = new ArrayList<>();
		for (SeedData seed : _seeds.values())
		{
			if (!crops.contains(seed.getCrop()) && (seed.getCrop() != 0) && !crops.contains(seed.getCrop()))
			{
				crops.add(seed.getCrop());
			}
		}
		return crops;
	}
	
	/**
	 * Method getAllSeeds.
	 * @return Map<Integer,SeedData>
	 */
	public Map<Integer, SeedData> getAllSeeds()
	{
		return _seeds;
	}
	
	/**
	 * Method getSeedBasicPrice.
	 * @param seedId int
	 * @return int
	 */
	public int getSeedBasicPrice(int seedId)
	{
		ItemTemplate seedItem = ItemHolder.getInstance().getTemplate(seedId);
		if (seedItem != null)
		{
			return seedItem.getReferencePrice();
		}
		return 0;
	}
	
	/**
	 * Method getSeedBasicPriceByCrop.
	 * @param cropId int
	 * @return int
	 */
	public int getSeedBasicPriceByCrop(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCrop() == cropId)
			{
				return getSeedBasicPrice(seed.getId());
			}
		}
		return 0;
	}
	
	/**
	 * Method getCropBasicPrice.
	 * @param cropId int
	 * @return int
	 */
	public int getCropBasicPrice(int cropId)
	{
		ItemTemplate cropItem = ItemHolder.getInstance().getTemplate(cropId);
		if (cropItem != null)
		{
			return cropItem.getReferencePrice();
		}
		return 0;
	}
	
	/**
	 * Method getMatureCrop.
	 * @param cropId int
	 * @return int
	 */
	public int getMatureCrop(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCrop() == cropId)
			{
				return seed.getMature();
			}
		}
		return 0;
	}
	
	/**
	 * Method getSeedBuyPrice.
	 * @param seedId int
	 * @return long
	 */
	public long getSeedBuyPrice(int seedId)
	{
		long buyPrice = getSeedBasicPrice(seedId) / 10;
		return buyPrice >= 0 ? buyPrice : 1;
	}
	
	/**
	 * Method getSeedMinLevel.
	 * @param seedId int
	 * @return int
	 */
	public int getSeedMinLevel(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getLevel() - 5;
		}
		return -1;
	}
	
	/**
	 * Method getSeedMaxLevel.
	 * @param seedId int
	 * @return int
	 */
	public int getSeedMaxLevel(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getLevel() + 5;
		}
		return -1;
	}
	
	/**
	 * Method getSeedLevelByCrop.
	 * @param cropId int
	 * @return int
	 */
	public int getSeedLevelByCrop(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCrop() == cropId)
			{
				return seed.getLevel();
			}
		}
		return 0;
	}
	
	/**
	 * Method getSeedLevel.
	 * @param seedId int
	 * @return int
	 */
	public int getSeedLevel(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getLevel();
		}
		return -1;
	}
	
	/**
	 * Method isAlternative.
	 * @param seedId int
	 * @return boolean
	 */
	public boolean isAlternative(int seedId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getId() == seedId)
			{
				return seed.isAlternative();
			}
		}
		return false;
	}
	
	/**
	 * Method getCropType.
	 * @param seedId int
	 * @return int
	 */
	public int getCropType(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getCrop();
		}
		return -1;
	}
	
	/**
	 * Method getRewardItem.
	 * @param cropId int
	 * @param type int
	 * @return int
	 */
	public synchronized int getRewardItem(int cropId, int type)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCrop() == cropId)
			{
				return seed.getReward(type);
			}
		}
		return -1;
	}
	
	/**
	 * Method getRewardAmountPerCrop.
	 * @param castle int
	 * @param cropId int
	 * @param type int
	 * @return long
	 */
	public synchronized long getRewardAmountPerCrop(int castle, int cropId, int type)
	{
		final CropProcure cs = ResidenceHolder.getInstance().getResidence(Castle.class, castle).getCropProcure(CastleManorManager.PERIOD_CURRENT).get(cropId);
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCrop() == cropId)
			{
				return cs.getPrice() / getCropBasicPrice(seed.getReward(type));
			}
		}
		return -1;
	}
	
	/**
	 * Method getRewardItemBySeed.
	 * @param seedId int
	 * @param type int
	 * @return int
	 */
	public synchronized int getRewardItemBySeed(int seedId, int type)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getReward(type);
		}
		return 0;
	}
	
	/**
	 * Method getCropsForCastle.
	 * @param castleId int
	 * @return List<Integer>
	 */
	public List<Integer> getCropsForCastle(int castleId)
	{
		List<Integer> crops = new ArrayList<>();
		for (SeedData seed : _seeds.values())
		{
			if ((seed.getManorId() == castleId) && !crops.contains(seed.getCrop()))
			{
				crops.add(seed.getCrop());
			}
		}
		return crops;
	}
	
	/**
	 * Method getSeedsForCastle.
	 * @param castleId int
	 * @return List<Integer>
	 */
	public List<Integer> getSeedsForCastle(int castleId)
	{
		List<Integer> seedsID = new ArrayList<>();
		for (SeedData seed : _seeds.values())
		{
			if ((seed.getManorId() == castleId) && !seedsID.contains(seed.getId()))
			{
				seedsID.add(seed.getId());
			}
		}
		return seedsID;
	}
	
	/**
	 * Method getCastleIdForSeed.
	 * @param seedId int
	 * @return int
	 */
	public int getCastleIdForSeed(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getManorId();
		}
		return 0;
	}
	
	/**
	 * Method getSeedSaleLimit.
	 * @param seedId int
	 * @return long
	 */
	public long getSeedSaleLimit(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getSeedLimit();
		}
		return 0;
	}
	
	/**
	 * Method getCropPuchaseLimit.
	 * @param cropId int
	 * @return long
	 */
	public long getCropPuchaseLimit(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCrop() == cropId)
			{
				return seed.getCropLimit();
			}
		}
		return 0;
	}
	
	/**
	 * @author Mobius
	 */
	public class SeedData
	{
		/**
		 * Field _id.
		 */
		private int _id;
		/**
		 * Field _level.
		 */
		private final int _level;
		/**
		 * Field _crop.
		 */
		private final int _crop;
		/**
		 * Field _mature.
		 */
		private final int _mature;
		/**
		 * Field _type1.
		 */
		private int _type1;
		/**
		 * Field _type2.
		 */
		private int _type2;
		/**
		 * Field _manorId.
		 */
		private int _manorId;
		/**
		 * Field _isAlternative.
		 */
		private int _isAlternative;
		/**
		 * Field _limitSeeds.
		 */
		private long _limitSeeds;
		/**
		 * Field _limitCrops.
		 */
		private long _limitCrops;
		
		/**
		 * Constructor for SeedData.
		 * @param level int
		 * @param crop int
		 * @param mature int
		 */
		public SeedData(int level, int crop, int mature)
		{
			_level = level;
			_crop = crop;
			_mature = mature;
		}
		
		/**
		 * Method setData.
		 * @param id int
		 * @param t1 int
		 * @param t2 int
		 * @param manorId int
		 * @param isAlt int
		 * @param lim1 long
		 * @param lim2 long
		 */
		public void setData(int id, int t1, int t2, int manorId, int isAlt, long lim1, long lim2)
		{
			_id = id;
			_type1 = t1;
			_type2 = t2;
			_manorId = manorId;
			_isAlternative = isAlt;
			_limitSeeds = lim1;
			_limitCrops = lim2;
		}
		
		/**
		 * Method getManorId.
		 * @return int
		 */
		public int getManorId()
		{
			return _manorId;
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
		 * Method getCrop.
		 * @return int
		 */
		public int getCrop()
		{
			return _crop;
		}
		
		/**
		 * Method getMature.
		 * @return int
		 */
		public int getMature()
		{
			return _mature;
		}
		
		/**
		 * Method getReward.
		 * @param type int
		 * @return int
		 */
		public int getReward(int type)
		{
			return type == 1 ? _type1 : _type2;
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
		 * Method isAlternative.
		 * @return boolean
		 */
		public boolean isAlternative()
		{
			return _isAlternative == 1;
		}
		
		/**
		 * Method getSeedLimit.
		 * @return long
		 */
		public long getSeedLimit()
		{
			return _limitSeeds;
		}
		
		/**
		 * Method getCropLimit.
		 * @return long
		 */
		public long getCropLimit()
		{
			return _limitCrops;
		}
	}
	
	/**
	 * Method parseData.
	 */
	private void parseData()
	{
		LineNumberReader lnr = null;
		try
		{
			File seedData = new File(Config.DATAPACK_ROOT, "data/xml/other/seeds.csv");
			lnr = new LineNumberReader(new BufferedReader(new FileReader(seedData)));
			String line = null;
			while ((line = lnr.readLine()) != null)
			{
				if ((line.trim().length() == 0) || ((line.length() > 0) && (line.charAt(0) == '#')))
				{
					continue;
				}
				SeedData seed = parseList(line);
				_seeds.put(seed.getId(), seed);
			}
			_log.info("ManorManager: Loaded " + _seeds.size() + " seeds");
		}
		catch (FileNotFoundException e)
		{
			_log.info("seeds.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.error("Error while loading seeds!", e);
		}
		finally
		{
			try
			{
				if (lnr != null)
				{
					lnr.close();
				}
			}
			catch (Exception e1)
			{
			}
		}
	}
	
	/**
	 * Method parseList.
	 * @param line String
	 * @return SeedData
	 */
	private SeedData parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line, ";");
		int seedId = Integer.parseInt(st.nextToken());
		int level = Integer.parseInt(st.nextToken());
		int cropId = Integer.parseInt(st.nextToken());
		int matureId = Integer.parseInt(st.nextToken());
		int type1R = Integer.parseInt(st.nextToken());
		int type2R = Integer.parseInt(st.nextToken());
		int manorId = Integer.parseInt(st.nextToken());
		int isAlt = Integer.parseInt(st.nextToken());
		long limitSeeds = Math.round(Integer.parseInt(st.nextToken()) * Config.RATE_MANOR);
		long limitCrops = Math.round(Integer.parseInt(st.nextToken()) * Config.RATE_MANOR);
		SeedData seed = new SeedData(level, cropId, matureId);
		seed.setData(seedId, type1R, type2R, manorId, isAlt, limitSeeds, limitCrops);
		return seed;
	}
}

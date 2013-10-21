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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.instances.L2StatueInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MuseumManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = Logger.getLogger(MuseumManager.class.getName());
	/**
	 * Field con.
	 */
	Connection con = null;
	
	/**
	 * @author Mobius
	 */
	private class LoadTask implements Runnable
	{
		/**
		 * Constructor for LoadTask.
		 */
		public LoadTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			load();
			MuseumManager.getInstance().spawnSculptures();
		}
	}
	
	/**
	 * Field loadTask.
	 */
	private final LoadTask loadTask;
	/**
	 * Field Museums.
	 */
	List<Museum> Museums;
	/**
	 * Field topPlayerItems.
	 */
	HashMap<Integer, int[]> topPlayerItems;
	/**
	 * Field topPlayerAppearance.
	 */
	HashMap<Integer, int[]> topPlayerAppearance;
	/**
	 * Field _locations.
	 */
	List<int[]> _locations;
	/**
	 * Field _loadingInfo.
	 */
	List<String[]> _loadingInfo;
	/**
	 * Field objects.
	 */
	private GameObject objects;
	
	/**
	 * Constructor for MuseumManager.
	 */
	public MuseumManager()
	{
		loadTask = new LoadTask();
		Museums = new ArrayList<>();
		topPlayerItems = new HashMap<>();
		topPlayerAppearance = new HashMap<>();
		_locations = new ArrayList<>();
		parseLocations();
		loadInfo();
		load();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(loadTask, 15 * 60000, 15 * 60000);
		spawnSculptures();
	}
	
	/**
	 * Method loadInfo.
	 */
	private void loadInfo()
	{
		_loadingInfo = new ArrayList<>();
		_loadingInfo.add("monthly,acquiredXP,0".split(","));
		_loadingInfo.add("total,acquiredXP,0".split(","));
		_loadingInfo.add("monthly,acquiredAdena,1".split(","));
		_loadingInfo.add("total,acquiredAdena,1".split(","));
		_loadingInfo.add("monthly,onlineTime,2".split(","));
		_loadingInfo.add("total,onlineTime,2".split(","));
		_loadingInfo.add("monthly,partyDuration,4".split(","));
		_loadingInfo.add("total,partyDuration,4".split(","));
		_loadingInfo.add("monthly,fullPartyDuration,5".split(","));
		_loadingInfo.add("total,fullPartyDuration,5".split(","));
		_loadingInfo.add("monthly,privateStoreSales,11".split(","));
		_loadingInfo.add("total,privateStoreSales,11".split(","));
		_loadingInfo.add("monthly,numberOfResurrectCast,18".split(","));
		_loadingInfo.add("total,numberOfResurrectCast,18".split(","));
		_loadingInfo.add("monthly,numberOfResurrectReceived,19".split(","));
		_loadingInfo.add("total,numberOfResurrectReceived,19".split(","));
		_loadingInfo.add("monthly,numberOfDeaths,20".split(","));
		_loadingInfo.add("total,numberOfDeaths,20".split(","));
		_loadingInfo.add("monthly,numberOfMonsterKillings,1000".split(","));
		_loadingInfo.add("total,numberOfMonsterKillings,1000".split(","));
		_loadingInfo.add("monthly,monsterKillXp,1001".split(","));
		_loadingInfo.add("total,monsterKillXp,1001".split(","));
		_loadingInfo.add("monthly,pkVictoryCount,2004".split(","));
		_loadingInfo.add("total,pkVictoryCount,2004".split(","));
		_loadingInfo.add("monthly,pvpVictoryCount,2005".split(","));
		_loadingInfo.add("total,pvpVictoryCount,2005".split(","));
		_loadingInfo.add("monthly,pkDefeatCount,2001".split(","));
		_loadingInfo.add("total,pkDefeatCount,2001".split(","));
		_loadingInfo.add("monthly,pvpDefeatCount,2002".split(","));
		_loadingInfo.add("total,pvpDefeatCount,2002".split(","));
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		_log.info(getClass().getSimpleName() + ": Initializing");
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			Museums.clear();
			for (String loadingInfo[] : _loadingInfo)
			{
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters_museum_" + loadingInfo[0] + " ORDER BY " + loadingInfo[1] + " DESC LIMIT 100");
				int objID;
				long acquiredItem;
				ResultSet rset = statement.executeQuery();
				while (rset.next())
				{
					objID = (rset.getInt("objectID"));
					acquiredItem = rset.getLong(loadingInfo[1]);
					if (acquiredItem != 0)
					{
						createMuseum(objID, acquiredItem, Integer.parseInt(loadingInfo[2]), loadingInfo[0].equals("total"));
					}
				}
				statement.execute();
				rset.close();
				statement.close();
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed loading museum for character. " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}
		_log.info(getClass().getSimpleName() + ": Loaded Museums information.");
	}
	
	/**
	 * Method createMuseum.
	 * @param objectID int
	 * @param acquiredItem long
	 * @param category int
	 * @param isTotal boolean
	 */
	private void createMuseum(int objectID, long acquiredItem, int category, boolean isTotal)
	{
		Museums.add(new Museum(objectID, acquiredItem, category, isTotal));
	}
	
	/**
	 * Method getMuseums.
	 * @param category int
	 * @param isTotal boolean
	 * @return List<Museum>
	 */
	public List<Museum> getMuseums(int category, boolean isTotal)
	{
		List<Museum> museums = new ArrayList<>();
		for (Museum museum : Museums)
		{
			if (museum.isTotal() == isTotal)
			{
				if (museum.getCategory() == category)
				{
					museums.add(museum);
				}
			}
		}
		return museums;
	}
	
	/**
	 * Method getMuseum.
	 * @param objectID int
	 * @param category int
	 * @param isTotal boolean
	 * @return Museum
	 */
	public Museum getMuseum(int objectID, int category, boolean isTotal)
	{
		for (Museum museum : Museums)
		{
			if (museum.getObjectId() == objectID)
			{
				if (museum.isTotal() == isTotal)
				{
					return museum;
				}
			}
		}
		return null;
	}
	
	/**
	 * Method getMuseums.
	 * @return List<Museum>
	 */
	public List<Museum> getMuseums()
	{
		return Museums;
	}
	
	/**
	 * @author Mobius
	 */
	public class Museum
	{
		/**
		 * Field category.
		 */
		/**
		 * Field objectID.
		 */
		int objectID, category;
		/**
		 * Field isTotal.
		 */
		boolean isTotal;
		/**
		 * Field acquiredItem.
		 */
		long acquiredItem;
		
		/**
		 * Constructor for Museum.
		 * @param _objectID int
		 * @param _acquiredItem long
		 * @param _category int
		 * @param _isTotal boolean
		 */
		public Museum(int _objectID, long _acquiredItem, int _category, boolean _isTotal)
		{
			objectID = _objectID;
			acquiredItem = _acquiredItem;
			category = _category;
			isTotal = _isTotal;
		}
		
		/**
		 * Method isTotal.
		 * @return boolean
		 */
		public boolean isTotal()
		{
			return isTotal;
		}
		
		/**
		 * Method getObjectId.
		 * @return int
		 */
		public int getObjectId()
		{
			return objectID;
		}
		
		/**
		 * Method getCategory.
		 * @return int
		 */
		public int getCategory()
		{
			return category;
		}
		
		/**
		 * Method getAcquiredItem.
		 * @return long
		 */
		public long getAcquiredItem()
		{
			return acquiredItem;
		}
	}
	
	/**
	 * Method getLoadingInfo.
	 * @return List<String[]>
	 */
	public List<String[]> getLoadingInfo()
	{
		return _loadingInfo;
	}
	
	/**
	 * Method getTopPlayerItems.
	 * @param objectID int
	 * @return int[]
	 */
	public int[] getTopPlayerItems(int objectID)
	{
		int[] items =
		{
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0
		};
		Connection con = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM items WHERE owner_id=? AND loc=?");
			statement.setInt(1, objectID);
			statement.setString(2, "PAPERDOLL");
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				int itemId = rset.getInt("item_id");
				int locData = rset.getInt("loc_data");
				switch (locData)
				{
					case Inventory.PAPERDOLL_NECK:
						items[0] = itemId;
						break;
					case Inventory.PAPERDOLL_HEAD:
						items[1] = itemId;
						break;
					case Inventory.PAPERDOLL_RHAND:
						items[2] = itemId;
						break;
					case Inventory.PAPERDOLL_LHAND:
						items[3] = itemId;
						break;
					case Inventory.PAPERDOLL_GLOVES:
						items[4] = itemId;
						break;
					case Inventory.PAPERDOLL_CHEST:
						items[5] = itemId;
						break;
					case Inventory.PAPERDOLL_LEGS:
						items[6] = itemId;
						break;
					case Inventory.PAPERDOLL_FEET:
						items[7] = itemId;
						break;
					case Inventory.PAPERDOLL_DECO6:
						items[8] = itemId;
						break;
					case Inventory.PAPERDOLL_HAIR:
						items[9] = itemId;
						break;
					case Inventory.PAPERDOLL_DHAIR:
						items[10] = itemId;
						break;
				}
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not check Items in player: " + objectID, e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}
		return items;
	}
	
	/**
	 * Method getTopPlayerAppearance.
	 * @param objectID int
	 * @return int[]
	 */
	public int[] getTopPlayerAppearance(int objectID)
	{
		int[] items =
		{
			0,
			0,
			0,
			0,
			0,
			0
		};
		Connection con = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM `characters` WHERE `charId`=?");
			statement.setInt(1, objectID);
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				items[0] = rset.getInt("classid");
				items[1] = rset.getInt("race");
				items[2] = rset.getInt("sex");
				items[3] = rset.getInt("hairStyle");
				items[4] = rset.getInt("hairColor");
				items[5] = rset.getInt("face");
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not check Items in player: " + objectID, e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}
		return items;
	}
	
	/**
	 * Method getTopMuseum.
	 * @param category int
	 * @return Museum
	 */
	private Museum getTopMuseum(int category)
	{
		for (Museum museum : Museums)
		{
			if ((museum.getCategory() == category) && !museum.isTotal())
			{
				return museum;
			}
		}
		return null;
	}
	
	/**
	 * Method spawnSculptures.
	 */
	void spawnSculptures()
	{
		if (objects instanceof L2StatueInstance)
		{
			((L2StatueInstance) objects).deleteMe();
		}
		if (!getMuseums().isEmpty())
		{
			Museum museum;
			for (String[] information : getLoadingInfo())
			{
				int category = Integer.parseInt(information[2]);
				if ((getTopMuseum(category) != null) && information[0].equalsIgnoreCase("monthly"))
				{
					museum = getTopMuseum(category);
					if (!getLocation(category).isEmpty())
					{
						for (int[] location : getLocation(category))
						{
							StatsSet ss = new StatsSet();
							ss.set("npcId", 0);
							ss.set("type", "Npc");
							ss.set("name", "");
							NpcTemplate template = new NpcTemplate(ss);
							new L2StatueInstance(IdFactory.getInstance().getNextId(), template, museum.getObjectId(), location, getTopPlayerItems(museum.getObjectId()), getTopPlayerAppearance(museum.getObjectId()));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method getLocation.
	 * @param category int
	 * @return List<int[]>
	 */
	private List<int[]> getLocation(int category)
	{
		List<int[]> locations = new ArrayList<>();
		if (!_locations.isEmpty())
		{
			for (int[] location : _locations)
			{
				if (location[4] == category)
				{
					locations.add(location);
				}
			}
		}
		return locations;
	}
	
	/**
	 * Method parseLocations.
	 */
	public void parseLocations()
	{
		_log.info(getClass().getSimpleName() + ": Initializing locations");
		_locations.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		File file = new File(Config.DATAPACK_ROOT, "data/StatuesLocations.xml");
		Document doc = null;
		if (file.exists())
		{
			try
			{
				doc = factory.newDocumentBuilder().parse(file);
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "Could not parse StatuesLocations.xml file: " + e.getMessage(), e);
			}
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equals("location"))
				{
					NamedNodeMap attrs = d.getAttributes();
					int location[] = new int[5];
					location[4] = Integer.parseInt(attrs.getNamedItem("category").getNodeValue());
					location[0] = Integer.parseInt(attrs.getNamedItem("x").getNodeValue());
					location[1] = Integer.parseInt(attrs.getNamedItem("y").getNodeValue());
					location[2] = Integer.parseInt(attrs.getNamedItem("z").getNodeValue());
					location[3] = Integer.parseInt(attrs.getNamedItem("heading").getNodeValue());
					_locations.add(location);
				}
			}
		}
		_log.info(getClass().getSimpleName() + ": Loaded " + _locations.size() + " Locations for statues.");
		spawnSculptures();
	}
	
	/**
	 * Method getInstance.
	 * @return MuseumManager
	 */
	public static MuseumManager getInstance()
	{
		if (_instance == null)
		{
			_log.info("Initializing: MuseumManager");
			_instance = new MuseumManager();
			_instance.load();
		}
		return _instance;
	}
	
	/**
	 * Field _instance.
	 */
	private static MuseumManager _instance;
}

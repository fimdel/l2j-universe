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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.geometry.Polygon;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HellboundManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(HellboundManager.class);
	/**
	 * Field _list.
	 */
	private static ArrayList<HellboundSpawn> _list;
	/**
	 * Field _spawnList.
	 */
	private static List<SimpleSpawner> _spawnList;
	/**
	 * Field _instance.
	 */
	private static HellboundManager _instance;
	/**
	 * Field _initialStage.
	 */
	static int _initialStage;
	/**
	 * Field _taskDelay.
	 */
	private static final long _taskDelay = 2 * 60 * 1000L;
	/**
	 * Field _deathListener.
	 */
	DeathListener _deathListener = new DeathListener();
	
	/**
	 * Method getInstance.
	 * @return HellboundManager
	 */
	public static HellboundManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new HellboundManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for HellboundManager.
	 */
	public HellboundManager()
	{
		getHellboundSpawn();
		spawnHellbound();
		doorHandler();
		_initialStage = getHellboundLevel();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new StageCheckTask(), _taskDelay, _taskDelay);
		_log.info("Hellbound Manager: Loaded");
	}
	
	/**
	 * Method openHellbound.
	 */
	public void openHellbound()
	{
		if (getHellboundLevel() == 0)
		{
			addConfidence(1);
			spawnHellbound();
			doorHandler();
			_initialStage = getHellboundLevel();
		}
	}
	
	/**
	 * Method getConfidence.
	 * @return long
	 */
	public static long getConfidence()
	{
		return ServerVariables.getLong("HellboundConfidence", 0);
	}
	
	/**
	 * Method addConfidence.
	 * @param value long
	 */
	public static void addConfidence(long value)
	{
		ServerVariables.set("HellboundConfidence", Math.round(getConfidence() + (value * Config.RATE_HELLBOUND_CONFIDENCE)));
	}
	
	/**
	 * Method reduceConfidence.
	 * @param value long
	 */
	public static void reduceConfidence(long value)
	{
		long i = getConfidence() - value;
		if (i < 1)
		{
			i = 1;
		}
		ServerVariables.set("HellboundConfidence", i);
	}
	
	/**
	 * Method setConfidence.
	 * @param value long
	 */
	public static void setConfidence(long value)
	{
		ServerVariables.set("HellboundConfidence", value);
	}
	
	/**
	 * Method getHellboundLevel.
	 * @return int
	 */
	public static int getHellboundLevel()
	{
		long confidence = ServerVariables.getLong("HellboundConfidence", 0);
		boolean judesBoxes = ServerVariables.getBool("HB_judesBoxes", false);
		boolean bernardBoxes = ServerVariables.getBool("HB_bernardBoxes", false);
		boolean derekKilled = ServerVariables.getBool("HB_derekKilled", false);
		boolean captainKilled = ServerVariables.getBool("HB_captainKilled", false);
		if (confidence < 1)
		{
			return 0;
		}
		else if ((confidence >= 1) && (confidence < 300000))
		{
			return 1;
		}
		else if ((confidence >= 300000) && (confidence < 600000))
		{
			return 2;
		}
		else if ((confidence >= 600000) && (confidence < 1000000))
		{
			return 3;
		}
		else if ((confidence >= 1000000) && (confidence < 1200000))
		{
			if (derekKilled && judesBoxes && bernardBoxes)
			{
				return 5;
			}
			else if (!derekKilled && judesBoxes && bernardBoxes)
			{
				return 4;
			}
			else if (!derekKilled && (!judesBoxes || !bernardBoxes))
			{
				return 3;
			}
		}
		else if ((confidence >= 1200000) && (confidence < 1500000))
		{
			return 6;
		}
		else if ((confidence >= 1500000) && (confidence < 1800000))
		{
			return 7;
		}
		else if ((confidence >= 1800000) && (confidence < 2100000))
		{
			if (captainKilled)
			{
				return 9;
			}
			return 8;
		}
		else if ((confidence >= 2100000) && (confidence < 2200000))
		{
			return 10;
		}
		else if (confidence >= 2200000)
		{
			return 11;
		}
		return 0;
	}
	
	/**
	 * @author Mobius
	 */
	private class DeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DeathListener.
		 */
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDeath.
		 * @param cha Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature cha, Creature killer)
		{
			if ((killer == null) || !cha.isMonster() || !killer.isPlayable())
			{
				return;
			}
			switch (getHellboundLevel())
			{
				case 0:
					break;
				case 1:
				{
					switch (cha.getNpcId())
					{
						case 22320:
						case 22321:
						case 22324:
						case 22325:
							addConfidence(1);
							break;
						case 22327:
						case 22328:
						case 22329:
							addConfidence(3);
							break;
						case 22322:
						case 22323:
						case 32299:
							reduceConfidence(10);
							break;
					}
					break;
				}
				case 2:
				{
					switch (cha.getNpcId())
					{
						case 18463:
						case 18464:
							addConfidence(5);
							break;
						case 22322:
						case 22323:
						case 32299:
							reduceConfidence(10);
							break;
					}
					break;
				}
				case 3:
				{
					switch (cha.getNpcId())
					{
						case 22342:
						case 22343:
							addConfidence(3);
							break;
						case 22341:
							addConfidence(100);
							break;
						case 22322:
						case 22323:
						case 32299:
							reduceConfidence(10);
							break;
					}
					break;
				}
				case 4:
				{
					switch (cha.getNpcId())
					{
						case 18465:
							addConfidence(10000);
							ServerVariables.set("HB_derekKilled", true);
							break;
						case 22322:
						case 22323:
						case 32299:
							reduceConfidence(10);
							break;
					}
					break;
				}
				case 5:
				{
					switch (cha.getNpcId())
					{
						case 22448:
							reduceConfidence(50);
							break;
					}
					break;
				}
				case 6:
				{
					switch (cha.getNpcId())
					{
						case 22326:
							addConfidence(500);
							break;
						case 18484:
							addConfidence(5);
							break;
					}
					break;
				}
				case 8:
				{
					switch (cha.getNpcId())
					{
						case 18466:
							addConfidence(10000);
							ServerVariables.set("HB_captainKilled", true);
							break;
					}
					break;
				}
				default:
					break;
			}
		}
	}
	
	/**
	 * Method spawnHellbound.
	 */
	void spawnHellbound()
	{
		SimpleSpawner spawnDat;
		NpcTemplate template;
		for (HellboundSpawn hbsi : _list)
		{
			if (ArrayUtils.contains(hbsi.getStages(), getHellboundLevel()))
			{
				try
				{
					template = NpcHolder.getInstance().getTemplate(hbsi.getNpcId());
					for (int i = 0; i < hbsi.getAmount(); i++)
					{
						spawnDat = new SimpleSpawner(template);
						spawnDat.setAmount(1);
						if (hbsi.getLoc() != null)
						{
							spawnDat.setLoc(hbsi.getLoc());
						}
						if (hbsi.getSpawnTerritory() != null)
						{
							spawnDat.setTerritory(hbsi.getSpawnTerritory());
						}
						spawnDat.setReflection(ReflectionManager.DEFAULT);
						spawnDat.setRespawnDelay(hbsi.getRespawn(), hbsi.getRespawnRnd());
						spawnDat.setRespawnTime(0);
						spawnDat.doSpawn(true);
						spawnDat.getLastSpawn().addListener(_deathListener);
						spawnDat.startRespawn();
						_spawnList.add(spawnDat);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		_log.info("HellboundManager: Spawned " + _spawnList.size() + " mobs and NPCs according to the current Hellbound stage");
	}
	
	/**
	 * Method getHellboundSpawn.
	 */
	private void getHellboundSpawn()
	{
		_list = new ArrayList<>();
		_spawnList = new ArrayList<>();
		try
		{
			File file = new File(Config.DATAPACK_ROOT + "/data/xml/other/hellbound_spawnlist.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc1 = factory.newDocumentBuilder().parse(file);
			int counter = 0;
			for (Node n1 = doc1.getFirstChild(); n1 != null; n1 = n1.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n1.getNodeName()))
				{
					for (Node d1 = n1.getFirstChild(); d1 != null; d1 = d1.getNextSibling())
					{
						if ("data".equalsIgnoreCase(d1.getNodeName()))
						{
							counter++;
							int npcId = Integer.parseInt(d1.getAttributes().getNamedItem("npc_id").getNodeValue());
							Location spawnLoc = null;
							if (d1.getAttributes().getNamedItem("loc") != null)
							{
								spawnLoc = Location.parseLoc(d1.getAttributes().getNamedItem("loc").getNodeValue());
							}
							int count = 1;
							if (d1.getAttributes().getNamedItem("count") != null)
							{
								count = Integer.parseInt(d1.getAttributes().getNamedItem("count").getNodeValue());
							}
							int respawn = 60;
							if (d1.getAttributes().getNamedItem("respawn") != null)
							{
								respawn = Integer.parseInt(d1.getAttributes().getNamedItem("respawn").getNodeValue());
							}
							int respawnRnd = 0;
							if (d1.getAttributes().getNamedItem("respawn_rnd") != null)
							{
								respawnRnd = Integer.parseInt(d1.getAttributes().getNamedItem("respawn_rnd").getNodeValue());
							}
							Node att = d1.getAttributes().getNamedItem("stage");
							StringTokenizer st = new StringTokenizer(att.getNodeValue(), ";");
							int tokenCount = st.countTokens();
							int[] stages = new int[tokenCount];
							for (int i = 0; i < tokenCount; i++)
							{
								Integer value = Integer.decode(st.nextToken().trim());
								stages[i] = value;
							}
							Territory territory = null;
							for (Node s1 = d1.getFirstChild(); s1 != null; s1 = s1.getNextSibling())
							{
								if ("territory".equalsIgnoreCase(s1.getNodeName()))
								{
									Polygon poly = new Polygon();
									for (Node s2 = s1.getFirstChild(); s2 != null; s2 = s2.getNextSibling())
									{
										if ("add".equalsIgnoreCase(s2.getNodeName()))
										{
											int x = Integer.parseInt(s2.getAttributes().getNamedItem("x").getNodeValue());
											int y = Integer.parseInt(s2.getAttributes().getNamedItem("y").getNodeValue());
											int minZ = Integer.parseInt(s2.getAttributes().getNamedItem("zmin").getNodeValue());
											int maxZ = Integer.parseInt(s2.getAttributes().getNamedItem("zmax").getNodeValue());
											poly.add(x, y).setZmin(minZ).setZmax(maxZ);
										}
									}
									territory = new Territory().add(poly);
									if (!poly.validate())
									{
										_log.error("HellboundManager: Invalid spawn territory : " + poly + "!");
										continue;
									}
								}
							}
							if ((spawnLoc == null) && (territory == null))
							{
								_log.error("HellboundManager: no spawn data for npc id : " + npcId + "!");
								continue;
							}
							HellboundSpawn hbs = new HellboundSpawn(npcId, spawnLoc, count, territory, respawn, respawnRnd, stages);
							_list.add(hbs);
						}
					}
				}
			}
			_log.info("HellboundManager: Loaded " + counter + " spawn entries.");
		}
		catch (Exception e)
		{
			_log.warn("HellboundManager: Spawn table could not be initialized.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method despawnHellbound.
	 */
	void despawnHellbound()
	{
		for (SimpleSpawner spawnToDelete : _spawnList)
		{
			spawnToDelete.deleteAll();
		}
		_spawnList.clear();
	}
	
	/**
	 * @author Mobius
	 */
	private class StageCheckTask extends RunnableImpl
	{
		/**
		 * Constructor for StageCheckTask.
		 */
		public StageCheckTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_initialStage != getHellboundLevel())
			{
				despawnHellbound();
				spawnHellbound();
				doorHandler();
				_initialStage = getHellboundLevel();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class HellboundSpawn
	{
		/**
		 * Field _npcId.
		 */
		private final int _npcId;
		/**
		 * Field _loc.
		 */
		private final Location _loc;
		/**
		 * Field _amount.
		 */
		private final int _amount;
		/**
		 * Field _spawnTerritory.
		 */
		private final Territory _spawnTerritory;
		/**
		 * Field _respawn.
		 */
		private final int _respawn;
		/**
		 * Field _respawnRnd.
		 */
		private final int _respawnRnd;
		/**
		 * Field _stages.
		 */
		private final int[] _stages;
		
		/**
		 * Constructor for HellboundSpawn.
		 * @param npcId int
		 * @param loc Location
		 * @param amount int
		 * @param spawnTerritory Territory
		 * @param respawn int
		 * @param respawnRnd int
		 * @param stages int[]
		 */
		public HellboundSpawn(int npcId, Location loc, int amount, Territory spawnTerritory, int respawn, int respawnRnd, int[] stages)
		{
			_npcId = npcId;
			_loc = loc;
			_amount = amount;
			_spawnTerritory = spawnTerritory;
			_respawn = respawn;
			_respawnRnd = respawnRnd;
			_stages = stages;
		}
		
		/**
		 * Method getNpcId.
		 * @return int
		 */
		public int getNpcId()
		{
			return _npcId;
		}
		
		/**
		 * Method getLoc.
		 * @return Location
		 */
		public Location getLoc()
		{
			return _loc;
		}
		
		/**
		 * Method getAmount.
		 * @return int
		 */
		public int getAmount()
		{
			return _amount;
		}
		
		/**
		 * Method getSpawnTerritory.
		 * @return Territory
		 */
		public Territory getSpawnTerritory()
		{
			return _spawnTerritory;
		}
		
		/**
		 * Method getRespawn.
		 * @return int
		 */
		public int getRespawn()
		{
			return _respawn;
		}
		
		/**
		 * Method getRespawnRnd.
		 * @return int
		 */
		public int getRespawnRnd()
		{
			return _respawnRnd;
		}
		
		/**
		 * Method getStages.
		 * @return int[]
		 */
		public int[] getStages()
		{
			return _stages;
		}
	}
	
	/**
	 * Method doorHandler.
	 */
	static void doorHandler()
	{
		final int NativeHell_native0131 = 19250001;
		final int NativeHell_native0132 = 19250002;
		final int NativeHell_native0133 = 19250003;
		final int NativeHell_native0134 = 19250004;
		final int sdoor_trans_mesh00 = 20250002;
		final int Hell_gate_door = 20250001;
		final int[] _doors =
		{
			NativeHell_native0131,
			NativeHell_native0132,
			NativeHell_native0133,
			NativeHell_native0134,
			sdoor_trans_mesh00,
			Hell_gate_door
		};
		for (int _door : _doors)
		{
			ReflectionUtils.getDoor(_door).closeMe();
		}
		switch (getHellboundLevel())
		{
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				break;
			case 6:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				break;
			case 7:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				ReflectionUtils.getDoor(sdoor_trans_mesh00).openMe();
				break;
			case 8:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				ReflectionUtils.getDoor(sdoor_trans_mesh00).openMe();
				break;
			case 9:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				ReflectionUtils.getDoor(sdoor_trans_mesh00).openMe();
				ReflectionUtils.getDoor(Hell_gate_door).openMe();
				break;
			case 10:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				ReflectionUtils.getDoor(sdoor_trans_mesh00).openMe();
				ReflectionUtils.getDoor(Hell_gate_door).openMe();
				break;
			case 11:
				ReflectionUtils.getDoor(NativeHell_native0131).openMe();
				ReflectionUtils.getDoor(NativeHell_native0132).openMe();
				ReflectionUtils.getDoor(sdoor_trans_mesh00).openMe();
				ReflectionUtils.getDoor(Hell_gate_door).openMe();
				break;
			default:
				break;
		}
	}
}

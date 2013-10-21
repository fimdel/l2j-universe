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
package lineage2.gameserver.model.entity;

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.door.impl.MasterOnOpenCloseListenerImpl;
import lineage2.gameserver.listener.reflection.OnReflectionCollapseListener;
import lineage2.gameserver.listener.zone.impl.AirshipControllerZoneListener;
import lineage2.gameserver.listener.zone.impl.NoLandingZoneListener;
import lineage2.gameserver.listener.zone.impl.ResidenceEnterLeaveListenerImpl;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.HardSpawner;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Reflection
{
	/**
	 * @author Mobius
	 */
	public class ReflectionListenerList extends ListenerList<Reflection>
	{
		/**
		 * Method onCollapse.
		 */
		public void onCollapse()
		{
			if (!getListeners().isEmpty())
			{
				for (Listener<Reflection> listener : getListeners())
				{
					((OnReflectionCollapseListener) listener).onReflectionCollapse(Reflection.this);
				}
			}
		}
	}
	
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Reflection.class);
	/**
	 * Field _nextId.
	 */
	private final static AtomicInteger _nextId = new AtomicInteger();
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _name.
	 */
	private String _name = StringUtils.EMPTY;
	/**
	 * Field _instance.
	 */
	private InstantZone _instance;
	/**
	 * Field _geoIndex.
	 */
	private int _geoIndex;
	/**
	 * Field _resetLoc.
	 */
	private Location _resetLoc;
	/**
	 * Field _returnLoc.
	 */
	private Location _returnLoc;
	/**
	 * Field _teleportLoc.
	 */
	private Location _teleportLoc;
	/**
	 * Field _spawns.
	 */
	protected List<Spawner> _spawns = new ArrayList<>();
	/**
	 * Field _objects.
	 */
	public List<GameObject> _objects = new ArrayList<>();
	/**
	 * Field _doors.
	 */
	protected IntObjectMap<DoorInstance> _doors = Containers.emptyIntObjectMap();
	/**
	 * Field _zones.
	 */
	protected Map<String, Zone> _zones = Collections.emptyMap();
	/**
	 * Field _spawners.
	 */
	protected Map<String, List<Spawner>> _spawners = Collections.emptyMap();
	/**
	 * Field _visitors.
	 */
	protected TIntHashSet _visitors = new TIntHashSet();
	/**
	 * Field lock.
	 */
	public final Lock lock = new ReentrantLock();
	/**
	 * Field _playerCount.
	 */
	protected int _playerCount;
	/**
	 * Field _party.
	 */
	protected Party _party;
	/**
	 * Field _commandChannel.
	 */
	protected CommandChannel _commandChannel;
	/**
	 * Field _collapseIfEmptyTime.
	 */
	private int _collapseIfEmptyTime;
	/**
	 * Field _isCollapseStarted.
	 */
	private boolean _isCollapseStarted;
	/**
	 * Field _collapseTask.
	 */
	private Future<?> _collapseTask;
	/**
	 * Field _collapse1minTask.
	 */
	private Future<?> _collapse1minTask;
	/**
	 * Field _hiddencollapseTask.
	 */
	private Future<?> _hiddencollapseTask;
	/**
	 * Field listeners.
	 */
	private final ReflectionListenerList listeners = new ReflectionListenerList();
	
	/**
	 * Constructor for Reflection.
	 */
	public Reflection()
	{
		this(_nextId.incrementAndGet());
	}
	
	/**
	 * Constructor for Reflection.
	 * @param id int
	 */
	private Reflection(int id)
	{
		_id = id;
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
	 * Method getInstancedZoneId.
	 * @return int
	 */
	public int getInstancedZoneId()
	{
		return _instance == null ? 0 : _instance.getId();
	}
	
	/**
	 * Method setParty.
	 * @param party Party
	 */
	public void setParty(Party party)
	{
		_party = party;
	}
	
	/**
	 * Method getParty.
	 * @return Party
	 */
	public Party getParty()
	{
		return _party;
	}
	
	/**
	 * Method setCommandChannel.
	 * @param commandChannel CommandChannel
	 */
	public void setCommandChannel(CommandChannel commandChannel)
	{
		_commandChannel = commandChannel;
	}
	
	/**
	 * Method setCollapseIfEmptyTime.
	 * @param value int
	 */
	public void setCollapseIfEmptyTime(int value)
	{
		_collapseIfEmptyTime = value;
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
	 * Method setName.
	 * @param name String
	 */
	protected void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * Method getInstancedZone.
	 * @return InstantZone
	 */
	public InstantZone getInstancedZone()
	{
		return _instance;
	}
	
	/**
	 * Method setInstancedZone.
	 * @param iz InstantZone
	 */
	protected void setInstancedZone(InstantZone iz)
	{
		_instance = iz;
	}
	
	/**
	 * Method getGeoIndex.
	 * @return int
	 */
	public int getGeoIndex()
	{
		return _geoIndex;
	}
	
	/**
	 * Method setGeoIndex.
	 * @param geoIndex int
	 */
	protected void setGeoIndex(int geoIndex)
	{
		_geoIndex = geoIndex;
	}
	
	/**
	 * Method setCoreLoc.
	 * @param l Location
	 */
	public void setCoreLoc(Location l)
	{
		_resetLoc = l;
	}
	
	/**
	 * Method getCoreLoc.
	 * @return Location
	 */
	public Location getCoreLoc()
	{
		return _resetLoc;
	}
	
	/**
	 * Method setReturnLoc.
	 * @param l Location
	 */
	public void setReturnLoc(Location l)
	{
		_returnLoc = l;
	}
	
	/**
	 * Method getReturnLoc.
	 * @return Location
	 */
	public Location getReturnLoc()
	{
		return _returnLoc;
	}
	
	/**
	 * Method setTeleportLoc.
	 * @param l Location
	 */
	public void setTeleportLoc(Location l)
	{
		_teleportLoc = l;
	}
	
	/**
	 * Method getTeleportLoc.
	 * @return Location
	 */
	public Location getTeleportLoc()
	{
		return _teleportLoc;
	}
	
	/**
	 * Method getSpawns.
	 * @return List<Spawner>
	 */
	public List<Spawner> getSpawns()
	{
		return _spawns;
	}
	
	/**
	 * Method getDoors.
	 * @return Collection<DoorInstance>
	 */
	public Collection<DoorInstance> getDoors()
	{
		return _doors.values();
	}
	
	/**
	 * Method getDoor.
	 * @param id int
	 * @return DoorInstance
	 */
	public DoorInstance getDoor(int id)
	{
		return _doors.get(id);
	}
	
	/**
	 * Method getZone.
	 * @param name String
	 * @return Zone
	 */
	public Zone getZone(String name)
	{
		return _zones.get(name);
	}
	
	/**
	 * Method startCollapseTimer.
	 * @param timeInMillis long
	 */
	public void startCollapseTimer(long timeInMillis)
	{
		if (isDefault())
		{
			new Exception("Basic reflection " + _id + " could not be collapsed!").printStackTrace();
			return;
		}
		lock.lock();
		try
		{
			if (_collapseTask != null)
			{
				_collapseTask.cancel(false);
				_collapseTask = null;
			}
			if (_collapse1minTask != null)
			{
				_collapse1minTask.cancel(false);
				_collapse1minTask = null;
			}
			_collapseTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					collapse();
				}
			}, timeInMillis);
			if (timeInMillis >= (60 * 1000L))
			{
				_collapse1minTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						minuteBeforeCollapse();
					}
				}, timeInMillis - (60 * 1000L));
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method stopCollapseTimer.
	 */
	public void stopCollapseTimer()
	{
		lock.lock();
		try
		{
			if (_collapseTask != null)
			{
				_collapseTask.cancel(false);
				_collapseTask = null;
			}
			if (_collapse1minTask != null)
			{
				_collapse1minTask.cancel(false);
				_collapse1minTask = null;
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method minuteBeforeCollapse.
	 */
	public void minuteBeforeCollapse()
	{
		if (_isCollapseStarted)
		{
			return;
		}
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isPlayer())
				{
					((Player) o).sendPacket(new SystemMessage(SystemMessage.THIS_INSTANCE_ZONE_WILL_BE_TERMINATED_IN_S1_MINUTES_YOU_WILL_BE_FORCED_OUT_OF_THE_DANGEON_THEN_TIME_EXPIRES).addNumber(1));
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method collapse.
	 */
	public void collapse()
	{
		if (_id <= 0)
		{
			new Exception("Basic reflection " + _id + " could not be collapsed!").printStackTrace();
			return;
		}
		lock.lock();
		try
		{
			if (_isCollapseStarted)
			{
				return;
			}
			_isCollapseStarted = true;
		}
		finally
		{
			lock.unlock();
		}
		listeners.onCollapse();
		try
		{
			stopCollapseTimer();
			if (_hiddencollapseTask != null)
			{
				_hiddencollapseTask.cancel(false);
				_hiddencollapseTask = null;
			}
			for (Spawner s : _spawns)
			{
				s.deleteAll();
			}
			for (String group : _spawners.keySet())
			{
				despawnByGroup(group);
			}
			for (DoorInstance d : _doors.values())
			{
				d.deleteMe();
			}
			_doors.clear();
			for (Zone zone : _zones.values())
			{
				zone.setActive(false);
			}
			_zones.clear();
			List<Player> teleport = new ArrayList<>();
			List<GameObject> delete = new ArrayList<>();
			lock.lock();
			try
			{
				for (GameObject o : _objects)
				{
					if (o.isPlayer())
					{
						teleport.add((Player) o);
					}
					else if (!o.isPlayable())
					{
						delete.add(o);
					}
				}
			}
			finally
			{
				lock.unlock();
			}
			for (Player player : teleport)
			{
				if (player.getParty() != null)
				{
					if (equals(player.getParty().getReflection()))
					{
						player.getParty().setReflection(null);
					}
					if ((player.getParty().getCommandChannel() != null) && equals(player.getParty().getCommandChannel().getReflection()))
					{
						player.getParty().getCommandChannel().setReflection(null);
					}
				}
				if (equals(player.getReflection()))
				{
					if (getReturnLoc() != null)
					{
						player.teleToLocation(getReturnLoc(), ReflectionManager.DEFAULT);
					}
					else
					{
						player.setReflection(ReflectionManager.DEFAULT);
					}
				}
				onPlayerExit(player);
			}
			if (_commandChannel != null)
			{
				_commandChannel.setReflection(null);
				_commandChannel = null;
			}
			if (_party != null)
			{
				_party.setReflection(null);
				_party = null;
			}
			for (GameObject o : delete)
			{
				o.deleteMe();
			}
			_spawns.clear();
			_objects.clear();
			_visitors.clear();
			_doors.clear();
			_playerCount = 0;
			onCollapse();
		}
		finally
		{
			ReflectionManager.getInstance().remove(this);
			GeoEngine.FreeGeoIndex(getGeoIndex());
		}
	}
	
	/**
	 * Method onCollapse.
	 */
	protected void onCollapse()
	{
	}
	
	/**
	 * Method addObject.
	 * @param o GameObject
	 */
	public void addObject(GameObject o)
	{
		if (_isCollapseStarted)
		{
			return;
		}
		lock.lock();
		try
		{
			_objects.add(o);
			if (o.isPlayer())
			{
				_playerCount++;
				_visitors.add(o.getObjectId());
				onPlayerEnter(o.getPlayer());
			}
		}
		finally
		{
			lock.unlock();
		}
		if ((_collapseIfEmptyTime > 0) && (_hiddencollapseTask != null))
		{
			_hiddencollapseTask.cancel(false);
			_hiddencollapseTask = null;
		}
	}
	
	/**
	 * Method removeObject.
	 * @param o GameObject
	 */
	public void removeObject(GameObject o)
	{
		if (_isCollapseStarted)
		{
			return;
		}
		lock.lock();
		try
		{
			if (!_objects.remove(o))
			{
				return;
			}
			if (o.isPlayer())
			{
				_playerCount--;
				onPlayerExit(o.getPlayer());
			}
		}
		finally
		{
			lock.unlock();
		}
		if ((_playerCount <= 0) && !isDefault() && (_hiddencollapseTask == null))
		{
			if (_collapseIfEmptyTime <= 0)
			{
				collapse();
			}
			else
			{
				_hiddencollapseTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						collapse();
					}
				}, _collapseIfEmptyTime * 60 * 1000L);
			}
		}
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 */
	public void onPlayerEnter(Player player)
	{
		player.getInventory().validateItems();
	}
	
	/**
	 * Method onPlayerExit.
	 * @param player Player
	 */
	public void onPlayerExit(Player player)
	{
		player.getInventory().validateItems();
	}
	
	/**
	 * Method getPlayers.
	 * @return List<Player>
	 */
	public List<Player> getPlayers()
	{
		List<Player> result = new ArrayList<>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isPlayer())
				{
					result.add((Player) o);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	/**
	 * Method getNpcs.
	 * @return List<NpcInstance>
	 */
	public List<NpcInstance> getNpcs()
	{
		List<NpcInstance> result = new ArrayList<>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isNpc())
				{
					result.add((NpcInstance) o);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	/**
	 * Method getAllByNpcId.
	 * @param npcId int
	 * @param onlyAlive boolean
	 * @return List<NpcInstance>
	 */
	public List<NpcInstance> getAllByNpcId(int npcId, boolean onlyAlive)
	{
		List<NpcInstance> result = new ArrayList<>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isNpc())
				{
					NpcInstance npc = (NpcInstance) o;
					if ((npcId == npc.getNpcId()) && (!onlyAlive || !npc.isDead()))
					{
						result.add(npc);
					}
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	/**
	 * Method canChampions.
	 * @return boolean
	 */
	public boolean canChampions()
	{
		return _id <= 0;
	}
	
	/**
	 * Method isAutolootForced.
	 * @return boolean
	 */
	public boolean isAutolootForced()
	{
		return false;
	}
	
	/**
	 * Method isCollapseStarted.
	 * @return boolean
	 */
	public boolean isCollapseStarted()
	{
		return _isCollapseStarted;
	}
	
	/**
	 * Method addSpawn.
	 * @param spawn SimpleSpawner
	 */
	public void addSpawn(SimpleSpawner spawn)
	{
		if (spawn != null)
		{
			_spawns.add(spawn);
		}
	}
	
	/**
	 * Method fillSpawns.
	 * @param si List<InstantZone.SpawnInfo>
	 */
	public void fillSpawns(List<InstantZone.SpawnInfo> si)
	{
		if (si == null)
		{
			return;
		}
		for (InstantZone.SpawnInfo s : si)
		{
			SimpleSpawner c;
			switch (s.getSpawnType())
			{
				case 0:
					for (Location loc : s.getCoords())
					{
						c = new SimpleSpawner(s.getNpcId());
						c.setReflection(this);
						c.setRespawnDelay(s.getRespawnDelay(), s.getRespawnRnd());
						c.setAmount(s.getCount());
						c.setLoc(loc);
						c.doSpawn(true);
						if (s.getRespawnDelay() == 0)
						{
							c.stopRespawn();
						}
						else
						{
							c.startRespawn();
						}
						addSpawn(c);
					}
					break;
				case 1:
					c = new SimpleSpawner(s.getNpcId());
					c.setReflection(this);
					c.setRespawnDelay(s.getRespawnDelay(), s.getRespawnRnd());
					c.setAmount(1);
					c.setLoc(s.getCoords().get(Rnd.get(s.getCoords().size())));
					c.doSpawn(true);
					if (s.getRespawnDelay() == 0)
					{
						c.stopRespawn();
					}
					else
					{
						c.startRespawn();
					}
					addSpawn(c);
					break;
				case 2:
					c = new SimpleSpawner(s.getNpcId());
					c.setReflection(this);
					c.setRespawnDelay(s.getRespawnDelay(), s.getRespawnRnd());
					c.setAmount(s.getCount());
					c.setTerritory(s.getLoc());
					for (int j = 0; j < s.getCount(); j++)
					{
						c.doSpawn(true);
					}
					if (s.getRespawnDelay() == 0)
					{
						c.stopRespawn();
					}
					else
					{
						c.startRespawn();
					}
					addSpawn(c);
			}
		}
	}
	
	/**
	 * Method init.
	 * @param doors IntObjectMap<DoorTemplate>
	 * @param zones Map<String,ZoneTemplate>
	 */
	public void init(IntObjectMap<DoorTemplate> doors, Map<String, ZoneTemplate> zones)
	{
		if (!doors.isEmpty())
		{
			_doors = new HashIntObjectMap<>(doors.size());
		}
		for (DoorTemplate template : doors.values())
		{
			DoorInstance door = new DoorInstance(IdFactory.getInstance().getNextId(), template);
			door.setReflection(this);
			door.setIsInvul(true);
			door.spawnMe(template.getLoc());
			if (template.isOpened())
			{
				door.openMe();
			}
			_doors.put(template.getNpcId(), door);
		}
		initDoors();
		if (!zones.isEmpty())
		{
			_zones = new HashMap<>(zones.size());
		}
		for (ZoneTemplate template : zones.values())
		{
			Zone zone = new Zone(template);
			zone.setReflection(this);
			switch (zone.getType())
			{
				case no_landing:
				case SIEGE:
					zone.addListener(NoLandingZoneListener.STATIC);
					break;
				case AirshipController:
					zone.addListener(new AirshipControllerZoneListener());
					break;
				case RESIDENCE:
					zone.addListener(ResidenceEnterLeaveListenerImpl.STATIC);
					break;
				default:
					break;
			}
			if (template.isEnabled())
			{
				zone.setActive(true);
			}
			_zones.put(template.getName(), zone);
		}
	}
	
	/**
	 * Method init0.
	 * @param doors IntObjectMap<InstantZone.DoorInfo>
	 * @param zones Map<String,InstantZone.ZoneInfo>
	 */
	private void init0(IntObjectMap<InstantZone.DoorInfo> doors, Map<String, InstantZone.ZoneInfo> zones)
	{
		if (!doors.isEmpty())
		{
			_doors = new HashIntObjectMap<>(doors.size());
		}
		for (InstantZone.DoorInfo info : doors.values())
		{
			DoorInstance door = new DoorInstance(IdFactory.getInstance().getNextId(), info.getTemplate());
			door.setReflection(this);
			door.setIsInvul(info.isInvul());
			door.spawnMe(info.getTemplate().getLoc());
			if (info.isOpened())
			{
				door.openMe();
			}
			_doors.put(info.getTemplate().getNpcId(), door);
		}
		initDoors();
		if (!zones.isEmpty())
		{
			_zones = new HashMap<>(zones.size());
		}
		for (InstantZone.ZoneInfo t : zones.values())
		{
			Zone zone = new Zone(t.getTemplate());
			zone.setReflection(this);
			switch (zone.getType())
			{
				case no_landing:
				case SIEGE:
					zone.addListener(NoLandingZoneListener.STATIC);
					break;
				case AirshipController:
					zone.addListener(new AirshipControllerZoneListener());
					break;
				case RESIDENCE:
					zone.addListener(ResidenceEnterLeaveListenerImpl.STATIC);
					break;
				default:
					break;
			}
			if (t.isActive())
			{
				zone.setActive(true);
			}
			_zones.put(t.getTemplate().getName(), zone);
		}
	}
	
	/**
	 * Method initDoors.
	 */
	private void initDoors()
	{
		for (DoorInstance door : _doors.values())
		{
			if (door.getTemplate().getMasterDoor() > 0)
			{
				DoorInstance masterDoor = getDoor(door.getTemplate().getMasterDoor());
				masterDoor.addListener(new MasterOnOpenCloseListenerImpl(door));
			}
		}
	}
	
	/**
	 * Method openDoor.
	 * @param doorId int
	 */
	public void openDoor(int doorId)
	{
		DoorInstance door = _doors.get(doorId);
		if (door != null)
		{
			door.openMe();
		}
	}
	
	/**
	 * Method closeDoor.
	 * @param doorId int
	 */
	public void closeDoor(int doorId)
	{
		DoorInstance door = _doors.get(doorId);
		if (door != null)
		{
			door.closeMe();
		}
	}
	
	/**
	 * Method clearReflection.
	 * @param timeInMinutes int
	 * @param message boolean
	 */
	public void clearReflection(int timeInMinutes, boolean message)
	{
		if (isDefault())
		{
			return;
		}
		for (NpcInstance n : getNpcs())
		{
			n.deleteMe();
		}
		startCollapseTimer(timeInMinutes * 60 * 1000L);
		if (message)
		{
			for (Player pl : getPlayers())
			{
				if (pl != null)
				{
					pl.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(timeInMinutes));
				}
			}
		}
	}
	
	/**
	 * Method addSpawnWithoutRespawn.
	 * @param npcId int
	 * @param loc Location
	 * @param randomOffset int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawnWithoutRespawn(int npcId, Location loc, int randomOffset)
	{
		Location newLoc;
		if (randomOffset > 0)
		{
			newLoc = Location.findPointToStay(loc, 0, randomOffset, getGeoIndex()).setH(loc.h);
		}
		else
		{
			newLoc = loc;
		}
		return NpcUtils.spawnSingle(npcId, newLoc, this);
	}
	
	/**
	 * Method addSpawnWithRespawn.
	 * @param npcId int
	 * @param loc Location
	 * @param randomOffset int
	 * @param respawnDelay int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawnWithRespawn(int npcId, Location loc, int randomOffset, int respawnDelay)
	{
		SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(npcId));
		sp.setLoc(randomOffset > 0 ? Location.findPointToStay(loc, 0, randomOffset, getGeoIndex()) : loc);
		sp.setReflection(this);
		sp.setAmount(1);
		sp.setRespawnDelay(respawnDelay);
		sp.doSpawn(true);
		sp.startRespawn();
		return sp.getLastSpawn();
	}
	
	/**
	 * Method isDefault.
	 * @return boolean
	 */
	public boolean isDefault()
	{
		return getId() <= 0;
	}
	
	/**
	 * Method getVisitors.
	 * @return int[]
	 */
	public int[] getVisitors()
	{
		return _visitors.toArray();
	}
	
	/**
	 * Method setReenterTime.
	 * @param time long
	 */
	public void setReenterTime(long time)
	{
		int[] players = null;
		lock.lock();
		try
		{
			players = _visitors.toArray();
		}
		finally
		{
			lock.unlock();
		}
		if (players != null)
		{
			Player player;
			for (int objectId : players)
			{
				try
				{
					player = World.getPlayer(objectId);
					if (player != null)
					{
						player.setInstanceReuse(getInstancedZoneId(), time);
					}
					else
					{
						mysql.set("REPLACE INTO character_instances (obj_id, id, reuse) VALUES (?,?,?)", objectId, getInstancedZoneId(), time);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Method onCreate.
	 */
	protected void onCreate()
	{
		ReflectionManager.getInstance().add(this);
	}
	
	/**
	 * Method createReflection.
	 * @param id int
	 * @return Reflection
	 */
	public static Reflection createReflection(int id)
	{
		if (id > 0)
		{
			throw new IllegalArgumentException("id should be <= 0");
		}
		return new Reflection(id);
	}
	
	/**
	 * Method init.
	 * @param instantZone InstantZone
	 */
	public void init(InstantZone instantZone)
	{
		setName(instantZone.getName());
		setInstancedZone(instantZone);
		if (instantZone.getMapX() >= 0)
		{
			int geoIndex = GeoEngine.NextGeoIndex(instantZone.getMapX(), instantZone.getMapY(), getId());
			setGeoIndex(geoIndex);
		}
		setTeleportLoc(instantZone.getTeleportCoord());
		if (instantZone.getReturnCoords() != null)
		{
			setReturnLoc(instantZone.getReturnCoords());
		}
		fillSpawns(instantZone.getSpawnsInfo());
		if (instantZone.getSpawns().size() > 0)
		{
			_spawners = new HashMap<>(instantZone.getSpawns().size());
			for (Map.Entry<String, InstantZone.SpawnInfo2> entry : instantZone.getSpawns().entrySet())
			{
				List<Spawner> spawnList = new ArrayList<>(entry.getValue().getTemplates().size());
				_spawners.put(entry.getKey(), spawnList);
				for (SpawnTemplate template : entry.getValue().getTemplates())
				{
					HardSpawner spawner = new HardSpawner(template);
					spawnList.add(spawner);
					spawner.setAmount(template.getCount());
					spawner.setRespawnDelay(template.getRespawn(), template.getRespawnRandom());
					spawner.setReflection(this);
					spawner.setRespawnTime(0);
				}
				if (entry.getValue().isSpawned())
				{
					spawnByGroup(entry.getKey());
				}
			}
		}
		init0(instantZone.getDoors(), instantZone.getZones());
		setCollapseIfEmptyTime(instantZone.getCollapseIfEmpty());
		startCollapseTimer(instantZone.getTimelimit() * 60 * 1000L);
		onCreate();
	}
	
	/**
	 * Method spawnByGroup.
	 * @param name String
	 */
	public void spawnByGroup(String name)
	{
		List<Spawner> list = _spawners.get(name);
		if (list == null)
		{
			throw new IllegalArgumentException(name);
		}
		for (Spawner s : list)
		{
			s.init();
		}
	}
	
	/**
	 * Method despawnByGroup.
	 * @param name String
	 */
	public void despawnByGroup(String name)
	{
		List<Spawner> list = _spawners.get(name);
		if (list == null)
		{
			throw new IllegalArgumentException();
		}
		for (Spawner s : list)
		{
			s.deleteAll();
		}
	}
	
	/**
	 * Method getZones.
	 * @return Collection<Zone>
	 */
	public Collection<Zone> getZones()
	{
		return _zones.values();
	}
	
	/**
	 * Method addListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends Listener<Reflection>> boolean addListener(T listener)
	{
		return listeners.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends Listener<Reflection>> boolean removeListener(T listener)
	{
		return listeners.remove(listener);
	}
}

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
package lineage2.gameserver.templates;

import java.util.List;
import java.util.Map;

import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;

import org.napile.primitive.maps.IntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class InstantZone
{
	/**
	 * @author Mobius
	 */
	public static class DoorInfo
	{
		/**
		 * Field _template.
		 */
		private final DoorTemplate _template;
		/**
		 * Field _opened.
		 */
		private final boolean _opened;
		/**
		 * Field _invul.
		 */
		private final boolean _invul;
		
		/**
		 * Constructor for DoorInfo.
		 * @param template DoorTemplate
		 * @param opened boolean
		 * @param invul boolean
		 */
		public DoorInfo(DoorTemplate template, boolean opened, boolean invul)
		{
			_template = template;
			_opened = opened;
			_invul = invul;
		}
		
		/**
		 * Method getTemplate.
		 * @return DoorTemplate
		 */
		public DoorTemplate getTemplate()
		{
			return _template;
		}
		
		/**
		 * Method isOpened.
		 * @return boolean
		 */
		public boolean isOpened()
		{
			return _opened;
		}
		
		/**
		 * Method isInvul.
		 * @return boolean
		 */
		public boolean isInvul()
		{
			return _invul;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ZoneInfo
	{
		/**
		 * Field _template.
		 */
		private final ZoneTemplate _template;
		/**
		 * Field _active.
		 */
		private final boolean _active;
		
		/**
		 * Constructor for ZoneInfo.
		 * @param template ZoneTemplate
		 * @param opened boolean
		 */
		public ZoneInfo(ZoneTemplate template, boolean opened)
		{
			_template = template;
			_active = opened;
		}
		
		/**
		 * Method getTemplate.
		 * @return ZoneTemplate
		 */
		public ZoneTemplate getTemplate()
		{
			return _template;
		}
		
		/**
		 * Method isActive.
		 * @return boolean
		 */
		public boolean isActive()
		{
			return _active;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SpawnInfo2
	{
		/**
		 * Field _template.
		 */
		private final List<SpawnTemplate> _template;
		/**
		 * Field _spawned.
		 */
		private final boolean _spawned;
		
		/**
		 * Constructor for SpawnInfo2.
		 * @param template List<SpawnTemplate>
		 * @param spawned boolean
		 */
		public SpawnInfo2(List<SpawnTemplate> template, boolean spawned)
		{
			_template = template;
			_spawned = spawned;
		}
		
		/**
		 * Method getTemplates.
		 * @return List<SpawnTemplate>
		 */
		public List<SpawnTemplate> getTemplates()
		{
			return _template;
		}
		
		/**
		 * Method isSpawned.
		 * @return boolean
		 */
		public boolean isSpawned()
		{
			return _spawned;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SpawnInfo
	{
		/**
		 * Field _spawnType.
		 */
		private final int _spawnType;
		/**
		 * Field _npcId.
		 */
		private final int _npcId;
		/**
		 * Field _count.
		 */
		private final int _count;
		/**
		 * Field _respawn.
		 */
		private final int _respawn;
		/**
		 * Field _respawnRnd.
		 */
		private final int _respawnRnd;
		/**
		 * Field _coords.
		 */
		private final List<Location> _coords;
		/**
		 * Field _territory.
		 */
		private final Territory _territory;
		
		/**
		 * Constructor for SpawnInfo.
		 * @param spawnType int
		 * @param npcId int
		 * @param count int
		 * @param respawn int
		 * @param respawnRnd int
		 * @param territory Territory
		 */
		public SpawnInfo(int spawnType, int npcId, int count, int respawn, int respawnRnd, Territory territory)
		{
			this(spawnType, npcId, count, respawn, respawnRnd, null, territory);
		}
		
		/**
		 * Constructor for SpawnInfo.
		 * @param spawnType int
		 * @param npcId int
		 * @param count int
		 * @param respawn int
		 * @param respawnRnd int
		 * @param coords List<Location>
		 */
		public SpawnInfo(int spawnType, int npcId, int count, int respawn, int respawnRnd, List<Location> coords)
		{
			this(spawnType, npcId, count, respawn, respawnRnd, coords, null);
		}
		
		/**
		 * Constructor for SpawnInfo.
		 * @param spawnType int
		 * @param npcId int
		 * @param count int
		 * @param respawn int
		 * @param respawnRnd int
		 * @param coords List<Location>
		 * @param territory Territory
		 */
		public SpawnInfo(int spawnType, int npcId, int count, int respawn, int respawnRnd, List<Location> coords, Territory territory)
		{
			_spawnType = spawnType;
			_npcId = npcId;
			_count = count;
			_respawn = respawn;
			_respawnRnd = respawnRnd;
			_coords = coords;
			_territory = territory;
		}
		
		/**
		 * Method getSpawnType.
		 * @return int
		 */
		public int getSpawnType()
		{
			return _spawnType;
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
		 * Method getCount.
		 * @return int
		 */
		public int getCount()
		{
			return _count;
		}
		
		/**
		 * Method getRespawnDelay.
		 * @return int
		 */
		public int getRespawnDelay()
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
		 * Method getCoords.
		 * @return List<Location>
		 */
		public List<Location> getCoords()
		{
			return _coords;
		}
		
		/**
		 * Method getLoc.
		 * @return Territory
		 */
		public Territory getLoc()
		{
			return _territory;
		}
	}
	
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _resetReuse.
	 */
	private final SchedulingPattern _resetReuse;
	/**
	 * Field _sharedReuseGroup.
	 */
	private final int _sharedReuseGroup;
	/**
	 * Field _timelimit.
	 */
	private final int _timelimit;
	/**
	 * Field _dispelBuffs.
	 */
	private final boolean _dispelBuffs;
	/**
	 * Field _minLevel.
	 */
	private final int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private final int _maxLevel;
	/**
	 * Field _minParty.
	 */
	private final int _minParty;
	/**
	 * Field _maxParty.
	 */
	private final int _maxParty;
	/**
	 * Field _onPartyDismiss.
	 */
	private final boolean _onPartyDismiss;
	/**
	 * Field _timer.
	 */
	private final int _timer;
	/**
	 * Field _teleportCoords.
	 */
	private final List<Location> _teleportCoords;
	/**
	 * Field _returnCoords.
	 */
	private final Location _returnCoords;
	/**
	 * Field _mapx.
	 */
	private final int _mapx;
	/**
	 * Field _mapy.
	 */
	private final int _mapy;
	/**
	 * Field _doors.
	 */
	private final IntObjectMap<DoorInfo> _doors;
	/**
	 * Field _zones.
	 */
	private final Map<String, ZoneInfo> _zones;
	/**
	 * Field _spawns.
	 */
	private final Map<String, SpawnInfo2> _spawns;
	/**
	 * Field _spawnsInfo.
	 */
	private final List<SpawnInfo> _spawnsInfo;
	/**
	 * Field _collapseIfEmpty.
	 */
	private final int _collapseIfEmpty;
	/**
	 * Field _maxChannels.
	 */
	private final int _maxChannels;
	/**
	 * Field _removedItemId.
	 */
	private final int _removedItemId;
	/**
	 * Field _removedItemCount.
	 */
	private final int _removedItemCount;
	/**
	 * Field _removedItemNecessity.
	 */
	private final boolean _removedItemNecessity;
	/**
	 * Field _giveItemId.
	 */
	private final int _giveItemId;
	/**
	 * Field _givedItemCount.
	 */
	private final int _givedItemCount;
	/**
	 * Field _requiredQuestId.
	 */
	private final int _requiredQuestId;
	/**
	 * Field _setReuseUponEntry.
	 */
	private final boolean _setReuseUponEntry;
	/**
	 * Field _addParams.
	 */
	private final StatsSet _addParams;
	/**
	 * Field _entryType.
	 */
	private final InstantZoneEntryType _entryType;
	
	/**
	 * Constructor for InstantZone.
	 * @param id int
	 * @param name String
	 * @param resetReuse SchedulingPattern
	 * @param sharedReuseGroup int
	 * @param timelimit int
	 * @param dispelBuffs boolean
	 * @param minLevel int
	 * @param maxLevel int
	 * @param minParty int
	 * @param maxParty int
	 * @param timer int
	 * @param onPartyDismiss boolean
	 * @param tele List<Location>
	 * @param ret Location
	 * @param mapx int
	 * @param mapy int
	 * @param doors IntObjectMap<DoorInfo>
	 * @param zones Map<String,ZoneInfo>
	 * @param spawns Map<String,SpawnInfo2>
	 * @param spawnsInfo List<SpawnInfo>
	 * @param collapseIfEmpty int
	 * @param maxChannels int
	 * @param removedItemId int
	 * @param removedItemCount int
	 * @param removedItemNecessity boolean
	 * @param giveItemId int
	 * @param givedItemCount int
	 * @param requiredQuestId int
	 * @param setReuseUponEntry boolean
	 * @param params StatsSet
	 */
	public InstantZone(int id, String name, SchedulingPattern resetReuse, int sharedReuseGroup, int timelimit, boolean dispelBuffs, int minLevel, int maxLevel, int minParty, int maxParty, int timer, boolean onPartyDismiss, List<Location> tele, Location ret, int mapx, int mapy, IntObjectMap<DoorInfo> doors, Map<String, ZoneInfo> zones, Map<String, SpawnInfo2> spawns, List<SpawnInfo> spawnsInfo, int collapseIfEmpty, int maxChannels, int removedItemId, int removedItemCount, boolean removedItemNecessity, int giveItemId, int givedItemCount, int requiredQuestId, boolean setReuseUponEntry, StatsSet params)
	{
		_id = id;
		_name = name;
		_resetReuse = resetReuse;
		_sharedReuseGroup = sharedReuseGroup;
		_timelimit = timelimit;
		_dispelBuffs = dispelBuffs;
		_minLevel = minLevel;
		_maxLevel = maxLevel;
		_teleportCoords = tele;
		_returnCoords = ret;
		_minParty = minParty;
		_maxParty = maxParty;
		_onPartyDismiss = onPartyDismiss;
		_timer = timer;
		_mapx = mapx;
		_mapy = mapy;
		_doors = doors;
		_zones = zones;
		_spawnsInfo = spawnsInfo;
		_spawns = spawns;
		_collapseIfEmpty = collapseIfEmpty;
		_maxChannels = maxChannels;
		_removedItemId = removedItemId;
		_removedItemCount = removedItemCount;
		_removedItemNecessity = removedItemNecessity;
		_giveItemId = giveItemId;
		_givedItemCount = givedItemCount;
		_requiredQuestId = requiredQuestId;
		_setReuseUponEntry = setReuseUponEntry;
		_addParams = params;
		if (getMinParty() == 1)
		{
			_entryType = InstantZoneEntryType.SOLO;
		}
		else if ((getMinParty() > 1) && (getMaxParty() <= 7))
		{
			_entryType = InstantZoneEntryType.PARTY;
		}
		else if (getMaxParty() > 7)
		{
			_entryType = InstantZoneEntryType.COMMAND_CHANNEL;
		}
		else
		{
			throw new IllegalArgumentException("Invalid type?: " + _name);
		}
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
	 * Method getResetReuse.
	 * @return SchedulingPattern
	 */
	public SchedulingPattern getResetReuse()
	{
		return _resetReuse;
	}
	
	/**
	 * Method isDispelBuffs.
	 * @return boolean
	 */
	public boolean isDispelBuffs()
	{
		return _dispelBuffs;
	}
	
	/**
	 * Method getTimelimit.
	 * @return int
	 */
	public int getTimelimit()
	{
		return _timelimit;
	}
	
	/**
	 * Method getMinLevel.
	 * @return int
	 */
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	/**
	 * Method getMinParty.
	 * @return int
	 */
	public int getMinParty()
	{
		return _minParty;
	}
	
	/**
	 * Method getMaxParty.
	 * @return int
	 */
	public int getMaxParty()
	{
		return _maxParty;
	}
	
	/**
	 * Method getTimerOnCollapse.
	 * @return int
	 */
	public int getTimerOnCollapse()
	{
		return _timer;
	}
	
	/**
	 * Method isCollapseOnPartyDismiss.
	 * @return boolean
	 */
	public boolean isCollapseOnPartyDismiss()
	{
		return _onPartyDismiss;
	}
	
	/**
	 * Method getTeleportCoord.
	 * @return Location
	 */
	public Location getTeleportCoord()
	{
		if (_teleportCoords.size() == 1)
		{
			return _teleportCoords.get(0);
		}
		return _teleportCoords.get(Rnd.get(_teleportCoords.size()));
	}
	
	/**
	 * Method getReturnCoords.
	 * @return Location
	 */
	public Location getReturnCoords()
	{
		return _returnCoords;
	}
	
	/**
	 * Method getMapX.
	 * @return int
	 */
	public int getMapX()
	{
		return _mapx;
	}
	
	/**
	 * Method getMapY.
	 * @return int
	 */
	public int getMapY()
	{
		return _mapy;
	}
	
	/**
	 * Method getSpawnsInfo.
	 * @return List<SpawnInfo>
	 */
	public List<SpawnInfo> getSpawnsInfo()
	{
		return _spawnsInfo;
	}
	
	/**
	 * Method getSharedReuseGroup.
	 * @return int
	 */
	public int getSharedReuseGroup()
	{
		return _sharedReuseGroup;
	}
	
	/**
	 * Method getCollapseIfEmpty.
	 * @return int
	 */
	public int getCollapseIfEmpty()
	{
		return _collapseIfEmpty;
	}
	
	/**
	 * Method getRemovedItemId.
	 * @return int
	 */
	public int getRemovedItemId()
	{
		return _removedItemId;
	}
	
	/**
	 * Method getRemovedItemCount.
	 * @return int
	 */
	public int getRemovedItemCount()
	{
		return _removedItemCount;
	}
	
	/**
	 * Method getRemovedItemNecessity.
	 * @return boolean
	 */
	public boolean getRemovedItemNecessity()
	{
		return _removedItemNecessity;
	}
	
	/**
	 * Method getGiveItemId.
	 * @return int
	 */
	public int getGiveItemId()
	{
		return _giveItemId;
	}
	
	/**
	 * Method getGiveItemCount.
	 * @return int
	 */
	public int getGiveItemCount()
	{
		return _givedItemCount;
	}
	
	/**
	 * Method getRequiredQuestId.
	 * @return int
	 */
	public int getRequiredQuestId()
	{
		return _requiredQuestId;
	}
	
	/**
	 * Method getSetReuseUponEntry.
	 * @return boolean
	 */
	public boolean getSetReuseUponEntry()
	{
		return _setReuseUponEntry;
	}
	
	/**
	 * Method getMaxChannels.
	 * @return int
	 */
	public int getMaxChannels()
	{
		return _maxChannels;
	}
	
	/**
	 * Method getEntryType.
	 * @return InstantZoneEntryType
	 */
	public InstantZoneEntryType getEntryType()
	{
		return _entryType;
	}
	
	/**
	 * Method getDoors.
	 * @return IntObjectMap<DoorInfo>
	 */
	public IntObjectMap<DoorInfo> getDoors()
	{
		return _doors;
	}
	
	/**
	 * Method getZones.
	 * @return Map<String,ZoneInfo>
	 */
	public Map<String, ZoneInfo> getZones()
	{
		return _zones;
	}
	
	/**
	 * Method getTeleportCoords.
	 * @return List<Location>
	 */
	public List<Location> getTeleportCoords()
	{
		return _teleportCoords;
	}
	
	/**
	 * Method getSpawns.
	 * @return Map<String,SpawnInfo2>
	 */
	public Map<String, SpawnInfo2> getSpawns()
	{
		return _spawns;
	}
	
	/**
	 * Method getAddParams.
	 * @return StatsSet
	 */
	public StatsSet getAddParams()
	{
		return _addParams;
	}
}

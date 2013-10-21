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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.text.StrTable;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameObjectsStorage
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(GameObjectsStorage.class);
	/**
	 * Field STORAGE_PLAYERS.
	 */
	private static final int STORAGE_PLAYERS = 0x00;
	/**
	 * Field STORAGE_SUMMONS.
	 */
	private static final int STORAGE_SUMMONS = 0x01;
	/**
	 * Field STORAGE_NPCS.
	 */
	private static final int STORAGE_NPCS = 0x02;
	/**
	 * Field STORAGE_OTHER.
	 */
	private static final int STORAGE_OTHER = 0x1E;
	/**
	 * Field STORAGE_NONE.
	 */
	private static final int STORAGE_NONE = 0x1F;
	/**
	 * Field storages.
	 */
	@SuppressWarnings("rawtypes")
	private static final GameObjectArray[] storages = new GameObjectArray[STORAGE_NONE];
	static
	{
		storages[STORAGE_PLAYERS] = new GameObjectArray<Player>("PLAYERS", Config.MAXIMUM_ONLINE_USERS, 1);
		storages[STORAGE_SUMMONS] = new GameObjectArray<Playable>("SUMMONS", Config.MAXIMUM_ONLINE_USERS, 1);
		storages[STORAGE_NPCS] = new GameObjectArray<NpcInstance>("NPCS", 60000 * Config.RATE_MOB_SPAWN, 5000);
		storages[STORAGE_OTHER] = new GameObjectArray<>("OTHER", 2000, 1000);
	}
	
	/**
	 * Method getStoragePlayers.
	 * @return GameObjectArray<Player>
	 */
	@SuppressWarnings("unchecked")
	private static GameObjectArray<Player> getStoragePlayers()
	{
		return storages[STORAGE_PLAYERS];
	}
	
	/**
	 * Method getStorageSummons.
	 * @return GameObjectArray<Playable>
	 */
	@SuppressWarnings(
	{
		"unchecked",
		"unused"
	})
	private static GameObjectArray<Playable> getStorageSummons()
	{
		return storages[STORAGE_SUMMONS];
	}
	
	/**
	 * Method getStorageNpcs.
	 * @return GameObjectArray<NpcInstance>
	 */
	@SuppressWarnings("unchecked")
	private static GameObjectArray<NpcInstance> getStorageNpcs()
	{
		return storages[STORAGE_NPCS];
	}
	
	/**
	 * Method selectStorageID.
	 * @param o GameObject
	 * @return int
	 */
	private static int selectStorageID(GameObject o)
	{
		if (o.isNpc())
		{
			return STORAGE_NPCS;
		}
		if (o.isPlayable())
		{
			return o.isPlayer() ? STORAGE_PLAYERS : STORAGE_SUMMONS;
		}
		return STORAGE_OTHER;
	}
	
	/**
	 * Method get.
	 * @param storedId long
	 * @return GameObject
	 */
	public static GameObject get(long storedId)
	{
		int STORAGE_ID;
		if ((storedId == 0) || ((STORAGE_ID = getStorageID(storedId)) == STORAGE_NONE))
		{
			return null;
		}
		GameObject result = storages[STORAGE_ID].get(getStoredIndex(storedId));
		return (result != null) && (result.getObjectId() == getStoredObjectId(storedId)) ? result : null;
	}
	
	/**
	 * Method get.
	 * @param storedId Long
	 * @return GameObject
	 */
	public static GameObject get(Long storedId)
	{
		int STORAGE_ID;
		if ((storedId == null) || (storedId == 0) || ((STORAGE_ID = getStorageID(storedId)) == STORAGE_NONE))
		{
			return null;
		}
		GameObject result = storages[STORAGE_ID].get(getStoredIndex(storedId));
		return (result != null) && (result.getObjectId() == getStoredObjectId(storedId)) ? result : null;
	}
	
	/**
	 * Method isStored.
	 * @param storedId long
	 * @return boolean
	 */
	public static boolean isStored(long storedId)
	{
		int STORAGE_ID;
		if ((storedId == 0) || ((STORAGE_ID = getStorageID(storedId)) == STORAGE_NONE))
		{
			return false;
		}
		GameObject o = storages[STORAGE_ID].get(getStoredIndex(storedId));
		return (o != null) && (o.getObjectId() == getStoredObjectId(storedId));
	}
	
	/**
	 * Method getAsNpc.
	 * @param storedId long
	 * @return NpcInstance
	 */
	public static NpcInstance getAsNpc(long storedId)
	{
		return (NpcInstance) get(storedId);
	}
	
	/**
	 * Method getAsNpc.
	 * @param storedId Long
	 * @return NpcInstance
	 */
	public static NpcInstance getAsNpc(Long storedId)
	{
		return (NpcInstance) get(storedId);
	}
	
	/**
	 * Method getAsPlayer.
	 * @param storedId long
	 * @return Player
	 */
	public static Player getAsPlayer(long storedId)
	{
		return (Player) get(storedId);
	}
	
	/**
	 * Method getAsPlayable.
	 * @param storedId long
	 * @return Playable
	 */
	public static Playable getAsPlayable(long storedId)
	{
		return (Playable) get(storedId);
	}
	
	/**
	 * Method getAsCharacter.
	 * @param storedId long
	 * @return Creature
	 */
	public static Creature getAsCharacter(long storedId)
	{
		return (Creature) get(storedId);
	}
	
	/**
	 * Method getAsMonster.
	 * @param storedId long
	 * @return MonsterInstance
	 */
	public static MonsterInstance getAsMonster(long storedId)
	{
		return (MonsterInstance) get(storedId);
	}
	
	/**
	 * Method getAsPet.
	 * @param storedId long
	 * @return PetInstance
	 */
	public static PetInstance getAsPet(long storedId)
	{
		return (PetInstance) get(storedId);
	}
	
	/**
	 * Method getAsItem.
	 * @param storedId long
	 * @return ItemInstance
	 */
	public static ItemInstance getAsItem(long storedId)
	{
		return (ItemInstance) get(storedId);
	}
	
	/**
	 * Method contains.
	 * @param storedId long
	 * @return boolean
	 */
	public static boolean contains(long storedId)
	{
		return get(storedId) != null;
	}
	
	/**
	 * Method getPlayer.
	 * @param name String
	 * @return Player
	 */
	public static Player getPlayer(String name)
	{
		return getStoragePlayers().findByName(name);
	}
	
	/**
	 * Method getPlayer.
	 * @param objId int
	 * @return Player
	 */
	public static Player getPlayer(int objId)
	{
		return getStoragePlayers().findByObjectId(objId);
	}
	
	/**
	 * Method getAllPlayers.
	 * @return List<Player>
	 */
	public static List<Player> getAllPlayers()
	{
		return getStoragePlayers().getAll();
	}
	
	/**
	 * Method getAllPlayersForIterate.
	 * @return Iterable<Player>
	 */
	public static Iterable<Player> getAllPlayersForIterate()
	{
		return getStoragePlayers();
	}
	
	/**
	 * Method getAllPlayersCount.
	 * @return int
	 */
	public static int getAllPlayersCount()
	{
		return getStoragePlayers().getRealSize();
	}
	
	/**
	 * Method getAllObjectsCount.
	 * @return int
	 */
	public static int getAllObjectsCount()
	{
		int result = 0;
		for (GameObjectArray<?> storage : storages)
		{
			if (storage != null)
			{
				result += storage.getRealSize();
			}
		}
		return result;
	}
	
	/**
	 * Method getAllObjects.
	 * @return List<GameObject>
	 */
	@SuppressWarnings(
	{
		"unchecked", "rawtypes"
	})
	public static List<GameObject> getAllObjects()
	{
		List<GameObject> result = new ArrayList<>(getAllObjectsCount());
		for (GameObjectArray storage : storages)
		{
			if (storage != null)
			{
				storage.getAll(result);
			}
		}
		return result;
	}
	
	/**
	 * Method findObject.
	 * @param objId int
	 * @return GameObject
	 */
	public static GameObject findObject(int objId)
	{
		GameObject result = null;
		for (GameObjectArray<?> storage : storages)
		{
			if (storage != null)
			{
				if ((result = storage.findByObjectId(objId)) != null)
				{
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Field offline_refresh.
	 */
	private static long offline_refresh = 0;
	/**
	 * Field offline_count.
	 */
	private static int offline_count = 0;
	
	/**
	 * Method getAllOfflineCount.
	 * @return int
	 */
	public static int getAllOfflineCount()
	{
		if (!Config.SERVICES_OFFLINE_TRADE_ALLOW)
		{
			return 0;
		}
		long now = System.currentTimeMillis();
		if (now > offline_refresh)
		{
			offline_refresh = now + 10000;
			offline_count = 0;
			for (Player player : getStoragePlayers())
			{
				if (player.isInOfflineMode())
				{
					offline_count++;
				}
			}
		}
		return offline_count;
	}
	
	/**
	 * Method getAllNpcs.
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getAllNpcs()
	{
		return getStorageNpcs().getAll();
	}
	
	/**
	 * Method getAllNpcsForIterate.
	 * @return Iterable<NpcInstance>
	 */
	public static Iterable<NpcInstance> getAllNpcsForIterate()
	{
		return getStorageNpcs();
	}
	
	/**
	 * Method getByNpcId.
	 * @param npc_id int
	 * @return NpcInstance
	 */
	public static NpcInstance getByNpcId(int npc_id)
	{
		NpcInstance result = null;
		for (NpcInstance temp : getStorageNpcs())
		{
			if (npc_id == temp.getNpcId())
			{
				if (!temp.isDead())
				{
					return temp;
				}
				result = temp;
			}
		}
		return result;
	}
	
	/**
	 * Method getAllByNpcId.
	 * @param npc_id int
	 * @param justAlive boolean
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getAllByNpcId(int npc_id, boolean justAlive)
	{
		List<NpcInstance> result = new ArrayList<>();
		for (NpcInstance temp : getStorageNpcs())
		{
			if ((temp.getTemplate() != null) && (npc_id == temp.getTemplate().getNpcId()) && (!justAlive || !temp.isDead()))
			{
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Method getAllByNpcId.
	 * @param npc_ids int[]
	 * @param justAlive boolean
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getAllByNpcId(int[] npc_ids, boolean justAlive)
	{
		List<NpcInstance> result = new ArrayList<>();
		for (NpcInstance temp : getStorageNpcs())
		{
			if (!justAlive || !temp.isDead())
			{
				for (int npc_id : npc_ids)
				{
					if (npc_id == temp.getNpcId())
					{
						result.add(temp);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getNpc.
	 * @param s String
	 * @return NpcInstance
	 */
	public static NpcInstance getNpc(String s)
	{
		List<NpcInstance> npcs = getStorageNpcs().findAllByName(s);
		if (npcs.size() == 0)
		{
			return null;
		}
		for (NpcInstance temp : npcs)
		{
			if (!temp.isDead())
			{
				return temp;
			}
		}
		if (npcs.size() > 0)
		{
			return npcs.remove(npcs.size() - 1);
		}
		return null;
	}
	
	/**
	 * Method getNpc.
	 * @param objId int
	 * @return NpcInstance
	 */
	public static NpcInstance getNpc(int objId)
	{
		return getStorageNpcs().findByObjectId(objId);
	}
	
	/**
	 * Method put.
	 * @param o GameObject
	 * @return long
	 */
	@SuppressWarnings("unchecked")
	public static long put(GameObject o)
	{
		int STORAGE_ID = selectStorageID(o);
		return (o.getObjectId() & 0xFFFFFFFFL) | ((STORAGE_ID & 0x1FL) << 32) | ((storages[STORAGE_ID].add(o) & 0xFFFFFFFFL) << 37);
	}
	
	/**
	 * Method putDummy.
	 * @param o GameObject
	 * @return long
	 */
	public static long putDummy(GameObject o)
	{
		return objIdNoStore(o.getObjectId());
	}
	
	/**
	 * Method objIdNoStore.
	 * @param objId int
	 * @return long
	 */
	public static long objIdNoStore(int objId)
	{
		return (objId & 0xFFFFFFFFL) | ((STORAGE_NONE & 0x1FL) << 32);
	}
	
	/**
	 * Method refreshId.
	 * @param o Creature
	 * @return long
	 */
	public static long refreshId(Creature o)
	{
		return (o.getObjectId() & 0xFFFFFFFFL) | ((o.getStoredId() >> 32) << 32);
	}
	
	/**
	 * Method remove.
	 * @param storedId long
	 * @return GameObject
	 */
	public static GameObject remove(long storedId)
	{
		int STORAGE_ID = getStorageID(storedId);
		return STORAGE_ID == STORAGE_NONE ? null : storages[STORAGE_ID].remove(getStoredIndex(storedId), getStoredObjectId(storedId));
	}
	
	/**
	 * Method getStorageID.
	 * @param storedId long
	 * @return int
	 */
	private static int getStorageID(long storedId)
	{
		return (int) (storedId >> 32) & 0x1F;
	}
	
	/**
	 * Method getStoredIndex.
	 * @param storedId long
	 * @return int
	 */
	private static int getStoredIndex(long storedId)
	{
		return (int) (storedId >> 37);
	}
	
	/**
	 * Method getStoredObjectId.
	 * @param storedId long
	 * @return int
	 */
	public static int getStoredObjectId(long storedId)
	{
		return (int) storedId;
	}
	
	/**
	 * Method getStats.
	 * @return StrTable
	 */
	public static StrTable getStats()
	{
		StrTable table = new StrTable("L2 Objects Storage Stats");
		GameObjectArray<?> storage;
		for (int i = 0; i < storages.length; i++)
		{
			if ((storage = storages[i]) == null)
			{
				continue;
			}
			synchronized (storage)
			{
				table.set(i, "Name", storage.name);
				table.set(i, "Size / Real", storage.size() + " / " + storage.getRealSize());
				table.set(i, "Capacity / init", storage.capacity() + " / " + storage.initCapacity);
			}
		}
		return table;
	}
}

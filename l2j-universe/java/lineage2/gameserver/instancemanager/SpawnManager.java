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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.gameserver.Config;
import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.data.xml.holder.SpawnHolder;
import lineage2.gameserver.listener.game.OnDayNightChangeListener;
import lineage2.gameserver.model.HardSpawner;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.spawn.PeriodOfDay;
import lineage2.gameserver.templates.spawn.SpawnTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SpawnManager
{
	/**
	 * @author Mobius
	 */
	private class Listeners implements OnDayNightChangeListener
	{
		/**
		 * Constructor for Listeners.
		 */
		public Listeners()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDay.
		 * @see lineage2.gameserver.listener.game.OnDayNightChangeListener#onDay()
		 */
		@Override
		public void onDay()
		{
			despawn(PeriodOfDay.NIGHT.name());
			spawn(PeriodOfDay.DAY.name());
		}
		
		/**
		 * Method onNight.
		 * @see lineage2.gameserver.listener.game.OnDayNightChangeListener#onNight()
		 */
		@Override
		public void onNight()
		{
			despawn(PeriodOfDay.DAY.name());
			spawn(PeriodOfDay.NIGHT.name());
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SpawnManager.class);
	/**
	 * Field _instance.
	 */
	private static SpawnManager _instance = new SpawnManager();
	/**
	 * Field _spawns.
	 */
	private final Map<String, List<Spawner>> _spawns = new ConcurrentHashMap<>();
	/**
	 * Field _listeners.
	 */
	private final Listeners _listeners = new Listeners();
	
	/**
	 * Method getInstance.
	 * @return SpawnManager
	 */
	public static SpawnManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for SpawnManager.
	 */
	private SpawnManager()
	{
		for (Map.Entry<String, List<SpawnTemplate>> entry : SpawnHolder.getInstance().getSpawns().entrySet())
		{
			fillSpawn(entry.getKey(), entry.getValue());
		}
		GameTimeController.getInstance().addListener(_listeners);
	}
	
	/**
	 * Method fillSpawn.
	 * @param group String
	 * @param templateList List<SpawnTemplate>
	 * @return List<Spawner>
	 */
	public List<Spawner> fillSpawn(String group, List<SpawnTemplate> templateList)
	{
		if (Config.DONTLOADSPAWN)
		{
			return Collections.emptyList();
		}
		List<Spawner> spawnerList = _spawns.get(group);
		if (spawnerList == null)
		{
			_spawns.put(group, spawnerList = new ArrayList<>(templateList.size()));
		}
		for (SpawnTemplate template : templateList)
		{
			HardSpawner spawner = new HardSpawner(template);
			spawnerList.add(spawner);
			NpcTemplate npcTemplate = NpcHolder.getInstance().getTemplate(spawner.getCurrentNpcId());
			if ((Config.RATE_MOB_SPAWN > 1) && (npcTemplate.getInstanceClass() == MonsterInstance.class) && (npcTemplate.level >= Config.RATE_MOB_SPAWN_MIN_LEVEL) && (npcTemplate.level <= Config.RATE_MOB_SPAWN_MAX_LEVEL))
			{
				spawner.setAmount(template.getCount() * Config.RATE_MOB_SPAWN);
			}
			else
			{
				spawner.setAmount(template.getCount());
			}
			spawner.setRespawnDelay(template.getRespawn(), template.getRespawnRandom());
			spawner.setReflection(ReflectionManager.DEFAULT);
			spawner.setRespawnTime(0);
			if (npcTemplate.isRaid && group.equals(PeriodOfDay.NONE.name()))
			{
				RaidBossSpawnManager.getInstance().addNewSpawn(npcTemplate.getNpcId(), spawner);
			}
		}
		return spawnerList;
	}
	
	/**
	 * Method spawnAll.
	 */
	public void spawnAll()
	{
		spawn(PeriodOfDay.NONE.name());
		if (Config.ALLOW_EVENT_GATEKEEPER)
		{
			spawn("event_gatekeeper");
		}
		if (!Config.ALLOW_CLASS_MASTERS_LIST.isEmpty())
		{
			spawn("class_master");
		}
	}
	
	/**
	 * Method spawn.
	 * @param group String
	 */
	public void spawn(String group)
	{
		List<Spawner> spawnerList = _spawns.get(group);
		if (spawnerList == null)
		{
			return;
		}
		int npcSpawnCount = 0;
		for (Spawner spawner : spawnerList)
		{
			npcSpawnCount += spawner.init();
			//if (((npcSpawnCount % 1000) == 0) && (npcSpawnCount != 0))
			//{
			//	_log.info("SpawnManager: spawned " + npcSpawnCount + " npc for group: " + group);
			//}
		}
		_log.info("SpawnManager: spawned " + npcSpawnCount + " npc; spawns: " + spawnerList.size() + "; group: " + group);
	}
	
	/**
	 * Method despawn.
	 * @param group String
	 */
	public void despawn(String group)
	{
		List<Spawner> spawnerList = _spawns.get(group);
		if (spawnerList == null)
		{
			return;
		}
		for (Spawner spawner : spawnerList)
		{
			spawner.deleteAll();
		}
	}
	
	/**
	 * Method getSpawners.
	 * @param group String
	 * @return List<Spawner>
	 */
	public List<Spawner> getSpawners(String group)
	{
		List<Spawner> list = _spawns.get(group);
		return list == null ? Collections.<Spawner> emptyList() : list;
	}
	
	/**
	 * Method reloadAll.
	 */
	public void reloadAll()
	{
		RaidBossSpawnManager.getInstance().cleanUp();
		for (List<Spawner> spawnerList : _spawns.values())
		{
			for (Spawner spawner : spawnerList)
			{
				spawner.deleteAll();
			}
		}
		RaidBossSpawnManager.getInstance().reloadBosses();
		spawnAll();
		if (GameTimeController.getInstance().isNowNight())
		{
			_listeners.onNight();
		}
		else
		{
			_listeners.onDay();
		}
	}
}

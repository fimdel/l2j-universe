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
package lineage2.gameserver.taskmanager;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SpawnTaskManager
{
	/**
	 * Field _spawnTasks.
	 */
	SpawnTask[] _spawnTasks = new SpawnTask[500];
	/**
	 * Field _spawnTasksSize.
	 */
	int _spawnTasksSize = 0;
	/**
	 * Field spawnTasks_lock.
	 */
	final Object spawnTasks_lock = new Object();
	/**
	 * Field _instance.
	 */
	private static SpawnTaskManager _instance;
	
	/**
	 * Constructor for SpawnTaskManager.
	 */
	public SpawnTaskManager()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnScheduler(), 2000, 2000);
	}
	
	/**
	 * Method getInstance.
	 * @return SpawnTaskManager
	 */
	public static SpawnTaskManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SpawnTaskManager();
		}
		return _instance;
	}
	
	/**
	 * Method addSpawnTask.
	 * @param actor NpcInstance
	 * @param interval long
	 */
	public void addSpawnTask(NpcInstance actor, long interval)
	{
		removeObject(actor);
		addObject(new SpawnTask(actor, System.currentTimeMillis() + interval));
	}
	
	/**
	 * @author Mobius
	 */
	public class SpawnScheduler extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_spawnTasksSize > 0)
			{
				try
				{
					List<NpcInstance> works = new ArrayList<>();
					synchronized (spawnTasks_lock)
					{
						long current = System.currentTimeMillis();
						int size = _spawnTasksSize;
						for (int i = size - 1; i >= 0; i--)
						{
							try
							{
								SpawnTask container = _spawnTasks[i];
								if ((container != null) && (container.endtime > 0) && (current > container.endtime))
								{
									NpcInstance actor = container.getActor();
									if ((actor != null) && (actor.getSpawn() != null))
									{
										works.add(actor);
									}
									container.endtime = -1;
								}
								if ((container == null) || (container.getActor() == null) || (container.endtime < 0))
								{
									if (i == (_spawnTasksSize - 1))
									{
										_spawnTasks[i] = null;
									}
									else
									{
										_spawnTasks[i] = _spawnTasks[_spawnTasksSize - 1];
										_spawnTasks[_spawnTasksSize - 1] = null;
									}
									if (_spawnTasksSize > 0)
									{
										_spawnTasksSize--;
									}
								}
							}
							catch (Exception e)
							{
								_log.error("", e);
							}
						}
					}
					for (NpcInstance work : works)
					{
						Spawner spawn = work.getSpawn();
						if (spawn == null)
						{
							continue;
						}
						spawn.decreaseScheduledCount();
						if (spawn.isDoRespawn())
						{
							spawn.respawnNpc(work);
						}
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
			}
		}
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("============= SpawnTask Manager Report ============\n\r");
		sb.append("Tasks count: ").append(_spawnTasksSize).append("\n\r");
		sb.append("Tasks dump:\n\r");
		long current = System.currentTimeMillis();
		for (SpawnTask container : _spawnTasks)
		{
			sb.append("Class/Name: ").append(container.getClass().getSimpleName()).append('/').append(container.getActor());
			sb.append(" spawn timer: ").append(Util.formatTime((int) (container.endtime - current))).append("\n\r");
		}
		return sb.toString();
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnTask
	{
		/**
		 * Field _npcRef.
		 */
		private final HardReference<NpcInstance> _npcRef;
		/**
		 * Field endtime.
		 */
		public long endtime;
		
		/**
		 * Constructor for SpawnTask.
		 * @param cha NpcInstance
		 * @param delay long
		 */
		SpawnTask(NpcInstance cha, long delay)
		{
			_npcRef = cha.getRef();
			endtime = delay;
		}
		
		/**
		 * Method getActor.
		 * @return NpcInstance
		 */
		public NpcInstance getActor()
		{
			return _npcRef.get();
		}
	}
	
	/**
	 * Method addObject.
	 * @param decay SpawnTask
	 */
	private void addObject(SpawnTask decay)
	{
		synchronized (spawnTasks_lock)
		{
			if (_spawnTasksSize >= _spawnTasks.length)
			{
				SpawnTask[] temp = new SpawnTask[_spawnTasks.length * 2];
				System.arraycopy(_spawnTasks, 0, temp, 0, _spawnTasksSize);
				_spawnTasks = temp;
			}
			_spawnTasks[_spawnTasksSize] = decay;
			_spawnTasksSize++;
		}
	}
	
	/**
	 * Method removeObject.
	 * @param actor NpcInstance
	 */
	public void removeObject(NpcInstance actor)
	{
		synchronized (spawnTasks_lock)
		{
			if (_spawnTasksSize > 1)
			{
				int k = -1;
				for (int i = 0; i < _spawnTasksSize; i++)
				{
					if (_spawnTasks[i].getActor() == actor)
					{
						k = i;
					}
				}
				if (k > -1)
				{
					_spawnTasks[k] = _spawnTasks[_spawnTasksSize - 1];
					_spawnTasks[_spawnTasksSize - 1] = null;
					_spawnTasksSize--;
				}
			}
			else if ((_spawnTasksSize == 1) && (_spawnTasks[0].getActor() == actor))
			{
				_spawnTasks[0] = null;
				_spawnTasksSize = 0;
			}
		}
	}
}

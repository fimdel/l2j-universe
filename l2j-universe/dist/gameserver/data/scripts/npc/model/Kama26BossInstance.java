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
package npc.model;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.reflection.OnReflectionCollapseListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.MinionData;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Kama26BossInstance extends KamalokaBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _spawner.
	 */
	ScheduledFuture<?> _spawner;
	/**
	 * Field _refCollapseListener.
	 */
	private final ReflectionCollapseListener _refCollapseListener = new ReflectionCollapseListener();
	
	/**
	 * Constructor for Kama26BossInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public Kama26BossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		getMinionList().addMinion(new MinionData(18556, 1));
	}
	
	/**
	 * Method notifyMinionDied.
	 * @param minion MinionInstance
	 */
	@Override
	public void notifyMinionDied(MinionInstance minion)
	{
		_spawner = ThreadPoolManager.getInstance().scheduleAtFixedRate(new MinionSpawner(), 60000, 60000);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		getReflection().addListener(_refCollapseListener);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		if (_spawner != null)
		{
			_spawner.cancel(false);
		}
		_spawner = null;
		super.onDeath(killer);
	}
	
	/**
	 * @author Mobius
	 */
	public class MinionSpawner extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isDead() && !getMinionList().hasAliveMinions())
			{
				getMinionList().spawnMinions();
				Functions.npcSayCustomMessage(Kama26BossInstance.this, "Kama26Boss.helpme");
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class ReflectionCollapseListener implements OnReflectionCollapseListener
	{
		/**
		 * Method onReflectionCollapse.
		 * @param ref Reflection
		 * @see lineage2.gameserver.listener.reflection.OnReflectionCollapseListener#onReflectionCollapse(Reflection)
		 */
		@Override
		public void onReflectionCollapse(Reflection ref)
		{
			if (_spawner != null)
			{
				_spawner.cancel(true);
			}
		}
	}
}

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
package instances;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.utils.Location;

public class TautiInstance extends Reflection
{
	private ScheduledFuture<?> firstStageGuardSpawn;
	private final ZoneListener _epicZoneListener;
	boolean _entryLocked;
	boolean _startLaunched;
	
	public TautiInstance()
	{
		_epicZoneListener = new ZoneListener();
		_entryLocked = false;
		_startLaunched = false;
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[tauti_zone_entrace]").addListener(_epicZoneListener);
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		doCleanup();
	}
	
	private void doCleanup()
	{
		if (firstStageGuardSpawn != null)
		{
			firstStageGuardSpawn.cancel(true);
		}
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (_entryLocked)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((player == null) || (!cha.isPlayer()))
			{
				return;
			}
			ThreadPoolManager.getInstance().schedule(new TautiInstance.StartTautiInstance(), 30000L);
			_startLaunched = true;
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	private class FirstStage extends RunnableImpl
	{
		FirstStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(29233, new Location(-147256, 212888, -10088), 0);
		}
	}
	
	private class PreStage extends RunnableImpl
	{
		PreStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.showQuestMovie(SceneMovie.sc_tauti_opening_b);
			}
			ThreadPoolManager.getInstance().schedule(new TautiInstance.FirstStage(), 500L);
		}
	}
	
	private class StartTautiInstance extends RunnableImpl
	{
		StartTautiInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			_entryLocked = true;
			ThreadPoolManager.getInstance().schedule(new TautiInstance.PreStage(), 1000L);
		}
	}
}
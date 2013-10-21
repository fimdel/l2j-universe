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

import org.apache.commons.lang3.ArrayUtils;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

public class KartiaLabyrinth95Solo extends Reflection
{
	private ScheduledFuture<?> firstStageGuardSpawn;
	DeathListener _deathListener;
	private final ZoneListener _epicZoneListener;
	private final ZoneListenerL _landingZoneListener;
	boolean _entryLocked;
	boolean _startLaunched;
	boolean _landingentered;
	
	private static final int DOOR1_ID = 16170002;
	private static final int DOOR2_ID = 16170003;

	private int KartiaGuard = 19226;
	private int KartiaWatchman = 19227;
	private int DimensionalWatchman = 19228;
	private int LordOfKartia = 19255;

	private static final int[] supporter = {33631,33633,33635,33637,33639};
	
	public KartiaLabyrinth95Solo()
	{
		_deathListener = new DeathListener();
		_epicZoneListener = new ZoneListener();
		_landingZoneListener = new ZoneListenerL();
		_landingentered = false;
		_entryLocked = false;
		_startLaunched = false;
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		
		getZone("[kartia_zone1]").addListener(_epicZoneListener);
		getZone("[kartia_zone2]").addListener(_landingZoneListener);
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		doCleanup();
	}
	
	void doCleanup()
	{
		if (firstStageGuardSpawn != null)
		{
			firstStageGuardSpawn.cancel(true);
		}
	}
	
	public class ZoneListenerL implements OnZoneEnterLeaveListener
	{
		public ZoneListenerL()
		{
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (!_landingentered)
			{
				ThreadPoolManager.getInstance().schedule(new TwoCycleStart(), 17999L);
				_landingentered = true;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
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
			ThreadPoolManager.getInstance().schedule(new StartKartiaSolo85(), 30000L);
			_startLaunched = true;
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	private class DeathListener implements OnDeathListener
	{
		DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if ((self.isNpc()) && (self.getNpcId() == DimensionalWatchman))
			{
				ThreadPoolManager.getInstance().schedule(new TwoCycle(), 17000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == LordOfKartia))
			{
				ThreadPoolManager.getInstance().schedule(new CloseInstance(), 9000L);
				self.deleteMe();
			}
		}
	}
	
	private class CloseInstance extends RunnableImpl
	{
		CloseInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			startCollapseTimer(300000L);
			doCleanup();
			for (Player p : getPlayers())
			{
				p.sendPacket(new SystemMessage(2106).addNumber(5));
				p.addExpAndSp(975674677, 8509978, 0, 0, true, false);
			}
		}
	}
	
	private class TowCycleStageSeven extends RunnableImpl
	{
		TowCycleStageSeven()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			NpcInstance kaliospawner = addSpawnWithoutRespawn(LordOfKartia, new Location(-111288, -15784, -11428), 0);
			kaliospawner.addListener(_deathListener);
		}
	}
	
	private class TwoCycleStageSix extends RunnableImpl
	{
		TwoCycleStageSix()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new TowCycleStageSeven(), 90000L);
		}
	}
	
	private class TwoCycleStageFive extends RunnableImpl
	{
		TwoCycleStageFive()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new TwoCycleStageSix(), 60000L);
		}
	}
	
	private class TwoCycleStageFour extends RunnableImpl
	{
		TwoCycleStageFour()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(DimensionalWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new TwoCycleStageFive(), 60000L);
		}
	}
	
	private class TwoCycleStageThree extends RunnableImpl
	{
		TwoCycleStageThree()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new TwoCycleStageFour(), 60000L);
		}
	}
	
	private class TwoCycleStageTwo extends RunnableImpl
	{
		TwoCycleStageTwo()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			closeDoor(DOOR2_ID);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new TwoCycleStageThree(), 60000L);
		}
	}
	
	private class TwoCycleStageOne extends RunnableImpl
	{
		TwoCycleStageOne()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_1, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			for(NpcInstance n : getNpcs())
			{
				if(!ArrayUtils.contains(supporter, n.getNpcId()))
				{
					n.deleteMe();
				}
			}
			openDoor(DOOR2_ID);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new TwoCycleStageTwo(), 60000L);
		}
	}
	
	private class TwoCycleStart extends RunnableImpl
	{
		TwoCycleStart()
		{
		}
		
		@Override
		public void runImpl()
		{
			closeDoor(DOOR1_ID);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110904, -12216, -11594), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110888, -12424, -11591), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110856, -12760, -11594), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111304, -13016, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111288, -12744, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111336, -12424, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111336, -12232, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111736, -12232, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111720, -12488, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(DimensionalWatchman, new Location(-110856, -13080, -11593), 0);
			ThreadPoolManager.getInstance().schedule(new TwoCycleStageOne(), 180000L);
		}
	}
	
	private class TwoCycle extends RunnableImpl
	{
		TwoCycle()
		{
		}
		
		@Override
		public void runImpl()
		{
			openDoor(DOOR1_ID);
		}
	}

	private class ThirdSevenStage extends RunnableImpl
	{
		ThirdSevenStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			NpcInstance sixstagestagemobv = addSpawnWithoutRespawn(DimensionalWatchman, new Location(-110664, -10360, -11883), 0);
			sixstagestagemobv.addListener(_deathListener);
		}
	}

	private class SecondSevenStage extends RunnableImpl
	{
		SecondSevenStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdSevenStage(), 21000L);
		}
	}

	private class SevenStage extends RunnableImpl
	{
		SevenStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondSevenStage(), 21000L);
		}
	}

	private class ThreeSixStage extends RunnableImpl
	{
		ThreeSixStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SevenStage(), 60000L);
		}
	}
	
	private class SecondSixStage extends RunnableImpl
	{
		SecondSixStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ThreeSixStage(), 21000L);
		}
	}
	
	private class SixStage extends RunnableImpl
	{
		SixStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondSixStage(), 21000L);
		}
	}
	
	private class ThirdFiveStage extends RunnableImpl
	{
		ThirdFiveStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SixStage(), 60000L);
		}
	}

	private class SecondFiveStage extends RunnableImpl
	{
		SecondFiveStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdFiveStage(), 21000L);
		}
	}

	private class FiveStage extends RunnableImpl
	{
		FiveStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondFiveStage(), 21000L);
		}
	}
	
	private class ThirdForthStage extends RunnableImpl
	{
		ThirdForthStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new FiveStage(), 60000L);
		}
	}
	
	private class SecondForthStage extends RunnableImpl
	{
		SecondForthStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdForthStage(), 21000L);
		}
	}
	
	private class ForthStage extends RunnableImpl
	{
		ForthStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			ThreadPoolManager.getInstance().schedule(new SecondForthStage(), 21000L);
		}
	}
	
	private class ThirdThirdStage extends RunnableImpl
	{
		ThirdThirdStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ForthStage(), 60000L);
		}
	}

	private class SecondThirdStage extends RunnableImpl
	{
		SecondThirdStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdThirdStage(), 21000L);
		}
	}
	
	private class ThirdStage extends RunnableImpl
	{
		ThirdStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			ThreadPoolManager.getInstance().schedule(new SecondThirdStage(), 21000L);
		}
	}
	
	private class SecondStageSecond extends RunnableImpl
	{
		SecondStageSecond()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdStage(), 60000L);
		}
	}
	
	private class SecondStage extends RunnableImpl
	{
		SecondStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondStageSecond(), 21000L);
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
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_1, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondStage(), 60000L);
		}
	}
	
	private class StartKartiaSolo85 extends RunnableImpl
	{
		StartKartiaSolo85()
		{
		}
		
		@Override
		public void runImpl()
		{
			_entryLocked = true;
			ThreadPoolManager.getInstance().schedule(new FirstStage(), 12000L);
		}
	}
}
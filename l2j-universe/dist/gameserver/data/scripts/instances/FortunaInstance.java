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
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

public class FortunaInstance extends Reflection
{
	private ScheduledFuture<?> firstStageGuardSpawn;
	DeathListener _deathListener;
	private final ZoneListener _epicZoneListener;
	boolean _startLaunched;
	
	public FortunaInstance()
	{
		_deathListener = new DeathListener();
		_epicZoneListener = new ZoneListener();
		_startLaunched = false;
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[fortuna_start]").addListener(_epicZoneListener);
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
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (_startLaunched)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((player == null) || (!cha.isPlayer()))
			{
				return;
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.StartFortunaInstance(), 30000L);
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
			if ((self.isNpc()) && (self.getNpcId() == 52000))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.SelphinaSpawn(), 10000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 52001))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMain(), 30000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 25837))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMain(), 30000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 25840))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMain(), 30000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 25843))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.BonusStageMain(), 30000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 52003))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMain(), 30000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 25841))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMain(), 30000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 25839))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.FinalStage(), 32000L);
				self.deleteMe();
			}
			else if ((self.isNpc()) && (self.getNpcId() == 25846))
			{
				ThreadPoolManager.getInstance().schedule(new FortunaInstance.CloseFortunaInstance(), 35000L);
				self.deleteMe();
			}
		}
	}
	
	private class CloseFortunaInstance extends RunnableImpl
	{
		CloseFortunaInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			startCollapseTimer(300000L);
			doCleanup();
			for (Player p : getPlayers())
			{
				p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5.0D));
			}
		}
	}
	
	private class FinalStageMainMob3SubStage extends RunnableImpl
	{
		FinalStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.CloseFortunaInstance(), 100L);
		}
	}
	
	private class FinalStageMainMob2SubStage extends RunnableImpl
	{
		FinalStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FinalStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class YotemakSpawn extends RunnableImpl
	{
		YotemakSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.YOTEMAK, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			NpcInstance hornapiraid = addSpawnWithoutRespawn(25846, new Location(42104, -175320, -7974, 15956), 0);
			hornapiraid.addListener(_deathListener);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FinalStageMainMob2SubStage(), 11500L);
		}
	}
	
	private class FinalStageMainMob1SubStage extends RunnableImpl
	{
		FinalStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.LAST_STAGE, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.YotemakSpawn(), 11500L);
		}
	}
	
	private class FinalStage extends RunnableImpl
	{
		FinalStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.VELIKOPLEPNO, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FinalStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class SevenStageRaidsSpawn extends RunnableImpl
	{
		SevenStageRaidsSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.MUKSHUANDHOPNAP, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(25838, new Location(42102, -175325, -7974), 0);
			NpcInstance hornapiraid = addSpawnWithoutRespawn(25839, new Location(42104, -175320, -7974, 15956), 0);
			hornapiraid.addListener(_deathListener);
		}
	}
	
	private class SevenStageMainMob6SubStage extends RunnableImpl
	{
		SevenStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageRaidsSpawn(), 19500L);
		}
	}
	
	private class SevenStageMainMob5SubStage extends RunnableImpl
	{
		SevenStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMainMob6SubStage(), 12500L);
		}
	}
	
	private class SevenStageMainMob4SubStage extends RunnableImpl
	{
		SevenStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMainMob5SubStage(), 12500L);
		}
	}
	
	private class SevenStageMainMob3SubStage extends RunnableImpl
	{
		SevenStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMainMob4SubStage(), 12500L);
		}
	}
	
	private class SevenStageMainMob2SubStage extends RunnableImpl
	{
		SevenStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class SevenStageMainMob1SubStage extends RunnableImpl
	{
		SevenStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(23085, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			addSpawnWithoutRespawn(23082, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23080, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(19084, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMainMob2SubStage(), 12500L);
		}
	}
	
	private class SevenStageMain extends RunnableImpl
	{
		SevenStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.POGLOTIVSE, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SevenStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class ResindaSpawn extends RunnableImpl
	{
		ResindaSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.RESINA, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			NpcInstance resindaraid = addSpawnWithoutRespawn(25841, new Location(42104, -175320, -7974, 15956), 0);
			resindaraid.addListener(_deathListener);
		}
	}
	
	private class SixStageMainMob7SubStage extends RunnableImpl
	{
		SixStageMainMob7SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.ResindaSpawn(), 19500L);
		}
	}
	
	private class SixStageMainMob6SubStage extends RunnableImpl
	{
		SixStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob7SubStage(), 12500L);
		}
	}
	
	private class SixStageMainMob5SubStage extends RunnableImpl
	{
		SixStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob6SubStage(), 12500L);
		}
	}
	
	private class SixStageMainMob4SubStage extends RunnableImpl
	{
		SixStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob5SubStage(), 12500L);
		}
	}
	
	private class SixStageMainMob3SubStage extends RunnableImpl
	{
		SixStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob4SubStage(), 12500L);
		}
	}
	
	private class SixStageMainMob2SubStage extends RunnableImpl
	{
		SixStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class SixStageMainMob1SubStage extends RunnableImpl
	{
		SixStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob2SubStage(), 12500L);
		}
	}
	
	private class SixStageMain extends RunnableImpl
	{
		SixStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.VSETOLKONACHINAETSA, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SixStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class EndBonusStageMob extends RunnableImpl
	{
		EndBonusStageMob()
		{
		}
		
		@Override
		public void runImpl()
		{
			NpcInstance krovopiycaSpecial = addSpawnWithoutRespawn(52003, new Location(42104, -176344, -7974, 15956), 0);
			krovopiycaSpecial.addListener(_deathListener);
		}
	}
	
	private class BonusStageMainMob5SubStage extends RunnableImpl
	{
		BonusStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.EndBonusStageMob(), 12500L);
		}
	}
	
	private class BonusStageMainMob4SubStage extends RunnableImpl
	{
		BonusStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.BonusStageMainMob5SubStage(), 12500L);
		}
	}
	
	private class BonusStageMainMob3SubStage extends RunnableImpl
	{
		BonusStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.BonusStageMainMob4SubStage(), 12500L);
		}
	}
	
	private class BonusStageMainMob2SubStage extends RunnableImpl
	{
		BonusStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.BonusStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class BonusStageMainMob1SubStage extends RunnableImpl
	{
		BonusStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.BONUS_STAGE, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(23081, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23081, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.BonusStageMainMob2SubStage(), 12500L);
		}
	}
	
	private class BonusStageMain extends RunnableImpl
	{
		BonusStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 84 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.BonusStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class KonyarSpawn extends RunnableImpl
	{
		KonyarSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.KONYAR, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			NpcInstance konyarraid = addSpawnWithoutRespawn(25843, new Location(42104, -175320, -7974, 15956), 0);
			konyarraid.addListener(_deathListener);
		}
	}
	
	private class FiveStageMainMob8SubStage extends RunnableImpl
	{
		FiveStageMainMob8SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.KonyarSpawn(), 19500L);
		}
	}
	
	private class FiveStageMainMob7SubStage extends RunnableImpl
	{
		FiveStageMainMob7SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob8SubStage(), 12500L);
		}
	}
	
	private class FiveStageMainMob6SubStage extends RunnableImpl
	{
		FiveStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob7SubStage(), 12500L);
		}
	}
	
	private class FiveStageMainMob5SubStage extends RunnableImpl
	{
		FiveStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob6SubStage(), 12500L);
		}
	}
	
	private class FiveStageMainMob4SubStage extends RunnableImpl
	{
		FiveStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 10 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 11 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 12 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 13 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob5SubStage(), 12500L);
		}
	}
	
	private class FiveStageMainMob3SubStage extends RunnableImpl
	{
		FiveStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 98 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 99 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 00 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 01 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob4SubStage(), 12500L);
		}
	}
	
	private class FiveStageMainMob2SubStage extends RunnableImpl
	{
		FiveStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class FiveStageMainMob1SubStage extends RunnableImpl
	{
		FiveStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob2SubStage(), 12500L);
		}
	}
	
	private class FiveStageMain extends RunnableImpl
	{
		FiveStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PRIDETSAOTPRAVITNEMNOGO, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FiveStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class KinnenSpawn extends RunnableImpl
	{
		KinnenSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.KINEN, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			NpcInstance plaksaraid = addSpawnWithoutRespawn(25840, new Location(42104, -175320, -7974, 15956), 0);
			plaksaraid.addListener(_deathListener);
		}
	}
	
	private class FourStageMainMob7SubStage extends RunnableImpl
	{
		FourStageMainMob7SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 33 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 34 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 35 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 36 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.KinnenSpawn(), 19500L);
		}
	}
	
	private class FourStageMainMob6SubStage extends RunnableImpl
	{
		FourStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 21 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 22 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 23 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 24 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob7SubStage(), 12500L);
		}
	}
	
	private class FourStageMainMob5SubStage extends RunnableImpl
	{
		FourStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 09 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 10 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 11 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 12 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob6SubStage(), 12500L);
		}
	}
	
	private class FourStageMainMob4SubStage extends RunnableImpl
	{
		FourStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 97 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 98 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 99 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 00 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob5SubStage(), 12500L);
		}
	}
	
	private class FourStageMainMob3SubStage extends RunnableImpl
	{
		FourStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 85 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 86 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 87 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 88 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob4SubStage(), 12500L);
		}
	}
	
	private class FourStageMainMob2SubStage extends RunnableImpl
	{
		FourStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 73 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 74 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 75 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 76 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class FourStageMainMob1SubStage extends RunnableImpl
	{
		FourStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob2SubStage(), 12500L);
		}
	}
	
	private class FourStageMain extends RunnableImpl
	{
		FourStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.POGLOTITESVET, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.FourStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class PlaksaSpawn extends RunnableImpl
	{
		PlaksaSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.PLAKSA, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			NpcInstance plaksaraid = addSpawnWithoutRespawn(25837, new Location(42104, -175320, -7974, 15956), 0);
			plaksaraid.addListener(_deathListener);
		}
	}
	
	private class FreeStageMainMob7SubStage extends RunnableImpl
	{
		FreeStageMainMob7SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 19 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 20 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 21 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 22 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.PlaksaSpawn(), 19500L);
		}
	}
	
	private class FreeStageMainMob6SubStage extends RunnableImpl
	{
		FreeStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 07 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 08 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 09 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 10 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob7SubStage(), 12500L);
		}
	}
	
	private class FreeStageMainMob5SubStage extends RunnableImpl
	{
		FreeStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 95 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 96 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 97 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 98 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob6SubStage(), 12500L);
		}
	}
	
	private class FreeStageMainMob4SubStage extends RunnableImpl
	{
		FreeStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 83 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 84 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 85 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 86 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob5SubStage(), 12500L);
		}
	}
	
	private class FreeStageMainMob3SubStage extends RunnableImpl
	{
		FreeStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 71 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 72 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 73 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 74 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob4SubStage(), 12500L);
		}
	}
	
	private class FreeStageMainMob2SubStage extends RunnableImpl
	{
		FreeStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 59 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 60 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 61 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 62 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob3SubStage(), 12500L);
		}
	}
	
	private class FreeStageMainMob1SubStage extends RunnableImpl
	{
		FreeStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 44 */for (Player player : getPlayers())
			{
				/* 45 */player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			/* 47 */addSpawnWithoutRespawn(23078, new Location(41448, -175608, -7974), 0);
			/* 48 */addSpawnWithoutRespawn(23078, new Location(42808, -175608, -7974), 0);
			/* 49 */addSpawnWithoutRespawn(23078, new Location(42104, -176344, -7974), 0);
			/* 50 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob2SubStage(), 12500L);
		}
	}
	
	private class FreeStageMain extends RunnableImpl
	{
		FreeStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 32 */for (Player player : getPlayers())
			{
				/* 33 */player.sendPacket(new ExShowScreenMessage(NpcString.POSMOTRIM, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			/* 35 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.FreeStageMainMob1SubStage(), 10000L);
		}
	}
	
	private class EndTwoStageMob extends RunnableImpl
	{
		EndTwoStageMob()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 21 */NpcInstance voitelSpecial = addSpawnWithoutRespawn(52001, new Location(42104, -176344, -7974, 15956), 0);
			/* 22 */voitelSpecial.addListener(_deathListener);
		}
	}
	
	private class TwoStageMainMob6SubStage extends RunnableImpl
	{
		TwoStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 09 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 10 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 11 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 12 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.EndTwoStageMob(), 1400L);
		}
	}
	
	private class TwoStageMainMob5SubStage extends RunnableImpl
	{
		TwoStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 97 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 98 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 99 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 00 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMainMob6SubStage(), 13000L);
		}
	}
	
	private class TwoStageMainMob4SubStage extends RunnableImpl
	{
		TwoStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 85 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 86 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 87 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 88 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMainMob5SubStage(), 13000L);
		}
	}
	
	private class TwoStageMainMob3SubStage extends RunnableImpl
	{
		TwoStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 73 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 74 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 75 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 76 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMainMob4SubStage(), 13000L);
		}
	}
	
	private class TwoStageMainMob2SubStage extends RunnableImpl
	{
		TwoStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 61 */addSpawnWithoutRespawn(23076, new Location(41448, -175608, -7974), 0);
			/* 62 */addSpawnWithoutRespawn(23076, new Location(42808, -175608, -7974), 0);
			/* 63 */addSpawnWithoutRespawn(23076, new Location(42104, -176344, -7974), 0);
			/* 64 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMainMob3SubStage(), 13000L);
		}
	}
	
	private class TwoStageMainMob1SubStage extends RunnableImpl
	{
		TwoStageMainMob1SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 46 */for (Player player : getPlayers())
			{
				/* 47 */player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, 1, -1, true, new String[0]));
			}
			/* 49 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 50 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 51 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 52 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMainMob2SubStage(), 13000L);
		}
	}
	
	private class TwoStageMain extends RunnableImpl
	{
		TwoStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 34 */for (Player player : getPlayers())
			{
				/* 35 */player.sendPacket(new ExShowScreenMessage(NpcString.AETTEKTO, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			/* 37 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMainMob1SubStage(), 30000L);
		}
	}
	
	private class SpawnCubics extends RunnableImpl
	{
		SpawnCubics()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(19082, new Location(41768, -175432, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(41928, -175256, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(42296, -175272, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(42456, -175432, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(42456, -175784, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(42280, -175944, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(41928, -175944, -7980), 0);
			addSpawnWithoutRespawn(19082, new Location(41768, -175784, -7980), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.TwoStageMain(), 22000L);
		}
	}
	
	private class SelphinaSpawn extends RunnableImpl
	{
		SelphinaSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.ZAVECHNIYPOKOI, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			addSpawnWithoutRespawn(33589, new Location(42104, -175320, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.SpawnCubics(), 11000L);
		}
	}
	
	private class EndOneStageMob extends RunnableImpl
	{
		EndOneStageMob()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 88 */NpcInstance yarostSpecial = addSpawnWithoutRespawn(52000, new Location(42104, -176344, -7974, 15956), 0);
			/* 89 */yarostSpecial.addListener(_deathListener);
		}
	}
	
	private class OneStageMainMob7SubStage extends RunnableImpl
	{
		OneStageMainMob7SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 74 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 75 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			
			/* 77 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.EndOneStageMob(), 700L);
		}
	}
	
	private class OneStageMainMob6SubStage extends RunnableImpl
	{
		OneStageMainMob6SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 62 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 63 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 64 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 65 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMainMob7SubStage(), 11400L);
		}
	}
	
	private class OneStageMainMob5SubStage extends RunnableImpl
	{
		OneStageMainMob5SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 50 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 51 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 52 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 53 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMainMob6SubStage(), 11400L);
		}
	}
	
	private class OneStageMainMob4SubStage extends RunnableImpl
	{
		OneStageMainMob4SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 38 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 39 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 40 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 41 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMainMob5SubStage(), 11400L);
		}
	}
	
	private class OneStageMainMob3SubStage extends RunnableImpl
	{
		OneStageMainMob3SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			/* 26 */addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			/* 27 */addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			/* 28 */addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			/* 29 */ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMainMob4SubStage(), 11400L);
		}
	}
	
	private class OneStageMainMob2SubStage extends RunnableImpl
	{
		OneStageMainMob2SubStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMainMob3SubStage(), 11400L);
		}
	}
	
	private class OneStageMain extends RunnableImpl
	{
		OneStageMain()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(23077, new Location(41448, -175608, -7974), 0);
			addSpawnWithoutRespawn(23077, new Location(42808, -175608, -7974), 0);
			addSpawnWithoutRespawn(23077, new Location(42104, -176344, -7974), 0);
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMainMob2SubStage(), 11400L);
		}
	}
	
	private class StartInstance extends RunnableImpl
	{
		StartInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			openDoor(21120001);
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.WHO_POTR_OUR_SAFETY, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.OneStageMain(), 20000L);
		}
	}
	
	private class StartFortunaInstance extends RunnableImpl
	{
		StartFortunaInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			ThreadPoolManager.getInstance().schedule(new FortunaInstance.StartInstance(), 35000L);
		}
	}
}

package instances;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author cruel
 */
public class SpezionNormal extends Reflection
{
	private final static int TELEPORT_CUBE = 32947;
	private final static int SPEZION_NORMAL = 25779;

	private boolean introShowed = false;
	private ScheduledFuture<?> failTask;
	private static NpcInstance TeleportCube;
	private static NpcInstance SpezionBossNormal;
	private DeathListener _deathListener = new DeathListener();
	private static final Location[] TELEPORT_CUBE_COORDS = {
		new Location(218120, 118824, -1789, 63),
		new Location(218136, 118312, -1781, 233),
		new Location(217416, 111576, -1375, 255),
		new Location(216616, 110040, -1331, 97),
		new Location(212456, 116280, -921, 33),
		new Location(210024, 118936, -1381, 15),
		new Location(208472, 120472, -1330, 97),
	};
	private static final int[] Time = {
		300,
		420,
		540
	};
	private int time_stage = 0;

	@Override
	public void onPlayerEnter(final Player player)
	{
		if(!introShowed)
		{
			introShowed = true;
			for(Player p : getPlayers())
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_SC_SPACIA_OPENING);
				ItemFunctions.removeItem(p, 17611, ItemFunctions.getItemCount(player, 17611), true);
			}
			ThreadPoolManager.getInstance().schedule(new Spawn(), 39500L);
		}
		super.onPlayerEnter(player);
	}

	private class Spawn extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			for(Player player : getPlayers())
			{
				player.sendPacket(new ExSendUIEvent(player, 0, 0, Time[time_stage], 0, NpcString.INSTALLATION_CHARGE));
			}

			spawnByGroup("spassia_first_room");
			Location coords = TELEPORT_CUBE_COORDS[Rnd.get(TELEPORT_CUBE_COORDS.length)];
			TeleportCube = addSpawnWithoutRespawn(TELEPORT_CUBE, coords, 0);
			failTask = ThreadPoolManager.getInstance().schedule(new Fail(), Time[time_stage] * 1000);
		}
	}

	private class Fail extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			time_stage++;
			if(time_stage == 3)
			{
				if(failTask != null)
				{
					failTask.cancel(true);
				}
				collapse();
			}
			for(Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.LOCATION_PORTAL_CHANGED, 5000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
				player.sendPacket(new ExSendUIEvent(player, 0, 0, 420, 0, NpcString.INSTALLATION_CHARGE));
			}
			TeleportCube.teleToLocation(TELEPORT_CUBE_COORDS[Rnd.get(TELEPORT_CUBE_COORDS.length)]);
			failTask = ThreadPoolManager.getInstance().schedule(new Fail(), Time[time_stage] * 1000);
		}
	}

	public void SecondRoom()
	{
		if(failTask != null)
		{
			failTask.cancel(true);
		}

		for(Player player : getPlayers())
		{
			player.showQuestMovie(ExStartScenePlayer.SCENE_SC_SPACIA_A);
			player.teleToLocation(new Location(213242, 53235, -8352));
			player.sendPacket(new ExSendUIEvent(player, 1, 1, 0, 0));
		}
		spawnByGroup("spassia_second_room");
	}

	public void openGate(int id)
	{
		if(!getDoor(id).isOpen())
		{
			openDoor(id);
			if(id == 26190004)
			{
				for(Player player : getPlayers())
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.THE_DOOR_OPENED, 5000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
				}
			}
			else
			{
				for(Player player : getPlayers())
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.THE_DOOR_OPENED_SOMEONE_HAS_TO_STAY_AND_WATCH_FOR_A_TIME_BOMB, 5000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
				}
			}
		}
	}

	public void thirdStage()
	{
		spawnByGroup("spassia_third_room");
		for(Player p : getPlayers())
		{
			p.showQuestMovie(ExStartScenePlayer.SCENE_SC_SPACIA_B);
			p.teleToLocation(new Location(184840, 144136, -11830));
		}
	}

	public void spazionSpawn()
	{
		for(Player p : getPlayers())
		{
			p.showQuestMovie(ExStartScenePlayer.SCENE_SC_SPACIA_C);
			p.sendPacket(new ExShowScreenMessage(NpcString.RESCUED_CHANGES_STATE_ONLY_AFTER_EXPOSURE_TO_LIGHT_IT, 5000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
		}
		SpezionBossNormal = addSpawnWithoutRespawn(SPEZION_NORMAL, new Location(184920, 143576, -11794, 0), 0);
		SpezionBossNormal.addListener(_deathListener);

	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == SPEZION_NORMAL)
			{
				for(NpcInstance npc : getNpcs())
				{
					if(npc.getNpcId() == 25780)
					{
						npc.deleteMe();
					}
				}

				for(Player p : getPlayers())
				{
					p.showQuestMovie(ExStartScenePlayer.SCENE_SC_SPACIA_ENDING);
				}
				clearReflection(5, true);
				//addSpawnWithoutRespawn(33385, self.getLoc(), 0); // id npc xz
			}
		}
	}

}
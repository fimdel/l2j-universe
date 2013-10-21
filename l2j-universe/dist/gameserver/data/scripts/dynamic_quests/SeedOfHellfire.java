package dynamic_quests;

import lineage2.gameserver.instancemanager.SoHManager;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.quest.dynamic.DynamicQuest;
import lineage2.gameserver.network.serverpackets.ExDynamicQuestPacket;
import lineage2.gameserver.network.serverpackets.ExDynamicQuestPacket.DynamicQuestInfo;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KilRoy
 */
public class SeedOfHellfire extends DynamicQuest implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(SeedOfHellfire.class);
	private static final String QUEST_ZONE_SOUTH = "[hellfire_south_stage2]";
	private static final String QUEST_ZONE_NORTH = "[hellfire_north_stage2]";

	private static final int QUEST_ID = 15;

	private static final int MIN_LEVEL = 97;
	private static final int MAX_LEVEL = 99;
	private static final int DURATION = 60 * 60;
	private static final int REWARD = 33709;
	private static final int ELITE_REWARD = 35548;
	private static final String START_TIME = "30 * * * *";
	private static final int KILL_SOH_MOBS = 1501;
	private final KillListenerImpl _killListener = new KillListenerImpl();
	private ZoneListener _zoneListener;
	private Zone zoneSouth;
	private Zone zoneNorth;
	private static final int MAX_TASK_POINT = 10000;
	private static final int SMELTING_FURNACE = 19259; // 1
	private static final int COOLING_DEVICE = 19279; // 2
	private static final int WEAPON_STAND = 19280; // 3
	private static final int ARMOR_STAND = 19281; // 3
	private static final int KOJA_THE_ENGINEER = 23223; // 4
	private static final int BOROK_THE_ENGINEER = 23222; // 3
	private static final int ADAK_THE_ENGINEER = 23221; // 2

	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();
		zoneSouth = ReflectionUtils.getZone(QUEST_ZONE_SOUTH);
		zoneSouth.addListener(_zoneListener);
		zoneNorth = ReflectionUtils.getZone(QUEST_ZONE_NORTH);
		zoneNorth.addListener(_zoneListener);
		_log.info("Dynamic Quest: Loaded quest ID " + QUEST_ID + ". Name: Seed of Hellfire - Zone Quest");
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	protected boolean isZoneQuest()
	{
		return true;
	}

	@Override
	protected boolean onStartCondition()
	{
		if(SoHManager.getCurrentStage() == 1)
		{
			return true;
		}
		return false;
	}

	public SeedOfHellfire()
	{
		super(QUEST_ID, DURATION);
		addTask(KILL_SOH_MOBS, MAX_TASK_POINT, TASK_INCREASE_MODE_NO_LIMIT);
		addReward(REWARD, 1);
		addEliteReward(ELITE_REWARD, 1, 1);
		addLevelCheck(MIN_LEVEL, MAX_LEVEL);
		addZoneCheck(QUEST_ZONE_SOUTH, QUEST_ZONE_NORTH);
		initSchedulingPattern(START_TIME);
	}

	@Override
	protected void onStart()
	{
	}

	@Override
	protected void onStop(boolean success)
	{
		for(int objectId : getParticipants())
		{
			Player player = GameObjectsStorage.getPlayer(objectId);
			if(player != null)
			{
				removeParticipant(player);
			}
		}
	}

	@Override
	protected boolean onPlayerEnter(Player player)
	{
		if(player.isInZone(zoneSouth) || player.isInZone(zoneNorth))
		{
			return true;
		}
		return false;
	}

	@Override
	protected void onAddParticipant(Player player)
	{
		player.getListeners().add(_killListener);
	}

	@Override
	protected void onRemoveParticipant(Player player)
	{
		player.getListeners().remove(_killListener);
	}

	@Override
	protected String onRequestHtml(Player player, boolean participant)
	{
		if(getCurrentStep() == 1)
		{
			if(isStarted())
			{
				if(!participant)
				{
					return "dc0015_01_start001.htm";
				}
				else
				{
					return "dc0015_01_context001.htm";
				}
			}
			else if(isSuccessed())
			{
				boolean rewardReceived = rewardReceived(player);
				if(rewardReceived)
				{
					return null;
				}
				else
				{
					return "dc0015_01_reward001.htm";
				}
			}
			else
			{
				return "dc0015_01_failed001.htm";
			}
		}
		return null;
	}

	@Override
	protected String onDialogEvent(String event, Player player)
	{
		String response = null;
		if(event.equals("Reward"))
		{
			tryReward(player);
			response = null;
		}
		else if(event.endsWith(".htm"))
		{
			response = event;
		}
		return response;
	}

	@Override
	protected void onTaskCompleted(int taskId)
	{
		SoHManager.setCurrentStage(2);
	}

	@Override
	protected void onFinish()
	{
	}

	private final class KillListenerImpl implements OnKillListener
	{
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			if(victim.isPlayer())
			{
				return;
			}

			if(!actor.isPlayer())
			{
				return;
			}

			if(victim.isNpc() && isStarted())
			{
				switch(victim.getNpcId())
				{
					case SMELTING_FURNACE:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 1);
						break;
					case COOLING_DEVICE:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 2);
						break;
					case WEAPON_STAND:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 3);
						break;
					case ARMOR_STAND:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 3);
						break;
					case KOJA_THE_ENGINEER:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 4);
						break;
					case BOROK_THE_ENGINEER:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 3);
						break;
					case ADAK_THE_ENGINEER:
						increaseTaskPoint(KILL_SOH_MOBS, actor.getPlayer(), 2);
						break;
				}
			}
		}

		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}

	private final class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature character)
		{
			if(zone == null)
			{
				return;
			}

			if(!character.isPlayer())
			{
				return;
			}

			Player player = character.getPlayer();
			if(isStarted() && !isSuccessed())
			{
				if(!getParticipants().contains(player.getObjectId()))
				{
					addParticipant(player);
				}
				else
				{
					sendQuestInfoParticipant(player);
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature character)
		{
			if(!character.isPlayer())
			{
				return;
			}

			Player player = character.getPlayer();
			if(isStarted() && !isSuccessed())
			{
				if(getParticipants().contains(player.getObjectId()))
				{
					DynamicQuestInfo questInfo = new DynamicQuestInfo(1);
					questInfo.questType = isZoneQuest() ? 1 : 0;
					questInfo.questId = getQuestId();
					questInfo.step = getCurrentStep();
					player.sendPacket(new ExDynamicQuestPacket(questInfo));
				}
			}
		}
	}
}
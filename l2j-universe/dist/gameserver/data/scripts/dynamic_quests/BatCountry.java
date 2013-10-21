package dynamic_quests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.actor.player.OnSocialActionListener;
import lineage2.gameserver.listener.actor.player.OnUseItemListener;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.dynamic.DynamicQuest;
import lineage2.gameserver.network.clientpackets.RequestActionUse;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;

public class BatCountry extends DynamicQuest implements ScriptFile
{
	public static final Logger _log = LoggerFactory.getLogger(BatCountry.class);
	private static final int QUEST_ID = 14;
	private static final int DURATION = 1800; // 30 minutes duration
	private static final String START_TIME = "00 14,18 * * Wed,Sat"; // Start days: Wednesday, Saturday - time: 2.00 p.m., 6.00 p.m.  
	private static final String SPAWN_GROUP = "bat_country_mobs"; //xml spawn
	
	private final OnSocialActionListener _action = new OnSocialActionListenerImpl();
	private final OnUseItemListener _onUseItem = new OnUseItemListenerImpl();
	
	private static final int MIN_LEVEL = 40;
	private static final int MAX_LEVEL = 99;
	// NPC
	private static final int NPC_1 = 33651; // Bear Doll
	private static final int NPC_2 = 33652; // Pumpkin Ghost
	private static final int NPC_3 = 33656; // Makeshift Bat
	private static final int BAT_COLONY = 33658; // mob to kill
	private static final int KILL_BAT_MOBS = 1401; // 1401 task id
	private static final int MAX_TASK_POINT = 100;
	// Items
	private static final int FIRECRACKER = 34761; // Firecracker of Fear - Campaign
	private static final int REWARD_1 = 33500; // Mark of a Lord
	private static final int REWARD_2 = 33501; // Mark of the King
	
	public BatCountry()
	{
		super(QUEST_ID, DURATION);
		addSpawns(SPAWN_GROUP);
		addTask(KILL_BAT_MOBS, MAX_TASK_POINT, TASK_INCREASE_MODE_NO_LIMIT);
		addReward(REWARD_1, 1);
		addEliteReward(REWARD_2, 2, 2);
		addLevelCheck(MIN_LEVEL, MAX_LEVEL);
		initSchedulingPattern(START_TIME);
	}

	@Override
	protected boolean isZoneQuest()
	{
		return false;
	}
	
	@Override
	public void onLoad()
	{
		_log.info("Dynamic Quest: Loaded quest ID "+QUEST_ID+". Name: Bat Country - Campaign");
	}

	@Override
	public void onReload() {
	}

	@Override
	public void onShutdown() {
	}

	@Override
	protected void onStart()
	{
		_log.info("Dynamic Quest: "+QUEST_ID+". Name: Bat Country - Campaign [STARTED]");
	}

	@Override
	protected void onStop(boolean success)
	{
		for (int objectId : getParticipants())
		{
			Player player = GameObjectsStorage.getPlayer(objectId);
			if (player != null)
				removeParticipant(player);
		}
	}

	@Override
	protected void onFinish() {
}

	@Override
	protected String onRequestHtml(Player player, boolean participant)
	{
		if (getCurrentStep() == 1)
		{
			if (isStarted())
			{
				if (!participant)
				{
					return "dc0014_start001.htm";
				}
				return "dc0014_context001.htm";
			}
			else if (isSuccessed())
			{
				boolean rewardReceived = rewardReceived(player);
				if (rewardReceived)
					return "dc0014_reward_received001.htm";
				return "dc0014_reward001.htm";
			}
			else
			{
				return "dc0014_failed001.htm";
			}
		}
		return null;
	}

	@Override
	protected boolean onPlayerEnter(Player player)
	{
		if (getParticipants().contains(player.getObjectId()))
		{
			addParticipant(player);
			return true;
		}
		return false;
	}

	@Override
	protected void onTaskCompleted(int taskId) {
	}

	@Override
	protected String onDialogEvent(String event, Player player)
	{
		String response = null;
		if (event.equals("Accept"))
		{
			addParticipant(player);
			response = "dc0014_context001.htm";
		}
		else if (event.equals("Reward"))
		{
			tryReward(player);
			response = "dc0014_reward_received001.htm";
		}
		else if (event.endsWith(".htm"))
		{
			response = event;
		}
		return response;
	}

	@Override
	protected void onAddParticipant(Player player)
	{
		player.getListeners().add(_onUseItem);
		player.getListeners().add(_action);
	}

	@Override
	protected void onRemoveParticipant(Player player)
	{
		player.getListeners().remove(_onUseItem);
		player.getListeners().remove(_action);
	}

	@Override
	protected boolean onStartCondition()
	{
		return true;
	}
	
	private final class OnSocialActionListenerImpl implements OnSocialActionListener
	{
		public OnSocialActionListenerImpl()
		{
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onSocialAction(Player player, GameObject target, RequestActionUse.Action action)
		{
			if (target != null && target.isNpc())
			{
				NpcInstance npc = (NpcInstance) target;
				double dist = player.getDistance(npc);
				switch (npc.getNpcId())
				{
					case NPC_1:
						if ((action.value == SocialAction.APPLAUD) && (dist < 50) && !npc.isDead())
						{
							player.sendPacket(new ExShowScreenMessage(NpcString.USE_THE_FIRECRACKER_OF_FEAR_ON_THE_BAT_COLONY, 4500, ScreenMessageAlign.TOP_CENTER));
							ItemFunctions.addItem(player, FIRECRACKER, Rnd.get(1,3), true);
							npc.doDie(player);
						}
						break;
					case NPC_2:
						if ((action.value == SocialAction.APPLAUD) && (dist < 50) && !npc.isDead())
						{
							player.sendPacket(new ExShowScreenMessage(NpcString.USE_THE_FIRECRACKER_OF_FEAR_ON_THE_BAT_COLONY, 4500, ScreenMessageAlign.TOP_CENTER));
							ItemFunctions.addItem(player, FIRECRACKER, Rnd.get(1,3), true);
							npc.doDie(player);
						}
						break;
					case NPC_3:
						if ((action.value == SocialAction.APPLAUD) && (dist < 50) && !npc.isDead())
						{
							player.sendPacket(new ExShowScreenMessage(NpcString.USE_THE_FIRECRACKER_OF_FEAR_ON_THE_BAT_COLONY, 4500, ScreenMessageAlign.TOP_CENTER));
							ItemFunctions.addItem(player, FIRECRACKER, Rnd.get(1,3), true);
							npc.doDie(player);
						}
						break;
				}
			}
		}
	}
	
	private final class OnUseItemListenerImpl extends Functions implements OnUseItemListener
	{

		public OnUseItemListenerImpl()
		{
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onUseItem(Player player, GameObject target, ItemInstance item)
		{
			if (target != null && target.isNpc())
			{
				NpcInstance npc = (NpcInstance) target;
				double dist = player.getDistance(npc);
				item = player.getInventory().getItemByItemId(FIRECRACKER);
				if ((item != null) && (dist < 70) && (npc.getNpcId() == BAT_COLONY) && !npc.isDead())
				{
					increaseTaskPoint(KILL_BAT_MOBS, player, 1);
					npc.doDie(player);
				}
			}
		}
	}
}

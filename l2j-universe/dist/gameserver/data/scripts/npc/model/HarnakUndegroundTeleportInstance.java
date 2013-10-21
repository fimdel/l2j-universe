package npc.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.HarnakUndegroundManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author KilRoy
 */
public final class HarnakUndegroundTeleportInstance extends NpcInstance
{
	private static final long serialVersionUID = -3733574033243896045L;

	private static final Map<Integer, Location> teleCoords = new HashMap<Integer, Location>();
	private static final int[] cycleStartTeleports = { 33306, 33314, 33322 };
	private static final int[] cycleEndTeleports = { 33313, 33321, 33329 };
	private static final int SEALED_HARNAK_KEY = 30429;
	private static final int HARNAK_KEY = 30430;
	private static int NOKTUM = 25771;
	private static int DEMONIC_NOKTUM = 25773;
	private DeathListener _deathListener = new DeathListener();
	private int _stateSecond = 0;
	private int _stateThird = 0;
	private static final NpcString[] FUCKED_HARNAK = { 
		NpcString.THERES_A_NOBLE_KING_WHO_DID_NOT_FALL_BEFORE_THE_HAMMER_OF_OLD_GODS, 
		NpcString.ALL_THE_GIGANTS_TRUSTED_THEIR_LIVES_ON_THAT_COURAGE, 
		NpcString.SAVING_THE_LIVES_WITH_BEAUTIFUL_LADY_BELIS_SLASHING_THE_GODS, 
		NpcString.EVEN_THE_FIVE_DRAGONS_OF_DARKNESS_SWALLOWED_THE_FLAMES_IN_REVERENCE, 
		NpcString.SWORD_RUSTS_AND_MAGIC_IS_FORGOTTEN, 
		NpcString.LADY_GOES_UNDER_THE_VEIL_AND_THE_KING_FALLS_ON_THE_DUST, 
		NpcString.WHO_REMEMBERS_THE_HALLS_OF_HARNAK_AND_POWERS_OF_PLATINUM, 
		NpcString.ONLY_THE_SOULS_LEFT_TO_WEEP_IN_THE_ARMS_OF_EIGHT_KNIGHTS };

	public HarnakUndegroundTeleportInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		teleCoords.put(Integer.valueOf(33306), new Location(-114700, 145282, -7680));
		teleCoords.put(Integer.valueOf(33307), new Location(-112811, 146063, -7680));
		teleCoords.put(Integer.valueOf(33308), new Location(-111346, 147920, -7680));
		teleCoords.put(Integer.valueOf(33309), new Location(-112716, 151218, -7688));
		teleCoords.put(Integer.valueOf(33310), new Location(-114711, 150314, -7680));
		teleCoords.put(Integer.valueOf(33311), new Location(-116710, 151179, -7680));
		teleCoords.put(Integer.valueOf(33312), new Location(-118080, 147916, -7680));
		teleCoords.put(Integer.valueOf(33313), new Location(-116610, 146042, -7680));

		teleCoords.put(Integer.valueOf(33314), new Location(-114712, 147016, -10760));
		teleCoords.put(Integer.valueOf(33315), new Location(-113128, 147720, -10760));
		teleCoords.put(Integer.valueOf(33316), new Location(-111768, 149272, -10760));
		teleCoords.put(Integer.valueOf(33317), new Location(-112712, 152120, -10760));
		teleCoords.put(Integer.valueOf(33318), new Location(-114712, 151384, -10760));
		teleCoords.put(Integer.valueOf(33319), new Location(-116712, 152056, -10760));
		teleCoords.put(Integer.valueOf(33320), new Location(-117672, 149256, -10760));
		teleCoords.put(Integer.valueOf(33321), new Location(-116456, 147560, -10760));

		teleCoords.put(Integer.valueOf(33322), new Location(-114689, 180723, -13808));
		teleCoords.put(Integer.valueOf(33323), new Location(-112841, 181530, -13808));
		teleCoords.put(Integer.valueOf(33324), new Location(-111350, 183341, -13800));
		teleCoords.put(Integer.valueOf(33325), new Location(-112714, 186547, -13808));
		teleCoords.put(Integer.valueOf(33326), new Location(-114706, 185708, -13808));
		teleCoords.put(Integer.valueOf(33327), new Location(-116702, 186551, -13808));
		teleCoords.put(Integer.valueOf(33328), new Location(-118084, 183353, -13800));
		teleCoords.put(Integer.valueOf(33329), new Location(-116591, 181492, -13808));
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;
		
		if(command.startsWith("teleport_harnak"))
		{
			if(player.getLevel() >= 85 && player.isAwaking())
				player.teleToLocation(-114712, 147848, -7740);
			else
				showChatWindow(player, "default/33344-noreq.htm");
		}
		if(command.startsWith("teleport_first_floor"))
		{
			player.teleToLocation(-114712, 147848, -7740);
		}
		if(command.startsWith("teleport_second_floor"))
		{
			player.teleToLocation(-114712, 149160, -10800);
		}
		if(command.startsWith("teleport_third_floor"))
		{
			player.teleToLocation(-114728, 183288, -13860);
		}
		if(command.startsWith("teleport_outside"))
		{
			player.teleToLocation(-116160, 236370, -3088);
		}
		if(command.startsWith("teleport_next"))
		{
			
			List<int[]> lst = Arrays.asList(cycleEndTeleports);
			if (lst.contains(getNpcId()))
				player.teleToLocation((Location)teleCoords.get(Integer.valueOf(getNpcId() - 7)));
			else
				player.teleToLocation((Location)teleCoords.get(Integer.valueOf(getNpcId() + 1)));
		}
		if(command.startsWith("teleport_prev"))
		{
			List<int[]> lst = Arrays.asList(cycleStartTeleports);
			if (lst.contains(getNpcId()))
				player.teleToLocation((Location)teleCoords.get(Integer.valueOf(getNpcId() + 7)));
			else
				player.teleToLocation((Location)teleCoords.get(Integer.valueOf(getNpcId() - 1)));
		}
		if(command.startsWith("key_altar"))
		{
			if(player.getInventory().getItemByItemId(SEALED_HARNAK_KEY) != null)
			{
				if(HarnakUndegroundManager.getInstance().addSecondStagePoint(getNpcId(), player))
				{
					showChatWindow(player, "default/altar-ok.htm");
					player.getInventory().removeItemByItemId(30429, 1);
					sayScreenMessage(player);
					if(getNpcState() == 0)
						ThreadPoolManager.getInstance().schedule(new runNpcStateChangeSecond(), 1000L);
					NpcInstance Noktum = NpcUtils.spawnSingle(NOKTUM, player.getLoc().getX() + 15, player.getLoc().getY(), player.getLoc().getZ(), 1800000L);
					Noktum.addListener(_deathListener);
				}
				else if(player.isInParty() && player.getParty().isLeader(player))
				{
					sayScreenMessage(player);
					NpcInstance Noktum = NpcUtils.spawnSingle(NOKTUM, player.getLoc().getX() + 15, player.getLoc().getY(), player.getLoc().getZ(), 1800000L);
					Noktum.addListener(_deathListener);
				}
				else
					showChatWindow(player, "default/altar-no.htm");
			}
			else
				showChatWindow(player, "default/"+getNpcId()+"-1.htm");
		}
		if(command.startsWith("key_prison"))
		{
			if(player.getInventory().getItemByItemId(HARNAK_KEY) != null)
			{
				if(HarnakUndegroundManager.getInstance().addThirdStagePoint(getNpcId(), player))
				{
					showChatWindow(player, "default/prison-ok.htm");
					player.getInventory().removeItemByItemId(30430, 1);
					sayScreenMessage(player);
					if(getNpcState() == 0)
						ThreadPoolManager.getInstance().schedule(new runNpcStateChangeThird(), 1000L);
					NpcInstance demonikNoktum = NpcUtils.spawnSingle(DEMONIC_NOKTUM, player.getLoc().getX() + 15, player.getLoc().getY(), player.getLoc().getZ(), 1800000L);
					demonikNoktum.addListener(_deathListener);
				}
				else if(player.isInParty() && player.getParty().isLeader(player))
				{
					sayScreenMessage(player);
					NpcInstance demonikNoktum = NpcUtils.spawnSingle(DEMONIC_NOKTUM, player.getLoc().getX() + 15, player.getLoc().getY(), player.getLoc().getZ(), 1800000L);
					demonikNoktum.addListener(_deathListener);
				}
				else
					showChatWindow(player, "default/prison-no.htm");
			}
			else
				showChatWindow(player, "default/"+getNpcId()+"-1.htm");
		}
		else
			super.onBypassFeedback(player, command);
	}

	private class runNpcStateChangeSecond extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			int npcState = getNpcState();
			if(npcState == 0)
			{
				_stateSecond = 1;
				setNpcState(npcState + 1);
				ThreadPoolManager.getInstance().schedule(this, 180000L);
			}
			if(npcState >= 1 && npcState < 8)
			{
				_stateSecond++;
				setNpcState(_stateSecond);
				ThreadPoolManager.getInstance().schedule(this, 120000L);
			}
			if(npcState == 8)
			{
				_stateSecond = 0;
				setNpcState(9);
				setNpcState(_stateSecond);
				HarnakUndegroundManager.getInstance().requestRemoveNpcFromListSecond(getNpcId());
			}
		}
	}

	private void sayScreenMessage(Player player)
	{
		if(player.isInParty())
		{
			for(Player players : player.getParty().getPartyMembers())
			{
				if(players.isInRange(player, 1000))
					players.sendPacket(new ExShowScreenMessage(Rnd.get(FUCKED_HARNAK), 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
			}
		}
		else
			player.sendPacket(new ExShowScreenMessage(Rnd.get(FUCKED_HARNAK), 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));

	}

	private class runNpcStateChangeThird extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			int npcState = getNpcState();
			if(npcState == 0)
			{
				_stateThird = 1;
				setNpcState(_stateThird);
				ThreadPoolManager.getInstance().schedule(this, 180000L);
			}
			if(npcState >= 1 && npcState < 8)
			{
				_stateThird++;
				setNpcState(_stateThird);
				ThreadPoolManager.getInstance().schedule(this, 120000L);
			}
			if(npcState == 8)
			{
				_stateThird = 0;
				setNpcState(9);
				setNpcState(_stateThird);
				HarnakUndegroundManager.getInstance().requestRemoveNpcFromListThird(getNpcId());
			}
		}
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(final Creature self, final Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == NOKTUM)
			{
				self.broadcastPacketToOthers(new ExShowScreenMessage(NpcString.GHOST_OF_HARNAK_CAN_ONLY_BE_HIT_NEAR_SOUL_CIRCLE, 5000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
				self.removeListener(_deathListener);

			}
			if(self.isNpc() && self.getNpcId() == DEMONIC_NOKTUM)
			{
				self.broadcastPacketToOthers(new ExShowScreenMessage(NpcString.GHOST_OF_HARNAK_CAN_ONLY_BE_HIT_NEAR_SOUL_CIRCLE, 5000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
				self.removeListener(_deathListener);
			}
		}
	}
}
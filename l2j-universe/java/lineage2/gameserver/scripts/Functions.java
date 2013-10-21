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
package lineage2.gameserver.scripts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.MapUtils;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.Strings;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Functions
{
	/**
	 * Field self.
	 */
	public HardReference<Player> self = HardReferences.emptyRef();
	/**
	 * Field npc.
	 */
	public HardReference<NpcInstance> npc = HardReferences.emptyRef();
	
	/**
	 * Method executeTask.
	 * @param caller Player
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param variables Map<String,Object>
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public static ScheduledFuture<?> executeTask(final Player caller, final String className, final String methodName, final Object[] args, final Map<String, Object> variables, long delay)
	{
		return ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				callScripts(caller, className, methodName, args, variables);
			}
		}, delay);
	}
	
	/**
	 * Method executeTask.
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param variables Map<String,Object>
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public static ScheduledFuture<?> executeTask(String className, String methodName, Object[] args, Map<String, Object> variables, long delay)
	{
		return executeTask(null, className, methodName, args, variables, delay);
	}
	
	/**
	 * Method executeTask.
	 * @param player Player
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public static ScheduledFuture<?> executeTask(Player player, String className, String methodName, Object[] args, long delay)
	{
		return executeTask(player, className, methodName, args, null, delay);
	}
	
	/**
	 * Method executeTask.
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public static ScheduledFuture<?> executeTask(String className, String methodName, Object[] args, long delay)
	{
		return executeTask(className, methodName, args, null, delay);
	}
	
	/**
	 * Method callScripts.
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @return Object
	 */
	public static Object callScripts(String className, String methodName, Object[] args)
	{
		return callScripts(className, methodName, args, null);
	}
	
	/**
	 * Method callScripts.
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param variables Map<String,Object>
	 * @return Object
	 */
	public static Object callScripts(String className, String methodName, Object[] args, Map<String, Object> variables)
	{
		return callScripts(null, className, methodName, args, variables);
	}
	
	/**
	 * Method callScripts.
	 * @param player Player
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param variables Map<String,Object>
	 * @return Object
	 */
	public static Object callScripts(Player player, String className, String methodName, Object[] args, Map<String, Object> variables)
	{
		return Scripts.getInstance().callScripts(player, className, methodName, args, variables);
	}
	
	/**
	 * Method show.
	 * @param text String
	 * @param self Player
	 */
	public void show(String text, Player self)
	{
		show(text, self, getNpc());
	}
	
	/**
	 * Method show.
	 * @param text String
	 * @param self Player
	 * @param npc NpcInstance
	 * @param arg Object[]
	 */
	public static void show(String text, Player self, NpcInstance npc, Object... arg)
	{
		if ((text == null) || (self == null))
		{
			return;
		}
		NpcHtmlMessage msg = new NpcHtmlMessage(self, npc);
		if (text.endsWith(".html") || text.endsWith(".htm"))
		{
			msg.setFile(text);
		}
		else
		{
			msg.setHtml(Strings.bbParse(text));
		}
		if ((arg != null) && ((arg.length % 2) == 0))
		{
			for (int i = 0; i < arg.length; i = +2)
			{
				msg.replace(String.valueOf(arg[i]), String.valueOf(arg[i + 1]));
			}
		}
		self.sendPacket(msg);
	}
	
	/**
	 * Method show.
	 * @param message CustomMessage
	 * @param self Player
	 */
	public static void show(CustomMessage message, Player self)
	{
		show(message.toString(), self, null);
	}
	
	/**
	 * Method sendMessage.
	 * @param text String
	 * @param self Player
	 */
	public static void sendMessage(String text, Player self)
	{
		self.sendMessage(text);
	}
	
	/**
	 * Method sendMessage.
	 * @param message CustomMessage
	 * @param self Player
	 */
	public static void sendMessage(CustomMessage message, Player self)
	{
		self.sendMessage(message);
	}
	
	/**
	 * Method npcSayInRange.
	 * @param npc NpcInstance
	 * @param text String
	 * @param range int
	 */
	public static void npcSayInRange(NpcInstance npc, String text, int range)
	{
		npcSayInRange(npc, range, NpcString.NONE, text);
	}
	
	/**
	 * Method npcSayInRange.
	 * @param npc NpcInstance
	 * @param range int
	 * @param fStringId NpcString
	 * @param params String[]
	 */
	public static void npcSayInRange(NpcInstance npc, int range, NpcString fStringId, String... params)
	{
		if (npc == null)
		{
			return;
		}
		NpcSay cs = new NpcSay(npc, ChatType.NPC_SAY, fStringId, params);
		for (Player player : World.getAroundPlayers(npc, range, Math.max(range / 2, 200)))
		{
			if (npc.getReflection() == player.getReflection())
			{
				player.sendPacket(cs);
			}
		}
	}
	
	/**
	 * Method npcSay.
	 * @param npc NpcInstance
	 * @param text String
	 */
	public static void npcSay(NpcInstance npc, String text)
	{
		npcSayInRange(npc, text, 1500);
	}
	
	/**
	 * Method npcSay.
	 * @param npc NpcInstance
	 * @param npcString NpcString
	 * @param params String[]
	 */
	public static void npcSay(NpcInstance npc, NpcString npcString, String... params)
	{
		npcSayInRange(npc, 1500, npcString, params);
	}
	
	/**
	 * Method npcSayInRangeCustomMessage.
	 * @param npc NpcInstance
	 * @param range int
	 * @param address String
	 * @param replacements Object[]
	 */
	public static void npcSayInRangeCustomMessage(NpcInstance npc, int range, String address, Object... replacements)
	{
		if (npc == null)
		{
			return;
		}
		for (Player player : World.getAroundPlayers(npc, range, Math.max(range / 2, 200)))
		{
			if (npc.getReflection() == player.getReflection())
			{
				player.sendPacket(new NpcSay(npc, ChatType.NPC_SAY, new CustomMessage(address, player, replacements).toString()));
			}
		}
	}
	
	/**
	 * Method npcSayCustomMessage.
	 * @param npc NpcInstance
	 * @param address String
	 * @param replacements Object[]
	 */
	public static void npcSayCustomMessage(NpcInstance npc, String address, Object... replacements)
	{
		npcSayInRangeCustomMessage(npc, 1500, address, replacements);
	}
	
	/**
	 * Method npcSayToPlayer.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param text String
	 */
	public static void npcSayToPlayer(NpcInstance npc, Player player, String text)
	{
		npcSayToPlayer(npc, player, NpcString.NONE, ChatType.TELL, text);
	}
	
	/**
	 * Method npcSayToPlayer.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param npcString NpcString
	 * @param params String[]
	 */
	public static void npcSayToPlayer(NpcInstance npc, Player player, NpcString npcString, String... params)
	{
		if (npc == null)
		{
			return;
		}
		npcSayToPlayer(npc, player, npcString, ChatType.TELL, params);
	}
	
	/**
	 * Method npcSayToPlayer.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param npcString NpcString
	 * @param chatType ChatType
	 * @param params String[]
	 */
	public static void npcSayToPlayer(NpcInstance npc, Player player, NpcString npcString, ChatType chatType, String... params)
	{
		if (npc == null)
		{
			return;
		}
		player.sendPacket(new NpcSay(npc, chatType, npcString, params));
	}
	
	/**
	 * Method npcShout.
	 * @param npc NpcInstance
	 * @param text String
	 */
	public static void npcShout(NpcInstance npc, String text)
	{
		npcShout(npc, NpcString.NONE, text);
	}
	
	/**
	 * Method npcShout.
	 * @param npc NpcInstance
	 * @param npcString NpcString
	 * @param params String[]
	 */
	public static void npcShout(NpcInstance npc, NpcString npcString, String... params)
	{
		if (npc == null)
		{
			return;
		}
		NpcSay cs = new NpcSay(npc, ChatType.SHOUT, npcString, params);
		int rx = MapUtils.regionX(npc);
		int ry = MapUtils.regionY(npc);
		int offset = Config.SHOUT_OFFSET;
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player.getReflection() != npc.getReflection())
			{
				continue;
			}
			int tx = MapUtils.regionX(player);
			int ty = MapUtils.regionY(player);
			if ((tx >= (rx - offset)) && (tx <= (rx + offset)) && (ty >= (ry - offset)) && (ty <= (ry + offset)))
			{
				player.sendPacket(cs);
			}
		}
	}
	
	/**
	 * Method npcShoutCustomMessage.
	 * @param npc NpcInstance
	 * @param address String
	 * @param replacements Object[]
	 */
	public static void npcShoutCustomMessage(NpcInstance npc, String address, Object... replacements)
	{
		if (npc == null)
		{
			return;
		}
		int rx = MapUtils.regionX(npc);
		int ry = MapUtils.regionY(npc);
		int offset = Config.SHOUT_OFFSET;
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player.getReflection() != npc.getReflection())
			{
				continue;
			}
			int tx = MapUtils.regionX(player);
			int ty = MapUtils.regionY(player);
			if (((tx >= (rx - offset)) && (tx <= (rx + offset)) && (ty >= (ry - offset)) && (ty <= (ry + offset))) || npc.isInRange(player, Config.CHAT_RANGE))
			{
				player.sendPacket(new NpcSay(npc, ChatType.SHOUT, new CustomMessage(address, player, replacements).toString()));
			}
		}
	}
	
	/**
	 * Method npcSay.
	 * @param npc NpcInstance
	 * @param address NpcString
	 * @param type ChatType
	 * @param range int
	 * @param replacements String[]
	 */
	public static void npcSay(NpcInstance npc, NpcString address, ChatType type, int range, String... replacements)
	{
		if (npc == null)
		{
			return;
		}
		for (Player player : World.getAroundPlayers(npc, range, Math.max(range / 2, 200)))
		{
			if (player.getReflection() == npc.getReflection())
			{
				player.sendPacket(new NpcSay(npc, type, address, replacements));
			}
		}
	}
	
	/**
	 * Method addItem.
	 * @param playable Playable
	 * @param itemId int
	 * @param count long
	 */
	public static void addItem(Playable playable, int itemId, long count)
	{
		ItemFunctions.addItem(playable, itemId, count, true);
	}
	
	/**
	 * Method getItemCount.
	 * @param playable Playable
	 * @param itemId int
	 * @return long
	 */
	public static long getItemCount(Playable playable, int itemId)
	{
		return ItemFunctions.getItemCount(playable, itemId);
	}
	
	/**
	 * Method removeItem.
	 * @param playable Playable
	 * @param itemId int
	 * @param count long
	 * @return long
	 */
	public static long removeItem(Playable playable, int itemId, long count)
	{
		return ItemFunctions.removeItem(playable, itemId, count, true);
	}
	
	/**
	 * Method ride.
	 * @param player Player
	 * @param pet int
	 * @return boolean
	 */
	public static boolean ride(Player player, int pet)
	{
		if (player.isMounted())
		{
			player.setMount(0, 0, 0);
		}
		if (player.getSummonList().getPet() != null)
		{
			player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
			return false;
		}
		player.setMount(pet, 0, 0);
		return true;
	}
	
	/**
	 * Method unRide.
	 * @param player Player
	 */
	public static void unRide(Player player)
	{
		if (player.isMounted())
		{
			player.setMount(0, 0, 0);
		}
	}
	
	/**
	 * Method unSummonPet.
	 * @param player Player
	 * @param onlyPets boolean
	 */
	public static void unSummonPet(Player player, boolean onlyPets)
	{
		if (onlyPets)
		{
			player.getSummonList().unsummonPet(false);
		}
		else
		{
			player.getSummonList().unsummonAll(false);
		}
	}
	
	/**
	 * Method spawn.
	 * @param loc Location
	 * @param npcId int
	 * @return NpcInstance
	 */
	public static NpcInstance spawn(Location loc, int npcId)
	{
		return spawn(loc, npcId, ReflectionManager.DEFAULT);
	}
	
	/**
	 * Method spawn.
	 * @param loc Location
	 * @param npcId int
	 * @param reflection Reflection
	 * @return NpcInstance
	 */
	@Deprecated
	public static NpcInstance spawn(Location loc, int npcId, Reflection reflection)
	{
		return NpcUtils.spawnSingle(npcId, loc, reflection, 0);
	}
	
	/**
	 * Method getSelf.
	 * @return Player
	 */
	public Player getSelf()
	{
		return self.get();
	}
	
	/**
	 * Method getNpc.
	 * @return NpcInstance
	 */
	public NpcInstance getNpc()
	{
		return npc.get();
	}
	
	/**
	 * Method SpawnNPCs.
	 * @param npcId int
	 * @param locations int[][]
	 * @param list List<SimpleSpawner>
	 */
	public static void SpawnNPCs(int npcId, int[][] locations, List<SimpleSpawner> list)
	{
		NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
		if (template == null)
		{
			System.out.println("WARNING! Functions.SpawnNPCs template is null for npc: " + npcId);
			Thread.dumpStack();
			return;
		}
		for (int[] location : locations)
		{
			SimpleSpawner sp = new SimpleSpawner(template);
			sp.setLoc(new Location(location[0], location[1], location[2]));
			sp.setAmount(1);
			sp.setRespawnDelay(0);
			sp.init();
			if (list != null)
			{
				list.add(sp);
			}
		}
	}
	
	/**
	 * Method deSpawnNPCs.
	 * @param list List<SimpleSpawner>
	 */
	public static void deSpawnNPCs(List<SimpleSpawner> list)
	{
		for (SimpleSpawner sp : list)
		{
			sp.deleteAll();
		}
		list.clear();
	}
	
	/**
	 * Method IsActive.
	 * @param name String
	 * @return boolean
	 */
	public static boolean IsActive(String name)
	{
		return ServerVariables.getString(name, "off").equalsIgnoreCase("on");
	}
	
	/**
	 * Method SetActive.
	 * @param name String
	 * @param active boolean
	 * @return boolean
	 */
	public static boolean SetActive(String name, boolean active)
	{
		if (active == IsActive(name))
		{
			return false;
		}
		if (active)
		{
			ServerVariables.set(name, "on");
		}
		else
		{
			ServerVariables.unset(name);
		}
		return true;
	}
	
	/**
	 * Method SimpleCheckDrop.
	 * @param mob Creature
	 * @param killer Creature
	 * @return boolean
	 */
	public static boolean SimpleCheckDrop(Creature mob, Creature killer)
	{
		return (mob != null) && mob.isMonster() && !mob.isRaid() && (killer != null) && (killer.getPlayer() != null) && ((killer.getLevel() - mob.getLevel()) < 9);
	}
	
	/**
	 * Method isPvPEventStarted.
	 * @return boolean
	 */
	public static boolean isPvPEventStarted()
	{
		if ((Boolean) callScripts("events.TvT.TvT", "isRunned", new Object[] {}))
		{
			return true;
		}
		if ((Boolean) callScripts("events.lastHero.LastHero", "isRunned", new Object[] {}))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method sendDebugMessage.
	 * @param player Player
	 * @param message String
	 */
	public static void sendDebugMessage(Player player, String message)
	{
		if (!player.isGM())
		{
			return;
		}
		player.sendMessage(message);
	}
	
	/**
	 * Method sendSystemMail.
	 * @param receiver Player
	 * @param title String
	 * @param body String
	 * @param items Map<Integer,Long>
	 */
	public static void sendSystemMail(Player receiver, String title, String body, Map<Integer, Long> items)
	{
		if ((receiver == null) || !receiver.isOnline())
		{
			return;
		}
		if (title == null)
		{
			return;
		}
		if (items.keySet().size() > 8)
		{
			return;
		}
		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName("Admin");
		mail.setReceiverId(receiver.getObjectId());
		mail.setReceiverName(receiver.getName());
		mail.setTopic(title);
		mail.setBody(body);
		for (Map.Entry<Integer, Long> itm : items.entrySet())
		{
			ItemInstance item = ItemFunctions.createItem(itm.getKey());
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			item.setCount(itm.getValue());
			item.save();
			mail.addAttachment(item);
		}
		mail.setType(Mail.SenderType.NEWS_INFORMER);
		mail.setUnread(true);
		mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
		mail.save();
		receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
	}
	
	/**
	 * Method GetStringCount.
	 * @param count long
	 * @return String
	 */
	public static String GetStringCount(long count)
	{
		String scount = Long.toString(count);
		if (count < 1000)
		{
			return scount;
		}
		if ((count > 999) && (count < 1000000))
		{
			return scount.substring(0, scount.length() - 3) + "к";
		}
		if ((count > 999999) && (count < 1000000000))
		{
			return scount.substring(0, scount.length() - 6) + "кк";
		}
		if (count > 999999999)
		{
			return scount.substring(0, scount.length() - 9) + "ккк";
		}
		if (count == 0)
		{
			return "00.00";
		}
		return "ERROR";
	}
}

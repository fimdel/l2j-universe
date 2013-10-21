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
package lineage2.gameserver.model.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.MonsterRace;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.DeleteObject;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MonRaceInfo;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RaceManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field LANES. (value is 8)
	 */
	public static final int LANES = 8;
	/**
	 * Field WINDOW_START. (value is 0)
	 */
	public static final int WINDOW_START = 0;
	/**
	 * Field history.
	 */
	@SuppressWarnings("unused")
	private static List<Race> history;
	/**
	 * Field managers.
	 */
	private static Set<RaceManagerInstance> managers;
	/**
	 * Field _raceNumber.
	 */
	static int _raceNumber = 1;
	/**
	 * Field SECOND. (value is 1000)
	 */
	private final static long SECOND = 1000;
	/**
	 * Field MINUTE.
	 */
	private final static long MINUTE = 60 * SECOND;
	/**
	 * Field minutes.
	 */
	private static int minutes = 5;
	/**
	 * Field ACCEPTING_BETS. (value is 0)
	 */
	private static final int ACCEPTING_BETS = 0;
	/**
	 * Field WAITING. (value is 1)
	 */
	private static final int WAITING = 1;
	/**
	 * Field STARTING_RACE. (value is 2)
	 */
	private static final int STARTING_RACE = 2;
	/**
	 * Field RACE_END. (value is 3)
	 */
	private static final int RACE_END = 3;
	/**
	 * Field state.
	 */
	private static int state = RACE_END;
	/**
	 * Field codes.
	 */
	protected static final int[][] codes =
	{
		{
			-1,
			0
		},
		{
			0,
			15322
		},
		{
			13765,
			-1
		}
	};
	/**
	 * Field notInitialized.
	 */
	private static boolean notInitialized = true;
	/**
	 * Field packet.
	 */
	protected static MonRaceInfo packet;
	/**
	 * Field cost.
	 */
	protected static int cost[] =
	{
		100,
		500,
		1000,
		5000,
		10000,
		20000,
		50000,
		100000
	};
	
	/**
	 * Constructor for RaceManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RaceManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (notInitialized)
		{
			notInitialized = false;
			_raceNumber = ServerVariables.getInt("monster_race", 1);
			history = new ArrayList<>();
			managers = new CopyOnWriteArraySet<>();
			ThreadPoolManager s = ThreadPoolManager.getInstance();
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKETS_ARE_NOW_AVAILABLE_FOR_THE_S1TH_MONSTER_RACE), 0, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.WE_ARE_NOW_SELLING_TICKETS_FOR_THE_S1TH_MONSTER_RACE), 30 * SECOND, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKETS_ARE_NOW_AVAILABLE_FOR_THE_S1TH_MONSTER_RACE), MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.WE_ARE_NOW_SELLING_TICKETS_FOR_THE_S1TH_MONSTER_RACE), MINUTE + (30 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S), 2 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S), 3 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S), 4 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S), 5 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKETS_SALES_ARE_CLOSED_FOR_THE_S1TH_MONSTER_RACE_ODDS_ARE_POSTED), 6 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.TICKETS_SALES_ARE_CLOSED_FOR_THE_S1TH_MONSTER_RACE_ODDS_ARE_POSTED), 7 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_S2TH_MONSTER_RACE_WILL_BEGIN_IN_S1_MINUTES), 7 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_S2TH_MONSTER_RACE_WILL_BEGIN_IN_S1_MINUTES), 8 * MINUTE, 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_S1TH_MONSTER_RACE_WILL_BEGIN_IN_30_SECONDS), (8 * MINUTE) + (30 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_S1TH_MONSTER_RACE_IS_ABOUT_TO_BEGIN_COUNTDOWN_IN_FIVE_SECONDS), (8 * MINUTE) + (50 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_RACE_WILL_BEGIN_IN_S1_SECONDS), (8 * MINUTE) + (55 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_RACE_WILL_BEGIN_IN_S1_SECONDS), (8 * MINUTE) + (56 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_RACE_WILL_BEGIN_IN_S1_SECONDS), (8 * MINUTE) + (57 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_RACE_WILL_BEGIN_IN_S1_SECONDS), (8 * MINUTE) + (58 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THE_RACE_WILL_BEGIN_IN_S1_SECONDS), (8 * MINUTE) + (59 * SECOND), 10 * MINUTE);
			s.scheduleAtFixedRate(new Announcement(SystemMessage.THEYRE_OFF), 9 * MINUTE, 10 * MINUTE);
		}
		managers.add(this);
	}
	
	/**
	 * Method removeKnownPlayer.
	 * @param player Player
	 */
	public void removeKnownPlayer(Player player)
	{
		for (int i = 0; i < 8; i++)
		{
			player.sendPacket(new DeleteObject(MonsterRace.getInstance().getMonsters()[i]));
		}
	}
	
	/**
	 * @author Mobius
	 */
	class Announcement extends RunnableImpl
	{
		/**
		 * Field type.
		 */
		private final int type;
		
		/**
		 * Constructor for Announcement.
		 * @param type int
		 */
		public Announcement(int type)
		{
			this.type = type;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			makeAnnouncement(type);
		}
	}
	
	/**
	 * Method makeAnnouncement.
	 * @param type int
	 */
	public void makeAnnouncement(int type)
	{
		SystemMessage sm = new SystemMessage(type);
		switch (type)
		{
			case SystemMessage.TICKETS_ARE_NOW_AVAILABLE_FOR_THE_S1TH_MONSTER_RACE:
			case SystemMessage.WE_ARE_NOW_SELLING_TICKETS_FOR_THE_S1TH_MONSTER_RACE:
				if (state != ACCEPTING_BETS)
				{
					state = ACCEPTING_BETS;
					startRace();
				}
				sm.addNumber(_raceNumber);
				break;
			case SystemMessage.TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S:
			case SystemMessage.THE_S2TH_MONSTER_RACE_WILL_BEGIN_IN_S1_MINUTES:
			case SystemMessage.THE_RACE_WILL_BEGIN_IN_S1_SECONDS:
				sm.addNumber(minutes);
				sm.addNumber(_raceNumber);
				minutes--;
				break;
			case SystemMessage.TICKETS_SALES_ARE_CLOSED_FOR_THE_S1TH_MONSTER_RACE_ODDS_ARE_POSTED:
				sm.addNumber(_raceNumber);
				state = WAITING;
				minutes = 2;
				break;
			case SystemMessage.THE_S1TH_MONSTER_RACE_IS_ABOUT_TO_BEGIN_COUNTDOWN_IN_FIVE_SECONDS:
			case SystemMessage.MONSTER_RACE_S1_IS_FINISHED:
				sm.addNumber(_raceNumber);
				minutes = 5;
				break;
			case SystemMessage.FIRST_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S1_SECOND_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S2:
				state = RACE_END;
				sm.addNumber(MonsterRace.getInstance().getFirstPlace());
				sm.addNumber(MonsterRace.getInstance().getSecondPlace());
				break;
		}
		broadcast(sm);
		if (type == SystemMessage.THEYRE_OFF)
		{
			state = STARTING_RACE;
			startRace();
			minutes = 5;
		}
	}
	
	/**
	 * Method broadcast.
	 * @param pkt L2GameServerPacket
	 */
	protected void broadcast(L2GameServerPacket pkt)
	{
		for (RaceManagerInstance manager : managers)
		{
			if (!manager.isDead())
			{
				manager.broadcastPacketToOthers(pkt);
			}
		}
	}
	
	/**
	 * Method sendMonsterInfo.
	 */
	public void sendMonsterInfo()
	{
		broadcast(packet);
	}
	
	/**
	 * Method startRace.
	 */
	private void startRace()
	{
		MonsterRace race = MonsterRace.getInstance();
		if (state == STARTING_RACE)
		{
			PlaySound SRace = new PlaySound("S_Race");
			broadcast(SRace);
			PlaySound SRace2 = new PlaySound(PlaySound.Type.SOUND, "ItemSound2.race_start", 1, 121209259, new Location(12125, 182487, -3559));
			broadcast(SRace2);
			packet = new MonRaceInfo(codes[1][0], codes[1][1], race.getMonsters(), race.getSpeeds());
			sendMonsterInfo();
			ThreadPoolManager.getInstance().schedule(new RunRace(), 5000);
		}
		else
		{
			race.newRace();
			race.newSpeeds();
			packet = new MonRaceInfo(codes[0][0], codes[0][1], race.getMonsters(), race.getSpeeds());
			sendMonsterInfo();
		}
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.startsWith("BuyTicket") && (state != ACCEPTING_BETS))
		{
			player.sendPacket(Msg.MONSTER_RACE_TICKETS_ARE_NO_LONGER_AVAILABLE);
			command = "Chat 0";
		}
		if (command.startsWith("ShowOdds") && (state == ACCEPTING_BETS))
		{
			player.sendPacket(Msg.MONSTER_RACE_PAYOUT_INFORMATION_IS_NOT_AVAILABLE_WHILE_TICKETS_ARE_BEING_SOLD);
			command = "Chat 0";
		}
		if (command.startsWith("BuyTicket"))
		{
			int val = Integer.parseInt(command.substring(10));
			if (val == 0)
			{
				player.setRace(0, 0);
				player.setRace(1, 0);
			}
			if (((val == 10) && (player.getRace(0) == 0)) || ((val == 20) && (player.getRace(0) == 0) && (player.getRace(1) == 0)))
			{
				val = 0;
			}
			showBuyTicket(player, val);
		}
		else if (command.equals("ShowOdds"))
		{
			showOdds(player);
		}
		else if (command.equals("ShowInfo"))
		{
			showMonsterInfo(player);
		}
		else if (command.equals("calculateWin"))
		{
		}
		else if (command.equals("viewHistory"))
		{
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method showOdds.
	 * @param player Player
	 */
	public void showOdds(Player player)
	{
		if (state == ACCEPTING_BETS)
		{
			return;
		}
		int npcId = getTemplate().npcId;
		String filename, search;
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		filename = getHtmlPath(npcId, 5, player);
		html.setFile(filename);
		for (int i = 0; i < 8; i++)
		{
			int n = i + 1;
			search = "Mob" + n;
			html.replace(search, MonsterRace.getInstance().getMonsters()[i].getTemplate().name);
		}
		html.replace("1race", String.valueOf(_raceNumber));
		player.sendPacket(html);
		player.sendActionFailed();
	}
	
	/**
	 * Method showMonsterInfo.
	 * @param player Player
	 */
	public void showMonsterInfo(Player player)
	{
		int npcId = getTemplate().npcId;
		String filename, search;
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		filename = getHtmlPath(npcId, 6, player);
		html.setFile(filename);
		for (int i = 0; i < 8; i++)
		{
			int n = i + 1;
			search = "Mob" + n;
			html.replace(search, MonsterRace.getInstance().getMonsters()[i].getTemplate().name);
		}
		player.sendPacket(html);
		player.sendActionFailed();
	}
	
	/**
	 * Method showBuyTicket.
	 * @param player Player
	 * @param val int
	 */
	public void showBuyTicket(Player player, int val)
	{
		if (state != ACCEPTING_BETS)
		{
			return;
		}
		int npcId = getTemplate().npcId;
		String filename, search, replace;
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		if (val < 10)
		{
			filename = getHtmlPath(npcId, 2, player);
			html.setFile(filename);
			for (int i = 0; i < 8; i++)
			{
				int n = i + 1;
				search = "Mob" + n;
				html.replace(search, MonsterRace.getInstance().getMonsters()[i].getTemplate().name);
			}
			search = "No1";
			if (val == 0)
			{
				html.replace(search, "");
			}
			else
			{
				html.replace(search, "" + val);
				player.setRace(0, val);
			}
		}
		else if (val < 20)
		{
			if (player.getRace(0) == 0)
			{
				return;
			}
			filename = getHtmlPath(npcId, 3, player);
			html.setFile(filename);
			html.replace("0place", "" + player.getRace(0));
			search = "Mob1";
			replace = MonsterRace.getInstance().getMonsters()[player.getRace(0) - 1].getTemplate().name;
			html.replace(search, replace);
			search = "0adena";
			if (val == 10)
			{
				html.replace(search, "");
			}
			else
			{
				html.replace(search, "" + cost[val - 11]);
				player.setRace(1, val - 10);
			}
		}
		else if (val == 20)
		{
			if ((player.getRace(0) == 0) || (player.getRace(1) == 0))
			{
				return;
			}
			filename = getHtmlPath(npcId, 4, player);
			html.setFile(filename);
			html.replace("0place", "" + player.getRace(0));
			search = "Mob1";
			replace = MonsterRace.getInstance().getMonsters()[player.getRace(0) - 1].getTemplate().name;
			html.replace(search, replace);
			search = "0adena";
			int price = cost[player.getRace(1) - 1];
			html.replace(search, "" + price);
			search = "0tax";
			int tax = 0;
			html.replace(search, "" + tax);
			search = "0total";
			int total = price + tax;
			html.replace(search, "" + total);
		}
		else
		{
			if ((player.getRace(0) == 0) || (player.getRace(1) == 0))
			{
				return;
			}
			if (player.getAdena() < cost[player.getRace(1) - 1])
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			int ticket = player.getRace(0);
			int priceId = player.getRace(1);
			player.setRace(0, 0);
			player.setRace(1, 0);
			player.reduceAdena(cost[priceId - 1], true);
			SystemMessage sm = new SystemMessage(SystemMessage.ACQUIRED__S1_S2);
			sm.addNumber(_raceNumber);
			sm.addItemName(4443);
			player.sendPacket(sm);
			ItemInstance item = ItemFunctions.createItem(4443);
			item.setEnchantLevel(_raceNumber);
			item.setCustomType1(ticket);
			item.setCustomType2(cost[priceId - 1] / 100);
			player.getInventory().addItem(item);
			return;
		}
		html.replace("1race", String.valueOf(_raceNumber));
		player.sendPacket(html);
		player.sendActionFailed();
	}
	
	/**
	 * @author Mobius
	 */
	public class Race
	{
		/**
		 * Field info.
		 */
		private final Info[] info;
		
		/**
		 * Constructor for Race.
		 * @param info Info[]
		 */
		public Race(Info[] info)
		{
			this.info = info;
		}
		
		/**
		 * Method getLaneInfo.
		 * @param lane int
		 * @return Info
		 */
		public Info getLaneInfo(int lane)
		{
			return info[lane];
		}
		
		/**
		 * @author Mobius
		 */
		public class Info
		{
			/**
			 * Field id.
			 */
			private final int id;
			/**
			 * Field place.
			 */
			private final int place;
			/**
			 * Field odds.
			 */
			private final int odds;
			/**
			 * Field payout.
			 */
			private final int payout;
			
			/**
			 * Constructor for Info.
			 * @param id int
			 * @param place int
			 * @param odds int
			 * @param payout int
			 */
			public Info(int id, int place, int odds, int payout)
			{
				this.id = id;
				this.place = place;
				this.odds = odds;
				this.payout = payout;
			}
			
			/**
			 * Method getId.
			 * @return int
			 */
			public int getId()
			{
				return id;
			}
			
			/**
			 * Method getOdds.
			 * @return int
			 */
			public int getOdds()
			{
				return odds;
			}
			
			/**
			 * Method getPayout.
			 * @return int
			 */
			public int getPayout()
			{
				return payout;
			}
			
			/**
			 * Method getPlace.
			 * @return int
			 */
			public int getPlace()
			{
				return place;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	class RunRace extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			packet = new MonRaceInfo(codes[2][0], codes[2][1], MonsterRace.getInstance().getMonsters(), MonsterRace.getInstance().getSpeeds());
			sendMonsterInfo();
			ThreadPoolManager.getInstance().schedule(new RunEnd(), 30000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	class RunEnd extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			makeAnnouncement(SystemMessage.FIRST_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S1_SECOND_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S2);
			makeAnnouncement(SystemMessage.MONSTER_RACE_S1_IS_FINISHED);
			_raceNumber++;
			ServerVariables.set("monster_race", _raceNumber);
			for (int i = 0; i < 8; i++)
			{
				broadcast(new DeleteObject(MonsterRace.getInstance().getMonsters()[i]));
			}
		}
	}
	
	/**
	 * Method getPacket.
	 * @return MonRaceInfo
	 */
	public MonRaceInfo getPacket()
	{
		return packet;
	}
}

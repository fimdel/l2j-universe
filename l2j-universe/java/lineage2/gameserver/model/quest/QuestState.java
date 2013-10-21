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
package lineage2.gameserver.model.quest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.network.serverpackets.ExShowQuestMark;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.QuestList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.TutorialEnableClientEvent;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.TutorialShowQuestionMark;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.spawn.PeriodOfDay;
import lineage2.gameserver.utils.ItemFunctions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class QuestState
{
	/**
	 * @author Mobius
	 */
	public class OnDeathListenerImpl implements OnDeathListener
	{
		/**
		 * Method onDeath.
		 * @param actor Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			Player player = actor.getPlayer();
			if (player == null)
			{
				return;
			}
			player.removeListener(this);
			_quest.notifyDeath(killer, actor, QuestState.this);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class PlayerOnKillListenerImpl implements OnKillListener
	{
		/**
		 * Method onKill.
		 * @param actor Creature
		 * @param victim Creature
		 * @see lineage2.gameserver.listener.actor.OnKillListener#onKill(Creature, Creature)
		 */
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			if (!victim.isPlayer())
			{
				return;
			}
			Player actorPlayer = (Player) actor;
			List<Player> players = null;
			switch (_quest.getParty())
			{
				case Quest.PARTY_NONE:
					players = Collections.singletonList(actorPlayer);
					break;
				case Quest.PARTY_ALL:
					if (actorPlayer.getParty() == null)
					{
						players = Collections.singletonList(actorPlayer);
					}
					else
					{
						players = new ArrayList<>(actorPlayer.getParty().getMemberCount());
						for (Player member : actorPlayer.getParty().getPartyMembers())
						{
							if (member.isInRange(actorPlayer, Creature.INTERACTION_DISTANCE))
							{
								players.add(member);
							}
						}
					}
					break;
				default:
					players = Collections.emptyList();
					break;
			}
			for (Player player : players)
			{
				QuestState questState = player.getQuestState(_quest.getClass());
				if ((questState != null) && !questState.isCompleted())
				{
					_quest.notifyKill((Player) victim, questState);
				}
			}
		}
		
		/**
		 * Method ignorePetOrSummon.
		 * @return boolean * @see lineage2.gameserver.listener.actor.OnKillListener#ignorePetOrSummon()
		 */
		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(QuestState.class);
	/**
	 * Field RESTART_HOUR. (value is 6)
	 */
	public static final int RESTART_HOUR = 6;
	/**
	 * Field RESTART_MINUTES. (value is 30)
	 */
	public static final int RESTART_MINUTES = 30;
	/**
	 * Field VAR_COND. (value is ""cond"")
	 */
	public static final String VAR_COND = "cond";
	/**
	 * Field EMPTY_ARRAY.
	 */
	public final static QuestState[] EMPTY_ARRAY = new QuestState[0];
	/**
	 * Field _player.
	 */
	private final Player _player;
	/**
	 * Field _quest.
	 */
	final Quest _quest;
	/**
	 * Field _state.
	 */
	private int _state;
	/**
	 * Field _cond.
	 */
	private Integer _cond = null;
	/**
	 * Field _vars.
	 */
	private final Map<String, String> _vars = new ConcurrentHashMap<>();
	/**
	 * Field _timers.
	 */
	private final Map<String, QuestTimer> _timers = new ConcurrentHashMap<>();
	/**
	 * Field _onKillListener.
	 */
	private OnKillListener _onKillListener = null;
	
	/**
	 * Constructor for QuestState.
	 * @param quest Quest
	 * @param player Player
	 * @param state int
	 */
	public QuestState(Quest quest, Player player, int state)
	{
		_quest = quest;
		_player = player;
		player.setQuestState(this);
		_state = state;
		quest.notifyCreate(this);
	}
	
	/**
	 * Method addExpAndSp.
	 * @param exp long
	 * @param sp long
	 */
	public void addExpAndSp(long exp, long sp)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (exp > 0)
		{
			player.addExpAndSp((long) (exp * getRateQuestsReward()), 0);
		}
		if (sp > 0)
		{
			player.addExpAndSp(0, (long) (sp * getRateQuestsReward()));
		}
	}
	
	/**
	 * Method addNotifyOfDeath.
	 * @param player Player
	 * @param withPet boolean
	 */
	public void addNotifyOfDeath(Player player, boolean withPet)
	{
		OnDeathListenerImpl listener = new OnDeathListenerImpl();
		player.addListener(listener);
		if (withPet)
		{
			for (Summon summon : player.getSummonList())
			{
				summon.addListener(listener);
			}
		}
	}
	
	/**
	 * Method addPlayerOnKillListener.
	 */
	public void addPlayerOnKillListener()
	{
		if (_onKillListener != null)
		{
			throw new IllegalArgumentException("Cant add twice kill listener to player");
		}
		_onKillListener = new PlayerOnKillListenerImpl();
		_player.addListener(_onKillListener);
	}
	
	/**
	 * Method removePlayerOnKillListener.
	 */
	public void removePlayerOnKillListener()
	{
		if (_onKillListener != null)
		{
			_player.removeListener(_onKillListener);
		}
	}
	
	/**
	 * Method addRadar.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void addRadar(int x, int y, int z)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.addRadar(x, y, z);
		}
	}
	
	/**
	 * Method addRadarWithMap.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void addRadarWithMap(int x, int y, int z)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.addRadarWithMap(x, y, z);
		}
	}
	
	/**
	 * Method exitCurrentQuest.
	 * @param quest Quest
	 */
	public void exitCurrentQuest(Quest quest)
	{
		Player player = getPlayer();
		exitCurrentQuest(true);
		quest.newQuestState(player, Quest.DELAYED);
		QuestState qs = player.getQuestState(quest.getClass());
		qs.setRestartTime();
	}
	
	/**
	 * Method exitCurrentQuest.
	 * @param repeatable boolean
	 * @return QuestState
	 */
	public QuestState exitCurrentQuest(boolean repeatable)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return this;
		}
		removePlayerOnKillListener();
		for (int itemId : _quest.getItems())
		{
			ItemInstance item = player.getInventory().getItemByItemId(itemId);
			if ((item == null) || (itemId == 57))
			{
				continue;
			}
			long count = item.getCount();
			player.getInventory().destroyItemByItemId(itemId, count);
			player.getWarehouse().destroyItemByItemId(itemId, count);
		}
		if (repeatable)
		{
			player.removeQuestState(_quest.getName());
			Quest.deleteQuestInDb(this);
			_vars.clear();
		}
		else
		{
			for (String var : _vars.keySet())
			{
				if (var != null)
				{
					unset(var);
				}
			}
			setState(Quest.COMPLETED);
			Quest.updateQuestInDb(this);
		}
		if (isCompleted())
		{
			WorldStatisticsManager.getInstance().updateStat(player, CategoryType.QUESTS_COMPLETED, 1);
		}
		player.sendPacket(new QuestList(player));
		return this;
	}
	
	/**
	 * Method abortQuest.
	 */
	public void abortQuest()
	{
		_quest.onAbort(this);
		exitCurrentQuest(true);
	}
	
	/**
	 * Method get.
	 * @param var String
	 * @return String
	 */
	public String get(String var)
	{
		return _vars.get(var);
	}
	
	/**
	 * Method getVars.
	 * @return Map<String,String>
	 */
	public Map<String, String> getVars()
	{
		return _vars;
	}
	
	/**
	 * Method getInt.
	 * @param var String
	 * @return int
	 */
	public int getInt(String var)
	{
		int varint = 0;
		try
		{
			String val = get(var);
			if (val == null)
			{
				return 0;
			}
			varint = Integer.parseInt(val);
		}
		catch (Exception e)
		{
			_log.error(getPlayer().getName() + ": variable " + var + " isn't an integer: " + varint, e);
		}
		return varint;
	}

	/**
	 * Method getInt.
	 * @param var String
	 * @return int
	 */
	public long getLong(String var)
	{
		long varlong = 0;
		try
		{
			String val = get(var);
			if (val == null)
			{
				return 0;
			}
			varlong = Long.parseLong(val);
		}
		catch (Exception e)
		{
			_log.error(getPlayer().getName() + ": variable " + var + " isn't a long: " + varlong, e);
		}
		return varlong;
	}

	/**
	 * Method getItemEquipped.
	 * @param loc int
	 * @return int
	 */
	public int getItemEquipped(int loc)
	{
		return getPlayer().getInventory().getPaperdollItemId(loc);
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _player;
	}
	
	/**
	 * Method getQuest.
	 * @return Quest
	 */
	public Quest getQuest()
	{
		return _quest;
	}
	
	/**
	 * Method checkQuestItemsCount.
	 * @param itemIds int[]
	 * @return boolean
	 */
	public boolean checkQuestItemsCount(int... itemIds)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return false;
		}
		for (int itemId : itemIds)
		{
			if (player.getInventory().getCountOf(itemId) <= 0)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method getSumQuestItemsCount.
	 * @param itemIds int[]
	 * @return long
	 */
	public long getSumQuestItemsCount(int... itemIds)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		long count = 0;
		for (int itemId : itemIds)
		{
			count += player.getInventory().getCountOf(itemId);
		}
		return count;
	}
	
	/**
	 * Method getQuestItemsCount.
	 * @param itemId int
	 * @return long
	 */
	public long getQuestItemsCount(int itemId)
	{
		Player player = getPlayer();
		return player == null ? 0 : player.getInventory().getCountOf(itemId);
	}
	
	/**
	 * Method getQuestItemsCount.
	 * @param itemsIds int[]
	 * @return long
	 */
	public long getQuestItemsCount(int... itemsIds)
	{
		long result = 0;
		for (int id : itemsIds)
		{
			result += getQuestItemsCount(id);
		}
		return result;
	}
	
	/**
	 * Method haveQuestItem.
	 * @param itemId int
	 * @param count int
	 * @return boolean
	 */
	public boolean haveQuestItem(int itemId, int count)
	{
		if (getQuestItemsCount(itemId) >= count)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method haveQuestItem.
	 * @param itemId int
	 * @return boolean
	 */
	public boolean haveQuestItem(int itemId)
	{
		return haveQuestItem(itemId, 1);
	}
	
	/**
	 * Method getState.
	 * @return int
	 */
	public int getState()
	{
		return _state == Quest.DELAYED ? Quest.CREATED : _state;
	}
	
	/**
	 * Method getStateName.
	 * @return String
	 */
	public String getStateName()
	{
		return Quest.getStateName(_state);
	}
	
	/**
	 * Method giveItems.
	 * @param itemId int
	 * @param count long
	 */
	public void giveItems(int itemId, long count)
	{
		if (itemId == ItemTemplate.ITEM_ID_ADENA)
		{
			giveItems(itemId, count, true);
		}
		else
		{
			giveItems(itemId, count, false);
		}
	}
	
	/**
	 * Method giveItems.
	 * @param itemId int
	 * @param count long
	 * @param rate boolean
	 */
	public void giveItems(int itemId, long count, boolean rate)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (count <= 0)
		{
			count = 1;
		}
		if (rate)
		{
			count = (long) (count * getRateQuestsReward());
		}
		ItemFunctions.addItem(player, itemId, count, true);
		player.sendChanges();
	}
	
	/**
	 * Method giveItems.
	 * @param itemId int
	 * @param count long
	 * @param element Element
	 * @param power int
	 */
	public void giveItems(int itemId, long count, Element element, int power)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (count <= 0)
		{
			count = 1;
		}
		ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
		if (template == null)
		{
			return;
		}
		for (int i = 0; i < count; i++)
		{
			ItemInstance item = ItemFunctions.createItem(itemId);
			if (element != Element.NONE)
			{
				item.setAttributeElement(element, power);
			}
			player.getInventory().addItem(item);
		}
		player.sendPacket(SystemMessage2.obtainItems(template.getItemId(), count, 0));
		player.sendChanges();
	}
	
	/**
	 * Method dropItem.
	 * @param npc NpcInstance
	 * @param itemId int
	 * @param count long
	 */
	public void dropItem(NpcInstance npc, int itemId, long count)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		ItemInstance item = ItemFunctions.createItem(itemId);
		item.setCount(count);
		item.dropToTheGround(player, npc);
	}
	
	/**
	 * Method rollDrop.
	 * @param count int
	 * @param calcChance double
	 * @return int
	 */
	public int rollDrop(int count, double calcChance)
	{
		if ((calcChance <= 0) || (count <= 0))
		{
			return 0;
		}
		return rollDrop(count, count, calcChance);
	}
	
	/**
	 * Method rollDrop.
	 * @param min int
	 * @param max int
	 * @param calcChance double
	 * @return int
	 */
	public int rollDrop(int min, int max, double calcChance)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0))
		{
			return 0;
		}
		int dropmult = 1;
		calcChance *= getRateQuestsDrop();
		if (getQuest().getParty() > Quest.PARTY_NONE)
		{
			Player player = getPlayer();
			if (player.getParty() != null)
			{
				calcChance *= Config.ALT_PARTY_BONUS[player.getParty().getMemberCountInRange(player, Config.ALT_PARTY_DISTRIBUTION_RANGE) - 1];
			}
		}
		if (calcChance > 100)
		{
			if ((int) Math.ceil(calcChance / 100) <= (calcChance / 100))
			{
				calcChance = Math.nextUp(calcChance);
			}
			dropmult = (int) Math.ceil(calcChance / 100);
			calcChance = calcChance / dropmult;
		}
		return Rnd.chance(calcChance) ? Rnd.get(min * dropmult, max * dropmult) : 0;
	}
	
	/**
	 * Method getRateQuestsDrop.
	 * @return double
	 */
	public double getRateQuestsDrop()
	{
		Player player = getPlayer();
		double Bonus = player == null ? 1. : player.getBonus().getQuestDropRate();
		return Config.RATE_QUESTS_DROP * Bonus;
	}
	
	/**
	 * Method getRateQuestsReward.
	 * @return double
	 */
	public double getRateQuestsReward()
	{
		Player player = getPlayer();
		double Bonus = player == null ? 1. : player.getBonus().getQuestRewardRate();
		return Config.RATE_QUESTS_REWARD * Bonus;
	}
	
	/**
	 * Method rollAndGive.
	 * @param itemId int
	 * @param min int
	 * @param max int
	 * @param limit int
	 * @param calcChance double
	 * @return boolean
	 */
	public boolean rollAndGive(int itemId, int min, int max, int limit, double calcChance)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0) || (limit <= 0) || (itemId <= 0))
		{
			return false;
		}
		long count = rollDrop(min, max, calcChance);
		if (count > 0)
		{
			long alreadyCount = getQuestItemsCount(itemId);
			if ((alreadyCount + count) > limit)
			{
				count = limit - alreadyCount;
			}
			if (count > 0)
			{
				giveItems(itemId, count, false);
				if ((count + alreadyCount) < limit)
				{
					playSound(Quest.SOUND_ITEMGET);
				}
				else
				{
					playSound(Quest.SOUND_MIDDLE);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method rollAndGive.
	 * @param itemId int
	 * @param min int
	 * @param max int
	 * @param calcChance double
	 */
	public void rollAndGive(int itemId, int min, int max, double calcChance)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0) || (itemId <= 0))
		{
			return;
		}
		int count = rollDrop(min, max, calcChance);
		if (count > 0)
		{
			giveItems(itemId, count, false);
			playSound(Quest.SOUND_ITEMGET);
		}
	}
	
	/**
	 * Method rollAndGive.
	 * @param itemId int
	 * @param count int
	 * @param calcChance double
	 * @return boolean
	 */
	public boolean rollAndGive(int itemId, int count, double calcChance)
	{
		if ((calcChance <= 0) || (count <= 0) || (itemId <= 0))
		{
			return false;
		}
		int countToDrop = rollDrop(count, calcChance);
		if (countToDrop > 0)
		{
			giveItems(itemId, countToDrop, false);
			playSound(Quest.SOUND_ITEMGET);
			return true;
		}
		return false;
	}
	
	/**
	 * Method isCompleted.
	 * @return boolean
	 */
	public boolean isCompleted()
	{
		return getState() == Quest.COMPLETED;
	}
	
	/**
	 * Method isStarted.
	 * @return boolean
	 */
	public boolean isStarted()
	{
		return getState() == Quest.STARTED;
	}
	
	/**
	 * Method isCreated.
	 * @return boolean
	 */
	public boolean isCreated()
	{
		return getState() == Quest.CREATED;
	}
	
	/**
	 * Method killNpcByObjectId.
	 * @param _objId int
	 */
	public void killNpcByObjectId(int _objId)
	{
		NpcInstance npc = GameObjectsStorage.getNpc(_objId);
		if (npc != null)
		{
			npc.doDie(null);
		}
		else
		{
			_log.warn("Attemp to kill object that is not npc in quest " + getQuest().getQuestIntId());
		}
	}
	
	/**
	 * Method set.
	 * @param var String
	 * @param val String
	 * @return String
	 */
	public String set(String var, String val)
	{
		return set(var, val, true);
	}
	
	/**
	 * Method set.
	 * @param var String
	 * @param intval int
	 * @return String
	 */
	public String set(String var, int intval)
	{
		return set(var, String.valueOf(intval), true);
	}

	/**
	 * Method set.
	 * @param var String
	 * @param longval long
	 * @return String
	 */
	public String set(String var, long longval)
	{
		return set(var, String.valueOf(longval), true);
	}
	
	/**
	 * Method set.
	 * @param var String
	 * @param val String
	 * @param store boolean
	 * @return String
	 */
	public String set(String var, String val, boolean store)
	{
		if (val == null)
		{
			val = StringUtils.EMPTY;
		}
		_vars.put(var, val);
		if (store)
		{
			Quest.updateQuestVarInDb(this, var, val);
		}
		return val;
	}
	
	/**
	 * Method setState.
	 * @param state int
	 * @return Object
	 */
	public Object setState(int state)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return null;
		}
		_state = state;
		if (getQuest().isVisible(player) && isStarted())
		{
			player.sendPacket(new ExShowQuestMark(getQuest().getQuestIntId(), getCond()));
		}
		Quest.updateQuestInDb(this);
		player.sendPacket(new QuestList(player));
		return state;
	}
	
	/**
	 * Method setStateAndNotSave.
	 * @param state int
	 * @return Object
	 */
	public Object setStateAndNotSave(int state)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return null;
		}
		_state = state;
		if (getQuest().isVisible(player) && isStarted())
		{
			player.sendPacket(new ExShowQuestMark(getQuest().getQuestIntId(), getCond()));
		}
		player.sendPacket(new QuestList(player));
		return state;
	}
	
	/**
	 * Method playSound.
	 * @param sound String
	 */
	public void playSound(String sound)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new PlaySound(sound));
		}
	}
	
	/**
	 * Method playTutorialVoice.
	 * @param voice String
	 */
	public void playTutorialVoice(String voice)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new PlaySound(PlaySound.Type.VOICE, voice, 0, 0, player.getLoc()));
		}
	}
	
	/**
	 * Method onTutorialClientEvent.
	 * @param number int
	 */
	public void onTutorialClientEvent(int number)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new TutorialEnableClientEvent(number));
		}
	}
	
	/**
	 * Method showQuestionMark.
	 * @param number int
	 */
	public void showQuestionMark(int number)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new TutorialShowQuestionMark(number));
		}
	}
	
	/**
	 * Method showTutorialHTML.
	 * @param html String
	 * @param type int
	 */
	public void showTutorialHTML(String html, int type)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		String text = html;
		if (type == 1)
		{
			text = HtmCache.getInstance().getNotNull("quests/_255_Tutorial/" + html, player);
		}
		player.sendPacket(new TutorialShowHtml(text, type));
	}

	/**
	 * Method startQuestTimer.
	 * @param name String
	 * @param time long
	 */
	public void startQuestTimer(String name, long time)
	{
		startQuestTimer(name, time, null);
	}
	
	/**
	 * Method startQuestTimer.
	 * @param name String
	 * @param time long
	 * @param npc NpcInstance
	 */
	public void startQuestTimer(String name, long time, NpcInstance npc)
	{
		QuestTimer timer = new QuestTimer(name, time, npc);
		timer.setQuestState(this);
		QuestTimer oldTimer = getTimers().put(name, timer);
		if (oldTimer != null)
		{
			oldTimer.stop();
		}
		timer.start();
	}
	
	/**
	 * Method isRunningQuestTimer.
	 * @param name String
	 * @return boolean
	 */
	public boolean isRunningQuestTimer(String name)
	{
		return getTimers().get(name) != null;
	}
	
	/**
	 * Method cancelQuestTimer.
	 * @param name String
	 * @return boolean
	 */
	public boolean cancelQuestTimer(String name)
	{
		QuestTimer timer = removeQuestTimer(name);
		if (timer != null)
		{
			timer.stop();
		}
		return timer != null;
	}
	
	/**
	 * Method removeQuestTimer.
	 * @param name String
	 * @return QuestTimer
	 */
	QuestTimer removeQuestTimer(String name)
	{
		QuestTimer timer = getTimers().remove(name);
		if (timer != null)
		{
			timer.setQuestState(null);
		}
		return timer;
	}
	
	/**
	 * Method pauseQuestTimers.
	 */
	public void pauseQuestTimers()
	{
		getQuest().pauseQuestTimers(this);
	}
	
	/**
	 * Method stopQuestTimers.
	 */
	public void stopQuestTimers()
	{
		for (QuestTimer timer : getTimers().values())
		{
			timer.setQuestState(null);
			timer.stop();
		}
		_timers.clear();
	}
	
	/**
	 * Method resumeQuestTimers.
	 */
	public void resumeQuestTimers()
	{
		getQuest().resumeQuestTimers(this);
	}
	
	/**
	 * Method getTimers.
	 * @return Map<String,QuestTimer>
	 */
	Map<String, QuestTimer> getTimers()
	{
		return _timers;
	}
	
	/**
	 * Method takeItems.
	 * @param itemId int
	 * @param count long
	 * @return long
	 */
	public long takeItems(int itemId, long count)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		ItemInstance item = player.getInventory().getItemByItemId(itemId);
		if (item == null)
		{
			return 0;
		}
		if ((count < 0) || (count > item.getCount()))
		{
			count = item.getCount();
		}
		player.getInventory().destroyItemByItemId(itemId, count);
		player.sendPacket(SystemMessage2.removeItems(itemId, count));
		return count;
	}
	
	/**
	 * Method takeAllItems.
	 * @param itemId int
	 * @return long
	 */
	public long takeAllItems(int itemId)
	{
		return takeItems(itemId, -1);
	}
	
	/**
	 * Method takeAllItems.
	 * @param itemsIds int[]
	 * @return long
	 */
	public long takeAllItems(int... itemsIds)
	{
		long result = 0;
		for (int id : itemsIds)
		{
			result += takeAllItems(id);
		}
		return result;
	}
	
	/**
	 * Method takeAllItems.
	 * @param itemsIds Collection<Integer>
	 * @return long
	 */
	public long takeAllItems(Collection<Integer> itemsIds)
	{
		long result = 0;
		for (int id : itemsIds)
		{
			result += takeAllItems(id);
		}
		return result;
	}
	
	/**
	 * Method unset.
	 * @param var String
	 * @return String
	 */
	public String unset(String var)
	{
		if (var == null)
		{
			return null;
		}
		String old = _vars.remove(var);
		if (old != null)
		{
			Quest.deleteQuestVarInDb(this, var);
		}
		return old;
	}
	
	/**
	 * Method checkPartyMember.
	 * @param member Player
	 * @param state int
	 * @param maxrange int
	 * @param rangefrom GameObject
	 * @return boolean
	 */
	private boolean checkPartyMember(Player member, int state, int maxrange, GameObject rangefrom)
	{
		if (member == null)
		{
			return false;
		}
		if ((rangefrom != null) && (maxrange > 0) && !member.isInRange(rangefrom, maxrange))
		{
			return false;
		}
		QuestState qs = member.getQuestState(getQuest().getName());
		if ((qs == null) || (qs.getState() != state))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method getPartyMembers.
	 * @param state int
	 * @param maxrange int
	 * @param rangefrom GameObject
	 * @return List<Player>
	 */
	public List<Player> getPartyMembers(int state, int maxrange, GameObject rangefrom)
	{
		List<Player> result = new ArrayList<>();
		Party party = getPlayer().getParty();
		if (party == null)
		{
			if (checkPartyMember(getPlayer(), state, maxrange, rangefrom))
			{
				result.add(getPlayer());
			}
			return result;
		}
		for (Player _member : party.getPartyMembers())
		{
			if (checkPartyMember(_member, state, maxrange, rangefrom))
			{
				result.add(getPlayer());
			}
		}
		return result;
	}
	
	/**
	 * Method getRandomPartyMember.
	 * @param state int
	 * @param maxrangefromplayer int
	 * @return Player
	 */
	public Player getRandomPartyMember(int state, int maxrangefromplayer)
	{
		return getRandomPartyMember(state, maxrangefromplayer, getPlayer());
	}
	
	/**
	 * Method getRandomPartyMember.
	 * @param state int
	 * @param maxrange int
	 * @param rangefrom GameObject
	 * @return Player
	 */
	public Player getRandomPartyMember(int state, int maxrange, GameObject rangefrom)
	{
		List<Player> list = getPartyMembers(state, maxrange, rangefrom);
		if (list.size() == 0)
		{
			return null;
		}
		return list.get(Rnd.get(list.size()));
	}
	
	/**
	 * Method addSpawn.
	 * @param npcId int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawn(int npcId)
	{
		return addSpawn(npcId, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, 0, 0);
	}
	
	/**
	 * Method addSpawn.
	 * @param npcId int
	 * @param despawnDelay int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawn(int npcId, int despawnDelay)
	{
		return addSpawn(npcId, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, 0, despawnDelay);
	}
	
	/**
	 * Method addSpawn.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawn(int npcId, int x, int y, int z)
	{
		return addSpawn(npcId, x, y, z, 0, 0, 0);
	}
	
	/**
	 * Method addSpawn.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param despawnDelay int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawn(int npcId, int x, int y, int z, int despawnDelay)
	{
		return addSpawn(npcId, x, y, z, 0, 0, despawnDelay);
	}
	
	/**
	 * Method addSpawn.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param heading int
	 * @param randomOffset int
	 * @param despawnDelay int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawn(int npcId, int x, int y, int z, int heading, int randomOffset, int despawnDelay)
	{
		return getQuest().addSpawn(npcId, x, y, z, heading, randomOffset, despawnDelay);
	}
	
	/**
	 * Method findTemplate.
	 * @param npcId int
	 * @return NpcInstance
	 */
	public NpcInstance findTemplate(int npcId)
	{
		for (Spawner spawn : SpawnManager.getInstance().getSpawners(PeriodOfDay.NONE.name()))
		{
			if ((spawn != null) && (spawn.getCurrentNpcId() == npcId))
			{
				return spawn.getLastSpawn();
			}
		}
		return null;
	}
	
	/**
	 * Method calculateLevelDiffForDrop.
	 * @param mobLevel int
	 * @param player int
	 * @return int
	 */
	public int calculateLevelDiffForDrop(int mobLevel, int player)
	{
		if (!Config.DEEPBLUE_DROP_RULES)
		{
			return 0;
		}
		return Math.max(player - mobLevel - Config.DEEPBLUE_DROP_MAXDIFF, 0);
	}
	
	/**
	 * Method getCond.
	 * @return int
	 */
	public int getCond()
	{
		if (_cond == null)
		{
			int val = getInt(VAR_COND);
			if ((val & 0x80000000) != 0)
			{
				val &= 0x7fffffff;
				for (int i = 1; i < 32; i++)
				{
					val = (val >> 1);
					if (val == 0)
					{
						val = i;
						break;
					}
				}
			}
			_cond = val;
		}
		return _cond;
	}
	
	/**
	 * Method setCond.
	 * @param newCond int
	 * @return String
	 */
	public String setCond(int newCond)
	{
		return setCond(newCond, true);
	}
	
	/**
	 * Method setCond.
	 * @param newCond int
	 * @param store boolean
	 * @return String
	 */
	public String setCond(int newCond, boolean store)
	{
		if (newCond == getCond())
		{
			return String.valueOf(newCond);
		}
		int oldCond = getInt(VAR_COND);
		_cond = newCond;
		if ((oldCond & 0x80000000) != 0)
		{
			if (newCond > 2)
			{
				oldCond &= 0x80000001 | ((1 << newCond) - 1);
				newCond = oldCond | (1 << (newCond - 1));
			}
		}
		else
		{
			if (newCond > 2)
			{
				newCond = 0x80000001 | (1 << (newCond - 1)) | ((1 << oldCond) - 1);
			}
		}
		final String sVal = String.valueOf(newCond);
		final String result = set(VAR_COND, sVal, false);
		if (store)
		{
			Quest.updateQuestVarInDb(this, VAR_COND, sVal);
		}
		final Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new QuestList(player));
			if ((newCond != 0) && getQuest().isVisible(player) && isStarted())
			{
				player.sendPacket(new ExShowQuestMark(getQuest().getQuestIntId(), getCond()));
			}
			player.sendPacket(new QuestList(player));
		}
		return result;
	}
	
	/**
	 * Method setRestartTime.
	 */
	public void setRestartTime()
	{
		Calendar reDo = Calendar.getInstance();
		if (reDo.get(Calendar.HOUR_OF_DAY) >= RESTART_HOUR)
		{
			reDo.add(Calendar.DATE, 1);
		}
		reDo.set(Calendar.HOUR_OF_DAY, RESTART_HOUR);
		reDo.set(Calendar.MINUTE, RESTART_MINUTES);
		set("restartTime", String.valueOf(reDo.getTimeInMillis()));
	}
	
	/**
	 * Method isNowAvailableByTime.
	 * @return boolean
	 */
	public boolean isNowAvailableByTime()
	{
		String val = get("restartTime");
		if (val == null)
		{
			return true;
		}
		long restartTime = Long.parseLong(val);
		return restartTime <= System.currentTimeMillis();
	}

	/**
	 * @return boolean
	 */
	public boolean isNowAvailable()
	{
		String val = get("restartTime");
		if(val == null)
		{
			return true;
		}

		long restartTime = Long.parseLong(val);

		return restartTime <= System.currentTimeMillis();
	}

	/**
	 */
	public void startQuest()
	{
		setState(2);    // STARTED
		setCond(1);
		playSound("ItemSound.quest_accept");
	}
}

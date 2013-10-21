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

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.logging.LogUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.TroveUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.olympiad.OlympiadGame;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.startcondition.ICheckStartCondition;
import lineage2.gameserver.model.quest.startcondition.impl.ClassLevelCondition;
import lineage2.gameserver.model.quest.startcondition.impl.PlayerLevelCondition;
import lineage2.gameserver.model.quest.startcondition.impl.QuestCompletedCondition;
import lineage2.gameserver.model.quest.startcondition.impl.SubClassCondition;
import lineage2.gameserver.network.serverpackets.ExNpcQuestHtmlMessage;
import lineage2.gameserver.network.serverpackets.ExQuestNpcLogList;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Quest
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Quest.class);
	/**
	 * Field SOUND_ITEMGET. (value is ""ItemSound.quest_itemget"")
	 */
	public static final String SOUND_ITEMGET = "ItemSound.quest_itemget";
	/**
	 * Field SOUND_ACCEPT. (value is ""ItemSound.quest_accept"")
	 */
	public static final String SOUND_ACCEPT = "ItemSound.quest_accept";
	/**
	 * Field SOUND_MIDDLE. (value is ""ItemSound.quest_middle"")
	 */
	public static final String SOUND_MIDDLE = "ItemSound.quest_middle";
	/**
	 * Field SOUND_FINISH. (value is ""ItemSound.quest_finish"")
	 */
	public static final String SOUND_FINISH = "ItemSound.quest_finish";
	/**
	 * Field SOUND_GIVEUP. (value is ""ItemSound.quest_giveup"")
	 */
	public static final String SOUND_GIVEUP = "ItemSound.quest_giveup";
	/**
	 * Field SOUND_TUTORIAL. (value is ""ItemSound.quest_tutorial"")
	 */
	public static final String SOUND_TUTORIAL = "ItemSound.quest_tutorial";
	/**
	 * Field SOUND_JACKPOT. (value is ""ItemSound.quest_jackpot"")
	 */
	public static final String SOUND_JACKPOT = "ItemSound.quest_jackpot";
	/**
	 * Field SOUND_HORROR2. (value is ""SkillSound5.horror_02"")
	 */
	public static final String SOUND_HORROR2 = "SkillSound5.horror_02";
	/**
	 * Field SOUND_BEFORE_BATTLE. (value is ""Itemsound.quest_before_battle"")
	 */
	public static final String SOUND_BEFORE_BATTLE = "Itemsound.quest_before_battle";
	/**
	 * Field SOUND_FANFARE_MIDDLE. (value is ""ItemSound.quest_fanfare_middle"")
	 */
	public static final String SOUND_FANFARE_MIDDLE = "ItemSound.quest_fanfare_middle";
	/**
	 * Field SOUND_FANFARE2. (value is ""ItemSound.quest_fanfare_2"")
	 */
	public static final String SOUND_FANFARE2 = "ItemSound.quest_fanfare_2";
	/**
	 * Field SOUND_BROKEN_KEY. (value is ""ItemSound2.broken_key"")
	 */
	public static final String SOUND_BROKEN_KEY = "ItemSound2.broken_key";
	/**
	 * Field SOUND_ENCHANT_SUCESS. (value is ""ItemSound3.sys_enchant_sucess"")
	 */
	public static final String SOUND_ENCHANT_SUCESS = "ItemSound3.sys_enchant_sucess";
	/**
	 * Field SOUND_ENCHANT_FAILED. (value is ""ItemSound3.sys_enchant_failed"")
	 */
	public static final String SOUND_ENCHANT_FAILED = "ItemSound3.sys_enchant_failed";
	/**
	 * Field SOUND_ED_CHIMES05. (value is ""AmdSound.ed_chimes_05"")
	 */
	public static final String SOUND_ED_CHIMES05 = "AmdSound.ed_chimes_05";
	/**
	 * Field SOUND_ARMOR_WOOD_3. (value is ""ItemSound.armor_wood_3"")
	 */
	public static final String SOUND_ARMOR_WOOD_3 = "ItemSound.armor_wood_3";
	/**
	 * Field SOUND_ITEM_DROP_EQUIP_ARMOR_CLOTH. (value is ""ItemSound.item_drop_equip_armor_cloth"")
	 */
	public static final String SOUND_ITEM_DROP_EQUIP_ARMOR_CLOTH = "ItemSound.item_drop_equip_armor_cloth";
	/**
	 * Field NO_QUEST_DIALOG. (value is ""no-quest"")
	 */
	public static final String NO_QUEST_DIALOG = "no-quest";
	/**
	 * Field FONT_QUEST_AVAILABLE. (value is ""<font color=\"6699ff\">"")
	 */
	private static final String FONT_QUEST_AVAILABLE = "<font color=\"6699ff\">";
	/**
	 * Field FONT_QUEST_DONE. (value is ""<font color=\"787878\">"")
	 */
	private static final String FONT_QUEST_DONE = "<font color=\"787878\">";
	/**
	 * Field FONT_QUEST_NOT_AVAILABLE. (value is ""<font color=\"a62f31\">"")
	 */
	private static final String FONT_QUEST_NOT_AVAILABLE = "<font color=\"a62f31\">";
	/**
	 * Field TODO_FIND_HTML. (value is ""<font color=\"6699ff\">TODO:<br>
	 * Find this dialog"")
	 */
	protected static final String TODO_FIND_HTML = "<font color=\"6699ff\">TODO:<br>Find this dialog";
	/**
	 * Field ADENA_ID. (value is 57)
	 */
	public static final int ADENA_ID = 57;
	/**
	 * Field PARTY_NONE. (value is 0)
	 */
	public static final int PARTY_NONE = 0;
	/**
	 * Field PARTY_ONE. (value is 1)
	 */
	public static final int PARTY_ONE = 1;
	/**
	 * Field PARTY_ALL. (value is 2)
	 */
	public static final int PARTY_ALL = 2;
	/**
	 * Field _pausedQuestTimers.
	 */
	private final Map<Integer, Map<String, QuestTimer>> _pausedQuestTimers = new ConcurrentHashMap<>();
	/**
	 * Field _questItems.
	 */
	private final TIntHashSet _questItems = new TIntHashSet();
	/**
	 * Field _npcLogList.
	 */
	private TIntObjectHashMap<List<QuestNpcLogInfo>> _npcLogList = TroveUtils.emptyIntObjectMap();
	/**
	 * Field startConditionList.
	 */
	private final List<ICheckStartCondition> startConditionList = new ArrayList<>();
	
	/**
	 * Method addQuestItem.
	 * @param ids int[]
	 */
	public void addQuestItem(int... ids)
	{
		for (int id : ids)
		{
			if (id != 0)
			{
				ItemTemplate i = null;
				i = ItemHolder.getInstance().getTemplate(id);
				if (_questItems.contains(id))
				{
					_log.warn("Item " + i + " multiple times in quest drop in " + getName());
				}
				_questItems.add(id);
			}
		}
	}
	
	/**
	 * Method getItems.
	 * @return int[]
	 */
	public int[] getItems()
	{
		return _questItems.toArray();
	}
	
	/**
	 * Method isQuestItem.
	 * @param id int
	 * @return boolean
	 */
	public boolean isQuestItem(int id)
	{
		return _questItems.contains(id);
	}
	
	/**
	 * Method updateQuestInDb.
	 * @param qs QuestState
	 */
	public static void updateQuestInDb(QuestState qs)
	{
		updateQuestVarInDb(qs, "<state>", qs.getStateName());
	}
	
	/**
	 * Method updateQuestVarInDb.
	 * @param qs QuestState
	 * @param var String
	 * @param value String
	 */
	public static void updateQuestVarInDb(QuestState qs, String var, String value)
	{
		Player player = qs.getPlayer();
		if (player == null)
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO character_quests (char_id,name,var,value) VALUES (?,?,?,?)");
			statement.setInt(1, qs.getPlayer().getObjectId());
			statement.setString(2, qs.getQuest().getName());
			statement.setString(3, var);
			statement.setString(4, value);
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error("could not insert char quest:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteQuestInDb.
	 * @param qs QuestState
	 */
	public static void deleteQuestInDb(QuestState qs)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_quests WHERE char_id=? AND name=?");
			statement.setInt(1, qs.getPlayer().getObjectId());
			statement.setString(2, qs.getQuest().getName());
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error("could not delete char quest:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteQuestVarInDb.
	 * @param qs QuestState
	 * @param var String
	 */
	public static void deleteQuestVarInDb(QuestState qs, String var)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_quests WHERE char_id=? AND name=? AND var=?");
			statement.setInt(1, qs.getPlayer().getObjectId());
			statement.setString(2, qs.getQuest().getName());
			statement.setString(3, var);
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error("could not delete char quest:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restoreQuestStates.
	 * @param player Player
	 */
	public static void restoreQuestStates(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		PreparedStatement invalidQuestData = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			invalidQuestData = con.prepareStatement("DELETE FROM character_quests WHERE char_id=? and name=?");
			statement = con.prepareStatement("SELECT name,value FROM character_quests WHERE char_id=? AND var=?");
			statement.setInt(1, player.getObjectId());
			statement.setString(2, "<state>");
			rset = statement.executeQuery();
			while (rset.next())
			{
				String questId = rset.getString("name");
				String state = rset.getString("value");
				if (state.equalsIgnoreCase("Start"))
				{
					invalidQuestData.setInt(1, player.getObjectId());
					invalidQuestData.setString(2, questId);
					invalidQuestData.executeUpdate();
					continue;
				}
				Quest q = QuestManager.getQuest(questId);
				if (q == null)
				{
					if (!Config.DONTLOADQUEST)
					{
						_log.warn("Unknown quest " + questId + " for player " + player.getName());
						if (Config.REMOVE_UNKNOWN_QUEST)
						{
							invalidQuestData.setInt(1, player.getObjectId());
							invalidQuestData.setString(2, questId);
							invalidQuestData.executeUpdate();
						}
					}
					continue;
				}
				new QuestState(q, player, getStateId(state));
			}
			DbUtils.close(statement, rset);
			statement = con.prepareStatement("SELECT name,var,value FROM character_quests WHERE char_id=?");
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				String questId = rset.getString("name");
				String var = rset.getString("var");
				String value = rset.getString("value");
				QuestState qs = player.getQuestState(questId);
				if (qs == null)
				{
					continue;
				}
				if (var.equals("cond") && (Integer.parseInt(value) < 0))
				{
					value = String.valueOf(Integer.parseInt(value) | 1);
				}
				qs.set(var, value, false);
			}
		}
		catch (Exception e)
		{
			_log.error("could not insert char quest:", e);
		}
		finally
		{
			DbUtils.closeQuietly(invalidQuestData);
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Field _name.
	 */
	protected final String _name;
	/**
	 * Field _party.
	 */
	protected final int _party;
	/**
	 * Field _questId.
	 */
	protected final int _questId;
	/**
	 * Field CREATED. (value is 1)
	 */
	public final static int CREATED = 1;
	/**
	 * Field STARTED. (value is 2)
	 */
	public final static int STARTED = 2;
	/**
	 * Field COMPLETED. (value is 3)
	 */
	public final static int COMPLETED = 3;
	/**
	 * Field DELAYED. (value is 4)
	 */
	public final static int DELAYED = 4;
	
	/**
	 * Method getStateName.
	 * @param state int
	 * @return String
	 */
	public static String getStateName(int state)
	{
		switch (state)
		{
			case CREATED:
				return "Start";
			case STARTED:
				return "Started";
			case COMPLETED:
				return "Completed";
			case DELAYED:
				return "Delayed";
		}
		return "Start";
	}
	
	/**
	 * Method getStateId.
	 * @param state String
	 * @return int
	 */
	public static int getStateId(String state)
	{
		if (state.equalsIgnoreCase("Start"))
		{
			return CREATED;
		}
		else if (state.equalsIgnoreCase("Started"))
		{
			return STARTED;
		}
		else if (state.equalsIgnoreCase("Completed"))
		{
			return COMPLETED;
		}
		else if (state.equalsIgnoreCase("Delayed"))
		{
			return DELAYED;
		}
		return CREATED;
	}
	
	/**
	 * Constructor for Quest.
	 * @param party boolean
	 */
	public Quest(boolean party)
	{
		this(party ? 1 : 0);
	}
	
	/**
	 * Constructor for Quest.
	 * @param party int
	 */
	public Quest(int party)
	{
		_name = getClass().getSimpleName();
		_questId = Integer.parseInt(_name.split("_")[1]);
		_party = party;
		QuestManager.addQuest(this);
	}
	
	/**
	 * Method getNpcLogList.
	 * @param cond int
	 * @return List<QuestNpcLogInfo>
	 */
	public List<QuestNpcLogInfo> getNpcLogList(int cond)
	{
		return _npcLogList.get(cond);
	}
	
	/**
	 * Method addAttackId.
	 * @param attackIds int[]
	 */
	public void addAttackId(int... attackIds)
	{
		for (int attackId : attackIds)
		{
			addEventId(attackId, QuestEventType.ATTACKED_WITH_QUEST);
		}
	}
	
	/**
	 * Method addEventId.
	 * @param npcId int
	 * @param eventType QuestEventType
	 * @return NpcTemplate
	 */
	public NpcTemplate addEventId(int npcId, QuestEventType eventType)
	{
		try
		{
			NpcTemplate t = NpcHolder.getInstance().getTemplate(npcId);
			if (t != null)
			{
				t.addQuestEvent(eventType, this);
			}
			return t;
		}
		catch (Exception e)
		{
			_log.error("", e);
			return null;
		}
	}
	
	/**
	 * Method addKillId.
	 * @param killIds int[]
	 */
	public void addKillId(int... killIds)
	{
		for (int killid : killIds)
		{
			addEventId(killid, QuestEventType.MOB_KILLED_WITH_QUEST);
		}
	}
	
	/**
	 * Method addKillNpcWithLog.
	 * @param cond int
	 * @param varName String
	 * @param max int
	 * @param killIds int[]
	 */
	public void addKillNpcWithLog(int cond, String varName, int max, int... killIds)
	{
		if (killIds.length == 0)
		{
			throw new IllegalArgumentException("Npc list cant be empty!");
		}
		addKillId(killIds);
		if (_npcLogList.isEmpty())
		{
			_npcLogList = new TIntObjectHashMap<>(5);
		}
		List<QuestNpcLogInfo> vars = _npcLogList.get(cond);
		if (vars == null)
		{
			_npcLogList.put(cond, (vars = new ArrayList<>(5)));
		}
		vars.add(new QuestNpcLogInfo(killIds, varName, max));
	}
	
	/**
	 * Method updateKill.
	 * @param npc NpcInstance
	 * @param st QuestState
	 * @return boolean
	 */
	public boolean updateKill(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		if (player == null)
		{
			return false;
		}
		List<QuestNpcLogInfo> vars = getNpcLogList(st.getCond());
		if (vars == null)
		{
			return false;
		}
		boolean done = true;
		boolean find = false;
		for (QuestNpcLogInfo info : vars)
		{
			int count = st.getInt(info.getVarName());
			if (!find && ArrayUtils.contains(info.getNpcIds(), npc.getNpcId()))
			{
				find = true;
				if (count < info.getMaxCount())
				{
					st.set(info.getVarName(), ++count);
					player.sendPacket(new ExQuestNpcLogList(st));
				}
			}
			if (count != info.getMaxCount())
			{
				done = false;
			}
		}
		return done;
	}
	
	/**
	 * Method addKillId.
	 * @param killIds Collection<Integer>
	 */
	public void addKillId(Collection<Integer> killIds)
	{
		for (int killid : killIds)
		{
			addKillId(killid);
		}
	}
	
	/**
	 * Method addSkillUseId.
	 * @param npcId int
	 * @return NpcTemplate
	 */
	public NpcTemplate addSkillUseId(int npcId)
	{
		return addEventId(npcId, QuestEventType.MOB_TARGETED_BY_SKILL);
	}
	
	/**
	 * Method addStartNpc.
	 * @param npcIds int[]
	 */
	public void addStartNpc(int... npcIds)
	{
		for (int talkId : npcIds)
		{
			addStartNpc(talkId);
		}
	}
	
	/**
	 * Method addStartNpc.
	 * @param npcId int
	 * @return NpcTemplate
	 */
	public NpcTemplate addStartNpc(int npcId)
	{
		addTalkId(npcId);
		return addEventId(npcId, QuestEventType.QUEST_START);
	}
	
	/**
	 * Method addFirstTalkId.
	 * @param npcIds int[]
	 */
	public void addFirstTalkId(int... npcIds)
	{
		for (int npcId : npcIds)
		{
			addEventId(npcId, QuestEventType.NPC_FIRST_TALK);
		}
	}
	
	/**
	 * Method addTalkId.
	 * @param talkIds int[]
	 */
	public void addTalkId(int... talkIds)
	{
		for (int talkId : talkIds)
		{
			addEventId(talkId, QuestEventType.QUEST_TALK);
		}
	}
	
	/**
	 * Method addTalkId.
	 * @param talkIds Collection<Integer>
	 */
	public void addTalkId(Collection<Integer> talkIds)
	{
		for (int talkId : talkIds)
		{
			addTalkId(talkId);
		}
	}
	
	/**
	 * Method addLevelCheck.
	 * @param min int
	 * @param max int
	 */
	public void addLevelCheck(int min, int max)
	{
		startConditionList.add(new PlayerLevelCondition(min, max));
	}
	
	/**
	 * Method addQuestCompletedCheck.
	 * @param clazz Class<?>
	 */
	public void addQuestCompletedCheck(Class<?> clazz)
	{
		startConditionList.add(new QuestCompletedCondition(clazz.getSimpleName()));
	}
	
	/**
	 * Method addClassLevelCheck.
	 * @param classLevels int[]
	 */
	public void addClassLevelCheck(int... classLevels)
	{
		startConditionList.add(new ClassLevelCondition(classLevels));
	}
	
	/**
	 * Method addSubClassCheck.
	 */
	public void addSubClassCheck()
	{
		startConditionList.add(new SubClassCondition());
	}
	
	/**
	 * Method getDescr.
	 * @param player Player
	 * @param isStartNpc boolean
	 * @return String
	 */
	public String getDescr(Player player, boolean isStartNpc)
	{
		if (!isVisible(player))
		{
			return null;
		}
		int state = getDescrState(player, isStartNpc);
		String font = FONT_QUEST_AVAILABLE;
		switch (state)
		{
			case 3:
				font = FONT_QUEST_DONE;
				break;
			case 4:
				font = FONT_QUEST_NOT_AVAILABLE;
				break;
		}
		int fStringId = getQuestIntId();
		if (fStringId >= 10000)
		{
			fStringId -= 5000;
		}
		fStringId = (fStringId * 100) + state;
		return font.concat("[").concat(HtmlUtils.htmlNpcString(fStringId)).concat("]</font>");
	}
	
	/**
	 * Method getDescrState.
	 * @param player Player
	 * @param isStartNpc boolean
	 * @return int
	 */
	public final int getDescrState(Player player, boolean isStartNpc)
	{
		QuestState qs = player.getQuestState(getName());
		int state = 4;
		if (isAvailableFor(player) && isStartNpc && ((qs == null) || (qs.isCreated() && qs.isNowAvailableByTime())))
		{
			state = 1;
		}
		else if ((qs != null) && qs.isStarted())
		{
			state = 2;
		}
		else if ((qs != null) && (qs.isCompleted() || !qs.isNowAvailableByTime()))
		{
			state = 3;
		}
		return state;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getQuestIntId.
	 * @return int
	 */
	public int getQuestIntId()
	{
		return _questId;
	}
	
	/**
	 * Method getParty.
	 * @return int
	 */
	public int getParty()
	{
		return _party;
	}
	
	/**
	 * Method newQuestState.
	 * @param player Player
	 * @param state int
	 * @return QuestState
	 */
	public QuestState newQuestState(Player player, int state)
	{
		QuestState qs = new QuestState(this, player, state);
		Quest.updateQuestInDb(qs);
		return qs;
	}
	
	/**
	 * Method newQuestStateAndNotSave.
	 * @param player Player
	 * @param state int
	 * @return QuestState
	 */
	public QuestState newQuestStateAndNotSave(Player player, int state)
	{
		return new QuestState(this, player, state);
	}
	
	/**
	 * Method notifyAttack.
	 * @param npc NpcInstance
	 * @param qs QuestState
	 */
	public void notifyAttack(NpcInstance npc, QuestState qs)
	{
		String res = null;
		try
		{
			res = onAttack(npc, qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return;
		}
		showResult(npc, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifyDeath.
	 * @param killer Creature
	 * @param victim Creature
	 * @param qs QuestState
	 */
	public void notifyDeath(Creature killer, Creature victim, QuestState qs)
	{
		String res = null;
		try
		{
			res = onDeath(killer, victim, qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return;
		}
		showResult(null, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifyEvent.
	 * @param event String
	 * @param qs QuestState
	 * @param npc NpcInstance
	 */
	public void notifyEvent(String event, QuestState qs, NpcInstance npc)
	{
		String res = null;
		try
		{
			res = onEvent(event, qs, npc);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return;
		}
		showResult(npc, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifyKill.
	 * @param npc NpcInstance
	 * @param qs QuestState
	 */
	public void notifyKill(NpcInstance npc, QuestState qs)
	{
		String res = null;
		try
		{
			res = onKill(npc, qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return;
		}
		showResult(npc, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifyKill.
	 * @param target Player
	 * @param qs QuestState
	 */
	public void notifyKill(Player target, QuestState qs)
	{
		String res = null;
		try
		{
			res = onKill(target, qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return;
		}
		showResult(null, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifyFirstTalk.
	 * @param npc NpcInstance
	 * @param player Player
	 * @return boolean
	 */
	public final boolean notifyFirstTalk(NpcInstance npc, Player player)
	{
		String res = null;
		try
		{
			res = onFirstTalk(npc, player);
		}
		catch (Exception e)
		{
			showError(player, e);
			return true;
		}
		return showResult(npc, player, res, true);
	}
	
	/**
	 * Method notifyTalk.
	 * @param npc NpcInstance
	 * @param qs QuestState
	 * @return boolean
	 */
	public boolean notifyTalk(NpcInstance npc, QuestState qs)
	{
		String res = null;
		try
		{
			res = onTalk(npc, qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return true;
		}
		return showResult(npc, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifySkillUse.
	 * @param npc NpcInstance
	 * @param skill Skill
	 * @param qs QuestState
	 * @return boolean
	 */
	public boolean notifySkillUse(NpcInstance npc, Skill skill, QuestState qs)
	{
		String res = null;
		try
		{
			res = onSkillUse(npc, skill, qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
			return true;
		}
		return showResult(npc, qs.getPlayer(), res);
	}
	
	/**
	 * Method notifyCreate.
	 * @param qs QuestState
	 */
	public void notifyCreate(QuestState qs)
	{
		try
		{
			onCreate(qs);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
		}
	}
	
	/**
	 * Method notifySocialActionUse.
	 * @param qs QuestState
	 * @param actionId int
	 */
	public void notifySocialActionUse(QuestState qs, int actionId)
	{
		try
		{
			onSocialActionUse(qs, actionId);
		}
		catch (Exception e)
		{
			showError(qs.getPlayer(), e);
		}
	}
	
	/**
	 * Method onSocialActionUse.
	 * @param qs QuestState
	 * @param actionId int
	 */
	public void onSocialActionUse(QuestState qs, int actionId)
	{
	}
	
	/**
	 * Method onCreate.
	 * @param qs QuestState
	 */
	public void onCreate(QuestState qs)
	{
	}
	
	/**
	 * Method onAttack.
	 * @param npc NpcInstance
	 * @param qs QuestState
	 * @return String
	 */
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		return null;
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 * @param victim Creature
	 * @param qs QuestState
	 * @return String
	 */
	public String onDeath(Creature killer, Creature victim, QuestState qs)
	{
		return null;
	}
	
	/**
	 * Method onEvent.
	 * @param event String
	 * @param qs QuestState
	 * @param npc NpcInstance
	 * @return String
	 */
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		return null;
	}
	
	/**
	 * Method onKill.
	 * @param npc NpcInstance
	 * @param qs QuestState
	 * @return String
	 */
	public String onKill(NpcInstance npc, QuestState qs)
	{
		return null;
	}
	
	/**
	 * Method onKill.
	 * @param killed Player
	 * @param st QuestState
	 * @return String
	 */
	public String onKill(Player killed, QuestState st)
	{
		return null;
	}
	
	/**
	 * Method onFirstTalk.
	 * @param npc NpcInstance
	 * @param player Player
	 * @return String
	 */
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		return null;
	}
	
	/**
	 * Method onTalk.
	 * @param npc NpcInstance
	 * @param qs QuestState
	 * @return String
	 */
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		return null;
	}
	
	/**
	 * Method onSkillUse.
	 * @param npc NpcInstance
	 * @param skill Skill
	 * @param qs QuestState
	 * @return String
	 */
	public String onSkillUse(NpcInstance npc, Skill skill, QuestState qs)
	{
		return null;
	}
	
	/**
	 * Method onOlympiadEnd.
	 * @param og OlympiadGame
	 * @param qs QuestState
	 */
	public void onOlympiadEnd(OlympiadGame og, QuestState qs)
	{
	}
	
	/**
	 * Method onAbort.
	 * @param qs QuestState
	 */
	public void onAbort(QuestState qs)
	{
	}
	
	/**
	 * Method canAbortByPacket.
	 * @return boolean
	 */
	public boolean canAbortByPacket()
	{
		return true;
	}
	
	/**
	 * Method showError.
	 * @param player Player
	 * @param t Throwable
	 */
	private void showError(Player player, Throwable t)
	{
		_log.error("", t);
		if ((player != null) && player.isGM())
		{
			String res = "<html><body><title>Script error</title>" + LogUtils.dumpStack(t).replace("\n", "<br>") + "</body></html>";
			showResult(null, player, res);
		}
	}
	
	/**
	 * Method showHtmlFile.
	 * @param player Player
	 * @param fileName String
	 * @param showQuestInfo boolean
	 */
	protected void showHtmlFile(Player player, String fileName, boolean showQuestInfo)
	{
		showHtmlFile(player, fileName, showQuestInfo, ArrayUtils.EMPTY_OBJECT_ARRAY);
	}
	
	/**
	 * Method showHtmlFile.
	 * @param player Player
	 * @param fileName String
	 * @param showQuestInfo boolean
	 * @param arg Object[]
	 */
	protected void showHtmlFile(Player player, String fileName, boolean showQuestInfo, Object... arg)
	{
		if (player == null)
		{
			return;
		}
		GameObject target = player.getTarget();
		NpcHtmlMessage npcReply = showQuestInfo ? new ExNpcQuestHtmlMessage(target == null ? 5 : target.getObjectId(), getQuestIntId()) : new NpcHtmlMessage(target == null ? 5 : target.getObjectId());
		npcReply.setFile("quests/" + getClass().getSimpleName() + "/" + fileName);
		if ((arg.length % 2) == 0)
		{
			for (int i = 0; i < arg.length; i += 2)
			{
				npcReply.replace(String.valueOf(arg[i]), String.valueOf(arg[i + 1]));
			}
		}
		player.sendPacket(npcReply);
	}
	
	/**
	 * Method showSimpleHtmFile.
	 * @param player Player
	 * @param fileName String
	 */
	protected void showSimpleHtmFile(Player player, String fileName)
	{
		if (player == null)
		{
			return;
		}
		NpcHtmlMessage npcReply = new NpcHtmlMessage(5);
		npcReply.setFile(fileName);
		player.sendPacket(npcReply);
	}
	
	/**
	 * Method showResult.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param res String
	 * @return boolean
	 */
	private boolean showResult(NpcInstance npc, Player player, String res)
	{
		return showResult(npc, player, res, false);
	}
	
	/**
	 * Method showResult.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param res String
	 * @param isFirstTalk boolean
	 * @return boolean
	 */
	private boolean showResult(NpcInstance npc, Player player, String res, boolean isFirstTalk)
	{
		boolean showQuestInfo = showQuestInfo(player);
		if (isFirstTalk)
		{
			showQuestInfo = false;
		}
		if (res == null)
		{
			return true;
		}
		if (res.isEmpty())
		{
			return false;
		}
		if (res.startsWith("no_quest") || res.equalsIgnoreCase("noquest") || res.equalsIgnoreCase("no-quest"))
		{
			showSimpleHtmFile(player, "no-quest.htm");
		}
		else if (res.equalsIgnoreCase("completed"))
		{
			showSimpleHtmFile(player, "completed-quest.htm");
		}
		else if (res.endsWith(".htm"))
		{
			showHtmlFile(player, res, showQuestInfo);
		}
		else
		{
			NpcHtmlMessage npcReply = showQuestInfo ? new ExNpcQuestHtmlMessage(npc == null ? 5 : npc.getObjectId(), getQuestIntId()) : new NpcHtmlMessage(npc == null ? 5 : npc.getObjectId());
			npcReply.setHtml(res);
			player.sendPacket(npcReply);
		}
		return true;
	}
	
	/**
	 * Method showQuestInfo.
	 * @param player Player
	 * @return boolean
	 */
	private boolean showQuestInfo(Player player)
	{
		QuestState qs = player.getQuestState(getName());
		if ((qs != null) && (qs.getState() != CREATED))
		{
			return false;
		}
		if (!isVisible(player))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method pauseQuestTimers.
	 * @param qs QuestState
	 */
	void pauseQuestTimers(QuestState qs)
	{
		if (qs.getTimers().isEmpty())
		{
			return;
		}
		for (QuestTimer timer : qs.getTimers().values())
		{
			timer.setQuestState(null);
			timer.pause();
		}
		_pausedQuestTimers.put(qs.getPlayer().getObjectId(), qs.getTimers());
	}
	
	/**
	 * Method resumeQuestTimers.
	 * @param qs QuestState
	 */
	void resumeQuestTimers(QuestState qs)
	{
		Map<String, QuestTimer> timers = _pausedQuestTimers.remove(qs.getPlayer().getObjectId());
		if (timers == null)
		{
			return;
		}
		qs.getTimers().putAll(timers);
		for (QuestTimer timer : qs.getTimers().values())
		{
			timer.setQuestState(qs);
			timer.start();
		}
	}
	
	/**
	 * Method str.
	 * @param i long
	 * @return String
	 */
	protected String str(long i)
	{
		return String.valueOf(i);
	}
	
	/**
	 * @author Mobius
	 */
	public class DeSpawnScheduleTimerTask extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		NpcInstance _npc = null;
		
		/**
		 * Constructor for DeSpawnScheduleTimerTask.
		 * @param npc NpcInstance
		 */
		public DeSpawnScheduleTimerTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_npc != null)
			{
				if (_npc.getSpawn() != null)
				{
					_npc.getSpawn().deleteAll();
				}
				else
				{
					_npc.deleteMe();
				}
			}
		}
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
		return addSpawn(npcId, new Location(x, y, z, heading), randomOffset, despawnDelay);
	}
	
	/**
	 * Method addSpawn.
	 * @param npcId int
	 * @param loc Location
	 * @param randomOffset int
	 * @param despawnDelay int
	 * @return NpcInstance
	 */
	public NpcInstance addSpawn(int npcId, Location loc, int randomOffset, int despawnDelay)
	{
		NpcInstance result = Functions.spawn(randomOffset > 50 ? Location.findPointToStay(loc, 0, randomOffset, ReflectionManager.DEFAULT.getGeoIndex()) : loc, npcId);
		if ((despawnDelay > 0) && (result != null))
		{
			ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(result), despawnDelay);
		}
		return result;
	}
	
	/**
	 * Method addSpawnToInstance.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param heading int
	 * @param randomOffset int
	 * @param refId int
	 * @return NpcInstance
	 */
	public static NpcInstance addSpawnToInstance(int npcId, int x, int y, int z, int heading, int randomOffset, int refId)
	{
		return addSpawnToInstance(npcId, new Location(x, y, z, heading), randomOffset, refId);
	}
	
	/**
	 * Method addSpawnToInstance.
	 * @param npcId int
	 * @param loc Location
	 * @param randomOffset int
	 * @param refId int
	 * @return NpcInstance
	 */
	public static NpcInstance addSpawnToInstance(int npcId, Location loc, int randomOffset, int refId)
	{
		try
		{
			NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
			if (template != null)
			{
				NpcInstance npc = NpcHolder.getInstance().getTemplate(npcId).getNewInstance();
				npc.setReflection(refId);
				npc.setSpawnedLoc(randomOffset > 50 ? Location.findPointToStay(loc, 50, randomOffset, npc.getGeoIndex()) : loc);
				npc.spawnMe(npc.getSpawnedLoc());
				return npc;
			}
		}
		catch (Exception e1)
		{
			_log.warn("Could not spawn Npc " + npcId);
		}
		return null;
	}
	
	/**
	 * Method isVisible.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isVisible(Player player)
	{
		return true;
	}
	
	/**
	 * Method isAvailableFor.
	 * @param player Player
	 * @return boolean
	 */
	public final boolean isAvailableFor(Player player)
	{
		for (ICheckStartCondition startCondition : startConditionList)
		{
			if (!startCondition.checkCondition(player))
			{
				return false;
			}
		}
		return true;
	}

	protected final void enterInstance(QuestState st, int instancedZoneId)
	{
		Player player = st.getPlayer();
		if (player == null)
		{
			return;
		}
		Reflection reflection = player.getActiveReflection();
		if (reflection != null)
		{
			if (player.canReenterInstance(instancedZoneId))
			{
				player.teleToLocation(reflection.getTeleportLoc(), reflection);
				onReenterInstance(st, reflection);
			}
		}
		else if (player.canEnterInstance(instancedZoneId))
		{
			Reflection newReflection = ReflectionUtils.enterReflection(player, instancedZoneId);
			onEnterInstance(st, newReflection);
		}
	}

	public void onEnterInstance(QuestState st, Reflection reflection)
	{
	}

	public void onReenterInstance(QuestState st, Reflection reflection)
	{
	}
}

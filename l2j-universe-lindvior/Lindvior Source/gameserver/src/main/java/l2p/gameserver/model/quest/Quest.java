/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.quest;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;
import l2p.commons.dbutils.DbUtils;
import l2p.commons.logging.LogUtils;
import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.TroveUtils;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.base.Experience;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.entity.olympiad.OlympiadGame;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.startcondition.ICheckStartCondition;
import l2p.gameserver.model.quest.startcondition.impl.*;
import l2p.gameserver.network.serverpackets.ExNpcQuestHtmlMessage;
import l2p.gameserver.network.serverpackets.ExQuestNpcLogList;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.HtmlUtils;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Quest {
    private static final Logger _log = LoggerFactory.getLogger(Quest.class);

    public static final String SOUND_ITEMGET = "ItemSound.quest_itemget";
    public static final String SOUND_ACCEPT = "ItemSound.quest_accept";
    public static final String SOUND_MIDDLE = "ItemSound.quest_middle";
    public static final String SOUND_FINISH = "ItemSound.quest_finish";
    public static final String SOUND_GIVEUP = "ItemSound.quest_giveup";
    public static final String SOUND_TUTORIAL = "ItemSound.quest_tutorial";
    public static final String SOUND_JACKPOT = "ItemSound.quest_jackpot";
    public static final String SOUND_HORROR2 = "SkillSound5.horror_02";
    public static final String SOUND_BEFORE_BATTLE = "Itemsound.quest_before_battle";
    public static final String SOUND_FANFARE_MIDDLE = "ItemSound.quest_fanfare_middle";
    public static final String SOUND_FANFARE2 = "ItemSound.quest_fanfare_2";
    public static final String SOUND_BROKEN_KEY = "ItemSound2.broken_key";
    public static final String SOUND_ENCHANT_SUCESS = "ItemSound3.sys_enchant_sucess";
    public static final String SOUND_ENCHANT_FAILED = "ItemSound3.sys_enchant_failed";
    public static final String SOUND_ED_CHIMES05 = "AmdSound.ed_chimes_05";
    public static final String SOUND_ARMOR_WOOD_3 = "ItemSound.armor_wood_3";
    public static final String SOUND_ITEM_DROP_EQUIP_ARMOR_CLOTH = "ItemSound.item_drop_equip_armor_cloth";

    public static final String NO_QUEST_DIALOG = "no-quest";

    private static final String FONT_QUEST_AVAILABLE = "<font color=\"6699ff\">";
    private static final String FONT_QUEST_DONE = "<font color=\"787878\">";
    private static final String FONT_QUEST_NOT_AVAILABLE = "<font color=\"a62f31\">";

    protected static final String TODO_FIND_HTML = "<font color=\"6699ff\">TODO:<br>Find this dialog";

    public static final int ADENA_ID = 57;

    public static final int PARTY_NONE = 0;
    public static final int PARTY_ONE = 1;
    public static final int PARTY_ALL = 2;

    // карта с приостановленными квестовыми таймерами для каждого игрока
    private final Map<Integer, Map<String, QuestTimer>> _pausedQuestTimers = new ConcurrentHashMap<Integer, Map<String, QuestTimer>>();

    private final TIntHashSet _questItems = new TIntHashSet();
    private TIntObjectHashMap<List<QuestNpcLogInfo>> _npcLogList = TroveUtils.emptyIntObjectMap();
    private final List<ICheckStartCondition> startConditionList = new ArrayList<ICheckStartCondition>();
    private final List<ICheckStartCondition> _startConditionList = new ArrayList<ICheckStartCondition>();

    /**
     * Этот метод для регистрации квестовых вещей, которые будут удалены при прекращении квеста, независимо от того, был он закончен или прерван. <strong>Добавлять сюда награды нельзя</strong>.
     */
    public void addQuestItem(int... ids) {
        for (int id : ids) {
            if (id != 0) {
                ItemTemplate i = null;
                i = ItemHolder.getInstance().getTemplate(id);

                if (_questItems.contains(id)) {
                    _log.warn("Item " + i + " multiple times in quest drop in " + getName());
                }

                _questItems.add(id);
            }
        }
    }

    public int[] getItems() {
        return _questItems.toArray();
    }

    public boolean isQuestItem(int id) {
        return _questItems.contains(id);
    }

    /**
     * Update informations regarding quest in database.<BR>
     * <U><I>Actions :</I></U><BR>
     * <LI>Get ID state of the quest recorded in object qs</LI> <LI>Save in database the ID state (with or without the star) for the variable called "&lt;state&gt;" of the quest</LI>
     *
     * @param qs : QuestState
     */
    public static void updateQuestInDb(QuestState qs) {
        updateQuestVarInDb(qs, "<state>", qs.getStateName());
    }

    /**
     * Insert in the database the quest for the player.
     *
     * @param qs    : QuestState pointing out the state of the quest
     * @param var   : String designating the name of the variable for the quest
     * @param value : String designating the value of the variable for the quest
     */
    public static void updateQuestVarInDb(QuestState qs, String var, String value) {
        Player player = qs.getPlayer();
        if (player == null) {
            return;
        }

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("REPLACE INTO character_quests (char_id,name,var,value) VALUES (?,?,?,?)");
            statement.setInt(1, qs.getPlayer().getObjectId());
            statement.setString(2, qs.getQuest().getName());
            statement.setString(3, var);
            statement.setString(4, value);
            statement.executeUpdate();
        } catch (Exception e) {
            _log.error("could not insert char quest:", e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    /**
     * Delete the player's quest from database.
     *
     * @param qs : QuestState pointing out the player's quest
     */
    public static void deleteQuestInDb(QuestState qs) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM character_quests WHERE char_id=? AND name=?");
            statement.setInt(1, qs.getPlayer().getObjectId());
            statement.setString(2, qs.getQuest().getName());
            statement.executeUpdate();
        } catch (Exception e) {
            _log.error("could not delete char quest:", e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    /**
     * Delete a variable of player's quest from the database.
     *
     * @param qs  : object QuestState pointing out the player's quest
     * @param var : String designating the variable characterizing the quest
     */
    public static void deleteQuestVarInDb(QuestState qs, String var) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM character_quests WHERE char_id=? AND name=? AND var=?");
            statement.setInt(1, qs.getPlayer().getObjectId());
            statement.setString(2, qs.getQuest().getName());
            statement.setString(3, var);
            statement.executeUpdate();
        } catch (Exception e) {
            _log.error("could not delete char quest:", e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    /**
     * Add quests to the L2Player.<BR>
     * <BR>
     * <U><I>Action : </U></I><BR>
     * Add state of quests, drops and variables for quests in the HashMap _quest of L2Player
     *
     * @param player : Player who is entering the world
     */
    public static void restoreQuestStates(Player player) {
        Connection con = null;
        PreparedStatement statement = null;
        PreparedStatement invalidQuestData = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            invalidQuestData = con.prepareStatement("DELETE FROM character_quests WHERE char_id=? and name=?");
            statement = con.prepareStatement("SELECT name,value FROM character_quests WHERE char_id=? AND var=?");
            statement.setInt(1, player.getObjectId());
            statement.setString(2, "<state>");
            rset = statement.executeQuery();
            while (rset.next()) {
                String questId = rset.getString("name");
                String state = rset.getString("value");

                if (state.equalsIgnoreCase("Start")) // невзятый квест
                {
                    invalidQuestData.setInt(1, player.getObjectId());
                    invalidQuestData.setString(2, questId);
                    invalidQuestData.executeUpdate();
                    continue;
                }

                // Search quest associated with the ID
                Quest q = QuestManager.getQuest(questId);
                if (q == null) {
                    if (!Config.DONTLOADQUEST) {
                        _log.warn("Unknown quest " + questId + " for player " + player.getName());
                    }
                    continue;
                }

                // Create a new QuestState for the player that will be added to the player's list of quests
                new QuestState(q, player, getStateId(state));
            }

            DbUtils.close(statement, rset);

            // Get list of quests owned by the player from the DB in order to add variables used in the quest.
            statement = con.prepareStatement("SELECT name,var,value FROM character_quests WHERE char_id=?");
            statement.setInt(1, player.getObjectId());
            rset = statement.executeQuery();
            while (rset.next()) {
                String questId = rset.getString("name");
                String var = rset.getString("var");
                String value = rset.getString("value");
                // Get the QuestState saved in the loop before
                QuestState qs = player.getQuestState(questId);
                if (qs == null) {
                    continue;
                }
                // затычка на пропущенный первый конд
                if (var.equals("cond") && (Integer.parseInt(value) < 0)) {
                    value = String.valueOf(Integer.parseInt(value) | 1);
                }
                // Add parameter to the quest
                qs.set(var, value, false);
            }
        } catch (Exception e) {
            _log.error("could not insert char quest:", e);
        } finally {
            DbUtils.closeQuietly(invalidQuestData);
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    protected final String _name;

    protected final int _party;

    protected final int _questId;

    public final static int CREATED = 1;
    public final static int STARTED = 2;
    public final static int COMPLETED = 3;
    public final static int DELAYED = 4;

    public static String getStateName(int state) {
        switch (state) {
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

    public static int getStateId(String state) {
        if (state.equalsIgnoreCase("Start")) {
            return CREATED;
        } else if (state.equalsIgnoreCase("Started")) {
            return STARTED;
        } else if (state.equalsIgnoreCase("Completed")) {
            return COMPLETED;
        } else if (state.equalsIgnoreCase("Delayed")) {
            return DELAYED;
        }
        return CREATED;
    }

    /**
     * Deprecated.
     */
    public Quest(boolean party) {
        this(party ? 1 : 0);
    }

    /**
     * 0 - по ластхиту, 1 - случайно по пати, 2 - всей пати.
     */
    public Quest(int party) {
        _name = getClass().getSimpleName();
        _questId = Integer.parseInt(_name.split("_")[1]);
        _party = party;
        QuestManager.addQuest(this);
    }

    public Quest(int party, int questId, String name) {
        this._name = name;
        this._questId = questId;
        this._party = party;

        QuestManager.addQuest(this);
    }

    public List<QuestNpcLogInfo> getNpcLogList(int cond) {
        return _npcLogList.get(cond);
    }

    /**
     * Add this quest to the list of quests that the passed mob will respond to for Attack Events.<BR>
     * <BR>
     *
     * @param attackIds
     */
    public void addAttackId(int... attackIds) {
        for (int attackId : attackIds) {
            addEventId(attackId, QuestEventType.ATTACKED_WITH_QUEST);
        }
    }

    /**
     * Add this quest to the list of quests that the passed mob will respond to for the specified Event type.<BR>
     * <BR>
     *
     * @param npcId     : id of the NPC to register
     * @param eventType : type of event being registered
     * @return int : npcId
     */
    public NpcTemplate addEventId(int npcId, QuestEventType eventType) {
        try {
            NpcTemplate t = NpcHolder.getInstance().getTemplate(npcId);
            if (t != null) {
                t.addQuestEvent(eventType, this);
            }
            return t;
        } catch (Exception e) {
            _log.error("", e);
            return null;
        }
    }

    /**
     * Add this quest to the list of quests that the passed mob will respond to for Kill Events.<BR>
     * <BR>
     *
     * @param killIds
     * @return int : killId
     */
    public void addKillId(int... killIds) {
        for (int killid : killIds) {
            addEventId(killid, QuestEventType.MOB_KILLED_WITH_QUEST);
        }
    }

    /**
     * Добавляет нпц масив для слушателя при их убийстве, и обновлении пакетом {@link l2p.gameserver.network.serverpackets.ExQuestNpcLogList}
     *
     * @param cond
     * @param varName
     * @param killIds
     */
    public void addKillNpcWithLog(int cond, String varName, int max, int... killIds) {
        if (killIds.length == 0) {
            throw new IllegalArgumentException("Npc list cant be empty!");
        }

        addKillId(killIds);
        if (_npcLogList.isEmpty()) {
            _npcLogList = new TIntObjectHashMap<List<QuestNpcLogInfo>>(5);
        }

        List<QuestNpcLogInfo> vars = _npcLogList.get(cond);
        if (vars == null) {
            _npcLogList.put(cond, (vars = new ArrayList<QuestNpcLogInfo>(5)));
        }

        vars.add(new QuestNpcLogInfo(killIds, varName, max));
    }

    public boolean updateKill(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        if (player == null) {
            return false;
        }
        List<QuestNpcLogInfo> vars = getNpcLogList(st.getCond());
        if (vars == null) {
            return false;
        }
        boolean done = true;
        boolean find = false;
        for (QuestNpcLogInfo info : vars) {
            int count = st.getInt(info.getVarName());
            if (!find && ArrayUtils.contains(info.getNpcIds(), npc.getNpcId())) {
                find = true;
                if (count < info.getMaxCount()) {
                    st.set(info.getVarName(), ++count);
                    player.sendPacket(new ExQuestNpcLogList(st));
                }
            }

            if (count != info.getMaxCount()) {
                done = false;
            }
        }

        return done;
    }

    public void addKillId(Collection<Integer> killIds) {
        for (int killid : killIds) {
            addKillId(killid);
        }
    }

    /**
     * Add this quest to the list of quests that the passed npc will respond to for Skill-Use Events.<BR>
     * <BR>
     *
     * @param npcId : ID of the NPC
     * @return int : ID of the NPC
     */
    public NpcTemplate addSkillUseId(int npcId) {
        return addEventId(npcId, QuestEventType.MOB_TARGETED_BY_SKILL);
    }

    public void addStartNpc(int... npcIds) {
        for (int talkId : npcIds) {
            addStartNpc(talkId);
        }
    }

    /**
     * Add the quest to the NPC's startQuest Вызывает addTalkId
     *
     * @param npcId
     * @return L2NpcTemplate : Start NPC
     */
    public NpcTemplate addStartNpc(int npcId) {
        addTalkId(npcId);
        return addEventId(npcId, QuestEventType.QUEST_START);
    }

    /**
     * Add the quest to the NPC's first-talk (default action dialog)
     *
     * @param npcIds
     * @return L2NpcTemplate : Start NPC
     */
    public void addFirstTalkId(int... npcIds) {
        for (int npcId : npcIds) {
            addEventId(npcId, QuestEventType.NPC_FIRST_TALK);
        }
    }

    /**
     * Add this quest to the list of quests that the passed npc will respond to for Talk Events.<BR>
     * <BR>
     *
     * @param talkIds : ID of the NPC
     * @return int : ID of the NPC
     */
    public void addTalkId(int... talkIds) {
        for (int talkId : talkIds) {
            addEventId(talkId, QuestEventType.QUEST_TALK);
        }
    }

    public void addTalkId(Collection<Integer> talkIds) {
        for (int talkId : talkIds) {
            addTalkId(talkId);
        }
    }

    /**
     * Добавляет проверку уровня при старте квеста
     *
     * @param min - минимальный уровень
     * @param max - максимальный уровень
     */
    public void addLevelCheck(int min, int max) {
        startConditionList.add(new PlayerLevelCondition(min, max));
    }

    public void addLevelCheck(int min) {
        addLevelCheck(min, Experience.getMaxLevel());
    }

    /**
     * Добавляет проверку выполненного квеста
     *
     * @param clazz - название квеста
     */
    public void addQuestCompletedCheck(Class<?> clazz) {
        startConditionList.add(new QuestCompletedCondition(clazz.getSimpleName()));
    }

    /**
     * Добавляет проверку уровня профессии
     *
     * @param classLevels - список уровней классов, для которых доступен квест
     */
    public void addClassLevelCheck(int... classLevels) {
        startConditionList.add(new ClassLevelCondition(classLevels));
    }

    public void addClassLevelCheck(ClassLevel[] classLevels) {
        _startConditionList.add(new ClassLevelCondition2(classLevels));
    }

    public void addNobleCheck(boolean isOnlyNoble) {
        _startConditionList.add(new NobleCondition(isOnlyNoble));
    }

    /**
     * Возвращает название квеста (Берется с npcstring-*.dat) state 1 = "" state 2 = "In Progress" state 3 = "Done" state 4 = ""
     */
    public String getDescr(Player player, boolean isStartNpc) {
        if (!isVisible(player)) {
            return null;
        }

        int state = getDescrState(player, isStartNpc);
        String font = FONT_QUEST_AVAILABLE; // state 1, 2
        switch (state) {
            case 3:
                font = FONT_QUEST_DONE;
                break;
            case 4:
                font = FONT_QUEST_NOT_AVAILABLE;
                break;
        }
        int fStringId = getQuestIntId();
        if (fStringId >= 10000) {
            fStringId -= 5000;
        }
        fStringId = (fStringId * 100) + state;
        return font.concat("[").concat(HtmlUtils.htmlNpcString(fStringId)).concat("]</font>");
    }

    /**
     * Только для отображения квестов в списке либо сравнения квестов!
     *
     * @return - текущее состояние квеста для конкретного игрока
     */
    public final int getDescrState(Player player, boolean isStartNpc) {
        QuestState qs = player.getQuestState(getName());
        int state = 4;
        if (isAvailableFor(player) && isStartNpc && ((qs == null) || (qs.isCreated() && qs.isNowAvailableByTime()))) {
            state = 1;
        } else if ((qs != null) && qs.isStarted()) { // TODO [Ragnarok], уточнить нужна ли здесь проверка на isAvailableFor()
            state = 2;
        } else if ((qs != null) && (qs.isCompleted() || !qs.isNowAvailableByTime())) {
            state = 3;
        }
        return state;
    }

    /**
     * Return name of the quest
     *
     * @return String
     */
    public String getName() {
        return _name;
    }

    /**
     * Return ID of the quest
     *
     * @return int
     */
    public int getQuestIntId() {
        return _questId;
    }

    /**
     * Return party state of quest
     *
     * @return String
     */
    public int getParty() {
        return _party;
    }

    /**
     * Add a new QuestState to the database and return it.
     *
     * @param player
     * @param state
     * @return QuestState : QuestState created
     */
    public QuestState newQuestState(Player player, int state) {
        QuestState qs = new QuestState(this, player, state);
        Quest.updateQuestInDb(qs);
        return qs;
    }

    public QuestState newQuestState(Player player) {
        QuestState qs = new QuestState(this, player, getInitialState());
        return qs;
    }

    public byte getInitialState() {
        return 0;
    }

    public QuestState newQuestStateAndNotSave(Player player, int state) {
        return new QuestState(this, player, state);
    }

    public void notifyAttack(NpcInstance npc, QuestState qs) {
        String res = null;
        try {
            res = onAttack(npc, qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return;
        }
        showResult(npc, qs.getPlayer(), res);
    }

    public void notifyDeath(Creature killer, Creature victim, QuestState qs) {
        String res = null;
        try {
            res = onDeath(killer, victim, qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return;
        }
        showResult(null, qs.getPlayer(), res);
    }

    public void notifyEvent(String event, QuestState qs, NpcInstance npc) {
        String res = null;
        try {
            res = onEvent(event, qs, npc);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return;
        }
        showResult(npc, qs.getPlayer(), res);
    }

    public void notifyKill(NpcInstance npc, QuestState qs) {
        String res = null;
        try {
            res = onKill(npc, qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return;
        }
        showResult(npc, qs.getPlayer(), res);
    }

    public void notifyKill(Player target, QuestState qs) {
        String res = null;
        try {
            res = onKill(target, qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return;
        }
        showResult(null, qs.getPlayer(), res);
    }

    /**
     * Override the default NPC dialogs when a quest defines this for the given NPC
     */
    public final boolean notifyFirstTalk(NpcInstance npc, Player player) {
        String res = null;
        try {
            res = onFirstTalk(npc, player);
        } catch (Exception e) {
            showError(player, e);
            return true;
        }
        // if the quest returns text to display, display it. Otherwise, use the default npc text.
        return showResult(npc, player, res, true);
    }

    public boolean notifyTalk(NpcInstance npc, QuestState qs) {
        String res = null;
        try {
            res = onTalk(npc, qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return true;
        }
        return showResult(npc, qs.getPlayer(), res);
    }

    public boolean notifySkillUse(NpcInstance npc, Skill skill, QuestState qs) {
        String res = null;
        try {
            res = onSkillUse(npc, skill, qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
            return true;
        }
        return showResult(npc, qs.getPlayer(), res);
    }

    public void notifyCreate(QuestState qs) {
        try {
            onCreate(qs);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
        }
    }

    public void notifySocialActionUse(QuestState qs, int actionId) {
        try {
            onSocialActionUse(qs, actionId);
        } catch (Exception e) {
            showError(qs.getPlayer(), e);
        }
    }

    public void onSocialActionUse(QuestState qs, int actionId) {
    }

    public void onCreate(QuestState qs) {
    }

    public String onAttack(NpcInstance npc, QuestState qs) {
        return null;
    }

    public String onDeath(Creature killer, Creature victim, QuestState qs) {
        return null;
    }

    public String onEvent(String event, QuestState qs, NpcInstance npc) {
        return null;
    }

    public String onKill(NpcInstance npc, QuestState qs) {
        return null;
    }

    public String onKill(Player killed, QuestState st) {
        return null;
    }

    public String onFirstTalk(NpcInstance npc, Player player) {
        return null;
    }

    public String onTalk(NpcInstance npc, QuestState qs) {
        return null;
    }

    public String onSkillUse(NpcInstance npc, Skill skill, QuestState qs) {
        return null;
    }

    public void onOlympiadEnd(OlympiadGame og, QuestState qs) {
    }

    public void onAbort(QuestState qs) {
    }

    public boolean canAbortByPacket() {
        return true;
    }

    /**
     * Show message error to player who has an access level greater than 0
     *
     * @param player : L2Player
     * @param t      : Throwable
     */
    private void showError(Player player, Throwable t) {
        _log.error("", t);
        if ((player != null) && player.isGM()) {
            String res = "<html><body><title>Script error</title>" + LogUtils.dumpStack(t).replace("\n", "<br>") + "</body></html>";
            showResult(null, player, res);
        }
    }

    protected void showHtmlFile(Player player, String fileName, boolean showQuestInfo) {
        showHtmlFile(player, fileName, showQuestInfo, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    protected void showHtmlFile(Player player, String fileName, boolean showQuestInfo, Object... arg) {
        if (player == null) {
            return;
        }

        GameObject target = player.getTarget();
        NpcHtmlMessage npcReply = showQuestInfo ? new ExNpcQuestHtmlMessage(target == null ? 5 : target.getObjectId(), getQuestIntId()) : new NpcHtmlMessage(target == null ? 5 : target.getObjectId());
        npcReply.setFile("quests/" + getClass().getSimpleName() + "/" + fileName);

        if ((arg.length % 2) == 0) {
            for (int i = 0; i < arg.length; i += 2) {
                npcReply.replace(String.valueOf(arg[i]), String.valueOf(arg[i + 1]));
            }
        }

        player.sendPacket(npcReply);
    }

    protected void showSimpleHtmFile(Player player, String fileName) {
        if (player == null) {
            return;
        }

        NpcHtmlMessage npcReply = new NpcHtmlMessage(5);
        npcReply.setFile(fileName);
        player.sendPacket(npcReply);
    }

    /**
     * Show a message to player.<BR>
     * <BR>
     * <U><I>Concept : </I></U><BR>
     * 3 cases are managed according to the value of the parameter "res" :<BR>
     * <LI><U>"res" ends with string ".html" :</U> an HTML is opened in order to be shown in a dialog box</LI> <LI><U>"res" starts with tag "html" :</U> the message hold in "res" is shown in a dialog box</LI> <LI><U>"res" is null :</U> do not show any message</LI> <LI><U>"res" is empty string :</U>
     * show default message</LI> <LI><U>otherwise :</U> the message hold in "res" is shown in chat box</LI>
     *
     * @param npc
     * @param player
     * @param res    : String pointing out the message to show at the player
     */
    private boolean showResult(NpcInstance npc, Player player, String res) {
        return showResult(npc, player, res, false);
    }

    private boolean showResult(NpcInstance npc, Player player, String res, boolean isFirstTalk) {
        boolean showQuestInfo = showQuestInfo(player);
        if (isFirstTalk) {
            showQuestInfo = false;
        }
        if (res == null) {
            return true;
        }
        if (res.isEmpty()) {
            return false;
        }
        if (res.startsWith("no_quest") || res.equalsIgnoreCase("noquest") || res.equalsIgnoreCase("no-quest")) {
            showSimpleHtmFile(player, "no-quest.htm");
        } else if (res.equalsIgnoreCase("completed")) {
            showSimpleHtmFile(player, "completed-quest.htm");
        } else if (res.endsWith(".htm")) {
            showHtmlFile(player, res, showQuestInfo);
        } else {
            NpcHtmlMessage npcReply = showQuestInfo ? new ExNpcQuestHtmlMessage(npc == null ? 5 : npc.getObjectId(), getQuestIntId()) : new NpcHtmlMessage(npc == null ? 5 : npc.getObjectId());
            npcReply.setHtml(res);
            player.sendPacket(npcReply);
        }
        return true;
    }

    // Проверяем, показывать ли информацию о квесте в диалоге.
    private boolean showQuestInfo(Player player) {
        QuestState qs = player.getQuestState(getName());
        if ((qs != null) && (qs.getState() != CREATED)) {
            return false;
        }
        if (!isVisible(player)) {
            return false;
        }

        return true;
    }

    // Останавливаем и сохраняем таймеры (при выходе из игры)
    void pauseQuestTimers(QuestState qs) {
        if (qs.getTimers().isEmpty()) {
            return;
        }

        for (QuestTimer timer : qs.getTimers().values()) {
            timer.setQuestState(null);
            timer.pause();
        }

        _pausedQuestTimers.put(qs.getPlayer().getObjectId(), qs.getTimers());
    }

    // Восстанавливаем таймеры (при входе в игру)
    void resumeQuestTimers(QuestState qs) {
        Map<String, QuestTimer> timers = _pausedQuestTimers.remove(qs.getPlayer().getObjectId());
        if (timers == null) {
            return;
        }

        qs.getTimers().putAll(timers);

        for (QuestTimer timer : qs.getTimers().values()) {
            timer.setQuestState(qs);
            timer.start();
        }
    }

    protected String str(long i) {
        return String.valueOf(i);
    }

    // =========================================================
    // QUEST SPAWNS
    // =========================================================

    public class DeSpawnScheduleTimerTask extends RunnableImpl {
        NpcInstance _npc = null;

        public DeSpawnScheduleTimerTask(NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void runImpl() throws Exception {
            if (_npc != null) {
                if (_npc.getSpawn() != null) {
                    _npc.getSpawn().deleteAll();
                } else {
                    _npc.deleteMe();
                }
            }
        }
    }

    public NpcInstance addSpawn(int npcId, int x, int y, int z, int heading, int randomOffset, int despawnDelay) {
        return addSpawn(npcId, new Location(x, y, z, heading), randomOffset, despawnDelay);
    }

    public NpcInstance addSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffSet, long despawnDelay) {
        return addSpawn(npcId, x, y, z, heading, randomOffSet, despawnDelay);
    }

    public NpcInstance addSpawn(int npcId, Location loc, int randomOffset, int despawnDelay) {
        NpcInstance result = Functions.spawn(randomOffset > 50 ? Location.findPointToStay(loc, 0, randomOffset, ReflectionManager.DEFAULT.getGeoIndex()) : loc, npcId);
        if ((despawnDelay > 0) && (result != null)) {
            ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(result), despawnDelay);
        }
        return result;
    }

    /**
     * Добавляет спаун с числовым значением разброса - от 50 до randomOffset. Если randomOffset указан мене 50, то координаты не меняются.
     */
    public static NpcInstance addSpawnToInstance(int npcId, int x, int y, int z, int heading, int randomOffset, int refId) {
        return addSpawnToInstance(npcId, new Location(x, y, z, heading), randomOffset, refId);
    }

    public static NpcInstance addSpawnToInstance(int npcId, Location loc, int randomOffset, int refId) {
        try {
            NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
            if (template != null) {
                NpcInstance npc = NpcHolder.getInstance().getTemplate(npcId).getNewInstance();
                npc.setReflection(refId);
                npc.setSpawnedLoc(randomOffset > 50 ? Location.findPointToStay(loc, 50, randomOffset, npc.getGeoIndex()) : loc);
                npc.spawnMe(npc.getSpawnedLoc());
                return npc;
            }
        } catch (Exception e1) {
            _log.warn("Could not spawn Npc " + npcId);
        }
        return null;
    }

    public boolean isVisible(Player player) {
        return true;
    }

    /**
     * Метод для проверки квеста на доступность данному персонажу. Используется при показе списка квестов, Доступные квесты выделяются синим, недоступные - красным Также используется для показа соответствующих диалогов
     *
     * @param player - игрок, для которого проверяем условия квеста
     * @return true, если игрок соответствует требованиям квеста, иначе - false
     */
    public final boolean isAvailableFor(Player player) {
        for (ICheckStartCondition startCondition : startConditionList) {
            if (!startCondition.checkCondition(player)) {
                return false;
            }
        }
        return true;
    }

    public static String getNoQuestMsg(Player player) {
        String result = HtmCache.getInstance().getNotNull("data/html-ru/no-quest.htm", player);
        if ((result != null) && (result.length() > 0)) {
            return result;
        }
        return "<html><body>У Вас нет квестов, связанных с этим существом, или Вы не удоволетворяте его минимальным критериям.</body></html>";
    }

    public static String getLowLevelMsg(int level) {
        return "<html><body>Вы должны достичь %level% уровня, чтобы получить возможность взять это задание.</body></html>".replace("%level%", String.valueOf(level));
    }

    public static String getHighLevelMsg(int level) {
        return "<html><body>Ваш уровень должен быть меньше %level% уровня, чтобы взять это задание.</body></html>".replace("%level%", String.valueOf(level));
    }

    public String getAlreadyCompletedMsg(Player player) {
        String result = HtmCache.getInstance().getNotNull("data/html-ru/completed-quest.htm", player);
        if ((result != null) && (result.length() > 0)) {
            return result;
        }
        return isDailyQuest() ? "<html><body>Этот квест уже был выполнен Вами.<br>(Это задание доступно один раз в день. Обновление квеста происходит ежедневно в 6:30 утра.)</body></html>" : "<html><body>Этот квест уже был выполнен Вами.</body></html>";
    }

    public boolean isDailyQuest() {
        return false;
    }

    public boolean canBeStarted(Player player) {
        return true;
    }

    public void addRaceCheck(Race paramArrayOfRace) {
        this.startConditionList.add(new PlayerRaceCondition(new Race[]
                {
                        paramArrayOfRace
                }));
    }

    protected final void enterInstance(QuestState st, int instancedZoneId) {
        Player player = st.getPlayer();
        if (player == null) {
            return;
        }
        Reflection reflection = player.getActiveReflection();
        if (reflection != null) {
            if (player.canReenterInstance(instancedZoneId)) {
                player.teleToLocation(reflection.getTeleportLoc(), reflection);
                onReenterInstance(st, reflection);
            }
        } else if (player.canEnterInstance(instancedZoneId)) {
            Reflection newReflection = ReflectionUtils.enterReflection(player, instancedZoneId);
            onEnterInstance(st, newReflection);
        }
    }

    public void onEnterInstance(QuestState st, Reflection reflection) {
    }

    public void onReenterInstance(QuestState st, Reflection reflection) {
    }

    public boolean checkStartCondition(Player player) {
        for (ICheckStartCondition startCondition : this._startConditionList) {
            if (!startCondition.checkCondition(player)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkTalkNpc(NpcInstance npc, QuestState st) {
        return true;
    }

    public void onHaosBattleEnd(Player player, boolean isWinner) {
    }

    public boolean checkStartNpc(NpcInstance npc, Player player) {
        return true;
    }
}

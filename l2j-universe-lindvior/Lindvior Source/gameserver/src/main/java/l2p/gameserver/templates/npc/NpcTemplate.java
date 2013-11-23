/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.npc;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.commons.util.TroveUtils;
import l2p.gameserver.ai.CharacterAI;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.TeleportLocation;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.instances.RaidBossInstance;
import l2p.gameserver.model.instances.ReflectionBossInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestEventType;
import l2p.gameserver.model.reward.RewardList;
import l2p.gameserver.model.reward.RewardType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Scripts;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.templates.CharTemplate;
import l2p.gameserver.templates.StatsSet;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public final class NpcTemplate extends CharTemplate {
    private static final Logger _log = LoggerFactory.getLogger(NpcTemplate.class);

    @SuppressWarnings("unchecked")
    public static final Constructor<NpcInstance> DEFAULT_TYPE_CONSTRUCTOR = (Constructor<NpcInstance>) NpcInstance.class.getConstructors()[0];
    @SuppressWarnings("unchecked")
    public static final Constructor<CharacterAI> DEFAULT_AI_CONSTRUCTOR = (Constructor<CharacterAI>) CharacterAI.class.getConstructors()[0];

    public static enum ShotsType {
        NONE,
        SOUL,
        SPIRIT,
        BSPIRIT,
        SOUL_SPIRIT,
        SOUL_BSPIRIT
    }

    public final int npcId;
    public final String name;
    public final String title;
    // не используется - public final String sex;
    public final int level;
    public final long rewardExp;
    public final int rewardSp;
    public final int rewardRp;
    public final int aggroRange;
    public final int rhand;
    public final int lhand;
    public final double rateHp;

    private Faction faction = Faction.NONE;

    public final int displayId;

    public final ShotsType shots;

    public boolean isRaid = false;
    private final StatsSet _AIParams;

    /**
     * fixed skills
     */
    private int race = 0;
    private final int _castleId;

    private Map<RewardType, RewardList> _rewards = Collections.emptyMap();

    private TIntObjectHashMap<TeleportLocation[]> _teleportList = TroveUtils.emptyIntObjectMap();
    private List<MinionData> _minions = Collections.emptyList();
    private List<AbsorbInfo> _absorbInfo = Collections.emptyList();

    private List<ClassId> _teachInfo = Collections.emptyList();
    private Map<QuestEventType, Quest[]> _questEvents = Collections.emptyMap();
    private TIntObjectHashMap<Skill> _skills = TroveUtils.emptyIntObjectMap();

    private Skill[] _damageSkills = Skill.EMPTY_ARRAY;
    private Skill[] _dotSkills = Skill.EMPTY_ARRAY;
    private Skill[] _debuffSkills = Skill.EMPTY_ARRAY;
    private Skill[] _buffSkills = Skill.EMPTY_ARRAY;
    private Skill[] _stunSkills = Skill.EMPTY_ARRAY;
    private Skill[] _healSkills = Skill.EMPTY_ARRAY;

    private Class<NpcInstance> _classType = NpcInstance.class;
    private Constructor<NpcInstance> _constructorType = DEFAULT_TYPE_CONSTRUCTOR;

    private Class<CharacterAI> _classAI = CharacterAI.class;
    private Constructor<CharacterAI> _constructorAI = DEFAULT_AI_CONSTRUCTOR;

    private String _htmRoot;
    private int summonPoints = 1;

    private int _isChaos;

    private List<NpcString> _dialogs = new ArrayList<NpcString>();
    private int _talkDelay = 2147483647;

    private boolean _isTalkRandom = false;
    private TIntObjectHashMap<WalkerRoute> _walkerRoute = new TIntObjectHashMap<WalkerRoute>();
    private RandomActions _randomActions = null;

    /**
     * Constructor<?> of L2Character.<BR><BR>
     *
     * @param set The StatsSet object to transfer data to the method
     */
    public NpcTemplate(StatsSet set) {
        super(set);
        npcId = set.getInteger("npcId");
        displayId = set.getInteger("displayId");

        name = set.getString("name");
        title = set.getString("title");
        // sex = set.getString("sex");
        level = set.getInteger("level");
        rewardExp = set.getLong("rewardExp");
        rewardSp = set.getInteger("rewardSp");
        rewardRp = set.getInteger("rewardRp");
        aggroRange = set.getInteger("aggroRange");
        rhand = set.getInteger("rhand", 0);
        lhand = set.getInteger("lhand", 0);
        rateHp = set.getDouble("baseHpRate");
        _htmRoot = set.getString("htm_root", null);
        _isChaos = set.getInteger("is_chaos", 0);
        shots = set.getEnum("shots", ShotsType.class, ShotsType.NONE);
        _castleId = set.getInteger("castle_id", 0);
        _AIParams = (StatsSet) set.getObject("aiParams", StatsSet.EMPTY);

        setType(set.getString("type", null));
        setAI(set.getString("ai_type", null));
    }

    public Class<? extends NpcInstance> getInstanceClass() {
        return _classType;
    }

    public Constructor<? extends NpcInstance> getInstanceConstructor() {
        return _constructorType;
    }

    public boolean isInstanceOf(Class<?> _class) {
        return _class.isAssignableFrom(_classType);
    }

    /**
     * Создает новый инстанс NPC. Для него следует вызывать (именно в этом порядке):
     * <br> setSpawnedLoc (обязательно)
     * <br> setReflection (если reflection не базовый)
     * <br> setChampion (опционально)
     * <br> setCurrentHpMp (если вызывался setChampion)
     * <br> spawnMe (в качестве параметра брать getSpawnedLoc)
     */
    public NpcInstance getNewInstance() {
        try {
            return _constructorType.newInstance(IdFactory.getInstance().getNextId(), this);
        } catch (Exception e) {
            _log.error("Unable to create instance of NPC " + npcId, e);
        }

        return null;
    }

    public CharacterAI getNewAI(NpcInstance npc) {
        try {
            return _constructorAI.newInstance(npc);
        } catch (Exception e) {
            _log.error("Unable to create ai of NPC " + npcId, e);
        }

        return new CharacterAI(npc);
    }

    @SuppressWarnings("unchecked")
    private void setType(String type) {
        Class<NpcInstance> classType = null;
        try {
            classType = (Class<NpcInstance>) Class.forName("l2p.gameserver.model.instances." + type + "Instance");
        } catch (ClassNotFoundException e) {
            classType = (Class<NpcInstance>) Scripts.getInstance().getClasses().get("npc.model." + type + "Instance");
        }

        if (classType == null)
            _log.error("Not found type class for type: " + type + ". NpcId: " + npcId);
        else {
            _classType = classType;
            _constructorType = (Constructor<NpcInstance>) _classType.getConstructors()[0];
        }

        if (_classType.isAnnotationPresent(Deprecated.class))
            _log.error("Npc type: " + type + ", is deprecated. NpcId: " + npcId);

        //TODO [G1ta0] сделать поле в соотвествующих классах
        isRaid = isInstanceOf(RaidBossInstance.class) && !isInstanceOf(ReflectionBossInstance.class);
    }

    @SuppressWarnings("unchecked")
    private void setAI(String ai) {
        Class<CharacterAI> classAI = null;
        try {
            classAI = (Class<CharacterAI>) Class.forName("l2p.gameserver.ai." + ai);
        } catch (ClassNotFoundException e) {
            classAI = (Class<CharacterAI>) Scripts.getInstance().getClasses().get("ai." + ai);
        }

        if (classAI == null)
            _log.error("Not found ai class for ai: " + ai + ". NpcId: " + npcId);
        else {
            _classAI = classAI;
            _constructorAI = (Constructor<CharacterAI>) _classAI.getConstructors()[0];
        }

        if (_classAI.isAnnotationPresent(Deprecated.class))
            _log.error("Ai type: " + ai + ", is deprecated. NpcId: " + npcId);
    }

    public void addTeachInfo(ClassId classId) {
        if (_teachInfo.isEmpty())
            _teachInfo = new ArrayList<ClassId>(1);
        _teachInfo.add(classId);
    }

    public List<ClassId> getTeachInfo() {
        return _teachInfo;
    }

    public boolean canTeach(ClassId classId) {
        return _teachInfo.contains(classId);
    }

    public void addTeleportList(int id, TeleportLocation[] list) {
        if (_teleportList.isEmpty())
            _teleportList = new TIntObjectHashMap<TeleportLocation[]>(1);

        _teleportList.put(id, list);
    }

    public TeleportLocation[] getTeleportList(int id) {
        return _teleportList.get(id);
    }

    public TIntObjectHashMap<TeleportLocation[]> getTeleportList() {
        return _teleportList;
    }

    public void putRewardList(RewardType rewardType, RewardList list) {
        if (_rewards.isEmpty())
            _rewards = new HashMap<RewardType, RewardList>(RewardType.values().length);
        _rewards.put(rewardType, list);
    }

    public RewardList getRewardList(RewardType t) {
        return _rewards.get(t);
    }

    public Map<RewardType, RewardList> getRewards() {
        return _rewards;
    }

    public void addAbsorbInfo(AbsorbInfo absorbInfo) {
        if (_absorbInfo.isEmpty())
            _absorbInfo = new ArrayList<AbsorbInfo>(1);

        _absorbInfo.add(absorbInfo);
    }

    public void addMinion(MinionData minion) {
        if (_minions.isEmpty())
            _minions = new ArrayList<MinionData>(1);

        _minions.add(minion);
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }

    public void addSkill(Skill skill) {
        if (_skills.isEmpty())
            _skills = new TIntObjectHashMap<Skill>();

        _skills.put(skill.getId(), skill);

        //TODO [G1ta0] перенести в AI
        if (skill.isNotUsedByAI() || skill.getTargetType() == Skill.SkillTargetType.TARGET_NONE || skill.getSkillType() == Skill.SkillType.NOTDONE || !skill.isActive())
            return;

        switch (skill.getSkillType()) {
            case PDAM:
            case MANADAM:
            case MDAM:
            case DRAIN:
            case DRAIN_SOUL: {
                boolean added = false;

                if (skill.hasEffects())
                    for (EffectTemplate eff : skill.getEffectTemplates())
                        switch (eff.getEffectType()) {
                            case Stun:
                                _stunSkills = ArrayUtils.add(_stunSkills, skill);
                                added = true;
                                break;
                            case DamOverTime:
                            case DamOverTimeLethal:
                            case ManaDamOverTime:
                            case LDManaDamOverTime:
                                _dotSkills = ArrayUtils.add(_dotSkills, skill);
                                added = true;
                                break;
                            default:
                                break;
                        }

                if (!added)
                    _damageSkills = ArrayUtils.add(_damageSkills, skill);

                break;
            }
            case DOT:
            case MDOT:
            case POISON:
            case BLEED:
                _dotSkills = ArrayUtils.add(_dotSkills, skill);
                break;
            case DEBUFF:
            case SLEEP:
            case ROOT:
            case PARALYZE:
            case MUTE:
            case TELEPORT_NPC:
            case AGGRESSION:
                _debuffSkills = ArrayUtils.add(_debuffSkills, skill);
                break;
            case BUFF:
                _buffSkills = ArrayUtils.add(_buffSkills, skill);
                break;
            case STUN:
                _stunSkills = ArrayUtils.add(_stunSkills, skill);
                break;
            case HEAL:
            case HEAL_PERCENT:
            case HOT:
                _healSkills = ArrayUtils.add(_healSkills, skill);
                break;
            default:

                break;
        }
    }

    public Skill[] getDamageSkills() {
        return _damageSkills;
    }

    public Skill[] getDotSkills() {
        return _dotSkills;
    }

    public Skill[] getDebuffSkills() {
        return _debuffSkills;
    }

    public Skill[] getBuffSkills() {
        return _buffSkills;
    }

    public Skill[] getStunSkills() {
        return _stunSkills;
    }

    public Skill[] getHealSkills() {
        return _healSkills;
    }

    public List<MinionData> getMinionData() {
        return _minions;
    }

    public TIntObjectHashMap<Skill> getSkills() {
        return _skills;
    }

    public void addQuestEvent(QuestEventType EventType, Quest q) {
        if (_questEvents.isEmpty())
            _questEvents = new HashMap<QuestEventType, Quest[]>();

        if (_questEvents.get(EventType) == null)
            _questEvents.put(EventType, new Quest[]{q});
        else {
            Quest[] _quests = _questEvents.get(EventType);
            int len = _quests.length;

            Quest[] tmp = new Quest[len + 1];
            for (int i = 0; i < len; i++) {
                if (_quests[i].getName().equals(q.getName())) {
                    _quests[i] = q;
                    return;
                }
                tmp[i] = _quests[i];
            }
            tmp[len] = q;

            _questEvents.put(EventType, tmp);
        }
    }

    public Quest[] getEventQuests(QuestEventType EventType) {
        return _questEvents.get(EventType);
    }

    public int getRace() {
        return race;
    }

    public void setRace(int newrace) {
        race = newrace;
    }

    public boolean isUndead() {
        return race == 1;
    }

    @Override
    public String toString() {
        return "Npc template " + name + "[" + npcId + "]";
    }

    @Override
    public int getNpcId() {
        return npcId;
    }

    public String getName() {
        return name;
    }

    public final StatsSet getAIParams() {
        return _AIParams;
    }

    public List<AbsorbInfo> getAbsorbInfo() {
        return _absorbInfo;
    }

    public int getCastleId() {
        return _castleId;
    }

    public Map<QuestEventType, Quest[]> getQuestEvents() {
        return _questEvents;
    }

    public String getHtmRoot() {
        return _htmRoot;
    }

    public int getSummonPoints() {
        return summonPoints;
    }

    public void setSummonPoints(int count) {
        summonPoints = count;
    }

    public List<NpcString> getTalkerDialogs() {
        return this._dialogs;
    }

    public void setTalkDelay(int talkDelay) {
        if (this._talkDelay <= 0) {
            _log.warn("Cannot set talk delay to zero - we shouldn't spam messages so fast.");
            return;
        }
        this._talkDelay = talkDelay;
    }

    public int getTalkDelay() {
        return this._talkDelay;
    }

    public void addTalkerDialog(NpcString dialog) {
        if (this._dialogs.size() > 32767) {
            _log.warn("Cannot add more than 32767 dialogs to NPC. Skipping dialog.");
            return;
        }
        this._dialogs.add(dialog);
    }

    public void addTalkerDialog(String dialog) {
        this._dialogs.add(NpcString.valueOf(dialog));
    }

    public void setRandomTalking() {
        this._isTalkRandom = true;
    }

    public boolean isTalkRandom() {
        return this._isTalkRandom;
    }

    public void addWalkerRoute(WalkerRoute walkerRoute) {
        if (!walkerRoute.isValid()) {
            return;
        }
        _walkerRoute.put(walkerRoute.getId(), walkerRoute);
    }

    public WalkerRoute getWalkerRoute(int id) {
        return (WalkerRoute) _walkerRoute.get(id);
    }

    public void setRandomActions(RandomActions randomActions) {
        _randomActions = randomActions;
    }

    public RandomActions getRandomActions() {
        return _randomActions;
    }

    public int getIsChaos() {
        return _isChaos;
    }
}

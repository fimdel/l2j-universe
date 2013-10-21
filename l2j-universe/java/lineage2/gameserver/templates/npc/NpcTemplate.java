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
package lineage2.gameserver.templates.npc;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.util.TroveUtils;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.TeleportLocation;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.model.instances.ReflectionBossInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.model.reward.RewardType;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.StatsSet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NpcTemplate extends CharTemplate
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(NpcTemplate.class);
	/**
	 * Field DEFAULT_TYPE_CONSTRUCTOR.
	 */
	@SuppressWarnings("unchecked")
	public static final Constructor<NpcInstance> DEFAULT_TYPE_CONSTRUCTOR = (Constructor<NpcInstance>) NpcInstance.class.getConstructors()[0];
	/**
	 * Field DEFAULT_AI_CONSTRUCTOR.
	 */
	@SuppressWarnings("unchecked")
	public static final Constructor<CharacterAI> DEFAULT_AI_CONSTRUCTOR = (Constructor<CharacterAI>) CharacterAI.class.getConstructors()[0];
	
	/**
	 * @author Mobius
	 */
	public static enum ShotsType
	{
		/**
		 * Field NONE.
		 */
		NONE,
		/**
		 * Field SOUL.
		 */
		SOUL,
		/**
		 * Field SPIRIT.
		 */
		SPIRIT,
		/**
		 * Field BSPIRIT.
		 */
		BSPIRIT,
		/**
		 * Field SOUL_SPIRIT.
		 */
		SOUL_SPIRIT,
		/**
		 * Field SOUL_BSPIRIT.
		 */
		SOUL_BSPIRIT
	}
	
	/**
	 * Field npcId.
	 */
	public final int npcId;
	/**
	 * Field name.
	 */
	public final String name;
	/**
	 * Field title.
	 */
	public final String title;
	/**
	 * Field level.
	 */
	public final int level;
	/**
	 * Field rewardExp.
	 */
	public final long rewardExp;
	/**
	 * Field rewardSp.
	 */
	public final long rewardSp;
	/**
	 * Field rewardRp.
	 */
	public final int rewardRp;
	/**
	 * Field aggroRange.
	 */
	public final int aggroRange;
	/**
	 * Field rhand.
	 */
	public final int rhand;
	/**
	 * Field lhand.
	 */
	public final int lhand;
	/**
	 * Field rateHp.
	 */
	public final double rateHp;
	/**
	 * Field faction.
	 */
	private Faction faction = Faction.NONE;
	/**
	 * Field jClass.
	 */
	public final String jClass;
	/**
	 * Field displayId.
	 */
	public final int displayId;
	/**
	 * Field shots.
	 */
	public final ShotsType shots;
	/**
	 * Field isRaid.
	 */
	public boolean isRaid = false;
	/**
	 * Field _AIParams.
	 */
	private final StatsSet _AIParams;
	/**
	 * Field race.
	 */
	private int race = 0;
	/**
	 * Field _castleId.
	 */
	private final int _castleId;
	/**
	 * Field _rewards.
	 */
	private Map<RewardType, RewardList> _rewards = Collections.emptyMap();
	/**
	 * Field _teleportList.
	 */
	private TIntObjectHashMap<TeleportLocation[]> _teleportList = TroveUtils.emptyIntObjectMap();
	/**
	 * Field _minions.
	 */
	private List<MinionData> _minions = Collections.emptyList();
	/**
	 * Field _absorbInfo.
	 */
	private List<AbsorbInfo> _absorbInfo = Collections.emptyList();
	/**
	 * Field _teachInfo.
	 */
	private List<ClassId> _teachInfo = Collections.emptyList();
	/**
	 * Field _questEvents.
	 */
	private Map<QuestEventType, Quest[]> _questEvents = Collections.emptyMap();
	/**
	 * Field _skills.
	 */
	private TIntObjectHashMap<Skill> _skills = TroveUtils.emptyIntObjectMap();
	/**
	 * Field _damageSkills.
	 */
	private Skill[] _damageSkills = Skill.EMPTY_ARRAY;
	/**
	 * Field _dotSkills.
	 */
	private Skill[] _dotSkills = Skill.EMPTY_ARRAY;
	/**
	 * Field _debuffSkills.
	 */
	private Skill[] _debuffSkills = Skill.EMPTY_ARRAY;
	/**
	 * Field _buffSkills.
	 */
	private Skill[] _buffSkills = Skill.EMPTY_ARRAY;
	/**
	 * Field _stunSkills.
	 */
	private Skill[] _stunSkills = Skill.EMPTY_ARRAY;
	/**
	 * Field _healSkills.
	 */
	private Skill[] _healSkills = Skill.EMPTY_ARRAY;
	/**
	 * Field _classType.
	 */
	private Class<NpcInstance> _classType = NpcInstance.class;
	/**
	 * Field _constructorType.
	 */
	private Constructor<NpcInstance> _constructorType = DEFAULT_TYPE_CONSTRUCTOR;
	/**
	 * Field _classAI.
	 */
	private Class<CharacterAI> _classAI = CharacterAI.class;
	/**
	 * Field _constructorAI.
	 */
	private Constructor<CharacterAI> _constructorAI = DEFAULT_AI_CONSTRUCTOR;
	/**
	 * Field _htmRoot.
	 */
	private final String _htmRoot;
	/**
	 * Field summonPoints.
	 */
	private int summonPoints = 1;
	
	private RandomActions _randomActions = null;
	private TIntObjectHashMap<WalkerRoute> _walkerRoute = new TIntObjectHashMap<WalkerRoute>();
	private boolean displayHpBar;
	
	/**
	 * Constructor for NpcTemplate.
	 * @param set StatsSet
	 */
	public NpcTemplate(StatsSet set)
	{
		super(set);
		npcId = set.getInteger("npcId");
		displayId = set.getInteger("displayId");
		name = set.getString("name");
		title = set.getString("title");
		level = set.getInteger("level");
		rewardExp = set.getLong("rewardExp");
		rewardSp = set.getLong("rewardSp");
		rewardRp = set.getInteger("rewardRp");
		aggroRange = set.getInteger("aggroRange");
		rhand = set.getInteger("rhand", 0);
		lhand = set.getInteger("lhand", 0);
		rateHp = set.getDouble("baseHpRate");
		jClass = set.getString("texture", null);
		_htmRoot = set.getString("htm_root", null);
		shots = set.getEnum("shots", ShotsType.class, ShotsType.NONE);
		_castleId = set.getInteger("castle_id", 0);
		_AIParams = (StatsSet) set.getObject("aiParams", StatsSet.EMPTY);
		setType(set.getString("type", null));
		setAI(set.getString("ai_type", null));
		displayHpBar = set.getBool("displayHpBar", true);
	}
	
	/**
	 * Method getInstanceClass.
	 * @return Class<? extends NpcInstance>
	 */
	public Class<? extends NpcInstance> getInstanceClass()
	{
		return _classType;
	}
	
	/**
	 * Method getInstanceConstructor.
	 * @return Constructor<? extends NpcInstance>
	 */
	public Constructor<? extends NpcInstance> getInstanceConstructor()
	{
		return _constructorType;
	}
	
	/**
	 * Method isInstanceOf.
	 * @param _class Class<?>
	 * @return boolean
	 */
	public boolean isInstanceOf(Class<?> _class)
	{
		return _class.isAssignableFrom(_classType);
	}
	
	/**
	 * Method getNewInstance.
	 * @return NpcInstance
	 */
	public NpcInstance getNewInstance()
	{
		try
		{
			return _constructorType.newInstance(IdFactory.getInstance().getNextId(), this);
		}
		catch (Exception e)
		{
			_log.error("Unable to create instance of NPC " + npcId, e);
		}
		return null;
	}
	
	/**
	 * Method getNewAI.
	 * @param npc NpcInstance
	 * @return CharacterAI
	 */
	public CharacterAI getNewAI(NpcInstance npc)
	{
		try
		{
			return _constructorAI.newInstance(npc);
		}
		catch (Exception e)
		{
			_log.error("Unable to create ai of NPC " + npcId, e);
		}
		return new CharacterAI(npc);
	}
	
	/**
	 * Method setType.
	 * @param type String
	 */
	@SuppressWarnings("unchecked")
	private void setType(String type)
	{
		Class<NpcInstance> classType = null;
		try
		{
			classType = (Class<NpcInstance>) Class.forName("lineage2.gameserver.model.instances." + type + "Instance");
		}
		catch (ClassNotFoundException e)
		{
			classType = (Class<NpcInstance>) Scripts.getInstance().getClasses().get("npc.model." + type + "Instance");
		}
		if (classType == null)
		{
			_log.error("Not found type class for type: " + type + ". NpcId: " + npcId);
		}
		else
		{
			_classType = classType;
			_constructorType = (Constructor<NpcInstance>) _classType.getConstructors()[0];
		}
		if (_classType.isAnnotationPresent(Deprecated.class))
		{
			_log.error("Npc type: " + type + ", is deprecated. NpcId: " + npcId);
		}
		isRaid = isInstanceOf(RaidBossInstance.class) && !isInstanceOf(ReflectionBossInstance.class);
	}
	
	/**
	 * Method setAI.
	 * @param ai String
	 */
	@SuppressWarnings("unchecked")
	private void setAI(String ai)
	{
		Class<CharacterAI> classAI = null;
		try
		{
			classAI = (Class<CharacterAI>) Class.forName("lineage2.gameserver.ai." + ai);
		}
		catch (ClassNotFoundException e)
		{
			classAI = (Class<CharacterAI>) Scripts.getInstance().getClasses().get("ai." + ai);
		}
		if (classAI == null)
		{
			_log.error("Not found ai class for ai: " + ai + ". NpcId: " + npcId);
		}
		else
		{
			_classAI = classAI;
			_constructorAI = (Constructor<CharacterAI>) _classAI.getConstructors()[0];
		}
		if (_classAI.isAnnotationPresent(Deprecated.class))
		{
			_log.error("Ai type: " + ai + ", is deprecated. NpcId: " + npcId);
		}
	}
	
	/**
	 * Method addTeachInfo.
	 * @param classId ClassId
	 */
	public void addTeachInfo(ClassId classId)
	{
		if (_teachInfo.isEmpty())
		{
			_teachInfo = new ArrayList<>(1);
		}
		_teachInfo.add(classId);
	}
	
	/**
	 * Method getTeachInfo.
	 * @return List<ClassId>
	 */
	public List<ClassId> getTeachInfo()
	{
		return _teachInfo;
	}
	
	/**
	 * Method canTeach.
	 * @param classId ClassId
	 * @return boolean
	 */
	public boolean canTeach(ClassId classId)
	{
		return _teachInfo.contains(classId);
	}
	
	/**
	 * Method addTeleportList.
	 * @param id int
	 * @param list TeleportLocation[]
	 */
	public void addTeleportList(int id, TeleportLocation[] list)
	{
		if (_teleportList.isEmpty())
		{
			_teleportList = new TIntObjectHashMap<>(1);
		}
		_teleportList.put(id, list);
	}
	
	/**
	 * Method getTeleportList.
	 * @param id int
	 * @return TeleportLocation[]
	 */
	public TeleportLocation[] getTeleportList(int id)
	{
		return _teleportList.get(id);
	}
	
	/**
	 * Method getTeleportList.
	 * @return TIntObjectHashMap<TeleportLocation[]>
	 */
	public TIntObjectHashMap<TeleportLocation[]> getTeleportList()
	{
		return _teleportList;
	}
	
	/**
	 * Method putRewardList.
	 * @param rewardType RewardType
	 * @param list RewardList
	 */
	public void putRewardList(RewardType rewardType, RewardList list)
	{
		if (_rewards.isEmpty())
		{
			_rewards = new HashMap<>(RewardType.values().length);
		}
		_rewards.put(rewardType, list);
	}
	
	/**
	 * Method getRewardList.
	 * @param t RewardType
	 * @return RewardList
	 */
	public RewardList getRewardList(RewardType t)
	{
		return _rewards.get(t);
	}
	
	/**
	 * Method getRewards.
	 * @return Map<RewardType,RewardList>
	 */
	public Map<RewardType, RewardList> getRewards()
	{
		return _rewards;
	}
	
	/**
	 * Method addAbsorbInfo.
	 * @param absorbInfo AbsorbInfo
	 */
	public void addAbsorbInfo(AbsorbInfo absorbInfo)
	{
		if (_absorbInfo.isEmpty())
		{
			_absorbInfo = new ArrayList<>(1);
		}
		_absorbInfo.add(absorbInfo);
	}
	
	/**
	 * Method addMinion.
	 * @param minion MinionData
	 */
	public void addMinion(MinionData minion)
	{
		if (_minions.isEmpty())
		{
			_minions = new ArrayList<>(1);
		}
		_minions.add(minion);
	}
	
	/**
	 * Method setFaction.
	 * @param faction Faction
	 */
	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}
	
	/**
	 * Method getFaction.
	 * @return Faction
	 */
	public Faction getFaction()
	{
		return faction;
	}
	
	/**
	 * Method addSkill.
	 * @param skill Skill
	 */
	public void addSkill(Skill skill)
	{
		if (_skills.isEmpty())
		{
			_skills = new TIntObjectHashMap<>();
		}
		_skills.put(skill.getId(), skill);
		if (skill.isNotUsedByAI() || (skill.getTargetType() == Skill.SkillTargetType.TARGET_NONE) || (skill.getSkillType() == Skill.SkillType.NOTDONE) || !skill.isActive())
		{
			return;
		}
		switch (skill.getSkillType())
		{
			case PDAM:
			case MANADAM:
			case MDAM:
			case DRAIN:
			case DRAIN_SOUL:
			{
				boolean added = false;
				if (skill.hasEffects())
				{
					for (EffectTemplate eff : skill.getEffectTemplates())
					{
						switch (eff.getEffectType())
						{
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
					}
				}
				if (!added)
				{
					_damageSkills = ArrayUtils.add(_damageSkills, skill);
				}
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
	
	/**
	 * Method getDamageSkills.
	 * @return Skill[]
	 */
	public Skill[] getDamageSkills()
	{
		return _damageSkills;
	}
	
	/**
	 * Method getDotSkills.
	 * @return Skill[]
	 */
	public Skill[] getDotSkills()
	{
		return _dotSkills;
	}
	
	/**
	 * Method getDebuffSkills.
	 * @return Skill[]
	 */
	public Skill[] getDebuffSkills()
	{
		return _debuffSkills;
	}
	
	/**
	 * Method getBuffSkills.
	 * @return Skill[]
	 */
	public Skill[] getBuffSkills()
	{
		return _buffSkills;
	}
	
	/**
	 * Method getStunSkills.
	 * @return Skill[]
	 */
	public Skill[] getStunSkills()
	{
		return _stunSkills;
	}
	
	/**
	 * Method getHealSkills.
	 * @return Skill[]
	 */
	public Skill[] getHealSkills()
	{
		return _healSkills;
	}
	
	/**
	 * Method getMinionData.
	 * @return List<MinionData>
	 */
	public List<MinionData> getMinionData()
	{
		return _minions;
	}
	
	/**
	 * Method getSkills.
	 * @return TIntObjectHashMap<Skill>
	 */
	public TIntObjectHashMap<Skill> getSkills()
	{
		return _skills;
	}
	
	/**
	 * Method addQuestEvent.
	 * @param EventType QuestEventType
	 * @param q Quest
	 */
	public void addQuestEvent(QuestEventType EventType, Quest q)
	{
		if (_questEvents.isEmpty())
		{
			_questEvents = new HashMap<>();
		}
		if (_questEvents.get(EventType) == null)
		{
			_questEvents.put(EventType, new Quest[]
			{
				q
			});
		}
		else
		{
			Quest[] _quests = _questEvents.get(EventType);
			int len = _quests.length;
			Quest[] tmp = new Quest[len + 1];
			for (int i = 0; i < len; i++)
			{
				if (_quests[i].getName().equals(q.getName()))
				{
					_quests[i] = q;
					return;
				}
				tmp[i] = _quests[i];
			}
			tmp[len] = q;
			_questEvents.put(EventType, tmp);
		}
	}
	
	/**
	 * Method getEventQuests.
	 * @param EventType QuestEventType
	 * @return Quest[]
	 */
	public Quest[] getEventQuests(QuestEventType EventType)
	{
		return _questEvents.get(EventType);
	}
	
	/**
	 * Method getRace.
	 * @return int
	 */
	public int getRace()
	{
		return race;
	}
	
	/**
	 * Method setRace.
	 * @param newrace int
	 */
	public void setRace(int newrace)
	{
		race = newrace;
	}
	
	/**
	 * Method isUndead.
	 * @return boolean
	 */
	public boolean isUndead()
	{
		return race == 1;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "Npc template " + name + "[" + npcId + "]";
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	@Override
	public int getNpcId()
	{
		return npcId;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Method getJClass.
	 * @return String
	 */
	public final String getJClass()
	{
		return jClass;
	}
	
	/**
	 * Method getAIParams.
	 * @return StatsSet
	 */
	public final StatsSet getAIParams()
	{
		return _AIParams;
	}
	
	/**
	 * Method getAbsorbInfo.
	 * @return List<AbsorbInfo>
	 */
	public List<AbsorbInfo> getAbsorbInfo()
	{
		return _absorbInfo;
	}
	
	/**
	 * Method getCastleId.
	 * @return int
	 */
	public int getCastleId()
	{
		return _castleId;
	}
	
	/**
	 * Method getQuestEvents.
	 * @return Map<QuestEventType,Quest[]>
	 */
	public Map<QuestEventType, Quest[]> getQuestEvents()
	{
		return _questEvents;
	}
	
	/**
	 * Method getHtmRoot.
	 * @return String
	 */
	public String getHtmRoot()
	{
		return _htmRoot;
	}
	
	/**
	 * Method getSummonPoints.
	 * @return int
	 */
	public int getSummonPoints()
	{
		return summonPoints;
	}
	
	/**
	 * Method setSummonPoints.
	 * @param count int
	 */
	public void setSummonPoints(int count)
	{
		summonPoints = count;
	}

	public void setRandomActions(RandomActions randomActions)
	{
		_randomActions = randomActions;
	}

	public RandomActions getRandomActions()
	{
		return _randomActions;
	}

	public void addWalkerRoute(WalkerRoute walkerRoute)
	{
		if (!walkerRoute.isValid())
		{
			return;
		}
		_walkerRoute.put(walkerRoute.getId(), walkerRoute);
	}

	public WalkerRoute getWalkerRoute(int id)
	{
		return this._walkerRoute.get(id);
	}

	public boolean isDisplayHpBar()
	{
		return displayHpBar;
	}
}

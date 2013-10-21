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
package lineage2.gameserver.model;

import static lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode.ZONE_ALTERED_FLAG;
import static lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode.ZONE_PEACE_FLAG;
import static lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode.ZONE_PVP_FLAG;
import static lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode.ZONE_SIEGE_FLAG;
import static lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode.ZONE_SSQ_FLAG;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.PlayableAI.nextAction;
import lineage2.gameserver.ai.PlayerAI;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.AccountBonusDAO;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.dao.CharacterGroupReuseDAO;
import lineage2.gameserver.dao.CharacterPostFriendDAO;
import lineage2.gameserver.dao.CharacterSubclassDAO;
import lineage2.gameserver.dao.EffectsDAO;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.data.xml.holder.HennaHolder;
import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.data.xml.holder.PlayerTemplateHolder;
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.handler.items.IItemHandler;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.instancemanager.BypassManager;
import lineage2.gameserver.instancemanager.BypassManager.BypassType;
import lineage2.gameserver.instancemanager.BypassManager.DecodedBypass;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager.ArenaParticipantsHolder;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import lineage2.gameserver.listener.actor.player.impl.ScriptAnswerListener;
import lineage2.gameserver.listener.actor.player.impl.SummonAnswerListener;
import lineage2.gameserver.model.GameObjectTasks.EndSitDownTask;
import lineage2.gameserver.model.GameObjectTasks.EndStandUpTask;
import lineage2.gameserver.model.GameObjectTasks.HourlyTask;
import lineage2.gameserver.model.GameObjectTasks.KickTask;
import lineage2.gameserver.model.GameObjectTasks.PvPFlagTask;
import lineage2.gameserver.model.GameObjectTasks.RecomBonusTask;
import lineage2.gameserver.model.GameObjectTasks.UnJailTask;
import lineage2.gameserver.model.GameObjectTasks.WaterTask;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.Skill.AddedSkill;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.instances.player.Bonus;
import lineage2.gameserver.model.actor.instances.player.BookMarkList;
import lineage2.gameserver.model.actor.instances.player.FriendList;
import lineage2.gameserver.model.actor.instances.player.Macro;
import lineage2.gameserver.model.actor.instances.player.MacroList;
import lineage2.gameserver.model.actor.instances.player.MenteeMentor;
import lineage2.gameserver.model.actor.instances.player.MenteeMentorList;
import lineage2.gameserver.model.actor.instances.player.RecomBonus;
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.model.actor.instances.player.ShortCutList;
import lineage2.gameserver.model.actor.instances.player.SubClassList;
import lineage2.gameserver.model.actor.instances.player.SummonList;
import lineage2.gameserver.model.actor.listener.PlayerListenerList;
import lineage2.gameserver.model.actor.recorder.PlayerStatsChangeRecorder;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.BaseStats;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.InvisibleType;
import lineage2.gameserver.model.base.PlayerAccess;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.olympiad.CompType;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.olympiad.OlympiadGame;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DecoyInstance;
import lineage2.gameserver.model.instances.GuardInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.instances.ReflectionBossInstance;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.model.instances.TamedBeastInstance;
import lineage2.gameserver.model.instances.TrapInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemContainer;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.LockType;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.model.items.PcFreight;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.PcRefund;
import lineage2.gameserver.model.items.PcWarehouse;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.model.items.Warehouse;
import lineage2.gameserver.model.items.Warehouse.WarehouseType;
import lineage2.gameserver.model.items.attachment.FlagItemAttachment;
import lineage2.gameserver.model.items.attachment.PickableAttachment;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.Privilege;
import lineage2.gameserver.model.pledge.RankPrivs;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.AbnormalStatusUpdate;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.CameraMode;
import lineage2.gameserver.network.serverpackets.ChairSit;
import lineage2.gameserver.network.serverpackets.ChangeWaitType;
import lineage2.gameserver.network.serverpackets.CharInfo;
import lineage2.gameserver.network.serverpackets.ConfirmDlg;
import lineage2.gameserver.network.serverpackets.EtcStatusUpdate;
import lineage2.gameserver.network.serverpackets.ExAbnormalStatusUpdateFromTarget;
import lineage2.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.ExBR_AgathionEnergyInfo;
import lineage2.gameserver.network.serverpackets.ExBR_ExtraUserInfo;
import lineage2.gameserver.network.serverpackets.ExBasicActionList;
import lineage2.gameserver.network.serverpackets.ExMentorList;
import lineage2.gameserver.network.serverpackets.ExNewSkillToLearnByLevelUp;
import lineage2.gameserver.network.serverpackets.ExOlympiadMatchEnd;
import lineage2.gameserver.network.serverpackets.ExOlympiadMode;
import lineage2.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import lineage2.gameserver.network.serverpackets.ExPCCafePointInfo;
import lineage2.gameserver.network.serverpackets.ExQuestItemList;
import lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.ExStorageMaxCount;
import lineage2.gameserver.network.serverpackets.ExSubjobInfo;
import lineage2.gameserver.network.serverpackets.ExUseSharedGroupItem;
import lineage2.gameserver.network.serverpackets.ExVitalityPointInfo;
import lineage2.gameserver.network.serverpackets.ExVoteSystemInfo;
import lineage2.gameserver.network.serverpackets.GetItem;
import lineage2.gameserver.network.serverpackets.HennaInfo;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.ItemList;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.LeaveWorld;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.MyTargetSelected;
import lineage2.gameserver.network.serverpackets.NpcInfoPoly;
import lineage2.gameserver.network.serverpackets.ObserverEnd;
import lineage2.gameserver.network.serverpackets.ObserverStart;
import lineage2.gameserver.network.serverpackets.PartySmallWindowUpdate;
import lineage2.gameserver.network.serverpackets.PartySpelled;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListDeleteAll;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import lineage2.gameserver.network.serverpackets.PrivateStoreListBuy;
import lineage2.gameserver.network.serverpackets.PrivateStoreListSell;
import lineage2.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import lineage2.gameserver.network.serverpackets.PrivateStoreMsgSell;
import lineage2.gameserver.network.serverpackets.QuestList;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.network.serverpackets.RecipeShopMsg;
import lineage2.gameserver.network.serverpackets.RecipeShopSellList;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.Ride;
import lineage2.gameserver.network.serverpackets.SendTradeDone;
import lineage2.gameserver.network.serverpackets.ServerClose;
import lineage2.gameserver.network.serverpackets.SetupGauge;
import lineage2.gameserver.network.serverpackets.ShortBuffStatusUpdate;
import lineage2.gameserver.network.serverpackets.ShortCutInit;
import lineage2.gameserver.network.serverpackets.ShortCutRegister;
import lineage2.gameserver.network.serverpackets.SkillCoolTime;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SpawnEmitter;
import lineage2.gameserver.network.serverpackets.SpecialCamera;
import lineage2.gameserver.network.serverpackets.StatusUpdate;
import lineage2.gameserver.network.serverpackets.StatusUpdate.StatusUpdateField;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.TargetSelected;
import lineage2.gameserver.network.serverpackets.TargetUnselected;
import lineage2.gameserver.network.serverpackets.TeleportToLocation;
import lineage2.gameserver.network.serverpackets.UserInfo;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.TimeStamp;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.skills.skillclasses.Charge;
import lineage2.gameserver.skills.skillclasses.Transformation;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.LifeParamTable;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.taskmanager.AutoSaveManager;
import lineage2.gameserver.taskmanager.LazyPrecisionTaskManager;
import lineage2.gameserver.templates.FishTemplate;
import lineage2.gameserver.templates.Henna;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ArmorTemplate.ArmorType;
import lineage2.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.RecipeTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.templates.jump.JumpTrack;
import lineage2.gameserver.templates.jump.JumpWay;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.player.PlayerTemplate;
import lineage2.gameserver.utils.AntiFlood;
import lineage2.gameserver.utils.EffectsComparator;
import lineage2.gameserver.utils.GameStats;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Language;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.Mentoring;
import lineage2.gameserver.utils.SiegeUtils;
import lineage2.gameserver.utils.SqlBatch;
import lineage2.gameserver.utils.Strings;
import lineage2.gameserver.utils.TeleportUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Player extends Playable implements PlayerGroup
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field DEFAULT_TITLE_COLOR.
	 */
	public static final int DEFAULT_TITLE_COLOR = 0xFFFF77;
	/**
	 * Field MAX_POST_FRIEND_SIZE. (value is 100)
	 */
	public static final int MAX_POST_FRIEND_SIZE = 100;
	/**
	 * Field MAX_FRIEND_SIZE. (value is 128)
	 */
	public static final int MAX_FRIEND_SIZE = 128;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Player.class);
	/**
	 * Field NO_TRADERS_VAR. (value is ""notraders"")
	 */
	public static final String NO_TRADERS_VAR = "notraders";
	/**
	 * Field NO_ANIMATION_OF_CAST_VAR. (value is ""notShowBuffAnim"")
	 */
	public static final String NO_ANIMATION_OF_CAST_VAR = "notShowBuffAnim";
	/**
	 * Field MY_BIRTHDAY_RECEIVE_YEAR. (value is ""MyBirthdayReceiveYear"")
	 */
	public static final String MY_BIRTHDAY_RECEIVE_YEAR = "MyBirthdayReceiveYear";
	/**
	 * Field NOT_CONNECTED. (value is ""<not connected>"")
	 */
	private static final String NOT_CONNECTED = "<not connected>";
	/**
	 * Field _classlist.
	 */
	public Map<Integer, SubClass> _classlist = new HashMap<Integer, SubClass>(4);
	/**
	 * Field OBSERVER_NONE. (value is 0)
	 */
	public final static int OBSERVER_NONE = 0;
	/**
	 * Field OBSERVER_STARTING. (value is 1)
	 */
	public final static int OBSERVER_STARTING = 1;
	/**
	 * Field OBSERVER_STARTED. (value is 3)
	 */
	public final static int OBSERVER_STARTED = 3;
	/**
	 * Field OBSERVER_LEAVING. (value is 2)
	 */
	public final static int OBSERVER_LEAVING = 2;
	/**
	 * Field STORE_PRIVATE_NONE. (value is 0)
	 */
	public static final int STORE_PRIVATE_NONE = 0;
	/**
	 * Field STORE_PRIVATE_SELL. (value is 1)
	 */
	public static final int STORE_PRIVATE_SELL = 1;
	/**
	 * Field STORE_PRIVATE_BUY. (value is 3)
	 */
	public static final int STORE_PRIVATE_BUY = 3;
	/**
	 * Field STORE_PRIVATE_MANUFACTURE. (value is 5)
	 */
	public static final int STORE_PRIVATE_MANUFACTURE = 5;
	/**
	 * Field STORE_OBSERVING_GAMES. (value is 7)
	 */
	public static final int STORE_OBSERVING_GAMES = 7;
	/**
	 * Field STORE_PRIVATE_SELL_PACKAGE. (value is 8)
	 */
	public static final int STORE_PRIVATE_SELL_PACKAGE = 8;
	/**
	 * Field RANK_VAGABOND. (value is 0)
	 */
	public static final int RANK_VAGABOND = 0;
	/**
	 * Field RANK_VASSAL. (value is 1)
	 */
	public static final int RANK_VASSAL = 1;
	/**
	 * Field RANK_HEIR. (value is 2)
	 */
	public static final int RANK_HEIR = 2;
	/**
	 * Field RANK_KNIGHT. (value is 3)
	 */
	public static final int RANK_KNIGHT = 3;
	/**
	 * Field RANK_WISEMAN. (value is 4)
	 */
	public static final int RANK_WISEMAN = 4;
	/**
	 * Field RANK_BARON. (value is 5)
	 */
	public static final int RANK_BARON = 5;
	/**
	 * Field RANK_VISCOUNT. (value is 6)
	 */
	public static final int RANK_VISCOUNT = 6;
	/**
	 * Field RANK_COUNT. (value is 7)
	 */
	public static final int RANK_COUNT = 7;
	/**
	 * Field RANK_MARQUIS. (value is 8)
	 */
	public static final int RANK_MARQUIS = 8;
	/**
	 * Field RANK_DUKE. (value is 9)
	 */
	public static final int RANK_DUKE = 9;
	/**
	 * Field RANK_GRAND_DUKE. (value is 10)
	 */
	public static final int RANK_GRAND_DUKE = 10;
	/**
	 * Field RANK_DISTINGUISHED_KING. (value is 11)
	 */
	public static final int RANK_DISTINGUISHED_KING = 11;
	/**
	 * Field RANK_EMPEROR. (value is 12)
	 */
	public static final int RANK_EMPEROR = 12;
	/**
	 * Field LANG_ENG. (value is 0)
	 */
	public static final int LANG_ENG = 0;
	/**
	 * Field LANG_RUS. (value is 1)
	 */
	public static final int LANG_RUS = 1;
	/**
	 * Field LANG_UNK. (value is -1)
	 */
	public static final int LANG_UNK = -1;
	/**
	 * Field EXPERTISE_LEVELS.
	 */
	public static final int[] EXPERTISE_LEVELS =
	{
		0,
		20,
		40,
		52,
		61,
		76,
		80,
		84,
		85,
		95,
		99,
		Integer.MAX_VALUE
	};
	/**
	 * Field _connection.
	 */
	private GameClient _connection;
	/**
	 * Field _login.
	 */
	private String _login;
	/**
	 * Field _pvpKills. Field _pkKills. Field _karma.
	 */
	private int _karma, _pkKills, _pvpKills;
	/**
	 * Field _hairColor. Field _hairStyle. Field _face.
	 */
	private int _face, _hairStyle, _hairColor;
	/**
	 * Field _fame. Field _recomLeftToday. Field _recomHave.
	 */
	private int _recomHave, _recomLeftToday, _fame;
	/**
	 * Field _recomLeft.
	 */
	private int _recomLeft = 20;
	/**
	 * Field _recomBonusTime.
	 */
	private int _recomBonusTime = 3600;
	/**
	 * Field _isRecomTimerActive. Field _isHourglassEffected.
	 */
	private boolean _isHourglassEffected, _isRecomTimerActive;
	/**
	 * Field _isUndying.
	 */
	private boolean _isUndying = false;
	/**
	 * Field _deleteTimer.
	 */
	private int _deleteTimer;
	private long _startingTimeInFullParty = 0L;
	private long _startingTimeInParty = 0L;
	/**
	 * Field _ping.
	 */
	private int _ping = -1;
	/**
	 * Field _NoChannelBegin. Field _NoChannel. Field _deleteClanTime. Field _leaveClanTime. Field _onlineBeginTime. Field _onlineTime. Field _createTime.
	 */
	private long _createTime, _onlineTime, _onlineBeginTime, _leaveClanTime, _deleteClanTime, _NoChannel, _NoChannelBegin;
	/**
	 * Field _uptime.
	 */
	private long _uptime;
	/**
	 * Field _lastAccess.
	 */
	private long _lastAccess;
	/**
	 * Field _titlecolor. Field _nameColor.
	 */
	private int _nameColor, _titlecolor;
	/**
	 * Field _vitality.
	 */
	private int _vitality;
	/**
	 * Field _overloaded.
	 */
	private boolean _overloaded;
	/**
	 * Field _identItem.
	 */
	public boolean _identItem = false;
	/**
	 * Field sittingTaskLaunched.
	 */
	boolean sittingTaskLaunched;
	/**
	 * Field _waitTimeWhenSit.
	 */
	private int _waitTimeWhenSit;
	/**
	 * Field AutoLootHerbs. Field _autoLoot.
	 */
	private boolean _autoLoot = Config.AUTO_LOOT, AutoLootHerbs = Config.AUTO_LOOT_HERBS;
	/**
	 * Field _inventory.
	 */
	private final PcInventory _inventory = new PcInventory(this);
	/**
	 * Field _warehouse.
	 */
	private final Warehouse _warehouse = new PcWarehouse(this);
	/**
	 * Field _refund.
	 */
	private final ItemContainer _refund = new PcRefund(this);
	/**
	 * Field _freight.
	 */
	private final PcFreight _freight = new PcFreight(this);
	/**
	 * Field bookmarks.
	 */
	public final BookMarkList bookmarks = new BookMarkList(this, 0);
	/**
	 * Field antiFlood.
	 */
	public final AntiFlood antiFlood = new AntiFlood();
	/**
	 * Field _recipebook.
	 */
	private final Map<Integer, RecipeTemplate> _recipebook = new TreeMap<Integer, RecipeTemplate>();
	/**
	 * Field _commonrecipebook.
	 */
	private final Map<Integer, RecipeTemplate> _commonrecipebook = new TreeMap<Integer, RecipeTemplate>();
	/**
	 * Field _premiumItems.
	 */
	private final Map<Integer, PremiumItem> _premiumItems = new TreeMap<Integer, PremiumItem>();
	/**
	 * Field _quests.
	 */
	private final Map<String, QuestState> _quests = new HashMap<String, QuestState>();
	/**
	 * Field _shortCuts.
	 */
	private final ShortCutList _shortCuts = new ShortCutList(this);
	/**
	 * Field _macroses.
	 */
	private final MacroList _macroses = new MacroList(this);
	/**
	 * Field _privatestore.
	 */
	private int _privatestore;
	/**
	 * Field _manufactureName.
	 */
	private String _manufactureName;
	/**
	 * Field _createList.
	 */
	private List<ManufactureItem> _createList = Collections.emptyList();
	/**
	 * Field _sellStoreName.
	 */
	private String _sellStoreName;
	/**
	 * Field _sellList.
	 */
	private List<TradeItem> _sellList = Collections.emptyList();
	/**
	 * Field _packageSellList.
	 */
	private List<TradeItem> _packageSellList = Collections.emptyList();
	/**
	 * Field _buyStoreName.
	 */
	private String _buyStoreName;
	/**
	 * Field _buyList.
	 */
	private List<TradeItem> _buyList = Collections.emptyList();
	/**
	 * Field _tradeList.
	 */
	private List<TradeItem> _tradeList = Collections.emptyList();
	/**
	 * Field _henna.
	 */
	private final Henna[] _henna = new Henna[3];
	/**
	 * Field _hennaCON. Field _hennaWIT. Field _hennaMEN. Field _hennaDEX. Field _hennaINT. Field _hennaSTR.
	 */
	private int _hennaSTR, _hennaINT, _hennaDEX, _hennaMEN, _hennaWIT, _hennaCON;
	/**
	 * Field _party.
	 */
	private Party _party;
	/**
	 * Field _lastPartyPosition.
	 */
	private Location _lastPartyPosition;
	/**
	 * Field _clan.
	 */
	private Clan _clan;
	/**
	 * Field _apprentice. Field _lvlJoinedAcademy. Field _powerGrade. Field _pledgeType. Field _pledgeClass.
	 */
	private int _pledgeClass = 0, _pledgeType = Clan.SUBUNIT_NONE, _powerGrade = 0, _lvlJoinedAcademy = 0, _apprentice = 0;
	/**
	 * Field _accessLevel.
	 */
	private int _accessLevel;
	/**
	 * Field _playerAccess.
	 */
	private PlayerAccess _playerAccess = new PlayerAccess();
	/**
	 * Field _blockAll. Field _tradeRefusal. Field _messageRefusal.
	 */
	private boolean _messageRefusal = false, _tradeRefusal = false, _blockAll = false;
	/**
	 * Field _riding.
	 */
	private boolean _riding;
	/**
	 * Field _decoy.
	 */
	private DecoyInstance _decoy = null;
	/**
	 * Field _cubics.
	 */
	private Map<Integer, EffectCubic> _cubics = null;
	/**
	 * Field _agathionId.
	 */
	private int _agathionId = 0;
	/**
	 * Field _request.
	 */
	private Request _request;
	/**
	 * Field _arrowItem.
	 */
	private ItemInstance _arrowItem;
	/**
	 * Field _fistsWeaponItem.
	 */
	private WeaponTemplate _fistsWeaponItem;
	/**
	 * Field _chars.
	 */
	private Map<Integer, String> _chars = new HashMap<Integer, String>(8);
	/**
	 * Field expertiseIndex.
	 */
	public int expertiseIndex = 0;
	/**
	 * Field _enchantScroll.
	 */
	private ItemInstance _enchantScroll = null;
	/**
	 * Field _usingWHType.
	 */
	private WarehouseType _usingWHType;
	/**
	 * Field _isOnline.
	 */
	private boolean _isOnline = false;
	/**
	 * Field _isLogout.
	 */
	private final AtomicBoolean _isLogout = new AtomicBoolean();
	/**
	 * Field _lastNpc.
	 */
	private HardReference<NpcInstance> _lastNpc = HardReferences.emptyRef();
	/**
	 * Field _multisell.
	 */
	private MultiSellListContainer _multisell = null;
	/**
	 * Field _activeSoulShots.
	 */
	private final Set<Integer> _activeSoulShots = new CopyOnWriteArraySet<Integer>();
	/**
	 * Field _observerRegion.
	 */
	private WorldRegion _observerRegion;
	/**
	 * Field _observerMode.
	 */
	private final AtomicInteger _observerMode = new AtomicInteger(0);
	/**
	 * Field _telemode.
	 */
	public int _telemode = 0;
	/**
	 * Field _handysBlockCheckerEventArena.
	 */
	private int _handysBlockCheckerEventArena = -1;
	/**
	 * Field entering.
	 */
	public boolean entering = true;
	/**
	 * Field _stablePoint.
	 */
	public Location _stablePoint = null;
	/**
	 * Field _loto.
	 */
	public int _loto[] = new int[5];
	/**
	 * Field _race.
	 */
	public int _race[] = new int[2];
	/**
	 * Field _blockList.
	 */
	private final Map<Integer, String> _blockList = new ConcurrentSkipListMap<Integer, String>();
	/**
	 * Field _friendList.
	 */
	private final FriendList _friendList = new FriendList(this);
	/**
	 * Field _menteeList.
	 */
	private final MenteeMentorList _menteeMentorList = new MenteeMentorList(this);
	/**
	 * Field _hero.
	 */
	private boolean _hero = false;
	/**
	 * Field _boat.
	 */
	private Boat _boat;
	/**
	 * Field _inBoatPosition.
	 */
	private Location _inBoatPosition;
	/**
	 * Field _bonus.
	 */
	private final Bonus _bonus = new Bonus();
	/**
	 * Field _bonusExpiration.
	 */
	private Future<?> _bonusExpiration;
	/**
	 * Field _isSitting.
	 */
	private boolean _isSitting;
	/**
	 * Field _sittingObject.
	 */
	private StaticObjectInstance _sittingObject;
	/**
	 * Field _noble.
	 */
	private boolean _noble = false;
	/**
	 * Field _inOlympiadMode.
	 */
	private boolean _inOlympiadMode;
	/**
	 * Field _olympiadGame.
	 */
	private OlympiadGame _olympiadGame;
	/**
	 * Field _olympiadObserveGame.
	 */
	private OlympiadGame _olympiadObserveGame;
	/**
	 * Field _olympiadSide.
	 */
	private int _olympiadSide = -1;

	private ItemInstance _enchantItem;
	private ItemInstance _enchantSupportItem;
	/**
	 * Field _varka.
	 */
	private int _varka = 0;
	/**
	 * Field _ketra.
	 */
	private int _ketra = 0;
	/**
	 * Field _ram.
	 */
	private int _ram = 0;
	/**
	 * Field _keyBindings.
	 */
	private byte[] _keyBindings = ArrayUtils.EMPTY_BYTE_ARRAY;
	/**
	 * Field _cursedWeaponEquippedId.
	 */
	private int _cursedWeaponEquippedId = 0;
	/**
	 * Field _fishing.
	 */
	private final Fishing _fishing = new Fishing(this);
	/**
	 * Field _isFishing.
	 */
	private boolean _isFishing;
	/**
	 * Field _taskWater.
	 */
	private Future<?> _taskWater;
	/**
	 * Field _autoSaveTask.
	 */
	private Future<?> _autoSaveTask;
	/**
	 * Field _kickTask.
	 */
	private Future<?> _kickTask;
	/**
	 * Field _pcCafePointsTask.
	 */
	private Future<?> _pcCafePointsTask;
	/**
	 * Field _unjailTask.
	 */
	private Future<?> _unjailTask;
	/**
	 * Field _storeLock.
	 */
	private final Lock _storeLock = new ReentrantLock();
	/**
	 * Field _subClassOperationLock.
	 */
	private final Lock _subClassOperationLock = new ReentrantLock();
	/**
	 * Field _zoneMask.
	 */
	private int _zoneMask;
	/**
	 * Field _offline.
	 */
	private boolean _offline = false;
	/**
	 * Field _pcBangPoints.
	 */
	private int _pcBangPoints;
	/**
	 * Field _transformationSkills.
	 */
	Map<Integer, Skill> _transformationSkills = new HashMap<Integer, Skill>();
	/**
	 * Field _expandInventory.
	 */
	private int _expandInventory = 0;
	/**
	 * Field _expandWarehouse.
	 */
	private int _expandWarehouse = 0;
	/**
	 * Field _battlefieldChatId.
	 */
	private int _battlefieldChatId;
	/**
	 * Field _lectureMark.
	 */
	private int _lectureMark;
	/**
	 * Field _invisibleType.
	 */
	private InvisibleType _invisibleType = InvisibleType.NONE;
	/**
	 * Field bypasses_bbs. Field bypasses.
	 */
	private List<String> bypasses = null, bypasses_bbs = null;
	/**
	 * Field _postFriends.
	 */
	private IntObjectMap<String> _postFriends = Containers.emptyIntObjectMap();
	/**
	 * Field _blockedActions.
	 */
	private final List<String> _blockedActions = new ArrayList<String>();
	/**
	 * Field _notShowBuffAnim.
	 */
	private boolean _notShowBuffAnim = false;
	/**
	 * Field _notShowTraders.
	 */
	private boolean _notShowTraders = false;
	/**
	 * Field _debug.
	 */
	private boolean _debug = false;
	/**
	 * Field _dropDisabled.
	 */
	private long _dropDisabled;
	/**
	 * Field _lastItemAuctionInfoRequest.
	 */
	private long _lastItemAuctionInfoRequest;
	/**
	 * Field _sharedGroupReuses.
	 */
	private final IntObjectMap<TimeStamp> _sharedGroupReuses = new CHashIntObjectMap<TimeStamp>();
	/**
	 * Field _askDialog.
	 */
	private Pair<Integer, OnAnswerListener> _askDialog = null;
	/**
	 * Field _matchingRoom.
	 */
	private MatchingRoom _matchingRoom;
	/**
	 * Field _petitionGroup.
	 */
	private PetitionMainGroup _petitionGroup;
	/**
	 * Field _instancesReuses.
	 */
	private final Map<Integer, Long> _instancesReuses = new ConcurrentHashMap<Integer, Long>();
	/**
	 * Field _currentJumpTrack.
	 */
	private JumpTrack _currentJumpTrack = null;
	/**
	 * Field _currentJumpWay.
	 */
	private JumpWay _currentJumpWay = null;
	/**
	 * Field _summons.
	 */
	private ConcurrentHashMap<Integer, Summon> _summons = new ConcurrentHashMap<Integer, Summon>(4);
	/**
	 * Field _tree.
	 */
	private boolean _tree;
	/**
	 * Field _ServitorShareRestore.
	 */
	private boolean _ServitorShareRestore = false;
	
	private Effect _ServitorShareRestoreData = null;
	/**
	 * Field _collision_radius.
	 */
	private final double _collision_radius;
	/**
	 * Field _collision_height.
	 */
	private final double _collision_height;
	/**
	 * Field _subClassList.
	 */
	private final SubClassList _subClassList = new SubClassList(this);
	/**
	 * Field _summonList.
	 */
	private final SummonList _summonList = new SummonList(this);
	
	/**
	 * Constructor for Player.
	 * @param objectId int
	 * @param template PlayerTemplate
	 * @param accountName String
	 */
	public Player(final int objectId, final PlayerTemplate template, final String accountName)
	{
		super(objectId, template);
		_login = accountName;
		_collision_radius = template.getCollisionRadius();
		_collision_height = template.getCollisionHeight();
		_nameColor = 0xFFFFFF;
		_titlecolor = 0xFFFF77;
	}
	
	/**
	 * Constructor for Player.
	 * @param objectId int
	 * @param template PlayerTemplate
	 */
	private Player(final int objectId, final PlayerTemplate template)
	{
		this(objectId, template, null);
		_ai = new PlayerAI(this);
		if (!Config.EVERYBODY_HAS_ADMIN_RIGHTS)
		{
			setPlayerAccess(Config.gmlist.get(objectId));
		}
		else
		{
			setPlayerAccess(Config.gmlist.get(0));
		}
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<Player>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HardReference<Player> getRef()
	{
		return (HardReference<Player>) super.getRef();
	}
	
	/**
	 * Method getAccountName.
	 * @return String
	 */
	public String getAccountName()
	{
		if (_connection == null)
		{
			return _login;
		}
		return _connection.getLogin();
	}
	
	/**
	 * Method getIP.
	 * @return String
	 */
	public String getIP()
	{
		if (_connection == null)
		{
			return NOT_CONNECTED;
		}
		return _connection.getIpAddr();
	}
	
	/**
	 * Method getAccountChars.
	 * @return Map<Integer,String>
	 */
	public Map<Integer, String> getAccountChars()
	{
		return _chars;
	}
	
	/**
	 * Method getTemplate.
	 * @return PlayerTemplate
	 */
	@Override
	public final PlayerTemplate getTemplate()
	{
		return (PlayerTemplate) _template;
	}
	
	/**
	 * Method changeSex.
	 */
	public void changeSex()
	{
		_template = PlayerTemplateHolder.getInstance().getPlayerTemplate(getRace(), getClassId(), Sex.VALUES[getSex()].revert());
	}
	
	/**
	 * Method getAI.
	 * @return PlayerAI
	 */
	@Override
	public PlayerAI getAI()
	{
		return (PlayerAI) _ai;
	}
	
	/**
	 * Method doCast.
	 * @param skill Skill
	 * @param target Creature
	 * @param forceUse boolean
	 */
	@Override
	public void doCast(final Skill skill, final Creature target, boolean forceUse)
	{
		if (skill == null)
		{
			return;
		}
		super.doCast(skill, target, forceUse);
	}
	
	/**
	 * Method sendReuseMessage.
	 * @param skill Skill
	 */
	@Override
	public void sendReuseMessage(Skill skill)
	{
		if (isCastingNow())
		{
			return;
		}
		TimeStamp sts = getSkillReuse(skill);
		if ((sts == null) || !sts.hasNotPassed())
		{
			return;
		}
		long timeleft = sts.getReuseCurrent();
		if ((!Config.ALT_SHOW_REUSE_MSG && (timeleft < 10000)) || (timeleft < 500))
		{
			return;
		}
		long hours = timeleft / 3600000;
		long minutes = (timeleft - (hours * 3600000)) / 60000;
		long seconds = (long) Math.ceil((timeleft - (hours * 3600000) - (minutes * 60000)) / 1000.);
		if (hours > 0)
		{
			sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S2_HOURS_S3_MINUTES_AND_S4_SECONDS_REMAINING_IN_S1S_REUSE_TIME).addSkillName(skill.getId(), skill.getDisplayLevel()).addNumber(hours).addNumber(minutes).addNumber(seconds));
		}
		else if (minutes > 0)
		{
			sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_IN_S1S_REUSE_TIME).addSkillName(skill.getId(), skill.getDisplayLevel()).addNumber(minutes).addNumber(seconds));
		}
		else
		{
			sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S2_SECONDS_REMAINING_IN_S1S_REUSE_TIME).addSkillName(skill.getId(), skill.getDisplayLevel()).addNumber(seconds));
		}
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	@Override
	public final int getLevel()
	{
		return getActiveSubClass() == null ? 1 : getActiveSubClass().getLevel();
	}
	
	/**
	 * Method getSex.
	 * @return int
	 */
	public int getSex()
	{
		return getTemplate().getSex().ordinal();
	}
	
	/**
	 * Method getFace.
	 * @return int
	 */
	public int getFace()
	{
		return _face;
	}
	
	/**
	 * Method setFace.
	 * @param face int
	 */
	public void setFace(int face)
	{
		_face = face;
	}
	
	/**
	 * Method getHairColor.
	 * @return int
	 */
	public int getHairColor()
	{
		return _hairColor;
	}
	
	/**
	 * Method setHairColor.
	 * @param hairColor int
	 */
	public void setHairColor(int hairColor)
	{
		_hairColor = hairColor;
	}
	
	/**
	 * Method getHairStyle.
	 * @return int
	 */
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	/**
	 * Method setHairStyle.
	 * @param hairStyle int
	 */
	public void setHairStyle(int hairStyle)
	{
		_hairStyle = hairStyle;
	}
	
	/**
	 * Method offline.
	 */
	public void offline()
	{
		if (_connection != null)
		{
			_connection.setActiveChar(null);
			_connection.close(ServerClose.STATIC);
			setNetConnection(null);
		}
		setNameColor(Config.SERVICES_OFFLINE_TRADE_NAME_COLOR);
		setOfflineMode(true);
		setVar("offline", String.valueOf(System.currentTimeMillis() / 1000L), -1);
		if (Config.SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK > 0)
		{
			startKickTask(Config.SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK * 1000L);
		}
		Party party = getParty();
		if (party != null)
		{
			leaveParty();
		}
		getSummonList().unsummonAll(false);
		CursedWeaponsManager.getInstance().doLogout(this);
		if (isInOlympiadMode())
		{
			Olympiad.logoutPlayer(this);
		}
		broadcastCharInfo();
		stopWaterTask();
		stopBonusTask();
		stopHourlyTask();
		stopPcBangPointsTask();
		stopAutoSaveTask();
		stopRecomBonusTask(true);
		stopQuestTimers();
		try
		{
			getInventory().store();
		}
		catch (Throwable t)
		{
			_log.error("", t);
		}
		try
		{
			store(false);
		}
		catch (Throwable t)
		{
			_log.error("", t);
		}
	}
	
	/**
	 * Method kick.
	 */
	public void kick()
	{
		if (_connection != null)
		{
			_connection.close(LeaveWorld.STATIC);
			setNetConnection(null);
		}
		prepareToLogout();
		deleteMe();
	}
	
	/**
	 * Method restart.
	 */
	public void restart()
	{
		if (_connection != null)
		{
			_connection.setActiveChar(null);
			setNetConnection(null);
		}
		prepareToLogout();
		deleteMe();
	}
	
	/**
	 * Method logout.
	 */
	public void logout()
	{
		if (_connection != null)
		{
			_connection.close(ServerClose.STATIC);
			setNetConnection(null);
		}
		prepareToLogout();
		deleteMe();
	}
	
	/**
	 * Method prepareToLogout.
	 */
	void prepareToLogout()
	{
		if (_isLogout.getAndSet(true))
		{
			return;
		}
		setNetConnection(null);
		setIsOnline(false);
		getListeners().onExit();
		if (isFlying() && !checkLandingState())
		{
			_stablePoint = TeleportUtils.getRestartLocation(this, RestartType.TO_VILLAGE);
		}
		if (isCastingNow())
		{
			abortCast(true, true);
		}
		Party party = getParty();
		if (party != null)
		{
			leaveParty();
		}
		CursedWeaponsManager.getInstance().doLogout(this);
		if (_olympiadObserveGame != null)
		{
			_olympiadObserveGame.removeSpectator(this);
		}
		if (isInOlympiadMode())
		{
			Olympiad.logoutPlayer(this);
		}
		stopFishing();
		if (_stablePoint != null)
		{
			teleToLocation(_stablePoint);
		}
		_summonList.store(true);
		_summonList.unsummonAll(true);
		_friendList.notifyFriends(false);
		mentoringLogoutConditions();
		if (isProcessingRequest())
		{
			getRequest().cancel();
		}
		stopAllTimers();
		if (isInBoat())
		{
			getBoat().removePlayer(this);
		}
		SubUnit unit = getSubUnit();
		UnitMember member = unit == null ? null : unit.getUnitMember(getObjectId());
		if (member != null)
		{
			int sponsor = member.getSponsor();
			int apprentice = getApprentice();
			PledgeShowMemberListUpdate memberUpdate = new PledgeShowMemberListUpdate(this);
			for (Player clanMember : _clan.getOnlineMembers(getObjectId()))
			{
				clanMember.sendPacket(memberUpdate);
				if (clanMember.getObjectId() == sponsor)
				{
					clanMember.sendPacket(new SystemMessage(SystemMessage.S1_YOUR_CLAN_ACADEMYS_APPRENTICE_HAS_LOGGED_OUT).addString(_name));
				}
				else if (clanMember.getObjectId() == apprentice)
				{
					clanMember.sendPacket(new SystemMessage(SystemMessage.S1_YOUR_CLAN_ACADEMYS_SPONSOR_HAS_LOGGED_OUT).addString(_name));
				}
			}
			member.setPlayerInstance(this, true);
		}
		FlagItemAttachment attachment = getActiveWeaponFlagAttachment();
		if (attachment != null)
		{
			attachment.onLogout(this);
		}
		if (CursedWeaponsManager.getInstance().getCursedWeapon(getCursedWeaponEquippedId()) != null)
		{
			CursedWeaponsManager.getInstance().getCursedWeapon(getCursedWeaponEquippedId()).setPlayer(null);
		}
		MatchingRoom room = getMatchingRoom();
		if (room != null)
		{
			if (room.getLeader() == this)
			{
				room.disband();
			}
			else
			{
				room.removeMember(this, false);
			}
		}
		setMatchingRoom(null);
		MatchingRoomManager.getInstance().removeFromWaitingList(this);
		destroyAllTraps();
		if (_decoy != null)
		{
			_decoy.unSummon();
			_decoy = null;
		}
		stopPvPFlag();
		Reflection ref = getReflection();
		if (ref != ReflectionManager.DEFAULT)
		{
			if (ref.getReturnLoc() != null)
			{
				_stablePoint = ref.getReturnLoc();
			}
			ref.removeObject(this);
		}
		try
		{
			getInventory().store();
			getRefund().clear();
		}
		catch (Throwable t)
		{
			_log.error("", t);
		}
		try
		{
			store(false);
		}
		catch (Throwable t)
		{
			_log.error("", t);
		}
	}
	
	/**
	 * Method getDwarvenRecipeBook.
	 * @return Collection<RecipeTemplate>
	 */
	public Collection<RecipeTemplate> getDwarvenRecipeBook()
	{
		return _recipebook.values();
	}
	
	/**
	 * Method getCommonRecipeBook.
	 * @return Collection<RecipeTemplate>
	 */
	public Collection<RecipeTemplate> getCommonRecipeBook()
	{
		return _commonrecipebook.values();
	}
	
	/**
	 * Method recipesCount.
	 * @return int
	 */
	public int recipesCount()
	{
		return _commonrecipebook.size() + _recipebook.size();
	}
	
	/**
	 * Method hasRecipe.
	 * @param id RecipeTemplate
	 * @return boolean
	 */
	public boolean hasRecipe(final RecipeTemplate id)
	{
		return _recipebook.containsValue(id) || _commonrecipebook.containsValue(id);
	}
	
	/**
	 * Method findRecipe.
	 * @param id int
	 * @return boolean
	 */
	public boolean findRecipe(final int id)
	{
		return _recipebook.containsKey(id) || _commonrecipebook.containsKey(id);
	}
	
	/**
	 * Method registerRecipe.
	 * @param recipe RecipeTemplate
	 * @param saveDB boolean
	 */
	public void registerRecipe(final RecipeTemplate recipe, boolean saveDB)
	{
		if (recipe == null)
		{
			return;
		}
		if (recipe.isDwarven())
		{
			_recipebook.put(recipe.getId(), recipe);
		}
		else
		{
			_commonrecipebook.put(recipe.getId(), recipe);
		}
		if (saveDB)
		{
			mysql.set("REPLACE INTO character_recipebook (char_id, id) VALUES(?,?)", getObjectId(), recipe.getId());
		}
	}
	
	/**
	 * Method unregisterRecipe.
	 * @param RecipeID int
	 */
	public void unregisterRecipe(final int RecipeID)
	{
		if (_recipebook.containsKey(RecipeID))
		{
			mysql.set("DELETE FROM `character_recipebook` WHERE `char_id`=? AND `id`=? LIMIT 1", getObjectId(), RecipeID);
			_recipebook.remove(RecipeID);
		}
		else if (_commonrecipebook.containsKey(RecipeID))
		{
			mysql.set("DELETE FROM `character_recipebook` WHERE `char_id`=? AND `id`=? LIMIT 1", getObjectId(), RecipeID);
			_commonrecipebook.remove(RecipeID);
		}
		else
		{
			_log.warn("Attempted to remove unknown RecipeList" + RecipeID);
		}
	}
	
	/**
	 * Method getQuestState.
	 * @param quest String
	 * @return QuestState
	 */
	public QuestState getQuestState(String quest)
	{
		questRead.lock();
		try
		{
			return _quests.get(quest);
		}
		finally
		{
			questRead.unlock();
		}
	}
	
	/**
	 * Method getQuestState.
	 * @param quest Class<?>
	 * @return QuestState
	 */
	public QuestState getQuestState(Class<?> quest)
	{
		return getQuestState(quest.getSimpleName());
	}
	
	/**
	 * Method isQuestCompleted.
	 * @param quest String
	 * @return boolean
	 */
	public boolean isQuestCompleted(String quest)
	{
		QuestState q = getQuestState(quest);
		return (q != null) && q.isCompleted();
	}
	
	/**
	 * Method isQuestCompleted.
	 * @param quest Class<?>
	 * @return boolean
	 */
	public boolean isQuestCompleted(Class<?> quest)
	{
		QuestState q = getQuestState(quest);
		return (q != null) && q.isCompleted();
	}
	
	/**
	 * Method setQuestState.
	 * @param qs QuestState
	 */
	public void setQuestState(QuestState qs)
	{
		questWrite.lock();
		try
		{
			_quests.put(qs.getQuest().getName(), qs);
		}
		finally
		{
			questWrite.unlock();
		}
	}
	
	/**
	 * Method removeQuestState.
	 * @param quest String
	 */
	public void removeQuestState(String quest)
	{
		questWrite.lock();
		try
		{
			_quests.remove(quest);
		}
		finally
		{
			questWrite.unlock();
		}
	}
	
	/**
	 * Method getAllActiveQuests.
	 * @return Quest[]
	 */
	public Quest[] getAllActiveQuests()
	{
		List<Quest> quests = new ArrayList<Quest>(_quests.size());
		questRead.lock();
		try
		{
			for (final QuestState qs : _quests.values())
			{
				if (qs.isStarted())
				{
					quests.add(qs.getQuest());
				}
			}
		}
		finally
		{
			questRead.unlock();
		}
		return quests.toArray(new Quest[quests.size()]);
	}
	
	/**
	 * Method getAllQuestsStates.
	 * @return QuestState[]
	 */
	public QuestState[] getAllQuestsStates()
	{
		questRead.lock();
		try
		{
			return _quests.values().toArray(new QuestState[_quests.size()]);
		}
		finally
		{
			questRead.unlock();
		}
	}
	
	/**
	 * Method getQuestsForEvent.
	 * @param npc NpcInstance
	 * @param event QuestEventType
	 * @param forNpcQuestList boolean
	 * @return List<QuestState>
	 */
	public List<QuestState> getQuestsForEvent(NpcInstance npc, QuestEventType event, boolean forNpcQuestList)
	{
		List<QuestState> states = new ArrayList<QuestState>();
		Quest[] quests = npc.getTemplate().getEventQuests(event);
		QuestState qs;
		if (quests != null)
		{
			for (Quest quest : quests)
			{
				qs = getQuestState(quest.getName());
				if (forNpcQuestList || ((qs != null) && !qs.isCompleted()))
				{
					if (forNpcQuestList && (qs == null))
					{
						qs = quest.newQuestStateAndNotSave(this, Quest.CREATED);
					}
					states.add(qs);
				}
			}
		}
		return states;
	}
	
	/**
	 * Method processQuestEvent.
	 * @param quest String
	 * @param event String
	 * @param npc NpcInstance
	 */
	public void processQuestEvent(String quest, String event, NpcInstance npc)
	{
		if (event == null)
		{
			event = "";
		}
		QuestState qs = getQuestState(quest);
		if (qs == null)
		{
			Quest q = QuestManager.getQuest(quest);
			if (q == null)
			{
				_log.warn("Quest " + quest + " not found!");
				return;
			}
			qs = q.newQuestState(this, Quest.CREATED);
		}
		if ((qs == null) || qs.isCompleted())
		{
			return;
		}
		qs.getQuest().notifyEvent(event, qs, npc);
		sendPacket(new QuestList(this));
	}
	
	/**
	 * Method isQuestContinuationPossible.
	 * @param msg boolean
	 * @return boolean
	 */
	public boolean isQuestContinuationPossible(boolean msg)
	{
		if ((getWeightPenalty() >= 3) || ((getInventoryLimit() * 0.9) < getInventory().getSize()) || ((Config.QUEST_INVENTORY_MAXIMUM * 0.9) < getInventory().getQuestSize()))
		{
			if (msg)
			{
				sendPacket(Msg.PROGRESS_IN_A_QUEST_IS_POSSIBLE_ONLY_WHEN_YOUR_INVENTORYS_WEIGHT_AND_VOLUME_ARE_LESS_THAN_80_PERCENT_OF_CAPACITY);
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Method stopQuestTimers.
	 */
	public void stopQuestTimers()
	{
		for (QuestState qs : getAllQuestsStates())
		{
			if (qs.isStarted())
			{
				qs.pauseQuestTimers();
			}
			else
			{
				qs.stopQuestTimers();
			}
		}
	}
	
	/**
	 * Method resumeQuestTimers.
	 */
	public void resumeQuestTimers()
	{
		for (QuestState qs : getAllQuestsStates())
		{
			qs.resumeQuestTimers();
		}
	}
	
	/**
	 * Method getAllShortCuts.
	 * @return Collection<ShortCut>
	 */
	public Collection<ShortCut> getAllShortCuts()
	{
		return _shortCuts.getAllShortCuts();
	}
	
	/**
	 * Method getShortCut.
	 * @param slot int
	 * @param page int
	 * @return ShortCut
	 */
	public ShortCut getShortCut(int slot, int page)
	{
		return _shortCuts.getShortCut(slot, page);
	}
	
	/**
	 * Method registerShortCut.
	 * @param shortcut ShortCut
	 */
	public void registerShortCut(ShortCut shortcut)
	{
		_shortCuts.registerShortCut(shortcut);
	}
	
	/**
	 * Method deleteShortCut.
	 * @param slot int
	 * @param page int
	 */
	public void deleteShortCut(int slot, int page)
	{
		_shortCuts.deleteShortCut(slot, page);
	}
	
	/**
	 * Method registerMacro.
	 * @param macro Macro
	 */
	public void registerMacro(Macro macro)
	{
		_macroses.registerMacro(macro);
	}
	
	/**
	 * Method deleteMacro.
	 * @param id int
	 */
	public void deleteMacro(int id)
	{
		_macroses.deleteMacro(id);
	}
	
	/**
	 * Method getMacroses.
	 * @return MacroList
	 */
	public MacroList getMacroses()
	{
		return _macroses;
	}
	
	/**
	 * Method isCastleLord.
	 * @param castleId int
	 * @return boolean
	 */
	public boolean isCastleLord(int castleId)
	{
		return (_clan != null) && isClanLeader() && (_clan.getCastle() == castleId);
	}
	
	/**
	 * Method isFortressLord.
	 * @param fortressId int
	 * @return boolean
	 */
	public boolean isFortressLord(int fortressId)
	{
		return (_clan != null) && isClanLeader() && (_clan.getHasFortress() == fortressId);
	}
	
	/**
	 * Method getPkKills.
	 * @return int
	 */
	public int getPkKills()
	{
		return _pkKills;
	}
	
	/**
	 * Method setPkKills.
	 * @param pkKills int
	 */
	public void setPkKills(final int pkKills)
	{
		_pkKills = pkKills;
	}
	
	/**
	 * Method getCreateTime.
	 * @return long
	 */
	public long getCreateTime()
	{
		return _createTime;
	}
	
	/**
	 * Method setCreateTime.
	 * @param createTime long
	 */
	public void setCreateTime(final long createTime)
	{
		_createTime = createTime;
	}
	
	/**
	 * Method getDeleteTimer.
	 * @return int
	 */
	public int getDeleteTimer()
	{
		return _deleteTimer;
	}
	
	/**
	 * Method setDeleteTimer.
	 * @param deleteTimer int
	 */
	public void setDeleteTimer(final int deleteTimer)
	{
		_deleteTimer = deleteTimer;
	}
	
	/**
	 * Method getCurrentLoad.
	 * @return int
	 */
	public int getCurrentLoad()
	{
		return getInventory().getTotalWeight();
	}
	
	/**
	 * Method getLastAccess.
	 * @return long
	 */
	public long getLastAccess()
	{
		return _lastAccess;
	}
	
	/**
	 * Method setLastAccess.
	 * @param value long
	 */
	public void setLastAccess(long value)
	{
		_lastAccess = value;
	}
	
	/**
	 * Method getRecomHave.
	 * @return int
	 */
	public int getRecomHave()
	{
		return _recomHave;
	}
	
	/**
	 * Method setRecomHave.
	 * @param value int
	 */
	public void setRecomHave(int value)
	{
		if (value > 255)
		{
			_recomHave = 255;
		}
		else if (value < 0)
		{
			_recomHave = 0;
		}
		else
		{
			_recomHave = value;
		}
	}
	
	/**
	 * Method getRecomBonusTime.
	 * @return int
	 */
	public int getRecomBonusTime()
	{
		if (_recomBonusTask != null)
		{
			return (int) Math.max(0, _recomBonusTask.getDelay(TimeUnit.SECONDS));
		}
		return _recomBonusTime;
	}
	
	/**
	 * Method setRecomBonusTime.
	 * @param val int
	 */
	public void setRecomBonusTime(int val)
	{
		_recomBonusTime = val;
	}
	
	/**
	 * Method getRecomLeft.
	 * @return int
	 */
	public int getRecomLeft()
	{
		return _recomLeft;
	}
	
	/**
	 * Method setRecomLeft.
	 * @param value int
	 */
	public void setRecomLeft(final int value)
	{
		_recomLeft = value;
	}
	
	/**
	 * Method isHourglassEffected.
	 * @return boolean
	 */
	public boolean isHourglassEffected()
	{
		return _isHourglassEffected;
	}
	
	/**
	 * Method setHourlassEffected.
	 * @param val boolean
	 */
	public void setHourlassEffected(boolean val)
	{
		_isHourglassEffected = val;
	}
	
	/**
	 * Method startHourglassEffect.
	 */
	public void startHourglassEffect()
	{
		setHourlassEffected(true);
		stopRecomBonusTask(true);
		sendVoteSystemInfo();
	}
	
	/**
	 * Method stopHourglassEffect.
	 */
	public void stopHourglassEffect()
	{
		setHourlassEffected(false);
		startRecomBonusTask();
		sendVoteSystemInfo();
	}
	
	/**
	 * Method addRecomLeft.
	 * @return int
	 */
	public int addRecomLeft()
	{
		int recoms = 0;
		if (getRecomLeftToday() < 20)
		{
			recoms = 10;
		}
		else
		{
			recoms = 1;
		}
		setRecomLeft(getRecomLeft() + recoms);
		setRecomLeftToday(getRecomLeftToday() + recoms);
		sendUserInfo();
		return recoms;
	}
	
	/**
	 * Method getRecomLeftToday.
	 * @return int
	 */
	public int getRecomLeftToday()
	{
		return _recomLeftToday;
	}
	
	/**
	 * Method setRecomLeftToday.
	 * @param value int
	 */
	public void setRecomLeftToday(final int value)
	{
		_recomLeftToday = value;
		setVar("recLeftToday", String.valueOf(_recomLeftToday), -1);
	}
	
	/**
	 * Method giveRecom.
	 * @param target Player
	 */
	public void giveRecom(final Player target)
	{
		int targetRecom = target.getRecomHave();
		if (targetRecom < 255)
		{
			target.addRecomHave(1);
		}
		if (getRecomLeft() > 0)
		{
			setRecomLeft(getRecomLeft() - 1);
		}
		sendUserInfo();
	}
	
	/**
	 * Method addRecomHave.
	 * @param val int
	 */
	public void addRecomHave(final int val)
	{
		setRecomHave(getRecomHave() + val);
		broadcastUserInfo();
		sendVoteSystemInfo();
	}
	
	/**
	 * Method getRecomBonus.
	 * @return int
	 */
	public int getRecomBonus()
	{
		if ((getRecomBonusTime() > 0) || isHourglassEffected())
		{
			return RecomBonus.getRecoBonus(this);
		}
		return 0;
	}
	
	/**
	 * Method getRecomBonusMul.
	 * @return double
	 */
	public double getRecomBonusMul()
	{
		if ((getRecomBonusTime() > 0) || isHourglassEffected())
		{
			return RecomBonus.getRecoMultiplier(this);
		}
		return 1;
	}
	
	/**
	 * Method sendVoteSystemInfo.
	 */
	public void sendVoteSystemInfo()
	{
		sendPacket(new ExVoteSystemInfo(this));
	}
	
	/**
	 * Method isRecomTimerActive.
	 * @return boolean
	 */
	public boolean isRecomTimerActive()
	{
		return _isRecomTimerActive;
	}
	
	/**
	 * Method setRecomTimerActive.
	 * @param val boolean
	 */
	public void setRecomTimerActive(boolean val)
	{
		if (_isRecomTimerActive == val)
		{
			return;
		}
		_isRecomTimerActive = val;
		if (val)
		{
			startRecomBonusTask();
		}
		else
		{
			stopRecomBonusTask(true);
		}
		sendVoteSystemInfo();
	}
	
	/**
	 * Field _recomBonusTask.
	 */
	private ScheduledFuture<?> _recomBonusTask;
	
	/**
	 * Method startRecomBonusTask.
	 */
	public void startRecomBonusTask()
	{
		if ((_recomBonusTask == null) && (getRecomBonusTime() > 0) && isRecomTimerActive() && !isHourglassEffected())
		{
			_recomBonusTask = ThreadPoolManager.getInstance().schedule(new RecomBonusTask(this), getRecomBonusTime() * 1000);
		}
	}
	
	/**
	 * Method stopRecomBonusTask.
	 * @param saveTime boolean
	 */
	public void stopRecomBonusTask(boolean saveTime)
	{
		if (_recomBonusTask != null)
		{
			if (saveTime)
			{
				setRecomBonusTime((int) Math.max(0, _recomBonusTask.getDelay(TimeUnit.SECONDS)));
			}
			_recomBonusTask.cancel(false);
			_recomBonusTask = null;
		}
	}
	
	/**
	 * Method getKarma.
	 * @return int
	 */
	@Override
	public int getKarma()
	{
		return _karma;
	}
	
	/**
	 * Method setKarma.
	 * @param karma int
	 */
	public void setKarma(int karma)
	{
		if (_karma == karma)
		{
			return;
		}
		_karma = karma;
		sendChanges();
		for (Summon summon : getSummonList())
		{
			summon.broadcastCharInfo();
		}
	}
	
	/**
	 * Method getMaxLoad.
	 * @return int
	 */
	@Override
	public int getMaxLoad()
	{
		int con = getCON();
		if (con < 1)
		{
			return (int) (31000 * Config.MAXLOAD_MODIFIER);
		}
		else if (con > 59)
		{
			return (int) (176000 * Config.MAXLOAD_MODIFIER);
		}
		else
		{
			return (int) calcStat(Stats.MAX_LOAD, Math.pow(1.029993928, con) * 30495.627366 * Config.MAXLOAD_MODIFIER, this, null);
		}
	}
	
	/**
	 * Field _updateEffectIconsTask.
	 */
	Future<?> _updateEffectIconsTask;
	
	/**
	 * Method restoreVitality.
	 */
	public void restoreVitality()
	{
		Calendar temp = Calendar.getInstance();
		temp.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		temp.set(Calendar.HOUR_OF_DAY, 6);
		temp.set(Calendar.MINUTE, 30);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		if ((_lastAccess < (temp.getTimeInMillis() / 1000)) && (System.currentTimeMillis() > temp.getTimeInMillis()))
		{
			setVitality(Config.MAX_VITALITY);
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `points` FROM `vitality_points` WHERE `account_name`=?");
			statement.setString(1, getAccountName());
			rset = statement.executeQuery();
			if (rset.next())
			{
				setVitality(rset.getInt(1));
			}
			else
			{
				setVitality(Config.MAX_VITALITY);
				DbUtils.closeQuietly(con, statement);
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("INSERT IGNORE INTO `vitality_points` (account_name, points) VALUES (?,?)");
				statement.setString(1, getAccountName());
				statement.setInt(2, Config.MAX_VITALITY);
				statement.execute();
			}
		}
		catch (SQLException e)
		{
			_log.error("Could not restore vitality points", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method sendSkillList.
	 */
	public void sendSkillList()
	{
		sendPacket(new SkillList(this));
		sendPacket(new ExAcquirableSkillListByClass(this));
	}
	
	@Override
	public void onInteract(Player player)
	{
		if(!isInStoreMode())
		{
			super.onInteract(player);
		}
		else
		{
			switch(getPrivateStoreType())
			{
				case STORE_PRIVATE_SELL:
				case STORE_PRIVATE_SELL_PACKAGE:
					player.sendPacket(new PrivateStoreListSell(player, this));
					break;
				case STORE_PRIVATE_BUY:
					player.sendPacket(new PrivateStoreListBuy(player, this));
					break;
				case STORE_PRIVATE_MANUFACTURE:
					player.sendPacket(new RecipeShopSellList(player, this));
					break;
			}
		}
	}
	
	/**
	 * Method getSummonList.
	 * @return SummonList
	 */
	public SummonList getSummonList()
	{
		return _summonList;
	}
	
	/**
	 * Method updateEffectIcons.
	 */
	@Override
	public void updateEffectIcons()
	{
		super.updateEffectIcons();
		if (entering || isLogoutStarted())
		{
			return;
		}
		updateEffectIconsImpl();
		return;
	}
	
	/**
	 * Method updateEffectIconsImpl.
	 */
	public void updateEffectIconsImpl()
	{
		Effect[] effects = getEffectList().getAllFirstEffects();
		Arrays.sort(effects, EffectsComparator.getInstance());
		PartySpelled ps = new PartySpelled(this, false);
		AbnormalStatusUpdate mi = new AbnormalStatusUpdate();
		for (Effect effect : effects)
		{
			if (effect.isInUse())
			{
				if (effect.getStackType().contains(EffectTemplate.HP_RECOVER_CAST))
				{
					sendPacket(new ShortBuffStatusUpdate(effect));
				}
				else
				{
					effect.addIcon(mi);
				}
				if (_party != null)
				{
					effect.addIcon(ps);
				}
			}
		}
		sendPacket(mi);
		if (_party != null)
		{
			_party.broadCast(ps);
		}
		if (isInOlympiadMode() && isOlympiadCompStart())
		{
			OlympiadGame olymp_game = _olympiadGame;
			if (olymp_game != null)
			{
				ExOlympiadSpelledInfo olympiadSpelledInfo = new ExOlympiadSpelledInfo();
				for (Effect effect : effects)
				{
					if ((effect != null) && effect.isInUse())
					{
						effect.addOlympiadSpelledIcon(this, olympiadSpelledInfo);
					}
				}
				if ((olymp_game.getType() == CompType.CLASSED) || (olymp_game.getType() == CompType.NON_CLASSED))
				{
					for (Player member : olymp_game.getTeamMembers(this))
					{
						member.sendPacket(olympiadSpelledInfo);
					}
				}
				for (Player member : olymp_game.getSpectators())
				{
					member.sendPacket(olympiadSpelledInfo);
				}
			}
		}
	}
	
	/**
	 * Method getWeightPenalty.
	 * @return int
	 */
	public int getWeightPenalty()
	{
		return getSkillLevel(4270, 0);
	}
	
	/**
	 * Method refreshOverloaded.
	 */
	public void refreshOverloaded()
	{
		if (isLogoutStarted() || (getMaxLoad() <= 0))
		{
			return;
		}
		setOverloaded(getCurrentLoad() > getMaxLoad());
		double weightproc = (100. * (getCurrentLoad() - calcStat(Stats.MAX_NO_PENALTY_LOAD, 0, this, null))) / getMaxLoad();
		int newWeightPenalty = 0;
		if (weightproc < 50)
		{
			newWeightPenalty = 0;
		}
		else if (weightproc < 66.6)
		{
			newWeightPenalty = 1;
		}
		else if (weightproc < 80)
		{
			newWeightPenalty = 2;
		}
		else if (weightproc < 100)
		{
			newWeightPenalty = 3;
		}
		else
		{
			newWeightPenalty = 4;
		}
		int current = getWeightPenalty();
		if (current == newWeightPenalty)
		{
			return;
		}
		if (newWeightPenalty > 0)
		{
			super.addSkill(SkillTable.getInstance().getInfo(4270, newWeightPenalty));
		}
		else
		{
			super.removeSkill(getKnownSkill(4270));
		}
		sendSkillList();
		sendEtcStatusUpdate();
		updateStats();
	}
	
	/**
	 * Method getArmorsExpertisePenalty.
	 * @return int
	 */
	public int getArmorsExpertisePenalty()
	{
		return getSkillLevel(6213, 0);
	}
	
	/**
	 * Method getWeaponsExpertisePenalty.
	 * @return int
	 */
	public int getWeaponsExpertisePenalty()
	{
		return getSkillLevel(6209, 0);
	}
	
	/**
	 * Method getExpertisePenalty.
	 * @param item ItemInstance
	 * @return int
	 */
	public int getExpertisePenalty(ItemInstance item)
	{
		if (item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON)
		{
			return getWeaponsExpertisePenalty();
		}
		else if ((item.getTemplate().getType2() == ItemTemplate.TYPE2_SHIELD_ARMOR) || (item.getTemplate().getType2() == ItemTemplate.TYPE2_ACCESSORY))
		{
			return getArmorsExpertisePenalty();
		}
		return 0;
	}
	
	/**
	 * Method refreshExpertisePenalty.
	 */
	public void refreshExpertisePenalty()
	{
		if (isLogoutStarted())
		{
			return;
		}
		int level = (int) calcStat(Stats.GRADE_EXPERTISE_LEVEL, getLevel(), null, null);
		int i;
		for (i = 0; i < EXPERTISE_LEVELS.length; i++)
		{
			if (level < EXPERTISE_LEVELS[i + 1])
			{
				break;
			}
		}
		boolean skillUpdate = false;
		if (expertiseIndex != i)
		{
			expertiseIndex = i;
			if (expertiseIndex > 0)
			{
				addSkill(SkillTable.getInstance().getInfo(239, expertiseIndex), false);
				skillUpdate = true;
			}
		}
		int newWeaponPenalty = 0;
		int newArmorPenalty = 0;
		ItemInstance[] items = getInventory().getPaperdollItems();
		for (ItemInstance item : items)
		{
			if (item != null)
			{
				int crystaltype = item.getTemplate().getCrystalType().ordinal();
				if (item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON)
				{
					if (crystaltype > newWeaponPenalty)
					{
						newWeaponPenalty = crystaltype;
					}
				}
				else if ((item.getTemplate().getType2() == ItemTemplate.TYPE2_SHIELD_ARMOR) || (item.getTemplate().getType2() == ItemTemplate.TYPE2_ACCESSORY))
				{
					if (crystaltype > newArmorPenalty)
					{
						newArmorPenalty = crystaltype;
					}
				}
			}
		}
		newWeaponPenalty = newWeaponPenalty - expertiseIndex;
		if (newWeaponPenalty <= 0)
		{
			newWeaponPenalty = 0;
		}
		else if (newWeaponPenalty >= 4)
		{
			newWeaponPenalty = 4;
		}
		newArmorPenalty = newArmorPenalty - expertiseIndex;
		if (newArmorPenalty <= 0)
		{
			newArmorPenalty = 0;
		}
		else if (newArmorPenalty >= 4)
		{
			newArmorPenalty = 4;
		}
		int weaponExpertise = getWeaponsExpertisePenalty();
		int armorExpertise = getArmorsExpertisePenalty();
		if (weaponExpertise != newWeaponPenalty)
		{
			weaponExpertise = newWeaponPenalty;
			if (newWeaponPenalty > 0)
			{
				super.addSkill(SkillTable.getInstance().getInfo(6209, weaponExpertise));
			}
			else
			{
				super.removeSkill(getKnownSkill(6209));
			}
			skillUpdate = true;
		}
		if (armorExpertise != newArmorPenalty)
		{
			armorExpertise = newArmorPenalty;
			if (newArmorPenalty > 0)
			{
				super.addSkill(SkillTable.getInstance().getInfo(6213, armorExpertise));
			}
			else
			{
				super.removeSkill(getKnownSkill(6213));
			}
			skillUpdate = true;
		}
		if (skillUpdate)
		{
			getInventory().validateItemsSkills();
			sendSkillList();
			sendEtcStatusUpdate();
			updateStats();
		}
	}
	
	/**
	 * Method getPvpKills.
	 * @return int
	 */
	public int getPvpKills()
	{
		return _pvpKills;
	}
	
	/**
	 * Method setPvpKills.
	 * @param pvpKills int
	 */
	public void setPvpKills(int pvpKills)
	{
		_pvpKills = pvpKills;
	}
	
	/**
	 * Method getClassId.
	 * @return ClassId
	 */
	public ClassId getClassId()
	{
		return ClassId.VALUES[getActiveClassId()];
	}
	
	/**
	 * Method changeClass.
	 * @param index int
	 */
	public void changeClass(final int index)
	{
		SystemMsg msg = checkChangeClassCondition();
		if (msg != null)
		{
			sendPacket(msg);
			return;
		}
		SubClass sub = _subClassList.getByIndex(index);
		if (sub == null)
		{
			return;
		}
		abortCast(true, true);
		int classId = sub.getClassId();
		int oldClassId = getActiveClassId();
		setActiveSubClass(classId, true, 0);
		Skill skill = SkillTable.getInstance().getInfo(1570, 1);
		skill.getEffects(this, this, false, false);
		if(isAwaking()) //If the characters returns to Main, or dual Subclass and Delete Skills prof are active, do check of Correct skills
		{
			if(Config.ALT_CHECK_SKILLS_AWAKENING)
			{
				AwakingManager.getInstance().checkAwakenPlayerSkills(this);
			}
		}
		sendPacket(new SystemMessage(SystemMessage.THE_TRANSFER_OF_SUB_CLASS_HAS_BEEN_COMPLETED).addClassName(oldClassId).addClassName(classId));
		abortCast(true, true);
	}
	
	/**
	 * Method checkChangeClassCondition.
	 * @return SystemMsg
	 */
	private SystemMsg checkChangeClassCondition()
	{
		if ((getWeightPenalty() >= 3) || ((getInventoryLimit() * 0.8) < getInventory().getSize()))
		{
			return SystemMsg.A_SUBCLASS_CANNOT_BE_CREATED_OR_CHANGED_BECAUSE_YOU_HAVE_EXCEEDED_YOUR_INVENTORY_LIMIT;
		}
		if (isInOlympiadMode())
		{
			return SystemMsg.THIS_TERRITORY_CAN_NOT_CHANGE_CLASS;
		}
		if (isDead())
		{
			return SystemMsg.YOU_CAN_NOT_CHANGE_CLASS_IN_TRANSFORMATION;
		}
		if (getTransformation() != 0)
		{
			return SystemMsg.YOU_CAN_NOT_CHANGE_CLASS_IN_TRANSFORMATION;
		}
		if (isInDuel())
		{
			return SystemMsg.THIS_TERRITORY_CAN_NOT_CHANGE_CLASS;
		}
		if (isInCombat() || isAttackingNow())
		{
			return SystemMsg.THIS_TERRITORY_CAN_NOT_CHANGE_CLASS;
		}
		return null;
	}
	
	/**
	 * Method getINT.
	 * @return int
	 */
	@Override
	public int getINT()
	{
		return Math.max(getTemplate().getMinAttr().getINT(), Math.min(getTemplate().getMaxAttr().getINT(), super.getINT()));
	}
	
	/**
	 * Method getSTR.
	 * @return int
	 */
	@Override
	public int getSTR()
	{
		return Math.max(getTemplate().getMinAttr().getSTR(), Math.min(getTemplate().getMaxAttr().getSTR(), super.getSTR()));
	}
	
	/**
	 * Method getCON.
	 * @return int
	 */
	@Override
	public int getCON()
	{
		return Math.max(getTemplate().getMinAttr().getCON(), Math.min(getTemplate().getMaxAttr().getCON(), super.getCON()));
	}
	
	/**
	 * Method getMEN.
	 * @return int
	 */
	@Override
	public int getMEN()
	{
		return Math.max(getTemplate().getMinAttr().getMEN(), Math.min(getTemplate().getMaxAttr().getMEN(), super.getMEN()));
	}
	
	/**
	 * Method getDEX.
	 * @return int
	 */
	@Override
	public int getDEX()
	{
		return Math.max(getTemplate().getMinAttr().getDEX(), Math.min(getTemplate().getMaxAttr().getDEX(), super.getDEX()));
	}
	
	/**
	 * Method getWIT.
	 * @return int
	 */
	@Override
	public int getWIT()
	{
		return Math.max(getTemplate().getMinAttr().getWIT(), Math.min(getTemplate().getMaxAttr().getWIT(), super.getWIT()));
	}
	
	/**
	 * Method getDefaultMaxCp.
	 * @return double
	 */
	public double getDefaultMaxCp()
	{
		return LifeParamTable.getCp(this) * BaseStats.CON.calcBonus(this);
	}
	
	/**
	 * Method getDefaultMaxHp.
	 * @return double
	 */
	@Override
	public double getDefaultMaxHp()
	{
		return LifeParamTable.getHp(this) * BaseStats.CON.calcBonus(this);
	}
	
	/**
	 * Method getDefaultMaxMp.
	 * @return double
	 */
	@Override
	public double getDefaultMaxMp()
	{
		return LifeParamTable.getMp(this) * BaseStats.MEN.calcBonus(this);
	}
	
	/**
	 * Method getMaxCp.
	 * @return int
	 */
	@Override
	public int getMaxCp()
	{
		return (int) calcStat(Stats.MAX_CP, getDefaultMaxCp(), null, null);
	}
	
	/**
	 * Method getMaxHp.
	 * @return int
	 */
	@Override
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, getDefaultMaxHp(), null, null);
	}
	
	/**
	 * Method getMaxMp.
	 * @return int
	 */
	@Override
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, getDefaultMaxMp(), null, null);
	}
	
	/**
	 * Method getRandomDamage.
	 * @return int
	 */
	@Override
	public int getRandomDamage()
	{
		WeaponTemplate weaponItem = getActiveWeaponItem();
		if (weaponItem == null)
		{
			return getTemplate().getBaseRandDam();
		}
		return weaponItem.getRandomDamage();
	}
	
	/**
	 * Method getHpRegen.
	 * @return double
	 */
	@Override
	public double getHpRegen()
	{
		return calcStat(Stats.REGENERATE_HP_RATE, getTemplate().getBaseHpReg(getLevel()));
	}
	
	/**
	 * Method getMpRegen.
	 * @return double
	 */
	@Override
	public double getMpRegen()
	{
		return calcStat(Stats.REGENERATE_MP_RATE, getTemplate().getBaseMpReg(getLevel()));
	}
	
	/**
	 * Method getCpRegen.
	 * @return double
	 */
	@Override
	public double getCpRegen()
	{
		return calcStat(Stats.REGENERATE_CP_RATE, getTemplate().getBaseCpReg(getLevel()));
	}
	
	/**
	 * Method useItem.
	 * @param item ItemInstance
	 * @param ctrlPressed boolean
	 * @return boolean
	 */
	public boolean useItem(ItemInstance item, boolean ctrlPressed)
	{
		boolean success = item.getTemplate().getHandler().useItem(this, item, ctrlPressed);
		if (success)
		{
			long nextTimeUse = item.getTemplate().getReuseType().next(item);
			if (nextTimeUse > System.currentTimeMillis())
			{
				TimeStamp timeStamp = new TimeStamp(item.getItemId(), nextTimeUse, item.getTemplate().getReuseDelay());
				addSharedGroupReuse(item.getTemplate().getReuseGroup(), timeStamp);
				if (item.getTemplate().getReuseDelay() > 0)
				{
					this.sendPacket(new ExUseSharedGroupItem(item.getTemplate().getDisplayReuseGroup(), timeStamp));
				}
			}
		}
		return success;
	}
	
	/**
	 * Field partySearchStatusIsOn.
	 */
	boolean partySearchStatusIsOn = false;
	
	/**
	 * Method getPartySearchStatus.
	 * @return boolean
	 */
	public boolean getPartySearchStatus()
	{
		return partySearchStatusIsOn;
	}
	
	/**
	 * Method setPartySearchStatus.
	 * @param partySearchStatus boolean
	 */
	public void setPartySearchStatus(boolean partySearchStatus)
	{
		partySearchStatusIsOn = partySearchStatus;
	}
	
	/**
	 * Field playerForChange.
	 */
	Player playerForChange;
	
	/**
	 * Method getPlayerForChange.
	 * @return Player
	 */
	public Player getPlayerForChange()
	{
		return playerForChange;
	}
	
	/**
	 * Method setPlayerForChange.
	 * @param _playerForChange Player
	 */
	public void setPlayerForChange(Player _playerForChange)
	{
		playerForChange = _playerForChange;
	}
	
	/**
	 * Field _isIgnoringDeath.
	 */
	boolean _isIgnoringDeath = false;
	
	/**
	 * Method setIsIgnoringDeath.
	 * @param isIgnoringDeath boolean
	 */
	public void setIsIgnoringDeath(boolean isIgnoringDeath)
	{
		_isIgnoringDeath = isIgnoringDeath;
	}
	
	/**
	 * Method isIgnoringDeath.
	 * @return boolean
	 */
	public boolean isIgnoringDeath()
	{
		return _isIgnoringDeath;
	}
	
	/**
	 * Method addClanPointsOnProfession.
	 * @param id int
	 */
	public void addClanPointsOnProfession(final int id)
	{
		if ((getLvlJoinedAcademy() != 0) && (_clan != null) && (_clan.getLevel() >= 5) && ClassId.VALUES[id].isOfLevel(ClassLevel.Second))
		{
			int earnedPoints = ((76 - getLvlJoinedAcademy()) * 45) + 200;
			if (earnedPoints > 2000)
			{
				earnedPoints = 2000;
			}
			_clan.removeClanMember(getObjectId());
			SystemMessage sm = new SystemMessage(SystemMessage.CLAN_ACADEMY_MEMBER_S1_HAS_SUCCESSFULLY_COMPLETED_THE_2ND_CLASS_TRANSFER_AND_OBTAINED_S2_CLAN_REPUTATION_POINTS);
			sm.addString(getName());
			sm.addNumber(_clan.incReputation(earnedPoints, true, "Academy"));
			_clan.broadcastToOnlineMembers(sm);
			_clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListDelete(getName()), this);
			setClan(null);
			setTitle("");
			sendPacket(Msg.CONGRATULATIONS_YOU_WILL_NOW_GRADUATE_FROM_THE_CLAN_ACADEMY_AND_LEAVE_YOUR_CURRENT_CLAN_AS_A_GRADUATE_OF_THE_ACADEMY_YOU_CAN_IMMEDIATELY_JOIN_A_CLAN_AS_A_REGULAR_MEMBER_WITHOUT_BEING_SUBJECT_TO_ANY_PENALTIES);
			setLeaveClanTime(0);
			broadcastCharInfo();
			sendPacket(PledgeShowMemberListDeleteAll.STATIC);
		}
	}
	
	/**
	 * Method setClassId.
	 * @param id int
	 * @param noban boolean
	 * @param bity boolean
	 */
	public synchronized void setClassId(final int id, boolean noban, boolean bity)
	{
		ClassId classId = ClassId.VALUES[id];
		if (!noban && !classId.equalsOrChildOf(ClassId.VALUES[getActiveClassId()]) && !getVarB("awakenByStoneOfDestiny",false) && !(getPlayerAccess().CanChangeClass || Config.EVERYBODY_HAS_ADMIN_RIGHTS))
		{
			Thread.dumpStack();
			return;
		}
		ClassId oldClassId = getClassId();
		if (!_subClassList.containsClassId(id))
		{
			final SubClass cclass = getActiveSubClass();
			final int oldClass = cclass.getClassId();
			_subClassList.changeSubClassId(oldClass, id);
			changeClassInDb(oldClass, id, cclass.getDefaultClassId() == 0 ? getSubClassList().getBaseSubClass().getDefaultClassId() : cclass.getDefaultClassId());
			if (cclass.isBase())
			{
				addClanPointsOnProfession(id);
			}
			switch (classId)
			{
				case CARDINAL:
					ItemFunctions.addItem(this, 15307, 7, true);
					break;
				case EVAS_SAINT:
					ItemFunctions.addItem(this, 15308, 7, true);
					break;
				case SHILLIEN_SAINT:
					ItemFunctions.addItem(this, 15309, 7, true);
					break;
				default:
					break;
			}
			rewardSkills(true,false);
			storeCharSubClasses();
			broadcastCharInfo();
		}
		if ((oldClassId == null) || !oldClassId.isOfRace(getClassId().getRace()) || !oldClassId.isOfType(getClassId().getType()))
		{
			PlayerTemplate t = PlayerTemplateHolder.getInstance().getPlayerTemplate(getRace(), classId, Sex.VALUES[getSex()]);
			if (t == null)
			{
				_log.error("Missing template for classId: " + id);
				return;
			}
			_template = t;
		}
		if (isInParty())
		{
			getParty().broadCast(new PartySmallWindowUpdate(this));
		}
		if (getClan() != null)
		{
			getClan().broadcastToOnlineMembers(new PledgeShowMemberListUpdate(this));
		}
		if (_matchingRoom != null)
		{
			_matchingRoom.broadcastPlayerUpdate(this);
		}
		sendPacket(new ExSubjobInfo(this, true));
	}
	
	/**
	 * Method getExp.
	 * @return long
	 */
	public long getExp()
	{
		return getActiveSubClass() == null ? 0 : getActiveSubClass().getExp();
	}
	
	/**
	 * Method getMaxExp.
	 * @return long
	 */
	public long getMaxExp()
	{
		return getActiveSubClass() == null ? Experience.LEVEL[Experience.getMaxLevel() + 1] : getActiveSubClass().getMaxExp();
	}
	
	/**
	 * Method setEnchantScroll.
	 * @param scroll ItemInstance
	 */
	public void setEnchantScroll(final ItemInstance scroll)
	{
		_enchantScroll = scroll;
	}
	
	/**
	 * Method getEnchantScroll.
	 * @return ItemInstance
	 */
	public ItemInstance getEnchantScroll()
	{
		return _enchantScroll;
	}
	
	/**
	 * Method setFistsWeaponItem.
	 * @param weaponItem WeaponTemplate
	 */
	public void setFistsWeaponItem(final WeaponTemplate weaponItem)
	{
		_fistsWeaponItem = weaponItem;
	}
	
	/**
	 * Method getFistsWeaponItem.
	 * @return WeaponTemplate
	 */
	public WeaponTemplate getFistsWeaponItem()
	{
		return _fistsWeaponItem;
	}
	
	/**
	 * Method findFistsWeaponItem.
	 * @param classId int
	 * @return WeaponTemplate
	 */
	public WeaponTemplate findFistsWeaponItem(final int classId)
	{
		if ((classId >= 0x00) && (classId <= 0x09))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(246);
		}
		if ((classId >= 0x0a) && (classId <= 0x11))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(251);
		}
		if ((classId >= 0x12) && (classId <= 0x18))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(244);
		}
		if ((classId >= 0x19) && (classId <= 0x1e))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(249);
		}
		if ((classId >= 0x1f) && (classId <= 0x25))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(245);
		}
		if ((classId >= 0x26) && (classId <= 0x2b))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(250);
		}
		if ((classId >= 0x2c) && (classId <= 0x30))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(248);
		}
		if ((classId >= 0x31) && (classId <= 0x34))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(252);
		}
		if ((classId >= 0x35) && (classId <= 0x39))
		{
			return (WeaponTemplate) ItemHolder.getInstance().getTemplate(247);
		}
		return null;
	}
	
	/**
	 * Method addExpAndCheckBonus.
	 * @param mob MonsterInstance
	 * @param noRateExp double
	 * @param noRateSp double
	 * @param partyVitalityMod double
	 */
	public void addExpAndCheckBonus(MonsterInstance mob, final double noRateExp, double noRateSp, double partyVitalityMod)
	{
		if (getActiveSubClass() == null)
		{
			return;
		}
		double neededExp = calcStat(Stats.SOULS_CONSUME_EXP, 0, mob, null);
		if ((neededExp > 0) && (noRateExp > neededExp))
		{
			mob.broadcastPacket(new SpawnEmitter(mob, this));
			ThreadPoolManager.getInstance().schedule(new GameObjectTasks.SoulConsumeTask(this), 1000);
		}
		int npcLevel = mob.getLevel();
		if (Config.ALT_VITALITY_ENABLED)
		{
			if (noRateExp > 0)
			{
				if (!mob.isRaid())
				{
					if (!(getVarB("NoExp") && (getExp() == (Experience.LEVEL[getLevel() + 1] - 1))))
					{
						double points = ((noRateExp / (npcLevel * npcLevel)) * 100) / 9;
						points *= Config.ALT_VITALITY_CONSUME_RATE;
						if (getEffectList().getEffectByType(EffectType.Vitality) != null || getEffectList().getEffectByStackType("vitalityRegen") != null)
						{
							points *= -1;
						}
						setVitality((int) (getVitality() - (points * partyVitalityMod)));
					}
				}
			}
		}
		long normalExp = (long) (noRateExp * (((Config.RATE_XP * getRateExp()) + getVitalityBonus() + getRecomBonusMul())));
		long normalSp = (long) (noRateSp * ((Config.RATE_SP * getRateSp()) + getVitalityBonus()));
		long expWithoutBonus = (long) (noRateExp * Config.RATE_XP * getRateExp());
		long spWithoutBonus = (long) (noRateSp * Config.RATE_SP * getRateSp());
		addExpAndSp(normalExp, normalSp, normalExp - expWithoutBonus, normalSp - spWithoutBonus, false, true);
	}
	
	/**
	 * Method addExpAndSp.
	 * @param exp long
	 * @param sp long
	 */
	@Override
	public void addExpAndSp(long exp, long sp)
	{
		addExpAndSp(exp, sp, 0, 0, false, false);
	}
	
	/**
	 * Method addExpAndSp.
	 * @param addToExp long
	 * @param addToSp long
	 * @param bonusAddExp long
	 * @param bonusAddSp long
	 * @param applyRate boolean
	 * @param applyToPet boolean
	 */
	public void addExpAndSp(long addToExp, long addToSp, long bonusAddExp, long bonusAddSp, boolean applyRate, boolean applyToPet)
	{
		if (getActiveSubClass() == null)
		{
			return;
		}
		if (applyRate)
		{
			addToExp *= Config.RATE_XP * getRateExp();
			addToSp *= Config.RATE_SP * getRateSp();
		}
		Summon pet = getSummonList().getPet();
		if (addToExp > 0)
		{
			if (applyToPet)
			{
				if ((pet != null) && !pet.isDead() && !PetDataTable.isVitaminPet(pet.getNpcId()))
				{
					if (pet.getNpcId() == PetDataTable.SIN_EATER_ID)
					{
						pet.addExpAndSp(addToExp, 0);
						addToExp = 0;
					}
					else if (pet.isPet() && (pet.getExpPenalty() > 0f))
					{
						if ((pet.getLevel() > (getLevel() - 20)) && (pet.getLevel() < (getLevel() + 5)))
						{
							pet.addExpAndSp((long) (addToExp * pet.getExpPenalty()), 0);
							addToExp *= 1. - pet.getExpPenalty();
						}
						else
						{
							pet.addExpAndSp((long) ((addToExp * pet.getExpPenalty()) / 5.), 0);
							addToExp *= 1. - (pet.getExpPenalty() / 5.);
						}
					}
					else if (pet.isServitor())
					{
						addToExp *= 1. - pet.getExpPenalty();
					}
				}
			}
			if (!isCursedWeaponEquipped() && (addToSp > 0L) && (_karma < 0))
			{
				_karma = (int) (_karma + (addToSp / (Config.KARMA_SP_DIVIDER * Config.RATE_SP)));
				addToExp = 0;
				addToSp = 0;
			}
			long max_xp = getVarB("NoExp") ? Experience.LEVEL[getLevel() + 1] - 1 : getMaxExp();
			addToExp = Math.min(addToExp, max_xp - getExp());
		}
		int oldLvl = getActiveSubClass().getLevel();
		getActiveSubClass().addExp(addToExp);
		getActiveSubClass().addSp(addToSp);
		if ((addToExp > 0L) && (addToSp > 0L) && ((bonusAddExp > 0L) || (bonusAddSp > 0L)))
		{
			sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_ACQUIRED_S1_EXP_BONUS_S2_AND_S3_SP_BONUS_S4).addLong(addToExp).addLong(bonusAddExp).addInteger(addToSp).addInteger((int) bonusAddSp));
		}
		else if ((addToSp > 0L) && (addToExp == 0L))
		{
			sendPacket(new SystemMessage(331).addNumber(addToSp));
		}
		else if ((addToSp > 0L) && (addToExp > 0L))
		{
			sendPacket(new SystemMessage(95).addNumber(addToExp).addNumber(addToSp));
		}
		else if ((addToSp == 0L) && (addToExp > 0L))
		{
			sendPacket(new SystemMessage(45).addNumber(addToExp));
		}
		int level = getActiveSubClass().getLevel();
		if (level != oldLvl)
		{
			int levels = level - oldLvl;
			levelSet(levels);
		}
		if ((pet != null) && pet.isPet() && PetDataTable.isVitaminPet(pet.getNpcId()))
		{
			PetInstance _pet = (PetInstance) pet;
			_pet.setLevel(getLevel());
			_pet.setExp(_pet.getExpForNextLevel());
			_pet.broadcastStatusUpdate();
		}
		WorldStatisticsManager.getInstance().updateStat(this, CategoryType.EXP_ADDED, addToExp);
		updateStats();
	}
	
	/**
	 * Method rewardSkills.
	 * @param send boolean
	 */
	public void rewardSkills(boolean send, boolean isSubclassAdd)
	{
		boolean update = false;
		if (Config.AUTO_LEARN_SKILLS || isSubclassAdd)
		{
			ClassId _cId = null;
			int unLearnable = 0;
			Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(this, AcquireType.NORMAL);
			while (skills.size() > unLearnable)
			{
				unLearnable = 0;
				for (SkillLearn s : skills)
				{
					Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
					if ((sk == null) || (sk.getId() > 10000 && sk.getId() != 20006 && sk.getId() != 19088 && sk.getId() != 19089 && sk.getId() != 19090 ) || (isAwaking() ? !sk.getCanLearn(getClassId()) && !sk.getCanLearn(_cId) : !sk.getCanLearn(getClassId())) || (!Config.AUTO_LEARN_FORGOTTEN_SKILLS && s.isClicked()))
					{
						unLearnable++;
						continue;
					}
					addSkill(sk, true);
				}
				skills = SkillAcquireHolder.getInstance().getAvailableSkills(this, AcquireType.NORMAL);
			}
			update = true;
		}
		else
		{
			Collection<SkillLearn> availableSkills = SkillAcquireHolder.getInstance().getAvailableSkills(this, AcquireType.NORMAL);
			for (SkillLearn skill : availableSkills)
			{
				if ((skill.getCost() == 0) && (skill.getItemId() == 0))
				{
					Skill sk = SkillTable.getInstance().getInfo(skill.getId(), skill.getLevel());
					addSkill(sk, true);
					if (
							(getAllShortCuts().size() > 0) && 
							(sk.getLevel() > 1)
						)
					{
						for (ShortCut sc : getAllShortCuts())
						{
							if ((sc.getId() == sk.getId()) && (sc.getType() == ShortCut.TYPE_SKILL))
							{
								ShortCut newsc = new ShortCut(sc.getSlot(), sc.getPage(), sc.getType(), sc.getId(), sk.getLevel(), 1);
								sendPacket(new ShortCutRegister(this, newsc));
								registerShortCut(newsc);
							}
						}
					}
					update = true;
				}
			}
			if (availableSkills.size() > 0)
			{
				sendPacket(new ExNewSkillToLearnByLevelUp());
			}
		}
		if (send && update)
		{
			sendSkillList();
		}
		updateStats();
	}
	
	/**
	 * Method getRace.
	 * @return Race
	 */
	public Race getRace()
	{
		return ClassId.VALUES[getBaseDefaultClassId()].getRace();
	}
	
	/**
	 * Method getIntSp.
	 * @return int
	 */
	public int getIntSp()
	{
		return (int) getSp();
	}
	
	/**
	 * Method getSp.
	 * @return long
	 */
	public long getSp()
	{
		return getActiveSubClass() == null ? 0 : getActiveSubClass().getSp();
	}
	
	/**
	 * Method setSp.
	 * @param sp long
	 */
	public void setSp(long sp)
	{
		if (getActiveSubClass() != null)
		{
			getActiveSubClass().setSp(sp);
		}
	}
	
	/**
	 * Method getClanId.
	 * @return int
	 */
	public int getClanId()
	{
		return _clan == null ? 0 : _clan.getClanId();
	}
	
	/**
	 * Method getLeaveClanTime.
	 * @return long
	 */
	public long getLeaveClanTime()
	{
		return _leaveClanTime;
	}
	
	/**
	 * Method getDeleteClanTime.
	 * @return long
	 */
	public long getDeleteClanTime()
	{
		return _deleteClanTime;
	}
	
	/**
	 * Method setLeaveClanTime.
	 * @param time long
	 */
	public void setLeaveClanTime(final long time)
	{
		_leaveClanTime = time;
	}
	
	/**
	 * Method setDeleteClanTime.
	 * @param time long
	 */
	public void setDeleteClanTime(final long time)
	{
		_deleteClanTime = time;
	}
	
	/**
	 * Method setOnlineTime.
	 * @param time long
	 */
	public void setOnlineTime(final long time)
	{
		_onlineTime = time;
		_onlineBeginTime = System.currentTimeMillis();
	}
	
	/**
	 * Method setNoChannel.
	 * @param time long
	 */
	public void setNoChannel(final long time)
	{
		_NoChannel = time;
		if ((_NoChannel > 2145909600000L) || (_NoChannel < 0))
		{
			_NoChannel = -1;
		}
		if (_NoChannel > 0)
		{
			_NoChannelBegin = System.currentTimeMillis();
		}
		else
		{
			_NoChannelBegin = 0;
		}
	}
	
	/**
	 * Method getNoChannel.
	 * @return long
	 */
	public long getNoChannel()
	{
		return _NoChannel;
	}
	
	/**
	 * Method getNoChannelRemained.
	 * @return long
	 */
	public long getNoChannelRemained()
	{
		if (_NoChannel == 0)
		{
			return 0;
		}
		else if (_NoChannel < 0)
		{
			return -1;
		}
		else
		{
			long remained = (_NoChannel - System.currentTimeMillis()) + _NoChannelBegin;
			if (remained < 0)
			{
				return 0;
			}
			return remained;
		}
	}
	
	/**
	 * Method setLeaveClanCurTime.
	 */
	public void setLeaveClanCurTime()
	{
		_leaveClanTime = System.currentTimeMillis();
	}
	
	/**
	 * Method setDeleteClanCurTime.
	 */
	public void setDeleteClanCurTime()
	{
		_deleteClanTime = System.currentTimeMillis();
	}
	
	/**
	 * Method canJoinClan.
	 * @return boolean
	 */
	public boolean canJoinClan()
	{
		if (_leaveClanTime == 0)
		{
			return true;
		}
		if ((System.currentTimeMillis() - _leaveClanTime) >= (24 * 60 * 60 * 1000L))
		{
			_leaveClanTime = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * Method canCreateClan.
	 * @return boolean
	 */
	public boolean canCreateClan()
	{
		if (_deleteClanTime == 0)
		{
			return true;
		}
		if ((System.currentTimeMillis() - _deleteClanTime) >= (10 * 24 * 60 * 60 * 1000L))
		{
			_deleteClanTime = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * Method canJoinParty.
	 * @param inviter Player
	 * @return IStaticPacket
	 */
	public IStaticPacket canJoinParty(Player inviter)
	{
		Request request = getRequest();
		if ((request != null) && request.isInProgress() && (request.getOtherPlayer(this) != inviter))
		{
			return SystemMsg.WAITING_FOR_ANOTHER_REPLY.packet(inviter);
		}
		if (isBlockAll() || getMessageRefusal())
		{
			return SystemMsg.THAT_PERSON_IS_IN_MESSAGE_REFUSAL_MODE.packet(inviter);
		}
		if (isInParty())
		{
			return new SystemMessage2(SystemMsg.C1_IS_A_MEMBER_OF_ANOTHER_PARTY_AND_CANNOT_BE_INVITED).addName(this);
		}
		if (inviter.getReflection() != getReflection())
		{
			if ((inviter.getReflection() != ReflectionManager.DEFAULT) && (getReflection() != ReflectionManager.DEFAULT))
			{
				return SystemMsg.INVALID_TARGET.packet(inviter);
			}
		}
		if (isCursedWeaponEquipped() || inviter.isCursedWeaponEquipped())
		{
			return SystemMsg.INVALID_TARGET.packet(inviter);
		}
		if (inviter.isInOlympiadMode() || isInOlympiadMode())
		{
			return SystemMsg.A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS.packet(inviter);
		}
		if (!inviter.getPlayerAccess().CanJoinParty || !getPlayerAccess().CanJoinParty)
		{
			return SystemMsg.INVALID_TARGET.packet(inviter);
		}
		if (getTeam() != TeamType.NONE)
		{
			return SystemMsg.INVALID_TARGET.packet(inviter);
		}
		return null;
	}
	
	/**
	 * Method getInventory.
	 * @return PcInventory
	 */
	@Override
	public PcInventory getInventory()
	{
		return _inventory;
	}
	
	/**
	 * Method getWearedMask.
	 * @return long
	 */
	@Override
	public long getWearedMask()
	{
		return _inventory.getWearedMask();
	}
	
	/**
	 * Method getFreight.
	 * @return PcFreight
	 */
	public PcFreight getFreight()
	{
		return _freight;
	}
	
	/**
	 * Method removeItemFromShortCut.
	 * @param objectId int
	 */
	public void removeItemFromShortCut(final int objectId)
	{
		_shortCuts.deleteShortCutByObjectId(objectId);
	}
	
	/**
	 * Method removeSkillFromShortCut.
	 * @param skillId int
	 */
	public void removeSkillFromShortCut(final int skillId)
	{
		_shortCuts.deleteShortCutBySkillId(skillId);
	}
	
	/**
	 * Method isSitting.
	 * @return boolean
	 */
	public boolean isSitting()
	{
		return _isSitting;
	}
	
	/**
	 * Method setSitting.
	 * @param val boolean
	 */
	public void setSitting(boolean val)
	{
		_isSitting = val;
	}
	
	/**
	 * Method getSittingTask.
	 * @return boolean
	 */
	public boolean getSittingTask()
	{
		return sittingTaskLaunched;
	}
	
	/**
	 * Method sitDown.
	 * @param throne StaticObjectInstance
	 */
	@Override
	public void sitDown(StaticObjectInstance throne)
	{
		if (isSitting() || sittingTaskLaunched || isAlikeDead())
		{
			return;
		}
		if (isStunned() || isSleeping() || isParalyzed() || isAttackingNow() || isCastingNow() || isMoving || isAirBinded() || isKnockedBack() || isKnockedDown() || isPulledNow())
		{
			getAI().setNextAction(nextAction.REST, null, null, false, false);
			return;
		}
		resetWaitSitTime();
		getAI().setIntention(CtrlIntention.AI_INTENTION_REST, null, null);
		if (throne == null)
		{
			broadcastPacket(new ChangeWaitType(this, ChangeWaitType.WT_SITTING));
		}
		else
		{
			broadcastPacket(new ChairSit(this, throne));
		}
		_sittingObject = throne;
		setSitting(true);
		sittingTaskLaunched = true;
		ThreadPoolManager.getInstance().schedule(new EndSitDownTask(this), 2500);
	}
	
	/**
	 * Method standUp.
	 */
	@Override
	public void standUp()
	{
		if (!isSitting() || sittingTaskLaunched || isInStoreMode() || isAlikeDead())
		{
			return;
		}
		getEffectList().stopAllSkillEffects(EffectType.Relax);
		getAI().clearNextAction();
		broadcastPacket(new ChangeWaitType(this, ChangeWaitType.WT_STANDING));
		_sittingObject = null;
		setSitting(false);
		sittingTaskLaunched = true;
		ThreadPoolManager.getInstance().schedule(new EndStandUpTask(this), 2500);
	}
	
	/**
	 * Method updateWaitSitTime.
	 */
	public void updateWaitSitTime()
	{
		if (_waitTimeWhenSit < 200)
		{
			_waitTimeWhenSit += 2;
		}
	}
	
	/**
	 * Method getWaitSitTime.
	 * @return int
	 */
	public int getWaitSitTime()
	{
		return _waitTimeWhenSit;
	}
	
	/**
	 * Method resetWaitSitTime.
	 */
	public void resetWaitSitTime()
	{
		_waitTimeWhenSit = 0;
	}
	
	/**
	 * Method getWarehouse.
	 * @return Warehouse
	 */
	public Warehouse getWarehouse()
	{
		return _warehouse;
	}
	
	/**
	 * Method getRefund.
	 * @return ItemContainer
	 */
	public ItemContainer getRefund()
	{
		return _refund;
	}
	
	/**
	 * Method getAdena.
	 * @return long
	 */
	public long getAdena()
	{
		return getInventory().getAdena();
	}
	
	/**
	 * Method reduceAdena.
	 * @param adena long
	 * @return boolean
	 */
	public boolean reduceAdena(long adena)
	{
		return reduceAdena(adena, false);
	}
	
	/**
	 * Method reduceAdena.
	 * @param adena long
	 * @param notify boolean
	 * @return boolean
	 */
	public boolean reduceAdena(long adena, boolean notify)
	{
		if (adena < 0)
		{
			return false;
		}
		if (adena == 0)
		{
			return true;
		}
		boolean result = getInventory().reduceAdena(adena);
		if (notify && result)
		{
			sendPacket(SystemMessage2.removeItems(ItemTemplate.ITEM_ID_ADENA, adena));
		}
		return result;
	}
	
	/**
	 * Method addAdena.
	 * @param adena long
	 * @return ItemInstance
	 */
	public ItemInstance addAdena(long adena)
	{
		return addAdena(adena, false);
	}
	
	/**
	 * Method addAdena.
	 * @param adena long
	 * @param notify boolean
	 * @return ItemInstance
	 */
	public ItemInstance addAdena(long adena, boolean notify)
	{
		if (adena < 1)
		{
			return null;
		}
		ItemInstance item = getInventory().addAdena(adena);
		if ((item != null) && notify)
		{
			sendPacket(SystemMessage2.obtainItems(ItemTemplate.ITEM_ID_ADENA, adena, 0));
		}
		return item;
	}
	
	/**
	 * Method getNetConnection.
	 * @return GameClient
	 */
	public GameClient getNetConnection()
	{
		return _connection;
	}
	
	/**
	 * Method getRevision.
	 * @return int
	 */
	public int getRevision()
	{
		return _connection == null ? 0 : _connection.getRevision();
	}
	
	/**
	 * Method setNetConnection.
	 * @param connection GameClient
	 */
	public void setNetConnection(final GameClient connection)
	{
		_connection = connection;
	}
	
	/**
	 * Method isConnected.
	 * @return boolean
	 */
	public boolean isConnected()
	{
		return (_connection != null) && _connection.isConnected();
	}
	
	/**
	 * Method broadcastStatusUpdate.
	 */
	@Override
	public void broadcastStatusUpdate()
	{
		super.broadcastStatusUpdate();

		sendPacket(new StatusUpdate(this).addAttribute(StatusUpdateField.CUR_HP, StatusUpdateField.CUR_MP, StatusUpdateField.CUR_CP, StatusUpdateField.MAX_HP, StatusUpdateField.MAX_MP, StatusUpdateField.MAX_CP));
		if(isInParty())
		// Send the Server->Client packet PartySmallWindowUpdate with current HP, MP and Level to all other L2Player of the Party
		{
			getParty().broadcastToPartyMembers(this, new PartySmallWindowUpdate(this));
		}

		if(isInOlympiadMode() && isOlympiadCompStart())
		{
			if(_olympiadGame != null)
			{
				_olympiadGame.broadcastInfo(this, null, false);
			}
		}
	}
	
	/**
	 * Method broadcastCharInfo.
	 */
	@Override
	public void broadcastCharInfo()
	{
		broadcastUserInfo();
	}
	
	/**
	 * Method broadcastUserInfo.
	 * @param force boolean
	 */
	public void broadcastUserInfo()
	{
		sendUserInfo();
		if (!isVisible() || isInvisible())
		{
			return;
		}
		L2GameServerPacket ci = isPolymorphed() ? new NpcInfoPoly(this) : new CharInfo(this);
		L2GameServerPacket exCi = new ExBR_ExtraUserInfo(this);
		for (Player player : World.getAroundPlayers(this))
		{
			player.sendPacket(ci, exCi);
			player.sendPacket(RelationChanged.update(player, this, player));
		}
		return;
	}
	
	/**
	 * Field _polyNpcId.
	 */
	private int _polyNpcId;
	
	/**
	 * Method setPolyId.
	 * @param polyid int
	 */
	public void setPolyId(int polyid)
	{
		_polyNpcId = polyid;
		teleToLocation(getLoc());
		broadcastUserInfo();
	}
	
	/**
	 * Method isPolymorphed.
	 * @return boolean
	 */
	public boolean isPolymorphed()
	{
		return _polyNpcId != 0;
	}
	
	/**
	 * Method getPolyId.
	 * @return int
	 */
	public int getPolyId()
	{
		return _polyNpcId;
	}
	
	/**
	 * Method broadcastRelationChanged.
	 */
	public void broadcastRelationChanged()
	{
		if (!isVisible() || isInvisible())
		{
			return;
		}
		for (Player player : World.getAroundPlayers(this))
		{
			player.sendPacket(RelationChanged.update(player, this, player));
		}
	}
	
	/**
	 * Method sendEtcStatusUpdate.
	 */
	public void sendEtcStatusUpdate()
	{
		if (!isVisible())
		{
			return;
		}
		sendPacket(new EtcStatusUpdate(this));
	}
	
	/**
	 * Method sendUserInfo.
	 * @param force boolean
	 */
	public void sendUserInfo()
	{
		if (!isVisible() || entering || isLogoutStarted())
		{
			return;
		}
		sendPacket(new UserInfo(this), new ExBR_ExtraUserInfo(this));
		return;
	}
	
	/**
	 * Method sendStatusUpdate.
	 * @param broadCast boolean
	 * @param withPet boolean
	 * @param fields int[]
	 */
	public void sendStatusUpdate(boolean broadCast, boolean withPet, StatusUpdateField... fields)
	{
		if ((fields.length == 0) || (entering && !broadCast))
		{
			return;
		}
		
		StatusUpdate su = new StatusUpdate(this).addAttribute(fields);

		if(su.isEmpty())
		{
			return;
		}

		List<L2GameServerPacket> packets = new ArrayList<L2GameServerPacket>(withPet ? 4 : 1);
		if (withPet)
		{
			for (Summon summon : getSummonList())
			{
				packets.add(new StatusUpdate(summon).addAttribute(fields));
			}
		}
		packets.add(su);
		if (!broadCast)
		{
			sendPacket(packets);
		}
		else if (entering)
		{
			broadcastPacketToOthers(packets);
		}
		else
		{
			broadcastPacket(packets);
		}
	}
	
	/**
	 * Method getAllyId.
	 * @return int
	 */
	public int getAllyId()
	{
		return _clan == null ? 0 : _clan.getAllyId();
	}
	
	/**
	 * Method sendPacket.
	 * @param p IStaticPacket
	 */
	@Override
	public void sendPacket(IStaticPacket p)
	{
		if (!isConnected())
		{
			return;
		}
		if (isPacketIgnored(p.packet(this)))
		{
			return;
		}
		_connection.sendPacket(p.packet(this));
	}
	
	/**
	 * Method sendPacket.
	 * @param packets IStaticPacket[]
	 */
	@Override
	public void sendPacket(IStaticPacket... packets)
	{
		if (!isConnected())
		{
			return;
		}
		for (IStaticPacket p : packets)
		{
			if (isPacketIgnored(p))
			{
				continue;
			}
			_connection.sendPacket(p.packet(this));
		}
	}
	
	/**
	 * Method isPacketIgnored.
	 * @param p IStaticPacket
	 * @return boolean
	 */
	private boolean isPacketIgnored(IStaticPacket p)
	{
		if (p == null)
		{
			return true;
		}
		if (_notShowBuffAnim && ((p.getClass() == MagicSkillUse.class) || (p.getClass() == MagicSkillLaunched.class)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method sendPacket.
	 * @param packets List<? extends IStaticPacket>
	 */
	@Override
	public void sendPacket(List<? extends IStaticPacket> packets)
	{
		if (!isConnected())
		{
			return;
		}
		for (IStaticPacket p : packets)
		{
			_connection.sendPacket(p.packet(this));
		}
	}
	
	/**
	 * Method doAutoLootOrDrop.
	 * @param item ItemInstance
	 * @param fromNpc NpcInstance
	 */
	public void doAutoLootOrDrop(ItemInstance item, NpcInstance fromNpc)
	{
		boolean forceAutoloot = fromNpc.isFlying() || getReflection().isAutolootForced();
		if ((fromNpc.isRaid() || (fromNpc instanceof ReflectionBossInstance)) && !Config.AUTO_LOOT_FROM_RAIDS && !item.isHerb() && !forceAutoloot)
		{
			item.dropToTheGround(this, fromNpc);
			return;
		}
		if (item.isHerb())
		{
			if (!AutoLootHerbs && !forceAutoloot)
			{
				item.dropToTheGround(this, fromNpc);
				return;
			}
			Skill[] skills = item.getTemplate().getAttachedSkills();
			if (skills.length > 0)
			{
				for (Skill skill : skills)
				{
					altUseSkill(skill, this);
					for (Summon summon : getSummonList())
					{
						if (summon.isDead())
						{
							summon.altUseSkill(skill, summon);
						}
					}
				}
			}
			item.deleteMe();
			return;
		}
		if (!_autoLoot && !forceAutoloot)
		{
			item.dropToTheGround(this, fromNpc);
			return;
		}
		if (!isInParty())
		{
			if (!pickupItem(item, Log.Pickup))
			{
				item.dropToTheGround(this, fromNpc);
				return;
			}
		}
		else
		{
			getParty().distributeItem(this, item, fromNpc);
		}
		broadcastPickUpMsg(item);
	}
	
	/**
	 * Method doPickupItem.
	 * @param object GameObject
	 */
	@Override
	public void doPickupItem(final GameObject object)
	{
		if (!object.isItem())
		{
			_log.warn("trying to pickup wrong target." + getTarget());
			return;
		}
		sendActionFailed();
		stopMove();
		ItemInstance item = (ItemInstance) object;
		synchronized (item)
		{
			if (!item.isVisible())
			{
				return;
			}
			if (!ItemFunctions.checkIfCanPickup(this, item))
			{
				SystemMessage sm;
				if (item.getItemId() == 57)
				{
					sm = new SystemMessage(SystemMessage.YOU_HAVE_FAILED_TO_PICK_UP_S1_ADENA);
					sm.addNumber(item.getCount());
				}
				else
				{
					sm = new SystemMessage(SystemMessage.YOU_HAVE_FAILED_TO_PICK_UP_S1);
					sm.addItemName(item.getItemId());
				}
				sendPacket(sm);
				return;
			}
			if (item.isHerb())
			{
				Skill[] skills = item.getTemplate().getAttachedSkills();
				if (skills.length > 0)
				{
					for (Skill skill : skills)
					{
						altUseSkill(skill, this);
					}
				}
				broadcastPacket(new GetItem(item, getObjectId()));
				item.deleteMe();
				return;
			}
			FlagItemAttachment attachment = item.getAttachment() instanceof FlagItemAttachment ? (FlagItemAttachment) item.getAttachment() : null;
			if (!isInParty() || (attachment != null))
			{
				if (pickupItem(item, Log.Pickup))
				{
					broadcastPacket(new GetItem(item, getObjectId()));
					broadcastPickUpMsg(item);
					item.pickupMe();
				}
			}
			else
			{
				getParty().distributeItem(this, item, null);
			}
		}
	}
	
	/**
	 * Method pickupItem.
	 * @param item ItemInstance
	 * @param log String
	 * @return boolean
	 */
	public boolean pickupItem(ItemInstance item, String log)
	{
		PickableAttachment attachment = item.getAttachment() instanceof PickableAttachment ? (PickableAttachment) item.getAttachment() : null;
		if (!ItemFunctions.canAddItem(this, item))
		{
			return false;
		}
		if ((item.getItemId() == ItemTemplate.ITEM_ID_ADENA) || (item.getItemId() == 6353))
		{
			Quest q = QuestManager.getQuest(255);
			if (q != null)
			{
				processQuestEvent(q.getName(), "CE" + item.getItemId(), null);
			}
		}
		if ((item.getItemId() == ItemTemplate.ITEM_ID_ADENA))
		{
			if (item.getOwnerId() == 0)
			{
				WorldStatisticsManager.getInstance().updateStat(this, CategoryType.ADENA_ADDED, item.getCount());
			}
		}
		Log.LogItem(this, log, item);
		sendPacket(SystemMessage2.obtainItems(item));
		getInventory().addItem(item);
		if (attachment != null)
		{
			attachment.pickUp(this);
		}
		sendChanges();
		return true;
	}
	
	/**
	 * Method setTarget.
	 * @param newTarget GameObject
	 */
	@Override
	public void setTarget(GameObject newTarget)
	{
		// Check if the new target is visible
		if ((newTarget != null) && !newTarget.isVisible())
		{
			newTarget = null;
		}

		GameObject oldTarget = getTarget();
		
		if (oldTarget != null)
		{
			if (oldTarget.equals(newTarget))
			{
				return;
			}
			if(oldTarget.isCreature())
			{
				((Creature) oldTarget).removeStatusListener(this);
			}
			
			broadcastPacket(new TargetUnselected(this));
		}
		
		if (newTarget != null)
		{
			if(newTarget.isCreature())
			{
				((Creature) newTarget).addStatusListener(this);

				if(newTarget.displayHpBar())
				{
					sendPacket(new StatusUpdate(((Creature) newTarget)).addAttribute(StatusUpdateField.MAX_HP, StatusUpdateField.CUR_HP));
				}
			}

			updateTargetSelectionInfo(newTarget);
			broadcastPacketToOthers(new TargetSelected(getObjectId(), newTarget.getObjectId(), getLoc()));
		}
		super.setTarget(newTarget);
	}
	
	/**
	 * Method getActiveWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
	}
	
	/**
	 * Method getActiveWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		final ItemInstance weapon = getActiveWeaponInstance();
		if (weapon == null)
		{
			return getFistsWeaponItem();
		}
		return (WeaponTemplate) weapon.getTemplate();
	}
	
	/**
	 * Method getSecondaryWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
	}
	
	/**
	 * Method getSecondaryWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		final ItemInstance weapon = getSecondaryWeaponInstance();
		if (weapon == null)
		{
			return getFistsWeaponItem();
		}
		final ItemTemplate item = weapon.getTemplate();
		if (item instanceof WeaponTemplate)
		{
			return (WeaponTemplate) item;
		}
		return null;
	}
	
	/**
	 * Method isWearingArmor.
	 * @param armorType ArmorType
	 * @return boolean
	 */
	public boolean isWearingArmor(final ArmorType armorType)
	{
		final ItemInstance chest = getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		if (chest == null)
		{
			return armorType == ArmorType.NONE;
		}
		if (chest.getItemType() != armorType)
		{
			return false;
		}
		if (chest.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
		{
			return true;
		}
		final ItemInstance legs = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		return legs == null ? armorType == ArmorType.NONE : legs.getItemType() == armorType;
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param damage double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if ((attacker == null) || isDead() || (attacker.isDead() && !isDot))
		{
			return;
		}
		if (attacker.isPlayer() && (Math.abs(attacker.getLevel() - getLevel()) > 10))
		{
			WorldStatisticsManager.getInstance().updateStat(attacker.getPlayer(), CategoryType.DAMAGE_TO_PC, (long) damage);
			WorldStatisticsManager.getInstance().updateStat(attacker.getPlayer(), CategoryType.DAMAGE_TO_PC_MAX, getActiveClassId(), (long) damage);
			WorldStatisticsManager.getInstance().updateStat(this, CategoryType.DAMAGE_FROM_PC, (long) damage);
			if ((attacker.getKarma() > 0) && (getEffectList().getEffectsBySkillId(5182) != null) && !isInZone(ZoneType.SIEGE))
			{
				return;
			}
			if ((getKarma() > 0) && (attacker.getEffectList().getEffectsBySkillId(5182) != null) && !attacker.isInZone(ZoneType.SIEGE))
			{
				return;
			}
		}
		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);

		if (attacker.getPlayer() == null)
			WorldStatisticsManager.getInstance().updateStat(this, CategoryType.DAMAGE_FROM_MONSTERS, getClassId().getId(), (long) damage);
	}
	
	/**
	 * Method onReduceCurrentHp.
	 * @param damage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 */
	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		if (standUp)
		{
			standUp();
			if (isFakeDeath())
			{
				breakFakeDeath();
			}
		}
		if (attacker.isPlayable())
		{
			if (!directHp && (getCurrentCp() > 0))
			{
				double cp = getCurrentCp();
				if (cp >= damage)
				{
					cp -= damage;
					damage = 0;
				}
				else
				{
					damage -= cp;
					cp = 0;
				}
				setCurrentCp(cp);
			}
		}
		double hp = getCurrentHp();
		DuelEvent duelEvent = getEvent(DuelEvent.class);
		if (duelEvent != null)
		{
			if ((hp - damage) <= 1)
			{
				setCurrentHp(1, false);
				duelEvent.onDie(this);
				return;
			}
		}
		if (isInOlympiadMode())
		{
			OlympiadGame game = _olympiadGame;
			if ((this != attacker) && ((skill == null) || skill.isOffensive()))
			{
				game.addDamage(this, Math.min(hp, damage));
			}
			if ((hp - damage) <= 1)
			{
				if (game.getType() != CompType.TEAM)
				{
					game.setWinner(getOlympiadSide() == 1 ? 2 : 1);
					game.endGame(20000, false);
					setCurrentHp(1, false);
					attacker.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
					attacker.sendActionFailed();
					return;
				}
				else if (game.doDie(this))
				{
					game.setWinner(getOlympiadSide() == 1 ? 2 : 1);
					game.endGame(20000, false);
				}
			}
		}
		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp);
	}
	
	/**
	 * Method altDeathPenalty.
	 * @param killer Creature
	 */
	private void altDeathPenalty(final Creature killer)
	{
		if (!Config.ALT_GAME_EXP_LOST)
		{
			return;
		}
		if (isInZoneBattle())
		{
			return;
		}
		deathPenalty(killer);
	}
	
	/**
	 * Method atWarWith.
	 * @param player Player
	 * @return boolean
	 */
	public final boolean atWarWith(final Player player)
	{
		return (_clan != null) && (player.getClan() != null) && (getPledgeType() != -1) && (player.getPledgeType() != -1) && _clan.isAtWarWith(player.getClan().getClanId());
	}
	
	/**
	 * Method atMutualWarWith.
	 * @param player Player
	 * @return boolean
	 */
	public boolean atMutualWarWith(Player player)
	{
		return (_clan != null) && (player.getClan() != null) && (getPledgeType() != -1) && (player.getPledgeType() != -1) && _clan.isAtWarWith(player.getClan().getClanId()) && player.getClan().isAtWarWith(_clan.getClanId());
	}
	
	/**
	 * Method doPurePk.
	 * @param killer Player
	 */
	public final void doPurePk(final Player killer)
	{
		final int pkCountMulti = Math.max(killer.getPkKills() / 2, 1);
		killer.decreaseKarma(Config.KARMA_MIN_KARMA * pkCountMulti);
		killer.setPkKills(killer.getPkKills() + 1);
		WorldStatisticsManager.getInstance().updateStat(killer, CategoryType.PK_COUNT, 1);
		WorldStatisticsManager.getInstance().updateStat(this, CategoryType.KILLED_BY_PK_COUNT, 1);
	}
	
	/**
	 * Method doKillInPeace.
	 * @param killer Player
	 */
	public final void doKillInPeace(final Player killer)
	{
		if (_karma >= 0)
		{
			doPurePk(killer);
		}
		else
		{
			if (killer.getKarma() >= 0)
			{
				killer.increaseKarma(Config.REPUTATION_COUNT);
			}
			killer.setNameColor(Config.PK_KILLER_NAME_COLOUR);
			WorldStatisticsManager.getInstance().updateStat(killer, CategoryType.PVP_COUNT, 1);
		}
	}
	
	/**
	 * Method checkAddItemToDrop.
	 * @param array List<ItemInstance>
	 * @param items List<ItemInstance>
	 * @param maxCount int
	 */
	public void checkAddItemToDrop(List<ItemInstance> array, List<ItemInstance> items, int maxCount)
	{
		for (int i = 0; (i < maxCount) && !items.isEmpty(); i++)
		{
			array.add(items.remove(Rnd.get(items.size())));
		}
	}
	
	/**
	 * Method getActiveWeaponFlagAttachment.
	 * @return FlagItemAttachment
	 */
	public FlagItemAttachment getActiveWeaponFlagAttachment()
	{
		ItemInstance item = getActiveWeaponInstance();
		if ((item == null) || !(item.getAttachment() instanceof FlagItemAttachment))
		{
			return null;
		}
		return (FlagItemAttachment) item.getAttachment();
	}
	
	/**
	 * Method doPKPVPManage.
	 * @param killer Creature
	 */
	protected void doPKPVPManage(Creature killer)
	{
		FlagItemAttachment attachment = getActiveWeaponFlagAttachment();
		if (attachment != null)
		{
			attachment.onDeath(this, killer);
		}
		if ((killer == null) || (killer == this))
		{
			return;
		}
		if (isInZoneBattle() || killer.isInZoneBattle())
		{
			return;
		}
		if ((killer instanceof Summon) && ((killer = killer.getPlayer()) == null))
		{
			return;
		}
		if (killer.isPlayer())
		{
			final Player pk = (Player) killer;
			final int repValue = (getLevel() - pk.getLevel()) >= 20 ? 2 : 1;
			boolean war = atMutualWarWith(pk);
			if (war)
			{
				if ((pk.getClan().getReputationScore() > 0) && (_clan.getLevel() >= 5) && (_clan.getReputationScore() > 0) && (pk.getClan().getLevel() >= 5))
				{
					_clan.broadcastToOtherOnlineMembers(new SystemMessage(SystemMessage.YOUR_CLAN_MEMBER_S1_WAS_KILLED_S2_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENT_CLAN_REPUTATION_SCORE).addString(getName()).addNumber(-_clan.incReputation(-repValue, true, "ClanWar")), this);
					pk.getClan().broadcastToOtherOnlineMembers(new SystemMessage(SystemMessage.FOR_KILLING_AN_OPPOSING_CLAN_MEMBER_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_OPPONENTS_CLAN_REPUTATION_SCORE).addNumber(pk.getClan().incReputation(repValue, true, "ClanWar")), pk);
				}
			}
			if (isOnSiegeField())
			{
				return;
			}
			if ((_pvpFlag > 0) || war)
			{
				pk.setPvpKills(pk.getPvpKills() + 1);
				WorldStatisticsManager.getInstance().updateStat(pk, CategoryType.PVP_COUNT, 1);
				WorldStatisticsManager.getInstance().updateStat(this, CategoryType.KILLED_IN_PVP_COUNT, 1);
			}
			else
			{
				doKillInPeace(pk);
			}
			pk.sendChanges();
		}
		if (_karma < 0)
		{
			increaseKarma(Config.KARMA_LOST_BASE);
			if (_karma > 0)
			{
				_karma = 0;
			}
		}
		boolean isPvP = killer.isPlayable() || (killer instanceof GuardInstance);
		if ((killer.isMonster() && !Config.DROP_ITEMS_ON_DIE) || (isPvP && ((_pkKills < Config.MIN_PK_TO_ITEMS_DROP) || ((_karma >= 0) && Config.KARMA_NEEDED_TO_DROP))) || (!killer.isMonster() && !isPvP))
		{
			return;
		}
		if (!Config.KARMA_DROP_GM && isGM())
		{
			return;
		}
		final int max_drop_count = isPvP ? Config.KARMA_DROP_ITEM_LIMIT : 1;
		double dropRate;
		if (isPvP)
		{
			dropRate = (_pkKills * Config.KARMA_DROPCHANCE_MOD) + Config.KARMA_DROPCHANCE_BASE;
		}
		else
		{
			dropRate = Config.NORMAL_DROPCHANCE_BASE;
		}
		int dropEquipCount = 0, dropWeaponCount = 0, dropItemCount = 0;
		for (int i = 0; (i < Math.ceil(dropRate / 100)) && (i < max_drop_count); i++)
		{
			if (Rnd.chance(dropRate))
			{
				int rand = Rnd.get(Config.DROPCHANCE_EQUIPPED_WEAPON + Config.DROPCHANCE_EQUIPMENT + Config.DROPCHANCE_ITEM) + 1;
				if (rand > (Config.DROPCHANCE_EQUIPPED_WEAPON + Config.DROPCHANCE_EQUIPMENT))
				{
					dropItemCount++;
				}
				else if (rand > Config.DROPCHANCE_EQUIPPED_WEAPON)
				{
					dropEquipCount++;
				}
				else
				{
					dropWeaponCount++;
				}
			}
		}
		List<ItemInstance> drop = new LazyArrayList<ItemInstance>(), dropItem = new LazyArrayList<ItemInstance>(), dropEquip = new LazyArrayList<ItemInstance>(), dropWeapon = new LazyArrayList<ItemInstance>();
		getInventory().writeLock();
		try
		{
			for (ItemInstance item : getInventory().getItems())
			{
				if (!item.canBeDropped(this, true) || Config.KARMA_LIST_NONDROPPABLE_ITEMS.contains(item.getItemId()))
				{
					continue;
				}
				if (item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON)
				{
					dropWeapon.add(item);
				}
				else if ((item.getTemplate().getType2() == ItemTemplate.TYPE2_SHIELD_ARMOR) || (item.getTemplate().getType2() == ItemTemplate.TYPE2_ACCESSORY))
				{
					dropEquip.add(item);
				}
				else if (item.getTemplate().getType2() == ItemTemplate.TYPE2_OTHER)
				{
					dropItem.add(item);
				}
			}
			checkAddItemToDrop(drop, dropWeapon, dropWeaponCount);
			checkAddItemToDrop(drop, dropEquip, dropEquipCount);
			checkAddItemToDrop(drop, dropItem, dropItemCount);
			if (drop.isEmpty())
			{
				return;
			}
			for (ItemInstance item : drop)
			{
				if (item.isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED)
				{
					item.setAugmentationId(0);
				}
				item = getInventory().removeItem(item);
				Log.LogItem(this, Log.PvPDrop, item);
				if (item.getEnchantLevel() > 0)
				{
					sendPacket(new SystemMessage(SystemMessage.DROPPED__S1_S2).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()));
				}
				else
				{
					sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_DROPPED_S1).addItemName(item.getItemId()));
				}
				if (killer.isPlayable() && ((Config.AUTO_LOOT && Config.AUTO_LOOT_PK) || isInFlyingTransform()))
				{
					killer.getPlayer().getInventory().addItem(item);
					Log.LogItem(this, Log.Pickup, item);
					killer.getPlayer().sendPacket(SystemMessage2.obtainItems(item));
				}
				else
				{
					item.dropToTheGround(this, Location.findAroundPosition(this, Config.KARMA_RANDOM_DROP_LOCATION_LIMIT));
				}
			}
		}
		finally
		{
			getInventory().writeUnlock();
		}
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		getDeathPenalty().checkCharmOfLuck();
		if (isInStoreMode())
		{
			setPrivateStoreType(Player.STORE_PRIVATE_NONE);
		}
		if (isProcessingRequest())
		{
			Request request = getRequest();
			if (isInTrade())
			{
				Player parthner = request.getOtherPlayer(this);
				sendPacket(SendTradeDone.FAIL);
				parthner.sendPacket(SendTradeDone.FAIL);
			}
			request.cancel();
		}
		setAgathion(0);
		boolean checkPvp = true;
		if (Config.ALLOW_CURSED_WEAPONS)
		{
			if (isCursedWeaponEquipped())
			{
				CursedWeaponsManager.getInstance().dropPlayer(this);
				checkPvp = false;
			}
			else if ((killer != null) && killer.isPlayer() && killer.isCursedWeaponEquipped())
			{
				CursedWeaponsManager.getInstance().increaseKills(((Player) killer).getCursedWeaponEquippedId());
				checkPvp = false;
			}
		}
		if (checkPvp)
		{
			doPKPVPManage(killer);
			altDeathPenalty(killer);
		}
		getDeathPenalty().notifyDead(killer);
		setIncreasedForce(0);
		stopWaterTask();
		if (!isSalvation() && isOnSiegeField() && isCharmOfCourage())
		{
			ask(new ConfirmDlg(SystemMsg.YOUR_CHARM_OF_COURAGE_IS_TRYING_TO_RESURRECT_YOU, 60000), new ReviveAnswerListener(this, 100, false));
			setCharmOfCourage(false);
		}
		if (getLevel() < 6)
		{
			Quest q = QuestManager.getQuest(255);
			if (q != null)
			{
				processQuestEvent(q.getName(), "CE30", null);
			}
		}
		WorldStatisticsManager.getInstance().updateStat(this, CategoryType.DIE_COUNT, 1);
		if (killer != null && killer.getPlayer() == null)
		{
			WorldStatisticsManager.getInstance().updateStat(this, CategoryType.KILLED_BY_MONSTER_COUNT, 1);
		}
		super.onDeath(killer);
	}
	
	/**
	 * Method restoreExp.
	 */
	public void restoreExp()
	{
		restoreExp(100.);
	}
	
	/**
	 * Method restoreExp.
	 * @param percent double
	 */
	public void restoreExp(double percent)
	{
		if (percent == 0)
		{
			return;
		}
		int lostexp = 0;
		String lostexps = getVar("lostexp");
		if (lostexps != null)
		{
			lostexp = Integer.parseInt(lostexps);
			unsetVar("lostexp");
		}
		if (lostexp != 0)
		{
			addExpAndSp((long) ((lostexp * percent) / 100), 0);
		}
	}
	
	/**
	 * Method deathPenalty.
	 * @param killer Creature
	 */
	public void deathPenalty(Creature killer)
	{
		if (killer == null)
		{
			return;
		}
		final boolean atwar = (killer.getPlayer() != null) && atWarWith(killer.getPlayer());
		double deathPenaltyBonus = getDeathPenalty().getLevel(this) * Config.ALT_DEATH_PENALTY_C5_EXPERIENCE_PENALTY;
		if (deathPenaltyBonus < 2)
		{
			deathPenaltyBonus = 1;
		}
		else
		{
			deathPenaltyBonus = deathPenaltyBonus / 2;
		}
		double percentLost = 8.0;
		int level = getLevel();
		if (level >= 79)
		{
			percentLost = 1.0;
		}
		else if (level >= 78)
		{
			percentLost = 1.5;
		}
		else if (level >= 76)
		{
			percentLost = 2.0;
		}
		else if (level >= 40)
		{
			percentLost = 4.0;
		}
		if (Config.ALT_DEATH_PENALTY)
		{
			percentLost = (percentLost * Config.RATE_XP) + (_pkKills * Config.ALT_PK_DEATH_RATE);
		}
		if (atwar)
		{
			percentLost = percentLost / 4.0;
		}
		long lostexp = (int) Math.round(((Experience.LEVEL[level + 1] - Experience.LEVEL[level]) * percentLost) / 100);
		lostexp *= deathPenaltyBonus;
		lostexp = (int) calcStat(Stats.EXP_LOST, lostexp, killer, null);
		lostexp = Math.min(lostexp, getExp() - Experience.LEVEL[level]);
		if (isOnSiegeField())
		{
			SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
			if (siegeEvent != null)
			{
				lostexp = 0;
			}
			if (siegeEvent != null)
			{
				List<Effect> effect = getEffectList().getEffectsBySkillId(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
				if (effect != null)
				{
					int syndromeLvl = effect.get(0).getSkill().getLevel();
					if (syndromeLvl < 5)
					{
						getEffectList().stopEffect(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
						Skill skill = SkillTable.getInstance().getInfo(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME, syndromeLvl + 1);
						skill.getEffects(this, this, false, false);
					}
					else if (syndromeLvl == 5)
					{
						getEffectList().stopEffect(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
						Skill skill = SkillTable.getInstance().getInfo(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME, 5);
						skill.getEffects(this, this, false, false);
					}
				}
				else
				{
					Skill skill = SkillTable.getInstance().getInfo(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME, 1);
					if (skill != null)
					{
						skill.getEffects(this, this, false, false);
					}
				}
			}
		}
		long before = getExp();
		addExpAndSp(-lostexp, 0);
		long lost = before - getExp();
		if (lost > 0)
		{
			setVar("lostexp", String.valueOf(lost), -1);
		}
	}
	
	/**
	 * Method setRequest.
	 * @param transaction Request
	 */
	public void setRequest(Request transaction)
	{
		_request = transaction;
	}
	
	/**
	 * Method getRequest.
	 * @return Request
	 */
	public Request getRequest()
	{
		return _request;
	}
	
	/**
	 * Method isBusy.
	 * @return boolean
	 */
	public boolean isBusy()
	{
		return isProcessingRequest() || isOutOfControl() || isInOlympiadMode() || (getTeam() != TeamType.NONE) || isInStoreMode() || isInDuel() || getMessageRefusal() || isBlockAll() || isInvisible();
	}
	
	/**
	 * Method isProcessingRequest.
	 * @return boolean
	 */
	public boolean isProcessingRequest()
	{
		if (_request == null)
		{
			return false;
		}
		if (!_request.isInProgress())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isInTrade.
	 * @return boolean
	 */
	public boolean isInTrade()
	{
		return isProcessingRequest() && getRequest().isTypeOf(L2RequestType.TRADE);
	}
	
	/**
	 * Method addVisibleObject.
	 * @param object GameObject
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	public List<L2GameServerPacket> addVisibleObject(GameObject object, Creature dropper)
	{
		if (isLogoutStarted() || (object == null) || (object.getObjectId() == getObjectId()) || !object.isVisible())
		{
			return Collections.emptyList();
		}
		return object.addPacketList(this, dropper);
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (isInvisible() && (forPlayer.getObjectId() != getObjectId()))
		{
			return Collections.emptyList();
		}
		if ((getPrivateStoreType() != STORE_PRIVATE_NONE) && forPlayer.getVarB("notraders"))
		{
			return Collections.emptyList();
		}
		if (isInObserverMode() && (getCurrentRegion() != getObserverRegion()) && (getObserverRegion() == forPlayer.getCurrentRegion()))
		{
			return Collections.emptyList();
		}
		List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>();
		if (forPlayer.getObjectId() != getObjectId())
		{
			list.add(isPolymorphed() ? new NpcInfoPoly(this) : new CharInfo(this));
		}
		list.add(new ExBR_ExtraUserInfo(this));
		if (isSitting() && (_sittingObject != null))
		{
			list.add(new ChairSit(this, _sittingObject));
		}
		if (getPrivateStoreType() != STORE_PRIVATE_NONE)
		{
			if (getPrivateStoreType() == STORE_PRIVATE_BUY)
			{
				list.add(new PrivateStoreMsgBuy(this));
			}
			else if ((getPrivateStoreType() == STORE_PRIVATE_SELL) || (getPrivateStoreType() == STORE_PRIVATE_SELL_PACKAGE))
			{
				list.add(new PrivateStoreMsgSell(this));
			}
			else if (getPrivateStoreType() == STORE_PRIVATE_MANUFACTURE)
			{
				list.add(new RecipeShopMsg(this));
			}
			if (forPlayer.isInZonePeace())
			{
				return list;
			}
		}
		if (isDoubleCastingNow())
		{
			Creature castingTarget = getCastingTarget();
			Skill castingSkill = getCastingSkill();
			long animationEndTime = getAnimationEndTime();
			if ((castingSkill != null) && (castingTarget != null) && castingTarget.isCreature() && (getAnimationEndTime() > 0))
			{
				list.add(new MagicSkillUse(this, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0L, isDoubleCastingNow()));
			}
		}
		else if (isCastingNow())
		{
			Creature castingTarget = getCastingTarget();
			Skill castingSkill = getCastingSkill();
			long animationEndTime = getAnimationEndTime();
			if ((castingSkill != null) && (castingTarget != null) && castingTarget.isCreature() && (getAnimationEndTime() > 0))
			{
				list.add(new MagicSkillUse(this, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0L, isDoubleCastingNow()));
			}
		}
		if (isInCombat())
		{
			list.add(new AutoAttackStart(getObjectId()));
		}
		list.add(RelationChanged.update(forPlayer, this, forPlayer));
		if (isInBoat())
		{
			list.add(getBoat().getOnPacket(this, getInBoatPosition()));
		}
		else
		{
			if (isMoving || isFollow)
			{
				list.add(movePacket());
			}
		}
		return list;
	}
	
	/**
	 * Method removeVisibleObject.
	 * @param object GameObject
	 * @param list List<L2GameServerPacket>
	 * @return List<L2GameServerPacket>
	 */
	public List<L2GameServerPacket> removeVisibleObject(GameObject object, List<L2GameServerPacket> list)
	{
		if (isLogoutStarted() || (object == null) || (object.getObjectId() == getObjectId()))
		{
			return null;
		}
		List<L2GameServerPacket> result = list == null ? object.deletePacketList() : list;
		getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT, object);
		return result;
	}
	
	/**
	 * Method levelSet.
	 * @param levels int
	 */
	private void levelSet(int levels)
	{
		if (levels > 0)
		{
			sendPacket(Msg.YOU_HAVE_INCREASED_YOUR_LEVEL);
			broadcastPacket(new SocialAction(getObjectId(), SocialAction.LEVEL_UP));
			setCurrentHpMp(getMaxHp(), getMaxMp());
			setCurrentCp(getMaxCp());
			Quest q = QuestManager.getQuest(255);
			if (q != null)
			{
				processQuestEvent(q.getName(), "CE40", null);
			}
			for (SkillLearn skillLearn : SkillAcquireHolder.getInstance().getAvailableSkills(this, AcquireType.NORMAL))
			{
				if (getKnownSkill(skillLearn.getId()) == null)
				{
					sendPacket(new ExNewSkillToLearnByLevelUp());
					break;
				}
			}
			int mentorId = getMenteeMentorList().getMentor();
			if (mentorId != 0)
			{
				String mentorName = getMenteeMentorList().getList().get(mentorId).getName();
				Player mentorPlayer = World.getPlayer(mentorName);
				if (getMenteeMentorList().someOneOnline(true) && getLevel() != 86)
				{
					Mentoring.applyMenteeBuffs(this);
					Mentoring.applyMentorBuffs(mentorPlayer);
				}
				if (mentorPlayer != null)
				{
					if (Mentoring.SIGN_OF_TUTOR.containsKey(getLevel()))
					{
						Map<Integer, Long> signOfTutor = new HashMap<Integer, Long>()
						{
							/**
	 * 
	 */
							private static final long serialVersionUID = 1L;
							
							{
								put(33804, (long) Mentoring.SIGN_OF_TUTOR.get(getLevel()));
							}
						};
						Mentoring.sendMentorMail(mentorPlayer, signOfTutor);
					}
				}
				if (getLevel() >= 86)
				{
					sendPacket(new SystemMessage2(SystemMsg.YOU_REACHED_LEVEL_86_RELATIONSHIP_WITH_S1_CAME_TO_AN_END).addString(mentorName));
					getMenteeMentorList().remove(mentorName, false, false);
					sendPacket(new ExMentorList(this));
					if (mentorPlayer != null)
					{
						if (mentorPlayer.isOnline())
						{
							mentorPlayer.sendPacket(new SystemMessage2(SystemMsg.THE_MENTEE_S1_HAS_REACHED_LEVEL_86).addName(this));
							mentorPlayer.getMenteeMentorList().remove(_name, true, false);
							mentorPlayer.sendPacket(new ExMentorList(mentorPlayer));
						}
						Mentoring.setTimePenalty(mentorId, System.currentTimeMillis() + (1 * 24 * 3600 * 1000L), -1);
					}
					Mentoring.cancelMenteeBuffs(this);
					Mentoring.cancelMentorBuffs(mentorPlayer);
				}
			}
		}
		else if (levels < 0)
		{
			if (Config.ALT_REMOVE_SKILLS_ON_DELEVEL)
			{
				checkSkills();
			}
		}
		if (isInParty())
		{
			getParty().recalculatePartyData();
		}
		if (_clan != null)
		{
			_clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(this));
		}
		if (_matchingRoom != null)
		{
			_matchingRoom.broadcastPlayerUpdate(this);
		}
		rewardSkills(true,false);
	}
	
	/**
	 * Method checkSkills.
	 */
	public void checkSkills()
	{
		for (Skill sk : getAllSkillsArray())
		{
			SkillTreeTable.checkSkill(this, sk);
		}
	}
	
	/**
	 * Method startTimers.
	 */
	public void startTimers()
	{
		startAutoSaveTask();
		startPcBangPointsTask();
		startBonusTask();
		getInventory().startTimers();
		resumeQuestTimers();
	}
	
	/**
	 * Method stopAllTimers.
	 */
	public void stopAllTimers()
	{
		setAgathion(0);
		stopWaterTask();
		stopBonusTask();
		stopHourlyTask();
		stopKickTask();
		stopPcBangPointsTask();
		stopAutoSaveTask();
		stopRecomBonusTask(true);
		getInventory().stopAllTimers();
		stopQuestTimers();
	}
	
	/**
	 * Method getSummonPointMax.
	 * @return int
	 */
	public int getSummonPointMax()
	{
		return (int) calcStat(Stats.SUMMON_POINT, 0);
	}
	
	/**
	 * Method scheduleDelete.
	 */
	public void scheduleDelete()
	{
		long time = 0L;
		if (Config.SERVICES_ENABLE_NO_CARRIER)
		{
			time = NumberUtils.toInt(getVar("noCarrier"), Config.SERVICES_NO_CARRIER_DEFAULT_TIME);
		}
		scheduleDelete(time * 1000L);
	}
	
	/**
	 * Method scheduleDelete.
	 * @param time long
	 */
	public void scheduleDelete(long time)
	{
		if (isLogoutStarted() || isInOfflineMode())
		{
			return;
		}
		broadcastCharInfo();
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!isConnected())
				{
					prepareToLogout();
					deleteMe();
				}
			}
		}, time);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		super.onDelete();
		WorldRegion observerRegion = getObserverRegion();
		if (observerRegion != null)
		{
			observerRegion.removeObject(this);
		}
		_friendList.notifyFriends(false);
		bookmarks.clear();
		_inventory.clear();
		_warehouse.clear();
		_summons = null;
		_arrowItem = null;
		_fistsWeaponItem = null;
		_chars = null;
		_enchantScroll = null;
		_enchantItem = null;
		_enchantSupportItem = null;
		_lastNpc = HardReferences.emptyRef();
		_observerRegion = null;
	}
	
	/**
	 * Method setTradeList.
	 * @param list List<TradeItem>
	 */
	public void setTradeList(List<TradeItem> list)
	{
		_tradeList = list;
	}
	
	/**
	 * Method getTradeList.
	 * @return List<TradeItem>
	 */
	public List<TradeItem> getTradeList()
	{
		return _tradeList;
	}
	
	/**
	 * Method getSellStoreName.
	 * @return String
	 */
	public String getSellStoreName()
	{
		return _sellStoreName;
	}
	
	/**
	 * Method setSellStoreName.
	 * @param name String
	 */
	public void setSellStoreName(String name)
	{
		_sellStoreName = Strings.stripToSingleLine(name);
	}
	
	/**
	 * Method setSellList.
	 * @param packageSell boolean
	 * @param list List<TradeItem>
	 */
	public void setSellList(boolean packageSell, List<TradeItem> list)
	{
		if (packageSell)
		{
			_packageSellList = list;
		}
		else
		{
			_sellList = list;
		}
	}
	
	/**
	 * Method getSellList.
	 * @return List<TradeItem>
	 */
	public List<TradeItem> getSellList()
	{
		return getSellList(_privatestore == STORE_PRIVATE_SELL_PACKAGE);
	}
	
	/**
	 * Method getSellList.
	 * @param packageSell boolean
	 * @return List<TradeItem>
	 */
	public List<TradeItem> getSellList(boolean packageSell)
	{
		return packageSell ? _packageSellList : _sellList;
	}
	
	/**
	 * Method getBuyStoreName.
	 * @return String
	 */
	public String getBuyStoreName()
	{
		return _buyStoreName;
	}
	
	/**
	 * Method setBuyStoreName.
	 * @param name String
	 */
	public void setBuyStoreName(String name)
	{
		_buyStoreName = Strings.stripToSingleLine(name);
	}
	
	/**
	 * Method setBuyList.
	 * @param list List<TradeItem>
	 */
	public void setBuyList(List<TradeItem> list)
	{
		_buyList = list;
	}
	
	/**
	 * Method getBuyList.
	 * @return List<TradeItem>
	 */
	public List<TradeItem> getBuyList()
	{
		return _buyList;
	}
	
	/**
	 * Method setManufactureName.
	 * @param name String
	 */
	public void setManufactureName(String name)
	{
		_manufactureName = Strings.stripToSingleLine(name);
	}
	
	/**
	 * Method getManufactureName.
	 * @return String
	 */
	public String getManufactureName()
	{
		return _manufactureName;
	}
	
	/**
	 * Method getCreateList.
	 * @return List<ManufactureItem>
	 */
	public List<ManufactureItem> getCreateList()
	{
		return _createList;
	}
	
	/**
	 * Method setCreateList.
	 * @param list List<ManufactureItem>
	 */
	public void setCreateList(List<ManufactureItem> list)
	{
		_createList = list;
	}
	
	/**
	 * Method setPrivateStoreType.
	 * @param type int
	 */
	public void setPrivateStoreType(final int type)
	{
		_privatestore = type;
		if (type != STORE_PRIVATE_NONE)
		{
			setVar("storemode", String.valueOf(type), -1);
		}
		else
		{
			unsetVar("storemode");
		}
	}
	
	/**
	 * Method isInStoreMode.
	 * @return boolean
	 */
	public boolean isInStoreMode()
	{
		return _privatestore != STORE_PRIVATE_NONE;
	}
	
	/**
	 * Method getPrivateStoreType.
	 * @return int
	 */
	public int getPrivateStoreType()
	{
		return _privatestore;
	}
	
	/**
	 * Method setClan.
	 * @param clan Clan
	 */
	public void setClan(Clan clan)
	{
		if ((_clan != clan) && (_clan != null))
		{
			unsetVar("canWhWithdraw");
		}
		Clan oldClan = _clan;
		if ((oldClan != null) && (clan == null))
		{
			for (Skill skill : oldClan.getAllSkills())
			{
				removeSkill(skill, false);
			}
		}
		_clan = clan;
		if (clan == null)
		{
			_pledgeType = Clan.SUBUNIT_NONE;
			_pledgeClass = 0;
			_powerGrade = 0;
			_apprentice = 0;
			getInventory().validateItems();
			return;
		}
		if (!clan.isAnyMember(getObjectId()))
		{
			setClan(null);
			if (!isNoble())
			{
				setTitle("");
			}
		}
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return _clan;
	}
	
	/**
	 * Method getSubUnit.
	 * @return SubUnit
	 */
	public SubUnit getSubUnit()
	{
		return _clan == null ? null : _clan.getSubUnit(_pledgeType);
	}
	
	/**
	 * Method getClanHall.
	 * @return ClanHall
	 */
	public ClanHall getClanHall()
	{
		int id = _clan != null ? _clan.getHasHideout() : 0;
		return ResidenceHolder.getInstance().getResidence(ClanHall.class, id);
	}
	
	/**
	 * Method getCastle.
	 * @return Castle
	 */
	public Castle getCastle()
	{
		int id = _clan != null ? _clan.getCastle() : 0;
		return ResidenceHolder.getInstance().getResidence(Castle.class, id);
	}
	
	/**
	 * Method getFortress.
	 * @return Fortress
	 */
	public Fortress getFortress()
	{
		int id = _clan != null ? _clan.getHasFortress() : 0;
		return ResidenceHolder.getInstance().getResidence(Fortress.class, id);
	}
	
	/**
	 * Method getAlliance.
	 * @return Alliance
	 */
	public Alliance getAlliance()
	{
		return _clan == null ? null : _clan.getAlliance();
	}
	
	/**
	 * Method isClanLeader.
	 * @return boolean
	 */
	public boolean isClanLeader()
	{
		return (_clan != null) && (getObjectId() == _clan.getLeaderId());
	}
	
	/**
	 * Method isAllyLeader.
	 * @return boolean
	 */
	public boolean isAllyLeader()
	{
		return (getAlliance() != null) && (getAlliance().getLeader().getLeaderId() == getObjectId());
	}
	
	/**
	 * Method reduceArrowCount.
	 */
	@Override
	public void reduceArrowCount()
	{
		ItemInstance newItem = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if (newItem.getItemType() == EtcItemType.UNLIMITED_ARROW)
		{
			return;
		}
		sendPacket(SystemMsg.YOU_CAREFULLY_NOCK_AN_ARROW);
		if (!getInventory().destroyItemByObjectId(getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND), 1L))
		{
			getInventory().setPaperdollItem(Inventory.PAPERDOLL_LHAND, null);
			_arrowItem = null;
		}
	}
	
	/**
	 * Method checkAndEquipArrows.
	 * @return boolean
	 */
	protected boolean checkAndEquipArrows()
	{
		if (getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND) == null)
		{
			ItemInstance activeWeapon = getActiveWeaponInstance();
			if (activeWeapon != null)
			{
				if (activeWeapon.getItemType() == WeaponType.BOW)
				{
					_arrowItem = getInventory().findArrowForBow(activeWeapon.getTemplate());
				}
				else if (activeWeapon.getItemType() == WeaponType.CROSSBOW)
				{
					getInventory().findArrowForCrossbow(activeWeapon.getTemplate());
				}
			}
			if (_arrowItem != null)
			{
				getInventory().setPaperdollItem(Inventory.PAPERDOLL_LHAND, _arrowItem);
			}
		}
		else
		{
			_arrowItem = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		}
		return _arrowItem != null;
	}
	
	/**
	 * Method setUptime.
	 * @param time long
	 */
	public void setUptime(final long time)
	{
		_uptime = time;
	}
	
	/**
	 * Method getUptime.
	 * @return long
	 */
	public long getUptime()
	{
		return System.currentTimeMillis() - _uptime;
	}
	
	/**
	 * Method isInParty.
	 * @return boolean
	 */
	public boolean isInParty()
	{
		return _party != null;
	}
	
	/**
	 * Method setParty.
	 * @param party Party
	 */
	public void setParty(final Party party)
	{
		_party = party;
	}
	
	/**
	 * Method joinParty.
	 * @param party Party
	 */
	public void joinParty(final Party party)
	{
		if (party != null)
		{
			party.addPartyMember(this);
		}
	}
	
	/**
	 * Method leaveParty.
	 */
	public void leaveParty()
	{
		if (isInParty())
		{
			_party.removePartyMember(this, false);
		}
	}
	
	/**
	 * Method getParty.
	 * @return Party
	 */
	public Party getParty()
	{
		return _party;
	}
	
	/**
	 * Method setLastPartyPosition.
	 * @param loc Location
	 */
	public void setLastPartyPosition(Location loc)
	{
		_lastPartyPosition = loc;
	}
	
	/**
	 * Method getLastPartyPosition.
	 * @return Location
	 */
	public Location getLastPartyPosition()
	{
		return _lastPartyPosition;
	}
	
	/**
	 * Method isGM.
	 * @return boolean
	 */
	public boolean isGM()
	{
		return _playerAccess == null ? false : _playerAccess.IsGM;
	}
	
	/**
	 * Method setAccessLevel.
	 * @param level int
	 */
	public void setAccessLevel(final int level)
	{
		_accessLevel = level;
	}
	
	/**
	 * Method getAccessLevel.
	 * @return int
	 */
	@Override
	public int getAccessLevel()
	{
		return _accessLevel;
	}
	
	/**
	 * Method setPlayerAccess.
	 * @param pa PlayerAccess
	 */
	public void setPlayerAccess(final PlayerAccess pa)
	{
		if (pa != null)
		{
			_playerAccess = pa;
		}
		else
		{
			_playerAccess = new PlayerAccess();
		}
		setAccessLevel(isGM() || _playerAccess.Menu ? 100 : 0);
	}
	
	/**
	 * Method getPlayerAccess.
	 * @return PlayerAccess
	 */
	public PlayerAccess getPlayerAccess()
	{
		return _playerAccess;
	}
	
	/**
	 * Method getLevelMod.
	 * @return double
	 */
	@Override
	public double getLevelMod()
	{
		return (89. + getLevel()) / 100.0;
	}
	
	/**
	 * Method updateStats.
	 */
	@Override
	public void updateStats()
	{
		if (entering || isLogoutStarted())
		{
			return;
		}
		refreshOverloaded();
		refreshExpertisePenalty();
		super.updateStats();
	}
	
	/**
	 * Method sendChanges.
	 */
	@Override
	public void sendChanges()
	{
		if (entering || isLogoutStarted())
		{
			return;
		}
		super.sendChanges();
	}
	
	/**
	 * Method updateKarma.
	 * @param flagChanged boolean
	 */
	public void updateKarma(boolean flagChanged)
	{
		sendStatusUpdate(true, true, StatusUpdateField.KARMA);

		if (flagChanged)
		{
			broadcastRelationChanged();
		}
	}
	
	/**
	 * Method isOnline.
	 * @return boolean
	 */
	public boolean isOnline()
	{
		return _isOnline;
	}
	
	/**
	 * Method setIsOnline.
	 * @param isOnline boolean
	 */
	public void setIsOnline(boolean isOnline)
	{
		_isOnline = isOnline;
	}
	
	/**
	 * Method setOnlineStatus.
	 * @param isOnline boolean
	 */
	public void setOnlineStatus(boolean isOnline)
	{
		_isOnline = isOnline;
		updateOnlineStatus();
	}
	
	/**
	 * Method updateOnlineStatus.
	 */
	private void updateOnlineStatus()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET online=?, lastAccess=? WHERE obj_id=?");
			statement.setInt(1, isOnline() && !isInOfflineMode() ? 1 : 0);
			statement.setLong(2, System.currentTimeMillis() / 1000L);
			statement.setInt(3, getObjectId());
			statement.execute();
		}
		catch (final Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method increaseKarma.
	 * @param add_karma long
	 */
	public void increaseKarma(final long add_karma)
	{
		long new_karma = _karma + add_karma;
		if (new_karma > Integer.MAX_VALUE)
		{
			new_karma = Integer.MAX_VALUE;
		}
		if ((_karma == 0) && (new_karma < 0))
		{
			if (_pvpFlag > 0)
			{
				_pvpFlag = 0;
				if (_PvPRegTask != null)
				{
					_PvPRegTask.cancel(true);
					_PvPRegTask = null;
				}
				sendStatusUpdate(true, true, StatusUpdateField.PVP_FLAG);
			}
			_karma = (int) new_karma;
		}
		else
		{
			_karma = (int) new_karma;
		}
		updateKarma(_karma == 0);
	}
	
	/**
	 * Method decreaseKarma.
	 * @param i int
	 */
	public void decreaseKarma(final int i)
	{
		_karma -= i;
		updateKarma(_karma > 0);
	}
	
	/**
	 * Method create.
	 * @param classId int
	 * @param sex int
	 * @param accountName String
	 * @param name String
	 * @param hairStyle int
	 * @param hairColor int
	 * @param face int
	 * @return Player
	 */
	public static Player create(int classId, int sex, String accountName, final String name, final int hairStyle, final int hairColor, final int face)
	{
		ClassId class_id = ClassId.VALUES[classId];
		PlayerTemplate template = PlayerTemplateHolder.getInstance().getPlayerTemplate(class_id.getRace(), class_id, Sex.VALUES[sex]);
		Player player = new Player(IdFactory.getInstance().getNextId(), template, accountName);
		player.setName(name);
		player.setTitle("");
		player.setHairStyle(hairStyle);
		player.setHairColor(hairColor);
		player.setFace(face);
		player.setCreateTime(System.currentTimeMillis());
		if (!CharacterDAO.getInstance().insert(player))
		{
			return null;
		}
		int level = 1;
		double hp = 0;
		double mp = 0;
		double cp = 0;
		long exp = Experience.getExpForLevel(level);
		int sp = 0;
		boolean active = true;
		SubClassType type = SubClassType.BASE_CLASS;
		if (!CharacterSubclassDAO.getInstance().insert(player.getObjectId(), classId, classId, exp, sp, hp, mp, cp, hp, mp, cp, level, active, type, null, 0, 0))
		{
			return null;
		}
		return player;
	}
	
	/**
	 * Method restore.
	 * @param objectId int
	 * @return Player
	 */
	public static Player restore(final int objectId)
	{
		Player player = null;
		Connection con = null;
		Statement statement = null;
		Statement statement2 = null;
		PreparedStatement statement3 = null;
		ResultSet rset = null;
		ResultSet rset2 = null;
		ResultSet rset3 = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			statement2 = con.createStatement();
			rset = statement.executeQuery("SELECT * FROM `characters` WHERE `obj_Id`=" + objectId + " LIMIT 1");
			rset2 = statement2.executeQuery("SELECT `class_id`, `default_class_id` FROM `character_subclasses` WHERE `char_obj_id`=" + objectId + " AND `type`=" + SubClassType.BASE_CLASS.ordinal() + " LIMIT 1");
			if (rset.next() && rset2.next())
			{
				final ClassId classId = ClassId.VALUES[rset2.getInt("class_id")];
				final ClassId defaultClassId = ClassId.VALUES[rset2.getInt("default_class_id")];
				final PlayerTemplate template = PlayerTemplateHolder.getInstance().getPlayerTemplate(defaultClassId.getRace(), classId, Sex.VALUES[rset.getInt("sex")]);
				player = new Player(objectId, template);
				player.loadVariables();
				player.loadInstanceReuses();
				player.loadPremiumItemList();
				player.bookmarks.setCapacity(rset.getInt("bookmarks"));
				player.bookmarks.restore();
				player._friendList.restore();
				player._postFriends = CharacterPostFriendDAO.getInstance().select(player);
				CharacterGroupReuseDAO.getInstance().select(player);
				player._login = rset.getString("account_name");
				player.setName(rset.getString("char_name"));
				player.setFace(rset.getInt("face"));
				player.setHairStyle(rset.getInt("hairStyle"));
				player.setHairColor(rset.getInt("hairColor"));
				player.setHeading(0);
				player.setKarma(rset.getInt("karma"));
				player.setPvpKills(rset.getInt("pvpkills"));
				player.setPkKills(rset.getInt("pkkills"));
				player.setLeaveClanTime(rset.getLong("leaveclan") * 1000L);
				if ((player.getLeaveClanTime() > 0) && player.canJoinClan())
				{
					player.setLeaveClanTime(0);
				}
				player.setDeleteClanTime(rset.getLong("deleteclan") * 1000L);
				if ((player.getDeleteClanTime() > 0) && player.canCreateClan())
				{
					player.setDeleteClanTime(0);
				}
				player.setNoChannel(rset.getLong("nochannel") * 1000L);
				if ((player.getNoChannel() > 0) && (player.getNoChannelRemained() < 0))
				{
					player.setNoChannel(0);
				}
				player.setOnlineTime(rset.getLong("onlinetime") * 1000L);
				final int clanId = rset.getInt("clanid");
				if (clanId > 0)
				{
					player.setClan(ClanTable.getInstance().getClan(clanId));
					player.setPledgeType(rset.getInt("pledge_type"));
					player.setPowerGrade(rset.getInt("pledge_rank"));
					player.setLvlJoinedAcademy(rset.getInt("lvl_joined_academy"));
					player.setApprentice(rset.getInt("apprentice"));
				}
				player.setCreateTime(rset.getLong("createtime") * 1000L);
				player.setDeleteTimer(rset.getInt("deletetime"));
				player.setTitle(rset.getString("title"));
				if (player.getVar("titlecolor") != null)
				{
					player.setTitleColor(Integer.decode("0x" + player.getVar("titlecolor")));
				}
				if (player.getVar("namecolor") == null)
				{
					if (player.isGM())
					{
						player.setNameColor(Config.GM_NAME_COLOUR);
					}
					else if ((player.getClan() != null) && (player.getClan().getLeaderId() == player.getObjectId()))
					{
						player.setNameColor(Config.CLANLEADER_NAME_COLOUR);
					}
					else
					{
						player.setNameColor(Config.NORMAL_NAME_COLOUR);
					}
				}
				else
				{
					player.setNameColor(Integer.decode("0x" + player.getVar("namecolor")));
				}
				if (Config.AUTO_LOOT_INDIVIDUAL)
				{
					player._autoLoot = player.getVarB("AutoLoot", Config.AUTO_LOOT);
					player.AutoLootHerbs = player.getVarB("AutoLootHerbs", Config.AUTO_LOOT_HERBS);
				}
				player.setUptime(System.currentTimeMillis());
				player.setLastAccess(rset.getLong("lastAccess"));
				player.setRecomHave(rset.getInt("rec_have"));
				player.setRecomLeft(rset.getInt("rec_left"));
				player.setRecomBonusTime(rset.getInt("rec_bonus_time"));
				if (player.getVar("recLeftToday") != null)
				{
					player.setRecomLeftToday(Integer.parseInt(player.getVar("recLeftToday")));
				}
				else
				{
					player.setRecomLeftToday(0);
				}
				player.setKeyBindings(rset.getBytes("key_bindings"));
				player.setPcBangPoints(rset.getInt("pcBangPoints"));
				player.setFame(rset.getInt("fame"), null);
				player.restoreRecipeBook();
				if (Config.ENABLE_OLYMPIAD)
				{
					player.setHero(Hero.getInstance().isHero(player.getObjectId()));
					player.setNoble(Olympiad.isNoble(player.getObjectId()));
				}
				player.updatePledgeClass();
				int reflection = 0;
				if ((player.getVar("jailed") != null) && ((System.currentTimeMillis() / 1000) < (Integer.parseInt(player.getVar("jailed")) + 60)))
				{
					player.setXYZ(-114648, -249384, -2984);
					player.sitDown(null);
					player.block();
					player._unjailTask = ThreadPoolManager.getInstance().schedule(new UnJailTask(player), Integer.parseInt(player.getVar("jailed")) * 1000L);
				}
				else
				{
					player.setXYZ(rset.getInt("x"), rset.getInt("y"), rset.getInt("z"));
					String jumpSafeLoc = player.getVar("@safe_jump_loc");
					if (jumpSafeLoc != null)
					{
						player.setLoc(Location.parseLoc(jumpSafeLoc));
						player.unsetVar("@safe_jump_loc");
					}
					String ref = player.getVar("reflection");
					if (ref != null)
					{
						reflection = Integer.parseInt(ref);
						if (reflection > 0)
						{
							String back = player.getVar("backCoords");
							if (back != null)
							{
								player.setLoc(Location.parseLoc(back));
								player.unsetVar("backCoords");
							}
							reflection = 0;
						}
					}
				}
				player.setReflection(reflection);
				EventHolder.getInstance().findEvent(player);
				Quest.restoreQuestStates(player);
				player.getSubClassList().restore();
				player.setActiveSubClass(player.getActiveClassId(), false, 0);
				player.restoreVitality();
				player.getInventory().restore();
				player._menteeMentorList.restore();
				try
				{
					String var = player.getVar("ExpandInventory");
					if (var != null)
					{
						player.setExpandInventory(Integer.parseInt(var));
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
				try
				{
					String var = player.getVar("ExpandWarehouse");
					if (var != null)
					{
						player.setExpandWarehouse(Integer.parseInt(var));
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
				try
				{
					String var = player.getVar(NO_ANIMATION_OF_CAST_VAR);
					if (var != null)
					{
						player.setNotShowBuffAnim(Boolean.parseBoolean(var));
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
				try
				{
					String var = player.getVar(NO_TRADERS_VAR);
					if (var != null)
					{
						player.setNotShowTraders(Boolean.parseBoolean(var));
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
				try
				{
					String var = player.getVar("pet");
					if (var != null)
					{
						player.setPetControlItem(Integer.parseInt(var));
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
				statement3 = con.prepareStatement("SELECT obj_Id, char_name FROM characters WHERE account_name=? AND obj_Id!=?");
				statement3.setString(1, player._login);
				statement3.setInt(2, objectId);
				rset3 = statement3.executeQuery();
				while (rset3.next())
				{
					final Integer charId = rset3.getInt("obj_Id");
					final String charName = rset3.getString("char_name");
					player._chars.put(charId, charName);
				}
				DbUtils.close(statement3, rset3);
				{
					List<Zone> zones = new ArrayList<Zone>();
					World.getZones(zones, player.getLoc(), player.getReflection());
					if (!zones.isEmpty())
					{
						for (Zone zone : zones)
						{
							if (zone.getType() == ZoneType.no_restart)
							{
								if (((System.currentTimeMillis() / 1000L) - player.getLastAccess()) > zone.getRestartTime())
								{
									player.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.EnterWorld.TeleportedReasonNoRestart", player));
									player.setLoc(TeleportUtils.getRestartLocation(player, RestartType.TO_VILLAGE));
								}
							}
							else if (zone.getType() == ZoneType.SIEGE)
							{
								SiegeEvent<?, ?> siegeEvent = player.getEvent(SiegeEvent.class);
								if (siegeEvent != null)
								{
									player.setLoc(siegeEvent.getEnterLoc(player));
								}
								else
								{
									Residence r = ResidenceHolder.getInstance().getResidence(zone.getParams().getInteger("residence"));
									player.setLoc(r.getNotOwnerRestartPoint(player));
								}
							}
						}
					}
					zones.clear();
				}
				player.restoreBlockList();
				player._macroses.restore();
				player.refreshExpertisePenalty();
				player.refreshOverloaded();
				player.getWarehouse().restore();
				player.getFreight().restore();
				player.restoreTradeList();
				if (player.getVar("storemode") != null)
				{
					player.setPrivateStoreType(Integer.parseInt(player.getVar("storemode")));
					player.setSitting(true);
				}
				player.updateKetraVarka();
				player.updateRam();
				player.checkRecom();
				player.restoreVitality();
				player.getSummonList().restore();
			}
		}
		catch (final Exception e)
		{
			_log.error("Could not restore char data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(statement2, rset2);
			DbUtils.closeQuietly(statement3, rset3);
			DbUtils.closeQuietly(con, statement, rset);
		}
		return player;
	}
	
	/**
	 * Method loadPremiumItemList.
	 */
	private void loadPremiumItemList()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT itemNum, itemId, itemCount, itemSender FROM character_premium_items WHERE charId=?");
			statement.setInt(1, getObjectId());
			rs = statement.executeQuery();
			while (rs.next())
			{
				int itemNum = rs.getInt("itemNum");
				int itemId = rs.getInt("itemId");
				long itemCount = rs.getLong("itemCount");
				String itemSender = rs.getString("itemSender");
				PremiumItem item = new PremiumItem(itemId, itemCount, itemSender);
				_premiumItems.put(itemNum, item);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method updatePremiumItem.
	 * @param itemNum int
	 * @param newcount long
	 */
	public void updatePremiumItem(int itemNum, long newcount)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE character_premium_items SET itemCount=? WHERE charId=? AND itemNum=?");
			statement.setLong(1, newcount);
			statement.setInt(2, getObjectId());
			statement.setInt(3, itemNum);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deletePremiumItem.
	 * @param itemNum int
	 */
	public void deletePremiumItem(int itemNum)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_premium_items WHERE charId=? AND itemNum=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, itemNum);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getPremiumItemList.
	 * @return Map<Integer,PremiumItem>
	 */
	public Map<Integer, PremiumItem> getPremiumItemList()
	{
		return _premiumItems;
	}
	
	/**
	 * Method store.
	 * @param fast boolean
	 */
	public void store(boolean fast)
	{
		if (!_storeLock.tryLock())
		{
			return;
		}
		try
		{
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("UPDATE characters SET face=?,hairStyle=?,hairColor=?,sex=?,x=?,y=?,z=?,karma=?,pvpkills=?,pkkills=?,rec_have=?,rec_left=?,rec_bonus_time=?,clanid=?,deletetime=?,title=?,accesslevel=?,online=?,leaveclan=?,deleteclan=?,nochannel=?,onlinetime=?,pledge_type=?,pledge_rank=?,lvl_joined_academy=?,apprentice=?,key_bindings=?,pcBangPoints=?,char_name=?,fame=?,bookmarks=? WHERE obj_Id=? LIMIT 1");
				statement.setInt(1, getFace());
				statement.setInt(2, getHairStyle());
				statement.setInt(3, getHairColor());
				statement.setInt(4, getSex());
				if (_stablePoint == null)
				{
					statement.setInt(5, getX());
					statement.setInt(6, getY());
					statement.setInt(7, getZ());
				}
				else
				{
					statement.setInt(5, _stablePoint.x);
					statement.setInt(6, _stablePoint.y);
					statement.setInt(7, _stablePoint.z);
				}
				statement.setInt(8, getKarma());
				statement.setInt(9, getPvpKills());
				statement.setInt(10, getPkKills());
				statement.setInt(11, getRecomHave());
				statement.setInt(12, getRecomLeft());
				statement.setInt(13, getRecomBonusTime());
				statement.setInt(14, getClanId());
				statement.setInt(15, getDeleteTimer());
				statement.setString(16, _title);
				statement.setInt(17, _accessLevel);
				statement.setInt(18, isOnline() && !isInOfflineMode() ? 1 : 0);
				statement.setLong(19, getLeaveClanTime() / 1000L);
				statement.setLong(20, getDeleteClanTime() / 1000L);
				statement.setLong(21, _NoChannel > 0 ? getNoChannelRemained() / 1000 : _NoChannel);
				statement.setInt(22, (int) (_onlineBeginTime > 0 ? ((_onlineTime + System.currentTimeMillis()) - _onlineBeginTime) / 1000L : _onlineTime / 1000L));
				
				if (_onlineBeginTime > 0L)
					WorldStatisticsManager.getInstance().updateStat(this, CategoryType.TIME_PLAYED, (System.currentTimeMillis() - _onlineBeginTime) / 1000);

				statement.setInt(23, getPledgeType());
				statement.setInt(24, getPowerGrade());
				statement.setInt(25, getLvlJoinedAcademy());
				statement.setInt(26, getApprentice());
				statement.setBytes(27, getKeyBindings());
				statement.setInt(28, getPcBangPoints());
				statement.setString(29, getName());
				statement.setInt(30, getFame());
				statement.setInt(31, bookmarks.getCapacity());
				statement.setInt(32, getObjectId());
				statement.executeUpdate();
				GameStats.increaseUpdatePlayerBase();
				if (!fast)
				{
					EffectsDAO.getInstance().insert(this);
					CharacterGroupReuseDAO.getInstance().insert(this);
					storeDisableSkills();
					storeBlockList();
				}
				storeCharSubClasses();
				bookmarks.store();
				DbUtils.closeQuietly(con, statement);
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("UPDATE `vitality_points` SET `points`=? WHERE `account_name`=?");
				statement.setInt(1, getVitality());
				statement.setString(2, getAccountName());
				statement.execute();
			}
			catch (Exception e)
			{
				_log.error("Could not store char data: " + this + "!", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
		finally
		{
			_storeLock.unlock();
		}
	}
	
	/**
	 * Method addCertSkill.
	 * @param newSkill Skill
	 * @param store boolean
	 * @return Skill
	 */
	public Skill addCertSkill(final Skill newSkill, final boolean isDual)
	{
		if (newSkill == null)
		{
			return null;
		}
		Skill oldSkill = super.addSkill(newSkill);
		if (newSkill.equals(oldSkill))
		{
			return oldSkill;
		}
		storeCertSkill(newSkill, oldSkill,isDual);
		return oldSkill;
	}
	
	/**
	 * Method addSkill.
	 * @param newSkill Skill
	 * @param store boolean
	 * @return Skill
	 */
	public Skill addSkill(final Skill newSkill, final boolean store)
	{
		if (newSkill == null)
		{
			return null;
		}
		if (newSkill.isRelationSkill())
		{
			//New Method
			List<Integer> lst = Arrays.asList(ArrayUtils.toObject(newSkill.getRelationSkills()));
			removeSkills(lst, true);
			/*
			//Old Method
			int[] _ss = newSkill.getRelationSkills();
	 		for (int _k : _ss)
			{
				removeSkill(_k, true);
			}
			 */
		}
		Skill oldSkill = super.addSkill(newSkill);
		if (newSkill.equals(oldSkill))
		{
			return oldSkill;
		}
		if (store)
		{
			storeSkill(newSkill, oldSkill);
		}
		return oldSkill;
	}
	
	/**
	 * Method removeSkill.
	 * @param skill Skill
	 * @param fromDB boolean
	 * @return Skill
	 */
	public Skill removeSkill(Skill skill, boolean fromDB)
	{
		if (skill == null)
		{
			return null;
		}
		return removeSkill(skill.getId(), fromDB);
	}
	
	/**
	 * Method removeSkill.
	 * @param id int
	 * @param fromDB boolean
	 * @return Skill
	 */
	//Must be used for single skill to remove don't use into loop cycle!
	public Skill removeSkill(int id, boolean fromDB)
	{
		Skill oldSkill = super.removeSkillById(id);
		if (!fromDB)
		{
			return oldSkill;
		}
		if (oldSkill != null)
		{
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("DELETE FROM character_skills WHERE skill_id=? AND char_obj_id=? AND class_index=?");
				statement.setInt(1, oldSkill.getId());
				statement.setInt(2, getObjectId());
				statement.setInt(3, getActiveClassId());
				statement.execute();
			}
			catch (final Exception e)
			{
				_log.error("Could not delete skill!", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
		return oldSkill;
	}

	/**
	 * Method removeSkills.
	 * @param _SkillToRemove List<Integer>
	 * @param fromDB boolean
	 */
	//Lighter removeSkills query by passing list of skill to remove.
	public void removeSkills(List<Integer> SkillToRemove, boolean fromDB)
	{
		Iterator<Integer> iterator = SkillToRemove.iterator();
		while (iterator.hasNext())
		{
			super.removeSkillById(iterator.next());
		}
		if (SkillToRemove.size() > 0 && fromDB)
		{
			String SkillList = SkillToRemove.toString();
			SkillList = SkillList.replace('[', '(');
			SkillList = SkillList.replace(']', ')');
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("DELETE FROM character_skills WHERE skill_id IN " + SkillList + " AND char_obj_id=? AND class_index=?");
				statement.setInt(1, getObjectId());
				statement.setInt(2, getActiveClassId());
				statement.execute();
			}
			catch (final Exception e)
			{
				_log.error("Could not delete skill!", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}

	/**
	 * Method storeCertSkill.
	 * @param newSkill Skill
	 * @param oldSkill Skill
	 */
	public HashMap <Integer, Integer> getCertSkill()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		HashMap <Integer, Integer> skillList = new HashMap<Integer,Integer>();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			for(SubClass sb : getSubClassList().values())
			{
				if(sb.getType() == SubClassType.BASE_CLASS)
				{
					List <Integer> certificationId = new ArrayList <Integer>();
					Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, AcquireType.CERTIFICATION);
					for(SkillLearn sklL : skillLearnList)
					{
						certificationId.add(sklL.getId());
					}
					statement = con.prepareStatement("SELECT skill_id, skill_level FROM character_skills WHERE char_obj_id=? AND class_index=?");
					statement.setInt(1, getObjectId());
					statement.setInt(2, sb.getClassId());
					rset = statement.executeQuery();					
					while(rset.next())
					{
						for(Integer sklId : certificationId)
						{
							if(sklId == rset.getInt("skill_id"))
							{
								skillList.put(rset.getInt("skill_id"), rset.getInt("skill_level"));									
							}
						}
					}
					break;
				}
			}
		}
		catch (final Exception e)
		{
			_log.error("Error could not get Certification skills!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return skillList;
	}

	/**
	 * Method storeCertSkill.
	 * @param newSkill Skill
	 * @param oldSkill Skill
	 */
	private void storeCertSkill(final Skill newSkill, final Skill oldSkill, final boolean isDual)
	{
		if (newSkill == null)
		{
			_log.warn("could not store new skill. its NULL");
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			for(SubClass sb : this.getSubClassList().values())
			{
				if(sb.getType() == SubClassType.BASE_CLASS || sb.getType() == SubClassType.DOUBLE_SUBCLASS || (sb.getType() == SubClassType.SUBCLASS && !isDual))
				{
					statement = con.prepareStatement("REPLACE INTO character_skills (char_obj_id,skill_id,skill_level,class_index) values(?,?,?,?)");
					statement.setInt(1, getObjectId());
					statement.setInt(2, newSkill.getId());
					statement.setInt(3, newSkill.getLevel());
					statement.setInt(4, sb.getClassId());		
					statement.execute();			
				}
			}
		}
		catch (final Exception e)
		{
			_log.error("Error could not store Certification skills!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method removeCertSkill.
	 * @param id int
	 * @param fromDB boolean
	 * @return Skill
	 */
	//Must be used for single skill to remove don't use into loop cycle!
	public void removeCertSkill(List <Integer> skillsRemove)
	{
		Iterator<Integer> iterator = skillsRemove.iterator();
		while (iterator.hasNext())
		{
			super.removeSkillById(iterator.next());
		}
		if (skillsRemove.size() > 0)
		{
			String SkillList = skillsRemove.toString();
			SkillList = SkillList.replace('[', '(');
			SkillList = SkillList.replace(']', ')');
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				for(SubClass subclass : getSubClassList().values())
				{
					statement = con.prepareStatement("DELETE FROM character_skills WHERE skill_id IN " + SkillList + " AND char_obj_id=? AND class_index=?");
					statement.setInt(1, getObjectId());
					statement.setInt(2, subclass.getClassId());
					statement.execute();					
				}
			}
			catch (final Exception e)
			{
				_log.error("Could not delete certification skill!", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}

	
	/**
	 * Method storeSkill.
	 * @param newSkill Skill
	 * @param oldSkill Skill
	 */
	private void storeSkill(final Skill newSkill, final Skill oldSkill)
	{
		if (newSkill == null)
		{
			_log.warn("could not store new skill. its NULL");
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO character_skills (char_obj_id,skill_id,skill_level,class_index) values(?,?,?,?)");
			statement.setInt(1, getObjectId());
			statement.setInt(2, newSkill.getId());
			statement.setInt(3, newSkill.getLevel());
			statement.setInt(4, getActiveClassId());
			statement.execute();
		}
		catch (final Exception e)
		{
			_log.error("Error could not store skills!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restoreSkills.
	 */
	private void restoreSkills()
	{
		//----------------------------------------------
		List<Integer> keepskill = new ArrayList<Integer>();
		//Nobless Skills
		keepskill.add(325);
		//Common Craft Skills
		keepskill.add(1320);
		//Mentor Skills
		keepskill.add(9227);
		keepskill.add(9228);
		keepskill.add(9229);
		keepskill.add(9230);
		keepskill.add(9231);
		keepskill.add(9232);
		keepskill.add(9233);
		keepskill.add(9256);
		keepskill.add(9376);
		keepskill.add(9377);
		keepskill.add(9378);
		keepskill.add(9379);
		//------------------------------------------------
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT skill_id,skill_level FROM character_skills WHERE char_obj_id=? AND (class_index=? OR class_index=-1)");
			statement.setInt(1, getObjectId());
			statement.setInt(2, getActiveClassId());
			rset = statement.executeQuery();
			List<Integer> _relationSkillToRemove = new ArrayList<Integer>();
			List<Integer> _SkillToRemove = new ArrayList<Integer>();
			while (rset.next())
			{
				final int id = rset.getInt("skill_id");
				final int level = rset.getInt("skill_level");
				final Skill skill = SkillTable.getInstance().getInfo(id, level);
				if (skill == null)
				{
					_log.warn("Problem! RestoreSkill Id: " + id + " level: " + level);
					continue;
				}
				if (!isAwaking() && !SkillAcquireHolder.getInstance().isSkillPossible(this, skill))
				{
					if (!keepskill.contains(skill.getId()))
					{
						_SkillToRemove.add(skill.getId());
						//removeSkill(skill, true);
						removeSkillFromShortCut(skill.getId());
						_log.info("SkillTree: Removed skill: " + skill.getId() + " - " + skill.getName() + " to the player " + getName());
						continue;
					}
				}
				//-------------------
				//SKILL RACE CHECK
				//-------------------
				if (isAwaking() && !SkillAcquireHolder.getInstance().isSkillRacePossible(this, skill))
				{
					_SkillToRemove.add(skill.getId());
					//removeSkill(skill, true);
					_log.info("Race Skill Removed: " + skill.getId() + " - " + skill.getName() + " to the player " + getName());
					continue;
				}
				//-------------------
				//SKILL DB CHECK
				//-------------------
				if (!SkillAcquireHolder.getInstance().isSkillPossible(this, skill))
				{
					if (!SkillAcquireHolder.getInstance().getAllClassSkillId().contains(skill.getId()))
					{
						if (!keepskill.contains(skill.getId()))
						{
							_SkillToRemove.add(skill.getId());
							//removeSkill(skill, true);
							_log.info("Removed Skill: " + skill.getId() + " - " + skill.getName() + " to the player " + getName());
							continue;
						}
					}
				}
				//-------------------
				if (Config.ALT_DELETE_SKILL_RELATION && skill.isRelationSkill())
				{
					int[] _ss = skill.getRelationSkills();
					for (int _k : _ss)
					{
						_relationSkillToRemove.add(_k);
					}
				}
				super.addSkill(skill);
			}
			
			removeSkills(_SkillToRemove, true);

			if (isNoble())
			{
				updateNobleSkills();
			}
			if (_hero && getSubClassList().isBaseClassActive())
			{
				Hero.addSkills(this);
			}
			if (_clan != null)
			{
				_clan.addSkillsQuietly(this);
				if ((_clan.getLeaderId() == getObjectId()) && (_clan.getLevel() >= 5))
				{
					SiegeUtils.addSiegeSkills(this);
				}
			}
			if (((getActiveClassId() >= 53) && (getActiveClassId() <= 57)) || (getActiveClassId() == 117) || (getActiveClassId() == 118))
			{
				super.addSkill(SkillTable.getInstance().getInfo(1321, 1));
			}
			super.addSkill(SkillTable.getInstance().getInfo(1322, 1));
			if (Config.UNSTUCK_SKILL && (getSkillLevel(1050) < 0))
			{
				super.addSkill(SkillTable.getInstance().getInfo(2099, 1));
			}
			if (Config.ALT_DELETE_SKILL_RELATION)
			{
				HashSet<Integer> _tmp = new HashSet<Integer>();
				_tmp.addAll(_relationSkillToRemove);
				_relationSkillToRemove.clear();
				_relationSkillToRemove.addAll(_tmp);
				for (Skill s : getAllSkills())
				{
					if (_relationSkillToRemove.contains(s.getId()))
					{
						removeSkill(s, true);
						removeSkillFromShortCut(s.getId());
						_log.info("SkillRelation: Removed skill: " + s.getId() + " - " + s.getName() + " to the player " + getName());
					}
				}
			}
		}
		catch (final Exception e)
		{
			_log.warn("Could not restore skills for player objId: " + getObjectId());
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method storeDisableSkills.
	 */
	public void storeDisableSkills()
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			statement.executeUpdate("DELETE FROM character_skills_save WHERE char_obj_id = " + getObjectId() + " AND class_index=" + getActiveClassId() + " AND `end_time` < " + System.currentTimeMillis());
			if (_skillReuses.isEmpty())
			{
				return;
			}
			SqlBatch b = new SqlBatch("REPLACE INTO `character_skills_save` (`char_obj_id`,`skill_id`,`skill_level`,`class_index`,`end_time`,`reuse_delay_org`) VALUES");
			synchronized (_skillReuses)
			{
				StringBuilder sb;
				for (TimeStamp timeStamp : _skillReuses.values())
				{
					if (timeStamp.hasNotPassed())
					{
						sb = new StringBuilder("(");
						sb.append(getObjectId()).append(',');
						sb.append(timeStamp.getId()).append(',');
						sb.append(timeStamp.getLevel()).append(',');
						sb.append(getActiveClassId()).append(',');
						sb.append(timeStamp.getEndTime()).append(',');
						sb.append(timeStamp.getReuseBasic()).append(')');
						b.write(sb.toString());
					}
				}
			}
			if (!b.isEmpty())
			{
				statement.executeUpdate(b.close());
			}
		}
		catch (final Exception e)
		{
			_log.warn("Could not store disable skills data: " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restoreDisableSkills.
	 */
	public void restoreDisableSkills()
	{
		_skillReuses.clear();
		Connection con = null;
		Statement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			rset = statement.executeQuery("SELECT skill_id,skill_level,end_time,reuse_delay_org FROM character_skills_save WHERE char_obj_id=" + getObjectId() + " AND class_index=" + getActiveClassId());
			while (rset.next())
			{
				int skillId = rset.getInt("skill_id");
				int skillLevel = rset.getInt("skill_level");
				long endTime = rset.getLong("end_time");
				long rDelayOrg = rset.getLong("reuse_delay_org");
				long curTime = System.currentTimeMillis();
				Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
				if ((skill != null) && ((endTime - curTime) > 500))
				{
					_skillReuses.put(skill.hashCode(), new TimeStamp(skill, endTime, rDelayOrg));
				}
			}
			DbUtils.close(statement);
			statement = con.createStatement();
			statement.executeUpdate("DELETE FROM character_skills_save WHERE char_obj_id = " + getObjectId() + " AND class_index=" + getActiveClassId() + " AND `end_time` < " + System.currentTimeMillis());
		}
		catch (Exception e)
		{
			_log.error("Could not restore active skills data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method restoreHenna.
	 */
	private void restoreHenna()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("select slot, symbol_id from character_hennas where char_obj_id=? AND class_index=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, getActiveClassId());
			rset = statement.executeQuery();
			for (int i = 0; i < 3; i++)
			{
				_henna[i] = null;
			}
			while (rset.next())
			{
				final int slot = rset.getInt("slot");
				if ((slot < 1) || (slot > 3))
				{
					continue;
				}
				final int symbol_id = rset.getInt("symbol_id");
				if (symbol_id != 0)
				{
					final Henna tpl = HennaHolder.getInstance().getHenna(symbol_id);
					if (tpl != null)
					{
						_henna[slot - 1] = tpl;
						if (tpl.getSkillId() > 0)
						{
							Skill skill = SkillTable.getInstance().getInfo(tpl.getSkillId(), 1);
							if (skill != null)
							{
								addSkill(skill, true);
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			_log.warn("could not restore henna: " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		recalcHennaStats();
	}
	
	/**
	 * Method getHennaEmptySlots.
	 * @return int
	 */
	public int getHennaEmptySlots()
	{
		int totalSlots = 1 + getClassLevel();
		for (int i = 0; i < 3; i++)
		{
			if (_henna[i] != null)
			{
				totalSlots--;
			}
		}
		if (totalSlots <= 0)
		{
			return 0;
		}
		return totalSlots;
	}
	
	/**
	 * Method removeHenna.
	 * @param slot int
	 * @return boolean
	 */
	public boolean removeHenna(int slot)
	{
		if ((slot < 1) || (slot > 3))
		{
			return false;
		}
		slot--;
		if (_henna[slot] == null)
		{
			return false;
		}
		final Henna henna = _henna[slot];
		final int dyeID = henna.getDyeId();
		_henna[slot] = null;
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_hennas where char_obj_id=? and slot=? and class_index=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, slot + 1);
			statement.setInt(3, getActiveClassId());
			statement.execute();
		}
		catch (final Exception e)
		{
			_log.warn("could not remove char henna: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		if (henna.getSkillId() > 0)
		{
			removeSkillById(henna.getSkillId());
		}
		recalcHennaStats();
		sendPacket(new HennaInfo(this));
		sendUserInfo();
		ItemFunctions.addItem(this, dyeID, henna.getDrawCount() / 2, true);
		return true;
	}
	
	/**
	 * Method addHenna.
	 * @param henna Henna
	 * @return boolean
	 */
	public boolean addHenna(Henna henna)
	{
		if (getHennaEmptySlots() == 0)
		{
			sendPacket(SystemMsg.NO_SLOT_EXISTS_TO_DRAW_THE_SYMBOL);
			return false;
		}
		for (int i = 0; i < 3; i++)
		{
			if (_henna[i] == null)
			{
				_henna[i] = henna;
				recalcHennaStats();
				Connection con = null;
				PreparedStatement statement = null;
				try
				{
					con = DatabaseFactory.getInstance().getConnection();
					statement = con.prepareStatement("INSERT INTO `character_hennas` (char_obj_id, symbol_id, slot, class_index) VALUES (?,?,?,?)");
					statement.setInt(1, getObjectId());
					statement.setInt(2, henna.getSymbolId());
					statement.setInt(3, i + 1);
					statement.setInt(4, getActiveClassId());
					statement.execute();
				}
				catch (Exception e)
				{
					_log.warn("could not save char henna: " + e);
				}
				finally
				{
					DbUtils.closeQuietly(con, statement);
				}
				if (henna.getSkillId() > 0)
				{
					Skill skill = SkillTable.getInstance().getInfo(henna.getSkillId(), 1);
					if (skill != null)
					{
						addSkill(skill, true);
					}
				}
				sendPacket(new HennaInfo(this));
				sendUserInfo();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method recalcHennaStats.
	 */
	private void recalcHennaStats()
	{
		_hennaINT = 0;
		_hennaSTR = 0;
		_hennaCON = 0;
		_hennaMEN = 0;
		_hennaWIT = 0;
		_hennaDEX = 0;
		for (int i = 0; i < 3; i++)
		{
			Henna henna = _henna[i];
			if (henna == null)
			{
				continue;
			}
			if (!henna.isForThisClass(this))
			{
				continue;
			}
			_hennaINT += henna.getStatINT();
			_hennaSTR += henna.getStatSTR();
			_hennaMEN += henna.getStatMEN();
			_hennaCON += henna.getStatCON();
			_hennaWIT += henna.getStatWIT();
			_hennaDEX += henna.getStatDEX();
		}
		if (_hennaINT > 15)
		{
			_hennaINT = 15;
		}
		if (_hennaSTR > 15)
		{
			_hennaSTR = 15;
		}
		if (_hennaMEN > 15)
		{
			_hennaMEN = 15;
		}
		if (_hennaCON > 15)
		{
			_hennaCON = 15;
		}
		if (_hennaWIT > 15)
		{
			_hennaWIT = 15;
		}
		if (_hennaDEX > 15)
		{
			_hennaDEX = 15;
		}
	}
	
	/**
	 * Method getHenna.
	 * @param slot int
	 * @return Henna
	 */
	public Henna getHenna(final int slot)
	{
		if ((slot < 1) || (slot > 3))
		{
			return null;
		}
		return _henna[slot - 1];
	}
	
	/**
	 * Method getHennaStatINT.
	 * @return int
	 */
	public int getHennaStatINT()
	{
		return _hennaINT;
	}
	
	/**
	 * Method getHennaStatSTR.
	 * @return int
	 */
	public int getHennaStatSTR()
	{
		return _hennaSTR;
	}
	
	/**
	 * Method getHennaStatCON.
	 * @return int
	 */
	public int getHennaStatCON()
	{
		return _hennaCON;
	}
	
	/**
	 * Method getHennaStatMEN.
	 * @return int
	 */
	public int getHennaStatMEN()
	{
		return _hennaMEN;
	}
	
	/**
	 * Method getHennaStatWIT.
	 * @return int
	 */
	public int getHennaStatWIT()
	{
		return _hennaWIT;
	}
	
	/**
	 * Method getHennaStatDEX.
	 * @return int
	 */
	public int getHennaStatDEX()
	{
		return _hennaDEX;
	}
	
	/**
	 * Method consumeItem.
	 * @param itemConsumeId int
	 * @param itemCount long
	 * @return boolean
	 */
	@Override
	public boolean consumeItem(int itemConsumeId, long itemCount)
	{
		if (getInventory().destroyItemByItemId(itemConsumeId, itemCount))
		{
			sendPacket(SystemMessage2.removeItems(itemConsumeId, itemCount));
			return true;
		}
		return false;
	}
	
	/**
	 * Method consumeItemMp.
	 * @param itemId int
	 * @param mp int
	 * @return boolean
	 */
	@Override
	public boolean consumeItemMp(int itemId, int mp)
	{
		for (ItemInstance item : getInventory().getPaperdollItems())
		{
			if ((item != null) && (item.getItemId() == itemId))
			{
				final int newMp = item.getLifeTime() - mp;
				if (newMp >= 0)
				{
					item.setLifeTime(newMp);
					sendPacket(new InventoryUpdate().addModifiedItem(item));
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	/**
	 * Method isMageClass.
	 * @return boolean
	 */
	@Override
	public boolean isMageClass()
	{
		return getClassId().isMage();
	}
	
	/**
	 * Method isMounted.
	 * @return boolean
	 */
	public boolean isMounted()
	{
		return _mountNpcId > 0;
	}
	
	/**
	 * Method isRiding.
	 * @return boolean
	 */
	public final boolean isRiding()
	{
		return _riding;
	}
	
	/**
	 * Method setRiding.
	 * @param mode boolean
	 */
	public final void setRiding(boolean mode)
	{
		_riding = mode;
	}
	
	/**
	 * Method checkLandingState.
	 * @return boolean
	 */
	public boolean checkLandingState()
	{
		if (isInZone(ZoneType.no_landing))
		{
			return false;
		}
		SiegeEvent<?, ?> siege = getEvent(SiegeEvent.class);
		if (siege != null)
		{
			Residence unit = siege.getResidence();
			if ((unit != null) && (getClan() != null) && isClanLeader() && ((getClan().getCastle() == unit.getId()) || (getClan().getHasFortress() == unit.getId())))
			{
				return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Method setMount.
	 * @param npcId int
	 * @param obj_id int
	 * @param level int
	 */
	public void setMount(int npcId, int obj_id, int level)
	{
		if (isCursedWeaponEquipped())
		{
			return;
		}
		switch (npcId)
		{
			case 0:
				setFlying(false);
				setRiding(false);
				if (getTransformation() > 0)
				{
					setTransformation(0);
				}
				removeSkillById(Skill.SKILL_STRIDER_ASSAULT);
				removeSkillById(Skill.SKILL_WYVERN_BREATH);
				getEffectList().stopEffect(Skill.SKILL_HINDER_STRIDER);
				break;
			case PetDataTable.STRIDER_WIND_ID:
			case PetDataTable.STRIDER_STAR_ID:
			case PetDataTable.STRIDER_TWILIGHT_ID:
			case PetDataTable.RED_STRIDER_WIND_ID:
			case PetDataTable.RED_STRIDER_STAR_ID:
			case PetDataTable.RED_STRIDER_TWILIGHT_ID:
			case PetDataTable.GUARDIANS_STRIDER_ID:
				setRiding(true);
				if (isNoble())
				{
					addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_STRIDER_ASSAULT, 1), false);
				}
				break;
			case PetDataTable.WYVERN_ID:
				setFlying(true);
				setLoc(getLoc().changeZ(32));
				addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_WYVERN_BREATH, 1), false);
				break;
			case PetDataTable.WGREAT_WOLF_ID:
			case PetDataTable.FENRIR_WOLF_ID:
			case PetDataTable.WFENRIR_WOLF_ID:
				setRiding(true);
				break;
		}
		if (npcId > 0)
		{
			unEquipWeapon();
		}
		_mountNpcId = npcId;
		_mountObjId = obj_id;
		_mountLevel = level;
		broadcastUserInfo();
		broadcastPacket(new Ride(this));
		broadcastUserInfo();
		sendSkillList();
	}
	
	/**
	 * Method unEquipWeapon.
	 */
	public void unEquipWeapon()
	{
		ItemInstance wpn = getSecondaryWeaponInstance();
		if (wpn != null)
		{
			sendDisarmMessage(wpn);
			getInventory().unEquipItem(wpn);
		}
		wpn = getActiveWeaponInstance();
		if (wpn != null)
		{
			sendDisarmMessage(wpn);
			getInventory().unEquipItem(wpn);
		}
		abortAttack(true, true);
		abortCast(true, true);
	}
	
	/**
	 * Method getSpeed.
	 * @param baseSpeed double
	 * @return int
	 */
	@Override
	public int getSpeed(double baseSpeed)
	{
		if (isMounted())
		{
			PetData petData = PetDataTable.getInstance().getInfo(_mountNpcId, _mountLevel);
			int speed = 187;
			if (petData != null)
			{
				speed = petData.getSpeed();
			}
			double mod = 1.0;
			int level = getLevel();
			if ((_mountLevel > level) && ((level - _mountLevel) > 10))
			{
				mod = 0.5;
			}
			baseSpeed = (int) (mod * speed);
			if (isRunning())
			{
				baseSpeed += getTemplate().getBaseRideRunSpd();
			}
			else
			{
				baseSpeed += getTemplate().getBaseRideWalkSpd();
			}
		}
		return super.getSpeed(baseSpeed);
	}
	
	/**
	 * Field _mountNpcId.
	 */
	private int _mountNpcId;
	/**
	 * Field _mountObjId.
	 */
	private int _mountObjId;
	/**
	 * Field _mountLevel.
	 */
	private int _mountLevel;
	
	/**
	 * Method getMountNpcId.
	 * @return int
	 */
	public int getMountNpcId()
	{
		return _mountNpcId;
	}
	
	/**
	 * Method getMountObjId.
	 * @return int
	 */
	public int getMountObjId()
	{
		return _mountObjId;
	}
	
	/**
	 * Method getMountLevel.
	 * @return int
	 */
	public int getMountLevel()
	{
		return _mountLevel;
	}
	
	/**
	 * Method sendDisarmMessage.
	 * @param wpn ItemInstance
	 */
	public void sendDisarmMessage(ItemInstance wpn)
	{
		if (wpn.getEnchantLevel() > 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.EQUIPMENT_OF__S1_S2_HAS_BEEN_REMOVED);
			sm.addNumber(wpn.getEnchantLevel());
			sm.addItemName(wpn.getItemId());
			sendPacket(sm);
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1__HAS_BEEN_DISARMED);
			sm.addItemName(wpn.getItemId());
			sendPacket(sm);
		}
	}
	
	/**
	 * Method setUsingWarehouseType.
	 * @param type WarehouseType
	 */
	public void setUsingWarehouseType(final WarehouseType type)
	{
		_usingWHType = type;
	}
	
	/**
	 * Method getUsingWarehouseType.
	 * @return WarehouseType
	 */
	public WarehouseType getUsingWarehouseType()
	{
		return _usingWHType;
	}
	
	/**
	 * Method getCubics.
	 * @return Collection<EffectCubic>
	 */
	public Collection<EffectCubic> getCubics()
	{
		return _cubics == null ? Collections.<EffectCubic> emptyList() : _cubics.values();
	}
	
	/**
	 * Method addCubic.
	 * @param cubic EffectCubic
	 */
	public void addCubic(EffectCubic cubic)
	{
		if (_cubics == null)
		{
			_cubics = new ConcurrentHashMap<Integer, EffectCubic>(3);
		}
		_cubics.put(cubic.getId(), cubic);
	}
	
	/**
	 * Method removeCubic.
	 * @param id int
	 */
	public void removeCubic(int id)
	{
		if (_cubics != null)
		{
			_cubics.remove(id);
		}
	}
	
	/**
	 * Method getCubic.
	 * @param id int
	 * @return EffectCubic
	 */
	public EffectCubic getCubic(int id)
	{
		return _cubics == null ? null : _cubics.get(id);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return getName() + "[" + getObjectId() + "]";
	}
	
	/**
	 * Method getEnchantEffect.
	 * @return int
	 */
	public int getEnchantEffect()
	{
		final ItemInstance wpn = getActiveWeaponInstance();
		if (wpn == null)
		{
			return 0;
		}
		return Math.min(127, wpn.getEnchantLevel());
	}
	
	/**
	 * Method setLastNpc.
	 * @param npc NpcInstance
	 */
	public void setLastNpc(final NpcInstance npc)
	{
		if (npc == null)
		{
			_lastNpc = HardReferences.emptyRef();
		}
		else
		{
			_lastNpc = npc.getRef();
		}
	}
	
	/**
	 * Method getLastNpc.
	 * @return NpcInstance
	 */
	public NpcInstance getLastNpc()
	{
		return _lastNpc.get();
	}
	
	/**
	 * Method setMultisell.
	 * @param multisell MultiSellListContainer
	 */
	public void setMultisell(MultiSellListContainer multisell)
	{
		_multisell = multisell;
	}
	
	/**
	 * Method getMultisell.
	 * @return MultiSellListContainer
	 */
	public MultiSellListContainer getMultisell()
	{
		return _multisell;
	}
	
	/**
	 * Method unChargeShots.
	 * @param spirit boolean
	 * @return boolean
	 */
	@Override
	public boolean unChargeShots(boolean spirit)
	{
		ItemInstance weapon = getActiveWeaponInstance();
		if (weapon == null)
		{
			return false;
		}
		if (spirit)
		{
			weapon.setChargedSpiritshot(ItemInstance.CHARGED_NONE);
		}
		else
		{
			weapon.setChargedSoulshot(ItemInstance.CHARGED_NONE);
		}
		autoShot();
		return true;
	}
	
	/**
	 * Method unChargeFishShot.
	 * @return boolean
	 */
	public boolean unChargeFishShot()
	{
		ItemInstance weapon = getActiveWeaponInstance();
		if (weapon == null)
		{
			return false;
		}
		weapon.setChargedFishshot(false);
		autoShot();
		return true;
	}
	
	/**
	 * Method autoShot.
	 */
	public void autoShot()
	{
		for (Integer shotId : _activeSoulShots)
		{
			ItemInstance item = getInventory().getItemByItemId(shotId);
			if (item == null)
			{
				removeAutoSoulShot(shotId);
				continue;
			}
			IItemHandler handler = item.getTemplate().getHandler();
			if (handler == null)
			{
				continue;
			}
			handler.useItem(this, item, false);
		}
	}
	
	/**
	 * Method getChargedFishShot.
	 * @return boolean
	 */
	public boolean getChargedFishShot()
	{
		ItemInstance weapon = getActiveWeaponInstance();
		return (weapon != null) && weapon.getChargedFishshot();
	}
	
	/**
	 * Method getChargedSoulShot.
	 * @return boolean
	 */
	@Override
	public boolean getChargedSoulShot()
	{
		ItemInstance weapon = getActiveWeaponInstance();
		return (weapon != null) && (weapon.getChargedSoulshot() == ItemInstance.CHARGED_SOULSHOT);
	}
	
	/**
	 * Method getChargedSpiritShot.
	 * @return int
	 */
	@Override
	public int getChargedSpiritShot()
	{
		ItemInstance weapon = getActiveWeaponInstance();
		if (weapon == null)
		{
			return 0;
		}
		return weapon.getChargedSpiritshot();
	}
	
	/**
	 * Method addAutoSoulShot.
	 * @param itemId Integer
	 */
	public void addAutoSoulShot(Integer itemId)
	{
		_activeSoulShots.add(itemId);
	}
	
	/**
	 * Method removeAutoSoulShot.
	 * @param itemId Integer
	 */
	public void removeAutoSoulShot(Integer itemId)
	{
		_activeSoulShots.remove(itemId);
	}
	
	/**
	 * Method getAutoSoulShot.
	 * @return Set<Integer>
	 */
	public Set<Integer> getAutoSoulShot()
	{
		return _activeSoulShots;
	}
	
	/**
	 * Method setInvisibleType.
	 * @param vis InvisibleType
	 */
	public void setInvisibleType(InvisibleType vis)
	{
		_invisibleType = vis;
	}
	
	/**
	 * Method getInvisibleType.
	 * @return InvisibleType
	 */
	@Override
	public InvisibleType getInvisibleType()
	{
		return _invisibleType;
	}
	
	/**
	 * Method getClanPrivileges.
	 * @return int
	 */
	public int getClanPrivileges()
	{
		if (_clan == null)
		{
			return 0;
		}
		if (isClanLeader())
		{
			return Clan.CP_ALL;
		}
		if ((_powerGrade < 1) || (_powerGrade > 9))
		{
			return 0;
		}
		RankPrivs privs = _clan.getRankPrivs(_powerGrade);
		if (privs != null)
		{
			return privs.getPrivs();
		}
		return 0;
	}
	
	/**
	 * Method teleToClosestTown.
	 */
	public void teleToClosestTown()
	{
		teleToLocation(TeleportUtils.getRestartLocation(this, RestartType.TO_VILLAGE), ReflectionManager.DEFAULT);
	}
	
	/**
	 * Method teleToCastle.
	 */
	public void teleToCastle()
	{
		teleToLocation(TeleportUtils.getRestartLocation(this, RestartType.TO_CASTLE), ReflectionManager.DEFAULT);
	}
	
	/**
	 * Method teleToFortress.
	 */
	public void teleToFortress()
	{
		teleToLocation(TeleportUtils.getRestartLocation(this, RestartType.TO_FORTRESS), ReflectionManager.DEFAULT);
	}
	
	/**
	 * Method teleToClanhall.
	 */
	public void teleToClanhall()
	{
		teleToLocation(TeleportUtils.getRestartLocation(this, RestartType.TO_CLANHALL), ReflectionManager.DEFAULT);
	}
	
	/**
	 * Method sendMessage.
	 * @param message CustomMessage
	 */
	@Override
	public void sendMessage(CustomMessage message)
	{
		sendMessage(message.toString());
	}
	
	/**
	 * Method teleToLocation.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param refId int
	 */
	@Override
	public void teleToLocation(int x, int y, int z, int refId)
	{
		if (isDeleted())
		{
			return;
		}
		super.teleToLocation(x, y, z, refId);
	}
	
	/**
	 * Method onTeleported.
	 * @return boolean
	 */
	@Override
	public boolean onTeleported()
	{
		if (!super.onTeleported())
		{
			return false;
		}
		if (isFakeDeath())
		{
			breakFakeDeath();
		}
		if (isInBoat())
		{
			setLoc(getBoat().getLoc());
		}
		setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
		spawnMe();
		setLastClientPosition(getLoc());
		setLastServerPosition(getLoc());
		if (isPendingRevive())
		{
			doRevive();
		}
		sendActionFailed();
		getAI().notifyEvent(CtrlEvent.EVT_TELEPORTED);
		
		if (isLockedTarget() && (getTarget() != null))
		{
			updateTargetSelectionInfo();
		}
		sendUserInfo();
		for (Summon summon : getSummonList())
		{
			summon.teleportToOwner();
		}
		return true;
	}
	
	/**
	 * Method enterObserverMode.
	 * @param loc Location
	 * @return boolean
	 */
	public boolean enterObserverMode(Location loc)
	{
		WorldRegion observerRegion = World.getRegion(loc);
		if (observerRegion == null)
		{
			return false;
		}
		if (!_observerMode.compareAndSet(OBSERVER_NONE, OBSERVER_STARTING))
		{
			return false;
		}
		setTarget(null);
		stopMove();
		sitDown(null);
		setFlying(true);
		World.removeObjectsFromPlayer(this);
		setObserverRegion(observerRegion);
		broadcastCharInfo();
		sendPacket(new ObserverStart(loc));
		return true;
	}
	
	/**
	 * Method appearObserverMode.
	 */
	public void appearObserverMode()
	{
		if (!_observerMode.compareAndSet(OBSERVER_STARTING, OBSERVER_STARTED))
		{
			return;
		}
		WorldRegion currentRegion = getCurrentRegion();
		WorldRegion observerRegion = getObserverRegion();
		if (!observerRegion.equals(currentRegion))
		{
			observerRegion.addObject(this);
		}
		World.showObjectsToPlayer(this);
		OlympiadGame game = getOlympiadObserveGame();
		if (game != null)
		{
			game.addSpectator(this);
			game.broadcastInfo(null, this, true);
		}
	}
	
	/**
	 * Method leaveObserverMode.
	 */
	public void leaveObserverMode()
	{
		if (!_observerMode.compareAndSet(OBSERVER_STARTED, OBSERVER_LEAVING))
		{
			return;
		}
		WorldRegion currentRegion = getCurrentRegion();
		WorldRegion observerRegion = getObserverRegion();
		if (!observerRegion.equals(currentRegion))
		{
			observerRegion.removeObject(this);
		}
		World.removeObjectsFromPlayer(this);
		setObserverRegion(null);
		setTarget(null);
		stopMove();
		sendPacket(new ObserverEnd(getLoc()));
	}
	
	/**
	 * Method returnFromObserverMode.
	 */
	public void returnFromObserverMode()
	{
		if (!_observerMode.compareAndSet(OBSERVER_LEAVING, OBSERVER_NONE))
		{
			return;
		}
		setLastClientPosition(null);
		setLastServerPosition(null);
		unblock();
		standUp();
		setFlying(false);
		broadcastCharInfo();
		World.showObjectsToPlayer(this);
	}
	
	/**
	 * Method enterOlympiadObserverMode.
	 * @param loc Location
	 * @param game OlympiadGame
	 * @param reflect Reflection
	 */
	public void enterOlympiadObserverMode(Location loc, OlympiadGame game, Reflection reflect)
	{
		WorldRegion observerRegion = World.getRegion(loc);
		if (observerRegion == null)
		{
			return;
		}
		OlympiadGame oldGame = getOlympiadObserveGame();
		if (!_observerMode.compareAndSet(oldGame != null ? OBSERVER_STARTED : OBSERVER_NONE, OBSERVER_STARTING))
		{
			return;
		}
		setTarget(null);
		stopMove();
		World.removeObjectsFromPlayer(this);
		setObserverRegion(observerRegion);
		if (oldGame != null)
		{
			oldGame.removeSpectator(this);
			sendPacket(ExOlympiadMatchEnd.STATIC);
		}
		else
		{
			block();
			broadcastCharInfo();
			sendPacket(new ExOlympiadMode(3));
		}
		setOlympiadObserveGame(game);
		setReflection(reflect);
		sendPacket(new TeleportToLocation(this, loc));
	}
	
	/**
	 * Method leaveOlympiadObserverMode.
	 * @param removeFromGame boolean
	 */
	public void leaveOlympiadObserverMode(boolean removeFromGame)
	{
		OlympiadGame game = getOlympiadObserveGame();
		if (game == null)
		{
			return;
		}
		if (!_observerMode.compareAndSet(OBSERVER_STARTED, OBSERVER_LEAVING))
		{
			return;
		}
		if (removeFromGame)
		{
			game.removeSpectator(this);
		}
		setOlympiadObserveGame(null);
		WorldRegion currentRegion = getCurrentRegion();
		WorldRegion observerRegion = getObserverRegion();
		if ((observerRegion != null) && (currentRegion != null) && !observerRegion.equals(currentRegion))
		{
			observerRegion.removeObject(this);
		}
		World.removeObjectsFromPlayer(this);
		setObserverRegion(null);
		setTarget(null);
		stopMove();
		sendPacket(new ExOlympiadMode(0));
		sendPacket(ExOlympiadMatchEnd.STATIC);
		setReflection(ReflectionManager.DEFAULT);
		sendPacket(new TeleportToLocation(this, getLoc()));
	}
	
	/**
	 * Method setOlympiadSide.
	 * @param i int
	 */
	public void setOlympiadSide(final int i)
	{
		_olympiadSide = i;
	}
	
	/**
	 * Method getOlympiadSide.
	 * @return int
	 */
	public int getOlympiadSide()
	{
		return _olympiadSide;
	}
	
	/**
	 * Method isInObserverMode.
	 * @return boolean
	 */
	@Override
	public boolean isInObserverMode()
	{
		return _observerMode.get() > 0;
	}
	
	/**
	 * Method getObserverMode.
	 * @return int
	 */
	public int getObserverMode()
	{
		return _observerMode.get();
	}
	
	/**
	 * Method getObserverRegion.
	 * @return WorldRegion
	 */
	public WorldRegion getObserverRegion()
	{
		return _observerRegion;
	}
	
	/**
	 * Method setObserverRegion.
	 * @param region WorldRegion
	 */
	public void setObserverRegion(WorldRegion region)
	{
		_observerRegion = region;
	}
	
	/**
	 * Method getTeleMode.
	 * @return int
	 */
	public int getTeleMode()
	{
		return _telemode;
	}
	
	/**
	 * Method setTeleMode.
	 * @param mode int
	 */
	public void setTeleMode(final int mode)
	{
		_telemode = mode;
	}
	
	/**
	 * Method setLoto.
	 * @param i int
	 * @param val int
	 */
	public void setLoto(final int i, final int val)
	{
		_loto[i] = val;
	}
	
	/**
	 * Method getLoto.
	 * @param i int
	 * @return int
	 */
	public int getLoto(final int i)
	{
		return _loto[i];
	}
	
	/**
	 * Method setRace.
	 * @param i int
	 * @param val int
	 */
	public void setRace(final int i, final int val)
	{
		_race[i] = val;
	}
	
	/**
	 * Method getRace.
	 * @param i int
	 * @return int
	 */
	public int getRace(final int i)
	{
		return _race[i];
	}
	
	/**
	 * Method getMessageRefusal.
	 * @return boolean
	 */
	public boolean getMessageRefusal()
	{
		return _messageRefusal;
	}
	
	/**
	 * Method setMessageRefusal.
	 * @param mode boolean
	 */
	public void setMessageRefusal(final boolean mode)
	{
		_messageRefusal = mode;
	}
	
	/**
	 * Method setTradeRefusal.
	 * @param mode boolean
	 */
	public void setTradeRefusal(final boolean mode)
	{
		_tradeRefusal = mode;
	}
	
	/**
	 * Method getTradeRefusal.
	 * @return boolean
	 */
	public boolean getTradeRefusal()
	{
		return _tradeRefusal;
	}
	
	/**
	 * Method addToBlockList.
	 * @param charName String
	 */
	public void addToBlockList(final String charName)
	{
		if ((charName == null) || charName.equalsIgnoreCase(getName()) || isInBlockList(charName))
		{
			sendPacket(Msg.YOU_HAVE_FAILED_TO_REGISTER_THE_USER_TO_YOUR_IGNORE_LIST);
			return;
		}
		Player block_target = World.getPlayer(charName);
		if (block_target != null)
		{
			if (block_target.isGM())
			{
				sendPacket(Msg.YOU_MAY_NOT_IMPOSE_A_BLOCK_ON_A_GM);
				return;
			}
			_blockList.put(block_target.getObjectId(), block_target.getName());
			sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED_TO_YOUR_IGNORE_LIST).addString(block_target.getName()));
			block_target.sendPacket(new SystemMessage(SystemMessage.S1__HAS_PLACED_YOU_ON_HIS_HER_IGNORE_LIST).addString(getName()));
			return;
		}
		int charId = CharacterDAO.getInstance().getObjectIdByName(charName);
		if (charId == 0)
		{
			sendPacket(Msg.YOU_HAVE_FAILED_TO_REGISTER_THE_USER_TO_YOUR_IGNORE_LIST);
			return;
		}
		if (Config.gmlist.containsKey(charId) && Config.gmlist.get(charId).IsGM)
		{
			sendPacket(Msg.YOU_MAY_NOT_IMPOSE_A_BLOCK_ON_A_GM);
			return;
		}
		_blockList.put(charId, charName);
		sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED_TO_YOUR_IGNORE_LIST).addString(charName));
	}
	
	/**
	 * Method removeFromBlockList.
	 * @param charName String
	 */
	public void removeFromBlockList(final String charName)
	{
		int charId = 0;
		for (int blockId : _blockList.keySet())
		{
			if (charName.equalsIgnoreCase(_blockList.get(blockId)))
			{
				charId = blockId;
				break;
			}
		}
		if (charId == 0)
		{
			sendPacket(Msg.YOU_HAVE_FAILED_TO_DELETE_THE_CHARACTER_FROM_IGNORE_LIST);
			return;
		}
		sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_REMOVED_FROM_YOUR_IGNORE_LIST).addString(_blockList.remove(charId)));
		Player block_target = GameObjectsStorage.getPlayer(charId);
		if (block_target != null)
		{
			block_target.sendMessage(getName() + " has removed you from his/her Ignore List.");
		}
	}
	
	/**
	 * Method isInBlockList.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isInBlockList(final Player player)
	{
		return isInBlockList(player.getObjectId());
	}
	
	/**
	 * Method isInBlockList.
	 * @param charId int
	 * @return boolean
	 */
	public boolean isInBlockList(final int charId)
	{
		return (_blockList != null) && _blockList.containsKey(charId);
	}
	
	/**
	 * Method isInBlockList.
	 * @param charName String
	 * @return boolean
	 */
	public boolean isInBlockList(final String charName)
	{
		for (int blockId : _blockList.keySet())
		{
			if (charName.equalsIgnoreCase(_blockList.get(blockId)))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method restoreBlockList.
	 */
	private void restoreBlockList()
	{
		_blockList.clear();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT target_Id, char_name FROM character_blocklist LEFT JOIN characters ON ( character_blocklist.target_Id = characters.obj_Id ) WHERE character_blocklist.obj_Id = ?");
			statement.setInt(1, getObjectId());
			rs = statement.executeQuery();
			while (rs.next())
			{
				int targetId = rs.getInt("target_Id");
				String name = rs.getString("char_name");
				if (name == null)
				{
					continue;
				}
				_blockList.put(targetId, name);
			}
		}
		catch (SQLException e)
		{
			_log.warn("Can't restore player blocklist " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method storeBlockList.
	 */
	private void storeBlockList()
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			statement.executeUpdate("DELETE FROM character_blocklist WHERE obj_Id=" + getObjectId());
			if (_blockList.isEmpty())
			{
				return;
			}
			SqlBatch b = new SqlBatch("INSERT IGNORE INTO `character_blocklist` (`obj_Id`,`target_Id`) VALUES");
			synchronized (_blockList)
			{
				StringBuilder sb;
				for (Entry<Integer, String> e : _blockList.entrySet())
				{
					sb = new StringBuilder("(");
					sb.append(getObjectId()).append(',');
					sb.append(e.getKey()).append(')');
					b.write(sb.toString());
				}
			}
			if (!b.isEmpty())
			{
				statement.executeUpdate(b.close());
			}
		}
		catch (Exception e)
		{
			_log.warn("Can't store player blocklist " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method isBlockAll.
	 * @return boolean
	 */
	public boolean isBlockAll()
	{
		return _blockAll;
	}
	
	/**
	 * Method setBlockAll.
	 * @param state boolean
	 */
	public void setBlockAll(final boolean state)
	{
		_blockAll = state;
	}
	
	/**
	 * Method getBlockList.
	 * @return Collection<String>
	 */
	public Collection<String> getBlockList()
	{
		return _blockList.values();
	}
	
	/**
	 * Method getBlockListMap.
	 * @return Map<Integer,String>
	 */
	public Map<Integer, String> getBlockListMap()
	{
		return _blockList;
	}
	
	/**
	 * Method setHero.
	 * @param hero boolean
	 */
	public void setHero(final boolean hero)
	{
		_hero = hero;
	}
	
	/**
	 * Method isHero.
	 * @return boolean
	 */
	@Override
	public boolean isHero()
	{
		return _hero;
	}
	
	/**
	 * Method setIsInOlympiadMode.
	 * @param b boolean
	 */
	public void setIsInOlympiadMode(final boolean b)
	{
		_inOlympiadMode = b;
	}
	
	/**
	 * Method isInOlympiadMode.
	 * @return boolean
	 */
	@Override
	public boolean isInOlympiadMode()
	{
		return _inOlympiadMode;
	}
	
	/**
	 * Method isOlympiadGameStart.
	 * @return boolean
	 */
	public boolean isOlympiadGameStart()
	{
		return (_olympiadGame != null) && (_olympiadGame.getState() == 1);
	}
	
	/**
	 * Method isOlympiadCompStart.
	 * @return boolean
	 */
	public boolean isOlympiadCompStart()
	{
		return (_olympiadGame != null) && (_olympiadGame.getState() == 2);
	}
	
	/**
	 * Method updateNobleSkills.
	 */
	public void updateNobleSkills()
	{
		if (isNoble())
		{
			if (isClanLeader() && (getClan().getCastle() > 0))
			{
				super.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_WYVERN_AEGIS, 1));
			}
			super.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_NOBLESSE_BLESSING, 1));
			super.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_SUMMON_CP_POTION, 1));
			super.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_FORTUNE_OF_NOBLESSE, 1));
			super.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_HARMONY_OF_NOBLESSE, 1));
			super.addSkill(SkillTable.getInstance().getInfo(Skill.SKILL_SYMPHONY_OF_NOBLESSE, 1));
		}
		else
		{
			super.removeSkillById(Skill.SKILL_WYVERN_AEGIS);
			super.removeSkillById(Skill.SKILL_NOBLESSE_BLESSING);
			super.removeSkillById(Skill.SKILL_SUMMON_CP_POTION);
			super.removeSkillById(Skill.SKILL_FORTUNE_OF_NOBLESSE);
			super.removeSkillById(Skill.SKILL_HARMONY_OF_NOBLESSE);
			super.removeSkillById(Skill.SKILL_SYMPHONY_OF_NOBLESSE);
		}
	}
	
	/**
	 * Method setNoble.
	 * @param noble boolean
	 */
	public void setNoble(boolean noble)
	{
		if (noble)
		{
			broadcastPacket(new MagicSkillUse(this, this, 6673, 1, 1000, 0));
		}
		_noble = noble;
	}
	
	/**
	 * Method isNoble.
	 * @return boolean
	 */
	public boolean isNoble()
	{
		return _noble;
	}
	
	/**
	 * Method getSubLevel.
	 * @return int
	 */
	public int getSubLevel()
	{
		return isBaseClassActive() ? 0 : getLevel();
	}
	
	/**
	 * Method updateKetraVarka.
	 */
	public void updateKetraVarka()
	{
		if (ItemFunctions.getItemCount(this, 7215) > 0)
		{
			_ketra = 5;
		}
		else if (ItemFunctions.getItemCount(this, 7214) > 0)
		{
			_ketra = 4;
		}
		else if (ItemFunctions.getItemCount(this, 7213) > 0)
		{
			_ketra = 3;
		}
		else if (ItemFunctions.getItemCount(this, 7212) > 0)
		{
			_ketra = 2;
		}
		else if (ItemFunctions.getItemCount(this, 7211) > 0)
		{
			_ketra = 1;
		}
		else if (ItemFunctions.getItemCount(this, 7225) > 0)
		{
			_varka = 5;
		}
		else if (ItemFunctions.getItemCount(this, 7224) > 0)
		{
			_varka = 4;
		}
		else if (ItemFunctions.getItemCount(this, 7223) > 0)
		{
			_varka = 3;
		}
		else if (ItemFunctions.getItemCount(this, 7222) > 0)
		{
			_varka = 2;
		}
		else if (ItemFunctions.getItemCount(this, 7221) > 0)
		{
			_varka = 1;
		}
		else
		{
			_varka = 0;
			_ketra = 0;
		}
	}
	
	/**
	 * Method getVarka.
	 * @return int
	 */
	public int getVarka()
	{
		return _varka;
	}
	
	/**
	 * Method getKetra.
	 * @return int
	 */
	public int getKetra()
	{
		return _ketra;
	}
	
	/**
	 * Method updateRam.
	 */
	public void updateRam()
	{
		if (ItemFunctions.getItemCount(this, 7247) > 0)
		{
			_ram = 2;
		}
		else if (ItemFunctions.getItemCount(this, 7246) > 0)
		{
			_ram = 1;
		}
		else
		{
			_ram = 0;
		}
	}
	
	/**
	 * Method getRam.
	 * @return int
	 */
	public int getRam()
	{
		return _ram;
	}
	
	/**
	 * Method setPledgeType.
	 * @param typeId int
	 */
	public void setPledgeType(final int typeId)
	{
		_pledgeType = typeId;
	}
	
	/**
	 * Method getPledgeType.
	 * @return int
	 */
	public int getPledgeType()
	{
		return _pledgeType;
	}
	
	/**
	 * Method setLvlJoinedAcademy.
	 * @param lvl int
	 */
	public void setLvlJoinedAcademy(int lvl)
	{
		_lvlJoinedAcademy = lvl;
	}
	
	/**
	 * Method getLvlJoinedAcademy.
	 * @return int
	 */
	public int getLvlJoinedAcademy()
	{
		return _lvlJoinedAcademy;
	}
	
	/**
	 * Method getPledgeClass.
	 * @return int
	 */
	public int getPledgeClass()
	{
		return _pledgeClass;
	}
	
	/**
	 * Method updatePledgeClass.
	 */
	public void updatePledgeClass()
	{
		int CLAN_LEVEL = _clan == null ? -1 : _clan.getLevel();
		boolean IN_ACADEMY = (_clan != null) && Clan.isAcademy(_pledgeType);
		boolean IS_GUARD = (_clan != null) && Clan.isRoyalGuard(_pledgeType);
		boolean IS_KNIGHT = (_clan != null) && Clan.isOrderOfKnights(_pledgeType);
		boolean IS_GUARD_CAPTAIN = false, IS_KNIGHT_COMMANDER = false, IS_LEADER = false;
		SubUnit unit = getSubUnit();
		if (unit != null)
		{
			UnitMember unitMember = unit.getUnitMember(getObjectId());
			if (unitMember == null)
			{
				_log.warn("Player: unitMember null, clan: " + _clan.getClanId() + "; pledgeType: " + unit.getType());
				return;
			}
			IS_GUARD_CAPTAIN = Clan.isRoyalGuard(unitMember.getLeaderOf());
			IS_KNIGHT_COMMANDER = Clan.isOrderOfKnights(unitMember.getLeaderOf());
			IS_LEADER = unitMember.getLeaderOf() == Clan.SUBUNIT_MAIN_CLAN;
		}
		switch (CLAN_LEVEL)
		{
			case -1:
				_pledgeClass = RANK_VAGABOND;
				break;
			case 0:
			case 1:
			case 2:
			case 3:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_HEIR;
				}
				else
				{
					_pledgeClass = RANK_VASSAL;
				}
				break;
			case 4:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_KNIGHT;
				}
				else
				{
					_pledgeClass = RANK_HEIR;
				}
				break;
			case 5:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_WISEMAN;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else
				{
					_pledgeClass = RANK_HEIR;
				}
				break;
			case 6:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_BARON;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else if (IS_GUARD_CAPTAIN)
				{
					_pledgeClass = RANK_WISEMAN;
				}
				else if (IS_GUARD)
				{
					_pledgeClass = RANK_HEIR;
				}
				else
				{
					_pledgeClass = RANK_KNIGHT;
				}
				break;
			case 7:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_COUNT;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else if (IS_GUARD_CAPTAIN)
				{
					_pledgeClass = RANK_VISCOUNT;
				}
				else if (IS_GUARD)
				{
					_pledgeClass = RANK_KNIGHT;
				}
				else if (IS_KNIGHT_COMMANDER)
				{
					_pledgeClass = RANK_BARON;
				}
				else if (IS_KNIGHT)
				{
					_pledgeClass = RANK_HEIR;
				}
				else
				{
					_pledgeClass = RANK_WISEMAN;
				}
				break;
			case 8:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_MARQUIS;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else if (IS_GUARD_CAPTAIN)
				{
					_pledgeClass = RANK_COUNT;
				}
				else if (IS_GUARD)
				{
					_pledgeClass = RANK_WISEMAN;
				}
				else if (IS_KNIGHT_COMMANDER)
				{
					_pledgeClass = RANK_VISCOUNT;
				}
				else if (IS_KNIGHT)
				{
					_pledgeClass = RANK_KNIGHT;
				}
				else
				{
					_pledgeClass = RANK_BARON;
				}
				break;
			case 9:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_DUKE;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else if (IS_GUARD_CAPTAIN)
				{
					_pledgeClass = RANK_MARQUIS;
				}
				else if (IS_GUARD)
				{
					_pledgeClass = RANK_BARON;
				}
				else if (IS_KNIGHT_COMMANDER)
				{
					_pledgeClass = RANK_COUNT;
				}
				else if (IS_KNIGHT)
				{
					_pledgeClass = RANK_WISEMAN;
				}
				else
				{
					_pledgeClass = RANK_VISCOUNT;
				}
				break;
			case 10:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_GRAND_DUKE;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else if (IS_GUARD)
				{
					_pledgeClass = RANK_VISCOUNT;
				}
				else if (IS_KNIGHT)
				{
					_pledgeClass = RANK_BARON;
				}
				else if (IS_GUARD_CAPTAIN)
				{
					_pledgeClass = RANK_DUKE;
				}
				else if (IS_KNIGHT_COMMANDER)
				{
					_pledgeClass = RANK_MARQUIS;
				}
				else
				{
					_pledgeClass = RANK_COUNT;
				}
				break;
			case 11:
				if (IS_LEADER)
				{
					_pledgeClass = RANK_DISTINGUISHED_KING;
				}
				else if (IN_ACADEMY)
				{
					_pledgeClass = RANK_VASSAL;
				}
				else if (IS_GUARD)
				{
					_pledgeClass = RANK_COUNT;
				}
				else if (IS_KNIGHT)
				{
					_pledgeClass = RANK_VISCOUNT;
				}
				else if (IS_GUARD_CAPTAIN)
				{
					_pledgeClass = RANK_GRAND_DUKE;
				}
				else if (IS_KNIGHT_COMMANDER)
				{
					_pledgeClass = RANK_DUKE;
				}
				else
				{
					_pledgeClass = RANK_MARQUIS;
				}
				break;
		}
		if (_hero && (_pledgeClass < RANK_MARQUIS))
		{
			_pledgeClass = RANK_MARQUIS;
		}
		else if (_noble && (_pledgeClass < RANK_BARON))
		{
			_pledgeClass = RANK_BARON;
		}
	}
	
	/**
	 * Method setPowerGrade.
	 * @param grade int
	 */
	public void setPowerGrade(final int grade)
	{
		_powerGrade = grade;
	}
	
	/**
	 * Method getPowerGrade.
	 * @return int
	 */
	public int getPowerGrade()
	{
		return _powerGrade;
	}
	
	/**
	 * Method setApprentice.
	 * @param apprentice int
	 */
	public void setApprentice(final int apprentice)
	{
		_apprentice = apprentice;
	}
	
	/**
	 * Method getApprentice.
	 * @return int
	 */
	public int getApprentice()
	{
		return _apprentice;
	}
	
	/**
	 * Method getSponsor.
	 * @return int
	 */
	public int getSponsor()
	{
		return _clan == null ? 0 : _clan.getAnyMember(getObjectId()).getSponsor();
	}
	
	/**
	 * Method getNameColor.
	 * @return int
	 */
	public int getNameColor()
	{
		if (isInObserverMode())
		{
			return Color.black.getRGB();
		}
		return _nameColor;
	}
	
	/**
	 * Method setNameColor.
	 * @param nameColor int
	 */
	public void setNameColor(final int nameColor)
	{
		if ((nameColor != Config.NORMAL_NAME_COLOUR) && (nameColor != Config.CLANLEADER_NAME_COLOUR) && (nameColor != Config.GM_NAME_COLOUR) && (nameColor != Config.SERVICES_OFFLINE_TRADE_NAME_COLOR))
		{
			setVar("namecolor", Integer.toHexString(nameColor), -1);
		}
		else if (nameColor == Config.NORMAL_NAME_COLOUR)
		{
			unsetVar("namecolor");
		}
		_nameColor = nameColor;
	}
	
	/**
	 * Method setNameColor.
	 * @param red int
	 * @param green int
	 * @param blue int
	 */
	public void setNameColor(final int red, final int green, final int blue)
	{
		_nameColor = (red & 0xFF) + ((green & 0xFF) << 8) + ((blue & 0xFF) << 16);
		if ((_nameColor != Config.NORMAL_NAME_COLOUR) && (_nameColor != Config.CLANLEADER_NAME_COLOUR) && (_nameColor != Config.GM_NAME_COLOUR) && (_nameColor != Config.SERVICES_OFFLINE_TRADE_NAME_COLOR))
		{
			setVar("namecolor", Integer.toHexString(_nameColor), -1);
		}
		else
		{
			unsetVar("namecolor");
		}
	}
	
	/**
	 * Field user_variables.
	 */
	private final Map<String, String> user_variables = new ConcurrentHashMap<String, String>();
	
	/**
	 * Method setVar.
	 * @param name String
	 * @param value String
	 * @param expirationTime long
	 */
	public void setVar(String name, String value, long expirationTime)
	{
		user_variables.put(name, value);
		mysql.set("REPLACE INTO character_variables (obj_id, type, name, value, expire_time) VALUES (?,'user-var',?,?,?)", getObjectId(), name, value, expirationTime);
	}
	
	/**
	 * Method setVar.
	 * @param name String
	 * @param value int
	 * @param expirationTime long
	 */
	public void setVar(String name, int value, long expirationTime)
	{
		setVar(name, String.valueOf(value), expirationTime);
	}
	
	/**
	 * Method setVar.
	 * @param name String
	 * @param value long
	 * @param expirationTime long
	 */
	public void setVar(String name, long value, long expirationTime)
	{
		setVar(name, String.valueOf(value), expirationTime);
	}
	
	/**
	 * Method unsetVar.
	 * @param name String
	 */
	public void unsetVar(String name)
	{
		if (name == null)
		{
			return;
		}
		if (user_variables.remove(name) != null)
		{
			mysql.set("DELETE FROM `character_variables` WHERE `obj_id`=? AND `type`='user-var' AND `name`=? LIMIT 1", getObjectId(), name);
		}
	}
	
	/**
	 * Method getVar.
	 * @param name String
	 * @return String
	 */
	public String getVar(String name)
	{
		return user_variables.get(name);
	}
	
	/**
	 * Method getVarB.
	 * @param name String
	 * @param defaultVal boolean
	 * @return boolean
	 */
	public boolean getVarB(String name, boolean defaultVal)
	{
		String var = user_variables.get(name);
		if (var == null)
		{
			return defaultVal;
		}
		return !(var.equals("0") || var.equalsIgnoreCase("false"));
	}
	
	/**
	 * Method getVarB.
	 * @param name String
	 * @return boolean
	 */
	public boolean getVarB(String name)
	{
		String var = user_variables.get(name);
		return !((var == null) || var.equals("0") || var.equalsIgnoreCase("false"));
	}
	
	/**
	 * Method getVarLong.
	 * @param name String
	 * @return long
	 */
	public long getVarLong(String name)
	{
		return getVarLong(name, 0L);
	}
	
	/**
	 * Method getVarLong.
	 * @param name String
	 * @param defaultVal long
	 * @return long
	 */
	public long getVarLong(String name, long defaultVal)
	{
		long result = defaultVal;
		String var = getVar(name);
		if (var != null)
		{
			result = Long.parseLong(var);
		}
		return result;
	}
	
	/**
	 * Method getVarInt.
	 * @param name String
	 * @return int
	 */
	public int getVarInt(String name)
	{
		return getVarInt(name, 0);
	}
	
	/**
	 * Method getVarInt.
	 * @param name String
	 * @param defaultVal int
	 * @return int
	 */
	public int getVarInt(String name, int defaultVal)
	{
		int result = defaultVal;
		String var = getVar(name);
		if (var != null)
		{
			result = Integer.parseInt(var);
		}
		return result;
	}
	
	/**
	 * Method getVars.
	 * @return Map<String,String>
	 */
	public Map<String, String> getVars()
	{
		return user_variables;
	}
	
	/**
	 * Method loadVariables.
	 */
	private void loadVariables()
	{
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT * FROM character_variables WHERE obj_id = ?");
			offline.setInt(1, getObjectId());
			rs = offline.executeQuery();
			while (rs.next())
			{
				String name = rs.getString("name");
				String value = Strings.stripSlashes(rs.getString("value"));
				user_variables.put(name, value);
			}
			if (getVar("lang@") == null)
			{
				setVar("lang@", Config.DEFAULT_LANG, -1);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
	}
	
	/**
	 * Method getVarFromPlayer.
	 * @param objId int
	 * @param var String
	 * @return String
	 */
	public static String getVarFromPlayer(int objId, String var)
	{
		String value = null;
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT value FROM character_variables WHERE obj_id = ? AND name = ?");
			offline.setInt(1, objId);
			offline.setString(2, var);
			rs = offline.executeQuery();
			if (rs.next())
			{
				value = Strings.stripSlashes(rs.getString("value"));
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
		return value;
	}
	
	/**
	 * Method getLang.
	 * @return String
	 */
	public String getLang()
	{
		return getVar("lang@");
	}
	
	/**
	 * Method getLanguage.
	 * @return Language
	 */
	public Language getLanguage()
	{
		return Language.ENGLISH;
	}
	
	/**
	 * Method isAtWarWith.
	 * @param id Integer
	 * @return int
	 */
	public int isAtWarWith(final Integer id)
	{
		return (_clan == null) || !_clan.isAtWarWith(id) ? 0 : 1;
	}
	
	/**
	 * Method isAtWar.
	 * @return int
	 */
	public int isAtWar()
	{
		return (_clan == null) || (_clan.isAtWarOrUnderAttack() <= 0) ? 0 : 1;
	}
	
	/**
	 * Method stopWaterTask.
	 */
	public void stopWaterTask()
	{
		if (_taskWater != null)
		{
			_taskWater.cancel(false);
			_taskWater = null;
			sendPacket(new SetupGauge(this, SetupGauge.BLUE_MINI, 0));
			sendChanges();
		}
	}
	
	/**
	 * Method startWaterTask.
	 */
	public void startWaterTask()
	{
		if (isDead())
		{
			stopWaterTask();
		}
		else if (Config.ALLOW_WATER && (_taskWater == null))
		{
			int timeinwater = (int) (calcStat(Stats.BREATH, getTemplate().getBaseBreathBonus(), null, null) * 1000L);
			sendPacket(new SetupGauge(this, SetupGauge.BLUE_MINI, timeinwater));
			if ((getTransformation() > 0) && (getTransformationTemplate() > 0) && !isCursedWeaponEquipped())
			{
				setTransformation(0);
			}
			_taskWater = ThreadPoolManager.getInstance().scheduleAtFixedRate(new WaterTask(this), timeinwater, 1000L);
			sendChanges();
		}
	}
	
	/**
	 * Method doRevive.
	 * @param percent double
	 */
	public void doRevive(double percent)
	{
		restoreExp(percent);
		doRevive();
	}
	
	/**
	 * Method doRevive.
	 */
	@Override
	public void doRevive()
	{
		super.doRevive();
		setAgathionRes(false);
		unsetVar("lostexp");
		getDeathPenalty().castEffect(this);
		updateEffectIcons();
		autoShot();
	}
	
	/**
	 * Method reviveRequest.
	 * @param reviver Player
	 * @param percent double
	 * @param pet boolean
	 */
	public void reviveRequest(Player reviver, double percent, boolean pet)
	{
		ReviveAnswerListener reviveAsk = (_askDialog != null) && (_askDialog.getValue() instanceof ReviveAnswerListener) ? (ReviveAnswerListener) _askDialog.getValue() : null;
		if (reviveAsk != null)
		{
			if ((reviveAsk.isForPet() == pet) && (reviveAsk.getPower() >= percent))
			{
				reviver.sendPacket(Msg.BETTER_RESURRECTION_HAS_BEEN_ALREADY_PROPOSED);
				return;
			}
			if (pet && !reviveAsk.isForPet())
			{
				reviver.sendPacket(Msg.SINCE_THE_MASTER_WAS_IN_THE_PROCESS_OF_BEING_RESURRECTED_THE_ATTEMPT_TO_RESURRECT_THE_PET_HAS_BEEN_CANCELLED);
				return;
			}
			if (pet && isDead())
			{
				reviver.sendPacket(Msg.WHILE_A_PET_IS_ATTEMPTING_TO_RESURRECT_IT_CANNOT_HELP_IN_RESURRECTING_ITS_MASTER);
				return;
			}
		}
		PetInstance petInstance = getSummonList().getPet();
		if ((pet && (petInstance != null) && petInstance.isDead()) || (!pet && isDead()))
		{
			ConfirmDlg pkt = new ConfirmDlg(SystemMsg.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU, 0);
			pkt.addName(reviver).addString(Math.round(percent) + " percent");
			ask(pkt, new ReviveAnswerListener(this, percent, pet));
		}
		WorldStatisticsManager.getInstance().updateStat(reviver, CategoryType.RESURRECTED_CHAR_COUNT, 1);
		WorldStatisticsManager.getInstance().updateStat(this, CategoryType.RESURRECTED_BY_OTHER_COUNT, 1);
	}
	
	/**
	 * Method summonCharacterRequest.
	 * @param summoner Creature
	 * @param loc Location
	 * @param summonConsumeCrystal int
	 */
	public void summonCharacterRequest(final Creature summoner, final Location loc, final int summonConsumeCrystal)
	{
		ConfirmDlg cd = new ConfirmDlg(SystemMsg.C1_WISHES_TO_SUMMON_YOU_FROM_S2, 60000);
		cd.addName(summoner).addZoneName(loc);
		ask(cd, new SummonAnswerListener(this, loc, summonConsumeCrystal));
	}
	
	/**
	 * Method scriptRequest.
	 * @param text String
	 * @param scriptName String
	 * @param args Object[]
	 */
	public void scriptRequest(String text, String scriptName, Object[] args)
	{
		ask(new ConfirmDlg(SystemMsg.S1, 30000).addString(text), new ScriptAnswerListener(this, scriptName, args));
	}
	
	/**
	 * Method updateNoChannel.
	 * @param time long
	 */
	public void updateNoChannel(final long time)
	{
		setNoChannel(time);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			final String stmt = "UPDATE characters SET nochannel = ? WHERE obj_Id=?";
			statement = con.prepareStatement(stmt);
			statement.setLong(1, _NoChannel > 0 ? _NoChannel / 1000 : _NoChannel);
			statement.setInt(2, getObjectId());
			statement.executeUpdate();
		}
		catch (final Exception e)
		{
			_log.warn("Could not activate nochannel:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		sendPacket(new EtcStatusUpdate(this));
	}
	
	/**
	 * Method checkRecom.
	 */
	private void checkRecom()
	{
		Calendar temp = Calendar.getInstance();
		temp.set(Calendar.HOUR_OF_DAY, 6);
		temp.set(Calendar.MINUTE, 30);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		long count = Math.round(((System.currentTimeMillis() / 1000) - _lastAccess) / 86400);
		if ((count == 0) && (_lastAccess < (temp.getTimeInMillis() / 1000)) && (System.currentTimeMillis() > temp.getTimeInMillis()))
		{
			count++;
		}
		for (int i = 1; i < count; i++)
		{
			setRecomHave(getRecomHave() - 20);
		}
		if (count > 0)
		{
			restartRecom();
		}
	}
	
	/**
	 * Method restartRecom.
	 */
	public void restartRecom()
	{
		setRecomBonusTime(3600);
		setRecomLeftToday(0);
		setRecomLeft(20);
		setRecomHave(getRecomHave() - 20);
		stopRecomBonusTask(false);
		startRecomBonusTask();
		sendUserInfo();
		sendVoteSystemInfo();
	}
	
	/**
	 * Method isInBoat.
	 * @return boolean
	 */
	@Override
	public boolean isInBoat()
	{
		return _boat != null;
	}
	
	/**
	 * Method getBoat.
	 * @return Boat
	 */
	@Override
	public Boat getBoat()
	{
		return _boat;
	}
	
	/**
	 * Method setBoat.
	 * @param boat Boat
	 */
	@Override
	public void setBoat(Boat boat)
	{
		_boat = boat;
	}
	
	/**
	 * Method getInBoatPosition.
	 * @return Location
	 */
	@Override
	public Location getInBoatPosition()
	{
		return _inBoatPosition;
	}
	
	/**
	 * Method setInBoatPosition.
	 * @param loc Location
	 */
	@Override
	public void setInBoatPosition(Location loc)
	{
		_inBoatPosition = loc;
	}
	
	/**
	 * Method getActiveSubClass.
	 * @return SubClass
	 */
	public SubClass getActiveSubClass()
	{
		return _subClassList.getActiveSubClass();
	}
	
	/**
	 * Method getActiveClassId.
	 * @return int
	 */
	public int getActiveClassId()
	{
		if (getActiveSubClass() != null)
		{
			return getActiveSubClass().getClassId();
		}
		return -1;
	}
	
	/**
	 * Method getActiveDefaultClassId.
	 * @return int
	 */
	public int getActiveDefaultClassId()
	{
		if (getActiveSubClass() != null)
		{
			return getActiveSubClass().getDefaultClassId();
		}
		return -1;
	}
	
	/**
	 * Method changeClassInDb.
	 * @param oldclass int
	 * @param newclass int
	 * @param defaultClass int
	 */
	private synchronized void changeClassInDb(final int oldclass, final int newclass, final int defaultClass)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE character_subclasses SET class_id=?, default_class_id=? WHERE char_obj_id=? AND class_id=?");
			statement.setInt(1, newclass);
			statement.setInt(2, defaultClass);
			statement.setInt(3, getObjectId());
			statement.setInt(4, oldclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE olympiad_nobles SET class_id=? WHERE char_id=?");
			statement.setInt(1, newclass);
			statement.setInt(2, getObjectId());
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_hennas WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, newclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE character_hennas SET class_index=? WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, newclass);
			statement.setInt(2, getObjectId());
			statement.setInt(3, oldclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE object_id=? AND class_index=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, newclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE character_shortcuts SET class_index=? WHERE object_id=? AND class_index=?");
			statement.setInt(1, newclass);
			statement.setInt(2, getObjectId());
			statement.setInt(3, oldclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, newclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE character_skills SET class_index=? WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, newclass);
			statement.setInt(2, getObjectId());
			statement.setInt(3, oldclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_effects_save WHERE object_id=? AND id=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, newclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE character_effects_save SET id=? WHERE object_id=? AND id=?");
			statement.setInt(1, newclass);
			statement.setInt(2, getObjectId());
			statement.setInt(3, oldclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_skills_save WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, getObjectId());
			statement.setInt(2, newclass);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE character_skills_save SET class_index=? WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, newclass);
			statement.setInt(2, getObjectId());
			statement.setInt(3, oldclass);
			statement.executeUpdate();
			DbUtils.close(statement);
		}
		catch (final SQLException e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method storeCharSubClasses.
	 */
	public void storeCharSubClasses()
	{
		SubClass main = getActiveSubClass();
		if (main != null)
		{
			main.setCp(getCurrentCp());
			main.setHp(getCurrentHp());
			main.setMp(getCurrentMp());
		}
		else
		{
			_log.warn("Could not store char sub data, main class " + getActiveClassId() + " not found for " + this);
		}
		CharacterSubclassDAO.getInstance().store(this);
	}
	
	/**
	 * Method addSubClass.
	 * @param classId int
	 * @param storeOld boolean
	 * @param certification int
	 * @return boolean
	 */
	public boolean addSubClass(final int classId, boolean storeOld, int certification, int dual_certification, boolean isDual, int index)
	{
		final SubClass newClass = new SubClass();
		SubClassType type = SubClassType.SUBCLASS;
		int level = 40;
		if(isDual)
		{
			type = SubClassType.DOUBLE_SUBCLASS;
			level = 85;
		}
		newClass.setType(type);
		newClass.setClassId(classId);
		newClass.setCertification(certification);
		if (index > 0 && index < 5)
		{
			getSubClassList().addToIndex(newClass, index);
		}
		else if (!getSubClassList().add(newClass))
		{
			return false;
		}		
		final long exp = Experience.getExpForLevel(level);
		final double hp = getMaxHp();
		final double mp = getMaxMp();
		final double cp = getMaxCp();
		if (!CharacterSubclassDAO.getInstance().insert(getObjectId(), classId, classId, exp, 0, hp, mp, cp, hp, mp, cp, level, false, type, null, certification, dual_certification))
		{
			return false;
		}
		setActiveSubClass(classId, storeOld, exp);
		HashMap <Integer,Integer> certificationSkills = getCertSkill();
		for(Iterator <Entry<Integer,Integer>> i = certificationSkills.entrySet().iterator();i.hasNext();)
		{
			Map.Entry<Integer,Integer> e = i.next();
			Skill skl = SkillTable.getInstance().getInfo(e.getKey(), e.getValue());
			addSkill(skl,true);									
		}
		rewardSkills(false,false);
		sendSkillList();
		setCurrentHpMp(getMaxHp(), getMaxMp(), true);
		setCurrentCp(getMaxCp());
		return true;
	}
	
	/**
	 * Method modifySubClass.
	 * @param oldClassId int
	 * @param newClassId int
	 * @return boolean
	 */
	public boolean modifySubClass(final int oldClassId, final int newClassId, boolean isDual)
	{
		final SubClass originalClass = getSubClassList().getByClassId(oldClassId);
		if ((originalClass == null) || originalClass.isBase())
		{
			return false;
		}
		final int dual_certification = originalClass.getCertification();
		final int certification = originalClass.getCertification();
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_subclasses WHERE char_obj_id=? AND class_id=? AND type != " + SubClassType.BASE_CLASS.ordinal());
			statement.setInt(1, getObjectId());
			statement.setInt(2, oldClassId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=? AND class_index=? ");
			statement.setInt(1, getObjectId());
			statement.setInt(2, oldClassId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_skills_save WHERE char_obj_id=? AND class_index=? ");
			statement.setInt(1, getObjectId());
			statement.setInt(2, oldClassId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_effects_save WHERE object_id=? AND id=? ");
			statement.setInt(1, getObjectId());
			statement.setInt(2, oldClassId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_hennas WHERE char_obj_id=? AND class_index=? ");
			statement.setInt(1, getObjectId());
			statement.setInt(2, oldClassId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE object_id=? AND class_index=? ");
			statement.setInt(1, getObjectId());
			statement.setInt(2, oldClassId);
			statement.execute();
			DbUtils.close(statement);
		}
		catch (final Exception e)
		{
			_log.warn("Could not delete char sub-class: " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		int index = getSubClassList().getByClassId(oldClassId).getIndex();
		getSubClassList().removeByClassId(oldClassId);
		return (newClassId <= 0) || addSubClass(newClassId, false, certification, dual_certification, isDual, index);
	}
	
	/**
	 * Method setActiveSubClass.
	 * @param subId int
	 * @param store boolean
	 */
	public void setActiveSubClass(int subId, boolean store, long exp)
	{
		if (!_subClassOperationLock.tryLock())
		{
			return;
		}
		try
		{
			SubClass oldActiveSub = getActiveSubClass();
			if (oldActiveSub != null)
			{
				EffectsDAO.getInstance().insert(this);
				storeDisableSkills();
				if (QuestManager.getQuest(422) != null)
				{
					String qn = QuestManager.getQuest(422).getName();
					if (qn != null)
					{
						QuestState qs = getQuestState(qn);
						if (qs != null)
						{
							qs.exitCurrentQuest(true);
						}
					}
				}
			}
			if (store)
			{
				oldActiveSub.setCp(getCurrentCp());
				oldActiveSub.setHp(getCurrentHp());
				oldActiveSub.setMp(getCurrentMp());
			}
			SubClass newActiveSub = _subClassList.changeActiveSubClass(subId);
			_template = PlayerTemplateHolder.getInstance().getPlayerTemplate(getRace(), getClassId(), Sex.VALUES[getSex()]);
			setClassId(subId, true, false);
			if(exp > 0)
			{
				newActiveSub.setExp(exp);
			}
			removeAllSkills();
			getEffectList().stopAllEffects();
			getSummonList().unsummonAllServitors();
			PetInstance pet = getSummonList().getPet();
			if ((pet != null) && (Config.ALT_IMPROVED_PETS_LIMITED_USE && (((pet.getNpcId() == PetDataTable.IMPROVED_BABY_KOOKABURRA_ID) && !isMageClass()) || ((pet.getNpcId() == PetDataTable.IMPROVED_BABY_BUFFALO_ID) && isMageClass()))))
			{
				getSummonList().unsummonPet(false);
			}
			getSummonList().unsummonAll(false);
			setAgathion(0);
			restoreSkills();
			rewardSkills(false,false);
			checkSkills();
			sendPacket(new ExStorageMaxCount(this));
			refreshExpertisePenalty();
			getInventory().refreshEquip();
			getInventory().validateItems();
			for (int i = 0; i < 3; i++)
			{
				_henna[i] = null;
			}
			restoreHenna();
			sendPacket(new HennaInfo(this));
			EffectsDAO.getInstance().restoreEffects(this);
			restoreDisableSkills();
			setCurrentHpMp(newActiveSub.getHp(), newActiveSub.getMp());
			setCurrentCp(newActiveSub.getCp());
			_shortCuts.restore();
			sendPacket(new ShortCutInit(this));
			for (int shotId : getAutoSoulShot())
			{
				sendPacket(new ExAutoSoulShot(shotId, true));
			}
			sendPacket(new SkillCoolTime(this));
			sendPacket(new ExSubjobInfo(this, false));
			broadcastPacket(new SocialAction(getObjectId(), SocialAction.LEVEL_UP));
			getDeathPenalty().restore(this);
			setIncreasedForce(0);
			startHourlyTask();
			sendUserInfo();
			sendSkillList();
			broadcastCharInfo();
			updateEffectIcons();
			updateStats();
		}
		finally
		{
			_subClassOperationLock.unlock();
		}
	}
	
	/**
	 * Method startKickTask.
	 * @param delayMillis long
	 */
	public void startKickTask(long delayMillis)
	{
		stopKickTask();
		_kickTask = ThreadPoolManager.getInstance().schedule(new KickTask(this), delayMillis);
	}
	
	/**
	 * Method stopKickTask.
	 */
	public void stopKickTask()
	{
		if (_kickTask != null)
		{
			_kickTask.cancel(false);
			_kickTask = null;
		}
	}
	
	/**
	 * Method startBonusTask.
	 */
	public void startBonusTask()
	{
		if (Config.SERVICES_RATE_TYPE != Bonus.NO_BONUS)
		{
			int bonusExpire = getNetConnection().getBonusExpire();
			double bonus = getNetConnection().getBonus();
			if (bonusExpire > (System.currentTimeMillis() / 1000L))
			{
				getBonus().setRateXp(bonus);
				getBonus().setRateSp(bonus);
				getBonus().setDropAdena(bonus);
				getBonus().setDropItems(bonus);
				getBonus().setDropSpoil(bonus);
				getBonus().setBonusExpire(bonusExpire);
				if (_bonusExpiration == null)
				{
					_bonusExpiration = LazyPrecisionTaskManager.getInstance().startBonusExpirationTask(this);
				}
			}
			else if ((bonus > 0) && (Config.SERVICES_RATE_TYPE == Bonus.BONUS_GLOBAL_ON_GAMESERVER))
			{
				AccountBonusDAO.getInstance().delete(getAccountName());
			}
		}
	}
	
	/**
	 * Method stopBonusTask.
	 */
	public void stopBonusTask()
	{
		if (_bonusExpiration != null)
		{
			_bonusExpiration.cancel(false);
			_bonusExpiration = null;
		}
	}
	
	/**
	 * Method getInventoryLimit.
	 * @return int
	 */
	@Override
	public int getInventoryLimit()
	{
		return (int) calcStat(Stats.INVENTORY_LIMIT, 0, null, null);
	}
	
	/**
	 * Method getWarehouseLimit.
	 * @return int
	 */
	public int getWarehouseLimit()
	{
		return (int) calcStat(Stats.STORAGE_LIMIT, 0, null, null);
	}
	
	/**
	 * Method getTradeLimit.
	 * @return int
	 */
	public int getTradeLimit()
	{
		return (int) calcStat(Stats.TRADE_LIMIT, 0, null, null);
	}
	
	/**
	 * Method getDwarvenRecipeLimit.
	 * @return int
	 */
	public int getDwarvenRecipeLimit()
	{
		return (int) calcStat(Stats.DWARVEN_RECIPE_LIMIT, 50, null, null) + Config.ALT_ADD_RECIPES;
	}
	
	/**
	 * Method getCommonRecipeLimit.
	 * @return int
	 */
	public int getCommonRecipeLimit()
	{
		return (int) calcStat(Stats.COMMON_RECIPE_LIMIT, 50, null, null) + Config.ALT_ADD_RECIPES;
	}
	
	/**
	 * Method getAttackElement.
	 * @return Element
	 */
	public Element getAttackElement()
	{
		return Formulas.getAttackElement(this, null);
	}
	
	/**
	 * Method getAttack.
	 * @param element Element
	 * @return int
	 */
	public int getAttack(Element element)
	{
		if (element == Element.NONE)
		{
			return 0;
		}
		return (int) calcStat(element.getAttack(), 0., null, null);
	}
	
	/**
	 * Method getDefence.
	 * @param element Element
	 * @return int
	 */
	public int getDefence(Element element)
	{
		if (element == Element.NONE)
		{
			return 0;
		}
		return (int) calcStat(element.getDefence(), 0., null, null);
	}
	
	/**
	 * Method getAndSetLastItemAuctionRequest.
	 * @return boolean
	 */
	public boolean getAndSetLastItemAuctionRequest()
	{
		if ((_lastItemAuctionInfoRequest + 2000L) < System.currentTimeMillis())
		{
			_lastItemAuctionInfoRequest = System.currentTimeMillis();
			return true;
		}
		_lastItemAuctionInfoRequest = System.currentTimeMillis();
		return false;
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	@Override
	public int getNpcId()
	{
		return -2;
	}
	
	/**
	 * Method getVisibleObject.
	 * @param id int
	 * @return GameObject
	 */
	public GameObject getVisibleObject(int id)
	{
		if (getObjectId() == id)
		{
			return this;
		}
		GameObject target = null;
		if (getTargetId() == id)
		{
			target = getTarget();
		}
		if ((target == null) && (_party != null))
		{
			for (Player p : _party.getPartyMembers())
			{
				if ((p != null) && (p.getObjectId() == id))
				{
					target = p;
					break;
				}
			}
		}
		if (target == null)
		{
			target = World.getAroundObjectById(this, id);
		}
		return (target == null) || target.isInvisible() ? null : target;
	}
	
	/**
	 * Method getPAtk.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPAtk(final Creature target)
	{
		double init = getActiveWeaponInstance() == null ? (isMageClass() ? 3 : 4) : 0;
		return (int) calcStat(Stats.POWER_ATTACK, init, target, null);
	}
	
	/**
	 * Method getPDef.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPDef(Creature target)
	{
		int init = 0;
		ItemInstance chest = getInventory().getPaperdollItem(10);
		if (chest == null)
		{
			init += getTemplate().getArmDef().getChestDef();
		}
		if ((getInventory().getPaperdollItem(11) == null) && ((chest == null) || (chest.getBodyPart() != 32768)))
		{
			init += getTemplate().getArmDef().getLegsDef();
		}
		if (getInventory().getPaperdollItem(6) == null)
		{
			init += getTemplate().getArmDef().getHelmetDef();
		}
		if (getInventory().getPaperdollItem(9) == null)
		{
			init += getTemplate().getArmDef().getGlovesDef();
		}
		if (getInventory().getPaperdollItem(12) == null)
		{
			init += getTemplate().getArmDef().getBootsDef();
		}
		if (getInventory().getPaperdollItem(0) == null)
		{
			init += getTemplate().getArmDef().getUnderwearDef();
		}
		if (getInventory().getPaperdollItem(13) == null)
		{
			init += getTemplate().getArmDef().getCloakDef();
		}
		return (int) calcStat(Stats.POWER_DEFENCE, init, target, null);
	}
	
	/**
	 * Method getMDef.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMDef(Creature target, Skill skill)
	{
		int init = 0;
		if (getInventory().getPaperdollItem(2) == null)
		{
			init += getTemplate().getJewlDef().getLEaaringDef();
		}
		if (getInventory().getPaperdollItem(1) == null)
		{
			init += getTemplate().getJewlDef().getREaaringDef();
		}
		if (getInventory().getPaperdollItem(3) == null)
		{
			init += getTemplate().getJewlDef().getNecklaceDef();
		}
		if (getInventory().getPaperdollItem(5) == null)
		{
			init += getTemplate().getJewlDef().getLRingDef();
		}
		if (getInventory().getPaperdollItem(4) == null)
		{
			init += getTemplate().getJewlDef().getRRingDef();
		}
		return (int) calcStat(Stats.MAGIC_DEFENCE, init, target, skill);
	}
	
	/**
	 * Method getTitle.
	 * @return String
	 */
	@Override
	public String getTitle()
	{
		return super.getTitle();
	}
	
	/**
	 * Method getTitleColor.
	 * @return int
	 */
	public int getTitleColor()
	{
		return _titlecolor;
	}
	
	/**
	 * Method setTitleColor.
	 * @param titlecolor int
	 */
	public void setTitleColor(final int titlecolor)
	{
		if (titlecolor != DEFAULT_TITLE_COLOR)
		{
			setVar("titlecolor", Integer.toHexString(titlecolor), -1);
		}
		else
		{
			unsetVar("titlecolor");
		}
		_titlecolor = titlecolor;
	}
	
	/**
	 * Method isCursedWeaponEquipped.
	 * @return boolean
	 */
	@Override
	public boolean isCursedWeaponEquipped()
	{
		return _cursedWeaponEquippedId != 0;
	}
	
	/**
	 * Method setCursedWeaponEquippedId.
	 * @param value int
	 */
	public void setCursedWeaponEquippedId(int value)
	{
		_cursedWeaponEquippedId = value;
	}
	
	/**
	 * Method getCursedWeaponEquippedId.
	 * @return int
	 */
	public int getCursedWeaponEquippedId()
	{
		return _cursedWeaponEquippedId;
	}
	
	/**
	 * Method isImmobilized.
	 * @return boolean
	 */
	@Override
	public boolean isImmobilized()
	{
		return super.isImmobilized() || isOverloaded() || isSitting() || isFishing();
	}
	
	/**
	 * Method isBlocked.
	 * @return boolean
	 */
	@Override
	public boolean isBlocked()
	{
		return super.isBlocked() || isInMovie() || isInObserverMode() || isTeleporting() || isLogoutStarted();
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return super.isInvul() || isInMovie();
	}
	
	/**
	 * Method setOverloaded.
	 * @param overloaded boolean
	 */
	public void setOverloaded(boolean overloaded)
	{
		_overloaded = overloaded;
	}
	
	/**
	 * Method isOverloaded.
	 * @return boolean
	 */
	public boolean isOverloaded()
	{
		return _overloaded;
	}
	
	/**
	 * Method isFishing.
	 * @return boolean
	 */
	public boolean isFishing()
	{
		return _isFishing;
	}
	
	/**
	 * Method getFishing.
	 * @return Fishing
	 */
	public Fishing getFishing()
	{
		return _fishing;
	}
	
	/**
	 * Method setFishing.
	 * @param value boolean
	 */
	public void setFishing(boolean value)
	{
		_isFishing = value;
	}
	
	/**
	 * Method startFishing.
	 * @param fish FishTemplate
	 * @param lureId int
	 */
	public void startFishing(FishTemplate fish, int lureId)
	{
		_fishing.setFish(fish);
		_fishing.setLureId(lureId);
		_fishing.startFishing();
	}
	
	/**
	 * Method stopFishing.
	 */
	public void stopFishing()
	{
		_fishing.stopFishing();
	}
	
	/**
	 * Method getFishLoc.
	 * @return Location
	 */
	public Location getFishLoc()
	{
		return _fishing.getFishLoc();
	}
	
	/**
	 * Method getBonus.
	 * @return Bonus
	 */
	public Bonus getBonus()
	{
		return _bonus;
	}
	
	/**
	 * Method hasBonus.
	 * @return boolean
	 */
	public boolean hasBonus()
	{
		return _bonus.getBonusExpire() > (System.currentTimeMillis() / 1000L);
	}
	
	/**
	 * Method getRateAdena.
	 * @return double
	 */
	@Override
	public double getRateAdena()
	{
		return _party == null ? _bonus.getDropAdena() : _party._rateAdena;
	}
	
	/**
	 * Method getRateItems.
	 * @return double
	 */
	@Override
	public double getRateItems()
	{
		return _party == null ? _bonus.getDropItems() : _party._rateDrop;
	}
	
	/**
	 * Method getRateExp.
	 * @return double
	 */
	@Override
	public double getRateExp()
	{
		return calcStat(Stats.EXP, (_party == null ? _bonus.getRateXp() : _party._rateExp), null, null);
	}
	
	/**
	 * Method getRateSp.
	 * @return double
	 */
	@Override
	public double getRateSp()
	{
		return calcStat(Stats.SP, (_party == null ? _bonus.getRateSp() : _party._rateSp), null, null);
	}
	
	/**
	 * Method getRateSpoil.
	 * @return double
	 */
	@Override
	public double getRateSpoil()
	{
		return _party == null ? _bonus.getDropSpoil() : _party._rateSpoil;
	}
	
	/**
	 * Field _maried.
	 */
	private boolean _maried = false;
	/**
	 * Field _partnerId.
	 */
	private int _partnerId = 0;
	/**
	 * Field _coupleId.
	 */
	private int _coupleId = 0;
	/**
	 * Field _maryrequest.
	 */
	private boolean _maryrequest = false;
	/**
	 * Field _maryaccepted.
	 */
	private boolean _maryaccepted = false;
	
	/**
	 * Method isMaried.
	 * @return boolean
	 */
	public boolean isMaried()
	{
		return _maried;
	}
	
	/**
	 * Method setMaried.
	 * @param state boolean
	 */
	public void setMaried(boolean state)
	{
		_maried = state;
	}
	
	/**
	 * Method setMaryRequest.
	 * @param state boolean
	 */
	public void setMaryRequest(boolean state)
	{
		_maryrequest = state;
	}
	
	/**
	 * Method isMaryRequest.
	 * @return boolean
	 */
	public boolean isMaryRequest()
	{
		return _maryrequest;
	}
	
	/**
	 * Method setMaryAccepted.
	 * @param state boolean
	 */
	public void setMaryAccepted(boolean state)
	{
		_maryaccepted = state;
	}
	
	/**
	 * Method isMaryAccepted.
	 * @return boolean
	 */
	public boolean isMaryAccepted()
	{
		return _maryaccepted;
	}
	
	/**
	 * Method getPartnerId.
	 * @return int
	 */
	public int getPartnerId()
	{
		return _partnerId;
	}
	
	/**
	 * Method setPartnerId.
	 * @param partnerid int
	 */
	public void setPartnerId(int partnerid)
	{
		_partnerId = partnerid;
	}
	
	/**
	 * Method getCoupleId.
	 * @return int
	 */
	public int getCoupleId()
	{
		return _coupleId;
	}
	
	/**
	 * Method setCoupleId.
	 * @param coupleId int
	 */
	public void setCoupleId(int coupleId)
	{
		_coupleId = coupleId;
	}
	
	/**
	 * Method setUndying.
	 * @param val boolean
	 */
	public void setUndying(boolean val)
	{
		if (!isGM())
		{
			return;
		}
		_isUndying = val;
	}
	
	/**
	 * Method isUndying.
	 * @return boolean
	 */
	public boolean isUndying()
	{
		return _isUndying;
	}
	
	/**
	 * Method resetReuse.
	 */
	public void resetReuse()
	{
		_skillReuses.clear();
		_sharedGroupReuses.clear();
	}
	
	/**
	 * Method getDeathPenalty.
	 * @return DeathPenalty
	 */
	public DeathPenalty getDeathPenalty()
	{
		return getActiveSubClass() == null ? null : getActiveSubClass().getDeathPenalty(this);
	}
	
	/**
	 * Field _charmOfCourage.
	 */
	private boolean _charmOfCourage = false;
	
	/**
	 * Method isCharmOfCourage.
	 * @return boolean
	 */
	public boolean isCharmOfCourage()
	{
		return _charmOfCourage;
	}
	
	/**
	 * Method setCharmOfCourage.
	 * @param val boolean
	 */
	public void setCharmOfCourage(boolean val)
	{
		_charmOfCourage = val;
		if (!val)
		{
			getEffectList().stopEffect(Skill.SKILL_CHARM_OF_COURAGE);
		}
		sendEtcStatusUpdate();
	}
	
	/**
	 * Field _increasedForce.
	 */
	private int _increasedForce = 0;
	/**
	 * Field _consumedSouls.
	 */
	private int _consumedSouls = 0;
	
	/**
	 * Method getIncreasedForce.
	 * @return int
	 */
	@Override
	public int getIncreasedForce()
	{
		return _increasedForce;
	}
	
	/**
	 * Method getConsumedSouls.
	 * @return int
	 */
	@Override
	public int getConsumedSouls()
	{
		return _consumedSouls;
	}
	
	/**
	 * Method setConsumedSouls.
	 * @param i int
	 * @param monster NpcInstance
	 */
	@Override
	public void setConsumedSouls(int i, NpcInstance monster)
	{
		if (i == _consumedSouls)
		{
			return;
		}
		int max = (int) calcStat(Stats.SOULS_LIMIT, 0, monster, null);
		if (i > max)
		{
			i = max;
		}
		if (i <= 0)
		{
			_consumedSouls = 0;
			sendEtcStatusUpdate();
			return;
		}
		if (_consumedSouls != i)
		{
			int diff = i - _consumedSouls;
			if (diff > 0)
			{
				SystemMessage sm = new SystemMessage(SystemMessage.YOUR_SOUL_HAS_INCREASED_BY_S1_SO_IT_IS_NOW_AT_S2);
				sm.addNumber(diff);
				sm.addNumber(i);
				sendPacket(sm);
			}
		}
		else if (max == i)
		{
			sendPacket(Msg.SOUL_CANNOT_BE_ABSORBED_ANY_MORE);
			return;
		}
		_consumedSouls = i;
		sendPacket(new EtcStatusUpdate(this));
	}
	
	/**
	 * Method setIncreasedForce.
	 * @param i int
	 */
	@Override
	public void setIncreasedForce(int i)
	{
		i = Math.min(i, Charge.MAX_CHARGE);
		i = Math.max(i, 0);
		if ((i != 0) && (i > _increasedForce))
		{
			sendPacket(new SystemMessage(SystemMessage.YOUR_FORCE_HAS_INCREASED_TO_S1_LEVEL).addNumber(i));
		}
		_increasedForce = i;
		sendEtcStatusUpdate();
	}
	
	/**
	 * Field _lastFalling.
	 */
	private long _lastFalling;
	
	/**
	 * Method isFalling.
	 * @return boolean
	 */
	public boolean isFalling()
	{
		return (System.currentTimeMillis() - _lastFalling) < 5000;
	}
	
	/**
	 * Method falling.
	 * @param height int
	 */
	public void falling(int height)
	{
		if (!Config.DAMAGE_FROM_FALLING || isDead() || isFlying() || isInWater() || isInBoat())
		{
			return;
		}
		_lastFalling = System.currentTimeMillis();
		int damage = (int) calcStat(Stats.FALL, (getMaxHp() / 2000) * height, null, null);
		if (damage > 0)
		{
			int curHp = (int) getCurrentHp();
			if ((curHp - damage) < 1)
			{
				setCurrentHp(1, false);
			}
			else
			{
				setCurrentHp(curHp - damage, false);
			}
			sendPacket(new SystemMessage(SystemMessage.YOU_RECEIVED_S1_DAMAGE_FROM_TAKING_A_HIGH_FALL).addNumber(damage));
		}
	}
	
	/**
	 * Method checkHpMessages.
	 * @param curHp double
	 * @param newHp double
	 */
	@Override
	public void checkHpMessages(double curHp, double newHp)
	{
		int[] _hp =
		{
			30,
			30
		};
		int[] skills =
		{
			290,
			291
		};
		int[] _effects_skills_id =
		{
			139,
			176,
			292,
			292,
			420
		};
		int[] _effects_hp =
		{
			30,
			30,
			30,
			60,
			30
		};
		double percent = getMaxHp() / 100;
		double _curHpPercent = curHp / percent;
		double _newHpPercent = newHp / percent;
		boolean needsUpdate = false;
		for (int i = 0; i < skills.length; i++)
		{
			int level = getSkillLevel(skills[i]);
			if (level > 0)
			{
				if ((_curHpPercent > _hp[i]) && (_newHpPercent <= _hp[i]))
				{
					sendPacket(new SystemMessage(SystemMessage.SINCE_HP_HAS_DECREASED_THE_EFFECT_OF_S1_CAN_BE_FELT).addSkillName(skills[i], level));
					needsUpdate = true;
				}
				else if ((_curHpPercent <= _hp[i]) && (_newHpPercent > _hp[i]))
				{
					sendPacket(new SystemMessage(SystemMessage.SINCE_HP_HAS_INCREASED_THE_EFFECT_OF_S1_WILL_DISAPPEAR).addSkillName(skills[i], level));
					needsUpdate = true;
				}
			}
		}
		for (Integer i = 0; i < _effects_skills_id.length; i++)
		{
			if (getEffectList().getEffectsBySkillId(_effects_skills_id[i]) != null)
			{
				if ((_curHpPercent > _effects_hp[i]) && (_newHpPercent <= _effects_hp[i]))
				{
					sendPacket(new SystemMessage(SystemMessage.SINCE_HP_HAS_DECREASED_THE_EFFECT_OF_S1_CAN_BE_FELT).addSkillName(_effects_skills_id[i], 1));
					needsUpdate = true;
				}
				else if ((_curHpPercent <= _effects_hp[i]) && (_newHpPercent > _effects_hp[i]))
				{
					sendPacket(new SystemMessage(SystemMessage.SINCE_HP_HAS_INCREASED_THE_EFFECT_OF_S1_WILL_DISAPPEAR).addSkillName(_effects_skills_id[i], 1));
					needsUpdate = true;
				}
			}
		}
		if (needsUpdate)
		{
			sendChanges();
		}
	}
	
	/**
	 * Method checkDayNightMessages.
	 */
	public void checkDayNightMessages()
	{
		int level = getSkillLevel(294);
		if (level > 0)
		{
			if (GameTimeController.getInstance().isNowNight())
			{
				sendPacket(new SystemMessage(SystemMessage.IT_IS_NOW_MIDNIGHT_AND_THE_EFFECT_OF_S1_CAN_BE_FELT).addSkillName(294, level));
			}
			else
			{
				sendPacket(new SystemMessage(SystemMessage.IT_IS_DAWN_AND_THE_EFFECT_OF_S1_WILL_NOW_DISAPPEAR).addSkillName(294, level));
			}
		}
		sendChanges();
	}
	
	/**
	 * Method getZoneMask.
	 * @return int
	 */
	public int getZoneMask()
	{
		return _zoneMask;
	}
	
	/**
	 * Method onUpdateZones.
	 * @param leaving List<Zone>
	 * @param entering List<Zone>
	 */
	@Override
	protected void onUpdateZones(List<Zone> leaving, List<Zone> entering)
	{
		super.onUpdateZones(leaving, entering);
		if (((leaving == null) || leaving.isEmpty()) && ((entering == null) || entering.isEmpty()))
		{
			return;
		}
		boolean lastInCombatZone = (_zoneMask & ZONE_PVP_FLAG) == ZONE_PVP_FLAG;
		boolean lastInDangerArea = (_zoneMask & ZONE_ALTERED_FLAG) == ZONE_ALTERED_FLAG;
		boolean lastOnSiegeField = (_zoneMask & ZONE_SIEGE_FLAG) == ZONE_SIEGE_FLAG;
		boolean isInCombatZone = isInCombatZone();
		boolean isInDangerArea = isInDangerArea();
		boolean isOnSiegeField = isOnSiegeField();
		boolean isInPeaceZone = isInPeaceZone();
		boolean isInSSQZone = isInSSQZone();
		int lastZoneMask = _zoneMask;
		_zoneMask = 0;
		if (isInCombatZone)
		{
			_zoneMask |= ZONE_PVP_FLAG;
		}
		if (isInDangerArea)
		{
			_zoneMask |= ZONE_ALTERED_FLAG;
		}
		if (isOnSiegeField)
		{
			_zoneMask |= ZONE_SIEGE_FLAG;
		}
		if (isInPeaceZone)
		{
			_zoneMask |= ZONE_PEACE_FLAG;
		}
		if (isInSSQZone)
		{
			_zoneMask |= ZONE_SSQ_FLAG;
		}
		if (lastZoneMask != _zoneMask)
		{
			sendPacket(new ExSetCompassZoneCode(this));
		}
		if (lastInCombatZone != isInCombatZone)
		{
			broadcastRelationChanged();
		}
		if (lastInDangerArea != isInDangerArea)
		{
			sendPacket(new EtcStatusUpdate(this));
		}
		if (lastOnSiegeField != isOnSiegeField)
		{
			broadcastRelationChanged();
			if (isOnSiegeField)
			{
				sendPacket(Msg.YOU_HAVE_ENTERED_A_COMBAT_ZONE);
			}
			else
			{
				sendPacket(Msg.YOU_HAVE_LEFT_A_COMBAT_ZONE);
				if (!isTeleporting() && (getPvpFlag() == 0))
				{
					startPvPFlag(null);
				}
			}
		}
		if (isInWater())
		{
			startWaterTask();
		}
		else
		{
			stopWaterTask();
		}
	}
	
	/**
	 * Method startAutoSaveTask.
	 */
	public void startAutoSaveTask()
	{
		if (!Config.AUTOSAVE)
		{
			return;
		}
		if (_autoSaveTask == null)
		{
			_autoSaveTask = AutoSaveManager.getInstance().addAutoSaveTask(this);
		}
	}
	
	/**
	 * Method stopAutoSaveTask.
	 */
	public void stopAutoSaveTask()
	{
		if (_autoSaveTask != null)
		{
			_autoSaveTask.cancel(false);
		}
		_autoSaveTask = null;
	}
	
	/**
	 * Method startPcBangPointsTask.
	 */
	public void startPcBangPointsTask()
	{
		if (!Config.ALT_PCBANG_POINTS_ENABLED || (Config.ALT_PCBANG_POINTS_DELAY <= 0))
		{
			return;
		}
		if (_pcCafePointsTask == null)
		{
			_pcCafePointsTask = LazyPrecisionTaskManager.getInstance().addPCCafePointsTask(this);
		}
	}
	
	/**
	 * Method stopPcBangPointsTask.
	 */
	public void stopPcBangPointsTask()
	{
		if (_pcCafePointsTask != null)
		{
			_pcCafePointsTask.cancel(false);
		}
		_pcCafePointsTask = null;
	}
	
	/**
	 * Method startUnjailTask.
	 * @param player Player
	 * @param time int
	 */
	public void startUnjailTask(Player player, int time)
	{
		if (_unjailTask != null)
		{
			_unjailTask.cancel(false);
		}
		_unjailTask = ThreadPoolManager.getInstance().schedule(new UnJailTask(player), time * 60000);
	}
	
	/**
	 * Method stopUnjailTask.
	 */
	public void stopUnjailTask()
	{
		if (_unjailTask != null)
		{
			_unjailTask.cancel(false);
		}
		_unjailTask = null;
	}
	
	/**
	 * Method sendMessage.
	 * @param message String
	 */
	@Override
	public void sendMessage(String message)
	{
		sendPacket(new SystemMessage(message));
	}
	
	/**
	 * Field _lastClientPosition.
	 */
	private Location _lastClientPosition;
	/**
	 * Field _lastServerPosition.
	 */
	private Location _lastServerPosition;
	
	/**
	 * Method setLastClientPosition.
	 * @param position Location
	 */
	public void setLastClientPosition(Location position)
	{
		_lastClientPosition = position;
	}
	
	/**
	 * Method getLastClientPosition.
	 * @return Location
	 */
	public Location getLastClientPosition()
	{
		return _lastClientPosition;
	}
	
	/**
	 * Method setLastServerPosition.
	 * @param position Location
	 */
	public void setLastServerPosition(Location position)
	{
		_lastServerPosition = position;
	}
	
	/**
	 * Method getLastServerPosition.
	 * @return Location
	 */
	public Location getLastServerPosition()
	{
		return _lastServerPosition;
	}
	
	/**
	 * Field _useSeed.
	 */
	private int _useSeed = 0;
	
	/**
	 * Method setUseSeed.
	 * @param id int
	 */
	public void setUseSeed(int id)
	{
		_useSeed = id;
	}
	
	/**
	 * Method getUseSeed.
	 * @return int
	 */
	public int getUseSeed()
	{
		return _useSeed;
	}
	
	/**
	 * Method getRelation.
	 * @param target Player
	 * @return int
	 */
	public int getRelation(Player target)
	{
		int result = 0;
		if (getClan() != null)
		{
			result |= RelationChanged.RELATION_CLAN_MEMBER;
			if (getClan() == target.getClan())
			{
				result |= RelationChanged.RELATION_CLAN_MATE;
			}
			if (getClan().getAllyId() != 0)
			{
				result |= RelationChanged.RELATION_ALLY_MEMBER;
			}
		}
		if (isClanLeader())
		{
			result |= RelationChanged.RELATION_LEADER;
		}
		Party party = getParty();
		if ((party != null) && (party == target.getParty()))
		{
			result |= RelationChanged.RELATION_HAS_PARTY;
			switch (party.getPartyMembers().indexOf(this))
			{
				case 0:
					result |= RelationChanged.RELATION_PARTYLEADER;
					break;
				case 1:
					result |= RelationChanged.RELATION_PARTY4;
					break;
				case 2:
					result |= RelationChanged.RELATION_PARTY3 + RelationChanged.RELATION_PARTY2 + RelationChanged.RELATION_PARTY1;
					break;
				case 3:
					result |= RelationChanged.RELATION_PARTY3 + RelationChanged.RELATION_PARTY2;
					break;
				case 4:
					result |= RelationChanged.RELATION_PARTY3 + RelationChanged.RELATION_PARTY1;
					break;
				case 5:
					result |= RelationChanged.RELATION_PARTY3;
					break;
				case 6:
					result |= RelationChanged.RELATION_PARTY2 + RelationChanged.RELATION_PARTY1;
					break;
				case 7:
					result |= RelationChanged.RELATION_PARTY2;
					break;
				case 8:
					result |= RelationChanged.RELATION_PARTY1;
					break;
			}
		}
		Clan clan1 = getClan();
		Clan clan2 = target.getClan();
		if ((clan1 != null) && (clan2 != null))
		{
			if ((target.getPledgeType() != Clan.SUBUNIT_ACADEMY) && (getPledgeType() != Clan.SUBUNIT_ACADEMY))
			{
				if (clan2.isAtWarWith(clan1.getClanId()))
				{
					result |= RelationChanged.RELATION_1SIDED_WAR;
					if (clan1.isAtWarWith(clan2.getClanId()))
					{
						result |= RelationChanged.RELATION_MUTUAL_WAR;
					}
				}
			}
			if (getBlockCheckerArena() != -1)
			{
				result |= RelationChanged.RELATION_INSIEGE;
				ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(getBlockCheckerArena());
				if (holder.getPlayerTeam(this) == 0)
				{
					result |= RelationChanged.RELATION_ENEMY;
				}
				else
				{
					result |= RelationChanged.RELATION_ALLY;
				}
				result |= RelationChanged.RELATION_ATTACKER;
			}
		}
		for (GlobalEvent e : getEvents())
		{
			result = e.getRelation(this, target, result);
		}
		return result;
	}
	
	/**
	 * Field _pvpFlag.
	 */
	protected int _pvpFlag;
	/**
	 * Field _PvPRegTask.
	 */
	private Future<?> _PvPRegTask;
	/**
	 * Field _lastPvpAttack.
	 */
	private long _lastPvpAttack;
	
	/**
	 * Method getlastPvpAttack.
	 * @return long
	 */
	public long getlastPvpAttack()
	{
		return _lastPvpAttack;
	}
	
	/**
	 * Method startPvPFlag.
	 * @param target Creature
	 */
	@Override
	public void startPvPFlag(Creature target)
	{
		if (_karma < 0)
		{
			return;
		}
		long startTime = System.currentTimeMillis();
		if ((target != null) && (target.getPvpFlag() != 0))
		{
			startTime -= Config.PVP_TIME / 2;
		}
		if ((_pvpFlag != 0) && (_lastPvpAttack > startTime))
		{
			return;
		}
		_lastPvpAttack = startTime;
		updatePvPFlag(1);
		if (_PvPRegTask == null)
		{
			_PvPRegTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new PvPFlagTask(this), 1000, 1000);
		}
	}
	
	/**
	 * Method stopPvPFlag.
	 */
	public void stopPvPFlag()
	{
		if (_PvPRegTask != null)
		{
			_PvPRegTask.cancel(false);
			_PvPRegTask = null;
		}
		updatePvPFlag(0);
	}
	
	/**
	 * Method updatePvPFlag.
	 * @param value int
	 */
	public void updatePvPFlag(int value)
	{
		if (_handysBlockCheckerEventArena != -1)
		{
			return;
		}
		if (_pvpFlag == value)
		{
			return;
		}
		setPvpFlag(value);
		sendStatusUpdate(true, true, StatusUpdateField.PVP_FLAG);
		broadcastRelationChanged();
	}
	
	/**
	 * Method setPvpFlag.
	 * @param pvpFlag int
	 */
	public void setPvpFlag(int pvpFlag)
	{
		_pvpFlag = pvpFlag;
	}
	
	/**
	 * Method getPvpFlag.
	 * @return int
	 */
	@Override
	public int getPvpFlag()
	{
		return _pvpFlag;
	}
	
	/**
	 * Method isInDuel.
	 * @return boolean
	 */
	public boolean isInDuel()
	{
		return getEvent(DuelEvent.class) != null;
	}
	
	/**
	 * Field _tamedBeasts.
	 */
	private final Map<Integer, TamedBeastInstance> _tamedBeasts = new ConcurrentHashMap<Integer, TamedBeastInstance>();
	
	/**
	 * Method getTrainedBeasts.
	 * @return Map<Integer,TamedBeastInstance>
	 */
	public Map<Integer, TamedBeastInstance> getTrainedBeasts()
	{
		return _tamedBeasts;
	}
	
	/**
	 * Method addTrainedBeast.
	 * @param tamedBeast TamedBeastInstance
	 */
	public void addTrainedBeast(TamedBeastInstance tamedBeast)
	{
		_tamedBeasts.put(tamedBeast.getObjectId(), tamedBeast);
	}
	
	/**
	 * Method removeTrainedBeast.
	 * @param npcId int
	 */
	public void removeTrainedBeast(int npcId)
	{
		_tamedBeasts.remove(npcId);
	}
	
	/**
	 * Field _lastAttackPacket.
	 */
	private long _lastAttackPacket = 0;
	
	/**
	 * Method getLastAttackPacket.
	 * @return long
	 */
	public long getLastAttackPacket()
	{
		return _lastAttackPacket;
	}
	
	/**
	 * Method setLastAttackPacket.
	 */
	public void setLastAttackPacket()
	{
		_lastAttackPacket = System.currentTimeMillis();
	}
	
	/**
	 * Field _lastMovePacket.
	 */
	private long _lastMovePacket = 0;
	
	/**
	 * Method getLastMovePacket.
	 * @return long
	 */
	public long getLastMovePacket()
	{
		return _lastMovePacket;
	}
	
	/**
	 * Method setLastMovePacket.
	 */
	public void setLastMovePacket()
	{
		_lastMovePacket = System.currentTimeMillis();
	}
	
	/**
	 * Method getKeyBindings.
	 * @return byte[]
	 */
	public byte[] getKeyBindings()
	{
		return _keyBindings;
	}
	
	/**
	 * Method setKeyBindings.
	 * @param keyBindings byte[]
	 */
	public void setKeyBindings(byte[] keyBindings)
	{
		if (keyBindings == null)
		{
			keyBindings = ArrayUtils.EMPTY_BYTE_ARRAY;
		}
		_keyBindings = keyBindings;
	}
	
	/**
	 * Method setTransformation.
	 * @param transformationId int
	 */
	@Override
	public void setTransformation(int transformationId)
	{
		if ((transformationId == getTransformation()) || ((getTransformation() != 0) && (transformationId != 0)))
		{
			return;
		}
		if (transformationId == 0)
		{
			for (Effect effect : getEffectList().getAllEffects())
			{
				if ((effect != null) && (effect.getEffectType() == EffectType.Transformation))
				{
					if (effect.calc() == 0)
					{
						continue;
					}
					effect.exit();
					preparateToTransform(effect.getSkill());
					break;
				}
			}
			if (!_transformationSkills.isEmpty())
			{
				for (Skill s : _transformationSkills.values())
				{
					if (!s.isCommon() && !SkillAcquireHolder.getInstance().isSkillPossible(this, s) && !s.isHeroic())
					{
						super.removeSkill(s);
					}
				}
				_transformationSkills.clear();
			}
		}
		else
		{
			if (!isCursedWeaponEquipped())
			{
				for (Effect effect : getEffectList().getAllEffects())
				{
					if ((effect != null) && (effect.getEffectType() == EffectType.Transformation))
					{
						if ((effect.getSkill() instanceof Transformation) && ((Transformation) effect.getSkill()).isDisguise)
						{
							for (Skill s : getAllSkills())
							{
								if ((s != null) && (s.isActive() || s.isToggle()))
								{
									_transformationSkills.put(s.getId(), s);
								}
							}
						}
						else
						{
							for (AddedSkill s : effect.getSkill().getAddedSkills())
							{
								if (s.level == 0)
								{
									int s2 = getSkillLevel(s.id);
									if (s2 > 0)
									{
										_transformationSkills.put(s.id, SkillTable.getInstance().getInfo(s.id, s2));
									}
								}
								else if (s.level == -2)
								{
									int learnLevel = Math.max(effect.getSkill().getMagicLevel(), 40);
									int maxLevel = SkillTable.getInstance().getBaseLevel(s.id);
									int curSkillLevel = 1;
									if (maxLevel > 3)
									{
										curSkillLevel += getLevel() - learnLevel;
									}
									else
									{
										curSkillLevel += (getLevel() - learnLevel) / ((76 - learnLevel) / maxLevel);
									}
									curSkillLevel = Math.min(Math.max(curSkillLevel, 1), maxLevel);
									_transformationSkills.put(s.id, SkillTable.getInstance().getInfo(s.id, curSkillLevel));
								}
								else
								{
									_transformationSkills.put(s.id, s.getSkill());
								}
							}
						}
						preparateToTransform(effect.getSkill());
						break;
					}
				}
			}
			else
			{
				preparateToTransform(null);
			}
			if (!isInOlympiadMode() && !isCursedWeaponEquipped() && _hero && getSubClassList().isBaseClassActive())
			{
				_transformationSkills.put(395, SkillTable.getInstance().getInfo(395, 1));
				_transformationSkills.put(396, SkillTable.getInstance().getInfo(396, 1));
				_transformationSkills.put(1374, SkillTable.getInstance().getInfo(1374, 1));
				_transformationSkills.put(1375, SkillTable.getInstance().getInfo(1375, 1));
				_transformationSkills.put(1376, SkillTable.getInstance().getInfo(1376, 1));
			}
			for (Skill s : _transformationSkills.values())
			{
				addSkill(s, false);
			}
		}
		super.setTransformation(transformationId);
		sendPacket(new ExBasicActionList(this));
		sendSkillList();
		sendPacket(new ShortCutInit(this));
		for (int shotId : getAutoSoulShot())
		{
			sendPacket(new ExAutoSoulShot(shotId, true));
		}
		if(transformationId == 0)
		{
			if(isAwaking())
			{
				if(Config.ALT_CHECK_SKILLS_AWAKENING)//When you untransform, checks for awakening skills
				{
					AwakingManager.getInstance().checkAwakenPlayerSkills(this);
				}
			}
		}
		broadcastUserInfo();
	}
	
	/**
	 * Method preparateToTransform.
	 * @param transSkill Skill
	 */
	private void preparateToTransform(Skill transSkill)
	{
		if ((transSkill == null) || !transSkill.isBaseTransformation())
		{
			for (Effect effect : getEffectList().getAllEffects())
			{
				if ((effect != null) && effect.getSkill().isToggle())
				{
					effect.exit();
				}
			}
		}
	}
	
	/**
	 * Method getAllSkills.
	 * @return Collection<Skill>
	 */
	@Override
	public final Collection<Skill> getAllSkills()
	{
		if (getTransformation() == 0)
		{
			return super.getAllSkills();
		}
		Map<Integer, Skill> tempSkills = new HashMap<Integer, Skill>();
		for (Skill s : super.getAllSkills())
		{
			if ((s != null) && !s.isActive() && !s.isToggle())
			{
				tempSkills.put(s.getId(), s);
			}
		}
		tempSkills.putAll(_transformationSkills);
		return tempSkills.values();
	}
	
	/**
	 * Method setAgathion.
	 * @param id int
	 */
	public void setAgathion(int id)
	{
		if (_agathionId == id)
		{
			return;
		}
		_agathionId = id;
		broadcastCharInfo();
	}
	
	/**
	 * Method getAgathionId.
	 * @return int
	 */
	public int getAgathionId()
	{
		return _agathionId;
	}
	
	/**
	 * Method getPcBangPoints.
	 * @return int
	 */
	public int getPcBangPoints()
	{
		return _pcBangPoints;
	}
	
	/**
	 * Method setPcBangPoints.
	 * @param val int
	 */
	public void setPcBangPoints(int val)
	{
		_pcBangPoints = val;
	}
	
	/**
	 * Method addPcBangPoints.
	 * @param count int
	 * @param doublePoints boolean
	 */
	public void addPcBangPoints(int count, boolean doublePoints)
	{
		if (doublePoints)
		{
			count *= 2;
		}
		_pcBangPoints += count;
		sendPacket(new SystemMessage(doublePoints ? SystemMessage.DOUBLE_POINTS_YOU_AQUIRED_S1_PC_BANG_POINT : SystemMessage.YOU_ACQUIRED_S1_PC_BANG_POINT).addNumber(count));
		sendPacket(new ExPCCafePointInfo(this, count, 1, 2, 12));
	}
	
	/**
	 * Method reducePcBangPoints.
	 * @param count int
	 * @return boolean
	 */
	public boolean reducePcBangPoints(int count)
	{
		if (_pcBangPoints < count)
		{
			return false;
		}
		_pcBangPoints -= count;
		sendPacket(new SystemMessage(SystemMessage.YOU_ARE_USING_S1_POINT).addNumber(count));
		sendPacket(new ExPCCafePointInfo(this, 0, 1, 2, 12));
		return true;
	}
	
	/**
	 * Field _groundSkillLoc.
	 */
	private Location _groundSkillLoc;
	
	/**
	 * Method setGroundSkillLoc.
	 * @param location Location
	 */
	public void setGroundSkillLoc(Location location)
	{
		_groundSkillLoc = location;
	}
	
	/**
	 * Method getGroundSkillLoc.
	 * @return Location
	 */
	public Location getGroundSkillLoc()
	{
		return _groundSkillLoc;
	}
	
	/**
	 * Method isLogoutStarted.
	 * @return boolean
	 */
	public boolean isLogoutStarted()
	{
		return _isLogout.get();
	}
	
	/**
	 * Method setOfflineMode.
	 * @param val boolean
	 */
	public void setOfflineMode(boolean val)
	{
		if (!val)
		{
			unsetVar("offline");
		}
		_offline = val;
	}
	
	/**
	 * Method isInOfflineMode.
	 * @return boolean
	 */
	public boolean isInOfflineMode()
	{
		return _offline;
	}
	
	/**
	 * Method saveTradeList.
	 */
	public void saveTradeList()
	{
		String val = "";
		if ((_sellList == null) || _sellList.isEmpty())
		{
			unsetVar("selllist");
		}
		else
		{
			for (TradeItem i : _sellList)
			{
				val += i.getObjectId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
			}
			setVar("selllist", val, -1);
			val = "";
			if ((_tradeList != null) && (getSellStoreName() != null))
			{
				setVar("sellstorename", getSellStoreName(), -1);
			}
		}
		if ((_packageSellList == null) || _packageSellList.isEmpty())
		{
			unsetVar("packageselllist");
		}
		else
		{
			for (TradeItem i : _packageSellList)
			{
				val += i.getObjectId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
			}
			setVar("packageselllist", val, -1);
			val = "";
			if ((_tradeList != null) && (getSellStoreName() != null))
			{
				setVar("sellstorename", getSellStoreName(), -1);
			}
		}
		if ((_buyList == null) || _buyList.isEmpty())
		{
			unsetVar("buylist");
		}
		else
		{
			for (TradeItem i : _buyList)
			{
				val += i.getItemId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
			}
			setVar("buylist", val, -1);
			val = "";
			if ((_tradeList != null) && (getBuyStoreName() != null))
			{
				setVar("buystorename", getBuyStoreName(), -1);
			}
		}
		if ((_createList == null) || _createList.isEmpty())
		{
			unsetVar("createlist");
		}
		else
		{
			for (ManufactureItem i : _createList)
			{
				val += i.getRecipeId() + ";" + i.getCost() + ":";
			}
			setVar("createlist", val, -1);
			if (getManufactureName() != null)
			{
				setVar("manufacturename", getManufactureName(), -1);
			}
		}
	}
	
	/**
	 * Method restoreTradeList.
	 */
	public void restoreTradeList()
	{
		String var;
		var = getVar("selllist");
		if (var != null)
		{
			_sellList = new CopyOnWriteArrayList<TradeItem>();
			String[] items = var.split(":");
			for (String item : items)
			{
				if (item.equals(""))
				{
					continue;
				}
				String[] values = item.split(";");
				if (values.length < 3)
				{
					continue;
				}
				int oId = Integer.parseInt(values[0]);
				long count = Long.parseLong(values[1]);
				long price = Long.parseLong(values[2]);
				ItemInstance itemToSell = getInventory().getItemByObjectId(oId);
				if ((count < 1) || (itemToSell == null))
				{
					continue;
				}
				if (count > itemToSell.getCount())
				{
					count = itemToSell.getCount();
				}
				TradeItem i = new TradeItem(itemToSell);
				i.setCount(count);
				i.setOwnersPrice(price);
				_sellList.add(i);
			}
			var = getVar("sellstorename");
			if (var != null)
			{
				setSellStoreName(var);
			}
		}
		var = getVar("packageselllist");
		if (var != null)
		{
			_packageSellList = new CopyOnWriteArrayList<TradeItem>();
			String[] items = var.split(":");
			for (String item : items)
			{
				if (item.equals(""))
				{
					continue;
				}
				String[] values = item.split(";");
				if (values.length < 3)
				{
					continue;
				}
				int oId = Integer.parseInt(values[0]);
				long count = Long.parseLong(values[1]);
				long price = Long.parseLong(values[2]);
				ItemInstance itemToSell = getInventory().getItemByObjectId(oId);
				if ((count < 1) || (itemToSell == null))
				{
					continue;
				}
				if (count > itemToSell.getCount())
				{
					count = itemToSell.getCount();
				}
				TradeItem i = new TradeItem(itemToSell);
				i.setCount(count);
				i.setOwnersPrice(price);
				_packageSellList.add(i);
			}
			var = getVar("sellstorename");
			if (var != null)
			{
				setSellStoreName(var);
			}
		}
		var = getVar("buylist");
		if (var != null)
		{
			_buyList = new CopyOnWriteArrayList<TradeItem>();
			String[] items = var.split(":");
			for (String item : items)
			{
				if (item.equals(""))
				{
					continue;
				}
				String[] values = item.split(";");
				if (values.length < 3)
				{
					continue;
				}
				TradeItem i = new TradeItem();
				i.setItemId(Integer.parseInt(values[0]));
				i.setCount(Long.parseLong(values[1]));
				i.setOwnersPrice(Long.parseLong(values[2]));
				_buyList.add(i);
			}
			var = getVar("buystorename");
			if (var != null)
			{
				setBuyStoreName(var);
			}
		}
		var = getVar("createlist");
		if (var != null)
		{
			_createList = new CopyOnWriteArrayList<ManufactureItem>();
			String[] items = var.split(":");
			for (String item : items)
			{
				if (item.equals(""))
				{
					continue;
				}
				String[] values = item.split(";");
				if (values.length < 2)
				{
					continue;
				}
				int recId = Integer.parseInt(values[0]);
				long price = Long.parseLong(values[1]);
				if (findRecipe(recId))
				{
					_createList.add(new ManufactureItem(recId, price));
				}
			}
			var = getVar("manufacturename");
			if (var != null)
			{
				setManufactureName(var);
			}
		}
	}
	
	/**
	 * Method restoreRecipeBook.
	 */
	public void restoreRecipeBook()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT id FROM character_recipebook WHERE char_id=?");
			statement.setInt(1, getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int id = rset.getInt("id");
				RecipeTemplate recipe = RecipeHolder.getInstance().getRecipeByRecipeId(id);
				registerRecipe(recipe, false);
			}
		}
		catch (Exception e)
		{
			_log.warn("count not recipe skills:" + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method getDecoy.
	 * @return DecoyInstance
	 */
	public DecoyInstance getDecoy()
	{
		return _decoy;
	}
	
	/**
	 * Method setDecoy.
	 * @param decoy DecoyInstance
	 */
	public void setDecoy(DecoyInstance decoy)
	{
		_decoy = decoy;
	}
	
	/**
	 * Method getMountType.
	 * @return int
	 */
	public int getMountType()
	{
		switch (getMountNpcId())
		{
			case PetDataTable.STRIDER_WIND_ID:
			case PetDataTable.STRIDER_STAR_ID:
			case PetDataTable.STRIDER_TWILIGHT_ID:
			case PetDataTable.RED_STRIDER_WIND_ID:
			case PetDataTable.RED_STRIDER_STAR_ID:
			case PetDataTable.RED_STRIDER_TWILIGHT_ID:
			case PetDataTable.GUARDIANS_STRIDER_ID:
				return 1;
			case PetDataTable.WYVERN_ID:
				return 2;
			case PetDataTable.WGREAT_WOLF_ID:
			case PetDataTable.FENRIR_WOLF_ID:
			case PetDataTable.WFENRIR_WOLF_ID:
				return 3;
		}
		return 0;
	}
	
	/**
	 * Method getColRadius.
	 * @return double
	 */
	@Override
	public double getColRadius()
	{
		if (getTransformation() != 0)
		{
			int template = getTransformationTemplate();
			if (template != 0)
			{
				NpcTemplate npcTemplate = NpcHolder.getInstance().getTemplate(template);
				if (npcTemplate != null)
				{
					return npcTemplate.getCollisionRadius();
				}
			}
		}
		else if (isMounted())
		{
			int mountTemplate = getMountNpcId();
			if (mountTemplate != 0)
			{
				NpcTemplate mountNpcTemplate = NpcHolder.getInstance().getTemplate(mountTemplate);
				if (mountNpcTemplate != null)
				{
					return mountNpcTemplate.getCollisionRadius();
				}
			}
		}
		return getCollisionRadius();
	}
	
	/**
	 * Method getColHeight.
	 * @return double
	 */
	@Override
	public double getColHeight()
	{
		if (getTransformation() != 0)
		{
			int template = getTransformationTemplate();
			if (template != 0)
			{
				NpcTemplate npcTemplate = NpcHolder.getInstance().getTemplate(template);
				if (npcTemplate != null)
				{
					return npcTemplate.getCollisionHeight();
				}
			}
		}
		else if (isMounted())
		{
			int mountTemplate = getMountNpcId();
			if (mountTemplate != 0)
			{
				NpcTemplate mountNpcTemplate = NpcHolder.getInstance().getTemplate(mountTemplate);
				if (mountNpcTemplate != null)
				{
					return mountNpcTemplate.getCollisionHeight();
				}
			}
		}
		return getCollisionHeight();
	}
	
	/**
	 * Method setReflection.
	 * @param reflection Reflection
	 */
	@Override
	public void setReflection(Reflection reflection)
	{
		if (getReflection() == reflection)
		{
			return;
		}
		super.setReflection(reflection);
		for (Summon summon : getSummonList())
		{
			if ((summon != null) && !summon.isDead())
			{
				summon.setReflection(reflection);
			}
		}
		if (reflection != ReflectionManager.DEFAULT)
		{
			String var = getVar("reflection");
			if ((var == null) || !var.equals(String.valueOf(reflection.getId())))
			{
				setVar("reflection", String.valueOf(reflection.getId()), -1);
			}
		}
		else
		{
			unsetVar("reflection");
		}
		if (getActiveSubClass() != null)
		{
			getInventory().validateItems();
			for (Summon summon : getSummonList())
			{
				if ((summon.getNpcId() == 14916) || (summon.getNpcId() == 14917))
				{
					summon.unSummon();
				}
			}
		}
	}
	
	/**
	 * Method getCollisionRadius.
	 * @return double
	 */
	public double getCollisionRadius()
	{
		return _collision_radius;
	}
	
	/**
	 * Method getCollisionHeight.
	 * @return double
	 */
	public double getCollisionHeight()
	{
		return _collision_height;
	}
	
	/**
	 * Method isTerritoryFlagEquipped.
	 * @return boolean
	 */
	public boolean isTerritoryFlagEquipped()
	{
		ItemInstance weapon = getActiveWeaponInstance();
		return (weapon != null) && weapon.getTemplate().isTerritoryFlag();
	}
	
	/**
	 * Field _buyListId.
	 */
	private int _buyListId;
	
	/**
	 * Method setBuyListId.
	 * @param listId int
	 */
	public void setBuyListId(int listId)
	{
		_buyListId = listId;
	}
	
	/**
	 * Method getBuyListId.
	 * @return int
	 */
	public int getBuyListId()
	{
		return _buyListId;
	}
	
	/**
	 * Method getFame.
	 * @return int
	 */
	public int getFame()
	{
		return _fame;
	}
	
	/**
	 * Method setFame.
	 * @param fame int
	 * @param log String
	 */
	public void setFame(int fame, String log)
	{
		fame = Math.min(Config.LIM_FAME, fame);
		if ((log != null) && !log.isEmpty())
		{
			Log.add(_name + "|" + (fame - _fame) + "|" + fame + "|" + log, "fame");
		}
		if (fame > _fame)
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S1_REPUTATION_SCORE).addNumber(fame - _fame));
		}
		_fame = fame;
		sendChanges();
	}
	
	/**
	 * Method getVitality.
	 * @return int
	 */
	public int getVitality()
	{
		return Config.ALT_VITALITY_ENABLED ? _vitality : 0;
	}
	
	/**
	 * Method addVitality.
	 * @param val int
	 */
	public void addVitality(int val)
	{
		setVitality(getVitality() + val);
	}
	
	/**
	 * Method setVitality.
	 * @param newVitality int
	 */
	public void setVitality(int newVitality)
	{
		if (!Config.ALT_VITALITY_ENABLED)
		{
			return;
		}
		newVitality = Math.max(Math.min(newVitality, Config.MAX_VITALITY), 0);
		if ((newVitality >= _vitality) || (getLevel() >= 10))
		{
			if (newVitality != _vitality)
			{
				if (newVitality == 0)
				{
					sendPacket(Msg.VITALITY_IS_FULLY_EXHAUSTED);
				}
				else if (newVitality == Config.MAX_VITALITY)
				{
					sendPacket(Msg.YOUR_VITALITY_IS_AT_MAXIMUM);
				}
			}
			_vitality = newVitality;
		}
		sendPacket(new ExVitalityPointInfo(_vitality));
	}
	
	/**
	 * Method getVitalityBonus.
	 * @return double
	 */
	public double getVitalityBonus()
	{
		return getVitality() > 0 ? Config.ALT_VITALITY_RATE : 0.0;
	}
	
	/**
	 * Field _incorrectValidateCount.
	 */
	private final int _incorrectValidateCount = 0;
	
	/**
	 * Method getIncorrectValidateCount.
	 * @return int
	 */
	public int getIncorrectValidateCount()
	{
		return _incorrectValidateCount;
	}
	
	/**
	 * Method setIncorrectValidateCount.
	 * @param count int
	 * @return int
	 */
	public int setIncorrectValidateCount(int count)
	{
		return _incorrectValidateCount;
	}
	
	/**
	 * Method getExpandInventory.
	 * @return int
	 */
	public int getExpandInventory()
	{
		return _expandInventory;
	}
	
	/**
	 * Method setExpandInventory.
	 * @param inventory int
	 */
	public void setExpandInventory(int inventory)
	{
		_expandInventory = inventory;
	}
	
	/**
	 * Method getExpandWarehouse.
	 * @return int
	 */
	public int getExpandWarehouse()
	{
		return _expandWarehouse;
	}
	
	/**
	 * Method setExpandWarehouse.
	 * @param warehouse int
	 */
	public void setExpandWarehouse(int warehouse)
	{
		_expandWarehouse = warehouse;
	}
	
	/**
	 * Method isNotShowBuffAnim.
	 * @return boolean
	 */
	public boolean isNotShowBuffAnim()
	{
		return _notShowBuffAnim;
	}
	
	/**
	 * Method setNotShowBuffAnim.
	 * @param value boolean
	 */
	public void setNotShowBuffAnim(boolean value)
	{
		_notShowBuffAnim = value;
	}
	
	/**
	 * Method enterMovieMode.
	 */
	public void enterMovieMode()
	{
		if (isInMovie())
		{
			return;
		}
		setTarget(null);
		stopMove();
		setIsInMovie(true);
		sendPacket(new CameraMode(1));
	}
	
	/**
	 * Method leaveMovieMode.
	 */
	public void leaveMovieMode()
	{
		setIsInMovie(false);
		sendPacket(new CameraMode(0));
		broadcastCharInfo();
	}
	
	/**
	 * Method specialCamera.
	 * @param target GameObject
	 * @param dist int
	 * @param yaw int
	 * @param pitch int
	 * @param time int
	 * @param duration int
	 */
	public void specialCamera(GameObject target, int dist, int yaw, int pitch, int time, int duration)
	{
		sendPacket(new SpecialCamera(target.getObjectId(), dist, yaw, pitch, time, duration));
	}
	
	/**
	 * Method specialCamera.
	 * @param target GameObject
	 * @param dist int
	 * @param yaw int
	 * @param pitch int
	 * @param time int
	 * @param duration int
	 * @param turn int
	 * @param rise int
	 * @param widescreen int
	 * @param unk int
	 */
	public void specialCamera(GameObject target, int dist, int yaw, int pitch, int time, int duration, int turn, int rise, int widescreen, int unk)
	{
		sendPacket(new SpecialCamera(target.getObjectId(), dist, yaw, pitch, time, duration, turn, rise, widescreen, unk));
	}
	
	/**
	 * Field _movieId.
	 */
	private int _movieId = 0;
	/**
	 * Field _isInMovie.
	 */
	private boolean _isInMovie;
	
	/**
	 * Method setMovieId.
	 * @param id int
	 */
	public void setMovieId(int id)
	{
		_movieId = id;
	}
	
	/**
	 * Method getMovieId.
	 * @return int
	 */
	public int getMovieId()
	{
		return _movieId;
	}
	
	/**
	 * Method isInMovie.
	 * @return boolean
	 */
	public boolean isInMovie()
	{
		return _isInMovie;
	}
	
	/**
	 * Method setIsInMovie.
	 * @param state boolean
	 */
	public void setIsInMovie(boolean state)
	{
		_isInMovie = state;
	}
	
	/**
	 * Method showQuestMovie.
	 * @param movie SceneMovie
	 */
	public void showQuestMovie(SceneMovie movie)
	{
		if (isInMovie())
		{
			return;
		}
		sendActionFailed();
		setTarget(null);
		stopMove();
		setMovieId(movie.getId());
		setIsInMovie(true);
		sendPacket(movie.packet(this));
	}
	
	/**
	 * Method showQuestMovie.
	 * @param movieId int
	 */
	public void showQuestMovie(int movieId)
	{
		if (isInMovie())
		{
			return;
		}
		sendActionFailed();
		setTarget(null);
		stopMove();
		setMovieId(movieId);
		setIsInMovie(true);
		sendPacket(new ExStartScenePlayer(movieId));
	}
	
	/**
	 * Method setAutoLoot.
	 * @param enable boolean
	 */
	public void setAutoLoot(boolean enable)
	{
		if (Config.AUTO_LOOT_INDIVIDUAL)
		{
			_autoLoot = enable;
			setVar("AutoLoot", String.valueOf(enable), -1);
		}
	}
	
	/**
	 * Method setAutoLootHerbs.
	 * @param enable boolean
	 */
	public void setAutoLootHerbs(boolean enable)
	{
		if (Config.AUTO_LOOT_INDIVIDUAL)
		{
			AutoLootHerbs = enable;
			setVar("AutoLootHerbs", String.valueOf(enable), -1);
		}
	}
	
	/**
	 * Method isAutoLootEnabled.
	 * @return boolean
	 */
	public boolean isAutoLootEnabled()
	{
		return _autoLoot;
	}
	
	/**
	 * Method isAutoLootHerbsEnabled.
	 * @return boolean
	 */
	public boolean isAutoLootHerbsEnabled()
	{
		return AutoLootHerbs;
	}
	
	/**
	 * Method reName.
	 * @param name String
	 * @param saveToDB boolean
	 */
	public final void reName(String name, boolean saveToDB)
	{
		setName(name);
		if (saveToDB)
		{
			saveNameToDB();
		}
		broadcastCharInfo();
	}
	
	/**
	 * Method reName.
	 * @param name String
	 */
	public final void reName(String name)
	{
		reName(name, false);
	}
	
	/**
	 * Method saveNameToDB.
	 */
	public final void saveNameToDB()
	{
		Connection con = null;
		PreparedStatement st = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.prepareStatement("UPDATE characters SET char_name = ? WHERE obj_Id = ?");
			st.setString(1, getName());
			st.setInt(2, getObjectId());
			st.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, st);
		}
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	@Override
	public Player getPlayer()
	{
		return this;
	}
	
	/**
	 * Method getStoredBypasses.
	 * @param bbs boolean
	 * @return List<String>
	 */
	private List<String> getStoredBypasses(boolean bbs)
	{
		if (bbs)
		{
			if (bypasses_bbs == null)
			{
				bypasses_bbs = new LazyArrayList<String>();
			}
			return bypasses_bbs;
		}
		if (bypasses == null)
		{
			bypasses = new LazyArrayList<String>();
		}
		return bypasses;
	}
	
	/**
	 * Method cleanBypasses.
	 * @param bbs boolean
	 */
	public void cleanBypasses(boolean bbs)
	{
		List<String> bypassStorage = getStoredBypasses(bbs);
		synchronized (bypassStorage)
		{
			bypassStorage.clear();
		}
	}
	
	/**
	 * Method encodeBypasses.
	 * @param htmlCode String
	 * @param bbs boolean
	 * @return String
	 */
	public String encodeBypasses(String htmlCode, boolean bbs)
	{
		List<String> bypassStorage = getStoredBypasses(bbs);
		synchronized (bypassStorage)
		{
			return BypassManager.encode(htmlCode, bypassStorage, bbs);
		}
	}
	
	/**
	 * Method decodeBypass.
	 * @param bypass String
	 * @return DecodedBypass
	 */
	public DecodedBypass decodeBypass(String bypass)
	{
		BypassType bpType = BypassManager.getBypassType(bypass);
		boolean bbs = (bpType == BypassType.ENCODED_BBS) || (bpType == BypassType.SIMPLE_BBS);
		List<String> bypassStorage = getStoredBypasses(bbs);
		if ((bpType == BypassType.ENCODED) || (bpType == BypassType.ENCODED_BBS))
		{
			return BypassManager.decode(bypass, bypassStorage, bbs, this);
		}
		if (bpType == BypassType.SIMPLE)
		{
			return new DecodedBypass(bypass, false).trim();
		}
		if ((bpType == BypassType.SIMPLE_BBS) && !bypass.startsWith("_bbsscripts"))
		{
			return new DecodedBypass(bypass, true).trim();
		}
		ICommunityBoardHandler handler = CommunityBoardManager.getInstance().getCommunityHandler(bypass);
		if (handler != null)
		{
			return new DecodedBypass(bypass, handler).trim();
		}
		_log.warn("Direct access to bypass: " + bypass + " / Player: " + getName());
		return null;
	}
	
	/**
	 * Method getTalismanCount.
	 * @return int
	 */
	public int getTalismanCount()
	{
		return (int) calcStat(Stats.TALISMANS_LIMIT, 0, null, null);
	}
	
	/**
	 * Method getOpenCloak.
	 * @return boolean
	 */
	public boolean getOpenCloak()
	{
		if (Config.ALT_OPEN_CLOAK_SLOT)
		{
			return true;
		}
		return (int) calcStat(Stats.CLOAK_SLOT, 0, null, null) > 0;
	}
	
	/**
	 * Method disableDrop.
	 * @param time int
	 */
	public final void disableDrop(int time)
	{
		_dropDisabled = System.currentTimeMillis() + time;
	}
	
	/**
	 * Method isDropDisabled.
	 * @return boolean
	 */
	public final boolean isDropDisabled()
	{
		return _dropDisabled > System.currentTimeMillis();
	}
	
	/**
	 * Field _petControlItem.
	 */
	private ItemInstance _petControlItem = null;
	
	/**
	 * Method setPetControlItem.
	 * @param itemObjId int
	 */
	public void setPetControlItem(int itemObjId)
	{
		setPetControlItem(getInventory().getItemByObjectId(itemObjId));
	}
	
	/**
	 * Method setPetControlItem.
	 * @param item ItemInstance
	 */
	public void setPetControlItem(ItemInstance item)
	{
		_petControlItem = item;
	}
	
	/**
	 * Method getPetControlItem.
	 * @return ItemInstance
	 */
	public ItemInstance getPetControlItem()
	{
		return _petControlItem;
	}
	
	/**
	 * Field isActive.
	 */
	private final AtomicBoolean isActive = new AtomicBoolean();
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return isActive.get();
	}
	
	/**
	 * Method setActive.
	 */
	public void setActive()
	{
		setNonAggroTime(0);
		if (isActive.getAndSet(true))
		{
			return;
		}
		onActive();
	}
	
	/**
	 * Method onActive.
	 */
	private void onActive()
	{
		setNonAggroTime(0);
		sendPacket(Msg.YOU_ARE_PROTECTED_AGGRESSIVE_MONSTERS);
		ThreadPoolManager.getInstance().execute(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{				
				getSummonList().summonAll();
				if(_ServitorShareRestore)
				{
					_ServitorShareRestoreData.start();
					_ServitorShareRestore = false;
					_ServitorShareRestoreData = null;
				}
			}
		});
	}
	
	/**
	 * Field _traps.
	 */
	private Map<Integer, Long> _traps;
	
	/**
	 * Method getTraps.
	 * @return Collection<TrapInstance>
	 */
	public Collection<TrapInstance> getTraps()
	{
		if (_traps == null)
		{
			return null;
		}
		Collection<TrapInstance> result = new ArrayList<TrapInstance>(getTrapsCount());
		TrapInstance trap;
		for (Integer trapId : _traps.keySet())
		{
			if ((trap = (TrapInstance) GameObjectsStorage.get(_traps.get(trapId))) != null)
			{
				result.add(trap);
			}
			else
			{
				_traps.remove(trapId);
			}
		}
		return result;
	}
	
	/**
	 * Method getTrapsCount.
	 * @return int
	 */
	public int getTrapsCount()
	{
		return _traps == null ? 0 : _traps.size();
	}
	
	/**
	 * Method addTrap.
	 * @param trap TrapInstance
	 */
	public void addTrap(TrapInstance trap)
	{
		if (_traps == null)
		{
			_traps = new HashMap<Integer, Long>();
		}
		_traps.put(trap.getObjectId(), trap.getStoredId());
	}
	
	/**
	 * Method removeTrap.
	 * @param trap TrapInstance
	 */
	public void removeTrap(TrapInstance trap)
	{
		Map<Integer, Long> traps = _traps;
		if ((traps == null) || traps.isEmpty())
		{
			return;
		}
		traps.remove(trap.getObjectId());
	}
	
	/**
	 * Method destroyFirstTrap.
	 */
	public void destroyFirstTrap()
	{
		Map<Integer, Long> traps = _traps;
		if ((traps == null) || traps.isEmpty())
		{
			return;
		}
		TrapInstance trap;
		for (Integer trapId : traps.keySet())
		{
			if ((trap = (TrapInstance) GameObjectsStorage.get(traps.get(trapId))) != null)
			{
				trap.deleteMe();
				return;
			}
			return;
		}
	}
	
	/**
	 * Method destroyAllTraps.
	 */
	public void destroyAllTraps()
	{
		Map<Integer, Long> traps = _traps;
		if ((traps == null) || traps.isEmpty())
		{
			return;
		}
		List<TrapInstance> toRemove = new ArrayList<TrapInstance>();
		for (Integer trapId : traps.keySet())
		{
			toRemove.add((TrapInstance) GameObjectsStorage.get(traps.get(trapId)));
		}
		for (TrapInstance t : toRemove)
		{
			if (t != null)
			{
				t.deleteMe();
			}
		}
	}
	
	/**
	 * Method setBlockCheckerArena.
	 * @param arena byte
	 */
	public void setBlockCheckerArena(byte arena)
	{
		_handysBlockCheckerEventArena = arena;
	}
	
	/**
	 * Method getBlockCheckerArena.
	 * @return int
	 */
	public int getBlockCheckerArena()
	{
		return _handysBlockCheckerEventArena;
	}
	
	/**
	 * Method getListeners.
	 * @return PlayerListenerList
	 */
	@Override
	public PlayerListenerList getListeners()
	{
		if (listeners == null)
		{
			synchronized (this)
			{
				if (listeners == null)
				{
					listeners = new PlayerListenerList(this);
				}
			}
		}
		return (PlayerListenerList) listeners;
	}
	
	/**
	 * Method getStatsRecorder.
	 * @return PlayerStatsChangeRecorder
	 */
	@Override
	public PlayerStatsChangeRecorder getStatsRecorder()
	{
		if (_statsRecorder == null)
		{
			synchronized (this)
			{
				if (_statsRecorder == null)
				{
					_statsRecorder = new PlayerStatsChangeRecorder(this);
				}
			}
		}
		return (PlayerStatsChangeRecorder) _statsRecorder;
	}
	
	/**
	 * Field _hourlyTask.
	 */
	private Future<?> _hourlyTask;
	/**
	 * Field _hoursInGame.
	 */
	private int _hoursInGame = 0;
	
	/**
	 * Method getHoursInGame.
	 * @return int
	 */
	public int getHoursInGame()
	{
		_hoursInGame++;
		return _hoursInGame;
	}
	
	/**
	 * Method startHourlyTask.
	 */
	public void startHourlyTask()
	{
		_hourlyTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new HourlyTask(this), 3600000L, 3600000L);
	}
	
	/**
	 * Method stopHourlyTask.
	 */
	public void stopHourlyTask()
	{
		if (_hourlyTask != null)
		{
			_hourlyTask.cancel(false);
			_hourlyTask = null;
		}
	}
	
	/**
	 * Method getPremiumPoints.
	 * @return long
	 */
	public long getPremiumPoints()
	{
		if (Config.GAME_POINT_ITEM_ID > 0)
		{
			return ItemFunctions.getItemCount(this, Config.GAME_POINT_ITEM_ID);
		}
		return 0;
	}
	
	/**
	 * Method reducePremiumPoints.
	 * @param val int
	 */
	public void reducePremiumPoints(final int val)
	{
		if (Config.GAME_POINT_ITEM_ID > 0)
		{
			ItemFunctions.removeItem(this, Config.GAME_POINT_ITEM_ID, val, true);
		}
	}
	
	/**
	 * Field _agathionResAvailable.
	 */
	private boolean _agathionResAvailable = false;
	
	/**
	 * Method isAgathionResAvailable.
	 * @return boolean
	 */
	public boolean isAgathionResAvailable()
	{
		return _agathionResAvailable;
	}
	
	/**
	 * Method setAgathionRes.
	 * @param val boolean
	 */
	public void setAgathionRes(boolean val)
	{
		_agathionResAvailable = val;
	}
	
	/**
	 * Method isClanAirShipDriver.
	 * @return boolean
	 */
	public boolean isClanAirShipDriver()
	{
		return isInBoat() && getBoat().isClanAirShip() && (((ClanAirShip) getBoat()).getDriver() == this);
	}
	
	/**
	 * Field _userSession.
	 */
	private Map<String, String> _userSession;
	
	/**
	 * Method getSessionVar.
	 * @param key String
	 * @return String
	 */
	public String getSessionVar(String key)
	{
		if (_userSession == null)
		{
			return null;
		}
		return _userSession.get(key);
	}
	
	/**
	 * Method setSessionVar.
	 * @param key String
	 * @param val String
	 */
	public void setSessionVar(String key, String val)
	{
		if (_userSession == null)
		{
			_userSession = new ConcurrentHashMap<String, String>();
		}
		if ((val == null) || val.isEmpty())
		{
			_userSession.remove(key);
		}
		else
		{
			_userSession.put(key, val);
		}
	}
	
	/**
	 * Method getFriendList.
	 * @return FriendList
	 */
	public FriendList getFriendList()
	{
		return _friendList;
	}
	
	/**
	 * Method getMenteeMentorList.
	 * @return _menteeMentorList
	 */
	public MenteeMentorList getMenteeMentorList()
	{
		return _menteeMentorList;
	}
	
	/**
	 * Method mentoringLoginConditions.
	 */
	public void mentoringLoginConditions()
	{
		if (getMenteeMentorList().someOneOnline(true))
		{
			getMenteeMentorList().notify(true);

			if (getClassId().getId() > 138 && getLevel() > 85)
			{
				Mentoring.applyMentorBuffs(this);
				for (MenteeMentor mentee : getMenteeMentorList().getList().values())
				{
					Player menteePlayer = World.getPlayer(mentee.getName());
					Mentoring.applyMenteeBuffs(menteePlayer);
				}
			}
			else
			{
				Mentoring.applyMenteeBuffs(this);
				Player mentorPlayer = World.getPlayer(getMenteeMentorList().getMentor());
				Mentoring.applyMentorBuffs(mentorPlayer);
			}
		}
	}
	
	/**
	 * Method mentoringLogoutConditions.
	 */
	public void mentoringLogoutConditions()
	{
		if (getMenteeMentorList().someOneOnline(false))
		{
			getMenteeMentorList().notify(false);

			if (getClassId().getId() > 138 && getLevel() > 85)
			{
				for (MenteeMentor mentee : getMenteeMentorList().getList().values())
				{
					Player menteePlayer = World.getPlayer(mentee.getName());
					Mentoring.cancelMenteeBuffs(menteePlayer);
				}
			}
			else
			{
				Player mentorPlayer = World.getPlayer(getMenteeMentorList().getMentor());
				Mentoring.cancelMentorBuffs(mentorPlayer);
			}
		}
	}
	
	/**
	 * Method isNotShowTraders.
	 * @return boolean
	 */
	public boolean isNotShowTraders()
	{
		return _notShowTraders;
	}
	
	/**
	 * Method setNotShowTraders.
	 * @param notShowTraders boolean
	 */
	public void setNotShowTraders(boolean notShowTraders)
	{
		_notShowTraders = notShowTraders;
	}
	
	/**
	 * Method isDebug.
	 * @return boolean
	 */
	public boolean isDebug()
	{
		return _debug;
	}
	
	/**
	 * Method setDebug.
	 * @param b boolean
	 */
	public void setDebug(boolean b)
	{
		_debug = b;
	}
	
	/**
	 * Method sendItemList.
	 * @param show boolean
	 */
	public void sendItemList(boolean show)
	{
		ItemInstance[] items = getInventory().getItems();
		LockType lockType = getInventory().getLockType();
		int[] lockItems = getInventory().getLockItems();
		int allSize = items.length;
		int questItemsSize = 0;
		int agathionItemsSize = 0;
		for (ItemInstance item : items)
		{
			if (item.getTemplate().isQuest())
			{
				questItemsSize++;
			}
			if (item.getTemplate().getAgathionEnergy() > 0)
			{
				agathionItemsSize++;
			}
		}
		sendPacket(new ItemList(allSize - questItemsSize, items, show, lockType, lockItems));
		if (questItemsSize > 0)
		{
			sendPacket(new ExQuestItemList(questItemsSize, items, lockType, lockItems));
		}
		if (agathionItemsSize > 0)
		{
			sendPacket(new ExBR_AgathionEnergyInfo(agathionItemsSize, items));
		}
	}
	
	/**
	 * Method getBeltInventoryIncrease.
	 * @return int
	 */
	public int getBeltInventoryIncrease()
	{
		ItemInstance item = getInventory().getPaperdollItem(Inventory.PAPERDOLL_BELT);
		if ((item != null) && (item.getTemplate().getAttachedSkills() != null))
		{
			for (Skill skill : item.getTemplate().getAttachedSkills())
			{
				for (FuncTemplate func : skill.getAttachedFuncs())
				{
					if (func._stat == Stats.INVENTORY_LIMIT)
					{
						return (int) func._value;
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * Method isPlayer.
	 * @return boolean
	 */
	@Override
	public boolean isPlayer()
	{
		return true;
	}
	
	/**
	 * Method checkCoupleAction.
	 * @param target Player
	 * @return boolean
	 */
	public boolean checkCoupleAction(Player target)
	{
		if (target.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IN_PRIVATE_STORE).addName(target));
			return false;
		}
		if (target.isFishing())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_FISHING).addName(target));
			return false;
		}
		if (target.isInCombat())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_COMBAT).addName(target));
			return false;
		}
		if (target.isCursedWeaponEquipped())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_CURSED_WEAPON_EQUIPED).addName(target));
			return false;
		}
		if (target.isInOlympiadMode())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_OLYMPIAD).addName(target));
			return false;
		}
		if (target.isOnSiegeField())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_SIEGE).addName(target));
			return false;
		}
		if (target.isInBoat() || (target.getMountNpcId() != 0))
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_VEHICLE_MOUNT_OTHER).addName(target));
			return false;
		}
		if (target.isTeleporting())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_TELEPORTING).addName(target));
			return false;
		}
		if (target.getTransformation() != 0)
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_TRANSFORM).addName(target));
			return false;
		}
		if (target.isDead())
		{
			sendPacket(new SystemMessage(SystemMessage.COUPLE_ACTION_CANNOT_C1_TARGET_IS_DEAD).addName(target));
			return false;
		}
		return true;
	}
	
	/**
	 * Method displayGiveDamageMessage.
	 * @param target Creature
	 * @param damage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param shld boolean
	 * @param magic boolean
	 */
	@Override
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		super.displayGiveDamageMessage(target, damage, crit, miss, shld, magic);
		if (crit)
		{
			if (magic)
			{
				sendPacket(new SystemMessage(SystemMessage.MAGIC_CRITICAL_HIT).addName(this).addDamage(target, target, damage));
			}
			else
			{
				sendPacket(new SystemMessage(SystemMessage.C1_HAD_A_CRITICAL_HIT).addName(this).addDamage(target, target, damage));
			}
		}
		if (miss)
		{
			sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addName(this).addDamage(target, target, damage));
		}
		else if (!target.isDamageBlocked())
		{
			sendPacket(new SystemMessage(SystemMessage.C1_HAS_GIVEN_C2_DAMAGE_OF_S3).addName(this).addName(target).addNumber(damage).addDamage(target, target, damage));
		}
		if (target.isPlayer())
		{
			if (shld && (damage > 1))
			{
				target.sendPacket(SystemMsg.YOUR_SHIELD_DEFENSE_HAS_SUCCEEDED);
			}
			else if (shld && (damage == 1))
			{
				target.sendPacket(SystemMsg.YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS);
			}
		}
	}
	
	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void displayReceiveDamageMessage(Creature attacker, int damage)
	{
		if (attacker != this)
		{
			sendPacket(new SystemMessage(SystemMessage.C1_HAS_RECEIVED_DAMAGE_OF_S3_FROM_C2).addName(this).addName(attacker).addNumber((long) damage).addDamage(attacker, attacker, damage));
		}
	}
	
	/**
	 * Method getPostFriends.
	 * @return IntObjectMap<String>
	 */
	public IntObjectMap<String> getPostFriends()
	{
		return _postFriends;
	}
	
	/**
	 * Method isSharedGroupDisabled.
	 * @param groupId int
	 * @return boolean
	 */
	public boolean isSharedGroupDisabled(int groupId)
	{
		TimeStamp sts = _sharedGroupReuses.get(groupId);
		if (sts == null)
		{
			return false;
		}
		if (sts.hasNotPassed())
		{
			return true;
		}
		_sharedGroupReuses.remove(groupId);
		return false;
	}
	
	/**
	 * Method getSharedGroupReuse.
	 * @param groupId int
	 * @return TimeStamp
	 */
	public TimeStamp getSharedGroupReuse(int groupId)
	{
		return _sharedGroupReuses.get(groupId);
	}
	
	/**
	 * Method addSharedGroupReuse.
	 * @param group int
	 * @param stamp TimeStamp
	 */
	public void addSharedGroupReuse(int group, TimeStamp stamp)
	{
		_sharedGroupReuses.put(group, stamp);
	}
	
	/**
	 * Method getSharedGroupReuses.
	 * @return Collection<IntObjectMap.Entry<TimeStamp>>
	 */
	public Collection<IntObjectMap.Entry<TimeStamp>> getSharedGroupReuses()
	{
		return _sharedGroupReuses.entrySet();
	}
	
	/**
	 * Method sendReuseMessage.
	 * @param item ItemInstance
	 */
	public void sendReuseMessage(ItemInstance item)
	{
		TimeStamp sts = getSharedGroupReuse(item.getTemplate().getReuseGroup());
		if ((sts == null) || !sts.hasNotPassed())
		{
			return;
		}
		long timeleft = sts.getReuseCurrent();
		long hours = timeleft / 3600000;
		long minutes = (timeleft - (hours * 3600000)) / 60000;
		long seconds = (long) Math.ceil((timeleft - (hours * 3600000) - (minutes * 60000)) / 1000.);
		if (hours > 0)
		{
			sendPacket(new SystemMessage2(item.getTemplate().getReuseType().getMessages()[2]).addItemName(item.getTemplate().getItemId()).addInteger(hours).addInteger(minutes).addInteger(seconds));
		}
		else if (minutes > 0)
		{
			sendPacket(new SystemMessage2(item.getTemplate().getReuseType().getMessages()[1]).addItemName(item.getTemplate().getItemId()).addInteger(minutes).addInteger(seconds));
		}
		else
		{
			sendPacket(new SystemMessage2(item.getTemplate().getReuseType().getMessages()[0]).addItemName(item.getTemplate().getItemId()).addInteger(seconds));
		}
	}
	
	/**
	 * Method ask.
	 * @param dlg ConfirmDlg
	 * @param listener OnAnswerListener
	 */
	public void ask(ConfirmDlg dlg, OnAnswerListener listener)
	{
		if (_askDialog != null)
		{
			return;
		}
		int rnd = Rnd.nextInt();
		_askDialog = new ImmutablePair<Integer, OnAnswerListener>(rnd, listener);
		dlg.setRequestId(rnd);
		sendPacket(dlg);
	}
	
	/**
	 * Method getAskListener.
	 * @param clear boolean
	 * @return Pair<Integer,OnAnswerListener>
	 */
	public Pair<Integer, OnAnswerListener> getAskListener(boolean clear)
	{
		if (!clear)
		{
			return _askDialog;
		}
		Pair<Integer, OnAnswerListener> ask = _askDialog;
		_askDialog = null;
		return ask;
	}
	
	/**
	 * Method isDead.
	 * @return boolean
	 */
	@Override
	public boolean isDead()
	{
		return (isInOlympiadMode() || isInDuel()) ? getCurrentHp() <= 1. : super.isDead();
	}
	
	/**
	 * Method getAgathionEnergy.
	 * @return int
	 */
	@Override
	public int getAgathionEnergy()
	{
		ItemInstance item = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LBRACELET);
		return item == null ? 0 : item.getAgathionEnergy();
	}
	
	/**
	 * Method setAgathionEnergy.
	 * @param val int
	 */
	@Override
	public void setAgathionEnergy(int val)
	{
		ItemInstance item = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LBRACELET);
		if (item == null)
		{
			return;
		}
		item.setAgathionEnergy(val);
		item.setJdbcState(JdbcEntityState.UPDATED);
		sendPacket(new ExBR_AgathionEnergyInfo(1, item));
	}
	
	/**
	 * Method hasPrivilege.
	 * @param privilege Privilege
	 * @return boolean
	 */
	public boolean hasPrivilege(Privilege privilege)
	{
		return (_clan != null) && ((getClanPrivileges() & privilege.mask()) == privilege.mask());
	}
	
	/**
	 * Method getMatchingRoom.
	 * @return MatchingRoom
	 */
	public MatchingRoom getMatchingRoom()
	{
		return _matchingRoom;
	}
	
	/**
	 * Method setMatchingRoom.
	 * @param matchingRoom MatchingRoom
	 */
	public void setMatchingRoom(MatchingRoom matchingRoom)
	{
		_matchingRoom = matchingRoom;
	}
	
	/**
	 * Method dispelBuffs.
	 */
	public void dispelBuffs()
	{
		for (Effect e : getEffectList().getAllEffects())
		{
			if (!e.getSkill().isOffensive() && !e.getSkill().isNewbie() && e.isCancelable() && !e.getSkill().isPreservedOnDeath())
			{
				sendPacket(new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(e.getSkill().getId(), e.getSkill().getLevel()));
				e.exit();
			}
		}
		for (Summon summon : getSummonList())
		{
			for (Effect e : summon.getEffectList().getAllEffects())
			{
				if (!e.getSkill().isOffensive() && !e.getSkill().isNewbie() && e.isCancelable() && !e.getSkill().isPreservedOnDeath())
				{
					e.exit();
				}
			}
		}
	}
	
	/**
	 * Method setInstanceReuse.
	 * @param id int
	 * @param time long
	 */
	public void setInstanceReuse(int id, long time)
	{
		final SystemMessage msg = new SystemMessage(SystemMessage.INSTANT_ZONE_FROM_HERE__S1_S_ENTRY_HAS_BEEN_RESTRICTED_YOU_CAN_CHECK_THE_NEXT_ENTRY_POSSIBLE).addString(getName());
		sendPacket(msg);
		_instancesReuses.put(id, time);
		mysql.set("REPLACE INTO character_instances (obj_id, id, reuse) VALUES (?,?,?)", getObjectId(), id, time);
	}
	
	/**
	 * Method removeInstanceReuse.
	 * @param id int
	 */
	public void removeInstanceReuse(int id)
	{
		if (_instancesReuses.remove(id) != null)
		{
			mysql.set("DELETE FROM `character_instances` WHERE `obj_id`=? AND `id`=? LIMIT 1", getObjectId(), id);
		}
	}
	
	/**
	 * Method removeAllInstanceReuses.
	 */
	public void removeAllInstanceReuses()
	{
		_instancesReuses.clear();
		mysql.set("DELETE FROM `character_instances` WHERE `obj_id`=?", getObjectId());
	}
	
	/**
	 * Method removeInstanceReusesByGroupId.
	 * @param groupId int
	 */
	public void removeInstanceReusesByGroupId(int groupId)
	{
		for (int i : InstantZoneHolder.getInstance().getSharedReuseInstanceIdsByGroup(groupId))
		{
			if (getInstanceReuse(i) != null)
			{
				removeInstanceReuse(i);
			}
		}
	}
	
	/**
	 * Method getInstanceReuse.
	 * @param id int
	 * @return Long
	 */
	public Long getInstanceReuse(int id)
	{
		return _instancesReuses.get(id);
	}
	
	/**
	 * Method getInstanceReuses.
	 * @return Map<Integer,Long>
	 */
	public Map<Integer, Long> getInstanceReuses()
	{
		return _instancesReuses;
	}
	
	/**
	 * Method loadInstanceReuses.
	 */
	private void loadInstanceReuses()
	{
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT * FROM character_instances WHERE obj_id = ?");
			offline.setInt(1, getObjectId());
			rs = offline.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("id");
				long reuse = rs.getLong("reuse");
				_instancesReuses.put(id, reuse);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
	}
	
	/**
	 * Method getActiveReflection.
	 * @return Reflection
	 */
	public Reflection getActiveReflection()
	{
		for (Reflection r : ReflectionManager.getInstance().getAll())
		{
			if ((r != null) && ArrayUtils.contains(r.getVisitors(), getObjectId()))
			{
				return r;
			}
		}
		return null;
	}
	
	/**
	 * Method canEnterInstance.
	 * @param instancedZoneId int
	 * @return boolean
	 */
	public boolean canEnterInstance(int instancedZoneId)
	{
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
		if (isDead())
		{
			return false;
		}
		if (ReflectionManager.getInstance().size() > Config.MAX_REFLECTIONS_COUNT)
		{
			sendPacket(SystemMsg.THE_MAXIMUM_NUMBER_OF_INSTANCE_ZONES_HAS_BEEN_EXCEEDED);
			return false;
		}
		if (iz == null)
		{
			sendPacket(SystemMsg.SYSTEM_ERROR);
			return false;
		}
		if (ReflectionManager.getInstance().getCountByIzId(instancedZoneId) >= iz.getMaxChannels())
		{
			sendPacket(SystemMsg.THE_MAXIMUM_NUMBER_OF_INSTANCE_ZONES_HAS_BEEN_EXCEEDED);
			return false;
		}
		return iz.getEntryType().canEnter(this, iz);
	}
	
	/**
	 * Method canReenterInstance.
	 * @param instancedZoneId int
	 * @return boolean
	 */
	public boolean canReenterInstance(int instancedZoneId)
	{
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
		if ((getActiveReflection() != null) && (getActiveReflection().getInstancedZoneId() != instancedZoneId))
		{
			sendPacket(SystemMsg.YOU_HAVE_ENTERED_ANOTHER_INSTANCE_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON);
			return false;
		}
		if (iz.isDispelBuffs())
		{
			dispelBuffs();
		}
		return iz.getEntryType().canReEnter(this, iz);
	}
	
	/**
	 * Method getBattlefieldChatId.
	 * @return int
	 */
	public int getBattlefieldChatId()
	{
		return _battlefieldChatId;
	}
	
	/**
	 * Method setBattlefieldChatId.
	 * @param battlefieldChatId int
	 */
	public void setBattlefieldChatId(int battlefieldChatId)
	{
		_battlefieldChatId = battlefieldChatId;
	}
	
	/**
	 * Method broadCast.
	 * @param packet IStaticPacket[]
	 * @see lineage2.gameserver.model.PlayerGroup#broadCast(IStaticPacket[])
	 */
	@Override
	public void broadCast(IStaticPacket... packet)
	{
		sendPacket(packet);
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<Player> * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Player> iterator()
	{
		return Collections.singleton(this).iterator();
	}
	
	/**
	 * Method getPlayerGroup.
	 * @return PlayerGroup
	 */
	public PlayerGroup getPlayerGroup()
	{
		if (getParty() != null)
		{
			if (getParty().getCommandChannel() != null)
			{
				return getParty().getCommandChannel();
			}
			return getParty();
		}
		return this;
	}
	
	/**
	 * Method isActionBlocked.
	 * @param action String
	 * @return boolean
	 */
	public boolean isActionBlocked(String action)
	{
		return _blockedActions.contains(action);
	}
	
	/**
	 * Method blockActions.
	 * @param actions String[]
	 */
	public void blockActions(String... actions)
	{
		Collections.addAll(_blockedActions, actions);
	}
	
	/**
	 * Method unblockActions.
	 * @param actions String[]
	 */
	public void unblockActions(String... actions)
	{
		for (String action : actions)
		{
			_blockedActions.remove(action);
		}
	}
	
	/**
	 * Method getOlympiadGame.
	 * @return OlympiadGame
	 */
	public OlympiadGame getOlympiadGame()
	{
		return _olympiadGame;
	}
	
	/**
	 * Method setOlympiadGame.
	 * @param olympiadGame OlympiadGame
	 */
	public void setOlympiadGame(OlympiadGame olympiadGame)
	{
		_olympiadGame = olympiadGame;
	}
	
	/**
	 * Method getOlympiadObserveGame.
	 * @return OlympiadGame
	 */
	public OlympiadGame getOlympiadObserveGame()
	{
		return _olympiadObserveGame;
	}
	
	/**
	 * Method setOlympiadObserveGame.
	 * @param olympiadObserveGame OlympiadGame
	 */
	public void setOlympiadObserveGame(OlympiadGame olympiadObserveGame)
	{
		_olympiadObserveGame = olympiadObserveGame;
	}
	
	/**
	 * Method addRadar.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void addRadar(int x, int y, int z)
	{
		sendPacket(new RadarControl(0, 1, x, y, z));
	}
	
	/**
	 * Method addRadarWithMap.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void addRadarWithMap(int x, int y, int z)
	{
		sendPacket(new RadarControl(0, 2, x, y, z));
	}
	
	/**
	 * Method getPetitionGroup.
	 * @return PetitionMainGroup
	 */
	public PetitionMainGroup getPetitionGroup()
	{
		return _petitionGroup;
	}
	
	/**
	 * Method setPetitionGroup.
	 * @param petitionGroup PetitionMainGroup
	 */
	public void setPetitionGroup(PetitionMainGroup petitionGroup)
	{
		_petitionGroup = petitionGroup;
	}
	
	/**
	 * Method getLectureMark.
	 * @return int
	 */
	public int getLectureMark()
	{
		return _lectureMark;
	}
	
	/**
	 * Method setLectureMark.
	 * @param lectureMark int
	 */
	public void setLectureMark(int lectureMark)
	{
		_lectureMark = lectureMark;
	}
	
	public void updateTargetSelectionInfo()
	{
		final GameObject obj = getTarget();

		if(obj != null)
		{
			updateTargetSelectionInfo(obj);
		}
	}

	public void updateTargetSelectionInfo(final GameObject ob)
	{
		sendPacket(new MyTargetSelected(this, ob));

		if(ob.isCreature() && !ob.isVehicle() && !ob.isDoor())
		{
			sendPacket(new ExAbnormalStatusUpdateFromTarget((Creature) ob));
		}
	}

	/**
	 * Method isAwaking.
	 * @return boolean
	 */
	public boolean isAwaking()
	{
		return getActiveClassId() > 138;
	}
	
	/**
	 * Method getCurrentJumpTrack.
	 * @return JumpTrack
	 */
	public JumpTrack getCurrentJumpTrack()
	{
		return _currentJumpTrack;
	}
	
	/**
	 * Method setCurrentJumpTrack.
	 * @param val JumpTrack
	 */
	public void setCurrentJumpTrack(JumpTrack val)
	{
		_currentJumpTrack = val;
	}
	
	/**
	 * Method getCurrentJumpWay.
	 * @return JumpWay
	 */
	public JumpWay getCurrentJumpWay()
	{
		return _currentJumpWay;
	}
	
	/**
	 * Method setCurrentJumpWay.
	 * @param val JumpWay
	 */
	public void setCurrentJumpWay(JumpWay val)
	{
		_currentJumpWay = val;
	}
	
	/**
	 * Method isInJumping.
	 * @return boolean
	 */
	public boolean isInJumping()
	{
		return _currentJumpTrack != null;
	}
	
	/**
	 * Method onJumpingBreak.
	 */
	public void onJumpingBreak()
	{
		sendActionFailed();
		unsetVar("@safe_jump_loc");
		setCurrentJumpTrack(null);
		setCurrentJumpWay(null);
	}
	
	/**
	 * Field is_bbs_use.
	 */
	private boolean is_bbs_use = false;
	
	/**
	 * Method setIsBBSUse.
	 * @param value boolean
	 */
	public void setIsBBSUse(boolean value)
	{
		is_bbs_use = value;
	}
	
	/**
	 * Method isBBSUse.
	 * @return boolean
	 */
	public boolean isBBSUse()
	{
		return is_bbs_use;
	}
	
	/**
	 * Method getSubClassList.
	 * @return SubClassList
	 */
	public SubClassList getSubClassList()
	{
		return _subClassList;
	}
	
	/**
	 * Method getBaseSubClass.
	 * @return SubClass
	 */
	public SubClass getBaseSubClass()
	{
		return _subClassList.getBaseSubClass();
	}
	
	/**
	 * Method getBaseClassId.
	 * @return int
	 */
	public int getBaseClassId()
	{
		if (getBaseSubClass() != null)
		{
			return getBaseSubClass().getClassId();
		}
		return -1;
	}
	
	/**
	 * Method getBaseDefaultClassId.
	 * @return int
	 */
	public int getBaseDefaultClassId()
	{
		if (getBaseSubClass() != null)
		{
			return getBaseSubClass().getDefaultClassId();
		}
		return -1;
	}
	
	/**
	 * Method getClassLevel.
	 * @return int
	 */
	public int getClassLevel()
	{
		return getClassId().getClassLevel().ordinal();
	}
	
	/**
	 * Field _acquiredItemMonthly.
	 */
	HashMap<Integer, Long> _acquiredItemMonthly = new HashMap<Integer, Long>();
	/**
	 * Field _acquiredItemTotal.
	 */
	HashMap<Integer, Long> _acquiredItemTotal = new HashMap<Integer, Long>();
	
	/**
	 * Method getAcquiredItem.
	 * @param category int
	 * @param isTotalInformation boolean
	 * @return long
	 */
	public long getAcquiredItem(int category, boolean isTotalInformation)
	{
		if (isTotalInformation)
		{
			if (_acquiredItemTotal.containsKey(category))
			{
				return _acquiredItemTotal.get(category);
			}
			return 0;
		}
		if (_acquiredItemMonthly.containsKey(category))
		{
			return _acquiredItemMonthly.get(category);
		}
		return 0;
	}
	
	/**
	 * Method setAcquiredItem.
	 * @param category int
	 * @param acquiredXP long
	 * @param isTotal boolean
	 */
	public void setAcquiredItem(int category, long acquiredXP, boolean isTotal)
	{
		if (isTotal)
		{
			if (_acquiredItemTotal.containsKey(category))
			{
				_acquiredItemTotal.remove(category);
			}
			_acquiredItemTotal.put(category, acquiredXP);
			return;
		}
		if (_acquiredItemMonthly.containsKey(category))
		{
			_acquiredItemMonthly.remove(category);
		}
		_acquiredItemMonthly.put(category, acquiredXP);
	}
	
	/**
	 * Method getOnlineTime.
	 * @param isTotalInformation boolean
	 * @return long
	 */
	public long getOnlineTime(boolean isTotalInformation)
	{
		long totalOnlineTime = _onlineTime;
		if (_onlineBeginTime > 0)
		{
			totalOnlineTime += (System.currentTimeMillis() - _onlineBeginTime) / 1000;
		}
		if (isTotalInformation)
		{
			return totalOnlineTime;
		}
		return totalOnlineTime;
	}
	
	/**
	 * Field partyTime.
	 */
	long partyTime = 0;
	
	/**
	 * Method setPartyTime.
	 * @param _partyTime long
	 */
	public void setPartyTime(long _partyTime)
	{
		partyTime = _partyTime;
	}
	
	/**
	 * Method getPartyTime.
	 * @return long
	 */
	public long getPartyTime()
	{
		return partyTime;
	}
	
	/**
	 * Field fullPartyTime.
	 */
	long fullPartyTime = 0;
	
	/**
	 * Method setFullPartyTime.
	 * @param _fullPartyTime long
	 */
	public void setFullPartyTime(long _fullPartyTime)
	{
		fullPartyTime = _fullPartyTime;
	}
	
	/**
	 * Method getFullPartyTime.
	 * @return long
	 */
	public long getFullPartyTime()
	{
		return fullPartyTime;
	}
	
	/**
	 * Method getStablePoint.
	 * @return Location
	 */
	public Location getStablePoint()
	{
		return _stablePoint;
	}
	
	/**
	 * Method setStablePoint.
	 * @param point Location
	 */
	public void setStablePoint(Location point)
	{
		_stablePoint = point;
	}
	
	/**
	 * Method checkAllowAction.
	 * @return boolean
	 */
	public boolean checkAllowAction()
	{
		if (getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isFishing())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isInCombat())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isCursedWeaponEquipped())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isInOlympiadMode())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isOnSiegeField())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isInBoat() || (getMountNpcId() != 0))
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isTeleporting())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (getTransformation() != 0)
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (isDead())
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		if (getTeam() != TeamType.NONE)
		{
			sendMessage("Action is not allowed.");
			return false;
		}
		return true;
	}
	
	/**
	 * Method isBaseClassActive.
	 * @return boolean
	 */
	public boolean isBaseClassActive()
	{
		return getActiveSubClass().isBase();
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public int getMaxLevel()
	{
		if (getActiveSubClass() != null)
		{
			return getActiveSubClass().getMaxLevel();
		}
		return Experience.getMaxLevel();
	}
	
	/**
	 * Method getTree.
	 * @return boolean
	 */
	public boolean getTree()
	{
		return _tree;
	}
	
	/**
	 * Method setTree.
	 * @param tree boolean
	 */
	public void setTree(boolean tree)
	{
		_tree = tree;
	}
	
	/**
	 * Method startSkillCastingTask.
	 * @param skillId int
	 * @param skilllLevel int
	 */
	public void startSkillCastingTask(int skillId, int skilllLevel)
	{
		_skill = SkillTable.getInstance().getInfo(skillId, skilllLevel);
		if (_skillCastingTask == null)
		{
			_skillCastingTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SummonSkillCastingTask(this), 100L, _skill.getReuseDelay());
		}
	}
	
	/**
	 * Method stopSkillCastingTask.
	 */
	public void stopSkillCastingTask()
	{
		if (_skillCastingTask != null)
		{
			_skillCastingTask.cancel(false);
			_skillCastingTask = null;
		}
	}
	
	/**
	 * Field _skillCastingTask.
	 */
	ScheduledFuture<?> _skillCastingTask;
	/**
	 * Field _skill.
	 */
	static Skill _skill;
	
	/**
	 * Constructor for SummonSkillCastingTask.
	 * @param caster Player
	 * @author Mobius
	 */
	private class SummonSkillCastingTask implements Runnable
	{
		/**
		 * Field _caster.
		 */
		private final Player _caster;
		
		/**
		 * Constructor for SummonSkillCastingTask.
		 * @param caster Player
		 */
		protected SummonSkillCastingTask(Player caster)
		{
			_caster = caster;
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if (_skill == null)
			{
				if (_skillCastingTask != null)
				{
					_skillCastingTask.cancel(false);
					_skillCastingTask = null;
				}
				return;
			}
			_caster.doCast(_skill, _caster, true);
		}
	}
	
	/**
	 * Method setHero.
	 * @param player Player
	 */
	public void setHero(Player player)
	{
		StatsSet hero = new StatsSet();
		hero.set(Olympiad.CLASS_ID, player.getClassId());
		hero.set(Olympiad.CHAR_ID, player.getObjectId());
		hero.set(Olympiad.CHAR_NAME, player.getName());
		hero.set(Hero.ACTIVE, 1);
		List<StatsSet> heroesToBe = new ArrayList<StatsSet>();
		heroesToBe.add(hero);
		Hero.getInstance().computeNewHeroes(heroesToBe);
		player.setHero(true);
		Hero.addSkills(player);
		player.updatePledgeClass();
		if (player.isHero())
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 16));
		}
		player.broadcastUserInfo();
	}
	
	/**
	 * Method setServitorShareRestore.
	 * */
	public void setServitorShareRestore(boolean result, Effect effectToRestore)
	{
		_ServitorShareRestore = result;
		_ServitorShareRestoreData = effectToRestore;
	}
	
	/**
	 * Method getPing.
	 * @return int
	 */
	public int getPing()
	{
		return _ping;
	}
	
	/**
	 * Method setPing.
	 * @param ping int
	 */
	public void setPing(int ping)
	{
		_ping = ping;
	}

	public long getStartingTimeInFullParty()
	{
		return _startingTimeInFullParty;
	}

	public void setStartingTimeInFullParty(long time)
	{
		_startingTimeInFullParty = time;
	}

	public long getStartingTimeInParty()
	{
		return _startingTimeInParty;
	}

	public void setStartingTimeInParty(long time)
	{
		_startingTimeInParty = time;
	}

	@Override
	public Collection<Summon> getPets()
	{
		if (_summons != null)
			return _summons.values();
		return new ArrayList<Summon>(0);
	}

	public boolean isChaotic()
	{
		return getKarma() < 0;
	}

	public void setAppearanceStone(final ItemInstance enchantItem)
	{
		_enchantItem = enchantItem;
	}

	public ItemInstance getAppearanceStone()
	{
		return _enchantItem;
	}

	public void setAppearanceExtractItem(final ItemInstance supportItem)
	{
		_enchantSupportItem = supportItem;
	}

	public ItemInstance getAppearanceExtractItem()
	{
		return _enchantSupportItem;
	}

}

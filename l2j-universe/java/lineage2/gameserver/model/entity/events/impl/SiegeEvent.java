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
package lineage2.gameserver.model.entity.events.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.ZoneObject;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.SummonInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.TimeUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class SiegeEvent<R extends Residence, S extends SiegeClanObject> extends GlobalEvent
{
	/**
	 * @author Mobius
	 */
	public class DoorDeathListener implements OnDeathListener
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
			if (!isInProgress())
			{
				return;
			}
			DoorInstance door = (DoorInstance) actor;
			if (door.getDoorType() == DoorTemplate.DoorType.WALL)
			{
				return;
			}
			broadcastTo(SystemMsg.THE_CASTLE_GATE_HAS_BEEN_DESTROYED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class KillListener implements OnKillListener
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
			Player winner = actor.getPlayer();
			if ((winner == null) || !victim.isPlayer() || (winner.getLevel() < 40) || (winner == victim) || (victim.getEvent(SiegeEvent.this.getClass()) != SiegeEvent.this) || !checkIfInZone(actor) || !checkIfInZone(victim))
			{
				return;
			}
			winner.setFame(winner.getFame() + Rnd.get(10, 20), SiegeEvent.this.toString());
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
	 * Field OWNER. (value is ""owner"")
	 */
	public static final String OWNER = "owner";
	/**
	 * Field OLD_OWNER. (value is ""old_owner"")
	 */
	public static final String OLD_OWNER = "old_owner";
	/**
	 * Field ATTACKERS. (value is ""attackers"")
	 */
	public static final String ATTACKERS = "attackers";
	/**
	 * Field DEFENDERS. (value is ""defenders"")
	 */
	public static final String DEFENDERS = "defenders";
	/**
	 * Field SPECTATORS. (value is ""spectators"")
	 */
	public static final String SPECTATORS = "spectators";
	/**
	 * Field SIEGE_ZONES. (value is ""siege_zones"")
	 */
	public static final String SIEGE_ZONES = "siege_zones";
	/**
	 * Field FLAG_ZONES. (value is ""flag_zones"")
	 */
	public static final String FLAG_ZONES = "flag_zones";
	/**
	 * Field DAY_OF_WEEK. (value is ""day_of_week"")
	 */
	public static final String DAY_OF_WEEK = "day_of_week";
	/**
	 * Field HOUR_OF_DAY. (value is ""hour_of_day"")
	 */
	public static final String HOUR_OF_DAY = "hour_of_day";
	/**
	 * Field REGISTRATION. (value is ""registration"")
	 */
	public static final String REGISTRATION = "registration";
	/**
	 * Field DOORS. (value is ""doors"")
	 */
	public static final String DOORS = "doors";
	/**
	 * Field _residence.
	 */
	protected R _residence;
	/**
	 * Field _isInProgress.
	 */
	private boolean _isInProgress;
	/**
	 * Field _isRegistrationOver.
	 */
	private boolean _isRegistrationOver;
	/**
	 * Field _dayOfWeek.
	 */
	protected int _dayOfWeek;
	/**
	 * Field _hourOfDay.
	 */
	protected int _hourOfDay;
	/**
	 * Field _oldOwner.
	 */
	protected Clan _oldOwner;
	/**
	 * Field _killListener.
	 */
	protected OnKillListener _killListener = new KillListener();
	/**
	 * Field _doorDeathListener.
	 */
	protected OnDeathListener _doorDeathListener = new DoorDeathListener();
	/**
	 * Field _siegeSummons.
	 */
	protected List<HardReference<SummonInstance>> _siegeSummons = new ArrayList<>();
	
	/**
	 * Constructor for SiegeEvent.
	 * @param set MultiValueSet<String>
	 */
	public SiegeEvent(MultiValueSet<String> set)
	{
		super(set);
		_dayOfWeek = set.getInteger(DAY_OF_WEEK, 0);
		_hourOfDay = set.getInteger(HOUR_OF_DAY, 0);
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		setInProgress(true);
		super.startEvent();
	}
	
	/**
	 * Method stopEvent.
	 */
	@Override
	public final void stopEvent()
	{
		stopEvent(false);
	}
	
	/**
	 * Method stopEvent.
	 * @param step boolean
	 */
	public void stopEvent(boolean step)
	{
		despawnSiegeSummons();
		setInProgress(false);
		reCalcNextTime(false);
		super.stopEvent();
	}
	
	/**
	 * Method processStep.
	 * @param clan Clan
	 */
	public void processStep(Clan clan)
	{
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		final Calendar startSiegeDate = getResidence().getSiegeDate();
		if (onInit)
		{
			if (startSiegeDate.getTimeInMillis() <= System.currentTimeMillis())
			{
				startSiegeDate.set(Calendar.DAY_OF_WEEK, _dayOfWeek);
				startSiegeDate.set(Calendar.HOUR_OF_DAY, _hourOfDay);
				validateSiegeDate(startSiegeDate, 2);
				getResidence().setJdbcState(JdbcEntityState.UPDATED);
			}
		}
		else
		{
			startSiegeDate.add(Calendar.WEEK_OF_YEAR, 2);
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
		}
		registerActions();
		getResidence().update();
	}
	
	/**
	 * Method validateSiegeDate.
	 * @param calendar Calendar
	 * @param add int
	 */
	protected void validateSiegeDate(Calendar calendar, int add)
	{
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		while (calendar.getTimeInMillis() < System.currentTimeMillis())
		{
			calendar.add(Calendar.WEEK_OF_YEAR, add);
		}
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return getResidence().getSiegeDate().getTimeInMillis();
	}
	
	/**
	 * Method teleportPlayers.
	 * @param t String
	 */
	@Override
	public void teleportPlayers(String t)
	{
		List<Player> players = new ArrayList<>();
		Clan ownerClan = getResidence().getOwner();
		if (t.equalsIgnoreCase(OWNER))
		{
			if (ownerClan != null)
			{
				for (Player player : getPlayersInZone())
				{
					if (player.getClan() == ownerClan)
					{
						players.add(player);
					}
				}
			}
		}
		else if (t.equalsIgnoreCase(ATTACKERS))
		{
			for (Player player : getPlayersInZone())
			{
				S siegeClan = getSiegeClan(ATTACKERS, player.getClan());
				if ((siegeClan != null) && siegeClan.isParticle(player))
				{
					players.add(player);
				}
			}
		}
		else if (t.equalsIgnoreCase(DEFENDERS))
		{
			for (Player player : getPlayersInZone())
			{
				if ((ownerClan != null) && (player.getClan() != null) && (player.getClan() == ownerClan))
				{
					continue;
				}
				S siegeClan = getSiegeClan(DEFENDERS, player.getClan());
				if ((siegeClan != null) && siegeClan.isParticle(player))
				{
					players.add(player);
				}
			}
		}
		else if (t.equalsIgnoreCase(SPECTATORS))
		{
			for (Player player : getPlayersInZone())
			{
				if ((ownerClan != null) && (player.getClan() != null) && (player.getClan() == ownerClan))
				{
					continue;
				}
				if ((player.getClan() == null) || ((getSiegeClan(ATTACKERS, player.getClan()) == null) && (getSiegeClan(DEFENDERS, player.getClan()) == null)))
				{
					players.add(player);
				}
			}
		}
		else
		{
			players = getPlayersInZone();
		}
		for (Player player : players)
		{
			Location loc = null;
			if (t.equalsIgnoreCase(OWNER) || t.equalsIgnoreCase(DEFENDERS))
			{
				loc = getResidence().getOwnerRestartPoint();
			}
			else
			{
				loc = getResidence().getNotOwnerRestartPoint(player);
			}
			player.teleToLocation(loc, ReflectionManager.DEFAULT);
		}
	}
	
	/**
	 * Method getPlayersInZone.
	 * @return List<Player>
	 */
	public List<Player> getPlayersInZone()
	{
		List<ZoneObject> zones = getObjects(SIEGE_ZONES);
		List<Player> result = new LazyArrayList<>();
		for (ZoneObject zone : zones)
		{
			result.addAll(zone.getInsidePlayers());
		}
		return result;
	}
	
	/**
	 * Method broadcastInZone.
	 * @param packet L2GameServerPacket[]
	 */
	public void broadcastInZone(L2GameServerPacket... packet)
	{
		for (Player player : getPlayersInZone())
		{
			player.sendPacket(packet);
		}
	}
	
	/**
	 * Method broadcastInZone.
	 * @param packet IStaticPacket[]
	 */
	public void broadcastInZone(IStaticPacket... packet)
	{
		for (Player player : getPlayersInZone())
		{
			player.sendPacket(packet);
		}
	}
	
	/**
	 * Method checkIfInZone.
	 * @param character Creature
	 * @return boolean
	 */
	public boolean checkIfInZone(Creature character)
	{
		List<ZoneObject> zones = getObjects(SIEGE_ZONES);
		for (ZoneObject zone : zones)
		{
			if (zone.checkIfInZone(character))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method broadcastInZone2.
	 * @param packet IStaticPacket[]
	 */
	public void broadcastInZone2(IStaticPacket... packet)
	{
		for (Player player : getResidence().getZone().getInsidePlayers())
		{
			player.sendPacket(packet);
		}
	}
	
	/**
	 * Method broadcastInZone2.
	 * @param packet L2GameServerPacket[]
	 */
	public void broadcastInZone2(L2GameServerPacket... packet)
	{
		for (Player player : getResidence().getZone().getInsidePlayers())
		{
			player.sendPacket(packet);
		}
	}
	
	/**
	 * Method loadSiegeClans.
	 */
	public void loadSiegeClans()
	{
		addObjects(ATTACKERS, SiegeClanDAO.getInstance().load(getResidence(), ATTACKERS));
		addObjects(DEFENDERS, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS));
	}
	
	/**
	 * Method newSiegeClan.
	 * @param type String
	 * @param clanId int
	 * @param param long
	 * @param date long
	 * @return S
	 */
	@SuppressWarnings("unchecked")
	public S newSiegeClan(String type, int clanId, long param, long date)
	{
		Clan clan = ClanTable.getInstance().getClan(clanId);
		return clan == null ? null : (S) new SiegeClanObject(type, clan, param, date);
	}
	
	/**
	 * Method updateParticles.
	 * @param start boolean
	 * @param arg String[]
	 */
	public void updateParticles(boolean start, String... arg)
	{
		for (String a : arg)
		{
			List<SiegeClanObject> siegeClans = getObjects(a);
			for (SiegeClanObject s : siegeClans)
			{
				s.setEvent(start, this);
			}
		}
	}
	
	/**
	 * Method getSiegeClan.
	 * @param name String
	 * @param clan Clan
	 * @return S
	 */
	public S getSiegeClan(String name, Clan clan)
	{
		if (clan == null)
		{
			return null;
		}
		return getSiegeClan(name, clan.getClanId());
	}
	
	/**
	 * Method getSiegeClan.
	 * @param name String
	 * @param objectId int
	 * @return S
	 */
	@SuppressWarnings("unchecked")
	public S getSiegeClan(String name, int objectId)
	{
		List<SiegeClanObject> siegeClanList = getObjects(name);
		if (siegeClanList.isEmpty())
		{
			return null;
		}
		for (int i = 0; i < siegeClanList.size(); i++)
		{
			SiegeClanObject siegeClan = siegeClanList.get(i);
			if (siegeClan.getObjectId() == objectId)
			{
				return (S) siegeClan;
			}
		}
		return null;
	}
	
	/**
	 * Method broadcastTo.
	 * @param packet IStaticPacket
	 * @param types String[]
	 */
	public void broadcastTo(IStaticPacket packet, String... types)
	{
		for (String type : types)
		{
			List<SiegeClanObject> siegeClans = getObjects(type);
			for (SiegeClanObject siegeClan : siegeClans)
			{
				siegeClan.broadcast(packet);
			}
		}
	}
	
	/**
	 * Method broadcastTo.
	 * @param packet L2GameServerPacket
	 * @param types String[]
	 */
	public void broadcastTo(L2GameServerPacket packet, String... types)
	{
		for (String type : types)
		{
			List<SiegeClanObject> siegeClans = getObjects(type);
			for (SiegeClanObject siegeClan : siegeClans)
			{
				siegeClan.broadcast(packet);
			}
		}
	}
	
	/**
	 * Method initEvent.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void initEvent()
	{
		_residence = (R) ResidenceHolder.getInstance().getResidence(getId());
		loadSiegeClans();
		clearActions();
		super.initEvent();
	}
	
	/**
	 * Method printInfo.
	 */
	@Override
	protected void printInfo()
	{
		final long startSiegeMillis = startTimeMillis();
		if (startSiegeMillis == 0)
		{
			info(getName() + " time - undefined");
		}
		else
		{
			info(getName() + " time - " + TimeUtils.toSimpleFormat(startSiegeMillis));
		}
	}
	
	/**
	 * Method ifVar.
	 * @param name String
	 * @return boolean
	 */
	@Override
	public boolean ifVar(String name)
	{
		if (name.equals(OWNER))
		{
			return getResidence().getOwner() != null;
		}
		if (name.equals(OLD_OWNER))
		{
			return _oldOwner != null;
		}
		return false;
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	@Override
	public boolean isParticle(Player player)
	{
		if (!isInProgress() || (player.getClan() == null))
		{
			return false;
		}
		return (getSiegeClan(ATTACKERS, player.getClan()) != null) || (getSiegeClan(DEFENDERS, player.getClan()) != null);
	}
	
	/**
	 * Method checkRestartLocs.
	 * @param player Player
	 * @param r Map<RestartType,Boolean>
	 */
	@Override
	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
		if (getObjects(FLAG_ZONES).isEmpty())
		{
			return;
		}
		S clan = getSiegeClan(ATTACKERS, player.getClan());
		if (clan != null)
		{
			if (clan.getFlag() != null)
			{
				r.put(RestartType.TO_FLAG, Boolean.TRUE);
			}
		}
	}
	
	/**
	 * Method getRestartLoc.
	 * @param player Player
	 * @param type RestartType
	 * @return Location
	 */
	@Override
	public Location getRestartLoc(Player player, RestartType type)
	{
		S attackerClan = getSiegeClan(ATTACKERS, player.getClan());
		Location loc = null;
		switch (type)
		{
			case TO_FLAG:
				if (!getObjects(FLAG_ZONES).isEmpty() && (attackerClan != null) && (attackerClan.getFlag() != null))
				{
					loc = Location.findPointToStay(attackerClan.getFlag(), 50, 75);
				}
				else
				{
					player.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
				}
				break;
			default:
				break;
		}
		return loc;
	}
	
	/**
	 * Method getRelation.
	 * @param thisPlayer Player
	 * @param targetPlayer Player
	 * @param result int
	 * @return int
	 */
	@Override
	public int getRelation(Player thisPlayer, Player targetPlayer, int result)
	{
		Clan clan1 = thisPlayer.getClan();
		Clan clan2 = targetPlayer.getClan();
		if ((clan1 == null) || (clan2 == null))
		{
			return result;
		}
		SiegeEvent<?, ?> siegeEvent2 = targetPlayer.getEvent(SiegeEvent.class);
		if (this == siegeEvent2)
		{
			result |= RelationChanged.RELATION_INSIEGE;
			SiegeClanObject siegeClan1 = getSiegeClan(SiegeEvent.ATTACKERS, clan1);
			SiegeClanObject siegeClan2 = getSiegeClan(SiegeEvent.ATTACKERS, clan2);
			if (((siegeClan1 == null) && (siegeClan2 == null)) || ((siegeClan1 != null) && (siegeClan2 != null) && isAttackersInAlly()))
			{
				result |= RelationChanged.RELATION_ALLY;
			}
			else
			{
				result |= RelationChanged.RELATION_ENEMY;
			}
			if (siegeClan1 != null)
			{
				result |= RelationChanged.RELATION_ATTACKER;
			}
		}
		return result;
	}
	
	/**
	 * Method getUserRelation.
	 * @param thisPlayer Player
	 * @param oldRelation int
	 * @return int
	 */
	@Override
	public int getUserRelation(Player thisPlayer, int oldRelation)
	{
		SiegeClanObject siegeClan = getSiegeClan(SiegeEvent.ATTACKERS, thisPlayer.getClan());
		if (siegeClan != null)
		{
			oldRelation |= 0x180;
		}
		else
		{
			oldRelation |= 0x80;
		}
		return oldRelation;
	}
	
	/**
	 * Method checkForAttack.
	 * @param target Creature
	 * @param attacker Creature
	 * @param skill Skill
	 * @param force boolean
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		SiegeEvent<?, ?> siegeEvent = target.getEvent(SiegeEvent.class);
		if (this != siegeEvent)
		{
			return null;
		}
		if (!checkIfInZone(target) || !checkIfInZone(attacker))
		{
			return null;
		}
		Player player = target.getPlayer();
		if (player == null)
		{
			return null;
		}
		SiegeClanObject siegeClan1 = getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
		if ((siegeClan1 == null) && attacker.isSiegeGuard())
		{
			return SystemMsg.INVALID_TARGET;
		}
		Player playerAttacker = attacker.getPlayer();
		if (playerAttacker == null)
		{
			return SystemMsg.INVALID_TARGET;
		}
		SiegeClanObject siegeClan2 = getSiegeClan(SiegeEvent.ATTACKERS, playerAttacker.getClan());
		if ((siegeClan1 != null) && (siegeClan2 != null) && isAttackersInAlly())
		{
			return SystemMsg.FORCE_ATTACK_IS_IMPOSSIBLE_AGAINST_A_TEMPORARY_ALLIED_MEMBER_DURING_A_SIEGE;
		}
		if ((siegeClan1 == null) && (siegeClan2 == null))
		{
			return SystemMsg.INVALID_TARGET;
		}
		return null;
	}
	
	/**
	 * Method isInProgress.
	 * @return boolean
	 */
	@Override
	public boolean isInProgress()
	{
		return _isInProgress;
	}
	
	/**
	 * Method action.
	 * @param name String
	 * @param start boolean
	 */
	@Override
	public void action(String name, boolean start)
	{
		if (name.equalsIgnoreCase(REGISTRATION))
		{
			setRegistrationOver(!start);
		}
		else
		{
			super.action(name, start);
		}
	}
	
	/**
	 * Method isAttackersInAlly.
	 * @return boolean
	 */
	public boolean isAttackersInAlly()
	{
		return false;
	}
	
	/**
	 * Method onAddEvent.
	 * @param object GameObject
	 */
	@Override
	public void onAddEvent(GameObject object)
	{
		if (_killListener == null)
		{
			return;
		}
		if (object.isPlayer())
		{
			((Player) object).addListener(_killListener);
		}
	}
	
	/**
	 * Method onRemoveEvent.
	 * @param object GameObject
	 */
	@Override
	public void onRemoveEvent(GameObject object)
	{
		if (_killListener == null)
		{
			return;
		}
		if (object.isPlayer())
		{
			((Player) object).removeListener(_killListener);
		}
	}
	
	/**
	 * Method broadcastPlayers.
	 * @param range int
	 * @return List<Player>
	 */
	@Override
	public List<Player> broadcastPlayers(int range)
	{
		return itemObtainPlayers();
	}
	
	/**
	 * Method itemObtainPlayers.
	 * @return List<Player>
	 */
	@Override
	public List<Player> itemObtainPlayers()
	{
		List<Player> playersInZone = getPlayersInZone();
		List<Player> list = new LazyArrayList<>(playersInZone.size());
		for (Player player : getPlayersInZone())
		{
			if (player.getEvent(getClass()) == this)
			{
				list.add(player);
			}
		}
		return list;
	}
	
	/**
	 * Method getEnterLoc.
	 * @param player Player
	 * @return Location
	 */
	public Location getEnterLoc(Player player)
	{
		S siegeClan = getSiegeClan(ATTACKERS, player.getClan());
		if (siegeClan != null)
		{
			if (siegeClan.getFlag() != null)
			{
				return Location.findAroundPosition(siegeClan.getFlag(), 50, 75);
			}
			return getResidence().getNotOwnerRestartPoint(player);
		}
		return getResidence().getOwnerRestartPoint();
	}
	
	/**
	 * Method getResidence.
	 * @return R
	 */
	public R getResidence()
	{
		return _residence;
	}
	
	/**
	 * Method setInProgress.
	 * @param b boolean
	 */
	public void setInProgress(boolean b)
	{
		_isInProgress = b;
	}
	
	/**
	 * Method isRegistrationOver.
	 * @return boolean
	 */
	public boolean isRegistrationOver()
	{
		return _isRegistrationOver;
	}
	
	/**
	 * Method setRegistrationOver.
	 * @param b boolean
	 */
	public void setRegistrationOver(boolean b)
	{
		_isRegistrationOver = b;
	}
	
	/**
	 * Method addSiegeSummon.
	 * @param summon SummonInstance
	 */
	public void addSiegeSummon(SummonInstance summon)
	{
		_siegeSummons.add(summon.getRef());
	}
	
	/**
	 * Method containsSiegeSummon.
	 * @param cha SummonInstance
	 * @return boolean
	 */
	public boolean containsSiegeSummon(SummonInstance cha)
	{
		return _siegeSummons.contains(cha.getRef());
	}
	
	/**
	 * Method despawnSiegeSummons.
	 */
	public void despawnSiegeSummons()
	{
		for (HardReference<SummonInstance> ref : _siegeSummons)
		{
			SummonInstance summon = ref.get();
			if (summon != null)
			{
				summon.unSummon();
			}
		}
		_siegeSummons.clear();
	}
}

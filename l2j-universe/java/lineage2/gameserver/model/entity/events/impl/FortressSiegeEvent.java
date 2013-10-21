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

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.listener.actor.npc.OnSpawnListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.events.objects.DoorObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.entity.events.objects.StaticObjectObject;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.TimeUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FortressSiegeEvent extends SiegeEvent<Fortress, SiegeClanObject>
{
	/**
	 * @author Mobius
	 */
	private class EnvoyDespawn extends RunnableImpl
	{
		/**
		 * Constructor for EnvoyDespawn.
		 */
		public EnvoyDespawn()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			despawnEnvoy();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class RestoreBarracksListener implements OnSpawnListener
	{
		/**
		 * Constructor for RestoreBarracksListener.
		 */
		public RestoreBarracksListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onSpawn.
		 * @param actor NpcInstance
		 * @see lineage2.gameserver.listener.actor.npc.OnSpawnListener#onSpawn(NpcInstance)
		 */
		@Override
		public void onSpawn(NpcInstance actor)
		{
			FortressSiegeEvent siegeEvent = actor.getEvent(FortressSiegeEvent.class);
			SpawnExObject siegeCommanders = siegeEvent.getFirstObject(FortressSiegeEvent.SIEGE_COMMANDERS);
			if (siegeCommanders.isSpawned())
			{
				siegeEvent.broadcastTo(SystemMsg.THE_BARRACKS_FUNCTION_HAS_BEEN_RESTORED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
			}
		}
	}
	
	/**
	 * Field FLAG_POLE. (value is ""flag_pole"")
	 */
	public static final String FLAG_POLE = "flag_pole";
	/**
	 * Field COMBAT_FLAGS. (value is ""combat_flags"")
	 */
	public static final String COMBAT_FLAGS = "combat_flags";
	/**
	 * Field SIEGE_COMMANDERS. (value is ""siege_commanders"")
	 */
	public static final String SIEGE_COMMANDERS = "siege_commanders";
	/**
	 * Field PEACE_COMMANDERS. (value is ""peace_commanders"")
	 */
	public static final String PEACE_COMMANDERS = "peace_commanders";
	/**
	 * Field UPGRADEABLE_DOORS. (value is ""upgradeable_doors"")
	 */
	public static final String UPGRADEABLE_DOORS = "upgradeable_doors";
	/**
	 * Field COMMANDER_DOORS. (value is ""commander_doors"")
	 */
	public static final String COMMANDER_DOORS = "commander_doors";
	/**
	 * Field ENTER_DOORS. (value is ""enter_doors"")
	 */
	public static final String ENTER_DOORS = "enter_doors";
	/**
	 * Field MACHINE_DOORS. (value is ""machine_doors"")
	 */
	public static final String MACHINE_DOORS = "machine_doors";
	/**
	 * Field OUT_POWER_UNITS. (value is ""out_power_units"")
	 */
	public static final String OUT_POWER_UNITS = "out_power_units";
	/**
	 * Field IN_POWER_UNITS. (value is ""in_power_units"")
	 */
	public static final String IN_POWER_UNITS = "in_power_units";
	/**
	 * Field GUARDS_LIVE_WITH_C_CENTER. (value is ""guards_live_with_c_center"")
	 */
	public static final String GUARDS_LIVE_WITH_C_CENTER = "guards_live_with_c_center";
	/**
	 * Field ENVOY. (value is ""envoy"")
	 */
	public static final String ENVOY = "envoy";
	/**
	 * Field MERCENARY_POINTS. (value is ""mercenary_points"")
	 */
	public static final String MERCENARY_POINTS = "mercenary_points";
	/**
	 * Field MERCENARY. (value is ""mercenary"")
	 */
	public static final String MERCENARY = "mercenary";
	/**
	 * Field SIEGE_WAIT_PERIOD.
	 */
	public static final long SIEGE_WAIT_PERIOD = 4 * 60 * 60 * 1000L;
	/**
	 * Field RESTORE_BARRACKS_LISTENER.
	 */
	public static final OnSpawnListener RESTORE_BARRACKS_LISTENER = new RestoreBarracksListener();
	/**
	 * Field _envoyTask.
	 */
	private Future<?> _envoyTask;
	/**
	 * Field _barrackStatus.
	 */
	private boolean[] _barrackStatus;
	
	/**
	 * Constructor for FortressSiegeEvent.
	 * @param set MultiValueSet<String>
	 */
	public FortressSiegeEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Method processStep.
	 * @param newOwnerClan Clan
	 */
	@Override
	public void processStep(Clan newOwnerClan)
	{
		if (newOwnerClan.getCastle() > 0)
		{
			getResidence().changeOwner(null);
		}
		else
		{
			getResidence().changeOwner(newOwnerClan);
			stopEvent(true);
		}
	}
	
	/**
	 * Method initEvent.
	 */
	@Override
	public void initEvent()
	{
		super.initEvent();
		SpawnExObject exObject = getFirstObject(SIEGE_COMMANDERS);
		_barrackStatus = new boolean[exObject.getSpawns().size()];
		int lvl = getResidence().getFacilityLevel(Fortress.DOOR_UPGRADE);
		List<DoorObject> doorObjects = getObjects(UPGRADEABLE_DOORS);
		for (DoorObject d : doorObjects)
		{
			d.setUpgradeValue(this, d.getDoor().getMaxHp() * lvl);
			d.getDoor().addListener(_doorDeathListener);
		}
		flagPoleUpdate(false);
		if (getResidence().getOwnerId() > 0)
		{
			spawnEnvoy();
		}
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		_oldOwner = getResidence().getOwner();
		if (_oldOwner != null)
		{
			addObject(DEFENDERS, new SiegeClanObject(DEFENDERS, _oldOwner, 0));
		}
		SiegeClanDAO.getInstance().delete(getResidence());
		flagPoleUpdate(true);
		updateParticles(true, ATTACKERS, DEFENDERS);
		broadcastTo(new SystemMessage2(SystemMsg.THE_FORTRESS_BATTLE_S1_HAS_BEGUN).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);
		super.startEvent();
	}
	
	/**
	 * Method stopEvent.
	 * @param step boolean
	 */
	@Override
	public void stopEvent(boolean step)
	{
		spawnAction(COMBAT_FLAGS, false);
		updateParticles(false, ATTACKERS, DEFENDERS);
		broadcastTo(new SystemMessage2(SystemMsg.THE_FORTRESS_BATTLE_OF_S1_HAS_FINISHED).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);
		Clan ownerClan = getResidence().getOwner();
		if (ownerClan != null)
		{
			if (_oldOwner != ownerClan)
			{
				ownerClan.broadcastToOnlineMembers(PlaySound.SIEGE_VICTORY);
				ownerClan.incReputation(1700, false, toString());
				broadcastTo(new SystemMessage2(SystemMsg.S1_IS_VICTORIOUS_IN_THE_FORTRESS_BATTLE_OF_S2).addString(ownerClan.getName()).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);
				getResidence().getOwnDate().setTimeInMillis(System.currentTimeMillis());
				getResidence().startCycleTask();
				spawnEnvoy();
			}
		}
		else
		{
			getResidence().getOwnDate().setTimeInMillis(0);
		}
		List<SiegeClanObject> attackers = removeObjects(ATTACKERS);
		for (SiegeClanObject siegeClan : attackers)
		{
			siegeClan.deleteFlag();
		}
		removeObjects(DEFENDERS);
		flagPoleUpdate(false);
		super.stopEvent(step);
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onStart boolean
	 */
	@Override
	public synchronized void reCalcNextTime(boolean onStart)
	{
		int attackersSize = getObjects(ATTACKERS).size();
		Calendar startSiegeDate = getResidence().getSiegeDate();
		Calendar lastSiegeDate = getResidence().getLastSiegeDate();
		final long currentTimeMillis = System.currentTimeMillis();
		if (startSiegeDate.getTimeInMillis() > currentTimeMillis)
		{
			if (attackersSize > 0)
			{
				if (onStart)
				{
					registerActions();
				}
				return;
			}
		}
		clearActions();
		if (attackersSize > 0)
		{
			if ((currentTimeMillis - lastSiegeDate.getTimeInMillis()) > SIEGE_WAIT_PERIOD)
			{
				startSiegeDate.setTimeInMillis(currentTimeMillis);
				startSiegeDate.add(Calendar.HOUR_OF_DAY, 1);
			}
			else
			{
				startSiegeDate.setTimeInMillis(lastSiegeDate.getTimeInMillis());
				startSiegeDate.add(Calendar.HOUR_OF_DAY, 5);
			}
			registerActions();
		}
		else
		{
			startSiegeDate.setTimeInMillis(0);
		}
		getResidence().setJdbcState(JdbcEntityState.UPDATED);
		getResidence().update();
	}
	
	/**
	 * Method announce.
	 * @param val int
	 */
	@Override
	public void announce(int val)
	{
		SystemMessage2 msg;
		int min = val / 60;
		if (min > 0)
		{
			msg = new SystemMessage2(SystemMsg.S1_MINUTES_UNTIL_THE_FORTRESS_BATTLE_STARTS).addInteger(min);
		}
		else
		{
			msg = new SystemMessage2(SystemMsg.S1_SECONDS_UNTIL_THE_FORTRESS_BATTLE_STARTS).addInteger(val);
		}
		broadcastTo(msg, ATTACKERS, DEFENDERS);
	}
	
	/**
	 * Method spawnEnvoy.
	 */
	public void spawnEnvoy()
	{
		long endTime = getResidence().getOwnDate().getTimeInMillis() + (60 * 60 * 1000L);
		long diff = endTime - System.currentTimeMillis();
		if ((diff > 0) && (getResidence().getContractState() == Fortress.NOT_DECIDED))
		{
			SpawnExObject exObject = getFirstObject(ENVOY);
			if (exObject.isSpawned())
			{
				info("Last siege: " + TimeUtils.toSimpleFormat(getResidence().getLastSiegeDate()) + ", own date: " + TimeUtils.toSimpleFormat(getResidence().getOwnDate()) + ", siege date: " + TimeUtils.toSimpleFormat(getResidence().getSiegeDate()));
			}
			spawnAction(ENVOY, true);
			_envoyTask = ThreadPoolManager.getInstance().schedule(new EnvoyDespawn(), diff);
		}
		else if (getResidence().getContractState() == Fortress.NOT_DECIDED)
		{
			getResidence().setFortState(Fortress.INDEPENDENT, 0);
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
			getResidence().update();
		}
	}
	
	/**
	 * Method despawnEnvoy.
	 */
	public void despawnEnvoy()
	{
		_envoyTask.cancel(false);
		_envoyTask = null;
		spawnAction(ENVOY, false);
		if (getResidence().getContractState() == Fortress.NOT_DECIDED)
		{
			getResidence().setFortState(Fortress.INDEPENDENT, 0);
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
			getResidence().update();
		}
	}
	
	/**
	 * Method flagPoleUpdate.
	 * @param dis boolean
	 */
	public void flagPoleUpdate(boolean dis)
	{
		StaticObjectObject object = getFirstObject(FLAG_POLE);
		if (object != null)
		{
			object.setMeshIndex(dis ? 0 : (getResidence().getOwner() != null ? 1 : 0));
		}
	}
	
	/**
	 * Method barrackAction.
	 * @param id int
	 * @param val boolean
	 */
	public synchronized void barrackAction(int id, boolean val)
	{
		_barrackStatus[id] = val;
	}
	
	/**
	 * Method checkBarracks.
	 */
	public synchronized void checkBarracks()
	{
		boolean allDead = true;
		for (boolean b : getBarrackStatus())
		{
			if (!b)
			{
				allDead = false;
			}
		}
		if (allDead)
		{
			if (_oldOwner != null)
			{
				SpawnExObject spawn = getFirstObject(FortressSiegeEvent.MERCENARY);
				NpcInstance npc = spawn.getFirstSpawned();
				if ((npc == null) || npc.isDead())
				{
					return;
				}
				Functions.npcShout(npc, NpcString.THE_COMMAND_GATE_HAS_OPENED_CAPTURE_THE_FLAG_QUICKLY_AND_RAISE_IT_HIGH_TO_PROCLAIM_OUR_VICTORY);
				spawnFlags();
			}
			else
			{
				spawnFlags();
			}
		}
	}
	
	/**
	 * Method spawnFlags.
	 */
	public void spawnFlags()
	{
		doorAction(FortressSiegeEvent.COMMANDER_DOORS, true);
		spawnAction(FortressSiegeEvent.SIEGE_COMMANDERS, false);
		spawnAction(FortressSiegeEvent.COMBAT_FLAGS, true);
		if (_oldOwner != null)
		{
			spawnAction(FortressSiegeEvent.MERCENARY, false);
		}
		spawnAction(FortressSiegeEvent.GUARDS_LIVE_WITH_C_CENTER, false);
		broadcastTo(SystemMsg.ALL_BARRACKS_ARE_OCCUPIED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
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
		if (name.equalsIgnoreCase("reinforce_1"))
		{
			return getResidence().getFacilityLevel(Fortress.REINFORCE) == 1;
		}
		if (name.equalsIgnoreCase("reinforce_2"))
		{
			return getResidence().getFacilityLevel(Fortress.REINFORCE) == 2;
		}
		if (name.equalsIgnoreCase("dwarvens"))
		{
			return getResidence().getFacilityLevel(Fortress.DWARVENS) == 1;
		}
		return false;
	}
	
	/**
	 * Method getBarrackStatus.
	 * @return boolean[]
	 */
	public boolean[] getBarrackStatus()
	{
		return _barrackStatus;
	}
	
	/**
	 * Method canRessurect.
	 * @param resurrectPlayer Player
	 * @param target Creature
	 * @param force boolean
	 * @return boolean
	 */
	@Override
	public boolean canRessurect(Player resurrectPlayer, Creature target, boolean force)
	{
		boolean playerInZone = resurrectPlayer.isInZone(Zone.ZoneType.SIEGE);
		boolean targetInZone = target.isInZone(Zone.ZoneType.SIEGE);
		if (!playerInZone && !targetInZone)
		{
			return true;
		}
		if (!targetInZone)
		{
			return false;
		}
		Player targetPlayer = target.getPlayer();
		FortressSiegeEvent siegeEvent = target.getEvent(FortressSiegeEvent.class);
		if (siegeEvent != this)
		{
			if (force)
			{
				targetPlayer.sendPacket(SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE);
			}
			resurrectPlayer.sendPacket(force ? SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE : SystemMsg.INVALID_TARGET);
			return false;
		}
		SiegeClanObject targetSiegeClan = siegeEvent.getSiegeClan(ATTACKERS, targetPlayer.getClan());
		if ((targetSiegeClan == null) || (targetSiegeClan.getFlag() == null))
		{
			if (force)
			{
				targetPlayer.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
			}
			resurrectPlayer.sendPacket(force ? SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE : SystemMsg.INVALID_TARGET);
			return false;
		}
		if (force)
		{
			return true;
		}
		resurrectPlayer.sendPacket(SystemMsg.INVALID_TARGET);
		return false;
	}
	
	/**
	 * Method setRegistrationOver.
	 * @param b boolean
	 */
	@Override
	public void setRegistrationOver(boolean b)
	{
		super.setRegistrationOver(b);
		if (b)
		{
			getResidence().getLastSiegeDate().setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis());
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
			getResidence().update();
			if (getResidence().getOwner() != null)
			{
				getResidence().getOwner().broadcastToOnlineMembers(SystemMsg.ENEMY_BLOOD_PLEDGES_HAVE_INTRUDED_INTO_THE_FORTRESS);
			}
		}
	}
}

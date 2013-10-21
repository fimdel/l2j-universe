package lineage2.gameserver.model.entity.events.impl;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.dao.CastleDamageZoneDAO;
import lineage2.gameserver.dao.CastleDoorUpgradeDAO;
import lineage2.gameserver.dao.CastleHiredGuardDAO;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.HeroDiary;
import lineage2.gameserver.model.entity.events.objects.*;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ResidenceSide;
import lineage2.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.support.MerchantGuard;
import lineage2.gameserver.utils.Location;
import org.napile.primitive.Containers;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.TreeIntSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author VISTALL
 * @date 15:12/14.02.2011
 */
public class CastleSiegeEvent extends SiegeEvent<Castle, SiegeClanObject> {
    private class NextSiegeDateSet extends RunnableImpl {
        public NextSiegeDateSet()
		{
			// TODO Auto-generated constructor stub
		}

		@Override
        public void runImpl() {
            setNextSiegeTime();
        }
    }

    public static final int MAX_SIEGE_CLANS = 20;
    public static final long DAY_IN_MILISECONDS = 86400000L;

    public static final String DEFENDERS_WAITING = "defenders_waiting";
    public static final String DEFENDERS_REFUSED = "defenders_refused";

    public static final String CONTROL_TOWERS = "control_towers";
    public static final String FLAME_TOWERS = "flame_towers";
    public static final String BOUGHT_ZONES = "bought_zones";
    public static final String GUARDS = "guards";
    public static final String HIRED_GUARDS = "hired_guards";

    private IntSet _nextSiegeTimes = Containers.EMPTY_INT_SET;
    private Future<?> _nextSiegeDateSetTask = null;
    private boolean _firstStep = false;

    public CastleSiegeEvent(MultiValueSet<String> set) {
        super(set);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        List<DoorObject> doorObjects = getObjects(DOORS);

        addObjects(BOUGHT_ZONES, CastleDamageZoneDAO.getInstance().load(getResidence()));

        for (DoorObject doorObject : doorObjects) {
            doorObject.setUpgradeValue(this, CastleDoorUpgradeDAO.getInstance().load(doorObject.getUId()));
            doorObject.getDoor().addListener(_doorDeathListener);
        }
    }

	public void takeCastle(Clan newOwnerClan, ResidenceSide side)
	{
		processStep(newOwnerClan);

		getResidence().setResidenceSide(side);
		getResidence().broadcastResidenceState();
	}

    @Override
    public void processStep(Clan newOwnerClan) {
        Clan oldOwnerClan = getResidence().getOwner();

        getResidence().changeOwner(newOwnerClan);

        if (oldOwnerClan != null) {
            SiegeClanObject ownerSiegeClan = getSiegeClan(DEFENDERS, oldOwnerClan);
            removeObject(DEFENDERS, ownerSiegeClan);

            ownerSiegeClan.setType(ATTACKERS);
            addObject(ATTACKERS, ownerSiegeClan);
        } else {
            if (getObjects(ATTACKERS).size() == 1) {
                stopEvent();
                return;
            }

            int allianceObjectId = newOwnerClan.getAllyId();
            if (allianceObjectId > 0) {
                List<SiegeClanObject> attackers = getObjects(ATTACKERS);
                boolean sameAlliance = true;
                for (SiegeClanObject sc : attackers)
                    if (sc != null && sc.getClan().getAllyId() != allianceObjectId)
                        sameAlliance = false;
                if (sameAlliance) {
                    stopEvent();
                    return;
                }
            }
        }

        SiegeClanObject newOwnerSiegeClan = getSiegeClan(ATTACKERS, newOwnerClan);
        newOwnerSiegeClan.deleteFlag();
        newOwnerSiegeClan.setType(DEFENDERS);

        removeObject(ATTACKERS, newOwnerSiegeClan);

        List<SiegeClanObject> defenders = removeObjects(DEFENDERS);
        for (SiegeClanObject siegeClan : defenders)
            siegeClan.setType(ATTACKERS);

        addObject(DEFENDERS, newOwnerSiegeClan);

        addObjects(ATTACKERS, defenders);

        updateParticles(true, ATTACKERS, DEFENDERS);

        teleportPlayers(ATTACKERS);
        teleportPlayers(SPECTATORS);

        if (!_firstStep) {
            _firstStep = true;

            broadcastTo(SystemMsg.THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_HAS_BEEN_DISSOLVED, ATTACKERS, DEFENDERS);

            if (_oldOwner != null) {
                spawnAction(HIRED_GUARDS, false);
                damageZoneAction(false);
                removeObjects(HIRED_GUARDS);
                removeObjects(BOUGHT_ZONES);

                CastleDamageZoneDAO.getInstance().delete(getResidence());
            } else
                spawnAction(GUARDS, false);

            List<DoorObject> doorObjects = getObjects(DOORS);
            for (DoorObject doorObject : doorObjects) {
                doorObject.setWeak(true);
                doorObject.setUpgradeValue(this, 0);

                CastleDoorUpgradeDAO.getInstance().delete(doorObject.getUId());
            }
        }

        spawnAction(DOORS, true);
        despawnSiegeSummons();
    }

    @Override
    public void startEvent() {
        _oldOwner = getResidence().getOwner();
        if (_oldOwner != null) {
            addObject(DEFENDERS, new SiegeClanObject(DEFENDERS, _oldOwner, 0));

            if (getResidence().getSpawnMerchantTickets().size() > 0) {
                for (ItemInstance item : getResidence().getSpawnMerchantTickets()) {
                    MerchantGuard guard = getResidence().getMerchantGuard(item.getItemId());

                    addObject(HIRED_GUARDS, new SpawnSimpleObject(guard.getNpcId(), item.getLoc()));

                    item.deleteMe();
                }

                CastleHiredGuardDAO.getInstance().delete(getResidence());

                spawnAction(HIRED_GUARDS, true);
            }
        }

        List<SiegeClanObject> attackers = getObjects(ATTACKERS);
        if (attackers.isEmpty()) {
            if (_oldOwner == null)
                broadcastToWorld(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST).addResidenceName(getResidence()));
            else
                broadcastToWorld(new SystemMessage2(SystemMsg.S1S_SIEGE_WAS_CANCELED_BECAUSE_THERE_WERE_NO_CLANS_THAT_PARTICIPATED).addResidenceName(getResidence()));

            reCalcNextTime(false);
            return;
        }

        SiegeClanDAO.getInstance().delete(getResidence());

        updateParticles(true, ATTACKERS, DEFENDERS);

        broadcastTo(SystemMsg.THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_IS_IN_EFFECT, ATTACKERS);
        broadcastTo(new SystemMessage2(SystemMsg.YOU_ARE_PARTICIPATING_IN_THE_SIEGE_OF_S1_THIS_SIEGE_IS_SCHEDULED_FOR_2_HOURS).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);

        super.startEvent();

        if (_oldOwner == null)
            initControlTowers();
        else
            damageZoneAction(true);
    }

    @Override
    public void stopEvent(boolean step) {
        List<DoorObject> doorObjects = getObjects(DOORS);
        for (DoorObject doorObject : doorObjects)
            doorObject.setWeak(false);

        damageZoneAction(false);

        updateParticles(false, ATTACKERS, DEFENDERS);

        List<SiegeClanObject> attackers = removeObjects(ATTACKERS);
        for (SiegeClanObject siegeClan : attackers)
            siegeClan.deleteFlag();

        broadcastToWorld(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_IS_FINISHED).addResidenceName(getResidence()));

        removeObjects(DEFENDERS);
        removeObjects(DEFENDERS_WAITING);
        removeObjects(DEFENDERS_REFUSED);

        Clan ownerClan = getResidence().getOwner();
        if (ownerClan != null) {
            if (_oldOwner == ownerClan) {
                getResidence().setRewardCount(getResidence().getRewardCount() + 1);
                ownerClan.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLANS_REPUTATION_SCORE).addInteger(ownerClan.incReputation(1500, false, toString())));
            } else {
                broadcastToWorld(new SystemMessage2(SystemMsg.CLAN_S1_IS_VICTORIOUS_OVER_S2S_CASTLE_SIEGE).addString(ownerClan.getName()).addResidenceName(getResidence()));

                ownerClan.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLANS_REPUTATION_SCORE).addInteger(ownerClan.incReputation(3000, false, toString())));

                if (_oldOwner != null)
                    _oldOwner.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOU_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENTS).addInteger(-_oldOwner.incReputation(-3000, false, toString())));

                for (UnitMember member : ownerClan) {
                    Player player = member.getPlayer();
                    if (player != null) {
                        player.sendPacket(PlaySound.SIEGE_VICTORY);
                        if (player.isOnline() && player.isNoble())
                            Hero.getInstance().addHeroDiary(player.getObjectId(), HeroDiary.ACTION_CASTLE_TAKEN, getResidence().getId());
                    }
                }
            }

            getResidence().getOwnDate().setTimeInMillis(System.currentTimeMillis());
            getResidence().getLastSiegeDate().setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis());
        } else {
            broadcastToWorld(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW).addResidenceName(getResidence()));

            getResidence().getOwnDate().setTimeInMillis(0);
            getResidence().getLastSiegeDate().setTimeInMillis(0);
        }

        despawnSiegeSummons();

        if (_oldOwner != null) {
            spawnAction(HIRED_GUARDS, false);
            removeObjects(HIRED_GUARDS);
        }

        super.stopEvent(step);
    }
    //========================================================================================================================================================================

    @Override
    public void reCalcNextTime(boolean onInit) {
        clearActions();

        final long currentTimeMillis = System.currentTimeMillis();
        final Calendar startSiegeDate = getResidence().getSiegeDate();
        final Calendar ownSiegeDate = getResidence().getOwnDate();
        if (onInit) {
            if (startSiegeDate.getTimeInMillis() > currentTimeMillis)
                registerActions();
            else if (startSiegeDate.getTimeInMillis() == 0) {
                if ((currentTimeMillis - ownSiegeDate.getTimeInMillis()) > DAY_IN_MILISECONDS)
                    setNextSiegeTime();
                else
                    generateNextSiegeDates();
            } else if (startSiegeDate.getTimeInMillis() <= currentTimeMillis)
                setNextSiegeTime();
        } else {
            if (getResidence().getOwner() != null) {
                getResidence().getSiegeDate().setTimeInMillis(0);
                getResidence().setJdbcState(JdbcEntityState.UPDATED);
                getResidence().update();

                generateNextSiegeDates();
            } else
                setNextSiegeTime();
        }
    }

    @Override
    public void loadSiegeClans() {
        super.loadSiegeClans();

        addObjects(DEFENDERS_WAITING, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS_WAITING));
        addObjects(DEFENDERS_REFUSED, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS_REFUSED));
    }

    @Override
    public void setRegistrationOver(boolean b) {
        if (b)
            broadcastToWorld(new SystemMessage2(SystemMsg.THE_DEADLINE_TO_REGISTER_FOR_THE_SIEGE_OF_S1_HAS_PASSED).addResidenceName(getResidence()));

        super.setRegistrationOver(b);
    }

    @Override
    public void announce(int val) {
        SystemMessage2 msg;
        int min = val / 60;
        int hour = min / 60;

        if (hour > 0)
            msg = new SystemMessage2(SystemMsg.S1_HOURS_UNTIL_CASTLE_SIEGE_CONCLUSION).addInteger(hour);
        else if (min > 0)
            msg = new SystemMessage2(SystemMsg.S1_MINUTES_UNTIL_CASTLE_SIEGE_CONCLUSION).addInteger(min);
        else
            msg = new SystemMessage2(SystemMsg.THIS_CASTLE_SIEGE_WILL_END_IN_S1_SECONDS).addInteger(val);

        broadcastTo(msg, ATTACKERS, DEFENDERS);
    }

    //========================================================================================================================================================================
    //                                                   Control Tower Support
    //========================================================================================================================================================================
    private void initControlTowers() {
        List<SpawnExObject> objects = getObjects(GUARDS);
        List<Spawner> spawns = new ArrayList<>();
        for (SpawnExObject o : objects)
            spawns.addAll(o.getSpawns());

        List<SiegeToggleNpcObject> ct = getObjects(CONTROL_TOWERS);

        SiegeToggleNpcInstance closestCt;
        double distance, distanceClosest;

        for (Spawner spawn : spawns) {
            Location spawnLoc = spawn.getCurrentSpawnRange().getRandomLoc(ReflectionManager.DEFAULT.getGeoIndex());

            closestCt = null;
            distanceClosest = 0;

            for (SiegeToggleNpcObject c : ct) {
                SiegeToggleNpcInstance npcTower = c.getToggleNpc();
                distance = npcTower.getDistance(spawnLoc);

                if (closestCt == null || distance < distanceClosest) {
                    closestCt = npcTower;
                    distanceClosest = distance;
                }

                closestCt.register(spawn);
            }
        }
    }

    //========================================================================================================================================================================
    //                                                    Damage Zone Actions
    //========================================================================================================================================================================
    private void damageZoneAction(boolean active) {
        zoneAction(BOUGHT_ZONES, active);
    }

    public void generateNextSiegeDates() {
        if (getResidence().getSiegeDate().getTimeInMillis() != 0)
            return;

        final Calendar calendar = (Calendar) Config.CASTLE_VALIDATION_DATE.clone();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        if (calendar.before(Config.CASTLE_VALIDATION_DATE))
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        validateSiegeDate(calendar, 2);

        _nextSiegeTimes = new TreeIntSet();

        for (int h : Config.CASTLE_SELECT_HOURS) {
            calendar.set(Calendar.HOUR_OF_DAY, h);
            _nextSiegeTimes.add((int) (calendar.getTimeInMillis() / 1000L));
        }

        long diff = (getResidence().getOwnDate().getTimeInMillis() + DAY_IN_MILISECONDS) - System.currentTimeMillis();
        _nextSiegeDateSetTask = ThreadPoolManager.getInstance().schedule(new NextSiegeDateSet(), diff);
    }

    public void setNextSiegeTime(int id) {
        if (!_nextSiegeTimes.contains(id) || _nextSiegeDateSetTask == null)
            return;

        _nextSiegeTimes = Containers.EMPTY_INT_SET;
        _nextSiegeDateSetTask.cancel(false);
        _nextSiegeDateSetTask = null;

        setNextSiegeTime(id * 1000L);
    }

    void setNextSiegeTime() {
        final Calendar calendar = (Calendar) Config.CASTLE_VALIDATION_DATE.clone();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, getResidence().getLastSiegeDate().get(Calendar.HOUR_OF_DAY));
        if (calendar.before(Config.CASTLE_VALIDATION_DATE))
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        validateSiegeDate(calendar, 2);

        setNextSiegeTime(calendar.getTimeInMillis());
    }

    private void setNextSiegeTime(long g) {
        broadcastToWorld(new SystemMessage2(SystemMsg.S1_HAS_ANNOUNCED_THE_NEXT_CASTLE_SIEGE_TIME).addResidenceName(getResidence()));

        clearActions();

        getResidence().getSiegeDate().setTimeInMillis(g);
        getResidence().setJdbcState(JdbcEntityState.UPDATED);
        getResidence().update();

        registerActions();
    }

    @Override
    public boolean isAttackersInAlly() {
        return !_firstStep;
    }

    public int[] getNextSiegeTimes() {
        return _nextSiegeTimes.toArray();
    }

    @Override
    public boolean canRessurect(Player resurrectPlayer, Creature target, boolean force) {
        boolean playerInZone = resurrectPlayer.isInZone(Zone.ZoneType.SIEGE);
        boolean targetInZone = target.isInZone(Zone.ZoneType.SIEGE);
        if (!playerInZone && !targetInZone)
            return true;
        if (!targetInZone)
            return false;

        Player targetPlayer = target.getPlayer();
        CastleSiegeEvent siegeEvent = target.getEvent(CastleSiegeEvent.class);
        if (siegeEvent != this) {
            if (force)
                targetPlayer.sendPacket(SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE);
            resurrectPlayer.sendPacket(force ? SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE : SystemMsg.INVALID_TARGET);
            return false;
        }

        SiegeClanObject targetSiegeClan = siegeEvent.getSiegeClan(ATTACKERS, targetPlayer.getClan());
        if (targetSiegeClan == null)
            targetSiegeClan = siegeEvent.getSiegeClan(DEFENDERS, targetPlayer.getClan());

        if (targetSiegeClan.getType() == ATTACKERS) {
            if (targetSiegeClan.getFlag() == null) {
                if (force)
                    targetPlayer.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
                resurrectPlayer.sendPacket(force ? SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE : SystemMsg.INVALID_TARGET);
                return false;
            }
        } else {
            List<SiegeToggleNpcObject> towers = getObjects(CastleSiegeEvent.CONTROL_TOWERS);

            boolean canRes = true;
            for (SiegeToggleNpcObject t : towers)
                if (!t.isAlive())
                    canRes = false;

            if (!canRes) {
                if (force)
                    targetPlayer.sendPacket(SystemMsg.THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE);
                resurrectPlayer.sendPacket(force ? SystemMsg.THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE : SystemMsg.INVALID_TARGET);
            }
        }

        if (force)
            return true;
		resurrectPlayer.sendPacket(SystemMsg.INVALID_TARGET);
		return false;
    }
}

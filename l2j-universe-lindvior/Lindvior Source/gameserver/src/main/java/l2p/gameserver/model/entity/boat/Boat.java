package l2p.gameserver.model.entity.boat;

import l2p.gameserver.ai.BoatAI;
import l2p.gameserver.ai.CharacterAI;
import l2p.gameserver.model.*;
import l2p.gameserver.model.entity.events.impl.BoatWayEvent;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.CharTemplate;
import l2p.gameserver.templates.item.WeaponTemplate;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.PositionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author VISTALL
 * @date 17:45/26.12.2010
 */
public abstract class Boat extends Creature {
    /**
     *
     */
    private static final long serialVersionUID = -6621304114989895348L;
    private int _moveSpeed; //speed 1
    private int _rotationSpeed; //speed 2

    protected int _fromHome;
    protected int _runState;

    private final BoatWayEvent[] _ways = new BoatWayEvent[2];
    protected final Set<Player> _players = new CopyOnWriteArraySet<Player>();

    public Boat(int objectId, CharTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onSpawn() {
        _fromHome = 1;

        getCurrentWay().reCalcNextTime(false);
    }

    @Override
    public void setXYZ(int x, int y, int z, boolean MoveTask) {
        super.setXYZ(x, y, z, MoveTask);

        updatePeopleInTheBoat(x, y, z);
    }

    public void onEvtArrived() {
        getCurrentWay().moveNext();
    }

    protected void updatePeopleInTheBoat(int x, int y, int z) {
        for (Player player : _players)
            if (player != null) {
                player.setXYZ(x, y, z, true);
                if (isShuttle()) {
                    for (Summon summon : player.getSummonList())
                        summon.setXYZ(x, y, z, true);
                }
            }
    }

    public void addPlayer(Player player, Location boatLoc) {
        synchronized (_players) {
            _players.add(player);

            player.setBoat(this);
            player.setLoc(getLoc(), true);
            player.setInBoatPosition(boatLoc);
            player.broadcastPacket(getOnPacket(player, boatLoc));
            if (isShuttle()) {
                for (Summon summon : player.getSummonList()) {
                    summon.setBoat(this);
                    summon.moveToLocation(getLoc(), 0, false);
                    summon.broadcastPacket(getOnPacket(summon, boatLoc));

                }
            }
        }
    }

    public void moveInBoat(Playable playable, Location ori, Location loc) {
        if (!playable.isPlayer())
            return;

        Player player = playable.getPlayer();
        if (!isShuttle()) {
            if (player.getSummonList().size() > 0) {
                player.sendPacket(SystemMsg.YOU_SHOULD_RELEASE_YOUR_PET_OR_SERVITOR_SO_THAT_IT_DOES_NOT_FALL_OFF_OF_THE_BOAT_AND_DROWN, ActionFail.STATIC);
                return;
            }

            if (player.getTransformation() != 0) {
                player.sendPacket(SystemMsg.YOU_CANNOT_BOARD_A_SHIP_WHILE_YOU_ARE_POLYMORPHED, ActionFail.STATIC);
                return;
            }
        }

        if (player.isMovementDisabled() || player.isSitting()) {
            player.sendActionFailed();
            return;
        }

        if (!player.isInBoat())
            player.setBoat(this);

        loc.h = PositionUtils.getHeadingTo(ori, loc);
        player.setInBoatPosition(loc);
        player.broadcastPacket(inMovePacket(player, ori, loc));
    }

    public void trajetEnded(boolean oust) {
        _runState = 0;
        _fromHome = _fromHome == 1 ? 0 : 1;

        L2GameServerPacket checkLocation = checkLocationPacket();
        if (checkLocation != null)
            broadcastPacket(infoPacket(), checkLocation);

        if (oust) {
            oustPlayers();
            getCurrentWay().reCalcNextTime(false);
        }
    }

    public void teleportShip(int x, int y, int z) {
        if (isMoving)
            stopMove(false);

        for (Player player : _players)
            player.teleToLocation(x, y, z);

        setHeading(calcHeading(x, y));

        setXYZ(x, y, z, true);

        getCurrentWay().moveNext();
    }

    public void oustPlayer(Player player, Location loc, boolean teleport) {
        synchronized (_players) {
            player.setStablePoint(null);
            player.setBoat(null);
            player.setInBoatPosition(null);
            player.broadcastPacket(getOffPacket(player, loc));

            if (teleport)
                player.teleToLocation(loc);

            if (isShuttle()) {
                for (Summon summon : player.getSummonList()) {
                    summon.setBoat(null);
                    summon.setInBoatPosition(null);
                    summon.broadcastPacket(getOffPacket(summon, loc));
                }
            }

            _players.remove(player);
        }
    }

    public void removePlayer(Player player) {
        synchronized (_players) {
            _players.remove(player);
        }
    }

    public void broadcastPacketToPassengers(IStaticPacket packet) {
        for (Player player : _players)
            player.sendPacket(packet);
    }

    //=========================================================================================================
    public abstract L2GameServerPacket infoPacket();

    @Override
    public abstract L2GameServerPacket movePacket();

    public abstract L2GameServerPacket inMovePacket(Player player, Location src, Location desc);

    @Override
    public abstract L2GameServerPacket stopMovePacket();

    public abstract L2GameServerPacket inStopMovePacket(Player player);

    public abstract L2GameServerPacket startPacket();

    public abstract L2GameServerPacket validateLocationPacket(Player player);

    public abstract L2GameServerPacket checkLocationPacket();

    public abstract L2GameServerPacket getOnPacket(Playable playable, Location location);

    public abstract L2GameServerPacket getOffPacket(Playable playable, Location location);

    public abstract void oustPlayers();

    //=========================================================================================================
    @Override
    public CharacterAI getAI() {
        if (_ai == null)
            _ai = new BoatAI(this);

        return _ai;
    }

    @Override
    public void broadcastCharInfo() {
        broadcastPacket(infoPacket());
    }

    @Override
    public void broadcastPacket(L2GameServerPacket... packets) {
        List<Player> players = new ArrayList<Player>();
        players.addAll(_players);
        players.addAll(World.getAroundPlayers(this));

        for (Player player : players)
            if (player != null)
                player.sendPacket(packets);
    }

    @Override
    public void validateLocation(int broadcast) {
    }

    @Override
    public void sendChanges() {
    }

    @Override
    public int getMoveSpeed() {
        return _moveSpeed;
    }

    @Override
    public int getRunSpeed() {
        return _moveSpeed;
    }

    @Override
    public ItemInstance getActiveWeaponInstance() {
        return null;
    }

    @Override
    public WeaponTemplate getActiveWeaponItem() {
        return null;
    }

    @Override
    public ItemInstance getSecondaryWeaponInstance() {
        return null;
    }

    @Override
    public WeaponTemplate getSecondaryWeaponItem() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public boolean isAutoAttackable(Creature attacker) {
        return false;
    }

    //=========================================================================================================
    public int getRunState() {
        return _runState;
    }

    public void setRunState(int runState) {
        _runState = runState;
    }

    public void setMoveSpeed(int moveSpeed) {
        _moveSpeed = moveSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        _rotationSpeed = rotationSpeed;
    }

    public int getRotationSpeed() {
        return _rotationSpeed;
    }

    public BoatWayEvent getCurrentWay() {
        return _ways[_fromHome];
    }

    public void setWay(int id, BoatWayEvent v) {
        _ways[id] = v;
    }

    public Set<Player> getPlayers() {
        return _players;
    }

    public boolean isDocked() {
        return _runState == 0;
    }

    public Location getReturnLoc() {
        return getCurrentWay().getReturnLoc();
    }

    @Override
    public boolean isBoat() {
        return true;
    }

    @Override
    public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper) {
        if (!isMoving)
            return Collections.singletonList(infoPacket());
        else {
            List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>(2);
            list.add(infoPacket());
            list.add(movePacket());
            return list;
        }
    }

    public int getBoatId() {
        return getObjectId();
    }
}

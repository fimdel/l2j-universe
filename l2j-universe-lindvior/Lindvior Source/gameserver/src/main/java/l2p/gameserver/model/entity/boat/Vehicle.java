package l2p.gameserver.model.entity.boat;

import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.templates.CharTemplate;
import l2p.gameserver.utils.Location;

/**
 * @author VISTALL
 * @date 17:46/26.12.2010
 */
public class Vehicle extends Boat {
    /**
     *
     */
    private static final long serialVersionUID = -7563699723543061209L;

    public Vehicle(int objectId, CharTemplate template) {
        super(objectId, template);
    }

    @Override
    public L2GameServerPacket startPacket() {
        return new VehicleStart(this);
    }

    @Override
    public L2GameServerPacket validateLocationPacket(Player player) {
        return new ValidateLocationInVehicle(player);
    }

    @Override
    public L2GameServerPacket checkLocationPacket() {
        return new VehicleCheckLocation(this);
    }

    @Override
    public L2GameServerPacket infoPacket() {
        return new VehicleInfo(this);
    }

    @Override
    public L2GameServerPacket movePacket() {
        return new VehicleDeparture(this);
    }

    @Override
    public L2GameServerPacket inMovePacket(Player player, Location src, Location desc) {
        return new MoveToLocationInVehicle(player, this, src, desc);
    }

    @Override
    public L2GameServerPacket stopMovePacket() {
        return new StopMove(this);
    }

    @Override
    public L2GameServerPacket inStopMovePacket(Player player) {
        return new StopMoveToLocationInVehicle(player);
    }

    @Override
    public L2GameServerPacket getOnPacket(Playable playable, Location location) {
        if (!playable.isPlayer())
            return null;

        return new GetOnVehicle(playable.getPlayer(), this, location);
    }

    @Override
    public L2GameServerPacket getOffPacket(Playable playable, Location location) {
        if (!playable.isPlayer())
            return null;

        return new GetOffVehicle(playable.getPlayer(), this, location);
    }

    @Override
    public void oustPlayers() {
        //
    }

    @Override
    public boolean isVehicle() {
        return true;
    }
}

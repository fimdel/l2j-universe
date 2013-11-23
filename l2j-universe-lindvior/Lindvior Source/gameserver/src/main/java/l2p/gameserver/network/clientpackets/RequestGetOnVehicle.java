package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.BoatHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.boat.Boat;
import l2p.gameserver.utils.Location;

public class RequestGetOnVehicle extends L2GameClientPacket {
    private int _objectId;
    private Location _loc = new Location();

    /**
     * packet type id 0x53
     * format:      cdddd
     */
    @Override
    protected void readImpl() {
        _objectId = readD();
        _loc.x = readD();
        _loc.y = readD();
        _loc.z = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        Boat boat = BoatHolder.getInstance().getBoat(_objectId);
        if (boat == null)
            return;

        player.setStablePoint(boat.getCurrentWay().getReturnLoc());
        boat.addPlayer(player, _loc);
    }
}
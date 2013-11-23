package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.BoatHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.boat.Boat;
import l2p.gameserver.utils.Location;

public class RequestMoveToLocationInVehicle extends L2GameClientPacket {
    private Location _pos = new Location();
    private Location _originPos = new Location();
    private int _boatObjectId;

    @Override
    protected void readImpl() {
        _boatObjectId = readD();
        _pos.x = readD();
        _pos.y = readD();
        _pos.z = readD();
        _originPos.x = readD();
        _originPos.y = readD();
        _originPos.z = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        Boat boat = BoatHolder.getInstance().getBoat(_boatObjectId);
        if (boat == null) {
            player.sendActionFailed();
            return;
        }

        boat.moveInBoat(player, _originPos, _pos);
    }
}
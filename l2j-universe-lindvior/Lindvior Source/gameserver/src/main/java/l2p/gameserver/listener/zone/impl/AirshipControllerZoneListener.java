package l2p.gameserver.listener.zone.impl;

import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.boat.ClanAirShip;
import l2p.gameserver.model.instances.ClanAirShipControllerInstance;

public class AirshipControllerZoneListener implements OnZoneEnterLeaveListener {
    private ClanAirShipControllerInstance _controllerInstance;

    @Override
    public void onZoneEnter(Zone zone, Creature actor) {
        if (_controllerInstance == null && actor instanceof ClanAirShipControllerInstance)
            _controllerInstance = (ClanAirShipControllerInstance) actor;
        else if (actor.isClanAirShip())
            _controllerInstance.setDockedShip((ClanAirShip) actor);
    }

    @Override
    public void onZoneLeave(Zone zone, Creature actor) {
        if (actor.isClanAirShip())
            _controllerInstance.setDockedShip(null);
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExAttackInAirShip extends L2GameServerPacket {
    /*
      * заготовка!!!
      * Format: dddcddddh[ddc]
      * ExAttackInAirShip AttackerID:%d DefenderID:%d Damage:%d bMiss:%d bCritical:%d AirShipID:%d
      */

    @Override
    protected final void writeImpl() {
        writeEx(0x73);
    }
}
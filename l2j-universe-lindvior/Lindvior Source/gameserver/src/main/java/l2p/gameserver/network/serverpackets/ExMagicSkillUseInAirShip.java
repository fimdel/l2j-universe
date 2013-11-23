package l2p.gameserver.network.serverpackets;

public class ExMagicSkillUseInAirShip extends L2GameServerPacket {
    /**
     * заготовка!!!
     * Format: ddddddddddh[h]h[ddd]
     */

    @Override
    protected final void writeImpl() {
        writeEx449(0x73);
    }
}
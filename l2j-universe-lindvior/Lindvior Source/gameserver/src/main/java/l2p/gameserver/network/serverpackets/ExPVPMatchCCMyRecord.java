package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.entity.events.objects.KrateisCubePlayerObject;

/**
 * @author VISTALL
 */
public class ExPVPMatchCCMyRecord extends L2GameServerPacket {
    private int _points;

    public ExPVPMatchCCMyRecord(KrateisCubePlayerObject player) {
        _points = player.getPoints();
    }

    @Override
    public void writeImpl() {
        writeEx449(0x8A);
        writeD(_points);
    }
}
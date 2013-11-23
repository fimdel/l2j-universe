/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import javolution.util.FastMap;
import l2p.gameserver.model.Player;

import java.util.Map;

public class ExInzoneWaitingInfo extends L2GameServerPacket {
    private int _currentInzoneID = -1;
    Map<Integer, Integer> _instanceTimes;

    public ExInzoneWaitingInfo(Player player) {
        _instanceTimes = new FastMap<Integer, Integer>();
    }

    @Override
    protected void writeImpl() {

        writeEx(0x123);
        writeD(_currentInzoneID);
        writeD(_instanceTimes.size());
        for (int instanceId : _instanceTimes.keySet()) {
            writeD(instanceId);
            writeD(_instanceTimes.get(instanceId));
        }
    }
}

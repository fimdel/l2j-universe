/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.utils.Location;

import java.util.HashMap;
import java.util.Map;

public class PartyMemberPosition extends L2GameServerPacket {
    private final Map<Integer, Location> positions = new HashMap<Integer, Location>();

    public PartyMemberPosition add(Player actor) {
        positions.put(actor.getObjectId(), actor.getLoc());
        return this;
    }

    public int size() {
        return positions.size();
    }

    @Override
    protected final void writeImpl() {
        writeC(0xba);
        writeD(positions.size());
        for (Map.Entry<Integer, Location> e : positions.entrySet()) {
            writeD(e.getKey());
            writeD(e.getValue().x);
            writeD(e.getValue().y);
            writeD(e.getValue().z);
        }
    }
}
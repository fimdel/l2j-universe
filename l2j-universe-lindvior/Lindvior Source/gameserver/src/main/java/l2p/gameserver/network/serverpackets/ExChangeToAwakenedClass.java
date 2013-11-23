/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;

/**
 * @author Darvin
 * @data 08.06.2012
 * <p/>
 */
public class ExChangeToAwakenedClass extends L2GameServerPacket {
    private int classId;

    public ExChangeToAwakenedClass(Player player, NpcInstance npc, int classId) {
        this.classId = classId;
        player.setLastNpc(npc);
    }

    @Override
    protected void writeImpl() {
        writeEx(0xFF);
        writeD(classId);
    }
}

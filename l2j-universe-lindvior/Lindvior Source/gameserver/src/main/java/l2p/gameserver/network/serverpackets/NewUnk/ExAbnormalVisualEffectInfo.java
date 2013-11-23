/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.NewUnk;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Created with IntelliJ IDEA.
 * User: Murzik
 * Date: 12.05.13
 * Time: 11:28
 */
public class ExAbnormalVisualEffectInfo extends L2GameServerPacket {

    protected final void writeImpl() {
        writeEx(0x13B);
    }

}
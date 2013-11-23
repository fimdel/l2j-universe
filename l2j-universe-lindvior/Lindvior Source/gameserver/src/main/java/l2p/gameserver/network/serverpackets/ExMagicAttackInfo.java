/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

/**
 * @author Darvin
 */
public class ExMagicAttackInfo extends L2GameServerPacket {
    Player player;

    public ExMagicAttackInfo(Player _player) {
        player = _player;
    }


    @Override
    protected void writeImpl() {
        writeEx(0xFB);
        writeD(player.getObjectId());
        writeD(player.getTargetId());
        writeD(0x01);
    }
}

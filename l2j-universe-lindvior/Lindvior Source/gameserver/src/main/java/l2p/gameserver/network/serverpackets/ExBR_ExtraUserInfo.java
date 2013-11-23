/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

public class ExBR_ExtraUserInfo extends L2GameServerPacket {
    private int _objectId;
    private int[] _effect3;

    public ExBR_ExtraUserInfo(Player cha) {
        _objectId = cha.getObjectId();
        _effect3 = cha.getAbnormalEffects();
    }

    @Override
    protected void writeImpl() {
        writeEx(0xDB);
        writeD(_objectId); //object id of player
        writeD(_effect3.length); // event effect id
        writeC(0);
    }
}

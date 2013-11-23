/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Shape_Shifting;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.templates.item.support.AppearanceStone;

public class ExChooseShapeShiftingItem extends L2GameServerPacket {
    private final AppearanceStone _stone;

    public ExChooseShapeShiftingItem(AppearanceStone stone) {
        this._stone = stone;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x12F);
        writeD(_stone.getType().ordinal());
        writeD(_stone.getClientTargetType().ordinal());
        writeD(_stone.getItemId());
    }
}
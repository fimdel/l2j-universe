/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.PremiumItem;

import java.util.Map;

/**
 * @author VISTALL
 * @date 23:37/23.03.2011
 */
public class ExGoodsInventoryInfo extends L2GameServerPacket {

    private Map<Integer, PremiumItem> _premiumItemMap;

    public ExGoodsInventoryInfo(Map<Integer, PremiumItem> premiumItemMap) {
        _premiumItemMap = premiumItemMap;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x112);
        if (!_premiumItemMap.isEmpty()) {
            writeH(this._premiumItemMap.size());
            for (Map.Entry entry : _premiumItemMap.entrySet()) {
                writeQ((Integer) entry.getKey());
                writeC(0);
                writeD(10003);
                writeS(((PremiumItem) entry.getValue()).getSender());
                writeS(((PremiumItem) entry.getValue()).getSender());//((PremiumItem)entry.getValue()).getSenderMessage());
                writeQ(0);
                writeC(2);
                writeC(0);

                writeS(null);
                writeS(null);

                writeH(1);
                writeD(((PremiumItem) entry.getValue()).getItemId());
                writeD((int) ((PremiumItem) entry.getValue()).getCount());
            }
        } else {
            writeH(0);
        }
    }
}

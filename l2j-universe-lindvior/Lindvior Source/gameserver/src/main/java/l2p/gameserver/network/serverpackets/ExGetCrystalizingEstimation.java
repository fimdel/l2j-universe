/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.items.CrystallizationInfo;
import l2p.gameserver.model.items.ItemInstance;

import java.util.ArrayList;
import java.util.List;

public class ExGetCrystalizingEstimation extends L2GameServerPacket {

    private List<CrystallizationInfo.CrystallizationItem> _crysItems;

    public ExGetCrystalizingEstimation(ItemInstance item) {
        this._crysItems = new ArrayList<CrystallizationInfo.CrystallizationItem>();

        int crystalId = item.getTemplate()._crystalType.getCrystalId();
        int crystalCount = item.getCrystalCountOnCrystallize();
        if ((crystalId > 0) && (crystalCount > 0)) {
            this._crysItems.add(new CrystallizationInfo.CrystallizationItem(crystalId, crystalCount, 100.0D));
        }
        this._crysItems.addAll(item.getTemplate().getCrystallizationInfo().getItems());
    }

    @Override
    protected void writeImpl() {
        writeEx449(0xE0);
        writeD(this._crysItems.size());
        for (CrystallizationInfo.CrystallizationItem item : this._crysItems) {
            writeD(item.getItemId());
            writeQ(item.getCount());
            writeF(item.getChance());
        }
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.etcitems.LifeStoneInfo;
import l2p.gameserver.model.items.etcitems.LifeStoneManager;
import l2p.gameserver.network.serverpackets.ExPutIntensiveResultForVariationMake;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.item.type.ItemGrade;

public class RequestConfirmRefinerItem extends AbstractRefinePacket {
    // format: (ch)dd
    private int _targetItemObjId;
    private int _refinerItemObjId;

    @Override
    protected void readImpl() {
        _targetItemObjId = readD();
        _refinerItemObjId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
        ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);

        if (targetItem == null || refinerItem == null) {
            activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
            return;
        }

        LifeStoneInfo lsi = LifeStoneManager.getStoneInfo(refinerItem.getItemId());

        if (lsi == null)
            return;

        if (!isValid(activeChar, targetItem, refinerItem)) {
            activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
            return;
        }

        final int refinerItemId = refinerItem.getItemId();
        final ItemGrade grade = targetItem.getTemplate().getItemGrade();
        final int gemStoneId = getGemStoneId(grade);
        final int gemStoneCount = getGemStoneCount(lsi.getGrade(), grade);

        SystemMessage sm = new SystemMessage(SystemMessage.REQUIRES_S1_S2).addNumber(gemStoneCount).addItemName(gemStoneId);
        activeChar.sendPacket(new ExPutIntensiveResultForVariationMake(_refinerItemObjId, refinerItemId, gemStoneId, gemStoneCount), sm);
    }
}
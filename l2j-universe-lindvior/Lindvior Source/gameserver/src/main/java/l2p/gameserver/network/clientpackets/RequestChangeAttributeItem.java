package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Element;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.ExChangeAttributeFail;
import l2p.gameserver.network.serverpackets.ExChangeAttributeOk;

public class RequestChangeAttributeItem extends L2GameClientPacket {
    private int _consumeItemId, _itemObjId, _newElementId;

    @Override
    public void readImpl() {
        _consumeItemId = readD();
        _itemObjId = readD();
        _newElementId = readD();
    }

    @Override
    public void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (_consumeItemId != 0 && _itemObjId != 0) {
            ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
            if (item.getOwnerId() == activeChar.getObjectId() && activeChar.getInventory().getItemByItemId(_consumeItemId) != null) {
                activeChar.getInventory().destroyItemByObjectId(_itemObjId, 1);
                item.setAttributeElement(Element.getElementById(_newElementId), item.getAttackElementValue());
                activeChar.sendPacket(new ExChangeAttributeOk());
                return;
            }
        }
        activeChar.sendPacket(new ExChangeAttributeFail());

    }
}
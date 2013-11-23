package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.ExChangeAttributeInfo;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 09.06.12
 * Time: 19:26
 */
public class SendChangeAttributeTargetItem extends L2GameClientPacket {
    public int _crystalItemId;
    public int _itemObjId;

    @Override
    protected void readImpl() {
        _crystalItemId = readD(); //Change Attribute Crystall ID
        _itemObjId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
        if (item == null || !item.isWeapon()) {
            activeChar.sendActionFailed();
            return;
        }
        activeChar.sendPacket(new ExChangeAttributeInfo(_crystalItemId, item));
    }
}

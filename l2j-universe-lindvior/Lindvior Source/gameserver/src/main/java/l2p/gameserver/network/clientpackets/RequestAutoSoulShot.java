package l2p.gameserver.network.clientpackets;

import l2p.gameserver.handler.items.IItemHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.ExAutoSoulShot;
import l2p.gameserver.network.serverpackets.SystemMessage;

/**
 * format:		chdd
 *
 * @param decrypt
 */
public class RequestAutoSoulShot extends L2GameClientPacket {
    private int _itemId;
    private boolean _type; // 1 = on : 0 = off;

    @Override
    protected void readImpl() {
        _itemId = readD();
        _type = readD() == 1;
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null)
            return;

        if (activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE || activeChar.isDead())
            return;

        ItemInstance item = activeChar.getInventory().getItemByItemId(_itemId);

        if (item == null)
            return;

        if (_type) {
            activeChar.addAutoSoulShot(_itemId);
            activeChar.sendPacket(new ExAutoSoulShot(_itemId, true));
            activeChar.sendPacket(new SystemMessage(SystemMessage.THE_USE_OF_S1_WILL_NOW_BE_AUTOMATED).addString(item.getName()));
            IItemHandler handler = item.getTemplate().getHandler();
            handler.useItem(activeChar, item, false);
            return;
        }

        activeChar.removeAutoSoulShot(_itemId);
        activeChar.sendPacket(new ExAutoSoulShot(_itemId, false));
        activeChar.sendPacket(new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addString(item.getName()));
    }
}
package l2p.gameserver.network.clientpackets;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Element;
import l2p.gameserver.model.items.ItemAttributes;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.network.serverpackets.ExBaseAttributeCancelResult;
import l2p.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author SYS
 */
public class RequestExRemoveItemAttribute extends L2GameClientPacket {
    // Format: chd
    private int _objectId;
    private int _attributeId;

    @Override
    protected void readImpl() {
        _objectId = readD();
        _attributeId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.isActionsDisabled() || activeChar.isInStoreMode() || activeChar.isInTrade()) {
            activeChar.sendActionFailed();
            return;
        }

        PcInventory inventory = activeChar.getInventory();
        ItemInstance itemToUnnchant = inventory.getItemByObjectId(_objectId);

        if (itemToUnnchant == null) {
            activeChar.sendActionFailed();
            return;
        }

        ItemAttributes set = itemToUnnchant.getAttributes();
        Element element = Element.getElementById(_attributeId);

        if (element == Element.NONE || set.getValue(element) <= 0) {
            activeChar.sendPacket(new ExBaseAttributeCancelResult(false, itemToUnnchant, element), ActionFail.STATIC);
            return;
        }

        // проверка делается клиентом, если зашло в эту проверку знач чит
        if (!activeChar.reduceAdena(ExShowBaseAttributeCancelWindow.getAttributeRemovePrice(itemToUnnchant), true)) {
            activeChar.sendPacket(new ExBaseAttributeCancelResult(false, itemToUnnchant, element), SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA, ActionFail.STATIC);
            return;
        }

        boolean equipped = false;
        if (equipped = itemToUnnchant.isEquipped())
            activeChar.getInventory().unEquipItem(itemToUnnchant);

        itemToUnnchant.setAttributeElement(element, 0);
        itemToUnnchant.setJdbcState(JdbcEntityState.UPDATED);
        itemToUnnchant.update();

        if (equipped)
            activeChar.getInventory().equipItem(itemToUnnchant);

        activeChar.sendPacket(new InventoryUpdate().addModifiedItem(itemToUnnchant));
        activeChar.sendPacket(new ExBaseAttributeCancelResult(true, itemToUnnchant, element));

        activeChar.updateStats();
    }
}
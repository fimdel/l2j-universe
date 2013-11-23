/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.templates.item.ItemTemplate;

public class RequestUnEquipItem extends L2GameClientPacket {
    private int _slot;

    /**
     * packet type id 0x16
     * format:		cd
     */
    @Override
    protected void readImpl() {
        _slot = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.isActionsDisabled()) {
            activeChar.sendActionFailed();
            return;
        }

        // You cannot do anything else while fishing
        if (activeChar.isFishing()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING);
            return;
        }

        // Нельзя снимать проклятое оружие и флаги
        if ((_slot == ItemTemplate.SLOT_R_HAND || _slot == ItemTemplate.SLOT_L_HAND || _slot == ItemTemplate.SLOT_LR_HAND) && (activeChar.isCursedWeaponEquipped() || activeChar.getActiveWeaponFlagAttachment() != null))
            return;

        if (_slot == ItemTemplate.SLOT_R_HAND) {
            ItemInstance weapon = activeChar.getActiveWeaponInstance();
            if (weapon == null)
                return;
            activeChar.abortAttack(true, true);
            activeChar.abortCast(true, true);
            activeChar.sendDisarmMessage(weapon);
        }

        activeChar.getInventory().unEquipItemInBodySlot(_slot);
    }
}
package l2p.gameserver.network.clientpackets;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.ShortCut;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.etcitems.LifeStoneInfo;
import l2p.gameserver.model.items.etcitems.LifeStoneManager;
import l2p.gameserver.network.serverpackets.ExVariationResult;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.ShortCutRegister;
import l2p.gameserver.tables.AugmentationData;

public final class RequestRefine extends AbstractRefinePacket {
    // format: (ch)dddd
    private int _targetItemObjId, _refinerItemObjId, _gemstoneItemObjId;
    private long _gemstoneCount;

    @Override
    protected void readImpl() {
        _targetItemObjId = readD();
        _refinerItemObjId = readD();
        _gemstoneItemObjId = readD();
        _gemstoneCount = readQ();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null || _gemstoneCount < 1)
            return;

        ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
        ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);
        ItemInstance gemstoneItem = activeChar.getInventory().getItemByObjectId(_gemstoneItemObjId);

        if (targetItem == null || refinerItem == null || gemstoneItem == null) {
            activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
            return;
        }

        LifeStoneInfo lsi = LifeStoneManager.getStoneInfo(refinerItem.getItemId());

        if (lsi == null)
            return;

        if (!isValid(activeChar, targetItem, refinerItem, gemstoneItem)) {
            activeChar.sendPacket(new ExVariationResult(0, 0, 0), Msg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
            return;
        }

        if (TryAugmentItem(activeChar, targetItem, lsi)) {
            int stat12 = 0x0000FFFF & targetItem.getAugmentationId();
            int stat34 = targetItem.getAugmentationId() >> 16;
            activeChar.sendPacket(new ExVariationResult(stat12, stat34, 1), Msg.THE_ITEM_WAS_SUCCESSFULLY_AUGMENTED);
        } else
            activeChar.sendPacket(new ExVariationResult(0, 0, 0), Msg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
    }

    boolean TryAugmentItem(Player player, ItemInstance targetItem, LifeStoneInfo lsi) {
        if (!player.getInventory().destroyItemByObjectId(_gemstoneItemObjId, _gemstoneCount))
            return false;

        if (!player.getInventory().destroyItemByObjectId(_refinerItemObjId, 1L))
            return false;

        int augmentation = AugmentationData.getInstance().generateRandomAugmentation(lsi.getLevel(), lsi.getGrade(), targetItem.getTemplate().getBodyPart());

        boolean equipped = false;
        if (equipped = targetItem.isEquipped())
            player.getInventory().unEquipItem(targetItem);

        targetItem.setAugmentationId(augmentation);
        targetItem.setJdbcState(JdbcEntityState.UPDATED);
        targetItem.update();

        if (equipped)
            player.getInventory().equipItem(targetItem);

        player.sendPacket(new InventoryUpdate().addModifiedItem(targetItem));

        for (ShortCut sc : player.getAllShortCuts())
            if (sc.getId() == targetItem.getObjectId() && sc.getType() == ShortCut.TYPE_ITEM)
                player.sendPacket(new ShortCutRegister(player, sc));
        player.sendChanges();
        return true;
    }


}
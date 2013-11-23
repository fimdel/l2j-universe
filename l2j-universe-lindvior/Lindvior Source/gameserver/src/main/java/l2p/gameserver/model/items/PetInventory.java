package l2p.gameserver.model.items;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.PetInstance;
import l2p.gameserver.model.items.ItemInstance.ItemLocation;
import l2p.gameserver.network.serverpackets.PetInventoryUpdate;
import l2p.gameserver.utils.ItemFunctions;

import java.util.Collection;

public class PetInventory extends Inventory {
    private final PetInstance _actor;

    public PetInventory(PetInstance actor) {
        super(actor.getPlayer().getObjectId());
        _actor = actor;
    }

    @Override
    public PetInstance getActor() {
        return _actor;
    }

    public Player getOwner() {
        return _actor.getPlayer();
    }

    @Override
    protected ItemLocation getBaseLocation() {
        return ItemLocation.PET_INVENTORY;
    }

    @Override
    protected ItemLocation getEquipLocation() {
        return ItemLocation.PET_PAPERDOLL;
    }

    @Override
    protected void onRefreshWeight() {
        getActor().sendPetInfo();
    }

    @Override
    protected void sendAddItem(ItemInstance item) {
        getOwner().sendPacket(new PetInventoryUpdate().addNewItem(item));
    }

    @Override
    protected void sendModifyItem(ItemInstance item) {
        getOwner().sendPacket(new PetInventoryUpdate().addModifiedItem(item));
    }

    @Override
    protected void sendRemoveItem(ItemInstance item) {
        getOwner().sendPacket(new PetInventoryUpdate().addRemovedItem(item));
    }

    @Override
    public void restore() {
        final int ownerId = getOwnerId();

        writeLock();
        try {
            Collection<ItemInstance> items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getBaseLocation());

            for (ItemInstance item : items) {
                _items.add(item);
                onRestoreItem(item);
            }

            items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getEquipLocation());

            for (ItemInstance item : items) {
                _items.add(item);
                onRestoreItem(item);
                if (ItemFunctions.checkIfCanEquip(getActor(), item) == null)
                    setPaperdollItem(item.getEquipSlot(), item);
            }
        } finally {
            writeUnlock();
        }

        refreshWeight();
    }

    @Override
    public void store() {
        writeLock();
        try {
            _itemsDAO.update(_items);
        } finally {
            writeUnlock();
        }
    }

    public void validateItems() {
        for (ItemInstance item : _paperdoll)
            if (item != null && (ItemFunctions.checkIfCanEquip(getActor(), item) != null || !item.getTemplate().testCondition(getActor(), item)))
                unEquipItem(item);
    }
}
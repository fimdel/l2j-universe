/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.items;

import l2p.commons.dao.JdbcEntityState;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.instancemanager.CursedWeaponsManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance.ItemLocation;
import l2p.gameserver.model.items.listeners.*;
import l2p.gameserver.network.serverpackets.ExBR_AgathionEnergyInfo;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.taskmanager.DelayedItemsManager;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.item.type.EtcItemType;
import l2p.gameserver.utils.ItemFunctions;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

public class PcInventory extends Inventory {
    private final Player _owner;

    // locks
    private LockType _lockType = LockType.NONE;
    private int[] _lockItems = ArrayUtils.EMPTY_INT_ARRAY;

    public PcInventory(Player owner) {
        super(owner.getObjectId());
        _owner = owner;

        addListener(ItemSkillsListener.getInstance());
        addListener(ItemAugmentationListener.getInstance());
        addListener(ItemEnchantOptionsListener.getInstance());
        addListener(ArmorSetListener.getInstance());
        addListener(BowListener.getInstance());
        addListener(AccessoryListener.getInstance());
    }

    @Override
    public Player getActor() {
        return _owner;
    }

    @Override
    protected ItemLocation getBaseLocation() {
        return ItemLocation.INVENTORY;
    }

    @Override
    protected ItemLocation getEquipLocation() {
        return ItemLocation.PAPERDOLL;
    }

    public long getAdena() {
        ItemInstance _adena = getItemByItemId(57);
        if (_adena == null)
            return 0;
        return _adena.getCount();
    }

    public long getAdena2() {
        ItemInstance _adena = getItemByItemId(36308);
        if (_adena == null)
            return 0;
        return _adena.getCount();
    }

    /**
     * Добавляет адену игроку.<BR><BR>
     *
     * @param amount - сколько адены дать
     * @return L2ItemInstance - новое количество адены
     */
    public ItemInstance addAdena(long amount) {
        return addItem(ItemTemplate.ITEM_ID_ADENA, amount);
    }

    public boolean reduceAdena(long adena) {
        return destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, adena);
    }

    public int getPaperdollAugmentationId(int slot) {
        ItemInstance item = _paperdoll[slot];
        if (item != null && item.isAugmented())
            return item.getAugmentationId();
        return 0;
    }

    @Override
    public int getPaperdollItemId(int slot) {
        Player player = getActor();

        int itemId = super.getPaperdollItemId(slot);

        if (slot == PAPERDOLL_RHAND && itemId == 0 && player.isClanAirShipDriver())
            itemId = 13556; // Затычка на отображение штурвала - Airship Helm

        return itemId;
    }

    @Override
    protected void onRefreshWeight() {
        // notify char for overload checking
        getActor().refreshOverloaded();
    }

    /**
     * Функция для валидации вещей в инвентаре.
     * Снимает все вещи, которые нельзя носить.
     * Применяется при входе в игру, смене саба, захвате замка, выходе из клана.
     */
    public void validateItems() {
        for (ItemInstance item : _paperdoll)
            if (item != null && (ItemFunctions.checkIfCanEquip(getActor(), item) != null || !item.getTemplate().testCondition(getActor(), item))) {
                unEquipItem(item);
                getActor().sendDisarmMessage(item);
            }
    }

    /**
     * FIXME[VISTALL] для скилов критично их всегда удалять и добавлять, для тригеров нет
     */
    public void validateItemsSkills() {
        for (ItemInstance item : _paperdoll) {
            if (item == null || item.getTemplate().getType2() != ItemTemplate.TYPE2_WEAPON)
                continue;

            boolean needUnequipSkills = getActor().getWeaponsExpertisePenalty() > 0;

            if (item.getTemplate().getAttachedSkills().length > 0) {
                boolean has = getActor().getSkillLevel(item.getTemplate().getAttachedSkills()[0].getId()) > 0;
                if (needUnequipSkills && has)
                    ItemSkillsListener.getInstance().onUnequip(item.getEquipSlot(), item, getActor());
                else if (!needUnequipSkills && !has)
                    ItemSkillsListener.getInstance().onEquip(item.getEquipSlot(), item, getActor());
            } else if (item.getTemplate().getEnchant4Skill() != null) {
                boolean has = getActor().getSkillLevel(item.getTemplate().getEnchant4Skill().getId()) > 0;
                if (needUnequipSkills && has)
                    ItemSkillsListener.getInstance().onUnequip(item.getEquipSlot(), item, getActor());
                else if (!needUnequipSkills && !has)
                    ItemSkillsListener.getInstance().onEquip(item.getEquipSlot(), item, getActor());
            } else if (!item.getTemplate().getTriggerList().isEmpty())
                if (needUnequipSkills)
                    ItemSkillsListener.getInstance().onUnequip(item.getEquipSlot(), item, getActor());
                else
                    ItemSkillsListener.getInstance().onEquip(item.getEquipSlot(), item, getActor());
        }
    }

    /**
     * FIXME Хак для обновления скиллов от эквипа при смене сабкласса
     */
    public boolean isRefresh = false;

    public void refreshEquip() {
        isRefresh = true;
        for (ItemInstance item : getItems())
            if (item.isEquipped()) {
                int slot = item.getEquipSlot();
                _listeners.onUnequip(slot, item);
                _listeners.onEquip(slot, item);
            } else if (item.getItemType() == EtcItemType.RUNE) {
                _listeners.onUnequip(-1, item);
                _listeners.onEquip(-1, item);
            }
        isRefresh = false;
    }

    /**
     * Вызывается из RequestSaveInventoryOrder
     */
    @SuppressWarnings("unused")
    public void sort(int[][] order) {
        boolean needSort = false;
        for (int[] element : order) {
            ItemInstance item = getItemByObjectId(element[0]);
            if (item == null)
                continue;
            if (item.getLocation() != ItemLocation.INVENTORY)
                continue;
            if (item.getLocData() == element[1])
                continue;
            item.setLocData(element[1]);
            item.setJdbcState(JdbcEntityState.UPDATED); // lazy update
            needSort = true;
        }
        //   if (needSort)
        //   CollectionUtils.eqSort(_items, ItemOrderComparator.getInstance());
    }

    private static final int[][] arrows = {
            //
            {17, 32249}, // NG
            {1341, 22067, 32250}, // D
            {1342, 22068, 32251}, // C
            {1343, 22069, 32252}, // B
            {1344, 22070, 32253}, // A
            {1345, 22071, 32254}, // S
            {18550, 32255},//R
    };

    public ItemInstance findArrowForBow(ItemTemplate bow) {
        int[] arrowsId = arrows[bow.getCrystalType().externalOrdinal];
        ItemInstance ret = null;
        for (int id : arrowsId)
            if ((ret = getItemByItemId(id)) != null)
                return ret;
        return null;
    }

    private static final int[][] bolts = {
            //
            {9632, 32256}, // NG
            {9633, 22144, 32257}, // D
            {9634, 22145, 32258}, // C
            {9635, 22146, 32259}, // B
            {9636, 22147, 32260}, // A
            {9637, 22148, 32261}, // S
            {19443, 32262}, // R
    };

    public ItemInstance findArrowForCrossbow(ItemTemplate xbow) {
        int[] boltsId = bolts[xbow.getCrystalType().externalOrdinal];
        ItemInstance ret = null;
        for (int id : boltsId)
            if ((ret = getItemByItemId(id)) != null)
                return ret;
        return null;
    }

    public ItemInstance findEquippedLure() {
        ItemInstance res = null;
        int last_lure = 0;
        Player owner = getActor();
        String LastLure = owner.getVar("LastLure");
        if (LastLure != null && !LastLure.isEmpty())
            last_lure = Integer.valueOf(LastLure);
        for (ItemInstance temp : getItems())
            if (temp.getItemType() == EtcItemType.BAIT)
                if (temp.getLocation() == ItemLocation.PAPERDOLL && temp.getEquipSlot() == PAPERDOLL_LHAND)
                    return temp;
                else if (last_lure > 0 && res == null && temp.getObjectId() == last_lure)
                    res = temp;
        return res;
    }

    public void lockItems(LockType lock, int[] items) {
        if (_lockType != LockType.NONE)
            return;

        _lockType = lock;
        _lockItems = items;

        getActor().sendItemList(false);
    }

    public void unlock() {
        if (_lockType == LockType.NONE)
            return;

        _lockType = LockType.NONE;
        _lockItems = ArrayUtils.EMPTY_INT_ARRAY;

        getActor().sendItemList(false);
    }

    public boolean isLockedItem(ItemInstance item) {
        switch (_lockType) {
            case INCLUDE:
                return ArrayUtils.contains(_lockItems, item.getItemId());
            case EXCLUDE:
                return !ArrayUtils.contains(_lockItems, item.getItemId());
            default:
                return false;
        }
    }

    public LockType getLockType() {
        return _lockType;
    }

    public int[] getLockItems() {
        return _lockItems;
    }

    @Override
    protected void onRestoreItem(ItemInstance item) {
        super.onRestoreItem(item);

        if (item.getItemType() == EtcItemType.RUNE)
            _listeners.onEquip(-1, item);

        if (item.isTemporalItem())
            item.startTimer(new LifeTimeTask(item));

        if (item.isCursed())
            CursedWeaponsManager.getInstance().checkPlayer(getActor(), item);
    }

    @Override
    protected void onAddItem(ItemInstance item) {
        super.onAddItem(item);

        if (item.getItemType() == EtcItemType.RUNE)
            _listeners.onEquip(-1, item);

        if (item.isTemporalItem())
            item.startTimer(new LifeTimeTask(item));

        if (item.isCursed())
            CursedWeaponsManager.getInstance().checkPlayer(getActor(), item);
    }

    @Override
    protected void onRemoveItem(ItemInstance item) {
        super.onRemoveItem(item);

        getActor().removeItemFromShortCut(item.getObjectId());

        if (item.getItemType() == EtcItemType.RUNE)
            _listeners.onUnequip(-1, item);

        if (item.isTemporalItem())
            item.stopTimer();
    }

    @Override
    protected void onEquip(int slot, ItemInstance item) {
        super.onEquip(slot, item);

        if (item.isShadowItem())
            item.startTimer(new ShadowLifeTimeTask(item));
    }

    @Override
    protected void onUnequip(int slot, ItemInstance item) {
        super.onUnequip(slot, item);

        if (item.isShadowItem())
            item.stopTimer();
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
            //  CollectionUtils.eqSort(_items, ItemOrderComparator.getInstance());

            items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getEquipLocation());

            for (ItemInstance item : items) {
                _items.add(item);
                onRestoreItem(item);
                if (item.getEquipSlot() >= PAPERDOLL_MAX) {
                    // Неверный слот - возвращаем предмет в инвентарь.
                    item.setLocation(getBaseLocation());
                    item.setLocData(0); // Немного некрасиво, но инвентарь еще весь не загружен и свободный слот не найти
                    item.setEquipped(false);
                    continue;
                }
                setPaperdollItem(item.getEquipSlot(), item);
            }
        } finally {
            writeUnlock();
        }

        DelayedItemsManager.getInstance().loadDelayed(getActor(), false);

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

    @Override
    protected void sendAddItem(ItemInstance item) {
        Player actor = getActor();

        actor.sendPacket(new InventoryUpdate().addNewItem(item));
        if (item.getTemplate().getAgathionEnergy() > 0)
            actor.sendPacket(new ExBR_AgathionEnergyInfo(1, item));
    }

    @Override
    protected void sendModifyItem(ItemInstance item) {
        Player actor = getActor();

        actor.sendPacket(new InventoryUpdate().addModifiedItem(item));
        if (item.getTemplate().getAgathionEnergy() > 0)
            actor.sendPacket(new ExBR_AgathionEnergyInfo(1, item));
    }

    @Override
    protected void sendRemoveItem(ItemInstance item) {
        getActor().sendPacket(new InventoryUpdate().addRemovedItem(item));
    }

    public void startTimers() {

    }

    public void stopAllTimers() {
        for (ItemInstance item : getItems())
            if (item.isShadowItem() || item.isTemporalItem())
                item.stopTimer();
    }

    protected class ShadowLifeTimeTask extends RunnableImpl {
        private ItemInstance item;

        ShadowLifeTimeTask(ItemInstance item) {
            this.item = item;
        }

        @Override
        public void runImpl() throws Exception {
            Player player = getActor();

            if (!item.isEquipped())
                return;

            int mana;
            synchronized (item) {
                item.setLifeTime(item.getLifeTime() - 1);
                mana = item.getShadowLifeTime();
                if (mana <= 0)
                    destroyItem(item);
            }

            SystemMessage sm = null;
            if (mana == 10)
                sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_10);
            else if (mana == 5)
                sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_5);
            else if (mana == 1)
                sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_1_IT_WILL_DISAPPEAR_SOON);
            else if (mana <= 0)
                sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_0_AND_THE_ITEM_HAS_DISAPPEARED);
            else
                player.sendPacket(new InventoryUpdate().addModifiedItem(item));

            if (sm != null) {
                sm.addItemName(item.getItemId());
                player.sendPacket(sm);
            }
        }
    }

    protected class LifeTimeTask extends RunnableImpl {
        private ItemInstance item;

        LifeTimeTask(ItemInstance item) {
            this.item = item;
        }

        @Override
        public void runImpl() throws Exception {
            Player player = getActor();

            int left;
            synchronized (item) {
                left = item.getTemporalLifeTime();
                if (left <= 0)
                    destroyItem(item);
            }

            if (left <= 0)
                player.sendPacket(new SystemMessage(SystemMessage.THE_LIMITED_TIME_ITEM_HAS_BEEN_DELETED).addItemName(item.getItemId()));
        }
    }

    public int getPaperdollVisualId(int slot) {
        if (_paperdoll[slot] == null) {
            return 0;
        }
        return _paperdoll[slot].getVisualId();
    }
}

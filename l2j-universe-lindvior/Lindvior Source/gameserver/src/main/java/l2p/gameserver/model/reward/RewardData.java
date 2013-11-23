package l2p.gameserver.model.reward;

import l2p.commons.math.SafeMath;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.templates.item.ItemTemplate;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class RewardData implements Cloneable {
    private final ItemTemplate _item;
    private boolean _notRate = false; // Рейты к вещи не применяются

    private long _mindrop;
    private long _maxdrop;
    private double _chance;
    private double _chanceInGroup;

    public RewardData(int itemId) {
        _item = ItemHolder.getInstance().getTemplate(itemId);
        if (_item.isArrow() // стрелы не рейтуются
                || (Config.NO_RATE_EQUIPMENT && _item.isEquipment() // отключаемая рейтовка эквипа
        ) || (Config.NO_RATE_KEY_MATERIAL && _item.isKeyMatherial() // отключаемая рейтовка ключевых материалов
        ) || (Config.NO_RATE_RECIPES && _item.isRecipe() // отключаемая рейтовка рецептов
        ) || ArrayUtils.contains(Config.NO_RATE_ITEMS, itemId)) {
            _notRate = true;
        }
    }

    public RewardData(int itemId, long min, long max, double chance) {
        this(itemId);
        _mindrop = min;
        _maxdrop = max;
        _chance = chance;
    }

    public boolean notRate() {
        return _notRate;
    }

    public void setNotRate(boolean notRate) {
        _notRate = notRate;
    }

    public int getItemId() {
        return _item.getItemId();
    }

    public ItemTemplate getItem() {
        return _item;
    }

    public long getMinDrop() {
        return _mindrop;
    }

    public long getMaxDrop() {
        return _maxdrop;
    }

    public double getChance() {
        return _chance;
    }

    public void setMinDrop(long mindrop) {
        _mindrop = mindrop;
    }

    public void setMaxDrop(long maxdrop) {
        _maxdrop = maxdrop;
    }

    public void setChance(double chance) {
        _chance = chance;
    }

    public void setChanceInGroup(double chance) {
        _chanceInGroup = chance;
    }

    public double getChanceInGroup() {
        return _chanceInGroup;
    }

    @Override
    public String toString() {
        return "ItemID: " + getItem() + " Min: " + getMinDrop() + " Max: " + getMaxDrop() + " Chance: " + (getChance() / 10000.0) + "%";
    }

    @Override
    public RewardData clone() {
        return new RewardData(getItemId(), getMinDrop(), getMaxDrop(), getChance());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RewardData) {
            RewardData drop = (RewardData) o;
            return drop.getItemId() == getItemId();
        }
        return false;
    }

    /**
     * Подсчет шанса выпадения этой конкретной вещи Используется в эвентах и некоторых специальных механизмах
     *
     * @param player игрок (его бонус влияет на шанс)
     * @param mod    (просто множитель шанса)
     * @return информация о выпавшей вещи
     */
    public List<RewardItem> roll(Player player, double mod) {
        double rate;
        if (_item.isAdena()) {
            rate = (Config.RATE_DROP_ADENA + player.getVitality().getDropMod()) * player.getRateAdena();
        } else {
            rate = (Config.RATE_DROP_ITEMS + player.getVitality().getDropMod()) * player.getRateItems();
        }

        return roll(rate * mod);
    }

    /**
     * Подсчет шанса выпадения этой конкретной вещи Используется в эвентах и некоторых специальных механизмах
     *
     * @param rate множитель количества
     * @return информация о выпавшей вещи
     */
    public List<RewardItem> roll(double rate) {
        double mult = Math.ceil(rate);

        List<RewardItem> ret = new ArrayList<RewardItem>(1);
        RewardItem t = null;
        long count;
        for (int n = 0; n < mult; n++) {
            if (Rnd.get(RewardList.MAX_CHANCE) <= (_chance * Math.min(rate - n, 1.0))) {
                if (getMinDrop() >= getMaxDrop()) {
                    count = getMinDrop();
                } else {
                    count = Rnd.get(getMinDrop(), getMaxDrop());
                }

                if (t == null) {
                    ret.add(t = new RewardItem(_item.getItemId()));
                    t.count = count;
                } else {
                    t.count = SafeMath.addAndLimit(t.count, count);
                }
            }
        }

        return ret;
    }
}
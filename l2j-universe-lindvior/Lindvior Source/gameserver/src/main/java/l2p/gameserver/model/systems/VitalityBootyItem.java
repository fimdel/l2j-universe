/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.systems;

/**
 * @author ALF
 * @date 26.10.2012
 */
public class VitalityBootyItem {
    private int itemId;
    private int level;

    public VitalityBootyItem(int _itemId, int _level) {
        itemId = _itemId;
        level = _level;
    }

    public int getItemId() {
        return itemId;
    }

    public int getLevel() {
        return level;
    }
}

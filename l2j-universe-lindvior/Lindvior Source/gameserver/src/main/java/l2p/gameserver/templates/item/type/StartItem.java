/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.type;

public final class StartItem {
    public final int _id;
    public final long _count;
    private final boolean _equiped;
    public final boolean _warehouse;

    public StartItem(int id, long count, boolean equiped, boolean warehouse) {
        _id = id;
        _count = count;
        _equiped = equiped;
        _warehouse = warehouse;
    }

    public int getItemId() {
        return _id;
    }

    public long getCount() {
        return _count;
    }

    public boolean isEquiped() {
        return _equiped;
    }
}

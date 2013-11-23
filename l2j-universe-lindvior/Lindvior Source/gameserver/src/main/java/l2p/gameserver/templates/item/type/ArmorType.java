/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.type;

public enum ArmorType
        implements ItemType {
    NONE,
    LIGHT,
    HEAVY,
    MAGIC,
    PET,
    SIGIL,
    SHIELD;

    public static final ArmorType[] VALUES = values();

    public String toString() {
        return name().toLowerCase();
    }

    public long mask() {
        return 1 << ordinal() + 1 + WeaponType.VALUES.length;
    }
}

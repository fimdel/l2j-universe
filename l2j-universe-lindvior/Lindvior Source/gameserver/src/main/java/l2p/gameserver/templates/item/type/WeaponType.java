/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.type;

import l2p.gameserver.stats.Stats;

public enum WeaponType
        implements ItemType {
    NONE("None", null, ExItemType.OTHER_WEAPON),
    SWORD("Sword", Stats.SWORD_WPN_VULNERABILITY, ExItemType.SWORD, ExItemType.MAGIC_SWORD),
    BLUNT("Blunt", Stats.BLUNT_WPN_VULNERABILITY, ExItemType.BLUNT_WEAPON, ExItemType.MAGIC_BLUNT_WEAPON),
    DAGGER("Dagger", Stats.DAGGER_WPN_VULNERABILITY, ExItemType.DAGGER),
    BOW("Bow", Stats.BOW_WPN_VULNERABILITY, ExItemType.BOW),
    POLE("Pole", Stats.POLE_WPN_VULNERABILITY, ExItemType.POLE),
    ETC("Etc", null, ExItemType.OTHER_WEAPON),
    FIST("Fist", Stats.FIST_WPN_VULNERABILITY, ExItemType.OTHER_WEAPON),
    DUAL("Dual Sword", Stats.DUAL_WPN_VULNERABILITY, ExItemType.DUAL_SWORD),
    DUALFIST("Dual Fist", Stats.FIST_WPN_VULNERABILITY, ExItemType.HAND_TO_HAND),
    BIGSWORD("Big Sword", Stats.SWORD_WPN_VULNERABILITY, ExItemType.BIG_SWORD),
    PET("Pet", Stats.FIST_WPN_VULNERABILITY, ExItemType.PET_EQUIPMENT),
    ROD("Rod", null, ExItemType.OTHER_WEAPON),
    BIGBLUNT("Big Blunt", Stats.BLUNT_WPN_VULNERABILITY, ExItemType.BIG_BLUNT_WEAPON, ExItemType.BIG_MAGIC_BLUNT_WEAPON),
    CROSSBOW("Crossbow", Stats.CROSSBOW_WPN_VULNERABILITY, ExItemType.CROSSBOW),
    RAPIER("Rapier", Stats.DAGGER_WPN_VULNERABILITY, ExItemType.RAPIER),
    ANCIENTSWORD("Ancient Sword", Stats.SWORD_WPN_VULNERABILITY, ExItemType.ANCIENT_SWORD),
    DUALDAGGER("Dual Dagger", Stats.DAGGER_WPN_VULNERABILITY, ExItemType.DUAL_DAGGER),
    DUALBLUNT("Dual Blunt", Stats.BLUNT_WPN_VULNERABILITY, ExItemType.DUAL_BLUNT_WEAPON);

    public static final WeaponType[] VALUES = values();
    private final String _name;
    private final Stats _defence;
    private ExItemType _physical;
    private ExItemType _magic;

    private WeaponType(String name, Stats defence, ExItemType physical) {
        this(name, defence, physical, null);
    }


    private WeaponType(String name, Stats defence, ExItemType physical, ExItemType magic) {
        _name = name;
        _defence = defence;
        _magic = magic;
        _physical = physical;
    }

    public String toString() {
        return _name;
    }

    public long mask() {
        return 1 << ordinal() + 1;
    }

    public Stats getDefence() {
        return _defence;
    }

    public ExItemType getEx(boolean magic) {
        return !magic || _magic == null ? _physical : _magic;
    }
}

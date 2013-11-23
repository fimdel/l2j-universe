/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.type;

public enum EtcItemType
        implements ItemType {
    NONE(ExItemType.OTHER_ITEMS),

    POTION(ExItemType.POTION),
    EWS(ExItemType.SCROLL_ENCHANT_WEAPON),
    EAS(ExItemType.SCROLL_ENCHANT_ARMOR),
    SCROLL(ExItemType.SCROLL_OTHER),
    SOULSHOT(ExItemType.SOULSHOT),
    SPIRITSHOT(ExItemType.SPIRITSHOT),

    PET_COLLAR(ExItemType.PET_SUPPLIES),

    CRYSTAL(ExItemType.CRYSTAL),
    RECIPE(ExItemType.RECIPE),
    MATERIAL(ExItemType.CRAFTING_MAIN_INGRIDIENTS),
    LIFE_STONE(ExItemType.LIFE_STONE),
    SOUL_CRYSTAL(ExItemType.SOUL_CRYSTAL),
    WEAPON_ENCHANT_STONE(ExItemType.WEAPON_ENCHANT_STONE),
    ARMOR_ENCHANT_STONE(ExItemType.ARMOR_ENCHANT_STONE),
    ATTRIBUTE_STONE(ExItemType.ATTRIBUTE_STONE),
    SPELLBOOK(ExItemType.SPELLBOOK),
    GEMSTONE(ExItemType.GEMSTONE),
    MAGIC_RUNE_CLIP(ExItemType.MAGIC_RUNE_CLIP),
    MAGIC_ORNAMENT(ExItemType.MAGIC_ORNAMENT),
    DYES(ExItemType.DYES),

    ARROW(ExItemType.OTHER_ITEMS),
    QUEST(ExItemType.OTHER_ITEMS),
    MONEY(ExItemType.OTHER_ITEMS),
    OTHER(ExItemType.OTHER_ITEMS),
    SEED(ExItemType.OTHER_ITEMS),
    SEED2(ExItemType.OTHER_ITEMS),
    BAIT(ExItemType.OTHER_ITEMS),
    BOLT(ExItemType.OTHER_ITEMS),
    RUNE(ExItemType.OTHER_ITEMS),
    HERB(ExItemType.OTHER_ITEMS),
    MERCENARY_TICKET(ExItemType.OTHER_ITEMS),
    UNLIMITED_ARROW(ExItemType.OTHER_ITEMS),
    CAPSULE(ExItemType.OTHER_ITEMS);

    private final ExItemType _exType;

    private EtcItemType(ExItemType exType) {
        _exType = exType;
    }

    public String toString() {
        return name().toLowerCase();
    }

    public long mask() {
        return 1 << ordinal() + 1 + WeaponType.VALUES.length + ArmorType.VALUES.length;
    }

    public ExItemType getEx() {
        return _exType;
    }
}

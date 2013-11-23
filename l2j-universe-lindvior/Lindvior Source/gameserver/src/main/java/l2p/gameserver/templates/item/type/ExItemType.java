/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Darvin
 */
public enum ExItemType {
    EMPTY0(0),
    // Weapons
    SWORD(0), // 1 (WeaponType.SWORD)
    MAGIC_SWORD(0), // 2 (WeaponType.SWORD) is_magic_weapon = true
    DAGGER(0), // 3 (WeaponType.DAGGER)
    RAPIER(0), // 4 (WeaponType.RAPIER)
    BIG_SWORD(0), // 5 (WeaponType.BIGSWORD)
    ANCIENT_SWORD(0), // 6 (WeaponType.ANCIENTSWORD)
    DUAL_SWORD(0), // 7 (WeponType.DUAL)
    DUAL_DAGGER(0), // 8 (WeaponType.DUALDAGGER)
    BLUNT_WEAPON(0), // 9 (WeaponType.BLUNT)
    MAGIC_BLUNT_WEAPON(0), // 10 (WeaponType.BLUNT) is_magic_weapon = true
    BIG_BLUNT_WEAPON(0), // 11 (WeaponType.BIGBLUNT)
    BIG_MAGIC_BLUNT_WEAPON(0), // 12 (WeaponType.BIGBLUNT) is_magic_weapon = true
    DUAL_BLUNT_WEAPON(0), // 13 (WeaponType.DUALBLUNT)
    BOW(0), // 14 (WeaponType.BOW)
    CROSSBOW(0), // 15 (WeaponType.CROSSBOW)
    HAND_TO_HAND(0), // 16 (WeaponType.DUALFIST)
    POLE(0), // 17 (WeaponType.POLE)
    OTHER_WEAPON(0), // 18 (WeaponType.ETC) (WeponType.ROW)

    // Armors
    HELMET(1), // 19 getBodyPart == ItemTemplate.SLOT_HEAD
    UPPER_PIECE(1), // 20 getBodyPart == ItemTemplate.SLOT_CHEST
    LOWER_PIECE(1), // 21 getBodyPart == ItemTemplate.SLOT_LEGS
    FULL_BODY(1), // 22 getBodyPart == ItemTemplate.SLOT_FULL_ARMOR
    GLOVES(1), // 23 getBodyPart == ItemTemplate.SLOT_GLOVES
    FEET(1), // 24 getBodyPart == ItemTemplate.SLOT_FEET
    SHIELD(1), // 25 (WeaponType.NONE) getBodyPart == ItemTemplate.SLOT_L_HAND
    SIGIL(1), // 26 (ArmorType.SIGIL)
    UNDERWEAR(1), // 27 getBodyPart == ItemTamplate.SLOT_UNDERWEAR
    CLOAK(1), // 28 getBodyPart == ItemTemplate.SLOT_BACK

    //Accessories
    RING(2), // 29 getBodyPart == ItemTemplate.SLOT_R_FINGER | ItemTemplate.SLOT_L_FINGER
    EARRING(2), // 30 getBodyPart == ItemTemplate.SLOT_R_EAR | ItemTemplate.SLOT_L_EAR
    NECKLACE(2), // 31 getBodyPart == ItemTemplate.SLOT_NECK
    BELT(2), // 32 getBodyPart == ItemTemplate.SLOT_BELT
    BRACELET(2), // 33 getBodyPart == ItemTemplate.SLOT_R_BRACELET || ItemTemplate.SLOT_L_BRACELET
    HAIR_ACCESSORY(2), // 34 getBodyPart == ItemTemplate.SLOT_HAIRALL

    // Supplies
    POTION(3), // 35 EtcItemType.POTION
    SCROLL_ENCHANT_WEAPON(3), // 36 EtcItemType.SCROLL_ENCHANT_WEAPON
    SCROLL_ENCHANT_ARMOR(3), // 37 EtcItemType.SCROLL_ENCHANT_ARMOR
    SCROLL_OTHER(3), // 38 EtcItemType.SCROLL && SoulCrystalHolder.getInstance.getCrystal(int itemId) == null
    SOULSHOT(3), // 39 ItemTemplate.SOULSHOT_IDS.contains(itemId)
    SPIRITSHOT(3), // 40 ItemTemplate.SPIRITSHOT_IDS.contains(itemId) || ItemTemplate.BLESSED_SPIRITSHOT_IDS.contains(itemId)
    EMPTY41(3), // Не используется в клиенте

    //Pet Goods
    PET_EQUIPMENT(4), // 42 WeaponType.PET || ArmorType.PET
    PET_SUPPLIES(4), // 43 EtcItemType.PET_COLLAR

    // Etc
    CRYSTAL(5), // 44 itemId == ItemTemplate.CRYSTAL_NONE || itemId == CRYSTAL_D = 1458 ...
    RECIPE(5), // 45 EtcItemType.RECIPE
    CRAFTING_MAIN_INGRIDIENTS(5), // 46 EtcItemType.MATERIAL в эту категорию должны попадать только основные материалы, необходимые для создания вещи.
    LIFE_STONE(5), // 47 ItemFunctions.isLifeStone(itemId) || ItemFunctions.isAccessoryLifeStone(itemId)
    SOUL_CRYSTAL(5), // 48 SoulCrystalHolder.getInstance.getCrystal(int itemId) != null
    ATTRIBUTE_STONE(5), // 49 ItemTemplate.ATTRIBUTE_STONES.contains(itemId)
    WEAPON_ENCHANT_STONE(5), // 50 ItemFunctions.CATALYST_WEAPON.contains(itemId)
    ARMOR_ENCHANT_STONE(5), // 51 ItemFunctions.CATALYST_ARMOR.contains(itemId)
    SPELLBOOK(5), // 52 EtcItemType.SPELLBOOK
    GEMSTONE(5), // 53 ItemTemplate.GEMSTONES.contains(itemId)
    POUCH(5), // 54
    PIN(5), // 55
    MAGIC_RUNE_CLIP(5), // 56
    MAGIC_ORNAMENT(5), // 57
    DYES(5), // 58 HennaHolder.getInstance.getAllHennaDyeIds.contains(itemId)
    OTHER_ITEMS(5); // 59 Сюда попадает всё остальное, что не подошло ни под одну категорию

    public static final int WEAPON_MASK = 0;
    public static final int ARMOR_MASK = 1;
    public static final int ACCESSORIES_MASK = 2;
    public static final int SUPPLIES_MASK = 3;
    public static final int PET_GOODS_MASK = 4;
    public static final int ETC_MASK = 5;

    private int mask;

    ExItemType(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    public static ExItemType[] getTypesForMask(int mask) {
        if (mask < WEAPON_MASK && mask > ETC_MASK) {
            return new ExItemType[0];
        }
        List<ExItemType> list = new ArrayList<ExItemType>();
        for (ExItemType exType : values()) {
            if (exType.getMask() == mask) {
                list.add(exType);
            }
        }
        return list.toArray(new ExItemType[list.size()]);
    }

    public static ExItemType valueOf(int ordinal) {
        for (ExItemType type : values()) {
            if (type.ordinal() == ordinal) {
                return type;
            }
        }
        return null;
    }
}
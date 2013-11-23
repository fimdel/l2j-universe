/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item;

import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.FuncTemplate;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.support.SoulshotGrade;
import l2p.gameserver.templates.item.type.WeaponType;

public final class WeaponTemplate extends ItemTemplate {
    private final int _soulShotCount;
    private final int _spiritShotCount;
    private final int _kamaelConvert;
    private final int _rndDam;
    private final int _atkReuse;
    private final int _mpConsume;
    private int _critical;

    public WeaponTemplate(StatsSet set) {
        super(set);
        type = set.getEnum("type", WeaponType.class);
        _soulShotCount = set.getInteger("soulshots", 0);
        _spiritShotCount = set.getInteger("spiritshots", 0);
        _kamaelConvert = set.getInteger("kamael_convert", 0);

        _rndDam = set.getInteger("rnd_dam", 0);
        _atkReuse = set.getInteger("atk_reuse", type == WeaponType.BOW ? 1500 : type == WeaponType.CROSSBOW ? 820 : 0);
        _mpConsume = set.getInteger("mp_consume", 0);

        if (getItemType() == WeaponType.NONE) {
            _type1 = TYPE1_SHIELD_ARMOR;
            _type2 = TYPE2_SHIELD_ARMOR;
        } else {
            _type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE;
            _type2 = TYPE2_WEAPON;
        }

        if (getItemType() == WeaponType.PET) {
            _type1 = ItemTemplate.TYPE1_WEAPON_RING_EARRING_NECKLACE;

            if (_bodyPart == ItemTemplate.SLOT_WOLF)
                _type2 = ItemTemplate.TYPE2_PET_WOLF;
            else if (_bodyPart == ItemTemplate.SLOT_GWOLF)
                _type2 = ItemTemplate.TYPE2_PET_GWOLF;
            else if (_bodyPart == ItemTemplate.SLOT_HATCHLING)
                _type2 = ItemTemplate.TYPE2_PET_HATCHLING;
            else
                _type2 = ItemTemplate.TYPE2_PET_STRIDER;

            _bodyPart = ItemTemplate.SLOT_R_HAND;
        }
    }

    /**
     * Returns the type of Weapon
     *
     * @return L2WeaponType
     */
    @Override
    public WeaponType getItemType() {
        return (WeaponType) type;
    }

    /**
     * Returns the ID of the Etc item after applying the mask.
     *
     * @return int : ID of the Weapon
     */
    @Override
    public long getItemMask() {
        return getItemType().mask();
    }

    /**
     * Returns the quantity of SoulShot used.
     *
     * @return int
     */
    public int getSoulShotCount() {
        return _soulShotCount;
    }

    /**
     * Returns the quatity of SpiritShot used.
     *
     * @return int
     */
    public int getSpiritShotCount() {
        return _spiritShotCount;
    }

    public int getCritical() {
        return _critical;
    }

    /**
     * Returns the random damage inflicted by the weapon
     *
     * @return int
     */
    public int getRandomDamage() {
        return _rndDam;
    }

    /**
     * Return the Attack Reuse Delay of the L2Weapon.<BR><BR>
     *
     * @return int
     */
    public int getAttackReuseDelay() {
        return _atkReuse;
    }

    /**
     * Returns the MP consumption with the weapon
     *
     * @return int
     */
    public int getMpConsume() {
        return _mpConsume;
    }

    /**
     * Возвращает разницу между длиной этого оружия и стандартной, то есть x-40
     */
    public int getAttackRange() {
        switch (getItemType()) {
            case BOW:
                return 460;
            case CROSSBOW:
                return 360;
            case POLE:
                return 40;
            default:
                return 0;
        }
    }

    @Override
    public void attachFunc(FuncTemplate f) {
        //TODO для параметров set с дп,может считать стат с L2ItemInstance? (VISTALL)
        if (f._stat == Stats.PHYSICAL_CRIT_BASE && f._order == 0x08)
            _critical = (int) Math.round(f._value / 10);
        super.attachFunc(f);
    }

    public int getKamaelConvert() {
        return _kamaelConvert;
    }

    public final SoulshotGrade getSoulshotGradeForItem() {
        switch (getItemGrade()) {
            case NONE:
                return SoulshotGrade.SS_NG;
            case D:
                return SoulshotGrade.SS_D;
            case C:
                return SoulshotGrade.SS_C;
            case B:
                return SoulshotGrade.SS_B;
            case A:
                return SoulshotGrade.SS_A;
            case S:
            case S80:
            case S84:
                return SoulshotGrade.SS_S;
            case R:
            case R95:
            case R99:
                return SoulshotGrade.SS_R;
        }
        return SoulshotGrade.SS_NG;
    }
}
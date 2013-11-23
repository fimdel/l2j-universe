/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates;


import l2p.gameserver.templates.item.type.WeaponType;
import l2p.gameserver.templates.player.StatAttributes;

public class CharTemplate {
    private static final int[] EMPTY_ATTRIBUTES = new int[6];
    private final StatAttributes _baseAttr;
    private int _baseAtkRange;
    private double _baseHpMax;
    private double _baseCpMax;
    private double _baseMpMax;
    private double _baseHpReg;
    private double _baseMpReg;
    private double _baseCpReg;
    private double _basePAtk;
    private double _baseMAtk;
    private double _basePDef;
    private double _baseMDef;
    private double _basePAtkSpd;
    private double _baseMAtkSpd;
    private double _baseShldDef;
    private double _baseShldRate;
    private double _baseCritRate;
    public double _baseMageCritRate;
    private int _baseRunSpd;
    private double _baseWalkSpd;
    private double _baseWaterRunSpd;
    private double _baseWaterWalkSpd;
    private final int[] _baseAttributeAttack;
    private final int[] _baseAttributeDefence;
    private double _collisionRadius;
    private double _collisionHeight;
    private final WeaponType _baseAttackType;

    public CharTemplate(StatsSet set) {
        _baseAttr = new StatAttributes(
                set.getInteger("baseINT", 0),
                set.getInteger("baseSTR", 0),
                set.getInteger("baseCON", 0),
                set.getInteger("baseMEN", 0),
                set.getInteger("baseDEX", 0),
                set.getInteger("baseWIT", 0));
        _baseHpMax = set.getDouble("baseHpMax", 0);
        _baseCpMax = set.getDouble("baseCpMax", 0);
        _baseMpMax = set.getDouble("baseMpMax", 0);
        _baseHpReg = set.getDouble("baseHpReg", 3.e-3f);
        _baseCpReg = set.getDouble("baseCpReg", 3.e-3f);
        _baseMpReg = set.getDouble("baseMpReg", 3.e-3f);
        _basePAtk = set.getDouble("basePAtk");
        _baseMAtk = set.getDouble("baseMAtk");
        _basePDef = set.getDouble("basePDef", 100);
        _baseMDef = set.getDouble("baseMDef", 100);
        _basePAtkSpd = set.getDouble("basePAtkSpd");
        _baseMAtkSpd = set.getDouble("baseMAtkSpd");
        _baseShldDef = set.getDouble("baseShldDef", 0);
        _baseAtkRange = set.getInteger("baseAtkRange");
        _baseShldRate = set.getDouble("baseShldRate", 0);
        _baseCritRate = set.getDouble("baseCritRate");
        _baseMageCritRate = set.getDouble("baseMageCritRate");
        _baseRunSpd = set.getInteger("baseRunSpd");
        _baseWalkSpd = set.getDouble("baseWalkSpd");
        _baseWaterRunSpd = set.getDouble("baseWaterRunSpd", 50);
        _baseWaterWalkSpd = set.getDouble("baseWaterWalkSpd", 50);
        _baseAttributeAttack = set.getIntegerArray("baseAttributeAttack", EMPTY_ATTRIBUTES);
        _baseAttributeDefence = set.getIntegerArray("baseAttributeDefence", EMPTY_ATTRIBUTES);

        // Geometry
        _collisionRadius = set.getDouble("collision_radius", 5);
        _collisionHeight = set.getDouble("collision_height", 5);
        _baseAttackType = WeaponType.valueOf(set.getString("baseAttackType", "FIST").toUpperCase());
    }

    public StatAttributes getBaseAttr() {
        return _baseAttr;
    }

    public double getBaseHpMax() {
        return _baseHpMax;
    }

    public double getBaseCpMax() {
        return _baseCpMax;
    }

    public double getBaseMpMax() {
        return _baseMpMax;
    }

    public double getBaseHpReg() {
        return _baseHpReg;
    }

    public double getBaseMpReg() {
        return _baseMpReg;
    }

    public double getBaseCpReg() {
        return _baseCpReg;
    }

    public double getBasePAtk() {
        return _basePAtk;
    }

    public double getBaseMAtk() {
        return _baseMAtk;
    }

    public double getBasePDef() {
        return _basePDef;
    }

    public double getBaseMDef() {
        return _baseMDef;
    }

    public double getBasePAtkSpd() {
        return _basePAtkSpd;
    }

    public double getBaseMAtkSpd() {
        return _baseMAtkSpd;
    }

    public double getBaseShldDef() {
        return _baseShldDef;
    }

    public int getBaseAtkRange() {
        return _baseAtkRange;
    }

    public double getBaseShldRate() {
        return _baseShldRate;
    }

    public double getBaseCritRate() {
        return _baseCritRate;
    }

    public double getBaseMageCritRate() {
        return _baseMageCritRate;
    }

    public int getBaseRunSpd() {
        return _baseRunSpd;
    }

    public int getBaseWalkSpd() {
        return (int) _baseWalkSpd;
    }

    public int getBaseWaterRunSpd() {
        return (int) _baseWaterRunSpd;
    }

    public int getBaseWaterWalkSpd() {
        return (int) _baseWaterWalkSpd;
    }

    public int[] getBaseAttributeAttack() {
        return _baseAttributeAttack;
    }

    public int[] getBaseAttributeDefence() {
        return _baseAttributeDefence;
    }

    public double getCollisionRadius() {
        return _collisionRadius;
    }

    public double getCollisionHeight() {
        return _collisionHeight;
    }

    public WeaponType getBaseAttackType() {
        return _baseAttackType;
    }

    public int getNpcId() {
        return 0;
    }

    public static StatsSet getEmptyStatsSet() {
        StatsSet npcDat = new StatsSet();
        npcDat.set("baseSTR", 0);
        npcDat.set("baseCON", 0);
        npcDat.set("baseDEX", 0);
        npcDat.set("baseINT", 0);
        npcDat.set("baseWIT", 0);
        npcDat.set("baseMEN", 0);
        npcDat.set("baseHpMax", 0);
        npcDat.set("baseCpMax", 0);
        npcDat.set("baseMpMax", 0);
        npcDat.set("baseHpReg", 3.e-3f);
        npcDat.set("baseCpReg", 0);
        npcDat.set("baseMpReg", 3.e-3f);
        npcDat.set("basePAtk", 0);
        npcDat.set("baseMAtk", 0);
        npcDat.set("basePDef", 100);
        npcDat.set("baseMDef", 100);
        npcDat.set("basePAtkSpd", 0);
        npcDat.set("baseMAtkSpd", 0);
        npcDat.set("baseShldDef", 0);
        npcDat.set("baseAtkRange", 0);
        npcDat.set("baseShldRate", 0);
        npcDat.set("baseCritRate", 0);
        npcDat.set("baseMageCritRate", 0);
        npcDat.set("baseRunSpd", 0);
        npcDat.set("baseWalkSpd", 0);
        return npcDat;
    }
}

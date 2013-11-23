/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.player;

import gnu.trove.TIntObjectHashMap;
import l2p.commons.util.Rnd;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.base.Sex;
import l2p.gameserver.templates.CharTemplate;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.type.StartItem;
import l2p.gameserver.utils.Location;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Darvin
 * Date: 06.03.12
 * Time: 2:59
 */
public final class PlayerTemplate extends CharTemplate {
    private final Race _race;
    private final Sex _sex;
    private final StatAttributes _minAttr;
    private final StatAttributes _maxAttr;
    private final StatAttributes _baseAttr;
    private final BaseArmorDefence _armDef;
    private final BaseJewelDefence _jewlDef;
    private int _baseRandDam = 0;
    private double _baseSafeFallHeight;
    private double _baseBreathBonus = 0;
    private double _baseFlyRunSpd = 0;
    private double _baseFlyWalkSpd = 0;
    private double _baseRideRunSpd = 0;
    private double _baseRideWalkSpd = 0;
    private final List<Location> _startLocs;
    private final List<StartItem> _startItems;
    private final TIntObjectHashMap<LvlUpData> _lvlUpData;

    public PlayerTemplate(StatsSet set, Race race, Sex sex, StatAttributes minAttr, StatAttributes maxAttr, StatAttributes baseAttr, BaseArmorDefence armDef, BaseJewelDefence jewlDef, List<Location> startLocs, List<StartItem> startItems, TIntObjectHashMap<LvlUpData> lvlUpData) {
        super(set);

        _race = race;
        _sex = sex;
        _minAttr = minAttr;
        _maxAttr = maxAttr;
        _baseAttr = baseAttr;
        _armDef = armDef;
        _jewlDef = jewlDef;
        _startLocs = startLocs;
        _startItems = startItems;
        _lvlUpData = lvlUpData;

        _baseMageCritRate = set.getDouble("baseMageCritRate", 1.0);
        _baseRandDam = set.getInteger("baseRandDam");
        _baseSafeFallHeight = set.getDouble("baseSafeFallHeight");
        _baseBreathBonus = set.getDouble("baseBreathBonus");
        _baseFlyRunSpd = set.getDouble("baseFlyRunSpd");
        _baseFlyWalkSpd = set.getDouble("baseFlyWalkSpd");
        _baseRideRunSpd = set.getDouble("baseRideRunSpd");
        _baseRideWalkSpd = set.getDouble("baseRideWalkSpd");
    }

    public Race getRace() {
        return _race;
    }

    public Sex getSex() {
        return _sex;
    }

    public StatAttributes getMinAttr() {
        return _minAttr;
    }

    public StatAttributes getMaxAttr() {
        return _maxAttr;
    }

    public StatAttributes getBaseAttr() {
        return _baseAttr;
    }

    public BaseArmorDefence getArmDef() {
        return _armDef;
    }

    public BaseJewelDefence getJewlDef() {
        return _jewlDef;
    }

    public int getBaseRandDam() {
        return _baseRandDam;
    }

    public double getBaseBreathBonus() {
        return _baseBreathBonus;
    }

    public double getBaseSafeFallHeight() {
        return _baseSafeFallHeight;
    }

    public double getBaseHpReg(int lvl) {
        return getLvlUpData(lvl).getHP();
    }

    public double getBaseMpReg(int lvl) {
        return getLvlUpData(lvl).getMP();
    }

    public double getBaseCpReg(int lvl) {
        return getLvlUpData(lvl).getCP();
    }

    public double getBaseFlyRunSpd() {
        return _baseFlyRunSpd;
    }

    public double getBaseFlyWalkSpd() {
        return _baseFlyWalkSpd;
    }

    public double getBaseRideRunSpd() {
        return _baseRideRunSpd;
    }

    public double getBaseRideWalkSpd() {
        return _baseRideWalkSpd;
    }

    public LvlUpData getLvlUpData(int lvl) {
        return _lvlUpData.get(lvl);
    }

    public StartItem[] getStartItems() {
        return _startItems.toArray(new StartItem[_startItems.size()]);
    }

    public Location getStartLocation() {
        return _startLocs.get(Rnd.get(_startLocs.size()));
    }
}

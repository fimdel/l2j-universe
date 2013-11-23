/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model;

import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.base.Experience;
import l2p.gameserver.model.base.SubClassType;

/**
 * Character Sub-Class<?> Definition
 * <BR>
 * Used to store key information about a character's sub-class.
 *
 * @author Tempy
 */
public class SubClass {
    public static final int CERTIFICATION_65 = 1 << 0;
    public static final int CERTIFICATION_70 = 1 << 1;
    public static final int CERTIFICATION_75 = 1 << 2;
    public static final int CERTIFICATION_80 = 1 << 3;

    public static final int CERTIFICATION_85 = 1 << 10;
    public static final int CERTIFICATION_90 = 1 << 11;
    public static final int CERTIFICATION_95 = 1 << 12;
    public static final int CERTIFICATION_99 = 1 << 13;

    private int _classId = 0;
    private int _defaultClassId = 0;
    private int _index = 1;

    private boolean _active = false;
    private SubClassType _type = SubClassType.BASE_CLASS;
    private DeathPenalty _dp;

    private int _level = 1;
    private long _exp = 0;
    private int _sp = 0;

    private int _maxLvl = Experience.getMaxLevel();
    private long _maxExp = Experience.LEVEL[_maxLvl + 1] - 1;

    private int _certification;

    private double _hp = 1;
    private double _mp = 1;
    private double _cp = 1;
    private int awakingId;

    public SubClass() {
    }

    public int getClassId() {
        return _classId;
    }

    public int getDefaultClassId() {
        return _defaultClassId;
    }

    public long getExp() {
        return _exp;
    }

    public long getMaxExp() {
        return _maxExp;
    }

    public void addExp(long val) {
        setExp(_exp + val);
    }

    public int getSp() {
        return _sp;
    }

    public void addSp(long val) {
        setSp(_sp + val);
    }

    public int getLevel() {
        return _level;
    }

    public void setClassId(int id) {
        if (_classId == id)
            return;

        _classId = id;
        refreshInfo();
    }

    public void setDefaultClassId(int id) {
        _defaultClassId = id;
    }

    public void setExp(long val) {
        _exp = Math.min(Math.max(Experience.LEVEL[_level], val), _maxExp);
        _level = Experience.getLevel(_exp);
    }

    public void setSp(long spValue) {
        _sp = (int) Math.min(Math.max(0, spValue), Integer.MAX_VALUE);
    }

    public void setHp(double hpValue) {
        _hp = hpValue;
    }

    public double getHp() {
        return _hp;
    }

    public void setMp(final double mpValue) {
        _mp = mpValue;
    }

    public double getMp() {
        return _mp;
    }

    public void setCp(final double cpValue) {
        _cp = cpValue;
    }

    public double getCp() {
        return _cp;
    }

    public void setActive(final boolean active) {
        _active = active;
    }

    public boolean isActive() {
        return _active;
    }

    public void setType(final SubClassType type) {
        if (_type == type)
            return;

        _type = type;
        refreshInfo();
    }

    public SubClassType getType() {
        return _type;
    }

    public boolean isBase() {
        return _type == SubClassType.BASE_CLASS;
    }

    public boolean isDouble() {
        return _type == SubClassType.DOUBLE_SUBCLASS;
    }

    public boolean isSub() {
        return _type == SubClassType.SUBCLASS;
    }

    public DeathPenalty getDeathPenalty(Player player) {
        if (_dp == null)
            _dp = new DeathPenalty(player, 0);
        return _dp;
    }

    public void setDeathPenalty(DeathPenalty dp) {
        _dp = dp;
    }

    public int getCertification() {
        return _certification;
    }

    public void setCertification(int certification) {
        _certification = certification;
    }

    public void addCertification(int c) {
        _certification |= c;
    }

    public boolean isCertificationGet(int v) {
        return (_certification & v) == v;
    }

    @Override
    public String toString() {
        return ClassId.VALUES[_classId].toString() + " " + _level;
    }

    public int getMaxLevel() {
        return _maxLvl;
    }

    public void setIndex(int i) {
        _index = i;
    }

    public int getIndex() {
        return _index;
    }

    private void refreshInfo() {
        if (_type == SubClassType.SUBCLASS) {
            _maxLvl = Experience.getMaxSubLevel();
            _maxExp = Experience.LEVEL[_maxLvl + 1] - 1;
            _level = Math.min(Math.max(40, _level), _maxLvl);
        } else if (_type == SubClassType.DOUBLE_SUBCLASS) {
            _maxLvl = Experience.getMaxDualLevel();
            _maxExp = Experience.LEVEL[_maxLvl + 1] - 1;
            _level = Math.min(Math.max(80, _level), _maxLvl);
        } else {
            if (ClassId.VALUES[_classId].isOfLevel(ClassLevel.AWAKED))
                _maxLvl = Experience.getMaxDualLevel();
            else
                _maxLvl = Experience.getMaxLevel();
            _maxExp = Experience.LEVEL[_maxLvl + 1] - 1;
            _level = Math.min(Math.max(1, _level), _maxLvl);
        }
        _exp = Math.min(Math.max(Experience.LEVEL[_level], _exp), _maxExp);
    }

    public int getAwakingId() {
        return awakingId;
    }

    public void setAwakingId(int _Id) {
        awakingId = _Id;
    }
}

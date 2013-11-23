package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.EffectList;
import l2p.gameserver.model.Skill;
import l2p.gameserver.skills.AbnormalEffect;
import l2p.gameserver.skills.EffectType;
import l2p.gameserver.stats.Env;
import l2p.gameserver.stats.StatTemplate;
import l2p.gameserver.stats.conditions.Condition;
import l2p.gameserver.templates.StatsSet;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class EffectTemplate extends StatTemplate {
    private static final Logger _log = LoggerFactory.getLogger(EffectTemplate.class);

    public static final EffectTemplate[] EMPTY_ARRAY = new EffectTemplate[0];

    public static final String NO_STACK = "none".intern();
    public static final String HP_RECOVER_CAST = "HpRecoverCast".intern();

    public Condition _attachCond;
    public final double _value;
    public final int _count;
    public final long _period; // in milliseconds
    //  public AbnormalEffect _abnormalEffect;

    public final EffectType _effectType;

    public List<String> _stackTypes = new ArrayList<String>();
    public final int _stackOrder;
    public final int _displayId;
    public final int _displayLevel;

    public final boolean _applyOnCaster;
    public final boolean _applyOnSummon;
    public final boolean _cancelOnAction;
    public final boolean _isReflectable;
    private final Boolean _isSaveable;
    private final Boolean _isCancelable;
    private final Boolean _isOffensive;
    private final int[] _abnormalEffects;
    private final String _abnormalType;
    private final int _abnormalLvl;
    private final AbnormalEffect[] _abnormalEffect;

    private final StatsSet _paramSet;
    private final int _chance;

    public EffectTemplate(StatsSet set, Skill skill) {
        _value = set.getDouble("value");
        _count = set.getInteger("count", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("count", 1);
        _period = Math.min(Integer.MAX_VALUE, 1000 * (set.getInteger("time", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("time", 1)));
        if (!set.isSet("abnormal")) {
            _abnormalEffects = ArrayUtils.EMPTY_INT_ARRAY;
        } else {
            final String[] ae = set.getString("abnormal").split(";");

            _abnormalEffects = new int[ae.length];

            for (int i = 0; i < ae.length; i++) {
                _abnormalEffects[i] = AbnormalEffect.valueOf(ae[i].toUpperCase()).ordinal();
            }
        }

        String args[] = set.getString("stackType", NO_STACK).split(";", -1);
        for (String arg : args)
            _stackTypes.add(arg);

        _stackOrder = set.getInteger("stackOrder", _stackTypes.contains(NO_STACK) ? 1 : 0);
        _applyOnCaster = set.getBool("applyOnCaster", false);
        _applyOnSummon = set.getBool("applyOnSummon", true);
        _cancelOnAction = set.getBool("cancelOnAction", false);
        _isReflectable = set.getBool("isReflectable", true);
        _isSaveable = set.isSet("isSaveable") ? set.getBool("isSaveable") : null;
        _isCancelable = set.isSet("isCancelable") ? set.getBool("isCancelable") : null;
        _isOffensive = set.isSet("isOffensive") ? set.getBool("isOffensive") : null;
        _displayId = set.getInteger("displayId", 0);
        _displayLevel = set.getInteger("displayLevel", 0);
        _effectType = set.getEnum("name", EffectType.class);
        _chance = set.getInteger("chance", Integer.MAX_VALUE);
        _abnormalType = skill.getAbnormalType();
        _abnormalLvl = skill.getAbnormalLvl();
        _abnormalEffect = skill.getAbnormalEffects();
        _paramSet = set;
    }

    public Effect getEffect(Env env) {
        if (_attachCond != null && !_attachCond.test(env))
            return null;
        try {
            return _effectType.makeEffect(env, this);
        } catch (Exception e) {
            _log.error("", e);
        }

        return null;
    }

    public int[] getAbnormalEffects() {
        return _abnormalEffects;
    }

    public String getAbnormalType() {
        return this._abnormalType;
    }

    public int getAbnormalLvl() {
        return this._abnormalLvl;
    }

    public AbnormalEffect[] getAbnormalEffect() {
        return _abnormalEffect;
    }

    public void attachCond(Condition c) {
        _attachCond = c;
    }

    public int getCount() {
        return _count;
    }

    public long getPeriod() {
        return _period;
    }

    public EffectType getEffectType() {
        return _effectType;
    }

    public Effect getSameByStackType(List<Effect> list) {
        for (Effect ef : list)
            if (ef != null && EffectList.checkStackType(ef.getTemplate(), this))
                return ef;
        return null;
    }

    public Effect getSameByStackType(EffectList list) {
        return getSameByStackType(list.getAllEffects());
    }

    public Effect getSameByStackType(Creature actor) {
        return getSameByStackType(actor.getEffectList().getAllEffects());
    }

    public StatsSet getParam() {
        return _paramSet;
    }

    public int chance(int val) {
        return _chance == Integer.MAX_VALUE ? val : _chance;
    }

    public boolean isSaveable(boolean def) {
        return _isSaveable != null ? _isSaveable : def;
    }

    public boolean isCancelable(boolean def) {
        return _isCancelable != null ? _isCancelable : def;
    }

    public boolean isOffensive(boolean def) {
        return _isOffensive != null ? _isOffensive : def;
    }
}

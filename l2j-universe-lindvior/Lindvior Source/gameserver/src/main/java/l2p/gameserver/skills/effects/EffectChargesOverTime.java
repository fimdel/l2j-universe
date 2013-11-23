package l2p.gameserver.skills.effects;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class EffectChargesOverTime extends Effect {
    private int _maxCharges;

    public EffectChargesOverTime(Env env, EffectTemplate template) {
        super(env, template);
        _maxCharges = getTemplate().getParam().getInteger("maxCharges", 10);
    }

    @Override
    public boolean onActionTime() {
        if (_effected.isDead())
            return false;

        double damage = calc();

        if (damage > _effected.getCurrentHp() - 1) {
            if (!getSkill().isOffensive())
                _effected.sendPacket(Msg.NOT_ENOUGH_HP);
            return false;
        }

        if (_effected.getIncreasedForce() >= _maxCharges) {
            _effected.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
            return false;
        }

        _effected.setIncreasedForce(_effected.getIncreasedForce() + 1);
        _effected.reduceCurrentHp(damage, 0, _effector, getSkill(), false, false, true, false, false, true, false);

        return true;
    }

}

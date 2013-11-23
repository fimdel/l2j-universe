package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Env;
import l2p.gameserver.stats.Stats;

public class EffectHealPercent extends Effect {
    private final boolean _ignoreHpEff;

    public EffectHealPercent(Env env, EffectTemplate template) {
        super(env, template);
        _ignoreHpEff = template.getParam().getBool("ignoreHpEff", true);
    }

    @Override
    public boolean checkCondition() {
        if (_effected.isHealBlocked())
            return false;
        return super.checkCondition();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (_effected.isHealBlocked())
            return;

        double hp = calc() * _effected.getMaxHp() / 100.;
        double newHp = hp * (!_ignoreHpEff ? _effected.calcStat(Stats.HEAL_EFFECTIVNESS, 100., _effector, getSkill()) : 100.) / 100.;
        double addToHp = Math.max(0, Math.min(newHp, _effected.calcStat(Stats.HP_LIMIT, null, null) * _effected.getMaxHp() / 100. - _effected.getCurrentHp()));

        _effected.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToHp)));

        if (addToHp > 0)
            _effected.setCurrentHp(addToHp + _effected.getCurrentHp(), false);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Env;
import l2p.gameserver.stats.Stats;

public class EffectHealCPPercent extends Effect {
    private final boolean _ignoreCpEff;

    public EffectHealCPPercent(Env env, EffectTemplate template) {
        super(env, template);
        _ignoreCpEff = template.getParam().getBool("ignoreCpEff", true);
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

        double cp = calc() * _effected.getMaxCp() / 100.;
        double newCp = cp * (!_ignoreCpEff ? _effected.calcStat(Stats.CPHEAL_EFFECTIVNESS, 100., _effector, getSkill()) : 100.) / 100.;
        double addToCp = Math.max(0, Math.min(newCp, _effected.calcStat(Stats.CP_LIMIT, null, null) * _effected.getMaxCp() / 100. - _effected.getCurrentCp()));

        _effected.sendPacket(new SystemMessage(SystemMessage.S1_WILL_RESTORE_S2S_CP).addNumber((long) addToCp));

        if (addToCp > 0)
            _effected.setCurrentCp(addToCp + _effected.getCurrentCp());
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
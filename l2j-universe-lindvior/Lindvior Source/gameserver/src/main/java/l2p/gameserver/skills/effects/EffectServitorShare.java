package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.Func;
import l2p.gameserver.stats.funcs.FuncTemplate;

/**
 * @author G1ta0
 */
public class EffectServitorShare extends Effect {
    public class FuncShare extends Func {
        public FuncShare(Stats stat, int order, Object owner, double value) {
            super(stat, order, owner, value);
        }

        @Override
        public void calc(Env env) {
            env.value += env.character.getPlayer().calcStat(stat, stat.getInit()) * value;
        }
    }

    public EffectServitorShare(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public Func[] getStatFuncs() {
        FuncTemplate[] funcTemplates = getTemplate().getAttachedFuncs();
        Func[] funcs = new Func[funcTemplates.length];
        for (int i = 0; i < funcs.length; i++)
            funcs[i] = new FuncShare(funcTemplates[i]._stat, funcTemplates[i]._order, this, funcTemplates[i]._value);
        return funcs;
    }

    @Override
    protected boolean onActionTime() {
        return false;
    }
}
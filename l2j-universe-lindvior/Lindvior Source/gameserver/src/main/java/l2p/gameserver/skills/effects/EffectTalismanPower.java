/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.skills.AbnormalEffect;
import l2p.gameserver.stats.Env;

public class EffectTalismanPower extends Effect {
    public EffectTalismanPower(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();

        switch (getSkill().getLevel()) {
            case 1:
            case 2:
                getEffected().startAbnormalEffect(AbnormalEffect.TALISMANPOWER1);
                break;
            case 3:
                getEffected().startAbnormalEffect(AbnormalEffect.TALISMANPOWER2);
                break;
            case 4:
                getEffected().startAbnormalEffect(AbnormalEffect.TALISMANPOWER3);
                break;
            case 5:
                getEffected().startAbnormalEffect(AbnormalEffect.TALISMANPOWER4);
                break;
            case 6:
                getEffected().startAbnormalEffect(AbnormalEffect.TALISMANPOWER5);
                break;
            default:
                getEffected().startAbnormalEffect(AbnormalEffect.NULL);
        }
    }

    @Override
    public void onExit() {
        super.onExit();

        getEffected().stopAbnormalEffect(AbnormalEffect.TALISMANPOWER1);
        getEffected().stopAbnormalEffect(AbnormalEffect.TALISMANPOWER2);
        getEffected().stopAbnormalEffect(AbnormalEffect.TALISMANPOWER3);
        getEffected().stopAbnormalEffect(AbnormalEffect.TALISMANPOWER4);
        getEffected().stopAbnormalEffect(AbnormalEffect.TALISMANPOWER5);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}

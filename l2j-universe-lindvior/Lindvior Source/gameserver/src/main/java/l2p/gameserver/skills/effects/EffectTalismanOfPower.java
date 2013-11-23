package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.skills.AbnormalEffect;
import l2p.gameserver.stats.Env;

/**
 * Эффект нужен для отображения красивого эффекта на чаре.
 */
public class EffectTalismanOfPower extends Effect {

    public EffectTalismanOfPower(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (getSkill().getLevel()) {
            case 1:
            case 2:
                getEffected().startAbnormalEffect(AbnormalEffect.SPECIAL_AURA);
                break;
            case 3:
                getEffected().startAbnormalEffect(AbnormalEffect.SPECIAL_AURA_1);
                break;
            case 4:
                getEffected().startAbnormalEffect(AbnormalEffect.SPECIAL_AURA_2);
                break;
            case 5:
                getEffected().startAbnormalEffect(AbnormalEffect.SPECIAL_AURA_3);
                break;
            case 6:
                getEffected().startAbnormalEffect(AbnormalEffect.SPECIAL_AURA_4);
                break;
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        getEffected().stopAbnormalEffect(AbnormalEffect.SPECIAL_AURA);
        getEffected().stopAbnormalEffect(AbnormalEffect.SPECIAL_AURA_1);
        getEffected().stopAbnormalEffect(AbnormalEffect.SPECIAL_AURA_2);
        getEffected().stopAbnormalEffect(AbnormalEffect.SPECIAL_AURA_3);
        getEffected().stopAbnormalEffect(AbnormalEffect.SPECIAL_AURA_4);
    }

    @Override
    protected boolean onActionTime() {
        return false;
    }

}

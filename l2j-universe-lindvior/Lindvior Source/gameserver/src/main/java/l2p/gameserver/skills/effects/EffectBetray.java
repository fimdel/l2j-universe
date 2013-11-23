package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Summon;
import l2p.gameserver.stats.Env;

import static l2p.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;

public class EffectBetray extends Effect {
    public EffectBetray(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_effected != null && _effected.isServitor()) {
            Summon summon = (Summon) _effected;
            summon.setDepressed(true);
            summon.getAI().Attack(summon.getPlayer(), true, false);
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        if (_effected != null && _effected.isServitor()) {
            Summon summon = (Summon) _effected;
            summon.setDepressed(false);
            summon.getAI().setIntention(AI_INTENTION_ACTIVE);
        }
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
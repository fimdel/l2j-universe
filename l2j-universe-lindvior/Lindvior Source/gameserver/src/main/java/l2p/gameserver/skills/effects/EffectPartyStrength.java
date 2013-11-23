/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2012.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Player;
import l2p.gameserver.stats.Env;

public class EffectPartyStrength extends l2p.gameserver.model.Effect {
    public EffectPartyStrength(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public boolean checkCondition() {
        if ((_effector == null) || (_effected == null)) {
            return false;
        }

        if (!_effector.isPlayer() || !_effected.isPlayer()) {
            return false;
        }

        final Player actor = _effector.getPlayer();
        final Player target = _effected.getPlayer();

        return (actor.getDistance(target) <= 900) && super.checkCondition();
    }

    @Override
    public boolean onActionTime() {
        if (!checkCondition() || (_effector.getEffectList().getEffectsBySkillId(getSkill().getId()) == null) || ((_effected != _effector) && ((_effected.getPlayer().getParty() == null) || (_effector.getPlayer().getParty() != _effected.getPlayer().getParty())))) {
            _effected.getEffectList().stopEffect(getSkill().getId());
        } else if ((_effector.getPlayer().getParty() != null)) {
            for (final Player s : _effector.getPlayer().getParty().getPartyMembers()) {
                if (s.getEffectList().getEffectsBySkillId(getSkill().getId()) == null) {
                    getSkill().getEffects(_effector, s, getSkill().getActivateRate() > 0, false);
                }
            }
        }

        return true;
    }
}


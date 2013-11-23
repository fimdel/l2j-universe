/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Skill;
import l2p.gameserver.skills.EffectType;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 24.11.12 Time: 0:04
 */
public class EffectDoubleCasting extends Effect {
    private static final int[] toggles =
            {
                    11007, 11008, 11009, 11010
            };

    public EffectDoubleCasting(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    protected void onStart() {
        super.onStart();

        for (final int t : toggles) {
            if (_effected.getEffectList().getEffectsBySkillId(t) == null) {
                final Skill start = SkillTable.getInstance().getInfo(t, _effected.getSkillLevel(t));
                if (start != null) {
                    start.getEffects(_effector, _effected, false, false);
                }
            }
        }
    }

    @Override
    protected void onExit() {
        super.onExit();

        for (final Effect ef : _effected.getEffectList().getAllEffects()) {
            if ((ef.getEffectType() == EffectType.ElementalyStance) && (ef.getStartTime() >= getStartTime())) {
                ef.exit();
            }
        }
    }

    @Override
    protected boolean onActionTime() {
        return false;
    }
}
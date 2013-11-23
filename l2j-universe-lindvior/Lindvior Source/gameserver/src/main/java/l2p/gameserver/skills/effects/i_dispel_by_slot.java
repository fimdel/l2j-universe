/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

import java.util.List;

public class i_dispel_by_slot extends Effect {
    private final String _abnormalType;
    private final int _maxAbnormalLvl;

    public i_dispel_by_slot(Env env, EffectTemplate template) {
        super(env, template);

        this._abnormalType = template.getParam().getString("abnormal_type");
        if (_abnormalType.equalsIgnoreCase("null") || _abnormalType.equalsIgnoreCase("none"))
            _maxAbnormalLvl = 0;
        else
            _maxAbnormalLvl = template.getParam().getInteger("max_abnormal_level", 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (_maxAbnormalLvl == 0) {
            return;
        }
        List<Effect> effects = getEffected().getEffectList().getEffectsByAbnormalType(this._abnormalType);
        for (Effect effect : effects) {
            if (effect.getSkill().isCancelable()) {
                if (_maxAbnormalLvl == -1 || effect.getAbnormalLvl() <= this._maxAbnormalLvl)
                    effect.exit();
            }
        }
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}

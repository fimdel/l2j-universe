/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class p_block_buff_slot extends Effect {
    private final String[] _blockedAbnormalTypes;

    public p_block_buff_slot(Env env, EffectTemplate template) {
        super(env, template);

        _blockedAbnormalTypes = template.getParam().getString("abnormal_types", "").split(";");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }

    public boolean checkBlockedAbnormalType(String param) {
        if (_blockedAbnormalTypes.length == 0) {
            return false;
        }
        for (String abnormalType : _blockedAbnormalTypes) {
            if (param.equalsIgnoreCase(abnormalType))
                return true;
        }
        return false;
    }
}
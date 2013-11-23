/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.entity.olympiad;

import l2p.gameserver.Config;

public enum CompType {
    NON_CLASSED(Config.ALT_OLY_NONCLASSED_RITEM_C, 5), CLASSED(Config.ALT_OLY_CLASSED_RITEM_C, 3);

    private int _reward;
    private int _looseMult;

    private CompType(int reward, int looseMult) {
        _reward = reward;
        _looseMult = looseMult;
    }

    public int getReward() {
        return _reward;
    }

    public int getLooseMult() {
        return _looseMult;
    }
}
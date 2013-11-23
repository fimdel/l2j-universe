/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.quest.startcondition.impl;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.startcondition.ICheckStartCondition;

public final class NobleCondition implements ICheckStartCondition {
    private final boolean _allowOnlyNoble;

    public NobleCondition(boolean allowOnlyNoble) {
        _allowOnlyNoble = allowOnlyNoble;
    }

    @Override
    public final boolean checkCondition(Player player) {
        if (_allowOnlyNoble && player.isNoble()) {
            return true;
        }
        return (!_allowOnlyNoble) && (!player.isNoble());
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.quest.startcondition.impl;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.quest.startcondition.ICheckStartCondition;
import org.apache.commons.lang3.ArrayUtils;

public class PlayerRaceCondition
        implements ICheckStartCondition {
    private Race[] races;

    public PlayerRaceCondition(Race[] paramArrayOfRace) {
        this.races = paramArrayOfRace;
    }

    public boolean checkCondition(Player paramPlayer) {
        return ArrayUtils.contains(this.races, paramPlayer.getRace());
    }
}

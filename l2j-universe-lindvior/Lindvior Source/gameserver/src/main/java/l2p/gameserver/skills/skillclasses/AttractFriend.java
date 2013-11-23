/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.templates.StatsSet;

public class AttractFriend extends AttractEnemy {
    public AttractFriend(StatsSet set) {
        super(set);
    }

    @Override
    public boolean isValidTarget(Creature activeChar, Creature target) {
        return (target != null) && (target.isPlayer()) && (target != activeChar);
    }
}
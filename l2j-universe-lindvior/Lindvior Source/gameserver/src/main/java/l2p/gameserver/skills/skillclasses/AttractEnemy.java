/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.utils.Location;

import java.util.List;

public class AttractEnemy extends Skill {
    public AttractEnemy(StatsSet set) {
        super(set);
    }

    public boolean isValidTarget(Creature activeChar, Creature target) {
        return (target != null) && (target.isMonster());
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets) {
            if (isValidTarget(activeChar, target)) {
                Location loc = target.getFlyLocation(activeChar, this);
                target.setLoc(loc);
                target.broadcastPacket(new FlyToLocation(target, loc, FlyToLocation.FlyType.CHARGE, 0));
                getEffects(activeChar, target, getActivateRate() > 0, false);
            }
        }
        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}

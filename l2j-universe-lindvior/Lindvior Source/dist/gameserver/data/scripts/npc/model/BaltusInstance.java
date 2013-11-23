/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class BaltusInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -5767532732085950450L;

    public BaltusInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public boolean isAutoAttackable(Creature attacker) {
        return attacker.isMonster();
    }

    @Override
    public boolean isInvul() {
        return false;
    }

    @Override
    public boolean isFearImmune() {
        return true;
    }

    @Override
    public boolean isParalyzeImmune() {
        return true;
    }
}


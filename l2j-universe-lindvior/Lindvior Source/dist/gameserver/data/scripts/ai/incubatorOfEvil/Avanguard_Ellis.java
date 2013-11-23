/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.incubatorOfEvil;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

/**
 * @author Mazaffaka
 */
public class Avanguard_Ellis extends DefaultAI {
    private NpcInstance target = null;

    public Avanguard_Ellis(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return false;
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (Rnd.chance(8)) {
            Functions.npcSay(actor, NpcString.I_CAN_HEAL_YOU_DURING_COMBAT);
        }
        return false;
    }
}
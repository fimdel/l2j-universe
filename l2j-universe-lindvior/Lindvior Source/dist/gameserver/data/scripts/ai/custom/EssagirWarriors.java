/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.custom;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

/**
 * Ai для Гвардов в Эссагире
 */
public class EssagirWarriors extends DefaultAI {
    private final static int[][] SMP_COORDS =
            {
                    {-115144, 237352, -3114}, {-115320, 236664, -3114}, {-115272, 235928, -3114}, {-114808, 235544, -3114}, {-114296, 235576, -3114}, {-113704, 236296, -3068}, {-113624, 236808, -3068}, {-113592, 237224, -3068}
            };
    private static final int SAY_RAFF = 12000;
    private int currentState;
    private long lastSayTimer = 0;

    public EssagirWarriors(NpcInstance actor) {
        super(actor);

        currentState = 0;
        lastSayTimer = 0;
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        int[][] coords;

        coords = SMP_COORDS;

        actor.setRunning();

        if ((actor.getTarget() == null) || (currentState >= coords.length) || (currentState == 0)) {
            if (currentState < coords.length) {
                actor.moveToLocation(coords[currentState][0], coords[currentState][1], coords[currentState][2], Rnd.get(0, 50), true);

                if (actor.getDestination() == null) {
                    ++currentState;
                }
            } else {
                actor.doDie(actor);
            }
        }

        if (lastSayTimer + SAY_RAFF < System.currentTimeMillis()) {
            lastSayTimer = System.currentTimeMillis();

            Functions.npcSay(actor, NpcString.IT_S_HERE, ChatType.NPC_SAY, 800);
        }

        return true;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    protected void onEvtDead(Creature killer) {
        return;
    }
}

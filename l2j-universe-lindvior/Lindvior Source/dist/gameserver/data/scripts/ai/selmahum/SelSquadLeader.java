package ai.selmahum;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.utils.Location;

public class SelSquadLeader extends Fighter {
    private boolean isBusy;
    private boolean isImmobilized;

    private long busyTimeout = 0;
    private long idleTimeout = 0;

    private static final NpcString[] phrase = {NpcString.COME_AND_EAT, NpcString.LOOKS_DELICIOUS, NpcString.LETS_GO_EAT};

    private static int NPC_ID_FIRE = 18927;
    private static int NPC_ID_FIRE_FEED = 18933;

    public SelSquadLeader(NpcInstance actor) {
        super(actor);
        MAX_PURSUE_RANGE = 6000;
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor.isDead())
            return true;

        if (!isBusy) {
            if (System.currentTimeMillis() > idleTimeout)
                for (NpcInstance npc : actor.getAroundNpc(600, 300))
                    if (npc.getNpcId() == NPC_ID_FIRE_FEED && GeoEngine.canSeeTarget(actor, npc, false)) // Cauldron
                    {
                        isBusy = true;
                        actor.setRunning();
                        actor.setNpcState(1); // Yummi State
                        busyTimeout = System.currentTimeMillis() + (60 + Rnd.get(15)) * 1000L;
                        addTaskMove(Location.findPointToStay(npc, 50, 150), true);
                        if (Rnd.chance(40))
                            Functions.npcSay(actor, phrase[Rnd.get(2)]);
                    } else if (npc.getNpcId() == NPC_ID_FIRE && npc.getNpcState() == 1 && GeoEngine.canSeeTarget(actor, npc, false)) {
                        isBusy = true;
                        actor.setNpcState(2); // Sleepy State
                        busyTimeout = System.currentTimeMillis() + (60 + Rnd.get(60)) * 1000L;
                        addTaskMove(Location.findPointToStay(npc, 50, 150), true);
                    }
        } else if (System.currentTimeMillis() > busyTimeout) {
            wakeUp();
            actor.setWalking();
            addTaskMove(actor.getSpawnedLoc(), true);
            return true;
        }

        if (isImmobilized)
            return true;

        return super.thinkActive();
    }

    private void wakeUp() {
        NpcInstance actor = getActor();

        if (isBusy) {
            isBusy = false;

            busyTimeout = 0;
            idleTimeout = System.currentTimeMillis() + Rnd.get(3 * 60, 5 * 60) * 1000L;

            if (isImmobilized) {
                isImmobilized = false;
                actor.stopImmobilized();
                actor.setNpcState(3);
                actor.setRHandId(0);
                actor.broadcastCharInfo();
            }
        }
    }

    @Override
    protected void onEvtArrived() {
        NpcInstance actor = getActor();

        super.onEvtArrived();

        if (isBusy) {
            isImmobilized = true;
            actor.startImmobilized();
            actor.setRHandId(15280);
            actor.broadcastCharInfo();
        }
    }

    @Override
    protected void onIntentionActive() {
        //таймаут после атаки
        idleTimeout = System.currentTimeMillis() + Rnd.get(60, 5 * 60) * 1000L;

        super.onIntentionActive();
    }

    @Override
    protected void onIntentionAttack(Creature target) {
        wakeUp();

        super.onIntentionAttack(target);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected void returnHome(boolean clearAggro, boolean teleport) {
    }
}
package ai;

import l2p.commons.collections.GArray;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

public class FieldMachine extends DefaultAI {
    private long _lastAction;

    public FieldMachine(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (attacker == null || attacker.getPlayer() == null)
            return;

        // Ругаемся не чаще, чем раз в 15 секунд
        if (System.currentTimeMillis() - _lastAction > 15000) {
            _lastAction = System.currentTimeMillis();
            Functions.npcSayCustomMessage(actor, "scripts.ai.FieldMachine." + actor.getNpcId());
            GArray<NpcInstance> around = actor.getAroundNpc(1500, 300);
            if (around != null && !around.isEmpty())
                for (NpcInstance npc : around)
                    if (npc.isMonster() && npc.getNpcId() >= 22656 && npc.getNpcId() <= 22659)
                        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
        }
    }
}
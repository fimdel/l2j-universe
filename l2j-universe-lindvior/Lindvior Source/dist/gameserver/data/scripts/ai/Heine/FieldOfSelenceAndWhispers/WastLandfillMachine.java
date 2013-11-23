package ai.Heine.FieldOfSelenceAndWhispers;

import l2p.gameserver.ai.CharacterAI;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

import java.util.List;

/**
 * - User: Mpa3uHaKaMa3e
 * - Date: 26.06.12
 * - Time: 21:33
 * - AI для нпц Waste Landfill Machine (18805).
 * - Если был атакован то кричит в чат и зовут на помощь.
 */
public class WastLandfillMachine extends CharacterAI {
    private boolean _firstTimeAttacked = true;

    public WastLandfillMachine(Creature actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = (NpcInstance) getActor();
        if (actor == null || actor.isDead())
            return;

        if (_firstTimeAttacked) {
            _firstTimeAttacked = false;
            Functions.npcSay(actor, NpcString.ALERT_ALERT_DAMAGE_DETECTION_RECOGNIZED_COUNTERMEASURES_ENABLED, ChatType.ALL, 5000);
            List<NpcInstance> around = actor.getAroundNpc(1500, 300);
            if (around != null && !around.isEmpty()) {
                for (NpcInstance npc : around) {
                    if (npc.isMonster() && npc.getNpcId() == 22656 || npc.getNpcId() == 22657)
                        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
                }
            }
        }

        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}
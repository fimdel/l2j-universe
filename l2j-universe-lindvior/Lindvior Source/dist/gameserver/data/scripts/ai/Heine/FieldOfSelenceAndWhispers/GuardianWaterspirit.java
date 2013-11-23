package ai.Heine.FieldOfSelenceAndWhispers;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

import java.util.List;

/**
 * - User: Mpa3uHaKaMa3e
 * - Date: 26.06.12
 * - Time: 20:46
 * - AI для нпц Guardian Waterspirit (22659).
 * - При атаке ругается в чат.
 * - Агрится на Мракобиан в пределах радиуса.
 */
public class GuardianWaterspirit extends Fighter {
    private NpcInstance mob = null;
    private boolean _firstTimeAttacked = true;
    public static final NpcString[] MsgText = {
            NpcString.AH_AH_FROM_THE_MAGIC_FORCE_NO_MORE_I_WILL_BE_FREED,
            NpcString.EVEN_THE_MAGIC_FORCE_BINDS_YOU_YOU_WILL_NEVER_BE_FORGIVEN
    };

    public GuardianWaterspirit(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onIntentionAttack(Creature target) {
        NpcInstance actor = getActor();
        if (actor == null)
            return;

        if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
            Functions.npcSay(actor, NpcString.DEFEND_OUR_DOMAIN_EVEN_AT_RISK_OF_YOUR_OWN_LIFE, ChatType.SHOUT, 5000);
        super.onIntentionAttack(target);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor == null || actor.isDead())
            return true;

        if (mob == null) {
            List<NpcInstance> around = getActor().getAroundNpc(300, 300);
            if (around != null && !around.isEmpty())
                for (NpcInstance npc : around)
                    if (npc.getNpcId() >= 22650 && npc.getNpcId() <= 22655)
                        if (mob == null || getActor().getDistance3D(npc) < getActor().getDistance3D(mob))
                            mob = npc;

        }
        if (mob != null) {
            actor.stopMove();
            actor.setRunning();
            getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, mob, 1);
            return true;
        }
        return false;
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (actor == null)
            return;

        if (_firstTimeAttacked) {
            _firstTimeAttacked = false;
            if (Rnd.chance(25))
                Functions.npcSay(actor, Rnd.get(MsgText), ChatType.ALL, 5000);
        } else if (Rnd.chance(10))
            Functions.npcSay(actor, NpcString.DEFEND_OUR_DOMAIN_EVEN_AT_RISK_OF_YOUR_OWN_LIFE, ChatType.SHOUT, 5000);
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}
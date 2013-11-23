package ai;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.QuestEventType;
import l2p.gameserver.model.quest.QuestState;

import java.util.List;

/**
 * Квестовый NPC, атакующий мобов. Игнорирует игроков.
 *
 * @author Diamond
 */
public class AttackMobNotPlayerFighter extends Fighter {
    public AttackMobNotPlayerFighter(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (attacker == null)
            return;

        Player player = attacker.getPlayer();
        if (player != null) {
            List<QuestState> quests = player.getQuestsForEvent(actor, QuestEventType.ATTACKED_WITH_QUEST, false);
            if (quests != null)
                for (QuestState qs : quests)
                    qs.getQuest().notifyAttack(actor, qs);
        }

        onEvtAggression(attacker, damage);
    }

    @Override
    protected void onEvtAggression(Creature attacker, int aggro) {
        NpcInstance actor = getActor();
        if (attacker == null)
            return;

        if (!actor.isRunning())
            startRunningTask(AI_TASK_ATTACK_DELAY);

        if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
    }
}
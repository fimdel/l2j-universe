package events.TheFlowOfTheHorror;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;

public class MonstersAI extends Fighter {
    private List<Location> _points = new ArrayList<Location>();
    private int current_point = -1;

    public void setPoints(List<Location> points) {
        _points = points;
    }

    public MonstersAI(NpcInstance actor) {
        super(actor);
        AI_TASK_ATTACK_DELAY = 500;
        MAX_PURSUE_RANGE = 30000;
    }

    @Override
    public int getMaxAttackTimeout() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor == null || actor.isDead())
            return true;

        if (_def_think) {
            doTask();
            return true;
        }

        if (current_point > -1 || Rnd.chance(5)) {
            if (current_point >= _points.size() - 1) {
                Creature target = GameObjectsStorage.getByNpcId(30754);
                if (target != null && !target.isDead()) {
                    clearTasks();
                    // TODO actor.addDamageHate(target, 0, 1000);
                    setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
                    return true;
                }
                return true;
            }

            current_point++;

            actor.setRunning();

            clearTasks();

            // Добавить новое задание
            addTaskMove(_points.get(current_point), true);
            doTask();
            return true;
        }

        if (randomAnimation())
            return true;

        return false;
    }
}
package ai.dragonvalley;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author pchayka
 */

public class Patrollers extends Fighter {
    protected Location[] _points;
    private int[] _teleporters = {22857, 22833, 22834};

    private int _lastPoint = 0;
    private boolean _firstThought = true;

    public Patrollers(NpcInstance actor) {
        super(actor);
        MAX_PURSUE_RANGE = Integer.MAX_VALUE - 10;
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    public boolean checkAggression(Creature target) {
        NpcInstance actor = getActor();
        if (target.isPlayable() && !target.isDead() && !target.isInvisible()) {
            actor.getAggroList().addDamageHate(target, 0, 1);
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
        }
        return true;
    }

    @Override
    protected boolean thinkActive() {
        if (super.thinkActive())
            return true;

        if (!getActor().isMoving)
            startMoveTask();

        return true;
    }

    @Override
    protected void onEvtArrived() {
        startMoveTask();
        super.onEvtArrived();
    }

    private void startMoveTask() {
        NpcInstance npc = getActor();
        if (_firstThought) {
            _lastPoint = getIndex(Location.findNearest(npc, _points));
            _firstThought = false;
        } else
            _lastPoint++;
        if (_lastPoint >= _points.length) {
            _lastPoint = 0;
            if (ArrayUtils.contains(_teleporters, npc.getNpcId()))
                npc.teleToLocation(_points[_lastPoint]);
        }
        npc.setRunning();
        if (Rnd.chance(30))
            npc.altOnMagicUseTimer(npc, SkillTable.getInstance().getInfo(6757, 1));
        addTaskMove(Location.findPointToStay(_points[_lastPoint], 250, npc.getGeoIndex()), true);
        doTask();
    }

    private int getIndex(Location loc) {
        for (int i = 0; i < _points.length; i++)
            if (_points[i] == loc)
                return i;
        return 0;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    @Override
    protected boolean maybeMoveToHome() {
        return false;
    }

    @Override
    protected void teleportHome() {
    }

    @Override
    protected void returnHome(boolean clearAggro, boolean teleport) {
        super.returnHome(clearAggro, teleport);
        clearTasks();
        _firstThought = true;
        startMoveTask();
    }
}

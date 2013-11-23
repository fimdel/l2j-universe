package ai.hermunkus_message;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

import java.util.List;

public class Rogin_19193 extends Dwarvs {
    private static final int BRONK_ID = 19192;

    private static final int[][] MOVE_LOC = {
            {116400, -183069, -1600}
    };

    private int currentPoint;

    public Rogin_19193(NpcInstance actor) {
        super(actor);
        currentPoint = 0;
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        getActor().setRunning();
        addTaskMove(MOVE_LOC[currentPoint][0], MOVE_LOC[currentPoint][1], MOVE_LOC[currentPoint][2], true);
        doTask();
    }

    @Override
    protected void onEvtArrived() {
        super.onEvtArrived();
        if (currentPoint == 0) {
            addTimer(1, 1600);
            currentPoint++;
        }
    }

    @Override
    protected void onEvtScriptEvent(String event, Object arg1, Object arg2) {
        if (event.equalsIgnoreCase("ROGIN_1"))
            addTimer(3, 1600);
        else if (event.equalsIgnoreCase("SHOUT_ALL"))
            Functions.npcSayInRange(getActor(), 1500, NpcString.CHIEF_);
        else
            super.onEvtScriptEvent(event, arg1, arg2);
    }

    @Override
    protected void onEvtTimer(int timerId, Object arg1, Object arg2) {
        super.onEvtTimer(timerId, arg1, arg2);

        Reflection r = getActor().getReflection();
        if (r == ReflectionManager.DEFAULT)
            return;
        List<NpcInstance> list;
        switch (timerId) {
            case 1:
                Functions.npcSayInRange(getActor(), 1500, NpcString.CHIEF_REPORTING_IN);
                addTimer(2, 1600);
                break;
            case 2:
                Functions.npcSayInRange(getActor(), 1500, NpcString.ENEMIES_ARE_APPROACHING_FROM_THE_SOUTH);
                list = r.getAllByNpcId(BRONK_ID, true);
                if (list.size() > 0) {
                    NpcInstance bronk = list.get(0);
                    bronk.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "BRONK_1");
                }
                break;
            case 3:
                Functions.npcSayInRange(getActor(), 1500, NpcString.THE_ELDERS_HAVENT_BEEN_MOVED_TO_SAFETY);
                addTimer(4, 1600);
                break;
            case 4:
                Functions.npcSayInRange(getActor(), 1500, NpcString.MANY_RESIDENTS_STILL_HAVENT_LEFT_THEIR_HOMES);
                list = r.getAllByNpcId(BRONK_ID, true);
                if (list.size() > 0) {
                    NpcInstance bronk = list.get(0);
                    bronk.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "BRONK_2");
                }
                break;
        }
    }
}

package ai.hermunkus_message;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

import java.util.List;

public class Torocco_19198 extends Dwarvs {
    private static final int ROGIN_ID = 19193;

    public Torocco_19198(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtScriptEvent(String event, Object arg1, Object arg2) {
        super.onEvtScriptEvent(event, arg1, arg2);

        if (event.equalsIgnoreCase("TOROCCO_1")) {
            addTimer(1, 1200);
        }
    }

    @Override
    protected void onEvtTimer(int timerId, Object arg1, Object arg2) {
        super.onEvtTimer(timerId, arg1, arg2);

        Reflection r = getActor().getReflection();
        if (r == ReflectionManager.DEFAULT)
            return;

        switch (timerId) {
            case 1:
                Functions.npcSayInRange(getActor(), 1500, NpcString.ROGIN_IM_HERE);
                List<NpcInstance> list = r.getAllByNpcId(ROGIN_ID, true);
                if (list.size() > 0) {
                    NpcInstance rogin = list.get(0);
                    rogin.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "ROGIN_1");
                }
                break;
        }
    }
}

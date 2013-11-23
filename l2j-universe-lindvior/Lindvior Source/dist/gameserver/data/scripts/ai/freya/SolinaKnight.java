package ai.freya;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.List;

public class SolinaKnight extends Fighter {
    private NpcInstance scarecrow = null;

    public SolinaKnight(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected boolean thinkActive() {
        if (scarecrow == null) {
            List<NpcInstance> around = getActor().getAroundNpc(300, 100);
            if (around != null && !around.isEmpty())
                for (NpcInstance npc : around)
                    if (npc.getNpcId() == 18912)
                        if (scarecrow == null || getActor().getDistance3D(npc) < getActor().getDistance3D(scarecrow))
                            scarecrow = npc;
        }

        if (scarecrow != null) {
            getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, scarecrow, 1);
            return true;
        }

        return false;
    }
}
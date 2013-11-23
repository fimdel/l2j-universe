package ai.dragonvalley;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Mystic;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.NpcUtils;

/**
 * @author pchayka
 *         После смерти призывает одного из двух монстров
 */
public class Necromancer extends Mystic {
    public Necromancer(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        super.onEvtDead(killer);
        if (Rnd.chance(30)) {
            NpcInstance n = NpcUtils.spawnSingle(Rnd.chance(50) ? 22818 : 22819, getActor().getLoc());
            n.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, 2);
        }
    }
}
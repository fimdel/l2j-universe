package ai.hellbound;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.instancemanager.naia.NaiaCoreManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

/**
 * @author pchayka
 */
public class MutatedElpy extends Fighter {
    public MutatedElpy(NpcInstance actor) {
        super(actor);
        actor.startImmobilized();
    }

    @Override
    protected void onEvtDead(Creature killer) {
        NaiaCoreManager.launchNaiaCore();
        super.onEvtDead(killer);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        actor.doDie(attacker);
    }

}
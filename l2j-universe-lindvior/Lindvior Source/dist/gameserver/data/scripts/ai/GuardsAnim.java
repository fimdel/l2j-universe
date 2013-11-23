/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SocialAction;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

import java.util.ArrayList;
import java.util.List;

public class GuardsAnim extends DefaultAI {
    private static final NpcString[] SAY_WESTERN = {NpcString.ATTACK_2, NpcString.FOLLOW_ME};

    public GuardsAnim(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        NpcInstance actor = getActor();
        super.onEvtSpawn();
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new startCaptain(actor), 15000L, 7500L);
    }

    private static class startCaptain extends RunnableImpl {
        private NpcInstance _actor;

        public startCaptain(NpcInstance actor) {
            _actor = actor;
        }

        @Override
        public void runImpl() {
            int rndSocial = Rnd.get(3, 7);
            if (_actor.getNpcId() == 33434)
                Functions.npcSay(_actor, GuardsAnim.SAY_WESTERN[Rnd.get(0, GuardsAnim.SAY_WESTERN.length - 1)]);
            _actor.broadcastPacket(new SocialAction(_actor.getObjectId(), rndSocial));

            int neededNpc = _actor.getNpcId() == 33434 ? 33437 : 33018;

            List<NpcInstance> around = _actor.getAroundNpc(1000, 300);
            List<NpcInstance> filteredNpc = new ArrayList<NpcInstance>();

            for (NpcInstance npc : around) {
                if (npc.getNpcId() == neededNpc)
                    filteredNpc.add(npc);
            }
            ThreadPoolManager.getInstance().schedule(new GuardsAnim.broadcastToMinions(rndSocial, filteredNpc), 2000L);
        }
    }

    private static class broadcastToMinions extends RunnableImpl {
        private final int _socialId;
        private List<NpcInstance> _mobList;

        public broadcastToMinions(int socialId, List<NpcInstance> mobList) {
            _socialId = socialId;
            _mobList = mobList;
        }

        public void runImpl() {
            for (NpcInstance minion : _mobList)
                if (minion != null)
                    minion.broadcastPacket(new SocialAction(minion.getObjectId(), _socialId));
        }
    }
}

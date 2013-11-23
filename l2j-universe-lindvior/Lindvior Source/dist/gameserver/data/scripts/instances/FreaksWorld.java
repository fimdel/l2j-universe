/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcSay;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;

import java.util.List;

public class FreaksWorld extends Reflection {
    private final int mystic = 33363;
    private boolean sayLocker = false;

    public FreaksWorld() {
        super();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
        ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.I_CAN_HEAL_YOU_DURING_COMBAT, mystic), 10000);
        ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.I_HIT_THINGS_THEY_FALL_DEAD, mystic), 6000);
        ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.WHAT_DO_I_FEEL_WHEN_I_KILL_SHILENS_MONSTERS_RECOIL, mystic), 3000);
        ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.MY_SUMMONS_ARE_NOT_AFRAID_OF_SHILENS_MONSTER, mystic), 9000);
        ThreadPoolManager.getInstance().schedule(new Exit(), 120000);
    }


    private class SayChatTask extends RunnableImpl {
        private NpcString msg;
        private int npcId;

        public SayChatTask(NpcString msg, int npcId) {
            this.msg = msg;
            this.npcId = npcId;
        }

        @Override
        public void runImpl() throws Exception {
            List<NpcInstance> npc = getAllByNpcId(npcId, true);
            if (!npc.isEmpty())
                npc.get(0).broadcastPacket(new NpcSay(npc.get(0), ChatType.NPC_SAY, msg));
            if (!sayLocker && !npc.isEmpty())
                ThreadPoolManager.getInstance().schedule(this, 8000);
        }
    }

    private class Exit extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {

            startCollapseTimer(1000L);
            for (Player player : getPlayers())
                player.showQuestMovie(104);
        }
    }
}

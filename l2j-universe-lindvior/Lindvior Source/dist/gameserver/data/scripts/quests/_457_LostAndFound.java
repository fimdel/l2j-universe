package quests;

import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

import java.util.concurrent.ScheduledFuture;

//TODO: Переписать этот бред!
public class _457_LostAndFound extends Quest implements ScriptFile {
    private ScheduledFuture<?> _followTask;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _457_LostAndFound() {
        super(true);
        addStartNpc(32759);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        String htmltext = event;
        if (event.equalsIgnoreCase("lost_villager_q0457_06.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);

            npc.setFollowTarget(st.getPlayer());
            if (_followTask != null) {
                _followTask.cancel(false);
                _followTask = null;
            }
            _followTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Follow(npc, player, st), 0L, 1000L);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        int npcId = npc.getNpcId();
        int state = st.getState();
        int cond = st.getCond();
        if (npcId == 32759) {
            if (state == 1) {
                if (npc.getFollowTarget() != null && npc.getFollowTarget() != player)
                    return "lost_villager_q0457_01a.htm";

                if (checkStartCondition(st.getPlayer())) {
                    if (st.isNowAvailable())
                        return "lost_villager_q0457_01.htm";
                    else
                        return "lost_villager_q0457_02.htm";
                }
                return "lost_villager_q0457_03.htm";
            }
            if (state == 2) {
                if (npc.getFollowTarget() != null && npc.getFollowTarget() != player)
                    return "lost_villager_q0457_01a.htm";

                if (cond == 1)
                    return "lost_villager_q0457_08.htm";
                else if (cond == 2) {
                    npc.deleteMe();

                    st.giveItems(15716, 1);
                    st.unset("cond");
                    st.playSound(SOUND_FINISH);
                    st.exitCurrentQuest(this);
                    return "lost_villager_q0457_09.htm";
                }
            }
        }
        return "noquest";
    }

    private void checkInRadius(int id, QuestState st, NpcInstance npc) {
        NpcInstance quest0457 = GameObjectsStorage.getByNpcId(id);
        if (npc.getRealDistance3D(quest0457) <= 150) {
            st.setCond(2);
            if (_followTask != null)
                _followTask.cancel(false);
            _followTask = null;
            npc.stopMove();
        }
    }

    private class Follow implements Runnable {
        private NpcInstance _npc;
        private Player player;
        private QuestState st;

        private Follow(NpcInstance npc, Player pl, QuestState _st) {
            _npc = npc;
            player = pl;
            st = _st;
        }

        @Override
        public void run() {
            _npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, player, 150);
            checkInRadius(32764, st, _npc);
        }
    }
}
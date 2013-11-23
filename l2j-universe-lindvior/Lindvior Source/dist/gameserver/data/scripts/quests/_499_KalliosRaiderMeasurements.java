package quests;

import gnu.trove.TIntIntHashMap;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExQuestNpcLogList;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 16.10.12
 * Time: 23:55
 */
public class _499_KalliosRaiderMeasurements extends Quest {
    private static final int ИсследовательКартии = 33647;
    private static final int Каллиос = 25884;
    private static final int СундукХранителя = 34932;

    public _499_KalliosRaiderMeasurements() {
        super(false);

        addStartNpc(ИсследовательКартии);
        addTalkId(ИсследовательКартии);
        addKillId(Каллиос);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (st == null) {
            return htmltext;
        }
        if (npc.getNpcId() == 33647) {
            if (event.equalsIgnoreCase("33647-07.htm")) {
                st.startQuest();
            } else if (event.equalsIgnoreCase("33647-10.htm")) {
                st.unset("cond");
                st.giveItem(СундукХранителя);
                st.playSound("ItemSound.quest_finish");
                st.exitQuest(false);
            }
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = getNoQuestMsg(st.getPlayer());
        if (npc.getNpcId() == 33647) {
            if (st.getPlayer().getLevel() < 95) {
                st.exitQuest(true);
                return "33647-02.htm";
            }
            switch (st.getState()) {
                case 1:
                    htmltext = "33647-01.htm";
                    break;
                case 2:
                    if (st.getCond() == 1) {
                        htmltext = "33647-08.htm";
                    } else {
                        if (st.getCond() != 2) break;
                        htmltext = "33647-09.htm";
                    }
                    break;
                case 3:
                    htmltext = "33647-03.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null)) {
            return null;
        }
        if ((st.isStarted()) && (st.getCond() == 1) && (npc.getNpcId() == 25884)) {
            TIntIntHashMap moblist = new TIntIntHashMap();
            moblist.put(1025884, 1);
            st.getPlayer().sendPacket(new ExQuestNpcLogList(499, moblist));
            st.setCond(2);
            st.playSound("ItemSound.quest_middle");
        }
        return null;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return player.getLevel() >= 95;
    }

    @Override
    public boolean isDailyQuest() {
        return true;
    }
}

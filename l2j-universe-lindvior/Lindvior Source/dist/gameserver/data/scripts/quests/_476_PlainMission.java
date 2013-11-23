package quests;

import gnu.trove.TIntIntHashMap;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExQuestNpcLogList;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.09.12
 * Time: 3:38
 */
public class _476_PlainMission extends Quest implements ScriptFile {
    private static final int Гид = 33463;
    private static final int Андрей = 31292;
    private static final int Антилопа = 21278;
    private static final int Драколов = 21282;
    private static final int Буйвол = 21286;
    private static final int Грендель = 21290;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _476_PlainMission() {
        super(false);
        addStartNpc(Гид);
        addTalkId(Гид);
        addTalkId(Андрей);
        addKillId(Антилопа, Драколов, Буйвол, Грендель);
    }

    public String onEvent(String event, QuestState st, NpcInstance npc) {


        if (!event.equalsIgnoreCase("33463-04.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {

        String htmltext = "noquest";

        if (npc.getNpcId() == 33463) {
            if (st.getPlayer().getLevel() < 65) {
                st.exitCurrentQuest(true);
                return getLowLevelMsg(65);
            }
            if (st.getPlayer().getLevel() > 69) {
                st.exitCurrentQuest(true);
                return getHighLevelMsg(69);
            }
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = getAlreadyCompletedMsg(st.getPlayer());
                    break;
                case CREATED:
                    htmltext = "33463-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1)
                        htmltext = "33463-05.htm";
                    else {
                        htmltext = "33463-06.htm";
                    }
            }
        } else if (npc.getNpcId() == 31292) {
            if (st.isStarted()) {
                if (st.getCond() == 1) {
                    htmltext = "31292-02.htm";
                } else if (st.getCond() == 2) {
                    htmltext = "31292-01.htm";
                    st.addExpAndSp(4685175, 3376245);
                    st.giveAdena(142200);
                    st.exitCurrentQuest(false);
                }
            } else if (st.isCompleted()) {
                htmltext = "31292-03.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {

        if ((npc == null) || (st == null)) {
            return null;
        }
        Player killer = st.getPlayer();
        if (st.getCond() == 1) {
            int _1 = st.getInt("_1");
            int _2 = st.getInt("_2");
            int _3 = st.getInt("_3");
            int _4 = st.getInt("_4");

            TIntIntHashMap moblist = new TIntIntHashMap();

            if ((npc.getNpcId() == 21278) && (_1 < 12)) {
                _1++;
                st.set("_1", String.valueOf(_1));
            } else if ((npc.getNpcId() == 21282) && (_2 < 12)) {
                _2++;
                st.set("_2", String.valueOf(_2));
            } else if ((npc.getNpcId() == 21286) && (_3 < 12)) {
                _3++;
                st.set("_3", String.valueOf(_3));
            } else if ((npc.getNpcId() == 21290) && (_4 < 12)) {
                _4++;
                st.set("_4", String.valueOf(_4));
            }
            if (_1 + _2 + _3 + _4 >= 48) {
                st.setCond(2);
                st.playSound("ItemSound.quest_middle");
            }
            moblist.put(1021278, _1);
            moblist.put(1021282, _2);
            moblist.put(1021286, _3);
            moblist.put(1021290, _4);
            killer.sendPacket(new ExQuestNpcLogList(476, moblist));
        }
        return null;
    }

    @Override
    public boolean isDailyQuest() {
        return true;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return (player.getLevel() >= 65) && (player.getLevel() <= 69);
    }
}

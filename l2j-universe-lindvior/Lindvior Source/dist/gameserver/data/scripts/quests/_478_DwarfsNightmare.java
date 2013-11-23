package quests;

import gnu.trove.TIntIntHashMap;
import l2p.commons.util.Rnd;
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
 * Time: 4:25
 */
public class _478_DwarfsNightmare extends Quest implements ScriptFile {
    private static final int Дайчир = 30537;
    private static final int Траскен = 29197;
    private static final int Щупальце = 29198;
    private static final int ОгромноеЩупальце = 29199;
    private static final int Личинка = 29207;
    private static final int[] Награды95 = {17623, 15559, 9552, 9553, 9554, 9555, 9556, 9557};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _478_DwarfsNightmare() {
        super(false);
        addStartNpc(Дайчир);
        addTalkId(Дайчир);
        addKillId(Щупальце, ОгромноеЩупальце, Личинка, Траскен);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equalsIgnoreCase("30537-07.htm")) {
            st.playSound(Quest.SOUND_ACCEPT);
            st.setState(Quest.STARTED);
            st.setCond(1);
        } else if (event.equalsIgnoreCase("30537-08.htm")) {
            if (st.getPlayer().getLevel() >= 95) {
                st.setCond(2);
                st.playSound("ItemSound.quest_middle");
            } else if (st.getPlayer().getLevel() >= 85 && st.getPlayer().getLevel() < 95) {
                st.setCond(3);
                st.playSound("ItemSound.quest_middle");
            }
        } else if (event.equalsIgnoreCase("30537-16.htm")) {
            if (st.getPlayer().getLevel() >= 95) {
                st.giveItems(Награды95[Rnd.get(Награды95.length)], 1);
                st.playSound("ItemSound.quest_finish");
                st.exitCurrentQuest(false);
            } else if (st.getPlayer().getLevel() >= 85 && st.getPlayer().getLevel() < 95) {
                st.giveAdena(550000);
                st.playSound("ItemSound.quest_finish");
                st.exitCurrentQuest(false);
            }
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";

        if (npc.getNpcId() == 30537) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "30537-03.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 85) {
                        htmltext = "30537-01.htm";
                    } else {
                        htmltext = "30537-02.htm";
                        st.exitCurrentQuest(true);
                    }
                    break;
                case STARTED:
                    switch (st.getCond()) {
                        case 1:
                            htmltext = "30537-01.htm";
                            break;
                        case 2:
                        case 3:
                            htmltext = "30537-09.htm";
                            break;
                        case 4:
                        case 5:
                            htmltext = "30537-15.htm";
                    }
            }

        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        Player killer = st.getPlayer();
        if (st.getCond() == 2 && npc.getNpcId() == 29197) {
            TIntIntHashMap moblist = new TIntIntHashMap();
            moblist.put(1029197, 1);
            if (killer.getParty() != null) {
                for (Player partyMember : killer.getParty().getPartyMembers()) {
                    QuestState pst = partyMember.getQuestState("_478_DwarfsNightmare");
                    if (pst != null && pst.getCond() == 2) {
                        st.setCond(4);
                        st.playSound("ItemSound.quest_middle");
                    }
                }
            } else {
                st.setCond(4);
                st.playSound("ItemSound.quest_middle");
            }
            killer.sendPacket(new ExQuestNpcLogList(478, moblist));
        } else if (st.getCond() == 3) {
            int _1 = st.getInt("_1");
            int _2 = st.getInt("_2");
            int _3 = st.getInt("_3");

            TIntIntHashMap moblist = new TIntIntHashMap();

            if (npc.getNpcId() == 29198 && _1 < 10) {
                _1++;
                st.set("_1", String.valueOf(_1));
            } else if (npc.getNpcId() == 29199 && _2 < 10) {
                _2++;
                st.set("_2", String.valueOf(_2));
            } else if (npc.getNpcId() == 29207 && _3 < 10) {
                _3++;
                st.set("_3", String.valueOf(_3));
            }

            if (_1 + _2 + _3 >= 30) {
                st.setCond(5);
                st.playSound("ItemSound.quest_middle");
            }
            moblist.put(1029198, _1);
            moblist.put(1029199, _2);
            moblist.put(1029207, _3);
            killer.sendPacket(new ExQuestNpcLogList(478, moblist));
        }
        return null;
    }

    @Override
    public boolean isDailyQuest() {
        return true;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return player.getLevel() >= 85;
    }
}

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _490_DutyOfTheSurvivor extends Quest implements ScriptFile {
    private static final int VOLLODOS = 30137;
    private static final int EXTRACT = 34059;
    private static final int BLOOD = 34060;
    private static final int DROP_CHANCE = 60;
    private static final int[] EXTRACT_MOBS = {23162, 23163, 23164, 23165, 23166, 23167};
    private static final int[] BLOOD_MOBS = {23168, 23169, 23170, 23171, 23172, 23173};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _490_DutyOfTheSurvivor() {
        super(false);

        addStartNpc(VOLLODOS);
        addTalkId(VOLLODOS);
        addKillId(EXTRACT_MOBS);
        addKillId(BLOOD_MOBS);
        addQuestItem(EXTRACT, BLOOD);
    }

    @Override
    public boolean isDailyQuest() {
        return true;
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if ((npc.getNpcId() == 30137) && (event.equalsIgnoreCase("30137-05.htm"))) {
            st.playSound(Quest.SOUND_ACCEPT);
            st.setState(Quest.STARTED);
            st.setCond(1);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        if (npc.getNpcId() == 30137) {
            if ((st.getPlayer().getLevel() < 85) || (st.getPlayer().getLevel() > 89)) {
                st.exitCurrentQuest(true);
                return "30137-00.htm";
            }
            switch (st.getState()) {
                case CREATED:
                    htmltext = "30137-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        if ((st.hasQuestItems(34059)) || (st.hasQuestItems(34060)))
                            htmltext = "30137-09.htm";
                        else
                            htmltext = "30137-06.htm";
                    } else {
                        if (st.getCond() != 2)
                            break;
                        st.takeItems(34059, -1L);
                        st.takeItems(34060, -1L);
                        st.addExpAndSp(145557000, 58119840);
                        st.unset("cond");
                        st.setState(2);
                        st.exitCurrentQuest(false);
                        htmltext = "30137-07.htm";
                    }
                    break;
                case COMPLETED:
                    htmltext = "30137-08.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if (st != null) {
            if ((ArrayUtils.contains(EXTRACT_MOBS, npc.getNpcId())) && (st.getQuestItemsCount(34059) < 20L) && (Rnd.get(100) < DROP_CHANCE))
                st.giveItems(34059, 1L);
            if ((ArrayUtils.contains(BLOOD_MOBS, npc.getNpcId())) && (st.getQuestItemsCount(34060) < 20L) && (Rnd.get(100) < DROP_CHANCE)) {
                st.giveItems(34060, 1L);
            }
            if ((st.getQuestItemsCount(34059) == 20L) && (st.getQuestItemsCount(34060) == 20L)) {
                st.setCond(2);
                st.playSound("ItemSound.quest_middle");
            }
        }
        return null;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return (player.getLevel() >= 85) && (player.getLevel() < 89);
    }
}

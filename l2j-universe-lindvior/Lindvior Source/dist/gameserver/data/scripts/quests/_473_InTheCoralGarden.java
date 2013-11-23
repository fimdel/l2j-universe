/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.11.12
 * Time: 18:55
 */
public class _473_InTheCoralGarden extends Quest implements ScriptFile {
    private static final int NPC_FIOREN = 33044;
    private static final int MOB_MICHAEL = 25799;
    private static final int REWARD_CERTIFICATION_OF_HELL = 30387;

    public _473_InTheCoralGarden() {
        super(PARTY_ALL);

        addStartNpc(NPC_FIOREN);
        addKillId(MOB_MICHAEL);
        addKillNpcWithLog(1, "Michael", 1, MOB_MICHAEL);
        addLevelCheck(97, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }

        if (event.equalsIgnoreCase("33044-04.htm")) {
            st.startQuest();
        } else if (event.equalsIgnoreCase("33044-07.htm")) {
            st.playSound(SOUND_FINISH);
            st.giveItems(REWARD_CERTIFICATION_OF_HELL, 10);
            st.exitCurrentQuest(this);
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";

        if (st == null) {
            return htmltext;
        }

        Player player = st.getPlayer();

        if (npc.getNpcId() == NPC_FIOREN) {
            if (player.getLevel() < 97) {
                st.exitCurrentQuest(true);
                return "33044-02.htm";
            }

            switch (st.getState()) {
                case DELAYED:
                    htmltext = "33044-08.htm";
                    break;
                case CREATED:
                    htmltext = "33044-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33044-05.htm";
                    } else {
                        htmltext = "33044-06.htm";
                    }
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null)) {
            return null;
        }

        if (updateKill(npc, st)) {
            st.setCond(2);
            st.playSound(SOUND_MIDDLE);
            st.unset("Michael");
        }

        return null;
    }

    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_473_InTheCoralGarden.class);

        return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }
}

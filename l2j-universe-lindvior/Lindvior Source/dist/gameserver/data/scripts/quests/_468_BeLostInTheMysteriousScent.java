/*
 * Copyright Mazaffaka Project (c) 2013.
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
 * Time: 18:50
 */
public class _468_BeLostInTheMysteriousScent extends Quest implements ScriptFile {
    private static final int NPC_SELINA = 33032;
    private static final int MOB_GARDEN_COMMANDER = 22962;
    private static final int MOB_MOON_GARDENER = 22958;
    private static final int MOB_MOON_GARDEN_MANAGER = 22960;
    private static final int MOB_GARDEN_PROTECTOR = 22959;
    private static final int REWARD_CERTIFICATE_OF_LIFE = 30385;

    public _468_BeLostInTheMysteriousScent() {
        super(false);

        addStartNpc(NPC_SELINA);
        addKillId(MOB_GARDEN_COMMANDER, MOB_GARDEN_PROTECTOR, MOB_MOON_GARDEN_MANAGER, MOB_MOON_GARDENER);
        addKillNpcWithLog(1, "Protector", 10, MOB_GARDEN_PROTECTOR);
        addKillNpcWithLog(1, "Manager", 10, MOB_MOON_GARDEN_MANAGER);
        addKillNpcWithLog(1, "Gardener", 10, MOB_MOON_GARDENER);
        addKillNpcWithLog(1, "Commander", 10, MOB_GARDEN_COMMANDER);
        addLevelCheck(90, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }

        if (event.equalsIgnoreCase("33032-04.htm")) {
            st.startQuest();
        } else if (event.equalsIgnoreCase("33032-07.htm")) {
            st.giveItems(REWARD_CERTIFICATE_OF_LIFE, 6);
            st.playSound(SOUND_FINISH);
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

        if (npc.getNpcId() == NPC_SELINA) {
            if (player.getLevel() < 90) {
                st.exitCurrentQuest(true);
                return "33032-02.htm";
            }

            switch (st.getState()) {
                case DELAYED:
                    htmltext = "33032-08.htm";
                    break;
                case CREATED:
                    htmltext = "33032-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33032-05.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "33032-06.htm";
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
            st.unset("Protector");
            st.unset("Manager");
            st.unset("Gardener");
            st.unset("Commander");
        }

        return null;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_468_BeLostInTheMysteriousScent.class);

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

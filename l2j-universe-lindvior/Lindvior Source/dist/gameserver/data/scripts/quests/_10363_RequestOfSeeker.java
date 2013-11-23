/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.listener.actor.player.OnSocialActionListener;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.clientpackets.RequestActionUse.Action;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;

public class _10363_RequestOfSeeker extends Quest implements ScriptFile {

    private static final int nagel = 33450;
    private static final int celin = 33451;

    private static final int soul1 = 19157;
    private static final int soul2 = 19158;

    private static final int wooden_helmet = 43;
    private static final int lesser_healing_potion = 1060;

    private ActionListener listener = null;

    public class ActionListener implements OnSocialActionListener {
        @Override
        public void onSocialAction(Player player, GameObject object, Action action) {
            QuestState st = player.getQuestState(_10363_RequestOfSeeker.class);

            // Do not forget to remove this listener
            if (st == null || (st.getCond() < 1 || st.getCond() > 6)) {
                player.removeListener(this);
                return;
            }
            // Corpses only
            if (!object.isNpc() || (((NpcInstance) object).getNpcId() < 32961 || ((NpcInstance) object).getNpcId() > 32964))
                return;

            double distance = player.getDistance(object);
            // Corpses are despawned in any cases
            object.deleteMe();
            // Quest update only when character is not so far from corpse
            if (distance > 50) {
                player.sendPacket(new ExShowScreenMessage(NpcString.YOU_ARE_TOO_FAR_FROM_CORPSE_TO_SHOW_YOUR_CONDOLENCES, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
                return;
            }
            // If player laught - reject
            if (action.equals(Action.ACTION31)) {
                player.sendPacket(new ExShowScreenMessage(NpcString.DONT_TOY_WITH_DEAD, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
                NpcInstance asa = NpcUtils.spawnSingle(soul1, new Location(player.getX() - Rnd.get(100), player.getY() - Rnd.get(100), player.getZ(), 0));
                asa.getAggroList().addDamageHate(st.getPlayer(), 0, 10000);
                asa.setAggressionTarget(player);
                NpcInstance ass = NpcUtils.spawnSingle(soul2, new Location(player.getX() - Rnd.get(100), player.getY() - Rnd.get(100), player.getZ(), 0));
                ass.getAggroList().addDamageHate(st.getPlayer(), 0, 10000);
                ass.setAggressionTarget(player);
                return;
            }
            // And if player do not sorrow - reject
            else if (!action.equals(Action.ACTION35))
                return;

            NpcString message = null;
            switch (st.getCond()) {
                case 1:
                    message = NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_ONE_CORPSE;
                    break;
                case 2:
                    message = NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_SECOND_CORPSE;
                    break;
                case 3:
                    message = NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_THIRD_CORPSE;
                    break;
                case 4:
                    message = NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_FOURTH_CORPSE;
                    break;
                case 5:
                    message = NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_FIFTH_CORPSE;
                    break;
            }
            if (message != null) {
                player.sendPacket(new ExShowScreenMessage(message, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
                st.playSound(SOUND_ITEMGET);
            }
            st.setCond(st.getCond() + 1);
        }
    }

    public _10363_RequestOfSeeker() {
        super(false);
        addStartNpc(nagel);
        addTalkId(celin);

        addLevelCheck(12, 20);
        addQuestCompletedCheck(_10362_CertificationOfSeeker.class);

    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_accept")) {
            htmltext = "nagel_q10363_3.htm";
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
            st.setCond(1);
            if (listener == null) {
                listener = new ActionListener();
                st.getPlayer().addListener(listener);
            }
        }
        // All corpses was "sorrowed"
        else if (st.getCond() == 6 && event.equalsIgnoreCase("request_done")) {
            htmltext = "nagel_q10363_5.htm";
            st.setCond(7);
        } else if (st.getCond() == 7 && event.equalsIgnoreCase("quest_done")) {
            htmltext = "celin_q10363_3.htm";
            st.getPlayer().addExpAndSp(70200, 8100);
            st.giveItems(wooden_helmet, 1);
            st.giveItems(ADENA_ID, 43000);
            st.giveItems(lesser_healing_potion, 100);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        }

        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        int npcId = npc.getNpcId();
        int cond = st.getCond();

        switch (npcId) {
            case nagel:
                if (cond == 0)
                    htmltext = "nagel_q10363_1.htm";
                else if (cond == 6)
                    htmltext = "nagel_q10363_4.htm";
                else if (!isAvailableFor(st.getPlayer()))
                    htmltext = "You need to complete quest Certification of the Seeker"; // TODO: Unknown text here
                else if (cond == 0 && !isAvailableFor(st.getPlayer()))
                    htmltext = "Only characters under level 12 to 20 can accept this quest"; // TODO: Unknown text here
                else if (cond < 6)
                    htmltext = "nagel_q10363_taken.htm";
                else
                    htmltext = "nagel_q10363_taken_2.htm";
                break;
            case celin:
                if (cond == 7)
                    htmltext = "celin_q10363_1.htm";
        }
        return htmltext;
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
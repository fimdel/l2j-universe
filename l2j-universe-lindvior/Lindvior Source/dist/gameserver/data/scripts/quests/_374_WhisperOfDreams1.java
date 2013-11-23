/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.Config;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Util;

public class _374_WhisperOfDreams1 extends Quest implements ScriptFile {
    // Sealed Mysterious Stone chance drop
    private static final double DROP_CHANCE_SEALD_MSTONE = 0.4; //default: ~ 1/250
    // Items chance drop
    private static final int DROP_CHANCE_ITEMS = 20;
    // Rewards
    private static final String[][] SHOP_LIST = {{"etc_leather_yellow_i00", "Sealed Tallum Tunic Textures"},
            {"etc_leather_gray_i00", "Sealed Dark Crystal Robe Fabrics"},
            {"etc_leather_gray_i00", "Sealed Nightmare Robe Fabric"},
            {"etc_leather_gray_i00", "Sealed Majestic Robe Frabrics"},
            {"etc_leather_gray_i00", "Sealed Tallum Stockings Fabrics"}};

    private static final int[][] SHOP_PRICES = {
            //    id,kolvo,adena
            {5485, 4, 10450}, {5486, 3, 2950}, {5487, 2, 18050}, {5488, 2, 18050}, {5489, 6, 15550}};

    // Quest items
    private static final int CB_TOOTH = 5884; //Cave Beast Tooth
    private static final int DW_LIGHT = 5885; //Death Wave Light
    private static final int SEALD_MSTONE = 5886; //Sealed Mysterious Stone
    private static final int MSTONE = 5887; //Mysterious Stone

    private static final String _default = "noquest";

    // NPCs
    private static final int MANAKIA = 30515;
    private static final int TORAI = 30557;

    // Mobs
    private static final int CB = 20620;
    private static final int DW = 20621;

    private String render_shop() {
        String html = "<html><head><body><font color=\"LEVEL\">Robe Armor Fabrics:</font><table border=0 width=300>";
        for (int i = 0; i < SHOP_LIST.length; i++) {
            html += "<tr><td width=35 height=45><img src=icon." + SHOP_LIST[i][0] + " width=32 height=32 align=left></td><td width=365 valign=top><table border=0 width=100%>";
            html += "<tr><td><a action=\"bypass -h Quest _374_WhisperOfDreams1 " + SHOP_PRICES[i][0] + "\"><font color=\"FFFFFF\">" + SHOP_LIST[i][1] + " x" + SHOP_PRICES[i][1] + "</font></a></td></tr>";
            html += "<tr><td><a action=\"bypass -h Quest _374_WhisperOfDreams1 " + SHOP_PRICES[i][0] + "\"><font color=\"B09878\">" + (int) (SHOP_PRICES[i][2] * Config.RATE_QUESTS_REWARD) + " adena</font></a></td></tr></table></td></tr>";
        }
        html += "</table></body></html>";
        return html;
    }

    public _374_WhisperOfDreams1() {
        super(true);

        // Quest NPC starter initialization
        addStartNpc(MANAKIA);
        // Quest initialization
        addTalkId(TORAI);

        addKillId(CB, DW);
        addQuestItem(CB_TOOTH, DW_LIGHT);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (event.equalsIgnoreCase("30515-4.htm")) {
            st.setState(STARTED);
            st.set("cond", "1");
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("30515-5.htm")) {
            st.takeItems(CB_TOOTH, -1);
            st.takeItems(DW_LIGHT, -1);
            st.exitCurrentQuest(true);
        } else if (event.equalsIgnoreCase("30515-6.htm")) {
            if (st.getQuestItemsCount(CB_TOOTH) == 65 && st.getQuestItemsCount(DW_LIGHT) == 65) {
                st.set("allow", "1");
                st.takeItems(CB_TOOTH, -1);
                st.takeItems(DW_LIGHT, -1);
                htmltext = "30515-7.htm";
            }
        } else if (event.equalsIgnoreCase("30515-8.htm")) {
            if (st.getQuestItemsCount(SEALD_MSTONE) > 0 && st.getInt("cond") < 2) {
                st.set("cond", "2");
                htmltext = "30515-9.htm";
            } else if (st.getInt("cond") == 2)
                htmltext = "30515-10.htm";
        } else if (event.equalsIgnoreCase("buy"))
            htmltext = render_shop();
        else if (Util.isNumber(event)) {
            int evt = Integer.parseInt(event);
            for (int[] element : SHOP_PRICES)
                if (evt == element[0]) {
                    int adena = (int) (element[2] * Config.RATE_QUESTS_REWARD);
                    st.giveItems(ADENA_ID, adena);
                    st.giveItems(element[0], element[1]);
                    st.playSound(SOUND_FINISH);
                    st.exitCurrentQuest(true);
                    htmltext = "30515-11.htm";
                    break;
                }
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = _default;
        int id = st.getState();
        int npcid = npc.getNpcId();
        if (npcid == MANAKIA) {
            if (id == CREATED) {
                st.set("cond", "0");
                st.set("allow", "0");
                htmltext = "30515-1.htm";
                if (st.getPlayer().getLevel() < 56) {
                    st.exitCurrentQuest(true);
                    htmltext = "30515-2.htm";
                }
            } else if (st.getInt("allow") == 1)
                htmltext = "30515-3.htm";
            else
                htmltext = "30515-3a.htm";
        } else if (npcid == TORAI)
            if (st.getQuestItemsCount(SEALD_MSTONE) > 0) {
                htmltext = "30557-1.htm";
                st.takeItems(SEALD_MSTONE, 1);
                st.giveItems(MSTONE, 1);
                st.set("cond", "3");
                st.playSound(SOUND_MIDDLE);
            }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcid = npc.getNpcId();
        if (npcid == CB)
            st.rollAndGive(CB_TOOTH, 1, 1, 65, DROP_CHANCE_ITEMS);
        else if (npcid == DW)
            st.rollAndGive(DW_LIGHT, 1, 1, 65, DROP_CHANCE_ITEMS);
        if (st.getState() == STARTED && st.getQuestItemsCount(SEALD_MSTONE) < 1)
            st.rollAndGive(SEALD_MSTONE, 1, 1, DROP_CHANCE_SEALD_MSTONE);
        return null;
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
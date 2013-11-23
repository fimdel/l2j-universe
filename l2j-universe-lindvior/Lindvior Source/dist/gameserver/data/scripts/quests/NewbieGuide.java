package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 03.09.12
 * Time: 5:44
 */
public class NewbieGuide extends Quest {
    private static final int NewbieItem = 32241;
    private static final int NewbieGuide = 33463;

    public NewbieGuide() {
        super(false);
        addStartNpc(NewbieGuide);
        addTalkId(NewbieGuide);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (event.equalsIgnoreCase("rnd"))
            htmltext = "33463-" + Rnd.get(1, 116) + ".htm";
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        if (!st.getPlayer().getVarB("NewbieGuide")) {
            st.giveItems(NewbieItem, 1);
            st.getPlayer().setVar("NewbieGuide", "1", 1);
        }
        return "33463-" + Rnd.get(1, 116) + ".htm";
    }
}

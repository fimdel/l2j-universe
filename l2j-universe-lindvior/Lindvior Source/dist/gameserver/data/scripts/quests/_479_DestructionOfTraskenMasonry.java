package quests;

import gnu.trove.TIntIntHashMap;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExQuestNpcLogList;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.09.12
 * Time: 4:45
 */
public class _479_DestructionOfTraskenMasonry extends Quest implements ScriptFile {
    private static final int _Daichir = 30537;
    private static final int Яйцо = 19080;
    private static final int _sprayA = 17620;
    private static final int _sprayB = 17621;
    private static final int _sprayC = 17622;
    private static final int[] СкилыРаспылителя = {12005, 12006, 12007};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _479_DestructionOfTraskenMasonry() {
        super(false);
        addStartNpc(_Daichir);
        addTalkId(_Daichir);
        addSkillUseId(Яйцо);
        addQuestItem(_sprayA, _sprayB, _sprayC);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equalsIgnoreCase("30537-06.htm")) {
            st.playSound(Quest.SOUND_ACCEPT);
            st.setState(Quest.STARTED);
            st.setCond(1);
            st.giveItems(_sprayA, 30L);
            st.giveItems(_sprayB, 30L);
            st.giveItems(_sprayC, 30L);
        } else if (event.equalsIgnoreCase("30537-12.htm")) {
            if (st.getInt("_1") >= 5) {
                st.giveAdena(993824);
                st.playSound("ItemSound.quest_finish");
                st.setState(COMPLETED);
                st.exitCurrentQuest(false);
            } else {
                return "30537-11.htm";
            }
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";

        if (npc.getNpcId() == _Daichir) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "30537-noday.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 85) {
                        htmltext = "30537-00.htm";
                    } else {
                        htmltext = "30537-nolvl.htm";
                        st.exitCurrentQuest(true);
                    }
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "30537-07.htm";
                    } else {
                        if (st.getCond() != 2)
                            break;
                        htmltext = "30537-10.htm";
                    }
            }
        }

        return htmltext;
    }

    public String onSkillSee(NpcInstance npc, Player caster, Skill skill, GameObject[] targets, boolean isPet) {
        QuestState st = caster.getQuestState("_479_DestructionOfTraskenMasonry");

        if ((npc == null) || (st == null)) {
            return null;
        }

        if ((st.getCond() == 1) && (ArrayUtils.contains(СкилыРаспылителя, skill.getId()))) {
            TIntIntHashMap moblist = new TIntIntHashMap();
            int count = st.getInt("_1");
            if (npc.getNpcId() == Яйцо) {
                count++;
                npc.doDie(caster);
                st.set("_1", String.valueOf(count));
                st.playSound("ItemSound.quest_itemget");
                moblist.put(1019080, count);
                caster.sendPacket(new ExQuestNpcLogList(479, moblist));
                if (count >= 5) {
                    st.playSound("ItemSound.quest_middle");
                    st.setCond(2);
                }
            }
        }
        return null;
    }
}

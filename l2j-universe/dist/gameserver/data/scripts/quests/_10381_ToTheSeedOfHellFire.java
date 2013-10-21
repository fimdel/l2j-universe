package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author KilRoy
 * @name 10381 - To The Seed Of HellFire
 * @category One quest. Single
 * @see http://l2wiki.com/To_the_Seed_of_Hellfire
 */
public class _10381_ToTheSeedOfHellFire extends Quest implements ScriptFile
{
	private final static int KEUCEREUS = 32548;
	private final static int SIZRAK = 33669;
	private final static int KBALDIR = 32733;

	@Override
	public void onLoad()
	{
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	public _10381_ToTheSeedOfHellFire()
	{
		super(false);

		addStartNpc(KEUCEREUS);

		addTalkId(KBALDIR);
		addTalkId(SIZRAK);

		addLevelCheck(97, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;

		if(event.equalsIgnoreCase("quest_accpted"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "kserth_q10381_03.htm";
		}
		if(event.equalsIgnoreCase("quest_next"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			htmltext = "kbarldire_q10381_03.htm";
		}
		if(event.equalsIgnoreCase("quest_done"))
		{
			st.giveItems(ADENA_ID, 3256740);
			st.addExpAndSp(951127800, 435041400);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			htmltext = "sofa_sizraku_q10381_03.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		if(npcId == KEUCEREUS)
		{
			if(st.isCompleted())
			{
				htmltext = "kserth_q10381_05.htm";
			}
			else if(cond == 0 && isAvailableFor(st.getPlayer()))
			{
				htmltext = "kserth_q10381_01.htm";
			}
			else if(cond > 0)
			{
				htmltext = "kserth_q10381_06.htm";
			}
			else
			{
				htmltext = "kserth_q10381_04.htm";
			}
		}
		if(npcId == KBALDIR)
		{
			if(cond == 2)
			{
				htmltext = "kbarldire_q10381_04.htm";
			}
			else if(cond > 0)
			{
				htmltext = "kbarldire_q10381_01.htm";
			}
		}
		if(npcId == SIZRAK)
		{
			if(cond == 2)
			{
				htmltext = "sofa_sizraku_q10381_01.htm";
			}
		}
		return htmltext;
	}
}
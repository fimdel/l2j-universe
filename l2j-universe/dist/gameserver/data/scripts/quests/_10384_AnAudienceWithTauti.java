package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author KilRoy
 * @name 10384 - An Audience With Tauti
 * @category One quest. Party
 * @see http://l2wiki.com/An_Audience_With_Tauti
 */
public class _10384_AnAudienceWithTauti extends Quest implements ScriptFile
{
	private final static int FERGASON = 33681;
	private final static int AKU = 33671;

	private final static int BOTTLE_OF_TAUTI_SOUL = 35295;
	private final static int TAUTI_FRAGMENT = 34960;

	private final static int TAUTI_EXTREME = 29234;

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

	public _10384_AnAudienceWithTauti()
	{
		super(2);

		addStartNpc(FERGASON);

		addTalkId(AKU);
		addTalkId(FERGASON);

		addKillId(TAUTI_EXTREME);

		addQuestItem(TAUTI_FRAGMENT);

		addLevelCheck(97, 99);
		addQuestCompletedCheck(_10383_FergasonsOffer.class);
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
			htmltext = "maestro_ferguson_q10384_04.htm";
		}
		if(event.equalsIgnoreCase("sofa_aku_q10384_02.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			htmltext = "sofa_aku_q10384_02.htm";
		}
		if(event.equalsIgnoreCase("quest_done"))
		{
			st.giveItems(ADENA_ID, 3256740);
			st.giveItems(BOTTLE_OF_TAUTI_SOUL, 1);
			st.addExpAndSp(951127800, 435041400);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			htmltext = "maestro_ferguson_q10384_11.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		if(npcId == FERGASON)
		{
			if(st.isCompleted())
			{
				htmltext = "maestro_ferguson_q10384_07.htm";
			}
			else if(!isAvailableFor(st.getPlayer()))
			{
				htmltext = "maestro_ferguson_q10384_06.htm";
			}
			else if(cond == 0)
			{
				htmltext = "maestro_ferguson_q10384_01.htm";
			}
			else if(cond == 1 | cond == 2)
			{
				htmltext = "maestro_ferguson_q10384_08.htm";
			}
			else if(cond == 3)
			{
				htmltext = "maestro_ferguson_q10384_09.htm";
			}
			else
			{
				htmltext = "maestro_ferguson_q10384_05.htm";
			}
		}
		if(npcId == AKU)
		{
			if(cond == 1)
			{
				htmltext = "sofa_aku_q10384_01.htm";
			}
			else if(cond == 2)
			{
				htmltext = "sofa_aku_q10383_02.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if(cond == 2 && npcId == TAUTI_EXTREME)
		{
			st.giveItems(TAUTI_FRAGMENT, 1);
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
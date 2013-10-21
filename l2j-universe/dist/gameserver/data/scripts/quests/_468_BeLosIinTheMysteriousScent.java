package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author KilRoy
 * @name 468 - Be Lost in the Mysterious Scent
 * @category Daily quest. Party
 * @see http://l2wiki.com/Be_Lost_in_the_Mysterious_Scent
 */
public class _468_BeLosIinTheMysteriousScent extends Quest implements ScriptFile
{
	private static final int SELINA = 33032;

	private static final int GARDEN_COMMANDER = 22962;
	private static final int MOON_GARDEN_MANAGER = 22958;
	private static final int MOON_GARDEN = 22960;
	private static final int GARDEN_PROTECTOR = 22959;

	private static final int CERTIFICATE_OF_LIFE = 30385;

	private static final String GARDEN_COMMANDER_KILL = "commander";
	private static final String MOON_GARDEN_MANAGER_KILL = "manager";
	private static final String MOON_GARDEN_KILL = "moon";
	private static final String GARDEN_PROTECTOR_KILL = "protector";

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _468_BeLosIinTheMysteriousScent()
	{
		super(2);
		addTalkId(SELINA);

		addKillNpcWithLog(1, GARDEN_COMMANDER_KILL, 10, GARDEN_COMMANDER);
		addKillNpcWithLog(1, MOON_GARDEN_MANAGER_KILL, 10, MOON_GARDEN_MANAGER);
		addKillNpcWithLog(1, MOON_GARDEN_KILL, 10, MOON_GARDEN);
		addKillNpcWithLog(1, GARDEN_PROTECTOR_KILL, 10, GARDEN_PROTECTOR);

		addLevelCheck(90, 99);
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
			htmltext = "33032-04.htm";
		}
		if(event.equalsIgnoreCase("quest_done"))
		{
			st.giveItems(CERTIFICATE_OF_LIFE, 2);
			st.exitCurrentQuest(this);
			st.playSound(SOUND_FINISH);
			htmltext = "33032-07.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		if(npcId == SELINA)
		{
			if(st.isCreated() && !st.isNowAvailableByTime())
				htmltext = "33032-08.htm";
			else if(cond == 0 && isAvailableFor(st.getPlayer()))
				htmltext = "33032-01.htm";
			else if(cond == 1)
				htmltext = "33032-05.htm";
			else if(cond == 2)
				htmltext = "33032-06.htm";
			else
				htmltext = "33032-02.htm";
		}
		return htmltext;
	}


	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		boolean doneKill = updateKill(npc, st);

		if(doneKill)
		{
			st.unset(GARDEN_COMMANDER_KILL);
			st.unset(MOON_GARDEN_MANAGER_KILL);
			st.unset(MOON_GARDEN_KILL);
			st.unset(GARDEN_PROTECTOR_KILL);
			st.playSound(SOUND_MIDDLE);
			st.setCond(2);
		}
		return null;
	}
}
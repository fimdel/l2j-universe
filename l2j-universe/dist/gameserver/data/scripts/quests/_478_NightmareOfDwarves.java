package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _478_NightmareOfDwarves extends Quest implements ScriptFile
{
	//npc
	public static final int DAICHIR = 30537;
	
	//mobs
	public static final int TRASKEN = 33159; //is this is the boss?
	
	public static final String WPL = "WPL";
	public static final String BWPL = "BWPL";
	public static final String LCK = "LCK";
	
	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _478_NightmareOfDwarves()
	{
		super(true);
		addStartNpc(DAICHIR);
		addKillId(TRASKEN);
		addKillNpcWithLog(2, WPL, 10, 29199); //TODO[Iqman/Nosferatu] ID is more like guessed
		addKillNpcWithLog(2, BWPL, 10, 29198); //TODO[Iqman/Nosferatu] ID is more like guessed
		addKillNpcWithLog(2, LCK, 10, 29204); //TODO[Iqman/Nosferatu] ID is more like guessed
		addLevelCheck(85, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		if(event.equalsIgnoreCase("30537-6.htm"))
		{
			if(player.getLevel() >= 85 && player.getLevel() <= 94)
				st.setCond(2);
			else
				st.setCond(3);
				
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == DAICHIR)
		{
			if(state == 1)
			{
				if(player.getLevel() < 85)
					return "30537-lvl.htm";
				if(!st.isNowAvailableByTime())
					return "30537-comp.htm";
					
				if(player.getLevel() < 85)
					return "30537-lvl.htm";
					
				return "30537.htm";
			}
			if(state == 2)
			{
				if(cond == 2 || cond == 3)
					return "30537-7.htm";
				if(cond == 4)
				{
					int reward = getReward();
					st.giveItems(reward, 1);
					st.unset("cond");
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
					return "30537-16.htm";	
					
				}
				if(cond == 5)	
				{
					st.giveItems(57, 550000);
					st.unset("cond");
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
					return "30537-16.htm";	
				}		
			}
		}
		return "noquest";
	}
	private static int getReward()
	{
		int chance = Rnd.get(100);
		if(chance > 0 && chance <= 15)
			return 4342;

		if(chance > 15 && chance <= 30)
			return 4343;	

		if(chance > 30 && chance <= 45)
			return 4344;			

		if(chance > 45 && chance <= 60)
			return 4345;

		if(chance > 60 && chance <= 75)
			return 4346;		

		if(chance > 75 && chance <= 90)
			return 4347;

		if(chance > 90 && chance <= 95)
			return 17623;		

		if(chance > 95 && chance <= 100)
			return 15559;	
		return 0;		
			
	}
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if(cond == 2)
		{
			boolean doneKill = updateKill(npc, st);
			if(doneKill)
			{
				st.unset(WPL);
				st.unset(BWPL);
				st.unset(LCK);
				st.setCond(5);
			}
		}
		else if(cond == 3)
		{
			if(npc.getNpcId() == TRASKEN)
				st.setCond(4);
		}	
		return null;
	}	
}
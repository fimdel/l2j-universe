package quests;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _492_TombRaiders extends Quest implements ScriptFile
{
	//npc
	public static final int ZENIA = 32140;
	
	//mobs
	private static final int[] Mobs = {23193, 23194, 23195, 23196};
	
	//q items
	public static final int ANCIENT_REL = 34769;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _492_TombRaiders()
	{
		super(true);
		addStartNpc(ZENIA);
		addKillId(Mobs);
		addQuestItem(ANCIENT_REL);
		addLevelCheck(80,85);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if(event.equalsIgnoreCase("32140-5.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == ZENIA)
		{
			if(state == 1)
			{
				if(!st.isNowAvailableByTime())
					return "32140-comp.htm";
				return "32140.htm";
			}
			if(state == 2)
			{
				if(cond == 1)
					return "32140-6.htm";
				if(cond == 2)
				{
					st.addExpAndSp(25000000, 28500000); //Retail EXP & SP
					st.takeItems(ANCIENT_REL, -1);
					st.unset("cond");
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);	
					return "32140-7.htm";	
				}	
			}
		}		
		return "noquest";
	}
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if(cond != 1 || npc == null)
			return null;
		if(ArrayUtils.contains(Mobs, npc.getNpcId()) && Rnd.chance(75))
		{
			st.giveItems(ANCIENT_REL, 1);
		}	
		if(st.getQuestItemsCount(ANCIENT_REL) >= 50)
			st.setCond(2);
			
		return null;
	}	
}
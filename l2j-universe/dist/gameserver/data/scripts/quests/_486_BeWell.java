package quests;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _486_BeWell extends Quest implements ScriptFile
{
	//npc
	public static final int GUIDE = 33463;
	public static final int ANEKOBI = 31555;
	
	//mobs
	private static final int[] Mobs = {21508, 21509, 21510, 21511, 21512, 21513, 21514, 21515, 21516, 21517, 21518, 21519};
	
	//q items
	public static final int STAKATO_PAN = 19498;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _486_BeWell()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(ANEKOBI);
		addKillId(Mobs);
		addQuestItem(STAKATO_PAN);
		addLevelCheck(70, 74);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if(event.equalsIgnoreCase("33463-3.htm"))
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
		if(npcId == GUIDE)
		{
			if(state == 1)
			{
				if(!st.isNowAvailableByTime())
					return "33463-comp.htm";
				return "33463.htm";
			}
			else if(state == 2)
			{
				if(cond == 1)
					return "33463-4.htm";
				if(cond == 2)
					return "33463-5.htm";
			}
			
		}
		if(npcId == ANEKOBI && state == 2)
		{
			if(cond == 1)
				return "31555-1.htm";
			else if(cond == 2)
			{
				st.addExpAndSp(9009000, 8997060);
				st.takeItems(STAKATO_PAN, -1);
				st.giveItems(57, 353160);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);			
				return "31555.htm"; //no further html do here
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
		if(ArrayUtils.contains(Mobs, npc.getNpcId()) && Rnd.chance(50))
		{
			st.giveItems(STAKATO_PAN, 2);
			st.playSound(SOUND_MIDDLE);
		}	
		if(st.getQuestItemsCount(STAKATO_PAN) >= 80)
			st.setCond(2);
			
		return null;
	}
}
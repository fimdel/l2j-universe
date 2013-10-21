package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 */
public class _460_PreciousResearchMaterial extends Quest implements ScriptFile
{
	private static final int NPC_AMER = 33092;
	private static final int NPC_FILAR = 30535;
	private static final int ITEM_TEREDOR_EGG_FRAGMENT = 17735;
	private static final int[] MOB_EGGS = {18997, 19023};
	private static final int REWARD_PROOF_OF_FIDELITY = 19450;

	public _460_PreciousResearchMaterial()
	{
		super(PARTY_ALL);

		addStartNpc(NPC_AMER);
		addTalkId(NPC_FILAR);
		addKillId(MOB_EGGS);
		addQuestItem(ITEM_TEREDOR_EGG_FRAGMENT);
		addLevelCheck(85, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;

		if(event.equalsIgnoreCase("30535-01.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.takeAllItems(ITEM_TEREDOR_EGG_FRAGMENT);
			st.giveItems(REWARD_PROOF_OF_FIDELITY, 2);
			st.exitCurrentQuest(this);
		}

		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";

		if(npc.getNpcId() == NPC_AMER)
		{
			if(st.getPlayer().getLevel() < 85)
			{
				return "";	//low level
			}

			switch(st.getState())
			{
				case COMPLETED:
					htmltext = "completed";
					break;
				case CREATED:
					if(isAvailableFor(st.getPlayer()))
					{
						htmltext = "33092-00.htm";

						st.setState(STARTED);
						st.playSound(SOUND_ACCEPT);
						st.setCond(1);
					}
					else
					{
						htmltext = "daily";
					}

					break;
				case STARTED:
					if(st.getCond() == 1)
					{
						htmltext = "33092-00.htm";
					}

					break;
			}
		}
		else if(npc.getNpcId() == NPC_FILAR)
		{
			if(st.isStarted() && (st.getCond() == 2))
			{
				htmltext = "30535-00.htm";
			}
		}

		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if((st.getCond() == 1) && Rnd.chance(50))
		{
			st.giveItems(ITEM_TEREDOR_EGG_FRAGMENT, 1);
			st.playSound(SOUND_ITEMGET);

			if(st.getQuestItemsCount(ITEM_TEREDOR_EGG_FRAGMENT) >= 20)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
		}

		return null;
	}

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
}

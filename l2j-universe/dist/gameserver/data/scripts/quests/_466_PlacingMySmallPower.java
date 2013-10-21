package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

/**
 * * @author coldy
 */
public class _466_PlacingMySmallPower extends Quest implements ScriptFile
{
	private static final int NPC_ASTERIOS = 30154;
	private static final int NPC_MIMILEAD = 32895;
	private static final int[] mobsFairy = {22867, 22875, 22883, 22868, 22876, 22884, 22869, 22877, 22885};
	private static final int[] mobsStoppedMutate = {22870, 22878, 22886, 22866, 22874, 22882};
	private static final int MOB_LARGE_COCCON = 32920;
	private static final int ITEM_FAIRY_WING = 17597;
	private static final int ITEM_COCCON_FRAGMENT = 17598;
	private static final int ITEM_KIMERIAN_BREATH = 17599;
	private static final int ITEM_RECIPE_TONIC = 17603;
	private static final int ITEM_TONIC = 17596;
	private static final int REWARD_CERTIFICATE_OF_PROMISE = 30384;

	public _466_PlacingMySmallPower()
	{
		super(false);

		addStartNpc(NPC_ASTERIOS);
		addTalkId(NPC_MIMILEAD);
		addKillId(mobsFairy);
		addKillId(mobsStoppedMutate);
		addKillId(MOB_LARGE_COCCON);
		addQuestItem(ITEM_FAIRY_WING, ITEM_COCCON_FRAGMENT, ITEM_KIMERIAN_BREATH, ITEM_RECIPE_TONIC, ITEM_TONIC);
		addLevelCheck(90, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(st == null)
		{
			return event;
		}

		if((npc.getNpcId() == NPC_ASTERIOS) && (event.equalsIgnoreCase("30154-05.htm")))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if((npc.getNpcId() == NPC_MIMILEAD) && (event.equalsIgnoreCase("32895-03.htm")))
		{
			st.setCond(2);
			st.playSound("ItemSound.quest_middle");
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";

		if(npc.getNpcId() == NPC_ASTERIOS)
		{
			if(player.getLevel() < 90)
			{
				st.exitCurrentQuest(true);
				return "30154-02.htm";
			}

			switch(st.getState())
			{
				case CREATED:
					htmltext = "30154-01.htm";
					break;
				case STARTED:
					switch(st.getCond())
					{
						case 1:
						case 2:
						case 3:
						case 4:
							htmltext = "30154-06.htm";
							break;
						case 5:
							htmltext = "30154-07.htm";

							st.playSound("ItemSound.quest_finish");
							st.takeAllItems(ITEM_TONIC);
							st.giveItems(REWARD_CERTIFICATE_OF_PROMISE, 3);
							st.exitCurrentQuest(this);
					}

					break;
				case DELAYED:
					htmltext = "30154-03.htm";
			}
		}
		else if(npc.getNpcId() == NPC_MIMILEAD)
		{
			if(st.isCompleted())
			{
				return "32895-08.htm";
			}

			switch(st.getCond())
			{
				case 1:
					htmltext = "32895-01.htm";
					break;
				case 2:
					htmltext = "32895-04.htm";
					break;
				case 3:
					st.setCond(4);
					st.giveItems(ITEM_RECIPE_TONIC, 1);
					st.playSound("ItemSound.quest_middle");

					htmltext = "32895-05.htm";
					break;
				case 4:
					if(st.getQuestItemsCount(ITEM_TONIC) != 5)
					{
						htmltext = "32895-06.htm";
					}
					else
					{
						st.takeAllItems(ITEM_TONIC);
						st.giveItems(ITEM_TONIC, 1);
						st.setCond(5);
						st.playSound("ItemSound.quest_middle");

						htmltext = "32895-07.htm";
					}

					break;
				case 5:
					htmltext = "32895-07.htm";
			}
		}

		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if((npc == null) || (st == null))
		{
			return null;
		}

		if(st.getCond() == 2)
		{
			if(ArrayUtils.contains(mobsFairy, npc.getNpcId()))
			{
				if(Rnd.chance(20))
				{
					if(st.getQuestItemsCount(ITEM_FAIRY_WING) < 5)
					{
						st.giveItems(ITEM_FAIRY_WING, 1);
					}
				}
			}
			else if(npc.getNpcId() == 32920)
			{
				if(Rnd.chance(50))
				{
					if(st.getQuestItemsCount(ITEM_COCCON_FRAGMENT) < 5)
					{
						st.giveItems(ITEM_COCCON_FRAGMENT, 1);
					}
				}
			}
			else if(ArrayUtils.contains(mobsStoppedMutate, npc.getNpcId()))
			{
				if(Rnd.chance(20))
				{
					if(st.getQuestItemsCount(ITEM_KIMERIAN_BREATH) < 5)
					{
						st.giveItems(ITEM_KIMERIAN_BREATH, 1);
					}
				}
			}

			if((st.getQuestItemsCount(ITEM_FAIRY_WING) >= 5) && (st.getQuestItemsCount(ITEM_COCCON_FRAGMENT) >= 5) && (st.getQuestItemsCount(ITEM_KIMERIAN_BREATH) >= 5))
			{
				st.setCond(3);
				st.playSound("ItemSound.quest_middle");
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

package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

/*
 */
public class _244_FollowMe extends Quest implements ScriptFile
{
	private static final int NPC_ISAEL = 30655;
	private static final int ITEM_TAMLIN_ORC_MARK = 30320;
	private static final int ITEM_MEMORIAL_CRYSTAL = 30321;
	private static final int ITEM_CRYSTAL_C = 1459;
	private static final int REWARD_ACADEMY_CIRCLET = 8181;
	private static final int[] monstersOrcs = new int[]{20601, 20602};
	private static final int[] monstersValley = new int[]{20603, 20604, 20605};
	Player sponsoredPlayer;
	Player sponsorPlayer;
	int sponsorObjId;

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

	public _244_FollowMe()
	{
		super(false);

		addStartNpc(NPC_ISAEL);
		addKillId(monstersOrcs);
		addKillId(monstersValley);
		addFirstTalkId(NPC_ISAEL);
		addQuestItem(ITEM_TAMLIN_ORC_MARK, ITEM_MEMORIAL_CRYSTAL);
		addLevelCheck(40, 50);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();

		if((player.getObjectId() == sponsorObjId) && (event.equals("30655-13.htm")))
		{
			if((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				if(sponsoredPlayer.getInventory().destroyItemByItemId(ITEM_CRYSTAL_C, 55))
				{
					sponsorPlayer.getQuestState(getName()).setCond(3);
					return event;
				}

				return "30655-14.htm";
			}

			return "30655-12.htm";
		}

		if(event.equals("30655-04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equals("30655-07.htm"))
		{
			st.set("talk", "1");
			st.takeAllItems(ITEM_TAMLIN_ORC_MARK);
			st.playSound("ItemSound.quest_middle");
		}

		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";

		if(npc.getNpcId() == NPC_ISAEL)
		{
			switch(st.getState())
			{
				case COMPLETED:
					htmltext = "30655-03.htm";
					break;
				case CREATED:
					if((player.getLevel() > 40) && (player.getLevel() < 50) && (player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
					{
						htmltext = "30655-01.htm";
					}
					else
					{
						htmltext = "30655-02.htm";

						st.exitCurrentQuest(true);
					}

					break;
				case STARTED:
					switch(st.getCond())
					{
						case 1:
							htmltext = "30655-05.htm";
							break;
						case 2:
							if(st.getInt("talk") == 1)
							{
								htmltext = "30655-06.htm";
							}
							else if((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
							{
								sponsorObjId = player.getSponsor();
								sponsoredPlayer = st.getPlayer();

								for(UnitMember unitMember : player.getClan().getAllMembers())
								{
									if(unitMember.getObjectId() == sponsorObjId)
									{
										sponsorPlayer = unitMember.getPlayer();
									}
								}

								if((sponsorPlayer != null) && (sponsorPlayer.isOnline()) && (player.getDistance(sponsorPlayer) < 200))
								{
									htmltext = "30655-10.htm";
								}
								else
								{
									htmltext = "30655-08.htm";
								}
							}
							else
							{
								htmltext = "30655-09.htm";
							}

							break;
						case 3:
							htmltext = "30655-17.htm";

							st.setCond(4);
							st.playSound("ItemSound.quest_middle");
							break;
						case 4:
							htmltext = "30655-18.htm";
							break;
						case 5:
							htmltext = "30655-19.htm";

							st.takeAllItems(ITEM_MEMORIAL_CRYSTAL);
							st.addExpAndSp(606680, 39200);
							st.giveItems(REWARD_ACADEMY_CIRCLET, 1);

							if(player.getPledgeType() == Clan.SUBUNIT_ACADEMY)
							{
								player.getClan().incReputation(100, true, "quest244");
							}

							st.playSound("ItemSound.quest_finish");
							st.exitCurrentQuest(false);
					}
			}
		}

		return htmltext;
	}

	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		if((player.getObjectId() == sponsorObjId) && (npc.getNpcId() == NPC_ISAEL))
		{
			if((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				return "30655-11.htm";
			}
		}

		return "";
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if(st.getState() != STARTED)
		{
			return null;
		}

		Player player = st.getPlayer();

		if(st.getCond() == 1)
		{
			if((ArrayUtils.contains(monstersValley, npc.getNpcId())) && (Rnd.chance(50)))
			{
				st.giveItems(ITEM_TAMLIN_ORC_MARK, 1, true);
				st.playSound("ItemSound.quest_itemget");

				if(st.getQuestItemsCount(ITEM_TAMLIN_ORC_MARK) >= 10)
				{
					st.setCond(2);
					st.playSound("ItemSound.quest_middle");
				}
			}
		}
		else if(st.getCond() == 4)
		{
			if(ArrayUtils.contains(monstersValley, npc.getNpcId()))
			{
				if((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
				{
					if(player.getDistance(sponsorPlayer) <= 400)
					{
						st.giveItems(ITEM_MEMORIAL_CRYSTAL, 1, true);
						st.playSound("ItemSound.quest_itemget");

						if(st.getQuestItemsCount(ITEM_MEMORIAL_CRYSTAL) >= 8)
						{
							st.setCond(5);
							st.playSound("ItemSound.quest_middle");
						}
					}
				}
			}
		}

		return null;
	}
}

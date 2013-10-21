package quests;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;
import instances.TautiNormal;

/**
 * @author KilRoy
 * @name 10382 - Day of Liberation
 * @category One quest. Party
 * @see http://l2wiki.com/Day_of_Liberation
 */
public class _10382_DayOfLiberation extends Quest implements ScriptFile
{
	private static final int SIZRAK = 33669;
	//private static final int TAUTI_NORMAL = 29233;
	private static final int TAUTI_NORMAL = 29236;
	private static final int TAUTIS_BRACLET = 35293;
	private static final int normalTautiInstanceId = 218;
	private static final int MARK_OF_THE_RESISTANCE = 34909;

	private static final String TAUTI_KILL = "tauti";

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

	public _10382_DayOfLiberation()
	{
		super(2);

		addStartNpc(SIZRAK);

		addTalkId(SIZRAK);

		addKillId(TAUTI_NORMAL);

		addKillNpcWithLog(1, TAUTI_KILL, 1, TAUTI_NORMAL);

		addLevelCheck(97, 99);
		addQuestCompletedCheck(_10381_ToTheSeedOfHellFire.class);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();

		if(event.equalsIgnoreCase("quest_accpted"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "sofa_sizraku_q10382_03.htm";
		}
		if(event.equalsIgnoreCase("enter_instance"))
		{
			if(player.getInventory().getItemByItemId(MARK_OF_THE_RESISTANCE) != null)
			{
				Reflection r = player.getActiveReflection();
				if(r != null)
				{
					if(player.canReenterInstance(normalTautiInstanceId))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if(player.canEnterInstance(normalTautiInstanceId))
				{
					ReflectionUtils.enterReflection(player, new TautiNormal(), normalTautiInstanceId);
				}
				return "";
			}
			else
			{
				htmltext = "sofa_sizraku_q10382_07.htm";
			}
		}
		if(event.equalsIgnoreCase("quest_done"))
		{
			st.giveItems(ADENA_ID, 3256740);
			st.giveItems(TAUTIS_BRACLET, 1);
			st.addExpAndSp(951127800, 435041400);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			htmltext = "sofa_sizraku_q10382_10.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		if(npcId == SIZRAK)
		{
			if(st.isCompleted())
			{
				htmltext = "sofa_sizraku_q10382_06.htm";
			}
			else if(!isAvailableFor(st.getPlayer()))
			{
				htmltext = "sofa_sizraku_q10382_05.htm";
			}
			else if(cond == 0)
			{
				htmltext = "sofa_sizraku_q10382_01.htm";
			}
			else if(cond == 1)
			{
				htmltext = "sofa_sizraku_q10382_08.htm";
			}
			else if(cond == 2)
			{
				htmltext = "sofa_sizraku_q10382_09.htm";
			}
			else
			{
				htmltext = "sofa_sizraku_q10382_04.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		boolean doneKill = updateKill(npc, st);

		if(doneKill)
		{
			st.unset(TAUTI_KILL);
			st.playSound(SOUND_MIDDLE);
			st.setCond(2);
		}
		return null;
	}
}
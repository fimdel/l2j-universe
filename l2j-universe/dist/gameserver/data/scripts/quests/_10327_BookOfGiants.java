/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.ReflectionUtils;

public class _10327_BookOfGiants extends Quest implements ScriptFile
{
	private static final int panteleon = 32972;
	private static final int table = 33126;
	private static final int assasin = 23121;
	private static final int tairen = 33004;
	private static final int book = 17575;
	private NpcInstance Tairen = null;
	private int killedassasin = 0;
	private static final int INSTANCE_ID = 182;
	private int bookDeskObjectId = 0;
	private boolean bookTaken = false;
	
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
	
	public _10327_BookOfGiants()
	{
		super(false);
		addStartNpc(panteleon);
		addTalkId(panteleon);
		addFirstTalkId(table);
		addQuestItem(book);
		addSkillUseId(assasin);
		addFirstTalkId(tairen);
		addKillId(assasin);
		addAttackId(assasin);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(_10326_RespectYourElders.class);
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState st) // Fix for Dwarves (no active skill until lv10)
	{
		int npcId = npc.getNpcId();
		Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.ENOUGH_OF_THIS_COME_AT_ME, ChatType.NPC_SAY);
		if (npcId == assasin)
		{
			if (Tairen != null)
			{
				Tairen.getAggroList().addDamageHate(npc, 0, 5000);
			}
			if (killedassasin >= 1)
			{
				st.setCond(3);
				st.cancelQuestTimer("attak");
				st.playSound(SOUND_MIDDLE);
				killedassasin = 0;
			}
			else
			{
				killedassasin++;
			}
		}
		return null;
	}
	
	private void enterInstance(Player player)
	{
		Reflection reflection = player.getActiveReflection();
		if (reflection != null)
		{
			if (player.canReenterInstance(INSTANCE_ID))
			{
				player.teleToLocation(reflection.getTeleportLoc(), reflection);
			}
		}
		else if (player.canEnterInstance(INSTANCE_ID))
		{
			ReflectionUtils.enterReflection(player, INSTANCE_ID);
		}
		List<NpcInstance> desks = player.getActiveReflection().getAllByNpcId(table, true);
		double seed = Math.random();
		int counter = 0;
		for (NpcInstance desk : desks)
		{
			if (((seed <= 0.25) && (counter == 0)) || ((seed > 0.25) && (seed <= 0.5) && (counter == 1)) || ((seed > 0.5) && (seed <= 0.75) && (counter == 2)) || ((seed > 0.75) && (counter == 3)))
			{
				bookDeskObjectId = desk.getObjectId();
			}
			++counter;
		}
		if ((bookDeskObjectId == 0) && (desks.size() > 0))
		{
			bookDeskObjectId = desks.get(0).getObjectId();
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-3.htm";
		}
		if (event.equalsIgnoreCase("enter_museum"))
		{
			player.teleToLocation(-114360, 260184, -1224);
			return null;
		}
		if (event.equalsIgnoreCase("enter_instance"))
		{
			enterInstance(st.getPlayer());
			st.playSound(SOUND_MIDDLE);
			bookTaken = false;
			Tairen = st.getPlayer().getActiveReflection().getAllByNpcId(tairen, true).get(0);
			if (Tairen != null)
			{
				Tairen.setRunning();
			}
			return null;
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.ACCESSORIES_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
			htmltext = "0-5.htm";
			st.getPlayer().addExpAndSp(7800, 3500);
			st.giveItems(57, 16000);
			st.giveItems(112, 2);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		if (event.equalsIgnoreCase("attak"))
		{
			htmltext = "";
			st.startQuestTimer("attak", 5000);
			if (Tairen != null)
			{
				Tairen.moveToLocation(st.getPlayer().getLoc(), Rnd.get(0, 100), true);
			}
			{
				if (Rnd.chance(33))
				{
					Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.LOOKS_LIKE_ONLY_SKILL_BASED_ATTACKS_DAMAGE_THEM, ChatType.NPC_SAY);
				}
				if (Rnd.chance(33))
				{
					Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.YOUR_NORMAL_ATTACKS_ARENT_WORKING, ChatType.NPC_SAY);
				}
				if (Rnd.chance(33))
				{
					Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.USE_YOUR_SKILL_ATTACKS_AGAINST_THEM, ChatType.NPC_SAY);
				}
			}
		}
		if (event.equalsIgnoreCase("spawnas"))
		{
			htmltext = "";
			NpcInstance asa = NpcUtils.spawnSingle(assasin, new Location(-114815, 244966, -7976, 0), player.getActiveReflection());
			NpcInstance ass = NpcUtils.spawnSingle(assasin, new Location(-114554, 244954, -7976, 0), player.getActiveReflection());
			Functions.npcSayToPlayer(ass, st.getPlayer(), NpcString.FINALLY_I_THOUGHT_I_WAS_GOING_TO_DIE_WAITING, ChatType.NPC_SAY);
			asa.getAggroList().addDamageHate(st.getPlayer(), 0, 10000);
			asa.setAggressionTarget(player);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == panteleon)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(book) >= 1))
			{
				htmltext = "0-4.htm";
			}
			else if (cond == 2)
			{
				htmltext = "0-3.htm";
				st.setCond(1);
				st.takeAllItems(book);
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		String htmltext = "3-4.htm";
		QuestState st = player.getQuestState(getClass());
		if (st == null)
		{
			return htmltext;
		}
		int npcId = npc.getNpcId();
		if (npcId == table)
		{
			if ((npc.getObjectId() == bookDeskObjectId) && !bookTaken)
			{
				bookTaken = true;
				player.sendPacket(new ExShowScreenMessage(NpcString.WATCH_OUT_YOU_ARE_BEING_ATTACKED, 4500, ScreenMessageAlign.TOP_CENTER));
				htmltext = "2-2.htm";
				st.takeAllItems(book);
				st.giveItems(book, 1, false);
				st.setCond(2);
				st.startQuestTimer("attak", 5000);
				st.startQuestTimer("spawnas", 50);
			}
			else
			{
				htmltext = "2-1.htm";
			}
		}
		if (npcId == tairen)
		{
			htmltext = "3-4.htm";
			if (st.getCond() == 1)
			{
				htmltext = "3-1.htm";
			}
			else if (st.getCond() == 2)
			{
				htmltext = "3-2.htm";
			}
			else if (st.getCond() == 3)
			{
				htmltext = "3-3.htm";
			}
		}
		return htmltext;
	}
}

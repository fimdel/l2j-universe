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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;

public class _727_HopewithintheDarkness extends Quest implements ScriptFile
{
	private static int KnightsEpaulette = 9912;
	private static int KanadisGuide3 = 25661;
	
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
	
	public _727_HopewithintheDarkness()
	{
		super(true);
		addStartNpc(36403, 36404, 36405, 36406, 36407, 36408, 36409, 36410, 36411);
		addKillId(KanadisGuide3);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		Player player = st.getPlayer();
		if (event.equals("dcw_q727_4.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("reward") && (cond == 1) && player.getVar("q727").equalsIgnoreCase("done"))
		{
			player.unsetVar("q727");
			st.giveItems(KnightsEpaulette, 300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
			return null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		Player player = st.getPlayer();
		QuestState qs726 = player.getQuestState(_726_LightwithintheDarkness.class);
		if (!check(st.getPlayer()))
		{
			st.exitCurrentQuest(true);
			return "dcw_q727_1a.htm";
		}
		if (qs726 != null)
		{
			st.exitCurrentQuest(true);
			return "dcw_q727_1b.htm";
		}
		else if (cond == 0)
		{
			if (st.getPlayer().getLevel() >= 70)
			{
				htmltext = "dcw_q727_1.htm";
			}
			else
			{
				htmltext = "dcw_q727_0.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			if ((player.getVar("q727") != null) && player.getVar("q727").equalsIgnoreCase("done"))
			{
				htmltext = "dcw_q727_6.htm";
			}
			else
			{
				htmltext = "dcw_q727_5.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		Party party = player.getParty();
		if ((cond == 1) && (npcId == KanadisGuide3) && checkAllDestroyed(KanadisGuide3, player.getReflectionId()))
		{
			if (player.isInParty())
			{
				for (Player member : party.getPartyMembers())
				{
					if (!member.isDead() && member.getParty().isInReflection())
					{
						member.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(5));
						member.setVar("q727", "done", -1);
					}
				}
			}
			player.getReflection().startCollapseTimer(5 * 60 * 1000L);
		}
		return null;
	}
	
	private static boolean checkAllDestroyed(int mobId, int refId)
	{
		for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(mobId, true))
		{
			if (npc.getReflectionId() == refId)
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean check(Player player)
	{
		Castle castle = ResidenceHolder.getInstance().getResidenceByObject(Castle.class, player);
		if (castle == null)
		{
			return false;
		}
		Clan clan = player.getClan();
		if (clan == null)
		{
			return false;
		}
		if (clan.getClanId() != castle.getOwnerId())
		{
			return false;
		}
		return true;
	}
}

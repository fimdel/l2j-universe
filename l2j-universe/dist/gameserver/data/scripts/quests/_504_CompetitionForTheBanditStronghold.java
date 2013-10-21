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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.TimeUtils;

public class _504_CompetitionForTheBanditStronghold extends Quest implements ScriptFile
{
	private static final int MESSENGER = 35437;
	private static final int TARLK_BUGBEAR = 20570;
	private static final int TARLK_BUGBEAR_WARRIOR = 20571;
	private static final int TARLK_BUGBEAR_HIGH_WARRIOR = 20572;
	private static final int TARLK_BASILISK = 20573;
	private static final int ELDER_TARLK_BASILISK = 20574;
	private static final int AMULET = 4332;
	private static final int ALIANCE_TROPHEY = 5009;
	private static final int CONTEST_CERTIFICATE = 4333;
	
	public _504_CompetitionForTheBanditStronghold()
	{
		super(PARTY_ALL);
		addStartNpc(MESSENGER);
		addTalkId(MESSENGER);
		addKillId(TARLK_BUGBEAR);
		addKillId(TARLK_BUGBEAR_WARRIOR);
		addKillId(TARLK_BUGBEAR_HIGH_WARRIOR);
		addKillId(TARLK_BASILISK);
		addKillId(ELDER_TARLK_BASILISK);
		addQuestItem(CONTEST_CERTIFICATE, AMULET, ALIANCE_TROPHEY);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmlText = event;
		if (event.equalsIgnoreCase("azit_messenger_q0504_02.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.giveItems(CONTEST_CERTIFICATE, 1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmlText;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		Player player = st.getPlayer();
		Clan clan = player.getClan();
		ClanHall clanhall = ResidenceHolder.getInstance().getResidence(35);
		if (clanhall.getSiegeEvent().isRegistrationOver())
		{
			htmltext = null;
			showHtmlFile(player, "azit_messenger_q0504_03.htm", false, "%siege_time%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate()));
		}
		else if ((clan == null) || (player.getObjectId() != clan.getLeaderId()))
		{
			htmltext = "azit_messenger_q0504_05.htm";
		}
		else if ((player.getObjectId() == clan.getLeaderId()) && (clan.getLevel() < 4))
		{
			htmltext = "azit_messenger_q0504_04.htm";
		}
		else if (clanhall.getSiegeEvent().getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) != null)
		{
			htmltext = "azit_messenger_q0504_06.htm";
		}
		else if (clan.getHasHideout() > 0)
		{
			htmltext = "azit_messenger_q0504_10.htm";
		}
		else
		{
			if (cond == 0)
			{
				htmltext = "azit_messenger_q0504_01.htm";
			}
			else if ((st.getQuestItemsCount(CONTEST_CERTIFICATE) == 1) && (st.getQuestItemsCount(AMULET) < 30))
			{
				htmltext = "azit_messenger_q0504_07.htm";
			}
			else if (st.getQuestItemsCount(ALIANCE_TROPHEY) >= 1)
			{
				htmltext = "azit_messenger_q0504_07a.htm";
			}
			else if ((st.getQuestItemsCount(CONTEST_CERTIFICATE) == 1) && (st.getQuestItemsCount(AMULET) == 30))
			{
				st.takeItems(AMULET, -1);
				st.takeItems(CONTEST_CERTIFICATE, -1);
				st.giveItems(ALIANCE_TROPHEY, 1);
				st.playSound(SOUND_FINISH);
				st.setCond(-1);
				htmltext = "azit_messenger_q0504_08.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getQuestItemsCount(AMULET) < 30)
		{
			st.giveItems(AMULET, 1);
			st.playSound(SOUND_ITEMGET);
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

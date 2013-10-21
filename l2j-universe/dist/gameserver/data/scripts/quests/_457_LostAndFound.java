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

import java.util.concurrent.ScheduledFuture;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _457_LostAndFound extends Quest implements ScriptFile
{
	private ScheduledFuture<?> _followTask;
	
	public _457_LostAndFound()
	{
		super(true);
		addStartNpc(32759);
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("lost_villager_q0457_06.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound("ItemSound.quest_accept");
			
			npc.setFollowTarget(st.getPlayer());
			if (_followTask != null)
			{
				_followTask.cancel(false);
				_followTask = null;
			}
			_followTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Follow(npc, player, st), 0L, 1000L);
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
		if (npcId == 32759)
		{
			if (state == 1)
			{
				if ((npc.getFollowTarget() != null) && (npc.getFollowTarget() != player))
				{
					return "lost_villager_q0457_01a.htm";
				}
				if (st.getPlayer().getLevel() >= 82)
				{
					if (st.isNowAvailableByTime())
					{
						return "lost_villager_q0457_01.htm";
					}
					return "lost_villager_q0457_02.htm";
				}
				return "lost_villager_q0457_03.htm";
			}
			if (state == 2)
			{
				if ((npc.getFollowTarget() != null) && (npc.getFollowTarget() != player))
				{
					return "lost_villager_q0457_01a.htm";
				}
				if (cond == 1)
				{
					return "lost_villager_q0457_08.htm";
				}
				if (cond == 2)
				{
					npc.deleteMe();
					
					st.giveItems(15716, 1L);
					st.unset("cond");
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(this);
					return "lost_villager_q0457_09.htm";
				}
			}
		}
		return "noquest";
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	void checkInRadius(int id, QuestState st, NpcInstance npc)
	{
		NpcInstance quest0457 = GameObjectsStorage.getByNpcId(id);
		if (npc.getRealDistance3D(quest0457) <= 150.0D)
		{
			st.setCond(2);
			if (_followTask != null)
			{
				_followTask.cancel(false);
			}
			_followTask = null;
			npc.stopMove();
		}
	}
	
	private class Follow implements Runnable
	{
		private final NpcInstance _npc;
		private final Player player;
		private final QuestState st;
		
		Follow(NpcInstance npc, Player pl, QuestState _st)
		{
			_npc = npc;
			player = pl;
			st = _st;
		}
		
		@Override
		public void run()
		{
			_npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, player, Integer.valueOf(150));
			checkInRadius(32764, st, _npc);
		}
	}
}
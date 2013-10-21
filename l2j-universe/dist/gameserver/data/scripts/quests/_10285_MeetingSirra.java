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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class _10285_MeetingSirra extends Quest implements ScriptFile
{
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Jinia2 = 32781;
	private static final int Kegor = 32761;
	private static final int Sirra = 32762;
	
	public _10285_MeetingSirra()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Jinia2, Kegor, Sirra);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("rafforty_q10285_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("enterinstance"))
		{
			if (st.getCond() == 1)
			{
				st.setCond(2);
			}
			enterInstance(st.getPlayer(), 141);
			return null;
		}
		else if (event.equalsIgnoreCase("jinia_q10285_02.htm"))
		{
			st.setCond(3);
		}
		else if (event.equalsIgnoreCase("kegor_q10285_02.htm"))
		{
			st.setCond(4);
		}
		else if (event.equalsIgnoreCase("sirraspawn"))
		{
			st.setCond(5);
			st.getPlayer().getReflection().addSpawnWithoutRespawn(Sirra, new Location(-23848, -8744, -5413, 49152), 0);
			for (NpcInstance sirra : st.getPlayer().getAroundNpc(1000, 100))
			{
				if (sirra.getNpcId() == Sirra)
				{
					Functions.npcSay(sirra, "Вас послушать, получается, что Вы знаете обо всем на свете. Но я больше не могу слушать Ваши мудрствования");
				}
			}
			return null;
		}
		else if (event.equalsIgnoreCase("sirra_q10285_07.htm"))
		{
			st.setCond(6);
			for (NpcInstance sirra : st.getPlayer().getAroundNpc(1000, 100))
			{
				if (sirra.getNpcId() == 32762)
				{
					sirra.deleteMe();
				}
			}
		}
		else if (event.equalsIgnoreCase("jinia_q10285_10.htm"))
		{
			if (!st.getPlayer().getReflection().isDefault())
			{
				st.getPlayer().getReflection().startCollapseTimer(60 * 1000L);
				st.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(1));
			}
			st.setCond(7);
		}
		else if (event.equalsIgnoreCase("exitinstance"))
		{
			st.getPlayer().getReflection().collapse();
			return null;
		}
		else if (event.equalsIgnoreCase("enterfreya"))
		{
			st.setCond(9);
			enterInstance(st.getPlayer(), 137);
			return null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Rafforty)
		{
			if (cond == 0)
			{
				QuestState qs = st.getPlayer().getQuestState(_10284_AcquisionOfDivineSword.class);
				if ((st.getPlayer().getLevel() >= 82) && (qs != null) && qs.isCompleted())
				{
					htmltext = "rafforty_q10285_01.htm";
				}
				else
				{
					htmltext = "rafforty_q10285_00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond >= 1) && (cond < 7))
			{
				htmltext = "rafforty_q10285_03.htm";
			}
			else if (cond == 10)
			{
				htmltext = "rafforty_q10285_04.htm";
				st.giveItems(ADENA_ID, 283425);
				st.addExpAndSp(939075, 83855);
				st.setState(COMPLETED);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if (npcId == Jinia)
		{
			if (cond == 2)
			{
				htmltext = "jinia_q10285_01.htm";
			}
			else if (cond == 4)
			{
				htmltext = "jinia_q10285_03.htm";
			}
			else if (cond == 6)
			{
				htmltext = "jinia_q10285_05.htm";
			}
			else if (cond == 7)
			{
				htmltext = "jinia_q10285_10.htm";
			}
		}
		else if (npcId == Kegor)
		{
			if (cond == 3)
			{
				htmltext = "kegor_q10285_01.htm";
			}
		}
		else if (npcId == Sirra)
		{
			if (cond == 5)
			{
				htmltext = "sirra_q10285_01.htm";
			}
		}
		else if (npcId == Jinia2)
		{
			if ((cond == 7) || (cond == 8))
			{
				st.setCond(8);
				htmltext = "jinia2_q10285_01.htm";
			}
			else if (cond == 9)
			{
				htmltext = "jinia2_q10285_02.htm";
			}
		}
		return htmltext;
	}
	
	private void enterInstance(Player player, int izId)
	{
		Reflection r = player.getActiveReflection();
		if (r != null)
		{
			if (player.canReenterInstance(izId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(izId))
		{
			Reflection newInstance = ReflectionUtils.enterReflection(player, izId);
			if (izId == 137)
			{
				ThreadPoolManager.getInstance().schedule(new FreyaSpawn(newInstance, player), 2 * 60 * 1000L);
			}
		}
	}
	
	private class FreyaSpawn extends RunnableImpl
	{
		private final Player _player;
		private final Reflection _r;
		
		public FreyaSpawn(Reflection r, Player player)
		{
			_r = r;
			_player = player;
		}
		
		@Override
		public void runImpl()
		{
			if (_r != null)
			{
				NpcInstance freya = _r.addSpawnWithoutRespawn(18847, new Location(114720, -117085, -11088, 15956), 0);
				ThreadPoolManager.getInstance().schedule(new FreyaMovie(_player, _r, freya), 2 * 60 * 1000L);
			}
		}
	}
	
	private class FreyaMovie extends RunnableImpl
	{
		Player _player;
		Reflection _r;
		NpcInstance _npc;
		
		public FreyaMovie(Player player, Reflection r, NpcInstance npc)
		{
			_player = player;
			_r = r;
			_npc = npc;
		}
		
		@Override
		public void runImpl()
		{
			for (Spawner sp : _r.getSpawns())
			{
				sp.deleteAll();
			}
			if ((_npc != null) && !_npc.isDead())
			{
				_npc.deleteMe();
			}
			_player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_FORCED_DEFEAT);
			ThreadPoolManager.getInstance().schedule(new ResetInstance(_player, _r), 23000L);
		}
	}
	
	private class ResetInstance extends RunnableImpl
	{
		Player _player;
		Reflection _r;
		
		public ResetInstance(Player player, Reflection r)
		{
			_player = player;
			_r = r;
		}
		
		@Override
		public void runImpl()
		{
			_player.getQuestState(_10285_MeetingSirra.class).setCond(10);
			_r.collapse();
		}
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

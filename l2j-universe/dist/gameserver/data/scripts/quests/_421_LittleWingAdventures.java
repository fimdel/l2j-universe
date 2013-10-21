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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.PetDataTable.L2Pet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public class _421_LittleWingAdventures extends Quest implements ScriptFile
{
	private static int Cronos = 30610;
	private static int Mimyu = 30747;
	private static int Fairy_Tree_of_Wind = 27185;
	private static int Fairy_Tree_of_Star = 27186;
	private static int Fairy_Tree_of_Twilight = 27187;
	private static int Fairy_Tree_of_Abyss = 27188;
	static int Soul_of_Tree_Guardian = 27189;
	private static int Dragonflute_of_Wind = L2Pet.HATCHLING_WIND.getControlItemId();
	private static int Dragonflute_of_Star = L2Pet.HATCHLING_STAR.getControlItemId();
	private static int Dragonflute_of_Twilight = L2Pet.HATCHLING_TWILIGHT.getControlItemId();
	private static int Dragon_Bugle_of_Wind = L2Pet.STRIDER_WIND.getControlItemId();
	private static int Dragon_Bugle_of_Star = L2Pet.STRIDER_STAR.getControlItemId();
	private static int Dragon_Bugle_of_Twilight = L2Pet.STRIDER_TWILIGHT.getControlItemId();
	private static int Fairy_Leaf = 4325;
	private static int Min_Fairy_Tree_Attaks = 110;
	
	public _421_LittleWingAdventures()
	{
		super(false);
		addStartNpc(Cronos);
		addTalkId(Mimyu);
		addKillId(Fairy_Tree_of_Wind);
		addKillId(Fairy_Tree_of_Star);
		addKillId(Fairy_Tree_of_Twilight);
		addKillId(Fairy_Tree_of_Abyss);
		addAttackId(Fairy_Tree_of_Wind);
		addAttackId(Fairy_Tree_of_Star);
		addAttackId(Fairy_Tree_of_Twilight);
		addAttackId(Fairy_Tree_of_Abyss);
		addQuestItem(Fairy_Leaf);
	}
	
	private static ItemInstance GetDragonflute(QuestState st)
	{
		List<ItemInstance> Dragonflutes = new ArrayList<>();
		for (ItemInstance item : st.getPlayer().getInventory().getItems())
		{
			if ((item != null) && ((item.getItemId() == Dragonflute_of_Wind) || (item.getItemId() == Dragonflute_of_Star) || (item.getItemId() == Dragonflute_of_Twilight)))
			{
				Dragonflutes.add(item);
			}
		}
		if (Dragonflutes.isEmpty())
		{
			return null;
		}
		if (Dragonflutes.size() == 1)
		{
			return Dragonflutes.get(0);
		}
		if (st.getState() == CREATED)
		{
			return null;
		}
		int dragonflute_id = st.getInt("dragonflute");
		for (ItemInstance item : Dragonflutes)
		{
			if (item.getObjectId() == dragonflute_id)
			{
				return item;
			}
		}
		return null;
	}
	
	private static boolean HatchlingSummoned(QuestState st, boolean CheckObjID)
	{
		Summon _pet = st.getPlayer().getSummonList().getPet();
		if (_pet == null)
		{
			return false;
		}
		if (CheckObjID)
		{
			int dragonflute_id = st.getInt("dragonflute");
			if (dragonflute_id == 0)
			{
				return false;
			}
			if (_pet.getControlItemObjId() != dragonflute_id)
			{
				return false;
			}
		}
		ItemInstance dragonflute = GetDragonflute(st);
		if (dragonflute == null)
		{
			return false;
		}
		if (PetDataTable.getControlItemId(_pet.getNpcId()) != dragonflute.getItemId())
		{
			return false;
		}
		return true;
	}
	
	private static boolean CheckTree(QuestState st, int Fairy_Tree_id)
	{
		return st.getInt(String.valueOf(Fairy_Tree_id)) == 1000000;
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int _state = st.getState();
		ItemInstance dragonflute = GetDragonflute(st);
		int dragonflute_id = st.getInt("dragonflute");
		int cond = st.getCond();
		if (event.equalsIgnoreCase("30610_05.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if ((event.equalsIgnoreCase("30747_03.htm") || event.equalsIgnoreCase("30747_04.htm")) && (_state == STARTED) && (cond == 1))
		{
			if (dragonflute == null)
			{
				return "noquest";
			}
			if (dragonflute.getObjectId() != dragonflute_id)
			{
				if (Rnd.chance(10))
				{
					st.takeItems(dragonflute.getItemId(), 1);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
				return "30747_00.htm";
			}
			if (!HatchlingSummoned(st, false))
			{
				return event.equalsIgnoreCase("30747_04.htm") ? "30747_04a.htm" : "30747_02.htm";
			}
			if (event.equalsIgnoreCase("30747_04.htm"))
			{
				st.setCond(2);
				st.takeItems(Fairy_Leaf, -1);
				st.giveItems(Fairy_Leaf, 4);
				st.playSound(SOUND_MIDDLE);
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int _state = st.getState();
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		ItemInstance dragonflute = GetDragonflute(st);
		int dragonflute_id = st.getInt("dragonflute");
		if (_state == CREATED)
		{
			if (npcId != Cronos)
			{
				return "noquest";
			}
			if (st.getPlayer().getLevel() < 45)
			{
				st.exitCurrentQuest(true);
				return "30610_01.htm";
			}
			if (dragonflute == null)
			{
				st.exitCurrentQuest(true);
				return "30610_02.htm";
			}
			if (dragonflute.getEnchantLevel() < 55)
			{
				st.exitCurrentQuest(true);
				return "30610_03.htm";
			}
			st.setCond(0);
			st.set("dragonflute", String.valueOf(dragonflute.getObjectId()));
			return "30610_04.htm";
		}
		if (_state != STARTED)
		{
			return "noquest";
		}
		if (npcId == Cronos)
		{
			if (dragonflute == null)
			{
				return "30610_02.htm";
			}
			return dragonflute.getObjectId() == dragonflute_id ? "30610_07.htm" : "30610_06.htm";
		}
		if (npcId == Mimyu)
		{
			if ((st.getQuestItemsCount(Dragon_Bugle_of_Wind) + st.getQuestItemsCount(Dragon_Bugle_of_Star) + st.getQuestItemsCount(Dragon_Bugle_of_Twilight)) > 0)
			{
				return "30747_00b.htm";
			}
			if (dragonflute == null)
			{
				return "noquest";
			}
			if (cond == 1)
			{
				return "30747_01.htm";
			}
			if (cond == 2)
			{
				if (!HatchlingSummoned(st, false))
				{
					return "30747_09.htm";
				}
				if (st.getQuestItemsCount(Fairy_Leaf) == 0)
				{
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "30747_11.htm";
				}
				return "30747_10.htm";
			}
			if (cond == 3)
			{
				if (dragonflute.getObjectId() != dragonflute_id)
				{
					return "30747_00a.htm";
				}
				if (st.getQuestItemsCount(Fairy_Leaf) > 0)
				{
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "30747_11.htm";
				}
				if (!(CheckTree(st, Fairy_Tree_of_Wind) && CheckTree(st, Fairy_Tree_of_Star) && CheckTree(st, Fairy_Tree_of_Twilight) && CheckTree(st, Fairy_Tree_of_Abyss)))
				{
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "30747_11.htm";
				}
				if (st.getInt("welldone") == 0)
				{
					if (!HatchlingSummoned(st, false))
					{
						return "30747_09.htm";
					}
					st.set("welldone", "1");
					return "30747_12.htm";
				}
				if (HatchlingSummoned(st, false) || (st.getPlayer().getSummonList() != null))
				{
					return "30747_13a.htm";
				}
				dragonflute.setItemId((Dragon_Bugle_of_Wind + dragonflute.getItemId()) - Dragonflute_of_Wind);
				dragonflute.setJdbcState(JdbcEntityState.UPDATED);
				dragonflute.update();
				st.getPlayer().sendPacket(new InventoryUpdate().addModifiedItem(dragonflute));
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
				return "30747_13.htm";
			}
		}
		return "noquest";
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState st)
	{
		if ((st.getState() != STARTED) || (st.getCond() != 2) || !HatchlingSummoned(st, true) || (st.getQuestItemsCount(Fairy_Leaf) == 0))
		{
			return null;
		}
		String npcID = String.valueOf(npc.getNpcId());
		Integer attaked_times = st.getInt(npcID);
		if (CheckTree(st, npc.getNpcId()))
		{
			return null;
		}
		if (attaked_times > Min_Fairy_Tree_Attaks)
		{
			st.set(npcID, "1000000");
			Functions.npcSay(npc, "Give me the leaf!");
			st.takeItems(Fairy_Leaf, 1);
			if (CheckTree(st, Fairy_Tree_of_Wind) && CheckTree(st, Fairy_Tree_of_Star) && CheckTree(st, Fairy_Tree_of_Twilight) && CheckTree(st, Fairy_Tree_of_Abyss))
			{
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		else
		{
			st.set(npcID, String.valueOf(attaked_times + 1));
		}
		return null;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		ThreadPoolManager.getInstance().schedule(new GuardiansSpawner(npc, st, Rnd.get(15, 20)), 1000);
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
	
	public class GuardiansSpawner extends RunnableImpl
	{
		private SimpleSpawner _spawn = null;
		private String agressor;
		private String agressors_pet = null;
		private List<String> agressors_party = null;
		private int tiks = 0;
		
		public GuardiansSpawner(NpcInstance npc, QuestState st, int _count)
		{
			NpcTemplate template = NpcHolder.getInstance().getTemplate(Soul_of_Tree_Guardian);
			if (template == null)
			{
				return;
			}
			try
			{
				_spawn = new SimpleSpawner(template);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			for (int i = 0; i < _count; i++)
			{
				_spawn.setLoc(Location.findPointToStay(npc, 50, 200));
				_spawn.setHeading(Rnd.get(0, 0xFFFF));
				_spawn.setAmount(1);
				_spawn.doSpawn(true);
				agressor = st.getPlayer().getName();
				if (st.getPlayer().getSummonList().getPet() != null)
				{
					agressors_pet = st.getPlayer().getSummonList().getPet().getName();
				}
				if (st.getPlayer().getParty() != null)
				{
					agressors_party = new ArrayList<>();
					for (Player _member : st.getPlayer().getParty().getPartyMembers())
					{
						if (!_member.equals(st.getPlayer()))
						{
							agressors_party.add(_member.getName());
						}
					}
				}
			}
			_spawn.stopRespawn();
			updateAgression();
		}
		
		private void AddAgression(Playable player, int aggro)
		{
			if (player == null)
			{
				return;
			}
			for (NpcInstance mob : _spawn.getAllSpawned())
			{
				mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, aggro);
			}
		}
		
		private void updateAgression()
		{
			Player _player = World.getPlayer(agressor);
			if (_player != null)
			{
				if ((agressors_pet != null) && (_player.getSummonList().getPet() != null) && _player.getSummonList().getPet().getName().equalsIgnoreCase(agressors_pet))
				{
					AddAgression(_player.getSummonList().getPet(), 10);
				}
				AddAgression(_player, 2);
			}
			if (agressors_party != null)
			{
				for (String _agressor : agressors_party)
				{
					AddAgression(World.getPlayer(_agressor), 1);
				}
			}
		}
		
		@Override
		public void runImpl()
		{
			if (_spawn == null)
			{
				return;
			}
			tiks++;
			if (tiks < 600)
			{
				updateAgression();
				ThreadPoolManager.getInstance().schedule(this, 1000);
				return;
			}
			_spawn.deleteAll();
		}
	}
}

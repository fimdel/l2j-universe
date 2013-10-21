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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.QuestState;

public class OpenSealedBox
{
	private final QuestState st;
	private String result = "";
	private int takecount = 0;
	private final Map<Integer, Long> rewards = new HashMap<>();
	private static final RewardGroup[] rewardgroups =
	{
		new RewardAdena(),
		new RewardRes1(),
		new RewardRes2(),
		new RewardEnchants(),
		new RewardParts()
	};
	public static final int[] counts =
	{
		1,
		5,
		10
	};
	
	public OpenSealedBox(QuestState st, int count)
	{
		this.st = st;
		if (count < 1)
		{
			return;
		}
		takecount = count;
		if (st.getQuestItemsCount(_620_FourGoblets.Sealed_Box) < count)
		{
			result = count == 1 ? "I don't see a box... Come back when you find one!" : "I don't see enougth boxes... Come back when you find enougth!";
			return;
		}
		int not_disintegrated = 0;
		for (int i = 0; i < count; i++)
		{
			not_disintegrated += Rnd.get(2);
		}
		if (not_disintegrated == 0)
		{
			result = count == 1 ? "I'm so sorry! The box just disintegrated!" : "I'm so sorry! The boxes just disintegrated!";
			return;
		}
		for (int i = 0; i < not_disintegrated; i++)
		{
			rewardgroups[Rnd.get(rewardgroups.length)].apply(rewards);
		}
		if (rewards.size() == 0)
		{
			result = count == 1 ? "Hmm. The box is empty." : "Hmm. All boxes is empty.";
			return;
		}
		result = "Wow! Something came out of it!";
	}
	
	public String apply()
	{
		if (takecount > 0)
		{
			if ((rewards.size() > 0) && !canGiveReward())
			{
				return "You haven't enougth free slots in your inventory.";
			}
			st.takeItems(_620_FourGoblets.Sealed_Box, takecount);
			for (Integer itemId : rewards.keySet())
			{
				st.giveItems(itemId, rewards.get(itemId), false);
			}
		}
		rewards.clear();
		return result;
	}
	
	private boolean canGiveReward()
	{
		int FreeInvSlots = st.getPlayer().getInventoryLimit() - st.getPlayer().getInventory().getSize();
		for (Integer itemId : rewards.keySet())
		{
			ItemInstance item = st.getPlayer().getInventory().getItemByItemId(itemId);
			if ((item == null) || !item.isStackable())
			{
				FreeInvSlots--;
			}
		}
		return FreeInvSlots > 0;
	}
	
	public abstract static class RewardGroup
	{
		public abstract void apply(Map<Integer, Long> rewards);
		
		protected static void putReward(Map<Integer, Long> rewards, int item_id, long count)
		{
			if (rewards.containsKey(item_id))
			{
				count += rewards.remove(item_id);
			}
			rewards.put(item_id, count);
		}
	}
	
	public static class RewardAdena extends RewardGroup
	{
		@Override
		public void apply(Map<Integer, Long> rewards)
		{
			putReward(rewards, 57, 10000);
		}
	}
	
	public static class RewardRes1 extends RewardGroup
	{
		@Override
		public void apply(Map<Integer, Long> rewards)
		{
			if (Rnd.chance(84.8))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 43)
				{
					putReward(rewards, 1884, 42);
				}
				else if (i1 < 66)
				{
					putReward(rewards, 1895, 36);
				}
				else if (i1 < 184)
				{
					putReward(rewards, 1876, 4);
				}
				else if (i1 < 250)
				{
					putReward(rewards, 1881, 6);
				}
				else if (i1 < 287)
				{
					putReward(rewards, 5549, 8);
				}
				else if (i1 < 484)
				{
					putReward(rewards, 1874, 1);
				}
				else if (i1 < 681)
				{
					putReward(rewards, 1889, 1);
				}
				else if (i1 < 799)
				{
					putReward(rewards, 1877, 1);
				}
				else if (i1 < 902)
				{
					putReward(rewards, 1894, 1);
				}
				else
				{
					putReward(rewards, 4043, 1);
				}
			}
			if (Rnd.chance(32.3))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 335)
				{
					putReward(rewards, 1888, 1);
				}
				else if (i1 < 556)
				{
					putReward(rewards, 4040, 1);
				}
				else if (i1 < 725)
				{
					putReward(rewards, 1890, 1);
				}
				else if (i1 < 872)
				{
					putReward(rewards, 5550, 1);
				}
				else if (i1 < 962)
				{
					putReward(rewards, 1893, 1);
				}
				else if (i1 < 986)
				{
					putReward(rewards, 4046, 1);
				}
				else
				{
					putReward(rewards, 4048, 1);
				}
			}
		}
	}
	
	public static class RewardRes2 extends RewardGroup
	{
		@Override
		public void apply(Map<Integer, Long> rewards)
		{
			if (Rnd.chance(84.7))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 148)
				{
					putReward(rewards, 1878, 8);
				}
				else if (i1 < 175)
				{
					putReward(rewards, 1882, 24);
				}
				else if (i1 < 273)
				{
					putReward(rewards, 1879, 4);
				}
				else if (i1 < 322)
				{
					putReward(rewards, 1880, 6);
				}
				else if (i1 < 357)
				{
					putReward(rewards, 1885, 6);
				}
				else if (i1 < 554)
				{
					putReward(rewards, 1875, 1);
				}
				else if (i1 < 685)
				{
					putReward(rewards, 1883, 1);
				}
				else if (i1 < 803)
				{
					putReward(rewards, 5220, 1);
				}
				else if (i1 < 901)
				{
					putReward(rewards, 4039, 1);
				}
				else
				{
					putReward(rewards, 4044, 1);
				}
			}
			if (Rnd.chance(25.1))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 350)
				{
					putReward(rewards, 1887, 1);
				}
				else if (i1 < 587)
				{
					putReward(rewards, 4042, 1);
				}
				else if (i1 < 798)
				{
					putReward(rewards, 1886, 1);
				}
				else if (i1 < 922)
				{
					putReward(rewards, 4041, 1);
				}
				else if (i1 < 966)
				{
					putReward(rewards, 1892, 1);
				}
				else if (i1 < 996)
				{
					putReward(rewards, 1891, 1);
				}
				else
				{
					putReward(rewards, 4047, 1);
				}
			}
		}
	}
	
	public static class RewardEnchants extends RewardGroup
	{
		@Override
		public void apply(Map<Integer, Long> rewards)
		{
			if (Rnd.chance(3.1))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 223)
				{
					putReward(rewards, 730, 1);
				}
				else if (i1 < 893)
				{
					putReward(rewards, 948, 1);
				}
				else
				{
					putReward(rewards, 960, 1);
				}
			}
			if (Rnd.chance(0.5))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 202)
				{
					putReward(rewards, 729, 1);
				}
				else if (i1 < 928)
				{
					putReward(rewards, 947, 1);
				}
				else
				{
					putReward(rewards, 959, 1);
				}
			}
		}
	}
	
	public static class RewardParts extends RewardGroup
	{
		@Override
		public void apply(Map<Integer, Long> rewards)
		{
			if (Rnd.chance(32.9))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 88)
				{
					putReward(rewards, 6698, 1);
				}
				else if (i1 < 185)
				{
					putReward(rewards, 6699, 1);
				}
				else if (i1 < 238)
				{
					putReward(rewards, 6700, 1);
				}
				else if (i1 < 262)
				{
					putReward(rewards, 6701, 1);
				}
				else if (i1 < 292)
				{
					putReward(rewards, 6702, 1);
				}
				else if (i1 < 356)
				{
					putReward(rewards, 6703, 1);
				}
				else if (i1 < 420)
				{
					putReward(rewards, 6704, 1);
				}
				else if (i1 < 482)
				{
					putReward(rewards, 6705, 1);
				}
				else if (i1 < 554)
				{
					putReward(rewards, 6706, 1);
				}
				else if (i1 < 576)
				{
					putReward(rewards, 6707, 1);
				}
				else if (i1 < 640)
				{
					putReward(rewards, 6708, 1);
				}
				else if (i1 < 704)
				{
					putReward(rewards, 6709, 1);
				}
				else if (i1 < 777)
				{
					putReward(rewards, 6710, 1);
				}
				else if (i1 < 799)
				{
					putReward(rewards, 6711, 1);
				}
				else if (i1 < 863)
				{
					putReward(rewards, 6712, 1);
				}
				else if (i1 < 927)
				{
					putReward(rewards, 6713, 1);
				}
				else
				{
					putReward(rewards, 6714, 1);
				}
			}
			if (Rnd.chance(5.4))
			{
				int i1 = Rnd.get(1000);
				if (i1 < 100)
				{
					putReward(rewards, 6688, 1);
				}
				else if (i1 < 198)
				{
					putReward(rewards, 6689, 1);
				}
				else if (i1 < 298)
				{
					putReward(rewards, 6690, 1);
				}
				else if (i1 < 398)
				{
					putReward(rewards, 6691, 1);
				}
				else if (i1 < 499)
				{
					putReward(rewards, 7579, 1);
				}
				else if (i1 < 601)
				{
					putReward(rewards, 6693, 1);
				}
				else if (i1 < 703)
				{
					putReward(rewards, 6694, 1);
				}
				else if (i1 < 801)
				{
					putReward(rewards, 6695, 1);
				}
				else if (i1 < 902)
				{
					putReward(rewards, 6696, 1);
				}
				else
				{
					putReward(rewards, 6697, 1);
				}
			}
		}
	}
}

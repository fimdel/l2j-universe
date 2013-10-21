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
package lineage2.gameserver.model.instances;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FeedableBeastInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(NpcInstance.class);
	
	/**
	 * @author Mobius
	 */
	private static class growthInfo
	{
		/**
		 * Field growth_level.
		 */
		public final int growth_level;
		/**
		 * Field growth_chance.
		 */
		public final int growth_chance;
		/**
		 * Field tameinfo.
		 */
		public final int[] tameinfo;
		/**
		 * Field spice.
		 */
		public final int[] spice;
		/**
		 * Field adultId.
		 */
		public final int[] adultId;
		
		/**
		 * Constructor for growthInfo.
		 * @param level int
		 * @param tame int[]
		 * @param sp int[]
		 * @param chance int
		 * @param adult int[]
		 */
		public growthInfo(int level, int[] tame, int[] sp, int chance, int[] adult)
		{
			growth_level = level;
			tameinfo = tame;
			spice = sp;
			growth_chance = chance;
			adultId = adult;
		}
	}
	
	/**
	 * Field growthCapableMobs.
	 */
	public static final TIntObjectHashMap<growthInfo> growthCapableMobs = new TIntObjectHashMap<>();
	/**
	 * Field tamedBeasts.
	 */
	public static final TIntArrayList tamedBeasts = new TIntArrayList();
	/**
	 * Field feedableBeasts.
	 */
	public static final TIntArrayList feedableBeasts = new TIntArrayList();
	static
	{
		growthCapableMobs.put(18873, new growthInfo(0, new int[]
		{
			18869,
			5
		}, new int[]
		{
			18874,
			18875
		}, 100, new int[]
		{
			18878,
			18879
		}));
		growthCapableMobs.put(18874, new growthInfo(1, new int[]
		{
			18869,
			5
		}, new int[]
		{
			18876,
			0
		}, 40, new int[]
		{
			18878,
			0
		}));
		growthCapableMobs.put(18875, new growthInfo(1, new int[]
		{
			18869,
			5
		}, new int[]
		{
			0,
			18877
		}, 40, new int[]
		{
			0,
			18879
		}));
		growthCapableMobs.put(18876, new growthInfo(2, new int[]
		{
			18869,
			15
		}, new int[]
		{
			18878,
			0
		}, 25, new int[]
		{
			18878,
			0
		}));
		growthCapableMobs.put(18877, new growthInfo(2, new int[]
		{
			18869,
			15
		}, new int[]
		{
			0,
			18879
		}, 25, new int[]
		{
			0,
			18879
		}));
		growthCapableMobs.put(18880, new growthInfo(0, new int[]
		{
			18870,
			5
		}, new int[]
		{
			18881,
			18882
		}, 100, new int[]
		{
			18885,
			18886
		}));
		growthCapableMobs.put(18881, new growthInfo(1, new int[]
		{
			18870,
			5
		}, new int[]
		{
			18883,
			0
		}, 40, new int[]
		{
			18885,
			0
		}));
		growthCapableMobs.put(18882, new growthInfo(1, new int[]
		{
			18870,
			5
		}, new int[]
		{
			0,
			18884
		}, 40, new int[]
		{
			0,
			18886
		}));
		growthCapableMobs.put(18883, new growthInfo(2, new int[]
		{
			18870,
			15
		}, new int[]
		{
			18885,
			0
		}, 25, new int[]
		{
			18885,
			0
		}));
		growthCapableMobs.put(18884, new growthInfo(2, new int[]
		{
			18870,
			15
		}, new int[]
		{
			0,
			18886
		}, 25, new int[]
		{
			0,
			18886
		}));
		growthCapableMobs.put(18887, new growthInfo(0, new int[]
		{
			18871,
			5
		}, new int[]
		{
			18888,
			18889
		}, 100, new int[]
		{
			18892,
			18893
		}));
		growthCapableMobs.put(18888, new growthInfo(1, new int[]
		{
			18871,
			5
		}, new int[]
		{
			18890,
			0
		}, 40, new int[]
		{
			18892,
			0
		}));
		growthCapableMobs.put(18889, new growthInfo(1, new int[]
		{
			18871,
			5
		}, new int[]
		{
			0,
			18891
		}, 40, new int[]
		{
			0,
			18893
		}));
		growthCapableMobs.put(18890, new growthInfo(2, new int[]
		{
			18871,
			15
		}, new int[]
		{
			18892,
			0
		}, 25, new int[]
		{
			18892,
			0
		}));
		growthCapableMobs.put(18891, new growthInfo(2, new int[]
		{
			18871,
			15
		}, new int[]
		{
			0,
			18893
		}, 25, new int[]
		{
			0,
			18893
		}));
		growthCapableMobs.put(18894, new growthInfo(0, new int[]
		{
			18872,
			5
		}, new int[]
		{
			18895,
			18896
		}, 100, new int[]
		{
			18899,
			18900
		}));
		growthCapableMobs.put(18895, new growthInfo(1, new int[]
		{
			18872,
			5
		}, new int[]
		{
			18897,
			0
		}, 40, new int[]
		{
			18899,
			0
		}));
		growthCapableMobs.put(18896, new growthInfo(1, new int[]
		{
			18872,
			5
		}, new int[]
		{
			0,
			18898
		}, 40, new int[]
		{
			0,
			18900
		}));
		growthCapableMobs.put(18897, new growthInfo(2, new int[]
		{
			18872,
			15
		}, new int[]
		{
			18899,
			0
		}, 25, new int[]
		{
			18899,
			0
		}));
		growthCapableMobs.put(18898, new growthInfo(2, new int[]
		{
			18872,
			15
		}, new int[]
		{
			0,
			18900
		}, 25, new int[]
		{
			0,
			18900
		}));
		for (int i = 18869; i <= 18872; i++)
		{
			tamedBeasts.add(i);
		}
		for (int i = 18869; i <= 18900; i++)
		{
			feedableBeasts.add(i);
		}
	}
	/**
	 * Field feedInfo.
	 */
	public static Map<Integer, Integer> feedInfo = new ConcurrentHashMap<>();
	
	/**
	 * Method isGoldenSpice.
	 * @param skillId int
	 * @return boolean
	 */
	private boolean isGoldenSpice(int skillId)
	{
		return (skillId == 9049) || (skillId == 9051) || (skillId == 9053);
	}
	
	/**
	 * Method isCrystalSpice.
	 * @param skillId int
	 * @return boolean
	 */
	private boolean isCrystalSpice(int skillId)
	{
		return (skillId == 9050) || (skillId == 9052) || (skillId == 9054);
	}
	
	/**
	 * Method isBlessed.
	 * @param skillId int
	 * @return boolean
	 */
	public boolean isBlessed(int skillId)
	{
		return (skillId == 9051) || (skillId == 9052);
	}
	
	/**
	 * Method isSGrade.
	 * @param skillId int
	 * @return boolean
	 */
	public boolean isSGrade(int skillId)
	{
		return (skillId == 9053) || (skillId == 9054);
	}
	
	/**
	 * Method getFoodSpice.
	 * @param skillId int
	 * @return int
	 */
	private int getFoodSpice(int skillId)
	{
		if (isGoldenSpice(skillId))
		{
			return 9049;
		}
		return 9050;
	}
	
	/**
	 * Method getItemIdBySkillId.
	 * @param skillId int
	 * @return int
	 */
	public int getItemIdBySkillId(int skillId)
	{
		int itemId = 0;
		switch (skillId)
		{
			case 9049:
				itemId = 15474;
				break;
			case 9050:
				itemId = 15475;
				break;
			case 9051:
				itemId = 15476;
				break;
			case 9052:
				itemId = 15477;
				break;
			case 9053:
				itemId = 15478;
				break;
			case 9054:
				itemId = 15479;
				break;
			default:
				itemId = 0;
		}
		return itemId;
	}
	
	/**
	 * Constructor for FeedableBeastInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public FeedableBeastInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method spawnNext.
	 * @param player Player
	 * @param growthLevel int
	 * @param food int
	 * @param skill_id int
	 */
	private void spawnNext(Player player, int growthLevel, int food, int skill_id)
	{
		int npcId = getNpcId();
		int nextNpcId = 0;
		int tameChance = growthCapableMobs.get(npcId).tameinfo[1];
		if (isBlessed(skill_id))
		{
			tameChance = 90;
		}
		if (Rnd.chance(tameChance))
		{
			nextNpcId = growthCapableMobs.get(npcId).tameinfo[0];
		}
		else
		{
			nextNpcId = growthCapableMobs.get(npcId).spice[food];
		}
		if (isSGrade(skill_id) && Rnd.chance(90))
		{
			nextNpcId = growthCapableMobs.get(npcId).adultId[food];
		}
		if (nextNpcId == 0)
		{
			return;
		}
		feedInfo.remove(getObjectId());
		if (growthCapableMobs.get(npcId).growth_level == 0)
		{
			onDecay();
		}
		else
		{
			deleteMe();
		}
		if (tamedBeasts.contains(nextNpcId))
		{
			if (player.getTrainedBeasts().size() >= 7)
			{
				return;
			}
			NpcTemplate template = NpcHolder.getInstance().getTemplate(nextNpcId);
			TamedBeastInstance nextNpc = new TamedBeastInstance(IdFactory.getInstance().getNextId(), template);
			Location loc = player.getLoc();
			loc.x = loc.x + Rnd.get(-50, 50);
			loc.y = loc.y + Rnd.get(-50, 50);
			nextNpc.spawnMe(loc);
			nextNpc.setTameType();
			nextNpc.setFoodType(getFoodSpice(skill_id));
			nextNpc.setRunning();
			nextNpc.setOwner(player);
			QuestState st = player.getQuestState("_020_BringUpWithLove");
			if ((st != null) && !st.isCompleted() && Rnd.chance(5) && (st.getQuestItemsCount(7185) == 0))
			{
				st.giveItems(7185, 1);
				st.setCond(2);
			}
			st = player.getQuestState("_655_AGrandPlanForTamingWildBeasts");
			if ((st != null) && !st.isCompleted() && (st.getCond() == 1))
			{
				if (st.getQuestItemsCount(8084) < 10)
				{
					st.giveItems(8084, 1);
				}
			}
		}
		else
		{
			MonsterInstance nextNpc = spawn(nextNpcId, getX(), getY(), getZ());
			feedInfo.put(nextNpc.getObjectId(), player.getObjectId());
			player.setTarget(nextNpc);
			ThreadPoolManager.getInstance().schedule(new AggrPlayer(nextNpc, player), 3000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class AggrPlayer extends RunnableImpl
	{
		/**
		 * Field _actor.
		 */
		private final NpcInstance _actor;
		/**
		 * Field _killer.
		 */
		private final Player _killer;
		
		/**
		 * Constructor for AggrPlayer.
		 * @param actor NpcInstance
		 * @param killer Player
		 */
		public AggrPlayer(NpcInstance actor, Player killer)
		{
			_actor = actor;
			_killer = killer;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _killer, 1000);
		}
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		feedInfo.remove(getObjectId());
		super.onDeath(killer);
	}
	
	/**
	 * Method spawn.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return MonsterInstance
	 */
	public MonsterInstance spawn(int npcId, int x, int y, int z)
	{
		try
		{
			MonsterInstance monster = (MonsterInstance) NpcHolder.getInstance().getTemplate(npcId).getInstanceConstructor().newInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(npcId));
			monster.setSpawnedLoc(new Location(x, y, z));
			monster.spawnMe(monster.getSpawnedLoc());
			return monster;
		}
		catch (Exception e)
		{
			_log.error("Could not spawn Npc " + npcId, e);
		}
		return null;
	}
	
	/**
	 * Method onSkillUse.
	 * @param player Player
	 * @param skillId int
	 */
	public void onSkillUse(Player player, int skillId)
	{
		int npcId = getNpcId();
		if (!feedableBeasts.contains(npcId))
		{
			return;
		}
		if (isGoldenSpice(skillId) && isCrystalSpice(skillId))
		{
			return;
		}
		int food = isGoldenSpice(skillId) ? 0 : 1;
		int objectId = getObjectId();
		broadcastPacket(new SocialAction(objectId, 2));
		if (growthCapableMobs.containsKey(npcId))
		{
			if (growthCapableMobs.get(npcId).spice[food] == 0)
			{
				return;
			}
			int growthLevel = growthCapableMobs.get(npcId).growth_level;
			if (growthLevel > 0)
			{
				if ((feedInfo.get(objectId) != null) && (feedInfo.get(objectId) != player.getObjectId()))
				{
					return;
				}
			}
			if (Rnd.chance(growthCapableMobs.get(npcId).growth_chance))
			{
				spawnNext(player, growthLevel, food, skillId);
			}
		}
		else if (Rnd.chance(60))
		{
			dropItem(player, getItemIdBySkillId(skillId), 1);
		}
	}
}

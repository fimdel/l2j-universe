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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.model.AggroList.HateInfo;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.HeroDiary;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RaidBossInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field minionMaintainTask.
	 */
	private ScheduledFuture<?> minionMaintainTask;
	/**
	 * Field MINION_UNSPAWN_INTERVAL. (value is 5000)
	 */
	private static final int MINION_UNSPAWN_INTERVAL = 5000;
	
	/**
	 * Constructor for RaidBossInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RaidBossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method isRaid.
	 * @return boolean
	 */
	@Override
	public boolean isRaid()
	{
		return true;
	}
	
	/**
	 * Method getMinionUnspawnInterval.
	 * @return int
	 */
	protected int getMinionUnspawnInterval()
	{
		return MINION_UNSPAWN_INTERVAL;
	}
	
	/**
	 * Method getKilledInterval.
	 * @param minion MinionInstance
	 * @return int
	 */
	protected int getKilledInterval(MinionInstance minion)
	{
		return 120000;
	}
	
	/**
	 * Method notifyMinionDied.
	 * @param minion MinionInstance
	 */
	@Override
	public void notifyMinionDied(MinionInstance minion)
	{
		minionMaintainTask = ThreadPoolManager.getInstance().schedule(new MaintainKilledMinion(minion), getKilledInterval(minion));
		super.notifyMinionDied(minion);
	}
	
	/**
	 * @author Mobius
	 */
	private class MaintainKilledMinion extends RunnableImpl
	{
		/**
		 * Field minion.
		 */
		private final MinionInstance minion;
		
		/**
		 * Constructor for MaintainKilledMinion.
		 * @param minion MinionInstance
		 */
		public MaintainKilledMinion(MinionInstance minion)
		{
			this.minion = minion;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isDead())
			{
				minion.refreshID();
				spawnMinion(minion);
			}
		}
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		if (minionMaintainTask != null)
		{
			minionMaintainTask.cancel(false);
			minionMaintainTask = null;
		}
		final int points = getTemplate().rewardRp;
		if (points > 0)
		{
			calcRaidPointsReward(points);
		}
		if (this instanceof ReflectionBossInstance)
		{
			super.onDeath(killer);
			return;
		}
		if (killer.isPlayable())
		{
			Player player = killer.getPlayer();
			if (player.isInParty())
			{
				for (Player member : player.getParty().getPartyMembers())
				{
					if (member.isNoble())
					{
						Hero.getInstance().addHeroDiary(member.getObjectId(), HeroDiary.ACTION_RAID_KILLED, getNpcId());
					}
				}
				player.getParty().broadCast(Msg.CONGRATULATIONS_YOUR_RAID_WAS_SUCCESSFUL);
			}
			else
			{
				if (player.isNoble())
				{
					Hero.getInstance().addHeroDiary(player.getObjectId(), HeroDiary.ACTION_RAID_KILLED, getNpcId());
				}
				player.sendPacket(Msg.CONGRATULATIONS_YOUR_RAID_WAS_SUCCESSFUL);
			}
			Quest q = QuestManager.getQuest(508);
			if (q != null)
			{
				String qn = q.getName();
				if ((player.getClan() != null) && player.getClan().getLeader().isOnline() && (player.getClan().getLeader().getPlayer().getQuestState(qn) != null))
				{
					QuestState st = player.getClan().getLeader().getPlayer().getQuestState(qn);
					st.getQuest().onKill(this, st);
				}
			}
		}
		if (getMinionList().hasAliveMinions())
		{
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					if (isDead())
					{
						getMinionList().unspawnMinions();
					}
				}
			}, getMinionUnspawnInterval());
		}
		int boxId = 0;
		switch (getNpcId())
		{
			case 25035:
				boxId = 31027;
				break;
			case 25054:
				boxId = 31028;
				break;
			case 25126:
				boxId = 31029;
				break;
			case 25220:
				boxId = 31030;
				break;
		}
		if (boxId != 0)
		{
			NpcTemplate boxTemplate = NpcHolder.getInstance().getTemplate(boxId);
			if (boxTemplate != null)
			{
				final NpcInstance box = new NpcInstance(IdFactory.getInstance().getNextId(), boxTemplate);
				box.spawnMe(getLoc());
				box.setSpawnedLoc(getLoc());
				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(box), 60000);
			}
		}
		super.onDeath(killer);
	}
	
	/**
	 * Method calcRaidPointsReward.
	 * @param totalPoints int
	 */
	@SuppressWarnings("unchecked")
	private void calcRaidPointsReward(int totalPoints)
	{
		Map<Object, Object[]> participants = new HashMap<>();
		double totalHp = getMaxHp();
		for (HateInfo ai : getAggroList().getPlayableMap().values())
		{
			Player player = ai.attacker.getPlayer();
			Object key = player.getParty() != null ? player.getParty().getCommandChannel() != null ? player.getParty().getCommandChannel() : player.getParty() : player.getPlayer();
			Object[] info = participants.get(key);
			if (info == null)
			{
				info = new Object[]
				{
					new HashSet<Player>(),
					new Long(0)
				};
				participants.put(key, info);
			}
			if (key instanceof CommandChannel)
			{
				for (Player p : ((CommandChannel) key))
				{
					if (p.isInRangeZ(this, Config.ALT_PARTY_DISTRIBUTION_RANGE))
					{
						((Set<Player>) info[0]).add(p);
					}
				}
			}
			else if (key instanceof Party)
			{
				for (Player p : ((Party) key).getPartyMembers())
				{
					if (p.isInRangeZ(this, Config.ALT_PARTY_DISTRIBUTION_RANGE))
					{
						((Set<Player>) info[0]).add(p);
					}
				}
			}
			else
			{
				((Set<Player>) info[0]).add(player);
			}
			info[1] = ((Long) info[1]).longValue() + ai.damage;
		}
		for (Object[] groupInfo : participants.values())
		{
			Set<Player> players = (HashSet<Player>) groupInfo[0];
			int perPlayer = (int) Math.round((totalPoints * ((Long) groupInfo[1]).longValue()) / (totalHp * players.size()));
			for (Player player : players)
			{
				int playerReward = perPlayer;
				playerReward = (int) Math.round(playerReward * Experience.penaltyModifier(calculateLevelDiffForDrop(player.getLevel()), 9));
				if (playerReward == 0)
				{
					continue;
				}
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EARNED_S1_RAID_POINTS).addNumber(playerReward));
				RaidBossSpawnManager.getInstance().addPoints(player.getObjectId(), getNpcId(), playerReward);
			}
		}
		RaidBossSpawnManager.getInstance().updatePointsDb();
		RaidBossSpawnManager.getInstance().calculateRanking();
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	protected void onDecay()
	{
		super.onDecay();
		RaidBossSpawnManager.getInstance().onBossDespawned(this);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		addSkill(SkillTable.getInstance().getInfo(4045, 1));
		RaidBossSpawnManager.getInstance().onBossSpawned(this);
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	/**
	 * Method hasRandomWalk.
	 * @return boolean
	 */
	@Override
	public boolean hasRandomWalk()
	{
		return false;
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	@Override
	public boolean canChampion()
	{
		return false;
	}
}

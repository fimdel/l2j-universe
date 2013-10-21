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

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.AggroList.HateInfo;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.MinionList;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.model.reward.RewardType;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.Faction;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MonsterInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	protected static final class RewardInfo
	{
		/**
		 * Field _attacker.
		 */
		protected Creature _attacker;
		/**
		 * Field _dmg.
		 */
		protected int _dmg = 0;
		
		/**
		 * Constructor for RewardInfo.
		 * @param attacker Creature
		 * @param dmg int
		 */
		public RewardInfo(final Creature attacker, final int dmg)
		{
			_attacker = attacker;
			_dmg = dmg;
		}
		
		/**
		 * Method addDamage.
		 * @param dmg int
		 */
		public void addDamage(int dmg)
		{
			if (dmg < 0)
			{
				dmg = 0;
			}
			_dmg += dmg;
		}
		
		/**
		 * Method hashCode.
		 * @return int
		 */
		@Override
		public int hashCode()
		{
			return _attacker.getObjectId();
		}
	}
	
	/**
	 * Field minionMaintainTask.
	 */
	private ScheduledFuture<?> minionMaintainTask;
	/**
	 * Field minionList.
	 */
	private final MinionList minionList;
	/**
	 * Field _isSeeded.
	 */
	private boolean _isSeeded;
	/**
	 * Field _seederId.
	 */
	private int _seederId;
	/**
	 * Field _altSeed.
	 */
	private boolean _altSeed;
	/**
	 * Field _harvestItem.
	 */
	private RewardItem _harvestItem;
	/**
	 * Field harvestLock.
	 */
	private final Lock harvestLock = new ReentrantLock();
	/**
	 * Field overhitAttackerId.
	 */
	private int overhitAttackerId;
	/**
	 * Field _overhitDamage.
	 */
	private double _overhitDamage;
	/**
	 * Field _absorbersIds.
	 */
	private TIntHashSet _absorbersIds;
	/**
	 * Field absorbLock.
	 */
	private final Lock absorbLock = new ReentrantLock();
	/**
	 * Field _isSpoiled.
	 */
	private boolean _isSpoiled;
	/**
	 * Field spoilerId.
	 */
	private int spoilerId;
	/**
	 * Field _sweepItems.
	 */
	private List<RewardItem> _sweepItems;
	/**
	 * Field sweepLock.
	 */
	private final Lock sweepLock = new ReentrantLock();
	/**
	 * Field _isChampion.
	 */
	private int _isChampion;
	
	/**
	 * Constructor for MonsterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MonsterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		minionList = new MinionList(this);
	}
	
	/**
	 * Method isMovementDisabled.
	 * @return boolean
	 */
	@Override
	public boolean isMovementDisabled()
	{
		return (getNpcId() == 18344) || (getNpcId() == 18345) || super.isMovementDisabled();
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return (_isChampion > 0) || (getNpcId() == 22215) || (getNpcId() == 22216) || (getNpcId() == 22217) || super.isLethalImmune();
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return (_isChampion > 0) || super.isFearImmune();
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return (_isChampion > 0) || super.isParalyzeImmune();
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return !attacker.isMonster();
	}
	
	/**
	 * Method getChampion.
	 * @return int
	 */
	public int getChampion()
	{
		return _isChampion;
	}
	
	/**
	 * Method setChampion.
	 */
	public void setChampion()
	{
		if (getReflection().canChampions() && canChampion())
		{
			double random = Rnd.nextDouble();
			if ((Config.ALT_CHAMPION_CHANCE2 / 100.) >= random)
			{
				setChampion(2);
			}
			else if (((Config.ALT_CHAMPION_CHANCE1 + Config.ALT_CHAMPION_CHANCE2) / 100.) >= random)
			{
				setChampion(1);
			}
			else
			{
				setChampion(0);
			}
		}
		else
		{
			setChampion(0);
		}
	}
	
	/**
	 * Method setChampion.
	 * @param level int
	 */
	public void setChampion(int level)
	{
		if (level == 0)
		{
			removeSkillById(4407);
			_isChampion = 0;
		}
		else
		{
			addSkill(SkillTable.getInstance().getInfo(4407, level));
			_isChampion = level;
		}
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	public boolean canChampion()
	{
		return (getTemplate().rewardExp > 0) && (getTemplate().level <= Config.ALT_CHAMPION_TOP_LEVEL);
	}
	
	/**
	 * Method getTeam.
	 * @return TeamType
	 */
	@Override
	public TeamType getTeam()
	{
		return getChampion() == 2 ? TeamType.RED : getChampion() == 1 ? TeamType.BLUE : TeamType.NONE;
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		setCurrentHpMp(getMaxHp(), getMaxMp(), true);
		if (getMinionList().hasMinions())
		{
			if (minionMaintainTask != null)
			{
				minionMaintainTask.cancel(false);
				minionMaintainTask = null;
			}
			minionMaintainTask = ThreadPoolManager.getInstance().schedule(new MinionMaintainTask(), 1000L);
		}
	}
	
	/**
	 * Method onDespawn.
	 */
	@Override
	protected void onDespawn()
	{
		setOverhitDamage(0);
		setOverhitAttacker(null);
		clearSweep();
		clearHarvest();
		clearAbsorbers();
		super.onDespawn();
	}
	
	/**
	 * Method getMinionList.
	 * @return MinionList
	 */
	@Override
	public MinionList getMinionList()
	{
		return minionList;
	}
	
	/**
	 * @author Mobius
	 */
	public class MinionMaintainTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (isDead())
			{
				return;
			}
			getMinionList().spawnMinions();
		}
	}
	
	/**
	 * Method getMinionPosition.
	 * @return Location
	 */
	public Location getMinionPosition()
	{
		return Location.findPointToStay(this, 100, 150);
	}
	
	/**
	 * Method notifyMinionDied.
	 * @param minion MinionInstance
	 */
	public void notifyMinionDied(MinionInstance minion)
	{
	}
	
	/**
	 * Method spawnMinion.
	 * @param minion MonsterInstance
	 */
	public void spawnMinion(MonsterInstance minion)
	{
		minion.setReflection(getReflection());
		if (getChampion() == 2)
		{
			minion.setChampion(1);
		}
		else
		{
			minion.setChampion(0);
		}
		minion.setHeading(getHeading());
		minion.setCurrentHpMp(minion.getMaxHp(), minion.getMaxMp(), true);
		minion.spawnMe(getMinionPosition());
	}
	
	/**
	 * Method hasMinions.
	 * @return boolean
	 */
	@Override
	public boolean hasMinions()
	{
		return getMinionList().hasMinions();
	}
	
	/**
	 * Method setReflection.
	 * @param reflection Reflection
	 */
	@Override
	public void setReflection(Reflection reflection)
	{
		super.setReflection(reflection);
		if (hasMinions())
		{
			for (MinionInstance m : getMinionList().getAliveMinions())
			{
				m.setReflection(reflection);
			}
		}
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		if (minionMaintainTask != null)
		{
			minionMaintainTask.cancel(false);
			minionMaintainTask = null;
		}
		getMinionList().deleteMinions();
		super.onDelete();
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
		calculateRewards(killer);
		super.onDeath(killer);
	}
	
	/**
	 * Method onReduceCurrentHp.
	 * @param damage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 */
	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		if ((skill != null) && skill.isOverhit())
		{
			double overhitDmg = (getCurrentHp() - damage) * -1;
			if (overhitDmg <= 0)
			{
				setOverhitDamage(0);
				setOverhitAttacker(null);
			}
			else
			{
				setOverhitDamage(overhitDmg);
				setOverhitAttacker(attacker);
			}
		}
		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp);
	}
	
	/**
	 * Method calculateRewards.
	 * @param lastAttacker Creature
	 */
	public void calculateRewards(Creature lastAttacker)
	{
		Creature topDamager = getAggroList().getTopDamager();
		if ((lastAttacker == null) || !lastAttacker.isPlayable())
		{
			lastAttacker = topDamager;
		}
		if ((lastAttacker == null) || !lastAttacker.isPlayable())
		{
			return;
		}
		Player killer = lastAttacker.getPlayer();
		if (killer == null)
		{
			return;
		}
		Map<Playable, HateInfo> aggroMap = getAggroList().getPlayableMap();
		Quest[] quests = getTemplate().getEventQuests(QuestEventType.MOB_KILLED_WITH_QUEST);
		if ((quests != null) && (quests.length > 0))
		{
			List<Player> players = null;
			if (isRaid() && Config.ALT_NO_LASTHIT)
			{
				players = new ArrayList<>();
				for (Playable pl : aggroMap.keySet())
				{
					if (!pl.isDead() && (isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE) || killer.isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE)))
					{
						if (!players.contains(pl.getPlayer()))
						{
							players.add(pl.getPlayer());
						}
					}
				}
			}
			else if (killer.getParty() != null)
			{
				players = new ArrayList<>(killer.getParty().getMemberCount());
				for (Player pl : killer.getParty().getPartyMembers())
				{
					if (!pl.isDead() && (isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE) || killer.isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE)))
					{
						players.add(pl);
					}
				}
			}
			for (Quest quest : quests)
			{
				Player toReward = killer;
				if ((quest.getParty() != Quest.PARTY_NONE) && (players != null))
				{
					if (isRaid() || (quest.getParty() == Quest.PARTY_ALL))
					{
						for (Player pl : players)
						{
							QuestState qs = pl.getQuestState(quest.getName());
							if ((qs != null) && !qs.isCompleted())
							{
								quest.notifyKill(this, qs);
							}
						}
						toReward = null;
					}
					else
					{
						List<Player> interested = new ArrayList<>(players.size());
						for (Player pl : players)
						{
							QuestState qs = pl.getQuestState(quest.getName());
							if ((qs != null) && !qs.isCompleted())
							{
								interested.add(pl);
							}
						}
						if (interested.isEmpty())
						{
							continue;
						}
						toReward = interested.get(Rnd.get(interested.size()));
						if (toReward == null)
						{
							toReward = killer;
						}
					}
				}
				if (toReward != null)
				{
					QuestState qs = toReward.getQuestState(quest.getName());
					if ((qs != null) && !qs.isCompleted())
					{
						quest.notifyKill(this, qs);
					}
				}
			}
		}
		Map<Player, RewardInfo> rewards = new HashMap<>();
		for (HateInfo info : aggroMap.values())
		{
			if (info.damage <= 1)
			{
				continue;
			}
			Playable attacker = (Playable) info.attacker;
			Player player = attacker.getPlayer();
			RewardInfo reward = rewards.get(player);
			if (reward == null)
			{
				rewards.put(player, new RewardInfo(player, info.damage));
			}
			else
			{
				reward.addDamage(info.damage);
			}
		}
		Player[] attackers = rewards.keySet().toArray(new Player[rewards.size()]);
		double[] xpsp = new double[2];
		for (Player attacker : attackers)
		{
			if (attacker.isDead())
			{
				continue;
			}
			RewardInfo reward = rewards.get(attacker);
			if (reward == null)
			{
				continue;
			}
			Party party = attacker.getParty();
			int maxHp = getMaxHp();
			xpsp[0] = 0.;
			xpsp[1] = 0.;
			if (party == null)
			{
				int damage = Math.min(reward._dmg, maxHp);
				if (damage > 0)
				{
					if (isInRangeZ(attacker, Config.ALT_PARTY_DISTRIBUTION_RANGE))
					{
						xpsp = calculateExpAndSp(attacker.getLevel(), damage);
					}
					xpsp[0] = applyOverhit(killer, xpsp[0]);
					attacker.addExpAndCheckBonus(this, (long) xpsp[0], (long) xpsp[1], 1.);
				}
				rewards.remove(attacker);
			}
			else
			{
				int partyDmg = 0;
				int partylevel = 1;
				List<Player> rewardedMembers = new ArrayList<>();
				for (Player partyMember : party.getPartyMembers())
				{
					RewardInfo ai = rewards.remove(partyMember);
					if (partyMember.isDead() || !isInRangeZ(partyMember, Config.ALT_PARTY_DISTRIBUTION_RANGE))
					{
						continue;
					}
					if (ai != null)
					{
						partyDmg += ai._dmg;
					}
					rewardedMembers.add(partyMember);
					if (partyMember.getLevel() > partylevel)
					{
						partylevel = partyMember.getLevel();
					}
				}
				partyDmg = Math.min(partyDmg, maxHp);
				if (partyDmg > 0)
				{
					xpsp = calculateExpAndSp(partylevel, partyDmg);
					double partyMul = (double) partyDmg / maxHp;
					xpsp[0] *= partyMul;
					xpsp[1] *= partyMul;
					xpsp[0] = applyOverhit(killer, xpsp[0]);
					party.distributeXpAndSp(xpsp[0], xpsp[1], rewardedMembers, lastAttacker, this);
				}
			}
		}
		CursedWeaponsManager.getInstance().dropAttackable(this, killer);
		if ((topDamager == null) || !topDamager.isPlayable())
		{
			return;
		}
		for (Map.Entry<RewardType, RewardList> entry : getTemplate().getRewards().entrySet())
		{
			rollRewards(entry, lastAttacker, topDamager);
		}
	}
	
	/**
	 * Method onRandomAnimation.
	 */
	@Override
	public void onRandomAnimation()
	{
		if ((System.currentTimeMillis() - _lastSocialAction) > 10000L)
		{
			broadcastPacket(new SocialAction(getObjectId(), 1));
			_lastSocialAction = System.currentTimeMillis();
		}
	}
	
	/**
	 * Method startRandomAnimation.
	 */
	@Override
	public void startRandomAnimation()
	{
	}
	
	/**
	 * Method getKarma.
	 * @return int
	 */
	@Override
	public int getKarma()
	{
		return 0;
	}
	
	/**
	 * Method addAbsorber.
	 * @param attacker Player
	 */
	public void addAbsorber(final Player attacker)
	{
		if (attacker == null)
		{
			return;
		}
		if (getCurrentHpPercents() > 50)
		{
			return;
		}
		absorbLock.lock();
		try
		{
			if (_absorbersIds == null)
			{
				_absorbersIds = new TIntHashSet();
			}
			_absorbersIds.add(attacker.getObjectId());
		}
		finally
		{
			absorbLock.unlock();
		}
	}
	
	/**
	 * Method isAbsorbed.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isAbsorbed(Player player)
	{
		absorbLock.lock();
		try
		{
			if (_absorbersIds == null)
			{
				return false;
			}
			if (!_absorbersIds.contains(player.getObjectId()))
			{
				return false;
			}
		}
		finally
		{
			absorbLock.unlock();
		}
		return true;
	}
	
	/**
	 * Method clearAbsorbers.
	 */
	public void clearAbsorbers()
	{
		absorbLock.lock();
		try
		{
			if (_absorbersIds != null)
			{
				_absorbersIds.clear();
			}
		}
		finally
		{
			absorbLock.unlock();
		}
	}
	
	/**
	 * Method takeHarvest.
	 * @return RewardItem
	 */
	public RewardItem takeHarvest()
	{
		harvestLock.lock();
		try
		{
			RewardItem harvest;
			harvest = _harvestItem;
			clearHarvest();
			return harvest;
		}
		finally
		{
			harvestLock.unlock();
		}
	}
	
	/**
	 * Method clearHarvest.
	 */
	public void clearHarvest()
	{
		harvestLock.lock();
		try
		{
			_harvestItem = null;
			_altSeed = false;
			_seederId = 0;
			_isSeeded = false;
		}
		finally
		{
			harvestLock.unlock();
		}
	}
	
	/**
	 * Method setSeeded.
	 * @param player Player
	 * @param seedId int
	 * @param altSeed boolean
	 * @return boolean
	 */
	public boolean setSeeded(Player player, int seedId, boolean altSeed)
	{
		harvestLock.lock();
		try
		{
			if (isSeeded())
			{
				return false;
			}
			_isSeeded = true;
			_altSeed = altSeed;
			_seederId = player.getObjectId();
			_harvestItem = new RewardItem(Manor.getInstance().getCropType(seedId));
			if (getTemplate().rateHp > 1)
			{
				_harvestItem.count = Rnd.get(Math.round(getTemplate().rateHp), Math.round(1.5 * getTemplate().rateHp));
			}
		}
		finally
		{
			harvestLock.unlock();
		}
		return true;
	}
	
	/**
	 * Method isSeeded.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isSeeded(Player player)
	{
		return isSeeded() && (_seederId == player.getObjectId()) && (getDeadTime() < 20000L);
	}
	
	/**
	 * Method isSeeded.
	 * @return boolean
	 */
	public boolean isSeeded()
	{
		return _isSeeded;
	}
	
	/**
	 * Method isSpoiled.
	 * @return boolean
	 */
	public boolean isSpoiled()
	{
		return _isSpoiled;
	}
	
	/**
	 * Method isSpoiled.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isSpoiled(Player player)
	{
		if (!isSpoiled())
		{
			return false;
		}
		if ((player.getObjectId() == spoilerId) && (getDeadTime() < 20000L))
		{
			return true;
		}
		if (player.isInParty())
		{
			for (Player pm : player.getParty().getPartyMembers())
			{
				if ((pm.getObjectId() == spoilerId) && (getDistance(pm) < Config.ALT_PARTY_DISTRIBUTION_RANGE))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method setSpoiled.
	 * @param player Player
	 * @return boolean
	 */
	public boolean setSpoiled(Player player)
	{
		sweepLock.lock();
		try
		{
			if (isSpoiled())
			{
				return false;
			}
			_isSpoiled = true;
			spoilerId = player.getObjectId();
		}
		finally
		{
			sweepLock.unlock();
		}
		return true;
	}
	
	/**
	 * Method isSweepActive.
	 * @return boolean
	 */
	public boolean isSweepActive()
	{
		sweepLock.lock();
		try
		{
			return (_sweepItems != null) && (_sweepItems.size() > 0);
		}
		finally
		{
			sweepLock.unlock();
		}
	}
	
	/**
	 * Method takeSweep.
	 * @return List<RewardItem>
	 */
	public List<RewardItem> takeSweep()
	{
		sweepLock.lock();
		try
		{
			List<RewardItem> sweep = _sweepItems;
			clearSweep();
			return sweep;
		}
		finally
		{
			sweepLock.unlock();
		}
	}
	
	/**
	 * Method clearSweep.
	 */
	public void clearSweep()
	{
		sweepLock.lock();
		try
		{
			_isSpoiled = false;
			spoilerId = 0;
			_sweepItems = null;
		}
		finally
		{
			sweepLock.unlock();
		}
	}
	
	/**
	 * Method rollRewards.
	 * @param entry Map.Entry<RewardType,RewardList>
	 * @param lastAttacker Creature
	 * @param topDamager Creature
	 */
	public void rollRewards(Map.Entry<RewardType, RewardList> entry, final Creature lastAttacker, Creature topDamager)
	{
		RewardType type = entry.getKey();
		RewardList list = entry.getValue();
		if ((type == RewardType.SWEEP) && !isSpoiled())
		{
			return;
		}
		final Creature activeChar = type == RewardType.SWEEP ? lastAttacker : topDamager;
		final Player activePlayer = activeChar.getPlayer();
		if (activePlayer == null)
		{
			return;
		}
		final int diff = calculateLevelDiffForDrop(topDamager.getLevel());
		double mod = calcStat(Stats.REWARD_MULTIPLIER, 1., activeChar, null);
		mod *= Experience.penaltyModifier(diff, 9);
		List<RewardItem> rewardItems = list.roll(activePlayer, mod, this instanceof RaidBossInstance);
		switch (type)
		{
			case SWEEP:
				_sweepItems = rewardItems;
				break;
			default:
				for (RewardItem drop : rewardItems)
				{
					if (isSeeded() && !_altSeed && !drop.isAdena)
					{
						continue;
					}
					dropItem(activePlayer, drop.itemId, drop.count);
				}
				break;
		}
	}
	
	/**
	 * Method calculateExpAndSp.
	 * @param level int
	 * @param damage long
	 * @return double[]
	 */
	private double[] calculateExpAndSp(int level, long damage)
	{
		int diff = level - getLevel();
		if ((level > 77) && (diff > 3) && (diff <= 5))
		{
			diff += 3;
		}
		double xp = (getExpReward() * damage) / getMaxHp();
		double sp = (getSpReward() * damage) / getMaxHp();
		if (diff > 5)
		{
			double mod = Math.pow(.83, diff - 5);
			xp *= mod;
			sp *= mod;
		}
		if ((level > 84) && (diff < -2))
		{
			double mod = Math.pow(.83, Math.abs(diff + 3));
			xp *= mod;
			sp *= mod;
		}
		if (diff > 10)
		{
			xp = 0.;
		}
		xp = Math.max(0., xp);
		sp = Math.max(0., sp);
		return new double[]
		{
			xp,
			sp
		};
	}
	
	/**
	 * Method applyOverhit.
	 * @param killer Player
	 * @param xp double
	 * @return double
	 */
	private double applyOverhit(Player killer, double xp)
	{
		if ((xp > 0) && (killer.getObjectId() == overhitAttackerId))
		{
			int overHitExp = calculateOverhitExp(xp);
			killer.sendPacket(Msg.OVER_HIT, new SystemMessage(SystemMessage.ACQUIRED_S1_BONUS_EXPERIENCE_THROUGH_OVER_HIT).addNumber(overHitExp));
			xp += overHitExp;
		}
		return xp;
	}
	
	/**
	 * Method setOverhitAttacker.
	 * @param attacker Creature
	 */
	@Override
	public void setOverhitAttacker(Creature attacker)
	{
		overhitAttackerId = attacker == null ? 0 : attacker.getObjectId();
	}
	
	/**
	 * Method getOverhitDamage.
	 * @return double
	 */
	public double getOverhitDamage()
	{
		return _overhitDamage;
	}
	
	/**
	 * Method setOverhitDamage.
	 * @param damage double
	 */
	@Override
	public void setOverhitDamage(double damage)
	{
		_overhitDamage = damage;
	}
	
	/**
	 * Method calculateOverhitExp.
	 * @param normalExp double
	 * @return int
	 */
	public int calculateOverhitExp(final double normalExp)
	{
		double overhitPercentage = (getOverhitDamage() * 100) / getMaxHp();
		if (overhitPercentage > 25)
		{
			overhitPercentage = 25;
		}
		double overhitExp = (overhitPercentage / 100) * normalExp;
		setOverhitAttacker(null);
		setOverhitDamage(0);
		return (int) Math.round(overhitExp);
	}
	
	/**
	 * Method isAggressive.
	 * @return boolean
	 */
	@Override
	public boolean isAggressive()
	{
		return (Config.ALT_CHAMPION_CAN_BE_AGGRO || (getChampion() == 0)) && super.isAggressive();
	}
	
	/**
	 * Method getFaction.
	 * @return Faction
	 */
	@Override
	public Faction getFaction()
	{
		return Config.ALT_CHAMPION_CAN_BE_SOCIAL || (getChampion() == 0) ? super.getFaction() : Faction.NONE;
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param i double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		checkUD(attacker, i);
		super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * Field MIN_DISTANCE_FOR_USE_UD.
	 */
	private final double MIN_DISTANCE_FOR_USE_UD = 200.0;
	/**
	 * Field MIN_DISTANCE_FOR_CANCEL_UD.
	 */
	private final double MIN_DISTANCE_FOR_CANCEL_UD = 50.0;
	/**
	 * Field UD_USE_CHANCE.
	 */
	private final double UD_USE_CHANCE = 30.0;
	
	/**
	 * Method checkUD.
	 * @param attacker Creature
	 * @param damage double
	 */
	private void checkUD(Creature attacker, double damage)
	{
		if ((getTemplate().getBaseAtkRange() > MIN_DISTANCE_FOR_USE_UD) || (getLevel() < 20) || (getLevel() > 78) || ((attacker.getLevel() - getLevel()) > 9) || ((getLevel() - attacker.getLevel()) > 9))
		{
			return;
		}
		if (isMinion() || (getMinionList() != null) || isRaid() || (this instanceof ReflectionBossInstance) || (this instanceof ChestInstance) || (getChampion() > 0))
		{
			return;
		}
		int skillId = 5044;
		int skillLvl = 1;
		if ((getLevel() >= 41) || (getLevel() <= 60))
		{
			skillLvl = 2;
		}
		else if (getLevel() > 60)
		{
			skillLvl = 3;
		}
		double distance = getDistance(attacker);
		if (distance <= MIN_DISTANCE_FOR_CANCEL_UD)
		{
			if ((getEffectList() != null) && (getEffectList().getEffectsBySkillId(skillId) != null))
			{
				for (Effect e : getEffectList().getEffectsBySkillId(skillId))
				{
					e.exit();
				}
			}
		}
		else if (distance >= MIN_DISTANCE_FOR_USE_UD)
		{
			double chance = UD_USE_CHANCE / (getMaxHp() / damage);
			if (Rnd.chance(chance))
			{
				Skill skill = SkillTable.getInstance().getInfo(skillId, skillLvl);
				if (skill != null)
				{
					skill.getEffects(this, this, false, false);
				}
			}
		}
	}
	
	/**
	 * Method isMonster.
	 * @return boolean
	 */
	@Override
	public boolean isMonster()
	{
		return true;
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return null;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return _isInvul;
	}
}

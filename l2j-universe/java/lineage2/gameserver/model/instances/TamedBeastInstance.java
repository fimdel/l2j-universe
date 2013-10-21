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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.NpcInfo;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TamedBeastInstance extends FeedableBeastInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field MAX_DISTANCE_FROM_OWNER. (value is 2000)
	 */
	private static final int MAX_DISTANCE_FROM_OWNER = 2000;
	/**
	 * Field MAX_DISTANCE_FOR_BUFF. (value is 200)
	 */
	private static final int MAX_DISTANCE_FOR_BUFF = 200;
	/**
	 * Field MAX_DURATION. (value is 1200000)
	 */
	private static final int MAX_DURATION = 1200000;
	/**
	 * Field DURATION_CHECK_INTERVAL. (value is 60000)
	 */
	private static final int DURATION_CHECK_INTERVAL = 60000;
	/**
	 * Field DURATION_INCREASE_INTERVAL. (value is 20000)
	 */
	private static final int DURATION_INCREASE_INTERVAL = 20000;
	/**
	 * Field _playerRef.
	 */
	private HardReference<Player> _playerRef = HardReferences.emptyRef();
	/**
	 * Field _remainingTime. Field _foodSkillId.
	 */
	private int _foodSkillId, _remainingTime = MAX_DURATION;
	/**
	 * Field _durationCheckTask.
	 */
	private Future<?> _durationCheckTask = null;
	/**
	 * Field _skills.
	 */
	private final List<Skill> _skills = new ArrayList<>();
	/**
	 * Field TAMED_DATA.
	 */
	@SuppressWarnings("unchecked")
	private static final Map.Entry<NpcString, int[]>[] TAMED_DATA = new Map.Entry[6];
	static
	{
		TAMED_DATA[0] = new AbstractMap.SimpleImmutableEntry<>(NpcString.RECKLESS_S1, new int[]
		{
			6671
		});
		TAMED_DATA[1] = new AbstractMap.SimpleImmutableEntry<>(NpcString.S1_OF_BALANCE, new int[]
		{
			6431,
			6666
		});
		TAMED_DATA[2] = new AbstractMap.SimpleImmutableEntry<>(NpcString.SHARP_S1, new int[]
		{
			6432,
			6668
		});
		TAMED_DATA[3] = new AbstractMap.SimpleImmutableEntry<>(NpcString.USEFUL_S1, new int[]
		{
			6433,
			6670
		});
		TAMED_DATA[4] = new AbstractMap.SimpleImmutableEntry<>(NpcString.S1_OF_BLESSING, new int[]
		{
			6669,
			6672
		});
		TAMED_DATA[5] = new AbstractMap.SimpleImmutableEntry<>(NpcString.SWIFT_S1, new int[]
		{
			6434,
			6667
		});
	}
	
	/**
	 * Constructor for TamedBeastInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public TamedBeastInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_hasRandomWalk = false;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}
	
	@Override
	public void onActionTargeted(final Player player, boolean forced)
	{
	}
	
	/**
	 * Method onReceiveFood.
	 */
	void onReceiveFood()
	{
		_remainingTime = _remainingTime + DURATION_INCREASE_INTERVAL;
		if (_remainingTime > MAX_DURATION)
		{
			_remainingTime = MAX_DURATION;
		}
	}
	
	/**
	 * Method getRemainingTime.
	 * @return int
	 */
	public int getRemainingTime()
	{
		return _remainingTime;
	}
	
	/**
	 * Method setRemainingTime.
	 * @param duration int
	 */
	public void setRemainingTime(int duration)
	{
		_remainingTime = duration;
	}
	
	/**
	 * Method getFoodType.
	 * @return int
	 */
	public int getFoodType()
	{
		return _foodSkillId;
	}
	
	/**
	 * Method setTameType.
	 */
	public void setTameType()
	{
		Map.Entry<NpcString, int[]> type = TAMED_DATA[Rnd.get(TAMED_DATA.length)];
		setNameNpcString(type.getKey());
		setName("#" + getNameNpcStringByNpcId().getId());
		for (int skillId : type.getValue())
		{
			Skill sk = SkillTable.getInstance().getInfo(skillId, 1);
			if (sk != null)
			{
				_skills.add(sk);
			}
		}
	}
	
	/**
	 * Method getNameNpcStringByNpcId.
	 * @return NpcString
	 */
	public NpcString getNameNpcStringByNpcId()
	{
		switch (getNpcId())
		{
			case 18869:
				return NpcString.ALPEN_KOOKABURRA;
			case 18870:
				return NpcString.ALPEN_COUGAR;
			case 18871:
				return NpcString.ALPEN_BUFFALO;
			case 18872:
				return NpcString.ALPEN_GRENDEL;
		}
		return NpcString.NONE;
	}
	
	/**
	 * Method buffOwner.
	 */
	public void buffOwner()
	{
		if (!isInRange(getPlayer(), MAX_DISTANCE_FOR_BUFF))
		{
			setFollowTarget(getPlayer());
			getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, getPlayer(), Config.FOLLOW_RANGE);
			return;
		}
		int delay = 0;
		for (Skill skill : _skills)
		{
			ThreadPoolManager.getInstance().schedule(new Buff(this, getPlayer(), skill), delay);
			delay = delay + skill.getHitTime() + 500;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class Buff extends RunnableImpl
	{
		/**
		 * Field _actor.
		 */
		private final NpcInstance _actor;
		/**
		 * Field _owner.
		 */
		private final Player _owner;
		/**
		 * Field _skill.
		 */
		private final Skill _skill;
		
		/**
		 * Constructor for Buff.
		 * @param actor NpcInstance
		 * @param owner Player
		 * @param skill Skill
		 */
		public Buff(NpcInstance actor, Player owner, Skill skill)
		{
			_actor = actor;
			_owner = owner;
			_skill = skill;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_actor != null)
			{
				_actor.doCast(_skill, _owner, true);
			}
		}
	}
	
	/**
	 * Method setFoodType.
	 * @param foodItemId int
	 */
	public void setFoodType(int foodItemId)
	{
		if (foodItemId > 0)
		{
			_foodSkillId = foodItemId;
			if (_durationCheckTask != null)
			{
				_durationCheckTask.cancel(false);
			}
			_durationCheckTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new CheckDuration(this), DURATION_CHECK_INTERVAL, DURATION_CHECK_INTERVAL);
		}
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (_durationCheckTask != null)
		{
			_durationCheckTask.cancel(false);
			_durationCheckTask = null;
		}
		Player owner = getPlayer();
		if (owner != null)
		{
			owner.removeTrainedBeast(getObjectId());
		}
		_foodSkillId = 0;
		_remainingTime = 0;
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	@Override
	public Player getPlayer()
	{
		return _playerRef.get();
	}
	
	/**
	 * Method setOwner.
	 * @param owner Player
	 */
	public void setOwner(Player owner)
	{
		_playerRef = owner == null ? HardReferences.<Player> emptyRef() : owner.getRef();
		if (owner != null)
		{
			setTitle(owner.getName());
			owner.addTrainedBeast(this);
			for (Player player : World.getAroundPlayers(this))
			{
				player.sendPacket(new NpcInfo(this, player));
			}
			setFollowTarget(getPlayer());
			getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
		}
		else
		{
			doDespawn();
		}
	}
	
	/**
	 * Method despawnWithDelay.
	 * @param delay int
	 */
	public void despawnWithDelay(int delay)
	{
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				doDespawn();
			}
		}, delay);
	}
	
	/**
	 * Method doDespawn.
	 */
	public void doDespawn()
	{
		stopMove();
		if (_durationCheckTask != null)
		{
			_durationCheckTask.cancel(false);
			_durationCheckTask = null;
		}
		Player owner = getPlayer();
		if (owner != null)
		{
			owner.removeTrainedBeast(getObjectId());
		}
		setTarget(null);
		_foodSkillId = 0;
		_remainingTime = 0;
		onDecay();
	}
	
	/**
	 * @author Mobius
	 */
	private static class CheckDuration extends RunnableImpl
	{
		/**
		 * Field _tamedBeast.
		 */
		private final TamedBeastInstance _tamedBeast;
		
		/**
		 * Constructor for CheckDuration.
		 * @param tamedBeast TamedBeastInstance
		 */
		CheckDuration(TamedBeastInstance tamedBeast)
		{
			_tamedBeast = tamedBeast;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player owner = _tamedBeast.getPlayer();
			if ((owner == null) || !owner.isOnline())
			{
				_tamedBeast.doDespawn();
				return;
			}
			if (_tamedBeast.getDistance(owner) > MAX_DISTANCE_FROM_OWNER)
			{
				_tamedBeast.doDespawn();
				return;
			}
			int foodTypeSkillId = _tamedBeast.getFoodType();
			_tamedBeast.setRemainingTime(_tamedBeast.getRemainingTime() - DURATION_CHECK_INTERVAL);
			ItemInstance item = null;
			int foodItemId = _tamedBeast.getItemIdBySkillId(foodTypeSkillId);
			if (foodItemId > 0)
			{
				item = owner.getInventory().getItemByItemId(foodItemId);
			}
			if ((item != null) && (item.getCount() >= 1))
			{
				_tamedBeast.onReceiveFood();
				owner.getInventory().destroyItem(item, 1);
			}
			else if (_tamedBeast.getRemainingTime() < (MAX_DURATION - 300000))
			{
				_tamedBeast.setRemainingTime(-1);
			}
			if (_tamedBeast.getRemainingTime() <= 0)
			{
				_tamedBeast.doDespawn();
			}
		}
	}
}

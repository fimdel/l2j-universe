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
package lineage2.gameserver.model;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.games.FishingChampionShipManager;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.network.serverpackets.ExFishingEnd;
import lineage2.gameserver.network.serverpackets.ExFishingHpRegen;
import lineage2.gameserver.network.serverpackets.ExFishingStart;
import lineage2.gameserver.network.serverpackets.ExFishingStartCombat;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.FishTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Fishing
{
	/**
	 * Field _fisher.
	 */
	final Player _fisher;
	/**
	 * Field FISHING_NONE. (value is 0)
	 */
	public final static int FISHING_NONE = 0;
	/**
	 * Field FISHING_STARTED. (value is 1)
	 */
	public final static int FISHING_STARTED = 1;
	/**
	 * Field FISHING_WAITING. (value is 2)
	 */
	public final static int FISHING_WAITING = 2;
	/**
	 * Field FISHING_COMBAT. (value is 3)
	 */
	public final static int FISHING_COMBAT = 3;
	/**
	 * Field _state.
	 */
	private final AtomicInteger _state;
	/**
	 * Field _time.
	 */
	int _time;
	/**
	 * Field _stop.
	 */
	int _stop;
	/**
	 * Field _gooduse.
	 */
	private int _gooduse;
	/**
	 * Field _anim.
	 */
	int _anim;
	/**
	 * Field _combatMode.
	 */
	int _combatMode = -1;
	/**
	 * Field _deceptiveMode.
	 */
	int _deceptiveMode;
	/**
	 * Field _fishCurHP.
	 */
	int _fishCurHP;
	/**
	 * Field _fish.
	 */
	FishTemplate _fish;
	/**
	 * Field _lureId.
	 */
	int _lureId;
	/**
	 * Field _fishingTask.
	 */
	private Future<?> _fishingTask;
	/**
	 * Field _fishLoc.
	 */
	private final Location _fishLoc = new Location();
	
	/**
	 * Constructor for Fishing.
	 * @param fisher Player
	 */
	public Fishing(Player fisher)
	{
		_fisher = fisher;
		_state = new AtomicInteger(FISHING_NONE);
	}
	
	/**
	 * Method setFish.
	 * @param fish FishTemplate
	 */
	public void setFish(FishTemplate fish)
	{
		_fish = fish;
	}
	
	/**
	 * Method setLureId.
	 * @param lureId int
	 */
	public void setLureId(int lureId)
	{
		_lureId = lureId;
	}
	
	/**
	 * Method getLureId.
	 * @return int
	 */
	public int getLureId()
	{
		return _lureId;
	}
	
	/**
	 * Method setFishLoc.
	 * @param loc Location
	 */
	public void setFishLoc(Location loc)
	{
		_fishLoc.x = loc.x;
		_fishLoc.y = loc.y;
		_fishLoc.z = loc.z;
	}
	
	/**
	 * Method getFishLoc.
	 * @return Location
	 */
	public Location getFishLoc()
	{
		return _fishLoc;
	}
	
	/**
	 * Method startFishing.
	 */
	public void startFishing()
	{
		if (!_state.compareAndSet(FISHING_NONE, FISHING_STARTED))
		{
			return;
		}
		_fisher.setFishing(true);
		_fisher.broadcastCharInfo();
		_fisher.broadcastPacket(new ExFishingStart(_fisher, _fish.getType(), _fisher.getFishLoc(), isNightLure(_lureId)));
		_fisher.sendPacket(Msg.STARTS_FISHING);
		startLookingForFishTask();
	}
	
	/**
	 * Method stopFishing.
	 */
	public void stopFishing()
	{
		if (_state.getAndSet(FISHING_NONE) == FISHING_NONE)
		{
			return;
		}
		stopFishingTask();
		_fisher.setFishing(false);
		_fisher.broadcastPacket(new ExFishingEnd(_fisher, false));
		_fisher.broadcastCharInfo();
		_fisher.sendPacket(Msg.CANCELS_FISHING);
	}
	
	/**
	 * Method endFishing.
	 * @param win boolean
	 */
	public void endFishing(boolean win)
	{
		if (!_state.compareAndSet(FISHING_COMBAT, FISHING_NONE))
		{
			return;
		}
		stopFishingTask();
		_fisher.setFishing(false);
		_fisher.broadcastPacket(new ExFishingEnd(_fisher, win));
		_fisher.broadcastCharInfo();
		_fisher.sendPacket(Msg.ENDS_FISHING);
	}
	
	/**
	 * Method stopFishingTask.
	 */
	void stopFishingTask()
	{
		if (_fishingTask != null)
		{
			_fishingTask.cancel(false);
			_fishingTask = null;
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class LookingForFishTask extends RunnableImpl
	{
		/**
		 * Field _endTaskTime.
		 */
		private final long _endTaskTime;
		
		/**
		 * Constructor for LookingForFishTask.
		 */
		protected LookingForFishTask()
		{
			_endTaskTime = System.currentTimeMillis() + _fish.getWaitTime() + 10000L;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (System.currentTimeMillis() >= _endTaskTime)
			{
				_fisher.sendPacket(Msg.BAITS_HAVE_BEEN_LOST_BECAUSE_THE_FISH_GOT_AWAY);
				stopFishingTask();
				endFishing(false);
				return;
			}
			if (!GameTimeController.getInstance().isNowNight() && isNightLure(_lureId))
			{
				_fisher.sendPacket(Msg.BAITS_HAVE_BEEN_LOST_BECAUSE_THE_FISH_GOT_AWAY);
				stopFishingTask();
				endFishing(false);
				return;
			}
			int check = Rnd.get(1000);
			if (_fish.getFishGuts() > check)
			{
				stopFishingTask();
				startFishCombat();
			}
		}
	}
	
	/**
	 * Method startLookingForFishTask.
	 */
	private void startLookingForFishTask()
	{
		if (!_state.compareAndSet(FISHING_STARTED, FISHING_WAITING))
		{
			return;
		}
		long checkDelay = 10000L;
		switch (_fish.getGroup())
		{
			case 0:
				checkDelay = Math.round(_fish.getGutsCheckTime() * 1.33);
				break;
			case 1:
				checkDelay = _fish.getGutsCheckTime();
				break;
			case 2:
				checkDelay = Math.round(_fish.getGutsCheckTime() * 0.66);
				break;
		}
		_fishingTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new LookingForFishTask(), 10000L, checkDelay);
	}
	
	/**
	 * @author Mobius
	 */
	private class FishCombatTask extends RunnableImpl
	{
		/**
		 * Constructor for FishCombatTask.
		 */
		public FishCombatTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_fishCurHP >= (_fish.getHP() * 2))
			{
				_fisher.sendPacket(Msg.THE_FISH_GOT_AWAY);
				doDie(false);
			}
			else if (_time <= 0)
			{
				_fisher.sendPacket(Msg.TIME_IS_UP_SO_THAT_FISH_GOT_AWAY);
				doDie(false);
			}
			else
			{
				_time--;
				if (((_combatMode == 1) && (_deceptiveMode == 0)) || ((_combatMode == 0) && (_deceptiveMode == 1)))
				{
					_fishCurHP += _fish.getHpRegen();
				}
				if (_stop == 0)
				{
					_stop = 1;
					if (Rnd.chance(30))
					{
						_combatMode = _combatMode == 0 ? 1 : 0;
					}
					if (_fish.getGroup() == 2)
					{
						if (Rnd.chance(10))
						{
							_deceptiveMode = _deceptiveMode == 0 ? 1 : 0;
						}
					}
				}
				else
				{
					_stop--;
				}
				ExFishingHpRegen efhr = new ExFishingHpRegen(_fisher, _time, _fishCurHP, _combatMode, 0, _anim, 0, _deceptiveMode);
				if (_anim != 0)
				{
					_fisher.broadcastPacket(efhr);
				}
				else
				{
					_fisher.sendPacket(efhr);
				}
			}
		}
	}
	
	/**
	 * Method isInCombat.
	 * @return boolean
	 */
	public boolean isInCombat()
	{
		return _state.get() == FISHING_COMBAT;
	}
	
	/**
	 * Method startFishCombat.
	 */
	void startFishCombat()
	{
		if (!_state.compareAndSet(FISHING_WAITING, FISHING_COMBAT))
		{
			return;
		}
		_stop = 0;
		_gooduse = 0;
		_anim = 0;
		_time = _fish.getCombatTime() / 1000;
		_fishCurHP = _fish.getHP();
		_combatMode = Rnd.chance(20) ? 1 : 0;
		switch (getLureGrade(_lureId))
		{
			case 0:
			case 1:
				_deceptiveMode = 0;
				break;
			case 2:
				_deceptiveMode = Rnd.chance(10) ? 1 : 0;
				break;
		}
		ExFishingStartCombat efsc = new ExFishingStartCombat(_fisher, _time, _fish.getHP(), _combatMode, _fish.getGroup(), _deceptiveMode);
		_fisher.broadcastPacket(efsc);
		_fisher.sendPacket(Msg.SUCCEEDED_IN_GETTING_A_BITE);
		_fishingTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new FishCombatTask(), 1000L, 1000L);
	}
	
	/**
	 * Method changeHp.
	 * @param hp int
	 * @param pen int
	 */
	private void changeHp(int hp, int pen)
	{
		_fishCurHP -= hp;
		if (_fishCurHP < 0)
		{
			_fishCurHP = 0;
		}
		_fisher.broadcastPacket(new ExFishingHpRegen(_fisher, _time, _fishCurHP, _combatMode, _gooduse, _anim, pen, _deceptiveMode));
		_gooduse = 0;
		_anim = 0;
		if (_fishCurHP > (_fish.getHP() * 2))
		{
			_fishCurHP = _fish.getHP() * 2;
			doDie(false);
		}
		else if (_fishCurHP == 0)
		{
			doDie(true);
		}
	}
	
	/**
	 * Method doDie.
	 * @param win boolean
	 */
	void doDie(boolean win)
	{
		stopFishingTask();
		if (win)
		{
			if (!_fisher.isInPeaceZone() && Rnd.chance(5))
			{
				win = false;
				_fisher.sendPacket(Msg.YOU_HAVE_CAUGHT_A_MONSTER);
				spawnPenaltyMonster(_fisher);
			}
			else
			{
				_fisher.sendPacket(Msg.SUCCEEDED_IN_FISHING);
				ItemFunctions.addItem(_fisher, _fish.getId(), 1, true);
				FishingChampionShipManager.getInstance().newFish(_fisher, _lureId);
			}
		}
		endFishing(win);
	}
	
	/**
	 * Method useFishingSkill.
	 * @param dmg int
	 * @param pen int
	 * @param skillType SkillType
	 */
	public void useFishingSkill(int dmg, int pen, SkillType skillType)
	{
		if (!isInCombat())
		{
			return;
		}
		int mode;
		if ((skillType == SkillType.REELING) && !GameTimeController.getInstance().isNowNight())
		{
			mode = 1;
		}
		else if ((skillType == SkillType.PUMPING) && GameTimeController.getInstance().isNowNight())
		{
			mode = 1;
		}
		else
		{
			mode = 0;
		}
		_anim = mode + 1;
		if (Rnd.chance(10))
		{
			_fisher.sendPacket(Msg.FISH_HAS_RESISTED);
			_gooduse = 0;
			changeHp(0, pen);
			return;
		}
		if (_combatMode == mode)
		{
			if (_deceptiveMode == 0)
			{
				showMessage(_fisher, dmg, pen, skillType, 1);
				_gooduse = 1;
				changeHp(dmg, pen);
			}
			else
			{
				showMessage(_fisher, dmg, pen, skillType, 2);
				_gooduse = 2;
				changeHp(-dmg, pen);
			}
		}
		else if (_deceptiveMode == 0)
		{
			showMessage(_fisher, dmg, pen, skillType, 2);
			_gooduse = 2;
			changeHp(-dmg, pen);
		}
		else
		{
			showMessage(_fisher, dmg, pen, skillType, 3);
			_gooduse = 1;
			changeHp(dmg, pen);
		}
	}
	
	/**
	 * Method showMessage.
	 * @param fisher Player
	 * @param dmg int
	 * @param pen int
	 * @param skillType SkillType
	 * @param messageId int
	 */
	private static void showMessage(Player fisher, int dmg, int pen, SkillType skillType, int messageId)
	{
		switch (messageId)
		{
			case 1:
				if (skillType == SkillType.PUMPING)
				{
					fisher.sendPacket(new SystemMessage(SystemMessage.PUMPING_IS_SUCCESSFUL_DAMAGE_S1).addNumber(dmg));
					if (pen == 50)
					{
						fisher.sendPacket(new SystemMessage(SystemMessage.YOUR_PUMPING_WAS_SUCCESSFUL_MASTERY_PENALTYS1_).addNumber(pen));
					}
				}
				else
				{
					fisher.sendPacket(new SystemMessage(SystemMessage.REELING_IS_SUCCESSFUL_DAMAGE_S1).addNumber(dmg));
					if (pen == 50)
					{
						fisher.sendPacket(new SystemMessage(SystemMessage.YOUR_REELING_WAS_SUCCESSFUL_MASTERY_PENALTYS1_).addNumber(pen));
					}
				}
				break;
			case 2:
				if (skillType == SkillType.PUMPING)
				{
					fisher.sendPacket(new SystemMessage(SystemMessage.PUMPING_FAILED_DAMAGE_S1).addNumber(dmg));
				}
				else
				{
					fisher.sendPacket(new SystemMessage(SystemMessage.REELING_FAILED_DAMAGE_S1).addNumber(dmg));
				}
				break;
			case 3:
				if (skillType == SkillType.PUMPING)
				{
					fisher.sendPacket(new SystemMessage(SystemMessage.PUMPING_IS_SUCCESSFUL_DAMAGE_S1).addNumber(dmg));
					if (pen == 50)
					{
						fisher.sendPacket(new SystemMessage(SystemMessage.YOUR_PUMPING_WAS_SUCCESSFUL_MASTERY_PENALTYS1_).addNumber(pen));
					}
				}
				else
				{
					fisher.sendPacket(new SystemMessage(SystemMessage.REELING_IS_SUCCESSFUL_DAMAGE_S1).addNumber(dmg));
					if (pen == 50)
					{
						fisher.sendPacket(new SystemMessage(SystemMessage.YOUR_REELING_WAS_SUCCESSFUL_MASTERY_PENALTYS1_).addNumber(pen));
					}
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Method spawnPenaltyMonster.
	 * @param fisher Player
	 */
	public static void spawnPenaltyMonster(Player fisher)
	{
		int npcId = 18319 + Math.min(fisher.getLevel() / 11, 7);
		MonsterInstance npc = new MonsterInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(npcId));
		npc.setSpawnedLoc(Location.findPointToStay(fisher, 100, 120));
		npc.setReflection(fisher.getReflection());
		npc.setHeading(fisher.getHeading() - 32768);
		npc.spawnMe(npc.getSpawnedLoc());
		npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, fisher, Rnd.get(1, 100));
	}
	
	/**
	 * Method getRandomFishType.
	 * @param lureId int
	 * @return int
	 */
	public static int getRandomFishType(int lureId)
	{
		int check = Rnd.get(100);
		int type;
		switch (lureId)
		{
			case 7807:
				if (check <= 54)
				{
					type = 5;
				}
				else if (check <= 77)
				{
					type = 4;
				}
				else
				{
					type = 6;
				}
				break;
			case 7808:
				if (check <= 54)
				{
					type = 4;
				}
				else if (check <= 77)
				{
					type = 6;
				}
				else
				{
					type = 5;
				}
				break;
			case 7809:
				if (check <= 54)
				{
					type = 6;
				}
				else if (check <= 77)
				{
					type = 5;
				}
				else
				{
					type = 4;
				}
				break;
			case 8486:
				if (check <= 33)
				{
					type = 4;
				}
				else if (check <= 66)
				{
					type = 5;
				}
				else
				{
					type = 6;
				}
				break;
			case 7610:
			case 7611:
			case 7612:
			case 7613:
			case 8496:
			case 8497:
			case 8498:
			case 8499:
			case 8500:
			case 8501:
			case 8502:
			case 8503:
			case 8504:
			case 8548:
				type = 3;
				break;
			case 6519:
			case 8505:
			case 6520:
			case 6521:
			case 8507:
				if (check <= 54)
				{
					type = 1;
				}
				else if (check <= 74)
				{
					type = 0;
				}
				else if (check <= 94)
				{
					type = 2;
				}
				else
				{
					type = 3;
				}
				break;
			case 6522:
			case 6523:
			case 6524:
			case 8508:
			case 8510:
				if (check <= 54)
				{
					type = 0;
				}
				else if (check <= 74)
				{
					type = 1;
				}
				else if (check <= 94)
				{
					type = 2;
				}
				else
				{
					type = 3;
				}
				break;
			case 6525:
			case 6526:
			case 6527:
			case 8511:
			case 8513:
				if (check <= 55)
				{
					type = 2;
				}
				else if (check <= 74)
				{
					type = 1;
				}
				else if (check <= 94)
				{
					type = 0;
				}
				else
				{
					type = 3;
				}
				break;
			case 8484:
				if (check <= 33)
				{
					type = 0;
				}
				else if (check <= 66)
				{
					type = 1;
				}
				else
				{
					type = 2;
				}
				break;
			case 8506:
				if (check <= 54)
				{
					type = 8;
				}
				else if (check <= 77)
				{
					type = 7;
				}
				else
				{
					type = 9;
				}
				break;
			case 8509:
				if (check <= 54)
				{
					type = 7;
				}
				else if (check <= 77)
				{
					type = 9;
				}
				else
				{
					type = 8;
				}
				break;
			case 8512:
				if (check <= 54)
				{
					type = 9;
				}
				else if (check <= 77)
				{
					type = 8;
				}
				else
				{
					type = 7;
				}
				break;
			case 8485:
				if (check <= 33)
				{
					type = 7;
				}
				else if (check <= 66)
				{
					type = 8;
				}
				else
				{
					type = 9;
				}
				break;
			default:
				type = 1;
				break;
		}
		return type;
	}
	
	/**
	 * Method getRandomFishLvl.
	 * @param player Player
	 * @return int
	 */
	public static int getRandomFishLvl(Player player)
	{
		int skilllvl = 0;
		Effect effect = player.getEffectList().getEffectByStackType("fishPot");
		if (effect != null)
		{
			skilllvl = (int) effect.getSkill().getPower();
		}
		else
		{
			skilllvl = player.getSkillLevel(1315);
		}
		if (skilllvl <= 0)
		{
			return 1;
		}
		int randomlvl;
		int check = Rnd.get(100);
		if (check < 50)
		{
			randomlvl = skilllvl;
		}
		else if (check <= 85)
		{
			randomlvl = skilllvl - 1;
			if (randomlvl <= 0)
			{
				randomlvl = 1;
			}
		}
		else
		{
			randomlvl = skilllvl + 1;
		}
		randomlvl = Math.min(27, Math.max(1, randomlvl));
		return randomlvl;
	}
	
	/**
	 * Method getFishGroup.
	 * @param lureId int
	 * @return int
	 */
	public static int getFishGroup(int lureId)
	{
		switch (lureId)
		{
			case 7807:
			case 7808:
			case 7809:
			case 8486:
				return 0;
			case 8506:
			case 8509:
			case 8512:
			case 8485:
				return 2;
			default:
				return 1;
		}
	}
	
	/**
	 * Method getLureGrade.
	 * @param lureId int
	 * @return int
	 */
	public static int getLureGrade(int lureId)
	{
		switch (lureId)
		{
			case 6519:
			case 6522:
			case 6525:
			case 8505:
			case 8508:
			case 8511:
				return 0;
			case 6520:
			case 6523:
			case 6526:
			case 7610:
			case 7611:
			case 7612:
			case 7613:
			case 7807:
			case 7808:
			case 7809:
			case 8484:
			case 8485:
			case 8486:
			case 8496:
			case 8497:
			case 8498:
			case 8499:
			case 8500:
			case 8501:
			case 8502:
			case 8503:
			case 8504:
			case 8548:
			case 8506:
			case 8509:
			case 8512:
				return 1;
			case 6521:
			case 6524:
			case 6527:
			case 8507:
			case 8510:
			case 8513:
				return 2;
			default:
				return -1;
		}
	}
	
	/**
	 * Method isNightLure.
	 * @param lureId int
	 * @return boolean
	 */
	public static boolean isNightLure(int lureId)
	{
		switch (lureId)
		{
			case 8505:
			case 8508:
			case 8511:
				return true;
			case 8496:
			case 8497:
			case 8498:
			case 8499:
			case 8500:
			case 8501:
			case 8502:
			case 8503:
			case 8504:
				return true;
			case 8506:
			case 8509:
			case 8512:
				return true;
			case 8510:
			case 8513:
				return true;
			case 8485:
				return true;
			default:
				return false;
		}
	}
}

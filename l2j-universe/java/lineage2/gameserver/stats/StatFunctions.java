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
package lineage2.gameserver.stats;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.BaseStats;
import lineage2.gameserver.model.base.ClassType2;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.conditions.ConditionPlayerState;
import lineage2.gameserver.stats.conditions.ConditionPlayerState.CheckPlayerState;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StatFunctions
{
	/**
	 * @author Mobius
	 */
	private static class FuncMultRegenResting extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMultRegenResting[] func = new FuncMultRegenResting[Stats.NUM_STATS];
		
		/**
		 * Method getFunc.
		 * @param stat Stats
		 * @return Func
		 */
		static Func getFunc(Stats stat)
		{
			int pos = stat.ordinal();
			if (func[pos] == null)
			{
				func[pos] = new FuncMultRegenResting(stat);
			}
			return func[pos];
		}
		
		/**
		 * Constructor for FuncMultRegenResting.
		 * @param stat Stats
		 */
		private FuncMultRegenResting(Stats stat)
		{
			super(stat, 0x30, null);
			setCondition(new ConditionPlayerState(CheckPlayerState.RESTING, true));
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.character.isPlayer() && (env.character.getLevel() <= 40) && (((Player) env.character).getClassLevel() < 2) && (stat == Stats.REGENERATE_HP_RATE))
			{
				env.value *= 6.;
			}
			else
			{
				env.value *= 1.5;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMultRegenStanding extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMultRegenStanding[] func = new FuncMultRegenStanding[Stats.NUM_STATS];
		
		/**
		 * Method getFunc.
		 * @param stat Stats
		 * @return Func
		 */
		static Func getFunc(Stats stat)
		{
			int pos = stat.ordinal();
			if (func[pos] == null)
			{
				func[pos] = new FuncMultRegenStanding(stat);
			}
			return func[pos];
		}
		
		/**
		 * Constructor for FuncMultRegenStanding.
		 * @param stat Stats
		 */
		private FuncMultRegenStanding(Stats stat)
		{
			super(stat, 0x30, null);
			setCondition(new ConditionPlayerState(CheckPlayerState.STANDING, true));
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= 1.1;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMultRegenRunning extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMultRegenRunning[] func = new FuncMultRegenRunning[Stats.NUM_STATS];
		
		/**
		 * Method getFunc.
		 * @param stat Stats
		 * @return Func
		 */
		static Func getFunc(Stats stat)
		{
			int pos = stat.ordinal();
			if (func[pos] == null)
			{
				func[pos] = new FuncMultRegenRunning(stat);
			}
			return func[pos];
		}
		
		/**
		 * Constructor for FuncMultRegenRunning.
		 * @param stat Stats
		 */
		private FuncMultRegenRunning(Stats stat)
		{
			super(stat, 0x30, null);
			setCondition(new ConditionPlayerState(CheckPlayerState.RUNNING, true));
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= 0.7;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPAtkMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncPAtkMul func = new FuncPAtkMul();
		
		/**
		 * Constructor for FuncPAtkMul.
		 */
		private FuncPAtkMul()
		{
			super(Stats.POWER_ATTACK, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.STR.calcBonus(env.character) * env.character.getLevelMod();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMAtkMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMAtkMul func = new FuncMAtkMul();
		
		/**
		 * Constructor for FuncMAtkMul.
		 */
		private FuncMAtkMul()
		{
			super(Stats.MAGIC_ATTACK, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			double ib = BaseStats.INT.calcBonus(env.character);
			double lvlb = env.character.getLevelMod();
			env.value *= lvlb * lvlb * ib * ib;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPDefMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncPDefMul func = new FuncPDefMul();
		
		/**
		 * Constructor for FuncPDefMul.
		 */
		private FuncPDefMul()
		{
			super(Stats.POWER_DEFENCE, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= env.character.getLevelMod();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMDefMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMDefMul func = new FuncMDefMul();
		
		/**
		 * Constructor for FuncMDefMul.
		 */
		private FuncMDefMul()
		{
			super(Stats.MAGIC_DEFENCE, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.MEN.calcBonus(env.character) * env.character.getLevelMod();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncAttackRange extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncAttackRange func = new FuncAttackRange();
		
		/**
		 * Constructor for FuncAttackRange.
		 */
		private FuncAttackRange()
		{
			super(Stats.POWER_ATTACK_RANGE, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			WeaponTemplate weapon = env.character.getActiveWeaponItem();
			if (weapon != null)
			{
				env.value += weapon.getAttackRange();
			}
		}
	}

	
	/**
	 * @author Mobius
	 */
	private static class FuncMAccuracyAdd extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMAccuracyAdd func = new FuncMAccuracyAdd();
		
		/**
		 * Constructor for FuncMAccuracyAdd.
		 */
		private FuncMAccuracyAdd()
		{
			super(Stats.MACCURACY_COMBAT, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.character.isPet())
			{
				return;
			}
			env.value += (Math.sqrt(env.character.getWIT()) * 3) + (env.character.getLevel() * 2);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMEvasionAdd extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMEvasionAdd func = new FuncMEvasionAdd();
		
		/**
		 * Constructor for FuncMEvasionAdd.
		 */
		private FuncMEvasionAdd()
		{
			super(Stats.MEVASION_RATE, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value += (Math.sqrt(env.character.getWIT()) * 3) + (env.character.getLevel() * 2);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncAccuracyAdd extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncAccuracyAdd func = new FuncAccuracyAdd();
		
		/**
		 * Constructor for FuncAccuracyAdd.
		 */
		private FuncAccuracyAdd()
		{
			super(Stats.ACCURACY_COMBAT, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.character.isPet())
			{
				return;
			}
			env.value += (Math.sqrt(env.character.getDEX()) * 5.5) + env.character.getLevel();
			if (env.character.isServitor())
			{
				env.value += env.character.getLevel() < 60 ? 4 : 5;
			}
			if (env.character.getLevel() > 77 && env.character.getLevel() < 85)
			{
				env.value += env.character.getLevel() - 77;
			}
			if (env.character.getLevel() > 69 && env.character.getLevel() < 85)
			{
				env.value += env.character.getLevel() - 69;
			}
			if (env.character.getLevel() > 84)
			{
				env.value += 24;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncEvasionAdd extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncEvasionAdd func = new FuncEvasionAdd();
		
		/**
		 * Constructor for FuncEvasionAdd.
		 */
		private FuncEvasionAdd()
		{
			super(Stats.EVASION_RATE, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value += (Math.sqrt(env.character.getDEX()) * 5.5) + env.character.getLevel();
			if (env.character.getLevel() > 69 && env.character.getLevel() < 85)
			{
				env.value += env.character.getLevel() - 69;
			}
			if (env.character.getLevel() > 84)
			{
				env.value += 16;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMCriticalRateMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMCriticalRateMul func = new FuncMCriticalRateMul();
		
		/**
		 * Constructor for FuncMCriticalRateMul.
		 */
		private FuncMCriticalRateMul()
		{
			super(Stats.MCRITICAL_RATE, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.WIT.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPCriticalRateMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncPCriticalRateMul func = new FuncPCriticalRateMul();
		
		/**
		 * Constructor for FuncPCriticalRateMul.
		 */
		private FuncPCriticalRateMul()
		{
			super(Stats.CRITICAL_BASE, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (!(env.character instanceof Summon))
			{
				env.value *= BaseStats.DEX.calcBonus(env.character);
			}
			env.value *= 0.01 * env.character.calcStat(Stats.CRITICAL_RATE, env.target, env.skill);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMoveSpeedMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMoveSpeedMul func = new FuncMoveSpeedMul();
		
		/**
		 * Constructor for FuncMoveSpeedMul.
		 */
		private FuncMoveSpeedMul()
		{
			super(Stats.RUN_SPEED, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPAtkSpeedMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncPAtkSpeedMul func = new FuncPAtkSpeedMul();
		
		/**
		 * Constructor for FuncPAtkSpeedMul.
		 */
		private FuncPAtkSpeedMul()
		{
			super(Stats.POWER_ATTACK_SPEED, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.DEX.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMAtkSpeedMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMAtkSpeedMul func = new FuncMAtkSpeedMul();
		
		/**
		 * Constructor for FuncMAtkSpeedMul.
		 */
		private FuncMAtkSpeedMul()
		{
			super(Stats.MAGIC_ATTACK_SPEED, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.WIT.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncHennaSTR extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncHennaSTR func = new FuncHennaSTR();
		
		/**
		 * Constructor for FuncHennaSTR.
		 */
		private FuncHennaSTR()
		{
			super(Stats.STAT_STR, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player pc = (Player) env.character;
			if (pc != null)
			{
				env.value = Math.max(1, env.value + pc.getHennaStatSTR());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncHennaDEX extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncHennaDEX func = new FuncHennaDEX();
		
		/**
		 * Constructor for FuncHennaDEX.
		 */
		private FuncHennaDEX()
		{
			super(Stats.STAT_DEX, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player pc = (Player) env.character;
			if (pc != null)
			{
				env.value = Math.max(1, env.value + pc.getHennaStatDEX());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncHennaINT extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncHennaINT func = new FuncHennaINT();
		
		/**
		 * Constructor for FuncHennaINT.
		 */
		private FuncHennaINT()
		{
			super(Stats.STAT_INT, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player pc = (Player) env.character;
			if (pc != null)
			{
				env.value = Math.max(1, env.value + pc.getHennaStatINT());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncHennaMEN extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncHennaMEN func = new FuncHennaMEN();
		
		/**
		 * Constructor for FuncHennaMEN.
		 */
		private FuncHennaMEN()
		{
			super(Stats.STAT_MEN, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player pc = (Player) env.character;
			if (pc != null)
			{
				env.value = Math.max(1, env.value + pc.getHennaStatMEN());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncHennaCON extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncHennaCON func = new FuncHennaCON();
		
		/**
		 * Constructor for FuncHennaCON.
		 */
		private FuncHennaCON()
		{
			super(Stats.STAT_CON, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player pc = (Player) env.character;
			if (pc != null)
			{
				env.value = Math.max(1, env.value + pc.getHennaStatCON());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncHennaWIT extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncHennaWIT func = new FuncHennaWIT();
		
		/**
		 * Constructor for FuncHennaWIT.
		 */
		private FuncHennaWIT()
		{
			super(Stats.STAT_WIT, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player pc = (Player) env.character;
			if (pc != null)
			{
				env.value = Math.max(1, env.value + pc.getHennaStatWIT());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMaxHpMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMaxHpMul func = new FuncMaxHpMul();
		
		/**
		 * Constructor for FuncMaxHpMul.
		 */
		private FuncMaxHpMul()
		{
			super(Stats.MAX_HP, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.CON.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMaxCpMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMaxCpMul func = new FuncMaxCpMul();
		
		/**
		 * Constructor for FuncMaxCpMul.
		 */
		private FuncMaxCpMul()
		{
			super(Stats.MAX_CP, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.CON.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMaxMpMul extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMaxMpMul func = new FuncMaxMpMul();
		
		/**
		 * Constructor for FuncMaxMpMul.
		 */
		private FuncMaxMpMul()
		{
			super(Stats.MAX_MP, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= BaseStats.MEN.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPDamageResists extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncPDamageResists func = new FuncPDamageResists();
		
		/**
		 * Constructor for FuncPDamageResists.
		 */
		private FuncPDamageResists()
		{
			super(Stats.PHYSICAL_DAMAGE, 0x30, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.target.isRaid() && ((env.character.getLevel() - env.target.getLevel()) > Config.RAID_MAX_LEVEL_DIFF))
			{
				env.value = 1;
				return;
			}
			WeaponTemplate weapon = env.character.getActiveWeaponItem();
			if (weapon == null)
			{
				env.value *= 0.01 * env.target.calcStat(Stats.FIST_WPN_VULNERABILITY, env.character, env.skill);
			}
			else if (weapon.getItemType().getDefence() != null)
			{
				env.value *= 0.01 * env.target.calcStat(weapon.getItemType().getDefence(), env.character, env.skill);
			}
			env.value = Formulas.calcDamageResists(env.skill, env.character, env.target, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMDamageResists extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncMDamageResists func = new FuncMDamageResists();
		
		/**
		 * Constructor for FuncMDamageResists.
		 */
		private FuncMDamageResists()
		{
			super(Stats.MAGIC_DAMAGE, 0x30, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.target.isRaid() && (Math.abs(env.character.getLevel() - env.target.getLevel()) > Config.RAID_MAX_LEVEL_DIFF))
			{
				env.value = 1;
				return;
			}
			env.value = Formulas.calcDamageResists(env.skill, env.character, env.target, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncInventory extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncInventory func = new FuncInventory();
		
		/**
		 * Constructor for FuncInventory.
		 */
		private FuncInventory()
		{
			super(Stats.INVENTORY_LIMIT, 0x01, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player player = (Player) env.character;
			if (player.isGM())
			{
				env.value = Config.INVENTORY_MAXIMUM_GM;
			}
			else if (player.getTemplate().getRace() == Race.dwarf)
			{
				env.value = Config.INVENTORY_MAXIMUM_DWARF;
			}
			else
			{
				env.value = Config.INVENTORY_MAXIMUM_NO_DWARF;
			}
			env.value += player.getExpandInventory();
			env.value = Math.min(env.value, Config.SERVICES_EXPAND_INVENTORY_MAX);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncWarehouse extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncWarehouse func = new FuncWarehouse();
		
		/**
		 * Constructor for FuncWarehouse.
		 */
		private FuncWarehouse()
		{
			super(Stats.STORAGE_LIMIT, 0x01, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player player = (Player) env.character;
			if (player.getTemplate().getRace() == Race.dwarf)
			{
				env.value = Config.WAREHOUSE_SLOTS_DWARF;
			}
			else
			{
				env.value = Config.WAREHOUSE_SLOTS_NO_DWARF;
			}
			env.value += player.getExpandWarehouse();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncTradeLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncTradeLimit func = new FuncTradeLimit();
		
		/**
		 * Constructor for FuncTradeLimit.
		 */
		private FuncTradeLimit()
		{
			super(Stats.TRADE_LIMIT, 0x01, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Player _cha = (Player) env.character;
			if (_cha.getRace() == Race.dwarf)
			{
				env.value = Config.MAX_PVTSTORE_SLOTS_DWARF;
			}
			else
			{
				env.value = Config.MAX_PVTSTORE_SLOTS_OTHER;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncSDefInit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncSDefInit();
		
		/**
		 * Constructor for FuncSDefInit.
		 */
		private FuncSDefInit()
		{
			super(Stats.SHIELD_RATE, 0x01, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			Creature cha = env.character;
			env.value = cha.getTemplate().getBaseShldRate();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncSDefAll extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncSDefAll func = new FuncSDefAll();
		
		/**
		 * Constructor for FuncSDefAll.
		 */
		private FuncSDefAll()
		{
			super(Stats.SHIELD_RATE, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.value == 0)
			{
				return;
			}
			Creature target = env.target;
			if (target != null)
			{
				WeaponTemplate weapon = target.getActiveWeaponItem();
				if (weapon != null)
				{
					switch (weapon.getItemType())
					{
						case BOW:
						case CROSSBOW:
							env.value += 30.;
							break;
						case DAGGER:
						case DUALDAGGER:
							env.value += 12.;
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncSDefPlayers extends Func
	{
		/**
		 * Field func.
		 */
		static final FuncSDefPlayers func = new FuncSDefPlayers();
		
		/**
		 * Constructor for FuncSDefPlayers.
		 */
		private FuncSDefPlayers()
		{
			super(Stats.SHIELD_RATE, 0x20, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.value == 0)
			{
				return;
			}
			Creature cha = env.character;
			ItemInstance shld = ((Player) cha).getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
			if ((shld == null) || (shld.getItemType() != WeaponType.NONE))
			{
				return;
			}
			env.value *= BaseStats.DEX.calcBonus(env.character);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMaxHpLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMaxHpLimit();
		
		/**
		 * Constructor for FuncMaxHpLimit.
		 */
		private FuncMaxHpLimit()
		{
			super(Stats.MAX_HP, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(100000, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMaxMpLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMaxMpLimit();
		
		/**
		 * Constructor for FuncMaxMpLimit.
		 */
		private FuncMaxMpLimit()
		{
			super(Stats.MAX_MP, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(100000, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMaxCpLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMaxCpLimit();
		
		/**
		 * Constructor for FuncMaxCpLimit.
		 */
		private FuncMaxCpLimit()
		{
			super(Stats.MAX_CP, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(150000, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncRunSpdLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncRunSpdLimit();
		
		/**
		 * Constructor for FuncRunSpdLimit.
		 */
		private FuncRunSpdLimit()
		{
			super(Stats.RUN_SPEED, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.character.getPlayer().isGM())
			{
				env.value = Math.min(Config.GM_LIM_MOVE, env.value);
			}
			else
			{
				env.value = Math.min(Config.LIM_MOVE, env.value);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPDefLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncPDefLimit();
		
		/**
		 * Constructor for FuncPDefLimit.
		 */
		private FuncPDefLimit()
		{
			super(Stats.POWER_DEFENCE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_PDEF, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMDefLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMDefLimit();
		
		/**
		 * Constructor for FuncMDefLimit.
		 */
		private FuncMDefLimit()
		{
			super(Stats.MAGIC_DEFENCE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_MDEF, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPAtkLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncPAtkLimit();
		
		/**
		 * Constructor for FuncPAtkLimit.
		 */
		private FuncPAtkLimit()
		{
			super(Stats.POWER_ATTACK, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_PATK, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMAtkLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMAtkLimit();
		
		/**
		 * Constructor for FuncMAtkLimit.
		 */
		private FuncMAtkLimit()
		{
			super(Stats.MAGIC_ATTACK, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_MATK, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncPAtkSpdLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncPAtkSpdLimit();
		
		/**
		 * Constructor for FuncPAtkSpdLimit.
		 */
		private FuncPAtkSpdLimit()
		{
			super(Stats.POWER_ATTACK_SPEED, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_PATK_SPD, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMAtkSpdLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMAtkSpdLimit();
		
		/**
		 * Constructor for FuncMAtkSpdLimit.
		 */
		private FuncMAtkSpdLimit()
		{
			super(Stats.MAGIC_ATTACK_SPEED, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_MATK_SPD, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncCAtkLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncCAtkLimit();
		
		/**
		 * Constructor for FuncCAtkLimit.
		 */
		private FuncCAtkLimit()
		{
			super(Stats.CRITICAL_DAMAGE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_CRIT_DAM / 2., env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncEvasionLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncEvasionLimit();
		
		/**
		 * Constructor for FuncEvasionLimit.
		 */
		private FuncEvasionLimit()
		{
			super(Stats.EVASION_RATE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_EVASION, env.value);
		}
	}
	/**
	 * @author Mobius
	 */
	private static class FuncMEvasionLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMEvasionLimit();
		
		/**
		 * Constructor for FuncEvasionLimit.
		 */
		private FuncMEvasionLimit()
		{
			super(Stats.MEVASION_RATE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_MEVASION, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncMAccuracyLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMAccuracyLimit();
		
		/**
		 * Constructor for FuncAccuracyLimit.
		 */
		private FuncMAccuracyLimit()
		{
			super(Stats.MACCURACY_COMBAT, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_MACCURACY, env.value);
		}
	}
	/**
	 * @author Mobius
	 */
	private static class FuncAccuracyLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncAccuracyLimit();
		
		/**
		 * Constructor for FuncAccuracyLimit.
		 */
		private FuncAccuracyLimit()
		{
			super(Stats.ACCURACY_COMBAT, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_ACCURACY, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncCritLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncCritLimit();
		
		/**
		 * Constructor for FuncCritLimit.
		 */
		private FuncCritLimit()
		{
			super(Stats.CRITICAL_BASE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_CRIT, env.value);
		}
	}
	
	private static class FuncMCritLimit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func func = new FuncMCritLimit();
		
		/**
		 * Constructor for FuncMCritLimit.
		 */
		private FuncMCritLimit()
		{
			super(Stats.MCRITICAL_RATE, 0x100, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value = Math.min(Config.LIM_MCRIT, env.value);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncAttributeAttackInit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func[] func = new FuncAttributeAttackInit[Element.VALUES.length];
		static
		{
			for (int i = 0; i < Element.VALUES.length; i++)
			{
				func[i] = new FuncAttributeAttackInit(Element.VALUES[i]);
			}
		}
		
		/**
		 * Method getFunc.
		 * @param element Element
		 * @return Func
		 */
		static Func getFunc(Element element)
		{
			return func[element.getId()];
		}
		
		/**
		 * Field element.
		 */
		private final Element element;
		
		/**
		 * Constructor for FuncAttributeAttackInit.
		 * @param element Element
		 */
		private FuncAttributeAttackInit(Element element)
		{
			super(element.getAttack(), 0x01, null);
			this.element = element;
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value += env.character.getTemplate().getBaseAttributeAttack()[element.getId()];
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncAttributeDefenceInit extends Func
	{
		/**
		 * Field func.
		 */
		static final Func[] func = new FuncAttributeDefenceInit[Element.VALUES.length];
		static
		{
			for (int i = 0; i < Element.VALUES.length; i++)
			{
				func[i] = new FuncAttributeDefenceInit(Element.VALUES[i]);
			}
		}
		
		/**
		 * Method getFunc.
		 * @param element Element
		 * @return Func
		 */
		static Func getFunc(Element element)
		{
			return func[element.getId()];
		}
		
		/**
		 * Field element.
		 */
		private final Element element;
		
		/**
		 * Constructor for FuncAttributeDefenceInit.
		 * @param element Element
		 */
		private FuncAttributeDefenceInit(Element element)
		{
			super(element.getDefence(), 0x01, null);
			this.element = element;
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value += env.character.getTemplate().getBaseAttributeDefence()[element.getId()];
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncAttributeAttackSet extends Func
	{
		/**
		 * Field func.
		 */
		static final Func[] func = new FuncAttributeAttackSet[Element.VALUES.length];
		static
		{
			for (int i = 0; i < Element.VALUES.length; i++)
			{
				func[i] = new FuncAttributeAttackSet(Element.VALUES[i].getAttack());
			}
		}
		
		/**
		 * Method getFunc.
		 * @param element Element
		 * @return Func
		 */
		static Func getFunc(Element element)
		{
			return func[element.getId()];
		}
		
		/**
		 * Constructor for FuncAttributeAttackSet.
		 * @param stat Stats
		 */
		private FuncAttributeAttackSet(Stats stat)
		{
			super(stat, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.character.getPlayer().getClassId().getType2() == ClassType2.Summoner)
			{
				env.value = env.character.getPlayer().calcStat(stat, 0.);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class FuncAttributeDefenceSet extends Func
	{
		/**
		 * Field func.
		 */
		static final Func[] func = new FuncAttributeDefenceSet[Element.VALUES.length];
		static
		{
			for (int i = 0; i < Element.VALUES.length; i++)
			{
				func[i] = new FuncAttributeDefenceSet(Element.VALUES[i].getDefence());
			}
		}
		
		/**
		 * Method getFunc.
		 * @param element Element
		 * @return Func
		 */
		static Func getFunc(Element element)
		{
			return func[element.getId()];
		}
		
		/**
		 * Constructor for FuncAttributeDefenceSet.
		 * @param stat Stats
		 */
		private FuncAttributeDefenceSet(Stats stat)
		{
			super(stat, 0x10, null);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			if (env.character.getPlayer().getClassId().getType2() == ClassType2.Summoner)
			{
				env.value = env.character.getPlayer().calcStat(stat, 0.);
			}
		}
	}
	
	/**
	 * Method addPredefinedFuncs.
	 * @param cha Creature
	 */
	public static void addPredefinedFuncs(Creature cha)
	{
		if (cha.isPlayer())
		{
			cha.addStatFunc(FuncMultRegenResting.getFunc(Stats.REGENERATE_CP_RATE));
			cha.addStatFunc(FuncMultRegenStanding.getFunc(Stats.REGENERATE_CP_RATE));
			cha.addStatFunc(FuncMultRegenRunning.getFunc(Stats.REGENERATE_CP_RATE));
			cha.addStatFunc(FuncMultRegenResting.getFunc(Stats.REGENERATE_HP_RATE));
			cha.addStatFunc(FuncMultRegenStanding.getFunc(Stats.REGENERATE_HP_RATE));
			cha.addStatFunc(FuncMultRegenRunning.getFunc(Stats.REGENERATE_HP_RATE));
			cha.addStatFunc(FuncMultRegenResting.getFunc(Stats.REGENERATE_MP_RATE));
			cha.addStatFunc(FuncMultRegenStanding.getFunc(Stats.REGENERATE_MP_RATE));
			cha.addStatFunc(FuncMultRegenRunning.getFunc(Stats.REGENERATE_MP_RATE));
			cha.addStatFunc(FuncMaxCpMul.func);
			cha.addStatFunc(FuncMaxHpMul.func);
			cha.addStatFunc(FuncMaxMpMul.func);
			cha.addStatFunc(FuncAttackRange.func);
			cha.addStatFunc(FuncMoveSpeedMul.func);
			cha.addStatFunc(FuncHennaSTR.func);
			cha.addStatFunc(FuncHennaDEX.func);
			cha.addStatFunc(FuncHennaINT.func);
			cha.addStatFunc(FuncHennaMEN.func);
			cha.addStatFunc(FuncHennaCON.func);
			cha.addStatFunc(FuncHennaWIT.func);
			cha.addStatFunc(FuncInventory.func);
			cha.addStatFunc(FuncWarehouse.func);
			cha.addStatFunc(FuncTradeLimit.func);
			cha.addStatFunc(FuncSDefPlayers.func);
			cha.addStatFunc(FuncMaxHpLimit.func);
			cha.addStatFunc(FuncMaxMpLimit.func);
			cha.addStatFunc(FuncMaxCpLimit.func);
			cha.addStatFunc(FuncRunSpdLimit.func);
			cha.addStatFunc(FuncRunSpdLimit.func);
			cha.addStatFunc(FuncPDefLimit.func);
			cha.addStatFunc(FuncMDefLimit.func);
			cha.addStatFunc(FuncPAtkLimit.func);
			cha.addStatFunc(FuncMAtkLimit.func);
		}
		if (cha.isPlayer() || cha.isPet())
		{
			cha.addStatFunc(FuncPAtkMul.func);
			cha.addStatFunc(FuncMAtkMul.func);
			cha.addStatFunc(FuncPDefMul.func);
			cha.addStatFunc(FuncMDefMul.func);
		}
		if (cha.isServitor())
		{
			cha.addStatFunc(FuncAttributeAttackSet.getFunc(Element.FIRE));
			cha.addStatFunc(FuncAttributeAttackSet.getFunc(Element.WATER));
			cha.addStatFunc(FuncAttributeAttackSet.getFunc(Element.EARTH));
			cha.addStatFunc(FuncAttributeAttackSet.getFunc(Element.WIND));
			cha.addStatFunc(FuncAttributeAttackSet.getFunc(Element.HOLY));
			cha.addStatFunc(FuncAttributeAttackSet.getFunc(Element.UNHOLY));
			cha.addStatFunc(FuncAttributeDefenceSet.getFunc(Element.FIRE));
			cha.addStatFunc(FuncAttributeDefenceSet.getFunc(Element.WATER));
			cha.addStatFunc(FuncAttributeDefenceSet.getFunc(Element.EARTH));
			cha.addStatFunc(FuncAttributeDefenceSet.getFunc(Element.WIND));
			cha.addStatFunc(FuncAttributeDefenceSet.getFunc(Element.HOLY));
			cha.addStatFunc(FuncAttributeDefenceSet.getFunc(Element.UNHOLY));
		}
		if (!cha.isPet())
		{
			cha.addStatFunc(FuncAccuracyAdd.func);
			cha.addStatFunc(FuncEvasionAdd.func);
			cha.addStatFunc(FuncMEvasionAdd.func);
			cha.addStatFunc(FuncMAccuracyAdd.func);
		}
		if (!cha.isPet() && !cha.isServitor())
		{
			cha.addStatFunc(FuncPAtkSpeedMul.func);
			cha.addStatFunc(FuncMAtkSpeedMul.func);
			cha.addStatFunc(FuncSDefInit.func);
			cha.addStatFunc(FuncSDefAll.func);
		}
		cha.addStatFunc(FuncPAtkSpdLimit.func);
		cha.addStatFunc(FuncMAtkSpdLimit.func);
		cha.addStatFunc(FuncCAtkLimit.func);
		cha.addStatFunc(FuncEvasionLimit.func);
		cha.addStatFunc(FuncMEvasionLimit.func);
		cha.addStatFunc(FuncAccuracyLimit.func);
		cha.addStatFunc(FuncMAccuracyLimit.func);
		cha.addStatFunc(FuncCritLimit.func);
		cha.addStatFunc(FuncMCritLimit.func);
		cha.addStatFunc(FuncMCriticalRateMul.func);
		cha.addStatFunc(FuncPCriticalRateMul.func);
		cha.addStatFunc(FuncPDamageResists.func);
		cha.addStatFunc(FuncMDamageResists.func);
		cha.addStatFunc(FuncAttributeAttackInit.getFunc(Element.FIRE));
		cha.addStatFunc(FuncAttributeAttackInit.getFunc(Element.WATER));
		cha.addStatFunc(FuncAttributeAttackInit.getFunc(Element.EARTH));
		cha.addStatFunc(FuncAttributeAttackInit.getFunc(Element.WIND));
		cha.addStatFunc(FuncAttributeAttackInit.getFunc(Element.HOLY));
		cha.addStatFunc(FuncAttributeAttackInit.getFunc(Element.UNHOLY));
		cha.addStatFunc(FuncAttributeDefenceInit.getFunc(Element.FIRE));
		cha.addStatFunc(FuncAttributeDefenceInit.getFunc(Element.WATER));
		cha.addStatFunc(FuncAttributeDefenceInit.getFunc(Element.EARTH));
		cha.addStatFunc(FuncAttributeDefenceInit.getFunc(Element.WIND));
		cha.addStatFunc(FuncAttributeDefenceInit.getFunc(Element.HOLY));
		cha.addStatFunc(FuncAttributeDefenceInit.getFunc(Element.UNHOLY));
	}
}

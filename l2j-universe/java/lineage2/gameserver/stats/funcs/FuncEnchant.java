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
package lineage2.gameserver.stats.funcs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.tables.EnchantHPBonusTable;
import lineage2.gameserver.tables.EnchantStatBonusTable;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.ItemType;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FuncEnchant extends Func
{

	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(EnchantStatBonusTable.class);
	
	private static int _limit1 = Config.OVERENCHANT_LIMIT1;
	
	private static int _limit2 = Config.OVERENCHANT_LIMIT2;
	
	private static int _limit3 = Config.OVERENCHANT_LIMIT3;
	
	private static int _limit4 = Config.OVERENCHANT_LIMIT4;
	
	private static double _overEnchantMul1 = Config.OVERENCHANT_MUL1;
	
	private static double _overEnchantMul2 = Config.OVERENCHANT_MUL2;
	
	private static double _overEnchantMul3 = Config.OVERENCHANT_MUL3;
	
	private static double _overEnchantMul4 = Config.OVERENCHANT_MUL4;
	
	private static double _blessedMultiplier = Config.BLESSED_ARMOR_WEAPON_MUL;
	/**
	 * Constructor for FuncEnchant.
	 * @param stat Stats
	 * @param order int
	 * @param owner Object
	 * @param value double
	 */
	public FuncEnchant(Stats stat, int order, Object owner, double value)
	{
		super(stat, order, owner);
	}
	
	/**
	 * Method calc.
	 * @param env Env
	 */
	@Override
	public void calc(Env env)
	{
		ItemInstance item = (ItemInstance) owner;
		int enchant = item.getEnchantLevel();
		if(enchant == 0)
		{
			return;
		}
		ItemType itemType = item.getItemType();
		boolean isBlessed = item.getTemplate().isBlessedEquipment();
		boolean isTopGrade = false;
		int crystal = item.getTemplate().getCrystalType().cry;
		Integer bodyPart = item.getBodyPart();
		if(crystal == ItemTemplate.CRYSTAL_R)
		{
			isTopGrade = true;
		}
		switch (stat)
		{
			case SHIELD_DEFENCE:
				double defenseBonus = EnchantStatBonusTable.getInstance().getDefenseBonus(crystal,enchant,_limit1);
				env.value += (int)calcStatBonus(enchant, defenseBonus,isBlessed, false,isTopGrade,false);
				break;
			case MAGIC_DEFENCE:
			{
				defenseBonus = EnchantStatBonusTable.getInstance().getDefenseBonus(crystal,enchant,_limit1);
				env.value += (int)calcStatBonus(enchant, defenseBonus,isBlessed, false,isTopGrade,false);
				break;				
			}
			case POWER_DEFENCE:
			{	
				defenseBonus = EnchantStatBonusTable.getInstance().getDefenseBonus(crystal,enchant,_limit1);
				env.value += (int)calcStatBonus(enchant, defenseBonus,isBlessed, false,isTopGrade,false);
				break;
			}
			case MAX_HP:
			{
				env.value += EnchantHPBonusTable.getInstance().getHPBonus(item);
				break;
			}
			case RUN_SPEED:
					if(item.isArmor() && enchant > _limit1)
					{
						double runSpdBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart, false);
						if(runSpdBonus == 0)
						{
							_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no run speed bonus for the body part." );
							break;
						}
						env.value += (int)calcStatBonus(enchant,runSpdBonus,isBlessed,true,isTopGrade,false);
					}
					break;
			case CRITICAL_RATE:
				if(item.isArmor() && enchant > _limit1)
				{
					double critRateBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart, false);
					if(critRateBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no critical rate bonus for the body part." );
						break;
					}
					env.value += (Math.round(calcStatBonus(enchant,critRateBonus,isBlessed,true,isTopGrade,false)*Math.pow(10,1))/Math.pow(10,1)* 2);
				}
				return;
			case MCRITICAL_RATE:
				if(item.isArmor() && enchant > _limit1)
				{
					double mcritRateBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart,true);
					if(mcritRateBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no magic Critical rate bonus for the body part." );
						break;
					}
					env.value += calcStatBonus(enchant,mcritRateBonus,isBlessed,true,isTopGrade,false);
				}
				return;
			case ACCURACY_COMBAT:
				if(item.isArmor() && enchant > _limit1)
				{
					double accCombatBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart, false);
					if(accCombatBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no accuracy bonus for the body part." );
						break;
					}
					env.value += (int)calcStatBonus(enchant,accCombatBonus,isBlessed,true,isTopGrade,false);
				}
				return;
			case MACCURACY_COMBAT:
				if(item.isArmor() && enchant > _limit1)
				{
					double maccCombatBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart,true);
					if(maccCombatBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no Magic Accuracy bonus for the body part." );
						break;
					}
					env.value += (int)calcStatBonus(enchant,maccCombatBonus,isBlessed,true,isTopGrade,false);
				}
				return;
			case EVASION_RATE:
				if(item.isArmor() && enchant > _limit1)
				{
					double evasionBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart,false);
					if(evasionBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no evasion bonus for the body part." );
						break;
					}
					env.value += (int)calcStatBonus(enchant,evasionBonus,isBlessed,true,isTopGrade,false);
				}
				return;
			case MEVASION_RATE:
				if(item.isArmor() && enchant > _limit1)
				{
					double mevasionBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart,false);
					if(mevasionBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no Magic Evasion bonus for the body part." );
						break;
					}
					env.value += (int)calcStatBonus(enchant,mevasionBonus,isBlessed,true,isTopGrade,false);
				}
				return;
			case MAGIC_ATTACK:
			{
				if(item.isWeapon())
				{
					double mAtkWeaponEnchant = EnchantStatBonusTable.getInstance().getWeaponStatBonus((WeaponType) itemType, crystal,true);
					env.value += (int)calcStatBonus(enchant,mAtkWeaponEnchant,isBlessed,false,isTopGrade,true);
				}
				else if(item.isArmor() && enchant > _limit1)
				{
					double mAtkArmorBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart,true);
					if(mAtkArmorBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no Magic Attack bonus for the body part." );
						break;
					}
					env.value += (int)calcStatBonus(enchant,mAtkArmorBonus,isBlessed,true,isTopGrade,false);
				}
				return;
			}
			case POWER_ATTACK:
			{
				if(item.isWeapon())
				{
					double pAtkWeaponEnchant = EnchantStatBonusTable.getInstance().getWeaponStatBonus((WeaponType) itemType, crystal,false);
					env.value += (int)calcStatBonus(enchant,pAtkWeaponEnchant,isBlessed,false,isTopGrade,true);					
				}
				else if(item.isArmor() && enchant > _limit1)
				{
					double pAtkArmorBonus = EnchantStatBonusTable.getInstance().getStatBonus(bodyPart,false);
					if(pAtkArmorBonus == 0)
					{
						_log.info("FuncEnchant: Error, item: " + item.getItemId() + " - " + item.getName() + " has no Physical Attack bonus for the body part." );
						break;
					}
					env.value += (int)calcStatBonus(enchant,pAtkArmorBonus,isBlessed, true,isTopGrade,false);					
				}
				return;
			}
			default:
				break;
		}
	}
	
	private double calcStatBonus(Integer enchantLevel, double enchantBonus, boolean isBlessedItem, boolean isArmorStat, boolean topgrade, boolean isWeapon)
	{
		double blessed = 1;
		if(isBlessedItem)
		{
			blessed = _blessedMultiplier;
		}
		double basicBonus = (enchantBonus * blessed);		
		if(!(Math.round((float)(basicBonus)) == 0) && !(Math.round((float)(basicBonus)) - (enchantBonus * blessed) < 0.5))
		{
			basicBonus = Math.round((float)(basicBonus)); 
		}
		if(enchantLevel <= _limit1)
		{	
			double result =  basicBonus * enchantLevel;
			result = Math.round((float)result);
			return result;			
		}
		else if (enchantLevel <= _limit2)
		{				
			double result = (basicBonus * (enchantLevel - _limit1)  * (!isArmorStat ? _overEnchantMul1 : 1));
			if(!isArmorStat)
			{
				result += basicBonus * _limit1;
			}
			result = Math.round((float)result);			
			return result;
		}
		else if (enchantLevel > _limit2 && (!(isWeapon && (enchantLevel > _limit3) && topgrade)))
		{			
			double result = basicBonus * (_limit2 - _limit1) * (!isArmorStat ? _overEnchantMul1 : 1) + basicBonus * (enchantLevel - _limit2) * (!isArmorStat && topgrade ? _overEnchantMul2 : _overEnchantMul1);
			if(!isArmorStat)
			{
				result += basicBonus * _limit1;
			}
			result = Math.round((float)result);
			return result; 
		}
		else if(isWeapon && (enchantLevel > _limit3 && enchantLevel <= _limit4) && topgrade)
		{
			double result = basicBonus * _limit1 + basicBonus * (_limit2 - _limit1) * (!isArmorStat ? _overEnchantMul1 : 1) + basicBonus * (_limit3 - _limit2) * _overEnchantMul2 + basicBonus * (enchantLevel - _limit3) * _overEnchantMul3;
			result = Math.round((float)result);
			return result;	
		}
		else if(isWeapon && (enchantLevel > _limit4) && topgrade)
		{
			double result = basicBonus * _limit1 + basicBonus * (_limit2 - _limit1) * (!isArmorStat ? _overEnchantMul1 : 1) + basicBonus * (_limit3 - _limit2) * _overEnchantMul2 + basicBonus * (_limit4 - _limit3) * _overEnchantMul3 + basicBonus * (enchantLevel - _limit4) * _overEnchantMul4;
			result = Math.round((float)result);
			return result;				
		}		
		return 0;
	}		
}

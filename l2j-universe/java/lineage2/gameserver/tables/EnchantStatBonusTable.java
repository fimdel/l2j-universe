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
package lineage2.gameserver.tables;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.enchantStat;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class EnchantStatBonusTable
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(EnchantStatBonusTable.class);
	
	private static HashMap <Integer, List<enchantStat>> _enchantStatListArmor = new HashMap<Integer,List <enchantStat>>();
	
	private static HashMap <WeaponType, List<enchantStat>> _enchantStatListWeapon = new HashMap<WeaponType,List <enchantStat>>();

	private List<enchantStat> _enchantDefenseList;
	
	private static EnchantStatBonusTable _instance = new EnchantStatBonusTable();
	
	/**
	 * Method getInstance.
	 * @return EnchantHPBonusTable
	 */
	public static EnchantStatBonusTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new EnchantStatBonusTable();
		}
		return _instance;
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		_enchantStatListArmor.clear();
		_instance = new EnchantStatBonusTable();
	}
	
	/**
	 * Constructor for EnchantHPBonusTable.
	 */
	private EnchantStatBonusTable()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/enchant_stats.xml");
			Document doc = factory.newDocumentBuilder().parse(file);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					List<enchantStat> defense = new ArrayList<enchantStat>();
					List<enchantStat> chest = new ArrayList<enchantStat>();
					List<enchantStat> legging = new ArrayList<enchantStat>();
					List<enchantStat> head = new ArrayList<enchantStat>();
					List<enchantStat> hand = new ArrayList<enchantStat>();
					List<enchantStat> feet = new ArrayList<enchantStat>();
					
					List<enchantStat> sword = new ArrayList<enchantStat>();
					List<enchantStat> blunt = new ArrayList<enchantStat>();
					List<enchantStat> rapier = new ArrayList<enchantStat>();
					List<enchantStat> ancient = new ArrayList<enchantStat>();
					List<enchantStat> pole = new ArrayList<enchantStat>();
					List<enchantStat> dagger = new ArrayList<enchantStat>();
					List<enchantStat> dualdagger = new ArrayList<enchantStat>();
					List<enchantStat> crossbow = new ArrayList<enchantStat>();
					
					List<enchantStat> bigsword = new ArrayList<enchantStat>();
					List<enchantStat> bigblunt = new ArrayList<enchantStat>();
					List<enchantStat> dualsword = new ArrayList<enchantStat>();
					List<enchantStat> dualblunt = new ArrayList<enchantStat>();
					List<enchantStat> dualfist = new ArrayList<enchantStat>();

					List<enchantStat> bow = new ArrayList<enchantStat>();
					for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						Double opt1;
						Double opt2;
						Integer grade;
						NamedNodeMap attrs = d.getAttributes();
						Node att;
						if("enchant".equalsIgnoreCase(d.getNodeName()))
						{
							att = attrs.getNamedItem("grade");
							if(att == null)
							{	
								_log.info("EnchantStatBonusTable: Missing grade. skipping");
								continue;
							}
							if("R".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_R;
							}
							else if ("S".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_S;
							}
							else if ("A".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_A;
							}
							else if ("B".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_B;								
							}
							else if ("C".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_C;
							}
							else if ("D".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_D;
							}
							else if ("N".equalsIgnoreCase(att.getNodeValue()))
							{
								grade = ItemTemplate.CRYSTAL_NONE;
							}
							else
							{
								_log.info("EnchantStatBonusTable: Invalid Grade Value. Skipping");
								continue;
							}
							att = attrs.getNamedItem("opt1");
							if(att == null)
							{
								_log.info("EnchantStatBonusTable: Missing opt1. skipping");
								continue;
							}
							opt1 = Double.parseDouble(att.getNodeValue());
							att = attrs.getNamedItem("opt2");
							if(att == null)
							{
								opt2 = 0D;
							}
							else
							{
								opt2 = Double.parseDouble(att.getNodeValue());
							}
							att = attrs.getNamedItem("name");
							if(att == null)
							{	
								_log.info("EnchantStatBonusTable: Missing name. skipping");
								continue;
							}
							if("defense".equalsIgnoreCase(att.getNodeValue()))
							{
								defense.add(new enchantStat(grade,opt1,opt2));
							}
							else if("chest".equalsIgnoreCase(att.getNodeValue()))
							{
								chest.add(new enchantStat(grade,opt1,opt2));
							}
							else if ("legging".equalsIgnoreCase(att.getNodeValue()))
							{
								legging.add(new enchantStat(grade,opt1,opt2));
							}
							else if ("head".equalsIgnoreCase(att.getNodeValue()))
							{
								head.add(new enchantStat(grade,opt1,opt2));
							}
							else if ("hand".equalsIgnoreCase(att.getNodeValue()))
							{
								hand.add(new enchantStat(grade,opt1,opt2));
							}
							else if ("feet".equalsIgnoreCase(att.getNodeValue()))
							{
								feet.add(new enchantStat(grade,opt1,opt2));
							}
							else if ("sword".equalsIgnoreCase(att.getNodeValue()))
							{
								sword.add(new enchantStat(grade,opt1,opt2));
							}
							else if("blunt".equalsIgnoreCase(att.getNodeValue()))
							{
								blunt.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("rapier".equalsIgnoreCase(att.getNodeValue()))
							{
								rapier.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("ancient".equalsIgnoreCase(att.getNodeValue()))
							{
								ancient.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("pole".equalsIgnoreCase(att.getNodeValue()))
							{
								pole.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("dagger".equalsIgnoreCase(att.getNodeValue()))
							{
								dagger.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("crossbow".equalsIgnoreCase(att.getNodeValue()))
							{
								crossbow.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("dualdagger".equalsIgnoreCase(att.getNodeValue()))
							{
								dualdagger.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("bigsword".equalsIgnoreCase(att.getNodeValue()))
							{
								bigsword.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("bigblunt".equalsIgnoreCase(att.getNodeValue()))
							{
								bigblunt.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("dualsword".equalsIgnoreCase(att.getNodeValue()))
							{
								dualsword.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("dualblunt".equalsIgnoreCase(att.getNodeValue()))
							{
								dualblunt.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("dualfist".equalsIgnoreCase(att.getNodeValue()))
							{
								dualfist.add(new enchantStat(grade,opt1,opt2));								
							}
							else if("bow".equalsIgnoreCase(att.getNodeValue()))
							{
								bow.add(new enchantStat(grade,opt1,opt2));								
							}
							else
							{
								_log.info("EnchantStatBonusTable: invalid bonus name. skipping");
								continue;
							}
						}
					}
					_enchantDefenseList = defense;
					
					_enchantStatListArmor.put(ItemTemplate.SLOT_CHEST, chest);
					_enchantStatListArmor.put(ItemTemplate.SLOT_LEGS, legging);
					_enchantStatListArmor.put(ItemTemplate.SLOT_HEAD, head);
					_enchantStatListArmor.put(ItemTemplate.SLOT_GLOVES, hand);
					_enchantStatListArmor.put(ItemTemplate.SLOT_FEET, feet);
					_enchantStatListArmor.put(ItemTemplate.SLOT_CHEST, chest);
					
					_enchantStatListWeapon.put(WeaponType.SWORD, sword);
					_enchantStatListWeapon.put(WeaponType.BLUNT, blunt);
					_enchantStatListWeapon.put(WeaponType.RAPIER, rapier);
					_enchantStatListWeapon.put(WeaponType.ANCIENTSWORD, ancient);
					_enchantStatListWeapon.put(WeaponType.POLE, pole);
					_enchantStatListWeapon.put(WeaponType.DAGGER, dagger);
					_enchantStatListWeapon.put(WeaponType.DUALDAGGER, dualdagger);
					_enchantStatListWeapon.put(WeaponType.CROSSBOW, crossbow);
					
					_enchantStatListWeapon.put(WeaponType.BIGSWORD, bigsword);
					_enchantStatListWeapon.put(WeaponType.BIGBLUNT, bigblunt);
					_enchantStatListWeapon.put(WeaponType.DUAL, dualsword);
					_enchantStatListWeapon.put(WeaponType.DUALBLUNT, dualblunt);
					_enchantStatListWeapon.put(WeaponType.DUALFIST, dualfist);
					
					_enchantStatListWeapon.put(WeaponType.BOW, bow);
					int i = 0;
					for(List <enchantStat> iterator : _enchantStatListArmor.values())
					{
						i += iterator.size();
					}
					_log.info("EnchantStatBonusTable: Loaded " + i +" Armor enchant bonuses.");
					i = 0;
					for(List <enchantStat> iterator : _enchantStatListWeapon.values())
					{
						i += iterator.size();
					}
					_log.info("EnchantStatBonusTable: Loaded " + i +" Weapon enchant bonuses.");
				}
			}
			_log.info("EnchantStatBonusTable: Loaded enchant bonuses.");
		}
		catch (Exception e)
		{
			_log.warn("EnchantStatBonusTable: Lists could not be initialized.");
			e.printStackTrace();
		}
	}

	public final double getDefenseBonus(Integer CrystalType, Integer EnchantLevel, Integer OELimit)
	{
		for(enchantStat crystalType : _enchantDefenseList)
		{
			if(crystalType.getGrade() == CrystalType)
			{
				if(CrystalType != ItemTemplate.CRYSTAL_R)
				{
					if(EnchantLevel <= OELimit)
					{
						return crystalType.getOption1();
					}
					else
					{
						return crystalType.getOption2();
					}					
				}
				else
				{
					return crystalType.getOption1();				
				}
			}
		}
		_log.info("EnchantStatBonusTable: Incorrect Crystal Type");
		return 0;
	}

	public final double getStatBonus(Integer bodyPart, boolean isMagicStat)
	{
		List <enchantStat> bonus = _enchantStatListArmor.get(bodyPart);
		if(bonus == null)
		{
			_log.info("EnchantStatBonusTable: Incorrect part for bonus on bodyPart Integer: " + bodyPart);
			return 0;
		}
		for(enchantStat statBonus : bonus)
		{
			if(isMagicStat && statBonus.getOption2() > 0)
			{
				return statBonus.getOption2(); 
			}
			else if(!isMagicStat && statBonus.getOption1() > 0)
			{
				return statBonus.getOption1();				
			}
			else
			{
				_log.info("EnchantStatBonusTable: Incorrect armor bonus");
			}
		}
		return 0;
	}
	
	public final double getWeaponStatBonus(WeaponType weapon, Integer CrystalType, boolean isMagicStat)
	{
		List <enchantStat> bonus = _enchantStatListWeapon.get(weapon);
		if(bonus == null)
		{
			_log.info("EnchantStatBonusTable: Incorrect weapon for bonus");
			return 0;
		}
		double option1 = 0;
		double option2 = 0;
		for(enchantStat stats : bonus)
		{
			if(stats.getGrade() == CrystalType)
			{
				option1 = stats.getOption1();
				option2 = stats.getOption2();
				break;
			}
		}
		if(isMagicStat && option2 > 0)
		{
			return option2;
		}
		else if (!isMagicStat && option1 > 0)
		{
			return option1;
		}
		else
		{
			_log.info("EnchantStatBonusTable: Incorrect option values for weapon");
			return 0D;
		}
	}
	
}

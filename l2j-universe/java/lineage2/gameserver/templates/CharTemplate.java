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
package lineage2.gameserver.templates;

import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.player.StatAttributes;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharTemplate
{
	/**
	 * Field EMPTY_ATTRIBUTES.
	 */
	private static final int[] EMPTY_ATTRIBUTES = new int[6];
	/**
	 * Field _baseAttr.
	 */
	private final StatAttributes _baseAttr;
	/**
	 * Field _baseAtkRange.
	 */
	private final int _baseAtkRange;
	/**
	 * Field _baseHpMax.
	 */
	private final double _baseHpMax;
	/**
	 * Field _baseCpMax.
	 */
	private final double _baseCpMax;
	/**
	 * Field _baseMpMax.
	 */
	private final double _baseMpMax;
	/**
	 * Field _baseHpReg.
	 */
	private final double _baseHpReg;
	/**
	 * Field _baseMpReg.
	 */
	private final double _baseMpReg;
	/**
	 * Field _baseCpReg.
	 */
	private final double _baseCpReg;
	/**
	 * Field _basePAtk.
	 */
	private final double _basePAtk;
	/**
	 * Field _baseMAtk.
	 */
	private final double _baseMAtk;
	/**
	 * Field _basePDef.
	 */
	private final double _basePDef;
	/**
	 * Field _baseMDef.
	 */
	private final double _baseMDef;
	/**
	 * Field _basePAtkSpd.
	 */
	private final double _basePAtkSpd;
	/**
	 * Field _baseMAtkSpd.
	 */
	private final double _baseMAtkSpd;
	/**
	 * Field _baseShldDef.
	 */
	private final double _baseShldDef;
	/**
	 * Field _baseShldRate.
	 */
	private final double _baseShldRate;
	/**
	 * Field _baseCritRate.
	 */
	private final double _baseCritRate;
	/**
	 * Field _baseRunSpd.
	 */
	private final int _baseRunSpd;
	/**
	 * Field _baseWalkSpd.
	 */
	private final double _baseWalkSpd;
	/**
	 * Field _baseWaterRunSpd.
	 */
	private final double _baseWaterRunSpd;
	/**
	 * Field _baseWaterWalkSpd.
	 */
	private final double _baseWaterWalkSpd;
	/**
	 * Field _baseAttributeAttack.
	 */
	private final int[] _baseAttributeAttack;
	/**
	 * Field _baseAttributeDefence.
	 */
	private final int[] _baseAttributeDefence;
	/**
	 * Field _collisionRadius.
	 */
	private final double _collisionRadius;
	/**
	 * Field _collisionHeight.
	 */
	private final double _collisionHeight;
	/**
	 * Field _baseAttackType.
	 */
	private final WeaponTemplate.WeaponType _baseAttackType;
	
	/**
	 * Constructor for CharTemplate.
	 * @param set StatsSet
	 */
	public CharTemplate(StatsSet set)
	{
		_baseAttr = new StatAttributes(set.getInteger("baseINT", 0), set.getInteger("baseSTR", 0), set.getInteger("baseCON", 0), set.getInteger("baseMEN", 0), set.getInteger("baseDEX", 0), set.getInteger("baseWIT", 0));
		_baseHpMax = set.getDouble("baseHpMax", 0);
		_baseCpMax = set.getDouble("baseCpMax", 0);
		_baseMpMax = set.getDouble("baseMpMax", 0);
		_baseHpReg = set.getDouble("baseHpReg", 3.e-3f);
		_baseCpReg = set.getDouble("baseCpReg", 3.e-3f);
		_baseMpReg = set.getDouble("baseMpReg", 3.e-3f);
		_basePAtk = set.getDouble("basePAtk");
		_baseMAtk = set.getDouble("baseMAtk");
		_basePDef = set.getDouble("basePDef", 100);
		_baseMDef = set.getDouble("baseMDef", 100);
		_basePAtkSpd = set.getDouble("basePAtkSpd");
		_baseMAtkSpd = set.getDouble("baseMAtkSpd");
		_baseShldDef = set.getDouble("baseShldDef", 0);
		_baseAtkRange = set.getInteger("baseAtkRange");
		_baseShldRate = set.getDouble("baseShldRate", 0);
		_baseCritRate = set.getDouble("baseCritRate");
		_baseRunSpd = set.getInteger("baseRunSpd");
		_baseWalkSpd = set.getDouble("baseWalkSpd");
		_baseWaterRunSpd = set.getDouble("baseWaterRunSpd", 50);
		_baseWaterWalkSpd = set.getDouble("baseWaterWalkSpd", 50);
		_baseAttributeAttack = set.getIntegerArray("baseAttributeAttack", EMPTY_ATTRIBUTES);
		_baseAttributeDefence = set.getIntegerArray("baseAttributeDefence", EMPTY_ATTRIBUTES);
		_collisionRadius = set.getDouble("collision_radius", 5);
		_collisionHeight = set.getDouble("collision_height", 5);
		_baseAttackType = WeaponTemplate.WeaponType.valueOf(set.getString("baseAttackType", "FIST").toUpperCase());
	}
	
	/**
	 * Method getBaseAttr.
	 * @return StatAttributes
	 */
	public StatAttributes getBaseAttr()
	{
		return _baseAttr;
	}
	
	/**
	 * Method getBaseHpMax.
	 * @return double
	 */
	public double getBaseHpMax()
	{
		return _baseHpMax;
	}
	
	/**
	 * Method getBaseCpMax.
	 * @return double
	 */
	public double getBaseCpMax()
	{
		return _baseCpMax;
	}
	
	/**
	 * Method getBaseMpMax.
	 * @return double
	 */
	public double getBaseMpMax()
	{
		return _baseMpMax;
	}
	
	/**
	 * Method getBaseHpReg.
	 * @return double
	 */
	public double getBaseHpReg()
	{
		return _baseHpReg;
	}
	
	/**
	 * Method getBaseMpReg.
	 * @return double
	 */
	public double getBaseMpReg()
	{
		return _baseMpReg;
	}
	
	/**
	 * Method getBaseCpReg.
	 * @return double
	 */
	public double getBaseCpReg()
	{
		return _baseCpReg;
	}
	
	/**
	 * Method getBasePAtk.
	 * @return double
	 */
	public double getBasePAtk()
	{
		return _basePAtk;
	}
	
	/**
	 * Method getBaseMAtk.
	 * @return double
	 */
	public double getBaseMAtk()
	{
		return _baseMAtk;
	}
	
	/**
	 * Method getBasePDef.
	 * @return double
	 */
	public double getBasePDef()
	{
		return _basePDef;
	}
	
	/**
	 * Method getBaseMDef.
	 * @return double
	 */
	public double getBaseMDef()
	{
		return _baseMDef;
	}
	
	/**
	 * Method getBasePAtkSpd.
	 * @return double
	 */
	public double getBasePAtkSpd()
	{
		return _basePAtkSpd;
	}
	
	/**
	 * Method getBaseMAtkSpd.
	 * @return double
	 */
	public double getBaseMAtkSpd()
	{
		return _baseMAtkSpd;
	}
	
	/**
	 * Method getBaseShldDef.
	 * @return double
	 */
	public double getBaseShldDef()
	{
		return _baseShldDef;
	}
	
	/**
	 * Method getBaseAtkRange.
	 * @return int
	 */
	public int getBaseAtkRange()
	{
		return _baseAtkRange;
	}
	
	/**
	 * Method getBaseShldRate.
	 * @return double
	 */
	public double getBaseShldRate()
	{
		return _baseShldRate;
	}
	
	/**
	 * Method getBaseCritRate.
	 * @return double
	 */
	public double getBaseCritRate()
	{
		return _baseCritRate;
	}
	
	/**
	 * Method getBaseRunSpd.
	 * @return int
	 */
	public int getBaseRunSpd()
	{
		return _baseRunSpd;
	}
	
	/**
	 * Method getBaseWalkSpd.
	 * @return int
	 */
	public int getBaseWalkSpd()
	{
		return (int) _baseWalkSpd;
	}
	
	/**
	 * Method getBaseWaterRunSpd.
	 * @return int
	 */
	public int getBaseWaterRunSpd()
	{
		return (int) _baseWaterRunSpd;
	}
	
	/**
	 * Method getBaseWaterWalkSpd.
	 * @return int
	 */
	public int getBaseWaterWalkSpd()
	{
		return (int) _baseWaterWalkSpd;
	}
	
	/**
	 * Method getBaseAttributeAttack.
	 * @return int[]
	 */
	public int[] getBaseAttributeAttack()
	{
		return _baseAttributeAttack;
	}
	
	/**
	 * Method getBaseAttributeDefence.
	 * @return int[]
	 */
	public int[] getBaseAttributeDefence()
	{
		return _baseAttributeDefence;
	}
	
	/**
	 * Method getCollisionRadius.
	 * @return double
	 */
	public double getCollisionRadius()
	{
		return _collisionRadius;
	}
	
	/**
	 * Method getCollisionHeight.
	 * @return double
	 */
	public double getCollisionHeight()
	{
		return _collisionHeight;
	}
	
	/**
	 * Method getBaseAttackType.
	 * @return WeaponTemplate.WeaponType
	 */
	public WeaponTemplate.WeaponType getBaseAttackType()
	{
		return _baseAttackType;
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	public int getNpcId()
	{
		return 0;
	}
	
	/**
	 * Method getEmptyStatsSet.
	 * @return StatsSet
	 */
	public static StatsSet getEmptyStatsSet()
	{
		StatsSet npcDat = new StatsSet();
		npcDat.set("baseSTR", 0);
		npcDat.set("baseCON", 0);
		npcDat.set("baseDEX", 0);
		npcDat.set("baseINT", 0);
		npcDat.set("baseWIT", 0);
		npcDat.set("baseMEN", 0);
		npcDat.set("baseHpMax", 0);
		npcDat.set("baseCpMax", 0);
		npcDat.set("baseMpMax", 0);
		npcDat.set("baseHpReg", 3.e-3f);
		npcDat.set("baseCpReg", 0);
		npcDat.set("baseMpReg", 3.e-3f);
		npcDat.set("basePAtk", 0);
		npcDat.set("baseMAtk", 0);
		npcDat.set("basePDef", 100);
		npcDat.set("baseMDef", 100);
		npcDat.set("basePAtkSpd", 0);
		npcDat.set("baseMAtkSpd", 0);
		npcDat.set("baseShldDef", 0);
		npcDat.set("baseAtkRange", 0);
		npcDat.set("baseShldRate", 0);
		npcDat.set("baseCritRate", 0);
		npcDat.set("baseRunSpd", 0);
		npcDat.set("baseWalkSpd", 0);
		return npcDat;
	}
}

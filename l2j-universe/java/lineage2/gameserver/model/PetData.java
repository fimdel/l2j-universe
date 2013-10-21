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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetData
{
	/**
	 * Field _id.
	 */
	private int _id;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _feedMax.
	 */
	private int _feedMax;
	/**
	 * Field _feedBattle.
	 */
	private int _feedBattle;
	/**
	 * Field _feedNormal.
	 */
	private int _feedNormal;
	/**
	 * Field _pAtk.
	 */
	private int _pAtk;
	/**
	 * Field _pDef.
	 */
	private int _pDef;
	/**
	 * Field _mAtk.
	 */
	private int _mAtk;
	/**
	 * Field _mDef.
	 */
	private int _mDef;
	/**
	 * Field _hp.
	 */
	private int _hp;
	/**
	 * Field _mp.
	 */
	private int _mp;
	/**
	 * Field _hpRegen.
	 */
	private int _hpRegen;
	/**
	 * Field _mpRegen.
	 */
	private int _mpRegen;
	/**
	 * Field _exp.
	 */
	private long _exp;
	/**
	 * Field _accuracy.
	 */
	private int _accuracy;
	/**
	 * Field _evasion.
	 */
	private int _evasion;
	/**
	 * Field _critical.
	 */
	private int _critical;
	/**
	 * Field _speed.
	 */
	private int _speed;
	/**
	 * Field _atkSpeed.
	 */
	private int _atkSpeed;
	/**
	 * Field _castSpeed.
	 */
	private int _castSpeed;
	/**
	 * Field _maxLoad.
	 */
	private int _maxLoad;
	/**
	 * Field _controlItemId.
	 */
	private int _controlItemId;
	/**
	 * Field _foodId.
	 */
	private int _foodId;
	/**
	 * Field _minLevel.
	 */
	private int _minLevel;
	/**
	 * Field _addFed.
	 */
	private int _addFed;
	/**
	 * Field _isMountable.
	 */
	private boolean _isMountable;
	
	/**
	 * Method getFeedBattle.
	 * @return int
	 */
	public int getFeedBattle()
	{
		return _feedBattle;
	}
	
	/**
	 * Method setFeedBattle.
	 * @param feedBattle int
	 */
	public void setFeedBattle(int feedBattle)
	{
		_feedBattle = feedBattle;
	}
	
	/**
	 * Method getFeedNormal.
	 * @return int
	 */
	public int getFeedNormal()
	{
		return _feedNormal;
	}
	
	/**
	 * Method setFeedNormal.
	 * @param feedNormal int
	 */
	public void setFeedNormal(int feedNormal)
	{
		_feedNormal = feedNormal;
	}
	
	/**
	 * Method getHP.
	 * @return int
	 */
	public int getHP()
	{
		return _hp;
	}
	
	/**
	 * Method setHP.
	 * @param petHP int
	 */
	public void setHP(int petHP)
	{
		_hp = petHP;
	}
	
	/**
	 * Method getID.
	 * @return int
	 */
	public int getID()
	{
		return _id;
	}
	
	/**
	 * Method setID.
	 * @param petID int
	 */
	public void setID(int petID)
	{
		_id = petID;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method setLevel.
	 * @param petLevel int
	 */
	public void setLevel(int petLevel)
	{
		_level = petLevel;
	}
	
	/**
	 * Method getMAtk.
	 * @return int
	 */
	public int getMAtk()
	{
		return _mAtk;
	}
	
	/**
	 * Method setMAtk.
	 * @param mAtk int
	 */
	public void setMAtk(int mAtk)
	{
		_mAtk = mAtk;
	}
	
	/**
	 * Method getFeedMax.
	 * @return int
	 */
	public int getFeedMax()
	{
		return _feedMax;
	}
	
	/**
	 * Method setFeedMax.
	 * @param feedMax int
	 */
	public void setFeedMax(int feedMax)
	{
		_feedMax = feedMax;
	}
	
	/**
	 * Method getMDef.
	 * @return int
	 */
	public int getMDef()
	{
		return _mDef;
	}
	
	/**
	 * Method setMDef.
	 * @param mDef int
	 */
	public void setMDef(int mDef)
	{
		_mDef = mDef;
	}
	
	/**
	 * Method getExp.
	 * @return long
	 */
	public long getExp()
	{
		return _exp;
	}
	
	/**
	 * Method setExp.
	 * @param exp long
	 */
	public void setExp(long exp)
	{
		_exp = exp;
	}
	
	/**
	 * Method getMP.
	 * @return int
	 */
	public int getMP()
	{
		return _mp;
	}
	
	/**
	 * Method setMP.
	 * @param mp int
	 */
	public void setMP(int mp)
	{
		_mp = mp;
	}
	
	/**
	 * Method getPAtk.
	 * @return int
	 */
	public int getPAtk()
	{
		return _pAtk;
	}
	
	/**
	 * Method setPAtk.
	 * @param pAtk int
	 */
	public void setPAtk(int pAtk)
	{
		_pAtk = pAtk;
	}
	
	/**
	 * Method getPDef.
	 * @return int
	 */
	public int getPDef()
	{
		return _pDef;
	}
	
	/**
	 * Method getAccuracy.
	 * @return int
	 */
	public int getAccuracy()
	{
		return _accuracy;
	}
	
	/**
	 * Method getEvasion.
	 * @return int
	 */
	public int getEvasion()
	{
		return _evasion;
	}
	
	/**
	 * Method getCritical.
	 * @return int
	 */
	public int getCritical()
	{
		return _critical;
	}
	
	/**
	 * Method getSpeed.
	 * @return int
	 */
	public int getSpeed()
	{
		return _speed;
	}
	
	/**
	 * Method getAtkSpeed.
	 * @return int
	 */
	public int getAtkSpeed()
	{
		return _atkSpeed;
	}
	
	/**
	 * Method getCastSpeed.
	 * @return int
	 */
	public int getCastSpeed()
	{
		return _castSpeed;
	}
	
	/**
	 * Method getMaxLoad.
	 * @return int
	 */
	public int getMaxLoad()
	{
		return _maxLoad != 0 ? _maxLoad : _level * 300;
	}
	
	/**
	 * Method setPDef.
	 * @param pDef int
	 */
	public void setPDef(int pDef)
	{
		_pDef = pDef;
	}
	
	/**
	 * Method getHpRegen.
	 * @return int
	 */
	public int getHpRegen()
	{
		return _hpRegen;
	}
	
	/**
	 * Method setHpRegen.
	 * @param hpRegen int
	 */
	public void setHpRegen(int hpRegen)
	{
		_hpRegen = hpRegen;
	}
	
	/**
	 * Method getMpRegen.
	 * @return int
	 */
	public int getMpRegen()
	{
		return _mpRegen;
	}
	
	/**
	 * Method setMpRegen.
	 * @param mpRegen int
	 */
	public void setMpRegen(int mpRegen)
	{
		_mpRegen = mpRegen;
	}
	
	/**
	 * Method setAccuracy.
	 * @param accuracy int
	 */
	public void setAccuracy(int accuracy)
	{
		_accuracy = accuracy;
	}
	
	/**
	 * Method setEvasion.
	 * @param evasion int
	 */
	public void setEvasion(int evasion)
	{
		_evasion = evasion;
	}
	
	/**
	 * Method setCritical.
	 * @param critical int
	 */
	public void setCritical(int critical)
	{
		_critical = critical;
	}
	
	/**
	 * Method setSpeed.
	 * @param speed int
	 */
	public void setSpeed(int speed)
	{
		_speed = speed;
	}
	
	/**
	 * Method setAtkSpeed.
	 * @param atkSpeed int
	 */
	public void setAtkSpeed(int atkSpeed)
	{
		_atkSpeed = atkSpeed;
	}
	
	/**
	 * Method setCastSpeed.
	 * @param castSpeed int
	 */
	public void setCastSpeed(int castSpeed)
	{
		_castSpeed = castSpeed;
	}
	
	/**
	 * Method setMaxLoad.
	 * @param maxLoad int
	 */
	public void setMaxLoad(int maxLoad)
	{
		_maxLoad = maxLoad;
	}
	
	/**
	 * Method getControlItemId.
	 * @return int
	 */
	public int getControlItemId()
	{
		return _controlItemId;
	}
	
	/**
	 * Method setControlItemId.
	 * @param itemId int
	 */
	public void setControlItemId(int itemId)
	{
		_controlItemId = itemId;
	}
	
	/**
	 * Method getFoodId.
	 * @return int
	 */
	public int getFoodId()
	{
		return _foodId;
	}
	
	/**
	 * Method setFoodId.
	 * @param id int
	 */
	public void setFoodId(int id)
	{
		_foodId = id;
	}
	
	/**
	 * Method getMinLevel.
	 * @return int
	 */
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	/**
	 * Method setMinLevel.
	 * @param level int
	 */
	public void setMinLevel(int level)
	{
		_minLevel = level;
	}
	
	/**
	 * Method getAddFed.
	 * @return int
	 */
	public int getAddFed()
	{
		return _addFed;
	}
	
	/**
	 * Method setAddFed.
	 * @param addFed int
	 */
	public void setAddFed(int addFed)
	{
		_addFed = addFed;
	}
	
	/**
	 * Method isMountable.
	 * @return boolean
	 */
	public boolean isMountable()
	{
		return _isMountable;
	}
	
	/**
	 * Method setMountable.
	 * @param mountable boolean
	 */
	public void setMountable(boolean mountable)
	{
		_isMountable = mountable;
	}
}

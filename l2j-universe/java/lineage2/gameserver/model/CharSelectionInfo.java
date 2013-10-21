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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharSelectionInfo implements Iterable<CharSelectionInfo.CharSelectInfoPackage>
{
	/**
	 * Field selectionInfo.
	 */
	private final List<CharSelectInfoPackage> selectionInfo;
	/**
	 * Field vitalityPoints.
	 */
	private int vitalityPoints;
	
	/**
	 * Constructor for CharSelectionInfo.
	 */
	public CharSelectionInfo()
	{
		selectionInfo = new ArrayList<>();
	}
	
	/**
	 * Method addSelectionInfo.
	 * @param info CharSelectInfoPackage
	 */
	public void addSelectionInfo(CharSelectInfoPackage info)
	{
		if (info != null)
		{
			selectionInfo.add(info);
		}
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<CharSelectInfoPackage> * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<CharSelectInfoPackage> iterator()
	{
		return selectionInfo.iterator();
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public int size()
	{
		return selectionInfo.size();
	}
	
	/**
	 * Method setVitalityPoints.
	 * @param vitalityPoints int
	 */
	public void setVitalityPoints(int vitalityPoints)
	{
		this.vitalityPoints = vitalityPoints;
	}
	
	/**
	 * Method getVitalityPoints.
	 * @return int
	 */
	public int getVitalityPoints()
	{
		return vitalityPoints;
	}
	
	/**
	 * @author Mobius
	 */
	public static class CharSelectInfoPackage
	{
		/**
		 * Field _name.
		 */
		private String _name;
		/**
		 * Field _objectId.
		 */
		private int _objectId = 0;
		/**
		 * Field _charId.
		 */
		private int _charId = 0x00030b7a;
		/**
		 * Field _exp.
		 */
		private long _exp = 0;
		/**
		 * Field _sp.
		 */
		private int _sp = 0;
		/**
		 * Field _clanId.
		 */
		private int _clanId = 0;
		/**
		 * Field _race.
		 */
		private int _race = 0;
		/**
		 * Field _classId.
		 */
		private int _classId = 0;
		/**
		 * Field _baseClassId.
		 */
		private int _baseClassId = 0;
		/**
		 * Field _deleteTimer.
		 */
		private int _deleteTimer = 0;
		/**
		 * Field _lastAccess.
		 */
		private long _lastAccess = 0L;
		/**
		 * Field _face.
		 */
		private int _face = 0;
		/**
		 * Field _hairStyle.
		 */
		private int _hairStyle = 0;
		/**
		 * Field _hairColor.
		 */
		private int _hairColor = 0;
		/**
		 * Field _sex.
		 */
		private int _sex = 0;
		/**
		 * Field _level.
		 */
		private int _level = 1;
		/**
		 * Field _pvp.
		 */
		/**
		 * Field _pk.
		 */
		/**
		 * Field _karma.
		 */
		private int _karma = 0, _pk = 0, _pvp = 0;
		/**
		 * Field _maxHp.
		 */
		private int _maxHp = 0;
		/**
		 * Field _currentHp.
		 */
		private double _currentHp = 0;
		/**
		 * Field _maxMp.
		 */
		private int _maxMp = 0;
		/**
		 * Field _currentMp.
		 */
		private double _currentMp = 0;
		/**
		 * Field _paperdoll.
		 */
		private final ItemInstance[] _paperdoll;
		/**
		 * Field _accesslevel.
		 */
		private int _accesslevel = 0;
		/**
		 * Field _z.
		 */
		/**
		 * Field _y.
		 */
		/**
		 * Field _x.
		 */
		private int _x = 0, _y = 0, _z = 0;
		
		/**
		 * Constructor for CharSelectInfoPackage.
		 * @param objectId int
		 * @param name String
		 */
		public CharSelectInfoPackage(int objectId, String name)
		{
			setObjectId(objectId);
			_name = name;
			Collection<ItemInstance> items = ItemsDAO.getInstance().getItemsByOwnerIdAndLoc(objectId, ItemInstance.ItemLocation.PAPERDOLL);
			_paperdoll = new ItemInstance[Inventory.PAPERDOLL_MAX];
			for (ItemInstance item : items)
			{
				if (item.getEquipSlot() < Inventory.PAPERDOLL_MAX)
				{
					_paperdoll[item.getEquipSlot()] = item;
				}
			}
		}
		
		/**
		 * Method getObjectId.
		 * @return int
		 */
		public int getObjectId()
		{
			return _objectId;
		}
		
		/**
		 * Method setObjectId.
		 * @param objectId int
		 */
		public void setObjectId(int objectId)
		{
			_objectId = objectId;
		}
		
		/**
		 * Method getCharId.
		 * @return int
		 */
		public int getCharId()
		{
			return _charId;
		}
		
		/**
		 * Method setCharId.
		 * @param charId int
		 */
		public void setCharId(int charId)
		{
			_charId = charId;
		}
		
		/**
		 * Method getClanId.
		 * @return int
		 */
		public int getClanId()
		{
			return _clanId;
		}
		
		/**
		 * Method setClanId.
		 * @param clanId int
		 */
		public void setClanId(int clanId)
		{
			_clanId = clanId;
		}
		
		/**
		 * Method getClassId.
		 * @return int
		 */
		public int getClassId()
		{
			return _classId;
		}
		
		/**
		 * Method getBaseClassId.
		 * @return int
		 */
		public int getBaseClassId()
		{
			return _baseClassId;
		}
		
		/**
		 * Method setBaseClassId.
		 * @param baseClassId int
		 */
		public void setBaseClassId(int baseClassId)
		{
			_baseClassId = baseClassId;
		}
		
		/**
		 * Method setClassId.
		 * @param classId int
		 */
		public void setClassId(int classId)
		{
			_classId = classId;
		}
		
		/**
		 * Method getCurrentHp.
		 * @return double
		 */
		public double getCurrentHp()
		{
			return _currentHp;
		}
		
		/**
		 * Method setCurrentHp.
		 * @param currentHp double
		 */
		public void setCurrentHp(double currentHp)
		{
			_currentHp = currentHp;
		}
		
		/**
		 * Method getCurrentMp.
		 * @return double
		 */
		public double getCurrentMp()
		{
			return _currentMp;
		}
		
		/**
		 * Method setCurrentMp.
		 * @param currentMp double
		 */
		public void setCurrentMp(double currentMp)
		{
			_currentMp = currentMp;
		}
		
		/**
		 * Method getDeleteTimer.
		 * @return int
		 */
		public int getDeleteTimer()
		{
			return _deleteTimer;
		}
		
		/**
		 * Method setDeleteTimer.
		 * @param deleteTimer int
		 */
		public void setDeleteTimer(int deleteTimer)
		{
			_deleteTimer = deleteTimer;
		}
		
		/**
		 * Method getLastAccess.
		 * @return long
		 */
		public long getLastAccess()
		{
			return _lastAccess;
		}
		
		/**
		 * Method setLastAccess.
		 * @param lastAccess long
		 */
		public void setLastAccess(long lastAccess)
		{
			_lastAccess = lastAccess;
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
		 * Method getFace.
		 * @return int
		 */
		public int getFace()
		{
			return _face;
		}
		
		/**
		 * Method setFace.
		 * @param face int
		 */
		public void setFace(int face)
		{
			_face = face;
		}
		
		/**
		 * Method getHairColor.
		 * @return int
		 */
		public int getHairColor()
		{
			return _hairColor;
		}
		
		/**
		 * Method setHairColor.
		 * @param hairColor int
		 */
		public void setHairColor(int hairColor)
		{
			_hairColor = hairColor;
		}
		
		/**
		 * Method getHairStyle.
		 * @return int
		 */
		public int getHairStyle()
		{
			return _hairStyle;
		}
		
		/**
		 * Method setHairStyle.
		 * @param hairStyle int
		 */
		public void setHairStyle(int hairStyle)
		{
			_hairStyle = hairStyle;
		}
		
		/**
		 * Method getPaperdollObjectId.
		 * @param slot int
		 * @return int
		 */
		public int getPaperdollObjectId(int slot)
		{
			ItemInstance item = _paperdoll[slot];
			if (item != null)
			{
				return item.getObjectId();
			}
			return 0;
		}
		
		/**
		 * Method getPaperdollAugmentationId.
		 * @param slot int
		 * @return int
		 */
		public int getPaperdollAugmentationId(int slot)
		{
			ItemInstance item = _paperdoll[slot];
			if ((item != null) && item.isAugmented())
			{
				return item.getAugmentationId();
			}
			return 0;
		}
		
		/**
		 * Method getPaperdollItemId.
		 * @param slot int
		 * @return int
		 */
		public int getPaperdollItemId(int slot)
		{
			ItemInstance item = _paperdoll[slot];
			if (item != null)
			{
				return item.getItemId();
			}
			return 0;
		}
		
		public int getVisualItemId(int slot)
		{
			ItemInstance item = _paperdoll[slot];
			if(item != null)
			{
				return item.getVisualId();
			}
			return 0;
		}

		/**
		 * Method getPaperdollEnchantEffect.
		 * @param slot int
		 * @return int
		 */
		public int getPaperdollEnchantEffect(int slot)
		{
			ItemInstance item = _paperdoll[slot];
			if (item != null)
			{
				return item.getEnchantLevel();
			}
			return 0;
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
		 * @param level int
		 */
		public void setLevel(int level)
		{
			_level = level;
		}
		
		/**
		 * Method getMaxHp.
		 * @return int
		 */
		public int getMaxHp()
		{
			return _maxHp;
		}
		
		/**
		 * Method setMaxHp.
		 * @param maxHp int
		 */
		public void setMaxHp(int maxHp)
		{
			_maxHp = maxHp;
		}
		
		/**
		 * Method getMaxMp.
		 * @return int
		 */
		public int getMaxMp()
		{
			return _maxMp;
		}
		
		/**
		 * Method setMaxMp.
		 * @param maxMp int
		 */
		public void setMaxMp(int maxMp)
		{
			_maxMp = maxMp;
		}
		
		/**
		 * Method getName.
		 * @return String
		 */
		public String getName()
		{
			return _name;
		}
		
		/**
		 * Method setName.
		 * @param name String
		 */
		public void setName(String name)
		{
			_name = name;
		}
		
		/**
		 * Method getRace.
		 * @return int
		 */
		public int getRace()
		{
			return _race;
		}
		
		/**
		 * Method setRace.
		 * @param race int
		 */
		public void setRace(int race)
		{
			_race = race;
		}
		
		/**
		 * Method getSex.
		 * @return int
		 */
		public int getSex()
		{
			return _sex;
		}
		
		/**
		 * Method setSex.
		 * @param sex int
		 */
		public void setSex(int sex)
		{
			_sex = sex;
		}
		
		/**
		 * Method getSp.
		 * @return int
		 */
		public int getSp()
		{
			return _sp;
		}
		
		/**
		 * Method setSp.
		 * @param sp int
		 */
		public void setSp(int sp)
		{
			_sp = sp;
		}
		
		/**
		 * Method getKarma.
		 * @return int
		 */
		public int getKarma()
		{
			return _karma;
		}
		
		/**
		 * Method setKarma.
		 * @param karma int
		 */
		public void setKarma(int karma)
		{
			_karma = karma;
		}
		
		/**
		 * Method getAccessLevel.
		 * @return int
		 */
		public int getAccessLevel()
		{
			return _accesslevel;
		}
		
		/**
		 * Method setAccessLevel.
		 * @param accesslevel int
		 */
		public void setAccessLevel(int accesslevel)
		{
			_accesslevel = accesslevel;
		}
		
		/**
		 * Method getX.
		 * @return int
		 */
		public int getX()
		{
			return _x;
		}
		
		/**
		 * Method setX.
		 * @param x int
		 */
		public void setX(int x)
		{
			_x = x;
		}
		
		/**
		 * Method getY.
		 * @return int
		 */
		public int getY()
		{
			return _y;
		}
		
		/**
		 * Method setY.
		 * @param y int
		 */
		public void setY(int y)
		{
			_y = y;
		}
		
		/**
		 * Method getZ.
		 * @return int
		 */
		public int getZ()
		{
			return _z;
		}
		
		/**
		 * Method setZ.
		 * @param z int
		 */
		public void setZ(int z)
		{
			_z = z;
		}
		
		/**
		 * Method getPk.
		 * @return int
		 */
		public int getPk()
		{
			return _pk;
		}
		
		/**
		 * Method setPk.
		 * @param pk int
		 */
		public void setPk(int pk)
		{
			_pk = pk;
		}
		
		/**
		 * Method getPvP.
		 * @return int
		 */
		public int getPvP()
		{
			return _pvp;
		}
		
		/**
		 * Method setPvP.
		 * @param pvp int
		 */
		public void setPvP(int pvp)
		{
			_pvp = pvp;
		}
	}
}

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
package lineage2.gameserver.model.items;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemInfo
{
	/**
	 * Field ownerId.
	 */
	private int ownerId;
	/**
	 * Field lastChange.
	 */
	private int lastChange;
	/**
	 * Field type1.
	 */
	private int type1;
	/**
	 * Field objectId.
	 */
	private int objectId;
	/**
	 * Field itemId.
	 */
	private int itemId;
	/**
	 * Field count.
	 */
	private long count;
	/**
	 * Field type2.
	 */
	private int type2;
	/**
	 * Field customType1.
	 */
	private int customType1;
	/**
	 * Field isEquipped.
	 */
	private boolean isEquipped;
	/**
	 * Field bodyPart.
	 */
	private int bodyPart;
	/**
	 * Field enchantLevel.
	 */
	private int enchantLevel;
	/**
	 * Field customType2.
	 */
	private int customType2;
	/**
	 * Field augmentationId.
	 */
	private int augmentationId;
	/**
	 * Field shadowLifeTime.
	 */
	private int shadowLifeTime;
	/**
	 * Field attackElement.
	 */
	private int attackElement = Element.NONE.getId();
	/**
	 * Field attackElementValue.
	 */
	private int attackElementValue;
	/**
	 * Field defenceFire.
	 */
	private int defenceFire;
	/**
	 * Field defenceWater.
	 */
	private int defenceWater;
	/**
	 * Field defenceWind.
	 */
	private int defenceWind;
	private int defenceEarth;
	private int defenceHoly;
	private int defenceUnholy;
	private int equipSlot;
	private int temporalLifeTime;
	private int[] enchantOptions = ItemInstance.EMPTY_ENCHANT_OPTIONS;
	private int visualId;

	private ItemTemplate item;
	
	/**
	 * Constructor for ItemInfo.
	 */
	public ItemInfo()
	{
	}
	
	/**
	 * Constructor for ItemInfo.
	 * @param item ItemInstance
	 */
	public ItemInfo(ItemInstance item)
	{
		setOwnerId(item.getOwnerId());
		setObjectId(item.getObjectId());
		setItemId(item.getItemId());
		setCount(item.getCount());
		setCustomType1(item.getCustomType1());
		setEquipped(item.isEquipped());
		setEnchantLevel(item.getEnchantLevel());
		setCustomType2(item.getCustomType2());
		setAugmentationId(item.getAugmentationId());
		setShadowLifeTime(item.getShadowLifeTime());
		setAttackElement(item.getAttackElement().getId());
		setAttackElementValue(item.getAttackElementValue());
		setDefenceFire(item.getDefenceFire());
		setDefenceWater(item.getDefenceWater());
		setDefenceWind(item.getDefenceWind());
		setDefenceEarth(item.getDefenceEarth());
		setDefenceHoly(item.getDefenceHoly());
		setDefenceUnholy(item.getDefenceUnholy());
		setEquipSlot(item.getEquipSlot());
		setTemporalLifeTime(item.getTemporalLifeTime());
		setEnchantOptions(item.getEnchantOptions());
		setVisualId(item.getVisualId());
	}
	
	/**
	 * Method getItem.
	 * @return ItemTemplate
	 */
	public ItemTemplate getItem()
	{
		return item;
	}
	
	/**
	 * Method setOwnerId.
	 * @param ownerId int
	 */
	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}
	
	/**
	 * Method setLastChange.
	 * @param lastChange int
	 */
	public void setLastChange(int lastChange)
	{
		this.lastChange = lastChange;
	}
	
	/**
	 * Method setType1.
	 * @param type1 int
	 */
	public void setType1(int type1)
	{
		this.type1 = type1;
	}
	
	/**
	 * Method setObjectId.
	 * @param objectId int
	 */
	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}
	
	/**
	 * Method setItemId.
	 * @param itemId int
	 */
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
		if (itemId > 0)
		{
			item = ItemHolder.getInstance().getTemplate(getItemId());
		}
		else
		{
			item = null;
		}
		if (item != null)
		{
			setType1(item.getType1());
			setType2(item.getType2ForPackets());
			setBodyPart(item.getBodyPart());
		}
	}
	
	/**
	 * Method setCount.
	 * @param count long
	 */
	public void setCount(long count)
	{
		this.count = count;
	}
	
	/**
	 * Method setType2.
	 * @param type2 int
	 */
	public void setType2(int type2)
	{
		this.type2 = type2;
	}
	
	/**
	 * Method setCustomType1.
	 * @param customType1 int
	 */
	public void setCustomType1(int customType1)
	{
		this.customType1 = customType1;
	}
	
	/**
	 * Method setEquipped.
	 * @param isEquipped boolean
	 */
	public void setEquipped(boolean isEquipped)
	{
		this.isEquipped = isEquipped;
	}
	
	/**
	 * Method setBodyPart.
	 * @param bodyPart int
	 */
	public void setBodyPart(int bodyPart)
	{
		this.bodyPart = bodyPart;
	}
	
	/**
	 * Method setEnchantLevel.
	 * @param enchantLevel int
	 */
	public void setEnchantLevel(int enchantLevel)
	{
		this.enchantLevel = enchantLevel;
	}
	
	/**
	 * Method setCustomType2.
	 * @param customType2 int
	 */
	public void setCustomType2(int customType2)
	{
		this.customType2 = customType2;
	}
	
	/**
	 * Method setAugmentationId.
	 * @param augmentationId int
	 */
	public void setAugmentationId(int augmentationId)
	{
		this.augmentationId = augmentationId;
	}
	
	/**
	 * Method setShadowLifeTime.
	 * @param shadowLifeTime int
	 */
	public void setShadowLifeTime(int shadowLifeTime)
	{
		this.shadowLifeTime = shadowLifeTime;
	}
	
	/**
	 * Method setAttackElement.
	 * @param attackElement int
	 */
	public void setAttackElement(int attackElement)
	{
		this.attackElement = attackElement;
	}
	
	/**
	 * Method setAttackElementValue.
	 * @param attackElementValue int
	 */
	public void setAttackElementValue(int attackElementValue)
	{
		this.attackElementValue = attackElementValue;
	}
	
	/**
	 * Method setDefenceFire.
	 * @param defenceFire int
	 */
	public void setDefenceFire(int defenceFire)
	{
		this.defenceFire = defenceFire;
	}
	
	/**
	 * Method setDefenceWater.
	 * @param defenceWater int
	 */
	public void setDefenceWater(int defenceWater)
	{
		this.defenceWater = defenceWater;
	}
	
	/**
	 * Method setDefenceWind.
	 * @param defenceWind int
	 */
	public void setDefenceWind(int defenceWind)
	{
		this.defenceWind = defenceWind;
	}
	
	/**
	 * Method setDefenceEarth.
	 * @param defenceEarth int
	 */
	public void setDefenceEarth(int defenceEarth)
	{
		this.defenceEarth = defenceEarth;
	}
	
	/**
	 * Method setDefenceHoly.
	 * @param defenceHoly int
	 */
	public void setDefenceHoly(int defenceHoly)
	{
		this.defenceHoly = defenceHoly;
	}
	
	/**
	 * Method setDefenceUnholy.
	 * @param defenceUnholy int
	 */
	public void setDefenceUnholy(int defenceUnholy)
	{
		this.defenceUnholy = defenceUnholy;
	}
	
	/**
	 * Method setEquipSlot.
	 * @param equipSlot int
	 */
	public void setEquipSlot(int equipSlot)
	{
		this.equipSlot = equipSlot;
	}
	
	/**
	 * Method setTemporalLifeTime.
	 * @param temporalLifeTime int
	 */
	public void setTemporalLifeTime(int temporalLifeTime)
	{
		this.temporalLifeTime = temporalLifeTime;
	}
	
	/**
	 * Method getOwnerId.
	 * @return int
	 */
	public int getOwnerId()
	{
		return ownerId;
	}
	
	/**
	 * Method getLastChange.
	 * @return int
	 */
	public int getLastChange()
	{
		return lastChange;
	}
	
	/**
	 * Method getType1.
	 * @return int
	 */
	public int getType1()
	{
		return type1;
	}
	
	/**
	 * Method getObjectId.
	 * @return int
	 */
	public int getObjectId()
	{
		return objectId;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return itemId;
	}
	
	/**
	 * Method getCount.
	 * @return long
	 */
	public long getCount()
	{
		return count;
	}
	
	/**
	 * Method getType2.
	 * @return int
	 */
	public int getType2()
	{
		return type2;
	}
	
	/**
	 * Method getCustomType1.
	 * @return int
	 */
	public int getCustomType1()
	{
		return customType1;
	}
	
	/**
	 * Method isEquipped.
	 * @return boolean
	 */
	public boolean isEquipped()
	{
		return isEquipped;
	}
	
	/**
	 * Method getBodyPart.
	 * @return int
	 */
	public int getBodyPart()
	{
		return bodyPart;
	}
	
	/**
	 * Method getEnchantLevel.
	 * @return int
	 */
	public int getEnchantLevel()
	{
		return enchantLevel;
	}
	
	/**
	 * Method getAugmentationId.
	 * @return int
	 */
	public int getAugmentationId()
	{
		return augmentationId;
	}
	
	/**
	 * Method getShadowLifeTime.
	 * @return int
	 */
	public int getShadowLifeTime()
	{
		return shadowLifeTime;
	}
	
	/**
	 * Method getCustomType2.
	 * @return int
	 */
	public int getCustomType2()
	{
		return customType2;
	}
	
	/**
	 * Method getAttackElement.
	 * @return int
	 */
	public int getAttackElement()
	{
		return attackElement;
	}
	
	/**
	 * Method getAttackElementValue.
	 * @return int
	 */
	public int getAttackElementValue()
	{
		return attackElementValue;
	}
	
	/**
	 * Method getDefenceFire.
	 * @return int
	 */
	public int getDefenceFire()
	{
		return defenceFire;
	}
	
	/**
	 * Method getDefenceWater.
	 * @return int
	 */
	public int getDefenceWater()
	{
		return defenceWater;
	}
	
	/**
	 * Method getDefenceWind.
	 * @return int
	 */
	public int getDefenceWind()
	{
		return defenceWind;
	}
	
	/**
	 * Method getDefenceEarth.
	 * @return int
	 */
	public int getDefenceEarth()
	{
		return defenceEarth;
	}
	
	/**
	 * Method getDefenceHoly.
	 * @return int
	 */
	public int getDefenceHoly()
	{
		return defenceHoly;
	}
	
	/**
	 * Method getDefenceUnholy.
	 * @return int
	 */
	public int getDefenceUnholy()
	{
		return defenceUnholy;
	}
	
	/**
	 * Method getEquipSlot.
	 * @return int
	 */
	public int getEquipSlot()
	{
		return equipSlot;
	}
	
	/**
	 * Method getTemporalLifeTime.
	 * @return int
	 */
	public int getTemporalLifeTime()
	{
		return temporalLifeTime;
	}
	
	/**
	 * Method equals.
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		if (getObjectId() == 0)
		{
			return getItemId() == ((ItemInfo) obj).getItemId();
		}
		return getObjectId() == ((ItemInfo) obj).getObjectId();
	}
	
	/**
	 * Method getEnchantOptions.
	 * @return int[]
	 */
	public int[] getEnchantOptions()
	{
		return enchantOptions;
	}
	
	/**
	 * Method setEnchantOptions.
	 * @param enchantOptions int[]
	 */
	public void setEnchantOptions(int[] enchantOptions)
	{
		this.enchantOptions = enchantOptions;
	}

	public int getVisualId()
	{
		return visualId;
	}

	public void setVisualId(int visualId)
	{
		this.visualId = visualId;
	}
}

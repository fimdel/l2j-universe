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
package lineage2.gameserver.model.reward;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.math.SafeMath;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RewardData implements Cloneable
{
	/**
	 * Field _item.
	 */
	private final ItemTemplate _item;
	/**
	 * Field _notRate.
	 */
	private boolean _notRate = false;
	/**
	 * Field _mindrop.
	 */
	private long _mindrop;
	/**
	 * Field _maxdrop.
	 */
	private long _maxdrop;
	/**
	 * Field _chance.
	 */
	private double _chance;
	/**
	 * Field _chanceInGroup.
	 */
	private double _chanceInGroup;
	
	/**
	 * Constructor for RewardData.
	 * @param itemId int
	 */
	public RewardData(int itemId)
	{
		_item = ItemHolder.getInstance().getTemplate(itemId);
		if (_item.isArrow() || (Config.NO_RATE_EQUIPMENT && _item.isEquipment()) || (Config.NO_RATE_KEY_MATERIAL && _item.isKeyMatherial()) || (Config.NO_RATE_RECIPES && _item.isRecipe()) || ArrayUtils.contains(Config.NO_RATE_ITEMS, itemId))
		{
			_notRate = true;
		}
	}
	
	/**
	 * Constructor for RewardData.
	 * @param itemId int
	 * @param min long
	 * @param max long
	 * @param chance double
	 */
	public RewardData(int itemId, long min, long max, double chance)
	{
		this(itemId);
		_mindrop = min;
		_maxdrop = max;
		_chance = chance;
	}
	
	/**
	 * Method notRate.
	 * @return boolean
	 */
	public boolean notRate()
	{
		return _notRate;
	}
	
	/**
	 * Method setNotRate.
	 * @param notRate boolean
	 */
	public void setNotRate(boolean notRate)
	{
		_notRate = notRate;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _item.getItemId();
	}
	
	/**
	 * Method getItem.
	 * @return ItemTemplate
	 */
	public ItemTemplate getItem()
	{
		return _item;
	}
	
	/**
	 * Method getMinDrop.
	 * @return long
	 */
	public long getMinDrop()
	{
		return _mindrop;
	}
	
	/**
	 * Method getMaxDrop.
	 * @return long
	 */
	public long getMaxDrop()
	{
		return _maxdrop;
	}
	
	/**
	 * Method getChance.
	 * @return double
	 */
	public double getChance()
	{
		return _chance;
	}
	
	/**
	 * Method setMinDrop.
	 * @param mindrop long
	 */
	public void setMinDrop(long mindrop)
	{
		_mindrop = mindrop;
	}
	
	/**
	 * Method setMaxDrop.
	 * @param maxdrop long
	 */
	public void setMaxDrop(long maxdrop)
	{
		_maxdrop = maxdrop;
	}
	
	/**
	 * Method setChance.
	 * @param chance double
	 */
	public void setChance(double chance)
	{
		_chance = chance;
	}
	
	/**
	 * Method setChanceInGroup.
	 * @param chance double
	 */
	public void setChanceInGroup(double chance)
	{
		_chanceInGroup = chance;
	}
	
	/**
	 * Method getChanceInGroup.
	 * @return double
	 */
	public double getChanceInGroup()
	{
		return _chanceInGroup;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "ItemID: " + getItem() + " Min: " + getMinDrop() + " Max: " + getMaxDrop() + " Chance: " + (getChance() / 10000.0) + "%";
	}
	
	/**
	 * Method clone.
	 * @return RewardData
	 */
	@Override
	public RewardData clone()
	{
		return new RewardData(getItemId(), getMinDrop(), getMaxDrop(), getChance());
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof RewardData)
		{
			RewardData drop = (RewardData) o;
			return drop.getItemId() == getItemId();
		}
		return false;
	}
	
	/**
	 * Method roll.
	 * @param player Player
	 * @param mod double
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(Player player, double mod)
	{
		double rate;
		if (_item.isAdena())
		{
			rate = (Config.RATE_DROP_ADENA + player.getVitalityBonus()) * player.getRateAdena();
		}
		else
		{
			rate = (Config.RATE_DROP_ITEMS + player.getVitalityBonus()) * (player.getRateItems());
		}
		return roll(rate * mod);
	}
	
	/**
	 * Method roll.
	 * @param rate double
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(double rate)
	{
		double mult = Math.ceil(rate);
		List<RewardItem> ret = new ArrayList<>(1);
		RewardItem t = null;
		long count;
		for (int n = 0; n < mult; n++)
		{
			if (Rnd.get(RewardList.MAX_CHANCE) <= (_chance * Math.min(rate - n, 1.0)))
			{
				if (getMinDrop() >= getMaxDrop())
				{
					count = getMinDrop();
				}
				else
				{
					count = Rnd.get(getMinDrop(), getMaxDrop());
				}
				if (t == null)
				{
					ret.add(t = new RewardItem(_item.getItemId()));
					t.count = count;
				}
				else
				{
					t.count = SafeMath.addAndLimit(t.count, count);
				}
			}
		}
		return ret;
	}
}

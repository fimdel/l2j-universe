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
import java.util.Collections;
import java.util.List;

import lineage2.commons.math.SafeMath;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RewardGroup implements Cloneable
{
	/**
	 * Field _chance.
	 */
	private double _chance;
	/**
	 * Field _isAdena.
	 */
	private boolean _isAdena = false;
	/**
	 * Field _notRate.
	 */
	private boolean _notRate = false;
	/**
	 * Field _items.
	 */
	private final List<RewardData> _items = new ArrayList<>();
	/**
	 * Field _chanceSum.
	 */
	private double _chanceSum;
	
	/**
	 * Constructor for RewardGroup.
	 * @param chance double
	 */
	public RewardGroup(double chance)
	{
		setChance(chance);
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
	 * Method getChance.
	 * @return double
	 */
	public double getChance()
	{
		return _chance;
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
	 * Method isAdena.
	 * @return boolean
	 */
	public boolean isAdena()
	{
		return _isAdena;
	}
	
	/**
	 * Method setIsAdena.
	 * @param isAdena boolean
	 */
	public void setIsAdena(boolean isAdena)
	{
		_isAdena = isAdena;
	}
	
	/**
	 * Method addData.
	 * @param item RewardData
	 */
	public void addData(RewardData item)
	{
		if (item.getItem().isAdena())
		{
			_isAdena = true;
		}
		_chanceSum += item.getChance();
		item.setChanceInGroup(_chanceSum);
		_items.add(item);
	}
	
	/**
	 * Method getItems.
	 * @return List<RewardData>
	 */
	public List<RewardData> getItems()
	{
		return _items;
	}
	
	/**
	 * Method clone.
	 * @return RewardGroup
	 */
	@Override
	public RewardGroup clone()
	{
		RewardGroup ret = new RewardGroup(_chance);
		for (RewardData i : _items)
		{
			ret.addData(i.clone());
		}
		return ret;
	}
	
	/**
	 * Method roll.
	 * @param type RewardType
	 * @param player Player
	 * @param mod double
	 * @param isRaid boolean
	 * @param isSiegeGuard boolean
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(RewardType type, Player player, double mod, boolean isRaid, boolean isSiegeGuard)
	{
		switch (type)
		{
			case NOT_RATED_GROUPED:
			case NOT_RATED_NOT_GROUPED:
				return rollItems(mod, 1.0, 1.0);
			case SWEEP:
				return rollItems(mod, (Config.RATE_DROP_SPOIL + player.getVitalityBonus()), player.getRateSpoil());
			case RATED_GROUPED:
				if (_isAdena)
				{
					return rollAdena(mod, (Config.RATE_DROP_ADENA + player.getVitalityBonus()), player.getRateAdena());
				}
				if (isRaid)
				{
					return rollItems(mod, (Config.RATE_DROP_RAIDBOSS + player.getVitalityBonus()), 1.0);
				}
				if (isSiegeGuard)
				{
					return rollItems(mod, (Config.RATE_DROP_SIEGE_GUARD + player.getVitalityBonus()), 1.0);
				}
				return rollItems(mod, (Config.RATE_DROP_ITEMS + player.getVitalityBonus()), player.getRateItems());
			default:
				return Collections.emptyList();
		}
	}
	
	/**
	 * Method rollItems.
	 * @param mod double
	 * @param baseRate double
	 * @param playerRate double
	 * @return List<RewardItem>
	 */
	public List<RewardItem> rollItems(double mod, double baseRate, double playerRate)
	{
		if (mod <= 0)
		{
			return Collections.emptyList();
		}
		double rate;
		if (_notRate)
		{
			rate = Math.min(mod, 1.0);
		}
		else
		{
			rate = baseRate * playerRate * mod;
		}
		double mult = Math.ceil(rate);
		List<RewardItem> ret = new ArrayList<>((int) (mult * _items.size()));
		for (long n = 0; n < mult; n++)
		{
			if (Rnd.get(1, RewardList.MAX_CHANCE) <= (_chance * Math.min(rate - n, 1.0)))
			{
				rollFinal(_items, ret, 1., Math.max(_chanceSum, RewardList.MAX_CHANCE));
			}
		}
		return ret;
	}
	
	/**
	 * Method rollAdena.
	 * @param mod double
	 * @param baseRate double
	 * @param playerRate double
	 * @return List<RewardItem>
	 */
	private List<RewardItem> rollAdena(double mod, double baseRate, double playerRate)
	{
		double chance = _chance;
		if (mod > 10)
		{
			mod *= _chance / RewardList.MAX_CHANCE;
			chance = RewardList.MAX_CHANCE;
		}
		if (mod <= 0)
		{
			return Collections.emptyList();
		}
		if (Rnd.get(1, RewardList.MAX_CHANCE) > chance)
		{
			return Collections.emptyList();
		}
		double rate = baseRate * playerRate * mod;
		List<RewardItem> ret = new ArrayList<>(_items.size());
		rollFinal(_items, ret, rate, Math.max(_chanceSum, RewardList.MAX_CHANCE));
		for (RewardItem i : ret)
		{
			i.isAdena = true;
		}
		return ret;
	}
	
	/**
	 * Method rollFinal.
	 * @param items List<RewardData>
	 * @param ret List<RewardItem>
	 * @param mult double
	 * @param chanceSum double
	 */
	private void rollFinal(List<RewardData> items, List<RewardItem> ret, double mult, double chanceSum)
	{
		int chance = Rnd.get(0, (int) chanceSum);
		long count;
		for (RewardData i : items)
		{
			if ((chance < i.getChanceInGroup()) && (chance > (i.getChanceInGroup() - i.getChance())))
			{
				double imult = i.notRate() ? 1.0 : mult;
				if (i.getMinDrop() >= i.getMaxDrop())
				{
					count = Math.round(i.getMinDrop() * imult);
				}
				else
				{
					count = Rnd.get(Math.round(i.getMinDrop() * imult), Math.round(i.getMaxDrop() * imult));
				}
				RewardItem t = null;
				for (RewardItem r : ret)
				{
					if (i.getItemId() == r.itemId)
					{
						t = r;
						break;
					}
				}
				if (t == null)
				{
					ret.add(t = new RewardItem(i.getItemId()));
					t.count = count;
				}
				else if (!i.notRate())
				{
					t.count = SafeMath.addAndLimit(t.count, count);
				}
				break;
			}
		}
	}
}

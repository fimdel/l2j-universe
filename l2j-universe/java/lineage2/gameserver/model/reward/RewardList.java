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

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class RewardList extends ArrayList<RewardGroup>
{
	/**
	 * Field MAX_CHANCE. (value is 1000000)
	 */
	public static final int MAX_CHANCE = 1000000;
	/**
	 * Field _type.
	 */
	private final RewardType _type;
	/**
	 * Field _autoLoot.
	 */
	private final boolean _autoLoot;
	
	/**
	 * Constructor for RewardList.
	 * @param rewardType RewardType
	 * @param a boolean
	 */
	public RewardList(RewardType rewardType, boolean a)
	{
		super(5);
		_type = rewardType;
		_autoLoot = a;
	}
	
	/**
	 * Method roll.
	 * @param player Player
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(Player player)
	{
		return roll(player, 1.0, false, false);
	}
	
	/**
	 * Method roll.
	 * @param player Player
	 * @param mod double
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(Player player, double mod)
	{
		return roll(player, mod, false, false);
	}
	
	/**
	 * Method roll.
	 * @param player Player
	 * @param mod double
	 * @param isRaid boolean
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(Player player, double mod, boolean isRaid)
	{
		return roll(player, mod, isRaid, false);
	}
	
	/**
	 * Method roll.
	 * @param player Player
	 * @param mod double
	 * @param isRaid boolean
	 * @param isSiegeGuard boolean
	 * @return List<RewardItem>
	 */
	public List<RewardItem> roll(Player player, double mod, boolean isRaid, boolean isSiegeGuard)
	{
		List<RewardItem> temp = new ArrayList<>(size());
		for (RewardGroup g : this)
		{
			List<RewardItem> tdl = g.roll(_type, player, mod, isRaid, isSiegeGuard);
			if (!tdl.isEmpty())
			{
				for (RewardItem itd : tdl)
				{
					temp.add(itd);
				}
			}
		}
		return temp;
	}
	
	/**
	 * Method validate.
	 * @return boolean
	 */
	public boolean validate()
	{
		for (RewardGroup g : this)
		{
			int chanceSum = 0;
			for (RewardData d : g.getItems())
			{
				chanceSum += d.getChance();
			}
			if (chanceSum <= MAX_CHANCE)
			{
				return true;
			}
			double mod = MAX_CHANCE / chanceSum;
			for (RewardData d : g.getItems())
			{
				double chance = d.getChance() * mod;
				d.setChance(chance);
				g.setChance(MAX_CHANCE);
			}
		}
		return false;
	}
	
	/**
	 * Method isAutoLoot.
	 * @return boolean
	 */
	public boolean isAutoLoot()
	{
		return _autoLoot;
	}
	
	/**
	 * Method getType.
	 * @return RewardType
	 */
	public RewardType getType()
	{
		return _type;
	}
}

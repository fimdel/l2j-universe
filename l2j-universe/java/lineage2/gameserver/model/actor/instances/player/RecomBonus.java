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
package lineage2.gameserver.model.actor.instances.player;

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RecomBonus
{
	/**
	 * Field _recoBonus.
	 */
	private static final int[][] _recoBonus =
	{
		{
			25,
			50,
			50,
			50,
			50,
			50,
			50,
			50,
			50,
			50
		},
		{
			16,
			33,
			50,
			50,
			50,
			50,
			50,
			50,
			50,
			50
		},
		{
			12,
			25,
			37,
			50,
			50,
			50,
			50,
			50,
			50,
			50
		},
		{
			10,
			20,
			30,
			40,
			50,
			50,
			50,
			50,
			50,
			50
		},
		{
			8,
			16,
			25,
			33,
			41,
			50,
			50,
			50,
			50,
			50
		},
		{
			7,
			14,
			21,
			28,
			35,
			42,
			50,
			50,
			50,
			50
		},
		{
			6,
			12,
			18,
			25,
			31,
			37,
			43,
			50,
			50,
			50
		},
		{
			5,
			11,
			16,
			22,
			27,
			33,
			38,
			44,
			50,
			50
		},
		{
			5,
			10,
			15,
			20,
			25,
			30,
			35,
			40,
			45,
			50
		},
		{
			4,
			9,
			14,
			18,
			23,
			28,
			33,
			37,
			40,
			50
		}
	};
	
	/**
	 * Method getRecoBonus.
	 * @param activeChar Player
	 * @return int
	 */
	public static int getRecoBonus(Player activeChar)
	{
		if ((activeChar != null) && activeChar.isOnline())
		{
			if (activeChar.getRecomHave() == 0)
			{
				return 0;
			}
			int _lvl = (int) Math.ceil(activeChar.getLevel() / 10);
			int _exp = (int) Math.ceil((Math.min(100, activeChar.getRecomHave()) - 1) / 10);
			return _recoBonus[_lvl][_exp];
		}
		return 0;
	}
	
	/**
	 * Method getRecoMultiplier.
	 * @param activeChar Player
	 * @return double
	 */
	public static double getRecoMultiplier(Player activeChar)
	{
		double bonus = getRecoBonus(activeChar);
		if (bonus > 0)
		{
			return 1. + (bonus / 100);
		}
		return 1.;
	}
}

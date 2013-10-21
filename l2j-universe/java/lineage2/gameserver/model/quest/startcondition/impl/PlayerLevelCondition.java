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
package lineage2.gameserver.model.quest.startcondition.impl;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.startcondition.ICheckStartCondition;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PlayerLevelCondition implements ICheckStartCondition
{
	/**
	 * Field min.
	 */
	private final int min;
	/**
	 * Field max.
	 */
	private final int max;
	
	/**
	 * Constructor for PlayerLevelCondition.
	 * @param min int
	 * @param max int
	 */
	public PlayerLevelCondition(int min, int max)
	{
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Method checkCondition.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.quest.startcondition.ICheckStartCondition#checkCondition(Player)
	 */
	@Override
	public final boolean checkCondition(Player player)
	{
		return (player.getLevel() >= min) && (player.getLevel() <= max);
	}
}

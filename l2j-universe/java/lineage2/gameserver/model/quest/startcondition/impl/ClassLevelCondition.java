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

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClassLevelCondition implements ICheckStartCondition
{
	/**
	 * Field classLevels.
	 */
	private final int[] classLevels;
	
	/**
	 * Constructor for ClassLevelCondition.
	 * @param classLevels int[]
	 */
	public ClassLevelCondition(int... classLevels)
	{
		this.classLevels = classLevels;
	}
	
	/**
	 * Method checkCondition.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.quest.startcondition.ICheckStartCondition#checkCondition(Player)
	 */
	@Override
	public boolean checkCondition(Player player)
	{
		return ArrayUtils.contains(classLevels, player.getClassId().getClassLevel().ordinal());
	}
}

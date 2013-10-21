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
public class SubClassCondition implements ICheckStartCondition
{
	/**
	 * Constructor for SubClassCondition.
	 */
	public SubClassCondition()
	{
	}
	
	/**
	 * Method checkCondition.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.quest.startcondition.ICheckStartCondition#checkCondition(Player)
	 */
	@Override
	public boolean checkCondition(Player player)
	{
		return player.getBaseClassId() != player.getClassId().getId();
	}
}

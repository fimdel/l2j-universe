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
package lineage2.gameserver.model.items.attachment;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public interface FlagItemAttachment extends PickableAttachment
{
	/**
	 * Method onLogout.
	 * @param player Player
	 */
	void onLogout(Player player);
	
	/**
	 * Method onDeath.
	 * @param owner Player
	 * @param killer Creature
	 */
	void onDeath(Player owner, Creature killer);
	
	/**
	 * Method canAttack.
	 * @param player Player
	 * @return boolean
	 */
	boolean canAttack(Player player);
	
	/**
	 * Method canCast.
	 * @param player Player
	 * @param skill Skill
	 * @return boolean
	 */
	boolean canCast(Player player, Skill skill);
}

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
package lineage2.gameserver.stats.triggers;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum TriggerType
{
	/**
	 * Field ADD.
	 */
	ADD,
	/**
	 * Field ATTACK.
	 */
	ATTACK,
	/**
	 * Field RECEIVE_DAMAGE.
	 */
	RECEIVE_DAMAGE,
	/**
	 * Field CRIT.
	 */
	CRIT,
	/**
	 * Field OFFENSIVE_PHYSICAL_SKILL_USE.
	 */
	OFFENSIVE_PHYSICAL_SKILL_USE,
	/**
	 * Field OFFENSIVE_MAGICAL_SKILL_USE.
	 */
	OFFENSIVE_MAGICAL_SKILL_USE,
	/**
	 * Field SUPPORT_MAGICAL_SKILL_USE.
	 */
	SUPPORT_MAGICAL_SKILL_USE,
	/**
	 * Field UNDER_MISSED_ATTACK.
	 */
	UNDER_MISSED_ATTACK,
	/**
	 * Field DIE.
	 */
	DIE,
	/**
	 * Field IDLE.
	 */
	IDLE,
}

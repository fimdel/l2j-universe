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
package lineage2.gameserver.model.entity.olympiad;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum BattleStatus
{
	/**
	 * Field Begining.
	 */
	Begining,
	/**
	 * Field Begin_Countdown.
	 */
	Begin_Countdown,
	/**
	 * Field PortPlayers.
	 */
	PortPlayers,
	/**
	 * Field Started.
	 */
	Started,
	/**
	 * Field CountDown.
	 */
	CountDown,
	/**
	 * Field StartComp.
	 */
	StartComp,
	/**
	 * Field ValidateWinner.
	 */
	ValidateWinner,
	/**
	 * Field Ending.
	 */
	Ending
}

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
package lineage2.gameserver.taskmanager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum TaskTypes
{
	/**
	 * Field TYPE_NONE.
	 */
	TYPE_NONE,
	/**
	 * Field TYPE_TIME.
	 */
	TYPE_TIME,
	/**
	 * Field TYPE_SHEDULED.
	 */
	TYPE_SHEDULED,
	/**
	 * Field TYPE_FIXED_SHEDULED.
	 */
	TYPE_FIXED_SHEDULED,
	/**
	 * Field TYPE_GLOBAL_TASK.
	 */
	TYPE_GLOBAL_TASK,
	/**
	 * Field TYPE_STARTUP.
	 */
	TYPE_STARTUP,
	/**
	 * Field TYPE_SPECIAL.
	 */
	TYPE_SPECIAL
}

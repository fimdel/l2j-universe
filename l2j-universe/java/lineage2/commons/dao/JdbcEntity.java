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
package lineage2.commons.dao;

import java.io.Serializable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public interface JdbcEntity extends Serializable
{
	/**
	 * Method setJdbcState.
	 * @param state JdbcEntityState
	 */
	public void setJdbcState(JdbcEntityState state);
	
	/**
	 * Method getJdbcState.
	 * @return JdbcEntityState
	 */
	public JdbcEntityState getJdbcState();
	
	/**
	 * Method save.
	 */
	public void save();
	
	/**
	 * Method update.
	 */
	public void update();
	
	/**
	 * Method delete.
	 */
	public void delete();
}

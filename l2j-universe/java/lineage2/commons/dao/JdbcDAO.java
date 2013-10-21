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
public interface JdbcDAO<K extends Serializable, E extends JdbcEntity>
{
	/**
	 * Method load.
	 * @param key K
	 * @return E
	 */
	public E load(K key);
	
	/**
	 * Method save.
	 * @param e E
	 */
	public void save(E e);
	
	/**
	 * Method update.
	 * @param e E
	 */
	public void update(E e);
	
	/**
	 * Method saveOrUpdate.
	 * @param e E
	 */
	public void saveOrUpdate(E e);
	
	/**
	 * Method delete.
	 * @param e E
	 */
	public void delete(E e);
	
	/**
	 * Method getStats.
	 * @return JdbcEntityStats
	 */
	public JdbcEntityStats getStats();
}

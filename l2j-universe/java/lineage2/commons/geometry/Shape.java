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
package lineage2.commons.geometry;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public interface Shape
{
	/**
	 * Method isInside.
	 * @param x int
	 * @param y int
	 * @return boolean
	 */
	public boolean isInside(int x, int y);
	
	/**
	 * Method isInside.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return boolean
	 */
	public boolean isInside(int x, int y, int z);
	
	/**
	 * Method getXmax.
	 * @return int
	 */
	public int getXmax();
	
	/**
	 * Method getXmin.
	 * @return int
	 */
	public int getXmin();
	
	/**
	 * Method getYmax.
	 * @return int
	 */
	public int getYmax();
	
	/**
	 * Method getYmin.
	 * @return int
	 */
	public int getYmin();
	
	/**
	 * Method getZmax.
	 * @return int
	 */
	public int getZmax();
	
	/**
	 * Method getZmin.
	 * @return int
	 */
	public int getZmin();
}

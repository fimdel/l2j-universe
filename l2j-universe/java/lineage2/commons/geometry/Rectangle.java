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
public class Rectangle extends AbstractShape
{
	/**
	 * Constructor for Rectangle.
	 * @param x1 int
	 * @param y1 int
	 * @param x2 int
	 * @param y2 int
	 */
	public Rectangle(int x1, int y1, int x2, int y2)
	{
		min.x = Math.min(x1, x2);
		min.y = Math.min(y1, y2);
		max.x = Math.max(x1, x2);
		max.y = Math.max(y1, y2);
	}
	
	/**
	 * Method setZmax.
	 * @param z int
	 * @return Rectangle
	 */
	@Override
	public Rectangle setZmax(int z)
	{
		max.z = z;
		return this;
	}
	
	/**
	 * Method setZmin.
	 * @param z int
	 * @return Rectangle
	 */
	@Override
	public Rectangle setZmin(int z)
	{
		min.z = z;
		return this;
	}
	
	/**
	 * Method isInside.
	 * @param x int
	 * @param y int
	 * @return boolean * @see lineage2.commons.geometry.Shape#isInside(int, int)
	 */
	@Override
	public boolean isInside(int x, int y)
	{
		return (x >= min.x) && (x <= max.x) && (y >= min.y) && (y <= max.y);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(min).append(", ").append(max);
		sb.append(']');
		return sb.toString();
	}
}

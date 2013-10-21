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
public class Circle extends AbstractShape
{
	/**
	 * Field c.
	 */
	protected final Point2D c;
	/**
	 * Field r.
	 */
	protected final int r;
	
	/**
	 * Constructor for Circle.
	 * @param center Point2D
	 * @param radius int
	 */
	public Circle(Point2D center, int radius)
	{
		c = center;
		r = radius;
		min.x = (c.x - r);
		max.x = (c.x + r);
		min.y = (c.y - r);
		max.y = (c.y + r);
	}
	
	/**
	 * Constructor for Circle.
	 * @param x int
	 * @param y int
	 * @param radius int
	 */
	public Circle(int x, int y, int radius)
	{
		this(new Point2D(x, y), radius);
	}
	
	/**
	 * Method setZmax.
	 * @param z int
	 * @return Circle
	 */
	@Override
	public Circle setZmax(int z)
	{
		max.z = z;
		return this;
	}
	
	/**
	 * Method setZmin.
	 * @param z int
	 * @return Circle
	 */
	@Override
	public Circle setZmin(int z)
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
		return (((x - c.x) * (c.x - x)) + ((y - c.y) * (c.y - y))) <= (r * r);
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
		sb.append(c).append("{ radius: ").append(r).append('}');
		sb.append(']');
		return sb.toString();
	}
}

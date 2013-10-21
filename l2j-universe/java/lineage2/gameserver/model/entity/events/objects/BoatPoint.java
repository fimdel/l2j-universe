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
package lineage2.gameserver.model.entity.events.objects;

import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BoatPoint extends Location
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _speed1.
	 */
	private int _speed1;
	/**
	 * Field _speed2.
	 */
	private int _speed2;
	/**
	 * Field _fuel.
	 */
	private final int _fuel;
	/**
	 * Field _teleport.
	 */
	private boolean _teleport;
	
	/**
	 * Constructor for BoatPoint.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param h int
	 * @param speed1 int
	 * @param speed2 int
	 * @param fuel int
	 * @param teleport boolean
	 */
	public BoatPoint(int x, int y, int z, int h, int speed1, int speed2, int fuel, boolean teleport)
	{
		super(x, y, z, h);
		_speed1 = speed1;
		_speed2 = speed2;
		_fuel = fuel;
		_teleport = teleport;
	}
	
	/**
	 * Method getSpeed1.
	 * @return int
	 */
	public int getSpeed1()
	{
		return _speed1;
	}
	
	/**
	 * Method getSpeed2.
	 * @return int
	 */
	public int getSpeed2()
	{
		return _speed2;
	}
	
	/**
	 * Method getFuel.
	 * @return int
	 */
	public int getFuel()
	{
		return _fuel;
	}
	
	/**
	 * Method isTeleport.
	 * @return boolean
	 */
	public boolean isTeleport()
	{
		return _teleport;
	}
	
	/**
	 * Method parse.
	 * @param element Element
	 * @return BoatPoint
	 */
	public static BoatPoint parse(Element element)
	{
		int speed1 = element.attributeValue("speed1") == null ? 0 : Integer.parseInt(element.attributeValue("speed1"));
		int speed2 = element.attributeValue("speed2") == null ? 0 : Integer.parseInt(element.attributeValue("speed2"));
		int x = Integer.parseInt(element.attributeValue("x"));
		int y = Integer.parseInt(element.attributeValue("y"));
		int z = Integer.parseInt(element.attributeValue("z"));
		int h = element.attributeValue("h") == null ? 0 : Integer.parseInt(element.attributeValue("h"));
		int fuel = element.attributeValue("fuel") == null ? 0 : Integer.parseInt(element.attributeValue("fuel"));
		boolean teleport = Boolean.parseBoolean(element.attributeValue("teleport"));
		return new BoatPoint(x, y, z, h, speed1, speed2, fuel, teleport);
	}
	
	/**
	 * Method setSpeed1.
	 * @param speed1 int
	 */
	public void setSpeed1(int speed1)
	{
		_speed1 = speed1;
	}
	
	/**
	 * Method setSpeed2.
	 * @param speed2 int
	 */
	public void setSpeed2(int speed2)
	{
		_speed2 = speed2;
	}
	
	/**
	 * Method setTeleport.
	 * @param teleport boolean
	 */
	public void setTeleport(boolean teleport)
	{
		_teleport = teleport;
	}
}

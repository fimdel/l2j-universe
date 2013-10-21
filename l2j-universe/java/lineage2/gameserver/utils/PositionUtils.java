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
package lineage2.gameserver.utils;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PositionUtils
{
	/**
	 * @author Mobius
	 */
	public enum TargetDirection
	{
		/**
		 * Field NONE.
		 */
		NONE,
		/**
		 * Field FRONT.
		 */
		FRONT,
		/**
		 * Field SIDE.
		 */
		SIDE,
		/**
		 * Field BEHIND.
		 */
		BEHIND
	}
	
	/**
	 * Field MAX_ANGLE. (value is 360)
	 */
	private static final int MAX_ANGLE = 360;
	/**
	 * Field FRONT_MAX_ANGLE. (value is 100.0)
	 */
	private static final double FRONT_MAX_ANGLE = 100;
	/**
	 * Field BACK_MAX_ANGLE. (value is 40.0)
	 */
	private static final double BACK_MAX_ANGLE = 40;
	
	/**
	 * Method getDirectionTo.
	 * @param target Creature
	 * @param attacker Creature
	 * @return TargetDirection
	 */
	public static TargetDirection getDirectionTo(Creature target, Creature attacker)
	{
		if ((target == null) || (attacker == null))
		{
			return TargetDirection.NONE;
		}
		if (isBehind(target, attacker))
		{
			return TargetDirection.BEHIND;
		}
		if (isInFrontOf(target, attacker))
		{
			return TargetDirection.FRONT;
		}
		return TargetDirection.SIDE;
	}
	
	/**
	 * Method isInFrontOf.
	 * @param target Creature
	 * @param attacker Creature
	 * @return boolean
	 */
	public static boolean isInFrontOf(Creature target, Creature attacker)
	{
		if (target == null)
		{
			return false;
		}
		double angleChar, angleTarget, angleDiff;
		angleTarget = calculateAngleFrom(target, attacker);
		angleChar = convertHeadingToDegree(target.getHeading());
		angleDiff = angleChar - angleTarget;
		if (angleDiff <= (-MAX_ANGLE + FRONT_MAX_ANGLE))
		{
			angleDiff += MAX_ANGLE;
		}
		if (angleDiff >= (MAX_ANGLE - FRONT_MAX_ANGLE))
		{
			angleDiff -= MAX_ANGLE;
		}
		if (Math.abs(angleDiff) <= FRONT_MAX_ANGLE)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method isBehind.
	 * @param target Creature
	 * @param attacker Creature
	 * @return boolean
	 */
	public static boolean isBehind(Creature target, Creature attacker)
	{
		if (target == null)
		{
			return false;
		}
		double angleChar, angleTarget, angleDiff;
		angleChar = calculateAngleFrom(attacker, target);
		angleTarget = convertHeadingToDegree(target.getHeading());
		angleDiff = angleChar - angleTarget;
		if (angleDiff <= (-MAX_ANGLE + BACK_MAX_ANGLE))
		{
			angleDiff += MAX_ANGLE;
		}
		if (angleDiff >= (MAX_ANGLE - BACK_MAX_ANGLE))
		{
			angleDiff -= MAX_ANGLE;
		}
		if (Math.abs(angleDiff) <= BACK_MAX_ANGLE)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method isFacing.
	 * @param attacker Creature
	 * @param target GameObject
	 * @param maxAngle int
	 * @return boolean
	 */
	public static boolean isFacing(Creature attacker, GameObject target, int maxAngle)
	{
		double angleChar, angleTarget, angleDiff, maxAngleDiff;
		if (target == null)
		{
			return false;
		}
		maxAngleDiff = maxAngle / 2;
		angleTarget = calculateAngleFrom(attacker, target);
		angleChar = convertHeadingToDegree(attacker.getHeading());
		angleDiff = angleChar - angleTarget;
		if (angleDiff <= (-360 + maxAngleDiff))
		{
			angleDiff += 360;
		}
		if (angleDiff >= (360 - maxAngleDiff))
		{
			angleDiff -= 360;
		}
		if (Math.abs(angleDiff) <= maxAngleDiff)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method calculateHeadingFrom.
	 * @param obj1 GameObject
	 * @param obj2 GameObject
	 * @return int
	 */
	public static int calculateHeadingFrom(GameObject obj1, GameObject obj2)
	{
		return calculateHeadingFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}
	
	/**
	 * Method calculateHeadingFrom.
	 * @param obj1X int
	 * @param obj1Y int
	 * @param obj2X int
	 * @param obj2Y int
	 * @return int
	 */
	public static int calculateHeadingFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
	{
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0)
		{
			angleTarget = MAX_ANGLE + angleTarget;
		}
		return (int) (angleTarget * 182.044444444);
	}
	
	/**
	 * Method calculateAngleFrom.
	 * @param obj1 GameObject
	 * @param obj2 GameObject
	 * @return double
	 */
	public static double calculateAngleFrom(GameObject obj1, GameObject obj2)
	{
		return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}
	
	/**
	 * Method calculateAngleFrom.
	 * @param obj1X int
	 * @param obj1Y int
	 * @param obj2X int
	 * @param obj2Y int
	 * @return double
	 */
	public static double calculateAngleFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
	{
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0)
		{
			angleTarget = 360 + angleTarget;
		}
		return angleTarget;
	}
	
	/**
	 * Method checkIfInRange.
	 * @param range int
	 * @param x1 int
	 * @param y1 int
	 * @param x2 int
	 * @param y2 int
	 * @return boolean
	 */
	public static boolean checkIfInRange(int range, int x1, int y1, int x2, int y2)
	{
		return checkIfInRange(range, x1, y1, 0, x2, y2, 0, false);
	}
	
	/**
	 * Method checkIfInRange.
	 * @param range int
	 * @param x1 int
	 * @param y1 int
	 * @param z1 int
	 * @param x2 int
	 * @param y2 int
	 * @param z2 int
	 * @param includeZAxis boolean
	 * @return boolean
	 */
	public static boolean checkIfInRange(int range, int x1, int y1, int z1, int x2, int y2, int z2, boolean includeZAxis)
	{
		long dx = x1 - x2;
		long dy = y1 - y2;
		if (includeZAxis)
		{
			long dz = z1 - z2;
			return ((dx * dx) + (dy * dy) + (dz * dz)) <= (range * range);
		}
		return ((dx * dx) + (dy * dy)) <= (range * range);
	}
	
	/**
	 * Method checkIfInRange.
	 * @param range int
	 * @param obj1 GameObject
	 * @param obj2 GameObject
	 * @param includeZAxis boolean
	 * @return boolean
	 */
	public static boolean checkIfInRange(int range, GameObject obj1, GameObject obj2, boolean includeZAxis)
	{
		if ((obj1 == null) || (obj2 == null))
		{
			return false;
		}
		return checkIfInRange(range, obj1.getX(), obj1.getY(), obj1.getZ(), obj2.getX(), obj2.getY(), obj2.getZ(), includeZAxis);
	}
	
	/**
	 * Method convertHeadingToDegree.
	 * @param heading int
	 * @return double
	 */
	public static double convertHeadingToDegree(int heading)
	{
		return heading / 182.044444444;
	}
	
	/**
	 * Method convertHeadingToRadian.
	 * @param heading int
	 * @return double
	 */
	public static double convertHeadingToRadian(int heading)
	{
		return Math.toRadians(convertHeadingToDegree(heading) - 90);
	}
	
	/**
	 * Method convertDegreeToClientHeading.
	 * @param degree double
	 * @return int
	 */
	public static int convertDegreeToClientHeading(double degree)
	{
		if (degree < 0)
		{
			degree = 360 + degree;
		}
		return (int) (degree * 182.044444444);
	}
	
	/**
	 * Method calculateDistance.
	 * @param x1 int
	 * @param y1 int
	 * @param z1 int
	 * @param x2 int
	 * @param y2 int
	 * @return double
	 */
	public static double calculateDistance(int x1, int y1, int z1, int x2, int y2)
	{
		return calculateDistance(x1, y1, 0, x2, y2, 0, false);
	}
	
	/**
	 * Method calculateDistance.
	 * @param x1 int
	 * @param y1 int
	 * @param z1 int
	 * @param x2 int
	 * @param y2 int
	 * @param z2 int
	 * @param includeZAxis boolean
	 * @return double
	 */
	public static double calculateDistance(int x1, int y1, int z1, int x2, int y2, int z2, boolean includeZAxis)
	{
		long dx = x1 - x2;
		long dy = y1 - y2;
		if (includeZAxis)
		{
			long dz = z1 - z2;
			return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		}
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	
	/**
	 * Method calculateDistance.
	 * @param obj1 GameObject
	 * @param obj2 GameObject
	 * @param includeZAxis boolean
	 * @return double
	 */
	public static double calculateDistance(GameObject obj1, GameObject obj2, boolean includeZAxis)
	{
		if ((obj1 == null) || (obj2 == null))
		{
			return Integer.MAX_VALUE;
		}
		return calculateDistance(obj1.getX(), obj1.getY(), obj1.getZ(), obj2.getX(), obj2.getY(), obj2.getZ(), includeZAxis);
	}
	
	/**
	 * Method getDistance.
	 * @param a1 GameObject
	 * @param a2 GameObject
	 * @return double
	 */
	public static double getDistance(GameObject a1, GameObject a2)
	{
		return getDistance(a1.getX(), a2.getY(), a2.getX(), a2.getY());
	}
	
	/**
	 * Method getDistance.
	 * @param loc1 Location
	 * @param loc2 Location
	 * @return double
	 */
	public static double getDistance(Location loc1, Location loc2)
	{
		return getDistance(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
	}
	
	/**
	 * Method getDistance.
	 * @param x1 int
	 * @param y1 int
	 * @param x2 int
	 * @param y2 int
	 * @return double
	 */
	public static double getDistance(int x1, int y1, int x2, int y2)
	{
		return Math.hypot(x1 - x2, y1 - y2);
	}
	
	/**
	 * Method getHeadingTo.
	 * @param actor GameObject
	 * @param target GameObject
	 * @return int
	 */
	public static int getHeadingTo(GameObject actor, GameObject target)
	{
		if ((actor == null) || (target == null) || (target == actor))
		{
			return -1;
		}
		return getHeadingTo(actor.getLoc(), target.getLoc());
	}
	
	/**
	 * Method getHeadingTo.
	 * @param actor Location
	 * @param target Location
	 * @return int
	 */
	public static int getHeadingTo(Location actor, Location target)
	{
		if ((actor == null) || (target == null) || target.equals(actor))
		{
			return -1;
		}
		int dx = target.x - actor.x;
		int dy = target.y - actor.y;
		int heading = target.h - (int) ((Math.atan2(-dy, -dx) * Creature.HEADINGS_IN_PI) + 32768);
		if (heading < 0)
		{
			heading = (heading + 1 + Integer.MAX_VALUE) & 0xFFFF;
		}
		else if (heading > 0xFFFF)
		{
			heading &= 0xFFFF;
		}
		return heading;
	}
}

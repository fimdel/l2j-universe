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
package lineage2.commons.util;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.math.random.MersenneTwister;
import org.apache.commons.math.random.RandomGenerator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Rnd
{
	/**
	 * Constructor for Rnd.
	 */
	private Rnd()
	{
	}
	
	/**
	 * Field random.
	 */
	private static final Random random = new Random();
	/**
	 * Field rnd.
	 */
	private static final ThreadLocal<RandomGenerator> rnd = new ThreadLocalGeneratorHolder();
	/**
	 * Field seedUniquifier.
	 */
	static AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
	
	/**
	 * @author Mobius
	 */
	static final class ThreadLocalGeneratorHolder extends ThreadLocal<RandomGenerator>
	{
		/**
		 * Method initialValue.
		 * @return RandomGenerator
		 */
		@Override
		public RandomGenerator initialValue()
		{
			return new MersenneTwister(seedUniquifier.getAndIncrement() + System.nanoTime());
		}
	}
	
	/**
	 * Method rnd.
	 * @return RandomGenerator
	 */
	private static RandomGenerator rnd()
	{
		return rnd.get();
	}
	
	/**
	 * Method get.
	 * @return double
	 */
	public static double get()
	{
		return rnd().nextDouble();
	}
	
	/**
	 * Method get.
	 * @param n int
	 * @return int
	 */
	public static int get(int n)
	{
		return rnd().nextInt(n);
	}
	
	/**
	 * Method get.
	 * @param n long
	 * @return long
	 */
	public static long get(long n)
	{
		return (long) (rnd().nextDouble() * n);
	}
	
	/**
	 * Method get.
	 * @param min int
	 * @param max int
	 * @return int
	 */
	public static int get(int min, int max)
	{
		return min + get((max - min) + 1);
	}
	
	/**
	 * Method get.
	 * @param min long
	 * @param max long
	 * @return long
	 */
	public static long get(long min, long max)
	{
		return min + get((max - min) + 1);
	}
	
	/**
	 * Method nextInt.
	 * @return int
	 */
	public static int nextInt()
	{
		return rnd().nextInt();
	}
	
	/**
	 * Method nextDouble.
	 * @return double
	 */
	public static double nextDouble()
	{
		return rnd().nextDouble();
	}
	
	/**
	 * Method nextGaussian.
	 * @return double
	 */
	public static double nextGaussian()
	{
		return rnd().nextGaussian();
	}
	
	/**
	 * Method nextBoolean.
	 * @return boolean
	 */
	public static boolean nextBoolean()
	{
		return rnd().nextBoolean();
	}
	
	/**
	 * Method chance.
	 * @param chance int
	 * @return boolean
	 */
	public static boolean chance(int chance)
	{
		return (chance >= 1) && ((chance > 99) || ((rnd().nextInt(99) + 1) <= chance));
	}
	
	/**
	 * Method chance.
	 * @param chance double
	 * @return boolean
	 */
	public static boolean chance(double chance)
	{
		return rnd().nextDouble() <= (chance / 100.);
	}
	
	/**
	 * Method get.
	 * @param list E[]
	 * @return E
	 */
	public static <E> E get(E[] list)
	{
		return list[get(list.length)];
	}
	
	/**
	 * Method get.
	 * @param list int[]
	 * @return int
	 */
	public static int get(int[] list)
	{
		return list[get(list.length)];
	}
	
	/**
	 * Method get.
	 * @param list List<E>
	 * @return E
	 */
	public static <E> E get(List<E> list)
	{
		return list.get(get(list.size()));
	}
	
	/**
	 * Method nextBytes.
	 * @param array byte[]
	 * @return byte[]
	 */
	public static byte[] nextBytes(byte[] array)
	{
		random.nextBytes(array);
		return array;
	}
}

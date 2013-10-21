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
package lineage2.commons.math.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.util.Rnd;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RndSelector<E>
{
	/**
	 * @author Mobius
	 */
	private class RndNode<T> implements Comparable<RndNode<T>>
	{
		/**
		 * Field value.
		 */
		final T value;
		/**
		 * Field weight.
		 */
		final int weight;
		
		/**
		 * Constructor for RndNode.
		 * @param value T
		 * @param weight int
		 */
		public RndNode(T value, int weight)
		{
			this.value = value;
			this.weight = weight;
		}
		
		/**
		 * Method compareTo.
		 * @param o RndNode<T>
		 * @return int
		 */
		@Override
		public int compareTo(RndNode<T> o)
		{
			return this.weight - weight;
		}
	}
	
	/**
	 * Field totalWeight.
	 */
	private int totalWeight = 0;
	/**
	 * Field nodes.
	 */
	private final List<RndNode<E>> nodes;
	
	/**
	 * Constructor for RndSelector.
	 */
	public RndSelector()
	{
		nodes = new ArrayList<>();
	}
	
	/**
	 * Constructor for RndSelector.
	 * @param initialCapacity int
	 */
	public RndSelector(int initialCapacity)
	{
		nodes = new ArrayList<>(initialCapacity);
	}
	
	/**
	 * Method add.
	 * @param value E
	 * @param weight int
	 */
	public void add(E value, int weight)
	{
		if ((value == null) || (weight <= 0))
		{
			return;
		}
		totalWeight += weight;
		nodes.add(new RndNode<>(value, weight));
	}
	
	/**
	 * Method chance.
	 * @param maxWeight int
	 * @return E
	 */
	public E chance(int maxWeight)
	{
		if (maxWeight <= 0)
		{
			return null;
		}
		Collections.sort(nodes);
		int r = Rnd.get(maxWeight);
		int weight = 0;
		for (int i = 0; i < nodes.size(); i++)
		{
			if ((weight += nodes.get(i).weight) > r)
			{
				return nodes.get(i).value;
			}
		}
		return null;
	}
	
	/**
	 * Method chance.
	 * @return E
	 */
	public E chance()
	{
		return chance(100);
	}
	
	/**
	 * Method select.
	 * @return E
	 */
	public E select()
	{
		return chance(totalWeight);
	}
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		totalWeight = 0;
		nodes.clear();
	}
}

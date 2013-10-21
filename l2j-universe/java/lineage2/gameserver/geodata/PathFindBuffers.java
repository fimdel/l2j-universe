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
package lineage2.gameserver.geodata;

import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.text.StrTable;
import lineage2.gameserver.Config;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PathFindBuffers
{
	/**
	 * Field MIN_MAP_SIZE.
	 */
	public final static int MIN_MAP_SIZE = 1 << 6;
	/**
	 * Field STEP_MAP_SIZE.
	 */
	public final static int STEP_MAP_SIZE = 1 << 5;
	/**
	 * Field MAX_MAP_SIZE.
	 */
	public final static int MAX_MAP_SIZE = 1 << 9;
	/**
	 * Field buffers.
	 */
	private static TIntObjectHashMap<PathFindBuffer[]> buffers = new TIntObjectHashMap<>();
	/**
	 * Field sizes.
	 */
	private static int[] sizes = new int[0];
	/**
	 * Field lock.
	 */
	private static Lock lock = new ReentrantLock();
	static
	{
		TIntIntHashMap config = new TIntIntHashMap();
		String[] k;
		for (String e : Config.PATHFIND_BUFFERS.split(";"))
		{
			if (!e.isEmpty() && ((k = e.split("x")).length == 2))
			{
				config.put(Integer.valueOf(k[1]), Integer.valueOf(k[0]));
			}
		}
		TIntIntIterator itr = config.iterator();
		while (itr.hasNext())
		{
			itr.advance();
			int size = itr.key();
			int count = itr.value();
			PathFindBuffer[] buff = new PathFindBuffer[count];
			for (int i = 0; i < count; i++)
			{
				buff[i] = new PathFindBuffer(size);
			}
			buffers.put(size, buff);
		}
		sizes = config.keys();
		Arrays.sort(sizes);
	}
	
	/**
	 * Method create.
	 * @param mapSize int
	 * @return PathFindBuffer
	 */
	private static PathFindBuffer create(int mapSize)
	{
		lock.lock();
		try
		{
			PathFindBuffer buffer;
			PathFindBuffer[] buff = buffers.get(mapSize);
			if (buff != null)
			{
				buff = lineage2.commons.lang.ArrayUtils.add(buff, buffer = new PathFindBuffer(mapSize));
			}
			else
			{
				buff = new PathFindBuffer[]
				{
					buffer = new PathFindBuffer(mapSize)
				};
				sizes = org.apache.commons.lang3.ArrayUtils.add(sizes, mapSize);
				Arrays.sort(sizes);
			}
			buffers.put(mapSize, buff);
			buffer.inUse = true;
			return buffer;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method get.
	 * @param mapSize int
	 * @return PathFindBuffer
	 */
	private static PathFindBuffer get(int mapSize)
	{
		lock.lock();
		try
		{
			PathFindBuffer[] buff = buffers.get(mapSize);
			for (PathFindBuffer buffer : buff)
			{
				if (!buffer.inUse)
				{
					buffer.inUse = true;
					return buffer;
				}
			}
			return null;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method alloc.
	 * @param mapSize int
	 * @return PathFindBuffer
	 */
	public static PathFindBuffer alloc(int mapSize)
	{
		if (mapSize > MAX_MAP_SIZE)
		{
			return null;
		}
		mapSize += STEP_MAP_SIZE;
		if (mapSize < MIN_MAP_SIZE)
		{
			mapSize = MIN_MAP_SIZE;
		}
		PathFindBuffer buffer = null;
		for (int size : sizes)
		{
			if (size >= mapSize)
			{
				mapSize = size;
				buffer = get(mapSize);
				break;
			}
		}
		if (buffer == null)
		{
			for (int size = MIN_MAP_SIZE; size < MAX_MAP_SIZE; size += STEP_MAP_SIZE)
			{
				if (size >= mapSize)
				{
					mapSize = size;
					buffer = create(mapSize);
					break;
				}
			}
		}
		return buffer;
	}
	
	/**
	 * Method recycle.
	 * @param buffer PathFindBuffer
	 */
	public static void recycle(PathFindBuffer buffer)
	{
		lock.lock();
		try
		{
			buffer.inUse = false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method getStats.
	 * @return StrTable
	 */
	public static StrTable getStats()
	{
		StrTable table = new StrTable("PathFind Buffers Stats");
		lock.lock();
		try
		{
			long totalUses = 0, totalPlayable = 0, totalTime = 0;
			int index = 0;
			int count;
			long uses;
			long playable;
			long itrs;
			long success;
			long overtime;
			long time;
			for (int size : sizes)
			{
				index++;
				count = 0;
				uses = 0;
				playable = 0;
				itrs = 0;
				success = 0;
				overtime = 0;
				time = 0;
				for (PathFindBuffer buff : buffers.get(size))
				{
					count++;
					uses += buff.totalUses;
					playable += buff.playableUses;
					success += buff.successUses;
					overtime += buff.overtimeUses;
					time += buff.totalTime / 1000000;
					itrs += buff.totalItr;
				}
				totalUses += uses;
				totalPlayable += playable;
				totalTime += time;
				table.set(index, "Size", size);
				table.set(index, "Count", count);
				table.set(index, "Uses (success%)", uses + "(" + String.format("%2.2f", (uses > 0) ? (success * 100.) / uses : 0) + "%)");
				table.set(index, "Uses, playble", playable + "(" + String.format("%2.2f", (uses > 0) ? (playable * 100.) / uses : 0) + "%)");
				table.set(index, "Uses, overtime", overtime + "(" + String.format("%2.2f", (uses > 0) ? (overtime * 100.) / uses : 0) + "%)");
				table.set(index, "Iter., avg", (uses > 0) ? itrs / uses : 0);
				table.set(index, "Time, avg (ms)", String.format("%1.3f", (uses > 0) ? (double) time / uses : 0.));
			}
			table.addTitle("Uses, total / playable  : " + totalUses + " / " + totalPlayable);
			table.addTitle("Uses, total time / avg (ms) : " + totalTime + " / " + String.format("%1.3f", totalUses > 0 ? (double) totalTime / totalUses : 0));
		}
		finally
		{
			lock.unlock();
		}
		return table;
	}
	
	/**
	 * @author Mobius
	 */
	public static class PathFindBuffer
	{
		/**
		 * Field mapSize.
		 */
		final int mapSize;
		/**
		 * Field nodes.
		 */
		final GeoNode[][] nodes;
		/**
		 * Field open.
		 */
		final Queue<GeoNode> open;
		/**
		 * Field offsetY.
		 */
		/**
		 * Field offsetX.
		 */
		int offsetX, offsetY;
		/**
		 * Field inUse.
		 */
		boolean inUse;
		/**
		 * Field totalUses.
		 */
		long totalUses;
		/**
		 * Field successUses.
		 */
		long successUses;
		/**
		 * Field overtimeUses.
		 */
		long overtimeUses;
		/**
		 * Field playableUses.
		 */
		long playableUses;
		/**
		 * Field totalTime.
		 */
		long totalTime;
		/**
		 * Field totalItr.
		 */
		long totalItr;
		
		/**
		 * Constructor for PathFindBuffer.
		 * @param mapSize int
		 */
		public PathFindBuffer(int mapSize)
		{
			open = new PriorityQueue<>(mapSize);
			this.mapSize = mapSize;
			nodes = new GeoNode[mapSize][mapSize];
			for (int i = 0; i < nodes.length; i++)
			{
				for (int j = 0; j < nodes[i].length; j++)
				{
					nodes[i][j] = new GeoNode();
				}
			}
		}
		
		/**
		 * Method free.
		 */
		public void free()
		{
			open.clear();
			for (GeoNode[] node : nodes)
			{
				for (GeoNode element : node)
				{
					element.free();
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class GeoNode implements Comparable<GeoNode>
	{
		/**
		 * Field NONE. (value is 0)
		 */
		public final static int NONE = 0;
		/**
		 * Field OPENED. (value is 1)
		 */
		public final static int OPENED = 1;
		/**
		 * Field CLOSED. (value is -1)
		 */
		public final static int CLOSED = -1;
		/**
		 * Field y.
		 */
		/**
		 * Field x.
		 */
		public int x, y;
		/**
		 * Field nswe.
		 */
		/**
		 * Field z.
		 */
		public short z, nswe;
		/**
		 * Field costToEnd.
		 */
		/**
		 * Field costFromStart.
		 */
		/**
		 * Field totalCost.
		 */
		public float totalCost, costFromStart, costToEnd;
		/**
		 * Field state.
		 */
		public int state;
		/**
		 * Field parent.
		 */
		public GeoNode parent;
		
		/**
		 * Constructor for GeoNode.
		 */
		public GeoNode()
		{
			nswe = -1;
		}
		
		/**
		 * Method set.
		 * @param x int
		 * @param y int
		 * @param z short
		 * @return GeoNode
		 */
		public GeoNode set(int x, int y, short z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}
		
		/**
		 * Method isSet.
		 * @return boolean
		 */
		public boolean isSet()
		{
			return nswe != -1;
		}
		
		/**
		 * Method free.
		 */
		public void free()
		{
			nswe = -1;
			costFromStart = 0f;
			totalCost = 0f;
			costToEnd = 0f;
			parent = null;
			state = NONE;
		}
		
		/**
		 * Method getLoc.
		 * @return Location
		 */
		public Location getLoc()
		{
			return new Location(x, y, z);
		}
		
		/**
		 * Method toString.
		 * @return String
		 */
		@Override
		public String toString()
		{
			return "[" + x + "," + y + "," + z + "] f: " + totalCost;
		}
		
		/**
		 * Method compareTo.
		 * @param o GeoNode
		 * @return int
		 */
		@Override
		public int compareTo(GeoNode o)
		{
			if (totalCost > o.totalCost)
			{
				return 1;
			}
			if (totalCost < o.totalCost)
			{
				return -1;
			}
			return 0;
		}
	}
}

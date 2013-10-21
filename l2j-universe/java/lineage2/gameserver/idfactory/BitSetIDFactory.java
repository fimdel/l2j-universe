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
package lineage2.gameserver.idfactory;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.math.PrimeFinder;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BitSetIDFactory extends IdFactory
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(BitSetIDFactory.class);
	/**
	 * Field freeIds.
	 */
	private BitSet freeIds;
	/**
	 * Field freeIdCount.
	 */
	private AtomicInteger freeIdCount;
	/**
	 * Field nextFreeId.
	 */
	private AtomicInteger nextFreeId;
	
	/**
	 * @author Mobius
	 */
	public class BitSetCapacityCheck extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (reachingBitSetCapacity())
			{
				increaseBitSetCapacity();
			}
		}
	}
	
	/**
	 * Constructor for BitSetIDFactory.
	 */
	protected BitSetIDFactory()
	{
		super();
		initialize();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new BitSetCapacityCheck(), 30000, 30000);
	}
	
	/**
	 * Method initialize.
	 */
	private void initialize()
	{
		try
		{
			freeIds = new BitSet(PrimeFinder.nextPrime(100000));
			freeIds.clear();
			freeIdCount = new AtomicInteger(FREE_OBJECT_ID_SIZE);
			for (int usedObjectId : extractUsedObjectIDTable())
			{
				int objectID = usedObjectId - FIRST_OID;
				if (objectID < 0)
				{
					_log.warn("Object ID " + usedObjectId + " in DB is less than minimum ID of " + FIRST_OID);
					continue;
				}
				freeIds.set(usedObjectId - FIRST_OID);
				freeIdCount.decrementAndGet();
			}
			nextFreeId = new AtomicInteger(freeIds.nextClearBit(0));
			initialized = true;
			_log.info("IdFactory: " + freeIds.size() + " id's available.");
		}
		catch (Exception e)
		{
			initialized = false;
			_log.error("BitSet ID Factory could not be initialized correctly!", e);
		}
	}
	
	/**
	 * Method releaseId.
	 * @param objectID int
	 */
	@Override
	public synchronized void releaseId(int objectID)
	{
		if ((objectID - FIRST_OID) > -1)
		{
			freeIds.clear(objectID - FIRST_OID);
			freeIdCount.incrementAndGet();
			super.releaseId(objectID);
		}
		else
		{
			_log.warn("BitSet ID Factory: release objectID " + objectID + " failed (< " + FIRST_OID + ")");
		}
	}
	
	/**
	 * Method getNextId.
	 * @return int
	 */
	@Override
	public synchronized int getNextId()
	{
		int newID = nextFreeId.get();
		freeIds.set(newID);
		freeIdCount.decrementAndGet();
		int nextFree = freeIds.nextClearBit(newID);
		if (nextFree < 0)
		{
			nextFree = freeIds.nextClearBit(0);
		}
		if (nextFree < 0)
		{
			if (freeIds.size() < FREE_OBJECT_ID_SIZE)
			{
				increaseBitSetCapacity();
			}
			else
			{
				throw new NullPointerException("Ran out of valid Id's.");
			}
		}
		nextFreeId.set(nextFree);
		return newID + FIRST_OID;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public synchronized int size()
	{
		return freeIdCount.get();
	}
	
	/**
	 * Method usedIdCount.
	 * @return int
	 */
	protected synchronized int usedIdCount()
	{
		return size() - FIRST_OID;
	}
	
	/**
	 * Method reachingBitSetCapacity.
	 * @return boolean
	 */
	protected synchronized boolean reachingBitSetCapacity()
	{
		return PrimeFinder.nextPrime((usedIdCount() * 11) / 10) > freeIds.size();
	}
	
	/**
	 * Method increaseBitSetCapacity.
	 */
	protected synchronized void increaseBitSetCapacity()
	{
		BitSet newBitSet = new BitSet(PrimeFinder.nextPrime((usedIdCount() * 11) / 10));
		newBitSet.or(freeIds);
		freeIds = newBitSet;
	}
}

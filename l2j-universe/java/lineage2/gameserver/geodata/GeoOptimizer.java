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

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GeoOptimizer
{
	/**
	 * Field log.
	 */
	static final Logger log = LoggerFactory.getLogger(GeoOptimizer.class);
	/**
	 * Field checkSums.
	 */
	public static int[][][] checkSums;
	/**
	 * Field version. (value is 1)
	 */
	private static final byte version = 1;
	
	/**
	 * @author Mobius
	 */
	public static class GeoBlocksMatchFinder extends RunnableImpl
	{
		/**
		 * Field maxScanRegions.
		 */
		/**
		 * Field ry.
		 */
		/**
		 * Field rx.
		 */
		/**
		 * Field geoY.
		 */
		/**
		 * Field geoX.
		 */
		private final int geoX, geoY, rx, ry, maxScanRegions;
		/**
		 * Field fileName.
		 */
		private final String fileName;
		
		/**
		 * Constructor for GeoBlocksMatchFinder.
		 * @param _geoX int
		 * @param _geoY int
		 * @param _maxScanRegions int
		 */
		public GeoBlocksMatchFinder(int _geoX, int _geoY, int _maxScanRegions)
		{
			super();
			geoX = _geoX;
			geoY = _geoY;
			rx = geoX + Config.GEO_X_FIRST;
			ry = geoY + Config.GEO_Y_FIRST;
			maxScanRegions = _maxScanRegions;
			fileName = "geodata/matches/" + rx + "_" + ry + ".matches";
		}
		
		/**
		 * Method exists.
		 * @return boolean
		 */
		private boolean exists()
		{
			return new File(Config.DATAPACK_ROOT, fileName).exists();
		}
		
		/**
		 * Method saveToFile.
		 * @param links BlockLink[]
		 */
		private void saveToFile(BlockLink[] links)
		{
			log.info("Saving matches to: " + fileName);
			File f = new File(Config.DATAPACK_ROOT, fileName);
			if (f.exists())
			{
				f.delete();
			}
			try(RandomAccessFile raf = new RandomAccessFile(f, "rw");
				FileChannel wChannel = raf.getChannel())
			{
				ByteBuffer buffer = wChannel.map(FileChannel.MapMode.READ_WRITE, 0, (links.length * 6) + 1);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				buffer.put(version);
				for (BlockLink link : links)
				{
					buffer.putShort((short) link.blockIndex);
					buffer.put(link.linkMapX);
					buffer.put(link.linkMapY);
					buffer.putShort((short) link.linkBlockIndex);
				}
			}
			catch (Exception e)
			{
				log.error("", e);
			}
		}
		
		/**
		 * Method calcMatches.
		 * @param curr_checkSums int[]
		 * @param mapX int
		 * @param mapY int
		 * @param putlinks List<BlockLink>
		 * @param notready boolean[]
		 */
		private void calcMatches(int[] curr_checkSums, int mapX, int mapY, List<BlockLink> putlinks, boolean[] notready)
		{
			int[] next_checkSums = checkSums[mapX][mapY];
			if (next_checkSums == null)
			{
				return;
			}
			int startIdx2;
			for (int blockIdx = 0; blockIdx < GeoEngine.BLOCKS_IN_MAP; blockIdx++)
			{
				if (notready[blockIdx])
				{
					startIdx2 = next_checkSums == curr_checkSums ? blockIdx + 1 : 0;
					for (int blockIdx2 = startIdx2; blockIdx2 < GeoEngine.BLOCKS_IN_MAP; blockIdx2++)
					{
						if (curr_checkSums[blockIdx] == next_checkSums[blockIdx2])
						{
							if (GeoEngine.compareGeoBlocks(geoX, geoY, blockIdx, mapX, mapY, blockIdx2))
							{
								putlinks.add(new BlockLink(blockIdx, (byte) mapX, (byte) mapY, blockIdx2));
								notready[blockIdx] = false;
								break;
							}
						}
					}
				}
			}
		}
		
		/**
		 * Method gen.
		 * @return BlockLink[]
		 */
		private BlockLink[] gen()
		{
			log.info("Searching matches for " + rx + "_" + ry);
			long started = System.currentTimeMillis();
			boolean[] notready = new boolean[GeoEngine.BLOCKS_IN_MAP];
			for (int i = 0; i < GeoEngine.BLOCKS_IN_MAP; i++)
			{
				notready[i] = true;
			}
			List<BlockLink> links = new ArrayList<>();
			int[] _checkSums = checkSums[geoX][geoY];
			int n = 0;
			for (int mapX = geoX; mapX < World.WORLD_SIZE_X; mapX++)
			{
				int startgeoY = mapX == geoX ? geoY : 0;
				for (int mapY = startgeoY; mapY < World.WORLD_SIZE_Y; mapY++)
				{
					calcMatches(_checkSums, mapX, mapY, links, notready);
					n++;
					if ((maxScanRegions > 0) && (maxScanRegions == n))
					{
						return links.toArray(new BlockLink[links.size()]);
					}
				}
			}
			started = System.currentTimeMillis() - started;
			log.info("Founded " + links.size() + " matches for " + rx + "_" + ry + " in " + (started / 1000f) + "s");
			return links.toArray(new BlockLink[links.size()]);
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!exists())
			{
				BlockLink[] links = gen();
				saveToFile(links);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class CheckSumLoader extends RunnableImpl
	{
		/**
		 * Field ry.
		 */
		/**
		 * Field rx.
		 */
		/**
		 * Field geoY.
		 */
		/**
		 * Field geoX.
		 */
		private final int geoX, geoY, rx, ry;
		/**
		 * Field region.
		 */
		private final byte[][][] region;
		/**
		 * Field fileName.
		 */
		private final String fileName;
		
		/**
		 * Constructor for CheckSumLoader.
		 * @param _geoX int
		 * @param _geoY int
		 * @param _region byte[][][]
		 */
		public CheckSumLoader(int _geoX, int _geoY, byte[][][] _region)
		{
			super();
			geoX = _geoX;
			geoY = _geoY;
			rx = geoX + Config.GEO_X_FIRST;
			ry = _geoY + Config.GEO_Y_FIRST;
			region = _region;
			fileName = "geodata/checksum/" + rx + "_" + ry + ".crc";
		}
		
		/**
		 * Method loadFromFile.
		 * @return boolean
		 */
		private boolean loadFromFile()
		{
			File GeoCrc = new File(Config.DATAPACK_ROOT, fileName);
			if (!GeoCrc.exists())
			{
				return false;
			}
			try(RandomAccessFile raf = new RandomAccessFile(GeoCrc, "r");
					FileChannel roChannel = raf.getChannel();)
			{
				if (roChannel.size() != (GeoEngine.BLOCKS_IN_MAP * 4))
				{
					return false;
				}
				ByteBuffer buffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, roChannel.size());
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				int[] _checkSums = new int[GeoEngine.BLOCKS_IN_MAP];
				for (int i = 0; i < GeoEngine.BLOCKS_IN_MAP; i++)
				{
					_checkSums[i] = buffer.getInt();
				}
				checkSums[geoX][geoY] = _checkSums;
				return true;
			}
			catch (Exception e)
			{
				log.error("", e);
				return false;
			}
		}
		
		/**
		 * Method saveToFile.
		 */
		private void saveToFile()
		{
			log.info("Saving checksums to: " + fileName);
			File f = new File(Config.DATAPACK_ROOT, fileName);
			if (f.exists())
			{
				f.delete();
			}
			try(RandomAccessFile raf = new RandomAccessFile(f, "rw");
				FileChannel wChannel = raf.getChannel())
			{
				ByteBuffer buffer = wChannel.map(FileChannel.MapMode.READ_WRITE, 0, GeoEngine.BLOCKS_IN_MAP * 4);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				int[] _checkSums = checkSums[geoX][geoY];
				for (int i = 0; i < GeoEngine.BLOCKS_IN_MAP; i++)
				{
					buffer.putInt(_checkSums[i]);
				}
			}
			catch (Exception e)
			{
				log.error("", e);
			}
		}
		
		/**
		 * Method gen.
		 */
		private void gen()
		{
			log.info("Generating checksums for " + rx + "_" + ry);
			int[] _checkSums = new int[GeoEngine.BLOCKS_IN_MAP];
			CRC32 crc32 = new CRC32();
			for (int i = 0; i < GeoEngine.BLOCKS_IN_MAP; i++)
			{
				crc32.update(region[i][0]);
				_checkSums[i] = (int) (crc32.getValue() ^ 0xFFFFFFFF);
				crc32.reset();
			}
			checkSums[geoX][geoY] = _checkSums;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!loadFromFile())
			{
				gen();
				saveToFile();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class BlockLink
	{
		/**
		 * Field linkBlockIndex.
		 */
		/**
		 * Field blockIndex.
		 */
		public final int blockIndex, linkBlockIndex;
		/**
		 * Field linkMapY.
		 */
		/**
		 * Field linkMapX.
		 */
		public final byte linkMapX, linkMapY;
		
		/**
		 * Constructor for BlockLink.
		 * @param _blockIndex short
		 * @param _linkMapX byte
		 * @param _linkMapY byte
		 * @param _linkBlockIndex short
		 */
		public BlockLink(short _blockIndex, byte _linkMapX, byte _linkMapY, short _linkBlockIndex)
		{
			blockIndex = _blockIndex & 0xFFFF;
			linkMapX = _linkMapX;
			linkMapY = _linkMapY;
			linkBlockIndex = _linkBlockIndex & 0xFFFF;
		}
		
		/**
		 * Constructor for BlockLink.
		 * @param _blockIndex int
		 * @param _linkMapX byte
		 * @param _linkMapY byte
		 * @param _linkBlockIndex int
		 */
		public BlockLink(int _blockIndex, byte _linkMapX, byte _linkMapY, int _linkBlockIndex)
		{
			blockIndex = _blockIndex & 0xFFFF;
			linkMapX = _linkMapX;
			linkMapY = _linkMapY;
			linkBlockIndex = _linkBlockIndex & 0xFFFF;
		}
	}
	
	/**
	 * Method loadBlockMatches.
	 * @param fileName String
	 * @return BlockLink[]
	 */
	public static BlockLink[] loadBlockMatches(String fileName)
	{
		File f = new File(Config.DATAPACK_ROOT, fileName);
		if (!f.exists())
		{
			return null;
		}
		try(RandomAccessFile raf = new RandomAccessFile(f, "r");
				FileChannel roChannel = raf.getChannel())
		{
			int count = (int) ((roChannel.size() - 1) / 6);
			ByteBuffer buffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, roChannel.size());
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			if (buffer.get() != version)
			{
				return null;
			}
			BlockLink[] links = new BlockLink[count];
			for (int i = 0; i < links.length; i++)
			{
				links[i] = new BlockLink(buffer.getShort(), buffer.get(), buffer.get(), buffer.getShort());
			}
			return links;
		}
		catch (Exception e)
		{
			log.error("", e);
			return null;
		}
	}
}

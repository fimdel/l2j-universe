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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.commons.geometry.Shape;
import lineage2.gameserver.Config;
import lineage2.gameserver.geodata.GeoOptimizer.BlockLink;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GeoEngine
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GeoEngine.class);
	/**
	 * Field NSWE_NONE. (value is 0) Field NSWE_ALL. (value is 15) Field NORTH. (value is 8) Field SOUTH. (value is 4) Field WEST. (value is 2) Field EAST. (value is 1)
	 */
	public static final byte EAST = 1, WEST = 2, SOUTH = 4, NORTH = 8, NSWE_ALL = 15, NSWE_NONE = 0;
	/**
	 * Field BLOCKTYPE_FLAT. (value is 0)
	 */
	public static final byte BLOCKTYPE_FLAT = 0;
	/**
	 * Field BLOCKTYPE_COMPLEX. (value is 1)
	 */
	public static final byte BLOCKTYPE_COMPLEX = 1;
	/**
	 * Field BLOCKTYPE_MULTILEVEL. (value is 2)
	 */
	public static final byte BLOCKTYPE_MULTILEVEL = 2;
	/**
	 * Field BLOCKS_IN_MAP.
	 */
	public static final int BLOCKS_IN_MAP = 256 * 256;
	/**
	 * Field MAX_LAYERS.
	 */
	public static int MAX_LAYERS = 1;
	/**
	 * Field rawgeo.
	 */
	private static final MappedByteBuffer[][] rawgeo = new MappedByteBuffer[World.WORLD_SIZE_X][World.WORLD_SIZE_Y];
	/**
	 * Field geodata.
	 */
	private static final byte[][][][][] geodata = new byte[World.WORLD_SIZE_X][World.WORLD_SIZE_Y][1][][];
	
	/**
	 * Method getType.
	 * @param x int
	 * @param y int
	 * @param geoIndex int
	 * @return short
	 */
	public static short getType(int x, int y, int geoIndex)
	{
		return NgetType((x - World.MAP_MIN_X) >> 4, (y - World.MAP_MIN_Y) >> 4, geoIndex);
	}
	
	/**
	 * Method getHeight.
	 * @param loc Location
	 * @param geoIndex int
	 * @return int
	 */
	public static int getHeight(Location loc, int geoIndex)
	{
		return getHeight(loc.x, loc.y, loc.z, geoIndex);
	}
	
	/**
	 * Method getHeight.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param geoIndex int
	 * @return int
	 */
	public static int getHeight(int x, int y, int z, int geoIndex)
	{
		return NgetHeight((x - World.MAP_MIN_X) >> 4, (y - World.MAP_MIN_Y) >> 4, z, geoIndex);
	}
	
	/**
	 * Method canMoveToCoord.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param geoIndex int
	 * @return boolean
	 */
	public static boolean canMoveToCoord(int x, int y, int z, int tx, int ty, int tz, int geoIndex)
	{
		return canMove(x, y, z, tx, ty, tz, false, geoIndex) == 0;
	}
	
	/**
	 * Method getNSWE.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param geoIndex int
	 * @return byte
	 */
	public static byte getNSWE(int x, int y, int z, int geoIndex)
	{
		return NgetNSWE((x - World.MAP_MIN_X) >> 4, (y - World.MAP_MIN_Y) >> 4, z, geoIndex);
	}
	
	/**
	 * Method moveCheck.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheck(int x, int y, int z, int tx, int ty, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, false, false, false, geoIndex);
	}
	
	/**
	 * Method moveCheck.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param returnPrev boolean
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheck(int x, int y, int z, int tx, int ty, boolean returnPrev, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, false, false, returnPrev, geoIndex);
	}
	
	/**
	 * Method moveCheckWithCollision.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckWithCollision(int x, int y, int z, int tx, int ty, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, true, false, false, geoIndex);
	}
	
	/**
	 * Method moveCheckWithCollision.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param returnPrev boolean
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckWithCollision(int x, int y, int z, int tx, int ty, boolean returnPrev, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, true, false, returnPrev, geoIndex);
	}
	
	/**
	 * Method moveCheckBackward.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckBackward(int x, int y, int z, int tx, int ty, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, false, true, false, geoIndex);
	}
	
	/**
	 * Method moveCheckBackward.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param returnPrev boolean
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckBackward(int x, int y, int z, int tx, int ty, boolean returnPrev, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, false, true, returnPrev, geoIndex);
	}
	
	/**
	 * Method moveCheckBackwardWithCollision.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckBackwardWithCollision(int x, int y, int z, int tx, int ty, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, true, true, false, geoIndex);
	}
	
	/**
	 * Method moveCheckBackwardWithCollision.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param returnPrev boolean
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckBackwardWithCollision(int x, int y, int z, int tx, int ty, boolean returnPrev, int geoIndex)
	{
		return MoveCheck(x, y, z, tx, ty, true, true, returnPrev, geoIndex);
	}
	
	/**
	 * Method moveInWaterCheck.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param waterZ int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveInWaterCheck(int x, int y, int z, int tx, int ty, int tz, int waterZ, int geoIndex)
	{
		return MoveInWaterCheck((x - World.MAP_MIN_X) >> 4, (y - World.MAP_MIN_Y) >> 4, z, (tx - World.MAP_MIN_X) >> 4, (ty - World.MAP_MIN_Y) >> 4, tz, waterZ, geoIndex);
	}
	
	/**
	 * Method moveCheckForAI.
	 * @param loc1 Location
	 * @param loc2 Location
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckForAI(Location loc1, Location loc2, int geoIndex)
	{
		return MoveCheckForAI((loc1.x - World.MAP_MIN_X) >> 4, (loc1.y - World.MAP_MIN_Y) >> 4, loc1.z, (loc2.x - World.MAP_MIN_X) >> 4, (loc2.y - World.MAP_MIN_Y) >> 4, geoIndex);
	}
	
	/**
	 * Method moveCheckInAir.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param collision double
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location moveCheckInAir(int x, int y, int z, int tx, int ty, int tz, double collision, int geoIndex)
	{
		int gx = (x - World.MAP_MIN_X) >> 4;
		int gy = (y - World.MAP_MIN_Y) >> 4;
		int tgx = (tx - World.MAP_MIN_X) >> 4;
		int tgy = (ty - World.MAP_MIN_Y) >> 4;
		int nz = NgetHeight(tgx, tgy, tz, geoIndex);
		if (tz <= (nz + 32))
		{
			tz = nz + 32;
		}
		Location result = canSee(gx, gy, z, tgx, tgy, tz, true, geoIndex);
		if (result.equals(gx, gy, z))
		{
			return null;
		}
		return result.geo2world();
	}
	
	/**
	 * Method canSeeTarget.
	 * @param actor GameObject
	 * @param target GameObject
	 * @param air boolean
	 * @return boolean
	 */
	public static boolean canSeeTarget(GameObject actor, GameObject target, boolean air)
	{
		if (target == null)
		{
			return false;
		}
		if ((target instanceof GeoCollision) || actor.equals(target))
		{
			return true;
		}
		return canSeeCoord(actor, target.getX(), target.getY(), target.getZ() + (int) target.getColHeight() + 32, air);
	}
	
	/**
	 * Method canSeeCoord.
	 * @param actor GameObject
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param air boolean
	 * @return boolean
	 */
	public static boolean canSeeCoord(GameObject actor, int tx, int ty, int tz, boolean air)
	{
		return (actor != null) && canSeeCoord(actor.getX(), actor.getY(), actor.getZ() + (int) actor.getColHeight() + 32, tx, ty, tz, air, actor.getGeoIndex());
	}
	
	/**
	 * Method canSeeCoord.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param air boolean
	 * @param geoIndex int
	 * @return boolean
	 */
	public static boolean canSeeCoord(int x, int y, int z, int tx, int ty, int tz, boolean air, int geoIndex)
	{
		int mx = (x - World.MAP_MIN_X) >> 4;
		int my = (y - World.MAP_MIN_Y) >> 4;
		int tmx = (tx - World.MAP_MIN_X) >> 4;
		int tmy = (ty - World.MAP_MIN_Y) >> 4;
		return canSee(mx, my, z, tmx, tmy, tz, air, geoIndex).equals(tmx, tmy, tz) && canSee(tmx, tmy, tz, mx, my, z, air, geoIndex).equals(mx, my, z);
	}
	
	/**
	 * Method canMoveWithCollision.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param geoIndex int
	 * @return boolean
	 */
	public static boolean canMoveWithCollision(int x, int y, int z, int tx, int ty, int tz, int geoIndex)
	{
		return canMove(x, y, z, tx, ty, tz, true, geoIndex) == 0;
	}
	
	/**
	 * Method checkNSWE.
	 * @param NSWE byte
	 * @param x int
	 * @param y int
	 * @param tx int
	 * @param ty int
	 * @return boolean
	 */
	public static boolean checkNSWE(byte NSWE, int x, int y, int tx, int ty)
	{
		if (NSWE == NSWE_ALL)
		{
			return true;
		}
		if (NSWE == NSWE_NONE)
		{
			return false;
		}
		if (tx > x)
		{
			if ((NSWE & EAST) == 0)
			{
				return false;
			}
		}
		else if (tx < x)
		{
			if ((NSWE & WEST) == 0)
			{
				return false;
			}
		}
		if (ty > y)
		{
			if ((NSWE & SOUTH) == 0)
			{
				return false;
			}
		}
		else if (ty < y)
		{
			if ((NSWE & NORTH) == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method geoXYZ2Str.
	 * @param _x int
	 * @param _y int
	 * @param _z int
	 * @return String
	 */
	public static String geoXYZ2Str(int _x, int _y, int _z)
	{
		return "(" + String.valueOf((_x << 4) + World.MAP_MIN_X + 8) + " " + String.valueOf((_y << 4) + World.MAP_MIN_Y + 8) + " " + _z + ")";
	}
	
	/**
	 * Method NSWE2Str.
	 * @param nswe byte
	 * @return String
	 */
	public static String NSWE2Str(byte nswe)
	{
		String result = "";
		if ((nswe & NORTH) == NORTH)
		{
			result += "N";
		}
		if ((nswe & SOUTH) == SOUTH)
		{
			result += "S";
		}
		if ((nswe & WEST) == WEST)
		{
			result += "W";
		}
		if ((nswe & EAST) == EAST)
		{
			result += "E";
		}
		return result.isEmpty() ? "X" : result;
	}
	
	/**
	 * Method NLOS_WATER.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param next_x int
	 * @param next_y int
	 * @param next_z int
	 * @param geoIndex int
	 * @return boolean
	 */
	private static boolean NLOS_WATER(int x, int y, int z, int next_x, int next_y, int next_z, int geoIndex)
	{
		short[] layers1 = new short[MAX_LAYERS + 1];
		short[] layers2 = new short[MAX_LAYERS + 1];
		NGetLayers(x, y, layers1, geoIndex);
		NGetLayers(next_x, next_y, layers2, geoIndex);
		if ((layers1[0] == 0) || (layers2[0] == 0))
		{
			return true;
		}
		short h;
		short z2 = Short.MIN_VALUE;
		for (int i = 1; i <= layers2[0]; i++)
		{
			h = (short) ((short) (layers2[i] & 0x0fff0) >> 1);
			if (Math.abs(next_z - z2) > Math.abs(next_z - h))
			{
				z2 = h;
			}
		}
		if ((next_z + 32) >= z2)
		{
			return true;
		}
		short z3 = Short.MIN_VALUE;
		for (int i = 1; i <= layers2[0]; i++)
		{
			h = (short) ((short) (layers2[i] & 0x0fff0) >> 1);
			if ((h < (z2 + Config.MIN_LAYER_HEIGHT)) && (Math.abs(next_z - z3) > Math.abs(next_z - h)))
			{
				z3 = h;
			}
		}
		if (z3 == Short.MIN_VALUE)
		{
			return false;
		}
		short z1 = Short.MIN_VALUE;
		byte NSWE1 = NSWE_ALL;
		for (int i = 1; i <= layers1[0]; i++)
		{
			h = (short) ((short) (layers1[i] & 0x0fff0) >> 1);
			if ((h < (z + Config.MIN_LAYER_HEIGHT)) && (Math.abs(z - z1) > Math.abs(z - h)))
			{
				z1 = h;
				NSWE1 = (byte) (layers1[i] & 0x0F);
			}
		}
		return checkNSWE(NSWE1, x, y, next_x, next_y);
	}
	
	/**
	 * Method FindNearestLowerLayer.
	 * @param layers short[]
	 * @param z int
	 * @return int
	 */
	private static int FindNearestLowerLayer(short[] layers, int z)
	{
		short h, nearest_layer_h = Short.MIN_VALUE;
		int nearest_layer = Integer.MIN_VALUE;
		for (int i = 1; i <= layers[0]; i++)
		{
			h = (short) ((short) (layers[i] & 0x0fff0) >> 1);
			if ((h < z) && (nearest_layer_h < h))
			{
				nearest_layer_h = h;
				nearest_layer = layers[i];
			}
		}
		return nearest_layer;
	}
	
	/**
	 * Method CheckNoOneLayerInRangeAndFindNearestLowerLayer.
	 * @param layers short[]
	 * @param z0 int
	 * @param z1 int
	 * @return short
	 */
	private static short CheckNoOneLayerInRangeAndFindNearestLowerLayer(short[] layers, int z0, int z1)
	{
		int z_min, z_max;
		if (z0 > z1)
		{
			z_min = z1;
			z_max = z0;
		}
		else
		{
			z_min = z0;
			z_max = z1;
		}
		short h, nearest_layer = Short.MIN_VALUE, nearest_layer_h = Short.MIN_VALUE;
		for (int i = 1; i <= layers[0]; i++)
		{
			h = (short) ((short) (layers[i] & 0x0fff0) >> 1);
			if ((z_min <= h) && (h <= z_max))
			{
				return Short.MIN_VALUE;
			}
			if ((h < z0) && (nearest_layer_h < h))
			{
				nearest_layer_h = h;
				nearest_layer = layers[i];
			}
		}
		return nearest_layer;
	}
	
	/**
	 * Method canSeeWallCheck.
	 * @param layer short
	 * @param nearest_lower_neighbor short
	 * @param directionNSWE byte
	 * @param curr_z int
	 * @param air boolean
	 * @return boolean
	 */
	public static boolean canSeeWallCheck(short layer, short nearest_lower_neighbor, byte directionNSWE, int curr_z, boolean air)
	{
		short nearest_lower_neighborh = (short) ((short) (nearest_lower_neighbor & 0x0fff0) >> 1);
		if (air)
		{
			return nearest_lower_neighborh < curr_z;
		}
		short layerh = (short) ((short) (layer & 0x0fff0) >> 1);
		int zdiff = nearest_lower_neighborh - layerh;
		return ((layer & 0x0F & directionNSWE) != 0) || ((zdiff > -Config.MAX_Z_DIFF) && (zdiff != 0));
	}
	
	/**
	 * Method canSee.
	 * @param _x int
	 * @param _y int
	 * @param _z int
	 * @param _tx int
	 * @param _ty int
	 * @param _tz int
	 * @param air boolean
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location canSee(int _x, int _y, int _z, int _tx, int _ty, int _tz, boolean air, int geoIndex)
	{
		int diff_x = _tx - _x, diff_y = _ty - _y, diff_z = _tz - _z;
		int dx = Math.abs(diff_x), dy = Math.abs(diff_y);
		float steps = Math.max(dx, dy);
		int curr_x = _x, curr_y = _y, curr_z = _z;
		short[] curr_layers = new short[MAX_LAYERS + 1];
		NGetLayers(curr_x, curr_y, curr_layers, geoIndex);
		Location result = new Location(_x, _y, _z, -1);
		if (steps == 0)
		{
			if (CheckNoOneLayerInRangeAndFindNearestLowerLayer(curr_layers, curr_z, curr_z + diff_z) != Short.MIN_VALUE)
			{
				result.set(_tx, _ty, _tz, 1);
			}
			return result;
		}
		float step_x = diff_x / steps, step_y = diff_y / steps, step_z = diff_z / steps;
		float half_step_z = step_z / 2.0f;
		float next_x = curr_x, next_y = curr_y, next_z = curr_z;
		int i_next_x, i_next_y, i_next_z, middle_z;
		short[] tmp_layers = new short[MAX_LAYERS + 1];
		short src_nearest_lower_layer, dst_nearest_lower_layer, tmp_nearest_lower_layer;
		for (int i = 0; i < steps; i++)
		{
			if (curr_layers[0] == 0)
			{
				result.set(_tx, _ty, _tz, 0);
				return result;
			}
			next_x += step_x;
			next_y += step_y;
			next_z += step_z;
			i_next_x = (int) (next_x + 0.5f);
			i_next_y = (int) (next_y + 0.5f);
			i_next_z = (int) (next_z + 0.5f);
			middle_z = (int) (curr_z + half_step_z);
			if ((src_nearest_lower_layer = CheckNoOneLayerInRangeAndFindNearestLowerLayer(curr_layers, curr_z, middle_z)) == Short.MIN_VALUE)
			{
				return result.setH(-10);
			}
			NGetLayers(curr_x, curr_y, curr_layers, geoIndex);
			if (curr_layers[0] == 0)
			{
				result.set(_tx, _ty, _tz, 0);
				return result;
			}
			if ((dst_nearest_lower_layer = CheckNoOneLayerInRangeAndFindNearestLowerLayer(curr_layers, i_next_z, middle_z)) == Short.MIN_VALUE)
			{
				return result.setH(-11);
			}
			if (curr_x == i_next_x)
			{
				if (!canSeeWallCheck(src_nearest_lower_layer, dst_nearest_lower_layer, i_next_y > curr_y ? SOUTH : NORTH, curr_z, air))
				{
					return result.setH(-20);
				}
			}
			else if (curr_y == i_next_y)
			{
				if (!canSeeWallCheck(src_nearest_lower_layer, dst_nearest_lower_layer, i_next_x > curr_x ? EAST : WEST, curr_z, air))
				{
					return result.setH(-21);
				}
			}
			else
			{
				NGetLayers(curr_x, i_next_y, tmp_layers, geoIndex);
				if (tmp_layers[0] == 0)
				{
					result.set(_tx, _ty, _tz, 0);
					return result;
				}
				if ((tmp_nearest_lower_layer = CheckNoOneLayerInRangeAndFindNearestLowerLayer(tmp_layers, i_next_z, middle_z)) == Short.MIN_VALUE)
				{
					return result.setH(-30);
				}
				if (!(canSeeWallCheck(src_nearest_lower_layer, tmp_nearest_lower_layer, i_next_y > curr_y ? SOUTH : NORTH, curr_z, air) && canSeeWallCheck(tmp_nearest_lower_layer, dst_nearest_lower_layer, i_next_x > curr_x ? EAST : WEST, curr_z, air)))
				{
					NGetLayers(i_next_x, curr_y, tmp_layers, geoIndex);
					if (tmp_layers[0] == 0)
					{
						result.set(_tx, _ty, _tz, 0);
						return result;
					}
					if ((tmp_nearest_lower_layer = CheckNoOneLayerInRangeAndFindNearestLowerLayer(tmp_layers, i_next_z, middle_z)) == Short.MIN_VALUE)
					{
						return result.setH(-31);
					}
					if (!canSeeWallCheck(src_nearest_lower_layer, tmp_nearest_lower_layer, i_next_x > curr_x ? EAST : WEST, curr_z, air))
					{
						return result.setH(-32);
					}
					if (!canSeeWallCheck(tmp_nearest_lower_layer, dst_nearest_lower_layer, i_next_x > curr_x ? EAST : WEST, curr_z, air))
					{
						return result.setH(-33);
					}
				}
			}
			result.set(curr_x, curr_y, curr_z);
			curr_x = i_next_x;
			curr_y = i_next_y;
			curr_z = i_next_z;
		}
		result.set(_tx, _ty, _tz, 0xFF);
		return result;
	}
	
	/**
	 * Method MoveInWaterCheck.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param tz int
	 * @param waterZ int
	 * @param geoIndex int
	 * @return Location
	 */
	private static Location MoveInWaterCheck(int x, int y, int z, int tx, int ty, int tz, int waterZ, int geoIndex)
	{
		int dx = tx - x;
		int dy = ty - y;
		int dz = tz - z;
		int inc_x = sign(dx);
		int inc_y = sign(dy);
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		if ((dx + dy) == 0)
		{
			return new Location(x, y, z).geo2world();
		}
		float inc_z_for_x = dx == 0 ? 0 : dz / dx;
		float inc_z_for_y = dy == 0 ? 0 : dz / dy;
		int prev_x;
		int prev_y;
		int prev_z;
		float next_x = x;
		float next_y = y;
		float next_z = z;
		if (dx >= dy)
		{
			int delta_A = 2 * dy;
			int d = delta_A - dx;
			int delta_B = delta_A - (2 * dx);
			for (int i = 0; i < dx; i++)
			{
				prev_x = x;
				prev_y = y;
				prev_z = z;
				x = (int) next_x;
				y = (int) next_y;
				z = (int) next_z;
				if (d > 0)
				{
					d += delta_B;
					next_x += inc_x;
					next_z += inc_z_for_x;
					next_y += inc_y;
					next_z += inc_z_for_y;
				}
				else
				{
					d += delta_A;
					next_x += inc_x;
					next_z += inc_z_for_x;
				}
				if ((next_z >= waterZ) || !NLOS_WATER(x, y, z, (int) next_x, (int) next_y, (int) next_z, geoIndex))
				{
					return new Location(prev_x, prev_y, prev_z).geo2world();
				}
			}
		}
		else
		{
			int delta_A = 2 * dx;
			int d = delta_A - dy;
			int delta_B = delta_A - (2 * dy);
			for (int i = 0; i < dy; i++)
			{
				prev_x = x;
				prev_y = y;
				prev_z = z;
				x = (int) next_x;
				y = (int) next_y;
				z = (int) next_z;
				if (d > 0)
				{
					d += delta_B;
					next_x += inc_x;
					next_z += inc_z_for_x;
					next_y += inc_y;
					next_z += inc_z_for_y;
				}
				else
				{
					d += delta_A;
					next_y += inc_y;
					next_z += inc_z_for_y;
				}
				if ((next_z >= waterZ) || !NLOS_WATER(x, y, z, (int) next_x, (int) next_y, (int) next_z, geoIndex))
				{
					return new Location(prev_x, prev_y, prev_z).geo2world();
				}
			}
		}
		return new Location((int) next_x, (int) next_y, (int) next_z).geo2world();
	}
	
	/**
	 * Method canMove.
	 * @param __x int
	 * @param __y int
	 * @param _z int
	 * @param __tx int
	 * @param __ty int
	 * @param _tz int
	 * @param withCollision boolean
	 * @param geoIndex int
	 * @return int
	 */
	private static int canMove(int __x, int __y, int _z, int __tx, int __ty, int _tz, boolean withCollision, int geoIndex)
	{
		int _x = (__x - World.MAP_MIN_X) >> 4;
		int _y = (__y - World.MAP_MIN_Y) >> 4;
		int _tx = (__tx - World.MAP_MIN_X) >> 4;
		int _ty = (__ty - World.MAP_MIN_Y) >> 4;
		int diff_x = _tx - _x, diff_y = _ty - _y, diff_z = _tz - _z;
		int dx = Math.abs(diff_x), dy = Math.abs(diff_y), dz = Math.abs(diff_z);
		float steps = Math.max(dx, dy);
		if (steps == 0)
		{
			return -5;
		}
		int curr_x = _x, curr_y = _y, curr_z = _z;
		short[] curr_layers = new short[MAX_LAYERS + 1];
		NGetLayers(curr_x, curr_y, curr_layers, geoIndex);
		if (curr_layers[0] == 0)
		{
			return 0;
		}
		float step_x = diff_x / steps, step_y = diff_y / steps;
		float next_x = curr_x, next_y = curr_y;
		int i_next_x, i_next_y;
		short[] next_layers = new short[MAX_LAYERS + 1];
		short[] temp_layers = new short[MAX_LAYERS + 1];
		short[] curr_next_switcher;
		for (int i = 0; i < steps; i++)
		{
			next_x += step_x;
			next_y += step_y;
			i_next_x = (int) (next_x + 0.5f);
			i_next_y = (int) (next_y + 0.5f);
			NGetLayers(i_next_x, i_next_y, next_layers, geoIndex);
			if ((curr_z = NcanMoveNext(curr_x, curr_y, curr_z, curr_layers, i_next_x, i_next_y, next_layers, temp_layers, withCollision, geoIndex)) == Integer.MIN_VALUE)
			{
				return 1;
			}
			curr_next_switcher = curr_layers;
			curr_layers = next_layers;
			next_layers = curr_next_switcher;
			curr_x = i_next_x;
			curr_y = i_next_y;
		}
		diff_z = curr_z - _tz;
		dz = Math.abs(diff_z);
		if (Config.ALLOW_FALL_FROM_WALLS)
		{
			return diff_z < Config.MAX_Z_DIFF ? 0 : diff_z * 10000;
		}
		return dz > Config.MAX_Z_DIFF ? dz * 1000 : 0;
	}
	
	/**
	 * Method MoveCheck.
	 * @param __x int
	 * @param __y int
	 * @param _z int
	 * @param __tx int
	 * @param __ty int
	 * @param withCollision boolean
	 * @param backwardMove boolean
	 * @param returnPrev boolean
	 * @param geoIndex int
	 * @return Location
	 */
	private static Location MoveCheck(int __x, int __y, int _z, int __tx, int __ty, boolean withCollision, boolean backwardMove, boolean returnPrev, int geoIndex)
	{
		int _x = (__x - World.MAP_MIN_X) >> 4;
		int _y = (__y - World.MAP_MIN_Y) >> 4;
		int _tx = (__tx - World.MAP_MIN_X) >> 4;
		int _ty = (__ty - World.MAP_MIN_Y) >> 4;
		int diff_x = _tx - _x, diff_y = _ty - _y;
		int dx = Math.abs(diff_x), dy = Math.abs(diff_y);
		float steps = Math.max(dx, dy);
		if (steps == 0)
		{
			return new Location(__x, __y, _z);
		}
		float step_x = diff_x / steps, step_y = diff_y / steps;
		int curr_x = _x, curr_y = _y, curr_z = _z;
		float next_x = curr_x, next_y = curr_y;
		int i_next_x, i_next_y, i_next_z = curr_z;
		short[] next_layers = new short[MAX_LAYERS + 1];
		short[] temp_layers = new short[MAX_LAYERS + 1];
		short[] curr_layers = new short[MAX_LAYERS + 1];
		short[] curr_next_switcher;
		NGetLayers(curr_x, curr_y, curr_layers, geoIndex);
		int prev_x = curr_x, prev_y = curr_y, prev_z = curr_z;
		for (int i = 0; i < steps; i++)
		{
			next_x += step_x;
			next_y += step_y;
			i_next_x = (int) (next_x + 0.5f);
			i_next_y = (int) (next_y + 0.5f);
			NGetLayers(i_next_x, i_next_y, next_layers, geoIndex);
			if ((i_next_z = NcanMoveNext(curr_x, curr_y, curr_z, curr_layers, i_next_x, i_next_y, next_layers, temp_layers, withCollision, geoIndex)) == Integer.MIN_VALUE)
			{
				break;
			}
			if (backwardMove && (NcanMoveNext(i_next_x, i_next_y, i_next_z, next_layers, curr_x, curr_y, curr_layers, temp_layers, withCollision, geoIndex) == Integer.MIN_VALUE))
			{
				break;
			}
			curr_next_switcher = curr_layers;
			curr_layers = next_layers;
			next_layers = curr_next_switcher;
			if (returnPrev)
			{
				prev_x = curr_x;
				prev_y = curr_y;
				prev_z = curr_z;
			}
			curr_x = i_next_x;
			curr_y = i_next_y;
			curr_z = i_next_z;
		}
		if (returnPrev)
		{
			curr_x = prev_x;
			curr_y = prev_y;
			curr_z = prev_z;
		}
		return new Location(curr_x, curr_y, curr_z).geo2world();
	}
	
	/**
	 * Method MoveList.
	 * @param __x int
	 * @param __y int
	 * @param _z int
	 * @param __tx int
	 * @param __ty int
	 * @param geoIndex int
	 * @param onlyFullPath boolean
	 * @return List<Location>
	 */
	public static List<Location> MoveList(int __x, int __y, int _z, int __tx, int __ty, int geoIndex, boolean onlyFullPath)
	{
		int _x = (__x - World.MAP_MIN_X) >> 4;
		int _y = (__y - World.MAP_MIN_Y) >> 4;
		int _tx = (__tx - World.MAP_MIN_X) >> 4;
		int _ty = (__ty - World.MAP_MIN_Y) >> 4;
		int diff_x = _tx - _x, diff_y = _ty - _y;
		int dx = Math.abs(diff_x), dy = Math.abs(diff_y);
		double steps = Math.max(dx, dy);
		if (steps == 0)
		{
			return Collections.emptyList();
		}
		double step_x = diff_x / steps, step_y = diff_y / steps;
		int curr_x = _x, curr_y = _y, curr_z = _z;
		double next_x = curr_x, next_y = curr_y;
		int i_next_x, i_next_y, i_next_z = curr_z;
		short[] next_layers = new short[MAX_LAYERS + 1];
		short[] temp_layers = new short[MAX_LAYERS + 1];
		short[] curr_layers = new short[MAX_LAYERS + 1];
		short[] curr_next_switcher;
		NGetLayers(curr_x, curr_y, curr_layers, geoIndex);
		if (curr_layers[0] == 0)
		{
			return null;
		}
		List<Location> result = new ArrayList<>((int) steps + 1);
		result.add(new Location(curr_x, curr_y, curr_z));
		steps = Math.ceil(steps);
		for (int i = 0; i < (int) steps; i++)
		{
			next_x += step_x;
			next_y += step_y;
			i_next_x = (int) (next_x + 0.5);
			i_next_y = (int) (next_y + 0.5);
			NGetLayers(i_next_x, i_next_y, next_layers, geoIndex);
			if ((i_next_z = NcanMoveNext(curr_x, curr_y, curr_z, curr_layers, i_next_x, i_next_y, next_layers, temp_layers, false, geoIndex)) == Integer.MIN_VALUE)
			{
				if (onlyFullPath)
				{
					return null;
				}
				break;
			}
			curr_next_switcher = curr_layers;
			curr_layers = next_layers;
			next_layers = curr_next_switcher;
			curr_x = i_next_x;
			curr_y = i_next_y;
			curr_z = i_next_z;
			result.add(new Location(curr_x, curr_y, curr_z));
		}
		return result;
	}
	
	/**
	 * Method MoveCheckForAI.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param tx int
	 * @param ty int
	 * @param geoIndex int
	 * @return Location
	 */
	private static Location MoveCheckForAI(int x, int y, int z, int tx, int ty, int geoIndex)
	{
		int dx = tx - x;
		int dy = ty - y;
		int inc_x = sign(dx);
		int inc_y = sign(dy);
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		if (((dx + dy) < 2) || ((dx == 2) && (dy == 0)) || ((dx == 0) && (dy == 2)))
		{
			return new Location(x, y, z).geo2world();
		}
		int prev_x = x;
		int prev_y = y;
		int prev_z = z;
		int next_x = x;
		int next_y = y;
		int next_z = z;
		if (dx >= dy)
		{
			int delta_A = 2 * dy;
			int d = delta_A - dx;
			int delta_B = delta_A - (2 * dx);
			for (int i = 0; i < dx; i++)
			{
				prev_x = x;
				prev_y = y;
				prev_z = z;
				x = next_x;
				y = next_y;
				z = next_z;
				if (d > 0)
				{
					d += delta_B;
					next_x += inc_x;
					next_y += inc_y;
				}
				else
				{
					d += delta_A;
					next_x += inc_x;
				}
				next_z = NcanMoveNextForAI(x, y, z, next_x, next_y, geoIndex);
				if (next_z == 0)
				{
					return new Location(prev_x, prev_y, prev_z).geo2world();
				}
			}
		}
		else
		{
			int delta_A = 2 * dx;
			int d = delta_A - dy;
			int delta_B = delta_A - (2 * dy);
			for (int i = 0; i < dy; i++)
			{
				prev_x = x;
				prev_y = y;
				prev_z = z;
				x = next_x;
				y = next_y;
				z = next_z;
				if (d > 0)
				{
					d += delta_B;
					next_x += inc_x;
					next_y += inc_y;
				}
				else
				{
					d += delta_A;
					next_y += inc_y;
				}
				next_z = NcanMoveNextForAI(x, y, z, next_x, next_y, geoIndex);
				if (next_z == 0)
				{
					return new Location(prev_x, prev_y, prev_z).geo2world();
				}
			}
		}
		return new Location(next_x, next_y, next_z).geo2world();
	}
	
	/**
	 * Method NcanMoveNextExCheck.
	 * @param x int
	 * @param y int
	 * @param h int
	 * @param nextx int
	 * @param nexty int
	 * @param hexth int
	 * @param temp_layers short[]
	 * @param geoIndex int
	 * @return boolean
	 */
	private static boolean NcanMoveNextExCheck(int x, int y, int h, int nextx, int nexty, int hexth, short[] temp_layers, int geoIndex)
	{
		NGetLayers(x, y, temp_layers, geoIndex);
		if (temp_layers[0] == 0)
		{
			return true;
		}
		int temp_layer;
		if ((temp_layer = FindNearestLowerLayer(temp_layers, h + Config.MIN_LAYER_HEIGHT)) == Integer.MIN_VALUE)
		{
			return false;
		}
		short temp_layer_h = (short) ((short) (temp_layer & 0x0fff0) >> 1);
		if ((Math.abs(temp_layer_h - hexth) >= Config.MAX_Z_DIFF) || (Math.abs(temp_layer_h - h) >= Config.MAX_Z_DIFF))
		{
			return false;
		}
		return checkNSWE((byte) (temp_layer & 0x0F), x, y, nextx, nexty);
	}
	
	/**
	 * Method NcanMoveNext.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param layers short[]
	 * @param next_x int
	 * @param next_y int
	 * @param next_layers short[]
	 * @param temp_layers short[]
	 * @param withCollision boolean
	 * @param geoIndex int
	 * @return int
	 */
	public static int NcanMoveNext(int x, int y, int z, short[] layers, int next_x, int next_y, short[] next_layers, short[] temp_layers, boolean withCollision, int geoIndex)
	{
		if ((layers[0] == 0) || (next_layers[0] == 0))
		{
			return z;
		}
		int layer, next_layer;
		if ((layer = FindNearestLowerLayer(layers, z + Config.MIN_LAYER_HEIGHT)) == Integer.MIN_VALUE)
		{
			return Integer.MIN_VALUE;
		}
		byte layer_nswe = (byte) (layer & 0x0F);
		if (!checkNSWE(layer_nswe, x, y, next_x, next_y))
		{
			return Integer.MIN_VALUE;
		}
		short layer_h = (short) ((short) (layer & 0x0fff0) >> 1);
		if ((next_layer = FindNearestLowerLayer(next_layers, layer_h + Config.MIN_LAYER_HEIGHT)) == Integer.MIN_VALUE)
		{
			return Integer.MIN_VALUE;
		}
		short next_layer_h = (short) ((short) (next_layer & 0x0fff0) >> 1);
		if ((x == next_x) || (y == next_y))
		{
			if (withCollision)
			{
				if (x == next_x)
				{
					NgetHeightAndNSWE(x - 1, y, layer_h, temp_layers, geoIndex);
					if ((Math.abs(temp_layers[0] - layer_h) > 15) || !checkNSWE(layer_nswe, x - 1, y, x, y) || !checkNSWE((byte) temp_layers[1], x - 1, y, x - 1, next_y))
					{
						return Integer.MIN_VALUE;
					}
					NgetHeightAndNSWE(x + 1, y, layer_h, temp_layers, geoIndex);
					if ((Math.abs(temp_layers[0] - layer_h) > 15) || !checkNSWE(layer_nswe, x + 1, y, x, y) || !checkNSWE((byte) temp_layers[1], x + 1, y, x + 1, next_y))
					{
						return Integer.MIN_VALUE;
					}
					return next_layer_h;
				}
				NgetHeightAndNSWE(x, y - 1, layer_h, temp_layers, geoIndex);
				if ((Math.abs(temp_layers[0] - layer_h) >= Config.MAX_Z_DIFF) || !checkNSWE(layer_nswe, x, y - 1, x, y) || !checkNSWE((byte) temp_layers[1], x, y - 1, next_x, y - 1))
				{
					return Integer.MIN_VALUE;
				}
				NgetHeightAndNSWE(x, y + 1, layer_h, temp_layers, geoIndex);
				if ((Math.abs(temp_layers[0] - layer_h) >= Config.MAX_Z_DIFF) || !checkNSWE(layer_nswe, x, y + 1, x, y) || !checkNSWE((byte) temp_layers[1], x, y + 1, next_x, y + 1))
				{
					return Integer.MIN_VALUE;
				}
			}
			return next_layer_h;
		}
		if (!NcanMoveNextExCheck(x, next_y, layer_h, next_x, next_y, next_layer_h, temp_layers, geoIndex))
		{
			return Integer.MIN_VALUE;
		}
		if (!NcanMoveNextExCheck(next_x, y, layer_h, next_x, next_y, next_layer_h, temp_layers, geoIndex))
		{
			return Integer.MIN_VALUE;
		}
		return next_layer_h;
	}
	
	/**
	 * Method NcanMoveNextForAI.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param next_x int
	 * @param next_y int
	 * @param geoIndex int
	 * @return int
	 */
	public static int NcanMoveNextForAI(int x, int y, int z, int next_x, int next_y, int geoIndex)
	{
		short[] layers1 = new short[MAX_LAYERS + 1];
		short[] layers2 = new short[MAX_LAYERS + 1];
		NGetLayers(x, y, layers1, geoIndex);
		NGetLayers(next_x, next_y, layers2, geoIndex);
		if ((layers1[0] == 0) || (layers2[0] == 0))
		{
			return z == 0 ? 1 : z;
		}
		short h;
		short z1 = Short.MIN_VALUE;
		byte NSWE1 = NSWE_ALL;
		for (int i = 1; i <= layers1[0]; i++)
		{
			h = (short) ((short) (layers1[i] & 0x0fff0) >> 1);
			if (Math.abs(z - z1) > Math.abs(z - h))
			{
				z1 = h;
				NSWE1 = (byte) (layers1[i] & 0x0F);
			}
		}
		if (z1 == Short.MIN_VALUE)
		{
			return 0;
		}
		short z2 = Short.MIN_VALUE;
		byte NSWE2 = NSWE_ALL;
		for (int i = 1; i <= layers2[0]; i++)
		{
			h = (short) ((short) (layers2[i] & 0x0fff0) >> 1);
			if (Math.abs(z - z2) > Math.abs(z - h))
			{
				z2 = h;
				NSWE2 = (byte) (layers2[i] & 0x0F);
			}
		}
		if (z2 == Short.MIN_VALUE)
		{
			return 0;
		}
		if ((z1 > z2) && ((z1 - z2) > Config.MAX_Z_DIFF))
		{
			return 0;
		}
		if (!checkNSWE(NSWE1, x, y, next_x, next_y) || !checkNSWE(NSWE2, next_x, next_y, x, y))
		{
			return 0;
		}
		return z2 == 0 ? 1 : z2;
	}
	
	/**
	 * Method NGetLayers.
	 * @param geoX int
	 * @param geoY int
	 * @param result short[]
	 * @param geoIndex int
	 */
	public static void NGetLayers(int geoX, int geoY, short[] result, int geoIndex)
	{
		result[0] = 0;
		byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
		if (block == null)
		{
			return;
		}
		int cellX, cellY;
		int index = 0;
		byte type = block[index];
		index++;
		switch (type)
		{
			case BLOCKTYPE_FLAT:
				short height = makeShort(block[index + 1], block[index]);
				height = (short) (height & 0x0fff0);
				result[0]++;
				result[1] = (short) ((short) (height << 1) | NSWE_ALL);
				return;
			case BLOCKTYPE_COMPLEX:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				index += ((cellX << 3) + cellY) << 1;
				height = makeShort(block[index + 1], block[index]);
				result[0]++;
				result[1] = height;
				return;
			case BLOCKTYPE_MULTILEVEL:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				int offset = (cellX << 3) + cellY;
				while (offset > 0)
				{
					byte lc = block[index];
					index += (lc << 1) + 1;
					offset--;
				}
				byte layer_count = block[index];
				index++;
				if ((layer_count <= 0) || (layer_count > MAX_LAYERS))
				{
					return;
				}
				result[0] = layer_count;
				while (layer_count > 0)
				{
					result[layer_count] = makeShort(block[index + 1], block[index]);
					layer_count--;
					index += 2;
				}
				return;
			default:
				_log.error("GeoEngine: Unknown block type");
				return;
		}
	}
	
	/**
	 * Method NgetType.
	 * @param geoX int
	 * @param geoY int
	 * @param geoIndex int
	 * @return short
	 */
	private static short NgetType(int geoX, int geoY, int geoIndex)
	{
		byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
		if (block == null)
		{
			return 0;
		}
		return block[0];
	}
	
	/**
	 * Method NgetHeight.
	 * @param geoX int
	 * @param geoY int
	 * @param z int
	 * @param geoIndex int
	 * @return int
	 */
	public static int NgetHeight(int geoX, int geoY, int z, int geoIndex)
	{
		byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
		if (block == null)
		{
			return z;
		}
		int cellX, cellY, index = 0;
		byte type = block[index];
		index++;
		short height;
		switch (type)
		{
			case BLOCKTYPE_FLAT:
				height = makeShort(block[index + 1], block[index]);
				return (short) (height & 0x0fff0);
			case BLOCKTYPE_COMPLEX:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				index += ((cellX << 3) + cellY) << 1;
				height = makeShort(block[index + 1], block[index]);
				return (short) ((short) (height & 0x0fff0) >> 1);
			case BLOCKTYPE_MULTILEVEL:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				int offset = (cellX << 3) + cellY;
				while (offset > 0)
				{
					byte lc = block[index];
					index += (lc << 1) + 1;
					offset--;
				}
				byte layers = block[index];
				index++;
				if ((layers <= 0) || (layers > MAX_LAYERS))
				{
					return (short) z;
				}
				int z_nearest_lower_limit = z + Config.MIN_LAYER_HEIGHT;
				int z_nearest_lower = Integer.MIN_VALUE;
				int z_nearest = Integer.MIN_VALUE;
				while (layers > 0)
				{
					height = (short) ((short) (makeShort(block[index + 1], block[index]) & 0x0fff0) >> 1);
					if (height < z_nearest_lower_limit)
					{
						z_nearest_lower = Math.max(z_nearest_lower, height);
					}
					else if (Math.abs(z - height) < Math.abs(z - z_nearest))
					{
						z_nearest = height;
					}
					layers--;
					index += 2;
				}
				return z_nearest_lower != Integer.MIN_VALUE ? z_nearest_lower : z_nearest;
			default:
				_log.error("GeoEngine: Unknown blockType");
				return z;
		}
	}
	
	/**
	 * Method NgetNSWE.
	 * @param geoX int
	 * @param geoY int
	 * @param z int
	 * @param geoIndex int
	 * @return byte
	 */
	public static byte NgetNSWE(int geoX, int geoY, int z, int geoIndex)
	{
		byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
		if (block == null)
		{
			return NSWE_ALL;
		}
		int cellX, cellY;
		int index = 0;
		byte type = block[index];
		index++;
		switch (type)
		{
			case BLOCKTYPE_FLAT:
				return NSWE_ALL;
			case BLOCKTYPE_COMPLEX:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				index += ((cellX << 3) + cellY) << 1;
				short height = makeShort(block[index + 1], block[index]);
				return (byte) (height & 0x0F);
			case BLOCKTYPE_MULTILEVEL:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				int offset = (cellX << 3) + cellY;
				while (offset > 0)
				{
					byte lc = block[index];
					index += (lc << 1) + 1;
					offset--;
				}
				byte layers = block[index];
				index++;
				if ((layers <= 0) || (layers > MAX_LAYERS))
				{
					return NSWE_ALL;
				}
				short tempz1 = Short.MIN_VALUE;
				short tempz2 = Short.MIN_VALUE;
				int index_nswe1 = NSWE_NONE;
				int index_nswe2 = NSWE_NONE;
				int z_nearest_lower_limit = z + Config.MIN_LAYER_HEIGHT;
				while (layers > 0)
				{
					height = (short) ((short) (makeShort(block[index + 1], block[index]) & 0x0fff0) >> 1);
					if (height < z_nearest_lower_limit)
					{
						if (height > tempz1)
						{
							tempz1 = height;
							index_nswe1 = index;
						}
					}
					else if (Math.abs(z - height) < Math.abs(z - tempz2))
					{
						tempz2 = height;
						index_nswe2 = index;
					}
					layers--;
					index += 2;
				}
				if (index_nswe1 > 0)
				{
					return (byte) (makeShort(block[index_nswe1 + 1], block[index_nswe1]) & 0x0F);
				}
				if (index_nswe2 > 0)
				{
					return (byte) (makeShort(block[index_nswe2 + 1], block[index_nswe2]) & 0x0F);
				}
				return NSWE_ALL;
			default:
				_log.error("GeoEngine: Unknown block type.");
				return NSWE_ALL;
		}
	}
	
	/**
	 * Method NgetHeightAndNSWE.
	 * @param geoX int
	 * @param geoY int
	 * @param z short
	 * @param result short[]
	 * @param geoIndex int
	 */
	public static void NgetHeightAndNSWE(int geoX, int geoY, short z, short[] result, int geoIndex)
	{
		byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
		if (block == null)
		{
			result[0] = z;
			result[1] = NSWE_ALL;
			return;
		}
		int cellX, cellY, index = 0;
		short height, NSWE = NSWE_ALL;
		byte type = block[index];
		index++;
		switch (type)
		{
			case BLOCKTYPE_FLAT:
				height = makeShort(block[index + 1], block[index]);
				result[0] = (short) (height & 0x0fff0);
				result[1] = NSWE_ALL;
				return;
			case BLOCKTYPE_COMPLEX:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				index += ((cellX << 3) + cellY) << 1;
				height = makeShort(block[index + 1], block[index]);
				result[0] = (short) ((short) (height & 0x0fff0) >> 1);
				result[1] = (short) (height & 0x0F);
				return;
			case BLOCKTYPE_MULTILEVEL:
				cellX = getCell(geoX);
				cellY = getCell(geoY);
				int offset = (cellX << 3) + cellY;
				while (offset > 0)
				{
					byte lc = block[index];
					index += (lc << 1) + 1;
					offset--;
				}
				byte layers = block[index];
				index++;
				if ((layers <= 0) || (layers > MAX_LAYERS))
				{
					result[0] = z;
					result[1] = NSWE_ALL;
					return;
				}
				short tempz1 = Short.MIN_VALUE;
				short tempz2 = Short.MIN_VALUE;
				int index_nswe1 = 0;
				int index_nswe2 = 0;
				int z_nearest_lower_limit = z + Config.MIN_LAYER_HEIGHT;
				while (layers > 0)
				{
					height = (short) ((short) (makeShort(block[index + 1], block[index]) & 0x0fff0) >> 1);
					if (height < z_nearest_lower_limit)
					{
						if (height > tempz1)
						{
							tempz1 = height;
							index_nswe1 = index;
						}
					}
					else if (Math.abs(z - height) < Math.abs(z - tempz2))
					{
						tempz2 = height;
						index_nswe2 = index;
					}
					layers--;
					index += 2;
				}
				if (index_nswe1 > 0)
				{
					NSWE = makeShort(block[index_nswe1 + 1], block[index_nswe1]);
					NSWE = (short) (NSWE & 0x0F);
				}
				else if (index_nswe2 > 0)
				{
					NSWE = makeShort(block[index_nswe2 + 1], block[index_nswe2]);
					NSWE = (short) (NSWE & 0x0F);
				}
				result[0] = tempz1 > Short.MIN_VALUE ? tempz1 : tempz2;
				result[1] = NSWE;
				return;
			default:
				_log.error("GeoEngine: Unknown block type.");
				result[0] = z;
				result[1] = NSWE_ALL;
				return;
		}
	}
	
	/**
	 * Method makeShort.
	 * @param b1 byte
	 * @param b0 byte
	 * @return short
	 */
	protected static short makeShort(byte b1, byte b0)
	{
		return (short) ((b1 << 8) | (b0 & 0xff));
	}
	
	/**
	 * Method getBlock.
	 * @param geoPos int
	 * @return int
	 */
	protected static int getBlock(int geoPos)
	{
		return (geoPos >> 3) % 256;
	}
	
	/**
	 * Method getCell.
	 * @param geoPos int
	 * @return int
	 */
	protected static int getCell(int geoPos)
	{
		return geoPos % 8;
	}
	
	/**
	 * Method getBlockIndex.
	 * @param blockX int
	 * @param blockY int
	 * @return int
	 */
	protected static int getBlockIndex(int blockX, int blockY)
	{
		return (blockX << 8) + blockY;
	}
	
	/**
	 * Method sign.
	 * @param x int
	 * @return byte
	 */
	private static byte sign(int x)
	{
		if (x >= 0)
		{
			return +1;
		}
		return -1;
	}
	
	/**
	 * Method getGeoBlockFromGeoCoords.
	 * @param geoX int
	 * @param geoY int
	 * @param geoIndex int
	 * @return byte[]
	 */
	private static byte[] getGeoBlockFromGeoCoords(int geoX, int geoY, int geoIndex)
	{
		if (!Config.ALLOW_GEODATA)
		{
			return null;
		}
		int ix = geoX >> 11;
		int iy = geoY >> 11;
		if ((ix < 0) || (ix >= World.WORLD_SIZE_X) || (iy < 0) || (iy >= World.WORLD_SIZE_Y))
		{
			return null;
		}
		byte[][][] region = geodata[ix][iy];
		int blockX = getBlock(geoX);
		int blockY = getBlock(geoY);
		int regIndex = 0;
		if ((geoIndex & 0x0f000000) == 0x0f000000)
		{
			int x = (geoIndex & 0x00ff0000) >> 16;
			int y = (geoIndex & 0x0000ff00) >> 8;
			if ((ix == x) && (iy == y))
			{
				regIndex = (geoIndex & 0x000000ff);
			}
		}
		return region[regIndex][getBlockIndex(blockX, blockY)];
	}
	
	/**
	 * Method load.
	 */
	public static void load()
	{
		if (!Config.ALLOW_GEODATA)
		{
			return;
		}
		_log.info("GeoEngine: Loading Geodata...");
		File f = new File(Config.DATAPACK_ROOT, "geodata");
		if (!f.exists() || !f.isDirectory())
		{
			_log.info("GeoEngine: Files missing, loading aborted.");
			return;
		}
		int counter = 0;
		Pattern p = Pattern.compile(Config.GEOFILES_PATTERN);
		for (File q : f.listFiles())
		{
			if (q.isDirectory())
			{
				continue;
			}
			String fn = q.getName();
			Matcher m = p.matcher(fn);
			if (m.matches())
			{
				fn = fn.substring(0, 5);
				String[] xy = fn.split("_");
				byte rx = Byte.parseByte(xy[0]);
				byte ry = Byte.parseByte(xy[1]);
				LoadGeodataFile(rx, ry);
				LoadGeodata(rx, ry, 0);
				counter++;
			}
		}
		_log.info("GeoEngine: Loaded " + counter + " map(s), max layers: " + MAX_LAYERS);
		if (Config.COMPACT_GEO)
		{
			compact();
		}
	}
	
	/**
	 * Method DumpGeodata.
	 * @param dir String
	 */
	public static void DumpGeodata(String dir)
	{
		new File(dir).mkdirs();
		for (int mapX = 0; mapX < World.WORLD_SIZE_X; mapX++)
		{
			for (int mapY = 0; mapY < World.WORLD_SIZE_Y; mapY++)
			{
				if (geodata[mapX][mapY] == null)
				{
					continue;
				}
				int rx = mapX + Config.GEO_X_FIRST;
				int ry = mapY + Config.GEO_Y_FIRST;
				String fName = dir + "/" + rx + "_" + ry + ".l2j";
				_log.info("Dumping geo: " + fName);
				DumpGeodataFile(fName, (byte) rx, (byte) ry);
			}
		}
	}
	
	/**
	 * Method DumpGeodataFile.
	 * @param cx int
	 * @param cy int
	 * @return boolean
	 */
	public static boolean DumpGeodataFile(int cx, int cy)
	{
		return DumpGeodataFileMap((byte) (Math.floor((float) cx / (float) 32768) + 20), (byte) (Math.floor((float) cy / (float) 32768) + 18));
	}
	
	/**
	 * Method DumpGeodataFileMap.
	 * @param rx byte
	 * @param ry byte
	 * @return boolean
	 */
	public static boolean DumpGeodataFileMap(byte rx, byte ry)
	{
		String name = "log/" + rx + "_" + ry + ".l2j";
		return DumpGeodataFile(name, rx, ry);
	}
	
	/**
	 * Method DumpGeodataFile.
	 * @param name String
	 * @param rx byte
	 * @param ry byte
	 * @return boolean
	 */
	public static boolean DumpGeodataFile(String name, byte rx, byte ry)
	{
		int ix = rx - Config.GEO_X_FIRST;
		int iy = ry - Config.GEO_Y_FIRST;
		byte[][] geoblocks = geodata[ix][iy][0];
		if (geoblocks == null)
		{
			return false;
		}
		File f = new File(name);
		if (f.exists())
		{
			f.delete();
		}
		OutputStream os = null;
		try
		{
			os = new BufferedOutputStream(new FileOutputStream(f));
			for (byte[] geoblock : geoblocks)
			{
				os.write(geoblock);
			}
		}
		catch (IOException e)
		{
			_log.error("", e);
			return false;
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		return true;
	}
	
	/**
	 * Method LoadGeodataFile.
	 * @param rx byte
	 * @param ry byte
	 * @return boolean
	 */
	public static boolean LoadGeodataFile(byte rx, byte ry)
	{
		String fname = "geodata/" + rx + "_" + ry + ".l2j";
		int ix = rx - Config.GEO_X_FIRST;
		int iy = ry - Config.GEO_Y_FIRST;
		if ((ix < 0) || (iy < 0) || (ix > ((World.MAP_MAX_X >> 15) + Math.abs(World.MAP_MIN_X >> 15))) || (iy > ((World.MAP_MAX_Y >> 15) + Math.abs(World.MAP_MIN_Y >> 15))))
		{
			_log.info("GeoEngine: File " + fname + " was not loaded!!! ");
			return false;
		}
		File geoFile = new File(Config.DATAPACK_ROOT, fname);
		try(RandomAccessFile raf = new RandomAccessFile(geoFile, "r");
				FileChannel roChannel = raf.getChannel())
		{
			long size = roChannel.size();
			MappedByteBuffer buf = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			rawgeo[ix][iy] = buf;
			if (size < (BLOCKS_IN_MAP * 3))
			{
				throw new RuntimeException("Invalid geodata : " + fname + "!");
			}
			return true;
		}
		catch (IOException e)
		{
			_log.error("", e);
		}
		return false;
	}
	
	/**
	 * Method LoadGeodata.
	 * @param rx int
	 * @param ry int
	 * @param regIndex int
	 */
	public static void LoadGeodata(int rx, int ry, int regIndex)
	{
		int ix = rx - Config.GEO_X_FIRST;
		int iy = ry - Config.GEO_Y_FIRST;
		MappedByteBuffer geo = rawgeo[ix][iy];
		int index = 0, orgIndex, block = 0, floor = 0;
		byte[][] blocks;
		synchronized (geodata)
		{
			if ((blocks = geodata[ix][iy][regIndex]) == null)
			{
				geodata[ix][iy][regIndex] = (blocks = new byte[BLOCKS_IN_MAP][]);
			}
		}
		for (block = 0; block < BLOCKS_IN_MAP; block++)
		{
			byte type = geo.get(index);
			index++;
			byte[] geoBlock;
			switch (type)
			{
				case BLOCKTYPE_FLAT:
					geoBlock = new byte[2 + 1];
					geoBlock[0] = type;
					geoBlock[1] = geo.get(index);
					geoBlock[2] = geo.get(index + 1);
					index += 2;
					blocks[block] = geoBlock;
					break;
				case BLOCKTYPE_COMPLEX:
					geoBlock = new byte[128 + 1];
					geoBlock[0] = type;
					geo.position(index);
					geo.get(geoBlock, 1, 128);
					index += 128;
					blocks[block] = geoBlock;
					break;
				case BLOCKTYPE_MULTILEVEL:
					orgIndex = index;
					for (int b = 0; b < 64; b++)
					{
						byte layers = geo.get(index);
						MAX_LAYERS = Math.max(MAX_LAYERS, layers);
						index += (layers << 1) + 1;
						if (layers > floor)
						{
							floor = layers;
						}
					}
					int diff = index - orgIndex;
					geoBlock = new byte[diff + 1];
					geoBlock[0] = type;
					geo.position(orgIndex);
					geo.get(geoBlock, 1, diff);
					blocks[block] = geoBlock;
					break;
				default:
					throw new RuntimeException("Invalid geodata: " + rx + "_" + ry + "!");
			}
		}
	}
	
	/**
	 * Method NextGeoIndex.
	 * @param rx int
	 * @param ry int
	 * @param refId int
	 * @return int
	 */
	public static int NextGeoIndex(int rx, int ry, int refId)
	{
		if (!Config.ALLOW_GEODATA)
		{
			return 0;
		}
		int ix = rx - Config.GEO_X_FIRST;
		int iy = ry - Config.GEO_Y_FIRST;
		int regIndex = -1;
		synchronized (geodata)
		{
			byte[][][] region = geodata[ix][iy];
			for (int i = 0; i < region.length; i++)
			{
				if (region[i] == null)
				{
					regIndex = i;
					break;
				}
			}
			if (regIndex == -1)
			{
				byte[][][] resizedRegion = new byte[(regIndex = region.length) + 1][][];
				for (int i = 0; i < region.length; i++)
				{
					resizedRegion[i] = region[i];
				}
				geodata[ix][iy] = resizedRegion;
			}
			LoadGeodata(rx, ry, regIndex);
		}
		return 0x0f000000 | (ix << 16) | (iy << 8) | regIndex;
	}
	
	/**
	 * Method FreeGeoIndex.
	 * @param geoIndex int
	 */
	public static void FreeGeoIndex(int geoIndex)
	{
		if (!Config.ALLOW_GEODATA)
		{
			return;
		}
		if ((geoIndex & 0x0f000000) != 0x0f000000)
		{
			return;
		}
		int ix = (geoIndex & 0x00ff0000) >> 16;
		int iy = (geoIndex & 0x0000ff00) >> 8;
		int regIndex = geoIndex & 0x000000ff;
		synchronized (geodata)
		{
			geodata[ix][iy][regIndex] = null;
		}
	}
	
	/**
	 * Method removeGeoCollision.
	 * @param collision GeoCollision
	 * @param geoIndex int
	 */
	public static void removeGeoCollision(GeoCollision collision, int geoIndex)
	{
		Shape shape = collision.getShape();
		byte[][] around = collision.getGeoAround();
		if (around == null)
		{
			throw new RuntimeException("Attempt to remove unitialized collision: " + collision);
		}
		int minX = (shape.getXmin() - World.MAP_MIN_X - 16) >> 4;
		int minY = (shape.getYmin() - World.MAP_MIN_Y - 16) >> 4;
		int minZ = shape.getZmin();
		int maxZ = shape.getZmax();
		short height;
		byte old_nswe;
		for (int gX = 0; gX < around.length; gX++)
		{
			for (int gY = 0; gY < around[gX].length; gY++)
			{
				int geoX = minX + gX;
				int geoY = minY + gY;
				byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
				if (block == null)
				{
					continue;
				}
				int cellX = getCell(geoX);
				int cellY = getCell(geoY);
				int index = 0;
				byte blockType = block[index];
				index++;
				switch (blockType)
				{
					case BLOCKTYPE_COMPLEX:
						index += ((cellX << 3) + cellY) << 1;
						height = makeShort(block[index + 1], block[index]);
						old_nswe = (byte) (height & 0x0F);
						height &= 0xfff0;
						height >>= 1;
						if ((height < minZ) || (height > maxZ))
						{
							break;
						}
						height <<= 1;
						height &= 0xfff0;
						height |= old_nswe;
						if (collision.isConcrete())
						{
							height |= around[gX][gY];
						}
						else
						{
							height &= ~around[gX][gY];
						}
						block[index + 1] = (byte) (height >> 8);
						block[index] = (byte) (height & 0x00ff);
						break;
					case BLOCKTYPE_MULTILEVEL:
						int neededIndex = -1;
						int offset = (cellX << 3) + cellY;
						while (offset > 0)
						{
							byte lc = block[index];
							index += (lc << 1) + 1;
							offset--;
						}
						byte layers = block[index];
						index++;
						if ((layers <= 0) || (layers > MAX_LAYERS))
						{
							break;
						}
						short temph = Short.MIN_VALUE;
						old_nswe = NSWE_ALL;
						while (layers > 0)
						{
							height = makeShort(block[index + 1], block[index]);
							byte tmp_nswe = (byte) (height & 0x0F);
							height &= 0xfff0;
							height >>= 1;
							int z_diff_last = Math.abs(minZ - temph);
							int z_diff_curr = Math.abs(maxZ - height);
							if (z_diff_last > z_diff_curr)
							{
								old_nswe = tmp_nswe;
								temph = height;
								neededIndex = index;
							}
							layers--;
							index += 2;
						}
						if ((temph == Short.MIN_VALUE) || ((temph < minZ) || (temph > maxZ)))
						{
							break;
						}
						temph <<= 1;
						temph &= 0xfff0;
						temph |= old_nswe;
						if (collision.isConcrete())
						{
							temph |= around[gX][gY];
						}
						else
						{
							temph &= ~around[gX][gY];
						}
						block[neededIndex + 1] = (byte) (temph >> 8);
						block[neededIndex] = (byte) (temph & 0x00ff);
						break;
				}
			}
		}
	}
	
	/**
	 * Method applyGeoCollision.
	 * @param collision GeoCollision
	 * @param geoIndex int
	 */
	public static void applyGeoCollision(GeoCollision collision, int geoIndex)
	{
		Shape shape = collision.getShape();
		if ((shape.getXmax() == shape.getYmax()) && (shape.getXmax() == 0))
		{
			throw new RuntimeException("Attempt to add incorrect collision: " + collision);
		}
		boolean isFirstTime = false;
		int minX = (shape.getXmin() - World.MAP_MIN_X - 16) >> 4;
		int maxX = ((shape.getXmax() - World.MAP_MIN_X) + 16) >> 4;
		int minY = (shape.getYmin() - World.MAP_MIN_Y - 16) >> 4;
		int maxY = ((shape.getYmax() - World.MAP_MIN_Y) + 16) >> 4;
		int minZ = shape.getZmin();
		int maxZ = shape.getZmax();
		byte[][] around = collision.getGeoAround();
		if (around == null)
		{
			isFirstTime = true;
			byte[][] cells = new byte[(maxX - minX) + 1][(maxY - minY) + 1];
			for (int gX = minX; gX <= maxX; gX++)
			{
				for (int gY = minY; gY <= maxY; gY++)
				{
					int x = (gX << 4) + World.MAP_MIN_X;
					int y = (gY << 4) + World.MAP_MIN_Y;
					loop:
					for (int ax = x; ax < (x + 16); ax++)
					{
						for (int ay = y; ay < (y + 16); ay++)
						{
							if (shape.isInside(ax, ay))
							{
								cells[gX - minX][gY - minY] = 1;
								break loop;
							}
						}
					}
				}
			}
			around = new byte[(maxX - minX) + 1][(maxY - minY) + 1];
			for (int gX = 0; gX < cells.length; gX++)
			{
				for (int gY = 0; gY < cells[gX].length; gY++)
				{
					if (cells[gX][gY] == 1)
					{
						around[gX][gY] = NSWE_ALL;
						byte _nswe;
						if (gY > 0)
						{
							if (cells[gX][gY - 1] == 0)
							{
								_nswe = around[gX][gY - 1];
								_nswe |= SOUTH;
								around[gX][gY - 1] = _nswe;
							}
						}
						if ((gY + 1) < cells[gX].length)
						{
							if (cells[gX][gY + 1] == 0)
							{
								_nswe = around[gX][gY + 1];
								_nswe |= NORTH;
								around[gX][gY + 1] = _nswe;
							}
						}
						if (gX > 0)
						{
							if (cells[gX - 1][gY] == 0)
							{
								_nswe = around[gX - 1][gY];
								_nswe |= EAST;
								around[gX - 1][gY] = _nswe;
							}
						}
						if ((gX + 1) < cells.length)
						{
							if (cells[gX + 1][gY] == 0)
							{
								_nswe = around[gX + 1][gY];
								_nswe |= WEST;
								around[gX + 1][gY] = _nswe;
							}
						}
					}
				}
			}
			collision.setGeoAround(around);
		}
		short height;
		byte old_nswe, close_nswe;
		for (int gX = 0; gX < around.length; gX++)
		{
			for (int gY = 0; gY < around[gX].length; gY++)
			{
				int geoX = minX + gX;
				int geoY = minY + gY;
				byte[] block = getGeoBlockFromGeoCoords(geoX, geoY, geoIndex);
				if (block == null)
				{
					continue;
				}
				int cellX = getCell(geoX);
				int cellY = getCell(geoY);
				int index = 0;
				byte blockType = block[index];
				index++;
				switch (blockType)
				{
					case BLOCKTYPE_COMPLEX:
						index += ((cellX << 3) + cellY) << 1;
						height = makeShort(block[index + 1], block[index]);
						old_nswe = (byte) (height & 0x0F);
						height &= 0xfff0;
						height >>= 1;
						if ((height < minZ) || (height > maxZ))
						{
							break;
						}
						close_nswe = around[gX][gY];
						if (isFirstTime)
						{
							if (collision.isConcrete())
							{
								close_nswe &= old_nswe;
							}
							else
							{
								close_nswe &= ~old_nswe;
							}
							around[gX][gY] = close_nswe;
						}
						height <<= 1;
						height &= 0xfff0;
						height |= old_nswe;
						if (collision.isConcrete())
						{
							height &= ~close_nswe;
						}
						else
						{
							height |= close_nswe;
						}
						block[index + 1] = (byte) (height >> 8);
						block[index] = (byte) (height & 0x00ff);
						break;
					case BLOCKTYPE_MULTILEVEL:
						int neededIndex = -1;
						int offset = (cellX << 3) + cellY;
						while (offset > 0)
						{
							byte lc = block[index];
							index += (lc << 1) + 1;
							offset--;
						}
						byte layers = block[index];
						index++;
						if ((layers <= 0) || (layers > MAX_LAYERS))
						{
							break;
						}
						short temph = Short.MIN_VALUE;
						old_nswe = NSWE_ALL;
						while (layers > 0)
						{
							height = makeShort(block[index + 1], block[index]);
							byte tmp_nswe = (byte) (height & 0x0F);
							height &= 0xfff0;
							height >>= 1;
							int z_diff_last = Math.abs(minZ - temph);
							int z_diff_curr = Math.abs(maxZ - height);
							if (z_diff_last > z_diff_curr)
							{
								old_nswe = tmp_nswe;
								temph = height;
								neededIndex = index;
							}
							layers--;
							index += 2;
						}
						if ((temph == Short.MIN_VALUE) || ((temph < minZ) || (temph > maxZ)))
						{
							break;
						}
						close_nswe = around[gX][gY];
						if (isFirstTime)
						{
							if (collision.isConcrete())
							{
								close_nswe &= old_nswe;
							}
							else
							{
								close_nswe &= ~old_nswe;
							}
							around[gX][gY] = close_nswe;
						}
						temph <<= 1;
						temph &= 0xfff0;
						temph |= old_nswe;
						if (collision.isConcrete())
						{
							temph &= ~close_nswe;
						}
						else
						{
							temph |= close_nswe;
						}
						block[neededIndex + 1] = (byte) (temph >> 8);
						block[neededIndex] = (byte) (temph & 0x00ff);
						break;
				}
			}
		}
	}
	
	/**
	 * Method compact.
	 */
	public static void compact()
	{
		long total = 0, optimized = 0;
		BlockLink[] links;
		byte[][][] link_region;
		for (int mapX = 0; mapX < World.WORLD_SIZE_X; mapX++)
		{
			for (int mapY = 0; mapY < World.WORLD_SIZE_Y; mapY++)
			{
				if (geodata[mapX][mapY] == null)
				{
					continue;
				}
				total += BLOCKS_IN_MAP;
				links = GeoOptimizer.loadBlockMatches("geodata/matches/" + (mapX + Config.GEO_X_FIRST) + "_" + (mapY + Config.GEO_Y_FIRST) + ".matches");
				if (links == null)
				{
					continue;
				}
				for (int i = 0; i < links.length; i++)
				{
					link_region = geodata[links[i].linkMapX][links[i].linkMapY];
					if (link_region == null)
					{
						continue;
					}
					link_region[links[i].linkBlockIndex][0] = geodata[mapX][mapY][links[i].blockIndex][0];
					optimized++;
				}
			}
		}
		_log.info(String.format("GeoEngine: - Compacted %d of %d blocks...", optimized, total));
	}
	
	/**
	 * Method equalsData.
	 * @param a1 byte[]
	 * @param a2 byte[]
	 * @return boolean
	 */
	public static boolean equalsData(byte[] a1, byte[] a2)
	{
		if (a1.length != a2.length)
		{
			return false;
		}
		for (int i = 0; i < a1.length; i++)
		{
			if (a1[i] != a2[i])
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method compareGeoBlocks.
	 * @param mapX1 int
	 * @param mapY1 int
	 * @param blockIndex1 int
	 * @param mapX2 int
	 * @param mapY2 int
	 * @param blockIndex2 int
	 * @return boolean
	 */
	public static boolean compareGeoBlocks(int mapX1, int mapY1, int blockIndex1, int mapX2, int mapY2, int blockIndex2)
	{
		return equalsData(geodata[mapX1][mapY1][blockIndex1][0], geodata[mapX2][mapY2][blockIndex2][0]);
	}
	
	/**
	 * Method initChecksums.
	 */
	private static void initChecksums()
	{
		_log.info("GeoEngine: - Generating Checksums...");
		new File(Config.DATAPACK_ROOT, "geodata/checksum").mkdirs();
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		GeoOptimizer.checkSums = new int[World.WORLD_SIZE_X][World.WORLD_SIZE_Y][];
		for (int mapX = 0; mapX < World.WORLD_SIZE_X; mapX++)
		{
			for (int mapY = 0; mapY < World.WORLD_SIZE_Y; mapY++)
			{
				if (geodata[mapX][mapY] != null)
				{
					executor.execute(new GeoOptimizer.CheckSumLoader(mapX, mapY, geodata[mapX][mapY]));
				}
			}
		}
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method initBlockMatches.
	 * @param maxScanRegions int
	 */
	private static void initBlockMatches(int maxScanRegions)
	{
		_log.info("GeoEngine: Generating Block Matches...");
		new File(Config.DATAPACK_ROOT, "geodata/matches").mkdirs();
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int mapX = 0; mapX < World.WORLD_SIZE_X; mapX++)
		{
			for (int mapY = 0; mapY < World.WORLD_SIZE_Y; mapY++)
			{
				if ((geodata[mapX][mapY] != null) && (GeoOptimizer.checkSums != null) && (GeoOptimizer.checkSums[mapX][mapY] != null))
				{
					executor.execute(new GeoOptimizer.GeoBlocksMatchFinder(mapX, mapY, maxScanRegions));
				}
			}
		}
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method deleteChecksumFiles.
	 */
	public static void deleteChecksumFiles()
	{
		for (int mapX = 0; mapX < World.WORLD_SIZE_X; mapX++)
		{
			for (int mapY = 0; mapY < World.WORLD_SIZE_Y; mapY++)
			{
				if (geodata[mapX][mapY] == null)
				{
					continue;
				}
				new File(Config.DATAPACK_ROOT, "geodata/checksum/" + (mapX + Config.GEO_X_FIRST) + "_" + (mapY + Config.GEO_Y_FIRST) + ".crc").delete();
			}
		}
	}
	
	/**
	 * Method genBlockMatches.
	 * @param maxScanRegions int
	 */
	public static void genBlockMatches(int maxScanRegions)
	{
		initChecksums();
		initBlockMatches(maxScanRegions);
	}
	
	/**
	 * Method unload.
	 */
	public static void unload()
	{
		for (int mapX = 0; mapX < World.WORLD_SIZE_X; mapX++)
		{
			for (int mapY = 0; mapY < World.WORLD_SIZE_Y; mapY++)
			{
				geodata[mapX][mapY] = null;
			}
		}
	}
}

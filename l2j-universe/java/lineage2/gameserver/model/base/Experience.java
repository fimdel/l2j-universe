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
package lineage2.gameserver.model.base;

import lineage2.gameserver.Config;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Experience
{
	/**
	 * Field LEVEL.
	 */
	public final static long LEVEL[] =
	{
		-1L,
		0L,
		68L,
		363L,
		1168L,
		2884L,
		6038L,
		11287L,
		19423L,
		31378L,
		48229L,
		71202L,
		101677L,
		141193L,
		191454L,
		254330L,
		331867L,
		426288L,
		540000L,
		675596L,
		835862L,
		920357L,
		1015431L,
		1123336L,
		1246808L,
		1389235L,
		1554904L,
		1749413L,
		1980499L,
		2260321L,
		2634751L,
		2844287L,
		3093068L,
		3389496L,
		3744042L,
		4169902L,
		4683988L,
		5308556L,
		6074376L,
		7029248L,
		8342182L,
		8718976L,
		9289560L,
		9991807L,
		10856075L,
		11920512L,
		13233701L,
		14858961L,
		16882633L,
		19436426L,
		22977080L,
		24605660L,
		26635948L,
		29161263L,
		32298229L,
		36193556L,
		41033917L,
		47093035L,
		54711546L,
		64407353L,
		77947292L,
		85775204L,
		95595386L,
		107869713L,
		123174171L,
		142229446L,
		165944812L,
		195677269L,
		233072222L,
		280603594L,
		335732975L,
		383597045L,
		442752112L,
		516018015L,
		606913902L,
		719832095L,
		860289228L,
		1035327669L,
		1259458516L,
		1534688053L,
		1909610088L,
		2342785974L,
		2861857696L,
		3478378664L,
		4211039578L,
		5078544041L,
		10985069426L,
		19192594397L,
		33533938399L,
		43503026615L,
		61895085913L,
		84465260437L,
		112359133751L,
		146853833970L,
		189558054903L,
		242517343994L,
		343490462139L,
		538901012155L,
		923857608218L,
		1701666675991L,
		1801666675991L
	};
	
	/**
	 * Method penaltyModifier.
	 * @param count long
	 * @param percents double
	 * @return double
	 */
	public static double penaltyModifier(long count, double percents)
	{
		return Math.max(1. - ((count * percents) / 100), 0);
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public static int getMaxLevel()
	{
		return Config.ALT_MAX_LEVEL;
	}
	
	/**
	 * Method getMaxSubLevel.
	 * @return int
	 */
	public static int getMaxSubLevel()
	{
		return Config.ALT_MAX_SUB_LEVEL;
	}
	
	/**
	 * Method getLevel.
	 * @param thisExp long
	 * @return int
	 */
	public static int getLevel(long thisExp)
	{
		int level = 0;
		for (int i = 0; i < LEVEL.length; i++)
		{
			long exp = LEVEL[i];
			if (thisExp >= exp)
			{
				level = i;
			}
		}
		return level;
	}
	
	/**
	 * Method getExpForLevel.
	 * @param lvl int
	 * @return long
	 */
	public static long getExpForLevel(int lvl)
	{
		if (lvl >= Experience.LEVEL.length)
		{
			return 0;
		}
		return Experience.LEVEL[lvl];
	}
	
	/**
	 * Method getExpPercent.
	 * @param level int
	 * @param exp long
	 * @return double
	 */
	public static double getExpPercent(int level, long exp)
	{
		return ((exp - getExpForLevel(level)) / ((getExpForLevel(level + 1) - getExpForLevel(level)) / 100.0D)) * 0.01D;
	}
	
	/**
	 * Method getMaxDualLevel.
	 * @return int
	 */
	public static int getMaxDualLevel()
	{
		return Config.ALT_MAX_DUAL_SUB_LEVEL;
	}
}

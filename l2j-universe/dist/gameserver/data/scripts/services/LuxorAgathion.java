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
package services;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LuxorAgathion extends Functions
{
	/**
	 * Field INGRIDIENTS.
	 */
	private static final int[][] INGRIDIENTS =
	{
		{
			6471,
			25
		},
		{
			5094,
			50
		},
		{
			9814,
			4
		},
		{
			9816,
			5
		},
		{
			9817,
			5
		},
		{
			9815,
			3
		},
		{
			57,
			7500000
		}
	};
	/**
	 * Field OldAgathion. (value is 10408)
	 */
	private static final int OldAgathion = 10408;
	/**
	 * Field ShadowPurpleVikingCirclet. (value is 10315)
	 */
	private static final int ShadowPurpleVikingCirclet = 10315;
	/**
	 * Field ShadowGoldenVikingCirclet. (value is 10321)
	 */
	private static final int ShadowGoldenVikingCirclet = 10321;
	/**
	 * Field ANGEL_BRACELET_IDS.
	 */
	private static int[] ANGEL_BRACELET_IDS = new int[]
	{
		10320,
		10316,
		10317,
		10318,
		10319
	};
	/**
	 * Field DEVIL_BRACELET_IDS.
	 */
	private static int[] DEVIL_BRACELET_IDS = new int[]
	{
		10326,
		10322,
		10323,
		10324,
		10325
	};
	/**
	 * Field SUCCESS_RATE.
	 */
	private static int SUCCESS_RATE = 60;
	/**
	 * Field RARE_RATE.
	 */
	private static int RARE_RATE = 5;
	
	/**
	 * Method angelAgathion.
	 */
	public void angelAgathion()
	{
		agathion(ANGEL_BRACELET_IDS, 1);
	}
	
	/**
	 * Method devilAgathion.
	 */
	public void devilAgathion()
	{
		agathion(DEVIL_BRACELET_IDS, 2);
	}
	
	/**
	 * Method agathion.
	 * @param braceletes int[]
	 * @param type int
	 */
	private void agathion(int braceletes[], int type)
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		for (int[] ingridient : INGRIDIENTS)
		{
			if (getItemCount(player, ingridient[0]) < ingridient[1])
			{
				show("merchant/30098-2.htm", player, npc);
				return;
			}
		}
		for (int[] ingridient : INGRIDIENTS)
		{
			removeItem(player, ingridient[0], ingridient[1]);
		}
		if (!Rnd.chance(SUCCESS_RATE))
		{
			addItem(player, OldAgathion, 1);
			if (type == 1)
			{
				addItem(player, ShadowPurpleVikingCirclet, 1);
			}
			else
			{
				addItem(player, ShadowGoldenVikingCirclet, 1);
			}
			show("merchant/30098-3.htm", player, npc);
			return;
		}
		addItem(player, braceletes[Rnd.chance(RARE_RATE) ? 0 : Rnd.get(1, braceletes.length - 1)], 1);
		show("merchant/30098-4.htm", player, npc);
	}
}

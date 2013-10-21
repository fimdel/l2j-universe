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
package events.l2day;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lineage2.gameserver.model.reward.RewardData;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class l2day extends LettersCollection
{
	/**
	 * Field BSOE.
	 */
	private static int BSOE = 3958;
	/**
	 * Field BSOR.
	 */
	private static int BSOR = 3959;
	/**
	 * Field GUIDANCE.
	 */
	private static int GUIDANCE = 3926;
	/**
	 * Field WHISPER.
	 */
	private static int WHISPER = 3927;
	/**
	 * Field FOCUS.
	 */
	private static int FOCUS = 3928;
	/**
	 * Field ACUMEN.
	 */
	private static int ACUMEN = 3929;
	/**
	 * Field HASTE.
	 */
	private static int HASTE = 3930;
	/**
	 * Field AGILITY.
	 */
	private static int AGILITY = 3931;
	/**
	 * Field EMPOWER.
	 */
	private static int EMPOWER = 3932;
	/**
	 * Field MIGHT.
	 */
	private static int MIGHT = 3933;
	/**
	 * Field WINDWALK.
	 */
	private static int WINDWALK = 3934;
	/**
	 * Field SHIELD.
	 */
	private static int SHIELD = 3935;
	/**
	 * Field ENCH_WPN_D.
	 */
	private static int ENCH_WPN_D = 955;
	/**
	 * Field ENCH_WPN_C.
	 */
	private static int ENCH_WPN_C = 951;
	/**
	 * Field ENCH_WPN_B.
	 */
	private static int ENCH_WPN_B = 947;
	/**
	 * Field ENCH_WPN_A.
	 */
	private static int ENCH_WPN_A = 729;
	/**
	 * Field RABBIT_EARS.
	 */
	private static int RABBIT_EARS = 8947;
	/**
	 * Field FEATHERED_HAT.
	 */
	private static int FEATHERED_HAT = 8950;
	/**
	 * Field FAIRY_ANTENNAE.
	 */
	private static int FAIRY_ANTENNAE = 8949;
	/**
	 * Field ARTISANS_GOOGLES.
	 */
	private static int ARTISANS_GOOGLES = 8951;
	/**
	 * Field LITTLE_ANGEL_WING.
	 */
	private static int LITTLE_ANGEL_WING = 8948;
	/**
	 * Field RING_OF_ANT_QUIEEN.
	 */
	private static int RING_OF_ANT_QUIEEN = 6660;
	/**
	 * Field EARRING_OF_ORFEN.
	 */
	private static int EARRING_OF_ORFEN = 6661;
	/**
	 * Field RING_OF_CORE.
	 */
	private static int RING_OF_CORE = 6662;
	/**
	 * Field FRINTEZZA_NECKLACE.
	 */
	private static int FRINTEZZA_NECKLACE = 8191;
	static
	{
		_name = "l2day";
		_msgStarted = "scripts.events.l2day.AnnounceEventStarted";
		_msgEnded = "scripts.events.l2day.AnnounceEventStoped";
		EVENT_MANAGERS = new int[][]
		{
			{
				19541,
				145419,
				-3103,
				30419
			},
			{
				147485,
				-59049,
				-2980,
				9138
			},
			{
				109947,
				218176,
				-3543,
				63079
			},
			{
				-81363,
				151611,
				-3121,
				42910
			},
			{
				144741,
				28846,
				-2453,
				2059
			},
			{
				44192,
				-48481,
				-796,
				23331
			},
			{
				-13889,
				122999,
				-3109,
				40099
			},
			{
				116278,
				75498,
				-2713,
				12022
			},
			{
				82029,
				55936,
				-1519,
				58708
			},
			{
				147142,
				28555,
				-2261,
				59402
			},
			{
				82153,
				148390,
				-3466,
				57344
			},
		};
		_words.put("LineageII", new Integer[][]
		{
			{
				L,
				1
			},
			{
				I,
				1
			},
			{
				N,
				1
			},
			{
				E,
				2
			},
			{
				A,
				1
			},
			{
				G,
				1
			},
			{
				II,
				1
			}
		});
		_rewards.put("LineageII", new RewardData[]
		{
			new RewardData(GUIDANCE, 3, 3, 85000),
			new RewardData(WHISPER, 3, 3, 85000),
			new RewardData(FOCUS, 3, 3, 85000),
			new RewardData(ACUMEN, 3, 3, 85000),
			new RewardData(HASTE, 3, 3, 85000),
			new RewardData(AGILITY, 3, 3, 85000),
			new RewardData(EMPOWER, 3, 3, 85000),
			new RewardData(MIGHT, 3, 3, 85000),
			new RewardData(WINDWALK, 3, 3, 85000),
			new RewardData(SHIELD, 3, 3, 85000),
			new RewardData(BSOE, 1, 1, 50000),
			new RewardData(BSOR, 1, 1, 50000),
			new RewardData(ENCH_WPN_C, 3, 3, 14000),
			new RewardData(ENCH_WPN_B, 2, 2, 7000),
			new RewardData(ENCH_WPN_A, 1, 1, 7000),
			new RewardData(RABBIT_EARS, 1, 1, 5000),
			new RewardData(FEATHERED_HAT, 1, 1, 5000),
			new RewardData(FAIRY_ANTENNAE, 1, 1, 5000),
			new RewardData(RING_OF_ANT_QUIEEN, 1, 1, 100),
			new RewardData(RING_OF_CORE, 1, 1, 100),
		});
		_words.put("Throne", new Integer[][]
		{
			{
				T,
				1
			},
			{
				H,
				1
			},
			{
				R,
				1
			},
			{
				O,
				1
			},
			{
				N,
				1
			},
			{
				E,
				1
			}
		});
		_rewards.put("Throne", new RewardData[]
		{
			new RewardData(GUIDANCE, 3, 3, 85000),
			new RewardData(WHISPER, 3, 3, 85000),
			new RewardData(FOCUS, 3, 3, 85000),
			new RewardData(ACUMEN, 3, 3, 85000),
			new RewardData(HASTE, 3, 3, 85000),
			new RewardData(AGILITY, 3, 3, 85000),
			new RewardData(EMPOWER, 3, 3, 85000),
			new RewardData(MIGHT, 3, 3, 85000),
			new RewardData(WINDWALK, 3, 3, 85000),
			new RewardData(SHIELD, 3, 3, 85000),
			new RewardData(BSOE, 1, 1, 50000),
			new RewardData(BSOR, 1, 1, 50000),
			new RewardData(ENCH_WPN_D, 4, 4, 16000),
			new RewardData(ENCH_WPN_C, 3, 3, 11000),
			new RewardData(ENCH_WPN_B, 2, 2, 6000),
			new RewardData(ARTISANS_GOOGLES, 1, 1, 6000),
			new RewardData(LITTLE_ANGEL_WING, 1, 1, 5000),
			new RewardData(RING_OF_ANT_QUIEEN, 1, 1, 100),
			new RewardData(RING_OF_CORE, 1, 1, 100),
		});
		_words.put("NCSoft", new Integer[][]
		{
			{
				N,
				1
			},
			{
				C,
				1
			},
			{
				S,
				1
			},
			{
				O,
				1
			},
			{
				F,
				1
			},
			{
				T,
				1
			}
		});
		_rewards.put("NCSoft", new RewardData[]
		{
			new RewardData(GUIDANCE, 3, 3, 85000),
			new RewardData(WHISPER, 3, 3, 85000),
			new RewardData(FOCUS, 3, 3, 85000),
			new RewardData(ACUMEN, 3, 3, 85000),
			new RewardData(HASTE, 3, 3, 85000),
			new RewardData(AGILITY, 3, 3, 85000),
			new RewardData(EMPOWER, 3, 3, 85000),
			new RewardData(MIGHT, 3, 3, 85000),
			new RewardData(WINDWALK, 3, 3, 85000),
			new RewardData(SHIELD, 3, 3, 85000),
			new RewardData(BSOE, 1, 1, 50000),
			new RewardData(BSOR, 1, 1, 50000),
			new RewardData(ENCH_WPN_D, 4, 4, 16000),
			new RewardData(ENCH_WPN_C, 3, 3, 11000),
			new RewardData(ENCH_WPN_B, 2, 2, 6000),
			new RewardData(ARTISANS_GOOGLES, 1, 1, 6000),
			new RewardData(LITTLE_ANGEL_WING, 1, 1, 5000),
			new RewardData(RING_OF_ANT_QUIEEN, 1, 1, 100),
			new RewardData(RING_OF_CORE, 1, 1, 100),
		});
		final int DROP_MULT = 3;
		Map<Integer, Integer> temp = new HashMap<Integer, Integer>();
		for (Integer[][] ii : _words.values())
		{
			for (Integer[] i : ii)
			{
				Integer curr = temp.get(i[0]);
				if (curr == null)
				{
					temp.put(i[0], i[1]);
				}
				else
				{
					temp.put(i[0], curr + i[1]);
				}
			}
		}
		letters = new int[temp.size()][2];
		int i = 0;
		for (Entry<Integer, Integer> e : temp.entrySet())
		{
			letters[i++] = new int[]
			{
				e.getKey(),
				e.getValue() * DROP_MULT
			};
		}
	}
}

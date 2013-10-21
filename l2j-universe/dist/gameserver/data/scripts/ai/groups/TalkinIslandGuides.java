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
package ai.groups;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExRotation;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TalkinIslandGuides extends DefaultAI
{
	/**
	 * Field SEARCHING_MYST_POWER_SOLDIER. (value is 33016)
	 */
	private final static int SEARCHING_MYST_POWER_SOLDIER = 33016;
	/**
	 * Field GOING_INTO_REAL_WAR_SOLDIER. (value is 33014)
	 */
	private final static int GOING_INTO_REAL_WAR_SOLDIER = 33014;
	/**
	 * Field BACKUP_SEEKERS_ASSASSIN. (value is 33204)
	 */
	private final static int BACKUP_SEEKERS_ASSASSIN = 33204;
	/**
	 * Field SEARCHING_MYST_POWER_STRING.
	 */
	private final static NpcString SEARCHING_MYST_POWER_STRING = NpcString.S1_COME_FOLLOW_ME;
	/**
	 * Field GOING_INTO_REAL_WAR_STRING.
	 */
	private final static NpcString GOING_INTO_REAL_WAR_STRING = NpcString.S1_COME_FOLLOW_ME;
	/**
	 * Field BACKUP_SEEKERS_STRING.
	 */
	private final static NpcString BACKUP_SEEKERS_STRING = NpcString.HEY_KID_HARRY_UP_AND_FOLLOW_ME;
	/**
	 * Field SAY_INTERVAL. (value is 6000)
	 */
	private static final int SAY_INTERVAL = 6000;
	/**
	 * Field SMP_COORDS.
	 */
	private final static int[][] SMP_COORDS =
	{
		{
			-111979,
			255327,
			-1432
		},
		{
			-112442,
			254703,
			-1528
		},
		{
			-112188,
			254235,
			-1536
		},
		{
			-111471,
			253984,
			-1712
		},
		{
			-110689,
			253733,
			-1792
		}
	};
	/**
	 * Field GRW_COORDS_LEFT.
	 */
	private final static int[][] GRW_COORDS_LEFT =
	{
		{
			-110885,
			253533,
			-1776
		},
		{
			-111050,
			253183,
			-1776
		},
		{
			-111007,
			252706,
			-1832
		},
		{
			-110957,
			252400,
			-1928
		},
		{
			-110643,
			252365,
			-1976
		}
	};
	/**
	 * Field GRW_COORDS_RIGHT.
	 */
	private final static int[][] GRW_COORDS_RIGHT =
	{
		{
			-110618,
			253655,
			-1792
		},
		{
			-110296,
			253160,
			-1848
		},
		{
			-110271,
			253163,
			-1816
		},
		{
			-110156,
			252874,
			-1888
		},
		{
			-110206,
			252422,
			-1984
		}
	};
	/**
	 * Field BS_COORDS.
	 */
	private final static int[][] BS_COORDS =
	{
		{
			-117996,
			255845,
			-1320
		},
		{
			-117103,
			255538,
			-1296
		},
		{
			-115719,
			254792,
			-1504
		},
		{
			-114695,
			254741,
			-1528
		},
		{
			-114589,
			253517,
			-1528
		}
	};
	/**
	 * Field currentState.
	 */
	private int currentState;
	/**
	 * Field lastSayTime.
	 */
	private long lastSayTime = 0;
	
	/**
	 * Constructor for TalkinIslandGuides.
	 * @param actor NpcInstance
	 */
	public TalkinIslandGuides(NpcInstance actor)
	{
		super(actor);
		currentState = 0;
		lastSayTime = 0;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		final Creature target = actor.getFollowTarget();
		if ((target == null) || !(target instanceof Player))
		{
			actor.deleteMe();
			return false;
		}
		final int npcId = actor.getNpcId();
		int[][] coords;
		NpcString string;
		NpcString end_String;
		switch (npcId)
		{
			case SEARCHING_MYST_POWER_SOLDIER:
				coords = SMP_COORDS;
				string = SEARCHING_MYST_POWER_STRING;
				end_String = NpcString.S1_THAT_MAN_IN_FRONT_IS_IBANE;
				break;
			case BACKUP_SEEKERS_ASSASSIN:
				coords = BS_COORDS;
				string = BACKUP_SEEKERS_STRING;
				end_String = NpcString.TALK_TO_THAT_APPRENTICE_AND_GET_ON_KOOKARU;
				break;
			case GOING_INTO_REAL_WAR_SOLDIER:
				final double distLeft = target.getDistance(GRW_COORDS_LEFT[0][0], GRW_COORDS_LEFT[0][1], GRW_COORDS_LEFT[0][2]);
				final double distRight = target.getDistance(GRW_COORDS_RIGHT[0][0], GRW_COORDS_RIGHT[0][1], GRW_COORDS_RIGHT[0][2]);
				if (distLeft <= distRight)
				{
					coords = GRW_COORDS_LEFT;
				}
				else
				{
					coords = GRW_COORDS_RIGHT;
				}
				string = GOING_INTO_REAL_WAR_STRING;
				end_String = NpcString.S1_THAT_MAN_IN_FRONT_IS_HOLDEN;
				break;
			default:
				return false;
		}
		actor.setRunning();
		if ((actor.getDistance(target) < 100) || (currentState == 0) || (currentState >= coords.length))
		{
			if (currentState < coords.length)
			{
				actor.moveToLocation(coords[currentState][0], coords[currentState][1], coords[currentState][2], Rnd.get(0, 50), true);
				if (actor.getDestination() == null)
				{
					++currentState;
				}
			}
			else
			{
				Functions.npcSay(actor, end_String, ChatType.NPC_SAY, 800, target.getName());
				actor.deleteMe();
			}
		}
		else if (((lastSayTime + SAY_INTERVAL) < System.currentTimeMillis()) && (actor.getDestination() == null))
		{
			final int heading = actor.calcHeading(target.getX(), target.getY());
			actor.setHeading(heading);
			actor.broadcastPacket(new ExRotation(actor.getObjectId(), heading));
			lastSayTime = System.currentTimeMillis();
			Functions.npcSay(actor, string, ChatType.NPC_SAY, 800, target.getName());
		}
		return true;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}

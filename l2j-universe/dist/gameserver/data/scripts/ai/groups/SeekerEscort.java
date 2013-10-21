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
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExRotation;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import quests._10365_SeekerEscort;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeekerEscort extends DefaultAI
{
	/**
	 * Field SAY_INTERVAL. (value is 3000)
	 */
	private static final int SAY_INTERVAL = 3000;
	/**
	 * Field SAY_RAFF. (value is 50000)
	 */
	private static final int SAY_RAFF = 50000;
	/**
	 * Field SMP_COORDS.
	 */
	private final static int[][] SMP_COORDS =
	{
		{
			-110648,
			238936,
			-2950
		},
		{
			-110840,
			239240,
			-2951
		},
		{
			-111000,
			239512,
			-2950
		},
		{
			-111096,
			239800,
			-2950
		},
		{
			-110728,
			240248,
			-2950
		},
		{
			-110776,
			240584,
			-2951
		},
		{
			-111064,
			240616,
			-2950
		},
		{
			-111304,
			240360,
			-2950
		},
		{
			-111672,
			239848,
			-2950
		},
		{
			-111944,
			239736,
			-2950
		},
		{
			-112296,
			239816,
			-2950
		},
		{
			-112616,
			239928,
			-2950
		},
		{
			-112696,
			240264,
			-2950
		},
		{
			-112456,
			240536,
			-2950
		},
		{
			-112120,
			240536,
			-2950
		},
		{
			-112024,
			240264,
			-2950
		},
		{
			-112216,
			240168,
			-2950
		},
		{
			-112328,
			240232,
			-2950
		},
	};
	/**
	 * Field SMP_COORDS2.
	 */
	private final static int[][] SMP_COORDS2 =
	{
		{
			-112776,
			234072,
			-3097
		},
		{
			-112376,
			233656,
			-3137
		},
		{
			-112184,
			233480,
			-3156
		},
		{
			-112152,
			233112,
			-3159
		},
		{
			-112472,
			232920,
			-3128
		},
		{
			-112712,
			232536,
			-3104
		},
		{
			-112504,
			232040,
			-3125
		},
		{
			-112248,
			232072,
			-3149
		},
		{
			-112088,
			232360,
			-3165
		},
		{
			-111720,
			232584,
			-3202
		},
		{
			-111240,
			232728,
			-3250
		},
		{
			-110792,
			232424,
			-3294
		},
		{
			-110776,
			232072,
			-3295
		},
		{
			-111144,
			231864,
			-3259
		},
		{
			-111512,
			231992,
			-3223
		},
		{
			-111784,
			231976,
			-3196
		},
		{
			-111736,
			231816,
			-3200
		},
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
	 * Field lastSayTimer.
	 */
	private long lastSayTimer = 0;
	/**
	 * Field currentState1.
	 */
	private int currentState1;
	
	/**
	 * Constructor for SeekerEscort.
	 * @param actor NpcInstance
	 */
	public SeekerEscort(NpcInstance actor)
	{
		super(actor);
		currentState = 0;
		lastSayTime = 0;
		lastSayTimer = 0;
		currentState1 = 0;
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
		int[][] coords = {};
		if ((target == null) || !(target instanceof Player))
		{
			actor.deleteMe();
			return false;
		}
		final Player player = target.getPlayer();
		final QuestState st = player.getQuestState(_10365_SeekerEscort.class);
		final int zone = st.getInt("zone");
		int saytimes = st.getInt("saytimes");
		final int cond = st.getCond();
		actor.setRunning();
		if ((saytimes == 9) || (cond == 0))
		{
			actor.deleteMe();
			st.set("seeksp", 0);
			st.set("zone", 1);
			st.unset("saytimes");
			target.sendPacket(new ExShowScreenMessage(NpcString.KING_HAS_RETURNED_TO_DEF_RETURN_TO_DEF_AND_START_AGAIN, 5500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
			return false;
		}
		if ((lastSayTimer + SAY_RAFF) < System.currentTimeMillis())
		{
			lastSayTimer = System.currentTimeMillis();
			Functions.npcSay(actor, NpcString.RUFF_RUFF_RRRRRR, ChatType.NPC_SAY, 800, st.getPlayer().getName());
		}
		if (zone == 1)
		{
			coords = SMP_COORDS;
			if ((actor.getDistance(target) < 100) || (currentState >= coords.length) || (currentState == 0))
			{
				st.unset("saytimes");
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
					actor.teleToLocation(-112776, 234072, -3097);
					st.set("zone", 2);
				}
			}
			else if (((lastSayTime + SAY_INTERVAL) < System.currentTimeMillis()) && (actor.getDestination() == null))
			{
				final int heading = actor.calcHeading(target.getX(), target.getY());
				actor.setHeading(heading);
				actor.broadcastPacket(new ExRotation(actor.getObjectId(), heading));
				lastSayTime = System.currentTimeMillis();
				target.sendPacket(new ExShowScreenMessage(NpcString.CATCH_UP_TO_KING_HES_WAITING, 1500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				st.set("saytimes", ++saytimes);
			}
		}
		else if (zone == 2)
		{
			if ((actor.getDistance(target) >= 100) && ((lastSayTime + SAY_INTERVAL) < System.currentTimeMillis()))
			{
				lastSayTime = System.currentTimeMillis();
				target.sendPacket(new ExShowScreenMessage(NpcString.YOU_MUST_MOVE_TO_EXPLORATION_AREA_5_IN_ORDER_TO_CONTINUE, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				st.set("saytimes", ++saytimes);
			}
			else if (actor.getDistance(target) < 100)
			{
				st.set("zone", 3);
			}
			st.unset("saytimes");
		}
		else if (zone == 3)
		{
			coords = SMP_COORDS2;
			if ((actor.getDistance(target) < 100) || (currentState1 >= coords.length))
			{
				if (currentState1 < coords.length)
				{
					st.unset("saytimes");
					actor.moveToLocation(coords[currentState1][0], coords[currentState1][1], coords[currentState1][2], Rnd.get(0, 50), true);
					if (actor.getDestination() == null)
					{
						++currentState1;
					}
				}
				else
				{
					actor.deleteMe();
					st.set("seeksp", 0);
					st.set("zone", 1);
					st.setCond(2);
				}
			}
			else if (((lastSayTime + SAY_INTERVAL) < System.currentTimeMillis()) && (actor.getDestination() == null))
			{
				final int heading = actor.calcHeading(target.getX(), target.getY());
				actor.setHeading(heading);
				actor.broadcastPacket(new ExRotation(actor.getObjectId(), heading));
				lastSayTime = System.currentTimeMillis();
				target.sendPacket(new ExShowScreenMessage(NpcString.CATCH_UP_TO_KING_HES_WAITING, 1500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				st.set("saytimes", ++saytimes);
			}
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

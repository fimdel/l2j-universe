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
package ai.hermunkus_message;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Dwarvs extends Fighter
{
	/**
	 * Field MESSAGES_1.
	 */
	private static final int[] MESSAGES_1 =
	{
		1620059,
		1620060,
		1620061
	};
	/**
	 * Field MESSAGES_2.
	 */
	private static final int[] MESSAGES_2 =
	{
		1620068,
		1620069,
		1620070,
		1620071,
		1620072
	};
	/**
	 * Field ATTACK_IDS.
	 */
	private static final int[] ATTACK_IDS =
	{
		19171,
		19172
	};
	/**
	 * Field MOVE_LOC.
	 */
	private static final Location[] MOVE_LOC =
	{
		new Location(115830, -182103, -1400),
		new Location(115955, -181387, -1624),
		new Location(116830, -180257, -1176),
		new Location(116552, -180008, -1187),
	};
	/**
	 * Field WAY_1.
	 */
	private static final Location[] WAY_1 =
	{
		new Location(117147, -179248, -1120),
	};
	/**
	 * Field WAY_2.
	 */
	private static final Location[] WAY_2 =
	{
		new Location(116279, -179360, -112),
	};
	/**
	 * Field WAY_3.
	 */
	private static final Location[] WAY_3 =
	{
		new Location(115110, -178852, -896),
	};
	/**
	 * Field startBattle.
	 */
	private boolean startBattle = false;
	/**
	 * Field currentPoint.
	 */
	private int currentPoint = 0;
	/**
	 * Field currentPoint2.
	 */
	private int currentPoint2 = 0;
	/**
	 * Field loc.
	 */
	private Location loc;
	/**
	 * Field diedTentacle.
	 */
	private int diedTentacle = 0;
	/**
	 * Field way.
	 */
	private Location[] way = {};
	
	/**
	 * Constructor for Dwarvs.
	 * @param actor NpcInstance
	 */
	public Dwarvs(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10;
		switch ((getActor().getParameter("wayType", 1)))
		{
			case 1:
				way = WAY_1;
				break;
			case 2:
				way = WAY_2;
				break;
			case 3:
				way = WAY_3;
				break;
		}
	}
	
	/**
	 * Method onEvtScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		if (event.equalsIgnoreCase("SHOUT_ALL_1"))
		{
			final int msg = MESSAGES_1[Rnd.get(MESSAGES_1.length)];
			Functions.npcSayInRange(getActor(), 1500, NpcString.valueOf(msg));
			startBattle = true;
		}
		else if (event.equalsIgnoreCase("SHOUT_ALL_2"))
		{
			final int msg = MESSAGES_2[Rnd.get(MESSAGES_2.length)];
			Functions.npcSayInRange(getActor(), 1500, NpcString.valueOf(msg));
		}
		else if (event.equalsIgnoreCase("TENTACLE_DIE"))
		{
			diedTentacle++;
		}
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		super.onEvtArrived();
		if ((loc != null) && (getActor().getDistance(loc) <= 100))
		{
			if (currentPoint <= (MOVE_LOC.length - 1))
			{
				currentPoint++;
			}
			else
			{
				currentPoint2++;
			}
			loc = null;
		}
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		final NpcInstance actor = getActor();
		if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
		{
			final AggroList.AggroInfo ai = actor.getAggroList().get(target);
			return (ai != null) && (ai.hate > 0);
		}
		if (!startBattle)
		{
			return false;
		}
		return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		if (target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
		{
			return false;
		}
		if (!startBattle)
		{
			return false;
		}
		return super.checkAggression(target);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		if ((diedTentacle < 3) || (currentPoint > (MOVE_LOC.length - 1)))
		{
			final List<Creature> list = World.getAroundCharacters(getActor(), getActor().getAggroRange(), getActor().getAggroRange());
			for (Creature target : list)
			{
				if ((target != null) && !target.isDead() && ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
				{
					clearTasks();
					actor.setRunning();
					addTaskAttack(target);
					return true;
				}
			}
			if ((currentPoint > (MOVE_LOC.length - 1)) && (currentPoint2 <= (way.length - 1)))
			{
				if (loc == null)
				{
					loc = new Location((way[currentPoint2].getX() + Rnd.get(50)) - Rnd.get(50), (way[currentPoint2].getY() + Rnd.get(50)) - Rnd.get(50), (way[currentPoint2].getZ() + Rnd.get(50)) - Rnd.get(50));
				}
				actor.setRunning();
				clearTasks();
				addTaskMove(loc, true);
				doTask();
				return true;
			}
			return false;
		}
		else if ((diedTentacle >= 3) && (currentPoint <= (MOVE_LOC.length - 1)))
		{
			if (loc == null)
			{
				loc = new Location((MOVE_LOC[currentPoint].getX() + Rnd.get(50)) - Rnd.get(50), (MOVE_LOC[currentPoint].getY() + Rnd.get(50)) - Rnd.get(50), (MOVE_LOC[currentPoint].getZ() + Rnd.get(50)) - Rnd.get(50));
			}
			actor.setRunning();
			clearTasks();
			addTaskMove(loc, true);
			doTask();
			return true;
		}
		return false;
	}
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
	
	/**
	 * Method getMaxAttackTimeout.
	 * @return int
	 */
	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
}

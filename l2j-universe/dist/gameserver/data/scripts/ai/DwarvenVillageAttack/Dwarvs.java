package ai.DwarvenVillageAttack;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
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

public class Dwarvs extends Fighter
{
	private static final int[] MESSAGES_1 = { 1620059, 1620060, 1620061 };
	private static final int[] MESSAGES_2 = { 1620068, 1620069, 1620070, 1620071, 1620072 };
	private static final int[] ATTACK_IDS = { 19171, 19172 };

	private static final Location[] MOVE_LOC = {
			new Location(115830, -182103, -1400),
			new Location(115955, -181387, -1624),
			new Location(116830, -180257, -1176),
			new Location(116552, -180008, -1187), };

	private static final Location[] WAY_1 = { new Location(117147, -179248, -1120), };

	private static final Location[] WAY_2 = { new Location(116279, -179360, -112), };

	private static final Location[] WAY_3 = { new Location(115110, -178852, -896), };

	private boolean startBattle = false;
	private int currentPoint = 0;
	private int currentPoint2 = 0;
	private Location loc;
	private int diedTentacle = 0; // if 3 - start move
	private Location[] way = {};

	public Dwarvs(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10;
		switch(getActor().getParameter("wayType", 1))
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

	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		if(event.equalsIgnoreCase("SHOUT_ALL_1"))
		{
			int msg = MESSAGES_1[Rnd.get(MESSAGES_1.length)];
			Functions.npcSayInRange(getActor(), 1500, NpcString.valueOf(msg));
			startBattle = true;
		}
		else if(event.equalsIgnoreCase("SHOUT_ALL_2"))
		{
			int msg = MESSAGES_2[Rnd.get(MESSAGES_2.length)];
			Functions.npcSayInRange(getActor(), 1500, NpcString.valueOf(msg));
		}
		else if(event.equalsIgnoreCase("TENTACLE_DIE"))
			diedTentacle++;
	}

	@Override
	protected void onEvtArrived()
	{
		super.onEvtArrived();
		if(loc != null && getActor().getDistance(loc) <= 100)
		{
			if(currentPoint <= MOVE_LOC.length - 1)
				currentPoint++;
			else
				currentPoint2++;
			loc = null;
		}
	}

	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		NpcInstance actor = getActor();
		if(getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
		{
			AggroList.AggroInfo ai = actor.getAggroList().get(target);
			return ai != null && ai.hate > 0;
		}
		if(!startBattle)
			return false;

		return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		if(getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
			return false;

		if(target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
			return false;

		if(!startBattle)
			return false;

		return super.checkAggression(target);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;

		if(_def_think)
		{
			doTask();
			return true;
		}

		if(diedTentacle < 3 || currentPoint > MOVE_LOC.length - 1)
		{
			List<Creature> list = World.getAroundCharacters(getActor(), getActor().getAggroRange(), getActor().getAggroRange());
			for(Creature target : list)
				if(target != null && !target.isDead() && ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
				{
					clearTasks();
					actor.setRunning();
					addTaskAttack(target);
					return true;
				}

			if(currentPoint > MOVE_LOC.length - 1 && currentPoint2 <= way.length - 1)
			{
				if(loc == null)
					loc = new Location(way[currentPoint2].getX() + Rnd.get(50) - Rnd.get(50), way[currentPoint2].getY() + Rnd.get(50) - Rnd.get(50), way[currentPoint2].getZ() + Rnd.get(50) - Rnd.get(50));
				actor.setRunning();
				clearTasks();
				addTaskMove(loc, true);
				doTask();
				return true;
			}
			return false;
		}
		else if(diedTentacle >= 3 && currentPoint <= MOVE_LOC.length - 1)
		{
			if(loc == null)
				loc = new Location(MOVE_LOC[currentPoint].getX() + Rnd.get(50) - Rnd.get(50), MOVE_LOC[currentPoint].getY() + Rnd.get(50) - Rnd.get(50), MOVE_LOC[currentPoint].getZ() + Rnd.get(50) - Rnd.get(50));
			actor.setRunning();
			clearTasks();
			addTaskMove(loc, true);
			doTask();
			return true;
		}
		return false;
	}

	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}

	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
}

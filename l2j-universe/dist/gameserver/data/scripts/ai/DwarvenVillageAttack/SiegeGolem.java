package ai.DwarvenVillageAttack;

import java.util.List;

import instances.MemoryOfDisaster;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

public class SiegeGolem extends DefaultAI
{
	private static final int SKILL_ID = 16024;
	private static final int[] ATTACK_IDS = { 19172, 19217 };

	private static final Location[] MOVE_LOC = { new Location(116560, -179440, -1144), new Location(116608, -179205, -1176) };

	private long lastCastTime = 0;
	private int diedTeredor = 0;
	private int currentPoint = -1;
	private Location loc;

	public SiegeGolem(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 50;
		AI_TASK_ACTIVE_DELAY = 250;
	}

	@Override
	protected void onEvtThink()
	{
		super.onEvtThink();
		if(!getActor().getAggroList().isEmpty())
		{
			List<Creature> chars = World.getAroundCharacters(getActor());
			CollectionUtils.eqSort(chars, _nearestTargetComparator);
			for(Creature cha : chars)
				if(getActor().getAggroList().get(cha) != null && checkAggression(cha))
				{
					Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
					if(lastCastTime + sk.getHitTime() + sk.getReuseDelay() <= System.currentTimeMillis())
					{
						lastCastTime = System.currentTimeMillis();
						addTaskCast(cha, sk);
					}
				}
		}
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

		if(diedTeredor < 3 || currentPoint >= MOVE_LOC.length - 1)
		{
			List<Creature> list = World.getAroundCharacters(getActor(), getActor().getAggroRange(), getActor().getAggroRange());
			for(Creature target : list)
				if(target != null && !target.isDead() && ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
				{
					Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
					if(lastCastTime + sk.getHitTime() + sk.getReuseDelay() <= System.currentTimeMillis())
					{
						lastCastTime = System.currentTimeMillis();
						clearTasks();
						addTaskCast(target, sk);
						return true;
					}
					return false;
				}
		}
		else if(diedTeredor >= 3 && currentPoint < MOVE_LOC.length - 1)
		{
			if(loc == null || getActor().getDistance(loc) <= 100)
			{
				currentPoint++;
				loc = new Location(MOVE_LOC[currentPoint].getX() + Rnd.get(50) - Rnd.get(50), MOVE_LOC[currentPoint].getY() + Rnd.get(50) - Rnd.get(50), MOVE_LOC[currentPoint].getZ() + Rnd.get(50) - Rnd.get(50));
				if(currentPoint == 0)
				{
					Reflection r = getActor().getReflection();
					if(r instanceof MemoryOfDisaster)
						((MemoryOfDisaster) r).spawnTransparentTeredor();
				}
			}
			actor.setWalking();
			clearTasks();
			addTaskMove(loc, true);
			doTask();
			return true;
		}
		return false;
	}

	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if(success && skill_id == SKILL_ID)
			getActor().broadcastPacket(new Earthquake(getActor().getLoc(), 50, 4));
	}

	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		if(event.equalsIgnoreCase("TEREDOR_DIE"))
			diedTeredor++;
	}

	@Override
	public boolean canAttackCharacter(Creature target)
	{
		return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
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

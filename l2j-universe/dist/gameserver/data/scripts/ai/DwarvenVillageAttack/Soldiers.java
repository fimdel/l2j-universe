package ai.DwarvenVillageAttack;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class Soldiers extends Fighter
{
	private static final int[] ATTACK_IDS = { 19171, 19172 };

	public Soldiers(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10;
	}

	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
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
		return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		if(getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
			return false;

		if(target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
			return false;

		return super.checkAggression(target);
	}

}

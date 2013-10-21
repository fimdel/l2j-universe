package ai.DwarvenVillageAttack;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class TeredorSecond extends Fighter
{

	private static final int[] ATTACK_IDS = {
			19189,
			19191,
			19192,
			19193,
			19196,
			19197,
			19198,
			19199,
			19200,
			19201,
			19202,
			19203,
			19204,
			19205,
			19206,
			19207,
			19208,
			19209,
			19210,
			19211,
			19212,
			19213,
			19214,
			19215 };

	public TeredorSecond(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10;
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
		return target.isPlayable() || ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
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

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		broadCastScriptEvent("TEREDOR_DIE", 500);
	}
}

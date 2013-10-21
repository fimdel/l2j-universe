package ai;

import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author ALF
 */
public class SwampSoul extends Mystic
{

	public SwampSoul(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
	}

	@Override
	public boolean isGlobalAI()
	{
		return false;
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();

		actor.deleteMe();
		return false;

		//return super.thinkActive();
	}
}

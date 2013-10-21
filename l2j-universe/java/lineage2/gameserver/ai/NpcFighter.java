package lineage2.gameserver.ai;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class NpcFighter extends Fighter
{
	public NpcFighter(NpcInstance actor)
	{
		super(actor);
	}

	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
}

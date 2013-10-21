package ai.gardenofgenesis;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class ApherusLookoutBewildered extends Fighter
{
	public ApherusLookoutBewildered(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		NpcInstance actor = getActor();
		if(actor != null && killer != null && actor != killer)
		{
			NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
			NpcUtils.spawnSingle(19001, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
			NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
		}
	}
}
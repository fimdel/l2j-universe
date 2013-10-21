package ai.incubatorOfEvil;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Iqman
 */
public class Avanguard_Barton extends DefaultAI
{
	public Avanguard_Barton(NpcInstance actor)
	{
		super(actor);
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
		if(Rnd.chance(8))
		{
			Functions.npcSay(actor, NpcString.I_HIT_THINGS_THEY_FALL_DEAD);
		}
		return false;
	}
}
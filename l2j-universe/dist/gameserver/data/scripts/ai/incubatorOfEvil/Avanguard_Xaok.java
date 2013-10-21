package ai.incubatorOfEvil;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Iqman
 */
public class Avanguard_Xaok extends DefaultAI
{
	public Avanguard_Xaok(NpcInstance actor)
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
			Functions.npcSay(actor, NpcString.WHAT_DO_I_FEEL_WHEN_I_KILL_SHILENS_MONSTERS_RECOIL);
		}
		return false;
	}
}
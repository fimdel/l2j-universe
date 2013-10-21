package ai.GuillotineFortress;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/*
 * User: Mangol
 * Date: 17.12.12
 * Time: 19:39
 * Location: Guillotine Fortress
 */
public class GuillotineNpcPriest extends DefaultAI{
    public GuillotineNpcPriest(NpcInstance actor) {
        super(actor);
    }
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2) {
		super.onEvtScriptEvent(event, arg1, arg2);
		if(event.equalsIgnoreCase("SHOUT_PRIEST_1")) {
			Functions.npcSayInRange(getActor(), 1000, NpcString.IT_LEFT_NOTHING_BEHIND);
		}
		else if(event.equalsIgnoreCase("SHOUT_PRIEST_2")) {
            Functions.npcSayInRange(getActor(), 1000, NpcString.IM_IN_A_PICKLE_WE_CANT_GO_BACK_LETS_LOOK_FURTHER);

		}
		else if(event.equalsIgnoreCase("SHOUT_PRIEST_3")) {
            Functions.npcSayInRange(getActor(), 1000, NpcString.WELL_BEGIN_INTERNAL_PURIFICATION_PROCESS);
		}
	}
	@Override
	protected boolean randomWalk() {
		return false;
	}
}

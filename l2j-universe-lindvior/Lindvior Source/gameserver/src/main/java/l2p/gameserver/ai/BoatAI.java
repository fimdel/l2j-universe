package l2p.gameserver.ai;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.entity.boat.Boat;

/**
 * Author: VISTALL
 * Date:  16:56/28.12.2010
 */
public class BoatAI extends CharacterAI {
    public BoatAI(Creature actor) {
        super(actor);
    }

    @Override
    protected void onEvtArrived() {
        Boat actor = (Boat) getActor();
        if (actor == null)
            return;

        actor.onEvtArrived();
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }
}

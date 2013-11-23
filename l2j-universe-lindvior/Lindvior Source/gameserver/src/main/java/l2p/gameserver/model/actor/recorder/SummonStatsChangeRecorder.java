package l2p.gameserver.model.actor.recorder;

import l2p.gameserver.model.Summon;

/**
 * @author G1ta0
 */
public class SummonStatsChangeRecorder extends CharStatsChangeRecorder<Summon> {
    public SummonStatsChangeRecorder(Summon actor) {
        super(actor);
    }

    @Override
    protected void onSendChanges() {
        super.onSendChanges();

        if ((_changes & SEND_CHAR_INFO) == SEND_CHAR_INFO)
            _activeChar.sendPetInfo();
        else if ((_changes & BROADCAST_CHAR_INFO) == BROADCAST_CHAR_INFO)
            _activeChar.broadcastCharInfo();
    }
}

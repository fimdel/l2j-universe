package l2p.gameserver.listener.actor.ai;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.listener.AiListener;
import l2p.gameserver.model.Creature;

public interface OnAiIntentionListener extends AiListener {
    public void onAiIntention(Creature actor, CtrlIntention intention, Object arg0, Object arg1);
}

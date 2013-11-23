package l2p.gameserver.listener.actor.ai;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.listener.AiListener;
import l2p.gameserver.model.Creature;

public interface OnAiEventListener extends AiListener {
    public void onAiEvent(Creature actor, CtrlEvent evt, Object[] args);
}

package l2p.gameserver.listener.actor.npc;

import l2p.gameserver.listener.NpcListener;
import l2p.gameserver.model.instances.NpcInstance;

public interface OnDecayListener extends NpcListener {
    public void onDecay(NpcInstance actor);
}

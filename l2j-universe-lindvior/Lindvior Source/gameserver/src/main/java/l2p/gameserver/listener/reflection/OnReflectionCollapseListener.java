package l2p.gameserver.listener.reflection;

import l2p.commons.listener.Listener;
import l2p.gameserver.model.entity.Reflection;

public interface OnReflectionCollapseListener extends Listener<Reflection> {
    public void onReflectionCollapse(Reflection reflection);
}

package l2p.gameserver.listener.actor.player;

import l2p.gameserver.listener.PlayerListener;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;

public interface OnTeleportListener extends PlayerListener {
    public void onTeleport(Player player, int x, int y, int z, Reflection reflection);
}

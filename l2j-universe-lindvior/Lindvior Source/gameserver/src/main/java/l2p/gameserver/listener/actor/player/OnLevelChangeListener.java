package l2p.gameserver.listener.actor.player;

import l2p.gameserver.listener.PlayerListener;
import l2p.gameserver.model.Player;

/**
 * @author : Ragnarok
 * @date : 28.03.12  16:54
 */
public interface OnLevelChangeListener extends PlayerListener {
    public void onLevelChange(Player player, int oldLvl, int newLvl);
}

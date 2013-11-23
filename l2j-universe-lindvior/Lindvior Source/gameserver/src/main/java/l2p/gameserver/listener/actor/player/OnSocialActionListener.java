/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.listener.actor.player;

import l2p.gameserver.listener.PlayerListener;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.RequestActionUse;

public interface OnSocialActionListener extends PlayerListener {
    public void onSocialAction(Player player, GameObject target, RequestActionUse.Action action);
}

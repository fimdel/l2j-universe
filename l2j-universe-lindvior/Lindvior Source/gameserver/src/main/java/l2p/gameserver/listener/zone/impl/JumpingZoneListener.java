/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.listener.zone.impl;

import l2p.commons.lang.reference.HardReference;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.network.serverpackets.ExNotifyFlyMoveStart;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 09.06.12
 * Time: 20:33
 */
public class JumpingZoneListener implements OnZoneEnterLeaveListener {
    public static final OnZoneEnterLeaveListener STATIC = new JumpingZoneListener();

    @Override
    public void onZoneEnter(Zone zone, Creature actor) {
        if (!actor.isPlayer())
            return;

        Player player = actor.getPlayer();
        if (!player.isAwaking()) {
            return;
        }
        if (player.getTransformation() != 0) {
            return;
        }
        if (player.getSummonList().size() > 0) {
            return;
        }

        player.sendPacket(ExNotifyFlyMoveStart.STATIC);
        ThreadPoolManager.getInstance().schedule(new NotifyPacketTask(zone, player), 500L);
    }

    @Override
    public void onZoneLeave(Zone zone, Creature cha) {
    }

    public static class NotifyPacketTask extends RunnableImpl {
        private final Zone _zone;
        private final HardReference<Player> _playerRef;

        public NotifyPacketTask(Zone zone, Player player) {
            _zone = zone;
            _playerRef = player.getRef();
        }

        @Override
        public void runImpl() {
            Player player = _playerRef.get();
            if (player == null)
                return;

            if (!player.isInZone(Zone.ZoneType.JUMPING))
                return;

            player.sendPacket(ExNotifyFlyMoveStart.STATIC);
            ThreadPoolManager.getInstance().schedule(new NotifyPacketTask(_zone, player), 500L);
        }
    }
}


/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package zones;

import l2p.gameserver.Announcements;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.ResidenceSide;
import l2p.gameserver.network.serverpackets.EventTrigger;
import l2p.gameserver.network.serverpackets.ExCastleState;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiegePiece implements ScriptFile {
    private static final Logger _log = LoggerFactory.getLogger(SiegePiece.class);
    private static ZoneListener _zoneListener;
    private ResidenceSide _side;
    private static Castle _castle;

    @Override
    public void onLoad() {
        _zoneListener = new ZoneListener();

        for (Zone zone : ReflectionUtils.getZonesByType(ZoneType.peace_zone))
            zone.addListener(_zoneListener);
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public void broadcastPacket(int value, boolean b) {
        L2GameServerPacket trigger = new EventTrigger(value, b);
        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            player.sendPacket(trigger);

    }

    public void broadcastSend(Castle castle) {
        L2GameServerPacket trigger = new ExCastleState(castle);
        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            player.sendPacket(trigger);

    }

    public class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (zone.getParams() == null || !cha.isPlayer())
                return;

            Castle castle = ResidenceHolder.getInstance().getResidence(zone.getTemplate().getIndex());

            if (castle != null) {
                if (_side.ordinal() == 1 || _side.ordinal() == 0) {
                    ((Player) cha).sendPacket(new ExCastleState(castle));
                    broadcastPacket(_side.ordinal(), true);
                    broadcastSend(castle);
                    Announcements.getInstance().announceToAll(new ExCastleState(castle));
                    //broadcastPacket(_side.ordinal(),false);
                } else {
                    ((Player) cha).sendPacket(new ExCastleState(castle));
                    broadcastPacket(_side.ordinal(), true);
                    broadcastSend(castle);
                    Announcements.getInstance().announceToAll(new ExCastleState(castle));
                    //broadcastPacket(_side.ordinal(),false);
                }

                //return;
            }

            //Announcements.getInstance().announceToAll(new ExCastleState(castle));
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
        }
    }
}
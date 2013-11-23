package l2p.gameserver.model;

import l2p.commons.collections.EmptyIterator;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;

import java.util.Iterator;

/**
 * @author VISTALL
 * @date 14:03/22.06.2011
 */
public interface PlayerGroup extends Iterable<Player> {
    public static final PlayerGroup EMPTY = new PlayerGroup() {
        @Override
        public void broadCast(IStaticPacket... packet) {

        }

        @Override
        public Iterator<Player> iterator() {
            return EmptyIterator.getInstance();
        }
    };

    void broadCast(IStaticPacket... packet);
}

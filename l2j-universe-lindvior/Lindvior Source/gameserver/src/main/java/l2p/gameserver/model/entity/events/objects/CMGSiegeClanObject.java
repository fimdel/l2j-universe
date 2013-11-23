package l2p.gameserver.model.entity.events.objects;

import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.pledge.Clan;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author VISTALL
 * @date 17:22/14.05.2011
 */
public class CMGSiegeClanObject extends SiegeClanObject {
    /**
     *
     */
    private static final long serialVersionUID = 4217176710932052875L;
    private IntSet _players = new HashIntSet();
    private long _param;

    public CMGSiegeClanObject(String type, Clan clan, long param, long date) {
        super(type, clan, param, date);
        _param = param;
    }

    public CMGSiegeClanObject(String type, Clan clan, long param) {
        super(type, clan, param);
        _param = param;
    }

    public void addPlayer(int objectId) {
        _players.add(objectId);
    }

    @Override
    public long getParam() {
        return _param;
    }

    @Override
    public boolean isParticle(Player player) {
        return _players.contains(player.getObjectId());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setEvent(boolean start, SiegeEvent event) {
        for (int i : _players.toArray()) {
            Player player = GameObjectsStorage.getPlayer(i);
            if (player != null) {
                if (start)
                    player.addEvent(event);
                else
                    player.removeEvent(event);
                player.broadcastCharInfo();
            }
        }
    }

    public void setParam(long param) {
        _param = param;
    }

    public IntSet getPlayers() {
        return _players;
    }
}

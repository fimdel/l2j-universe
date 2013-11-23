package l2p.gameserver.model.entity.events.objects;

import l2p.gameserver.model.entity.events.GlobalEvent;

import java.io.Serializable;

/**
 * @author VISTALL
 * @date 16:28/10.12.2010
 */
public interface SpawnableObject extends Serializable {
    void spawnObject(GlobalEvent event);

    void despawnObject(GlobalEvent event);

    void refreshObject(GlobalEvent event);
}

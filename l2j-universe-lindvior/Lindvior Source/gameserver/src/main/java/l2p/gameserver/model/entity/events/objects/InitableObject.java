package l2p.gameserver.model.entity.events.objects;

import l2p.gameserver.model.entity.events.GlobalEvent;

import java.io.Serializable;

/**
 * @author VISTALL
 * @date 11:38/30.06.2011
 */
public interface InitableObject extends Serializable {
    void initObject(GlobalEvent e);
}

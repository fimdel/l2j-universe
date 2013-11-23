package l2p.gameserver.model.entity.events;

/**
 * @author VISTALL
 * @date 14:01/10.12.2010
 */
public interface EventAction {
    void call(GlobalEvent event);
}

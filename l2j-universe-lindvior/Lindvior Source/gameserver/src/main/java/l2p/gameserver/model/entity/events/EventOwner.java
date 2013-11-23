package l2p.gameserver.model.entity.events;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author VISTALL
 * @date 10:27/24.02.2011
 */
public abstract class EventOwner implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1561061111804154856L;
    private Set<GlobalEvent> _events = new HashSet<GlobalEvent>(2);

    @SuppressWarnings("unchecked")
    public <E extends GlobalEvent> E getEvent(Class<E> eventClass) {
        for (GlobalEvent e : _events) {
            if (e.getClass() == eventClass) // fast hack
                return (E) e;
            if (eventClass.isAssignableFrom(e.getClass())) //FIXME [VISTALL]    какойто другой способ определить
                return (E) e;
        }

        return null;
    }

    public void addEvent(GlobalEvent event) {
        _events.add(event);
    }

    public void removeEvent(GlobalEvent event) {
        _events.remove(event);
    }

    public Set<GlobalEvent> getEvents() {
        return _events;
    }
}

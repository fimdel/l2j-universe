/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test;

import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.handler.test.impl.DynamicQuest;
import l2p.gameserver.handler.test.impl.ManorMenuSelect;
import l2p.gameserver.handler.test.impl.MenuSelect;
import l2p.gameserver.handler.test.impl.ReceivePremium;

import java.util.HashMap;
import java.util.Map;

public class BypassHolder extends AbstractHolder {
    private static final BypassHolder _instance = new BypassHolder();

    private Map<String, IBypassHandler> _handlers = new HashMap();

    public BypassHolder() {
        registerBypass(new DynamicQuest());
        registerBypass(new MenuSelect());
        registerBypass(new ManorMenuSelect());
        registerBypass(new ReceivePremium());
    }

    public void clear() {
        this._handlers.clear();
    }

    public static BypassHolder getInstance() {
        return _instance;
    }

    public int size() {
        return this._handlers.size();
    }

    public IBypassHandler getHandler(String command) {
        for (Map.Entry bypass : this._handlers.entrySet())
            if (command.contains((CharSequence) bypass.getKey()))
                return (IBypassHandler) bypass.getValue();
        return null;
    }

    public void removeBypass(IBypassHandler bypass) {
        for (String command : bypass.getBypassList())
            this._handlers.remove(command.toLowerCase());
    }

    public void registerBypass(IBypassHandler bypass) {
        for (String command : bypass.getBypassList())
            this._handlers.put(command.toLowerCase(), bypass);
    }
}

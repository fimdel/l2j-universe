/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test.impl;

import l2p.gameserver.handler.test.IBypassHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class BypassHandler
        implements IBypassHandler {
    protected Map<String, String> parseBypass(String _command) {
        Map result = new HashMap();

        String params = _command.substring(_command.indexOf("?") + 1);
        StringTokenizer st = new StringTokenizer(params, "&");

        while (st.hasMoreTokens()) {
            String[] t = st.nextToken().split("=");

            result.put(t[0], t[1]);
        }

        if (_command.contains("?")) {
            result.put("main", _command.substring(0, _command.indexOf("?")));
        }
        return result;
    }
}

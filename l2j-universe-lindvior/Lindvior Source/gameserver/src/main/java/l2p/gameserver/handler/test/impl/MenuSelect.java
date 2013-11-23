/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test.impl;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.Map;

public class MenuSelect extends BypassHandler {
    public String[] getBypassList() {
        return new String[]{"menu_select"};
    }

    public void onBypassCommand(String command, Player activeChar, Creature target) {
        try {
            Map params = parseBypass(command);

            if ((params.size() < 3) || (target == null) || (!target.isNpc())) {
                return;
            }
            int ask = Integer.parseInt((String) params.get("ask"));
            int reply = Integer.parseInt((String) params.get("reply"));

            ((NpcInstance) target).onMenuSelect(activeChar, ask, reply);
        } catch (Exception e) {
            _log.error("Exception in " + getClass().getSimpleName());
        }
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract interface IBypassHandler {
    public static final Logger _log = LoggerFactory.getLogger(IBypassHandler.class);

    public abstract String[] getBypassList();

    public abstract void onBypassCommand(String paramString, Player paramPlayer, Creature paramCreature);
}

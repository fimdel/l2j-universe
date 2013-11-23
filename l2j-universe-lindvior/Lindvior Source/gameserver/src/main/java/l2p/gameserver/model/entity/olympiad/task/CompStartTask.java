/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.entity.olympiad.task;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Announcements;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.entity.olympiad.OlympiadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompStartTask extends RunnableImpl {
    private static final Logger _log = LoggerFactory.getLogger(CompStartTask.class);

    @Override
    public void runImpl() throws Exception {
        if (Olympiad.isOlympiadEnd()) {
            return;
        }

        Olympiad._manager = new OlympiadManager();
        Olympiad._inCompPeriod = true;

        new Thread(Olympiad._manager).start();
        ThreadPoolManager.getInstance().schedule(new CompEndTask(), Olympiad.getMillisToCompEnd());
        Announcements.getInstance().announceToAll(Msg.THE_OLYMPIAD_GAME_HAS_STARTED);
        _log.info("Olympiad System: Olympiad Game Started.");
    }
}
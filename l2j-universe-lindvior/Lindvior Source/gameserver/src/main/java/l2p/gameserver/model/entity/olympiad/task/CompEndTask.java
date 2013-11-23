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
import l2p.gameserver.model.entity.olympiad.OlympiadDatabase;
import l2p.gameserver.model.entity.olympiad.OlympiadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompEndTask extends RunnableImpl {
    private static final Logger _log = LoggerFactory.getLogger(CompEndTask.class);

    @Override
    public void runImpl() throws Exception {
        if (Olympiad.isOlympiadEnd()) {
            return;
        }

        Olympiad._inCompPeriod = false;

        try {
            OlympiadManager manager = Olympiad._manager;

            // Если остались игры, ждем их завершения еще одну минуту
            if ((manager != null) && !manager.getOlympiadGames().isEmpty()) {
                ThreadPoolManager.getInstance().schedule(new CompEndTask(), 60000);
                return;
            }

            Announcements.getInstance().announceToAll(Msg.THE_OLYMPIAD_GAME_HAS_ENDED);
            _log.info("Olympiad System: Olympiad Game Ended");
            OlympiadDatabase.save();
        } catch (Exception e) {
            _log.warn("Olympiad System: Failed to save Olympiad configuration:");
            _log.error("", e);
        }

        Olympiad.init();
    }
}
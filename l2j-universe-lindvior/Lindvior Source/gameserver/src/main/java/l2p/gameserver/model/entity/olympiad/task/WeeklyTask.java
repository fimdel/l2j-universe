/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.entity.olympiad.task;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Config;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class WeeklyTask extends RunnableImpl {
    private static final Logger _log = LoggerFactory.getLogger(WeeklyTask.class);

    @Override
    public void runImpl() throws Exception {
        Olympiad.doWeekTasks();
        _log.info("Olympiad System: Added weekly points to nobles");

        Calendar nextChange = Calendar.getInstance();

        Olympiad._nextWeeklyChange = nextChange.getTimeInMillis() + Config.ALT_OLY_WPERIOD;
    }
}
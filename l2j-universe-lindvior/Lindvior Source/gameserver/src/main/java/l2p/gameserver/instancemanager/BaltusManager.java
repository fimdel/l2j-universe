/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.instancemanager;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class BaltusManager {
    private static final Logger _log = LoggerFactory.getLogger(BaltusManager.class);
    private static BaltusManager _instance;
    private static final long _taskDelay = 1 * 86400000L;
    private static final int first = 1;
    private static final int second = 15;

    public static BaltusManager getInstance() {
        if (_instance == null)
            _instance = new BaltusManager();
        return _instance;
    }

    public BaltusManager() {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
    }

    private class ChangeStage extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 15 || Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1) {
                SpawnManager.getInstance().spawn("antharas_instance");
                _log.info("Open registration for antharas instance ");
            } else {
                SpawnManager.getInstance().despawn("antharas_instance");
            }
        }
    }

}

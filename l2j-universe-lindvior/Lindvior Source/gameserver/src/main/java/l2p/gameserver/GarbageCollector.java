package l2p.gameserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GarbageCollector {
    private static final Logger _log = LoggerFactory.getLogger(GarbageCollector.class);

    static {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new GarbageCollectorTask(), Config.GARBAGE_COLLECTOR_INTERVAL, Config.GARBAGE_COLLECTOR_INTERVAL);
    }

    static class GarbageCollectorTask implements Runnable {
        @Override
        public void run() {
            _log.info("GarbageCollector: start");

            System.gc();
            System.runFinalization();

            _log.info("GarbageCollector: finish");
        }
    }
}

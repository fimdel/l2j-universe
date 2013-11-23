package l2p.loginserver;

import l2p.commons.threading.RunnableImpl;

import java.util.concurrent.*;

public class ThreadPoolManager {
    private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;

    private static final ThreadPoolManager _instance = new ThreadPoolManager();

    public static final ThreadPoolManager getInstance() {
        return _instance;
    }

    private final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private ThreadPoolManager() {
        scheduleAtFixedRate(new RunnableImpl() {
            @Override
            public void runImpl() {
                executor.purge();
                scheduledExecutor.purge();
            }
        }, 600000L, 600000L);
    }

    private final long validate(long delay) {
        return Math.max(0, Math.min(MAX_DELAY, delay));
    }

    public void execute(Runnable r) {
        executor.execute(r);
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        return scheduledExecutor.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initial, long delay) {
        return scheduledExecutor.scheduleAtFixedRate(r, validate(initial), validate(delay), TimeUnit.MILLISECONDS);
    }
}

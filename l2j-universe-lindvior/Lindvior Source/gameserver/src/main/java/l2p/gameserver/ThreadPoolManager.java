package l2p.gameserver;

import l2p.commons.threading.LoggingRejectedExecutionHandler;
import l2p.commons.threading.PriorityThreadFactory;
import l2p.commons.threading.RunnableImpl;

import java.util.concurrent.*;

public class ThreadPoolManager {
    private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;

    private static final ThreadPoolManager _instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return _instance;
    }

    private final ScheduledThreadPoolExecutor _scheduledExecutor;
    private final ThreadPoolExecutor _executor;
    private ThreadPoolExecutor _pathfindThreadPool;

    private ScheduledThreadPoolExecutor _moveScheduledThreadPool, _npcAiScheduledThreadPool, _playerAiScheduledThreadPool;

    private boolean _shutdown;

    private ThreadPoolManager() {
        _scheduledExecutor = new ScheduledThreadPoolExecutor(Config.SCHEDULED_THREAD_POOL_SIZE, new PriorityThreadFactory("ScheduledThreadPool", Thread.NORM_PRIORITY), new LoggingRejectedExecutionHandler());
        _scheduledExecutor.setKeepAliveTime(1, TimeUnit.SECONDS);
        _scheduledExecutor.allowCoreThreadTimeOut(true);
        _executor = new ThreadPoolExecutor(Config.EXECUTOR_THREAD_POOL_SIZE, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("ThreadPoolExecutor", Thread.NORM_PRIORITY), new LoggingRejectedExecutionHandler());

        _moveScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.THREAD_P_MOVE, new PriorityThreadFactory("MoveSTPool", Thread.NORM_PRIORITY + 3));
        _npcAiScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.NPC_AI_MAX_THREAD, new PriorityThreadFactory("NpcAISTPool", Thread.NORM_PRIORITY - 2));
        _playerAiScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.PLAYER_AI_MAX_THREAD, new PriorityThreadFactory("PlayerAISTPool", Thread.NORM_PRIORITY + 2));
        _pathfindThreadPool = new ThreadPoolExecutor(Config.THREAD_P_PATHFIND, Config.THREAD_P_PATHFIND, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("Pathfind Pool", Thread.NORM_PRIORITY));

        _moveScheduledThreadPool.setKeepAliveTime(1, TimeUnit.SECONDS);
        _npcAiScheduledThreadPool.setKeepAliveTime(1, TimeUnit.SECONDS);
        _playerAiScheduledThreadPool.setKeepAliveTime(1, TimeUnit.SECONDS);

        _moveScheduledThreadPool.allowCoreThreadTimeOut(true);
        _npcAiScheduledThreadPool.allowCoreThreadTimeOut(true);
        _playerAiScheduledThreadPool.allowCoreThreadTimeOut(true);
        _pathfindThreadPool.allowCoreThreadTimeOut(true);

        //Очистка каждые 5 минут
        scheduleAtFixedRate(new RunnableImpl() {
            @Override
            public void runImpl() {
                _playerAiScheduledThreadPool.purge();
                _npcAiScheduledThreadPool.purge();
                _moveScheduledThreadPool.purge();
                _pathfindThreadPool.purge();
                _scheduledExecutor.purge();
                _executor.purge();
            }
        }, 300000L, 300000L);
    }

    private long validate(long delay) {
        return Math.max(0, Math.min(MAX_DELAY, delay));
    }

    public boolean isShutdown() {
        return _shutdown;
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        try {
            return _scheduledExecutor.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            if (!isShutdown())
                Thread.dumpStack();
            return null;
        }
    }

    public ScheduledFuture<?> scheduleMove(Runnable r, long delay) {
        try {
            return _moveScheduledThreadPool.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            if (!isShutdown())
                Thread.dumpStack();
            return null;
        }
    }

    public ScheduledFuture<?> scheduleAi(Runnable r, long delay, boolean isPlayer) {
        try {
            if (isPlayer)
                return _playerAiScheduledThreadPool.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
            return _npcAiScheduledThreadPool.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            if (!isShutdown())
                Thread.dumpStack();
            return null;
        }
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initial, long delay) {
        try {
            return _scheduledExecutor.scheduleAtFixedRate(r, validate(initial), validate(delay), TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            if (!isShutdown())
                Thread.dumpStack();
            return null;
        }
    }

    public ScheduledFuture<?> scheduleAtFixedDelay(Runnable r, long initial, long delay) {
        try {
            return _scheduledExecutor.scheduleWithFixedDelay(r, validate(initial), validate(delay), TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            if (!isShutdown())
                Thread.dumpStack();
            return null;
        }
    }

    public void execute(Runnable r) {
        _executor.execute(r);
    }

    public void executeMove(Runnable r) {
        _moveScheduledThreadPool.execute(r);
    }

    public void executePathfind(Runnable r) {
        _pathfindThreadPool.execute(r);
    }

    public void shutdown() throws InterruptedException {
        _shutdown = true;
        try {
            _npcAiScheduledThreadPool.shutdown();
            _playerAiScheduledThreadPool.shutdown();
            _pathfindThreadPool.shutdown();
            _moveScheduledThreadPool.shutdown();
            _scheduledExecutor.shutdown();

            _npcAiScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            _playerAiScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            _pathfindThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            _moveScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            _scheduledExecutor.awaitTermination(1, TimeUnit.SECONDS);
        } finally {
            _executor.shutdown();
            _executor.awaitTermination(1, TimeUnit.SECONDS);
            System.out.println("All ThreadPools are now stoped.");
        }
    }

    public CharSequence getStats() {
        StringBuilder list = new StringBuilder();

        list.append("ScheduledThreadPool\n");
        list.append("=================================================\n");
        list.append("\tgetActiveCount: ...... ").append(_scheduledExecutor.getActiveCount()).append("\n");
        list.append("\tgetCorePoolSize: ..... ").append(_scheduledExecutor.getCorePoolSize()).append("\n");
        list.append("\tgetPoolSize: ......... ").append(_scheduledExecutor.getPoolSize()).append("\n");
        list.append("\tgetLargestPoolSize: .. ").append(_scheduledExecutor.getLargestPoolSize()).append("\n");
        list.append("\tgetMaximumPoolSize: .. ").append(_scheduledExecutor.getMaximumPoolSize()).append("\n");
        list.append("\tgetCompletedTaskCount: ").append(_scheduledExecutor.getCompletedTaskCount()).append("\n");
        list.append("\tgetQueuedTaskCount: .. ").append(_scheduledExecutor.getQueue().size()).append("\n");
        list.append("\tgetTaskCount: ........ ").append(_scheduledExecutor.getTaskCount()).append("\n");
        list.append("ThreadPoolExecutor\n");
        list.append("=================================================\n");
        list.append("\tgetActiveCount: ...... ").append(_executor.getActiveCount()).append("\n");
        list.append("\tgetCorePoolSize: ..... ").append(_executor.getCorePoolSize()).append("\n");
        list.append("\tgetPoolSize: ......... ").append(_executor.getPoolSize()).append("\n");
        list.append("\tgetLargestPoolSize: .. ").append(_executor.getLargestPoolSize()).append("\n");
        list.append("\tgetMaximumPoolSize: .. ").append(_executor.getMaximumPoolSize()).append("\n");
        list.append("\tgetCompletedTaskCount: ").append(_executor.getCompletedTaskCount()).append("\n");
        list.append("\tgetQueuedTaskCount: .. ").append(_executor.getQueue().size()).append("\n");
        list.append("\tgetTaskCount: ........ ").append(_executor.getTaskCount()).append("\n");
        list.append("_moveScheduledThreadPool\n");
        list.append("=================================================\n");
        list.append("\tgetActiveCount: ...... ").append(_moveScheduledThreadPool.getActiveCount()).append("\n");
        list.append("\tgetCorePoolSize: ..... ").append(_moveScheduledThreadPool.getCorePoolSize()).append("\n");
        list.append("\tgetPoolSize: ......... ").append(_moveScheduledThreadPool.getPoolSize()).append("\n");
        list.append("\tgetLargestPoolSize: .. ").append(_moveScheduledThreadPool.getLargestPoolSize()).append("\n");
        list.append("\tgetMaximumPoolSize: .. ").append(_moveScheduledThreadPool.getMaximumPoolSize()).append("\n");
        list.append("\tgetCompletedTaskCount: ").append(_moveScheduledThreadPool.getCompletedTaskCount()).append("\n");
        list.append("\tgetQueuedTaskCount: .. ").append(_moveScheduledThreadPool.getQueue().size()).append("\n");
        list.append("\tgetTaskCount: ........ ").append(_moveScheduledThreadPool.getTaskCount()).append("\n");
        list.append("_pathfindThreadPool\n");
        list.append("=================================================\n");
        list.append("\tgetActiveCount: ...... ").append(_pathfindThreadPool.getActiveCount()).append("\n");
        list.append("\tgetCorePoolSize: ..... ").append(_pathfindThreadPool.getCorePoolSize()).append("\n");
        list.append("\tgetPoolSize: ......... ").append(_pathfindThreadPool.getPoolSize()).append("\n");
        list.append("\tgetLargestPoolSize: .. ").append(_pathfindThreadPool.getLargestPoolSize()).append("\n");
        list.append("\tgetMaximumPoolSize: .. ").append(_pathfindThreadPool.getMaximumPoolSize()).append("\n");
        list.append("\tgetCompletedTaskCount: ").append(_pathfindThreadPool.getCompletedTaskCount()).append("\n");
        list.append("\tgetQueuedTaskCount: .. ").append(_pathfindThreadPool.getQueue().size()).append("\n");
        list.append("\tgetTaskCount: ........ ").append(_pathfindThreadPool.getTaskCount()).append("\n");
        list.append("_npcAiScheduledThreadPool\n");
        list.append("=================================================\n");
        list.append("\tgetActiveCount: ...... ").append(_npcAiScheduledThreadPool.getActiveCount()).append("\n");
        list.append("\tgetCorePoolSize: ..... ").append(_npcAiScheduledThreadPool.getCorePoolSize()).append("\n");
        list.append("\tgetPoolSize: ......... ").append(_npcAiScheduledThreadPool.getPoolSize()).append("\n");
        list.append("\tgetLargestPoolSize: .. ").append(_npcAiScheduledThreadPool.getLargestPoolSize()).append("\n");
        list.append("\tgetMaximumPoolSize: .. ").append(_npcAiScheduledThreadPool.getMaximumPoolSize()).append("\n");
        list.append("\tgetCompletedTaskCount: ").append(_npcAiScheduledThreadPool.getCompletedTaskCount()).append("\n");
        list.append("\tgetQueuedTaskCount: .. ").append(_npcAiScheduledThreadPool.getQueue().size()).append("\n");
        list.append("\tgetTaskCount: ........ ").append(_npcAiScheduledThreadPool.getTaskCount()).append("\n");
        list.append("_playerAiScheduledThreadPool\n");
        list.append("=================================================\n");
        list.append("\tgetActiveCount: ...... ").append(_playerAiScheduledThreadPool.getActiveCount()).append("\n");
        list.append("\tgetCorePoolSize: ..... ").append(_playerAiScheduledThreadPool.getCorePoolSize()).append("\n");
        list.append("\tgetPoolSize: ......... ").append(_playerAiScheduledThreadPool.getPoolSize()).append("\n");
        list.append("\tgetLargestPoolSize: .. ").append(_playerAiScheduledThreadPool.getLargestPoolSize()).append("\n");
        list.append("\tgetMaximumPoolSize: .. ").append(_playerAiScheduledThreadPool.getMaximumPoolSize()).append("\n");
        list.append("\tgetCompletedTaskCount: ").append(_playerAiScheduledThreadPool.getCompletedTaskCount()).append("\n");
        list.append("\tgetQueuedTaskCount: .. ").append(_playerAiScheduledThreadPool.getQueue().size()).append("\n");
        list.append("\tgetTaskCount: ........ ").append(_playerAiScheduledThreadPool.getTaskCount()).append("\n");
        return list;
    }
}

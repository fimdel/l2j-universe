package l2p.gameserver.taskmanager;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.threading.SteppingRunnableQueueManager;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;

/**
 * Менеджер задач AI, шаг выполенния задач 250 мс.
 *
 * @author G1ta0
 */
public class AiTaskManager extends SteppingRunnableQueueManager {
    private final static long TICK = 250L;

    private final static AiTaskManager[] _instances = new AiTaskManager[Config.AI_TASK_MANAGER_COUNT];

    static {
        for (int i = 0; i < _instances.length; i++)
            _instances[i] = new AiTaskManager();
    }

    private static int randomizer = 0;

    public static AiTaskManager getInstance() {
        return _instances[randomizer++ & _instances.length - 1];
    }

    private AiTaskManager() {
        super(TICK);
        ThreadPoolManager.getInstance().scheduleAtFixedRate(this, Rnd.get(TICK), TICK);
        //Очистка каждую минуту
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl() {
            @Override
            public void runImpl() throws Exception {
                AiTaskManager.this.purge();
            }

        }, 60000L, 60000L);
    }

    public CharSequence getStats(int num) {
        return _instances[num].getStats();
    }
}

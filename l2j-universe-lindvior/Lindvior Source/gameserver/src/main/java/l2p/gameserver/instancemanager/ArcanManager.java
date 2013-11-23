package l2p.gameserver.instancemanager;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.EventTrigger;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.network.serverpackets.components.NpcString;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 29.10.12 Time: 16:46
 */
public class ArcanManager {
    private static ArcanManager _instance;
    private static final long _taskDelay = 30 * 60 * 1000L; // 30min
    private static int _Stage = 0;
    public static int _BLUE = 262001;
    public static int _RED = 262003;

    public static ArcanManager getInstance() {
        if (_instance == null) {
            _instance = new ArcanManager();
        }
        return _instance;
    }

    public ArcanManager() {
        setStage(_BLUE);
        SpawnManager.getInstance().despawn("magmeld_ritual");
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
    }

    public class ChangeStage extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            if (getStage() == _RED) {
                setStage(_BLUE);
                broadcastPacket(_BLUE, true, false);
                broadcastPacket(_RED, false, false);
                SpawnManager.getInstance().despawn("magmeld_ritual");
            } else {
                setStage(_RED);
                broadcastPacket(_RED, true, true);
                broadcastPacket(_BLUE, false, false);
                SpawnManager.getInstance().spawn("magmeld_ritual");
            }
        }
    }

    public void broadcastPacket(int value, boolean b, boolean message) {
        L2GameServerPacket trigger = new EventTrigger(value, b);
        for (Player player : GameObjectsStorage.getAllPlayersForIterate()) {
            player.sendPacket(trigger);
        }

        if (message) {
            L2GameServerPacket sm = new ExShowScreenMessage(NpcString.DARK_POWER_SEEPS_OUT_FROM_THE_MIDDLE_OF_THE_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true);
            for (Player player : GameObjectsStorage.getAllPlayersForIterate()) {
                player.sendPacket(sm);
            }
        }
    }

    public static int getStage() {
        return _Stage;
    }

    public void setStage(int s) {
        _Stage = s;
    }
}

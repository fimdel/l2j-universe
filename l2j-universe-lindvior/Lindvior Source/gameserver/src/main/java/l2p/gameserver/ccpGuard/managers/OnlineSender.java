package l2p.gameserver.ccpGuard.managers;


import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ccpGuard.ConfigProtect;
import l2p.gameserver.ccpGuard.packets.OnlineSpecial;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

public class OnlineSender implements Runnable {
    private static ScheduledFuture _task = null;
    private static final Logger _log = Logger.getLogger(OnlineSender.class.getName());

    private static OnlineSender instance = null;

    public OnlineSender() {
        if (ConfigProtect.PROTECT_ONLINE_PACKET_TIME > 0) {
            _log.config("OnlineSender: Loaded enable");
            start();
        } else {
            _log.config("OnlineSender: Loaded disable");
        }
    }

    public static OnlineSender getInstance() {
        if (instance == null)
            instance = new OnlineSender();
        return instance;
    }

    public void run() {
        int online = GameObjectsStorage.getAllPlayersCount();
        for (Player player : GameObjectsStorage.getAllPlayersForIterate()) {
            sendOnline(player, online);
        }
    }

    public void sendOnline(Player player, int online) {
        player.sendPacket(new OnlineSpecial(ConfigProtect.PROTECT_ONLINE_PACKET_X, ConfigProtect.PROTECT_ONLINE_PACKET_Y, ConfigProtect.PROTECT_ONLINE_PACKET_COLOR, online));
    }

    public void start() {
        _task = ThreadPoolManager.getInstance().scheduleAtFixedRate(getInstance(), ConfigProtect.PROTECT_ONLINE_PACKET_TIME, ConfigProtect.PROTECT_ONLINE_PACKET_TIME);
    }

    public void stop() {
        if (_task != null) {
            _task.cancel(true);
            _task = null;
        }
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.listener.event;

import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2p.gameserver.scripts.ScriptFile;

public interface CollectionEventGlobalInterfaceListner extends ScriptFile, OnDeathListener, OnPlayerEnterListener {
    boolean isActive();

    void spawnEventManagers();

    void unSpawnEventManagers();

    void startEvent();

    void stopEvent();

    void exchange(String[] var);
}

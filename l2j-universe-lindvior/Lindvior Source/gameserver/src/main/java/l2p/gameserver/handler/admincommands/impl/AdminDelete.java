package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Spawner;
import l2p.gameserver.model.instances.NpcInstance;
import org.apache.commons.lang3.math.NumberUtils;

public class AdminDelete implements IAdminCommandHandler {
    private static enum Commands {
        admin_delete
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().CanEditNPC)
            return false;

        switch (command) {
            case admin_delete:
                GameObject obj = wordList.length == 1 ? activeChar.getTarget() : GameObjectsStorage.getNpc(NumberUtils.toInt(wordList[1]));
                if (obj != null && obj.isNpc()) {
                    NpcInstance target = (NpcInstance) obj;
                    target.deleteMe();

                    Spawner spawn = target.getSpawn();
                    if (spawn != null)
                        spawn.stopRespawn();

                    //TODO SimpleSpawner SpawnTable.getInstance().deleteSpawn(spawn);
                } else
                    activeChar.sendPacket(Msg.INVALID_TARGET);
                break;
        }

        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.utils.NpcUtils;

public class DimensionalTreasureChest extends Functions {
    private int x, y, z;

    public void deleteChest() {
        NpcInstance npc = getNpc();

        if (npc == null) {
            return;
        }

        x = npc.getX();
        y = npc.getY();
        z = npc.getZ();

        npc.deleteMe(); // убираем сундук
        NpcUtils.spawnSingle(141, x, y, z); // спавним телепорт
    }

    public void teleport() {
        Player player = getSelf();
        player.teleToLocation(75375, -213437, -3712); //todo точность координат (поидее координаты главаря в комнате)
    }
}

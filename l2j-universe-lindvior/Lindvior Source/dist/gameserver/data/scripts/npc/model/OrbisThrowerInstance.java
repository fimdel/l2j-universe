/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class OrbisThrowerInstance extends MonsterInstance {
    private final int _weaponId;

    public OrbisThrowerInstance(int ObjectID, NpcTemplate temp) {
        super(ObjectID, temp);
        _weaponId = getParameter("weapon_id", 17372);
    }

    public void startAttackStanceTask() {
        equipWeapon();
        super.startAttackStanceTask();
    }

    public void stopAttackStanceTask() {
        unequipWeapon();
        super.stopAttackStanceTask();
    }

    private void unequipWeapon() {
        if (isAlikeDead())
            return;


        setRHandId(0);
        broadcastCharInfoImpl();
    }

    private void equipWeapon() {
        setRHandId(_weaponId);
        broadcastCharInfoImpl();
    }
}

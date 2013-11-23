package ai.hellbound;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.instances.DoorInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.ReflectionUtils;

/**
 * При смерти активирует зону, при спавне деактивирует ее
 *
 * @author pchayka
 */
public class MasterZelos extends Fighter {
    private static Zone _zone;
    private static final int[] doors = {19260054, 19260053};

    public MasterZelos(NpcInstance actor) {
        super(actor);
        _zone = ReflectionUtils.getZone("[tully1]");
    }

    @Override
    protected void onEvtSpawn() {
        setZoneInactive();
        super.onEvtSpawn();
        //Doors
        for (int i = 0; i < doors.length; i++) {
            DoorInstance door = ReflectionUtils.getDoor(doors[i]);
            door.closeMe();
        }
    }

    @Override
    protected void onEvtDead(Creature killer) {
        //Doors
        for (int i = 0; i < doors.length; i++) {
            DoorInstance door = ReflectionUtils.getDoor(doors[i]);
            door.openMe();
        }
        super.onEvtDead(killer);
        setZoneActive();
    }

    private void setZoneActive() {
        _zone.setActive(true);
    }

    private void setZoneInactive() {
        _zone.setActive(false);
    }
}
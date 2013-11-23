package l2p.gameserver.model.entity.events.objects;

import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;

/**
 * @author VISTALL
 * @date 16:32/14.07.2011
 */
public class SpawnSimpleObject implements SpawnableObject {
    /**
     *
     */
    private static final long serialVersionUID = 5816846246418756504L;
    private int _npcId;
    private Location _loc;

    private NpcInstance _npc;

    public SpawnSimpleObject(int npcId, Location loc) {
        _npcId = npcId;
        _loc = loc;
    }

    @Override
    public void spawnObject(GlobalEvent event) {
        _npc = NpcUtils.spawnSingle(_npcId, _loc, event.getReflection());
        _npc.addEvent(event);
    }

    @Override
    public void despawnObject(GlobalEvent event) {
        _npc.removeEvent(event);
        _npc.deleteMe();
    }

    @Override
    public void refreshObject(GlobalEvent event) {

    }
}

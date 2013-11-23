package npc.model.residences.castle;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2p.gameserver.model.entity.events.objects.CastleDamageZoneObject;
import l2p.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @date 8:58/17.03.2011
 */
public class CastleFlameTowerInstance extends SiegeToggleNpcInstance {
    private static final long serialVersionUID = 1L;

    private Set<String> _zoneList;

    public CastleFlameTowerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onDeathImpl(Creature killer) {
        CastleSiegeEvent event = getEvent(CastleSiegeEvent.class);
        if (event == null || !event.isInProgress())
            return;

        for (String s : _zoneList) {
            List<CastleDamageZoneObject> objects = event.getObjects(s);
            for (CastleDamageZoneObject zone : objects)
                zone.getZone().setActive(false);
        }
    }

    @Override
    public void setZoneList(Set<String> set) {
        _zoneList = set;
    }
}

package npc.model.residences.castle;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Spawner;
import l2p.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

import java.util.HashSet;
import java.util.Set;

public class CastleControlTowerInstance extends SiegeToggleNpcInstance {
    private static final long serialVersionUID = 1L;

    private Set<Spawner> _spawnList = new HashSet<Spawner>();

    public CastleControlTowerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onDeathImpl(Creature killer) {
        for (Spawner spawn : _spawnList)
            spawn.stopRespawn();
        _spawnList.clear();
    }

    @Override
    public void register(Spawner spawn) {
        _spawnList.add(spawn);
    }
}
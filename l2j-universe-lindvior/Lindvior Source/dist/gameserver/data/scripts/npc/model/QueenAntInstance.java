package npc.model;

import l2p.commons.util.Rnd;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.instances.BossInstance;
import l2p.gameserver.model.instances.MinionInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.PlaySound;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;

public class QueenAntInstance extends BossInstance {
    /**
     *
     */
    private static final long serialVersionUID = 1736191173721863916L;

    private static final int Queen_Ant_Larva = 29002;

    private List<SimpleSpawner> _spawns = new ArrayList<SimpleSpawner>();
    private NpcInstance Larva = null;

    public QueenAntInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    public NpcInstance getLarva() {
        if (Larva == null)
            Larva = SpawnNPC(Queen_Ant_Larva, new Location(-21600, 179482, -5846, Rnd.get(0, 0xFFFF)));
        return Larva;
    }

    @Override
    protected int getKilledInterval(MinionInstance minion) {
        return minion.getNpcId() == 29003 ? 10000 : 280000 + Rnd.get(40000);
    }

    @Override
    protected void onDeath(Creature killer) {
        broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, 0, getLoc()));
        Functions.deSpawnNPCs(_spawns);
        Larva = null;
        super.onDeath(killer);
    }

    @Override
    protected void onSpawn() {
        super.onSpawn();
        getLarva();
        broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS01_A", 1, 0, getLoc()));
    }

    private NpcInstance SpawnNPC(int npcId, Location loc) {
        NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
        if (template == null) {
            System.out.println("WARNING! template is null for npc: " + npcId);
            Thread.dumpStack();
            return null;
        }
        try {
            SimpleSpawner sp = new SimpleSpawner(template);
            sp.setLoc(loc);
            sp.setAmount(1);
            sp.setRespawnDelay(0);
            _spawns.add(sp);
            return sp.spawnOne();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
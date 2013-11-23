package ai.hellbound;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.utils.Location;

public class Tully extends Fighter {
    // 32371
    private static final Location[] locSD = {
            new Location(-12524, 273932, -9014, 49151),
            new Location(-10831, 273890, -9040, 81895),
            new Location(-10817, 273986, -9040, -16452),
            new Location(-13773, 275119, -9040, 8428),
            new Location(-11547, 271772, -9040, -19124),};

    //22392
    private static final Location[] locFTT = {
            new Location(-10832, 273808, -9040, 0),
            new Location(-10816, 274096, -9040, 14964),
            new Location(-13824, 275072, -9040, -24644),
            new Location(-11504, 271952, -9040, 9328),};

    private boolean s = false;
    private static NpcInstance removable_ghost = null;

    public Tully(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        for (int i = 0; i < locSD.length; i++)
            try {
                SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(32371));
                sp.setLoc(locSD[i]);
                sp.doSpawn(true);
                if (!s) {
                    Functions.npcShout(sp.getLastSpawn(), "Self Destruction mechanism launched: 10 minutes to explosion");
                    s = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        for (int i = 0; i < locFTT.length; i++)
            try {
                SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(22392));
                sp.setLoc(locFTT[i]);
                sp.doSpawn(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(32370));
            sp.setLoc(new Location(-11984, 272928, -9040, 23644));
            sp.doSpawn(true);
            removable_ghost = sp.getLastSpawn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ThreadPoolManager.getInstance().schedule(new UnspawnAndExplode(), 600 * 1000L); // 10 mins

        super.onEvtDead(killer);
    }

    private class UnspawnAndExplode extends RunnableImpl {
        public UnspawnAndExplode() {
        }

        @Override
        public void runImpl() {
            for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(32371, true))
                npc.deleteMe();

            for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(22392, true))
                npc.deleteMe();

            if (removable_ghost != null)
                removable_ghost.deleteMe();
        }
    }
}
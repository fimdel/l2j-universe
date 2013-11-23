/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.instancemanager.SpawnManager;
import l2p.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaltusKnight extends Reflection {
    private static final Logger _log = LoggerFactory.getLogger(Vullock.class);
    private static final int Antharas = 29223;
    private static final int Член_Экспедиции = 19133;
    private static final int Член_Экспедиции2 = 19136;
    private Location Antharasspawn = new Location(178664, 115096, -7733, 32767);
    private DeathListener _deathListener = new DeathListener();
    private CurrentHpListener _currentHpListener = new CurrentHpListener();

    @Override
    public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
        ThreadPoolManager.getInstance().schedule(new AntarasSpawn(this), 1);

    }

    private class DeathListener implements OnDeathListener {
        @Override
        public void onDeath(Creature self, Creature killer) {
            if (self.isNpc() && self.getNpcId() == Antharas) {
                for (Player p : getPlayers()) {
                    p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
                }
                startCollapseTimer(5 * 60 * 1000L);
            }
        }
    }

    public class CurrentHpListener implements OnCurrentHpDamageListener {
        @Override
        public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill) {
            if (actor.getNpcId() == Antharas) {
                if (actor == null || actor.isDead())
                    return;

                if (actor.getCurrentHp() <= 7850000) // С потолка.
                {
                    actor.removeListener(_currentHpListener);
                    SpawnManager.getInstance().spawn("derevo_drakona");
                }

                if (actor.getCurrentHp() <= 6850000) // С потолка.
                {
                    actor.removeListener(_currentHpListener);
                    SpawnManager.getInstance().spawn("derevo_drakona");
                }

                if (actor.getCurrentHp() <= 6550000) // С потолка.
                {
                    actor.removeListener(_currentHpListener);
                    SpawnManager.getInstance().spawn("derevo_drakona");
                }

                if (actor.getCurrentHp() <= 5850000) // С потолка.
                {
                    actor.removeListener(_currentHpListener);
                    SpawnManager.getInstance().spawn("derevo_drakona");
                }
            }
        }


    }

    public class AntarasSpawn extends RunnableImpl {
        Reflection _r;

        public AntarasSpawn(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
            Location Loc = Antharasspawn;
            NpcInstance AntharasStay = addSpawnWithoutRespawn(Antharas, Loc, 0);
            NpcInstance Кейл = addSpawnWithoutRespawn(19132, new Location(174072, 113944, -7733, 2555), 0);
            NpcInstance Фело = addSpawnWithoutRespawn(19128, new Location(177656, 115128, -7733, 0), 0);
            NpcInstance Эйтельд = addSpawnWithoutRespawn(19129, new Location(177560, 115032, -7733, 1297), 0);
            NpcInstance Фаулла = addSpawnWithoutRespawn(19130, new Location(177608, 115240, -7733, 0), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177512, 115328, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177512, 115528, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177512, 115728, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177512, 115928, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177384, 115328, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177384, 115528, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177384, 115728, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции, new Location(177384, 115928, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177512, 114488, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177512, 114688, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177512, 114288, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177512, 114088, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177384, 114688, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177384, 114488, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177384, 114288, -7733, 63813), 0);
            _r.addSpawnWithoutRespawn(Член_Экспедиции2, new Location(177384, 114088, -7733, 63813), 0);
            AntharasStay.addListener(_deathListener);
            AntharasStay.addListener(_currentHpListener);
        }
    }
}

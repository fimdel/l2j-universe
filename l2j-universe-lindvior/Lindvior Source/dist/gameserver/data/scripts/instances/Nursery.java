/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExSendUIEvent;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;

import java.util.concurrent.atomic.AtomicInteger;

public class Nursery extends Reflection {
    private int Creature1 = 23033;
    private int Creature2 = 23034;
    private int Creature3 = 23035;
    private int Creature4 = 23036;
    private int Creature5 = 23037;
    private int BeforeDelay = 1;
    private int BeforeEnd = 30 * 60 * 1000;
    public static int reward;
    NpcInstance tuy;
    public boolean _entryLocked = false;
    public boolean _startLaunched = false;
    public final AtomicInteger raidplayers = new AtomicInteger();
    private DeathListener _deathListener = new DeathListener();
    public final ZoneListener _epicZoneListener = new ZoneListener();

    @Override
    protected void onCreate() {
        super.onCreate();
        getZone("[SansillionInstanceInside]").addListener(_epicZoneListener);
        //   ThreadPoolManager.getInstance().schedule(new Start(), 7500);
        ThreadPoolManager.getInstance().schedule(new Stop(), BeforeEnd);
    }

    @Override
    protected void onCollapse() {
        super.onCollapse();
    }

    public void onBypassFeedback() {
        ThreadPoolManager.getInstance().schedule(new Start(), 7500);
        ThreadPoolManager.getInstance().schedule(new Stop(), BeforeEnd);
    }

    public class Start extends RunnableImpl {
        /*     Reflection _r;

public Start(Reflection r) {
   _r = r;
}         */
        @Override
        public void runImpl() {
            NpcInstance C1 = addSpawnWithRespawn(Creature5, new Location(-187080, 148600, -15337, 26634), 0, 30);
            C1.addListener(_deathListener);
            NpcInstance C2 = addSpawnWithRespawn(Creature3, new Location(-187432, 148456, -15336, 36818), 0, 30);
            C2.addListener(_deathListener);
            NpcInstance C3 = addSpawnWithRespawn(Creature5, new Location(-187176, 148056, -15337, 55191), 0, 30);
            C3.addListener(_deathListener);
            NpcInstance C4 = addSpawnWithRespawn(Creature3, new Location(-187384, 147560, -15337, 41319), 0, 30);
            C4.addListener(_deathListener);
            NpcInstance C5 = addSpawnWithRespawn(Creature1, new Location(-187032, 146968, -15337, 54933), 0, 30);
            C5.addListener(_deathListener);
            NpcInstance C6 = addSpawnWithRespawn(Creature2, new Location(-186824, 147176, -15337, 8191), 0, 30);
            C6.addListener(_deathListener);
            NpcInstance C7 = addSpawnWithRespawn(Creature3, new Location(-186856, 146456, -15336, 47722), 0, 30);
            C7.addListener(_deathListener);
            NpcInstance C8 = addSpawnWithRespawn(Creature3, new Location(-186392, 146568, -15337, 2470), 0, 30);
            C8.addListener(_deathListener);
            NpcInstance C9 = addSpawnWithRespawn(Creature1, new Location(-186104, 146888, -15337, 8740), 0, 30);
            C9.addListener(_deathListener);
            NpcInstance A1 = addSpawnWithRespawn(Creature4, new Location(-185720, 146120, -15336, 49803), 0, 30);
            A1.addListener(_deathListener);
            NpcInstance A2 = addSpawnWithRespawn(Creature4, new Location(-185560, 146376, -15330, 10557), 0, 30);
            A2.addListener(_deathListener);
            NpcInstance A3 = addSpawnWithRespawn(Creature3, new Location(-185576, 146712, -15337, 13828), 0, 30);
            A3.addListener(_deathListener);
            NpcInstance A4 = addSpawnWithRespawn(Creature4, new Location(-185096, 146664, -15332, 64381), 0, 30);
            A4.addListener(_deathListener);
            NpcInstance A5 = addSpawnWithRespawn(Creature3, new Location(-184856, 146520, -15336, 57956), 0, 30);
            A5.addListener(_deathListener);
            NpcInstance A6 = addSpawnWithRespawn(Creature2, new Location(-184840, 147032, -15335, 15640), 0, 30);
            A6.addListener(_deathListener);
            NpcInstance A7 = addSpawnWithRespawn(Creature1, new Location(-184872, 147400, -15325, 17288), 0, 30);
            A7.addListener(_deathListener);
            NpcInstance A8 = addSpawnWithRespawn(Creature2, new Location(-184328, 147048, -15336, 60384), 0, 30);
            A8.addListener(_deathListener);
            NpcInstance A9 = addSpawnWithRespawn(Creature1, new Location(-184248, 147448, -15335, 14325), 0, 30);
            A9.addListener(_deathListener);

            NpcInstance B1 = addSpawnWithRespawn(Creature4, new Location(-184584, 147656, -15337, 26986), 0, 30);
            B1.addListener(_deathListener);
            NpcInstance B2 = addSpawnWithRespawn(Creature4, new Location(-184408, 147784, -15337, 24575), 0, 30);
            B2.addListener(_deathListener);
            NpcInstance B3 = addSpawnWithRespawn(Creature1, new Location(-184120, 148040, -15336, 13028), 0, 30);
            B3.addListener(_deathListener);
            NpcInstance B4 = addSpawnWithRespawn(Creature2, new Location(-184536, 148184, -15337, 29292), 0, 30);
            B4.addListener(_deathListener);
            NpcInstance B5 = addSpawnWithRespawn(Creature2, new Location(-184264, 148456, -15336, 9137), 0, 30);
            B5.addListener(_deathListener);
            NpcInstance B6 = addSpawnWithRespawn(Creature1, new Location(-184872, 148600, -15337, 27714), 0, 30);
            B6.addListener(_deathListener);
            NpcInstance B7 = addSpawnWithRespawn(Creature4, new Location(-184584, 148744, -15337, 4836), 0, 30);
            B7.addListener(_deathListener);
            NpcInstance B8 = addSpawnWithRespawn(Creature5, new Location(-184488, 149080, -15335, 13481), 0, 30);
            B8.addListener(_deathListener);
            NpcInstance B9 = addSpawnWithRespawn(Creature4, new Location(-184696, 149128, -15336, 30402), 0, 30);
            B9.addListener(_deathListener);
            NpcInstance D1 = addSpawnWithRespawn(Creature3, new Location(-184984, 148904, -15337, 39662), 0, 30);
            D1.addListener(_deathListener);
            NpcInstance D2 = addSpawnWithRespawn(Creature1, new Location(-185080, 149272, -15337, 19045), 0, 30);
            D2.addListener(_deathListener);
            NpcInstance D3 = addSpawnWithRespawn(Creature2, new Location(-185048, 149576, -15335, 15290), 0, 30);
            D3.addListener(_deathListener);
            NpcInstance D4 = addSpawnWithRespawn(Creature5, new Location(-185368, 149560, -15336, 33289), 0, 30);
            D4.addListener(_deathListener);
            NpcInstance D5 = addSpawnWithRespawn(Creature4, new Location(-185608, 149288, -15337, 41611), 0, 30);
            D5.addListener(_deathListener);
            NpcInstance D6 = addSpawnWithRespawn(Creature3, new Location(-185448, 149160, -15337, 58498), 0, 30);
            D6.addListener(_deathListener);
            NpcInstance D7 = addSpawnWithRespawn(Creature2, new Location(-185816, 149080, -15337, 35000), 0, 30);
            D7.addListener(_deathListener);
            NpcInstance D8 = addSpawnWithRespawn(Creature1, new Location(-185928, 149480, -15336, 14325), 0, 30);
            D8.addListener(_deathListener);
            NpcInstance D9 = addSpawnWithRespawn(Creature2, new Location(-186232, 149656, -15335, 27294), 0, 30);
            D9.addListener(_deathListener);
            NpcInstance E1 = addSpawnWithRespawn(Creature5, new Location(-186536, 149400, -15336, 40068), 0, 30);
            E1.addListener(_deathListener);
            NpcInstance E2 = addSpawnWithRespawn(Creature3, new Location(-186440, 149032, -15337, 51813), 0, 30);
            E2.addListener(_deathListener);
            NpcInstance E3 = addSpawnWithRespawn(Creature3, new Location(-186792, 149160, -15337, 29130), 0, 30);
            E3.addListener(_deathListener);
            NpcInstance E4 = addSpawnWithRespawn(Creature5, new Location(-187000, 149320, -15335, 25928), 0, 30);
            E4.addListener(_deathListener);
            NpcInstance E5 = addSpawnWithRespawn(Creature4, new Location(-187128, 148904, -15336, 46038), 0, 30);
            E5.addListener(_deathListener);
            NpcInstance E6 = addSpawnWithRespawn(Creature2, new Location(-186872, 148792, -15337, 61312), 0, 30);
            E6.addListener(_deathListener);
            NpcInstance E7 = addSpawnWithRespawn(Creature1, new Location(-186536, 148808, -15337, 3355), 0, 30);
            E7.addListener(_deathListener);
            NpcInstance E8 = addSpawnWithRespawn(Creature1, new Location(-187016, 148328, -15337, 39714), 0, 30);
            E8.addListener(_deathListener);
            NpcInstance E9 = addSpawnWithRespawn(Creature3, new Location(-187096, 147384, -15335, 50306), 0, 30);
            E9.addListener(_deathListener);
            NpcInstance F1 = addSpawnWithRespawn(Creature3, new Location(-186664, 146856, -15337, 6469), 0, 30);
            F1.addListener(_deathListener);
            NpcInstance F2 = addSpawnWithRespawn(Creature2, new Location(-186520, 146328, -15336, 52054), 0, 30);
            F2.addListener(_deathListener);
            NpcInstance F3 = addSpawnWithRespawn(Creature1, new Location(-185992, 146264, -15336, 64277), 0, 30);
            F3.addListener(_deathListener);
            NpcInstance F4 = addSpawnWithRespawn(Creature3, new Location(-185864, 146696, -15335, 13379), 0, 30);
            F4.addListener(_deathListener);
            NpcInstance F5 = addSpawnWithRespawn(Creature5, new Location(-184312, 146792, -15335, 65119), 0, 30);
            F5.addListener(_deathListener);
            for (Player p : getPlayers()) {
                if (Rnd.chance(10)) {
                    Skill skill = SkillTable.getInstance().getInfo(14228, 1);
                    if (skill != null) {
                        skill.getEffects(p, p, true, true);
                        // player.sendPacket(new ExShowScreenMessage(NpcString.RECEIVED_REGENERATION_ENERGY, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false, -1, false));
                    }
                }
            }

            int count = reward + Rnd.get(15);
            for (Player p : getPlayers())
                p.sendPacket(new ExSendUIEvent(p, 0, 1, count, 0, "Количество собранных остатков"));
        }
    }

    private class DeathListener implements OnDeathListener {
        private NpcInstance exchange;

        @Override
        public void onDeath(Creature self, Creature killer) {
            if (self.isNpc() && self.getNpcId() == Creature1 || self.getNpcId() == Creature2 || self.getNpcId() == Creature3 || self.getNpcId() == Creature4 || self.getNpcId() == Creature5) {
                int count = reward + Rnd.get(15);
            }
        }
    }

    public class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (_entryLocked) {
                return;
            }

            Player player = cha.getPlayer();
            if ((player == null) || !cha.isPlayer()) {
                return;
            }

            if (checkstartCond(raidplayers.incrementAndGet())) {
                ThreadPoolManager.getInstance().schedule(new Start(), 1L);
                _startLaunched = true;
            }
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
            Player player = cha.getPlayer();
            if ((player == null) || !cha.isPlayer()) {
                return;
            }

            raidplayers.decrementAndGet();
        }
    }

    public boolean checkstartCond(int raidplayers) {
        return !((raidplayers < getInstancedZone().getMinLevel()) || _startLaunched);
    }

    public class Stop extends RunnableImpl {
        @Override
        public void runImpl() {
            for (Player p : getPlayers()) {
                p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
                p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
            }
            startCollapseTimer(5 * 60 * 1000L);
        }
    }


}

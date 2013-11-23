/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExSendUIEvent;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Location;

public class AltarShilen extends Reflection {
    private static final int Start = 32798;
    private static final int Altar = 33785;
    //1 этаж
    private static final int KeyStage1 = 23131;
    private static final int BossStage1 = 25857;
    private static final int DoorEnter1 = 25180001;
    //2 этаж
    private static final int Mob1Stage2 = 23126;
    private static final int Mob2Stage2 = 23127;
    private static final int Mob3Stage2 = 23128;
    private static final int Mob4Stage2 = 23129;
    private static final int Mob5Stage2 = 23130;
    private static final int KeyStage2 = 23138;
    private static final int BossStage2 = 25858;
    private static final int DoorEnter2 = 25180002;
    //3этаж
    private static final int Mob1Stage3 = 23132;
    private static final int Mob2Stage3 = 23133;
    private static final int Mob3Stage3 = 23134;
    private static final int Mob4Stage3 = 23135;
    private static final int Mob5Stage3 = 23136;
    private static final int Mob6Stage3 = 23137;
    private static final int BossStage3 = 25855;
    private static final int BossStage4 = 25856;

    private static final int DoorEnter3 = 25180003;
    private static final int DoorEnter4 = 25180004;
    private static final int DoorEnter5 = 25180005;
    private static final int DoorEnter6 = 25180006;
    private static final int DoorEnter7 = 25180007;
    //private static final int ExChanger = 33385; ToDo: Хз тот, или нет

    private Location NPC1Loc = new Location(179576, 13592, -7420);
    private Location NPC2Loc = new Location(179576, 13592, -9852);
    private Location KeyStage1Loc = new Location(179656, 18535, -8157);
    private Location KeyStage2Loc = new Location(179656, 13528, -8157);
    private Location Boss1Loc = new Location(178152, 14856, -8341, 16383);
    private Location Boss2Loc = new Location(178152, 14856, -10768, 16383);
    private Location End1Loc = new Location(178152, 13576, -8045);
    private Location End2Loc = new Location(178152, 13576, -10476);


    private DeathListener _deathListener = new DeathListener();
    private static final long BeforeDelay = 1 * 1000L;
    private long _savedTime;

    @Override
    public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
        ThreadPoolManager.getInstance().schedule(new CannonSpawn(this), BeforeDelay);
        ThreadPoolManager.getInstance().schedule(new CheckStageOne(), 15 * 60 * 1000L);
        NpcInstance Key1 = addSpawnWithoutRespawn(KeyStage1, new Location(179656, 18535, -8157), 0);
        NpcInstance Key2 = addSpawnWithoutRespawn(KeyStage2, new Location(179656, 18535, -10733), 0);
        Key1.addListener(_deathListener);
        Key2.addListener(_deathListener);
        player.setVar("Altar1", "true", -1);

    }

    @Override
    public void onPlayerExit(Player player) {
        super.onPlayerExit(player);
    }

    private class DeathListener implements OnDeathListener {
        @Override
        public void onDeath(Creature self, Creature killer) {
            if (self.isNpc() && self.getNpcId() == KeyStage1) {
                NpcInstance Boss1 = addSpawnWithoutRespawn(BossStage1, new Location(178152, 15016, -8341, 16383), 0);
                Boss1.addListener(_deathListener);
            } else if (self.isNpc() && self.getNpcId() == KeyStage2) {
                NpcInstance Boss2 = addSpawnWithoutRespawn(BossStage2, new Location(179432, 18376, -10589, 6711), 0);
                Boss2.addListener(_deathListener);
            } else if (self.isNpc() && self.getNpcId() == BossStage1) {
                for (Player p : getPlayers()) {
                    if (p.getVar("Altar1") != null) {
                        p.sendPacket(new ExShowScreenMessage("Алтарь закрыт", 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true));
                        p.setVar("Altar2", "true", -1);
                        p.unsetVar("Altar1");
                        p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
                    }
                }
            } else if (self.isNpc() && self.getNpcId() == BossStage2) {
                for (Player p : getPlayers()) {
                    if (p.getVar("Altar2") != null) {
                        p.sendPacket(new ExShowScreenMessage("Алтарь закрыт", 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true));
                        p.setVar("Altar3", "true", -1);
                        p.unsetVar("Altar2");
                        p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
                    }
                }
            }
        }
    }

    private class InstanceConclusion extends RunnableImpl {
        @Override
        public void runImpl() {
            startCollapseTimer(5 * 60 * 1000L);
            for (Player p : getPlayers())
                p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
        }
    }


    public class CannonSpawn extends RunnableImpl {
        Reflection _r;

        public CannonSpawn(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
            _r.addSpawnWithoutRespawn(Start, new Location(179576, 13592, -7420), 0);
            _r.addSpawnWithoutRespawn(Start, new Location(179576, 13592, -9852), 0);
            _r.addSpawnWithoutRespawn(Start, new Location(179576, 13592, -12796), 0);
            _r.addSpawnWithoutRespawn(Altar, new Location(178152, 13576, -8045), 0);
            _r.addSpawnWithoutRespawn(Altar, new Location(178152, 13576, -10476), 0);
            _r.addSpawnWithoutRespawn(Altar, new Location(178152, 13576, -13421), 0);
            //_r.addSpawnWithoutRespawn(BossStage1, new Location(178152, 15016, -8341), 0);
        }
    }

    public class CheckStageOne extends RunnableImpl {
        @Override
        public void runImpl() {
            for (Player p : getPlayers()) {
                if (p.getVar("Altar1") != null) {
                    startCollapseTimer(5 * 60 * 1000L);
                    p.unsetVar("Altar1");
                    p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
                }
            }
        }
    }

}

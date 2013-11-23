/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExSendUIEvent;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.network.serverpackets.components.SceneMovie;
import l2p.gameserver.tables.SkillTable;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class HarnakUndergroundRuins extends Reflection {
    private static final int DOOR1_ID = 16240100;
    private static final int DOOR2_ID = 16240102;
    private static final String ZONE_1 = "[harnak_underground_4pf_1]";
    private static final String ZONE_2 = "[harnak_underground_4pf_2]";
    private static final int RAKZAN_ID = 27440;
    private static final int DEFENSE_SKILL_ID = 14700;
    private static final String FIRST_ROOM_SECOND_GROUP = "2_group";
    private static final String SECOND_ROOM_FIRST_GROUP = "3_group_1";
    private static final String SECOND_ROOM_SOURCE_POWER = "3_group_source_power";
    private static final String THIRD_ROOM_GROUP = "4_group";
    private static final String THIRD_ROOM_SEALS = "4_group_seal";
    private static final String THIRD_ROOM_MINIONS = "4_group_minion";
    private static final String HERMUNKUS_GROUP = "hermunkus";

    private boolean introShowed = false;
    private volatile int first_room_mobs_count = 8;
    private int secondRoomGroup = 0;
    private int classId = -1;
    private ScheduledFuture<?> failTask;

    private int state;

    public HarnakUndergroundRuins(int state) {
        this.state = state;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
        if (state == 1) // Начало инстанса
        {
            if (!introShowed) {
                ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.AN_INTRUDER_INTERESTING), 2500);
                ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.PROVE_YOUR_WORTH), 5000);
                ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.PROVE_YOUR_WORTH), 7500);
                ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.ONLY_THOSE_STRONG_ENOUGH_SHALL_PROCEED), 8500);
                ThreadPoolManager.getInstance().schedule(new SpawnNpcTask(), 7500);
                for (ClassId classId1 : ClassId.VALUES) {
                    if (classId1.isOfLevel(ClassLevel.AWAKED) && classId1.childOf(player.getClassId())) {
                        classId = classId1.getId();
                        break;
                    }
                }
                introShowed = true;
            }
        } else if (state == 2) // Спаун только Гермункуса
        {
            spawnByGroup(HERMUNKUS_GROUP);
            getDoor(DOOR1_ID).openMe();
            getDoor(DOOR2_ID).openMe();
        }
    }

    public void decreaseFirstRoomMobsCount() {
        if (--first_room_mobs_count == 0) {
            getDoor(DOOR1_ID).openMe();
            spawnByGroup(FIRST_ROOM_SECOND_GROUP);
            Zone z = getZone(ZONE_1);
            if (z != null) {
                z.setActive(true);
                z.addListener(new ZoneListener(1));
            }
        }
    }

    public void increaseSecondRoomGroup() {
        secondRoomGroup++;
        if (secondRoomGroup == 2) {
            ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.I_MUST_GO_HELP_SOME_MORE), 100);
            Skill skill = SkillTable.getInstance().getInfo(DEFENSE_SKILL_ID, 1);
            for (Player player : getPlayers()) {
                skill.getEffects(player, player, false, false);
            }
            spawnByGroup(SECOND_ROOM_SOURCE_POWER);
        } else if (secondRoomGroup == 4) {
            getDoor(DOOR2_ID).openMe();
            Zone z = getZone(ZONE_2);
            if (z != null) {
                z.setActive(true);
                z.addListener(new ZoneListener(2));
            }
        }
    }

    public void startLastStage() {
        for (Player player : getPlayers()) {
            player.sendPacket(new ExSendUIEvent(player, 0, 0, 60, 0, NpcString.REMAINING_TIME));
            player.sendPacket(new ExShowScreenMessage(NpcString.NO_THE_SEAL_CONTROLS_HAVE_BEEN_EXPOSED_GUARDS_PROTECT_THE_SEAL_CONTROLS, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
        }
        ThreadPoolManager.getInstance().schedule(new SpawnNpcByPlayerClass(THIRD_ROOM_MINIONS), 1);
        failTask = ThreadPoolManager.getInstance().schedule(new FailTask(), 60000);
        spawnByGroup(THIRD_ROOM_SEALS);
    }

    public void successEndInstance() {
        if (failTask != null)
            failTask.cancel(true);
        despawnByGroup(THIRD_ROOM_SEALS);
        despawnByGroup(THIRD_ROOM_GROUP);
        despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId);
        for (Player p : getPlayers()) {
            p.startScenePlayer(SceneMovie.sc_awakening_boss_ending_a);
        }
        spawnByGroup(HERMUNKUS_GROUP);
    }

    private class ScreenMessageTask extends RunnableImpl {
        private NpcString msg;

        public ScreenMessageTask(NpcString msg) {
            this.msg = msg;
        }

        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.sendPacket(new ExShowScreenMessage(msg, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
            }
        }
    }

    private class SpawnNpcTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            spawnByGroup("1_group");
            List<NpcInstance> npcs = getAllByNpcId(RAKZAN_ID, true);
            if (!npcs.isEmpty())
                npcs.get(0).getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "SELECT_ME", "empty", "empty");
        }
    }

    private class SpawnNpcByPlayerClass extends RunnableImpl {
        private String group;

        public SpawnNpcByPlayerClass(String group) {
            this.group = group;
        }

        @Override
        public void runImpl() throws Exception {
            spawnByGroup(group + "_" + classId);
        }
    }

    private class ZoneListener implements OnZoneEnterLeaveListener {
        private int state;

        public ZoneListener(int state) {
            this.state = state;
        }

        @Override
        public void onZoneEnter(Zone zone, Creature actor) {
            if (actor.isPlayer()) {
                if (state == 1) {
                    ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.PROVE_YOUR_WORTH), 100);
                    ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.ARE_YOU_STRONG_OR_WEAK_OF_THE_LIGHT_OR_DARKNESS), 2600);
                    ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.ONLY_THOSE_OF_LIGHT_MAY_PASS_OTHERS_MUST_PROVE_THEIR_STRENGTH), 5100);
                    ThreadPoolManager.getInstance().schedule(new SpawnNpcByPlayerClass(SECOND_ROOM_FIRST_GROUP), 6900);
                } else if (state == 2) {
                    ThreadPoolManager.getInstance().schedule(new SpawnThirdRoom(), 28000);
                    for (Player p : getPlayers()) {
                        p.startScenePlayer(SceneMovie.sc_awakening_boss_opening);
                    }
                }
            }
            zone.setActive(false);
            zone.removeListener(this);
        }

        @Override
        public void onZoneLeave(Zone zone, Creature actor) {
        }
    }

    private class SpawnThirdRoom extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            spawnByGroup(THIRD_ROOM_GROUP);
        }
    }

    private class FailTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (NpcInstance npc : getNpcs()) {
                npc.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "FAIL_INSTANCE", "empty", "empty");
            }
            for (Player p : getPlayers()) {
                p.startScenePlayer(SceneMovie.sc_awakening_boss_ending_b);
            }
            ThreadPoolManager.getInstance().schedule(new EndTask(), 13500);
        }
    }

    private class EndTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (Player p : getPlayers()) {
                p.teleToLocation(getReturnLoc(), ReflectionManager.DEFAULT);
            }
        }
    }
}

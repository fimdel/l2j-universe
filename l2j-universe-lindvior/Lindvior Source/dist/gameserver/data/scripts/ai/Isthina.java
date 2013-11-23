/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.EventTrigger;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.PlaySound;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.ReflectionUtils;

import java.util.concurrent.ScheduledFuture;

/**
 * Описать и сделать скилы
 */
public class Isthina extends DefaultAI {
    // NPC ID
    final int ISTINA_LIGHT = 29195;
    final int ISTINA_HARD = 29196;
    // SKILLS
    private static final Skill BARRIER_OF_REFLECTION = SkillTable.getInstance().getInfo(14215, 1);
    private static final Skill FLOOD = SkillTable.getInstance().getInfo(14220, 1);
    private static final Skill MANIFESTATION_OF_AUTHORITY = SkillTable.getInstance().getInfo(14289, 1);
    private static final Skill ACID_ERUPTION1 = SkillTable.getInstance().getInfo(14221, 1);
    private static final Skill ACID_ERUPTION2 = SkillTable.getInstance().getInfo(14222, 1);
    private static final Skill ACID_ERUPTION3 = SkillTable.getInstance().getInfo(14223, 1);
    // ITEMS
    final int DEATH_BLOW = 14219;
    final int ISTINA_MARK = 14218;
    // RING zone (Trigger)
    final int RED_RING = 14220101;
    final int BLUE_RING = 14220102;
    final int GREEN_RING = 14220103;
    // RING LOCATIONS
    final Zone RED_RING_LOC;
    final Zone BLUE_RING_LOC;
    final Zone GREEN_RING_LOC;
    private ScheduledFuture<?> _effectCheckTask = null;
    private boolean _authorityLock = false;
    private boolean _hasFlood = false;
    private boolean _hasBarrier = false;
    private int _ring;
    private static Zone _zone;

    public Isthina(NpcInstance actor) {
        super(actor);

        _zone = ReflectionUtils.getZone("[Isthina_epic]");
        RED_RING_LOC = ReflectionUtils.getZone("[Isthina_red_zone]");
        BLUE_RING_LOC = ReflectionUtils.getZone("[Isthina_blue_zone]");
        GREEN_RING_LOC = ReflectionUtils.getZone("[Isthina_green_zone]");
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
    }

    @Override
    protected void thinkAttack() {
        NpcInstance npc = getActor();

        if (_effectCheckTask == null) {
            _effectCheckTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new EffectCheckTask(npc), 0, 2000);
        } else {
            ThreadPoolManager.getInstance().scheduleAtFixedRate(new EffectCheckTask(npc), 0, 2000);
        }

        double damage = (npc.getMaxHp() - npc.getCurrentHp());
        double lastPercentHp = (npc.getCurrentHp() + damage) / npc.getMaxHp();
        double currentPercentHp = npc.getCurrentHp() / npc.getMaxHp();

        if (lastPercentHp > 0.9D && currentPercentHp <= 0.9) {
            onPercentHpReached(npc, 90);
        } else if (lastPercentHp > 0.8 && currentPercentHp <= 0.8) {
            onPercentHpReached(npc, 80);
        } else if ((lastPercentHp > 0.7) && (currentPercentHp <= 0.7)) {
            onPercentHpReached(npc, 70);
        } else if ((lastPercentHp > 0.6) && (currentPercentHp <= 0.6)) {
            onPercentHpReached(npc, 60);
        } else if ((lastPercentHp > 0.5) && (currentPercentHp <= 0.5)) {
            onPercentHpReached(npc, 50);
        } else if ((lastPercentHp > 0.45) && (currentPercentHp <= 0.45)) {
            onPercentHpReached(npc, 45);
        } else if ((lastPercentHp > 0.4) && (currentPercentHp <= 0.4)) {
            onPercentHpReached(npc, 40);
        } else if ((lastPercentHp > 0.35) && (currentPercentHp <= 0.35)) {
            onPercentHpReached(npc, 35);
        } else if ((lastPercentHp > 0.3) && (currentPercentHp <= 0.3)) {
            onPercentHpReached(npc, 30);
        } else if ((lastPercentHp > 0.25) && (currentPercentHp <= 0.25)) {
            onPercentHpReached(npc, 25);
        } else if ((lastPercentHp > 0.2) && (currentPercentHp <= 0.2)) {
            onPercentHpReached(npc, 20);
        } else if ((lastPercentHp > 0.15) && (currentPercentHp <= 0.15)) {
            onPercentHpReached(npc, 15);
        } else if ((lastPercentHp > 0.1) && (currentPercentHp <= 0.1)) {
            onPercentHpReached(npc, 10);
        } else if ((lastPercentHp > 0.05) && (currentPercentHp <= 0.05)) {
            onPercentHpReached(npc, 5);
        } else if ((lastPercentHp > 0.02) && (currentPercentHp <= 0.02)) {
            onPercentHpReached(npc, 2);
        } else if ((lastPercentHp > 0.01) && (currentPercentHp <= 0.01)) {
            onPercentHpReached(npc, 1);
        } else {
            double seed = Rnd.get();

            if ((seed < 0.005) && (!_authorityLock)) {
                authorityField(npc);
            }
        }
    }

    public void onPercentHpReached(NpcInstance npc, int percent) {
        ThreadPoolManager.getInstance().schedule(new onPercentHpReached(npc, percent), 60000L);
    }

    private void authorityField(final NpcInstance npc) {
        _authorityLock = true;

        double seed = Rnd.get();

        _ring = (seed >= 0.33) && (seed < 0.66) ? 1 : (seed < 0.33) ? 0 : 2;

        if (seed < 0.33) {
            npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_GREEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
        } else {
            if ((seed >= 0.33) && (seed < 0.66)) {
                npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_BLUE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
            } else {
                npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_RED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
            }
        }

        npc.broadcastPacket(new PlaySound("istina.istina_voice_01"));
        ThreadPoolManager.getInstance().schedule(new runAuthorityRing(npc), 10000L);
    }

    private class EffectCheckTask extends RunnableImpl {
        private NpcInstance _npc;

        public EffectCheckTask(NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void runImpl() {
            if (_npc == null) {
                if (_effectCheckTask != null) {
                    _effectCheckTask.cancel(false);
                }
            }

            boolean hasBarrier = false;
            boolean hasFlood = false;

            if (_npc != null) {
                if (_npc.getEffectList().getEffectsBySkillId(BARRIER_OF_REFLECTION.getId()) != null) {
                    hasBarrier = true;

                    if (hasFlood) {
                        return;
                    }
                } else {
                    if (_npc.getEffectList().getEffectsBySkillId(FLOOD.getId()) != null) {
                        hasFlood = true;
                    }

                    if (hasBarrier) {
                        return;
                    }
                }
            }

            if ((_hasBarrier) && (!hasBarrier)) {
                if (_npc != null) {
                    _npc.setNpcState(2);
                }
                // TODO[M] - Use skill
                if (_npc != null) {
                    _npc.setNpcState(0);
                }
                _npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SPREADS_PROTECTIVE_SHEET, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
            } else if ((!_hasBarrier) && (hasBarrier)) {
                _npc.setNpcState(1);
            }

            if ((_hasFlood) && (hasFlood)) {
                _npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_GETS_FURIOUS_AND_RECKLESSLY_CRAZY, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
            } else if ((_hasFlood) && (!hasFlood)) {
                _npc.broadcastPacket(new ExShowScreenMessage(NpcString.BERSERKER_OF_ISTINA_HAS_BEEN_DISABLED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
            }
        }
    }

    private class onPercentHpReached extends RunnableImpl {
        private int _percent = 0;
        private NpcInstance _npc;

        onPercentHpReached(NpcInstance npc, int percent) {
            _percent = percent;
            _npc = npc;
        }

        @Override
        public void runImpl() {
            Skill skillToCast;
            final NpcInstance npcs = _npc;

            if (Rnd.get() <= 0.4) {
                skillToCast = ACID_ERUPTION1;
            } else if (Rnd.get() <= 0.5) {
                skillToCast = ACID_ERUPTION2;
            } else {
                skillToCast = ACID_ERUPTION3;
            }

            _npc.doCast(skillToCast, _npc, false);

            if (((_percent >= 50) && (_percent % 10 == 0)) || ((_percent < 50) && (_percent % 5 == 0))) {
                _npc.doCast(FLOOD, _npc, false);
            }

            npcs.doCast(BARRIER_OF_REFLECTION, npcs, false);
        }
    }

    private class runAuthorityRing extends RunnableImpl {
        private NpcInstance _npc;

        runAuthorityRing(NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void runImpl() {
            NpcInstance npc = _npc;
            Zone zones;

            if (_ring != 0) {
                npc.broadcastPacket(new EventTrigger(GREEN_RING, true));
                npc.broadcastPacket(new EventTrigger(RED_RING, true));

                zones = BLUE_RING_LOC;
            } else if (_ring != 1) {
                npc.broadcastPacket(new EventTrigger(BLUE_RING, true));
                npc.broadcastPacket(new EventTrigger(GREEN_RING, true));

                zones = RED_RING_LOC;
            } else {
                npc.broadcastPacket(new EventTrigger(RED_RING, true));
                npc.broadcastPacket(new EventTrigger(BLUE_RING, true));

                zones = GREEN_RING_LOC;
            }

            for (Player player : _zone.getInsidePlayers()) {
                if (!player.isInZone(zones)) {
                    MANIFESTATION_OF_AUTHORITY.getEffects(npc, player, false, false);
                }
            }
        }
    }
}
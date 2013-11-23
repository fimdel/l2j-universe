/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.components;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExStartScenePlayer;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author VISTALL
 * @date 12:51/29.12.2010
 */
public enum SceneMovie implements IStaticPacket {
    LINDVIOR_SPAWN(1, 45500, false),

    // Echmus
    ECHMUS_OPENING(2, 62000, false),
    ECHMUS_SUCCESS(3, 18000, false),
    ECHMUS_FAIL(4, 17000, false),

    // Tiat
    TIAT_OPENING(5, 54200, false),
    TIAT_SUCCESS(6, 26100, false),
    TIAT_FAIL(7, 24800, false),

    // Seven Signs Quests
    SSQ_SERIES_OF_DOUBT(8, 26000, false),
    SSQ_DYING_MESSAGE(9, 27000, false),
    SSQ_MAMMONS_CONTRACT(10, 98000, false),
    SSQ_SECRET_RITUAL_PRIEST(11, 30000, false),
    SSQ_SEAL_EMPEROR_1(12, 18000, false),
    SSQ_SEAL_EMPEROR_2(13, 26000, false),
    SSQ_EMBRYO(14, 28000, false),

    // Freya
    FREYA_OPENING(15, 53500, false),
    FREYA_PHASE_CHANGE_A(16, 21100, false),
    FREYA_PHASE_CHANGE_B(17, 21500, false),
    KEGOR_INTRUSION(18, 27000, false),
    FREYA_ENDING_A(19, 16000, false),
    FREYA_ENDING_B(20, 56000, false),
    FREYA_FORCED_DEFEAT(21, 21000, false),
    FREYA_DEFEAT(22, 20500, false),
    ICE_HEAVY_KNIGHT_SPAWN(23, 7000, false),

    // High Five Seven Signs Quests
    SSQ2_HOLY_BURIAL_GROUND_OPENING(24, 23000, false),
    SSQ2_HOLY_BURIAL_GROUND_CLOSING(25, 22000, false),
    SSQ2_SOLINA_TOMB_OPENING(26, 25000, false),
    SSQ2_SOLINA_TOMB_CLOSING(27, 15000, false),
    SSQ2_ELYSS_NARRATION(28, 59000, false),
    SSQ2_BOSS_OPENING(29, 60000, false),
    SSQ2_BOSS_CLOSING(30, 60000, false),

    sc_istina_opening(31, 36700, true),
    sc_istina_ending_a(32, 23300, false),
    sc_istina_ending_b(33, 22200, false),
    sc_istina_bridge(34, 7200, false),

    sc_octabis_opening(35, 26600, true),
    sc_octabis_phasech_a(36, 10000, false),
    sc_octabis_phasech_b(37, 14000, false),
    sc_octabis_ending(38, 38000, false),

    sc_gd1_prologue(42, 64000, true),

    sc_talking_island_boss_opening(43, 47430, true),
    sc_talking_island_boss_ending(44, 32040, true),

    sc_awakening_opening(45, 27000, true),
    sc_awakening_boss_opening(46, 29950, true),
    sc_awakening_boss_ending_a(47, 25050, true),
    sc_awakening_boss_ending_b(48, 13100, true),

    sc_earthworm_ending(49, 32600, false),

    sc_spacia_opening(50, 38600, false),
    sc_spacia_a(51, 29500, false),
    sc_spacia_b(52, 45000, false),
    sc_spacia_c(53, 36000, false),
    sc_spacia_ending(54, 23000, false),

    sc_awakening_view(55, 34000, true),
    sc_awakening_opening_c(56, 28500, true),
    sc_awakening_opening_d(57, 20000, true),
    sc_awakening_opening_e(58, 24000, true),
    sc_awakening_opening_f(59, 38100, true),

    sc_tauti_opening_b(69, 15000, false),
    sc_tauti_opening(70, 15000, false),
    sc_tauti_phase(71, 15000, false),
    sc_tauti_ending(72, 15000, false),
    sc_soulisland_quest(73, 25000, false),
    sc_metucellar_opening(74, 25000, false),
    sc_sub_quest(75, 25000, false),
    sc_noble_opening(99, 10000, false),
    sc_noble_ending(100, 10000, false),

    si_illusion_01_que(101, 29200, true),
    si_illusion_02_que(102, 27150, true),
    si_illusion_03_que(103, 16100, true),

    si_arkan_enter(104, 30300, true),

    si_barlog_opening(105, 19300, false),
    si_barlog_story(106, 67500, false),

    si_illusion_04_que(107, 10100, true),
    si_illusion_05_que(108, 10100, true),

    // Airship
    LANDING_KSERTH_LEFT(1000, 10000, false),
    LANDING_KSERTH_RIGHT(1001, 10000, false),
    LANDING_INFINITY(1002, 10000, false),
    LANDING_DESTRUCTION(1003, 10000, false),
    LANDING_ANNIHILATION(1004, 15000, false);

    private final int _id;
    private final int _duration;
    private final boolean _cancellable;
    private final L2GameServerPacket _static;

    SceneMovie(int id, int duration, boolean cancellable) {
        _id = id;
        _duration = duration;
        _cancellable = cancellable;
        _static = new ExStartScenePlayer(this);
    }

    public int getId() {
        return _id;
    }

    public int getDuration() {
        return _duration;
    }

    public boolean isCancellable() {
        return _cancellable;
    }

    public static SceneMovie getMovie(int id) {
        for (SceneMovie movie : values()) {
            if (movie.getId() == id)
                return movie;
        }
        return null;
    }

    @Override
    public L2GameServerPacket packet(Player player) {
        return _static;
    }
}

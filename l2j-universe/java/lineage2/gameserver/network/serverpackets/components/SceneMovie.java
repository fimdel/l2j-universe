package lineage2.gameserver.network.serverpackets.components;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author VISTALL
 * @date 12:51/29.12.2010
 */
public enum SceneMovie implements IStaticPacket
{
	LINDVIOR_SPAWN(1, 45500),

	// Echmus
	ECHMUS_OPENING(2, 62000),
	ECHMUS_SUCCESS(3, 18000),
	ECHMUS_FAIL(4, 17000),

	// Tiat
	TIAT_OPENING(5, 54200),
	TIAT_SUCCESS(6, 26100),
	TIAT_FAIL(7, 24800),

	// Seven Signs Quests
	SSQ_SERIES_OF_DOUBT(8, 26000),
	SSQ_DYING_MESSAGE(9, 27000),
	SSQ_MAMMONS_CONTRACT(10, 98000),
	SSQ_SECRET_RITUAL_PRIEST(11, 30000),
	SSQ_SEAL_EMPEROR_1(12, 18000),
	SSQ_SEAL_EMPEROR_2(13, 26000),
	SSQ_EMBRYO(14, 28000),

	// Freya
	FREYA_OPENING(15, 53500),
	FREYA_PHASE_CHANGE_A(16, 21100),
	FREYA_PHASE_CHANGE_B(17, 21500),
	KEGOR_INTRUSION(18, 27000),
	FREYA_ENDING_A(19, 16000),
	FREYA_ENDING_B(20, 56000),
	FREYA_FORCED_DEFEAT(21, 21000),
	FREYA_DEFEAT(22, 20500),
	ICE_HEAVY_KNIGHT_SPAWN(23, 7000),

	// High Five Seven Signs Quests
	SSQ2_HOLY_BURIAL_GROUND_OPENING(24, 23000),
	SSQ2_HOLY_BURIAL_GROUND_CLOSING(25, 22000),
	SSQ2_SOLINA_TOMB_OPENING(26, 25000),
	SSQ2_SOLINA_TOMB_CLOSING(27, 15000),
	SSQ2_ELYSS_NARRATION(28, 59000),
	SSQ2_BOSS_OPENING(29, 60000),
	SSQ2_BOSS_CLOSING(30, 60000),

	sc_istina_opening(31, 36700),
	sc_istina_ending_a(32, 23300),
	sc_istina_ending_b(33, 22200),
	sc_istina_bridge(34, 7200),

	sc_octabis_opening(35, 26600),
	sc_octabis_phasech_a(36, 10000),
	sc_octabis_phasech_b(37, 14000),
	sc_octabis_ending(38, 38000),

	sc_gd1_prologue(42, 64000),

	sc_talking_island_boss_opening(43, 47430),
	sc_talking_island_boss_ending(44, 32040),

	sc_awakening_opening(45, 27000),
	sc_awakening_boss_opening(46, 29950),
	sc_awakening_boss_ending_a(47, 25050),
	sc_awakening_boss_ending_b(48, 13100),

	sc_earthworm_ending(49, 32600),

	sc_spacia_opening(50, 38600),
	sc_spacia_a(51, 29500),
	sc_spacia_b(52, 45000),
	sc_spacia_c(53, 36000),
	sc_spacia_ending(54, 23000),

	sc_awakening_view(55, 34000),
	sc_awakening_opening_c(56, 28500),
	sc_awakening_opening_d(57, 20000),
	sc_awakening_opening_e(58, 24000),
	sc_awakening_opening_f(59, 38100),

	sc_tauti_opening_b(69, 15000),
	sc_tauti_opening(70, 15000),
	sc_tauti_phase(71, 15000),
	sc_tauti_ending(72, 15000),
	sc_noble_opening(99, 10000),
	sc_noble_ending(100, 10000),

	si_illusion_01_que(101, 29200),
	si_illusion_02_que(102, 27150),
	si_illusion_03_que(103, 16100),

	si_arkan_enter(104, 30300),

	si_barlog_opening(105, 19300),
	si_barlog_story(106, 67500),

	si_illusion_04_que(107, 10100),
	si_illusion_05_que(108, 10100),

	// Airship
	LANDING_KSERTH_LEFT(1000, 10000),
	LANDING_KSERTH_RIGHT(1001, 10000),
	LANDING_INFINITY(1002, 10000),
	LANDING_DESTRUCTION(1003, 10000),
	LANDING_ANNIHILATION(1004, 15000);

	private final int _id;
	private final int _duration;
	private final L2GameServerPacket _static;

	SceneMovie(int id, int duration)
	{
		_id = id;
		_duration = duration;
		_static = new ExStartScenePlayer(this);
	}

	public int getId()
	{
		return _id;
	}

	public int getDuration()
	{
		return _duration;
	}

	@Override
	public L2GameServerPacket packet(Player player)
	{
		return _static;
	}
}

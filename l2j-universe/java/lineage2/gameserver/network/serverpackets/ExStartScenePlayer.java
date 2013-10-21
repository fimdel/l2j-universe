package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.network.serverpackets.components.SceneMovie;

public class ExStartScenePlayer extends L2GameServerPacket
{
	private final int _sceneId;

	public static final int SCENE_LINDVIOR = 1;
	public static final int SCENE_ECHMUS_OPENING = 2;
	public static final int SCENE_ECHMUS_SUCCESS = 3;
	public static final int SCENE_ECHMUS_FAIL = 4;
	public static final int SCENE_TIAT_OPENING = 5;
	public static final int SCENE_TIAT_SUCCESS = 6;
	public static final int SCENE_TIAT_FAIL = 7;
	public static final int SCENE_SSQ_SUSPICIOUS_DEATH = 8;
	public static final int SCENE_SSQ_DYING_MASSAGE = 9;
	public static final int SCENE_SSQ_CONTRACT_OF_MAMMON = 10;
	public static final int SCENE_SSQ_RITUAL_OF_PRIEST = 11;
	public static final int SCENE_SSQ_SEALING_EMPEROR_1ST = 12;
	public static final int SCENE_SSQ_SEALING_EMPEROR_2ND = 13;
	public static final int SCENE_SSQ_EMBRYO = 14;
	public static final int SCENE_BOSS_FREYA_OPENING = 15;
	public static final int SCENE_BOSS_FREYA_PHASE_A = 16;
	public static final int SCENE_BOSS_FREYA_PHASE_B = 17;
	public static final int SCENE_BOSS_KEGOR_INTRUSION = 18;
	public static final int SCENE_BOSS_FREYA_ENDING_A = 19;
	public static final int SCENE_BOSS_FREYA_ENDING_B = 20;
	public static final int SCENE_BOSS_FREYA_FORCED_DEFEAT = 21;
	public static final int SCENE_BOSS_FREYA_DEFEAT = 22;
	public static final int SCENE_ICE_HEAVYKNIGHT_SPAWN = 23;
	public static final int SCENE_SSQ2_HOLY_BURIAL_GROUND_OPENING = 24;
	public static final int SCENE_SSQ2_HOLY_BURIAL_GROUND_CLOSING = 25;
	public static final int SCENE_SSQ2_SOLINA_TOMB_OPENING = 26;
	public static final int SCENE_SSQ2_SOLINA_TOMB_CLOSING = 27;
	public static final int SCENE_SSQ2_ELYSS_NARRATION = 28;
	public static final int SCENE_SSQ2_BOSS_OPENING = 29;
	public static final int SCENE_SSQ2_BOSS_CLOSING = 30;
	public static final int SCENE_BOSS_ISTHINA_OPENING = 31; // 36700.00000000
	public static final int SCENE_BOSS_ISTHINA_ENDING_A = 32; // 23300.00000000
	public static final int SCENE_BOSS_ISTHINA_ENDING_B = 33; // 22200.00000000
	public static final int SCENE_BOSS_ISTHINA_BRIDGE = 34; // 7200.00000000
	public static final int SCENE_BOSS_OCTABIS_OPENING = 35; // 26600.00000000
	public static final int SCENE_BOSS_OCTABIS_PHASECH_A = 36; // 10000.00000000
	public static final int SCENE_BOSS_OCTABIS_PHASECH_B = 37; // 14000.00000000
	public static final int SCENE_BOSS_OCTABIS_ENDING = 38; // 38000.00000000
	public static final int SCENE_TALKING_ISLAND_BOSS_OPENING = 43; //Квест на первую профу старт
	public static final int SCENE_TALKING_ISLAND_BOSS_ENDING = 44; //Квест на первую профу финиш
	public static final int SCENE_AWAKENING_OPENING = 45;
	public static final int SCENE_AWAKENING_BOSS_OPENING = 46;
	public static final int SCENE_AWAKENING_BOSS_ENDING_A = 47; // 25050
	public static final int SCENE_AWAKENING_BOSS_ENDING_B = 48;
	public static final int SCENE_SC_EARTHWORM_ENDING = 49; // 32600.00000000
	public static final int SCENE_SC_SPACIA_OPENING = 50; // 38600.00000000
	public static final int SCENE_SC_SPACIA_A = 51; // 29500.00000000
	public static final int SCENE_SC_SPACIA_B = 52; // 45000.00000000
	public static final int SCENE_SC_SPACIA_C = 53; // 36000.00000000
	public static final int SCENE_SC_SPACIA_ENDING = 54; // 23000.00000000
	public static final int SCENE_AWAKENING_VIEW = 55;
	public static final int SCENE_AWAKENING_OPENING_C = 56;
	public static final int SCENE_AWAKENING_OPENING_D = 57; // 20000
	public static final int SCENE_AWAKENING_OPENING_E = 58;
	public static final int SCENE_AWAKENING_OPENING_F = 59;
	public static final int SCENE_SC_AWAKENING_OPENING_F = 59; // 38100.00000000
	public static final int SCENE_SC_TAUTI_OPENING_B = 69; // 15000.00000000
	public static final int SCENE_SC_TAUTI_OPENING = 70; // 15000.00000000
	public static final int SCENE_SC_TAUTI_PHASE = 71; // 15000.00000000
	public static final int SCENE_SC_TAUTI_ENDING = 72; // 15000.00000000
	public static final int SCENE_SC_NOBLE_OPENING = 99; // 10000.00000000
	public static final int SCENE_SC_NOBLE_ENDING = 100; // 10000.00000000
	public static final int SCENE_SI_ILLUSION_01_QUE = 101; // 29200.00000000 TI
	public static final int SCENE_SI_ILLUSION_02_QUE = 102; // 27150.00000000 TI 
	public static final int SCENE_SI_ILLUSION_03_QUE = 103; // 16100.00000000 Исагира
	public static final int SCENE_SI_ARKAN_ENTER = 104; // 30300.00000000
	public static final int SCENE_SI_BARLOG_OPENING = 105; // 19300.00000000
	public static final int SCENE_SI_BARLOG_STORY = 106; // 67500.00000000
	public static final int SCENE_SI_ILLUSION_04_QUE = 107; // 10100.00000000 НЕ понятно
	public static final int SCENE_SI_ILLUSION_05_QUE = 108; // 10100.00000000 НЕ понятно (камера падает вниз откуда то)
	public static final int SCENE_SC_BLOODVEIN_OPENING = 109; // 13000.00000000
	public static final int SCENE_LAND_KSERTH_A = 1000; // 10000.00000000
	public static final int SCENE_LAND_KSERTH_B = 1001; // 10000.00000000
	public static final int SCENE_LAND_UNDEAD_A = 1002; // 10000.00000000
	public static final int SCENE_LAND_DISTRUCTION_A = 1003; // 10000.00000000
	public static final int SCENE_LAND_ANNIHILATION_A = 1004; // 15000.00000000
	public static final int SCENE_BR_G_CARTIA_1_SIN = 2001; // 17800.00000000
	public static final int SCENE_BR_G_CARTIA_2_SIN = 2002; // 15800.00000000


	public ExStartScenePlayer(int sceneId)
	{
		_sceneId = sceneId;
	}

	public ExStartScenePlayer(SceneMovie scene)
	{
		_sceneId = scene.getId();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x9A);
		writeD(_sceneId);
	}
}
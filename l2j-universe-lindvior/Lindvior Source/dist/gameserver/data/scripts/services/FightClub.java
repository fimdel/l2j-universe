/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package services;

import l2p.gameserver.Config;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.11.12
 * Time: 14:43
 */
public class FightClub extends Functions implements ScriptFile {
    private static final ArrayList<SimpleSpawner> _spawns_fight_club_manager = new ArrayList<SimpleSpawner>();

    private static int FIGHT_CLUB_MANAGER = 36600;

    private void spawnFightClub() {
        final int FIGHT_CLUB_MANAGER_SPAWN[][] = {

                {82042, 149711, -3424, 58312}, // Giran
                {146408, 28536, -2255, 49151}, // Aden 1
                {148504, 28536, -2255, 49151}, // Aden 2
                {145800, -57112, -2966, 49151}, //Goddard 1
                {150120, -56504, -2966, 4836}, //Goddard 2
                {43656, -46792, -784, 17301}, //Rune
                {19448, 145048, -3094, 49151}, //Dion 1
                {17832, 144312, -3037, 46596}, //Dion 2
                {82888, 55304, -1480, 0}, //Oren 1
                {80104, 53608, -1520, 49151}, //Oren 2
                {-15064, 124296, -3104, 49151}, //Gludio 1
                {-12184, 122552, -3086, 32767}, //Gludio 2
                {-82648, 149887, -3115, 32767}, //Gludin 1
                {-81800, 155368, -3163, 58312}, //Gludin 2
                {89272, -141592, -1525, 20115}, //Shuttgart 1
                {87672, -140776, -1525, 26898}, //Shuttgart 2
                {115496, 218728, -3648, 31934}, //Heine 1
                {107384, 217704, -3661, 16500}, //Heine 2
                {116920, 75480, -2720, 21220}, //Hunter's Village
        };

        SpawnNPCs(FIGHT_CLUB_MANAGER, FIGHT_CLUB_MANAGER_SPAWN, _spawns_fight_club_manager);
    }

    @Override
    public void onLoad() {
        if (Config.FIGHT_CLUB_ENABLED)
            spawnFightClub();
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }
}

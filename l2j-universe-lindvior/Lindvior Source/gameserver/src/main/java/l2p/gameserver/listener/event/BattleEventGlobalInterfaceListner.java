/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.listener.event;

import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.actor.player.OnPlayerExitListener;
import l2p.gameserver.listener.actor.player.OnTeleportListener;
import l2p.gameserver.model.Player;
import l2p.gameserver.scripts.ScriptFile;

import java.util.List;

public interface BattleEventGlobalInterfaceListner extends OnPlayerExitListener, OnDeathListener, ScriptFile, OnTeleportListener {
    void isActive();

    void activateEvent();

    void deactivateEvent();

    void isRunned();

    void getMinLevelForCategory();

    void getMaxLevelForCategory();

    void getCategory();

    void start(String[] var);

    void sayToAll(String address, String[] replacements);

    void question();

    void announce();

    void addPlayer();

    void checkPlayer(Player player, boolean first);

    void autoContinue();

    void go();

    void endBattle();

    void end();

    void prepare();

    void saveBackCoords();

    void scheduleEventStart();

    void teleportPlayersToColiseum();

    void paralyzePlayers();

    void upParalyzePlayers();

    void ressurectPlayers();

    void cleanPlayers();

    void healPlayers();

    void removeAura();

    void loosePlayer(Player player);

    void removePlayer(Player player);

    void clearArena();

    void openColiseumDoors();

    void closeColiseumDoors();

    void mageBuff(Player player);

    void fighterBuff(Player player);

    void buffPlayers();

    void buffPlayer(Player player);

    List<Player> getPlayers(List<Long> list);

    void teleportPlayersToSavedCoords();
}

package zones;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 25.07.12
 * Time: 3:51
 */
public class TeleToFarmZone extends Functions implements ScriptFile {

    // Items
    private static final int ITEM_ZONE1 = 57;    //adena
    private static final int ITEM_ZONE2 = 57;  //adena
    private static final int ITEM_ZONE3 = 57;  //adena
    private static final int ITEM_ZONE4 = 10654;  // Указ Стража - Ивент   это выбивается в 3 зоне

    private static final Location TELEPORT_ZONE1 = new Location(213016, 50424, -8432);
    private static final Location TELEPORT_ZONE2 = new Location(213016, 50424, -14665);
    private static final Location TELEPORT_ZONE3 = new Location(214312, 112696, -12825);
    private static final Location TELEPORT_ZONE4 = new Location(12780, -248480, -9575);

    public void teleToZone1() {
        Player player = getSelf();
        NpcInstance npc = getNpc();
        if (player.getLevel() >= 85)
            if (Functions.getItemCount(player, ITEM_ZONE1) > 15000000) {
                Functions.removeItem(player, ITEM_ZONE1, 15000000);
                player.teleToLocation(TELEPORT_ZONE1);
            } else
                show("<html><body>Телепортер фарм зоны<br>У Вас нет интересующих меня предметов(*Адена-150000000шт*), возвращайтесь когда они будут.</body></html>", player, npc);
        else
            show("<html><body>Телепортер фарм зоны<br>Вы слишком слабы. получите 85лвл и я Вас пропущу.</body></html>", player, npc);
    }

    public void teleToZone2() {
        Player player = getSelf();
        NpcInstance npc = getNpc();
        if (player.getLevel() >= 89)
            if (Functions.getItemCount(player, ITEM_ZONE2) >= 15000000) {
                Functions.removeItem(player, ITEM_ZONE2, 15000000);
                player.teleToLocation(TELEPORT_ZONE2);
            } else
                show("<html><body>Телепортер фарм зоны<br>У Вас нет интересующих меня предметов(*Синяя Субстанция-10шт*), возвращайтесь когда они будут.</body></html>", player, npc);
        else
            show("<html><body>Телепортер фарм зоны 1 лвла:<br>Вы слишком слабы. получите 89лвл и я Вас пропущу.</body></html>", player, npc);
    }

    public void teleToZone3() {
        Player player = getSelf();
        NpcInstance npc = getNpc();
        if (player.getLevel() >= 93)
            if (Functions.getItemCount(player, ITEM_ZONE3) >= 15000000) {
                Functions.removeItem(player, ITEM_ZONE3, 15000000);
                player.teleToLocation(TELEPORT_ZONE3);
            } else
                show("<html><body>Телепортер фарм зоны<br>У Вас нет интересующих меня предметов(*Красная Субстанция-15шт*), возвращайтесь когда они будут.</body></html>", player, npc);
        else
            show("<html><body>Телепортер фарм зоны 2 лвла:<br>Вы слишком слабы. получите 93лвл и я Вас пропущу.</body></html>", player, npc);
    }

    public void teleToZone4() {
        Player player = getSelf();
        NpcInstance npc = getNpc();
        if (player.getLevel() >= 95)
            if (Functions.getItemCount(player, ITEM_ZONE4) >= 20) {
                Functions.removeItem(player, ITEM_ZONE4, 20);
                player.teleToLocation(TELEPORT_ZONE4);
            } else
                show("<html><body>Телепортер фарм зоны<br>У Вас нет интересующих меня предметов(*Указ Стража - Ивент - 20шт*), возвращайтесь когда они будут.</body></html>", player, npc);
        else
            show("<html><body>Телепортер фарм зоны 3 лвла:<br>Вы слишком слабы. получите 95лвл и я Вас пропущу.</body></html>", player, npc);
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }
}

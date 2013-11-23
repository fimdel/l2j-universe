package services;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.utils.ItemFunctions;

/**
 * @author pchayka
 */
public class Misc extends Functions {
    public void assembleAntharasCrystal() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        if (player == null || npc == null || !NpcInstance.canBypassCheck(player, player.getLastNpc()))
            return;

        if (ItemFunctions.getItemCount(player, 17266) < 1 || ItemFunctions.getItemCount(player, 17267) < 1) {
            show("teleporter/32864-2.htm", player);
            return;
        }
        if (ItemFunctions.removeItem(player, 17266, 1, true) > 0 && ItemFunctions.removeItem(player, 17267, 1, true) > 0) {
            ItemFunctions.addItem(player, 17268, 1, true);
            show("teleporter/32864-3.htm", player);
            return;
        }
    }
}
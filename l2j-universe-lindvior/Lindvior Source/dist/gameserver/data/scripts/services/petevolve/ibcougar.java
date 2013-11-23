package services.petevolve;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Summon;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.PetDataTable;
import l2p.gameserver.tables.PetDataTable.L2Pet;

/**
 * User: darkevil
 * Date: 07.06.2008
 * Time: 16:28:42
 */
public class ibcougar extends Functions {
    private static final int BABY_COUGAR = PetDataTable.BABY_COUGAR_ID;
    private static final int BABY_COUGAR_CHIME = L2Pet.BABY_COUGAR.getControlItemId();
    private static final int IN_COUGAR_CHIME = L2Pet.IMPROVED_BABY_COUGAR.getControlItemId();

    public void evolve() {
        Player player = getSelf();
        NpcInstance npc = getNpc();
        if (player == null || npc == null)
            return;
        Summon pl_pet = player.getSummonList().getPet();
        if (player.getInventory().getItemByItemId(BABY_COUGAR_CHIME) == null) {
            show("scripts/services/petevolve/no_item.htm", player, npc);
            return;
        }
        if (pl_pet == null || pl_pet.isDead()) {
            show("scripts/services/petevolve/evolve_no.htm", player, npc);
            return;
        }
        if (pl_pet.getNpcId() != BABY_COUGAR) {
            show("scripts/services/petevolve/no_pet.htm", player, npc);
            return;
        }
        if (pl_pet.getLevel() < 55) {
            show("scripts/services/petevolve/no_level.htm", player, npc);
            return;
        }

        int controlItemId = pl_pet.getControlItemObjId();
        player.getSummonList().unsummonPet();

        ItemInstance control = player.getInventory().getItemByObjectId(controlItemId);
        control.setItemId(IN_COUGAR_CHIME);
        control.setEnchantLevel(L2Pet.IMPROVED_BABY_COUGAR.getMinLevel());
        control.setJdbcState(JdbcEntityState.UPDATED);
        control.update();
        player.sendItemList(false);

        show("scripts/services/petevolve/yes_pet.htm", player, npc);
    }
}
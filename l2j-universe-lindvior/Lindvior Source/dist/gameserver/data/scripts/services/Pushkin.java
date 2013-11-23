package services;

import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.data.xml.holder.MultiSellHolder;
import l2p.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.MultiSellEntry;
import l2p.gameserver.model.base.MultiSellIngredient;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.templates.item.ItemTemplate;

public class Pushkin extends Functions {
    public String DialogAppend_30300(Integer val) {
        if (val != 0 || !Config.ALT_SIMPLE_SIGNS && !Config.ALT_BS_CRYSTALLIZE)
            return "";

        StringBuilder append = new StringBuilder();

        if (getSelf().isLangRus()) {
            if (Config.ALT_SIMPLE_SIGNS) {
                append.append("<br><center>Опции семи печатей:</center><br>");
                append.append("<center>[npc_%objectId%_Multisell 3112605|Сделать S-грейд меч]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112606|Вставить SA в оружие S-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112607|Распечатать броню S-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112608|Распечатать бижутерию S-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112609|Сделать A-грейд меч]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112610|Вставить SA в оружие A-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112611|Распечатать броню A-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112612|Распечатать бижутерию A-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112613|Запечатать броню A-грейда]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112601|Удалить SA из оружия]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112602|Обменять оружие с доплатой]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112603|Обменять оружие на равноценное]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112604|Завершить редкую вещь]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3111301|Купить что-нибудь]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 400|Обменять камни]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 500|Приобрести расходные материалы]</center><br1>");
            }
            if (Config.ALT_BS_CRYSTALLIZE)// TODO: сделать у всех кузнецов
                append.append("<br1>[scripts_services.Pushkin:doCrystallize|Кристаллизация]");
        } else {
            if (Config.ALT_SIMPLE_SIGNS) {
                append.append("<br><center>Seven Signs options:</center><br>");
                append.append("<center>[npc_%objectId%_Multisell 3112605|Manufacture an S-grade sword]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112606|Bestow the special S-grade weapon some abilities]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112607|Release the S-grade armor seal]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112608|Release the S-grade accessory seal]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112609|Manufacture an A-grade sword]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112610|Bestow the special A-grade weapon some abilities]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112611|Release the A-grade armor seal]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112612|Release the A-grade accessory seal]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112613|Seal the A-grade armor again]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112601|Remove the special abilities from a weapon]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112602|Upgrade weapon]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112603|Make an even exchange of weapons]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3112604|Complete a Foundation Item]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 3111301|Buy Something]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 400|Exchange Seal Stones]</center><br1>");
                append.append("<center>[npc_%objectId%_Multisell 500|Purchase consumable items]</center><br1>");
            }
            if (Config.ALT_BS_CRYSTALLIZE)
                append.append("<br1>[scripts_services.Pushkin:doCrystallize|Crystallize]");
        }

        return append.toString();
    }

    public String DialogAppend_30086(Integer val) {
        return DialogAppend_30300(val);
    }

    public String DialogAppend_30098(Integer val) {
        if (val != 0 || !Config.ALT_ALLOW_TATTOO)
            return "";
        return getSelf().isLangRus() ? "<br>[npc_%objectId%_Multisell 6500|Купить тату]" : "<br>[npc_%objectId%_Multisell 6500|Buy tattoo]";
    }

    public void doCrystallize() {
        Player player = getSelf();
        NpcInstance merchant = player.getLastNpc();
        Castle castle = merchant != null ? merchant.getCastle(player) : null;

        MultiSellListContainer list = new MultiSellListContainer();
        list.setShowAll(false);
        list.setKeepEnchant(true);
        list.setNoTax(false);
        int entry = 0;
        final Inventory inv = player.getInventory();
        for (final ItemInstance itm : inv.getItems())
            if (itm.canBeCrystallized(player)) {
                final ItemTemplate crystal = ItemHolder.getInstance().getTemplate(itm.getTemplate().getCrystalType().cry);
                MultiSellEntry possibleEntry = new MultiSellEntry(++entry, crystal.getItemId(), itm.getTemplate().getCrystalCount(), 0);
                possibleEntry.addIngredient(new MultiSellIngredient(itm.getItemId(), 1, itm.getEnchantLevel()));
                possibleEntry.addIngredient(new MultiSellIngredient(ItemTemplate.ITEM_ID_ADENA, Math.round(itm.getTemplate().getCrystalCount() * crystal.getReferencePrice() * 0.05), 0));
                list.addEntry(possibleEntry);
            }

        MultiSellHolder.getInstance().SeparateAndSend(list, player, castle == null ? 0. : castle.getTaxRate());
    }
}
package events.MasterOfEnchanting;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Autor: Bonux
 * Date: 30.08.09
 * Time: 17:49
 * http://www.lineage2.com/archive/2009/06/master_of_encha.html *
 */
public class EnchantingReward extends Functions implements ScriptFile {
    private static int MASTER_YOGI_STAFF = 13539;
    private static int MASTER_YOGI_SCROLL = 13540;

    private static int ADENA = 57;
    private static int STAFF_PRICE = 1000;
    private static int TIMED_SCROLL_PRICE = 6000;
    private static int TIMED_SCROLL_HOURS = 6;

    private static int ONE_SCROLL_PRICE = 77777;
    private static int TEN_SCROLLS_PRICE = 777770;

    private static int[] HAT_SHADOW = new int[]{13074, 13075, 13076};
    private static int[] HAT_EVENT = new int[]{13518, 13519, 13522};
    private static int[] SOUL_CRYSTALL = new int[]{9570, 9571, 9572};

    public void buy_staff() {
        Player player = getSelf();
        if (getItemCount(player, MASTER_YOGI_STAFF) == 0 && getItemCount(player, ADENA) >= STAFF_PRICE) {
            removeItem(player, ADENA, STAFF_PRICE);
            addItem(player, MASTER_YOGI_STAFF, 1);
            show("scripts/events/MasterOfEnchanting/32599-staffbuyed.htm", player);
        } else
            show("scripts/events/MasterOfEnchanting/32599-staffcant.htm", player);
    }

    public void buy_scroll_lim() {
        Player player = getSelf();
        long _reuse_time = TIMED_SCROLL_HOURS * 60 * 60 * 1000;
        long _curr_time = System.currentTimeMillis();
        String _last_use_time = player.getVar("MasterOfEnch");
        long _remaining_time;
        if (_last_use_time != null)
            _remaining_time = _curr_time - Long.parseLong(_last_use_time);
        else
            _remaining_time = _reuse_time;
        if (_remaining_time >= _reuse_time) {
            if (getItemCount(player, ADENA) >= TIMED_SCROLL_PRICE) {
                removeItem(player, ADENA, TIMED_SCROLL_PRICE);
                addItem(player, MASTER_YOGI_SCROLL, 1);
                player.setVar("MasterOfEnch", String.valueOf(_curr_time), -1);
                show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
            } else
                show("scripts/events/MasterOfEnchanting/32599-s24-no.htm", player);
        } else {
            int hours = (int) (_reuse_time - _remaining_time) / 3600000;
            int minutes = (int) (_reuse_time - _remaining_time) % 3600000 / 60000;
            if (hours > 0) {
                SystemMessage sm = new SystemMessage(SystemMessage.THERE_ARE_S1_HOURSS_AND_S2_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED);
                sm.addNumber(hours);
                sm.addNumber(minutes);
                player.sendPacket(sm);
                show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
            } else if (minutes > 0) {
                SystemMessage sm = new SystemMessage(SystemMessage.THERE_ARE_S1_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED);
                sm.addNumber(minutes);
                player.sendPacket(sm);
                show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
            } else if (getItemCount(player, ADENA) >= TIMED_SCROLL_PRICE) {
                removeItem(player, ADENA, TIMED_SCROLL_PRICE);
                addItem(player, MASTER_YOGI_SCROLL, 1);
                player.setVar("MasterOfEnch", String.valueOf(_curr_time), -1);
                show("scripts/events/MasterOfEnchanting/32599-scroll24.htm", player);
            } else
                show("scripts/events/MasterOfEnchanting/32599-s24-no.htm", player);
        }
    }

    public void buy_scroll_1() {
        Player player = getSelf();
        if (getItemCount(player, ADENA) >= ONE_SCROLL_PRICE) {
            removeItem(player, ADENA, ONE_SCROLL_PRICE);
            addItem(player, MASTER_YOGI_SCROLL, 1);
            show("scripts/events/MasterOfEnchanting/32599-scroll-ok.htm", player);
        } else
            show("scripts/events/MasterOfEnchanting/32599-s1-no.htm", player);
    }

    public void buy_scroll_10() {
        Player player = getSelf();
        if (getItemCount(player, ADENA) >= TEN_SCROLLS_PRICE) {
            removeItem(player, ADENA, TEN_SCROLLS_PRICE);
            addItem(player, MASTER_YOGI_SCROLL, 10);
            show("scripts/events/MasterOfEnchanting/32599-scroll-ok.htm", player);
        } else
            show("scripts/events/MasterOfEnchanting/32599-s10-no.htm", player);
    }

    public void receive_reward() {
        Player player = getSelf();
        int Equip_Id = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
        if (Equip_Id != MASTER_YOGI_STAFF) {
            show("scripts/events/MasterOfEnchanting/32599-rewardnostaff.htm", player);
            return;
        }
        ItemInstance enchanteditem = player.getInventory().getItemByItemId(Equip_Id);
        int Ench_Lvl = enchanteditem.getEnchantLevel();

        if (Equip_Id == MASTER_YOGI_STAFF && Ench_Lvl > 3) {
            switch (Ench_Lvl) {
                case 4:
                    addItem(player, 6406, 1); // Firework
                    break;
                case 5:
                    addItem(player, 6406, 2); // Firework
                    addItem(player, 6407, 1); // Large Firework
                    break;
                case 6:
                    addItem(player, 6406, 3); // Firework
                    addItem(player, 6407, 2); // Large Firework
                    break;
                case 7:
                    addItem(player, HAT_SHADOW[Rnd.get(HAT_SHADOW.length)], 1); //Shadow Hat Accessory
                    break;
                case 8:
                    addItem(player, 955, 1); // Scroll: Enchant Weapon (D)
                    break;
                case 9:
                    addItem(player, 955, 1); // Scroll: Enchant Weapon (D)
                    addItem(player, 956, 1); // Scroll: Enchant Armor (D)
                    break;
                case 10:
                    addItem(player, 951, 1); // Scroll: Enchant Weapon (C)
                    break;
                case 11:
                    addItem(player, 951, 1); // Scroll: Enchant Weapon (C)
                    addItem(player, 952, 1); // Scroll: Enchant Armor (C)
                    break;
                case 12:
                    addItem(player, 948, 1); // Scroll: Enchant Armor (B)
                    break;
                case 13:
                    addItem(player, 729, 1); // Scroll: Enchant Weapon (A)
                    break;
                case 14:
                    addItem(player, HAT_EVENT[Rnd.get(HAT_EVENT.length)], 1); //Event Hat Accessory
                    break;
                case 15:
                    addItem(player, 13992, 1); // Grade S Accessory Chest (Event)
                    break;
                case 16:
                    addItem(player, 8762, 1); // Top-Grade Life Stone: level 76
                    break;
                case 17:
                    addItem(player, 959, 1); // Scroll: Enchant Weapon (S)
                    break;
                case 18:
                    addItem(player, 13991, 1); // Grade S Armor Chest (Event)
                    break;
                case 19:
                    addItem(player, 13990, 1); // Grade S Weapon Chest (Event)
                    break;
                case 20:
                    addItem(player, SOUL_CRYSTALL[Rnd.get(SOUL_CRYSTALL.length)], 1); // Red/Blue/Green Soul Crystal - Stage 14
                    break;
                case 21:
                    addItem(player, 8762, 1); // Top-Grade Life Stone: level 76
                    addItem(player, 8752, 1); // High-Grade Life Stone: level 76
                    addItem(player, SOUL_CRYSTALL[Rnd.get(SOUL_CRYSTALL.length)], 1); // Red/Blue/Green Soul Crystal - Stage 14
                    break;
                case 22:
                    addItem(player, 13989, 1); // S80 Grade Armor Chest (Event)
                    break;
                case 23:
                    addItem(player, 13988, 1); // S80 Grade Weapon Chest (Event)
                    break;
                default:
                    if (Ench_Lvl > 23)
                        addItem(player, 13988, 1); // S80 Grade Weapon Chest (Event)
                    break;
            }
            removeItem(player, Equip_Id, 1);
            show("scripts/events/MasterOfEnchanting/32599-rewardok.htm", player);
        } else
            show("scripts/events/MasterOfEnchanting/32599-rewardnostaff.htm", player);
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
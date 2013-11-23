package services;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.utils.ItemFunctions;

import static l2p.gameserver.model.entity.olympiad.Olympiad.OLYMPIAD_HTML_PATH;

public class HeroItems extends Functions {
    //TODO: Bonux, переделать, чтобы было мультиязычное!
    private static final String[][] HERO_ITEMS = {
            {
                    "30392",
                    "weapon_the_dagger_of_hero_i00",
                    "Острие Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк. Дополнительно увеличивает шанс Крит. Атк.",
                    "640/399",
                    "Dagger"},
            {
                    "30393",
                    "weapon_the_sword_of_hero_i00",
                    "Резак Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. И Скор. Атк. Дополнительно увеличивает шанс Крит. Атк.",
                    "720/399",
                    "Sword"},
            {
                    "30394",
                    "weapon_the_two_handed_sword_of_hero_i00",
                    "Эспадон Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. И Скор. Атк. Дополнительно увеличивает шанс Крит. Атк.",
                    "877/399",
                    "Big Sword"},
            {
                    "30395",
                    "weapon_the_axe_of_hero_i00",
                    "Мститель Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк. Дополнительно увеличивает шанс Крит. Атк.",
                    "720/399",
                    "Blunt"},
            {
                    "30396",
                    "weapon_the_fist_of_hero_i00",
                    "Воитель Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "877/399",
                    "Dual Fist"},
            {
                    "30397",
                    "weapon_the_pole_of_hero_i00",
                    "Буревестник Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "720/399",
                    "Pole"},
            {
                    "30398",
                    "weapon_the_bow_of_hero_i00",
                    "Бросок Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "1419/399",
                    "Bow"},
            {
                    "30399",
                    "weapon_infinity_shooter_i00",
                    "Страж Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "908/399",
                    "Crossbow"},
            {
                    "30400",
                    "weapon_infinity_sword_i00",
                    "Расчленитель Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно придает эффект Проницательности.",
                    "592/508",
                    "Sword"},
            {
                    "30401",
                    "weapon_the_mace_of_hero_i00",
                    "Заклинатель Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно придает эффект Проницательности.",
                    "592/508",
                    "Blunt"},
            {
                    "30402",
                    "weapon_the_staff_of_hero_i00",
                    "Возмездие Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно придает эффект Проницательности.",
                    "721/551",
                    "Blunt"},
            {
                    "30403",
                    "weapon_r_dualsword_i00",
                    "Парные Мечи Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "877/399",
                    "Dual"},
            {
                    "30404",
                    "weapon_r_dualdagger_i00",
                    "Парные Кинжалы Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "738/399",
                    "Dual Dagger"},
            {
                    "30405",
                    "weapon_r_dualblunt_i00",
                    "Парные Дубины Вечности",
                    "Священное Оружие Героя. Увеличивает макс. HP/макс. MP/макс. CP/ урон при PvP/ Скор. Маг. и Скор. Атк.. Дополнительно увеличивает шанс Крит. Атк.",
                    "877/399",
                    "Dual Blunt"}};

    @SuppressWarnings("unused")
    public void rendershop(String[] val) {
        Player player = getSelf();
        NpcInstance npc = getNpc();
        if (player == null || npc == null)
            return;

        String fileName = OLYMPIAD_HTML_PATH;
        boolean succ = true;
        if (!player.isHero()) {
            fileName += "monument_weapon_no_hero.htm";
            succ = false;
        } else
            for (String heroItem[] : HERO_ITEMS) {
                int itemId = Integer.parseInt(heroItem[0]);
                if (player.getInventory().getItemByItemId(itemId) != null) {
                    fileName += "monument_weapon_have.htm";
                    succ = false;
                    break;
                }
            }

        if (!succ) {
            npc.showChatWindow(player, fileName);
            return;
        }

        boolean isKamael = player.getRace() == Race.kamael;
        String htmltext = "";
        if (val[0].equalsIgnoreCase("list")) {
            htmltext = "<html><body><font color=\"LEVEL\">List of Hero Weapons:</font><table border=0 width=270><tr><td>";
            for (int i = 0; i < HERO_ITEMS.length; i++) {
                htmltext += "<tr><td width=32 height=45 valign=top>";
                htmltext += "<img src=icon." + HERO_ITEMS[i][1] + " width=32 height=32></td>";
                htmltext += "<td valign=top>[<a action=\"bypass -h scripts_services.HeroItems:rendershop " + i + "\">" + HERO_ITEMS[i][2] + "</a>]<br1>";
                htmltext += "Type: " + HERO_ITEMS[i][5] + ", Patk/Matk: " + HERO_ITEMS[i][4];
                htmltext += "</td></tr>";
            }
            htmltext += "</table>";
        } else if (Integer.parseInt(val[0]) >= 0 && Integer.parseInt(val[0]) <= HERO_ITEMS.length) {
            int itemIndex = Integer.parseInt(val[0]);

            // Для камаэль оружия сообщение:
            // 2234 Will you use the selected Kamael race-only Hero Weapon?
            // Для всего остального оружия сообщение:
            // 1484 Are you sure this is the Hero weapon you wish to use? Kamael race cannot use this.
            int msgId = /**itemIndex > 10 ? 2234 : **/
                    1484;

            htmltext = "<html><body><font color=\"LEVEL\">Item Information:</font><table border=0 width=270><tr><td>";
            htmltext += "<img src=\"L2UI.SquareWhite\" width=270 height=1>";
            htmltext += "<table border=0 width=240>";
            htmltext += "<tr><td width=32 height=45 valign=top>";
            htmltext += "<img src=icon." + HERO_ITEMS[itemIndex][1] + " width=32 height=32></td>";
            htmltext += "<td valign=top>[<a action=\"bypass -h scripts_services.HeroItems:getweapon " + HERO_ITEMS[itemIndex][0] + "\" msg=\"" + msgId + "\">" + HERO_ITEMS[itemIndex][2] + "</a>]<br1>";
            htmltext += "Type: " + HERO_ITEMS[itemIndex][5] + ", Patk/Matk: " + HERO_ITEMS[itemIndex][4] + "<br1>";
            htmltext += "</td></tr></table>";
            htmltext += "<font color=\"B09878\">" + HERO_ITEMS[itemIndex][3] + "</font>";
            htmltext += "</td></tr></table><br>";
            htmltext += "<img src=\"L2UI.SquareWhite\" width=270 height=1><br><br>";
            htmltext += "<CENTER><button value=Back action=\"bypass -h scripts_services.HeroItems:rendershop list\" width=40 height=15 back=L2UI_CT1.Button_DF fore=L2UI_CT1.Button_DF></CENTER>";

        }
        show(htmltext, player, npc);
    }

    public void getweapon(String[] var) {
        Player player = getSelf();
        if (player == null)
            return;

        int item = Integer.parseInt(var[0]);
        if (item < 30392 && item > 30405) {
            System.out.println(player.getName() + " tried to obtain non hero item using hero weapon service. Ban him!");
            return;
        }

        NpcInstance npc = getNpc();
        if (npc == null)
            return;

        String fileName = OLYMPIAD_HTML_PATH;
        if (player.isHero()) {
            boolean have = false;
            for (String heroItem[] : HERO_ITEMS) {
                int itemId = Integer.parseInt(heroItem[0]);
                if (player.getInventory().getItemByItemId(itemId) != null) {
                    fileName += "monument_weapon_have.htm";
                    have = true;
                    break;
                }
            }
            if (!have) {
                ItemFunctions.addItem(player, item, 1, true);
                fileName += "monument_weapon_give.htm";
            }
        } else
            fileName += "monument_weapon_no_hero.htm";

        npc.showChatWindow(player, fileName);
    }

    public String getcir() {
        Player player = getSelf();
        if (player == null)
            return null;

        NpcInstance npc = getNpc();
        if (npc == null)
            return null;

        String fileName = OLYMPIAD_HTML_PATH;
        if (player.isHero()) {
            if (player.getInventory().getItemByItemId(6842) != null)
                fileName += "monument_circlet_have.htm";
            else {
                ItemFunctions.addItem(player, 6842, 1, true); //Wings of Destiny Circlet
                fileName += "monument_circlet_give.htm";
            }
        } else
            fileName += "monument_circlet_no_hero.htm";

        npc.showChatWindow(player, fileName);
        return null;
    }
}
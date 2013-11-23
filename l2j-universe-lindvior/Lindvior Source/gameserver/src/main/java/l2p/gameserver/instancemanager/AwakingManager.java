/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.instancemanager;

import gnu.trove.map.hash.TIntIntHashMap;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.tables.SkillTreeTable;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwakingManager {// implements OnPlayerEnterListener {
    private static final Logger _log = LoggerFactory.getLogger(AwakingManager.class);
    private static AwakingManager _instance;

    public static final int SCROLL_OF_AFTERLIFE = 17600;
    public static final Location TELEPORT_LOC = new Location(-114962, 226564, -2864);

    public static TIntIntHashMap _CA = new TIntIntHashMap(36);

    /**
     * ******************************************************************************************
     * 139 Рыцарь Савуло: Рыцарь Феникса, Рыцарь Ада, Храмовник Евы, Храмовник Шилен.
     * 140 Воин Тейваза: Полководец, Дуэлист, Титан, Аватар, Мастер, Каратель.
     * 141 Разбойник Отилы: Авантюрист, Странник Ветра, Призрачный Охотник, Кладоискатель.
     * 142 Лучник Эйваза: Снайпер, Страж Лунного Света, Страж Теней, Диверсант.
     * 143 Волшебник Фео: Archmahe, Soultaker, Mystic muse, StormScreamer, SoulHound
     * 144 Ис Заклинатель: Hierophant, Doomcryer, Dominator, Sword Muse, Spectral Dancer, Judicator
     * 145 Призыватель Веньо: Arcana Lord, Elemental master, Spectral Master
     * 146 Целитель Альгиза: Cardinal, Eva’s Saint, Shilien saint
     * ********************************************************************************************
     */

    public void load() {
        _CA.clear();

        _CA.put(90, 139);
        _CA.put(91, 139);
        _CA.put(99, 139);
        _CA.put(106, 139);
        _CA.put(89, 140);
        _CA.put(88, 140);
        _CA.put(113, 140);
        _CA.put(114, 140);
        _CA.put(118, 140);
        _CA.put(131, 140);
        _CA.put(93, 141);
        _CA.put(101, 141);
        _CA.put(108, 141);
        _CA.put(117, 141);
        _CA.put(92, 142);
        _CA.put(102, 142);
        _CA.put(109, 142);
        _CA.put(134, 142);
        _CA.put(94, 143);
        _CA.put(95, 143);
        _CA.put(103, 143);
        _CA.put(110, 143);
        _CA.put(132, 143);
        _CA.put(133, 143);
        _CA.put(98, 144);
        _CA.put(116, 144);
        _CA.put(115, 144);
        _CA.put(100, 144);
        _CA.put(107, 144);
        _CA.put(136, 144);
        _CA.put(96, 145);
        _CA.put(104, 145);
        _CA.put(111, 145);
        _CA.put(97, 146);
        _CA.put(105, 146);
        _CA.put(112, 146);

        _log.info("AwakingManager: Loaded 8 Awaking class for " + _CA.size() + " normal class.");

    }

    public static AwakingManager getInstance() {
        if (_instance == null) {
            _log.info("Initializing: AwakingManager");
            _instance = new AwakingManager();
            _instance.load();
        }
        return _instance;
    }

    /*  public static void SendReqToStartQuest(Player player) {
if (player.getClassId().level() < 3)
return;
if (player.getLevel() < 85)
return;
if (player.isAwaking())
return;
int newClass = _CA.get(player.getClassId().getId());
player.sendPacket(new ExCallToChangeClass(newClass, true));
}

public static void SendReqToAwaking(Player player) {
if (player.getClassId().level() < 3)
return;
if (player.getLevel() < 85)
return;
if (player.isAwaking())
return;
int newClass = _CA.get(player.getClassId().getId());
player.sendPacket(new ExChangeToAwakenedClass(newClass));
}

public void onStartQuestAccept(Player player) {
// Телепортируем в музей
player.teleToLocation(-114628, 259915, -1192);
// Показываем видео
player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
}

public void SetAwakingId(Player player) {
int _oldId = player.getClassId().getId();
player.setClassId(_CA.get(_oldId), false);
player.broadcastUserInfo(true);
player.broadcastPacket(new SocialAction(player.getObjectId(), (_CA.get(_oldId) - 119)));
giveItems(player);
}

@Override
public void onPlayerEnter(Player player) {
if (!Config.AWAKING_FREE) {
if (player.getClassId().level() < 3)
return;
if (player.getLevel() < 85)
return;
if (player.isAwaking())
return;
if (player.getActiveSubClass().isBase() || player.getActiveSubClass().isDouble()) {
player.sendPacket(new ExCallToChangeClass(_CA.get(player.getClassId().getId()), false));
}
}
}

public void giveItems(Player player) {
switch (player.getClassId().getId()) {
case 139:
ItemFunctions.addItem(player, 32264, 1, false);
ItemFunctions.addItem(player, 33735, 1, false);
break;
case 140:
ItemFunctions.addItem(player, 32265, 1, false);
ItemFunctions.addItem(player, 33742, 1, false);
break;
case 141:
ItemFunctions.addItem(player, 32266, 1, false);
ItemFunctions.addItem(player, 33722, 1, false);
break;
case 142:
ItemFunctions.addItem(player, 32267, 1, false);
ItemFunctions.addItem(player, 33763, 1, false);
break;
case 143:
ItemFunctions.addItem(player, 32268, 1, false);
ItemFunctions.addItem(player, 33732, 1, false);
break;
case 144:
ItemFunctions.addItem(player, 32270, 1, false);
ItemFunctions.addItem(player, 33727, 1, false);
break;
case 145:
ItemFunctions.addItem(player, 32269, 1, false);
ItemFunctions.addItem(player, 33740, 1, false);
break;
case 146:
ItemFunctions.addItem(player, 32271, 1, false);
ItemFunctions.addItem(player, 33726, 1, false);
}
}                           */

    public Skill getRaceSkill(Player player) {
        int race = player.getRace().ordinal();
        Skill skill = null;
        switch (race) {
            case 0:
                skill = SkillTable.getInstance().getInfo(1901, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1902, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1903, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1904, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(248, 6);
                player.addSkill(skill);
                break;
            case 1:
                skill = SkillTable.getInstance().getInfo(1905, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1906, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1907, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1908, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(248, 6);
                player.addSkill(skill);
                break;
            case 2:
                skill = SkillTable.getInstance().getInfo(1909, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1910, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1911, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1912, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1913, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(248, 6);
                player.addSkill(skill);
                break;
            case 3:
                skill = SkillTable.getInstance().getInfo(1914, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1915, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1916, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1917, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(248, 6);
                player.addSkill(skill);
                break;
            case 4:
                skill = SkillTable.getInstance().getInfo(1919, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1920, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1921, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1922, 1);
                player.addSkill(skill);
                if (player.getLevel() >= 85 || player.getLevel() <= 89) {
                    skill = SkillTable.getInstance().getInfo(19088, 1);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 90 || player.getLevel() <= 94) {
                    skill = SkillTable.getInstance().getInfo(19088, 2);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 95 || player.getLevel() <= 98) {
                    skill = SkillTable.getInstance().getInfo(19088, 3);
                    player.addSkill(skill);
                }
                if (player.getLevel() == 99) {
                    skill = SkillTable.getInstance().getInfo(19088, 4);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 85 || player.getLevel() <= 89) {
                    skill = SkillTable.getInstance().getInfo(19089, 1);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 90 || player.getLevel() <= 94) {
                    skill = SkillTable.getInstance().getInfo(19089, 2);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 95 || player.getLevel() <= 98) {
                    skill = SkillTable.getInstance().getInfo(19089, 3);
                    player.addSkill(skill);
                }
                if (player.getLevel() == 99) {
                    skill = SkillTable.getInstance().getInfo(19089, 4);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 85 || player.getLevel() <= 89) {
                    skill = SkillTable.getInstance().getInfo(19090, 1);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 90 || player.getLevel() <= 94) {
                    skill = SkillTable.getInstance().getInfo(19090, 2);
                    player.addSkill(skill);
                }
                if (player.getLevel() >= 95 || player.getLevel() <= 98) {
                    skill = SkillTable.getInstance().getInfo(19090, 3);
                    player.addSkill(skill);
                }
                if (player.getLevel() == 99) {
                    skill = SkillTable.getInstance().getInfo(19090, 4);
                    player.addSkill(skill);
                }
                skill = SkillTable.getInstance().getInfo(248, 6);
                player.addSkill(skill);
                break;
            case 5:
                skill = SkillTable.getInstance().getInfo(1923, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1924, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1925, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1926, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(1954, 1);
                player.addSkill(skill);
                skill = SkillTable.getInstance().getInfo(248, 6);
                player.addSkill(skill);
                break;
        }
        player.updateStats();
        return null;
    }

    public static void ItemsOnLevelDual(Player player) {
        int level = player.getLevel();
        if (player.getActiveSubClass().isDouble()) {
            if (level == 85)
                ItemFunctions.addItem(player, 36078, 1, true);
            if (level == 90)
                ItemFunctions.addItem(player, 36078, 1, true);
            if (level == 95)
                ItemFunctions.addItem(player, 36078, 1, true);
            if (level == 99)
                ItemFunctions.addItem(player, 36078, 1, true);
        }

    }      //Glory Days

    public static void ItemsOnLevel(Player player) {
        int level = player.getLevel();
        switch (level) {
            case 5:
                ItemFunctions.addItem(player, 20463, 1, false);   //<!-- Assassin's Bamboo Hat 7-Day Pack -->
                player.sendMessage("Вы достигли уровня 5!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 10:
                ItemFunctions.addItem(player, 13276, 1, false);   //<!-- Hunting Helper Exchange Coupon 1-Sheet Pack -->
                player.sendMessage("Вы достигли уровня 10!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 20:
                ItemFunctions.addItem(player, 15225, 1, false);  //<!-- Brigandine of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15229, 1, false);  //<!-- Manticore Leather of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15233, 1, false);  //<!-- Mithril Tunic of Fortune Set Pack (10-day limited period) -->
                player.sendMessage("Вы достигли уровня 20!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 40:
                ItemFunctions.addItem(player, 15232, 1, false); //<!-- Karmian of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15224, 1, false); //<!-- Full Plate of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15228, 1, false); //<!-- Plate Leather of Fortune Set Pack (10-day limited period) -->
                player.sendMessage("Вы достигли уровня 40!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 45:
                ItemFunctions.addItem(player, 13276, 1, false);  //<!-- Hunting Helper Exchange Coupon 1-Sheet Pack -->
                player.sendMessage("Вы достигли уровня 45!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 52:
                ItemFunctions.addItem(player, 15223, 1, false);  //<!-- Doom Plate Armor of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15227, 1, false);  //<!-- Leather Armor of Doom of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15231, 1, false);  //<!-- Avadon Robe of Fortune Set Pack (10-day limited period) -->
                player.sendMessage("Вы достигли уровня 52!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 60:
                ItemFunctions.addItem(player, 13276, 1, false);  //<!-- Hunting Helper Exchange Coupon 1-Sheet Pack -->
                player.sendMessage("Вы достигли уровня 60!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 61:
                ItemFunctions.addItem(player, 15226, 1, false); //<!-- Dark Crystal Light Armor of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15230, 1, false); //<!-- Dark Crystal Robe of Fortune Set Pack (10-day limited period) -->
                ItemFunctions.addItem(player, 15222, 1, false); //<!-- Fortune Heavy Armor of Nightmare Set Pack (10-day limited period) -->
                player.sendMessage("Вы достигли уровня 61!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 66:
                ItemFunctions.addItem(player, 21630, 1, false);   //<!-- Vitality Maintaining Potion Pack -->
                ItemFunctions.addItem(player, 21631, 1, false);   //<!-- Vitality Replenishing Potion Pack -->
                player.sendMessage("Вы достигли уровня 66!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 70:
                ItemFunctions.addItem(player, 21630, 1, false);   //<!-- Vitality Maintaining Potion Pack -->
                ItemFunctions.addItem(player, 21631, 1, false);   //<!-- Vitality Replenishing Potion Pack -->
                player.sendMessage("Вы достигли уровня 70!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 72:
                ItemFunctions.addItem(player, 14291, 1, false);   //<!-- High-Grade Hunting Helper Exchange Coupon 1-Sheet Pack (Event) -->
                player.sendMessage("Вы достигли уровня 72!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 78:
                ItemFunctions.addItem(player, 14291, 1, false);  //<!-- High-Grade Hunting Helper Exchange Coupon 1-Sheet Pack (Event) -->
                player.sendMessage("Вы достигли уровня 78!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 81:
                ItemFunctions.addItem(player, 21630, 1, false);  //<!-- Vitality Maintaining Potion Pack -->
                ItemFunctions.addItem(player, 21631, 1, false);  //<!-- Vitality Replenishing Potion Pack -->
                player.sendMessage("Вы достигли уровня 81!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 82:
                ItemFunctions.addItem(player, 14291, 1, false);  //<!-- High-Grade Hunting Helper Exchange Coupon 1-Sheet Pack (Event) -->
                player.sendMessage("Вы достигли уровня 82!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 84:
                ItemFunctions.addItem(player, 21630, 1, false);  //<!-- Vitality Maintaining Potion Pack -->
                ItemFunctions.addItem(player, 21631, 1, false);  //<!-- Vitality Replenishing Potion Pack -->
                player.sendMessage("Вы достигли уровня 84!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 85:
                ItemFunctions.addItem(player, 6673, 25, false); //<!-- Festival Adena -->
                ItemFunctions.addItem(player, 21237, 1, false); //<!-- Super Healthy Juice (HP) Pack -->
                ItemFunctions.addItem(player, 21241, 1, false); //<!-- Super Healthy Juice (CP) Pack -->
                player.sendMessage("Вы достигли уровня 85!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            case 99:
                ItemFunctions.addItem(player, SkillTreeTable.NEW_ENCHANT_BOOK, 5, false);
                ItemFunctions.addItem(player, SkillTreeTable.NEW_SAFE_ENCHANT_BOOK, 5, false);
                ItemFunctions.addItem(player, SkillTreeTable.NEW_CHANGE_ENCHANT_BOOK, 5, false);
                ItemFunctions.addItem(player, SkillTreeTable.UNTRAIN_NEW_ENCHANT_BOOK, 5, false);
                player.sendMessage("Вы достигли уровня 99!\\nВы получаете подарок, который поможет в Ваших странствиях.\\nКоманда сервера Rest-Zone.");
                break;
            default:
                break;
        }
    }

    /*  public int getAwakeningId(int oldClass) {
 return _CA.containsKey(oldClass) ? _CA.get(oldClass) : oldClass;
}       */
}

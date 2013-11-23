package l2p.gameserver.model.base;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.tables.SkillTreeTable;
import l2p.gameserver.utils.ItemFunctions;

/**
 * @author ALF
 * @data 09.02.2012 Description: В процесс Перерождения улучшение умений второй и третьей профессии обнуляется,
 * и персонаж получает компенсацию в виде Эссенций Гигантов.
 * Эссенции Гигантов можно обменять на Кодексы Перерождения у НПЦ Слуга Гигантов Лабидорф:
 */
public class EssenceGiants {
    private static final int ESSENCE_OF_THE_LESSER_GIANTS = 30306;
    private static final int[] count30 = {0,//0
            0,//1
            0,//2
            0,//3
            1,//4
            1,//5
            2,//6
            3,//7
            4,//8
            5,//9
            6,//10
            7,//11
            9,//12
            10,//13
            12,//14
            13,//15
            15,//16
            17,//17
            19,//18
            22,//19
            24,//20
            27,//21
            29,//22
            32,//23
            35,//24
            42,//25
            45,//26
            48,//27
            63,//28
            70,//29
            83 //30
    };

    private static final int[] count15 = {0,//0
            0,//1
            0,//2
            0,//3
            1,//4
            1,//5
            2,//6
            3,//7
            4,//8
            5,//9
            7,//10
            9,//11
            10,//12
            19,//13
            24,//14
            35 //15
    };

    public static void compensate(Player player, Skill skill) {
        int count;
        int skillLvl = skill.getDisplayId();
        int elevel = skillLvl % 100;

        if (SkillTreeTable.isEnchantable(skill) == 0)
            return;

        EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(skill.getId(), skillLvl);

        if (sl == null)
            return;

        if (sl.getMaxLevel() == 15) {
            elevel = Math.min(count15.length, elevel);
            count = count15[elevel];
        } else {
            elevel = Math.min(count30.length, elevel);
            count = count30[elevel];
        }

        ItemFunctions.addItem(player, ESSENCE_OF_THE_LESSER_GIANTS, count, true);
    }
}

/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.base;

import l2p.gameserver.model.Player;
import l2p.gameserver.utils.ItemFunctions;

public class ProfessionRewards {
    public static void checkedAndGiveChest(Player player, int classId) {
        int chestId = 0;
        switch (classId) {
            case 139:
                chestId = 32264;
                break;
            case 140:
                chestId = 32265;
                break;
            case 141:
                chestId = 32266;
                break;
            case 142:
                chestId = 32267;
                break;
            case 143:
                chestId = 32268;
                break;
            case 144:
                chestId = 32271;
                break;
            case 145:
                chestId = 32269;
                break;
            case 146:
                chestId = 32270;
        }
        ItemFunctions.addItem(player, chestId, 1, true);
    }

    public static void checkedAndGiveWeapon(Player player, int oldClassId) {
        int weaponId = 0;
        // Humans
        switch (oldClassId) {
            case 89:
                weaponId = 33718;
                break;
            case 88:
                weaponId = 33717;
                break;
            case 90:
                weaponId = 33719;
                break;
            case 91:
                weaponId = 33720;
                break;
            case 93:
                weaponId = 33722;
                break;
            case 92:
                weaponId = 33721;
                break;
            case 94:
                weaponId = 33723;
                break;
            case 95:
                weaponId = 33724;
                break;
            case 96:
                weaponId = 33725;
                break;
            case 97:
                weaponId = 33726;
                break;
            case 98:
                weaponId = 33727;
                break;
            // Elves
            case 99:
                weaponId = 33728;
                break;
            case 100:
                weaponId = 33729;
                break;
            case 101:
                weaponId = 33730;
                break;
            case 102:
                weaponId = 33731;
                break;
            case 103:
                weaponId = 33732;
                break;
            case 104:
                weaponId = 33733;
                break;
            case 105:
                weaponId = 33734;
                break;
            // Dark Elves
            case 106:
                weaponId = 33735;
                break;
            case 107:
                weaponId = 33736;
                break;
            case 108:
                weaponId = 33737;
                break;
            case 109:
                weaponId = 33738;
                break;
            case 110:
                weaponId = 33739;
                break;
            case 111:
                weaponId = 33740;
                break;
            case 112:
                weaponId = 33741;
                break;
            // Orks
            case 113:
                weaponId = 33742;
                break;
            case 114:
                weaponId = 33743;
                break;
            case 115:
                weaponId = 33744;
                break;
            case 116:
                weaponId = 33745;
                break;
            // Dwarfs
            case 117:
                weaponId = 33746;
                break;
            case 118:
                weaponId = 33747;
                break;
            // Kamaels
            case 131:
                weaponId = 33760;
                break;
            case 132:
                weaponId = 33761;
                break;
            case 135:
                weaponId = 33765;
                break;
            case 133:
                weaponId = 33762;
                break;
            case 134:
                weaponId = 33763;
        }
        ItemFunctions.addItem(player, weaponId, 1, true);
    }

    public static int getThirdClassForId(int classId) {
        int result = -1;
        switch (classId) {
            case 30:
                result = 105;
                break;
            case 20:
                result = 99;
                break;
            case 8:
                result = 93;
                break;
            case 14:
                result = 96;
                break;
            case 12:
                result = 94;
                break;
            case 16:
                result = 97;
                break;
            case 51:
                result = 115;
                break;
            case 127:
                result = 131;
                break;
            case 52:
                result = 116;
                break;
            case 3:
                result = 89;
                break;
            case 2:
                result = 88;
                break;
            case 28:
                result = 104;
                break;
            case 55:
                result = 117;
                break;
            case 36:
                result = 108;
                break;
            case 37:
                result = 109;
                break;
            case 48:
                result = 114;
                break;
            case 6:
                result = 91;
                break;
            case 17:
                result = 98;
                break;
            case 57:
                result = 118;
                break;
            case 24:
                result = 102;
                break;
            case 27:
                result = 103;
                break;
            case 5:
                result = 90;
                break;
            case 9:
                result = 92;
                break;
            case 43:
                result = 112;
                break;
            case 33:
                result = 106;
                break;
            case 128:
                result = 132;
                break;
            case 129:
                result = 133;
                break;
            case 13:
                result = 95;
                break;
            case 34:
                result = 107;
                break;
            case 41:
                result = 111;
                break;
            case 40:
                result = 110;
                break;
            case 21:
                result = 100;
                break;
            case 46:
                result = 113;
                break;
            case 130:
                result = 134;
                break;
            case 23:
                result = 101;
                break;
            case 135:
                result = 136;
        }
        return result;
    }

    public static int getAwakenedClassForId(int classId) {
        int result = -1;
        switch (classId) {
            case 90:
            case 91:
            case 99:
            case 106:
                result = 139;
                break;
            case 88:
            case 89:
            case 113:
            case 114:
            case 118:
            case 131:
                result = 140;
                break;
            case 93:
            case 101:
            case 108:
            case 117:
                result = 141;
                break;
            case 92:
            case 102:
            case 109:
            case 134:
                result = 142;
                break;
            case 94:
            case 95:
            case 103:
            case 110:
            case 132:
            case 133:
                result = 143;
                break;
            case 98:
            case 100:
            case 107:
            case 115:
            case 116:
            case 136:
                result = 144;
                break;
            case 96:
            case 104:
            case 111:
                result = 145;
                break;
            case 97:
            case 105:
            case 112:
                result = 146;
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 135:
        }
        return result;
    }
}

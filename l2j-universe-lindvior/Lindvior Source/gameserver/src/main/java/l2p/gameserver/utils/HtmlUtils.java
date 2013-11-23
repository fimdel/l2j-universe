/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.utils;

import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.network.serverpackets.components.SysString;

/**
 * @author VISTALL
 * @date 17:17/21.04.2011
 */
public class HtmlUtils {
    public static final String PREV_BUTTON = "<button value=\"&$1037;\" action=\"bypass %prev_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
    public static final String NEXT_BUTTON = "<button value=\"&$1038;\" action=\"bypass %next_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

    public static String htmlResidenceName(int id) {
        return "&%" + id + ";";
    }

    public static String htmlNpcName(int npcId) {
        return "&@" + npcId + ";";
    }

    public static String htmlSysString(SysString sysString) {
        return htmlSysString(sysString.getId());
    }

    public static String htmlSysString(int id) {
        return "&$" + id + ";";
    }

    public static String htmlItemName(int itemId) {
        return "&#" + itemId + ";";
    }

    public static String htmlClassName(int classId) {
        return "<ClassId>" + classId + "</ClassId>";
    }

    public static String htmlNpcString(NpcString id, Object... params) {
        return htmlNpcString(id.getId(), params);
    }

    public static String htmlNpcString(int id, Object... params) {
        String replace = "<fstring";
        if (params.length > 0)
            for (int i = 0; i < params.length; i++)
                replace += " p" + (i + 1) + "=\"" + String.valueOf(params[i]) + "\"";
        replace += ">" + id + "</fstring>";
        return replace;
    }

    public static String htmlButton(String value, String action, int width) {
        return htmlButton(value, action, width, 22);
    }

    public static String htmlButton(String value, String action, int width, int height) {
        return String.format("<button value=\"%s\" action=\"%s\" back=\"L2UI_CT1.Button_DF_Small_Down\" width=%d height=%d fore=\"L2UI_CT1.Button_DF_Small\">", value, action, width, height);
    }

    public static String htmlClassNameNonClient(int classId) {
        String sClassName = "";
        switch (classId) {
            case 2:
                sClassName = "Gladiator";
                break;
            case 3:
                sClassName = "Warlord";
                break;
            case 5:
                sClassName = "Paladin";
                break;
            case 6:
                sClassName = "Dark Avenger";
                break;
            case 8:
                sClassName = "Treasure Hunter";
                break;
            case 9:
                sClassName = "Hawkeye";
                break;
            case 12:
                sClassName = "Sorceror";
                break;
            case 13:
                sClassName = "Necromancer";
                break;
            case 14:
                sClassName = "Warlock";
                break;
            case 16:
                sClassName = "Bishop";
                break;
            case 17:
                sClassName = "Prophet";
                break;
            case 20:
                sClassName = "Temple Knight";
                break;
            case 21:
                sClassName = "Sword Singer";
                break;
            case 23:
                sClassName = "Plains Walker";
                break;
            case 24:
                sClassName = "Silver Ranger";
                break;
            case 27:
                sClassName = "Spellsinger";
                break;
            case 28:
                sClassName = "Elemental Summoner";
                break;
            case 30:
                sClassName = "Elder";
                break;
            case 33:
                sClassName = "Shillien Knight";
                break;
            case 34:
                sClassName = "Bladedancer";
                break;
            case 36:
                sClassName = "Abyss Walker";
                break;
            case 37:
                sClassName = "Phantom Ranger";
                break;
            case 40:
                sClassName = "Spellhowler";
                break;
            case 41:
                sClassName = "Phantom Summoner";
                break;
            case 43:
                sClassName = "Shillien Elder";
                break;
            case 46:
                sClassName = "Destroyer";
                break;
            case 48:
                sClassName = "Tyrant";
                break;
            case 51:
                sClassName = "Overlord";
                break;
            case 52:
                sClassName = "Warcryer";
                break;
            case 55:
                sClassName = "Bounty Hunter";
                break;
            case 57:
                sClassName = "Warsmith";
                break;
            case 88:
                sClassName = "Duelist";
                break;
            case 89:
                sClassName = "Dreadnought";
                break;
            case 90:
                sClassName = "Phoenix Knight";
                break;
            case 91:
                sClassName = "Hell Knight";
                break;
            case 92:
                sClassName = "Sagittarius";
                break;
            case 93:
                sClassName = "Adventurer";
                break;
            case 94:
                sClassName = "Archmage";
                break;
            case 95:
                sClassName = "Soultaker";
                break;
            case 96:
                sClassName = "Arcana Lord";
                break;
            case 97:
                sClassName = "Cardinal";
                break;
            case 98:
                sClassName = "Hierophant";
                break;
            case 99:
                sClassName = "Eva Templar";
                break;
            case 100:
                sClassName = "Sword Muse";
                break;
            case 101:
                sClassName = "Wind Rider";
                break;
            case 102:
                sClassName = "Moonlight Sentinel";
                break;
            case 103:
                sClassName = "Mystic Muse";
                break;
            case 104:
                sClassName = "Elemental Master";
                break;
            case 105:
                sClassName = "Eva Saint";
                break;
            case 106:
                sClassName = "Shillien Templar";
                break;
            case 107:
                sClassName = "Spectral Dancer";
                break;
            case 108:
                sClassName = "Ghost Hunter";
                break;
            case 109:
                sClassName = "Ghost Sentinel";
                break;
            case 110:
                sClassName = "Storm Screamer";
                break;
            case 111:
                sClassName = "Spectral Master";
                break;
            case 112:
                sClassName = "Shillien Saint";
                break;
            case 113:
                sClassName = "Titan";
                break;
            case 114:
                sClassName = "Ggrand Khauatari";
                break;
            case 115:
                sClassName = "Dominator";
                break;
            case 116:
                sClassName = "Doomcryer";
                break;
            case 117:
                sClassName = "Fortune Seeker";
                break;
            case 118:
                sClassName = "Maestro";
                break;
            case 127:
                sClassName = "Berserker";
                break;
            case 128:
                sClassName = "Male Soulbreaker";
                break;
            case 129:
                sClassName = "Female Soulbreaker";
                break;
            case 130:
                sClassName = "Arbalester";
                break;
            case 131:
                sClassName = "Doombringer";
                break;
            case 132:
                sClassName = "Male Soulhound";
                break;
            case 133:
                sClassName = "Female Soulhound";
                break;
            case 134:
                sClassName = "Trickster";
                break;
            case 135:
                sClassName = "Inspector";
                break;
            case 136:
                sClassName = "Judicator";
                break;
            case 139:
                sClassName = "sigelKnight";
                break;
            case 140:
                sClassName = "tyrrWarrior";
                break;
            case 141:
                sClassName = "othellRogue";
                break;
            case 142:
                sClassName = "yulArcher";
                break;
            case 143:
                sClassName = "feohWizard";
                break;
            case 144:
                sClassName = "issEnchanter";
                break;
            case 145:
                sClassName = "wynnSummoner";
                break;
            case 146:
                sClassName = "aeoreHealer";
                break;
            default:
                sClassName = "No name " + classId;
        }
        return sClassName;
    }
}

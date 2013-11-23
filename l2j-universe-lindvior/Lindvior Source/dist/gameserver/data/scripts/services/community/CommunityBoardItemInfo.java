/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import l2p.gameserver.Config;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.base.Element;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.*;
import l2p.gameserver.templates.item.ArmorTemplate;
import l2p.gameserver.templates.item.EtcItemTemplate;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.item.WeaponTemplate;
import l2p.gameserver.utils.HtmlUtils;
import l2p.gameserver.utils.Language;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;

public class CommunityBoardItemInfo implements ScriptFile, ICommunityBoardHandler {
    private static CommunityBoardItemInfo _Instance = null;
    private static final NumberFormat pf = NumberFormat.getPercentInstance(Locale.ENGLISH);
    private static final NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
    private String val1 = "";
    private String val2 = "";
    private String val3 = "";
    private String val4 = "";

    static {
        pf.setMaximumFractionDigits(4);
        df.setMinimumFractionDigits(2);
    }

    public static CommunityBoardItemInfo getInstance() {
        if (_Instance == null) {
            _Instance = new CommunityBoardItemInfo();
        }
        return _Instance;
    }

    @Override
    public void onLoad() {
        CommunityBoardHandler.getInstance().registerHandler(this);
    }

    @Override
    public void onReload() {
        CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]
                {
                        "_bbsitemlist", "_bbsitematributes", "_bbsitemstats", "_bbsitemskills", "_bbsarmorinfolist", "_bbsweaponinfolist", "_bbsiteminfolist", "_bbsarmorinfoid", "_bbsarmorinfoname", "_bbsweaponinfoid", "_bbsweaponinfoname", "_bbsiteminfoid", "_bbsiteminfoname"
                };
    }

    @Override
    public void onBypassCommand(Player activeChar, String command) {
        StringTokenizer st = new StringTokenizer(command, " ");
        String cmd = st.nextToken();
        val1 = "";
        val2 = "";
        val3 = "";
        val4 = "";

        if (st.countTokens() == 1) {
            val1 = st.nextToken();
        } else if (st.countTokens() == 2) {
            val1 = st.nextToken();
            val2 = st.nextToken();
        } else if (st.countTokens() == 3) {
            val1 = st.nextToken();
            val2 = st.nextToken();
            val3 = st.nextToken();
        } else if (st.countTokens() == 4) {
            val1 = st.nextToken();
            val2 = st.nextToken();
            val3 = st.nextToken();
            val4 = st.nextToken();
        }

        if (cmd.equalsIgnoreCase("_bbsitemlist")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/list.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsarmorinfolist")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/armorlist.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsweaponinfolist")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/weaponlist.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsiteminfolist")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/itemlist.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsarmorinfoid")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            content = content.replace("%iteminfo%", generateArmorInfo(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsarmorinfoname")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            String str = null;
            if (!val1.equals("")) {
                str = val1;
            }

            if (!val2.equals("")) {
                str = val1 + " " + val2;
            }

            if (!val3.equals("")) {
                str = val1 + " " + val2 + " " + val3;
            }

            if (!val4.equals("")) {
                str = val1 + " " + val2 + " " + val3 + " " + val4;
            }

            content = content.replace("%iteminfo%", generateArmorInfo(activeChar, str));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsweaponinfoid")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            content = content.replace("%iteminfo%", generateWeaponInfo(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsweaponinfoname")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            String str = null;

            if (!val1.equals("")) {
                str = val1;
            }

            if (!val2.equals("")) {
                str = val1 + " " + val2;
            }

            if (!val3.equals("")) {
                str = val1 + " " + val2 + " " + val3;
            }

            if (!val4.equals("")) {
                str = val1 + " " + val2 + " " + val3 + " " + val4;
            }

            content = content.replace("%iteminfo%", generateWeaponInfo(activeChar, str));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsiteminfoid")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            content = content.replace("%iteminfo%", generateItemInfo(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsiteminfoname")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            String str = null;

            if (!val1.equals("")) {
                str = val1;
            }

            if (!val2.equals("")) {
                str = val1 + " " + val2;
            }

            if (!val3.equals("")) {
                str = val1 + " " + val2 + " " + val3;
            }

            if (!val4.equals("")) {
                str = val1 + " " + val2 + " " + val3 + " " + val4;
            }

            content = content.replace("%iteminfo%", generateItemInfo(activeChar, str));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsitemskills")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            content = content.replace("%iteminfo%", generateItemSkills(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsitemstats")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            content = content.replace("%iteminfo%", generateItemStats(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsitematributes")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/iteminfo/iteminfo.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            content = content.replace("%iteminfo%", generateItemAttribute(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        }
    }

    private String generateItemSkills(Player player, int id) {
        StringBuilder result = new StringBuilder();

        result.append("<table width=400 border=0>");
        result.append("<tr><td><img src=\"L2UI.SquareBlank\" width=400 height=10> </td></tr>");
        if (player.getLanguage() == Language.RUSSIAN) {
            result.append("<tr><td><table width=400 border=0><tr><td><font color=\"aaccff\">Список скиллов:</font></td></tr></table></td></tr>");
        } else {
            result.append("<tr><td><table width=400 border=0><tr><td><font color=\"aaccff\">List skills:</font></td></tr></table></td></tr>");
        }
        result.append("<tr><td><img src=\"L2UI.SquareWhite\" width=400 height=1> </td></tr>");
        result.append("<tr><td><img src=\"L2UI.SquareBlank\" width=400 height=10> </td></tr>");
        result.append("<tr><td><table>");

        ItemTemplate temp = ItemHolder.getInstance().getTemplate(id);

        String str;
        if (temp.isWeapon()) {
            str = "_bbsweaponinfoid";
        } else if (temp.isArmor() || temp.isAccessory()) {
            str = "_bbsarmorinfoid";
        } else {
            str = "_bbsiteminfoid";
        }

        for (Skill skill : temp.getAttachedSkills()) {
            result.append("<tr><td><font color=\"0000FF\">").append(skill.getName()).append("&nbsp;").append("</font></td></tr><br>");
        }

        result.append("<tr><td><button value=\"");
        result.append(player.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
        result.append("\" action=\"bypass ").append(str).append(" ").append(temp.getItemId()).append("\" width=300 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>\n");
        result.append("</table></td></tr>");
        result.append("</table>");
        return result.toString();
    }

    private String generateItemStats(Player player, int id) {
        StringBuilder result = new StringBuilder();

        result.append("<table width=400 border=0>");
        result.append("<tr><td><img src=\"L2UI.SquareBlank\" width=400 height=10> </td></tr>");
        if (player.getLanguage() == Language.RUSSIAN) {
            result.append("<tr><td><table width=400 border=0><tr><td><font color=\"aaccff\">Список бонусов:</font></td></tr></table></td></tr>");
        } else {
            result.append("<tr><td><table width=400 border=0><tr><td><font color=\"aaccff\">List bonuses:</font></td></tr></table></td></tr>");
        }
        result.append("<tr><td><img src=\"L2UI.SquareWhite\" width=400 height=1> </td></tr>");
        result.append("<tr><td><img src=\"L2UI.SquareBlank\" width=400 height=10> </td></tr>");
        result.append("<tr><td><table>");

        ItemTemplate temp = ItemHolder.getInstance().getTemplate(id);

        String str;
        if (temp.isWeapon()) {
            str = "_bbsweaponinfoid";
        } else if (temp.isArmor() || temp.isAccessory()) {
            str = "_bbsarmorinfoid";
        } else {
            str = "_bbsiteminfoid";
        }

        for (FuncTemplate func : temp.getAttachedFuncs()) {
            if (getFunc(player, func) != null) {
                result.append("<tr><td><font color=\"33FFFF\">").append(getFunc(player, func)).append("&nbsp;").append("</font></td></tr><br>");
            }
        }

        result.append("<tr><td><button value=\"");
        result.append(player.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
        result.append("\" action=\"bypass ").append(str).append(" ").append(temp.getItemId()).append("\" width=300 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>\n");
        result.append("</table></td></tr>");
        result.append("</table>");
        return result.toString();
    }

    private String generateItemAttribute(Player player, int id) {
        StringBuilder result = new StringBuilder();

        ItemTemplate temp = ItemHolder.getInstance().getTemplate(id);

        String str;
        if (temp.isWeapon()) {
            str = "_bbsweaponinfoid";
        } else if (temp.isArmor() || temp.isAccessory()) {
            str = "_bbsarmorinfoid";
        } else {
            str = "_bbsiteminfoid";
        }

        if (temp.getBaseAttributeValue(Element.FIRE) > 0) {
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"icon.etc_fire_stone_i00\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Атрибут Огня</font> " : "Attributes Fire</font>").append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "Бонус атрибута:</font> " : "Attributes bonuses:</font> ").append(temp.getBaseAttributeValue(Element.FIRE)).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        if (temp.getBaseAttributeValue(Element.WATER) > 0) {
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"icon.etc_water_stone_i00\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Атрибут Воды</font> " : "Attributes Water</font>").append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "Бонус атрибута:</font> " : "Attributes bonuses:</font> ").append(temp.getBaseAttributeValue(Element.WATER)).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        if (temp.getBaseAttributeValue(Element.WIND) > 0) {
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"icon.etc_wind_stone_i00\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Атрибут Ветра</font> " : "Attributes Wind</font>").append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "Бонус атрибута:</font> " : "Attributes bonuses:</font> ").append(temp.getBaseAttributeValue(Element.WIND)).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        if (temp.getBaseAttributeValue(Element.EARTH) > 0) {
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"icon.etc_earth_stone_i00\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Атрибут Земли</font> " : "Attributes Earth</font>").append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "Бонус атрибута:</font> " : "Attributes bonuses:</font> ").append(temp.getBaseAttributeValue(Element.EARTH)).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        if (temp.getBaseAttributeValue(Element.HOLY) > 0) {
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"icon.etc_holy_stone_i00\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Атрибут Света</font> " : "Attributes Light</font>").append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "Бонус атрибута:</font> " : "Attributes bonuses:</font> ").append(temp.getBaseAttributeValue(Element.HOLY)).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        if (temp.getBaseAttributeValue(Element.UNHOLY) > 0) {
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"icon.etc_unholy_stone_i00\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Атрибут Темноты</font> " : "Attributes Dark</font>").append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "Бонус атрибута:</font> " : "Attributes bonuses:</font> ").append(temp.getBaseAttributeValue(Element.UNHOLY)).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        result.append("<center><table width=690>");
        result.append("<tr>");
        result.append("<td WIDTH=690 align=center valign=top>");
        result.append("<center><br><br><button value=\"");
        result.append(player.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
        result.append("\" action=\"bypass ").append(str).append(" ").append(temp.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
        result.append("</td>");
        result.append("</tr>");
        result.append("</table></center>");

        return result.toString();
    }

    private String generateItemInfo(Player player, String name) {
        StringBuilder result = new StringBuilder();

        for (ItemTemplate temp : ItemHolder.getInstance().getAllTemplates()) {
            if ((temp != null) && !temp.isArmor() && !temp.isWeapon() && !temp.isAccessory() && ((temp.getName() == name) || val2.equals("") ? temp.getName().startsWith(name) : temp.getName().contains(name) || temp.getName().equals(name) || temp.getName().equalsIgnoreCase(name))) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(temp.getName());
                result.append("\" action=\"bypass _bbsiteminfoid ").append(temp.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }
        }

        return result.toString();
    }

    private String generateItemInfo(Player player, int id) {
        StringBuilder result = new StringBuilder();

        ItemTemplate temp = ItemHolder.getInstance().getTemplate(id);
        if ((temp != null) && !temp.isArmor() && !temp.isWeapon() && !temp.isAccessory()) {
            EtcItemTemplate etcitem = (EtcItemTemplate) temp;
            String icon = etcitem.getIcon();
            if ((icon == null) || icon.equals(StringUtils.EMPTY)) {
                icon = "icon.etc_question_mark_i00";
            }

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(etcitem.getItemId())).append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "ID предмета:</font> " : "Item ID:</font> ").append(etcitem.getItemId()).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<br><table width=690>");
            result.append("<tr>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Тип предмета: " : "Item type: ").append(etcitem.getItemType().toString()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Вес: " : "Weight: ").append(etcitem.getWeight()).append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Цена продажи в магазин: " : "Sale price to the store: ").append(etcitem.getReferencePrice() / 2).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Будет стыковаться: " : "It will be docked: ").append(etcitem.isStackable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Временный предмет: " : "A temporary items: ")
                    .append(etcitem.getDurability() > 0 ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно выбросить: " : "You can throw: ").append(etcitem.isDropable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно продать: " : "Can be sold: ").append(etcitem.isSellable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно обменять: " : "Can be exchanged: ").append(etcitem.isStoreable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            if (etcitem.getAttachedSkills().length > 0) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список скиллов" : "List skills");
                result.append("\" action=\"bypass _bbsitemskills ").append(etcitem.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }
        } else {
            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Предмет не найден</font></center></td></tr></table><br>"
                    : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Item not found</font></center></td></tr></table><br>");
        }

        return result.toString();
    }

    private String generateWeaponInfo(Player player, String name) {
        StringBuilder result = new StringBuilder();

        for (ItemTemplate temp : ItemHolder.getInstance().getAllTemplates()) {
            if ((temp != null) && temp.isWeapon() && ((temp.getName() == name) || val2.equals("") ? temp.getName().startsWith(name) : temp.getName().contains(name) || temp.getName().equals(name) || temp.getName().equalsIgnoreCase(name))) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(temp.getName());
                result.append("\" action=\"bypass _bbsweaponinfoid ").append(temp.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }
        }

        return result.toString();
    }

    private String generateWeaponInfo(Player player, int id) {
        StringBuilder result = new StringBuilder();

        ItemTemplate temp = ItemHolder.getInstance().getTemplate(id);
        if ((temp != null) && temp.isWeapon()) {
            WeaponTemplate weapon = (WeaponTemplate) temp;
            String icon = weapon.getIcon();
            if ((icon == null) || icon.equals(StringUtils.EMPTY)) {
                icon = "icon.etc_question_mark_i00";
            }

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(weapon.getItemId())).append(" (<font color=\"b09979\">").append(weapon.getItemType().toString())
                    .append("</font>)<br1><font color=\"LEVEL\">").append(player.getLanguage() == Language.RUSSIAN ? "ID предмета:</font> " : "Item ID:</font> ").append(weapon.getItemId()).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<br><table width=690>");
            result.append("<tr>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Грейд оружия: " : "Weapon grade: ").append(weapon.getCrystalType()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Слот: " : "Slot: ").append(getBodyPart(player, weapon)).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Разбивается на кристаллы: " : "Divided into crystals: ")
                    .append(weapon.isCrystallizable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            if (weapon.isCrystallizable()) {
                result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Количество кристаллов: " : "Number of crystals: ").append(weapon.getCrystalCount()).append("&nbsp;").append("</font><br>");
            } else {
                result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Количество кристаллов: 0" : "Number of crystals: 0").append("&nbsp;").append("</font><br>");
            }
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Потребление спиритов: " : "Consume spiritshot: ").append(weapon.getSpiritShotCount()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Оружие камаелей: " : "Kamael weapons: ")
                    .append(weapon.getKamaelConvert() > 0 ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Вес: " : "Weight: ").append(weapon.getWeight()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Цена продажи в магазин: " : "Sale price to the store: ").append(weapon.getReferencePrice() / 2).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Будет стыковаться: " : "It will be docked: ").append(weapon.isStackable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Временный предмет: " : "A temporary item: ")
                    .append(weapon.getDurability() > 0 ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно выбросить: " : "You can throw: ").append(weapon.isDropable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Реюз Атаки: " : "Attack reuse: ").append(weapon.getAttackReuseDelay() / 1000).append(" сек.").append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно продать: " : "Can be sold: ").append(weapon.isSellable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно вставить аугментацию: " : "You can insert the argument: ")
                    .append(weapon.isAugmentable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно вставить атрибут: " : "You can insert an attribute: ")
                    .append(weapon.isAttributable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно обменять: " : "Can be exchanged: ").append(weapon.isStoreable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Потребление сосок: " : "Consume soulshot: ").append(weapon.getSoulShotCount()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Потребление МП: " : "Consume Mp: ").append(weapon.getMpConsume()).append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            if (weapon.getAttachedSkills().length > 0) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список скиллов" : "List skills");
                result.append("\" action=\"bypass _bbsitemskills ").append(weapon.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if (weapon.getAttachedFuncs().length > 0) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список бонусов" : "List bonuses");
                result.append("\" action=\"bypass _bbsitemstats ").append(weapon.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if ((weapon.getBaseAttributeValue(Element.FIRE) > 0) || (weapon.getBaseAttributeValue(Element.WATER) > 0) || (weapon.getBaseAttributeValue(Element.WIND) > 0) || (weapon.getBaseAttributeValue(Element.EARTH) > 0) || (weapon.getBaseAttributeValue(Element.HOLY) > 0)
                    || (weapon.getBaseAttributeValue(Element.UNHOLY) > 0)) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список Атрибутов" : "List attributes");
                result.append("\" action=\"bypass _bbsitematributes ").append(weapon.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }
        } else {
            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Предмет не найден</font></center></td></tr></table><br>"
                    : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Item not found</font></center></td></tr></table><br>");
        }

        return result.toString();
    }

    private String generateArmorInfo(Player player, String name) {
        StringBuilder result = new StringBuilder();

        for (ItemTemplate temp : ItemHolder.getInstance().getAllTemplates()) {
            if ((temp != null) && (temp.isArmor() || temp.isAccessory()) && ((temp.getName() == name) || val2.equals("") ? temp.getName().startsWith(name) : temp.getName().contains(name) || temp.getName().startsWith(name) || temp.getName().equals(name) || temp.getName().equalsIgnoreCase(name))) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(temp.getName());
                result.append("\" action=\"bypass _bbsarmorinfoid ").append(temp.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }
        }

        return result.toString();
    }

    private String generateArmorInfo(Player player, int id) {
        StringBuilder result = new StringBuilder();

        ItemTemplate temp = ItemHolder.getInstance().getTemplate(id);
        if ((temp != null) && (temp.isArmor() || temp.isAccessory())) {
            ArmorTemplate armor = (ArmorTemplate) temp;
            String icon = armor.getIcon();
            if ((icon == null) || icon.equals(StringUtils.EMPTY)) {
                icon = "icon.etc_question_mark_i00";
            }

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<table border=0 cellspacing=4 cellpadding=3>");
            result.append("<tr>");
            result.append("<td FIXWIDTH=50 align=right valign=top>");
            result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
            result.append("</td>");
            result.append("<td FIXWIDTH=671 align=left valign=top>");
            result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(armor.getItemId())).append("<br1><font color=\"LEVEL\">")
                    .append(player.getLanguage() == Language.RUSSIAN ? "ID предмета:</font> " : "Item ID:</font> ").append(armor.getItemId()).append("&nbsp;");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<table border=0 cellspacing=0 cellpadding=0>");
            result.append("<tr>");
            result.append("<td width=690>");
            result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");
            result.append("<br><table width=690>");
            result.append("<tr>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Тип доспехов: " : "Armor type: ").append(armor.getItemType().toString()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Грейд доспехов: " : "Armor grade: ").append(armor.getCrystalType()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Слот: " : "Slot: ").append(getBodyPart(player, armor)).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Разбивается на кристаллы: " : "Divided into crystals: ")
                    .append(armor.isCrystallizable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            if (armor.isCrystallizable()) {
                result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Количество кристаллов: " : "Number of crystals: ").append(armor.getCrystalCount()).append("&nbsp;").append("</font><br>");
            } else {
                result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Количество кристаллов: 0" : "Number of crystals: 0").append("&nbsp;").append("</font><br>");
            }
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Вес: " : "Weight: ").append(armor.getWeight()).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Цена продажи в магазин: " : "Sale price to the store: ").append(armor.getReferencePrice() / 2).append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Будет стыковаться: " : "It will be docked: ").append(armor.isStackable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Временный предмет: " : "A temporary item: ")
                    .append(armor.getDurability() > 0 ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно выбросить: " : "You can throw: ").append(armor.isDropable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("<td>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно продать: " : "Can be sold").append(armor.isSellable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;")
                    .append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно вставить аугментацию: " : "You can insert the argument: ")
                    .append(armor.isAugmentable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно вставить атрибут: " : "You can insert an attribute: ")
                    .append(armor.isAttributable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no").append("&nbsp;").append("</font><br>");
            result.append("› <font color=\"b09979\">").append(player.getLanguage() == Language.RUSSIAN ? "Можно обменять: " : "Can be exchanged: ").append(armor.isStoreable() ? player.getLanguage() == Language.RUSSIAN ? "да" : "yes" : player.getLanguage() == Language.RUSSIAN ? "нет" : "no")
                    .append("&nbsp;").append("</font><br>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            if (armor.getAttachedSkills().length > 0) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список скиллов" : "List skills");
                result.append("\" action=\"bypass _bbsitemskills ").append(armor.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if (armor.getAttachedFuncs().length > 0) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список бонусов" : "List bonuses");
                result.append("\" action=\"bypass _bbsitemstats ").append(armor.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if ((armor.getBaseAttributeValue(Element.FIRE) > 0) || (armor.getBaseAttributeValue(Element.WATER) > 0) || (armor.getBaseAttributeValue(Element.WIND) > 0) || (armor.getBaseAttributeValue(Element.EARTH) > 0) || (armor.getBaseAttributeValue(Element.HOLY) > 0)
                    || (armor.getBaseAttributeValue(Element.UNHOLY) > 0)) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(player.getLanguage() == Language.RUSSIAN ? "Список Атрибутов" : "List attributes");
                result.append("\" action=\"bypass _bbsitematributes ").append(armor.getItemId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }
        } else {
            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Предмет не найден</font></center></td></tr></table><br>"
                    : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Item not found</font></center></td></tr></table><br>");
        }

        return result.toString();
    }

    private String getFunc(Player player, FuncTemplate func) {
        if (func.getFunc(null) != null) {
            String str;
            Func f = func.getFunc(null);
            if (getStats(player, f) != null) {
                if (f instanceof FuncAdd) {
                    str = player.getLanguage() == Language.RUSSIAN ? "Увеличивает " : "Increases";
                    return str + getStats(player, f) + " на " + f.value;
                } else if (f instanceof FuncSet) {
                    str = player.getLanguage() == Language.RUSSIAN ? "Увеличивает " : "Sets";
                    return str + getStats(player, f) + " в " + f.value;
                } else if (f instanceof FuncSub) {
                    str = player.getLanguage() == Language.RUSSIAN ? "Увеличивает " : "Decreases";
                    return str + getStats(player, f) + " на " + f.value;
                } else if (f instanceof FuncMul) {
                    str = player.getLanguage() == Language.RUSSIAN ? "Увеличивает " : "Multiplies";
                    return str + getStats(player, f) + " на " + f.value;
                } else if (f instanceof FuncDiv) {
                    str = player.getLanguage() == Language.RUSSIAN ? "Увеличивает " : "Divides";
                    return str + getStats(player, f) + " на " + f.value;
                } else if (f instanceof FuncEnchant) {
                    str = player.getLanguage() == Language.RUSSIAN ? "Увеличивает " : "Increases in the sharpening";
                    return str + getStats(player, f) + " на " + f.value;
                }
            }
        }

        return "Не опознано";
    }

    private String getStats(Player player, Func f) {
        String str;
        if (f.stat == Stats.MAX_HP) {
            str = player.getLanguage() == Language.RUSSIAN ? "максимальное ХП" : "max HP";
            return str;
        } else if (f.stat == Stats.MAX_MP) {
            str = player.getLanguage() == Language.RUSSIAN ? "максимальное МП" : "max MP";
            return str;
        } else if (f.stat == Stats.MAX_CP) {
            str = player.getLanguage() == Language.RUSSIAN ? "максимальное СП" : " max CP";
            return str;
        } else if (f.stat == Stats.REGENERATE_HP_RATE) {
            str = player.getLanguage() == Language.RUSSIAN ? "регенерация ХП" : "regeneration HP";
            return str;
        } else if (f.stat == Stats.REGENERATE_CP_RATE) {
            str = player.getLanguage() == Language.RUSSIAN ? "регенерация СП" : "regeneration CP";
            return str;
        } else if (f.stat == Stats.REGENERATE_MP_RATE) {
            str = player.getLanguage() == Language.RUSSIAN ? "регенерация МП" : "regeneration MP";
            return str;
        } else if (f.stat == Stats.RUN_SPEED) {
            str = player.getLanguage() == Language.RUSSIAN ? "скорость" : "speed";
            return str;
        } else if (f.stat == Stats.PHYSICAL_DEFENCE) {
            str = player.getLanguage() == Language.RUSSIAN ? "физическую защиту" : "physical defence";
            return str;
        } else if (f.stat == Stats.MAGIC_DEFENCE) {
            str = player.getLanguage() == Language.RUSSIAN ? "магическую защиту" : "magical defence";
            return str;
        } else if (f.stat == Stats.PHYSICAL_ATTACK) {
            str = player.getLanguage() == Language.RUSSIAN ? "физическую атаку" : "physical attack";
            return str;
        } else if (f.stat == Stats.MAGIC_ATTACK) {
            str = player.getLanguage() == Language.RUSSIAN ? "магическую атаку" : "magical attack";
            return str;
        } else if ((f.stat == Stats.ATK_REUSE) || (f.stat == Stats.ATK_BASE)) {
            str = player.getLanguage() == Language.RUSSIAN ? "реюз атаку" : "reuse attack";
            return str;
        } else if (f.stat == Stats.PHYSICAL_EVASION_RATE) {
            str = player.getLanguage() == Language.RUSSIAN ? "точность" : "avoid";
            return str;
        } else if (f.stat == Stats.PHYSICAL_ACCURACY_COMBAT) {
            str = player.getLanguage() == Language.RUSSIAN ? "уклонение" : "evasion";
            return str;
        } else if (f.stat == Stats.PHYSICAL_CRIT_BASE) {
            str = player.getLanguage() == Language.RUSSIAN ? "шанс критического удара" : "crit";
            return str;
        } else if (f.stat == Stats.SHIELD_DEFENCE) {
            str = player.getLanguage() == Language.RUSSIAN ? "защиту щитом" : "defense shield";
            return str;
        } else if (f.stat == Stats.SHIELD_RATE) {
            str = player.getLanguage() == Language.RUSSIAN ? "шанс уклониться щитом" : "chance to avoid a shield";
            return str;
        } else if (f.stat == Stats.PHYSICAL_ATTACK_RANGE) {
            str = player.getLanguage() == Language.RUSSIAN ? "радиус физической атаки" : "reuse physical attack";
            return str;
        } else if (f.stat == Stats.STAT_STR) {
            str = player.getLanguage() == Language.RUSSIAN ? "СТР" : "STR";
            return str;
        } else if (f.stat == Stats.STAT_CON) {
            str = player.getLanguage() == Language.RUSSIAN ? "КОН" : "CON";
            return str;
        } else if (f.stat == Stats.STAT_DEX) {
            str = player.getLanguage() == Language.RUSSIAN ? "ДЕХ" : "DEX";
            return str;
        } else if (f.stat == Stats.STAT_INT) {
            str = player.getLanguage() == Language.RUSSIAN ? "ИНТ" : "INT";
            return str;
        } else if (f.stat == Stats.STAT_WIT) {
            str = player.getLanguage() == Language.RUSSIAN ? "ВИТ" : "WIT";
            return str;
        } else if (f.stat == Stats.STAT_MEN) {
            str = player.getLanguage() == Language.RUSSIAN ? "МЕН" : "MEN";
            return str;
        } else if (f.stat == Stats.MP_PHYSICAL_SKILL_CONSUME) {
            str = player.getLanguage() == Language.RUSSIAN ? "потребление мп физических скилов" : "mp consume physical skill";
            return str;
        }
        return player.getLanguage() == Language.RUSSIAN ? "Не опознано" : "Not recognized";
    }

    private String getBodyPart(Player player, ItemTemplate item) {
        if ((item.getBodyPart() == ItemTemplate.SLOT_R_EAR) || (item.getBodyPart() == ItemTemplate.SLOT_L_EAR)) {
            return player.getLanguage() == Language.RUSSIAN ? "Серьга" : "Earing";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_NECK) {
            return player.getLanguage() == Language.RUSSIAN ? "Ожерелье" : "Neclaces";
        } else if ((item.getBodyPart() == ItemTemplate.SLOT_R_FINGER) || (item.getBodyPart() == ItemTemplate.SLOT_L_FINGER)) {
            return player.getLanguage() == Language.RUSSIAN ? "Кольцо" : "Ring";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_HEAD) {
            return player.getLanguage() == Language.RUSSIAN ? "Шлем" : "Helmet";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_L_HAND) {
            return player.getLanguage() == Language.RUSSIAN ? "Щит" : "shield";
        } else if ((item.getBodyPart() == ItemTemplate.SLOT_R_HAND) || (item.getBodyPart() == ItemTemplate.SLOT_LR_HAND)) {
            return player.getLanguage() == Language.RUSSIAN ? "Оружие" : "Weapon";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_GLOVES) {
            return player.getLanguage() == Language.RUSSIAN ? "Перчатки" : "Gloves";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_CHEST) {
            return player.getLanguage() == Language.RUSSIAN ? "Куртка" : "Jacket";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_LEGS) {
            return player.getLanguage() == Language.RUSSIAN ? "Штаны" : "Pants";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_FEET) {
            return player.getLanguage() == Language.RUSSIAN ? "Ботинки" : "Boots";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_BACK) {
            return player.getLanguage() == Language.RUSSIAN ? "Клоак" : "Cloak";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR) {
            return player.getLanguage() == Language.RUSSIAN ? "Полная броня" : "Full armor";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_HAIR) {
            return player.getLanguage() == Language.RUSSIAN ? "Украшение" : "Decoration";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_FORMAL_WEAR) {
            return player.getLanguage() == Language.RUSSIAN ? "Костюм" : "Suit";
        } else if (item.getBodyPart() == ItemTemplate.SLOT_FORMAL_WEAR) {
            return player.getLanguage() == Language.RUSSIAN ? "Свадебное платье" : "Wedding Dress";
        } else if (item.isUnderwear()) {
            return player.getLanguage() == Language.RUSSIAN ? "Бельё" : "Linen";
        } else if (item.isBracelet()) {
            return player.getLanguage() == Language.RUSSIAN ? "Браслет" : "Bracelet";
        } else if (item.isTalisman()) {
            return player.getLanguage() == Language.RUSSIAN ? "Талисман" : "Talisman";
        } else if (item.isBelt()) {
            return player.getLanguage() == Language.RUSSIAN ? "Пояс" : "Belt";
        }
        return player.getLanguage() == Language.RUSSIAN ? "Не опознано" : "Not recognized";
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
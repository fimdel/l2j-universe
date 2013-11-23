/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import javolution.text.TextBuilder;
import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.base.PlayerClass;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.utils.HtmlUtils;
import l2p.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommunityBoardClassMaster extends Functions implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardClassMaster.class);

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: Stats service loaded.");
            CommunityBoardHandler.getInstance().registerHandler(this);
        }
    }

    @Override
    public void onReload() {
        if (Config.COMMUNITYBOARD_ENABLED)
            CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]{"_bbsclass", "_bbsclass;change_class;"};
    }

    @Override
    public void onBypassCommand(Player player, String command) {
        if (command.equals("_bbsclass"))
            showClassPage(player);
        else if (command.startsWith("_bbsclass;change_class;")) {
            StringTokenizer selectedClass = new StringTokenizer(command, ";");
            selectedClass.nextToken();
            selectedClass.nextToken();
            int classID = Integer.parseInt(selectedClass.nextToken());
            int classPrice = Integer.parseInt(selectedClass.nextToken());
            changeClass(player, classID, classPrice);
        } else if (command.startsWith("_bbsclass;sub_class;")) {
            StringTokenizer selectedSubClass = new StringTokenizer(command, ";");
            selectedSubClass.nextToken();
            selectedSubClass.nextToken();

            int classId = 0;
            int newClassId = 0;
            int intVal = 0;

            for (String id : command.substring(20, command.length()).split(";")) {
                if (intVal == 0) {
                    intVal = Integer.parseInt(id);
                    continue;
                }
                if (classId > 0) {
                    newClassId = Integer.parseInt(id);
                    continue;
                }
                classId = Integer.parseInt(id);
            }
            changeSubClass(player, intVal, classId, newClassId);
        } else if (player.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br></body></html>", player);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
    }

    private void showClassPage(Player player) {
        ClassId classId = player.getClassId();
        int jobLevel = classId.level();
        int level = player.getLevel();
        TextBuilder html = new TextBuilder("");
        html.append("<table width=755>");
        html.append("<tr>");
        html.append("<td WIDTH=20 align=left valign=top></td>");
        html.append("<td WIDTH=690 align=left valign=top><br><br>");
        html.append("<font color=LEVEL>»</font> Добро пожаловать " + player.getName() + ".");
        html.append("</td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td WIDTH=20 align=left valign=top></td>");
        html.append("<td WIDTH=690 align=left valign=top>");
        html.append("<font color=LEVEL>»</font> Ваша текущая професия <font color=LEVEL>" + player.getClassId().name().substring(0, 1).toUpperCase() + player.getClassId().name().substring(1) + "</font>.");
        html.append("</td>");
        html.append("</tr>");
        html.append("</table>");

        if (level >= 20 && jobLevel == 1 || level >= 40 && jobLevel == 2 || level >= 76 && jobLevel == 3) {
            ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM);

            for (ClassId cid : ClassId.VALUES) {
                if (cid == ClassId.INSPECTOR)
                    continue;
                if (cid.childOf(classId) && cid.level() == classId.level() + 1)
                    if (player.isLangRus()) {
                        html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_royal_membership_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                        html.append("<font color=\"0099FF\">" + cid.name().substring(0, 1).toUpperCase() + cid.name().substring(1) + ".</font>&nbsp;<br1>›&nbsp;Стоимость:&nbsp;");
                        html.append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]) + "&nbsp;" + item.getName() + "");
                        html.append("</td>");
                        html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Купить\" action=\"bypass _bbsclass;change_class;" + cid.getId() + ";" + Config.CLASS_MASTERS_PRICE_LIST[jobLevel] + "\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                        html.append("</tr></table>");
                    } else {
                        html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_royal_membership_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                        html.append("<font color=\"0099FF\">" + cid.name().substring(0, 1).toUpperCase() + cid.name().substring(1) + ".</font>&nbsp;<br1>›&nbsp;Cost:&nbsp;");
                        html.append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]) + "&nbsp;" + item.getName() + "");
                        html.append("</td>");
                        html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Buy\" action=\"bypass _bbsclass;change_class;" + cid.getId() + ";" + Config.CLASS_MASTERS_PRICE_LIST[jobLevel] + "\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                        html.append("</tr></table>");
                    }
            }
        } else
            switch (jobLevel) {
                case 1:
                    if (player.isLangRus()) {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Для того, чтобы сменить вашу профессию, вы должны достичь: <font color=F2C202>20-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Для активации сабклассов, вы должны достичь <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Чтобы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");
                    } else {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To change your profession you have to reach: <font color=F2C202>20 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To activate the subclass you have to reach <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To become a noblesse, you have to bleed to subclass <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");
                    }
                    break;
                case 2:
                    if (player.isLangRus()) {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Для того, чтобы сменить вашу профессию, вы должны достичь: <font color=F2C202>40-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Для активации сабклассов, вы должны достичь <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Чтобы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");
                    } else {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To change your profession you have to reach: <font color=F2C202>40 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To activate the subclass you have to reach <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To become a noblesse, you have to bleed to subclass <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");
                    }
                    break;
                case 3:
                    if (player.isLangRus()) {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Для того, чтобы сменить вашу профессию, вы должны достичь: <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Для активации сабклассов, вы должны достичь <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> Чтобы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");
                    } else {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To change your profession you have to reach: <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To activate the subclass you have to reach <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> To become a noblesse, you have to bleed to subclass <font color=F2C202>76 level</font>.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");
                    }
                    break;
                case 4:
                    if (player.isLangRus()) {
                        if (level >= 76) {
                            html.append("<table width=755>");
                            html.append("<tr>");
                            html.append("<td WIDTH=20 align=left valign=top></td>");
                            html.append("<td WIDTH=690 align=left valign=top>");
                            html.append("<font color=LEVEL>»</font> Вы достигли <font color=F2C202>76-го уровня</font> активация сабклассов теперь доступна.");
                            html.append("</td>");
                            html.append("</tr>");
                            html.append("</table>");

                            if (!player.isNoble() && player.getSubLevel() < 75) {
                                html.append("<table width=755>");
                                html.append("<tr>");
                                html.append("<td WIDTH=20 align=left valign=top></td>");
                                html.append("<td WIDTH=690 align=left valign=top>");
                                html.append("<font color=LEVEL>»</font> Вы можете получить дворянство только после того как ваш саб-класс достигнет 76-го уровня.");
                                html.append("</td>");
                                html.append("</tr>");
                                html.append("</table>");
                            } else if (!player.isNoble() && player.getSubLevel() > 75) {
                                html.append("<table width=755>");
                                html.append("<tr>");
                                html.append("<td WIDTH=20 align=left valign=top></td>");
                                html.append("<td WIDTH=690 align=left valign=top>");
                                html.append("<font color=LEVEL>»</font> Вы можете получить дворянство. Ваш саб-класс достиг 76-го уровня.");
                                html.append("</td>");
                                html.append("</tr>");
                                html.append("</table>");
                            } else if (player.isNoble()) {
                                html.append("<table width=755>");
                                html.append("<tr>");
                                html.append("<td WIDTH=20 align=left valign=top></td>");
                                html.append("<td WIDTH=690 align=left valign=top>");
                                html.append("<font color=LEVEL>»</font> Вы уже дворянин. Получение дворянства более не доступно.");
                                html.append("</td>");
                                html.append("</tr>");
                                html.append("</table>");
                            }
                            ItemTemplate itemName = ItemHolder.getInstance().getTemplate(Config.BBS_CLASS_MASTER_PRICE_ITEM);
                            html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_quest_subclass_reward_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                            html.append("<font color=\"0099FF\">Добавить саб-класс.</font>&nbsp;<br1>›&nbsp;Стоимость:&nbsp;" + Config.BBS_CLASS_MASTER_PRICE_COUNT + "&nbsp;" + itemName.getName() + ".</td>");
                            html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Добавить\" action=\"bypass _bbsclass;sub_class;1\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                            html.append("</tr></table>");
                            html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_quest_subclass_reward_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                            html.append("<font color=\"0099FF\">Изменить саб-класс.</font>&nbsp;<br1>›&nbsp;Стоимость:&nbsp;" + Config.BBS_CLASS_MASTER_PRICE_COUNT + "&nbsp;" + itemName.getName() + ".</td>");
                            html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Изменить\" action=\"bypass _bbsclass;sub_class;2\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                            html.append("</tr></table>");
                            html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_quest_subclass_reward_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                            html.append("<font color=\"0099FF\">Отменить саб-класс.</font>&nbsp;<br1>›&nbsp;Стоимость:&nbsp;" + Config.BBS_CLASS_MASTER_PRICE_COUNT + "&nbsp;" + itemName.getName() + ".</td>");
                            html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Отменить\" action=\"bypass _bbsclass;sub_class;3\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                            html.append("</tr></table>");
                        }
                    } else if (level >= 76) {
                        html.append("<table width=755>");
                        html.append("<tr>");
                        html.append("<td WIDTH=20 align=left valign=top></td>");
                        html.append("<td WIDTH=690 align=left valign=top>");
                        html.append("<font color=LEVEL>»</font> You have reached the <font color=F2C202>level 75</font> activation of the subclass is now available.");
                        html.append("</td>");
                        html.append("</tr>");
                        html.append("</table>");

                        if (!player.isNoble() && player.getSubLevel() < 75) {
                            html.append("<table width=755>");
                            html.append("<tr>");
                            html.append("<td WIDTH=20 align=left valign=top></td>");
                            html.append("<td WIDTH=690 align=left valign=top>");
                            html.append("<font color=LEVEL>»</font> You can get the noblesse only after your sub-class reaches the 76 level.");
                            html.append("</td>");
                            html.append("</tr>");
                            html.append("</table>");
                        } else if (!player.isNoble() && player.getSubLevel() > 75) {
                            html.append("<table width=755>");
                            html.append("<tr>");
                            html.append("<td WIDTH=20 align=left valign=top></td>");
                            html.append("<td WIDTH=690 align=left valign=top>");
                            html.append("<font color=LEVEL>»</font> You can get the noblesse. Your sub-class has reached the 76th level.");
                            html.append("</td>");
                            html.append("</tr>");
                            html.append("</table>");
                        } else if (player.isNoble()) {
                            html.append("<table width=755>");
                            html.append("<tr>");
                            html.append("<td WIDTH=20 align=left valign=top></td>");
                            html.append("<td WIDTH=690 align=left valign=top>");
                            html.append("<font color=LEVEL>»</font> You have a noblesse. Getting the noblesse no longer available.");
                            html.append("</td>");
                            html.append("</tr>");
                            html.append("</table>");
                        }
                        ItemTemplate itemName = ItemHolder.getInstance().getTemplate(Config.BBS_CLASS_MASTER_PRICE_ITEM);
                        html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_quest_subclass_reward_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                        html.append("<font color=\"0099FF\">Add sub-class.</font>&nbsp;<br1>›&nbsp;Cost:&nbsp;" + Config.BBS_CLASS_MASTER_PRICE_COUNT + "&nbsp;" + itemName.getName() + ".</td>");
                        html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Add\" action=\"bypass _bbsclass;sub_class;1\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                        html.append("</tr></table>");
                        html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_quest_subclass_reward_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                        html.append("<font color=\"0099FF\">Change sub-class.</font>&nbsp;<br1>›&nbsp;Cost:&nbsp;" + Config.BBS_CLASS_MASTER_PRICE_COUNT + "&nbsp;" + itemName.getName() + ".</td>");
                        html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Change\" action=\"bypass _bbsclass;sub_class;2\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                        html.append("</tr></table>");
                        html.append("<table border=0 cellspacing=0 cellpadding=0><tr><td width=720><img src=\"l2ui.squaregray\" width=\"720\" height=\"1\"></td></tr></table><table border=0 cellspacing=4 cellpadding=3><tr><td FIXWIDTH=50 align=right valign=top><img src=\"icon.etc_quest_subclass_reward_i00\" width=32 height=32></td><td FIXWIDTH=576 align=left valign=top>");
                        html.append("<font color=\"0099FF\">Delete sub-class.</font>&nbsp;<br1>›&nbsp;Cost:&nbsp;" + Config.BBS_CLASS_MASTER_PRICE_COUNT + "&nbsp;" + itemName.getName() + ".</td>");
                        html.append("<td FIXWIDTH=95 align=center valign=top><button value=\"Delete\" action=\"bypass _bbsclass;sub_class;3\" back=\"l2ui_ct1.button.button_df_small_down\" fore=\"l2ui_ct1.button.button_df_small\" width=\"80\" height=\"25\"/></td>");
                        html.append("</tr></table>");
                    }
                    break;
            }

        String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/services/classmanager.htm", player);
        content = content.replace("%classmaster%", html.toString());
        content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
        content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
        content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        ShowBoard.separateAndSend(content, player);
    }

    private void changeClass(Player player, int classID, long classPrice) {
        if (player.getClassId().level() == 3)
            player.sendPacket(Msg.YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS);
        else
            player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);

        if (player.getInventory().destroyItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM, classPrice)) {
            player.setClassId(classID, false);
            player.broadcastUserInfo(true);
            showClassPage(player);
        } else if (Config.CLASS_MASTERS_PRICE_ITEM == 57)
            player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
        else
            player.sendPacket(new SystemMessage(SystemMessage.INCORRECT_ITEM_COUNT));
    }

    private void changeSubClass(Player player, int intVal, int classId, int newClassId) {
        StringBuilder html = new StringBuilder("<html noscrollbar><title>Класс Мастер</title><body><table border=0 cellpadding=0 cellspacing=0 width=292 height=358 background=\"l2ui_ct1.Windows_DF_TooltipBG\"><tr><td valign=\"top\" align=\"center\">");

        if (player.getPet() != null) {
            player.sendPacket(SystemMsg.A_SUBCLASS_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SERVITOR_OR_PET_IS_SUMMONED);
            return;
        }

        if (player.isActionsDisabled() || player.getTransformation() != 0) {
            player.sendPacket(SystemMsg.SUBCLASSES_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SKILL_IS_IN_USE);
            return;
        }

        if (player.getWeightPenalty() >= 3) {
            player.sendPacket(SystemMsg.A_SUBCLASS_CANNOT_BE_CREATED_OR_CHANGED_WHILE_YOU_ARE_OVER_YOUR_WEIGHT_LIMIT);
            return;
        }

        if (player.getInventoryLimit() * 0.8 < player.getInventory().getSize()) {
            player.sendPacket(SystemMsg.A_SUBCLASS_CANNOT_BE_CREATED_OR_CHANGED_BECAUSE_YOU_HAVE_EXCEEDED_YOUR_INVENTORY_LIMIT);
            return;
        }
        Collection<SubClass> playerClassList = player.getSubClassList().values();
        //Map<Integer, SubClass> playerClassList = player.getSubClasses();
        Set<ClassId> subsAvailable;

        if (player.getLevel() < 40) {
            String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/services/classmanager.htm", player);
            content = content.replace("%classmaster%", "You must be level 40 or more to operate with your sub-classes.");
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;
        }

        switch (intVal) {
            case 1: // Возвращает список сабов, которые можно взять (см case 4)
                subsAvailable = getAvailableSubClasses(player, true);

                if (subsAvailable != null && !subsAvailable.isEmpty()) {
                    html.append("<center><br><br><font name=\"hs12\">Доступные саб-классы</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");

                    for (ClassId subClass : subsAvailable)
                        html.append("<button value=\"" + formatClassForDisplay(subClass) + "\" action=\"bypass _bbsclass;sub_class;4;" + subClass.ordinal() + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_Info_Down fore=L2UI_CT1.OlympiadWnd_DF_Info><br1>");
                    html.append("</center></td></tr></table></body></html>");
                    showClassPage(player);
                    show(html.toString(), player);
                }
                break;
            case 2: // Установка уже взятого саба (см case 5)
                html.append("<center><br><br><font name=\"hs12\">Переключить саб-класс</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");

                final int baseClassId = player.getBaseClassId();

                if (playerClassList.size() < 2)
                    html.append("У вас нет саб-классов для переключения, но вы можете добавить его прямо сейчас<br><a action=\"bypass _bbsclass;sub_class;1\">Добавить сабкласс</a>");
                else {
                    html.append("Какой саб-класс вы желаете использовать?<br>");

                    if (baseClassId == player.getActiveClassId())
                        html.append("<button value=\"" + HtmlUtils.htmlClassNameNonClient(baseClassId) + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_Watch_Down fore=L2UI_CT1.OlympiadWnd_DF_Watch><br1>");
                    else
                        html.append("<button value=\"" + HtmlUtils.htmlClassNameNonClient(baseClassId) + " (Базовый)" + "\" action=\"bypass _bbsclass;sub_class;5;" + baseClassId + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm_Down fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm><br1>");

                    for (SubClass subClass : playerClassList) {
                        if (subClass.isBase())
                            continue;
                        int subClassId = subClass.getClassId();

                        if (subClassId == player.getActiveClassId())
                            html.append("<button value=\"" + HtmlUtils.htmlClassNameNonClient(subClassId) + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_Watch_Down fore=L2UI_CT1.OlympiadWnd_DF_Watch><br1>");
                        else
                            html.append("<button value=\"" + HtmlUtils.htmlClassNameNonClient(subClassId) + "\" action=\"bypass _bbsclass;sub_class;5;" + subClassId + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_Info_Down fore=L2UI_CT1.OlympiadWnd_DF_Info><br1>");
                    }
                }
                html.append("</center></td></tr></table></body></html>");
                showClassPage(player);
                show(html.toString(), player);
                break;
            case 3: // Отмена сабкласса - список имеющихся (см case 6)
                html.append("<center><br><br><font name=\"hs12\">Отмена саб-класса</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                html.append("<br>Какой из имеющихся сабов вы хотете заменить?<br>");

                for (SubClass sub : playerClassList)
                    if (!sub.isBase())
                        html.append("<button value=\"" + HtmlUtils.htmlClassNameNonClient(sub.getClassId()) + "\" action=\"bypass _bbsclass;sub_class;6;" + sub.getClassId() + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_Info_Down fore=L2UI_CT1.OlympiadWnd_DF_Info><br1>");

                html.append("<br>Если вы смените саб-класс вы станете<br1> 40 уровнем со второй профессией");
                html.append("</center></td></tr></table></body></html>");
                showClassPage(player);
                show(html.toString(), player);
                break;
            case 4: // Добавление сабкласса - обработка выбора из case 1
                boolean allowAddition = true;

                // Проверка хватает ли уровня
                if (player.getLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS) {
                    player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.NoSubBeforeLevel", player).addNumber(Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS));
                    allowAddition = false;
                }

                if (!playerClassList.isEmpty())
                    for (SubClass subClass : playerClassList)
                        if (subClass.getLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS) {
                            player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.NoSubBeforeLevel", player).addNumber(Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS));
                            allowAddition = false;
                            break;
                        }

                if (Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player)) {
                    player.sendPacket(Msg.YOU_CANT_JOIN_THE_OLYMPIAD_WITH_A_SUB_JOB_CHARACTER);
                    return;
                }

                if (!Config.ALT_GAME_SUBCLASS_WITHOUT_QUESTS && !playerClassList.isEmpty() && playerClassList.size() < 2 + Config.ALT_GAME_SUB_ADD)
                    if (player.isQuestCompleted("_234_FatesWhisper")) {
                        if (player.getRace() == Race.kamael) {
                            allowAddition = player.isQuestCompleted("_236_SeedsOfChaos");
                            if (!allowAddition)
                                player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.QuestSeedsOfChaos", player));
                        } else {
                            allowAddition = player.isQuestCompleted("_235_MimirsElixir");
                            if (!allowAddition)
                                player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.QuestMimirsElixir", player));
                        }
                    } else {
                        player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.QuestFatesWhisper", player));
                        allowAddition = false;
                    }

                if (allowAddition) {
                    if (!player.getInventory().destroyItemByItemId(Config.BBS_CLASS_MASTER_PRICE_ITEM, Config.BBS_CLASS_MASTER_PRICE_COUNT)) {
                        if (Config.BBS_CLASS_MASTER_PRICE_ITEM == 57) {
                            player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                            html.append("<center><br><br><font name=\"hs12\" color=\"FF0000\">Ошибка:</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                            html.append("<br><br>Недостаточно средств.<br>Для активации сабклассов вы должны иметь:<br1>");
                            html.append(ItemHolder.getInstance().getTemplate(Config.BBS_CLASS_MASTER_PRICE_ITEM).getName()).append(" в количестве: ").append(String.valueOf(Config.BBS_CLASS_MASTER_PRICE_COUNT));
                        } else {
                            player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
                            html.append("<center><br><br><font name=\"hs12\" color=\"FF0000\">Ошибка:</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                            html.append("<br><br>Недостаточно средств.<br>Для активации сабклассов вы должны иметь:<br1>");
                            html.append(ItemHolder.getInstance().getTemplate(Config.BBS_CLASS_MASTER_PRICE_ITEM).getName()).append(" в количестве: ").append(String.valueOf(Config.BBS_CLASS_MASTER_PRICE_COUNT));
                        }
                    } else if (!player.addSubClass(classId, true, 0)) {
                        html.append("<center><br><br><font name=\"hs12\" color=\"FF0000\">Ошибка</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                        html.append("<br><br>У вас максимальное количество сабклассов.");
                    } else {
                        html.append("<center><br><br><font name=\"hs12\" color=\"LEVEL\">Поздравляем</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                        html.append("<br><br>Саб-класс <font color=\"LEVEL\">" + HtmlUtils.htmlClassName(classId) + "</font> успешно добавлен!");
                        player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
                        player.sendMessage(player.isLangRus() ? "Исчезло " + String.valueOf(Config.BBS_CLASS_MASTER_PRICE_COUNT) + " " + ItemHolder.getInstance().getTemplate(Config.BBS_CLASS_MASTER_PRICE_ITEM).getName() : String.valueOf(Config.BBS_CLASS_MASTER_PRICE_COUNT) + " " + ItemHolder.getInstance().getTemplate(Config.BBS_CLASS_MASTER_PRICE_ITEM).getName() + " disappeared");
                    }
                } else {
                    html.append("<center><br><br><font name=\"hs12\" color=\"FF0000\">Ошибка:</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                    html.append("<br><br>Вы не можете добавить подкласс в данный момент.<br>Для активации сабклассов вы должны достичь<br1> <font color=F2C202>76-го уровня</font><br>");
                }
                html.append("</body></html>");
                html.append("</center></td></tr></table></body></html>");
                showClassPage(player);
                show(html.toString(), player);
                break;
            case 5: // Смена саба на другой из уже взятых - обработка выбора из case 2
                if (Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player)) {
                    player.sendPacket(Msg.YOU_CANT_JOIN_THE_OLYMPIAD_WITH_A_SUB_JOB_CHARACTER);
                    return;
                }

                if (player.isDead() || player.isAlikeDead() || player.isCastingNow() || player.isAttackingNow()) {
                    player.sendMessage(player.isLangRus() ? "Сменить саб-класс в вашем состоянии невозможно" : "You can`t change sub-class in this condition");
                    return;
                }

                if (player.isInCombat()) {
                    player.sendMessage(player.isLangRus() ? "Сменить саб-класс в боевом режиме нельзя" : "You can`t change sub-class in fight mode");
                    return;
                }

                if (player.isInZone(Zone.ZoneType.battle_zone) || player.isInZone(Zone.ZoneType.no_escape) || player.isInZone(Zone.ZoneType.epic) || player.isInZone(Zone.ZoneType.SIEGE) || player.isInZone(Zone.ZoneType.RESIDENCE) || player.getVar("jailed") != null) {
                    player.sendMessage(player.isLangRus() ? "Нельзя сменить саб-класс в данной локации" : "You can`t change sub-class in this location");
                    return;
                }

                if (player.getActiveWeaponFlagAttachment() != null) {
                    player.sendMessage(player.isLangRus() ? "Сменить саб-класс со Знаменем невозможно" : "You can`t change sub-class with handing the flag");
                    return;
                }

                if (player.isInOlympiadMode()) {
                    player.sendMessage(player.isLangRus() ? "Во время Олимпиады сменить саб-класс невозможно" : "You can`t change sub-class during the Olympiad running");
                    return;
                }

                if (player.getReflection() != ReflectionManager.DEFAULT) {
                    player.sendMessage(player.isLangRus() ? "Вы не можете сменить саб-класс, находясь во временной зоне" : "You can`t change sub-class being in time zone");
                    return;
                }

                if (player.isInDuel()) {
                    player.sendMessage(player.isLangRus() ? "Во время дуэли сменить саб-класс невозможно" : "You can`t change sub-class during a duel");
                    return;
                }

                if (player.isInCombat() || player.getPvpFlag() != 0) {
                    player.sendMessage(player.isLangRus() ? "Во время боя сменить саб-класс невозможно" : "You can`t change sub-class during the fight");
                    return;
                }

                if (player.isOnSiegeField() || player.isInZoneBattle()) {
                    player.sendMessage(player.isLangRus() ? "Во время полномасштабных сражений - осад крепостей, замков, холлов клана, сменить саб-класс невозможно" : "You can`t change sub-class in siege battle");
                    return;
                }

                if (player.isFlying()) {
                    player.sendMessage(player.isLangRus() ? "Во время полета сменить саб-класс невозможно" : "You can`t change sub-class during the flight");
                    return;
                }

                if (player.isInWater() || player.isInBoat()) {
                    player.sendMessage(player.isLangRus() ? "Вы не можете сменить саб-класс, находясь в воде" : "You can`t change sub-class being in water");
                    return;
                }

                player.setActiveSubClass(classId, true);

                html.append("<br>Ваш активный саб-класс теперь: <font color=\"LEVEL\">" + HtmlUtils.htmlClassName(player.getActiveClassId()) + "</font>.");
                html.append("</center></td></tr></table></body></html>");
                showClassPage(player);
                show(html.toString(), player);
                player.sendPacket(SystemMsg.YOU_HAVE_SUCCESSFULLY_SWITCHED_TO_YOUR_SUBCLASS);
                break;
            case 6: // Отмена сабкласса - обработка выбора из case 3
                html.append("<center><br><br><font name=\"hs12\">Выберите саб-класс</font><br1><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br1>");
                html.append("<br>*Внимание! * <br1>Все скилы для этого саб-класса будут удалены.<br>");

                subsAvailable = getAvailableSubClasses(player, false);

                if (!subsAvailable.isEmpty())
                    for (ClassId subClass : subsAvailable)
                        html.append("<button value=\"" + HtmlUtils.htmlClassNameNonClient(subClass.ordinal()) + "\" action=\"bypass _bbsclass;sub_class;7;" + classId + ";" + subClass.ordinal() + "\" width=200 height=29 back=L2UI_CT1.OlympiadWnd_DF_Info_Down fore=L2UI_CT1.OlympiadWnd_DF_Info><br1>");
                else {
                    player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.NoSubAtThisTime", player));
                    return;
                }
                html.append("</center></td></tr></table></body></html>");
                showClassPage(player);
                show(html.toString(), player);
                break;
            case 7: // Отмена сабкласса - обработка выбора из case 6
                if (Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player)) {
                    player.sendPacket(Msg.YOU_CANT_JOIN_THE_OLYMPIAD_WITH_A_SUB_JOB_CHARACTER);
                    return;
                }

                if (player.modifySubClass(classId, newClassId)) {

                    html.append("<br>Ваш саб-класс изменен на: <font color=\"LEVEL\">" + HtmlUtils.htmlClassName(newClassId) + "</font>.");
                    player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
                } else {
                    player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.SubclassCouldNotBeAdded", player));
                    return;
                }
                html.append("</center></td></tr></table></body></html>");
                showClassPage(player);
                show(html.toString(), player);
                break;
        }
    }

    public static Set<ClassId> getAvailableSubClasses(Player player, boolean isNew) {
        Set<ClassId> availSubs = null;
        Set availSubs1 = null;
        Race race = player.getRace();
        if (race == Race.kamael) {
            availSubs = EnumSet.copyOf(PlayerClass.KAMAEL_SUBCLASS_SET);
        } else {
            ClassId classId = player.getClassId();
            if ((classId.isOfLevel(ClassLevel.SECOND)) || (classId.isOfLevel(ClassLevel.THIRD)) || (classId.isOfLevel(ClassLevel.AWAKED))) {
                availSubs = EnumSet.copyOf(PlayerClass.MAIN_SUBCLASS_SET);
                availSubs1 = EnumSet.copyOf(PlayerClass.MAIN_SUBCLASS_SET1);

                availSubs.removeAll(PlayerClass.BANNED_SUBCLASSES);
                availSubs.remove(classId);

                availSubs1.removeAll(PlayerClass.BANNED_SUBCLASSES);
                availSubs1.remove(classId);

                switch (race.ordinal()) {
                    case 1:
                        availSubs.removeAll(PlayerClass.getSet(Race.darkelf, ClassLevel.SECOND));
                        break;
                    case 2:
                        availSubs.removeAll(PlayerClass.getSet(Race.elf, ClassLevel.SECOND));
                }

                switch (race.ordinal()) {
                    case 1:
                        availSubs1.removeAll(PlayerClass.getSet(Race.darkelf, ClassLevel.AWAKED));
                        break;
                    case 2:
                        availSubs1.removeAll(PlayerClass.getSet(Race.elf, ClassLevel.AWAKED));
                }

                Set unavailableClasses = (Set) PlayerClass.SUBCLASS_SET_MAP.get(classId);

                if (unavailableClasses != null) {
                    availSubs.removeAll(unavailableClasses);
                    availSubs1.removeAll(unavailableClasses);
                }
            }
        }
        int charClassId = player.getBaseClassId();

        ClassId currClass = ClassId.VALUES[charClassId];

        if (availSubs == null) {
            return Collections.emptySet();
        }
        availSubs.remove(currClass);

        for (ClassId availSub : availSubs) {
            for (SubClass subClass : player.getSubClassList().values()) {
                if (availSub.getId() == subClass.getClassId()) {
                    availSubs.remove(availSub);
                } else {
                    ClassId parent = ClassId.VALUES[availSub.ordinal()].getParent(player.getSex());
                    if ((parent != null) && (parent.getId() == subClass.getClassId())) {
                        availSubs.remove(availSub);
                    } else {
                        ClassId subParent = ClassId.VALUES[subClass.getClassId()].getParent(player.getSex());
                        if ((subParent != null) && (subParent.getId() == availSub.getId()))
                            availSubs.remove(availSub);
                    }
                }
            }
            if (availSub.isOfRace(Race.kamael)) {
                if (((currClass == ClassId.M_SOUL_HOUND) || (currClass == ClassId.F_SOUL_HOUND) || (currClass == ClassId.F_SOUL_BREAKER) || (currClass == ClassId.M_SOUL_BREAKER)) && ((availSub == ClassId.F_SOUL_BREAKER) || (availSub == ClassId.M_SOUL_BREAKER))) {
                    availSubs.remove(availSub);
                }
                if (((currClass == ClassId.BERSERKER) || (currClass == ClassId.DOOMBRINGER) || (currClass == ClassId.ARBALESTER) || (currClass == ClassId.TRICKSTER)) && (((player.getSex() == 1) && (availSub == ClassId.M_SOUL_BREAKER)) || ((player.getSex() == 0) && (availSub == ClassId.F_SOUL_BREAKER)))) {
                    availSubs.remove(availSub);
                }
                if (availSub == ClassId.INSPECTOR)
                    if (player.getSubClassList().size() < (isNew ? 3 : 4))
                        availSubs.remove(availSub);
            }
        }
        return availSubs;
    }

    private String formatClassForDisplay(ClassId className) {
        String classNameStr = className.toString();
        char[] charArray = classNameStr.toCharArray();

        for (int i = 1; i < charArray.length; i++)
            if (Character.isUpperCase(charArray[i]))
                classNameStr = classNameStr.substring(0, i) + " " + classNameStr.substring(i);

        return classNameStr;
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
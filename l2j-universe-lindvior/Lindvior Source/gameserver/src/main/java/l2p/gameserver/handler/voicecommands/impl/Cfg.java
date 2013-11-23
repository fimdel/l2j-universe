package l2p.gameserver.handler.voicecommands.impl;

import l2p.commons.text.PrintfFormat;
import l2p.gameserver.Config;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;
import org.apache.commons.lang3.math.NumberUtils;

public class Cfg extends Functions implements IVoicedCommandHandler {
    private String[] _commandList = new String[]{"lang", "cfg"};

    public static final PrintfFormat cfg_row = new PrintfFormat("<table><tr><td width=5></td><td width=120>%s:</td><td width=100>%s</td></tr></table>");
    public static final PrintfFormat cfg_button = new PrintfFormat("<button width=%d back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h user_cfg %s\" value=\"%s\">");

    public boolean useVoicedCommand(String command, Player activeChar, String args) {
        if (command.equals("cfg"))
            if (args != null) {
                String[] param = args.split(" ");
                if (param.length == 2) {
                    if (param[0].equalsIgnoreCase("dli"))
                        if (param[1].equalsIgnoreCase("on"))
                            activeChar.setVar("DroplistIcons", "1", -1);
                        else if (param[1].equalsIgnoreCase("of"))
                            activeChar.unsetVar("DroplistIcons");

                    if (param[0].equalsIgnoreCase("lang"))
                        if (param[1].equalsIgnoreCase("en"))
                            activeChar.setVar("lang@", "en", -1);
                        else if (param[1].equalsIgnoreCase("ru"))
                            activeChar.setVar("lang@", "ru", -1);

                    if (param[0].equalsIgnoreCase("noe"))
                        if (param[1].equalsIgnoreCase("on"))
                            activeChar.setVar("NoExp", "1", -1);
                        else if (param[1].equalsIgnoreCase("of"))
                            activeChar.unsetVar("NoExp");

                    if (param[0].equalsIgnoreCase(Player.NO_TRADERS_VAR))
                        if (param[1].equalsIgnoreCase("on")) {
                            activeChar.setNotShowTraders(true);
                            activeChar.setVar(Player.NO_TRADERS_VAR, "1", -1);
                        } else if (param[1].equalsIgnoreCase("of")) {
                            activeChar.setNotShowTraders(false);
                            activeChar.unsetVar(Player.NO_TRADERS_VAR);
                        }

                    if (param[0].equalsIgnoreCase(Player.NO_ANIMATION_OF_CAST_VAR))
                        if (param[1].equalsIgnoreCase("on")) {
                            activeChar.setNotShowBuffAnim(true);
                            activeChar.setVar(Player.NO_ANIMATION_OF_CAST_VAR, "1", -1);
                        } else if (param[1].equalsIgnoreCase("of")) {
                            activeChar.setNotShowBuffAnim(false);
                            activeChar.unsetVar(Player.NO_ANIMATION_OF_CAST_VAR);
                        }

                    if (param[0].equalsIgnoreCase("noShift"))
                        if (param[1].equalsIgnoreCase("on"))
                            activeChar.setVar("noShift", "1", -1);
                        else if (param[1].equalsIgnoreCase("of"))
                            activeChar.unsetVar("noShift");

                    if (Config.SERVICES_ENABLE_NO_CARRIER && param[0].equalsIgnoreCase("noCarrier")) {
                        int time = NumberUtils.toInt(param[1], Config.SERVICES_NO_CARRIER_DEFAULT_TIME);

                        if (time > Config.SERVICES_NO_CARRIER_MAX_TIME)
                            time = Config.SERVICES_NO_CARRIER_MAX_TIME;
                        else if (time < Config.SERVICES_NO_CARRIER_MIN_TIME)
                            time = Config.SERVICES_NO_CARRIER_MIN_TIME;

                        activeChar.setVar("noCarrier", String.valueOf(time), -1);
                    }

                    if (param[0].equalsIgnoreCase("translit"))
                        if (param[1].equalsIgnoreCase("on"))
                            activeChar.setVar("translit", "tl", -1);
                        else if (param[1].equalsIgnoreCase("la"))
                            activeChar.setVar("translit", "tc", -1);
                        else if (param[1].equalsIgnoreCase("of"))
                            activeChar.unsetVar("translit");

                    if (param[0].equalsIgnoreCase("autoloot"))
                        activeChar.setAutoLoot(Boolean.parseBoolean(param[1]));

                    if (param[0].equalsIgnoreCase("autolooth"))
                        activeChar.setAutoLootHerbs(Boolean.parseBoolean(param[1]));
                }
            }

        String dialog = HtmCache.getInstance().getNotNull("command/cfg.htm", activeChar);

        dialog = dialog.replaceFirst("%lang%", activeChar.getVar("lang@").toUpperCase());
        dialog = dialog.replaceFirst("%dli%", activeChar.getVarB("DroplistIcons") ? "On" : "Off");
        dialog = dialog.replaceFirst("%noe%", activeChar.getVarB("NoExp") ? "On" : "Off");
        dialog = dialog.replaceFirst("%notraders%", activeChar.getVarB("notraders") ? "On" : "Off");
        dialog = dialog.replaceFirst("%notShowBuffAnim%", activeChar.getVarB("notShowBuffAnim") ? "On" : "Off");
        dialog = dialog.replaceFirst("%noShift%", activeChar.getVarB("noShift") ? "On" : "Off");
        dialog = dialog.replaceFirst("%noCarrier%", Config.SERVICES_ENABLE_NO_CARRIER ? activeChar.getVarB("noCarrier") ? activeChar.getVar("noCarrier") : "0" : "N/A");
        String tl = activeChar.getVar("translit");
        if (tl == null)
            dialog = dialog.replaceFirst("%translit%", "Off");
        else if (tl.equals("tl"))
            dialog = dialog.replaceFirst("%translit%", "On");
        else
            dialog = dialog.replaceFirst("%translit%", "Lt");

        String additional = "";

        if (Config.AUTO_LOOT_INDIVIDUAL) {
            String bt;
            if (activeChar.isAutoLootEnabled())
                bt = cfg_button.sprintf(100, "autoloot false", new CustomMessage("common.Disable", activeChar).toString());
            else
                bt = cfg_button.sprintf(100, "autoloot true", new CustomMessage("common.Enable", activeChar).toString());
            additional += cfg_row.sprintf("Auto-loot", bt);

            if (activeChar.isAutoLootHerbsEnabled())
                bt = cfg_button.sprintf(100, "autolooth false", new CustomMessage("common.Disable", activeChar).toString());
            else
                bt = cfg_button.sprintf(100, "autolooth true", new CustomMessage("common.Enable", activeChar).toString());
            additional += cfg_row.sprintf("Auto-loot herbs", bt);
        }

        dialog = dialog.replaceFirst("%additional%", additional);

        StringBuilder events = new StringBuilder();
        for (GlobalEvent e : activeChar.getEvents())
            events.append(e.toString()).append("<br>");
        dialog = dialog.replace("%events%", events.toString());

        show(dialog, activeChar);

        return true;
    }

    public String[] getVoicedCommandList() {
        return _commandList;
    }
}
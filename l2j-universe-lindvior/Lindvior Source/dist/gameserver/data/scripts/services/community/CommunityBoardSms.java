/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import javolution.text.TextBuilder;
import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

public class CommunityBoardSms implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardSms.class);

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: SMS service loaded.");
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

    public class SMSCountryList {
        public String Country = "";
    }

    public class SMSOperatorList {
        public String Operator = "";
        public int Phone;
        public double Price;
    }

    public String mobileHtmlOperator;
    public String mobileHtmlCountry;

    @Override
    public String[] getBypassCommands() {
        return new String[]{"_bbssms", "_bbssmscounrty", "_bbssmsoperator"};
    }

    @Override
    public void onBypassCommand(Player player, String command) {
        if (command.equals("_bbssms"))
            showSMSCountryPage(player, "", "");
        else if (command.startsWith("_bbssmscounrty")) {
            StringTokenizer selectedCountry = new StringTokenizer(command, " ");
            if (selectedCountry.countTokens() == 2) {
                selectedCountry.nextToken();
                String countryName = selectedCountry.nextToken();
                showSMSCountryPage(player, countryName, "");
            } else if (selectedCountry.countTokens() == 3) {
                selectedCountry.nextToken();
                String countryName = selectedCountry.nextToken() + " " + selectedCountry.nextToken();
                showSMSCountryPage(player, countryName, "");
            }
        } else if (command.startsWith("_bbssmsoperator")) {
            StringTokenizer selectedOperator = new StringTokenizer(command, " ");
            if (selectedOperator.countTokens() == 3) {
                selectedOperator.nextToken();
                String countryName = selectedOperator.nextToken();
                String operatorName = selectedOperator.nextToken();
                showSMSCountryPage(player, countryName, operatorName);
            } else if (selectedOperator.countTokens() == 4) {
                selectedOperator.nextToken();
                String countryName = selectedOperator.nextToken();
                String operatorName = selectedOperator.nextToken() + " " + selectedOperator.nextToken();
                showSMSCountryPage(player, countryName, operatorName);
            } else if (selectedOperator.countTokens() == 5) {
                selectedOperator.nextToken();
                String countryName = selectedOperator.nextToken();
                String operatorName = selectedOperator.nextToken() + " " + selectedOperator.nextToken() + " " + selectedOperator.nextToken();
                showSMSCountryPage(player, countryName, operatorName);
            } else if (selectedOperator.countTokens() == 6) {
                selectedOperator.nextToken();
                String countryName = selectedOperator.nextToken();
                String operatorName = selectedOperator.nextToken() + " " + selectedOperator.nextToken() + " " + selectedOperator.nextToken() + " " + selectedOperator.nextToken();
                showSMSCountryPage(player, countryName, operatorName);
            }
        } else if (player.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br>", player);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
    }

    private void showSMSCountryPage(Player player, String country, String operator) {
        SMSCountryList smsCountryData;
        SMSOperatorList smsOperatorData;
        Connection conCountry = null, conOperator = null;
        TextBuilder htmlCountry = new TextBuilder();
        TextBuilder htmlOperator = new TextBuilder();

        try {
            conCountry = DatabaseFactory.getInstance().getConnection();
            PreparedStatement stCountry = conCountry.prepareStatement("SELECT country FROM bbs_sms_country;");
            ResultSet rsCountry = stCountry.executeQuery();
            while (rsCountry.next()) {
                smsCountryData = new SMSCountryList();
                smsCountryData.Country = rsCountry.getString("country");
                htmlCountry.append(smsCountryData.Country + ";");
            }
            DbUtils.closeQuietly(stCountry);

            if (country != "")
                try {
                    conOperator = DatabaseFactory.getInstance().getConnection();
                    PreparedStatement stOperator = conOperator.prepareStatement("SELECT operator FROM bbs_sms_data WHERE country=? GROUP BY operator;");
                    stOperator.setString(1, country);
                    ResultSet rsOperator = stOperator.executeQuery();
                    while (rsOperator.next()) {
                        smsOperatorData = new SMSOperatorList();
                        smsOperatorData.Operator = rsOperator.getString("operator");
                        htmlOperator.append(smsOperatorData.Operator + ";");
                    }
                    DbUtils.closeQuietly(stOperator, rsOperator);
                } catch (Exception e) {
                } finally {
                    DbUtils.closeQuietly(conOperator);
                }

            String html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/donate/sms.htm", player);

            if (country == "" && operator == "") {
                if (htmlCountry.toString().length() == 0)
                    html = html.replace("%selectCountry%", "<td width=160 align=\"center\">Выберите страну: </td><td width=200 align=\"center\">Нет доступных стран для SMS</td><td width=140></td>");
                else
                    html = html.replace("%selectCountry%", "<td width=160 align=\"center\">Выберите страну: </td><td width=200 align=\"center\"><combobox width=200 var=\"countryBtn\" list=\"" + htmlCountry.toString() + "\"></td><td width=140 align=\"center\"><button value=\"Выбрать\" action=\"bypass _bbssmscounrty $countryBtn\" width=70 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                html = html.replace("%selectOperator%", "");
                html = html.replace("%tarifTable%", "");
                country = "Страна не выбрана";
                operator = "Оператор не выбран";
            } else if (country != "" && operator == "") {
                html = html.replace("%selectCountry%", "<td width=160 align=\"center\">Выбрана страна: </td><td width=200 align=\"center\">" + country + "</td><td width=140></td>");
                if (htmlOperator.toString().length() == 0)
                    html = html.replace("%selectOperator%", "<td width=160 align=\"center\">Выберите оператора: </td><td width=200 align=\"center\">Нет операторов для данной страны</td><td width=140></td>");
                else
                    html = html.replace("%selectOperator%", "<td width=160 align=\"center\">Выберите оператора: </td><td width=200 align=\"center\"><combobox width=200 var=\"operatorBtn\" list=\"" + htmlOperator.toString() + "\"></td><td width=140 align=\"center\"><button value=\"Выбрать\" action=\"bypass _bbssmsoperator " + country + " $operatorBtn\" width=70 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                html = html.replace("%tarifTable%", "");
                operator = "Оператор не выбран";
            } else if (country != "" && operator != "") {
                html = html.replace("%selectCountry%", "<td width=160 align=\"center\">Выбрана страна: </td><td width=200 align=\"center\">" + country + "</td><td width=140></td>");
                html = html.replace("%selectOperator%", "<td width=160 align=\"center\">Выбран оператор: </td><td width=200 align=\"center\">" + operator + "</td><td width=140></td>");
                html = html.replace("%tarifTable%", showTarifsTable(player, country, operator));
            }
            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(html, player);
            return;
        } catch (Exception e) {
            _log.error("No SQL data");
        } finally {
            DbUtils.closeQuietly(conCountry);
        }
    }

    private String showTarifsTable(Player player, String country, String operator) {
        Connection conSmsData = null;
        StringBuilder html = new StringBuilder();

        html.append("<table width=500 valign=top align=center border=1 cellpadding=2 cellspacing=2>");
        html.append("<tr>");
        html.append("<td width=165 align=center>Короткий номер</td>");
        html.append("<td width=165 align=center>Стоимость</td>");
        html.append("<td width=165 align=center>Стоимость с НДС</td>");
        html.append("</tr>");

        try {
            conSmsData = DatabaseFactory.getInstance().getConnection();
            PreparedStatement stSmsData = conSmsData.prepareStatement("SELECT phone, price, price_nds FROM bbs_sms_data WHERE country=? AND operator=?;");
            stSmsData.setString(1, country);
            stSmsData.setString(2, operator);
            ResultSet rsSmsData = stSmsData.executeQuery();
            while (rsSmsData.next()) {
                html.append("<tr>");
                html.append("<td align=center width=165>").append(rsSmsData.getInt("phone")).append("</td>");
                html.append("<td align=center width=165>").append(rsSmsData.getDouble("price")).append("</td>");
                html.append("<td align=center width=165>").append(rsSmsData.getDouble("price_nds")).append("</td>");
                html.append("</tr>");
            }
            DbUtils.closeQuietly(stSmsData, rsSmsData);
        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(conSmsData);
        }
        html.append("</table>");

        html.append("<table width=500 valign=top align=center cellpadding=5 cellspacing=5>");
        html.append("<tr><td width=500 align=center colspan=3>Отправьте СМС на выбранный номер из таблицы.</td></tr>");
        html.append("<tr><td width=500 align=center colspan=3>Формат СМС: WTP " + Config.SMS_PAYMENT_PREFIX + " " + player.getName() + "</td></tr>");
        html.append("</table>");

        return html.toString();
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class CommunityBoardNews implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardNews.class);

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: News service loaded.");
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
        return new String[]{"_bbssqlnews", "_bbssfullproject", "_bbssfullserver"};
    }

    public static class CBPNewsMan {
        public int[] newsId = new int[7];
        public int[] newsType = new int[7];
        public String[] newsTitleRu = new String[7];
        public String[] newsTitleEn = new String[7];
        public String[] newsTextRu = new String[7];
        public String[] newsTextEn = new String[7];
        public String[] newsInfoRu = new String[7];
        public String[] newsInfoEn = new String[7];
        public String[] newsAuthor = new String[7];
        public Date[] newsDate = new Date[7];
    }

    public static class CBSNewsMan {
        public int[] newsId = new int[7];
        public int[] newsType = new int[7];
        public String[] newsTitleRu = new String[7];
        public String[] newsTitleEn = new String[7];
        public String[] newsTextRu = new String[7];
        public String[] newsTextEn = new String[7];
        public String[] newsInfoRu = new String[7];
        public String[] newsInfoEn = new String[7];
        public String[] newsAuthor = new String[7];
        public Date[] newsDate = new Date[7];
    }

    static CBPNewsMan pbBPNews = new CBPNewsMan();
    static CBSNewsMan pbBSNews = new CBSNewsMan();

    public long lUpdateTime = System.currentTimeMillis() / 1000;
    public int nCounter = 0;

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public void onBypassCommand(Player player, String command) {
        if (command.equals("_bbssqlnews")) {
            if (lUpdateTime + Config.BBS_NEWS_UPDATE_TIME * 60 < System.currentTimeMillis() / 1000) {
                selectPortalNews(player);
                selectServerNews(player);
                lUpdateTime = System.currentTimeMillis() / 1000;
            }
            showNewsMainPage(player);
        } else if (command.startsWith("_bbssfullproject")) {
            StringTokenizer selectedPFNewsId = new StringTokenizer(command, " ");
            selectedPFNewsId.nextToken();
            int nPFNewsId = Integer.parseInt(selectedPFNewsId.nextToken());
            showProjectFullPage(player, nPFNewsId);
        } else if (command.startsWith("_bbssfullserver")) {
            StringTokenizer selectedSFNewsId = new StringTokenizer(command, " ");
            selectedSFNewsId.nextToken();
            int nSFNewsId = Integer.parseInt(selectedSFNewsId.nextToken());
            showServerFullPage(player, nSFNewsId);
        } else if (player.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br>", player);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
    }

    private void showNewsMainPage(Player player) {
        String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/news/all_news.htm", player);
        TextBuilder htmlP = new TextBuilder("");
        TextBuilder htmlS = new TextBuilder("");

        for (int i = 0; i < 7; i++) {
            if (pbBPNews.newsTitleRu[i] != null) {
                String PNDate = dateFormat.format(pbBPNews.newsDate[i]);
                htmlP.append("<table border=0 cellspacing=4 cellpadding=3>");
                htmlP.append("<tr>");
                htmlP.append("<td FIXWIDTH=50 align=right valign=top>");
                htmlP.append("<img src=l2ui.ActionWnd.bbs_Webfolder width=32 height=32>");
                htmlP.append("</td>");
                htmlP.append("<td FIXWIDTH=200 align=left valign=top>");
                htmlP.append("<a action=\"bypass _bbssfullproject ").append(pbBPNews.newsId[i]).append("\">").append(player.isLangRus() ? pbBPNews.newsTitleRu[i] : pbBPNews.newsTitleEn[i]).append("</a><br1>");
                htmlP.append("<font color=AAAAAA>").append(player.isLangRus() ? pbBPNews.newsInfoRu[i] : pbBPNews.newsInfoEn[i]).append("<font>");
                htmlP.append("</td>");
                htmlP.append("<td FIXWIDTH=95 align=center valign=top>").append(PNDate).append("</td>");
                htmlP.append("</tr>");
                htmlP.append("</table>");
                htmlP.append("<table border=0 cellspacing=0 cellpadding=0>");
                htmlP.append("<tr>");
                htmlP.append("<td width=345>");
                htmlP.append("<img src=l2ui.squaregray width=345 height=1>");
                htmlP.append("</td>");
                htmlP.append("</tr>");
                htmlP.append("</table>");
                content = content.replace("%news_p_" + i + "%", htmlP.toString());
            } else {
                htmlP.append("<table border=0 cellspacing=4 cellpadding=3>");
                htmlP.append("<tr>");
                htmlP.append("<td FIXWIDTH=50 align=right valign=top>");
                htmlP.append("</td>");
                htmlP.append("<td FIXWIDTH=200 align=left valign=top>");
                htmlP.append("</td>");
                htmlP.append("<td FIXWIDTH=95 align=center valign=top></td>");
                htmlP.append("</tr>");
                htmlP.append("</table>");
                htmlP.append("<table border=0 cellspacing=0 cellpadding=0>");
                htmlP.append("<tr>");
                htmlP.append("<td width=345>");
                htmlP.append("</td>");
                htmlP.append("</tr>");
                htmlP.append("</table>");
                content = content.replace("%news_p_" + i + "%", htmlP.toString());
            }

            if (pbBSNews.newsTitleRu[i] != null) {
                String SNDate = dateFormat.format(pbBSNews.newsDate[i]);
                htmlS.append("<table border=0 cellspacing=4 cellpadding=3>");
                htmlS.append("<tr>");
                htmlS.append("<td FIXWIDTH=50 align=right valign=top>");
                htmlS.append("<img src=l2ui.bbs_folder width=32 height=32>");
                htmlS.append("</td>");
                htmlS.append("<td FIXWIDTH=200 align=left valign=top>");
                htmlS.append("<a action=\"bypass _bbssfullserver ").append(pbBSNews.newsId[i]).append("\">").append(player.isLangRus() ? pbBSNews.newsTitleRu[i] : pbBSNews.newsTitleEn[i]).append("</a><br1>");
                htmlS.append("<font color=AAAAAA>").append(player.isLangRus() ? pbBSNews.newsInfoRu[i] : pbBSNews.newsInfoEn[i]).append("<font>");
                htmlS.append("</td>");
                htmlS.append("<td FIXWIDTH=95 align=center valign=top>").append(SNDate).append("</td>");
                htmlS.append("</tr>");
                htmlS.append("</table>");
                htmlS.append("<table border=0 cellspacing=0 cellpadding=0>");
                htmlS.append("<tr>");
                htmlS.append("<td width=345>");
                htmlS.append("<img src=l2ui.squaregray width=345 height=1>");
                htmlS.append("</td>");
                htmlS.append("</tr>");
                htmlS.append("</table>");
                content = content.replace("%news_s_" + i + "%", htmlS.toString());
            } else {
                htmlS.append("<table border=0 cellspacing=4 cellpadding=3>");
                htmlS.append("<tr>");
                htmlS.append("<td FIXWIDTH=50 align=right valign=top>");
                htmlS.append("</td>");
                htmlS.append("<td FIXWIDTH=200 align=left valign=top>");
                htmlS.append("</td>");
                htmlS.append("<td FIXWIDTH=95 align=center valign=top></td>");
                htmlS.append("</tr>");
                htmlS.append("</table>");
                htmlS.append("<table border=0 cellspacing=0 cellpadding=0>");
                htmlS.append("<tr>");
                htmlS.append("<td width=345>");
                htmlS.append("</td>");
                htmlS.append("</tr>");
                htmlS.append("</table>");
                content = content.replace("%news_s_" + i + "%", htmlS.toString());
            }
            htmlP.clear();
            htmlS.clear();
        }

        content = content.replace("%page_title%", player.isLangRus() ? "Новости" : "News");
        content = content.replace("%news_p_title%", player.isLangRus() ? "Новости Проекта" : "Project News");
        content = content.replace("%news_s_title%", player.isLangRus() ? "Новости Сервера" : "Server News");
        content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
        content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
        content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        ShowBoard.separateAndSend(content, player);
    }

    private void selectPortalNews(Player player) {
        Connection conPNews = null;
        PreparedStatement statementPNews = null;
        ResultSet rsPNews = null;
        int counter = 0;

        try {
            conPNews = DatabaseFactory.getInstance().getConnection();
            statementPNews = conPNews.prepareStatement("SELECT * FROM bbs_news WHERE newsType='0' ORDER BY newsDate DESC, newsId DESC LIMIT 0,7");
            rsPNews = statementPNews.executeQuery();

            while (rsPNews.next()) {
                pbBPNews.newsId[counter] = rsPNews.getInt("newsId");
                pbBPNews.newsType[counter] = rsPNews.getInt("newsType");
                pbBPNews.newsTitleRu[counter] = rsPNews.getString("newsTitleRu");
                pbBPNews.newsTitleEn[counter] = rsPNews.getString("newsTitleEn");
                pbBPNews.newsTextRu[counter] = rsPNews.getString("newsTextRu");
                pbBPNews.newsTextEn[counter] = rsPNews.getString("newsTextEn");
                pbBPNews.newsInfoRu[counter] = rsPNews.getString("newsInfoRu");
                pbBPNews.newsInfoEn[counter] = rsPNews.getString("newsInfoEn");
                pbBPNews.newsAuthor[counter] = rsPNews.getString("newsAuthor");
                pbBPNews.newsDate[counter] = rsPNews.getDate("newsDate");
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conPNews, statementPNews, rsPNews);
        }
    }

    private void selectServerNews(Player player) {
        Connection conSNews = null;
        PreparedStatement statementSNews = null;
        ResultSet rsSNews = null;
        int counter = 0;

        try {
            conSNews = DatabaseFactory.getInstance().getConnection();
            statementSNews = conSNews.prepareStatement("SELECT * FROM bbs_news WHERE newsType='1' ORDER BY newsDate DESC, newsId DESC LIMIT 0,7");
            rsSNews = statementSNews.executeQuery();

            while (rsSNews.next()) {
                pbBSNews.newsId[counter] = rsSNews.getInt("newsId");
                pbBSNews.newsType[counter] = rsSNews.getInt("newsType");
                pbBSNews.newsTitleRu[counter] = rsSNews.getString("newsTitleRu");
                pbBSNews.newsTitleEn[counter] = rsSNews.getString("newsTitleEn");
                pbBSNews.newsTextRu[counter] = rsSNews.getString("newsTextRu");
                pbBSNews.newsTextEn[counter] = rsSNews.getString("newsTextEn");
                pbBSNews.newsInfoRu[counter] = rsSNews.getString("newsInfoRu");
                pbBSNews.newsInfoEn[counter] = rsSNews.getString("newsInfoEn");
                pbBSNews.newsAuthor[counter] = rsSNews.getString("newsAuthor");
                pbBSNews.newsDate[counter] = rsSNews.getDate("newsDate");
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conSNews, statementSNews, rsSNews);
        }
    }

    private void showProjectFullPage(Player player, int nPFNewsId) {
        String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/news/project_news_full.htm", player);

        for (int i = 0; i < 7; i++)
            if (pbBPNews.newsId[i] == nPFNewsId) {
                String PNFDate = dateFormat.format(pbBPNews.newsDate[i]);
                content = content.replace("%project_news_title%", player.isLangRus() ? pbBPNews.newsTitleRu[i] : pbBPNews.newsTitleEn[i]);
                content = content.replace("%project_news_content%", player.isLangRus() ? pbBPNews.newsTextRu[i] : pbBPNews.newsTextEn[i]);
                content = content.replace("%project_news_date%", PNFDate);
                content = content.replace("%project_news_info%", player.isLangRus() ? pbBPNews.newsInfoRu[i] : pbBPNews.newsInfoEn[i]);
                content = content.replace("%project_news_author%", pbBPNews.newsAuthor[i]);
            }

        content = content.replace("%project_news_page_title%", player.isLangRus() ? "Новости проекта - полная версия" : "Project News - full version");
        content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
        content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
        content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        ShowBoard.separateAndSend(content, player);
    }

    private void showServerFullPage(Player player, int nSFNewsId) {
        String content = HtmCache.getInstance().getNotNull("/scripts/services/community/" + Config.BBS_FOLDER + "/news/server_news_full.htm", player);

        for (int i = 0; i < 7; i++)
            if (pbBSNews.newsId[i] == nSFNewsId) {
                String SNFDate = dateFormat.format(pbBSNews.newsDate[i]);
                content = content.replace("%server_news_title%", player.isLangRus() ? pbBSNews.newsTitleRu[i] : pbBSNews.newsTitleEn[i]);
                content = content.replace("%server_news_content%", player.isLangRus() ? pbBSNews.newsTextRu[i] : pbBSNews.newsTextEn[i]);
                content = content.replace("%server_news_date%", SNFDate);
                content = content.replace("%server_news_info%", player.isLangRus() ? pbBSNews.newsInfoRu[i] : pbBSNews.newsInfoEn[i]);
                content = content.replace("%server_news_author%", pbBSNews.newsAuthor[i]);
            }

        content = content.replace("%server_news_page_title%", player.isLangRus() ? "Новости сервера - полная версия" : "Server News - full version");

        content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
        content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
        content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        ShowBoard.separateAndSend(content, player);
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
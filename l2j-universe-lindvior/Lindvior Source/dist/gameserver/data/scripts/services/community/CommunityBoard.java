/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.data.xml.holder.MultiSellHolder;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.scripts.Scripts;
import l2p.gameserver.tables.ClanTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

public class CommunityBoard implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoard.class);

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: Community service loaded.");
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

    public static boolean checkPlayerByPremium(Player player) {
        if (Config.BBS_ALLOW_IN_ALL_ZONE_ONLY_FOR_PREMIUM && !player.isInZonePeace() && player.getBonus().getBonusExpire() <= 0) // check! If player is not in peace zone and he is not premium, set false open cb.
            return false;
        else
            return true;
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]{
                "_bbshome",
                "_bbslink",
                "_bbsbuff",
                "_bbsnews",
                "_bbsmultisell",
                "_bbspage",
                "_bbsscripts",
                "_wiki",
                "_teleport",
                "_stat",
                "_services",
                "_info",
                "_event",
                "_donate",
                "_bbsshop"};
    }

    @Override
    public void onBypassCommand(Player player, String bypass) {
        if (!checkPlayerByPremium(player)) {
            player.scriptRequest(new CustomMessage("community.Ask.Player.Buy.Premium", player).toString(), "services.RateBonus:list", new Object[0]);
            return;
        }

        StringTokenizer st = new StringTokenizer(bypass, "_");
        String cmd = st.nextToken();
        String html = "";
        if ("bbshome".equals(cmd)) {
            StringTokenizer p = new StringTokenizer(Config.BBS_DEFAULT, "_");
            String dafault = p.nextToken();
            if (dafault.equals(cmd)) {
                html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_top.htm", player);

                int favCount = 0;
                Connection con = null;
                PreparedStatement statement = null;
                ResultSet rset = null;
                try {
                    con = DatabaseFactory.getInstance().getConnection();
                    statement = con.prepareStatement("SELECT count(*) as cnt FROM `bbs_favorites` WHERE `object_id` = ?");
                    statement.setInt(1, player.getObjectId());
                    rset = statement.executeQuery();
                    if (rset.next())
                        favCount = rset.getInt("cnt");
                } catch (Exception e) {
                } finally {
                    DbUtils.closeQuietly(con, statement, rset);
                }

                html = html.replace("<?fav_count?>", String.valueOf(favCount));
                html = html.replace("<?clan_count?>", String.valueOf(ClanTable.getInstance().getClans().length));
                html = html.replace("<?market_count?>", String.valueOf(CommunityBoardHandler.getInstance().getIntProperty("col_count")));
            } else {
                onBypassCommand(player, Config.BBS_DEFAULT);
                return;
            }
        } else if ("bbslink".equals(cmd))
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_homepage.htm", player);
        else if (bypass.startsWith("_bbsnews")) {
            // Example: "bypass _bbsnews:news_1".
            String[] b = bypass.split(":");
            String news_id = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/news/" + news_id + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_bbsshop")) {
            // Example: "bypass _bbsshop:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/shop/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_donate")) {
            // Example: "bypass _donate:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/donate/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_event")) {
            // Example: "bypass _event:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/event/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_info")) {
            // Example: "bypass _info:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/info/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_services")) {
            // Example: "bypass _services:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/services/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_stat")) {
            // Example: "bypass _stat:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/stat/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_teleport")) {
            // Example: "bypass _teleport:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/teleport/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_bbsbuff")) {
            // Example: "bypass _bbsbuff:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_wiki")) {
            // Example: "bypass _wiki:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_bbspage")) {
            // Example: "bypass _bbspage:index".
            String[] b = bypass.split(":");
            String page = b[1];
            html = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/pages/" + page + ".htm", player);

            html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
        } else if (bypass.startsWith("_bbsmultisell")) {
            // Example: "_bbsmultisell:10000;_bbspage:index" or
            // "_bbsmultisell:10000;_bbshome" or "_bbsmultisell:10000"...
            StringTokenizer st2 = new StringTokenizer(bypass, ";");
            String[] mBypass = st2.nextToken().split(":");
            String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
            if (pBypass != null) {
                ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
                if (handler != null)
                    handler.onBypassCommand(player, pBypass);
            }

            int listId = Integer.parseInt(mBypass[1]);
            MultiSellHolder.getInstance().SeparateAndSend(listId, player, 0);
            return;
        } else if (bypass.startsWith("_bbsscripts")) {
            // Example: "_bbsscripts:events.GvG.GvG:addGroup;_bbspage:index" or
            // "_bbsscripts:events.GvG.GvG:addGroup;_bbshome" or
            // "_bbsscripts:events.GvG.GvG:addGroup"...
            StringTokenizer st2 = new StringTokenizer(bypass, ";");
            String sBypass = st2.nextToken().substring(12);
            String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
            if (pBypass != null) {
                ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
                if (handler != null)
                    handler.onBypassCommand(player, pBypass);
            }

            String[] word = sBypass.split("\\s+");
            String[] args = sBypass.substring(word[0].length()).trim().split("\\s+");
            String[] path = word[0].split(":");
            if (path.length != 2)
                return;

            Scripts.getInstance().callScripts(player, path[0], path[1], word.length == 1 ? new Object[]{} : new Object[]{args});
            return;
        }

        html = html.replace("<?nick_change_price?>", String.valueOf(Config.SERVICES_CHANGE_NICK_PRICE));
        html = html.replace("<?nick_change_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_NICK_ITEM).getName());

        html = html.replace("<?pet_name_change_price?>", String.valueOf(Config.SERVICES_CHANGE_PET_NAME_PRICE));
        html = html.replace("<?pet_name_change_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_PET_NAME_ITEM).getName());

        html = html.replace("<?baby_pet_exchange_price?>", String.valueOf(Config.SERVICES_EXCHANGE_BABY_PET_PRICE));
        html = html.replace("<?baby_pet_exchange_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_EXCHANGE_BABY_PET_ITEM).getName());

        html = html.replace("<?sex_change_price?>", String.valueOf(Config.SERVICES_CHANGE_SEX_PRICE));
        html = html.replace("<?sex_change_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_SEX_ITEM).getName());

        html = html.replace("<?base_change_price?>", String.valueOf(Config.SERVICES_CHANGE_BASE_PRICE));
        html = html.replace("<?base_change_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_BASE_ITEM).getName());

        html = html.replace("<?separate_sub_price?>", String.valueOf(Config.SERVICES_SEPARATE_SUB_PRICE));
        html = html.replace("<?separate_sub_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_SEPARATE_SUB_ITEM).getName());

        html = html.replace("<?nick_color_change_price?>", String.valueOf(Config.SERVICES_CHANGE_NICK_COLOR_PRICE));
        html = html.replace("<?nick_color_change_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_NICK_COLOR_ITEM).getName());

        html = html.replace("<?nobless_sell_price?>", String.valueOf(Config.SERVICES_NOBLESS_SELL_PRICE));
        html = html.replace("<?nobless_sell_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_NOBLESS_SELL_ITEM).getName());

        html = html.replace("<?expand_inventory_price?>", String.valueOf(Config.SERVICES_EXPAND_INVENTORY_PRICE));
        html = html.replace("<?expand_inventory_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_EXPAND_INVENTORY_ITEM).getName());
        html = html.replace("<?expand_inventory_max?>", String.valueOf(Config.SERVICES_EXPAND_INVENTORY_MAX));

        html = html.replace("<?expand_warehouse_price?>", String.valueOf(Config.SERVICES_EXPAND_WAREHOUSE_PRICE));
        html = html.replace("<?expand_warehouse_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_EXPAND_WAREHOUSE_ITEM).getName());

        html = html.replace("<?expand_cwh_price?>", String.valueOf(Config.SERVICES_EXPAND_CWH_PRICE));
        html = html.replace("<?expand_cwh_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_EXPAND_CWH_ITEM).getName());

        html = html.replace("<?clan_name_change_price?>", String.valueOf(Config.SERVICES_CHANGE_CLAN_NAME_PRICE));
        html = html.replace("<?clan_name_change_item?>", ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_CLAN_NAME_ITEM).getName());

        html = html.replace("<?player_name?>", String.valueOf(player.getName()));

        String[] adminNames = null;
        String[] supportNames = null;
        String[] gmNames = null;
        String[] forumNames = null;

        adminNames = Config.COMMUNITYBOARD_SERVER_ADMIN_NAME.split(";");
        supportNames = Config.COMMUNITYBOARD_SERVER_SUPPORT_NAME.split(";");
        gmNames = Config.COMMUNITYBOARD_SERVER_GM_NAME.split(";");
        forumNames = Config.COMMUNITYBOARD_FORUM_ADMIN_NAME.split(";");

        for (int i = 0; i < adminNames.length; i++) {
            html = html.replace("<?server_admin_name_" + i + "?>", adminNames[i]);
            if (GameObjectsStorage.getPlayer(adminNames[i]) != null && GameObjectsStorage.getPlayer(adminNames[i]).isOnline())
                html = html.replace("<?server_admin_" + i + "_status?>", player.isLangRus() ? "<font color=\"18FF00\">Онлайн</font>" : "<font color=\"18FF00\">Online</font>");
            else
                html = html.replace("<?server_admin_" + i + "_status?>", player.isLangRus() ? "<font color=\"FF0000\">Офлайн</font>" : "<font color=\"FF0000\">Offline</font>");
        }

        for (int i = 0; i < supportNames.length; i++) {
            html = html.replace("<?server_support_name_" + i + "?>", supportNames[i]);
            if (GameObjectsStorage.getPlayer(supportNames[i]) != null && GameObjectsStorage.getPlayer(supportNames[i]).isOnline())
                html = html.replace("<?server_support_" + i + "_status?>", player.isLangRus() ? "<font color=\"18FF00\">Онлайн</font>" : "<font color=\"18FF00\">Online</font>");
            else
                html = html.replace("<?server_support_" + i + "_status?>", player.isLangRus() ? "<font color=\"FF0000\">Офлайн</font>" : "<font color=\"FF0000\">Offline</font>");
        }

        for (int i = 0; i < gmNames.length; i++) {
            html = html.replace("<?server_gm_name_" + i + "?>", gmNames[i]);
            if (GameObjectsStorage.getPlayer(gmNames[i]) != null && GameObjectsStorage.getPlayer(gmNames[i]).isOnline())
                html = html.replace("<?server_gm_" + i + "_status?>", player.isLangRus() ? "<font color=\"18FF00\">Онлайн</font>" : "<font color=\"18FF00\">Online</font>");
            else
                html = html.replace("<?server_gm_" + i + "_status?>", player.isLangRus() ? "<font color=\"FF0000\">Офлайн</font>" : "<font color=\"FF0000\">Offline</font>");
        }

        for (int i = 0; i < forumNames.length; i++)
            html = html.replace("<?server_forum_name_" + i + "?>", forumNames[i]);

        html = html.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
        html = html.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);

        html = html.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());

        ShowBoard.separateAndSend(html, player);
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }

    public static boolean checkPlayer(Player player) {
        if (Config.ALLOW_PVPCB_ABNORMAL) // restriction to do in configs. if false
        {
            if (!Config.CB_DEATH) //if dead or alike dead
                if (player.isDead() || player.isAlikeDead() || player.isFakeDeath())
                    return false;

            if (!Config.CB_ACTION) //if is in action or so
                if (player.isCastingNow() || player.isInCombat() || player.isAttackingNow() || player.getPvpFlag() > 0)
                    return false;

            if (!Config.CB_OLY) //if is in olympiad
                if (player.isInOlympiadMode() || player.getOlympiadObserveGame() != null)
                    return false;

            if (!Config.CB_FLY) //if is in fly mode.
                if (player.isFlying() || player.isInFlyingTransform())
                    return false;

            if (!Config.CB_VEICHLE) //if is in veichle.
                if (player.isInBoat())
                    return false;

            if (!Config.CB_MOUNTED) //if is riding.
                if (player.isMounted())
                    return false;

            if (!Config.CB_CANT_MOVE) //if cannot move
                if (player.isMovementDisabled() || player.isParalyzed() || player.isStunned() || player.isSleeping() || player.isRooted() || player.isImmobilized())
                    return false;

            if (!Config.CB_STORE_MODE) //if is in storre mode.
                if (player.isInStoreMode() || player.isInTrade() || player.isInOfflineMode())
                    return false;

            if (!Config.CB_FISHING) //if is fishing
                if (player.isFishing())
                    return false;

            if (!Config.CB_TEMP_ACTION) //if not certantly in game.
                if (player.isLogoutStarted() || player.isTeleporting())
                    return false;

            if (!Config.CB_DUEL) //if is in duel.
                if (player.isInDuel())
                    return false;

            if (!Config.CB_CURSED) //if have cursed weapon.
                if (player.isCursedWeaponEquipped())
                    return false;

            if (!Config.CB_PK) //if is pk.
                if (player.getKarma() > 0)
                    return false;

            if (Config.CB_LEADER) //if is clan leader.
                if (!player.isClanLeader())
                    return false;

            if (Config.CB_NOBLE) //if is noble.
                if (!player.isNoble())
                    return false;

            if (!Config.CB_TERITORY) //if is in territory siege.
                if (player.isOnSiegeField())
                    return false;

            if (Config.CB_PEACEZONE_ONLY) //if is territory siege in progress.
                if (!player.isInZonePeace())
                    return false;
            return true;
        } else
            return true;
    }
}
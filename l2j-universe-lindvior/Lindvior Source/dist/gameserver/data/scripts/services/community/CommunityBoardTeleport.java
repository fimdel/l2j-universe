/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import javolution.text.TextBuilder;
import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

public class CommunityBoardTeleport implements ScriptFile, ICommunityBoardHandler {
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardTeleport.class);

    public class CBteleport {
        public int TpId = 0; // Teport location ID
        public String TpName = ""; // Location name
        public int PlayerId = 0; // charID
        public int xC = 0; // Location coords X
        public int yC = 0; // Location coords Y
        public int zC = 0; // Location coords Z
    }

    public static final ZoneType[] FORBIDDEN_ZONES = new ZoneType[]{
            ZoneType.RESIDENCE,
            ZoneType.ssq_zone,
            ZoneType.battle_zone,
            ZoneType.SIEGE,
            ZoneType.no_restart,
            ZoneType.no_summon,};

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: Teleport service loaded.");
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
        return new String[]{"_bbsteleport"};
    }

    @Override
    public void onBypassCommand(Player player, String command) {
        if (command.equals("_bbsteleport;"))
            showTeleportPoint(player);
        else if (command.startsWith("_bbsteleport;delete;")) {
            StringTokenizer stDell = new StringTokenizer(command, ";");
            stDell.nextToken();
            stDell.nextToken();
            int TpNameDell = Integer.parseInt(stDell.nextToken());
            deleteTeleportPoint(player, TpNameDell);
            showTeleportPoint(player);
        } else if (command.startsWith("_bbsteleport;save;")) {
            String TpNameAdd;
            StringTokenizer stAdd = new StringTokenizer(command, " ");
            stAdd.nextToken();
            if (stAdd.hasMoreTokens())
                TpNameAdd = stAdd.nextToken();
            else
                TpNameAdd = "Default";
            int AddTpPrice = Config.CB_TELEPORT_SAVE_PRICE;
            addTeleportPoint(player, TpNameAdd, AddTpPrice);
            showTeleportPoint(player);
        } else if (command.startsWith("_bbsteleport;teleport;")) {
            StringTokenizer stGoTp = new StringTokenizer(command, " ");
            stGoTp.nextToken();
            int xTp = Integer.parseInt(stGoTp.nextToken());
            int yTp = Integer.parseInt(stGoTp.nextToken());
            int zTp = Integer.parseInt(stGoTp.nextToken());
            int priceTp;
            if (player.getLevel() > Config.CBB_TELEPORT_FREE_LEVEL)
                priceTp = Config.CB_TELEPORT_PRICE;
            else
                priceTp = 0;
            goToTeleportPoint(player, xTp, yTp, zTp, priceTp);
            showTeleportPoint(player);
        } else if (player.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br>", player);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
    }

    private void goToTeleportPoint(Player player, int xTp, int yTp, int zTp, int priceTp) {
        Location loc = player.getLoc();

        if (!checkFirstConditions(player) || !checkTeleportLocation(player, loc))
            return;

        if (priceTp > 0 && player.getAdena() < priceTp) {
            player.sendMessage(player.isLangRus() ? "Недостаточно денег." : "It is not enough money.");
            return;
        } else {
            if (priceTp > 0 || player.getNetConnection().getBonus() >= 1. && Config.ALT_BBS_TELEPORT_PRICE_PA)
                player.reduceAdena(priceTp, true);
            player.teleToLocation(xTp, yTp, zTp);
        }
    }

    private void showTeleportPoint(Player player) {
        CBteleport tp;
        Connection con = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM bbs_teleport WHERE charId=?;");
            st.setLong(1, player.getObjectId());
            ResultSet rset = st.executeQuery();
            TextBuilder html = new TextBuilder();
            html.append("<table width=220>");
            while (rset.next()) {
                tp = new CBteleport();
                tp.TpId = rset.getInt("TpId");
                tp.TpName = rset.getString("name");
                tp.PlayerId = rset.getInt("charId");
                tp.xC = rset.getInt("xPos");
                tp.yC = rset.getInt("yPos");
                tp.zC = rset.getInt("zPos");
                html.append("<tr>");
                html.append("<td>");
                html.append("<button value=\"" + tp.TpName + "\" action=\"bypass _bbsteleport;teleport; " + tp.xC + " " + tp.yC + " " + tp.zC + " " + 100000 + "\" width=200 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
                html.append("</td>");
                html.append("<td>");
                html.append("<button value=" + (player.isLangRus() ? "Удалить" : "Delete") + " action=\"bypass _bbsteleport;delete;" + tp.TpId + "\" width=80 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
                html.append("</td>)");
                html.append("</tr>");
            }
            html.append("</table>");

            DbUtils.closeQuietly(st, rset);

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/teleport/save.htm", player);
            content = content.replace("%tp%", html.toString());
            content = content.replace("<?tp_price?>", Integer.toString(Config.CB_TELEPORT_PRICE));
            content = content.replace("<?tp_max_count?>", Integer.toString(Config.CB_TELEPORT_MAX_COUNT));
            content = content.replace("<?tp_free_min_lvl?>", Integer.toString(Config.CBB_TELEPORT_FREE_LEVEL));

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;

        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con);
        }

    }

    private void deleteTeleportPoint(Player player, int TpNameDell) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM bbs_teleport WHERE charId=? AND TpId=?;");
            statement.setInt(1, player.getObjectId());
            statement.setInt(2, TpNameDell);
            statement.execute();
        } catch (Exception e) {
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    private void addTeleportPoint(Player player, String TpNameAdd, int AddTpPrice) {
        if (player.getNetConnection().getBonus() <= 1. && Config.ALT_BBS_TELEPORT_POINT_PA) {
            player.sendMessage(player.isLangRus() ? "Для доступа к этой функции преобретите премиум аккаунт" : "To access this feature preobretite premium account");
            return;
        }

        if (player.isDead() || player.isAlikeDead() || player.isCastingNow() || player.isAttackingNow()) {
            player.sendMessage(player.isLangRus() ? "Сохранить закладку в вашем состоянии невозможно." : "Bookmark in your state is impossible.");
            return;
        }

        if (player.isInCombat()) {
            player.sendMessage(player.isLangRus() ? "Сохранить закладку в боевом режиме нельзя." : "Bookmark not in combat mode.");
            return;
        }

        if (player.isInZone(Zone.ZoneType.battle_zone) || player.isInZone(Zone.ZoneType.no_escape) || player.isInZone(Zone.ZoneType.epic) || player.isInZone(Zone.ZoneType.SIEGE) || player.isInZone(Zone.ZoneType.RESIDENCE) || player.getVar("jailed") != null) {
            player.sendMessage(player.isLangRus() ? "Нельзя сохранить данную локацию." : "You can not save this location.");
            return;
        }

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT COUNT(*) FROM bbs_teleport WHERE charId=?;");
            statement.setLong(1, player.getObjectId());
            rset = statement.executeQuery();
            rset.next();
            if (rset.getInt(1) < Config.CB_TELEPORT_MAX_COUNT) {
                statement = con.prepareStatement("SELECT COUNT(*) FROM bbs_teleport WHERE charId=? AND name=?;");
                statement.setLong(1, player.getObjectId());
                statement.setString(2, TpNameAdd);
                ResultSet rset1 = statement.executeQuery();
                rset1.next();
                if (rset1.getInt(1) == 0) {
                    statement = con.prepareStatement("INSERT INTO bbs_teleport (charId, xPos, yPos, zPos, name) VALUES(?,?,?,?,?)");
                    statement.setInt(1, player.getObjectId());
                    statement.setInt(2, player.getX());
                    statement.setInt(3, player.getY());
                    statement.setInt(4, player.getZ());
                    statement.setString(5, TpNameAdd);
                    statement.execute();
                } else {
                    statement = con.prepareStatement("UPDATE bbs_teleport SET xPos=?, yPos=?, zPos=? WHERE charId=? AND name=?;");
                    statement.setInt(1, player.getObjectId());
                    statement.setInt(2, player.getX());
                    statement.setInt(3, player.getY());
                    statement.setInt(4, player.getZ());
                    statement.setString(5, TpNameAdd);
                    statement.execute();
                }
                if (AddTpPrice > 0 && player.getAdena() < AddTpPrice) {
                    player.sendMessage(player.isLangRus() ? "Недостаточно денег." : "It is not enough money.");
                    return;
                } else if (AddTpPrice > 0)
                    player.reduceAdena(AddTpPrice, true);
            } else
                player.sendMessage(player.isLangRus() ? "Вы не можете сохранить более " + Config.CB_TELEPORT_MAX_COUNT + " закладок." : "You can not store more than " + Config.CB_TELEPORT_MAX_COUNT + " tabs.");

        } catch (Exception e) {
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean checkFirstConditions(Player player) {
        if (player == null)
            return false;

        if (player.getActiveWeaponFlagAttachment() != null) {
            player.sendPacket(Msg.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
            return false;
        }
        if (player.isInOlympiadMode()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_IN_AN_OLYMPIAD_MATCH);
            return false;
        }
        if (player.getReflection() != ReflectionManager.DEFAULT) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_IN_AN_INSTANT_ZONE);
            return false;
        }
        if (player.isInDuel()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_DUEL);
            return false;
        }
        if (player.isInCombat() || player.getPvpFlag() != 0) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_BATTLE);
            return false;
        }
        if (player.isOnSiegeField() || player.isInZoneBattle()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_A_LARGE_SCALE_BATTLE_SUCH_AS_A_CASTLE_SIEGE);
            return false;
        }
        if (player.isFlying()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_FLYING);
            return false;
        }
        if (player.isInWater() || player.isInBoat()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_UNDERWATER);
            return false;
        }

        return true;
    }

    public static boolean checkTeleportConditions(Player player) {
        if (player == null)
            return false;

        if (player.isAlikeDead()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_DEAD);
            return false;
        }
        if (player.isInStoreMode() || player.isInTrade()) {
            player.sendPacket(Msg.YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_THE_PRIVATE_SHOPS);
            return false;
        }
        if (player.isInBoat() || player.isParalyzed() || player.isStunned() || player.isSleeping()) {
            player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_IN_A_FLINT_OR_PARALYZED_STATE);
            return false;
        }

        return true;
    }

    public static boolean checkTeleportLocation(Player player, Location loc) {
        return checkTeleportLocation(player, loc.x, loc.y, loc.z);
    }

    public static boolean checkTeleportLocation(Player player, int x, int y, int z) {
        if (player == null)
            return false;

        for (ZoneType zoneType : FORBIDDEN_ZONES) {
            Zone zone = player.getZone(zoneType);
            if (zone != null) {
                player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_TO_REACH_THIS_AREA);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}
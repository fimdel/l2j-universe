package services;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.database.mysql;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.base.SubClassType;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.SubUnit;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.ClanTable;
import l2p.gameserver.tables.SubClassTable;
import l2p.gameserver.utils.Log;
import l2p.gameserver.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Rename extends Functions {
    public void rename_page() {
        Player player = getSelf();
        if (player == null)
            return;

        String append = "!Rename";
        append += "<br>";
        append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.RenameFor", getSelf()).addString(Util.formatAdena(Config.SERVICES_CHANGE_NICK_PRICE)).addItemName(Config.SERVICES_CHANGE_NICK_ITEM) + "</font>";
        append += "<table>";
        append += "<tr><td>" + new CustomMessage("scripts.services.Rename.NewName", getSelf()) + " <edit var=\"new_name\" width=80></td></tr>";
        append += "<tr><td></td></tr>";
        append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.Rename.RenameButton", getSelf()) + "\" action=\"bypass -h scripts_services.Rename:rename $new_name\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
        append += "</table>";
        show(append, player);
    }

    public void changesex_page() {
        Player player = getSelf();
        if (player == null)
            return;

        if (!player.isInPeaceZone()) {
            show("You must be in peace zone to use this service.", player);
            return;
        }

        String append = "Sex changing";
        append += "<br>";
        append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.SexChange.SexChangeFor", player).addString(Util.formatAdena(Config.SERVICES_CHANGE_SEX_PRICE)).addItemName(Config.SERVICES_CHANGE_SEX_ITEM) + "</font>";
        append += "<table>";
        append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.SexChange.Button", player) + "\" action=\"bypass -h scripts_services.Rename:changesex\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
        append += "</table>";
        show(append, player);
    }

    public void separate_page() {
        Player player = getSelf();
        if (player == null)
            return;

        if (player.isHero()) {
            show("Not available for heroes.", player);
            return;
        }

        if (player.getSubClassList().size() == 1) {
            show("You must have at least 1 subclass.", player);
            return;
        }

        if (!player.isBaseClassActive()) {
            show("You must be at main class.", player);
            return;
        }

        if (player.getActiveSubClass().getLevel() < 75) {
            show("You must have at least 75 level.", player);
            return;
        }

        String append = "Subclass separation";
        append += "<br>";
        append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Separate.Price", player).addString(Util.formatAdena(Config.SERVICES_SEPARATE_SUB_PRICE)).addItemName(Config.SERVICES_SEPARATE_SUB_ITEM) + "</font>&nbsp;";
        append += "<edit var=\"name\" width=80 height=15 /><br>";
        append += "<table>";

        for (SubClass s : player.getSubClassList().values())
            if (!s.isBase() && s.getClassId() != ClassId.INSPECTOR.getId() && s.getClassId() != ClassId.JUDICATOR.getId())
                append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.Separate.Button", player).addString(ClassId.VALUES[s.getClassId()].toString()) + "\" action=\"bypass -h scripts_services.Rename:separate " + s.getClassId() + " $name\" width=200 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";

        append += "</table>";
        show(append, player);
    }

    public void separate(String[] param) {
        Player player = getSelf();
        if (player == null)
            return;

        if (player.isHero()) {
            show("Not available for heroes.", player);
            return;
        }

        if (player.getSubClassList().size() == 1) {
            show("You must have at least 1 subclass.", player);
            return;
        }

        if (!player.getActiveSubClass().isBase()) {
            show("You must be at main class.", player);
            return;
        }

        if (player.getActiveSubClass().getLevel() < 75) {
            show("You must have at least 75 level.", player);
            return;
        }

        if (param.length < 2) {
            show("You must specify target.", player);
            return;
        }

        if (getItemCount(player, Config.SERVICES_SEPARATE_SUB_ITEM) < Config.SERVICES_SEPARATE_SUB_PRICE) {
            if (Config.SERVICES_SEPARATE_SUB_ITEM == 57)
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            else
                player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return;
        }

        int classtomove = Integer.parseInt(param[0]);
        int newcharid = 0;
        for (Entry<Integer, String> e : player.getAccountChars().entrySet())
            if (e.getValue().equalsIgnoreCase(param[1]))
                newcharid = e.getKey();

        if (newcharid == 0) {
            show("Target not exists.", player);
            return;
        }

        if (mysql.simple_get_int("level", "character_subclasses", "char_obj_id=" + newcharid + " AND level > 1") > 1) {
            show("Target must have level 1.", player);
            return;
        }

        mysql.set("DELETE FROM character_subclasses WHERE char_obj_id=" + newcharid);
        mysql.set("DELETE FROM character_skills WHERE char_obj_id=" + newcharid);
        mysql.set("DELETE FROM character_skills_save WHERE char_obj_id=" + newcharid);
        mysql.set("DELETE FROM character_effects_save WHERE object_id=" + newcharid);
        mysql.set("DELETE FROM character_hennas WHERE char_obj_id=" + newcharid);
        mysql.set("DELETE FROM character_shortcuts WHERE char_obj_id=" + newcharid);
        mysql.set("DELETE FROM character_variables WHERE obj_id=" + newcharid);

        mysql.set("UPDATE character_subclasses SET char_obj_id=" + newcharid + ", isBase=1, certification=0 WHERE char_obj_id=" + player.getObjectId() + " AND class_id=" + classtomove);
        mysql.set("UPDATE character_skills SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
        mysql.set("UPDATE character_skills_save SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
        mysql.set("UPDATE character_effects_save SET object_id=" + newcharid + " WHERE object_id=" + player.getObjectId() + " AND id=" + classtomove);
        mysql.set("UPDATE character_hennas SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
        mysql.set("UPDATE character_shortcuts SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);

        mysql.set("UPDATE character_variables SET obj_id=" + newcharid + " WHERE obj_id=" + player.getObjectId() + " AND name like 'TransferSkills%'");

        player.modifySubClass(classtomove, 0);

        removeItem(player, Config.SERVICES_CHANGE_BASE_ITEM, Config.SERVICES_CHANGE_BASE_PRICE);
        player.logout();
        //Log.add("Character " + player + " base changed to " + target, "services");
    }

    public void changebase_page() {
        Player player = getSelf();
        if (player == null)
            return;

        if (!player.isInPeaceZone()) {
            show("You must be in peace zone to use this service.", player);
            return;
        }

        if (player.isHero()) {
            sendMessage("Not available for heroes.", player);
            return;
        }

        String append = "Base class changing";
        append += "<br>";
        append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.BaseChange.Price", player).addString(Util.formatAdena(Config.SERVICES_CHANGE_BASE_PRICE)).addItemName(Config.SERVICES_CHANGE_BASE_ITEM) + "</font>";
        append += "<table>";

        List<SubClass> possible = new ArrayList<SubClass>();
        if (player.getActiveSubClass().isBase()) {
            possible.addAll(player.getSubClassList().values());
            possible.remove(player.getSubClassList().getByClassId(player.getBaseClassId()));

            for (SubClass s : player.getSubClassList().values())
                for (SubClass s2 : player.getSubClassList().values())
                    if (s != s2 && !SubClassTable.areClassesComportable(ClassId.VALUES[s.getClassId()], ClassId.VALUES[s2.getClassId()]) || s2.getLevel() < 75)
                        possible.remove(s2);
        }

        if (possible.isEmpty())
            append += "<tr><td width=300>" + new CustomMessage("scripts.services.BaseChange.NotPossible", player) + "</td></tr>";
        else
            for (SubClass s : possible)
                append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.BaseChange.Button", player).addString(ClassId.VALUES[s.getClassId()].toString()) + "\" action=\"bypass -h scripts_services.Rename:changebase " + s.getClassId() + "\" width=200 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
        append += "</table>";
        show(append, player);
    }

    public void changebase(String[] param) {
        Player player = getSelf();
        if (player == null)
            return;

        if (!player.isInPeaceZone()) {
            show("You must be in peace zone to use this service.", player);
            return;
        }

        if (!player.getActiveSubClass().isBase()) {
            show("You must be on your base class to use this service.", player);
            return;
        }

        if (player.isHero()) {
            show("Not available for heroes.", player);
            return;
        }

        if (getItemCount(player, Config.SERVICES_CHANGE_BASE_ITEM) < Config.SERVICES_CHANGE_BASE_PRICE) {
            if (Config.SERVICES_CHANGE_BASE_ITEM == 57)
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            else
                player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return;
        }

        int target = Integer.parseInt(param[0]);
        SubClass newBase = player.getSubClassList().getByClassId(target);

        player.getActiveSubClass().setType(SubClassType.SUBCLASS);
        player.getActiveSubClass().setCertification(newBase.getCertification());

        newBase.setCertification(0);
        player.getActiveSubClass().setExp(player.getExp());
        player.checkSkills();

        newBase.setType(SubClassType.BASE_CLASS);

        player.setHairColor(0);
        player.setHairStyle(0);
        player.setFace(0);
        Olympiad.unRegisterNoble(player);
        removeItem(player, Config.SERVICES_CHANGE_BASE_ITEM, Config.SERVICES_CHANGE_BASE_PRICE);
        player.logout();
        //Log.add("Character " + player + " base changed to " + target, "services");
    }

    public void rename(String[] args) {
        Player player = getSelf();
        if (player == null)
            return;

        if (player.isHero()) {
            sendMessage("Not available for heroes.", player);
            return;
        }

        if (args.length != 1) {
            show(new CustomMessage("scripts.services.Rename.incorrectinput", player), player);
            return;
        }

        if (player.getEvent(SiegeEvent.class) != null) {
            show(new CustomMessage("scripts.services.Rename.SiegeNow", player), player);
            return;
        }

        String name = args[0];
        if (!Util.isMatchingRegexp(name, Config.CNAME_TEMPLATE)) {
            show(new CustomMessage("scripts.services.Rename.incorrectinput", player), player);
            return;
        }

        if (getItemCount(player, Config.SERVICES_CHANGE_NICK_ITEM) < Config.SERVICES_CHANGE_NICK_PRICE) {
            if (Config.SERVICES_CHANGE_NICK_ITEM == 57)
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            else
                player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return;
        }

        if (CharacterDAO.getInstance().getObjectIdByName(name) > 0) {
            show(new CustomMessage("scripts.services.Rename.Thisnamealreadyexists", player), player);
            return;
        }

        removeItem(player, Config.SERVICES_CHANGE_NICK_ITEM, Config.SERVICES_CHANGE_NICK_PRICE);

        String oldName = player.getName();
        player.reName(name, true);
        Log.add("Character " + oldName + " renamed to " + name, "renames");
        show(new CustomMessage("scripts.services.Rename.changedname", player).addString(oldName).addString(name), player);
    }

    public void changesex() {
        Player player = getSelf();
        if (player == null)
            return;

        if (player.getRace() == Race.kamael) {
            show("Not available for Kamael.", player);
            return;
        }

        if (!player.isInPeaceZone()) {
            show("You must be in peace zone to use this service.", player);
            return;
        }

        if (getItemCount(player, Config.SERVICES_CHANGE_SEX_ITEM) < Config.SERVICES_CHANGE_SEX_PRICE) {
            if (Config.SERVICES_CHANGE_SEX_ITEM == 57)
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            else
                player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return;
        }

        Connection con = null;
        PreparedStatement offline = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            offline = con.prepareStatement("UPDATE characters SET sex = ? WHERE obj_Id = ?");
            offline.setInt(1, player.getSex() == 1 ? 0 : 1);
            offline.setInt(2, player.getObjectId());
            offline.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            show(new CustomMessage("common.Error", player), player);
            return;
        } finally {
            DbUtils.closeQuietly(con, offline);
        }

        player.setHairColor(0);
        player.setHairStyle(0);
        player.setFace(0);
        removeItem(player, Config.SERVICES_CHANGE_SEX_ITEM, Config.SERVICES_CHANGE_SEX_PRICE);
        player.logout();
        Log.add("Character " + player + " sex changed to " + (player.getSex() == 1 ? "male" : "female"), "renames");
    }

    public void rename_clan_page() {
        Player player = getSelf();
        if (player == null)
            return;

        if (player.getClan() == null || !player.isClanLeader()) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
            return;
        }

        String append = "!Rename clan";
        append += "<br>";
        append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.RenameFor", getSelf()).addString(Util.formatAdena(Config.SERVICES_CHANGE_CLAN_NAME_PRICE)).addItemName(Config.SERVICES_CHANGE_CLAN_NAME_ITEM) + "</font>";
        append += "<table>";
        append += "<tr><td>" + new CustomMessage("scripts.services.Rename.NewName", getSelf()) + ": <edit var=\"new_name\" width=80></td></tr>";
        append += "<tr><td></td></tr>";
        append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.Rename.RenameButton", getSelf()) + "\" action=\"bypass -h scripts_services.Rename:rename_clan $new_name\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
        append += "</table>";
        show(append, player);
    }

    public void rename_clan(String[] param) {
        Player player = getSelf();
        if (player == null || param == null || param.length == 0)
            return;

        if (player.getClan() == null || !player.isClanLeader()) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
            return;
        }

        if (player.getEvent(SiegeEvent.class) != null) {
            show(new CustomMessage("scripts.services.Rename.SiegeNow", player), player);
            return;
        }

        if (!Util.isMatchingRegexp(param[0], Config.CLAN_NAME_TEMPLATE)) {
            player.sendPacket(SystemMsg.CLAN_NAME_IS_INVALID);
            return;
        }
        if (ClanTable.getInstance().getClanByName(param[0]) != null) {
            player.sendPacket(SystemMsg.THIS_NAME_ALREADY_EXISTS);
            return;
        }

        if (getItemCount(player, Config.SERVICES_CHANGE_CLAN_NAME_ITEM) < Config.SERVICES_CHANGE_CLAN_NAME_PRICE) {
            if (Config.SERVICES_CHANGE_CLAN_NAME_ITEM == 57)
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            else
                player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return;
        }

        show(new CustomMessage("scripts.services.Rename.changedname", player).addString(player.getClan().getName()).addString(param[0]), player);
        SubUnit sub = player.getClan().getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
        sub.setName(param[0], true);

        removeItem(player, Config.SERVICES_CHANGE_CLAN_NAME_ITEM, Config.SERVICES_CHANGE_CLAN_NAME_PRICE);
        player.getClan().broadcastClanStatus(true, true, false);
    }
}